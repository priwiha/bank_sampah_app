package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bank_sampah.MainActivity;
import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.AdminMenuAdapter;
import com.example.bank_sampah.adapter.TransactionMenuAdapter;
import com.example.bank_sampah.model.AdminMenuModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionMenuActivity extends AppCompatActivity {

    private TextView tvNama;

    private String id_member;

    private RecyclerView rcv_menu;
    private List<AdminMenuModel> list;
    private TransactionMenuAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_menu);

        tvNama = (TextView) findViewById(R.id.tvnama);

        rcv_menu = (RecyclerView) findViewById(R.id.rcv_menu);

        Intent intent = getIntent();
        if (intent.hasExtra("idmember")) {
            Log.e("DATA DARI HOME ", intent.getStringExtra("idmember"));
            id_member = intent.getStringExtra("idmember");
            tvNama.setText("Nama Member - "+id_member);
        }


        list = new ArrayList<AdminMenuModel>();
        list.add(new AdminMenuModel("1","Timbang Bobot",""));
        list.add(new AdminMenuModel("2","Histori Timbang",""));
        list.add(new AdminMenuModel("3","Histori Reedem",""));
        list.add(new AdminMenuModel("4","Back",""));
        adapter = new TransactionMenuAdapter(list,this);//array dimasukkan ke adapter
        rcv_menu.setAdapter(adapter);
        rcv_menu.setLayoutManager(new GridLayoutManager(this,3));




        adapter.setOnItemClickListener(new TransactionMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e("cek id",list.get(position).getIdmenu().toString());

                if (list.get(position).getIdmenu().toString().equals("1"))
                {
                    /*Intent i = new Intent(POListActivity.this, PODetailActivity.class);
                    i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);*/
                } else if (list.get(position).getIdmenu().toString().equals("2")) {
                    /*Intent i = new Intent(POListActivity.this, PODetailActivity.class);
                    i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);*/
                } else if (list.get(position).getIdmenu().toString().equals("3")){
                    /*Intent i = new Intent(POListActivity.this, PODetailActivity.class);
                    i.putExtra("kode_po", list.get(position).getNopo().toString());
                    startActivity(i);*/
                } else if (list.get(position).getIdmenu().toString().equals("4")){
                    /*Intent i = new Intent(HomeAdminActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();*/
                    finish();
                }

            }
        });
    }
}