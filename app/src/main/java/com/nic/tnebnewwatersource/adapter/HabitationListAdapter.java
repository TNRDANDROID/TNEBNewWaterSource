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
import com.nic.tnebnewwatersource.databinding.ConnectionStatusAdapterBinding;
import com.nic.tnebnewwatersource.databinding.HabitationListItemViewBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class HabitationListAdapter extends RecyclerView.Adapter<HabitationListAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    int Pos=-1;

    public HabitationListAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;
    }

    @NonNull
    @Override
    public HabitationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        HabitationListItemViewBinding habitationListItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.habitation_list_item_view, viewGroup, false);
        return new MyViewHolder(habitationListItemViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final HabitationListAdapter.MyViewHolder holder, final int position) {

        holder.habitationListItemViewBinding.habitationName.setText(list.get(position).getHabitation_name());
        dbData.open();
        ArrayList<TNEBSystem> previousList = new ArrayList<>();
        previousList.addAll(dbData.getWaterSupplyDetails(list.get(position).getHabitation_code()));
        if(previousList.size()>0){
            holder.habitationListItemViewBinding.addIcon.setImageResource(R.drawable.ic_tick_icon);
        }
        else {
            holder.habitationListItemViewBinding.addIcon.setImageResource(R.drawable.add_icon);
        }
        holder.habitationListItemViewBinding.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WaterSupplyActivity)context).showPopUp(position);
            }
        });
        holder.habitationListItemViewBinding.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WaterSupplyActivity)context).get_daily_drinking_water_supply_status_view(list.get(position).getHabitation_code(),list.get(position).getHabitation_name());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private HabitationListItemViewBinding habitationListItemViewBinding;

        public MyViewHolder(HabitationListItemViewBinding Binding) {
            super(Binding.getRoot());
            habitationListItemViewBinding = Binding;
        }
    }
}
