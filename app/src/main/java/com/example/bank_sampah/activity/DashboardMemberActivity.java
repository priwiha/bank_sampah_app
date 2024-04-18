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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.HistoriTimbangAdapter;
import com.example.bank_sampah.adapter.TrxSampahAdapter;
import com.example.bank_sampah.model.HistoriTransactionModel;
import com.example.bank_sampah.model.TrxSampahModel;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.ViewDialog;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.service.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardMemberActivity extends AppCompatActivity {


    ///menu bottom
    //private TextView btn_dashboard;
    private TextView btn_reedem_hist;
    private TextView btn_profile;

    private EditText et_tgl_fill;
    private TextView btn_reedem_act;
    private TextView btn_logout;

    private RecyclerView rcv_trx;
    private List<TrxSampahModel> list;
    private TrxSampahAdapter adapter;

    private TextView tv_date;
    private TextView tv_amt;
    //header
    private TextView tv_memname;


    /////retrofit2
    private DataService dataService;
    private static final String TAG = DashboardMemberActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_dashboard_member);

        btn_logout = (TextView) findViewById(R.id.member_logout);

        ///menu bottom
        //btn_dashboard = (TextView) findViewById(R.id.member_dashboard);
        btn_reedem_hist = (TextView) findViewById(R.id.member_reedemhist);
        btn_profile = (TextView) findViewById(R.id.member_profile);

        //btn action
        btn_reedem_act = (TextView) findViewById(R.id.member_reedemreq);

        rcv_trx = (RecyclerView) findViewById(R.id.rcv_trx);
        tv_date = (TextView) findViewById(R.id.tvdate);
        tv_amt = (TextView) findViewById(R.id.tvamt);

        //header
        tv_memname = (TextView) findViewById(R.id.memname);

        // Inisialisasi SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = DashboardMemberActivity.this;
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

        tv_date.setText(formattedDate);


        // Aktivitas penerima
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Mendapatkan data dari Bundle
            String id = bundle.getString("id");
            String userid = bundle.getString("userid");
            String name = bundle.getString("name");


            tv_memname.setText(name);
            // Gunakan data sesuai kebutuhan
            // Misalnya, tampilkan data dalam logcat
            Log.d("AktivitasPenerima", "Nama: " + id);
            Log.d("AktivitasPenerima", "Userid: " + userid);
        } else {
            Log.e("AktivitasPenerima", "Bundle kosong");
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


        // on below line we are initializing our variables.
        et_tgl_fill = (EditText) findViewById(R.id.editTextDate);
        //dateEdt = findViewById(R.id.idEdtDate);

        btn_reedem_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(DashboardMemberActivity.this,"2");
            }
        });

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        et_tgl_fill.setText(date);
        tv_date.setText(date);

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
                        DashboardMemberActivity.this,
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
                                //Toast.makeText(DashboardMemberActivity.this, date, Toast.LENGTH_SHORT).show();

                                ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                if (date.trim().isEmpty()) {
                                    getHistoryTimbang(id_member, mContext, loading);
                                } else {
                                    fillTimbang(id_member, date, loading, mContext);
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

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardMemberActivity.this, UpdateDataMemberActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_reedem_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardMemberActivity.this, HistoryReedemActivity.class);
                startActivity(i);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int x = 0; x < dataList.size(); x++) {
                    globalData.removeData(dataList.get(x));
                }
                Intent i = new Intent(DashboardMemberActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                //Toasty.success(MainActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                Toast.makeText(DashboardMemberActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillTimbang(String id_member, String date, ProgressDialog loading, Context mContext) {

        dataService.GetTimbang_byMemberCode_Date(id_member,date).
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
                                else{
                                    getHistoryTimbang(id_member,mContext,loading);
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
                            //loading.dismiss();////
                            //mSwipeRefreshLayout.setRefreshing(false);//
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
                            //loading.dismiss();///
                            //mSwipeRefreshLayout.setRefreshing(true);//
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
                        //loading.dismiss();//
                        //mSwipeRefreshLayout.setRefreshing(false);//
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponJson(JSONArray dataArray,Context mContext, ProgressDialog loading) {
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

            tv_amt.setText(angkaFormatted);
            tv_memname.setText(nama+"\n("+id_member+")");

            getHistoryTimbang(id_member,mContext,loading);
        }
    }

    private void getHistoryTimbang(String id_member, Context mContext, ProgressDialog loading) {

        dataService.GetTimbang_byMemberCode(id_member).
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
        String kategori = "";
        String bobot = "";
        String rupiah = "";
        String tgl = "";



        list = new ArrayList<TrxSampahModel>();
        if (dataArray.length() > 0) {
            try {

                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject jo2 = dataArray.getJSONObject(i);
                    kategori = jo2.getString("namecategory");
                    bobot =  jo2.getString("qty")+" "+jo2.getString("uomname");
                    rupiah = jo2.getString("pricetot");
                    tgl = jo2.getString("date");


                    // Mengonversi string menjadi double
                    double angka = Double.parseDouble(jo2.getString("pricetot"));
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

                        list.add(new TrxSampahModel(kategori,bobot,angkaFormatted,formattedDate));

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }


                adapter = new TrxSampahAdapter(list,this);//array dimasukkan ke adapter
                // Memanggil notifyDataSetChanged() pada adapter.
                adapter.notifyDataSetChanged();
                rcv_trx.setAdapter(adapter);
                rcv_trx.setLayoutManager(new LinearLayoutManager(this));

            } catch (JSONException e) {
                e.printStackTrace();
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