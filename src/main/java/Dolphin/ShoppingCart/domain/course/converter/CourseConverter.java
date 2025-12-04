package Dolphin.ShoppingCart.domain.course.converter;


import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureInfoDTO;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureListDTO;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.ScheduleDTO;
import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.entity.TeachInfo;

import java.util.stream.Collectors;

public class CourseConverter {

    public static LectureInfoDTO toLectureInfoDTO(Teach teach) {
        return LectureInfoDTO.builder()
                .teachId(teach.getId())
                .courseName(teach.getCourse().getName())
                .professorName(teach.getProfessor().getProfessorName())
                .credit(teach.getCourse().getCredit())
                .year(teach.getYear())
                .semester(teach.getSemester().getDescription())   // 문자열
                .className(teach.getClassName())
                .targetGrade(teach.getTargetGrade())
                .type(teach.getType().getDescription())            // 문자열
                .schedules(
                        teach.getTeachInfos().stream()
                                .map(CourseConverter::toScheduleDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    // 시간표 변환
    public static ScheduleDTO toScheduleDTO(TeachInfo info) {
        return ScheduleDTO.builder()
                .day(info.getDayOfTheWeek().getDescription()) // "월요일", "화요일"
                .startTime(info.getStartTime())
                .endTime(info.getEndTime())
                .classroom(info.getClassroom())
                .build();
    }

    // 강의 리스트 변환
    public static LectureListDTO toLectureListDTO(
            java.util.List<LectureInfoDTO> lectures,
            Long nextCursor
    ) {
        boolean hasNext = nextCursor != null;

        return LectureListDTO.builder()
                .lectures(lectures)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
