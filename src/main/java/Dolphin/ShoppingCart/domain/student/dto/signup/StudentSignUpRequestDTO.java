package Dolphin.ShoppingCart.domain.student.dto.signup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentSignUpRequestDTO {

    @NotBlank(message = "학생 이름은 필수입니다.")
    private String studentName;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 사이여야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;
}