package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.nic.tnebnewwatersource.activity.MissedConnectionActivity;
import com.nic.tnebnewwatersource.databinding.ConnectionListNewAdapterBinding;
import com.nic.tnebnewwatersource.databinding.NewConnectionSavedListAdapterBinding;
import com.nic.tnebnewwatersource.databinding.NewConnectionSavedListAdapterNewBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.ArrayList;

public class NewConnetionSavedListAdapter extends RecyclerView.Adapter<NewConnetionSavedListAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private PrefManager prefManager;
    ArrayList<TNEBSystem> list;

    public NewConnetionSavedListAdapter(Context context,ArrayList<TNEBSystem> list) {
        this.list = list;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NewConnectionSavedListAdapterNewBinding connectionListAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.new_connection_saved_list_adapter_new, parent, false);
        return new NewConnetionSavedListAdapter.MyViewHolder(connectionListAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.connectionListAdapterBinding.consumerName.setText(list.get(position).getConsumer_name());
        holder.connectionListAdapterBinding.connectionNumber.setText(list.get(position).getCuscode());
        if(prefManager.getLocalLanguage().equals("ta")) {
            holder.connectionListAdapterBinding.habitation.setText(list.get(position).getHabitation_name_ta());
            holder.connectionListAdapterBinding.purposeAsPerTneb.setText(list.get(position).getConnection_name_ta());
        }
        else {
            holder.connectionListAdapterBinding.habitation.setText(list.get(position).getHabitation_name());
            holder.connectionListAdapterBinding.purposeAsPerTneb.setText(list.get(position).getConncetion_name());
        }
        holder.connectionListAdapterBinding.location.setText(list.get(position).getLocation());
        if(list.get(position).getKEY_METER_IMAGE()!=null&& !list.get(position).getKEY_METER_IMAGE().equals("")) {
            holder.connectionListAdapterBinding.availableStatus.setImageBitmap(StringToBitMap(list.get(position).getKEY_METER_IMAGE()));
        }
        else {
            holder.connectionListAdapterBinding.availableStatus.setImageResource(R.drawable.gallery_empty_1);
        }

        holder.connectionListAdapterBinding.availableStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getKEY_METER_IMAGE()!=null&& !list.get(position).getKEY_METER_IMAGE().equals("")) {
                    ExpandedImage(holder.connectionListAdapterBinding.availableStatus.getDrawable());
                }
                else {

                }
            }
        });
        holder.connectionListAdapterBinding.deleteIdLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MissedConnectionActivity)context).save_and_delete_alert(list.get(position).getId(),"delete");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NewConnectionSavedListAdapterNewBinding connectionListAdapterBinding;

        public MyViewHolder(NewConnectionSavedListAdapterNewBinding Binding) {
            super(Binding.getRoot());
            connectionListAdapterBinding = Binding;
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    private void ExpandedImage(Drawable profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageDrawable(profile);

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

}
