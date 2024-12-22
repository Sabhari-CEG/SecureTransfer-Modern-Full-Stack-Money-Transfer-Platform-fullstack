package com.sabhari.app.transaction.Transaction;

public class TransactionResponse {
    private String type;
    private float amount;
    private boolean success;
    private String receiverMobile;
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getReceiverMobile() {
        return receiverMobile;
    }
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
