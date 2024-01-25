package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.HistoriReedemAdapter;
import com.example.bank_sampah.adapter.TrxSampahAdapter;
import com.example.bank_sampah.model.HistoriReedemModel;
import com.example.bank_sampah.model.TrxSampahModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryReedemActivity extends AppCompatActivity {

    private TextView btnBack;


    private RecyclerView rcv_reedem;
    private List<HistoriReedemModel> list;
    private HistoriReedemAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_reedem);

        btnBack = (TextView) findViewById(R.id.member_back);
        rcv_reedem = (RecyclerView) findViewById(R.id.rcv_reedem);

        list = new ArrayList<HistoriReedemModel>();
        for (int x = 0; x < 10; x++) {

            String tgl = "01/01/2000";
            String rupiah = x+".000,00 ";

            list.add(new HistoriReedemModel(tgl,rupiah));
        }
        adapter = new HistoriReedemAdapter(list,this);//array dimasukkan ke adapter
        rcv_reedem.setAdapter(adapter);
        rcv_reedem.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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