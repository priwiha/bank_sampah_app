package com.example.bank_sampah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bank_sampah.activity.DashboardMemberActivity;
import com.example.bank_sampah.activity.HomeAdminActivity;


public class MainActivity extends AppCompatActivity {


    //layout
    private LinearLayout login_layout;
    private LinearLayout register_layout;

    //button layout login
    private Button lg_login_btn;
    private Button lg_register_btn;


    //button layout register
    private Button rg_register_btn;
    private Button rg_back_btn;


    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //layout
        login_layout = (LinearLayout) findViewById(R.id.layout_login_view);
        register_layout = (LinearLayout) findViewById(R.id.layout_register_view);

        //button layout login
        lg_login_btn = (Button) findViewById(R.id.LoginButtonLg);
        lg_register_btn = (Button) findViewById(R.id.RegisterButtonLg);

        //button layout login
        rg_register_btn = (Button) findViewById(R.id.RegisterButtonRg);
        rg_back_btn = (Button) findViewById(R.id.BackButtonRg);

        register_layout.setVisibility(View.GONE);

        //show register layout
        lg_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_layout.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);
            }
        });

        rg_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_layout.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.GONE);
            }
        });


        lg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(MainActivity.this, DashboardMemberActivity.class);
                Intent i = new Intent(MainActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();

                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
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