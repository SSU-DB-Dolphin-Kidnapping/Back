package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BucketRepository extends JpaRepository<Bucket, Long>{
    List<Bucket> findAllByStudent(Student student);

    @Query("""
        SELECT DISTINCT b FROM Bucket b
        LEFT JOIN FETCH b.bucketElements be
        LEFT JOIN FETCH be.teach t
        LEFT JOIN FETCH t.course c
        LEFT JOIN FETCH be.subElement se
        LEFT JOIN FETCH se.teach
        WHERE b.id = :id
        """)
    Optional<Bucket> findByIdWithElements(@Param("id") Long id);
}
