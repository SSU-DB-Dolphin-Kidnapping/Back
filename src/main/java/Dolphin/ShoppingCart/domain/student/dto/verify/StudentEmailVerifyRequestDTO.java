package Dolphin.ShoppingCart.domain.student.dto.verify;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentEmailVerifyRequestDTO {

    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String code;
}