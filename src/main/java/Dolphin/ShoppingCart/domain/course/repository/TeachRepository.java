package Dolphin.ShoppingCart.domain.course.repository;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeachRepository extends JpaRepository<Teach, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Teach t WHERE t.id = :id")
    Optional<Teach> findByIdWithLock(@Param("id") Long id);
}
