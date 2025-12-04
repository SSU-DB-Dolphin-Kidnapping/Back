package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;

public interface StudentService {
    Double getReactionTime(Long studentId);
    void updateReactionTime(Long studentId, StudentReactionRequestDTO request);
    StudentSignUpResponseDTO signUp(StudentSignUpRequestDTO requestDTO);

}
