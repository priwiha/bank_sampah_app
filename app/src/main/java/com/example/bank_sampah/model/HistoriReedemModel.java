package com.example.bank_sampah.model;

public class HistoriReedemModel {


    private String id;
    private String tgl;
    private String amt;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



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

    public HistoriReedemModel(String id,String tgl, String amt, String status) {
        this.id = id;
        this.tgl = tgl;
        this.amt = amt;
        this.status = status;
    }

}
