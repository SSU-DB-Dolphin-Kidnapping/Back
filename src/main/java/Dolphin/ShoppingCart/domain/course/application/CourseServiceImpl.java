package Dolphin.ShoppingCart.domain.course.application;

import Dolphin.ShoppingCart.domain.course.converter.CourseConverter;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureInfoDTO;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureListDTO;
import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final TeachRepository teachRepository;

    @Override
    public LectureListDTO getLectures(
            Long cursorId,
            Integer size,
            String name,
            String professor,
            Integer grade
    ) {
        if (size == null || size <= 0) size = 20;

        Pageable pageable = PageRequest.of(0, size);

        List<Teach> teaches = teachRepository.searchLectures(
                cursorId,
                (name == null || name.isBlank()) ? null : name,
                (professor == null || professor.isBlank()) ? null : professor,
                grade,
                pageable
        );

        List<LectureInfoDTO> list = teaches.stream()
                .map(CourseConverter::toLectureInfoDTO)
                .collect(Collectors.toList());

        Long nextCursor = teaches.size() < size
                ? null
                : teaches.get(teaches.size() - 1).getId();

        return CourseConverter.toLectureListDTO(list, nextCursor);
    }

    @Override
    public List<LectureInfoDTO> searchLectures(
            String name,
            String professorName,
            Integer grade,
            Pageable pageable
    ) {
        // 검색만 따로 쓰고 싶으면 cursorId 없이 호출
        List<Teach> teaches = teachRepository.searchLectures(
                null,
                (name == null || name.isBlank()) ? null : name,
                (professorName == null || professorName.isBlank()) ? null : professorName,
                grade,
                pageable
        );

        return teaches.stream()
                .map(CourseConverter::toLectureInfoDTO)
                .collect(Collectors.toList());
    }
}
