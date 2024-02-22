package com.example.bank_sampah.utility;

import java.util.ArrayList;

public class GlobalData {
    private static GlobalData instance;
    //private String globalVariable;
    private ArrayList<String> dataList;

    private GlobalData() {
        // Constructor privat agar tidak dapat diinstansiasi secara langsung
        dataList = new ArrayList<>();
    }

    public static synchronized GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public ArrayList<String> getDataList() {
        return dataList;
    }

    public void addData(String data) {
        dataList.add(data);
    }

    public void removeData(String data) {
        dataList.remove(data);
    }

    public void updateData(int index, String newData) {
        if (index >= 0 && index < dataList.size()) {
            dataList.set(index, newData);
        }
    }

    /*public String getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(String value) {
        globalVariable = value;
    }

    public void clearGlobalVariable() {
        globalVariable = null;
    }*/

}
