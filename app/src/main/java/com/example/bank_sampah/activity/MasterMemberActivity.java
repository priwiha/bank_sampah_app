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
import com.example.bank_sampah.adapter.MasterDataAdapter;
import com.example.bank_sampah.adapter.MemberDataAdapter;
import com.example.bank_sampah.model.MasterDataModel;
import com.example.bank_sampah.model.MemberDataModel;

import java.util.ArrayList;
import java.util.List;

public class MasterMemberActivity extends AppCompatActivity {
    private RecyclerView rcv_master;
    private List<MemberDataModel> list;
    private MemberDataAdapter adapter;

    private TextView btn_add;
    private TextView btn_back;

    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_member);

        rcv_master = (RecyclerView) findViewById(R.id.rcv_datamaster);
        btn_add = (TextView) findViewById(R.id.btnAddCat);
        btn_back = (TextView) findViewById(R.id.btnBack);


        list = new ArrayList<MemberDataModel>();
        for (int x = 0; x < 10; x++) {

            String id = "No Member"+x;
            String nama = "Nama Member "+x;
            //String satuan = "Satuan "+x;
            list.add(new MemberDataModel(id,"username "+x,nama ,"mail "+x,"phone "+x,"123456","Y"));
        }
        adapter = new MemberDataAdapter(list,this);//array dimasukkan ke adapter
        rcv_master.setAdapter(adapter);
        rcv_master.setLayoutManager(new LinearLayoutManager(this));

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for (int x = 0; x < 5; x++) {
                //Toast.makeText(MasterKategoriActivity.this,list.get(x).getName().toString(),Toast.LENGTH_SHORT).show();
                //}
                Toast.makeText(MasterMemberActivity.this,"Add New Member",Toast.LENGTH_SHORT).show();

                /*Intent i = new Intent(MasterMemberActivity.this, CreateOrUpdateCategoryActivity.class);
                i.putExtra("add_or_update", "add");
                startActivity(i);
                finish();*/
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MasterMemberActivity.this, HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        });


        adapter.setOnItemClickListener(new MemberDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(MasterMemberActivity.this,list.get(position).getName().toString(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MasterMemberActivity.this, ApprovalOrUpdateMemberActivity.class);
                i.putExtra("add_or_update", "update");
                i.putExtra("id", list.get(position).getId().toString());
                i.putExtra("username", list.get(position).getUsername().toString());
                i.putExtra("name", list.get(position).getName().toString());
                i.putExtra("phone", list.get(position).getPhone().toString());
                i.putExtra("mail", list.get(position).getMail().toString());
                i.putExtra("pass", list.get(position).getPass().toString());
                i.putExtra("status", list.get(position).getStatus().toString());
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