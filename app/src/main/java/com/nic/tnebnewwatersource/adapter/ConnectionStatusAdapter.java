package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.activity.ConnectionSubmitActivity;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ConnectionStatusAdapterBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

public class ConnectionStatusAdapter extends RecyclerView.Adapter<ConnectionStatusAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    int Pos=-1;

    public ConnectionStatusAdapter(Context context, List<TNEBSystem> list, dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;
    }

    @NonNull
    @Override
    public ConnectionStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ConnectionStatusAdapterBinding connectionStatusAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.connection_status_adapter, viewGroup, false);
        return new ConnectionStatusAdapter.MyViewHolder(connectionStatusAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final ConnectionStatusAdapter.MyViewHolder holder, final int position) {

        holder.connectionStatusAdapterBinding.connectionStatus.setText(list.get(position).getConnection_status());

        if (list.get(position).getConncetion_id().equalsIgnoreCase("1")){
            holder.connectionStatusAdapterBinding.connectionStatus.setText(context.getResources().getString(R.string.valid_connection));
            holder.connectionStatusAdapterBinding.arrowIc.setRotation(270);
            holder.connectionStatusAdapterBinding.arrowLayout.setBackgroundColor(context.getResources().getColor(R.color.mild_green));
            //holder.connectionStatusAdapterBinding.left3.setBackground(context.getResources().getDrawable(R.drawable.grey_bg));
            holder.connectionStatusAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.connectionStatusAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.grey_8));

        }else if (list.get(position).getConncetion_id().equalsIgnoreCase("2")){
            holder.connectionStatusAdapterBinding.connectionStatus.setText(context.getResources().getString(R.string.valid_connection_disconnected));
            holder.connectionStatusAdapterBinding.arrowIc.setRotation(90);
            holder.connectionStatusAdapterBinding.arrowLayout.setBackgroundColor(context.getResources().getColor(R.color.mild_red));
            //holder.connectionStatusAdapterBinding.left3.setBackground(context.getResources().getDrawable(R.drawable.grey_bg));
            holder.connectionStatusAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.connectionStatusAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.grey_8));


        }else if (list.get(position).getConncetion_id().equalsIgnoreCase("3")){
            holder.connectionStatusAdapterBinding.connectionStatus.setText(context.getResources().getString(R.string.invalid_connection));
            holder.connectionStatusAdapterBinding.arrowIc.setRotation(90);
            holder.connectionStatusAdapterBinding.arrowLayout.setBackgroundColor(context.getResources().getColor(R.color.pinkLite));
            //holder.connectionStatusAdapterBinding.left3.setBackground(context.getResources().getDrawable(R.drawable.grey_bg));
            holder.connectionStatusAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.connectionStatusAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.grey_8));


        }
        if(position==Pos){
            if (list.get(position).getConncetion_id().equalsIgnoreCase("1")){
                holder.connectionStatusAdapterBinding.arrowLayout.setBackgroundColor(context.getResources().getColor(R.color.accept));
                holder.connectionStatusAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                //holder.connectionStatusAdapterBinding.connectionListItem.setBackground(context.getResources().getDrawable(R.drawable.blue_bg));
                holder.connectionStatusAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.white));

            }else if (list.get(position).getConncetion_id().equalsIgnoreCase("2")){
                holder.connectionStatusAdapterBinding.arrowLayout.setBackgroundColor(context.getResources().getColor(R.color.subscription_type_red_color));
                holder.connectionStatusAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.connectionStatusAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.white));
            }else if (list.get(position).getConncetion_id().equalsIgnoreCase("3")){
                holder.connectionStatusAdapterBinding.arrowLayout.setBackgroundColor(context.getResources().getColor(R.color.pink));
                holder.connectionStatusAdapterBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.connectionStatusAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        holder.connectionStatusAdapterBinding.connectionListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pos=position;
                notifyDataSetChanged();
                ((ConnectionSubmitActivity)context).viewDetail(list.get(position).getConncetion_id());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ConnectionStatusAdapterBinding connectionStatusAdapterBinding;

        public MyViewHolder(ConnectionStatusAdapterBinding Binding) {
            super(Binding.getRoot());
            connectionStatusAdapterBinding = Binding;
        }
    }
}
