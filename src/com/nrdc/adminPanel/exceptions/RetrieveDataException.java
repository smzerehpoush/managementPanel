package com.nrdc.adminPanel.exceptions;

public class RetrieveDataException extends Exception {

    public RetrieveDataException() {
    }

    public RetrieveDataException(String message) {
        super(message);
    }

    public RetrieveDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetrieveDataException(Throwable cause) {
        super(cause);
    }

    public RetrieveDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
