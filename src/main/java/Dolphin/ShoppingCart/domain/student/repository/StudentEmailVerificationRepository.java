package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.StudentEmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentEmailVerificationRepository extends JpaRepository<StudentEmailVerification, Long> {

    // studentId 기준으로 "가장 최근" 코드 가져오고 싶다면 쿼리 메서드 이렇게
    Optional<StudentEmailVerification> findTopByStudentIdOrderByCreatedAtDesc(Long studentId);
}