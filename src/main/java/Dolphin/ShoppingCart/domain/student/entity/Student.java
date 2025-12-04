package Dolphin.ShoppingCart.domain.student.entity;

import Dolphin.ShoppingCart.domain.academic.entity.Department;
import Dolphin.ShoppingCart.global.entity.BaseEntity;
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
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String studentName;

    @Column(name = "avg_reaction_time")
    private Double avgReactionTime;

    private Long bestBucket; // 대표 장바구니 번호

    private Integer grade;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Bucket> buckets;

    public void updateReactionTime(Double avgReactionTime) {
        this.avgReactionTime = avgReactionTime;
    }
}