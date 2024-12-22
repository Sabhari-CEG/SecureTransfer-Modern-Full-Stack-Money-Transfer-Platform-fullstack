package com.sabhari.app.transaction.Transaction;

import java.sql.Timestamp;

public class LogResponse {
    private Timestamp time;
    private String type;
    private float amount;
    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
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
}
