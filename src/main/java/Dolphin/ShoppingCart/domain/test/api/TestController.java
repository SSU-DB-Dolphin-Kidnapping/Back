package Dolphin.ShoppingCart.domain.test.api;

import Dolphin.ShoppingCart.domain.test.application.TestCommandService;
import Dolphin.ShoppingCart.domain.test.application.TestQueryService;
import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "시뮬레이션 테스트 API", description = "수강신청 시뮬레이션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Validated
public class TestController {

    private final TestCommandService testCommandService;
    private final TestQueryService testQueryService;

    @Operation(summary = "수강신청 시뮬레이션 실행", description = "모든 학생의 bestBucket에 대해 수강신청 시뮬레이션을 실행합니다.")
    @PostMapping("/simulation")
    public BaseResponse<TestResponseDTO.SimulationResultDTO> runSimulation() {
        TestResponseDTO.SimulationResultDTO result = testCommandService.runSimulation();
        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }

    @Operation(summary = "내 테스트 결과 전체 리스트 조회", description = "현재 로그인한 학생의 모든 테스트 결과 요약 정보를 조회합니다.")
    @GetMapping("/results")
    public BaseResponse<List<TestResponseDTO.TestSummaryDTO>> getStudentTestResults() {
        // TODO: JWT 토큰에서 studentId 추출 예정
        Long studentId = 1L;
        List<TestResponseDTO.TestSummaryDTO> results = testQueryService.getStudentTestResults(studentId);
        return BaseResponse.onSuccess(SuccessStatus.OK, results);
    }

    @Operation(summary = "내 테스트 결과 상세 조회", description = "현재 로그인한 학생의 특정 테스트 결과를 상세하게 조회합니다.")
    @GetMapping("/results/{testId}")
    public BaseResponse<TestResponseDTO.TestDetailDTO> getStudentTestDetail(
            @Parameter(description = "테스트 ID", required = true)
            @PathVariable Long testId) {
        // TODO: JWT 토큰에서 studentId 추출 예정
        Long studentId = 1L;
        TestResponseDTO.TestDetailDTO result = testQueryService.getStudentTestDetail(studentId, testId);
        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }
}
