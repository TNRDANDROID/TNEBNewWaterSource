package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.activity.PendingScreen;
import com.nic.tnebnewwatersource.databinding.BankDetailsListAdapterBinding;
import com.nic.tnebnewwatersource.databinding.ConnectionListNewAdapterBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

public class BankAccountDetailsAdapter extends RecyclerView.Adapter<BankAccountDetailsAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<TNEBSystem> filteredConnList;
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;


    public BankAccountDetailsAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;
        this.filteredConnList = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        BankDetailsListAdapterBinding bankDetailsListAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.bank_details_list_adapter, viewGroup, false);
        return new BankAccountDetailsAdapter.MyViewHolder(bankDetailsListAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bankDetailsListAdapterBinding.accountNumber.setText(list.get(position).getAccount_no());
        holder.bankDetailsListAdapterBinding.ifsc.setText(list.get(position).getIfsc_code());
        holder.bankDetailsListAdapterBinding.accountName.setText(list.get(position).getAccount_name());

        holder.bankDetailsListAdapterBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accId=list.get(position).getAccount_id();
                ((PendingScreen)context).save_and_delete_bank_alert(accId,"delete");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private BankDetailsListAdapterBinding bankDetailsListAdapterBinding;

        public MyViewHolder(BankDetailsListAdapterBinding Binding) {
            super(Binding.getRoot());
            bankDetailsListAdapterBinding = Binding;
        }
    }
    @Override
    public Filter getFilter() {
        return null;
    }

}
