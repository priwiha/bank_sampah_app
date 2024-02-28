package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.MemberDataModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemberDataAdapter extends RecyclerView.Adapter<MemberDataAdapter.ViewHolder> implements Filterable {

    private List<MemberDataModel> data;
    //private List<YourData> originalData;
    private List<MemberDataModel> filteredData;
    private Context context;

    public MemberDataAdapter(List<MemberDataModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
        this.filteredData = new ArrayList<>(data);
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
        String status="";
        if (temp.getStatus()=="Y") {
            status = "Aktif";
        }
        else {
            status = "Non-Aktif";
        }
        holder.nama.setText(temp.getName()+" ("+status+")");
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
                    List<MemberDataModel> filteredList = new ArrayList<>();
                    for (MemberDataModel item : data) {
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
                filteredData = (List<MemberDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
