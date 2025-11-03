package Dolphin.ShoppingCart.domain.course.entity;

import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import Dolphin.ShoppingCart.domain.model.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.model.enums.SemesterType;
import Dolphin.ShoppingCart.domain.model.enums.TeachType;
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

    private Integer maxCount;
    private Integer enrolledCount;

    @OneToMany(mappedBy = "teach", fetch = FetchType.LAZY)
    private List<TeachInfo> teachInfos;

    @OneToMany(mappedBy = "teach", fetch = FetchType.LAZY)
    private List<BucketElement> bucketElements;
}