package Dolphin.ShoppingCart.domain.student.dto.onboarding;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentOnboardingRequestDTO {

    private Long collegeId;
    private Long departmentId;
    private Integer grade;
    private String studentNumber;
}