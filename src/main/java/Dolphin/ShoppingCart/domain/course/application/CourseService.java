package Dolphin.ShoppingCart.domain.course.application;

import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureInfoDTO;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureListDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    // 1) 강의 리스트 조회 (커서 페이징 + 검색 포함)
    LectureListDTO getLectures(
            Long cursorId,
            Integer size,
            String name,
            String professor,
            Integer grade
    );

    // 2) 강의 검색 (별도)
    List<LectureInfoDTO> searchLectures(
            String name,
            String professorName,
            Integer grade,
            Pageable pageable
    );
}
