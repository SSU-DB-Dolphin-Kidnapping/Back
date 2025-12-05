package Dolphin.ShoppingCart.domain.student.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentLoginRequestDTO {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}