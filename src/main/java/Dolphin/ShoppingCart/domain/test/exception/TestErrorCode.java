package Dolphin.ShoppingCart.domain.test.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.error.code.status.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TestErrorCode implements BaseErrorCode {

    // Test 관련 에러
    NO_TEST_COMPLETE(HttpStatus.BAD_REQUEST, "TEST400", "테스트 결과가 나오지 않았습니다."),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST4001", "학생을 찾을 수 없습니다."),
    BUCKET_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST4002", "장바구니를 찾을 수 없습니다."),
    TEST_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST4003", "테스트를 찾을 수 없습니다."),
    TEST_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST4004", "해당 테스트 결과를 찾을 수 없습니다."),
    BEST_BUCKET_NOT_SET(HttpStatus.BAD_REQUEST, "TEST4005", "학생의 대표 장바구니가 설정되지 않았습니다."),
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
