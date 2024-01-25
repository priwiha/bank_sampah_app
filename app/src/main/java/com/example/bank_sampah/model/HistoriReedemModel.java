package com.example.bank_sampah.model;

public class HistoriReedemModel {
    private String tgl;
    private String amt;

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public HistoriReedemModel(String tgl, String amt) {
        this.tgl = tgl;
        this.amt = amt;
    }

}
