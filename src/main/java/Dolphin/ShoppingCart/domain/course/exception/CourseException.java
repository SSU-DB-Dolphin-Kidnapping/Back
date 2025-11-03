package Dolphin.ShoppingCart.domain.course.exception;

import Dolphin.ShoppingCart.global.error.code.status.BaseErrorCode;
import Dolphin.ShoppingCart.global.exception.GeneralException;

public class CourseException extends GeneralException {
    public CourseException(BaseErrorCode code) {
        super(code);
    }
}
