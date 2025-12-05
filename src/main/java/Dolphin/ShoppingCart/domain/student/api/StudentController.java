package Dolphin.ShoppingCart.domain.student.api;

import Dolphin.ShoppingCart.domain.student.dto.info.StudentInfoResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.onboarding.StudentOnboardingRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.update.StudentUpdateRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.verify.StudentEmailSendRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.verify.StudentEmailVerifyRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.application.StudentService;
import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.SuccessStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Validated
public class StudentController {
    private final StudentService studentService; // 생성자 주입 필요

    @GetMapping("/reaction-time")
    public BaseResponse<Double> getReactionTime() {
        Long studentId = 1L; // 임시 ID (추후 SecurityContext 사용)
        return BaseResponse.onSuccess(SuccessStatus.OK, studentService.getReactionTime(studentId));
    }

    @PatchMapping("/reaction-time")
    public BaseResponse<Void> updateReactionTime(@RequestBody StudentReactionRequestDTO request) {
        Long studentId = 1L; // 임시 ID
        studentService.updateReactionTime(studentId, request);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "학생 회원가입 API",
            description = "학생 이름, 닉네임(Unique), 비밀번호로 학생 계정을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT_200_1", description = "학생 회원가입이 성공적으로 완료되었습니다.")
    })
    public BaseResponse<StudentSignUpResponseDTO> signUp(
            @Valid @RequestBody StudentSignUpRequestDTO requestDTO
    ) {
        StudentSignUpResponseDTO result = studentService.signUp(requestDTO);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_SIGNUP_SUCCESS, result);
    }

    @PostMapping("/login")
    @Operation(summary = "학생 로그인 API",
            description = "닉네임과 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT2002", description = "학생 로그인이 성공적으로 완료되었습니다.")
    })
    public BaseResponse<StudentLoginResponseDTO> login(
            @Valid @RequestBody StudentLoginRequestDTO requestDTO
    ) {
        StudentLoginResponseDTO result = studentService.login(requestDTO);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_LOGIN_SUCCESS, result);
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "학생 정보 조회 API",
            description = "학생 ID로 회원 정보를 조회합니다. 비밀번호는 포함되지 않습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT2003", description = "학생 정보 조회 성공")
    })
    public BaseResponse<StudentInfoResponseDTO> getStudentInfo(
            @PathVariable Long studentId
    ) {
        StudentInfoResponseDTO result = studentService.getStudentInfo(studentId);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_INFO_SUCCESS, result);
    }

    @PatchMapping("/{studentId}")
    @Operation(summary = "학생 정보 수정 API",
            description = "학생 이름, 비밀번호, 학년, 학과를 수정합니다. 전달된 값만 변경됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT2004", description = "학생 정보 수정 성공")
    })
    public BaseResponse<StudentInfoResponseDTO> updateStudentInfo(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentUpdateRequestDTO requestDTO
    ) {
        StudentInfoResponseDTO result = studentService.updateStudentInfo(studentId, requestDTO);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_UPDATE_SUCCESS, result);
    }


    @PatchMapping("/{studentId}/onboarding")
    @Operation(summary = "학생 온보딩 API",
            description = "학부, 학과, 학년, 학번 정보를 입력해서 저장합니다. (현재는 OCR 없이 직접 입력)")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT2005", description = "학생 온보딩 정보가 성공적으로 저장되었습니다.")
    })
    public BaseResponse<StudentInfoResponseDTO> onboarding(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentOnboardingRequestDTO requestDTO
    ) {
        StudentInfoResponseDTO result = studentService.onboarding(studentId, requestDTO);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_ONBOARDING_SUCCESS, result);
    }

    // 인증 메일 보내기
    @PostMapping("/{studentId}/email/send-verification")
    @Operation(summary = "숭실대 이메일 인증 메일 발송",
            description = "숭실대학교 이메일로 인증 코드를 발송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT2006", description = "인증 메일이 성공적으로 발송되었습니다.")
    })
    public BaseResponse<Void> sendVerificationEmail(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentEmailSendRequestDTO requestDTO
    ) {
        studentService.sendVerificationEmail(studentId, requestDTO);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_EMAIL_SEND_SUCCESS, null);
    }

    // 인증 코드 검증
    @PostMapping("/{studentId}/email/verify")
    @Operation(summary = "숭실대 이메일 인증 코드 검증",
            description = "이메일로 받은 인증 코드를 검증하고, 학생을 숭실대 재학생으로 인증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "STUDENT2007", description = "숭실대 이메일 인증이 완료되었습니다.")
    })
    public BaseResponse<StudentInfoResponseDTO> verifyEmail(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentEmailVerifyRequestDTO requestDTO
    ) {
        StudentInfoResponseDTO result = studentService.verifyEmail(studentId, requestDTO);
        return BaseResponse.onSuccess(SuccessStatus.STUDENT_EMAIL_VERIFY_SUCCESS, result);
    }
}
