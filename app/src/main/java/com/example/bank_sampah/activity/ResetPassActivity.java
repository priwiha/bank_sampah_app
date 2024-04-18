package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
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
import java.util.Random;

public class ResetPassActivity extends AppCompatActivity {
    private TextView back;
    private TextView save;
    private TextView generate;

    private EditText id;
    private EditText nama;
    private EditText mail;
    private EditText phone;
    private EditText pass;
    private Spinner status;


    private String vtipe ="";
    private String vid ="";
    private String vuser ="";
    private String vuserid ="";
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
    private static final String TAG = ResetPassActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_reset_pass);

        back = (TextView) findViewById(R.id.btnBack);
        save = (TextView) findViewById(R.id.save_approve_member);
        generate = (TextView) findViewById(R.id.generate_pass_member);

        id = (EditText) findViewById(R.id.et_id_member);
        nama= (EditText) findViewById(R.id.et_name_member);
        mail= (EditText) findViewById(R.id.et_mail_member);
        phone= (EditText) findViewById(R.id.et_phone_member);
        pass= (EditText) findViewById(R.id.et_pass_member);
        status= (Spinner) findViewById(R.id.spn_status);

        mail.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        status.setVisibility(View.GONE);

        pass.setEnabled(false);


        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = ResetPassActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        list_status_name = new ArrayList<>();
        list_status_id = new ArrayList<>();
        list_status_name.add("--Pilih Status--");
        list_status_name.add("Ya");
        list_status_name.add("Tidak");
        list_status_id.add("0");
        list_status_id.add("Y");
        list_status_id.add("T");

        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            vtipe       = intent.getStringExtra("add_or_update");

            if (!vtipe.equals("add"))
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtipe.equals("update1")) {
                    Intent i = new Intent(ResetPassActivity.this, MasterMemberActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    finish();
                }
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Panjang string yang diinginkan
                int length = 6;

                // Generate random string
                String randomString = generateRandomString(length);

                // Tampilkan hasil
                System.out.println("Random String: " + randomString +" userid "+vuser);


                pass.setText(randomString);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_pass(pass.getText().toString(),vuser,mContext);
            }
        });
    }

    private void reset_pass(String randomString, String vuserid, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        Log.e("cek param",randomString+"  "+vuserid);
        dataService.ResetPass(randomString,vuserid).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: Berhasil");
                            //Log.i("cek ",String.valueOf(response.body()));
                            loading.dismiss();
                            try {
                                finish();
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
        //Intent i = new Intent(ResetPassActivity.this, MasterMemberActivity.class);
        //startActivity(i);
        finish();
    }

    // Method untuk generate random string
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String characters2 = "!@#$&*";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        char symbol = characters2.charAt(random.nextInt(6));

        // Tambahkan simbol ke string
        sb.append(symbol);

        for (int i = 0; i < length-1; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        // Acak ulang string agar simbol tidak selalu pada posisi awal
        for (int i = 0; i < length; i++) {
            int index1 = random.nextInt(length);
            int index2 = random.nextInt(length);
            char temp = sb.charAt(index1);
            sb.setCharAt(index1, sb.charAt(index2));
            sb.setCharAt(index2, temp);
        }

        return sb.toString();
    }
}