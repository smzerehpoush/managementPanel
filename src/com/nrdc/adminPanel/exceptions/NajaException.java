package com.nrdc.adminPanel.exceptions;

import com.nrdc.adminPanel.helper.Constants;

public class NajaException extends Exception {
    public NajaException() {
        super(Constants.CAN_NOT_RETRIEVE_DATA_FROM_NAJA);
    }

    public NajaException(String message) {
        super(message);
    }
}
