package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.Interface.recyclerItemClicked;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.databinding.CommonRecyclerItemViewBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

public class EveningWaterSupplyTimimgAdapter extends RecyclerView.Adapter<EveningWaterSupplyTimimgAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private LayoutInflater layoutInflater;
    int pos=-1;
    com.nic.tnebnewwatersource.Interface.recyclerItemClicked recyclerItemClicked;

    public EveningWaterSupplyTimimgAdapter(Context context, List<TNEBSystem> list,recyclerItemClicked recyclerItemClicked) {
        this.context = context;
        prefManager = new PrefManager(context);
        this.list = list;
        this.recyclerItemClicked = recyclerItemClicked;

    }

    @NonNull
    @Override
    public EveningWaterSupplyTimimgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        CommonRecyclerItemViewBinding commonRecyclerItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.common_recycler_item_view, viewGroup, false);
        return new MyViewHolder(commonRecyclerItemViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final EveningWaterSupplyTimimgAdapter.MyViewHolder holder, final int position) {

        holder.commonRecyclerItemViewBinding.name.setText(list.get(position).getSupply_timing());
        if(pos==position){
            holder.commonRecyclerItemViewBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.commonRecyclerItemViewBinding.name.setTextColor(context.getResources().getColor(R.color.white));
        }
        else {
            holder.commonRecyclerItemViewBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.commonRecyclerItemViewBinding.name.setTextColor(context.getResources().getColor(R.color.grey4));
        }

        holder.commonRecyclerItemViewBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                notifyDataSetChanged();
                recyclerItemClicked.onItemClicked(position,"Evening_Supply_Timing");
            }
        });
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
