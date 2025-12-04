package Dolphin.ShoppingCart.domain.student.dto.signup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudentSignUpResponseDTO {

    private Long id;
    private String studentName;
    private String nickname;
}