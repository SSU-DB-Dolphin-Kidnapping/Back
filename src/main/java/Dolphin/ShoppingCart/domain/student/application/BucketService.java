package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.dto.bucket.*;
import java.util.List;

public interface BucketService {
    List<BucketResponseDTO> getBucketList(Long studentId);
    void addCourse(Long studentId, BucketAddRequestDTO request);
    void deleteCourse(Long studentId, Long bucketElementId);
    void updatePriorities(Long studentId, List<BucketPriorityRequestDTO> requests);
    void updateAlternateCourse(Long studentId, BucketAlternateRequestDTO request);
    List<BucketSummaryDTO> getMyBuckets(Long studentId);
    void createBucket(Long studentId, BucketCreateRequestDTO request);
    void deleteBucket(Long studentId, Long bucketId);
    void setBestBucket(Long studentId, BucketSelectRequestDTO request);
}