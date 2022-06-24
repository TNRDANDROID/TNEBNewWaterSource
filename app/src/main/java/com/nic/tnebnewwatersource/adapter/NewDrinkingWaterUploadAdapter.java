package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
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
import com.nic.tnebnewwatersource.activity.NewWaterSupplyStatusEntryForm;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.databinding.NewDrinkingWaterUploadAdapterBinding;
import com.nic.tnebnewwatersource.model.ImageString;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewDrinkingWaterUploadAdapter extends RecyclerView.Adapter<NewDrinkingWaterUploadAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<ImageString> imageList = new ArrayList<>();
    ArrayList<TNEBSystem> pendingListImages = new ArrayList<>();
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    JSONObject dataSetTrack = new JSONObject();


    public NewDrinkingWaterUploadAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;

    }


    @NonNull
    @Override
    public NewDrinkingWaterUploadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        NewDrinkingWaterUploadAdapterBinding newDrinkingWaterUploadAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.new_drinking_water_upload_adapter, viewGroup, false);
        return new MyViewHolder(newDrinkingWaterUploadAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull NewDrinkingWaterUploadAdapter.MyViewHolder holder, final int position) {

        holder.drinkingWaterUploadAdapterBinding.villageName.setText(list.get(position).getPvName());
        holder.drinkingWaterUploadAdapterBinding.habitationName.setText(list.get(position).getHabitation_name());
        holder.drinkingWaterUploadAdapterBinding.waterTypeName.setText(list.get(position).getWater_type());
        holder.drinkingWaterUploadAdapterBinding.morningTimeName.setText(list.get(position).getMorning_water_supply_timing_name());
        holder.drinkingWaterUploadAdapterBinding.eveningTimeName.setText(list.get(position).getEvening_water_supply_timing_name());
        holder.drinkingWaterUploadAdapterBinding.sourceWaterName.setText(list.get(position).getWater_source_type_name());

        holder.drinkingWaterUploadAdapterBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new toUploadSaveDataTask().execute(String.valueOf(position));
            }
        });

        holder.drinkingWaterUploadAdapterBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewWaterSupplyStatusEntryForm)context).save_and_delete_alert(new JSONObject(),String.valueOf(list.get(position).getNew_water_details_primary_id()),"delete");
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
        private NewDrinkingWaterUploadAdapterBinding drinkingWaterUploadAdapterBinding;

        public MyViewHolder(NewDrinkingWaterUploadAdapterBinding Binding) {
            super(Binding.getRoot());
            drinkingWaterUploadAdapterBinding = Binding;
        }
    }

    public class toUploadSaveDataTask extends AsyncTask<String, Void,
            JSONObject> {
        String new_water_details_primary_id;
        JSONObject jsonObject;
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                int position = Integer.parseInt(params[0]);
                JSONArray water_supply_status_details = new JSONArray();
                jsonObject = new JSONObject();
                new_water_details_primary_id = String.valueOf(list.get(position).getNew_water_details_primary_id());
                dataSetTrack = new JSONObject();
                try {
                    dataSetTrack.put("bcode", list.get(position).getBlockCode());
                    dataSetTrack.put("pvcode", list.get(position).getPvCode());
                    dataSetTrack.put("habcode", list.get(position).getHabitation_code());
                    dataSetTrack.put("entry_dt", list.get(position).getEntry_date());
                    dataSetTrack.put("water_supply_status", list.get(position).getWater_supply_status_id());
                    if(list.get(position).getWater_supply_status_id().equals("1")){
                        if(list.get(position).getSession_id().equals("1")){
                            dataSetTrack.put("session_id", list.get(position).getSession_id());
                            dataSetTrack.put("session_fn_water_type", list.get(position).getWater_type_id());
                            dataSetTrack.put("session_fn_timing", list.get(position).getMorning_water_supply_timing_id());
                            dataSetTrack.put("session_fn_src_id", list.get(position).getWater_source_details_id());
                        }
                        else {
                            dataSetTrack.put("session_id", list.get(position).getSession_id());
                            dataSetTrack.put("session_an_water_type", list.get(position).getWater_type_id());
                            dataSetTrack.put("session_an_timing", list.get(position).getEvening_water_supply_timing_id());
                            dataSetTrack.put("session_an_src_id", list.get(position).getWater_source_details_id());

                        }

                    }
                    else {
                        dataSetTrack.put("no_supply_reason", list.get(position).getWater_supplied_reason_id());
                    }
                    water_supply_status_details.put(dataSetTrack);
                    jsonObject.put(AppConstant.KEY_SERVICE_ID, "daily_drinking_water_supply_status_v2_save");
                    jsonObject.put("water_supply_status_details",water_supply_status_details);
                    //dataSetTrack.put("water_supply_status_details",water_supply_status_details);
                    Log.d("trackData",jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            ((NewWaterSupplyStatusEntryForm)context).save_and_delete_alert(dataset,new_water_details_primary_id,"save");
        }
    }

}
