package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.TrxSampahModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrxSampahAdapter extends RecyclerView.Adapter<TrxSampahAdapter.ViewHolder>{
    private List<TrxSampahModel> data;
    private Context context;

    public TrxSampahAdapter(List<TrxSampahModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
    }


    @NonNull
    @Override
    public TrxSampahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        return new TrxSampahAdapter.ViewHolder(item,context);//1:1 adapter:activity
    }

    @Override
    public void onBindViewHolder(@NonNull TrxSampahAdapter.ViewHolder holder, int position) {
        TrxSampahModel temp = data.get(position);
        holder.kategori.setText(temp.getKategori());
        holder.bobot.setText(temp.getBobot());
        holder.rupiah.setText(temp.getRupiah());
        holder.tgl.setText(temp.getTanggal());
    }


    @Override
    public int getItemCount() {
        //return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView kategori;
        private TextView bobot;
        private TextView rupiah;
        private TextView tgl;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            kategori = (TextView) itemView.findViewById(R.id.txtNama);
            bobot = (TextView) itemView.findViewById(R.id.txtKg);
            rupiah = (TextView) itemView.findViewById(R.id.txtRp);
            tgl = (TextView) itemView.findViewById(R.id.txttgl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getLayoutPosition());
                    }
                    else if(longItemClickListener != null){
                        longItemClickListener.onLongItemClick(v,getLayoutPosition());
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View itemView, int position);
    }

    public interface OnEditTextChanged{
        void onTextChanged(int position, String charSeq);
    }

    private static TrxSampahAdapter.OnEditTextChanged onEditTextChanged;
    private static TrxSampahAdapter.OnItemClickListener itemClickListener;
    private static TrxSampahAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(TrxSampahAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(TrxSampahAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(TrxSampahAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }
}
