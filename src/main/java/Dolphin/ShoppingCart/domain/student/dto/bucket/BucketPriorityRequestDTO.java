package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BucketPriorityRequestDTO {
    private Long bucketElementId;
    private Integer priority;
}