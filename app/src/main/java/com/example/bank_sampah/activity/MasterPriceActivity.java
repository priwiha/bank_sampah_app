package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.PriceAdapter;
import com.example.bank_sampah.model.MasterPriceModel;

import java.util.ArrayList;
import java.util.List;

public class MasterPriceActivity extends AppCompatActivity {

    private RecyclerView rcv_master;
    private List<MasterPriceModel> list;
    private PriceAdapter adapter;

    private TextView btn_add;
    private TextView btn_back;


    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_price);

        rcv_master = (RecyclerView) findViewById(R.id.rcv_datamaster);
        btn_add = (TextView) findViewById(R.id.btnAddCat);
        btn_back = (TextView) findViewById(R.id.btnBack);


        list = new ArrayList<MasterPriceModel>();
        for (int x = 0; x < 10; x++) {

            String id = "No "+x;
            String nama = "Kategori "+x;
            String satuan = "Satuan "+x;
            list.add(new MasterPriceModel(nama,String.valueOf(x)+",00",satuan,"Y","date","id "+x));
        }
        adapter = new PriceAdapter(list,this);//array dimasukkan ke adapter
        rcv_master.setAdapter(adapter);
        rcv_master.setLayoutManager(new LinearLayoutManager(this));

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for (int x = 0; x < 5; x++) {
                //Toast.makeText(MasterKategoriActivity.this,list.get(x).getName().toString(),Toast.LENGTH_SHORT).show();
                //}
                Toast.makeText(MasterPriceActivity.this,"Add New Category",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MasterPriceActivity.this, CreateOrUpdatePricelistActivity.class);
                i.putExtra("add_or_update", "add");
                startActivity(i);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MasterPriceActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        });


        adapter.setOnItemClickListener(new PriceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(MasterPriceActivity.this,list.get(position).getKategori().toString(),Toast.LENGTH_SHORT).show();
                String status ="";
                if (list.get(position).getAktif().toString().equals("Y")){
                    status="Aktif";
                }
                else {
                    status="Non-Aktif";
                }
                Intent i = new Intent(MasterPriceActivity.this, CreateOrUpdatePricelistActivity.class);
                i.putExtra("add_or_update", "update");
                i.putExtra("kategori", list.get(position).getKategori().toString());
                i.putExtra("price", list.get(position).getPrice().toString());
                i.putExtra("satuan", list.get(position).getSatuan().toString());
                i.putExtra("tgl", list.get(position).getTglins().toString());
                i.putExtra("status", status/*list.get(position).getAktif().toString()*/);
                i.putExtra("id", list.get(position).getId().toString());
                startActivity(i);
                finish();
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