package Dolphin.ShoppingCart.domain.course.entity;

import Dolphin.ShoppingCart.domain.model.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.model.enums.MajorType;
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
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MajorType majority; // 전필/전선/교양 등 ENUM 타입

    private Float time;   // 시수
    private Float credit; // 학점

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseType> courseTypes;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Teach> teaches;
}