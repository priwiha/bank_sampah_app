package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TransaksiReedemActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView tvNama;
    private TextView tvtelp;
    private TextView tvdate;
    private TextView tvamt;

    private TextView btn_back;

    private EditText inputreedem;
    private TextView save;
    /////retrofit2
    private DataService dataService;
    private static final String TAG = TransaksiReedemActivity.class.getSimpleName();
    /////retrofit2
    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    String id_member = dataList.get(1);
    //global var

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_reedem);

        tvNama = (TextView) findViewById(R.id.tvnama);
        tvtelp = (TextView) findViewById(R.id.tvtelp);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvamt = (TextView) findViewById(R.id.tv_amt);

        btn_back = (TextView) findViewById(R.id.btnBack);
        inputreedem = (EditText) findViewById(R.id.input_reedem);
        save = (TextView) findViewById(R.id.save_reedem);

// Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = TransaksiReedemActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2


        // Dapatkan tanggal sistem
        LocalDate currentDate = null;
        // Tentukan format yang diinginkan
        DateTimeFormatter formatter = null;
        String formattedDate="";

        // Format tanggal sesuai dengan format yang telah ditentukan
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formattedDate = currentDate.format(formatter);
        }

        tvdate.setText(formattedDate);
        initComponents(mContext,id_member);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil metode untuk memuat ulang data
                //fetchData();
                initComponents(mContext,id_member);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransaksiReedemActivity.this, TransactionMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void initComponents(Context mContext, String id_member) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show()
        dataService.GetMemberByCode(id_member).
                enqueue(new Callback<ResponseBody>() {
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
                                        // Periksa apakah kunci "data" ada di dalam objek JSON
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
                                                getResponJson(dataArray);
                                            }
                                        }
                                        else{
                                            Toast.makeText(mContext,
                                                    message,
                                                    Toast.LENGTH_SHORT).show();
                                        }


                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                else {

                                    loading.dismiss();
                                    // Tanggapan API sukses, tetapi ada kesalahan aplikasi
                                    String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Unknown error";
                                    // Tampilkan errorMessage atau lakukan tindakan lain
                                    Toast.makeText(mContext,errorMessage,Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
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
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponJson(JSONArray dataArray) {
        if (dataArray.length() > 0) {
            String amt="";
            String nama="";
            String telp="";
            String mail="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    amt = child.getString("totalamt");
                    nama = child.getString("name");
                    telp = child.getString("notelp");
                    mail = child.getString("mail");
                }
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Mengonversi string menjadi double
            double angka = Double.parseDouble(amt);
            // Membuat format rupiah
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            // Menggunakan format rupiah untuk mengonversi angka menjadi string dengan separator
            String angkaFormatted = formatRupiah.format(angka);

            tvamt.setText(angkaFormatted);
            tvNama.setText(nama+" ("+id_member+")");
            tvtelp.setText("Phone : "+telp);
        }
    }

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