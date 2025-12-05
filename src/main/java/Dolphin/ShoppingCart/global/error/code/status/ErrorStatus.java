package Dolphin.ShoppingCart.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 학생 도메인 에러

    STUDENT_NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "STUDENT4091", "이미 사용 중인 닉네임입니다."),
    STUDENT_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "STUDENT4011", "닉네임 또는 비밀번호가 올바르지 않습니다."),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDENT4041", "해당 학생을 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "DEPARTMENT4041", "해당 학과를 찾을 수 없습니다."),
    STUDENT_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "STUDENT4092", "이미 등록된 학번입니다."),
    COLLEGE_NOT_FOUND(HttpStatus.NOT_FOUND, "COLLEGE4041", "해당 학부를 찾을 수 없습니다."),

    // 이메일 인증 에러 ---------------------------
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL5001", "인증 메일 전송에 실패했습니다."),
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "VERIFY4001", "인증 코드가 올바르지 않습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "VERIFY4002", "인증 코드가 만료되었습니다."),
    VERIFICATION_CODE_ALREADY_USED(HttpStatus.BAD_REQUEST, "VERIFY4003", "이미 사용된 인증 코드입니다."),
    EMAIL_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "STU4001", "이미 사용 중인 숭실대 이메일입니다."),
    ALREADY_VERIFIED_STUDENT(HttpStatus.BAD_REQUEST, "STU4002", "이미 이메일 인증이 완료된 학생입니다."),
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
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}