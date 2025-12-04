package Dolphin.ShoppingCart.domain.student.api;

import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
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
@RequestMapping("/student-")
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
}
