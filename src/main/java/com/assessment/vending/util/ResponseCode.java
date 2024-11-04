package com.assessment.vending.util;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum ResponseCode {

    TIMEOUT("-1", "Timeout"),
    SUCCESS("00", "Successful"),
    DATABASE_ERROR("01", "Database Error"),
    UNABLE_TO_LOCATE_RECORD("02", "Cannot Locate Record"),
    GENERIC_ERROR("03", "Something went wrong while trying to process your request"),
    SAVE_TO_DATABASE_ERROR("04", "Error Occurred While Saving To The Database"),
    WEBSERVICE_CALL_FAILED("05", "API call failed"),
    VALIDATION_ERROR("06", "Validation Error"),
    OUT_OF_STOCK("07", "Available quantity is less than what is requested");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ResponseCode getResponseByCode(String code){
        return Arrays.stream(ResponseCode.values()).filter(respCode -> respCode.getCode().equals(code)).findFirst().orElse(null);
    }

}
