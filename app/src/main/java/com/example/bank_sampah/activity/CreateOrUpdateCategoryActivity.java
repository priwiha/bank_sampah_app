package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;

public class CreateOrUpdateCategoryActivity extends AppCompatActivity {

    private LinearLayout lt_add_cat;
    private LinearLayout lt_ch_cat;

    private TextView btn_add_cat;
    private TextView btn_ch_cat;

    private EditText etadd_name_cat;
    private EditText etch_name_cat;
    private EditText etch_satuan_cat;

    private TextView back;

    private String tipe ="";
    private String name = "";
    private String id="";
    private String satuan="";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_category);

        lt_add_cat = (LinearLayout) findViewById(R.id.add_categori_lt);
        lt_ch_cat = (LinearLayout) findViewById(R.id.ch_categori_lt);

        btn_add_cat = (TextView) findViewById(R.id.save_add_category);
        btn_ch_cat = (TextView) findViewById(R.id.save_ch_category);

        etadd_name_cat = (EditText) findViewById(R.id.add_name_cat);
        etch_name_cat = (EditText) findViewById(R.id.ch_name_cat);
        etch_satuan_cat = (EditText) findViewById(R.id.ch_satuan_cat);

        back = (TextView) findViewById(R.id.btnBack);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateOrUpdateCategoryActivity.this, MasterKategoriActivity.class);
                startActivity(i);
                finish();
            }
        });

        lt_add_cat.setVisibility(View.VISIBLE);
        lt_ch_cat.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            tipe    = intent.getStringExtra("add_or_update");
            name    = "";
            id      = "";

            if (tipe.equals("update"))
            {
                lt_add_cat.setVisibility(View.GONE);
                lt_ch_cat.setVisibility(View.VISIBLE);

                name=intent.getStringExtra("name");
                id=intent.getStringExtra("id");
                satuan=intent.getStringExtra("satuan");
            }
            else {
                lt_add_cat.setVisibility(View.VISIBLE);
                lt_ch_cat.setVisibility(View.GONE);
            }
            etch_name_cat.setText(name);
            etch_satuan_cat.setText(satuan);
        }
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