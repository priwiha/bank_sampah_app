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

public class ApprovalOrUpdateMemberActivity extends AppCompatActivity {

    private TextView back;
    private TextView save;

    private EditText id;
    private EditText nama;
    private EditText mail;
    private EditText phone;
    private EditText pass;
    private EditText status;

    private String vtipe ="";
    private String vid ="";
    private String vuser ="";
    private String vnama="";
    private String vmail="";
    private String vphone="";
    private String vpass="";
    private String vstatus="";

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_or_update_member);

        back = (TextView) findViewById(R.id.btnBack);
        save = (TextView) findViewById(R.id.save_approve_member);

        id = (EditText) findViewById(R.id.et_id_member);
        nama= (EditText) findViewById(R.id.et_name_member);
        mail= (EditText) findViewById(R.id.et_mail_member);
        phone= (EditText) findViewById(R.id.et_mail_member);
        pass= (EditText) findViewById(R.id.et_phone_member);
        status= (EditText) findViewById(R.id.et_Status_member);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ApprovalOrUpdateMemberActivity.this, MasterMemberActivity.class);
                startActivity(i);
                finish();
            }
        });



        Intent intent = getIntent();
        if (intent.hasExtra("add_or_update")) {
            vtipe       = intent.getStringExtra("add_or_update");


            if (vtipe.equals("update"))
            {
                vid = intent.getStringExtra("id");
                vuser = intent.getStringExtra("username");
                vnama= intent.getStringExtra("name");
                vmail= intent.getStringExtra("mail");
                vphone= intent.getStringExtra("phone");
                vpass= intent.getStringExtra("pass");
                vstatus= intent.getStringExtra("status");

                id.setText(vid);
                nama.setText(vnama);
                mail.setText(vmail);
                phone.setText(vphone);
                pass.setText(vpass);
                status.setText(vstatus);


            }
            else {
                id.setText(vid);
                nama.setText(vnama);
                mail.setText(vmail);
                phone.setText(vphone);
                pass.setText(vpass);
                status.setText(vstatus);

            }

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