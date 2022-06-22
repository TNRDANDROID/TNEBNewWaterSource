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
import com.nic.tnebnewwatersource.activity.PendingScreen;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.databinding.DrinkingWaterSourceUploadItemViewBinding;
import com.nic.tnebnewwatersource.model.ImageString;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkingWaterUploadAdapter extends RecyclerView.Adapter<DrinkingWaterUploadAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<ImageString> imageList = new ArrayList<>();
    ArrayList<TNEBSystem> pendingListImages = new ArrayList<>();
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    JSONObject dataSetTrack = new JSONObject();


    public DrinkingWaterUploadAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;

    }


    @NonNull
    @Override
    public DrinkingWaterUploadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        DrinkingWaterSourceUploadItemViewBinding drinkingWaterSourceUploadItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.drinking_water_source_upload_item_view, viewGroup, false);
        return new MyViewHolder(drinkingWaterSourceUploadItemViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull DrinkingWaterUploadAdapter.MyViewHolder holder, final int position) {

        holder.waterSourceUploadItemViewBinding.habitationName.setText(list.get(position).getHabitation_name());
        holder.waterSourceUploadItemViewBinding.waterSourceName.setText(list.get(position).getWater_source_type_name());
        holder.waterSourceUploadItemViewBinding.landMark.setText(list.get(position).getKEY_LAND_MARK());

        holder.waterSourceUploadItemViewBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new toUploadSaveDataTask().execute(String.valueOf(position));
            }
        });

        holder.waterSourceUploadItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DrinkingWaterSourceSave)context).save_and_delete_alert(new JSONObject(),String.valueOf(list.get(position).getWater_source_details_primary_id()),"delete");
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
        private DrinkingWaterSourceUploadItemViewBinding waterSourceUploadItemViewBinding;

        public MyViewHolder(DrinkingWaterSourceUploadItemViewBinding Binding) {
            super(Binding.getRoot());
            waterSourceUploadItemViewBinding = Binding;
        }
    }

    public class toUploadSaveDataTask extends AsyncTask<String, Void,
            JSONObject> {
        String water_source_details_primary_id;
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                int position = Integer.parseInt(params[0]);
                String hab_code = list.get(position).getHabitation_code();
                String water_source_type_id = list.get(position).getWater_source_type_id();
                String landmark = list.get(position).getKEY_LAND_MARK();
                String no_of_photos = list.get(position).getNo_of_photos();
                String lat_1 = list.get(position).getImage_1_lat();
                String long_1 = list.get(position).getImage_1_long();
                String image_1 = list.get(position).getImage_1();
                String lat_2 = list.get(position).getImage_2_lat();
                String long_2 = list.get(position).getImage_2_long();
                String image_2 = list.get(position).getImage_2();
                String water_source_details_id = list.get(position).getWater_source_details_id();
                water_source_details_primary_id = String.valueOf(list.get(position).getWater_source_details_primary_id());
                dataSetTrack = new JSONObject();
                try {
                    dataSetTrack.put(AppConstant.KEY_SERVICE_ID, "drinking_water_source_village_level_save");
                    dataSetTrack.put("hab_code", hab_code);
                    dataSetTrack.put("water_source_type_id", water_source_type_id);
                    dataSetTrack.put("landmark", landmark);
                    dataSetTrack.put("no_of_photos", no_of_photos);
                    dataSetTrack.put("lat_1", lat_1);
                    dataSetTrack.put("long_1", long_1);
                    dataSetTrack.put("image_1", image_1);
                    if(no_of_photos.equals("2")){
                        dataSetTrack.put("lat_2", lat_2);
                        dataSetTrack.put("long_2", long_2);
                        dataSetTrack.put("image_2", image_2);
                    }
                    dataSetTrack.put("water_source_details_id", water_source_details_id);
                    Log.d("trackData",dataSetTrack.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dataSetTrack;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            ((DrinkingWaterSourceSave)context).save_and_delete_alert(dataset,water_source_details_primary_id,"save");
        }
    }

}
