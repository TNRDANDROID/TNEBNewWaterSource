package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.activity.WaterSupplyActivity;
import com.nic.tnebnewwatersource.databinding.CommonRecyclerItemViewBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class CommonRecyclerAdapter extends RecyclerView.Adapter<CommonRecyclerAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private LayoutInflater layoutInflater;
    int Pos=-1;
    String type;

    public CommonRecyclerAdapter(Context context, List<TNEBSystem> list,String type) {
        this.context = context;
        prefManager = new PrefManager(context);
        this.list = list;
        this.type = type;
    }

    @NonNull
    @Override
    public CommonRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        CommonRecyclerItemViewBinding commonRecyclerItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.common_recycler_item_view, viewGroup, false);
        return new MyViewHolder(commonRecyclerItemViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final CommonRecyclerAdapter.MyViewHolder holder, final int position) {


        if(type.equals("Habitation")){
            holder.commonRecyclerItemViewBinding.name.setText(list.get(position).getHabitation_name());
        }
        else if(type.equals("Water_Supply_Timing")){
            holder.commonRecyclerItemViewBinding.name.setText(list.get(position).getSupply_timing());
        }
        else if(type.equals("Water_Session")) {
            holder.commonRecyclerItemViewBinding.name.setText(list.get(position).getSession_name());
        }
        else {
            holder.commonRecyclerItemViewBinding.name.setText(list.get(position).getWater_type());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CommonRecyclerItemViewBinding commonRecyclerItemViewBinding;

        public MyViewHolder(CommonRecyclerItemViewBinding Binding) {
            super(Binding.getRoot());
            commonRecyclerItemViewBinding = Binding;
        }
    }
}
