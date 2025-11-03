package Dolphin.ShoppingCart.domain.academic.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.exception.GeneralException;

public class AcademicException extends GeneralException {
    public AcademicException(BaseErrorCode code) {
        super(code);
    }
}
