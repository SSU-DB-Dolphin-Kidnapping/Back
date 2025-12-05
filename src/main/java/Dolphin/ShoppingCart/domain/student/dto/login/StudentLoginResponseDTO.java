package Dolphin.ShoppingCart.domain.student.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudentLoginResponseDTO {

    private Long id;
    private String studentName;
    private String nickname;
}