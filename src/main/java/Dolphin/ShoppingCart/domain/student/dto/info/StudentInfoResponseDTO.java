package Dolphin.ShoppingCart.domain.student.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudentInfoResponseDTO {

    private Long id;
    private String studentName;
    private String nickname;
    private String studentNumber;
    private Integer grade;
    private Long departmentId;
    private Double avgReactionTime;
    private Long bestBucket;
}