package com.example.bank_sampah.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.activity.HistoryReedemAdminActivity;
import com.example.bank_sampah.activity.TransaksiReedemActivity;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.response.ApiResponse;
import com.example.bank_sampah.utility.network.service.DataService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDialog {

    /////retrofit2
    private DataService dataService;
    private static final String TAG = ViewDialog.class.getSimpleName();
    /////retrofit2

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    String id_member = dataList.get(1);
    //global var

    public void showDialog(Activity activity,String role) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_input_amt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText amt = (EditText) dialog.findViewById(R.id.input_reedem);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = activity;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        Button mDialogNo = dialog.findViewById(R.id.frmNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button mDialogOk = dialog.findViewById(R.id.frmOk);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create_trx_redeem(id_member,role,amt.getText().toString(),id_member,mContext);

                Toast.makeText(activity.getApplicationContext(),"Okay "+amt.getText().toString() ,Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void create_trx_redeem(String id_member,String role, String inputreedem, String id_member1, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, price, Toast.LENGTH_SHORT).show();
        //Call<ApiResponse> call = dataService.CreateTimbang(id_member,kat_id,iduom,bobot,harga, userid);

        if (role == "2") {
            dataService.CreateRedeemMember(id_member, inputreedem, userid).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBodyString = response.body().string();
                            ApiResponse apiResponse = new Gson().fromJson(responseBodyString, ApiResponse.class);

                            if (apiResponse != null && apiResponse.isSuccess()) {
                                // Tanggapan sukses, lakukan sesuatu di sini
                                Log.i("debug", "onResponse: Berhasil");
                                //Log.i("cek ",String.valueOf(response.body()));
                                loading.dismiss();
                                try {
                                    boolean success = apiResponse.isSuccess();
                                    String message = apiResponse.getMessage();
                                    System.out.println("Success: " + success);
                                    System.out.println("Message: " + message);

                                    // Ambil objek data dari JSON
                                    JSONObject jsonRESULTS = new JSONObject(responseBodyString);
                                    if (jsonRESULTS.has("data")) {
                                        JSONObject dataObject = jsonRESULTS.getJSONObject("data");

                                        // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                        JSONArray dataArray = new JSONArray();
                                        dataArray.put(dataObject);

                                        // Output array JSON
                                        System.out.println(dataArray.toString());

                                        Log.e("panjang json array satuan", String.valueOf(dataArray.length()));
                                        if (dataArray.length() > 0) {
                                            getResponRedeemJson(dataArray, mContext,role);
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                loading.dismiss();
                                // Tanggapan API sukses, tetapi ada kesalahan aplikasi
                                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Unknown error";
                                // Tampilkan errorMessage atau lakukan tindakan lain
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        loading.dismiss();
                        // Tanggapan HTTP tidak berhasil
                        try {
                            String errorBody = response.errorBody().string();
                            // Tangani errorBody sesuai kebutuhan
                            Toast.makeText(mContext, errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Kesalahan koneksi atau respons tidak berhasil
                    loading.dismiss();
                    t.printStackTrace();

                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            dataService.CreateRedeem(id_member,inputreedem,userid).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBodyString = response.body().string();
                            ApiResponse apiResponse = new Gson().fromJson(responseBodyString, ApiResponse.class);

                            if (apiResponse != null && apiResponse.isSuccess()) {
                                // Tanggapan sukses, lakukan sesuatu di sini
                                Log.i("debug", "onResponse: Berhasil");
                                //Log.i("cek ",String.valueOf(response.body()));
                                loading.dismiss();
                                try {
                                    boolean success = apiResponse.isSuccess();
                                    String message = apiResponse.getMessage();
                                    System.out.println("Success: " + success);
                                    System.out.println("Message: " + message);

                                    // Ambil objek data dari JSON
                                    JSONObject jsonRESULTS = new JSONObject(responseBodyString);
                                    if (jsonRESULTS.has("data")) {
                                        JSONObject dataObject = jsonRESULTS.getJSONObject("data");

                                        // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                        JSONArray dataArray = new JSONArray();
                                        dataArray.put(dataObject);

                                        // Output array JSON
                                        System.out.println(dataArray.toString());

                                        Log.e("panjang json array satuan",String.valueOf(dataArray.length()));
                                        if (dataArray.length()>0)
                                        {
                                            getResponRedeemJson(dataArray, mContext,role);
                                            //getResponRedeemJson(dataArray);
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                loading.dismiss();
                                // Tanggapan API sukses, tetapi ada kesalahan aplikasi
                                String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Unknown error";
                                // Tampilkan errorMessage atau lakukan tindakan lain
                                Toast.makeText(mContext,errorMessage,Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        loading.dismiss();
                        // Tanggapan HTTP tidak berhasil
                        try {
                            String errorBody = response.errorBody().string();
                            // Tangani errorBody sesuai kebutuhan
                            Toast.makeText(mContext,errorBody,Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Kesalahan koneksi atau respons tidak berhasil
                    loading.dismiss();
                    t.printStackTrace();

                    Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getResponRedeemJson(JSONArray dataArray, Context mContext,String role) {
        if (dataArray.length() > 0) {
            String idredeem="";
            String redeemamt="";
            String membercode="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    membercode = child.getString("membercode");
                    idredeem = child.getString("idredeem");
                    redeemamt = child.getString("redeemamt");



                }
                Log.e( "id transaksi"+idredeem,"member "+membercode+" berhasil diproses redeem "+redeemamt);

                if (role=="2") {
                    Toast.makeText(mContext,
                            "member " + membercode + " berhasil melakukan pengajuan redeem " + redeemamt, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext,
                            "member "+membercode+" berhasil diproses redeem "+redeemamt, Toast.LENGTH_SHORT).show();
                    Log.e( "id transaksi"+idredeem,"member "+membercode+" berhasil diproses redeem "+redeemamt);
                }
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
