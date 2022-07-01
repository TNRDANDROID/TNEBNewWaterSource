package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.ImageZoom.ImageMatrixTouchHandler;
import com.nic.tnebnewwatersource.Interface.recyclerItemClicked;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.databinding.ServerDrinkingWatreDetailsItemViewBinding;
import com.nic.tnebnewwatersource.databinding.SourceOfWaterItemViewBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

public class SourceOfWaterAdapter extends RecyclerView.Adapter<SourceOfWaterAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    int pos =-1;
    recyclerItemClicked recyclerItemClicked;

    public SourceOfWaterAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData,recyclerItemClicked recyclerItemClicked) {
        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;
        this.recyclerItemClicked = recyclerItemClicked;

    }


    @NonNull
    @Override
    public SourceOfWaterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        SourceOfWaterItemViewBinding sourceOfWaterItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.source_of_water_item_view, viewGroup, false);
        return new MyViewHolder(sourceOfWaterItemViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull SourceOfWaterAdapter.MyViewHolder holder, final int position) {

        holder.sourceOfWaterItemViewBinding.waterSourceName.setText(list.get(position).getWater_source_type_name());
        holder.sourceOfWaterItemViewBinding.landMark.setText(list.get(position).getKEY_LAND_MARK());
        /*if(!list.get(position).getIs_others().equals("")){
            holder.sourceOfWaterItemViewBinding.waterSourceName.setText(list.get(position).getWater_source_type_name()+" ("+list.get(position).getIs_others()+" )");
        }
        else {
            holder.sourceOfWaterItemViewBinding.waterSourceName.setText(list.get(position).getWater_source_type_name());
        }*/
        if(list.get(position).getImage_1()!=null&&!list.get(position).getImage_1().equals("")){
            holder.sourceOfWaterItemViewBinding.image1.setImageBitmap(StringToBitMap(list.get(position).getImage_1()));
        }
        else {
            holder.sourceOfWaterItemViewBinding.image1.setImageResource(R.drawable.no_data_ic);
        }
        if(pos==position){
            holder.sourceOfWaterItemViewBinding.clickedView.setImageResource((R.drawable.ic_check));
        }
        else {
            holder.sourceOfWaterItemViewBinding.clickedView.setImageResource((R.drawable.ic_oval));
        }

        holder.sourceOfWaterItemViewBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                notifyDataSetChanged();
                recyclerItemClicked.onItemClicked(position,"Source_Of_water");
            }
        });
    }

    private void ExpandedImage(String profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageBitmap(StringToBitMap(profile));

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(context);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setView(ImagePopupLayout);

            final AlertDialog alert = dialogBuilder.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_zoomInOut;
            alert.show();
            alert.getWindow().setBackgroundDrawableResource(R.color.full_transparent);
            alert.setCanceledOnTouchOutside(true);

            zoomImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
//            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            byte [] encodeByte= Base64.decode(encodedString,0);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private SourceOfWaterItemViewBinding sourceOfWaterItemViewBinding;

        public MyViewHolder(SourceOfWaterItemViewBinding Binding) {
            super(Binding.getRoot());
            sourceOfWaterItemViewBinding = Binding;
        }
    }


}
