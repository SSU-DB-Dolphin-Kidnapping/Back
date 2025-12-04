package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BucketSelectRequestDTO {
    private Long bucketId; // 대표로 설정할 장바구니 ID
}