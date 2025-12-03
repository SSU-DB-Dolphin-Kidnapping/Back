package Dolphin.ShoppingCart.domain.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TestResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimulationResultDTO {
        private Long testId;
        private Integer totalStudents;
        private Integer totalSuccess;
        private Integer totalFail;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestSummaryDTO {
        private Long testId;
        private LocalDateTime testDate;
        private Integer totalCourses;
        private Integer successCount;
        private Integer failCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDetailDTO {
        private Long testId;
        private LocalDateTime testDate;
        private String studentName;
        private String bucketName;
        private Integer totalCourses;
        private Integer successCount;
        private Integer failCount;
        private List<CourseResultDTO> courses;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseResultDTO {
        private String courseName;
        private String className;
        private String professorName;
        private Boolean isSuccess;
        private String failedReason;
        private Integer priority;
    }
}
