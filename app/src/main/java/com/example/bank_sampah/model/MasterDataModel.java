package com.example.bank_sampah.model;

public class MasterDataModel {
    public MasterDataModel(String id, String name, String satuan) {
        this.id = id;
        this.name = name;
        this.satuan = satuan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    private String id;
    private String name;


    private String satuan;
}
