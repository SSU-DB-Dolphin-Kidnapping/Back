package Dolphin.ShoppingCart.domain.student.repository;

import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface BucketElementRepository extends JpaRepository<BucketElement, Long>{

    @Query("SELECT be FROM BucketElement be " +
            "JOIN FETCH be.teach t " +
            "JOIN FETCH t.course c " +
            "JOIN FETCH t.professor p " +
            "LEFT JOIN FETCH be.subElement se " +
            "LEFT JOIN FETCH se.teach st " +
            "LEFT JOIN FETCH st.course sc " +
            "WHERE be.bucket.id = :bucketId " +
            "ORDER BY be.priority ASC")
    List<BucketElement> findAllByBucketIdWithDetails(@Param("bucketId") Long bucketId);

    // 장바구니 내 특정 수업이 이미 있는지 확인
    boolean existsByBucketIdAndTeachId(Long bucketId, Long teachId);

    // 가장 높은 우선순위 조회
    @Query("SELECT MAX(be.priority) FROM BucketElement be WHERE be.bucket.id = :bucketId")
    Optional<Integer> findMaxPriorityByBucketId(@Param("bucketId") Long bucketId);

    // 특정 수업 ID로 내 장바구니 항목 찾기 (대체과목 연결용)
    Optional<BucketElement> findByBucketIdAndTeachId(Long bucketId, Long teachId);
}
