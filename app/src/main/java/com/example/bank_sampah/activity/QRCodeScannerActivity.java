package com.example.bank_sampah.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.Manifest;
import com.example.bank_sampah.R;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.response.ApiResponse;
import com.example.bank_sampah.utility.network.service.DataService;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class QRCodeScannerActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 1;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = QRCodeScannerActivity.class.getSimpleName();
    /////retrofit2

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    //global var
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = QRCodeScannerActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                initQRCodeScanner();

            }
        } else {
            initQRCodeScanner();
        }
    }
    private void initQRCodeScanner() {
        // Initialize QR code scanner here
        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR code");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();*/

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    //In the "initQRCodeScanner" method,
    //create an instance of the ZXing scanner and set the required configurations
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    //  Override the onActivityResult method to handle the scanned QR code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {

                 String id_member=result.getContents();
                cekMember(QRCodeScannerActivity.this,id_member);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void cekMember(Context mContext, String id_member) {
        //mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();

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
                                            //JSONObject dataObject = jsonRESULTS.getJSONObject("data");

                                            // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                            //JSONArray dataArray = new JSONArray();
                                            //dataArray.put(dataObject);

                                            // Output array JSON
                                            //System.out.println(dataArray.toString());

                                            Intent i = new Intent(QRCodeScannerActivity.this, TransactionMenuActivity.class);
                                            i.putExtra("idmember", id_member);
                                            startActivity(i);
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
                        //mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }


}