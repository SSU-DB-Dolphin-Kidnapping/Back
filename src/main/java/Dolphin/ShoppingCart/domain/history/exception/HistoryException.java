package Dolphin.ShoppingCart.domain.history.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.exception.GeneralException;

public class HistoryException extends GeneralException {
    public HistoryException(BaseErrorCode code) {
        super(code);
    }
}
