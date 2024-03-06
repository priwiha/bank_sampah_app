package com.example.bank_sampah.utility.network;

import com.example.bank_sampah.utility.network.response.RetrofitClient;
import com.example.bank_sampah.utility.network.service.DataService;

public class UtilsApi {
    public static final String BASE_URL_API="http://172.18.13.61/CI-4.4.5/public/";//kantor
    //public static final String BASE_URL_API="http://192.168.1.9/ci4/public/";//rumah

    public static DataService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(DataService.class);
    }
}
