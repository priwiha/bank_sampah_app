package com.example.bank_sampah.activity;

import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.bank_sampah.model.HistoriReedemModel;
import com.example.bank_sampah.utility.ApprovalDialog;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.ViewDialog;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.response.ApiResponse;
import com.example.bank_sampah.utility.network.service.DataService;
import com.google.gson.Gson;

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

public class TransaksiReedemActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView tvNama;
    private TextView tvtelp;
    private TextView tvdate;
    private TextView tvamt;

    private TextView btn_back;

    private EditText inputreedem;
    private TextView save;//tidak dipakai
    private RecyclerView rcv_reedem;
    private EditText et_tgl_fill;
    private List<HistoriReedemModel> list;
    private HistoriReedemAdapter adapter;
    private TextView btn_reedem_act;//ganti
    /////retrofit2
    private DataService dataService;
    private static final String TAG = TransaksiReedemActivity.class.getSimpleName();
    /////retrofit2
    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    String id_member = dataList.get(1);
    //global var

    private String idredeem,redeemamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_reedem);

        tvNama = (TextView) findViewById(R.id.tvnama);
        tvtelp = (TextView) findViewById(R.id.tvtelp);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvamt = (TextView) findViewById(R.id.tv_amt);

        btn_back = (TextView) findViewById(R.id.btnBack);
        inputreedem = (EditText) findViewById(R.id.input_reedem);
        //save = (TextView) findViewById(R.id.save_reedem);
        //btn action
        btn_reedem_act = (TextView) findViewById(R.id.adm_reedem);


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

        Context mContext = TransaksiReedemActivity.this;
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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransaksiReedemActivity.this, TransactionMenuActivity.class);
                startActivity(i);
                finish();
            }
        });


        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_trx_redeem(id_member,inputreedem.getText().toString(),userid,mContext);
            }
        });*/

        btn_reedem_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(TransaksiReedemActivity.this,"1");

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
                        mContext,
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
        dataService.GetWaitReedem_byMemberCode_Date(id_member,date).
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
                                        getResponListTimbangJson(dataArray,mContext);
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

    private void create_trx_redeem(String id_member, String inputreedem, String userid, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, price, Toast.LENGTH_SHORT).show();
        //Call<ApiResponse> call = dataService.CreateTimbang(id_member,kat_id,iduom,bobot,harga, userid);
        dataService.CreateRedeem(id_member,inputreedem,userid).enqueue(new Callback<ResponseBody>() {
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
                                        getResponRedeemJson(dataArray);
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

    private void getResponRedeemJson(JSONArray dataArray) {
        if (dataArray.length() > 0) {
            String idredeem="";
            String redeemamt="";
            String membercode="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    membercode = child.getString("membercode");
                    idredeem = child.getString("idredeem");
                    redeemamt = child.getString("redeemamt");



                }
                Log.e( "id transaksi"+idredeem,"member "+membercode+" berhasil diproses redeem "+redeemamt);

                Intent i = new Intent(TransaksiReedemActivity.this, HistoryReedemAdminActivity.class);
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

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show()
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
                                        getResponJson(dataArray,mContext,loading);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
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

    private void getResponJson(JSONArray dataArray, Context mContext, ProgressDialog loading) {
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

            // Mengonversi string menjadi double
            double angka = Double.parseDouble(amt);
            // Membuat format rupiah
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            // Menggunakan format rupiah untuk mengonversi angka menjadi string dengan separator
            String angkaFormatted = formatRupiah.format(angka);

            tvamt.setText(angkaFormatted);
            tvNama.setText(nama+" ("+id_member+")");
            tvtelp.setText("Phone : "+telp);

            getHistoryRedeem(id_member,mContext,loading);
        }
    }

    private void getHistoryRedeem(String id_member, Context mContext, ProgressDialog loading) {
        dataService.GetWaitReedem_byMemberCode(id_member).
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
                                        getResponListTimbangJson(dataArray,mContext);
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

    private void getResponListTimbangJson(JSONArray dataArray,Context mContext) {
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
                    approved = jo2.getString("approved");


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

        adapter.setOnItemClickListener(new HistoriReedemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                idredeem = list.get(position).getId();
                redeemamt = list.get(position).getAmt();
                //ApprovalDialog alert = new ApprovalDialog();
                //alert.showDialog(TransaksiReedemActivity.this,"Redeem");

                // Membuat dialog konfirmasi
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin approve data?");

                // Tombol "Ya"
                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang akan diambil jika pengguna menekan tombol "Ya"
                        // Misalnya, menyimpan data atau menjalankan tindakan lain
                        approveRedeem(idredeem,userid,id_member,mContext);
                    }
                });

                // Tombol "Batal"
                builder.setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
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

    private void approveRedeem(String idredeem, String userid, String id_member, Context mContext) {
        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        dataService.ApproveRedeem(idredeem,id_member,userid).
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
                                        //getResponListTimbangJson(dataArray,mContext);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                    Toast.makeText(mContext,MessageString,Toast.LENGTH_SHORT).show();
                                }

                                getHistoryRedeem(id_member,mContext,loading);

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