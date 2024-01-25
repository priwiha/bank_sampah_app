package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.MainActivity;
import com.example.bank_sampah.R;

public class UpdateDataMemberActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView btnSaveProfile;
    private TextView btnMoveChpass;

    private TextView btnSavePass;
    private TextView btnMoveUpdateProf;

    private LinearLayout ltChProf;
    private LinearLayout ltChPass;

    //private TextView btn_dashboard;


    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_member);

        btnBack = (TextView) findViewById(R.id.member_back);
        btnSaveProfile = (TextView) findViewById(R.id.member_ch_profile);
        btnMoveChpass = (TextView) findViewById(R.id.member_ch_pass_move);
        btnSavePass = (TextView) findViewById(R.id.member_save_pass);
        btnMoveUpdateProf = (TextView) findViewById(R.id.member_updateprof_move);

        //btn_dashboard = (TextView) findViewById(R.id.member_dashboard);

        ltChProf = (LinearLayout) findViewById(R.id.member_chprofile);
        ltChPass = (LinearLayout) findViewById(R.id.member_chpass);

        ltChProf.setVisibility(View.VISIBLE);
        ltChPass.setVisibility(View.GONE);
        btnMoveChpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ltChProf.setVisibility(View.GONE);
                ltChPass.setVisibility(View.VISIBLE);
            }
        });

        /*btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateDataMemberActivity.this, DashboardMemberActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        btnMoveUpdateProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ltChProf.setVisibility(View.VISIBLE);
                ltChPass.setVisibility(View.GONE);
            }
        });


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