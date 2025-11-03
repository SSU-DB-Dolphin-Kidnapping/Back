package Dolphin.ShoppingCart.domain.academic.repository;

import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
