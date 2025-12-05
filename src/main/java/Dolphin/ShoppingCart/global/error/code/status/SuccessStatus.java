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


    // Lecture
    LECTURE_READ_SUCCESS(HttpStatus.OK, "LECTURE_200", "강의 조회에 성공했습니다."),


    STUDENT_SIGNUP_SUCCESS(HttpStatus.OK, "STUDENT2001", "학생 회원가입이 성공적으로 완료되었습니다."),
    STUDENT_LOGIN_SUCCESS(HttpStatus.OK, "STUDENT2002", "학생 로그인이 성공적으로 완료되었습니다."),
    STUDENT_INFO_SUCCESS(HttpStatus.OK, "STUDENT2003", "학생 정보가 성공적으로 조회되었습니다."),
    STUDENT_UPDATE_SUCCESS(HttpStatus.OK, "STUDENT2004", "학생 정보가 성공적으로 수정되었습니다."),
    STUDENT_ONBOARDING_SUCCESS(HttpStatus.OK, "STUDENT2005", "학생 온보딩 정보가 성공적으로 저장되었습니다."),
    STUDENT_EMAIL_SEND_SUCCESS(HttpStatus.OK, "STUDENT2006", "인증 메일이 성공적으로 발송되었습니다."),
    STUDENT_EMAIL_VERIFY_SUCCESS(HttpStatus.OK, "STUDENT2007", "숭실대 이메일 인증이 완료되었습니다."),
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
