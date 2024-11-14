package com.huytran.PetCareService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1009, "Permission not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1010, "Role not existed", HttpStatus.NOT_FOUND),
    PERMISSION_GROUP_NOT_EXISTED(1011, "Permission group not existed", HttpStatus.NOT_FOUND),
    PERMISSION_GROUP_PERMISSION_NOT_EXISTED(1012, "Permission group permission not existed", HttpStatus.NOT_FOUND),
    ROLE_PERMISSION_GROUP_NOT_EXISTED(1013, "Role permission group not existed", HttpStatus.NOT_FOUND),
    INVALID_INPUT(1014, "Permission group name cannot be null or empty", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
