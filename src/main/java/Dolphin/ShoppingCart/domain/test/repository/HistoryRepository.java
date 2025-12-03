package Dolphin.ShoppingCart.domain.test.repository;

import Dolphin.ShoppingCart.domain.test.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("""
            SELECT h FROM History h
            JOIN FETCH h.bucketElement be
            JOIN FETCH be.bucket b
            WHERE b.student.id = :studentId
            """)
    List<History> findAllByStudentId(@Param("studentId") Long studentId);

    @Query("""
            SELECT h FROM History h
            JOIN FETCH h.bucketElement be
            JOIN FETCH be.bucket b
            JOIN FETCH be.teach t
            JOIN FETCH t.course c
            WHERE b.student.id = :studentId AND h.test.id = :testId
            """)
    List<History> findByStudentIdAndTestId(@Param("studentId") Long studentId, @Param("testId") Long testId);
}
