package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.MasterDataModel;
import com.example.bank_sampah.model.MasterPriceModel;
import com.example.bank_sampah.model.MemberDataModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MasterDataAdapter  extends RecyclerView.Adapter<MasterDataAdapter.ViewHolder> implements Filterable {

    private List<MasterDataModel> data;
    private List<MasterDataModel> filteredData;
    private Context context;

    public MasterDataAdapter(List<MasterDataModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }


    @NonNull
    @Override
    public MasterDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_master, parent, false);
        return new MasterDataAdapter.ViewHolder(item,context);//1:1 adapter:activity
    }

    @Override
    public void onBindViewHolder(@NonNull MasterDataAdapter.ViewHolder holder, int position) {
        MasterDataModel temp = data.get(position);
        holder.id.setText(temp.getName());
        holder.field1.setText(temp.getSatuan_nm());
        holder.nama.setText("Satuan per "+temp.getSatuan_nm());
        holder.field2.setText(temp.getName());

        //if (temp.getType().equals("kategori")) {
        holder.field1.setVisibility(View.GONE);
        holder.field2.setVisibility(View.GONE);
        //}
    }

    @Override
    public int getItemCount() {
        //return 0;
        //return data.size();
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                FilterResults results = new FilterResults();

                if (filterPattern.isEmpty()) {
                    results.values = data;
                } else {
                    List<MasterDataModel> filteredList = new ArrayList<>();
                    for (MasterDataModel item : data) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                    results.values = filteredList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (List<MasterDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView field1;
        private TextView nama;
        private TextView field2;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.txtIdMaster);
            nama = (TextView) itemView.findViewById(R.id.txtNamaMaster);
            field1 = (TextView) itemView.findViewById(R.id.txtfield1);
            field2 = (TextView) itemView.findViewById(R.id.txtfield2);


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

    private static MasterDataAdapter.OnEditTextChanged onEditTextChanged;
    private static MasterDataAdapter.OnItemClickListener itemClickListener;
    private static MasterDataAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(MasterDataAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(MasterDataAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener =  listener;
    }

    public void setOnEditTextChanged(MasterDataAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged =  listener;
    }
}
