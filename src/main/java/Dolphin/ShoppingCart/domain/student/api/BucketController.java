package Dolphin.ShoppingCart.domain.student.api;

import Dolphin.ShoppingCart.domain.student.application.BucketService;
import Dolphin.ShoppingCart.domain.student.dto.bucket.*;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "장바구니 API", description = "수강신청 장바구니 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buckets")
public class BucketController {

    private final BucketService bucketService;

    // ========================================
    // 장바구니 관리
    // ========================================

    @Operation(summary = "내 장바구니 목록 조회",
            description = "현재 학생이 생성한 모든 장바구니 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 목록 조회 성공")
    })
    @GetMapping
    public BaseResponse<List<BucketSummaryDTO>> getMyBuckets() {
        Long studentId = 1L; // JWT 적용 전 임시 ID
        return BaseResponse.onSuccess(SuccessStatus.OK, bucketService.getMyBuckets(studentId));
    }

    @Operation(summary = "새 장바구니 생성",
            description = "새로운 장바구니를 생성합니다. 첫 번째 장바구니는 자동으로 대표 장바구니로 설정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 생성 성공")
    })
    @PostMapping
    public BaseResponse<Void> createBucket(
            @Valid @RequestBody BucketCreateRequestDTO request) {
        Long studentId = 1L;
        bucketService.createBucket(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "장바구니 삭제",
            description = "특정 장바구니를 삭제합니다. 대표 장바구니는 삭제할 수 없습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 삭제 성공")
    })
    @DeleteMapping("/{bucketId}")
    public BaseResponse<Void> deleteBucket(
            @Parameter(description = "삭제할 장바구니 ID", required = true)
            @PathVariable Long bucketId) {
        Long studentId = 1L;
        bucketService.deleteBucket(studentId, bucketId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "대표 장바구니 설정",
            description = "특정 장바구니를 대표 장바구니로 설정합니다. 수강신청 시 대표 장바구니가 사용됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대표 장바구니 설정 성공")
    })
    @PatchMapping("/{bucketId}/select")
    public BaseResponse<Void> setBestBucket(
            @Parameter(description = "대표로 설정할 장바구니 ID", required = true)
            @PathVariable Long bucketId) {
        Long studentId = 1L;
        bucketService.setBestBucket(studentId, bucketId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    // ========================================
    // 장바구니 항목 관리
    // ========================================

    @Operation(summary = "장바구니 과목 목록 조회",
            description = "특정 장바구니에 담긴 과목 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "과목 목록 조회 성공")
    })
    @GetMapping("/{bucketId}/elements")
    public BaseResponse<List<BucketResponseDTO>> getBucketElements(
            @Parameter(description = "장바구니 ID", required = true)
            @PathVariable Long bucketId) {
        Long studentId = 1L; // 임시 ID
        return BaseResponse.onSuccess(SuccessStatus.OK, bucketService.getBucketElements(studentId, bucketId));
    }

    @Operation(summary = "장바구니에 과목 추가",
            description = "특정 장바구니에 새로운 과목을 추가합니다. 우선순위는 자동으로 마지막에 배정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "과목 추가 성공")
    })
    @PostMapping("/{bucketId}/elements")
    public BaseResponse<Void> addCourse(
            @Parameter(description = "장바구니 ID", required = true)
            @PathVariable Long bucketId,
            @Valid @RequestBody BucketAddRequestDTO request) {
        Long studentId = 1L;
        bucketService.addCourse(studentId, bucketId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "장바구니에서 과목 삭제",
            description = "장바구니에서 특정 과목을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "과목 삭제 성공")
    })
    @DeleteMapping("/{bucketId}/elements/{elementId}")
    public BaseResponse<Void> deleteCourse(
            @Parameter(description = "장바구니 ID", required = true)
            @PathVariable Long bucketId,
            @Parameter(description = "삭제할 장바구니 항목 ID", required = true)
            @PathVariable Long elementId) {
        Long studentId = 1L;
        bucketService.deleteCourse(studentId, bucketId, elementId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "장바구니 과목 우선순위 변경",
            description = "장바구니에 담긴 과목들의 우선순위를 일괄 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "우선순위 변경 성공")
    })
    @PatchMapping("/{bucketId}/elements/priorities")
    public BaseResponse<Void> updatePriorities(
            @Parameter(description = "장바구니 ID", required = true)
            @PathVariable Long bucketId,
            @Valid @RequestBody List<BucketPriorityRequestDTO> requests) {
        Long studentId = 1L;
        bucketService.updatePriorities(studentId, bucketId, requests);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "대체 과목 설정",
            description = "특정 과목의 대체 과목을 설정합니다. 수강신청 실패 시 자동으로 대체 과목으로 신청을 시도합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대체 과목 설정 성공")
    })
    @PatchMapping("/{bucketId}/elements/{elementId}/alternate")
    public BaseResponse<Void> updateAlternateCourse(
            @Parameter(description = "장바구니 ID", required = true)
            @PathVariable Long bucketId,
            @Parameter(description = "대체 과목을 설정할 항목 ID", required = true)
            @PathVariable Long elementId,
            @Valid @RequestBody BucketAlternateRequestDTO request) {
        Long studentId = 1L;
        bucketService.updateAlternateCourse(studentId, bucketId, elementId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "대체 과목 설정 해제",
            description = "특정 과목에 설정된 대체 과목을 해제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대체 과목 해제 성공")
    })
    @DeleteMapping("/{bucketId}/elements/{elementId}/alternate")
    public BaseResponse<Void> removeAlternateCourse(
            @Parameter(description = "장바구니 ID", required = true)
            @PathVariable Long bucketId,
            @Parameter(description = "대체 과목을 해제할 항목 ID", required = true)
            @PathVariable Long elementId) {
        Long studentId = 1L;
        bucketService.removeAlternateCourse(studentId, bucketId, elementId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }
}