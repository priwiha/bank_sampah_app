package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;

public class UpdateHistoryTimbangActivity extends AppCompatActivity {
    private TextView btn_back;
    private Spinner et_cat;
    private EditText et_sat;
    private EditText et_bobot;

    private TextView btn_save;


    private String tipe="";
    private String id="";
    private String ket="";
    private String tgl="";
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_history_timbang);

        btn_back = (TextView) findViewById(R.id.btnBack);
        et_cat = (Spinner) findViewById(R.id.spn_satuan_cat);
        et_sat = (EditText) findViewById(R.id.select_satuan);
        et_bobot = (EditText) findViewById(R.id.input_bobot);
        btn_save = (TextView) findViewById(R.id.save_timbang);

        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            tipe    = intent.getStringExtra("add_or_update");


            if (tipe.equals("update"))
            {

                id=intent.getStringExtra("id");
                ket=intent.getStringExtra("ket");
                tgl=intent.getStringExtra("tgl");
            }
            else {
            }

            et_bobot.setText(ket);
        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateHistoryTimbangActivity.this, HistoryTimbangActivity.class);
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