package com.victorgolovanov.TinkoffService.exception;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
