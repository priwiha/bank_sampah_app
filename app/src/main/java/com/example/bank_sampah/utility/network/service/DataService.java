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

    ////////////CRUD PROCESS USER
    @FormUrlEncoded
    @POST("user/create")
    Call<ResponseBody> registerRequest(@Field("userid") String userid,
                                       @Field("name") String name,
                                       @Field("email") String mail,
                                       @Field("phone") String telpon,
                                       @Field("password") String pass,
                                       @Field("role") String role/*,
                                       @Field("Status") String status*/);
    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> LoginRequest(@Field("userid") String userid,
                                    @Field("password") String pass);
    ////////////CRUD PROCESS USER

    ////////////CRUD PROCESS UOM
    @FormUrlEncoded
    @POST("uom/getuom")
    Call<ResponseBody> UomRequest(@Field("iduom") String id);
    //@FormUrlEncoded
    @GET("uom/index")
    Call<ResponseBody> UomRequestAll();

    ////////////CRUD PROCESS CATEGORY
    @FormUrlEncoded
    @POST("category/create")
    Call<ResponseBody> CreateCategory(@Field("namecategory") String name,
                                      @Field("iduom") String iduom,
                                      @Field("inuserid") String userid);
    @GET("category/index")
    Call<ResponseBody> CategoryRequestAll();
    @FormUrlEncoded
    @POST("category/categorychange")
    Call<ResponseBody> ChangeCategory(@Field("idcategory") String id,
                                      @Field("namecategory") String name,
                                      @Field("iduom") String iduom,
                                      @Field("chuserid") String chuserid);
    ////////////CRUD PROCESS CATEGORY

}
