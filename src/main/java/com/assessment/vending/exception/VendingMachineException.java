package com.assessment.vending.exception;

import com.assessment.vending.util.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendingMachineException extends Exception {

    private ResponseCode responseCode;

    public VendingMachineException(ResponseCode responseCode,
                                   String message, Throwable cause) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public VendingMachineException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public VendingMachineException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
}
