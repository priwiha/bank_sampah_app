package com.example.bank_sampah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.adapter.MasterDataAdapter;
import com.example.bank_sampah.model.MasterDataModel;

import java.util.ArrayList;
import java.util.List;

public class MasterKategoriActivity extends AppCompatActivity {

    private RecyclerView rcv_master;
    private List<MasterDataModel> list;
    private MasterDataAdapter adapter;

    private TextView btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_kategori);

        rcv_master = (RecyclerView) findViewById(R.id.rcv_datamaster);
        btn_add = (TextView) findViewById(R.id.btnAddCat);


        list = new ArrayList<MasterDataModel>();
        for (int x = 0; x < 10; x++) {

            String id = "No "+x;
            String nama = "Kategori "+x;

            list.add(new MasterDataModel(id,nama));
        }
        adapter = new MasterDataAdapter(list,this);//array dimasukkan ke adapter
        rcv_master.setAdapter(adapter);
        rcv_master.setLayoutManager(new LinearLayoutManager(this));

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int x = 0; x < 5; x++) {

                    Toast.makeText(MasterKategoriActivity.this,list.get(x).getName().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        adapter.setOnItemClickListener(new MasterDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(MasterKategoriActivity.this,list.get(position).getName().toString(),Toast.LENGTH_SHORT).show();
            }
        });


    }
}