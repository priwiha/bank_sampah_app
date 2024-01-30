package com.example.bank_sampah.model;

public class MasterPriceModel {

    private String id;
    private String kategori;
    private String price;
    private String satuan;
    private String aktif;
    private String tglins;

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getAktif() {
        return aktif;
    }

    public void setAktif(String aktif) {
        this.aktif = aktif;
    }

    public String getTglins() {
        return tglins;
    }

    public void setTglins(String tglins) {
        this.tglins = tglins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MasterPriceModel(String kategori, String price, String satuan, String aktif, String tglins,String id) {
        this.kategori = kategori;
        this.price = price;
        this.satuan = satuan;
        this.aktif = aktif;
        this.tglins = tglins;
        this.id = id;
    }
}
