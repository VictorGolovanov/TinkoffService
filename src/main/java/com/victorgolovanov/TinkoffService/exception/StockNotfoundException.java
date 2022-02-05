package com.victorgolovanov.TinkoffService.exception;

public class StockNotfoundException extends RuntimeException {
    public StockNotfoundException(String message) {
        super(message);
    }
}
