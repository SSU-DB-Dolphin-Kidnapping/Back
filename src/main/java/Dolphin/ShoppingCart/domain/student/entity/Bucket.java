package Dolphin.ShoppingCart.domain.student.entity;

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
public class Bucket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String name;

    @OneToMany(mappedBy = "bucket", fetch = FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 100)
    private List<BucketElement> bucketElements;
}