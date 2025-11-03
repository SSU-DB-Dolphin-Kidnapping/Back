package Dolphin.ShoppingCart.domain.course.entity;

import Dolphin.ShoppingCart.domain.model.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.model.enums.SecondMajorType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class CourseType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SecondMajorType secondMajor; // 복수/부전공/교양 등 ENUM
}