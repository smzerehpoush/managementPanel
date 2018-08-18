package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestGetTicketsByQuantity {
    private String token;
    private int quantity;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;

    }

    @Override
    public String toString() {
        return "RequestGetTicketsByQuantity{" +
                "token='" + token + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
