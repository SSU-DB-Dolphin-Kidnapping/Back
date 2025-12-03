package Dolphin.ShoppingCart.domain.course.entity;

import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import Dolphin.ShoppingCart.global.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.course.enums.SemesterType;
import Dolphin.ShoppingCart.domain.course.enums.TeachType;
import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Teach extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    private Integer number;   // 과목번호
    private String className; // 분반명

    @Enumerated(EnumType.STRING)
    private TeachType type;   // 강의 유형

    private Integer maxCount; // 최대 인원
    private Integer enrolledCount; // 담은 인원
    private Integer remainCount; // 잔여 인원

    @OneToMany(mappedBy = "teach", fetch = FetchType.LAZY)
    private List<TeachInfo> teachInfos;

    @OneToMany(mappedBy = "teach", fetch = FetchType.LAZY)
    private List<BucketElement> bucketElements;

    // 시뮬레이션을 위한 카운트 초기화
    public void resetCounts() {
        this.enrolledCount = 0;
        this.remainCount = this.maxCount;
    }

    // 수강신청 성공 시 카운트 증가 (비관적 락으로 동시성 제어)
    public boolean tryEnroll() {
        if (this.remainCount > 0) {
            this.enrolledCount++;
            this.remainCount--;
            return true;
        }
        return false;
    }
}