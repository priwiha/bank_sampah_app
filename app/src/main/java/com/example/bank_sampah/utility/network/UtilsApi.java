package com.example.bank_sampah.utility.network;

import com.example.bank_sampah.utility.network.response.RetrofitClient;
import com.example.bank_sampah.utility.network.service.DataService;

public class UtilsApi {
    public static final String BASE_URL_API="http://192.168.1.9:8000";

    public static DataService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(DataService.class);
    }
}
