package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.MainActivity;
import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.AdminMenuAdapter;
import com.example.bank_sampah.adapter.TransactionMenuAdapter;
import com.example.bank_sampah.model.AdminMenuModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionMenuActivity extends AppCompatActivity {

    private TextView tvNama;
    private TextView btn_back;


    private String id_member;

    private RecyclerView rcv_menu;
    private List<AdminMenuModel> list;
    private TransactionMenuAdapter adapter;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_menu);

        tvNama = (TextView) findViewById(R.id.tvnama);
        btn_back = (TextView) findViewById(R.id.btnBack);

        rcv_menu = (RecyclerView) findViewById(R.id.rcv_menu);

        Intent intent = getIntent();
        if (intent.hasExtra("idmember")) {
            Log.e("DATA DARI HOME ", intent.getStringExtra("idmember"));
            id_member = intent.getStringExtra("idmember");
            tvNama.setText("Nama Member - "+id_member);
        }


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