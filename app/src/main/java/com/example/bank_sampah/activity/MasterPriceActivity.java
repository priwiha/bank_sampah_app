package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.MasterDataAdapter;
import com.example.bank_sampah.adapter.PriceAdapter;
import com.example.bank_sampah.model.MasterDataModel;
import com.example.bank_sampah.model.MasterPriceModel;
import com.example.bank_sampah.model.MemberDataModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MasterPriceActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rcv_master;
    private List<MasterPriceModel> list;
    private PriceAdapter adapter;

    private TextView btn_add;
    private TextView btn_back;
    private EditText search_date;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = MasterPriceActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_master_price);

        rcv_master = (RecyclerView) findViewById(R.id.rcv_datamaster);
        btn_add = (TextView) findViewById(R.id.btnAddCat);
        btn_back = (TextView) findViewById(R.id.btnBack);
        search_date = (EditText) findViewById(R.id.input_name);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = MasterPriceActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2





        initComponents(mContext);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil metode untuk memuat ulang data
                //fetchData();
                initComponents(mContext);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for (int x = 0; x < 5; x++) {
                //Toast.makeText(MasterKategoriActivity.this,list.get(x).getName().toString(),Toast.LENGTH_SHORT).show();
                //}
                Toast.makeText(MasterPriceActivity.this,"Add New Category",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MasterPriceActivity.this, CreateOrUpdatePricelistActivity.class);
                i.putExtra("add_or_update", "add");
                startActivity(i);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MasterPriceActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        });





    }

    private void initComponents(Context mContext) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();

        dataService.PricelistRequestAll().
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: Berhasil");
                            //Log.i("cek ",String.valueOf(response.body()));
                            loading.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            try {
                                String ResponseString = response.body().string();
                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                //JSONObject dataObject = jsonRESULTS.getJSONObject("data");
                                String MessageString = jsonRESULTS.get("message").toString();

                                if (jsonRESULTS.has("data")) {

                                    // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                    JSONArray dataArray = new JSONArray();
                                    dataArray.put(jsonRESULTS);

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    // Ambil objek pertama dari dataArray
                                    JSONObject dataObject = dataArray.getJSONObject(0);
                                    // Ambil array "data" dari objek tersebut
                                    JSONArray dataArrayInside = dataObject.getJSONArray("data");

                                    if (dataArrayInside.length() > 0) {
                                        getDataJson(dataArrayInside);
                                        System.out.println(MessageString);
                                    } else {
                                        System.out.println(MessageString);
                                    }
                                    Toast.makeText(mContext, MessageString, Toast.LENGTH_SHORT).show();
                                }
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
                            mSwipeRefreshLayout.setRefreshing(false);


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDataJson(JSONArray dataArray) {

        String id = "";
        String idcat = "";
        String nama = "";
        String price = "";
        String statusprice = "";
        String begdateprice = "";
        String satuan = "";
        String satuan_nama = "";
        list = new ArrayList<MasterPriceModel>();

        if (dataArray.length() > 0) {
            try {

                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject jo2 = dataArray.getJSONObject(i);
                    id  = jo2.getString("idprice");
                    idcat = jo2.getString("idcategory");
                    nama = jo2.getString("namecategory");
                    price = jo2.getString("price");
                    statusprice = jo2.getString("status");
                    begdateprice = jo2.getString("begdate");
                    satuan = jo2.getString("iduom");
                    satuan_nama = jo2.getString("uomname");

                    // Mengonversi string menjadi double
                    double angka = Double.parseDouble(jo2.getString("price"));

                    // Membuat format rupiah
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                    // Menggunakan format rupiah untuk mengonversi angka menjadi string dengan separator
                    String angkaFormatted = formatRupiah.format(angka);

                    list.add(new MasterPriceModel(nama,angkaFormatted,satuan_nama,statusprice,begdateprice,id));
                }

                adapter = new PriceAdapter(list,this);//array dimasukkan ke adapter
                rcv_master.setAdapter(adapter);
                rcv_master.setLayoutManager(new LinearLayoutManager(this));

                String finalNama = nama;
                adapter.setOnItemClickListener(new PriceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        //Toast.makeText(MasterPriceActivity.this,list.get(position).getKategori().toString(),Toast.LENGTH_SHORT).show();
                        String status ="";
                        if (list.get(position).getAktif().toString().equals("Y")){
                            status="Aktif";
                        }
                        else {
                            status="Non-Aktif";
                        }
                        Intent i = new Intent(MasterPriceActivity.this, CreateOrUpdatePricelistActivity.class);
                        i.putExtra("add_or_update", "update");
                        i.putExtra("kategori", list.get(position).getKategori().toString());
                        i.putExtra("price", list.get(position).getPrice().toString());
                        i.putExtra("satuan", list.get(position).getSatuan().toString());
                        i.putExtra("tgl", list.get(position).getTglins().toString());
                        i.putExtra("status", status);
                        i.putExtra("id", list.get(position).getId().toString());
                        startActivity(i);
                        finish();
                        }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Tambahkan OnClickListener ke EditText untuk memunculkan DatePickerDialog
        search_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Tambahkan TextWatcher ke EditText untuk filter saat teks berubah
        search_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Invoke the filter method when text changes
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });

    }

    private void showDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                // Handle date selection and update EditText
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(selectedYear, selectedMonth, selectedDay);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                search_date.setText(sdf.format(selectedDate.getTime()));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    // Metode ini mengembalikan daftar data Anda (dummy data atau dari sumber data lainnya)
    /*private List<MasterPriceModel> getYourDataList() {
        // Implementasikan sesuai dengan kebutuhan Anda
    }*/

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