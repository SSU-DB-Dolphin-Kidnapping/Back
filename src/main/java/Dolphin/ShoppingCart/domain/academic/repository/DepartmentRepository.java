package Dolphin.ShoppingCart.domain.academic.repository;

import Dolphin.ShoppingCart.domain.academic.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
