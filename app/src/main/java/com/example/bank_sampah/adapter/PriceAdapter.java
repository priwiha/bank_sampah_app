package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.MasterPriceModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder>{

    private List<MasterPriceModel> data;
    private Context context;

    public PriceAdapter(List<MasterPriceModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
    }


    @NonNull
    @Override
    public PriceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_master, parent, false);
        return new PriceAdapter.ViewHolder(item,context);//1:1 adapter:activity
    }

    @Override
    public void onBindViewHolder(@NonNull PriceAdapter.ViewHolder holder, int position) {
        MasterPriceModel temp = data.get(position);
        String status = "";
        if (temp.getAktif().equals("Y")){
            status="Aktif";
        }
        else {
            status="Non-Aktif";
        }
        holder.id.setText(temp.getTglins());
        holder.nama.setText(temp.getKategori()+" Harga: "+temp.getPrice()+"/"+temp.getSatuan()+" ("+status+")");
    }

    @Override
    public int getItemCount() {
        //return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView nama;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.txtIdMaster);
            nama = (TextView) itemView.findViewById(R.id.txtNamaMaster);

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

    private static PriceAdapter.OnEditTextChanged onEditTextChanged;
    private static PriceAdapter.OnItemClickListener itemClickListener;
    private static PriceAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(PriceAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(PriceAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(PriceAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }
}
