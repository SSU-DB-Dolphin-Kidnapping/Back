package Dolphin.ShoppingCart.global.exception;

import Dolphin.ShoppingCart.global.common.response.BaseResponse;
import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j // 로깅을 위한 Lombok 어노테이션(log.info(), log.error() 등 사용 가능)
@RestControllerAdvice(annotations = {RestController.class})
// 모든 @RestController 대상에서 발생하는 예외를 전역처리하는 어노테이션
// annotations = {RestController.class} -> @RestController가 붙은 클래스만 처리 대상으로 한정
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {
// ResponseEntityExceptionHandler -> Spring이 제공하는 기본 예외 처리 클래스
// 이를 상속 받음으로써 HTTP 요청 처리 중 발생하는 예외에 대해 맞춤 응답을 줄 수 있음


    // 쿼리 파라미터 등에 잘못된 타입이 들어온 경우 예외 처리
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String errorMessage = e.getPropertyName() + ": 올바른 값이 아닙니다.";

        return handleExceptionInternalMessage(e, headers, request, errorMessage);
    }

    // 필수 파라미터가 누락된 경우 처리
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String errorMessage = e.getParameterName() + ": 올바른 값이 아닙니다.";

        return handleExceptionInternalMessage(e, headers, request, errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage =
                e.getConstraintViolations().stream()
                        .map(constraintViolation -> constraintViolation.getMessage())
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(
                e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(
                        fieldError -> {
                            String fieldName = fieldError.getField();
                            String errorMessage;
                            try {
                                errorMessage = Optional.ofNullable(ErrorStatus.valueOf(fieldError.getDefaultMessage()).getMessage()).orElse("");
                            } catch (IllegalArgumentException ex) {
                                errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                            }
                            errors.merge(
                                    fieldName,
                                    errorMessage,
                                    (existingErrorMessage, newErrorMessage) ->
                                            existingErrorMessage + ", " + newErrorMessage);
                        });

        return handleExceptionInternalArgs(
                e, HttpHeaders.EMPTY, ErrorStatus.valueOf("_BAD_REQUEST"), request, errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();

        return handleExceptionInternalFalse(
                e,
                ErrorStatus._INTERNAL_SERVER_ERROR,
                HttpHeaders.EMPTY,
                ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(),
                request,
                e.getMessage());
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity onThrowException(
            GeneralException generalException, HttpServletRequest request) {
        return handleExceptionInternal(generalException, generalException.getCode(), null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e, BaseErrorCode code, HttpHeaders headers, HttpServletRequest request) {

        BaseResponse<Object> body =
                BaseResponse.onFailure(code, null);

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(e, body, headers, code.getReasonHttpStatus().getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(
            Exception e,
            ErrorStatus errorCommonStatus,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request,
            String errorPoint) {
        BaseResponse<Object> body =
                BaseResponse.onFailure(errorCommonStatus, errorPoint);
        return super.handleExceptionInternal(e, body, headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
            Exception e,
            HttpHeaders headers,
            ErrorStatus errorCommonStatus,
            WebRequest request,
            Map<String, String> errorArgs) {
        BaseResponse<Object> body =
                BaseResponse.onFailure(errorCommonStatus, errorArgs);
        return super.handleExceptionInternal(
                e, body, headers, errorCommonStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(
            Exception e, ErrorStatus errorCommonStatus, HttpHeaders headers, WebRequest request) {
        BaseResponse<Object> body =
                BaseResponse.onFailure(errorCommonStatus, null);
        return super.handleExceptionInternal(
                e, body, headers, errorCommonStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalMessage(
            Exception e, HttpHeaders headers, WebRequest request, String errorMessage) {
        ErrorStatus errorStatus = ErrorStatus._BAD_REQUEST;
        BaseResponse<String> body =
                BaseResponse.onFailure(errorStatus, errorMessage);

        return super.handleExceptionInternal(
                e, body, headers, errorStatus.getHttpStatus(), request);
    }
}