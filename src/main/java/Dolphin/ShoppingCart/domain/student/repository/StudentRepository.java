package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
    boolean existsByNickname(String nickname);

}
