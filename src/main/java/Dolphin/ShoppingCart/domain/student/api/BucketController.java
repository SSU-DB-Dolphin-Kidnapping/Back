package Dolphin.ShoppingCart.domain.student.api;

import Dolphin.ShoppingCart.domain.student.application.BucketService;
import Dolphin.ShoppingCart.domain.student.dto.bucket.*;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bucket-")
public class BucketController {

    private final BucketService bucketService;

    @GetMapping
    public BaseResponse<List<BucketResponseDTO>> getBucket() {
        Long studentId = 1L; // 임시 ID
        return BaseResponse.onSuccess(SuccessStatus.OK, bucketService.getBucketList(studentId));
    }

    @PostMapping
    public BaseResponse<Void> addCourse(@RequestBody BucketAddRequestDTO request) {
        Long studentId = 1L;
        bucketService.addCourse(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @DeleteMapping("/{bucketElementId}")
    public BaseResponse<Void> deleteCourse(@PathVariable Long bucketElementId) {
        Long studentId = 1L;
        bucketService.deleteCourse(studentId, bucketElementId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PatchMapping("/priority")
    public BaseResponse<Void> updatePriorities(@RequestBody List<BucketPriorityRequestDTO> requests) {
        Long studentId = 1L;
        bucketService.updatePriorities(studentId, requests);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PatchMapping("/alternate")
    public BaseResponse<Void> updateAlternateCourse(@RequestBody BucketAlternateRequestDTO request) {
        Long studentId = 1L;
        bucketService.updateAlternateCourse(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }
}