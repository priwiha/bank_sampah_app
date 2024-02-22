package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.AdminMenuAdapter;
import com.example.bank_sampah.model.AdminMenuModel;
import com.example.bank_sampah.utility.GlobalData;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

public class HomeAdminActivity extends AppCompatActivity {

    private RecyclerView rcv_menu;
    private List<AdminMenuModel> list;
    private AdminMenuAdapter adapter;

    private TextView btn_scan;
    private EditText input_kode;

    private int offset_m=0;
    private int dariSearchC=0;

    boolean doubleBackToExitPressedOnce = false;

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    //global var

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        btn_scan = (TextView) findViewById(R.id.btnscan);
        input_kode = (EditText) findViewById(R.id.input_code);


        rcv_menu = (RecyclerView) findViewById(R.id.rcv_menu);

        //Toast.makeText(HomeAdminActivity.this,"cek get global var "+userid,Toast.LENGTH_SHORT).show();

        // Aktivitas penerima
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Mendapatkan data dari Bundle
            String id = bundle.getString("id");
            String userid = bundle.getString("userid");
            String name = bundle.getString("name");

            // Gunakan data sesuai kebutuhan
            // Misalnya, tampilkan data dalam logcat
            Log.d("AktivitasPenerima", "Nama: " + id);
            Log.d("AktivitasPenerima", "Userid: " + userid);
        } else {
            Log.e("AktivitasPenerima", "Bundle kosong");
        }

        input_kode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Search Customer & bisa pake barcode
        input_kode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("SEARCH MAN. ", "MASUK");
                    Log.e("SEARCH MAN. KEY", v.getText().toString());
                    offset_m = 0;
                    //list_cust = new ArrayList<>();
                    dariSearchC = 1;
                    //populate_cust(offset_b, srch_cust.getText().toString(), ListAllCustomer.this);
                    dariSearchC = 0;
                    Toast.makeText(HomeAdminActivity.this,
                            "Id Member : " +input_kode.getText().toString(), Toast.LENGTH_SHORT).show();

                    if (!input_kode.getText().toString().trim().isEmpty()){

                        Intent i = new Intent(HomeAdminActivity.this, TransactionMenuActivity.class);
                        i.putExtra("idmember", input_kode.getText().toString());
                        startActivity(i);
                    }

                    input_kode.setText("");
                }
                //barcode
                else if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    input_kode.setSelection(0);
                    Log.e("Scan Barcode", String.valueOf(input_kode.getText()));

                    input_kode.requestFocus();
                    Toast.makeText(HomeAdminActivity.this,
                            "Id Member QR: " +input_kode.getText().toString(), Toast.LENGTH_SHORT).show();

                    if (!input_kode.getText().toString().trim().isEmpty()){

                        Intent i = new Intent(HomeAdminActivity.this, TransactionMenuActivity.class);
                        i.putExtra("idmember", input_kode.getText().toString());
                        startActivity(i);
                    }

                    input_kode.setText("");
                }
                //END
                return false;
            }
        });
        // END




        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeAdminActivity.this, QRCodeScannerActivity.class);
                startActivity(i);
            }
        });


        list = new ArrayList<AdminMenuModel>();
        /*for (int x = 0; x < 10; x++) {

            String id = String.valueOf(x);
            String nama =  "Menu "+ x;
            String image = " ";

            list.add(new AdminMenuModel(id,nama,image));
        }*/
        list.add(new AdminMenuModel("1","Kategori",""));
        list.add(new AdminMenuModel("2","Price",""));
        list.add(new AdminMenuModel("3","Member",""));
        //list.add(new AdminMenuModel("4","Reedem",""));
        list.add(new AdminMenuModel("4","Log Out",""));
        adapter = new AdminMenuAdapter(list,this);//array dimasukkan ke adapter
        rcv_menu.setAdapter(adapter);
        rcv_menu.setLayoutManager(new GridLayoutManager(this,3));


        adapter.setOnItemClickListener(new AdminMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("cek id",list.get(position).getIdmenu().toString());

                if (list.get(position).getIdmenu().toString().equals("1"))
                {
                    Intent i = new Intent(HomeAdminActivity.this, MasterKategoriActivity.class);
                    //i.putExtra("userid", userid);
                    startActivity(i);
                } else if (list.get(position).getIdmenu().toString().equals("2")) {
                    Intent i = new Intent(HomeAdminActivity.this, MasterPriceActivity.class);
                    //i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);
                } else if (list.get(position).getIdmenu().toString().equals("3")){
                    Intent i = new Intent(HomeAdminActivity.this, MasterMemberActivity.class);
                    //i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);
                //} else if (list.get(position).getIdmenu().toString().equals("4")){
                    /*Intent i = new Intent(HomeAdminActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();*/
                }else if (list.get(position).getIdmenu().toString().equals("4")){
                    globalData.removeData(userid);
                    Intent i = new Intent(HomeAdminActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

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