package Dolphin.ShoppingCart.domain.test.application;

import Dolphin.ShoppingCart.domain.test.dto.TestResponseDTO;

import java.util.List;

public interface TestQueryService {
    List<TestResponseDTO.TestSummaryDTO> getStudentTestResults(Long studentId);

    TestResponseDTO.TestDetailDTO getStudentTestDetail(Long studentId, Long testId);
}
