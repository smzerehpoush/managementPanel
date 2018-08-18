package com.nrdc.adminPanel.jsonModel;

import java.util.List;


public class VTResponse<T> {

    private int resultCode;
    private String resultMessage;
    private T response;
    private List<T> responseList;

    public VTResponse() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public List<T> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<T> responseList) {
        this.responseList = responseList;
    }

    public VTResponse<T> getNOKExceptions(String exceptionMessage) {
        VTResponse<T> response = new VTResponse();
        response.setResultCode(-1);
        response.setResultMessage(exceptionMessage);
        return response;
    }
}
