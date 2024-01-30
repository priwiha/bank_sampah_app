package com.example.bank_sampah.model;

import com.example.bank_sampah.adapter.PriceAdapter;

public class HistoriTransactionModel {
    public String getIdtrx() {
        return idtrx;
    }

    public void setIdtrx(String idtrx) {
        this.idtrx = idtrx;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    private String idtrx;
    private String tgl;
    private String ket;

    public HistoriTransactionModel(String idtrx, String tgl, String ket) {
        this.idtrx = idtrx;
        this.tgl = tgl;
        this.ket = ket;
    }
}
