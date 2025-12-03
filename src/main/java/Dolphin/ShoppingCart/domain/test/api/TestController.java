package Dolphin.ShoppingCart.domain.test.api;

import Dolphin.ShoppingCart.domain.test.application.TestService;
import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "시뮬레이션 테스트 API", description = "수강신청 시뮬레이션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Validated
public class TestController {

    private final TestService testService;

    @Operation(summary = "수강신청 시뮬레이션 실행", description = "모든 학생의 bestBucket에 대해 수강신청 시뮬레이션을 실행합니다.")
    @PostMapping("/simulation")
    public BaseResponse<TestResponseDTO.SimulationResultDTO> runSimulation() {
        TestResponseDTO.SimulationResultDTO result = testService.runSimulation();
        return BaseResponse.onSuccess(SuccessStatus.OK, result);
    }
}
