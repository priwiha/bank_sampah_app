package com.example.bank_sampah.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.AdminMenuModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionMenuAdapter  extends RecyclerView.Adapter<TransactionMenuAdapter.ViewHolder>{
    private List<AdminMenuModel> data;
    private Context context;

    public TransactionMenuAdapter(List<AdminMenuModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new TransactionMenuAdapter.ViewHolder(item,context);//1:1 adapter:activity
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionMenuAdapter.ViewHolder holder, int position) {
        AdminMenuModel temp = data.get(position);
        holder.nama.setText(temp.getNamamenu());

        if (temp.getIdmenu().equals("1")){
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.scale);
            holder.image_Menu.setImageDrawable(drawable);

        } else if (temp.getIdmenu().equals("2")) {

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.history);
            holder.image_Menu.setImageDrawable(drawable);

        } else if (temp.getIdmenu().equals("3")) {

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.claim);
            holder.image_Menu.setImageDrawable(drawable);
        } else if (temp.getIdmenu().equals("4")) {

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.backarrow);
            holder.image_Menu.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        //return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nama;
        private ImageView image_Menu;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.txtNama);
            image_Menu = (ImageView) itemView.findViewById(R.id.imageMenu);

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

    private static TransactionMenuAdapter.OnEditTextChanged onEditTextChanged;
    private static TransactionMenuAdapter.OnItemClickListener itemClickListener;
    private static TransactionMenuAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(TransactionMenuAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(TransactionMenuAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(TransactionMenuAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }
}
