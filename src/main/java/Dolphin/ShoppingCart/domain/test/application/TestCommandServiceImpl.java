package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.repository.BucketRepository;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.domain.test.converter.TestConverter;
import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;
import Dolphin.ShoppingCart.domain.test.entity.History;
import Dolphin.ShoppingCart.domain.test.entity.Test;
import Dolphin.ShoppingCart.domain.test.repository.HistoryRepository;
import Dolphin.ShoppingCart.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCommandServiceImpl implements TestCommandService {

    private final TestRepository testRepository;
    private final HistoryRepository historyRepository;
    private final TeachRepository teachRepository;
    private final StudentRepository studentRepository;
    private final BucketRepository bucketRepository;

    @Override
    @Transactional
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

    private Test createTest() {
        return testRepository.save(new Test());
    }

    private void resetTeachCounts() {
        List<Teach> teaches = teachRepository.findAll();
        teaches.forEach(Teach::resetCounts);
        teachRepository.saveAll(teaches);
    }

    private List<SimulationTask> prepareSimulationTasks() {
        List<Student> students = studentRepository.findAll();
        List<SimulationTask> tasks = new ArrayList<>();

        for (Student student : students) {
            if (student.getBestBucket() != null) {
                bucketRepository.findById(student.getBestBucket())
                        .ifPresent(bucket -> tasks.add(new SimulationTask(student, bucket)));
            }
        }

        tasks.sort(Comparator.comparing(task -> task.student.getAvgReactionTime()));
        log.info("시뮬레이션 대상 학생: {} 명", tasks.size());

        return tasks;
    }

    private SimulationResult executeSimulation(Test test, List<SimulationTask> tasks) {
        int totalSuccess = 0;
        int totalFail = 0;

        for (SimulationTask task : tasks) {
            waitForReactionTime(task.student);

            for (BucketElement element : task.bucket.getBucketElements()) {
                RegistrationResult registrationResult = tryRegisterCourse(element);

                saveHistory(element, test, registrationResult);

                if (registrationResult.enrolled) {
                    totalSuccess++;
                } else {
                    totalFail++;
                }
            }
        }

        return new SimulationResult(totalSuccess, totalFail);
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

    private RegistrationResult tryRegisterCourse(BucketElement element) {
        BucketElement current = element;

        while (current != null) {
            Teach teach = current.getTeach();

            if (teach.tryEnroll()) {
                log.info("  ✓ {} - 성공", teach.getCourse().getName());
                return new RegistrationResult(true, null);
            }

            log.info("  ✗ {} - 실패 (정원 초과)", teach.getCourse().getName());
            current = current.getSubElement();

            if (current != null) {
                log.info("    → 대체 과목 시도: {}", current.getTeach().getCourse().getName());
            }
        }

        return new RegistrationResult(false, "정원 초과");
    }

    private void saveHistory(BucketElement element, Test test, RegistrationResult result) {
        History history = History.create(
                element,
                test,
                result.enrolled,
                result.enrolled ? null : result.failReason
        );
        historyRepository.save(history);
    }

    // 내부 클래스
    private static class SimulationTask {
        Student student;
        Bucket bucket;

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

    private static class RegistrationResult {
        boolean enrolled;
        String failReason;

        RegistrationResult(boolean enrolled, String failReason) {
            this.enrolled = enrolled;
            this.failReason = failReason;
        }
    }
}
