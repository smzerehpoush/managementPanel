package com.nrdc.managementPanel.jsonModel;

public class StandardResponse<T> {

    private int resultCode;
    private String resultMessage;
    private T response;

    public StandardResponse() {
    }

    public static StandardResponse getNOKExceptions(String exceptionMessage) {
        StandardResponse response = new StandardResponse();
        response.setResultCode(-1);
        response.setResultMessage(exceptionMessage);
        return response;
    }

    public static StandardResponse getOKResponse() {
        StandardResponse response = new StandardResponse();
        response.setResultCode(1);
        response.setResultMessage("OK");
        return response;
    }

    public static StandardResponse getNOKExceptions(Exception ex) {
        return getNOKExceptions(ex.getMessage());
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StandardResponse{");
        sb.append("resultCode=").append(resultCode);
        sb.append(", resultMessage='").append(resultMessage).append('\'');
        sb.append(", response=").append(response);
        sb.append('}');
        return sb.toString();
    }
}
