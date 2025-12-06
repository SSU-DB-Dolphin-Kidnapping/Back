package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BucketAlternateRequestDTO {
    private Long alternateTeachId; // 대체할 과목의 수업 ID (없으면 null)
}