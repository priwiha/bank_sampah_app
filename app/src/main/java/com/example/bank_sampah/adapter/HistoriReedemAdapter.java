package com.example.bank_sampah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.HistoriReedemModel;
import com.example.bank_sampah.model.TrxSampahModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoriReedemAdapter extends RecyclerView.Adapter<HistoriReedemAdapter.ViewHolder>{
    private List<HistoriReedemModel> data;
    private Context context;

    public HistoriReedemAdapter(List<HistoriReedemModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
    }


    @NonNull
    @Override
    public HistoriReedemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reedem, parent, false);
        return new HistoriReedemAdapter.ViewHolder(item,context);//1:1 adapter:activity
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HistoriReedemAdapter.ViewHolder holder, int position) {
        HistoriReedemModel temp = data.get(position);
        holder.tgl.setText(temp.getTgl());
        holder.amt.setText(temp.getAmt());
        String status = "Approve";
        if (temp.getStatus().trim().equals("0")){
            status = "Belum Approve";
            holder.status.setTextColor(R.color.softmaroon);
        }
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        //return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tgl;
        private TextView amt;

        private TextView status;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tgl = (TextView) itemView.findViewById(R.id.txtTgl);
            amt = (TextView) itemView.findViewById(R.id.txtRp);
            status = (TextView) itemView.findViewById(R.id.txtfield1);

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

    private static HistoriReedemAdapter.OnEditTextChanged onEditTextChanged;
    private static HistoriReedemAdapter.OnItemClickListener itemClickListener;
    private static HistoriReedemAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(HistoriReedemAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(HistoriReedemAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(HistoriReedemAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }
}
