package Dolphin.ShoppingCart.domain.academic.repository;

import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByProfessorName(String professorName);
}
