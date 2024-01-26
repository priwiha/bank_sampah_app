package com.example.bank_sampah.model;

public class AdminMenuModel {
    private String idmenu;
    private String namamenu;
    private String image;

    public String getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(String idmenu) {
        this.idmenu = idmenu;
    }

    public String getNamamenu() {
        return namamenu;
    }

    public void setNamamenu(String namamenu) {
        this.namamenu = namamenu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public AdminMenuModel(String idmenu, String namamenu, String image) {
        this.idmenu = idmenu;
        this.namamenu = namamenu;
        this.image = image;
    }

}
