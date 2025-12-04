package Dolphin.ShoppingCart.domain.student.dto.bucket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BucketResponseDTO {
    private Long bucketElementId;   // 담은 과목 ID (삭제/수정용)
    private Long teachId;           // 수업 ID
    private Integer priority;       // 우선순위
    private String majorType;       // 이수구분 (전공, 교양 등)
    private String courseName;      // 과목명
    private String professorName;   // 교수명
    private String timePlace;       // 시간/장소 (예: "월 10:30 (정보관-101)")
    private Long alternateTeachId;  // 대체과목의 Teach ID
    private String alternateSubjectName; // 대체과목명
}