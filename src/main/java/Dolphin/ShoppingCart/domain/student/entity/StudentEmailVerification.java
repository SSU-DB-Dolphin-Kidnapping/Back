package Dolphin.ShoppingCart.domain.student.entity;

import Dolphin.ShoppingCart.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEmailVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 학생의 인증코드인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 인증 코드 (6자리 등)
    @Column(nullable = false)
    private String code;

    // 해당 이메일 주소 (기록용)
    @Column(nullable = false)
    private String email;

    // 만료 시각
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    // 이미 사용되었는지 여부
    @Column(nullable = false)
    private Boolean used;

    public void markUsed() {
        this.used = true;
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}