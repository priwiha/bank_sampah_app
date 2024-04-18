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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.MasterDataAdapter;
import com.example.bank_sampah.model.AdminMenuModel;
import com.example.bank_sampah.model.MasterDataModel;
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

public class MasterKategoriActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rcv_master;
    private List<MasterDataModel> list;
    private MasterDataAdapter adapter;

    private TextView btn_add;
    private TextView btn_back;
    private EditText search_name;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = MainActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_master_kategori);

        rcv_master = (RecyclerView) findViewById(R.id.rcv_datamaster);
        btn_add = (TextView) findViewById(R.id.btnAddCat);
        btn_back = (TextView) findViewById(R.id.btnBack);
        search_name = (EditText) findViewById(R.id.input_name);
        search_name.setVisibility(View.GONE);


        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = MasterKategoriActivity.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2

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


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for (int x = 0; x < 5; x++) {
                    //Toast.makeText(MasterKategoriActivity.this,list.get(x).getName().toString(),Toast.LENGTH_SHORT).show();
                //}
                Toast.makeText(MasterKategoriActivity.this,"Add New Category",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MasterKategoriActivity.this, CreateOrUpdateCategoryActivity.class);
                i.putExtra("add_or_update", "add");
                startActivity(i);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MasterKategoriActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        });





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
                                String ResponseString = response.body().string();
                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(ResponseString);

                                String MessageString = jsonRESULTS.get("message").toString();

                                if (jsonRESULTS.has("data")) {

                                    Object dataObject = jsonRESULTS.get("data");
                                    System.out.println(dataObject);

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

                                    // Buat array JSON baru dan tambahkan objek data ke dalamnya
                                    //JSONArray dataArray = (JSONArray) dataObject;

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        getDataJson(dataArray);
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
        String nama = "";
        String satuan = "";
        String satuan_nama = "";
        list = new ArrayList<MasterDataModel>();
        if (dataArray.length() > 0) {
            try {

                int ndata = dataArray.length();
                for (int i = 0; i < ndata; i++) {
                    JSONObject jo2 = dataArray.getJSONObject(i);

                    id = jo2.getString("idcategory");
                    nama = jo2.getString("namecategory");
                    satuan = jo2.getString("iduom");
                    satuan_nama = jo2.getString("uomname");
                    list.add(new MasterDataModel(id,nama,satuan,satuan_nama,"kategori"));
                }
                adapter = new MasterDataAdapter(list,this);//array dimasukkan ke adapter
                rcv_master.setAdapter(adapter);
                rcv_master.setLayoutManager(new LinearLayoutManager(this));

                //String finalNama = nama;
                adapter.setOnItemClickListener(new MasterDataAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        //Toast.makeText(MasterKategoriActivity.this,list.get(position).getName().toString(),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MasterKategoriActivity.this, CreateOrUpdateCategoryActivity.class);
                        i.putExtra("add_or_update", "update");
                        i.putExtra("name", list.get(position).getName());
                        i.putExtra("satuan", list.get(position).getSatuan().toString());
                        i.putExtra("id", list.get(position).getId().toString());
                        startActivity(i);
                        finish();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        search_name.addTextChangedListener(new TextWatcher() {
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