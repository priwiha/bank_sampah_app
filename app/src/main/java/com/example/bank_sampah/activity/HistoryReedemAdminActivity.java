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
import com.example.bank_sampah.adapter.HistoriTimbangAdapter;
import com.example.bank_sampah.model.HistoriTransactionModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryReedemAdminActivity extends AppCompatActivity {

    private TextView btn_back;


    private RecyclerView rcv_reedem;
    private List<HistoriTransactionModel> list;
    private HistoriTimbangAdapter adapter;

    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_reedem_admin);

        btn_back = (TextView) findViewById(R.id.btnBack);
        rcv_reedem = (RecyclerView) findViewById(R.id.rcv_reedem);

        list = new ArrayList<HistoriTransactionModel>();
        for (int x = 0; x < 10; x++) {

            String id = "0"+x;
            String tgl = "01/01/2000";
            String rupiah = x+".000,00 ";

            list.add(new HistoriTransactionModel(id,tgl,rupiah));
        }
        adapter = new HistoriTimbangAdapter(list,this);//array dimasukkan ke adapter
        rcv_reedem.setAdapter(adapter);
        rcv_reedem.setLayoutManager(new LinearLayoutManager(this));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoryReedemAdminActivity.this, TransactionMenuActivity.class);
                startActivity(i);
                finish();
            }
        });

        adapter.setOnItemClickListener(new HistoriTimbangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(HistoryReedemAdminActivity.this,list.get(position).getIdtrx().toString(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HistoryReedemAdminActivity.this, UpdateHistoryReedemActivity.class);
                i.putExtra("add_or_update", "update");
                i.putExtra("tgl", list.get(position).getTgl().toString());
                i.putExtra("ket", list.get(position).getKet().toString());
                i.putExtra("id", list.get(position).getIdtrx().toString());
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