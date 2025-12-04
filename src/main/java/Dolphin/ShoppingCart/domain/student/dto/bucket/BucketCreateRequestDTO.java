package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BucketCreateRequestDTO {
    private String name; // 장바구니 이름 (예: "플랜 A", "망하면 이거")
}