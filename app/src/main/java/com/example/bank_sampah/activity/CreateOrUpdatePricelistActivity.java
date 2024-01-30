package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bank_sampah.R;

public class CreateOrUpdatePricelistActivity extends AppCompatActivity {

    private TextView back;
    private TextView save;

    private EditText kategori;
    private EditText price;
    private EditText satuan;
    private EditText tgl;
    private EditText status;

    private String vtipe;
    private String vkategori;
    private String vprice;
    private String vsatuan;
    private String vtgl;
    private String vstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_pricelist);

        back = (TextView) findViewById(R.id.btnBack);
        save = (TextView) findViewById(R.id.save_price_category);

        kategori = (EditText) findViewById(R.id.add_name_cat);
        price = (EditText) findViewById(R.id.add_harga_cat);
        satuan = (EditText) findViewById(R.id.add_satuan_cat);
        tgl = (EditText) findViewById(R.id.add_tanggal_cat);
        status = (EditText) findViewById(R.id.add_status_cat);

        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            vtipe       = intent.getStringExtra("add_or_update");
            vkategori   = "";
            vprice      = "";
            vsatuan     = "";
            vtgl        = "";
            vstatus     = "";

            if (vtipe.equals("update"))
            {
                save.setVisibility(View.GONE);

                vkategori   = intent.getStringExtra("kategori");
                vprice      = intent.getStringExtra("price");
                vsatuan     = intent.getStringExtra("satuan");
                vtgl        = intent.getStringExtra("tgl");
                vstatus     = intent.getStringExtra("status");

                kategori.setText(vkategori);
                price.setText(vprice);
                satuan.setText(vsatuan);
                tgl.setText(vtgl);
                status.setText(vstatus);

                kategori.setEnabled(false);
                price.setEnabled(false);
                satuan.setEnabled(false);
                tgl.setEnabled(false);
                status.setEnabled(false);

            }
            else {
                kategori.setText(vkategori);
                price.setText(vprice);
                satuan.setText(vsatuan);
                tgl.setText(vtgl);
                status.setText(vstatus);

                satuan.setVisibility(View.GONE);
                tgl.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
            }

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateOrUpdatePricelistActivity.this, MasterPriceActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}