package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.info.StudentInfoResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.onboarding.StudentOnboardingRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.update.StudentUpdateRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.verify.StudentEmailSendRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.verify.StudentEmailVerifyRequestDTO;

public interface StudentService {
    Double getReactionTime(Long studentId);
    void updateReactionTime(Long studentId, StudentReactionRequestDTO request);
    StudentSignUpResponseDTO signUp(StudentSignUpRequestDTO requestDTO);
    StudentLoginResponseDTO login(StudentLoginRequestDTO requestDTO);
    StudentInfoResponseDTO getStudentInfo(Long studentId);
    StudentInfoResponseDTO updateStudentInfo(Long studentId, StudentUpdateRequestDTO requestDTO);
    StudentInfoResponseDTO onboarding(Long studentId, StudentOnboardingRequestDTO requestDTO);
    void sendVerificationEmail(Long studentId, StudentEmailSendRequestDTO requestDTO);
    StudentInfoResponseDTO verifyEmail(Long studentId, StudentEmailVerifyRequestDTO requestDTO);

}
