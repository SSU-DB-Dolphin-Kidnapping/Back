package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.entity.TeachInfo;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import Dolphin.ShoppingCart.domain.test.entity.History;
import Dolphin.ShoppingCart.domain.test.entity.Test;
import Dolphin.ShoppingCart.domain.test.repository.HistoryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentRegistrationService {

    private final TeachRepository teachRepository;
    private final HistoryRepository historyRepository;
    private final EntityManager entityManager;

    // 각 학생의 수강신청을 독립적인 트랜잭션으로 처리 (실제 수강신청과 동일)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public StudentRegistrationResult processStudentRegistration(
            TestCommandServiceImpl.SimulationTask task, Test test) {

        int successCount = 0;
        int failCount = 0;

        // 학생별 등록된 과목 리스트
        List<Teach> enrolledTeaches = new ArrayList<>();

        for (BucketElement element : task.bucket.getBucketElements()) {
            RegistrationResult result = tryRegisterCourse(element, enrolledTeaches);

            // History 즉시 저장 (실제 수강신청과 동일)
            BucketElement managedElement = entityManager.getReference(BucketElement.class, element.getId());
            History history = History.create(
                    managedElement,
                    test,
                    result.enrolled,
                    result.failReason
            );
            historyRepository.save(history);

            if (result.enrolled) {
                successCount++;
                enrolledTeaches.add(result.enrolledTeach);
            } else {
                failCount++;
            }
        }

        return new StudentRegistrationResult(successCount, failCount);
    }

    private RegistrationResult tryRegisterCourse(BucketElement element, List<Teach> enrolledTeaches) {
        BucketElement current = element;
        String lastFailReason = null;

        while (current != null) {
            // 비관적 락으로 Teach 조회 (실제 수강신청과 동일)
            Teach teach = teachRepository.findByIdWithLock(current.getTeach().getId())
                    .orElseThrow(() -> new IllegalStateException("Teach not found"));

            // course_id 중복 체크
            if (hasCourseIdConflict(teach, enrolledTeaches)) {
                log.info("  ✗ {} - 실패 (동일 과목 중복 신청)", teach.getCourse().getName());
                lastFailReason = "이미 동일한 과목을 신청했습니다";
                current = current.getSubElement();

                if (current != null) {
                    log.info("    → 대체 과목 시도: {}", current.getTeach().getCourse().getName());
                }
                continue;
            }

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

            // 정원 체크 및 등록
            if (teach.tryEnroll()) {
                log.info("  ✓ {} - 성공", teach.getCourse().getName());
                teachRepository.save(teach);  // 변경사항 즉시 저장
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

    private boolean hasCourseIdConflict(Teach newTeach, List<Teach> enrolledTeaches) {
        Long newCourseId = newTeach.getCourse().getId();

        for (Teach enrolledTeach : enrolledTeaches) {
            if (enrolledTeach.getCourse().getId().equals(newCourseId)) {
                return true;
            }
        }

        return false;
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

    // 내부 클래스
    public static class RegistrationResult {
        boolean enrolled;
        String failReason;
        Teach enrolledTeach;

        RegistrationResult(boolean enrolled, String failReason, Teach enrolledTeach) {
            this.enrolled = enrolled;
            this.failReason = failReason;
            this.enrolledTeach = enrolledTeach;
        }
    }

    public static class StudentRegistrationResult {
        public int successCount;
        public int failCount;

        public StudentRegistrationResult(int successCount, int failCount) {
            this.successCount = successCount;
            this.failCount = failCount;
        }
    }
}
