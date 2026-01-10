package com.nims.auth.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_EXISTS(10005, "ERR_EMAIL_EXISTS"),
    CONTACT_EXISTS(10006, "ERR_CONTACT_EXISTS"),
    DOCTOR_NOT_FOUND(10007, "ERR_DOCTOR_NOT_FOUND"),
    PERSON_PROFILE_NOT_FOUND(10008, "ERR_PERSON_PROFILE_NOT_FOUND"),
    STAFF_PROFILE_NOT_FOUND(10009, "ERR_STAFF_PROFILE_NOT_FOUND"),
    DOCTOR_PROFILE_NOT_FOUND(10010, "ERR_DOCTOR_PROFILE_NOT_FOUND"),
    USER_NOT_FOUND(10011, "ERR_USER_NOT_FOUND");

    private final int numericCode;
    private final String stringCode;

    ErrorCode(int numericCode, String stringCode) {
        this.numericCode = numericCode;
        this.stringCode = stringCode;
    }

}
