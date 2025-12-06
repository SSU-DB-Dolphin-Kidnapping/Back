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

        // 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());

        log.info("=== 멀티스레드 시뮬레이션 시작 ===");

        for (SimulationTask task : tasks) {
            executorService.submit(() -> {
                try {
                    // 모든 학생이 동시에 시작하기 위해 대기
                    startSignal.await();

                    // 학생의 반응 시간만큼 대기
                    waitForReactionTime(task.student);

                    // 각 학생별로 독립적인 트랜잭션으로 처리 (프록시를 통해 REQUIRES_NEW 작동)
                    StudentRegistrationService.StudentRegistrationResult result =
                            studentRegistrationService.processStudentRegistration(task, test);

                    totalSuccess.addAndGet(result.successCount);
                    totalFail.addAndGet(result.failCount);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("학생 {} 처리 중 인터럽트 발생", task.student.getStudentName(), e);
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
            Thread.sleep((long) (student.getAvgReactionTime() * 1000));
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
