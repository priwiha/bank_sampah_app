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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;

public class ApprovalOrUpdateMemberActivity extends AppCompatActivity {
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView back;
    private TextView save;

    private EditText id;
    private EditText nama;
    private EditText mail;
    private EditText phone;
    //private EditText pass;
    private Spinner status;

    private String vtipe ="";
    private String vid ="";
    private String vuser ="";
    private String vnama="";
    private String vmail="";
    private String vphone="";
    //private String vpass="";
    private String vstatus="";

    private String status_id;
    private List<String> list_status_id,list_status_name;
    private ArrayAdapter<String> adapter_status;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = ApprovalOrUpdateMemberActivity.class.getSimpleName();
    /////retrofit2

    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    //global var

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_or_update_member);

        back = (TextView) findViewById(R.id.btnBack);
        save = (TextView) findViewById(R.id.save_approve_member);

        id = (EditText) findViewById(R.id.et_id_member);
        nama= (EditText) findViewById(R.id.et_name_member);
        mail= (EditText) findViewById(R.id.et_mail_member);
        phone= (EditText) findViewById(R.id.et_phone_member);
        //pass= (EditText) findViewById(R.id.et_phone_member);
        status= (Spinner) findViewById(R.id.spn_status);


        // Inisialisasi SwipeRefreshLayout
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = ApprovalOrUpdateMemberActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ApprovalOrUpdateMemberActivity.this, MasterMemberActivity.class);
                startActivity(i);
                finish();
            }
        });

        list_status_name = new ArrayList<>();
        list_status_id = new ArrayList<>();
        list_status_name.add("--Pilih Status--");
        list_status_name.add("Ya");
        list_status_name.add("Tidak");
        list_status_id.add("0");
        list_status_id.add("Y");
        list_status_id.add("T");


        adapter_status= new ArrayAdapter<String>(ApprovalOrUpdateMemberActivity.this,
                android.R.layout.simple_spinner_item, list_status_name);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter_status);

        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            vtipe       = intent.getStringExtra("add_or_update");


            if (vtipe.equals("update"))
            {
                vid = intent.getStringExtra("id");
                vuser = intent.getStringExtra("username");
                vnama= intent.getStringExtra("name");
                vmail= intent.getStringExtra("mail");
                vphone= intent.getStringExtra("phone");
                //vpass= intent.getStringExtra("pass");
                vstatus= intent.getStringExtra("status");

                id.setText(vid);
                nama.setText(vnama);
                mail.setText(vmail);
                phone.setText(vphone);
                //pass.setText(vpass);

                //status.setText(vstatus);

                id.setEnabled(false);
                Log.e("cek terima status", vstatus);
                if (!vstatus.trim().isEmpty()){
                    //set default spinner
                    for (int i = 0; i < list_status_id.size(); i++) {
                        if (vstatus.equals(list_status_id.get(i))) {
                            Log.e("cek list status", list_status_name.get(i) + " = " + vstatus);
                            status.setSelection(i);
                            status_id = vstatus;
                        }
                    }
                }
            }
            else {
                id.setText(vid);
                nama.setText(vnama);
                mail.setText(vmail);
                phone.setText(vphone);
                //pass.setText(vpass);
                //status.setText(vstatus);

            }

        }

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                status_id = list_status_id.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(userid,vuser,nama.getText().toString(),
                        mail.getText().toString(),phone.getText().toString(),status_id,mContext);
            }
        });

    }

    private void updateData(String userid, String id, String nama, String mail, String phone, String status_id, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, price, Toast.LENGTH_SHORT).show();

        dataService.MemberUpdate(id,nama,phone,mail,userid,status_id).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: Berhasil");
                            //Log.i("cek ",String.valueOf(response.body()));
                            loading.dismiss();
                            try {

                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
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
                                    /*login_layout.setVisibility(View.VISIBLE);
                                    register_layout.setVisibility(View.GONE);*/
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: Tidak Berhasil");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponJson(JSONArray dataArray) {
        if (dataArray.length() > 0) {
            String idmember="";
            //String price="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    idmember = child.getString("membercode");
                    //price = child.getString("price");


                }
                Log.e( "id member"+idmember," berhasil diproses harga ");

                Intent i = new Intent(ApprovalOrUpdateMemberActivity.this, MasterMemberActivity.class);
                startActivity(i);
                finish();
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }
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