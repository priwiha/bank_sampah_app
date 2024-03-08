package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.response.ApiResponse;
import com.example.bank_sampah.utility.network.service.DataService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class MainActivity extends AppCompatActivity {

    //layout
    private LinearLayout login_layout;
    private LinearLayout register_layout;

    //button layout login
    private Button lg_login_btn;
    private Button lg_register_btn;
    private EditText lg_userid;
    private EditText lg_pass;


    //button layout register
    private Button rg_register_btn;
    private Button rg_back_btn;
    private EditText rg_userid;
    private EditText rg_name;
    private EditText rg_phone;
    private EditText rg_mail;
    private EditText rg_pass;
    private EditText rg_konfpass;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = MainActivity.class.getSimpleName();
    /////retrofit2
    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //layout
        login_layout = (LinearLayout) findViewById(R.id.layout_login_view);
        register_layout = (LinearLayout) findViewById(R.id.layout_register_view);

        //button layout login
        lg_login_btn = (Button) findViewById(R.id.LoginButtonLg);
        lg_register_btn = (Button) findViewById(R.id.RegisterButtonLg);
        lg_userid = (EditText) findViewById(R.id.editTextUserid);
        lg_pass = (EditText) findViewById(R.id.editTextPassword);

        //button layout login
        rg_register_btn = (Button) findViewById(R.id.RegisterButtonRg);
        rg_back_btn = (Button) findViewById(R.id.BackButtonRg);
        rg_userid = (EditText) findViewById(R.id.editTextUseridrg);
        rg_name = (EditText) findViewById(R.id.editTextNama);
        rg_phone = (EditText) findViewById(R.id.editTextPhone);
        rg_mail = (EditText) findViewById(R.id.editTextMail);
        rg_pass = (EditText) findViewById(R.id.editTextPass);
        rg_konfpass = (EditText) findViewById(R.id.editTextConfPassword);

        ///global var
        /*
        // Menambah data
        globalData.addData("Data Baru");

        // Mendapatkan data
        ArrayList<String> dataList = globalData.getDataList();

        // Menghapus data
        globalData.removeData("Data Lama");

        // Memperbarui data
        globalData.updateData(0, "Data Terupdate");*/
        //global var

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = MainActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        // methode ini berfungsi untuk mendeklarasikan widget yang ada di layout
        initComponents();


        //11.2.2024
        //Retrofit retrofit = new Retrofit.Builder().baseUrl(Endpoint.API_URL)

        register_layout.setVisibility(View.GONE);

        //show register layout
        lg_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_layout.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);
            }
        });

        rg_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_layout.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.GONE);
            }
        });


        lg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

                    dataService.LoginRequest(lg_userid.getText().toString(),
                            lg_pass.getText().toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Log.i("debug", "onResponse: Berhasil");
                                //Log.i("cek ",String.valueOf(response.body()));
                                loading.dismiss();
                                try {
                                    String ResponseString = response.body().string();
                                    // Ambil objek data dari JSON
                                    JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                    String MessageString = jsonRESULTS.get("message").toString();

                                    if (jsonRESULTS.has("data")) {
                                        JSONObject dataObject = jsonRESULTS.getJSONObject("data");


                                        System.out.println(MessageString.toString());

                                        // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                        JSONArray dataArray = new JSONArray();
                                        dataArray.put(dataObject);

                                        // Output array JSON
                                        System.out.println(dataArray.toString());

                                        Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                        if (dataArray.length() > 0) {
                                            getDataJsonLogin(dataArray);
                                            System.out.println(MessageString);

                                            lg_userid.setText("");
                                            lg_pass.setText("");
                                            rg_userid.setText("");
                                            rg_name.setText("");
                                            rg_phone.setText("");
                                            rg_mail.setText("");
                                            rg_pass.setText("");
                                            rg_konfpass.setText("");
                                        } else {
                                            System.out.println(MessageString);
                                        }
                                    }
                                    Toast.makeText(mContext,MessageString,Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.i("debug", "onResponse: Tidak Berhasil");
                                // Tanggapan HTTP tidak berhasil
                                try {
                                    String errorBody = response.errorBody().string();
                                    // Tangani errorBody sesuai kebutuhan
                                    Toast.makeText(mContext,errorBody,Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                loading.dismiss();
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
        });


        rg_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serialCode = generateSerialCode();
                if (rg_pass.getText().toString().trim().equals(rg_konfpass.getText().toString().trim())){

                    ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

                    dataService.registerRequest(rg_userid.getText().toString(),
                            serialCode,
                            rg_name.getText().toString(),
                            rg_mail.getText().toString(),
                            rg_phone.getText().toString(),
                            rg_konfpass.getText().toString(),"2").
                            enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        Log.i("debug", "onResponse: Berhasil");
                                        //Log.i("cek ",String.valueOf(response.body()));
                                        loading.dismiss();
                                        try {
                                            String ResponseString = response.body().string();
                                            // Ambil objek data dari JSON
                                            JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                            String MessageString = jsonRESULTS.get("message").toString();

                                            if (jsonRESULTS.has("data")) {
                                                JSONObject dataObject = jsonRESULTS.getJSONObject("data");


                                                System.out.println(MessageString.toString());

                                                // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                                JSONArray dataArray = new JSONArray();
                                                dataArray.put(dataObject);

                                                // Output array JSON
                                                System.out.println(dataArray.toString());

                                                Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                                if (dataArray.length() > 0) {
                                                    getDataJson(dataArray);
                                                    System.out.println(MessageString);

                                                    lg_userid.setText("");
                                                    lg_pass.setText("");
                                                    rg_userid.setText("");
                                                    rg_name.setText("");
                                                    rg_phone.setText("");
                                                    rg_mail.setText("");
                                                    rg_pass.setText("");
                                                    rg_konfpass.setText("");
                                                } else {
                                                    System.out.println(MessageString);
                                                }
                                            }
                                            Toast.makeText(mContext,MessageString,Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.i("debug", "onResponse: Tidak Berhasil");
                                        // Tanggapan HTTP tidak berhasil
                                        try {
                                            String errorBody = response.errorBody().string();
                                            // Tangani errorBody sesuai kebutuhan
                                            Toast.makeText(mContext,errorBody,Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        loading.dismiss();


                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                                    loading.dismiss();

                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                    /*dataService.registerRequest(rg_userid.getText().toString(),
                            serialCode,
                            rg_name.getText().toString(),
                            rg_mail.getText().toString(),
                            rg_phone.getText().toString(),
                            rg_konfpass.getText().toString(),"2"*//*,"Y"*//*).
                            enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        Log.i("debug", "onResponse: Berhasil");
                                        //Log.i("cek ",String.valueOf(response.body()));
                                        loading.dismiss();
                                        try {

                                            String responseBodyString = response.body().string();
                                            ApiResponse apiResponse = new Gson().fromJson(responseBodyString, ApiResponse.class);
                                            boolean success = apiResponse.isSuccess();
                                            String message = apiResponse.getMessage();
                                            System.out.println("Success: " + success);
                                            System.out.println("Message: " + message);

                                            // Ambil objek data dari JSON
                                            JSONObject jsonRESULTS = new JSONObject(responseBodyString);
                                            // Periksa apakah kunci "data" ada di dalam objek JSON
                                            if (jsonRESULTS.has("data")) {
                                                JSONObject dataObject = jsonRESULTS.getJSONObject("data");

                                                // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                                JSONArray dataArray = new JSONArray();
                                                dataArray.put(dataObject);

                                                // Output array JSON
                                                //System.out.println(dataArray.toString());

                                                Log.e("cek panjang json array",String.valueOf(dataArray.length()));
                                                if (dataArray.length()>0)
                                                {
                                                    getDataJson(dataArray);
                                                }

                                                login_layout.setVisibility(View.VISIBLE);
                                                register_layout.setVisibility(View.GONE);

                                                lg_userid.setText("");
                                                lg_pass.setText("");
                                                rg_userid.setText("");
                                                rg_name.setText("");
                                                rg_phone.setText("");
                                                rg_mail.setText("");
                                                rg_pass.setText("");
                                                rg_konfpass.setText("");
                                            }
                                            else{
                                                Toast.makeText(mContext,
                                                        message,
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        Toast.makeText(mContext,
                                                "onResponse: Tidak Berhasil",
                                                Toast.LENGTH_SHORT).show();
                                        Log.i("debug", "onResponse: Tidak Berhasil");
                                        loading.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                                    loading.dismiss();
                                    Toast.makeText(mContext, "onFailure: ERROR > " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });*/

            }
            else {
                    Toast.makeText(mContext, "Mohon Cek Kembali Data !", Toast.LENGTH_SHORT).show();
                    Log.e("cek code",serialCode);
                }
        }});

    }

    // Method to generate a serial code
    public static String generateSerialCode() {
        // Using UUID to create a unique code
        String serialCode = UUID.randomUUID().toString();

        // Removing "-" characters and trimming to the desired length
        serialCode = "MBD"+serialCode.replaceAll("-", "").substring(0, 5);

        return serialCode;
    }

    private void getDataJsonLogin(JSONArray dataArray) {
        String id ="";
        String userid = "";
        String role = "";
        String name = "";
        try{
            for (int x = 0; x < dataArray.length(); x++)
            {
                JSONObject child = dataArray.getJSONObject(x);

                id = child.getString("id");
                userid = child.getString("userid");
                name = child.getString("name");
                role = child.getString("role");

                //Toast.makeText(MainActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                login_layout.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.GONE);
            }

            /////////////GlobalVariables
            globalData.addData(userid);
            /////////GlobalVariables

            if (role.equals("1")) {

                Intent i = new Intent(MainActivity.this, HomeAdminActivity.class);
                // Membuat objek Bundle
                Bundle bundle = new Bundle();
                // Menambahkan data ke Bundle
                bundle.putString("id", id);
                bundle.putString("userid", userid);
                bundle.putString("name", name);
                // Menambahkan Bundle ke Intent
                i.putExtras(bundle);
                startActivity(i);
                finish();
            } else if (role.equals("2")) {
                //GlobalVariables(id,userid);
                Intent i = new Intent(MainActivity.this, DashboardMemberActivity.class);
                // Membuat objek Bundle
                Bundle bundle = new Bundle();
                // Menambahkan data ke Bundle
                bundle.putString("id", id);
                bundle.putString("userid", userid);
                bundle.putString("name", name);
                // Menambahkan Bundle ke Intent
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getDataJson(JSONArray jsonRESULTS) {
        try{
            for (int x = 0; x < jsonRESULTS.length(); x++)
            {
                JSONObject child = jsonRESULTS.getJSONObject(x);

                String id = child.getString("id");
                String userid = child.getString("userid");

                Toast.makeText(MainActivity.this, "Member "+userid+" Berhasil terdaftar dengan no "+id, Toast.LENGTH_SHORT).show();

            }
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void requestRegister() {


    }
    private void initComponents() {

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




}