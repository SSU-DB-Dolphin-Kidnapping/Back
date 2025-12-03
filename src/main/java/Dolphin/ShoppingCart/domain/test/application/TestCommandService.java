package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;

public interface TestCommandService {
    TestResponseDTO.SimulationResultDTO runSimulation();
}
