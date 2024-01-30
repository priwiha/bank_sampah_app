package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.MemberDataModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemberDataAdapter extends RecyclerView.Adapter<MemberDataAdapter.ViewHolder>{

    private List<MemberDataModel> data;
    private Context context;

    public MemberDataAdapter(List<MemberDataModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
    }


    @NonNull
    @Override
    public MemberDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_master, parent, false);
        return new MemberDataAdapter.ViewHolder(item,context);//1:1 adapter:activity
    }

    @Override
    public void onBindViewHolder(@NonNull MemberDataAdapter.ViewHolder holder, int position) {
        MemberDataModel temp = data.get(position);
        holder.id.setText(temp.getId());
        holder.nama.setText(temp.getName());
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

    private static MemberDataAdapter.OnEditTextChanged onEditTextChanged;
    private static MemberDataAdapter.OnItemClickListener itemClickListener;
    private static MemberDataAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(MemberDataAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(MemberDataAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(MemberDataAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }

}
