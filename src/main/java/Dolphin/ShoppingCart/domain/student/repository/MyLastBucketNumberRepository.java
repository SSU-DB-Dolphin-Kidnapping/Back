package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.MyLastBucketNumber;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyLastBucketNumberRepository extends JpaRepository<MyLastBucketNumber, Long>{
    Optional<MyLastBucketNumber> findByStudent(Student student);

}
