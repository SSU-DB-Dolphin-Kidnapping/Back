package Dolphin.ShoppingCart.global.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.error.code.status.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}