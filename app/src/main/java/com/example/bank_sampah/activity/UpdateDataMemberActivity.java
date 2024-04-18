package com.example.bank_sampah.activity;

import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.service.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateDataMemberActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView btnSaveProfile;
    private TextView btnMoveChpass;
    private EditText et_ch_name;
    private EditText et_ch_mail;
    private EditText et_ch_phone;
    private TextView btnSavePass;
    private TextView btnMoveUpdateProf;
    private EditText et_ch_pass_now;
    private EditText et_ch_pass_new;
    private EditText et_ch_pass_new_konf;

    private LinearLayout ltChProf;
    private LinearLayout ltChPass;

    //private TextView btn_dashboard;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = UpdateDataMemberActivity.class.getSimpleName();
    /////retrofit2

    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        setContentView(R.layout.activity_update_data_member);

        btnBack = (TextView) findViewById(R.id.member_back);
        btnSaveProfile = (TextView) findViewById(R.id.member_ch_profile);
        btnMoveUpdateProf = (TextView) findViewById(R.id.member_updateprof_move);
        et_ch_name = (EditText) findViewById(R.id.ch_name_member);
        et_ch_phone = (EditText) findViewById(R.id.ch_phone_member);
        et_ch_mail = (EditText) findViewById(R.id.ch_mail_member);

        btnSavePass = (TextView) findViewById(R.id.member_save_pass);
        btnMoveChpass = (TextView) findViewById(R.id.member_ch_pass_move);
        et_ch_pass_now = (EditText) findViewById(R.id.ch_current_pass);
        et_ch_pass_new = (EditText) findViewById(R.id.ch_new_pass);
        et_ch_pass_new_konf = (EditText) findViewById(R.id.ch_new_pass2);

        //btn_dashboard = (TextView) findViewById(R.id.member_dashboard);

        ltChProf = (LinearLayout) findViewById(R.id.member_chprofile);
        ltChPass = (LinearLayout) findViewById(R.id.member_chpass);

        ltChProf.setVisibility(View.VISIBLE);
        ltChPass.setVisibility(View.GONE);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = UpdateDataMemberActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        initComponents(mContext,id_member);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil metode untuk memuat ulang data
                //fetchData();
                initComponents(mContext,id_member);
            }
        });

        btnMoveChpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ltChProf.setVisibility(View.GONE);
                ltChPass.setVisibility(View.VISIBLE);
            }
        });

        /*btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateDataMemberActivity.this, DashboardMemberActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        btnMoveUpdateProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ltChProf.setVisibility(View.VISIBLE);
                ltChPass.setVisibility(View.GONE);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateDataMemberActivity.this, DashboardMemberActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuat dialog konfirmasi
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDataMemberActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin menyimpan data?");

                // Tombol "Ya"
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Ya"
                        // Misalnya, menyimpan data atau menjalankan tindakan lain
                        updateProfile(userid,et_ch_name.getText().toString(),
                                et_ch_mail.getText().toString(),
                                et_ch_phone.getText().toString(),mContext);
                    }
                });

                // Tombol "Batal"
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Batal"
                        // Misalnya, tidak melakukan apa pun atau menutup dialog
                        dialog.cancel(); // atau dialog.dismiss();
                    }
                });

                // Tampilkan dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

///BELUM BISA
        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuat dialog konfirmasi
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDataMemberActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin mengubah password ?");

                // Tombol "Ya"
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Ya"
                        // Misalnya, menyimpan data atau menjalankan tindakan lain

                        if (et_ch_pass_new.getText().toString().equals(et_ch_pass_new_konf.getText().toString())) {
                            updatePass(userid,
                                    et_ch_pass_now.getText().toString(),
                                    et_ch_pass_new_konf.getText().toString(),
                                    mContext);
                        }
                        else{
                            Toast.makeText(UpdateDataMemberActivity.this,"Periksa Kembali Inputan Password Baru! ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Tombol "Batal"
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Batal"
                        // Misalnya, tidak melakukan apa pun atau menutup dialog
                        dialog.cancel(); // atau dialog.dismiss();
                    }
                });

                // Tampilkan dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void updatePass(String userid, String old_pass, String new_pass, Context mContext) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        dataService.ChangePass(userid,old_pass,new_pass).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();////
                            mSwipeRefreshLayout.setRefreshing(false);//
                            try {
                                String ResponseString = response.body().string();
                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                String MessageString = jsonRESULTS.get("message").toString();
                                Toast.makeText(UpdateDataMemberActivity.this,MessageString,Toast.LENGTH_SHORT).show();
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

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        //getResponUpdateJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }

                                    Intent i = new Intent(UpdateDataMemberActivity.this, DashboardMemberActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            loading.dismiss();///
                            mSwipeRefreshLayout.setRefreshing(true);//
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
                        loading.dismiss();//
                        mSwipeRefreshLayout.setRefreshing(false);//
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void initComponents(Context mContext, String id_member) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        dataService.GetMemberByCode(id_member).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();////
                            mSwipeRefreshLayout.setRefreshing(false);//
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
                            loading.dismiss();///
                            mSwipeRefreshLayout.setRefreshing(true);//
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
                        loading.dismiss();//
                        mSwipeRefreshLayout.setRefreshing(false);//
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponJson(JSONArray dataArray, Context mContext) {
        String amt="";
        String nama="";
        String telp="";
        String mail="";
        String status="";
        String user="";
        if (dataArray.length() > 0) {
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    amt = child.getString("totalamt");
                    nama = child.getString("name");
                    telp = child.getString("notelp");
                    mail = child.getString("mail");
                    status = child.getString("aktif");
                    user = child.getString("userid");
                }
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }

            et_ch_name.setText(nama);
            et_ch_mail.setText(mail);
            et_ch_phone.setText(telp);
        }

    }


    private void updateProfile(String userid,String nama,
                               String mail, String phone, Context mContext) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        dataService.MemberUpdate(userid,nama,phone,mail,userid,"Y").
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();////
                            mSwipeRefreshLayout.setRefreshing(false);//
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

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        //getResponUpdateJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }

                                    Intent i = new Intent(UpdateDataMemberActivity.this, DashboardMemberActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            loading.dismiss();///
                            mSwipeRefreshLayout.setRefreshing(true);//
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
                        loading.dismiss();//
                        mSwipeRefreshLayout.setRefreshing(false);//
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponUpdateJson(JSONArray dataArray) {


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