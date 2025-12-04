package Dolphin.ShoppingCart.domain.student.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentUpdateRequestDTO {
    private String studentName;
    private String password;
    private Integer grade;
    private Long departmentId;
}