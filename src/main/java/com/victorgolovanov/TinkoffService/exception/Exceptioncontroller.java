package com.victorgolovanov.TinkoffService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class Exceptioncontroller extends ResponseEntityExceptionHandler {

    @ExceptionHandler({StockNotfoundException.class})
    public ResponseEntity<ErrorDto> handleStockNotFound (Exception ex) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PriceNotFoundException.class})
    public ResponseEntity<ErrorDto> handlePriceNotFound (Exception ex) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }
}
