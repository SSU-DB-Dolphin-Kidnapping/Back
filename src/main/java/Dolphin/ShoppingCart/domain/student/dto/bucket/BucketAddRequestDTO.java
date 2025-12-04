package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BucketAddRequestDTO {
    private Long teachId; // 담을 수업 ID
}