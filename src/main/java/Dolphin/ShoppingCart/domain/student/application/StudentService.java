package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.info.StudentInfoResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;

public interface StudentService {
    Double getReactionTime(Long studentId);
    void updateReactionTime(Long studentId, StudentReactionRequestDTO request);
    StudentSignUpResponseDTO signUp(StudentSignUpRequestDTO requestDTO);
    StudentLoginResponseDTO login(StudentLoginRequestDTO requestDTO);
    StudentInfoResponseDTO getStudentInfo(Long studentId);

}
