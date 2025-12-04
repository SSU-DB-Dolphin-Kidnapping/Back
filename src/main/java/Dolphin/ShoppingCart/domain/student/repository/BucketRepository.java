package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long>{
    List<Bucket> findAllByStudent(Student student);
}
