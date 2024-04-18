package com.example.bank_sampah.model;

public class MasterDataModel {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MasterDataModel(String id, String name, String satuan, String satuan_nm, String type) {
        this.id = id;
        this.name = name;
        this.satuan = satuan;
        this.satuan_nm = satuan_nm;
        this.type = type;
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
    private String satuan_nm;
    private String type;

    public String getSatuan_nm() {
        return satuan_nm;
    }

    public void setSatuan_nm(String satuan_nm) {
        this.satuan_nm = satuan_nm;
    }


}
