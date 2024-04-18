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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKet1() {
        return ket1;
    }

    public void setKet1(String ket1) {
        this.ket1 = ket1;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private String idtrx;
    private String tgl;
    private String name;
    private String type;
    private String ket;
    private String ket1;

    public HistoriTransactionModel(String idtrx, String tgl, String name, String type, String ket, String ket1) {
        this.idtrx = idtrx;
        this.tgl = tgl;
        this.name = name;
        this.type = type;
        this.ket = ket;
        this.ket1 = ket1;
    }
}
