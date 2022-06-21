package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.databinding.BankAccountDetailsAdapterServerViewBinding;
import com.nic.tnebnewwatersource.databinding.BankDetailsListAdapterBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

public class BankAccountDetailsAdapterServer extends RecyclerView.Adapter<BankAccountDetailsAdapterServer.MyViewHolder> implements Filterable {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<TNEBSystem> filteredConnList;
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;


    public BankAccountDetailsAdapterServer(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;
        this.filteredConnList = list;
    }


    @NonNull
    @Override
    public BankAccountDetailsAdapterServer.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        BankAccountDetailsAdapterServerViewBinding bankAccountDetailsAdapterServerViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.bank_account_details_adapter_server_view, viewGroup, false);
        return new BankAccountDetailsAdapterServer.MyViewHolder(bankAccountDetailsAdapterServerViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull BankAccountDetailsAdapterServer.MyViewHolder holder, int position) {

        holder.bankAccountDetailsAdapterServerViewBinding.accountNumber.setText(list.get(position).getAccount_no());
        holder.bankAccountDetailsAdapterServerViewBinding.bankBranch.setText(list.get(position).getBank_name()+" & "+list.get(position).getBranch_name());
        holder.bankAccountDetailsAdapterServerViewBinding.ifscCode.setText(list.get(position).getIfsc_code());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private BankAccountDetailsAdapterServerViewBinding bankAccountDetailsAdapterServerViewBinding;

        public MyViewHolder(BankAccountDetailsAdapterServerViewBinding Binding) {
            super(Binding.getRoot());
            bankAccountDetailsAdapterServerViewBinding = Binding;
        }
    }
    @Override
    public Filter getFilter() {
        return null;
    }

}
