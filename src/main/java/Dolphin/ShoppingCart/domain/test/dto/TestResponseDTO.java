package Dolphin.ShoppingCart.domain.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
