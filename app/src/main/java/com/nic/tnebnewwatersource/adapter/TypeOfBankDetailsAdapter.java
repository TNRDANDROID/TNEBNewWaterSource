package com.nic.tnebnewwatersource.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.activity.BankDetailActivity;
import com.nic.tnebnewwatersource.databinding.BankDetailsListAdapterBinding;
import com.nic.tnebnewwatersource.databinding.TypesOfBankDetailsAdapterBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

public class TypeOfBankDetailsAdapter extends RecyclerView.Adapter<TypeOfBankDetailsAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;

    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;


    public TypeOfBankDetailsAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;

    }
    @NonNull
    @Override
    public TypeOfBankDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TypesOfBankDetailsAdapterBinding typesOfBankDetailsAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.types_of_bank_details_adapter, parent, false);
        return new TypeOfBankDetailsAdapter.MyViewHolder(typesOfBankDetailsAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull TypeOfBankDetailsAdapter.MyViewHolder holder, int position) {

        holder.bankDetailsListAdapterBinding.connectionStatus.setText(list.get(position).getAccount_name());
        if(list.get(position).getAccount_saved_status().equals("Y")){
            holder.bankDetailsListAdapterBinding.arrowLayout.setBackground(context.getResources().getDrawable(R.drawable.dark_green_pending_adapter_bg));
            holder.bankDetailsListAdapterBinding.arrowIc.setTextColor(context.getResources().getColor(R.color.white));
            holder.bankDetailsListAdapterBinding.arrowIc.setText(context.getResources().getString(R.string.added_bank));
        }
        else {
            holder.bankDetailsListAdapterBinding.arrowLayout.setBackground(context.getResources().getDrawable(R.drawable.mild_green_pendind_adapter_bg));
            holder.bankDetailsListAdapterBinding.arrowIc.setTextColor(context.getResources().getColor(R.color.grey_8));
            holder.bankDetailsListAdapterBinding.arrowIc.setText(context.getResources().getString(R.string.add_bank));
        }

        holder.bankDetailsListAdapterBinding.arrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((BankDetailActivity)context).addBankDetailsView(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.bankDetailsListAdapterBinding.infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;

//                ((BankDetailActivity)context).showInformationAccount(list.get(position).getAccount_id());
                ((BankDetailActivity)context).showPopUp(activity,list.get(position).getAccount_id());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TypesOfBankDetailsAdapterBinding bankDetailsListAdapterBinding;

        public MyViewHolder(TypesOfBankDetailsAdapterBinding Binding) {
            super(Binding.getRoot());
            bankDetailsListAdapterBinding = Binding;
        }
    }


    public static void showAlert1(Context context, String msg){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button dialogButton1 = (Button) dialog.findViewById(R.id.btn_cancel);
            dialogButton1.setVisibility(View.GONE);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
