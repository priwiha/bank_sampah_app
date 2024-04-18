package com.example.bank_sampah.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.activity.TransactionMenuActivity;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.service.DataService;

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

public class ApprovalDialog {

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

    public void showDialog(Activity activity,String Type) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_input_amt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText amt = (EditText) dialog.findViewById(R.id.input_reedem);
        TextView title = (TextView) dialog.findViewById(R.id.title);

        amt.setVisibility(View.GONE);
        title.setText("Approval");

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = activity;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        Button mDialogNo = dialog.findViewById(R.id.frmNo);
        mDialogNo.setText("Reject");
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(),"Reject" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button mDialogOk = dialog.findViewById(R.id.frmOk);
        mDialogOk.setText("Approve");
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve_member(id_member,userid,mContext);
                Toast.makeText(activity.getApplicationContext(),"Approve" ,Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void approve_member(String id_member, String userid, Context mContext) {
        //mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        dataService.MemberActivate(id_member,userid).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            //mSwipeRefreshLayout.setRefreshing(false);
                            try {
                                String ResponseString = response.body().string();
                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                String MessageString = jsonRESULTS.get("message").toString();

                                if (jsonRESULTS.has("data")) {
                                    //JSONObject dataObject = jsonRESULTS.getJSONObject("data");
                                    Object dataObject = jsonRESULTS.get("data");
                                    JSONArray dataArray = new JSONArray();
                                    // Periksa apakah dataObject adalah objek JSON atau array JSON
                                    if (dataObject instanceof JSONArray) {
                                        dataArray = (JSONArray) dataObject;
                                        // Anda dapat melanjutkan pemrosesan seperti biasa jika dataObject adalah array JSON
                                    } else if (dataObject instanceof JSONObject) {
                                        // Buatlah array JSON baru dan tambahkan objek JSON ke dalamnya
                                        dataArray.put(dataObject);
                                        // Anda dapat melanjutkan pemrosesan dengan array JSON yang baru saja dibuat
                                    }

                                    System.out.println(MessageString.toString());

                                    // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                    //JSONArray dataArray = new JSONArray();
                                    //dataArray.put(dataObject);

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        getResponJson(dataArray,mContext);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            loading.dismiss();
                            //mSwipeRefreshLayout.setRefreshing(true);
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

    private void getResponJson(JSONArray dataArray, Context mContext) {
        // Panggil metode untuk memperbarui aktivitas setelah dialog ditutup
        TransactionMenuActivity.refreshActivity(mContext);
    }
}
