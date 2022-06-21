package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnebnewwatersource.ImageZoom.ImageMatrixTouchHandler;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.activity.PendingScreen;
import com.nic.tnebnewwatersource.activity.ViewImagesActivity;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.databinding.ConnectionListNewAdapterBinding;
import com.nic.tnebnewwatersource.databinding.PendingScreenAdapterBinding;
import com.nic.tnebnewwatersource.model.ImageString;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PendingScreenAdapter extends RecyclerView.Adapter<PendingScreenAdapter.MyViewHolder> {
    private Context context;
    private PrefManager prefManager;
    private List<TNEBSystem> list;
    private List<ImageString> imageList = new ArrayList<>();
    ArrayList<TNEBSystem> pendingListImages = new ArrayList<>();
    private final com.nic.tnebnewwatersource.dataBase.dbData dbData;
    private LayoutInflater layoutInflater;
    JSONObject dataSetTrack = new JSONObject();
    public String connection_number,cus_code,conn_number,connection_id;

    public PendingScreenAdapter(Context context, List<TNEBSystem> list, com.nic.tnebnewwatersource.dataBase.dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.list = list;

    }


    @NonNull
    @Override
    public PendingScreenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PendingScreenAdapterBinding pendingScreenAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.pending_screen_adapter, viewGroup, false);
        return new PendingScreenAdapter.MyViewHolder(pendingScreenAdapterBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull PendingScreenAdapter.MyViewHolder holder, final int position) {
        conn_number=list.get(position).getConnection_number();
        cus_code=list.get(position).getCuscode();
        holder.pendingScreenAdapterBinding.connectionNumber.setText(conn_number);
        holder.pendingScreenAdapterBinding.cusCode.setText(cus_code);

        if (list.get(position).getKEY_CONNECTION_STATUS_ID().equalsIgnoreCase("1")){
            holder.pendingScreenAdapterBinding.connectionStatus.setText(context.getResources().getString(R.string.valid_connection));
            holder.pendingScreenAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.accept));
            holder.pendingScreenAdapterBinding.availableStatus.setImageResource(R.drawable.gallery_icon1);
            holder.pendingScreenAdapterBinding.view.setBackgroundColor(context.getResources().getColor(R.color.accept));
        }else if (list.get(position).getKEY_CONNECTION_STATUS_ID().equalsIgnoreCase("2")){
            holder.pendingScreenAdapterBinding.connectionStatus.setText(context.getResources().getString(R.string.valid_connection_disconnected));
            holder.pendingScreenAdapterBinding.availableStatus.setImageResource(R.drawable.gallery_empty_1);
            holder.pendingScreenAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.subscription_type_red_color));
            holder.pendingScreenAdapterBinding.view.setBackgroundColor(context.getResources().getColor(R.color.subscription_type_red_color));

        }else if (list.get(position).getKEY_CONNECTION_STATUS_ID().equalsIgnoreCase("3")){
            holder.pendingScreenAdapterBinding.connectionStatus.setText(context.getResources().getString(R.string.invalid_connection));
            holder.pendingScreenAdapterBinding.connectionStatus.setTextColor(context.getResources().getColor(R.color.pink));
            holder.pendingScreenAdapterBinding.availableStatus.setImageResource(R.drawable.gallery_empty_1);
            holder.pendingScreenAdapterBinding.view.setBackgroundColor(context.getResources().getColor(R.color.pink));

        }
        holder.pendingScreenAdapterBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection_number=list.get(position).getConnection_number();
                connection_id=list.get(position).getConncetion_id();
                new  toUploadSaveDataTask().execute();
            }
        });

        holder.pendingScreenAdapterBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection_number=list.get(position).getConnection_number();
                ((PendingScreen)context).save_and_delete_alert(dataSetTrack,connection_number,"delete");
            }
        });


        holder.pendingScreenAdapterBinding.availableStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getKEY_CONNECTION_STATUS_ID().equals("1")){
                    Intent images_intent=new Intent(context, ViewImagesActivity.class);
                    Bundle bundle=new Bundle();
                    /*bundle.putString("eb_card_image",list.get(position).getKEY_EB_CARD_IMAGE());
                    bundle.putString("eb_bill_image",list.get(position).getKEY_LAST_BILL_IMAGE());
                    bundle.putString("eb_meter_image",list.get(position).getKEY_METER_IMAGE());
                    */
                    bundle.putString("id",list.get(position).getConncetion_id());
                    /*bundle.putString("oht_image",list.get(position).getKEY_OHT_MOTOR_IMAGE());
                    bundle.putString("mini_oht_image",list.get(position).getKEY_MINI_OHT_MOTOR_IMAGE());
                    bundle.putString("grl_motor_image",list.get(position).getKEY_GLR_MOTOR_IMAGE());
                    bundle.putString("mini_power_pump_motor_image",list.get(position).getKEY_MINI_POWER_PUMP_MOTOR_IMAGE());
                    */bundle.putString("activity_flag","PendingScreen");
                    images_intent.putExtras(bundle);
                    context.startActivity(images_intent);
                }

                else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_image), Toast.LENGTH_SHORT).show();
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
        private PendingScreenAdapterBinding pendingScreenAdapterBinding;

        public MyViewHolder(PendingScreenAdapterBinding Binding) {
            super(Binding.getRoot());
            pendingScreenAdapterBinding = Binding;
        }
    }

    public class toUploadSaveDataTask extends AsyncTask<String, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                boolean flag=false;
                dbData.open();
                //ArrayList<TNEBSystem> saveLatLongLists = dbData.getSavedTrackForParticularTank(String.valueOf(params[0]));
                JSONArray saveLatLongArray = new JSONArray();
                JSONArray purposeArray = new JSONArray();
                pendingListImages = new ArrayList<>();
                pendingListImages = dbData.getConnectionDetailsImages(connection_id);

                    JSONArray itemArray = new JSONArray(prefManager.getPurposeCodeJson().toString());
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getConnection_number().equals(connection_number)) {
                                JSONObject latLongData = new JSONObject();
                                latLongData.put("id", list.get(i).getConncetion_id());
                                latLongData.put("connection_number", list.get(i).getConnection_number());
                                latLongData.put("cuscode", list.get(i).getCuscode());
                                latLongData.put("connection_status_id", list.get(i).getKEY_CONNECTION_STATUS_ID());
                                if (list.get(i).getKEY_CONNECTION_STATUS_ID().equals("1")) {
                                    latLongData.put("connection_habcode", list.get(i).getKEY_HAB_CODE());
                                    latLongData.put("meter_image", pendingListImages.get(0).getKEY_METER_IMAGE());
                                    latLongData.put("last_bill_image", pendingListImages.get(0).getKEY_LAST_BILL_IMAGE());
                                    latLongData.put("eb_card_image", pendingListImages.get(0).getKEY_EB_CARD_IMAGE());
                                    latLongData.put("meter_image_lat", list.get(i).getKEY_METER_IMAGE_LAT());
                                    latLongData.put("meter_image_long", list.get(i).getKEY_METER_IMAGE_LONG());
                                    latLongData.put("meter_available", list.get(i).getMeter_available());
//                                latLongData.put("purpose_ids", list.get(i).getKEY_PURPOSE_IDS());

                                    try {
                                        // jsonString is a string variable that holds the JSON

                                        for (int j = 0; j < itemArray.length(); j++) {

                                            JSONObject itemA = new JSONObject();
                                            int value = itemArray.getInt(j);
                                            itemA.put("purpose_id", value);

                                            if (value == 34) {
                                                    itemA.put("no_of_street_lights", list.get(i).getStreet_light_count());
                                                }
                                            else if (value == 20) {
                                                    itemA.put("motor_type", list.get(i).getMotor_type());
                                                    itemA.put("tank_capacity", list.get(i).getTank_capacity());
                                                    itemA.put("motor_hp", list.get(i).getHorse_power());
                                                if(pendingListImages.size()>0) {
                                                    itemA.put("motor_image", pendingListImages.get(0).getKEY_OHT_MOTOR_IMAGE());
                                                }
                                                    itemA.put("motor_lat", list.get(i).getKEY_OHT_LATITUDE());
                                                    itemA.put("motor_long", list.get(i).getKEY_OHT_LANGTITUDE());
                                           /* itemA.put("tank_image",list.get(i).getKEY_OHT_TANK_IMAGE());
                                            itemA.put("tank_lat",list.get(i).getKEY_OHT_TANK_LATITUDE());
                                            itemA.put("tank_long",list.get(i).getKEY_OHT_TANK_LANGTITUDE());*/
                                                    itemA.put("land_mark", list.get(i).getKEY_LAND_MARK());
                                                    itemA.put("no_of_tanks", list.get(i).getOht_tank_count());
                                                }
                                            else if (value == 9) {
                                                itemA.put("motor_type", list.get(i).getKEY_GLR_MOTOR_TYPE());
                                                itemA.put("tank_capacity", list.get(i).getKEY_GLR_TANK_CAPACITY());
                                                itemA.put("motor_hp", list.get(i).getKEY_GLR_HORSE_POWER());
                                                if(pendingListImages.size()>0) {
                                                    itemA.put("motor_image", pendingListImages.get(0).getKEY_GLR_MOTOR_IMAGE());
                                                }
                                                itemA.put("motor_lat", list.get(i).getKEY_GLR_LATITUDE());
                                                itemA.put("motor_long", list.get(i).getKEY_GLR_LONGTITUDE());
                                           /* itemA.put("tank_image",list.get(i).getKEY_OHT_TANK_IMAGE());
                                            itemA.put("tank_lat",list.get(i).getKEY_OHT_TANK_LATITUDE());
                                            itemA.put("tank_long",list.get(i).getKEY_OHT_TANK_LANGTITUDE());*/
                                                itemA.put("land_mark", list.get(i).getKEY_GLR_LAND_MARK());
                                                itemA.put("no_of_tanks", list.get(i).getKEY_GLR_MOTOR_TANK_COUNT());
                                            }
                                            else if (value == 46) {
                                                itemA.put("motor_type", list.get(i).getKEY_mini_with_out_oht_MOTOR_TYPE());
                                                itemA.put("motor_hp", list.get(i).getKEY_MINI_WITH_OUT_OHT_HORSE_POWER());
                                                if(pendingListImages.size()>0) {
                                                    itemA.put("motor_image", pendingListImages.get(0).getKEY_mini_with_out_oht_MOTOR_IMAGE());
                                                }
                                                itemA.put("motor_lat", list.get(i).getKEY_mini_with_out_oht_LATITUDE());
                                                itemA.put("motor_long", list.get(i).getKEY_mini_with_out_oht_LONGTITUDE());
                                                itemA.put("land_mark", list.get(i).getKEY_mini_with_out_oht_LAND_MARK());

                                            }
                                            else if (value == 45) {
                                                itemA.put("motor_type", list.get(i).getKEY_MINI_POWER_PUMP_MOTOR_TYPE());
                                                itemA.put("tank_capacity", list.get(i).getKEY_MINI_POWER_PUMP_TANK_CAPACITY());
                                                itemA.put("motor_hp", list.get(i).getKEY_MINI_POWER_PUMP_HORSE_POWER());
                                                if(pendingListImages.size()>0) {
                                                    itemA.put("motor_image", pendingListImages.get(0).getKEY_MINI_POWER_PUMP_MOTOR_IMAGE());
                                                }
                                                itemA.put("motor_lat", list.get(i).getKEY_MINI_POWER_PUMP_LATITUDE());
                                                itemA.put("motor_long", list.get(i).getKEY_MINI_POWER_PUMP_LONGTITUDE());
                                           /* itemA.put("tank_image",list.get(i).getKEY_OHT_TANK_IMAGE());
                                            itemA.put("tank_lat",list.get(i).getKEY_OHT_TANK_LATITUDE());
                                            itemA.put("tank_long",list.get(i).getKEY_OHT_TANK_LANGTITUDE());*/
                                                itemA.put("land_mark", list.get(i).getKEY_MINI_POWER_PUMP_LAND_MARK());
                                                itemA.put("no_of_tanks", list.get(i).getKEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT());
                                            }
                                            else if (value == 15) {
                                                itemA.put("motor_type", list.get(i).getKEY_MINI_MOTOR_TYPE());
                                                itemA.put("tank_capacity", list.get(i).getKEY_MINI_MOTOR_TANK_CAPACITY());
                                                itemA.put("motor_hp", list.get(i).getKEY_MINI_MOTOR_HP());
                                                if(pendingListImages.size()>0) {
                                                    itemA.put("motor_image", pendingListImages.get(0).getKEY_MINI_OHT_MOTOR_IMAGE());

                                                }itemA.put("motor_lat", list.get(i).getKEY_MINI_OHT_LATITUDE());
                                                itemA.put("motor_long", list.get(i).getKEY_MINI_OHT_LANGTITUDE());
                                           /* itemA.put("tank_image",list.get(i).getKEY_MINI_OHT_TANK_IMAGE());
                                            itemA.put("tank_lat",list.get(i).getKEY_MINI_OHT_TANK_LATITUDE());
                                            itemA.put("tank_long",list.get(i).getKEY_MINI_OHT_TANK_LANGTITUDE());*/
                                                itemA.put("land_mark", list.get(i).getKEY_MINI_LAND_MARK());
                                                itemA.put("no_of_tanks", list.get(i).getMini_oht_tank_count());

                                            }
                                            Log.e("json", i + "=" + value);
                                            purposeArray.put(itemA);


                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }

                                latLongData.put("purpose_details", purposeArray);
                                saveLatLongArray.put(latLongData);
                            }
                        }
                    }

                dataSetTrack = new JSONObject();
                try {
                    dataSetTrack.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_TN_EB_CONNECTION_STATUS_UPDATE);
                    dataSetTrack.put(AppConstant.KEY_TRACK_DATA, saveLatLongArray);
                    Log.d("trackData",dataSetTrack.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dataSetTrack;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            ((PendingScreen)context).save_and_delete_alert(dataset,connection_number,"save");
        }
    }

}
