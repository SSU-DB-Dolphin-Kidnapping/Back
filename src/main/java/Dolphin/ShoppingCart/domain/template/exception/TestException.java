package Dolphin.ShoppingCart.domain.template.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.exception.GeneralException;

public class TestException extends GeneralException {
    public TestException(BaseErrorCode code) {
        super(code);
    }
}
