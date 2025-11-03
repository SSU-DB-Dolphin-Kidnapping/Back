package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketElementRepository extends JpaRepository<BucketElement, Long>{
}
