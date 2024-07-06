package dev.coder.booker.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "Not Implemented"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "Forbidden: User Account is Locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "Forbidden: User Account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.FORBIDDEN, "User Email or Password is incorrect"),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Current Password incorrect"),
    NEW_PASSWORD_MISMATCH(301, HttpStatus.BAD_REQUEST, "New Password does not match"),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    ErrorCode(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
