package com.example.bank_sampah.utility.network.response;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

public class ApiResponse {
    private boolean success;
    private String message;
    private JSONObject data;



    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


    public JSONObject getData() {
        return data;
    }
    

    public JSONObject getJsonObject() {
        return getData();
    }
}
