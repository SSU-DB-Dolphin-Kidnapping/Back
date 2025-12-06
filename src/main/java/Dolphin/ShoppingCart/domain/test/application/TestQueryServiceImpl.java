package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.repository.BucketRepository;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.domain.test.converter.TestConverter;
import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;
import Dolphin.ShoppingCart.domain.test.entity.History;
import Dolphin.ShoppingCart.domain.test.entity.Test;
import Dolphin.ShoppingCart.domain.test.exception.TestErrorCode;
import Dolphin.ShoppingCart.domain.test.exception.TestException;
import Dolphin.ShoppingCart.domain.test.repository.HistoryRepository;
import Dolphin.ShoppingCart.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestQueryServiceImpl implements TestQueryService {

    private final TestRepository testRepository;
    private final HistoryRepository historyRepository;
    private final StudentRepository studentRepository;
    private final BucketRepository bucketRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TestResponseDTO.TestSummaryDTO> getStudentTestResults(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new TestException(TestErrorCode.STUDENT_NOT_FOUND);
        }

        List<History> histories = historyRepository.findAllByStudentId(studentId);

        Map<Test, List<History>> historyByTest = histories.stream()
                .collect(Collectors.groupingBy(History::getTest));

        return TestConverter.toTestSummaryListDTO(historyByTest);
    }

    @Override
    @Transactional(readOnly = true)
    public TestResponseDTO.TestDetailDTO getStudentTestDetail(Long studentId, Long testId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new TestException(TestErrorCode.STUDENT_NOT_FOUND));

        if (student.getBestBucket() == null) {
            throw new TestException(TestErrorCode.BEST_BUCKET_NOT_SET);
        }

        Bucket bucket = bucketRepository.findByIdWithElements(student.getBestBucket())
                .orElseThrow(() -> new TestException(TestErrorCode.BUCKET_NOT_FOUND));

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new TestException(TestErrorCode.TEST_NOT_FOUND));

        List<History> histories = historyRepository.findByStudentIdAndTestId(studentId, testId);

        if (histories.isEmpty()) {
            throw new TestException(TestErrorCode.TEST_RESULT_NOT_FOUND);
        }

        histories.sort(Comparator.comparing(h -> h.getBucketElement().getPriority()));

        return TestConverter.toTestDetailDTO(test, student, bucket, histories);
    }
}
