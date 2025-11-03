package Dolphin.ShoppingCart.domain.academic.entity;

import Dolphin.ShoppingCart.domain.model.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.course.entity.Teach;
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
public class Professor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false, length = 50)
    private String professorName;

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private List<Teach> teaches;
}