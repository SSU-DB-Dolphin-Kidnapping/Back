package Dolphin.ShoppingCart.domain.student.entity;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.test.entity.History;
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
public class BucketElement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teach_id")
    private Teach teach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    private Integer priority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_element_id")
    private BucketElement subElement; // 대체과목

    @OneToMany(mappedBy = "bucketElement", fetch = FetchType.LAZY)
    private List<History> histories;
}