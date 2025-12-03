package Dolphin.ShoppingCart.domain.test.entity;

import Dolphin.ShoppingCart.global.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
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
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_element_id", nullable = false)
    private BucketElement bucketElement;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    private Boolean isSuccess;
    private String failedReason;
}