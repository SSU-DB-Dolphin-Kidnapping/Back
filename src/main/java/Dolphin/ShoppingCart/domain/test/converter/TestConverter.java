package Dolphin.ShoppingCart.domain.test.converter;

import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;
import Dolphin.ShoppingCart.domain.test.entity.History;
import Dolphin.ShoppingCart.domain.test.entity.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestConverter {

    public static TestResponseDTO.SimulationResultDTO toSimulationResultDTO(Long testId, Integer totalStudents, Integer totalSuccess, Integer totalFail) {
        return TestResponseDTO.SimulationResultDTO.builder()
                .testId(testId)
                .totalStudents(totalStudents)
                .totalSuccess(totalSuccess)
                .totalFail(totalFail)
                .message("시뮬레이션이 완료되었습니다.")
                .build();
    }

    public static TestResponseDTO.TestSummaryDTO toTestSummaryDTO(Test test, List<History> histories) {
        long successCount = histories.stream()
                .filter(History::getIsSuccess)
                .count();
        long failCount = histories.stream()
                .filter(h -> !h.getIsSuccess())
                .count();

        return TestResponseDTO.TestSummaryDTO.builder()
                .testId(test.getId())
                .testDate(test.getCreatedAt())
                .totalCourses(histories.size())
                .successCount((int) successCount)
                .failCount((int) failCount)
                .build();
    }

    public static List<TestResponseDTO.TestSummaryDTO> toTestSummaryListDTO(Map<Test, List<History>> historyByTest) {
        return historyByTest.entrySet().stream()
                .map(entry -> toTestSummaryDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(TestResponseDTO.TestSummaryDTO::getTestDate).reversed())
                .collect(Collectors.toList());
    }

    public static TestResponseDTO.TestDetailDTO toTestDetailDTO(
            Test test,
            Student student,
            Bucket bucket,
            List<History> histories) {

        List<TestResponseDTO.CourseResultDTO> courseResults = histories.stream()
                .map(TestConverter::toCourseResultDTO)
                .collect(Collectors.toList());

        long successCount = histories.stream()
                .filter(History::getIsSuccess)
                .count();
        long failCount = histories.stream()
                .filter(h -> !h.getIsSuccess())
                .count();

        return TestResponseDTO.TestDetailDTO.builder()
                .testId(test.getId())
                .testDate(test.getCreatedAt())
                .studentName(student.getStudentName())
                .bucketName(bucket.getName())
                .totalCourses(histories.size())
                .successCount((int) successCount)
                .failCount((int) failCount)
                .courses(courseResults)
                .build();
    }

    private static TestResponseDTO.CourseResultDTO toCourseResultDTO(History history) {
        return TestResponseDTO.CourseResultDTO.builder()
                .courseName(history.getBucketElement().getTeach().getCourse().getName())
                .className(history.getBucketElement().getTeach().getClassName())
                .professorName(history.getBucketElement().getTeach().getProfessor().getProfessorName())
                .isSuccess(history.getIsSuccess())
                .failedReason(history.getFailedReason())
                .priority(history.getBucketElement().getPriority())
                .build();
    }
}
