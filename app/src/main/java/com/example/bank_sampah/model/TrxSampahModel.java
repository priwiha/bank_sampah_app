package com.example.bank_sampah.model;

public class TrxSampahModel {
    private String kategori;
    private String bobot;
    private String rupiah;
    private String tanggal;


    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getBobot() {
        return bobot;
    }

    public void setBobot(String bobot) {
        this.bobot = bobot;
    }

    public String getRupiah() {
        return rupiah;
    }

    public void setRupiah(String rupiah) {
        this.rupiah = rupiah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }


    public TrxSampahModel(String kategori, String bobot, String rupiah, String tanggal) {
        this.kategori = kategori;
        this.bobot = bobot;
        this.rupiah = rupiah;
        this.tanggal = tanggal;
    }



}
