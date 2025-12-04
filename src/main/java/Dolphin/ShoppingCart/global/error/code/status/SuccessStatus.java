package Dolphin.ShoppingCart.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    //Common
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),

    //테스트
    Test_SUCCESS(HttpStatus.OK,"TEST_200", "테스트가 성공적으로 완료되었습니다."),

    STUDENT_SIGNUP_SUCCESS(HttpStatus.OK, "STUDENT2001", "학생 회원가입이 성공적으로 완료되었습니다."),
    STUDENT_LOGIN_SUCCESS(HttpStatus.OK, "STUDENT2002", "학생 로그인이 성공적으로 완료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
