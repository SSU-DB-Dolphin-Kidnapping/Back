package Dolphin.ShoppingCart.domain.student.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.exception.GeneralException;

public class StudentException extends GeneralException {
    public StudentException(BaseErrorCode code) {
        super(code);
    }
}
