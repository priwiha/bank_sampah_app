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

public class UpdateHistoryReedemActivity extends AppCompatActivity {
    private TextView btn_back;
    private EditText inputreedem;
    private TextView save;

    private String tipe="";
    private String id="";
    private String ket="";
    private String tgl="";
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_history_reedem);

        btn_back = (TextView) findViewById(R.id.btnBack);
        inputreedem = (EditText) findViewById(R.id.input_reedem);
        //save = (TextView) findViewById(R.id.save_reedem);

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

            inputreedem.setText(ket);
        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateHistoryReedemActivity.this, HistoryReedemAdminActivity.class);
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