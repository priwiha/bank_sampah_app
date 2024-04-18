package com.example.bank_sampah.utility.network.service;

import android.os.Parcelable;

import com.example.bank_sampah.utility.network.response.ApiResponse;

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
                                       @Field("membercode") String code,
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

    @FormUrlEncoded
    @POST("user/chpass")
    Call<ResponseBody> ChangePass(@Field("userid") String userid,
                                    @Field("pass_old") String old_pass,
                                  @Field("pass_new") String new_pass);

    @FormUrlEncoded
    @POST("user/reset_pass")
    Call<ResponseBody> ResetPass(@Field("password") String password,
                                  @Field("userid") String userid);
    ////////////CRUD PROCESS USER

    ///////////CRUD PROSES MEMBER
    @FormUrlEncoded
    @POST("member/updatemember")
    Call<ResponseBody> UpdateMember(@Field("userid") String userid,
                                    @Field("name") String pass,
                                    @Field("aktif") String aktif,
                                    @Field("notelp") String notelp,
                                    @Field("mail") String mail,
                                    @Field("chuserid") String chuserid);
    ///////////

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


    ////////////CRUD PROCESS PRICE
    @FormUrlEncoded
    @POST("price/create")
    Call<ResponseBody> CreatePrice(@Field("idcategory") String id,
                                      @Field("price") String name,
                                      @Field("inuserid") String userid);

    @GET("price/index")
    Call<ResponseBody> PricelistRequestAll();

    @FormUrlEncoded
    @POST("price/get_price_date")
    Call<ResponseBody> GetPrice_Date(@Field("date") String date);


    ////////////CRUD PROCESS PRICE


    ////////////CRUD PROCESS MEMBER
    @FormUrlEncoded
    @POST("member/getbycode")
    Call<ResponseBody> GetMemberByCode(@Field("membercode") String id);

    @GET("member/index")
    Call<ResponseBody> MemberRequestAll();
    @FormUrlEncoded
    @POST("member/updatemember")
    Call<ResponseBody> MemberUpdate(@Field("userid") String id,
                                    @Field("name") String name,
                                    @Field("notelp") String notelp,
                                    @Field("mail") String mail,
                                    @Field("chuserid") String chuserid,
                                    @Field("aktif") String aktif);

    @FormUrlEncoded
    @POST("member/chstatus_member")
    Call<ResponseBody> MemberActivate(@Field("membercode") String id,
                                    @Field("chuserid") String chuserid);

    ////////////CRUD PROCESS PRICE


    ///////////TRANSAKSI
    /////TIMBANG
    @GET("transaksi/getprice")
    Call<ResponseBody> GetCategoryPrice();

    @FormUrlEncoded
    @POST("transaksi/timbang")
    Call<ResponseBody> CreateTimbang(@Field("membercode") String membercode,
                                    @Field("idcategory") String idcategory,
                                    @Field("iduom") String iduom,
                                    @Field("qty") String bobot,
                                    @Field("price") String harga,
                                    @Field("userid") String userid);

    @FormUrlEncoded
    @POST("transaksi/timbang_list")
    Call<ResponseBody> GetTimbang_byMemberCode(@Field("membercode") String membercode);
    @FormUrlEncoded
    @POST("transaksi/timbang_list_date")
    Call<ResponseBody> GetTimbang_byMemberCode_Date(@Field("membercode") String membercode,
                                                    @Field("date") String date);


    //////REDEEM
    @FormUrlEncoded
    @POST("transaksi/redeem_adm")
    Call<ResponseBody> CreateRedeem(@Field("membercode") String membercode,
                                     @Field("redeemamt") String redeemamt,
                                     @Field("inuserid") String inuserid);


    @FormUrlEncoded
    @POST("transaksi/redeem_mem")
    Call<ResponseBody> CreateRedeemMember(@Field("membercode") String membercode,
                                    @Field("redeemamt") String redeemamt,
                                    @Field("inuserid") String inuserid);


    @FormUrlEncoded
    @POST("redeem/approve_redeem")
    Call<ResponseBody> ApproveRedeem(@Field("idredeem") String idredeem,
                                           @Field("membercode") String membercode,
                                          @Field("chuserid") String chuserid);


    @FormUrlEncoded
    @POST("redeem/redeem_list")
    Call<ResponseBody> GetReedem_byMemberCode(@Field("membercode") String membercode);

    @FormUrlEncoded
    @POST("redeem/redeem_list_date")
    Call<ResponseBody> GetReedem_byMemberCode_Date(@Field("membercode") String membercode,
                                                    @Field("date") String date);
    ///////////TRANSAKSI

    ///REDEEM
    @FormUrlEncoded
    @POST("redeem/redeem_wait_list")
    Call<ResponseBody> GetWaitReedem_byMemberCode(@Field("membercode") String membercode);

    @FormUrlEncoded
    @POST("redeem/redeem_wait_list_date")
    Call<ResponseBody> GetWaitReedem_byMemberCode_Date(@Field("membercode") String membercode,
                                                   @Field("date") String date);
}
