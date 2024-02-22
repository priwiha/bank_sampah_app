package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import com.example.bank_sampah.adapter.TrxSampahAdapter;
import com.example.bank_sampah.model.TrxSampahModel;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.ViewDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardMemberActivity extends AppCompatActivity {
    ////////paket global var
    private String globalVariable;

    public String getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(String value) {
        globalVariable = value;
    }
    ////////paket global var

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


    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
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


        list = new ArrayList<TrxSampahModel>();
        for (int x = 0; x < 10; x++) {

            String kategori = "Kategori "+x;
            String bobot =  x+" kg";
            String rupiah = x+".000,00 ";
            String tgl = "01/01/2000";

            list.add(new TrxSampahModel(kategori,bobot,rupiah,tgl));
        }
        adapter = new TrxSampahAdapter(list,this);//array dimasukkan ke adapter
        rcv_trx.setAdapter(adapter);
        rcv_trx.setLayoutManager(new LinearLayoutManager(this));


        // on below line we are initializing our variables.
        et_tgl_fill = (EditText) findViewById(R.id.editTextDate);
        //dateEdt = findViewById(R.id.idEdtDate);

        btn_reedem_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(DashboardMemberActivity.this);
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
                                // on below line we are setting date to our edit text.
                                et_tgl_fill.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

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
                //Toasty.success(MainActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                Toast.makeText(DashboardMemberActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_reedem_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardMemberActivity.this, HistoryReedemActivity.class);
                startActivity(i);
                //Toasty.success(MainActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                Toast.makeText(DashboardMemberActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardMemberActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                //Toasty.success(MainActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                Toast.makeText(DashboardMemberActivity.this, "Success!", Toast.LENGTH_SHORT).show();
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