package com.assessment.vending.exception;

import com.assessment.vending.dto.response.BaseStandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.assessment.vending.util.ResponseCode.GENERIC_ERROR;
import static com.assessment.vending.util.ResponseCode.VALIDATION_ERROR;


@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseStandardResponse<String> handleAllException(Exception ex, WebRequest request) {
        log.error("An Exception Occurred :: ", ex);
        return new BaseStandardResponse<>(GENERIC_ERROR);
    }

    @ExceptionHandler(VendingMachineException.class)
    public final ResponseEntity<Object> handleServiceException(VendingMachineException ex, WebRequest request) {
        log.error("BookStoreException :: ", ex);
        return ResponseEntity.ok(new BaseStandardResponse<>(ex.getResponseCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseStandardResponse<>(VALIDATION_ERROR, errors.toString()));
    }
}
