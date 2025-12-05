package Dolphin.ShoppingCart.domain.course.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

public class CourseResponseDTO {

    @Getter
    @Builder
    public static class LectureInfoDTO {
        private Long teachId;
        private String courseName;
        private String professorName;
        private Float credit;
        private Integer year;
        private String semester; // 문자열
        private String className;
        private Integer targetGrade;
        private String type; // 문자열
        private List<ScheduleDTO> schedules;
    }

    @Getter
    @Builder
    public static class ScheduleDTO {
        private String day; // 월/화/수.. 문자열
        private LocalTime startTime;
        private LocalTime endTime;
        private String classroom;
    }

    @Getter
    @Builder
    public static class LectureListDTO {
        private List<LectureInfoDTO> lectures;
        private Long nextCursor;
        private boolean hasNext;
    }
}
