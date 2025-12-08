package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.repository.BucketRepository;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.domain.test.converter.TestConverter;
import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;
import Dolphin.ShoppingCart.domain.test.entity.Test;
import Dolphin.ShoppingCart.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCommandServiceImpl implements TestCommandService {

    private final TestRepository testRepository;
    private final TeachRepository teachRepository;
    private final StudentRepository studentRepository;
    private final BucketRepository bucketRepository;
    private final StudentRegistrationService studentRegistrationService;

    @Override
    public TestResponseDTO.SimulationResultDTO runSimulation() {
        log.info("=== 수강신청 시뮬레이션 시작 ===");

        Test savedTest = createTest();
        resetTeachCounts();
        List<SimulationTask> tasks = prepareSimulationTasks();

        SimulationResult result = executeSimulation(savedTest, tasks);

        log.info("=== 시뮬레이션 완료 ===");
        log.info("총 성공: {} 건, 실패: {} 건", result.totalSuccess, result.totalFail);

        return TestConverter.toSimulationResultDTO(
                savedTest.getId(),
                tasks.size(),
                result.totalSuccess,
                result.totalFail
        );
    }

    @Transactional
    protected Test createTest() {
        return testRepository.save(new Test());
    }

    @Transactional
    protected void resetTeachCounts() {
        List<Teach> teaches = teachRepository.findAllWithTeachInfos();
        teaches.forEach(Teach::resetCounts);
        teachRepository.saveAll(teaches);
        log.info("Teach 카운트 초기화 완료: {} 개", teaches.size());
    }

    private List<SimulationTask> prepareSimulationTasks() {
        List<Student> students = studentRepository.findAll();
        List<SimulationTask> tasks = new ArrayList<>();

        for (Student student : students) {
            if (student.getBestBucket() != null) {
                bucketRepository.findByIdWithElements(student.getBestBucket())
                        .ifPresent(bucket -> tasks.add(new SimulationTask(student, bucket)));
            }
        }

        tasks.sort(Comparator.comparing(
                task -> task.student.getAvgReactionTime(),
                Comparator.nullsLast(Double::compareTo)
        ));
        log.info("시뮬레이션 대상 학생: {} 명", tasks.size());

        return tasks;
    }

    private SimulationResult executeSimulation(Test test, List<SimulationTask> tasks) {
        AtomicInteger totalSuccess = new AtomicInteger(0);
        AtomicInteger totalFail = new AtomicInteger(0);

        // 모든 학생이 동시에 시작하도록 CountDownLatch 사용
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(tasks.size());

        // 스레드 풀 생성 (실제 수강신청처럼 모든 학생이 동시에 경쟁)
        ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
        log.info("시뮬레이션 학생 수: {} (모두 동시 실행)", tasks.size());

        log.info("=== 멀티스레드 시뮬레이션 시작 ===");

        for (SimulationTask task : tasks) {
            executorService.submit(() -> {
                try {
                    // 모든 학생이 동시에 시작하기 위해 대기
                    startSignal.await();

                    // 학생의 반응 시간만큼 대기
                    waitForReactionTime(task.student);

                    // 락 타임아웃 시 재시도 (최대 3번)
                    StudentRegistrationService.StudentRegistrationResult result = null;
                    int retryCount = 0;
                    int maxRetries = 3;

                    while (retryCount < maxRetries) {
                        try {
                            result = studentRegistrationService.processStudentRegistration(task, test);
                            break;  // 성공하면 루프 탈출
                        } catch (Exception e) {
                            if (e.getMessage() != null && e.getMessage().contains("Lock wait timeout")) {
                                retryCount++;
                                if (retryCount < maxRetries) {
                                    log.warn("학생 {} 락 타임아웃, 재시도 {}/{}",
                                            task.student.getStudentName(), retryCount, maxRetries);
                                    Thread.sleep(100);  // 100ms 대기 후 재시도
                                } else {
                                    log.error("학생 {} 락 타임아웃 최대 재시도 초과", task.student.getStudentName());
                                    result = new StudentRegistrationService.StudentRegistrationResult(0, task.bucket.getBucketElements().size());
                                }
                            } else {
                                throw e;  // 다른 예외는 그대로 throw
                            }
                        }
                    }

                    if (result != null) {
                        totalSuccess.addAndGet(result.successCount);
                        totalFail.addAndGet(result.failCount);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("학생 {} 처리 중 인터럽트 발생", task.student.getStudentName(), e);
                } catch (Exception e) {
                    log.error("학생 {} 처리 중 예외 발생", task.student.getStudentName(), e);
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        // 모든 학생 동시 시작!
        log.info("모든 학생 동시 수강신청 시작!");
        startSignal.countDown();

        try {
            // 모든 학생의 수강신청이 완료될 때까지 대기
            doneSignal.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("시뮬레이션 대기 중 인터럽트 발생", e);
        } finally {
            executorService.shutdown();
        }

        return new SimulationResult(totalSuccess.get(), totalFail.get());
    }

    private void waitForReactionTime(Student student) {
        log.info("학생 {} ({}초) 처리 중", student.getStudentName(), student.getAvgReactionTime());

        try {
            Thread.sleep((long) (student.getAvgReactionTime() * 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("대기 중 인터럽트 발생", e);
        }
    }

    // 내부 클래스
    public static class SimulationTask {
        public Student student;
        public Bucket bucket;

        SimulationTask(Student student, Bucket bucket) {
            this.student = student;
            this.bucket = bucket;
        }
    }

    private static class SimulationResult {
        int totalSuccess;
        int totalFail;

        SimulationResult(int totalSuccess, int totalFail) {
            this.totalSuccess = totalSuccess;
            this.totalFail = totalFail;
        }
    }
}
