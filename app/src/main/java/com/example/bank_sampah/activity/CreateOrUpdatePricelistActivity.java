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

public class CreateOrUpdatePricelistActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView back;
    private TextView save;

    private Spinner kategori;
    private EditText price;
    private EditText satuan;
    private EditText tgl;
    private EditText status;

    private String vtipe;
    private String vkategori;
    private String vprice;
    private String vsatuan;
    private String vtgl;
    private String vstatus;

    private String kat_id,kat_name,uomname,iduom;
    private List<String> list_kat,list_katid,list_uom,list_iduom;
    private ArrayAdapter<String> adapter_kat;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = CreateOrUpdatePricelistActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_create_or_update_pricelist);

        back = (TextView) findViewById(R.id.btnBack);
        save = (TextView) findViewById(R.id.save_price_category);

        kategori = (Spinner) findViewById(R.id.spn_satuan_cat);
        price = (EditText) findViewById(R.id.add_harga_cat);
        satuan = (EditText) findViewById(R.id.add_satuan_cat);
        tgl = (EditText) findViewById(R.id.add_tanggal_cat);
        status = (EditText) findViewById(R.id.add_status_cat);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = CreateOrUpdatePricelistActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            vtipe       = intent.getStringExtra("add_or_update");
            vkategori   = "";
            vprice      = "";
            vsatuan     = "";
            vtgl        = "";
            vstatus     = "";

            if (vtipe.equals("update"))
            {
                save.setVisibility(View.GONE);

                vkategori   = intent.getStringExtra("kategori");
                vprice      = intent.getStringExtra("price");
                vsatuan     = intent.getStringExtra("satuan");
                vtgl        = intent.getStringExtra("tgl");
                vstatus     = intent.getStringExtra("status");

                //kategori.setText(vkategori);
                price.setText(vprice);
                satuan.setText(vsatuan);
                tgl.setText(vtgl);
                status.setText(vstatus);

                kategori.setEnabled(false);
                price.setEnabled(false);
                satuan.setEnabled(false);
                tgl.setEnabled(false);
                status.setEnabled(false);


            }
            else {
                //kategori.setText(vkategori);
                price.setText(vprice);
                satuan.setText(vsatuan);
                tgl.setText(vtgl);
                status.setText(vstatus);

                //satuan.setVisibility(View.GONE);
                satuan.setEnabled(false);
                tgl.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
            }

        }


        // methode ini berfungsi untuk mendeklarasikan widget yang ada di layout
        initComponents(mContext);

        // SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil metode untuk memuat ulang data
                //fetchData();
                initComponents(mContext);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateOrUpdatePricelistActivity.this, MasterPriceActivity.class);
                startActivity(i);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!kat_id.equals("0")){
                    createPrice(kat_id,price.getText().toString(),userid,mContext);
                }
                else {
                    Toast.makeText(mContext, "Pilihan tipe categori "+kat_name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createPrice(String kat_id, String price, String userid, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, price, Toast.LENGTH_SHORT).show();

        dataService.CreatePrice(kat_id,price,userid).
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
            String idcategory="";
            String price="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    idcategory = child.getString("idcategory");
                    price = child.getString("price");



                }
                Log.e( "id category"+idcategory," berhasil diproses harga "+price);

                Intent i = new Intent(CreateOrUpdatePricelistActivity.this, MasterPriceActivity.class);
                startActivity(i);
                finish();
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initComponents(Context mContext) {

        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();

        dataService.CategoryRequestAll().
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
                                    getDataJson(dataArray);
                                }
                                /*login_layout.setVisibility(View.VISIBLE);
                                register_layout.setVisibility(View.GONE);*/

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
                });

    }

    private void getDataJson(JSONArray dataArray) {
        list_kat = new ArrayList<>();
        list_katid = new ArrayList<>();
        list_uom = new ArrayList<>();
        list_iduom = new ArrayList<>();
        list_kat.add("--Pilih Satuan--");
        list_katid.add("0");
        list_uom.add("-");
        list_iduom.add("0");


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

                    Log.e("cek uomname",jo2.getString("uomname"));
                    Log.e("cek namecategory",jo2.getString("namecategory"));
                    Log.e("cek idcategory",jo2.getString("idcategory"));
                    Log.e("cek iduom",jo2.getString("iduom"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        adapter_kat= new ArrayAdapter<String>(CreateOrUpdatePricelistActivity.this, android.R.layout.simple_spinner_item, list_kat);
        adapter_kat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(adapter_kat);

        if (!vkategori.trim().isEmpty()){
            //set default spinner
            //if (!vsatuan.equals("")) {
            for (int i = 0; i < list_kat.size(); i++) {
                if (vkategori.equals(list_kat.get(i))) {
                    Log.e("cek list kategori", list_kat.get(i) + " = " + vkategori);
                    Log.e("cek list satuan", list_uom.get(i) + " = " + vsatuan);
                    kategori.setSelection(i);
                    satuan.setText(vsatuan);
                }
            }
            //}
        }
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kat_id = list_katid.get(position);
                kat_name = list_kat.get(position);
                vsatuan/*uomname*/ = list_uom.get(position);
                iduom = list_iduom.get(position);

                satuan.setText(vsatuan/*uomname*/);

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