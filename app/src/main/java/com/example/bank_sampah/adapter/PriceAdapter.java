package com.example.bank_sampah.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank_sampah.R;
import com.example.bank_sampah.activity.MasterPriceActivity;
import com.example.bank_sampah.model.MasterPriceModel;
import com.example.bank_sampah.model.MemberDataModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> implements Filterable {

    private List<MasterPriceModel> data;
    private List<MasterPriceModel> filteredData;
    private Context context;
    private String formattedDate;

    public PriceAdapter(List<MasterPriceModel> Data, Context context) {
        this.data = Data;//model
        this.context = context;
        this.filteredData = new ArrayList<>(data);
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
        holder.id.setText(temp.getPrice()+"/"+temp.getSatuan());
        holder.nama.setText(temp.getTglins()+" ("+status+") "+temp.getKategori());


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
                    List<MasterPriceModel> filteredList = new ArrayList<>();
                    for (MasterPriceModel item : data) {
                        String formattedDate = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String dateString=item.getTglins();
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
                filteredData = (List<MasterPriceModel>) filterResults.values;
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
