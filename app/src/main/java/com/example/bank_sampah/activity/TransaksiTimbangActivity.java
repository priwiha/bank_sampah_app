package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;

public class TransaksiTimbangActivity extends AppCompatActivity {
    private TextView btn_back;

    private EditText et_cat;
    private EditText et_sat;
    private EditText et_bobot;

    private TextView btn_save;

    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_timbang);

        btn_back = (TextView) findViewById(R.id.btnBack);
        et_cat = (EditText) findViewById(R.id.select_category);
        et_sat = (EditText) findViewById(R.id.select_satuan);
        et_bobot = (EditText) findViewById(R.id.input_bobot);
        btn_save = (TextView) findViewById(R.id.save_timbang);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransaksiTimbangActivity.this, TransactionMenuActivity.class);
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