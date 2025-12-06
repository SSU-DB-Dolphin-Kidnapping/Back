package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.dto.bucket.*;
import java.util.List;

public interface BucketService {
    List<BucketResponseDTO> getBucketElements(Long studentId, Long bucketId);
    void addCourse(Long studentId, Long bucketId, BucketAddRequestDTO request);
    void deleteCourse(Long studentId, Long bucketId, Long bucketElementId);
    void updatePriorities(Long studentId, Long bucketId, List<BucketPriorityRequestDTO> requests);
    void updateAlternateCourse(Long studentId, Long bucketId, Long bucketElementId, BucketAlternateRequestDTO request);
    void removeAlternateCourse(Long studentId, Long bucketId, Long bucketElementId);
    List<BucketSummaryDTO> getMyBuckets(Long studentId);
    void createBucket(Long studentId, BucketCreateRequestDTO request);
    void deleteBucket(Long studentId, Long bucketId);
    void setBestBucket(Long studentId, Long bucketId);
}