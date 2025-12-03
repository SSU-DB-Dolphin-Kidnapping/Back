package Dolphin.ShoppingCart.domain.test.converter;

import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;

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
}
