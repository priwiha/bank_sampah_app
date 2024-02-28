package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.TransactionMenuAdapter;
import com.example.bank_sampah.model.AdminMenuModel;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.service.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionMenuActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView tvNama;
    private TextView btn_back;
    private TextView tvamt;
    private TextView tvtelp;
    private TextView tvdate;

    private String id_member;

    private RecyclerView rcv_menu;
    private List<AdminMenuModel> list;
    private TransactionMenuAdapter adapter;

    /////retrofit2
    private DataService dataService;
    private static final String TAG = TransactionMenuActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_transaction_menu);

        tvNama = (TextView) findViewById(R.id.tvnama);
        btn_back = (TextView) findViewById(R.id.btnBack);
        tvamt = (TextView) findViewById(R.id.tv_amt);
        tvtelp = (TextView) findViewById(R.id.tvtelp);
        tvdate = (TextView) findViewById(R.id.tvdate);
        rcv_menu = (RecyclerView) findViewById(R.id.rcv_menu);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = TransactionMenuActivity.this;
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

        Intent intent = getIntent();
        if (intent.hasExtra("idmember")) {
            Log.e("DATA DARI HOME ", intent.getStringExtra("idmember"));
            id_member = intent.getStringExtra("idmember");
            globalData.addData(id_member);
        }
        else{
            //jika bukan dari data
            if (!dataList.get(1).isEmpty())
            {
                id_member = dataList.get(1);
            }
            else {
                id_member="";
            }

        }

        initComponents(mContext,id_member);



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil metode untuk memuat ulang data
                //fetchData();
                initComponents(mContext,id_member);
            }
        });


        list = new ArrayList<AdminMenuModel>();
        list.add(new AdminMenuModel("1","Timbang Bobot",""));
        list.add(new AdminMenuModel("2","Reedem",""));
        list.add(new AdminMenuModel("3","Histori Timbang",""));
        list.add(new AdminMenuModel("4","Histori Reedem",""));
        //list.add(new AdminMenuModel("5","Back",""));
        adapter = new TransactionMenuAdapter(list,this);//array dimasukkan ke adapter
        rcv_menu.setAdapter(adapter);
        rcv_menu.setLayoutManager(new GridLayoutManager(this,3));



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_member!=""){
                    globalData.removeData(id_member);
                }

                Intent i = new Intent(TransactionMenuActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        });
        adapter.setOnItemClickListener(new TransactionMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("cek id",list.get(position).getIdmenu().toString());

                if (list.get(position).getIdmenu().toString().equals("1"))
                {
                    Intent i = new Intent(TransactionMenuActivity.this, TransaksiTimbangActivity.class);
                    //i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);
                } else if (list.get(position).getIdmenu().toString().equals("2")) {
                    Intent i = new Intent(TransactionMenuActivity.this, TransaksiReedemActivity.class);
                    //i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);
                } else if (list.get(position).getIdmenu().toString().equals("3")){
                    Intent i = new Intent(TransactionMenuActivity.this, HistoryTimbangActivity.class);
                    //i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);
                } else if (list.get(position).getIdmenu().toString().equals("4")){
                    Intent i = new Intent(TransactionMenuActivity.this, HistoryReedemAdminActivity.class);
                    startActivity(i);
                    finish();
                }
                /*else if (list.get(position).getIdmenu().toString().equals("5")){
                    Intent i = new Intent(TransactionMenuActivity.this, HomeAdminActivity.class);
                    startActivity(i);
                    finish();
                }*/
            }
        });
    }

    public void initComponents(Context mContext, String id_member) {
        mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();

        dataService.GetMemberByCode(id_member).
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

            tvamt.setText(amt);
            tvNama.setText(nama+" ("+id_member+")");
            tvtelp.setText("Phone : "+telp);
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