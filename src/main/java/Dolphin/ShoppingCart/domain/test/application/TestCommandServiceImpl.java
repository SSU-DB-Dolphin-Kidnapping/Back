package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.entity.TeachInfo;
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

            // 각 학생별로 등록된 과목의 시간대 추적
            List<Teach> enrolledTeaches = new ArrayList<>();

            for (BucketElement element : task.bucket.getBucketElements()) {
                RegistrationResult registrationResult = tryRegisterCourse(element, enrolledTeaches);

                saveHistory(element, test, registrationResult);

                if (registrationResult.enrolled) {
                    totalSuccess++;
                    // 성공한 경우 등록된 과목 추가
                    enrolledTeaches.add(registrationResult.enrolledTeach);
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

    private RegistrationResult tryRegisterCourse(BucketElement element, List<Teach> enrolledTeaches) {
        BucketElement current = element;
        String lastFailReason = null;

        while (current != null) {
            Teach teach = current.getTeach();

            // 시간대 충돌 체크
            if (hasTimeConflict(teach, enrolledTeaches)) {
                log.info("  ✗ {} - 실패 (시간대 충돌)", teach.getCourse().getName());
                lastFailReason = "신청하려는 시간대에 이미 과목이 있습니다";
                current = current.getSubElement();

                if (current != null) {
                    log.info("    → 대체 과목 시도: {}", current.getTeach().getCourse().getName());
                }
                continue;
            }

            // 정원 체크
            if (teach.tryEnroll()) {
                log.info("  ✓ {} - 성공", teach.getCourse().getName());
                return new RegistrationResult(true, null, teach);
            }

            log.info("  ✗ {} - 실패 (정원 초과)", teach.getCourse().getName());
            lastFailReason = "정원 초과";
            current = current.getSubElement();

            if (current != null) {
                log.info("    → 대체 과목 시도: {}", current.getTeach().getCourse().getName());
            }
        }

        // 마지막으로 시도한 과목의 실패 사유 반환
        return new RegistrationResult(false, lastFailReason != null ? lastFailReason : "정원 초과", null);
    }

    private boolean hasTimeConflict(Teach newTeach, List<Teach> enrolledTeaches) {
        List<TeachInfo> newTeachInfos = newTeach.getTeachInfos();

        for (Teach enrolledTeach : enrolledTeaches) {
            List<TeachInfo> enrolledTeachInfos = enrolledTeach.getTeachInfos();

            for (TeachInfo newInfo : newTeachInfos) {
                for (TeachInfo enrolledInfo : enrolledTeachInfos) {
                    // 같은 요일인지 확인
                    if (newInfo.getDayOfTheWeek().equals(enrolledInfo.getDayOfTheWeek())) {
                        // 시간이 겹치는지 확인
                        if (isTimeOverlap(newInfo.getStartTime(), newInfo.getEndTime(),
                                enrolledInfo.getStartTime(), enrolledInfo.getEndTime())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isTimeOverlap(java.time.LocalTime start1, java.time.LocalTime end1,
                                   java.time.LocalTime start2, java.time.LocalTime end2) {
        // 두 시간대가 겹치는지 확인: start1 < end2 && start2 < end1
        return start1.isBefore(end2) && start2.isBefore(end1);
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
        Teach enrolledTeach;

        RegistrationResult(boolean enrolled, String failReason, Teach enrolledTeach) {
            this.enrolled = enrolled;
            this.failReason = failReason;
            this.enrolledTeach = enrolledTeach;
        }
    }
}
