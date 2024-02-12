package com.example.bank_sampah.utility.network.service;

import android.os.Parcelable;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataService extends Parcelable {

    // Fungsi ini untuk memanggil API http://192.168.88.20/latihan1/login.php
    // penamaan link sesuai dengan localhost masing-masing
    @FormUrlEncoded
    @POST("/api/register")
    Call<ResponseBody> registerRequest(@Field("userid") String userid,
                                       @Field("name") String name,
                                       @Field("email") String mail,
                                       @Field("phone") String telpon,
                                       @Field("pass") String pass,
                                       @Field("role") String role,
                                       @Field("Status") String status);



}
