package com.nic.tnebnewwatersource.adapter;

import android.app.Activity;
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
import com.nic.tnebnewwatersource.activity.ConnectionCheckActivity;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ConnectionListAdapterBinding;
import com.nic.tnebnewwatersource.databinding.ConnectionListNewAdapterBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConnectionListAdapter extends RecyclerView.Adapter<ConnectionListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<TNEBSystem> filteredConnList;
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;

    public ConnectionListAdapter(Context context, List<TNEBSystem> list, dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;
        this.filteredConnList = list;
    }

    @NonNull
    @Override
    public ConnectionListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ConnectionListNewAdapterBinding connectionListAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.connection_list_new_adapter, viewGroup, false);
        return new ConnectionListAdapter.MyViewHolder(connectionListAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionListAdapter.MyViewHolder holder, final int position) {

        holder.connectionListAdapterBinding.consumerName.setText(context.getResources().getString(R.string.name)+" - "+list.get(position).getConsumer_name());
        holder.connectionListAdapterBinding.location.setText(context.getResources().getString(R.string.location)+" - "+list.get(position).getLocation());
        holder.connectionListAdapterBinding.connectionNumber.setText(context.getResources().getString(R.string.connection_no)+" - "+list.get(position).getConnection_number());
        holder.connectionListAdapterBinding.purposeAsPerTneb.setText(context.getResources().getString(R.string.purpose_as_per_tneb)+" - "+list.get(position).getPurpose_as_per_tneb());

        if(list.get(position).getPHOTO_SAVED_STATUS().equals("Y")){
            //holder.connectionListAdapterBinding.savedStatusLl.setBackground(context.getResources().getDrawable(R.drawable.new_bg_green_rect_bootm_curve));
            holder.connectionListAdapterBinding.statusRlNew.setBackground(context.getResources().getDrawable(R.drawable.status_completed_bg));
            //holder.connectionListAdapterBinding.statusNewRl.setBackground(context.getResources().getDrawable(R.drawable.status_complete_strong_bg_green));
        }
        else {
            //holder.connectionListAdapterBinding.savedStatusLl.setBackground(context.getResources().getDrawable(R.drawable.new_bg_log_in_btn_purple));
            holder.connectionListAdapterBinding.statusRlNew.setBackground(context.getResources().getDrawable(R.drawable.status_not_complete_bg));
            //holder.connectionListAdapterBinding.statusNewRl.setBackground(context.getResources().getDrawable(R.drawable.status_not_complete_strong_bg));
        }

        holder.connectionListAdapterBinding.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline()) {
                    ((ConnectionCheckActivity) context).viewSavedDetails(list.get(position).getId());
                }else {
                    Activity activity = (Activity) context;
                    Utils.showAlert(activity,activity.getResources().getString(R.string.no_internet_connection));
                }
            }
        });
        holder.connectionListAdapterBinding.updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ConnectionCheckActivity)context).viewConnectionDetail(list,position);
            }
        });
        holder.connectionListAdapterBinding.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline()) {
                    ((ConnectionCheckActivity) context).viewSavedDetails(list.get(position).getId());
                }else {
                    Activity activity = (Activity) context;
                    Utils.showAlert(activity,activity.getResources().getString(R.string.no_internet_connection));
                }
            }
        });

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = filteredConnList;
                } else {
                    List<TNEBSystem> filteredList = new ArrayList<>();
                    for (TNEBSystem row : filteredConnList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getConnection_number().toLowerCase().contains(charString.toLowerCase()) || row.getConnection_number().toLowerCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<TNEBSystem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ConnectionListNewAdapterBinding connectionListAdapterBinding;

        public MyViewHolder(ConnectionListNewAdapterBinding Binding) {
            super(Binding.getRoot());
            connectionListAdapterBinding = Binding;
        }
    }
}
