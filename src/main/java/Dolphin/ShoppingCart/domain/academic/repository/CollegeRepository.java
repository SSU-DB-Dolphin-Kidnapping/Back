package Dolphin.ShoppingCart.domain.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Dolphin.ShoppingCart.domain.academic.entity.College;

public interface CollegeRepository extends JpaRepository<College, Long> {
}
