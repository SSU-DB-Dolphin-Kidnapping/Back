package Dolphin.ShoppingCart.domain.course.api;

import Dolphin.ShoppingCart.domain.course.application.CourseService;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureListDTO;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
@Validated
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public BaseResponse<LectureListDTO> getLectures(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String professor,
            @RequestParam(required = false) Integer grade
    ) {
        LectureListDTO result = courseService.getLectures(cursorId, size, name, professor, grade);

        return BaseResponse.onSuccess(SuccessStatus.LECTURE_READ_SUCCESS, result);
    }
}
