package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;

public interface StudentService {
    Double getReactionTime(Long studentId);
    void updateReactionTime(Long studentId, StudentReactionRequestDTO request);
}
