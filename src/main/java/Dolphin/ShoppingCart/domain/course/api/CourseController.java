package Dolphin.ShoppingCart.domain.course.api;

import Dolphin.ShoppingCart.domain.course.application.CourseService;
import Dolphin.ShoppingCart.domain.course.dto.CourseResponseDTO.LectureListDTO;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "강의 API", description = "강의 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
@Validated
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "강의 목록 조회",
            description = "커서 기반 페이지네이션으로 강의 목록을 조회합니다. 강의명, 교수명, 학년으로 필터링할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "LECTURE_200_1", description = "강의 목록 조회 성공")
    })
    @GetMapping
    public BaseResponse<LectureListDTO> getLectures(
            @Parameter(description = "커서 ID (다음 페이지 조회용)", required = false)
            @RequestParam(required = false) Long cursorId,
            @Parameter(description = "한 페이지당 개수 (기본값: 20)", required = false)
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @Parameter(description = "강의명 (부분 검색)", required = false)
            @RequestParam(required = false) String name,
            @Parameter(description = "교수명 (부분 검색)", required = false)
            @RequestParam(required = false) String professor,
            @Parameter(description = "학년 필터", required = false)
            @RequestParam(required = false) Integer grade
    ) {
        LectureListDTO result = courseService.getLectures(cursorId, size, name, professor, grade);

        return BaseResponse.onSuccess(SuccessStatus.LECTURE_READ_SUCCESS, result);
    }
}
