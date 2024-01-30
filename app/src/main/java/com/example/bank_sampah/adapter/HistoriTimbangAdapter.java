package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.HistoriTransactionModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoriTimbangAdapter extends RecyclerView.Adapter<HistoriTimbangAdapter.ViewHolder>{
private List<HistoriTransactionModel> data;
private Context context;

public HistoriTimbangAdapter(List<HistoriTransactionModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
        }


@NonNull
@Override
public HistoriTimbangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reedem, parent, false);
        return new HistoriTimbangAdapter.ViewHolder(item,context);//1:1 adapter:activity
        }

@Override
public void onBindViewHolder(@NonNull HistoriTimbangAdapter.ViewHolder holder, int position) {
    HistoriTransactionModel temp = data.get(position);
        holder.tgl.setText(temp.getTgl());
        holder.amt.setText(temp.getKet());
        }

@Override
public int getItemCount() {
        //return 0;
        return data.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView tgl;
    private TextView amt;
    public ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        tgl = (TextView) itemView.findViewById(R.id.txtTgl);
        amt = (TextView) itemView.findViewById(R.id.txtRp);

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

    private static HistoriTimbangAdapter.OnEditTextChanged onEditTextChanged;
    private static HistoriTimbangAdapter.OnItemClickListener itemClickListener;
    private static HistoriTimbangAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(HistoriTimbangAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(HistoriTimbangAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(HistoriTimbangAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }
}
