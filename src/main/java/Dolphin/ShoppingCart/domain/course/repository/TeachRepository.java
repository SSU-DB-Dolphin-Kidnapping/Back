package Dolphin.ShoppingCart.domain.course.repository;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface TeachRepository extends JpaRepository<Teach, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT DISTINCT t FROM Teach t
        LEFT JOIN FETCH t.course c
        LEFT JOIN FETCH t.teachInfos
        WHERE t.id = :id
        """)
    Optional<Teach> findByIdWithLock(@Param("id") Long id);

    // 비관적 락 없이 원자적으로 등록 (빠른 처리, 타임아웃 없음)
    @Modifying
    @Query("""
        UPDATE Teach t
        SET t.enrolledCount = t.enrolledCount + 1,
            t.remainCount = t.remainCount - 1
        WHERE t.id = :id AND t.remainCount > 0
        """)
    int tryEnrollAtomic(@Param("id") Long id);

    // 락 없이 조회
    @Query("""
        SELECT DISTINCT t FROM Teach t
        LEFT JOIN FETCH t.course c
        LEFT JOIN FETCH t.teachInfos
        WHERE t.id = :id
        """)
    Optional<Teach> findByIdWithDetails(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT t FROM Teach t
        LEFT JOIN FETCH t.teachInfos
        """)
    List<Teach> findAllWithTeachInfos();

    // 페이징 + 검색
    @Query("""
        SELECT t FROM Teach t
        JOIN t.course c
        JOIN t.professor p
        WHERE (:cursorId IS NULL OR t.id < :cursorId)
        AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:professor IS NULL OR LOWER(p.professorName) LIKE LOWER(CONCAT('%', :professor, '%')))
        AND (:grade IS NULL OR t.targetGrade = :grade OR t.targetGrade IS NULL)
        ORDER BY t.id DESC
    """)
    List<Teach> searchLectures(
            @Param("cursorId") Long cursorId,
            @Param("name") String name,
            @Param("professor") String professor,
            @Param("grade") Integer grade,
            Pageable pageable
    );
}
