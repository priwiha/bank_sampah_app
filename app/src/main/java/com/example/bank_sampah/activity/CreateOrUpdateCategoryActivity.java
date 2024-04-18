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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class CreateOrUpdateCategoryActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout lt_add_cat;
    private LinearLayout lt_ch_cat;

    private TextView btn_add_cat;
    private TextView btn_ch_cat;

    private EditText etadd_name_cat;
    private Spinner spnadd_satuan_cat;
    private EditText etch_name_cat;
    private Spinner spnch_satuan_cat;

    private String sat_id,sat_name;
    private List<String> list_sat,list_satid;
    private ArrayAdapter<String> adapter_sat;

    private TextView back;

    private String tipe ="";
    private String name = "";
    private String id="";
    private String satuan="";

    private DataService dataService;
    private static final String TAG = CreateOrUpdateCategoryActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    //global var

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_category);

        lt_add_cat = (LinearLayout) findViewById(R.id.add_categori_lt);
        lt_ch_cat = (LinearLayout) findViewById(R.id.ch_categori_lt);

        btn_add_cat = (TextView) findViewById(R.id.save_add_category);
        btn_ch_cat = (TextView) findViewById(R.id.save_ch_category);

        etadd_name_cat = (EditText) findViewById(R.id.add_name_cat);
        spnadd_satuan_cat = (Spinner) findViewById(R.id.spn_satuan_cat);
        etch_name_cat = (EditText) findViewById(R.id.ch_name_cat);
        spnch_satuan_cat = (Spinner) findViewById(R.id.spn_chsatuan_cat);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = CreateOrUpdateCategoryActivity.this;
        dataService = UtilsApi.getAPIService();
        // methode ini berfungsi untuk mendeklarasikan widget yang ada di layout
        initComponents();
        getData(mContext,"");

        //Toast.makeText(CreateOrUpdateCategoryActivity.this,"cek get global var "+userid,Toast.LENGTH_SHORT).show();

        // SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil metode untuk memuat ulang data
                //fetchData();
                getData(mContext,"");
            }
        });

        back = (TextView) findViewById(R.id.btnBack);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateOrUpdateCategoryActivity.this, MasterKategoriActivity.class);
                startActivity(i);
                finish();
            }
        });

        lt_add_cat.setVisibility(View.VISIBLE);
        lt_ch_cat.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            tipe    = intent.getStringExtra("add_or_update");
            name    = "";
            id      = "";

            if (tipe.equals("update"))
            {
                lt_add_cat.setVisibility(View.GONE);
                lt_ch_cat.setVisibility(View.VISIBLE);

                name=intent.getStringExtra("name");
                id=intent.getStringExtra("id");
                satuan=intent.getStringExtra("satuan");
                etch_name_cat.setText(name);
                getData(mContext,satuan);
                //Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

            }
            else {
                lt_add_cat.setVisibility(View.VISIBLE);
                lt_ch_cat.setVisibility(View.GONE);
            }

            //etch_satuan_cat.setText(satuan);
        }


        btn_add_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuat dialog konfirmasi
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrUpdateCategoryActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin proses data?");

                // Tombol "Ya"
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Ya"
                        // Misalnya, menyimpan data atau menjalankan tindakan lain

                        name=etadd_name_cat.getText().toString();
                        if (!sat_id.equalsIgnoreCase("0")) {
                            //Toast.makeText(CreateOrUpdateCategoryActivity.this,sat_id+"-"+sat_name,Toast.LENGTH_SHORT).show();
                            if (!name.trim().isEmpty())
                            {
                                saveCategory(name,sat_id,userid,mContext);
                            }
                            else {
                                Toast.makeText(CreateOrUpdateCategoryActivity.this,"Nama Kategori tidak boleh kosong",Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            Toast.makeText(CreateOrUpdateCategoryActivity.this,"Satuan harus dipilih",Toast.LENGTH_SHORT).show();

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

        btn_ch_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuat dialog konfirmasi
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrUpdateCategoryActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin proses data?");

                // Tombol "Ya"
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Ya"
                        // Misalnya, menyimpan data atau menjalankan tindakan lain

                        //name=etch_name_cat.getText().toString();
                        if (!sat_id.equalsIgnoreCase("0")) {
                            if (!etch_name_cat.getText().toString().trim().isEmpty())
                            {
                                ChangeCategory(id,etch_name_cat.getText().toString(),sat_id,userid,mContext);
                            }
                            else {
                                Toast.makeText(CreateOrUpdateCategoryActivity.this,"Nama Kategori tidak boleh kosong",Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            Toast.makeText(CreateOrUpdateCategoryActivity.this,"Satuan harus dipilih",Toast.LENGTH_SHORT).show();

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

    private void ChangeCategory(String id, String name, String sat_id, String userid, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);


        dataService.ChangeCategory(id,name,sat_id,userid).
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
                                        getResponJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
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
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void saveCategory(String name_category, String sat_id, String userid, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        dataService.CreateCategory(name_category,sat_id,userid).
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
                                    Object dataObject = jsonRESULTS.get("data");
                                    System.out.println(MessageString.toString());

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

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        getResponJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
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
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponJson(JSONArray dataArray) {

        if (dataArray.length() > 0) {
            String namecategory="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    namecategory = child.getString("namecategory");



                }
                Toast.makeText(CreateOrUpdateCategoryActivity.this, namecategory+" berhasil diproses", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(CreateOrUpdateCategoryActivity.this, MasterKategoriActivity.class);
                startActivity(i);
                finish();
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getData(Context mContext, String satuan) {
        mSwipeRefreshLayout.setRefreshing(true);
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();

        dataService.UomRequestAll().
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            try {
                                String ResponseString = response.body().string();
                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                //JSONObject dataObject = jsonRESULTS.getJSONObject("data");
                                String MessageString = jsonRESULTS.get("message").toString();

                                if (jsonRESULTS.has("data")) {

                                    Object dataObject = jsonRESULTS.get("data");
                                    System.out.println(MessageString.toString());

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
                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        getDataJson(dataArray,satuan);
                                        //getDataJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                    Toast.makeText(mContext, MessageString, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
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
                /*enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: Berhasil");
                            //Log.i("cek ",String.valueOf(response.body()));
                            loading.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            try {

                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                //JSONObject dataObject = jsonRESULTS.getJSONObject("data");

                                // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                JSONArray dataArray = new JSONArray();
                                dataArray.put(jsonRESULTS);

                                // Output array JSON
                                System.out.println(dataArray.toString());

                                Log.e("panjang json array satuan",String.valueOf(dataArray.length()));
                                if (dataArray.length()>0)
                                {
                                    getDataJson(dataArray,satuan);
                                }
                                *//*login_layout.setVisibility(View.VISIBLE);
                                register_layout.setVisibility(View.GONE);*//*

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: Tidak Berhasil");
                            loading.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });*/
    }


    private void initComponents() {


    }

    private void getDataJson(JSONArray dataArray,String satuan) {
        list_sat = new ArrayList<>();
        list_satid = new ArrayList<>();
        list_sat.add("--Pilih Satuan--");
        list_satid.add("0");

        if (dataArray.length() > 0) {
            try {
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject jo2 = dataArray.getJSONObject(x);
                    list_sat.add(jo2.getString("uomname"));
                    list_satid.add(jo2.getString("iduom"));
                }

                // Ambil objek pertama dari dataArray
                /*JSONObject dataObject = dataArray.getJSONObject(0);
                // Ambil array "data" dari objek tersebut
                JSONArray dataArrayInside = dataObject.getJSONArray("data");
                for (int i = 0; i < dataArrayInside.length(); i++) {
                    JSONObject jo2 = dataArrayInside.getJSONObject(i);
                    list_sat.add(jo2.getString("uomname"));
                    list_satid.add(jo2.getString("iduom"));

                    Log.e("cek uomname",jo2.getString("uomname"));
                    Log.e("cek iduom",jo2.getString("iduom"));

                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        adapter_sat= new ArrayAdapter<String>(CreateOrUpdateCategoryActivity.this, android.R.layout.simple_spinner_item, list_sat);
        adapter_sat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnadd_satuan_cat.setAdapter(adapter_sat);
        spnadd_satuan_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sat_id = list_satid.get(position);
                sat_name = list_sat.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter_sat= new ArrayAdapter<String>(CreateOrUpdateCategoryActivity.this, android.R.layout.simple_spinner_item, list_sat);
        adapter_sat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnch_satuan_cat.setAdapter(adapter_sat);
        //set default spinner
        if (!satuan.equals("")) {
            for (int i = 0; i < list_sat.size(); i++) {
                if (satuan.equals(list_satid.get(i))) {
                    Log.e("cek list uomname", list_sat.get(i) + " = " + satuan);
                    Log.e("cek list iduom", list_satid.get(i) + " = " + satuan);
                    spnch_satuan_cat.setSelection(i);
                }
            }
        }
        spnch_satuan_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String kurir;
                sat_id = list_satid.get(position);
                sat_name = list_sat.get(position);

                /*if (!sat_id.equalsIgnoreCase("0")) {
                    Toast.makeText(CreateOrUpdateCategoryActivity.this,sat_id+"-"+sat_name,Toast.LENGTH_SHORT).show();
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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