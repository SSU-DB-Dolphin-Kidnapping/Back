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
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final HistoryRepository historyRepository;
    private final TeachRepository teachRepository;
    private final StudentRepository studentRepository;
    private final BucketRepository bucketRepository;

    @Override
    @Transactional
    public TestResponseDTO.SimulationResultDTO runSimulation() {
        log.info("=== 수강신청 시뮬레이션 시작 ===");

        Test test = new Test();
        Test savedTest = testRepository.save(test);

        List<Teach> teaches = teachRepository.findAll();
        teaches.forEach(Teach::resetCounts);
        teachRepository.saveAll(teaches);

        List<Student> students = studentRepository.findAll();
        List<SimulationTask> tasks = new ArrayList<>();

        for (Student student : students) {
            if (student.getBestBucket() != null) {
                Bucket bucket = bucketRepository.findById(student.getBestBucket())
                        .orElse(null);
                if (bucket != null) {
                    tasks.add(new SimulationTask(student, bucket));
                }
            }
        }

        tasks.sort(Comparator.comparing(task -> task.student.getAvgReactionTime()));

        int totalSuccess = 0;
        int totalFail = 0;

        for (SimulationTask task : tasks) {
            Student student = task.student;
            Bucket bucket = task.bucket;

            log.info("학생 {} ({}초) - 장바구니: {}", student.getStudentName(), student.getAvgReactionTime(), bucket.getName());

            // avgReactionTime만큼 대기 (밀리초로 변환)
            try {
                Thread.sleep((long) (student.getAvgReactionTime() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("대기 중 인터럽트 발생", e);
            }

            // 장바구니의 모든 항목에 대해 수강신청 시도
            for (BucketElement element : bucket.getBucketElements()) {
                BucketElement current = element;
                boolean enrolled = false;
                String failReason = null;

                // 대체 과목 체인 따라가기
                while (current != null && !enrolled) {
                    Teach teach = current.getTeach();

                    if (teach.tryEnroll()) {
                        enrolled = true;
                        totalSuccess++;
                        log.info("  ✓ {} - 성공", teach.getCourse().getName());
                    } else {
                        failReason = "정원 초과";
                        log.info("  ✗ {} - 실패 (정원 초과)", teach.getCourse().getName());
                        current = current.getSubElement();

                        if (current != null) {
                            log.info("    → 대체 과목 시도: {}",
                                    current.getTeach().getCourse().getName());
                        }
                    }
                }

                History history = History.create(element, savedTest, enrolled, enrolled ? null : failReason);
                historyRepository.save(history);

                if (!enrolled) {
                    totalFail++;
                }
            }
        }

        log.info("=== 시뮬레이션 완료 ===");
        log.info("총 성공: {} 건, 실패: {} 건", totalSuccess, totalFail);

        return TestConverter.toSimulationResultDTO(savedTest.getId(), tasks.size(), totalSuccess, totalFail);
    }

    // 내부 클래스: 시뮬레이션 작업
    private static class SimulationTask {
        Student student;
        Bucket bucket;

        SimulationTask(Student student, Bucket bucket) {
            this.student = student;
            this.bucket = bucket;
        }
    }
}
