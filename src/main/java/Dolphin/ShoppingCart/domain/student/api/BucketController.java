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
@RequestMapping("/api/bucket")
public class BucketController {

    private final BucketService bucketService;

    @Operation(summary = "대표 장바구니 과목 목록 조회",
            description = "현재 학생의 대표 장바구니에 담긴 과목 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 조회 성공")
    })
    @GetMapping
    public BaseResponse<List<BucketResponseDTO>> getBucket() {
        Long studentId = 1L; // 임시 ID
        return BaseResponse.onSuccess(SuccessStatus.OK, bucketService.getBucketList(studentId));
    }

    @Operation(summary = "장바구니에 과목 추가",
            description = "대표 장바구니에 새로운 과목을 추가합니다. 우선순위는 자동으로 마지막에 배정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "과목 추가 성공")
    })
    @PostMapping
    public BaseResponse<Void> addCourse(
            @Valid @RequestBody BucketAddRequestDTO request) {
        Long studentId = 1L;
        bucketService.addCourse(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "장바구니에서 과목 삭제",
            description = "장바구니에서 특정 과목을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "과목 삭제 성공")
    })
    @DeleteMapping("/{bucketElementId}")
    public BaseResponse<Void> deleteCourse(
            @Parameter(description = "삭제할 장바구니 항목 ID", required = true)
            @PathVariable Long bucketElementId) {
        Long studentId = 1L;
        bucketService.deleteCourse(studentId, bucketElementId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "장바구니 과목 우선순위 변경",
            description = "장바구니에 담긴 과목들의 우선순위를 일괄 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "우선순위 변경 성공")
    })
    @PatchMapping("/priority")
    public BaseResponse<Void> updatePriorities(
            @Valid @RequestBody List<BucketPriorityRequestDTO> requests) {
        Long studentId = 1L;
        bucketService.updatePriorities(studentId, requests);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "대체 과목 설정",
            description = "특정 과목의 대체 과목을 설정합니다. 수강신청 실패 시 자동으로 대체 과목으로 신청을 시도합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대체 과목 설정 성공")
    })
    @PatchMapping("/alternate")
    public BaseResponse<Void> updateAlternateCourse(
            @Valid @RequestBody BucketAlternateRequestDTO request) {
        Long studentId = 1L;
        bucketService.updateAlternateCourse(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "내 장바구니 목록 조회",
            description = "현재 학생이 생성한 모든 장바구니 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 목록 조회 성공")
    })
    @GetMapping("/list")
    public BaseResponse<List<BucketSummaryDTO>> getMyBuckets() {
        Long studentId = 1L; // JWT 적용 전 임시 ID
        return BaseResponse.onSuccess(SuccessStatus.OK, bucketService.getMyBuckets(studentId));
    }

    @Operation(summary = "새 장바구니 생성",
            description = "새로운 장바구니를 생성합니다. 첫 번째 장바구니는 자동으로 대표 장바구니로 설정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 생성 성공")
    })
    @PostMapping("/create")
    public BaseResponse<Void> createBucket(
            @Valid @RequestBody BucketCreateRequestDTO request) {
        Long studentId = 1L;
        bucketService.createBucket(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "대표 장바구니 설정",
            description = "특정 장바구니를 대표 장바구니로 설정합니다. 수강신청 시 대표 장바구니가 사용됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대표 장바구니 설정 성공")
    })
    @PatchMapping("/best")
    public BaseResponse<Void> setBestBucket(
            @Valid @RequestBody BucketSelectRequestDTO request) {
        Long studentId = 1L;
        bucketService.setBestBucket(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @Operation(summary = "장바구니 삭제",
            description = "특정 장바구니를 삭제합니다. 대표 장바구니는 삭제할 수 없습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 삭제 성공")
    })
    @DeleteMapping("/cart/{bucketId}")
    public BaseResponse<Void> deleteBucket(
            @Parameter(description = "삭제할 장바구니 ID", required = true)
            @PathVariable Long bucketId) {
        Long studentId = 1L;
        bucketService.deleteBucket(studentId, bucketId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }
}