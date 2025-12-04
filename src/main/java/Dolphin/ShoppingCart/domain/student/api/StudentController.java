package Dolphin.ShoppingCart.domain.student.api;

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
}
