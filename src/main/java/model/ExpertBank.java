package model;

import java.io.Serializable;

public class ExpertBank implements Serializable {
    private static final long serialVersionUID = 1L;
    private int eBankID;
    private String bankAccount;
    private String bankName;
    private String binCode;
    private int expertID;

    public ExpertBank(String bankName, String bankAccount, String binCode, int expertID) {
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.binCode = binCode;
        this.expertID = expertID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public int geteBankID() {
        return eBankID;
    }

    public void seteBankID(int eBankID) {
        this.eBankID = eBankID;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }
}
