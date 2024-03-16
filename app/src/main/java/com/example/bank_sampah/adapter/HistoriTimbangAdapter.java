package com.example.bank_sampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.bank_sampah.R;
import com.example.bank_sampah.model.HistoriTransactionModel;
import com.example.bank_sampah.model.MasterPriceModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoriTimbangAdapter extends RecyclerView.Adapter<HistoriTimbangAdapter.ViewHolder> implements Filterable {
    private List<HistoriTransactionModel> data;
    private List<HistoriTransactionModel> filteredData;
    private Context context;

    public HistoriTimbangAdapter(List<HistoriTransactionModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }


    @NonNull
    @Override
    public HistoriTimbangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reedem, parent, false);
        return new HistoriTimbangAdapter.ViewHolder(item, context);//1:1 adapter:activity
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
        //return data.size();
        if (filteredData != null) {
            return filteredData.size();
        } else {
            return 0; // Or return any default size as needed
        }
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
                    List<HistoriTransactionModel> filteredList = new ArrayList<>();
                    for (HistoriTransactionModel item : data) {
                        String formattedDate = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String dateString=item.getTgl();
                            // Konversi string menjadi objek LocalDate
                            LocalDate date = LocalDate.parse(dateString, dateFormatter);

                            // Format LocalDate menjadi string dengan pola yang diinginkan
                            formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        }

                        if (formattedDate.contains(filterPattern)) {

                            filteredList.add(item);
                        }
                    }
                    results.values = filteredList;
                    //return results;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (List<HistoriTransactionModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
                    } else if (longItemClickListener != null) {
                        longItemClickListener.onLongItemClick(v, getLayoutPosition());
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

    public interface OnEditTextChanged {
        void onTextChanged(int position, String charSeq);
    }

    private static HistoriTimbangAdapter.OnEditTextChanged onEditTextChanged;
    private static HistoriTimbangAdapter.OnItemClickListener itemClickListener;
    private static HistoriTimbangAdapter.OnLongItemClickListener longItemClickListener;

    public void setOnItemClickListener(HistoriTimbangAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(HistoriTimbangAdapter.OnLongItemClickListener listener) {
        this.longItemClickListener = listener;
    }

    public void setOnEditTextChanged(HistoriTimbangAdapter.OnEditTextChanged listener) {
        this.onEditTextChanged = listener;
    }
}
