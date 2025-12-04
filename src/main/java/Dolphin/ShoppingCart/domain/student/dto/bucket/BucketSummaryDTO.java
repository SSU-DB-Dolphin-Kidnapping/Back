package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BucketSummaryDTO {
    private Long bucketId;
    private String name;
    private Boolean isBest; // 현재 대표 장바구니 여부
    private LocalDateTime createdAt;
}