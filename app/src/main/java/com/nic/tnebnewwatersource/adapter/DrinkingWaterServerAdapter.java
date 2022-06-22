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
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.activity.DrinkingWaterSourceSave;
import com.nic.tnebnewwatersource.databinding.ServerDrinkingWatreDetailsItemViewBinding;
import com.nic.tnebnewwatersource.model.ImageString;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkingWaterServerAdapter extends RecyclerView.Adapter<DrinkingWaterServerAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<ImageString> imageList = new ArrayList<>();
    ArrayList<TNEBSystem> pendingListImages = new ArrayList<>();
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    JSONObject dataSetTrack = new JSONObject();


    public DrinkingWaterServerAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;

    }


    @NonNull
    @Override
    public DrinkingWaterServerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ServerDrinkingWatreDetailsItemViewBinding serverDrinkingWatreDetailsItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.server_drinking_watre_details_item_view, viewGroup, false);
        return new MyViewHolder(serverDrinkingWatreDetailsItemViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull DrinkingWaterServerAdapter.MyViewHolder holder, final int position) {

        holder.serverDrinkingWatreDetailsItemViewBinding.habitationName.setText(list.get(position).getHabitation_name());
        holder.serverDrinkingWatreDetailsItemViewBinding.waterSourceName.setText(list.get(position).getWater_source_type_name());
        holder.serverDrinkingWatreDetailsItemViewBinding.landMark.setText(list.get(position).getKEY_LAND_MARK());

        if(list.get(position).getImage_1()!=null&&!list.get(position).getImage_1().equals("")){
            holder.serverDrinkingWatreDetailsItemViewBinding.image1.setImageBitmap(StringToBitMap(list.get(position).getImage_1()));
        }
        else {
            holder.serverDrinkingWatreDetailsItemViewBinding.image1.setImageResource(R.drawable.no_data_ic);
        }
        if(list.get(position).getImage_2()!=null&&!list.get(position).getImage_2().equals("")){
            holder.serverDrinkingWatreDetailsItemViewBinding.image2.setImageBitmap(StringToBitMap(list.get(position).getImage_2()));
        }
        else {
            holder.serverDrinkingWatreDetailsItemViewBinding.image2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_no_image_icon));
        }
        holder.serverDrinkingWatreDetailsItemViewBinding.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((DrinkingWaterSourceSave)context).edit_alert(position);
            }
        });

        holder.serverDrinkingWatreDetailsItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        holder.serverDrinkingWatreDetailsItemViewBinding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getImage_1()!=null&&!list.get(position).getImage_1().equals("")){
                    ExpandedImage(list.get(position).getImage_1());
                }
            }
        });
        holder.serverDrinkingWatreDetailsItemViewBinding.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getImage_2()!=null&&!list.get(position).getImage_2().equals("")){
                    ExpandedImage(list.get(position).getImage_2());
                }
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
        private ServerDrinkingWatreDetailsItemViewBinding serverDrinkingWatreDetailsItemViewBinding;

        public MyViewHolder(ServerDrinkingWatreDetailsItemViewBinding Binding) {
            super(Binding.getRoot());
            serverDrinkingWatreDetailsItemViewBinding = Binding;
        }
    }


}
