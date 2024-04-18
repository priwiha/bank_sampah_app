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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.HistoriReedemAdapter;
import com.example.bank_sampah.adapter.HistoriTimbangAdapter;
import com.example.bank_sampah.adapter.TrxSampahAdapter;
import com.example.bank_sampah.model.HistoriReedemModel;
import com.example.bank_sampah.model.HistoriTransactionModel;
import com.example.bank_sampah.model.TrxSampahModel;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.service.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryReedemActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView tv_memname;
    private RecyclerView rcv_reedem;
    private EditText et_tgl_fill;
    private List<HistoriReedemModel> list;
    private HistoriReedemAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;


    /////retrofit2
    private DataService dataService;
    private static final String TAG = HistoryReedemActivity.class.getSimpleName();
    /////retrofit2

    private SwipeRefreshLayout mSwipeRefreshLayout;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    String id_member = dataList.get(1);
    //global var


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_reedem);

        btnBack = (TextView) findViewById(R.id.member_back);
        tv_memname = (TextView) findViewById(R.id.memname);
        rcv_reedem = (RecyclerView) findViewById(R.id.rcv_reedem);
        // on below line we are initializing our variables.
        et_tgl_fill = (EditText) findViewById(R.id.editTextDate);
        //dateEdt = findViewById(R.id.idEdtDate);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        et_tgl_fill.setText(date);


        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = HistoryReedemActivity.this;
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // on below line we are adding click listener
        // for our pick date button
        et_tgl_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        HistoryReedemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Mengonversi bulan menjadi format dua digit.
                                String formattedMonth = String.format("%02d", monthOfYear + 1);
                                String formattedDay = String.format("%02d", dayOfMonth);

                                et_tgl_fill.setText(formattedDay + "/" + formattedMonth + "/" + year);
                                String date = et_tgl_fill.getText().toString();
                                System.out.println("cek fill tgl: "+date);
                                //Toast.makeText(HistoryReedemActivity.this, date, Toast.LENGTH_SHORT).show();

                                ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                if (date.trim().isEmpty()) {
                                    getHistoryRedeem(id_member,mContext,loading);
                                } else {
                                    fillRedeem(id_member, date, loading, mContext);
                                }
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    private void fillRedeem(String id_member, String date, ProgressDialog loading, Context mContext) {
        dataService.GetReedem_byMemberCode_Date(id_member,date).
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
                                        getResponListTimbangJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                    Toast.makeText(mContext,MessageString,Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    getHistoryRedeem(id_member,mContext,loading);
                                }

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
                                        getResponJson(dataArray,mContext,loading);
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

    private void getResponJson(JSONArray dataArray, Context mContext, ProgressDialog loading) {
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

            // Mengonversi string menjadi double
            double angka = Double.parseDouble(amt);
            // Membuat format rupiah
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            // Menggunakan format rupiah untuk mengonversi angka menjadi string dengan separator
            String angkaFormatted = formatRupiah.format(angka);

            //tv_amt.setText(angkaFormatted);
            tv_memname.setText(nama+"\n("+id_member+")");

            getHistoryRedeem(id_member,mContext,loading);
        }
    }

    private void getHistoryRedeem(String id_member, Context mContext, ProgressDialog loading) {
        dataService.GetReedem_byMemberCode(id_member).
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
                                        getResponListTimbangJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                    Toast.makeText(mContext,MessageString,Toast.LENGTH_SHORT).show();
                                }

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

    private void getResponListTimbangJson(JSONArray dataArray) {
        String id = "";
        String tgl = "";
        String rupiah = "";
        String approved = "";
        String status = "";

        list = new ArrayList<HistoriReedemModel>();

        if (dataArray.length() > 0) {
            try {

                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject jo2 = dataArray.getJSONObject(i);
                    id     = jo2.getString("idredeem");
                    rupiah    = jo2.getString("redeemamt");
                    tgl    = jo2.getString("redeemdate");
                    approved = jo2.getString("userapproved");


                    // Mengonversi string menjadi double
                    double angka = Double.parseDouble(rupiah);
                    // Membuat format rupiah
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                    // Menggunakan format rupiah untuk mengonversi angka menjadi string dengan separator
                    String angkaFormatted = formatRupiah.format(angka);

                    // Format tanggal
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        // Parsing string tanggal menjadi objek Date
                        Date date = inputFormat.parse(tgl);
                        // Mengonversi objek Date menjadi string dengan format yang diinginkan
                        String formattedDate = outputFormat.format(date);
                        // Output hasilnya
                        System.out.println("Tanggal dalam format baru: " + formattedDate);

                        list.add(new HistoriReedemModel(id,formattedDate,angkaFormatted,approved));

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new HistoriReedemAdapter(list,this);//array dimasukkan ke adapter
        // Memanggil notifyDataSetChanged() pada adapter.
        adapter.notifyDataSetChanged();
        rcv_reedem.setAdapter(adapter);
        rcv_reedem.setLayoutManager(new LinearLayoutManager(this));

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