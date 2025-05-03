package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WithdrawRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private int requestID;
    private int expertID;
    private double amount;
    private LocalDateTime requestDate;
    private String status;
    private String note;
    private int eBankID;
    private String bankName; // For display purposes
    private String bankAccount; // For display purposes
    private java.sql.Timestamp formattedDate; // For JSP display

    public WithdrawRequest() {
    }

    public WithdrawRequest(int expertID, double amount, String note, int eBankID) {
        this.expertID = expertID;
        this.amount = amount;
        this.note = note;
        this.eBankID = eBankID;
        this.requestDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int geteBankID() {
        return eBankID;
    }

    public void seteBankID(int eBankID) {
        this.eBankID = eBankID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public java.sql.Timestamp getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(java.sql.Timestamp formattedDate) {
        this.formattedDate = formattedDate;
    }
} 