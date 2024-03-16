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
import java.util.List;
import java.util.Locale;

public class TransaksiTimbangActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView btn_back;

    private Spinner et_cat;
    private EditText et_sat;
    private EditText et_bobot;
    private EditText et_harga;

    private TextView tvNama;
    private TextView tvtelp;
    private TextView tvdate;

    private TextView btn_save;

    private String kat_id,kat_name,uomname,iduom,harga;
    private List<String> list_kat,list_katid,list_uom,list_iduom,list_harga;
    private ArrayAdapter<String> adapter_kat;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = TransaksiTimbangActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_transaksi_timbang);

        btn_back = (TextView) findViewById(R.id.btnBack);
        et_cat = (Spinner) findViewById(R.id.spn_satuan_cat);
        et_sat = (EditText) findViewById(R.id.select_satuan);
        et_bobot = (EditText) findViewById(R.id.input_bobot);
        et_harga = (EditText) findViewById(R.id.select_harga);
        btn_save = (TextView) findViewById(R.id.save_timbang);
        tvNama = (TextView) findViewById(R.id.tvnama);
        tvtelp = (TextView) findViewById(R.id.tvtelp);
        tvdate = (TextView) findViewById(R.id.tvdate);

        et_sat.setEnabled(false);
        et_harga.setEnabled(false);


        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = TransaksiTimbangActivity.this;
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

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTimbang(id_member,kat_id,iduom,et_bobot.getText().toString(),harga,userid,mContext);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransaksiTimbangActivity.this, TransactionMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void createTimbang(String id_member, String kat_id, String iduom, String bobot,
                               String harga, String userid, Context mContext) {

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, price, Toast.LENGTH_SHORT).show();
        //Call<ApiResponse> call = dataService.CreateTimbang(id_member,kat_id,iduom,bobot,harga, userid);
        dataService.CreateTimbang(id_member,kat_id,iduom,bobot,harga, userid).enqueue(new Callback<ResponseBody>() {
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
                                        getResponTimbangJson(dataArray);
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

    private void getResponTimbangJson(JSONArray dataArray) {
        if (dataArray.length() > 0) {
            String idtimbang="";
            String totalamt="";
            String membercode="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    membercode = child.getString("membercode");
                    totalamt = child.getString("pricetot");
                    idtimbang = child.getString("idgrb");



                }
                Log.e( "id transaksi"+idtimbang,"member "+membercode+" berhasil diproses harga "+totalamt);

                Intent i = new Intent(TransaksiTimbangActivity.this, HistoryTimbangActivity.class);
                startActivity(i);
                finish();
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initComponents(Context mContext, String id_member) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();
        dataService.GetMemberByCode(id_member).
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
                                /*
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
                                }*/
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            loading.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
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

            //tvamt.setText(amt);
            tvNama.setText(nama+" ("+id_member+")");
            tvtelp.setText("Phone : "+telp);
        }

        getKategori(TransaksiTimbangActivity.this);
    }

    private void getKategori(Context mContext) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        dataService.GetCategoryPrice().
                enqueue(new Callback<ResponseBody>() {
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
                                    getDataJsonKat(dataArray);
                                }

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
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDataJsonKat(JSONArray dataArray) {
        list_kat = new ArrayList<>();
        list_katid = new ArrayList<>();
        list_uom = new ArrayList<>();
        list_iduom = new ArrayList<>();
        list_harga = new ArrayList<>();
        list_kat.add("--Pilih Satuan--");
        list_katid.add("0");
        list_uom.add("-");
        list_iduom.add("0");
        list_harga.add("0");


        if (dataArray.length() > 0) {
            try {
                // Ambil objek pertama dari dataArray
                JSONObject dataObject = dataArray.getJSONObject(0);
                // Ambil array "data" dari objek tersebut
                JSONArray dataArrayInside = dataObject.getJSONArray("data");
                for (int i = 0; i < dataArrayInside.length(); i++) {
                    JSONObject jo2 = dataArrayInside.getJSONObject(i);
                    list_kat.add(jo2.getString("namecategory"));
                    list_katid.add(jo2.getString("idcategory"));
                    list_uom.add(jo2.getString("uomname"));
                    list_iduom.add(jo2.getString("iduom"));
                    list_harga.add(jo2.getString("price"));

                    Log.e("cek uomname",jo2.getString("uomname"));
                    Log.e("cek namecategory",jo2.getString("namecategory"));
                    Log.e("cek idcategory",jo2.getString("idcategory"));
                    Log.e("cek iduom",jo2.getString("iduom"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        adapter_kat= new ArrayAdapter<String>(TransaksiTimbangActivity.this, android.R.layout.simple_spinner_item, list_kat);
        adapter_kat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_cat.setAdapter(adapter_kat);


        et_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kat_id = list_katid.get(position);
                kat_name = list_kat.get(position);
                //vsatuan/*uomname*/ = list_uom.get(position);
                iduom = list_iduom.get(position);
                et_sat.setText(list_uom.get(position));

                harga = list_harga.get(position);

                // Mengonversi string menjadi double
                double angka = Double.parseDouble(list_harga.get(position));
                // Membuat format rupiah
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                // Menggunakan format rupiah untuk mengonversi angka menjadi string dengan separator
                String angkaFormatted = formatRupiah.format(angka);
                et_harga.setText(angkaFormatted);


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