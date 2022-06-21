package com.nic.tnebnewwatersource.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.ImageZoom.ImageMatrixTouchHandler;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ImagesLayoutBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewImagesActivity extends AppCompatActivity implements Api.ServerResponseListener {

    ImagesLayoutBinding imagesLayoutBinding;
    String eb_meter__image_string="";
    String eb_card_image_string="";
    String last_eb_bill_image_string="";
    String oht_image_string="";
    String mini_oht_image_string="";
    String mini_power_pump_motor_image_string="";
    String mini_with_out_oht_motor_image_string="";
    String grl_motor_image_string="";
    String activity_flag="";
    String connection_id="";
    private PrefManager prefManager;
    String id="",region_code="",circle_code="",section_code="",distribution_code="",consumer_code="",cuscode="",consumer_name="",connection_number="",location="",tariff="",tariff_desc="",purpose_as_per_tneb
            ="", connection_status_id="",connection_habcode="",meter_image_file_name="",meter_image_lat="",meter_image_long
            ="",last_bill_image_file_name="",eb_card_image_file_name="",eb_card_image_available="",last_bill_image_available="",meter_image_available="",con_purpose_id="",connection_name="",no_of_street_light="",
            motor_type="",motor_type_name="",motor_hp="",motor_tank_capacity="",eb_card_image="",last_bill_image="",meter_image="",meter_available="";
    String oht_motor_number="";
    String oht_motor_number_formated="";
    String oht_motor_lat="";
    String oht_motor_long="";
    String oht_tank_lat="";
    String oht_tank_long="";
    String oht_motor_image_available="";
    String oht_tank_image_available="";
    String oht_land_mark="";
    String oht_motor_image="";
    String oht_tank_image="";
    String oht_tank_capacity="";
    String oht_tank_count="";
    String oht_tank_hp="";
    String oht_motor_type="";

    String mini_oht_motor_number="";
    String mini_oht_motor_number_formated="";
    String mini_oht_motor_lat="";
    String mini_oht_motor_long="";
    String mini_oht_tank_lat="";
    String mini_oht_tank_long="";
    String mini_oht_motor_image_available="";
    String mini_oht_tank_image_available="";
    String mini_oht_land_mark="";
    String mini_oht_motor_image="";
    String mini_oht_tank_image="";
    String mini_oht_tank_capacity="";
    String mini_oht_tank_count="";
    String mini_oht_tank_hp="";
    String mini_oht_motor_type="";

    ///Mini Power Pump
    String mini_power_pump_motor_type="";
    String mini_power_pump_tank_capacity="";
    String mini_power_pump_tank_count="";
    String mini_power_pump_horse_power="";
    String mini_power_pump_land_mark="";
    String mini_power_pump_motor_number="";
    String mini_power_pump_motor_number_formated="";
    String mini_power_pump_motor_number_long="";
    String mini_power_pump_motor_lat="";
    String mini_power_pump_motor_image_available="";
    String mini_power_pump_motor_image="";

    ///GLR
    String glr_motor_image="";
    String glr_motor_image_available="";
    String glr_motor_lat="";
    String glr_motor_number_long="";
    String glr_motor_number_formated="";
    String glr_motor_number="";
    String glr_land_mark="";
    String glr_horse_power="";
    String glr_tank_count="";
    String glr_tank_capacity="";
    String glr_motor_type="";

    ////Mini With Out OHT
    String mini_with_out_oht_motor_image="";
    String mini_with_out_oht_motor_image_available="";
    String mini_with_out_oht_motor_lat="";
    String mini_with_out_oht_motor_number_long="";
    String mini_with_out_oht_motor_number_formated="";
    String mini_with_out_oht_motor_number="";
    String mini_with_out_oht_land_mark="";
    String mini_with_out_oht_horse_power="";
    String mini_with_out_oht_motor_type="";


    ArrayList<TNEBSystem> imagesListString;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagesLayoutBinding = DataBindingUtil.setContentView(this, R.layout.images_layout);
        imagesLayoutBinding.setActivity(this);
        prefManager = new PrefManager(this);

        String localLanguage = prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);
        imagesLayoutBinding.connectionDetailsLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ll1.setVisibility(View.GONE);
        imagesLayoutBinding.noOfStreetLightCountLayout.setVisibility(View.GONE);
        imagesLayoutBinding.tankCapacityLayout.setVisibility(View.GONE);
        imagesLayoutBinding.horsePowerLayout.setVisibility(View.GONE);
        imagesLayoutBinding.motorTypeLayout.setVisibility(View.GONE);
        imagesLayoutBinding.imagesLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ebCardImgLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ebBillImgLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ebMeterImgLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ohtMotorImageLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniOhtMotorImageLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ohtTankImageLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniOhtTankImageLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniMotorTypeLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniHorsePowerLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniTankCapacityLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniTankCountLayout.setVisibility(View.GONE);
        imagesLayoutBinding.tankCountLayout.setVisibility(View.GONE);
        imagesLayoutBinding.ohtDetailsHeading.setVisibility(View.GONE);
        imagesLayoutBinding.miniOhtDetailsHeading.setVisibility(View.GONE);
        imagesLayoutBinding.ohtMotorLandMarkLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniMotorLandMarkLayout.setVisibility(View.GONE);
        imagesLayoutBinding.meterAvailableStatusLayout.setVisibility(View.GONE);

        ///GLR
        imagesLayoutBinding.glrDetailsHeading.setVisibility(View.GONE);
        imagesLayoutBinding.glrMotorTypeLayout.setVisibility(View.GONE);
        imagesLayoutBinding.glrTankCountLayout.setVisibility(View.GONE);
        imagesLayoutBinding.glrTankCapacityLayout.setVisibility(View.GONE);
        imagesLayoutBinding.glrMotorLandMarkLayout.setVisibility(View.GONE);
        imagesLayoutBinding.glrHorsePowerLayout.setVisibility(View.GONE);
        imagesLayoutBinding.grlMotorImageLayout.setVisibility(View.GONE);

        ///Mini Power Pump
        imagesLayoutBinding.miniPowerPumpMotorLandMarkLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniPowerPumpDetailsHeading.setVisibility(View.GONE);
        imagesLayoutBinding.miniPowerPumpTankCountLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniPowerPumpTankCapacityLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniPowerPumpMotorTypeLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniPowerPumpHorsePowerLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniPowerPumpMotorImageLayout.setVisibility(View.GONE);


        ////Mini With Oht
        imagesLayoutBinding.miniWithOutOhtMotorLandMarkLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniWithOutOhtDetailsHeading.setVisibility(View.GONE);
        imagesLayoutBinding.miniWithOutOhtHorsePowerLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniWithOutOhtMotorTypeLayout.setVisibility(View.GONE);
        imagesLayoutBinding.miniWithOutOhtMotorImageLayout.setVisibility(View.GONE);


        getBundleValues();

        imagesLayoutBinding.ebMeterCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(eb_card_image_string);
            }
        });
        imagesLayoutBinding.lastEbBillImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(last_eb_bill_image_string);
            }
        });
        imagesLayoutBinding.ebMeterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(eb_meter__image_string);
            }
        });
        imagesLayoutBinding.ebMeterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(meter_image);
            }
        });
        imagesLayoutBinding.ebCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(eb_card_image);
            }
        });
        imagesLayoutBinding.ebBillImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(last_bill_image);
            }
        });
        imagesLayoutBinding.ohtMotorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(oht_motor_image);
            }
        });
        imagesLayoutBinding.miniOhtMotorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(mini_oht_motor_image);
            }
        });
        imagesLayoutBinding.ohtMotorImageLl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(oht_image_string);
            }
        });
        imagesLayoutBinding.miniOhtMotorImageLl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(mini_oht_image_string);
            }
        });
        imagesLayoutBinding.miniPowerPumpMotorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(mini_power_pump_motor_image);
            }
        });
        imagesLayoutBinding.grlMotorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(glr_motor_image);
            }
        });
        imagesLayoutBinding.miniWithOutOhtMotorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(mini_with_out_oht_motor_image);
            }
        });

        imagesLayoutBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imagesLayoutBinding.miniPowerPumpMotorImageLl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(mini_power_pump_motor_image_string);
            }
        });
        imagesLayoutBinding.miniWithOutOhtMotorImageLl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(mini_with_out_oht_motor_image_string);
            }
        });
        imagesLayoutBinding.grlMotorImageLl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(grl_motor_image_string);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }

    private void viewConnectionDetails() {
        try {
            new ApiService(this).makeJSONObjectRequest("savedList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), connectionListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject connectionListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), JsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("savedList", "" + authKey);
        return dataSet;
    }

    public  JSONObject JsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "tneb_connection_details");
        dataSet.put("tneb_id", connection_id);
        Log.d("savedList", "" + dataSet);
        return dataSet;
    }



    public void getBundleValues(){
        Bundle bundle = getIntent().getExtras();
        activity_flag=bundle.getString("activity_flag","");
        if(activity_flag.equals("PendingScreen")){
            dbData.open();
            imagesListString=new ArrayList<>();
            imagesListString= dbData.getConnectionDetailsImages(bundle.getString("id",""));
            imagesLayoutBinding.connectionDetailsLayout.setVisibility(View.GONE);
            imagesLayoutBinding.ll1.setVisibility(View.VISIBLE);
            imagesLayoutBinding.titleText.setText("Images");
            /*eb_meter__image_string=bundle.getString("eb_meter_image","");
            eb_card_image_string=bundle.getString("eb_card_image","");
            last_eb_bill_image_string=bundle.getString("eb_bill_image","");
            oht_image_string=bundle.getString("oht_image","");
            mini_oht_image_string=bundle.getString("mini_oht_image","");
            grl_motor_image_string=bundle.getString("grl_motor_image","");
            mini_power_pump_motor_image_string=bundle.getString("mini_power_pump_motor_image","");
*/
            if(imagesListString.size()>0){
                eb_meter__image_string=imagesListString.get(0).getKEY_METER_IMAGE();
                eb_card_image_string=imagesListString.get(0).getKEY_EB_CARD_IMAGE();
                last_eb_bill_image_string=imagesListString.get(0).getKEY_LAST_BILL_IMAGE();
                oht_image_string=imagesListString.get(0).getKEY_OHT_MOTOR_IMAGE();
                mini_oht_image_string=imagesListString.get(0).getKEY_MINI_OHT_MOTOR_IMAGE();
                grl_motor_image_string=imagesListString.get(0).getKEY_GLR_MOTOR_IMAGE();
                mini_power_pump_motor_image_string=imagesListString.get(0).getKEY_MINI_POWER_PUMP_MOTOR_IMAGE();
                mini_with_out_oht_motor_image_string=imagesListString.get(0).getKEY_mini_with_out_oht_MOTOR_IMAGE();

                if(imagesListString.get(0).getKEY_METER_IMAGE()!=null && !imagesListString.get(0).getKEY_METER_IMAGE().equals("")){
                    imagesLayoutBinding.ebMeterImage.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_METER_IMAGE()));
                }
                else {
                    imagesLayoutBinding.ebMeterCaptureLayout.setVisibility(View.GONE);
                }
                if(imagesListString.get(0).getKEY_LAST_BILL_IMAGE()!=null && !imagesListString.get(0).getKEY_LAST_BILL_IMAGE().equals("")){
                    imagesLayoutBinding.lastEbBillImage.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_LAST_BILL_IMAGE()));
                }
                else {
                    imagesLayoutBinding.lastEbBillImageLayout.setVisibility(View.GONE);
                }
                if(imagesListString.get(0).getKEY_OHT_MOTOR_IMAGE()!=null && !imagesListString.get(0).getKEY_OHT_MOTOR_IMAGE().equals("")){
                    imagesLayoutBinding.ohtMotorImageLl1.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_OHT_MOTOR_IMAGE()));
                }
                else {
                    imagesLayoutBinding.ohtMotorCaptureLayout.setVisibility(View.GONE);
                }
                if(imagesListString.get(0).getKEY_MINI_OHT_MOTOR_IMAGE()!=null && !imagesListString.get(0).getKEY_MINI_OHT_MOTOR_IMAGE().equals("")){
                    imagesLayoutBinding.miniOhtMotorImageLl1.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_MINI_OHT_MOTOR_IMAGE()));
                }
                else {
                    imagesLayoutBinding.miniOhtMotorCaptureLayout.setVisibility(View.GONE);
                }
                if(imagesListString.get(0).getKEY_GLR_MOTOR_IMAGE()!=null && !imagesListString.get(0).getKEY_GLR_MOTOR_IMAGE().equals("")){
                    imagesLayoutBinding.grlMotorImageLl1.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_GLR_MOTOR_IMAGE()));
                }
                else {
                    imagesLayoutBinding.grlMotorCaptureLayout.setVisibility(View.GONE);
                }
                if(imagesListString.get(0).getKEY_MINI_POWER_PUMP_MOTOR_IMAGE()!=null && !imagesListString.get(0).getKEY_MINI_POWER_PUMP_MOTOR_IMAGE().equals("")){
                    imagesLayoutBinding.miniPowerPumpMotorImageLl1.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_MINI_POWER_PUMP_MOTOR_IMAGE()));
                }
                else {
                    imagesLayoutBinding.miniPowerPumpMotorCaptureLayout.setVisibility(View.GONE);
                }

                if(imagesListString.get(0).getKEY_EB_CARD_IMAGE()!=null && !imagesListString.get(0).getKEY_EB_CARD_IMAGE().equals("")){
                    imagesLayoutBinding.ebMeterCardImage.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_EB_CARD_IMAGE()));
                }
                else {
                    imagesLayoutBinding.ebMeterCardImageLayout.setVisibility(View.GONE);
                }

                if(imagesListString.get(0).getKEY_mini_with_out_oht_MOTOR_IMAGE()!=null && !imagesListString.get(0).getKEY_mini_with_out_oht_MOTOR_IMAGE().equals("")){
                    imagesLayoutBinding.miniWithOutOhtMotorImageLl1.setImageBitmap(StringToBitMap(imagesListString.get(0).getKEY_mini_with_out_oht_MOTOR_IMAGE()));
                }
                else {
                    imagesLayoutBinding.miniWithOutOhtMotorCaptureLayout.setVisibility(View.GONE);
                }
            }
            /*if(!eb_meter__image_string.equals("")){
                imagesLayoutBinding.ebMeterImage.setImageBitmap(StringToBitMap(eb_meter__image_string));
            }
            else {
                imagesLayoutBinding.ebMeterCaptureLayout.setVisibility(View.GONE);
            }
            if(!eb_card_image_string.equals("")){
                imagesLayoutBinding.ebMeterCardImage.setImageBitmap(StringToBitMap(eb_card_image_string));
            }
            else {
                imagesLayoutBinding.ebMeterCardImageLayout.setVisibility(View.GONE);
            }
            if(!last_eb_bill_image_string.equals("")){
                imagesLayoutBinding.lastEbBillImage.setImageBitmap(StringToBitMap(last_eb_bill_image_string));
            }
            else {
                imagesLayoutBinding.lastEbBillImageLayout.setVisibility(View.GONE);
            }
            if(!oht_image_string.equals("")){
                imagesLayoutBinding.ohtMotorImageLl1.setImageBitmap(StringToBitMap(oht_image_string));
            }
            else {
                imagesLayoutBinding.ohtMotorCaptureLayout.setVisibility(View.GONE);
            }
            if(!mini_oht_image_string.equals("")){
                imagesLayoutBinding.miniOhtMotorImageLl1.setImageBitmap(StringToBitMap(mini_oht_image_string));
            }
            else {
                imagesLayoutBinding.miniOhtMotorCaptureLayout.setVisibility(View.GONE);
            }
            if(!grl_motor_image_string.equals("")){
                imagesLayoutBinding.grlMotorImageLl1.setImageBitmap(StringToBitMap(grl_motor_image_string));
            }
            else {
                imagesLayoutBinding.grlMotorCaptureLayout.setVisibility(View.GONE);
            }
            if(!mini_power_pump_motor_image_string.equals("")){
                imagesLayoutBinding.miniPowerPumpMotorImageLl1.setImageBitmap(StringToBitMap(mini_power_pump_motor_image_string));
            }
            else {
                imagesLayoutBinding.miniPowerPumpMotorCaptureLayout.setVisibility(View.GONE);
            }*/
        }
        else {
            imagesLayoutBinding.titleText.setText(getResources().getString(R.string.connection_details));
            connection_id=bundle.getString("id","");
            if (Utils.isOnline()) {
                viewConnectionDetails();
            }else {
                Utils.showAlert(ViewImagesActivity.this,getResources().getString(R.string.no_internet_connection));
            }

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
    private void ExpandedImage(String profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageBitmap(StringToBitMap(profile));

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(this);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("savedList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("savedList", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = new JSONArray();
                    JSONArray purpose_jsonArray = new JSONArray();
                    jsonArray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                    for(int j=0; j<jsonArray.length(); j++){
                        id=jsonArray.getJSONObject(j).getString("id");
                        region_code=jsonArray.getJSONObject(j).getString("region_code");
                        circle_code=jsonArray.getJSONObject(j).getString("circle_code");
                        section_code=jsonArray.getJSONObject(j).getString("section_code");
                        distribution_code=jsonArray.getJSONObject(j).getString("distribution_code");
                        consumer_code=jsonArray.getJSONObject(j).getString("consumer_code");
                        cuscode=jsonArray.getJSONObject(j).getString("cuscode");
                        consumer_name=jsonArray.getJSONObject(j).getString("consumer_name");
                        connection_number=jsonArray.getJSONObject(j).getString("connection_number");
                        location=jsonArray.getJSONObject(j).getString("location");
                        tariff=jsonArray.getJSONObject(j).getString("tariff");
                        tariff_desc=jsonArray.getJSONObject(j).getString("tariff_desc");
                        purpose_as_per_tneb=jsonArray.getJSONObject(j).getString("purpose_as_per_tneb");
                        connection_status_id =jsonArray.getJSONObject(j).getString("onnection_status_id");
                        connection_habcode=jsonArray.getJSONObject(j).getString("connection_habcode");
                        if(connection_status_id.equalsIgnoreCase("1")){
                            meter_image_file_name=jsonArray.getJSONObject(j).getString("meter_image_file_name");
                            meter_image_lat=jsonArray.getJSONObject(j).getString("meter_image_lat");
                            meter_image_long=jsonArray.getJSONObject(j).getString("meter_image_long");
                            last_bill_image_file_name=jsonArray.getJSONObject(j).getString("last_bill_image_file_name");
                            eb_card_image_file_name=jsonArray.getJSONObject(j).getString("eb_card_image_file_name");
                            eb_card_image_available=jsonArray.getJSONObject(j).getString("eb_card_image_available");
                            last_bill_image_available=jsonArray.getJSONObject(j).getString("last_bill_image_available");
                            meter_image_available=jsonArray.getJSONObject(j).getString("meter_image_available");
                            meter_available=jsonArray.getJSONObject(j).getString("meter_available");
                            if(eb_card_image_available.equalsIgnoreCase("Y")){
                                eb_card_image=jsonArray.getJSONObject(j).getString("eb_card_image");
                            }
                            if(last_bill_image_available.equalsIgnoreCase("Y")){
                                last_bill_image=jsonArray.getJSONObject(j).getString("last_bill_image");
                            }
                            if(meter_image_available.equalsIgnoreCase("Y")){
                                meter_image=jsonArray.getJSONObject(j).getString("meter_image");
                            }
                            purpose_jsonArray = jsonArray.getJSONObject(j).getJSONArray("purpose_details");
                        }


                    }

                    String item = "";
                    if(purpose_jsonArray.length()>0) {
                        imagesLayoutBinding.purposeDetailsLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < purpose_jsonArray.length(); i++) {
                            item = item + purpose_jsonArray.getJSONObject(i).getString("connection_name");
                            if (i != purpose_jsonArray.length() - 1) {
                                item = item + ", ";
                            }
                            con_purpose_id = String.valueOf((purpose_jsonArray.getJSONObject(i).getInt("con_purpose_id")));

                            if (con_purpose_id.equalsIgnoreCase("34") ) {
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");
                                no_of_street_light = purpose_jsonArray.getJSONObject(i).getString("no_of_street_light");


                            }
                            else if (con_purpose_id.equalsIgnoreCase("15")) {
                                motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type"));
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");
                                mini_oht_motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type_name"));
                                mini_oht_tank_hp = (purpose_jsonArray.getJSONObject(i).getString("motor_hp"));
                                mini_oht_tank_capacity = (purpose_jsonArray.getJSONObject(i).getString("motor_tank_capacity"));
                                mini_oht_tank_count = (purpose_jsonArray.getJSONObject(i).getString("no_of_tanks"));
                                mini_oht_motor_number = (purpose_jsonArray.getJSONObject(i).getString("motor_number"));
                                mini_oht_motor_number_formated = (purpose_jsonArray.getJSONObject(i).getString("motor_number_formated"));
                                mini_oht_motor_lat = (purpose_jsonArray.getJSONObject(i).getString("motor_lat"));
                                mini_oht_motor_long = (purpose_jsonArray.getJSONObject(i).getString("motor_long"));
                              /*  mini_oht_tank_lat = (purpose_jsonArray.getJSONObject(i).getString("tank_lat"));
                                mini_oht_tank_long = (purpose_jsonArray.getJSONObject(i).getString("tank_long"));*/
                                mini_oht_land_mark = (purpose_jsonArray.getJSONObject(i).getString("land_mark"));
                                mini_oht_motor_image_available = (purpose_jsonArray.getJSONObject(i).getString("motor_image_available"));
                                if(mini_oht_motor_image_available.equals("Y")){
                                    mini_oht_motor_image = (purpose_jsonArray.getJSONObject(i).getString("motor_image"));
                                }
                                else {
                                    mini_oht_motor_image="";
                                }

                              /*  mini_oht_tank_image_available = (purpose_jsonArray.getJSONObject(i).getString("tank_image_available"));
                                if(mini_oht_tank_image_available.equals("Y")){
                                    mini_oht_tank_image = (purpose_jsonArray.getJSONObject(i).getString("tank_image"));
                                }else {
                                    mini_oht_tank_image="";
                                }*/



                            }
                            else if (con_purpose_id.equalsIgnoreCase("20")) {
                                motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type"));
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");
                                oht_motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type_name"));
                                oht_tank_hp = (purpose_jsonArray.getJSONObject(i).getString("motor_hp"));
                                oht_tank_capacity = (purpose_jsonArray.getJSONObject(i).getString("motor_tank_capacity"));
                                oht_tank_count = (purpose_jsonArray.getJSONObject(i).getString("no_of_tanks"));
                                oht_motor_number = (purpose_jsonArray.getJSONObject(i).getString("motor_number"));
                                oht_motor_number_formated = (purpose_jsonArray.getJSONObject(i).getString("motor_number_formated"));
                                oht_motor_lat = (purpose_jsonArray.getJSONObject(i).getString("motor_lat"));
                                oht_motor_long = (purpose_jsonArray.getJSONObject(i).getString("motor_long"));
                              /*  oht_tank_lat = (purpose_jsonArray.getJSONObject(i).getString("tank_lat"));
                                oht_tank_long = (purpose_jsonArray.getJSONObject(i).getString("tank_long"));*/
                                oht_land_mark = (purpose_jsonArray.getJSONObject(i).getString("land_mark"));
                                oht_motor_image_available = (purpose_jsonArray.getJSONObject(i).getString("motor_image_available"));
                                if(oht_motor_image_available.equals("Y")){
                                    oht_motor_image = (purpose_jsonArray.getJSONObject(i).getString("motor_image"));
                                }
                                else {
                                    oht_motor_image="";
                                }

                              /*  oht_tank_image_available = (purpose_jsonArray.getJSONObject(i).getString("tank_image_available"));
                                if(oht_tank_image_available.equals("Y")){
                                    oht_tank_image = (purpose_jsonArray.getJSONObject(i).getString("tank_image"));
                                }else {
                                    oht_tank_image="";
                                }

*/

                            }
                            else if (con_purpose_id.equalsIgnoreCase("45")) {
                                motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type"));
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");
                                mini_power_pump_motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type_name"));
                                mini_power_pump_horse_power = (purpose_jsonArray.getJSONObject(i).getString("motor_hp"));
                                mini_power_pump_tank_capacity = (purpose_jsonArray.getJSONObject(i).getString("motor_tank_capacity"));
                                mini_power_pump_tank_count = (purpose_jsonArray.getJSONObject(i).getString("no_of_tanks"));
                                mini_power_pump_motor_number = (purpose_jsonArray.getJSONObject(i).getString("motor_number"));
                                mini_power_pump_motor_number_formated = (purpose_jsonArray.getJSONObject(i).getString("motor_number_formated"));
                                mini_power_pump_motor_lat = (purpose_jsonArray.getJSONObject(i).getString("motor_lat"));
                                mini_power_pump_motor_number_long = (purpose_jsonArray.getJSONObject(i).getString("motor_long"));
                              /*  oht_tank_lat = (purpose_jsonArray.getJSONObject(i).getString("tank_lat"));
                                oht_tank_long = (purpose_jsonArray.getJSONObject(i).getString("tank_long"));*/
                                mini_power_pump_land_mark = (purpose_jsonArray.getJSONObject(i).getString("land_mark"));
                                mini_power_pump_motor_image_available = (purpose_jsonArray.getJSONObject(i).getString("motor_image_available"));
                                if(mini_power_pump_motor_image_available.equals("Y")){
                                    mini_power_pump_motor_image = (purpose_jsonArray.getJSONObject(i).getString("motor_image"));
                                }
                                else {
                                    mini_power_pump_motor_image="";
                                }

                              /*  oht_tank_image_available = (purpose_jsonArray.getJSONObject(i).getString("tank_image_available"));
                                if(oht_tank_image_available.equals("Y")){
                                    oht_tank_image = (purpose_jsonArray.getJSONObject(i).getString("tank_image"));
                                }else {
                                    oht_tank_image="";
                                }

*/

                            }
                            else if (con_purpose_id.equalsIgnoreCase("46")) {
                                motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type"));
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");
                                mini_with_out_oht_motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type_name"));
                                mini_with_out_oht_horse_power = (purpose_jsonArray.getJSONObject(i).getString("motor_hp"));
                                mini_with_out_oht_motor_number = (purpose_jsonArray.getJSONObject(i).getString("motor_number"));
                                mini_with_out_oht_motor_number_formated = (purpose_jsonArray.getJSONObject(i).getString("motor_number_formated"));
                                mini_with_out_oht_motor_lat = (purpose_jsonArray.getJSONObject(i).getString("motor_lat"));
                                mini_with_out_oht_motor_number_long = (purpose_jsonArray.getJSONObject(i).getString("motor_long"));
                                mini_with_out_oht_land_mark = (purpose_jsonArray.getJSONObject(i).getString("land_mark"));
                                mini_with_out_oht_motor_image_available = (purpose_jsonArray.getJSONObject(i).getString("motor_image_available"));
                                if(mini_with_out_oht_motor_image_available.equals("Y")){
                                    mini_with_out_oht_motor_image = (purpose_jsonArray.getJSONObject(i).getString("motor_image"));
                                }
                                else {
                                    mini_with_out_oht_motor_image="";
                                }

                              /*  oht_tank_image_available = (purpose_jsonArray.getJSONObject(i).getString("tank_image_available"));
                                if(oht_tank_image_available.equals("Y")){
                                    oht_tank_image = (purpose_jsonArray.getJSONObject(i).getString("tank_image"));
                                }else {
                                    oht_tank_image="";
                                }

*/

                            }
                            else if (con_purpose_id.equalsIgnoreCase("9")) {
                                motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type"));
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");
                                glr_motor_type = (purpose_jsonArray.getJSONObject(i).getString("motor_type_name"));
                                glr_horse_power = (purpose_jsonArray.getJSONObject(i).getString("motor_hp"));
                                glr_tank_capacity = (purpose_jsonArray.getJSONObject(i).getString("motor_tank_capacity"));
                                glr_tank_count = (purpose_jsonArray.getJSONObject(i).getString("no_of_tanks"));
                                glr_motor_number = (purpose_jsonArray.getJSONObject(i).getString("motor_number"));
                                glr_motor_number_formated = (purpose_jsonArray.getJSONObject(i).getString("motor_number_formated"));
                                glr_motor_lat = (purpose_jsonArray.getJSONObject(i).getString("motor_lat"));
                                glr_motor_number_long = (purpose_jsonArray.getJSONObject(i).getString("motor_long"));
                              /*  oht_tank_lat = (purpose_jsonArray.getJSONObject(i).getString("tank_lat"));
                                oht_tank_long = (purpose_jsonArray.getJSONObject(i).getString("tank_long"));*/
                                glr_land_mark = (purpose_jsonArray.getJSONObject(i).getString("land_mark"));
                                glr_motor_image_available = (purpose_jsonArray.getJSONObject(i).getString("motor_image_available"));
                                if(glr_motor_image_available.equals("Y")){
                                    glr_motor_image = (purpose_jsonArray.getJSONObject(i).getString("motor_image"));
                                }
                                else {
                                    glr_motor_image="";
                                }

                              /*  oht_tank_image_available = (purpose_jsonArray.getJSONObject(i).getString("tank_image_available"));
                                if(oht_tank_image_available.equals("Y")){
                                    oht_tank_image = (purpose_jsonArray.getJSONObject(i).getString("tank_image"));
                                }else {
                                    oht_tank_image="";
                                }

*/

                            }

                            else {
                                connection_name = purpose_jsonArray.getJSONObject(i).getString("connection_name");

                            }

                        }
                    }else {
                        imagesLayoutBinding.purposeDetailsLayout.setVisibility(View.GONE);
                    }

                    imagesLayoutBinding.connectionNumberText.setText(getResources().getString(R.string.connection_no_)+" "+connection_number);
                    if (connection_status_id.equalsIgnoreCase("1")){
                        imagesLayoutBinding.connectionStatus.setText(getResources().getString(R.string.valid_connection));
                        imagesLayoutBinding.arrowIc.setRotation(270);
                        imagesLayoutBinding.arrowLayout.setBackgroundColor(this.getResources().getColor(R.color.accept));
                    }
                    else if (connection_status_id.equalsIgnoreCase("2")){
                        imagesLayoutBinding.connectionStatus.setText(getResources().getString(R.string.valid_connection_disconnected));
                        imagesLayoutBinding.arrowIc.setRotation(90);
                        imagesLayoutBinding.arrowLayout.setBackgroundColor(this.getResources().getColor(R.color.subscription_type_red_color));
                    }
                    else if (connection_status_id.equalsIgnoreCase("3")){
                        imagesLayoutBinding.connectionStatus.setText(getResources().getString(R.string.invalid_connection));
                        imagesLayoutBinding.arrowIc.setRotation(90);
                        imagesLayoutBinding.arrowLayout.setBackgroundColor(this.getResources().getColor(R.color.pink));
                    }
                    else {
                        imagesLayoutBinding.connectionStatus.setText("Not Updated");
                        imagesLayoutBinding.arrowIc.setRotation(0);
                        imagesLayoutBinding.arrowLayout.setBackgroundColor(this.getResources().getColor(R.color.grey_4));
                    }

                    imagesLayoutBinding.cusCode.setText(cuscode);
                    imagesLayoutBinding.consumerName.setText(consumer_name);
                    imagesLayoutBinding.location.setText(location);
                    imagesLayoutBinding.purposeAsPerTneb.setText(purpose_as_per_tneb);
                    imagesLayoutBinding.connectionPurpose.setText(item);

                    if(meter_available !=null && !meter_available.equalsIgnoreCase("")){
                        imagesLayoutBinding.meterAvailableStatusLayout.setVisibility(View.VISIBLE);
                        if(meter_available.equalsIgnoreCase("Y")){
                            imagesLayoutBinding.meterAvailableStatus.setText(getResources().getString(R.string.yes));
                        }
                        else {
                            imagesLayoutBinding.meterAvailableStatus.setText(getResources().getString(R.string.no));
                        }

                    }
                    if(no_of_street_light != null && !no_of_street_light.equalsIgnoreCase("")){
                        imagesLayoutBinding.noOfStreetLightCountLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.streetLightCount.setText(no_of_street_light);
                    }

                    ///OHT
                    if(!oht_tank_capacity.equals("")|| !oht_tank_count.equals("") || !oht_tank_hp.equals("") || !oht_motor_type.equals("")){
                        imagesLayoutBinding.ohtDetailsHeading.setVisibility(View.VISIBLE);
                    }
                    if( oht_tank_capacity!= null && !oht_tank_capacity.equalsIgnoreCase("")){
                        imagesLayoutBinding.tankCapacityLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.tankCapacity.setText(oht_tank_capacity);
                    }
                    if( oht_tank_count!= null && !oht_tank_count.equalsIgnoreCase("")){
                        imagesLayoutBinding.tankCountLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.tankCount.setText(oht_tank_count);
                    }
                    if(oht_tank_hp != null && !oht_tank_hp.equalsIgnoreCase("")){
                        imagesLayoutBinding.horsePowerLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.horsePower.setText(oht_tank_hp);
                    }
                    if(oht_motor_type != null && !oht_motor_type.equalsIgnoreCase("")){
                        imagesLayoutBinding.motorTypeLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.motorType.setText(oht_motor_type);
                    }
                    if(oht_land_mark != null && !oht_land_mark.equalsIgnoreCase("")){
                        imagesLayoutBinding.ohtMotorLandMarkLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.ohtMotorLandMark.setText(oht_land_mark);
                    }

                    /////Mini With Out Oht
                    ///OHT
                    if(mini_with_out_oht_horse_power != null && !mini_with_out_oht_horse_power.equals("") ||
                            (mini_with_out_oht_motor_type != null && !mini_with_out_oht_motor_type.equals(""))){
                        imagesLayoutBinding.miniWithOutOhtDetailsHeading.setVisibility(View.VISIBLE);
                    }
                    if(mini_with_out_oht_horse_power != null && !mini_with_out_oht_horse_power.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniWithOutOhtHorsePowerLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniWithOutOhtHorsePower.setText(mini_with_out_oht_horse_power);
                    }
                    if(mini_with_out_oht_motor_type != null && !mini_with_out_oht_motor_type.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniWithOutOhtMotorTypeLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniWithOutOhtMotorType.setText(mini_with_out_oht_motor_type);
                    }
                    if(mini_with_out_oht_land_mark != null && !mini_with_out_oht_land_mark.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniWithOutOhtMotorLandMarkLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniWithOutOhtMotorLandMark.setText(mini_with_out_oht_land_mark);
                    }

                    ///Mini OHT
                    if(mini_oht_motor_type != null && !mini_oht_motor_type.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniMotorTypeLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniMotorType.setText(mini_oht_motor_type);
                    }
                    if(!mini_oht_tank_capacity.equals("")|| !mini_oht_tank_count.equals("") || !mini_oht_tank_hp.equals("") || !mini_oht_land_mark.equals("")){
                        imagesLayoutBinding.miniOhtDetailsHeading.setVisibility(View.VISIBLE);
                    }
                    if(mini_oht_tank_capacity != null && !mini_oht_tank_capacity.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniTankCapacityLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniTankCapacity.setText(mini_oht_tank_capacity);
                    }
                    if(mini_oht_tank_count != null && !mini_oht_tank_count.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniTankCountLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniTankCount.setText(mini_oht_tank_count);
                    }
                    if(mini_oht_tank_hp != null && !mini_oht_tank_hp.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniHorsePowerLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniHorsePower.setText(mini_oht_tank_hp);
                    }
                    if(mini_oht_land_mark != null && !mini_oht_land_mark.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniMotorLandMarkLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniMotorLandMark.setText(mini_oht_land_mark);
                    }


                    ////GLR Details
                    if(!glr_tank_capacity.equals("")|| !glr_tank_count.equals("") || !glr_horse_power.equals("") || !glr_land_mark.equals("")){
                        imagesLayoutBinding.glrDetailsHeading.setVisibility(View.VISIBLE);
                    }
                    if( glr_tank_capacity!= null && !glr_tank_capacity.equalsIgnoreCase("")){
                        imagesLayoutBinding.glrTankCapacityLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.glrTankCapacity.setText(glr_tank_capacity);
                    }
                    if( glr_tank_count!= null && !glr_tank_count.equalsIgnoreCase("")){
                        imagesLayoutBinding.glrTankCountLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.glrTankCount.setText(glr_tank_count);
                    }
                    if(glr_horse_power != null && !glr_horse_power.equalsIgnoreCase("")){
                        imagesLayoutBinding.glrHorsePowerLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.glrHorsePower.setText(glr_horse_power);
                    }
                    if(glr_motor_type != null && !glr_motor_type.equalsIgnoreCase("")){
                        imagesLayoutBinding.glrMotorTypeLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.glrMotorType.setText(glr_motor_type);
                    }
                    if(glr_land_mark != null && !glr_land_mark.equalsIgnoreCase("")){
                        imagesLayoutBinding.glrMotorLandMarkLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.glrMotorLandMark.setText(glr_land_mark);
                    }
                    /////


                    /////Mini Power Pump Details
                    if(!mini_power_pump_tank_capacity.equals("")|| !mini_power_pump_tank_count.equals("") || !mini_power_pump_horse_power.equals("") || !mini_power_pump_land_mark.equals("")){
                        imagesLayoutBinding.miniPowerPumpDetailsHeading.setVisibility(View.VISIBLE);
                    }
                    if( mini_power_pump_tank_capacity!= null && !mini_power_pump_tank_capacity.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniPowerPumpTankCapacityLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniPowerPumpTankCapacity.setText(mini_power_pump_tank_capacity);
                    }
                    if( mini_power_pump_tank_count!= null && !mini_power_pump_tank_count.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniPowerPumpTankCountLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniPowerPumpTankCount.setText(mini_power_pump_tank_count);
                    }
                    if(mini_power_pump_horse_power != null && !mini_power_pump_horse_power.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniPowerPumpHorsePowerLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniPowerPumpHorsePower.setText(mini_power_pump_horse_power);
                    }
                    if(glr_motor_type != null && !glr_motor_type.equalsIgnoreCase("")){
                        imagesLayoutBinding.glrMotorTypeLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.glrMotorType.setText(glr_motor_type);
                    }
                    if(mini_power_pump_land_mark != null && !mini_power_pump_land_mark.equalsIgnoreCase("")){
                        imagesLayoutBinding.miniPowerPumpMotorLandMarkLayout.setVisibility(View.VISIBLE);
                        imagesLayoutBinding.miniPowerPumpMotorLandMark.setText(mini_power_pump_land_mark);
                    }

                    //////


                    if(eb_card_image_available.equalsIgnoreCase("Y") || last_bill_image_available.equalsIgnoreCase("Y") || meter_image_available.equalsIgnoreCase("Y")
                                || oht_tank_image_available.equals("Y")|| oht_motor_image_available.equals("Y")|| mini_oht_motor_image_available.equals("Y")||mini_oht_tank_image_available.equals("Y")
                            || glr_motor_image_available.equals("Y") || mini_power_pump_motor_image_available.equals("Y") || mini_with_out_oht_motor_image_available.equalsIgnoreCase("Y")) {
                        imagesLayoutBinding.imagesLayout.setVisibility(View.VISIBLE);
                        if(eb_card_image != null && !eb_card_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.ebCardImgLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.ebCardImg.setImageBitmap(StringToBitMap(eb_card_image));
                        }
                        if(last_bill_image != null && !last_bill_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.ebBillImgLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.ebBillImg.setImageBitmap(StringToBitMap(last_bill_image));
                        }
                        if(meter_image != null && !meter_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.ebMeterImgLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.ebMeterImg.setImageBitmap(StringToBitMap(meter_image));
                        }
                        if(oht_motor_image != null && !oht_motor_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.ohtMotorImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.ohtMotorImage.setImageBitmap(StringToBitMap(oht_motor_image));
                        }
                        if(mini_oht_motor_image != null && !mini_oht_motor_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniOhtMotorImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.miniOhtMotorImage.setImageBitmap(StringToBitMap(mini_oht_motor_image));
                        }
                        if(glr_motor_image != null && !glr_motor_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.grlMotorImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.grlMotorImage.setImageBitmap(StringToBitMap(glr_motor_image));
                        }
                        if(mini_power_pump_motor_image != null && !mini_power_pump_motor_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniPowerPumpMotorImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.miniPowerPumpMotorImage.setImageBitmap(StringToBitMap(mini_power_pump_motor_image));
                        }
                        if(mini_with_out_oht_motor_image != null && !mini_with_out_oht_motor_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniWithOutOhtMotorImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.miniWithOutOhtMotorImage.setImageBitmap(StringToBitMap(mini_with_out_oht_motor_image));
                        }
/*
                        if(oht_tank_image != null && !oht_tank_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.ohtTankImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.ohtMotorImage.setImageBitmap(StringToBitMap(oht_tank_image));
                        }
*/
/*
                        if(mini_oht_tank_image != null && !mini_oht_tank_image.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniOhtTankImageLayout.setVisibility(View.VISIBLE);
                            imagesLayoutBinding.miniOhtTankImage.setImageBitmap(StringToBitMap(mini_oht_tank_image));
                        }
*/
                        if(mini_oht_motor_number_formated != null && !mini_oht_motor_number_formated.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniOhtMotorNumber.setText(mini_oht_motor_number_formated);
                        }
                        if(oht_motor_number_formated != null && !oht_motor_number_formated.equalsIgnoreCase("")){
                            imagesLayoutBinding.ohtMotorNumber.setText(oht_motor_number_formated);
                        }
                        if(glr_motor_number_formated != null && !glr_motor_number_formated.equalsIgnoreCase("")){
                            imagesLayoutBinding.grlMotorNumber.setText(glr_motor_number_formated);
                        }
                        if(mini_power_pump_motor_number_formated != null && !mini_power_pump_motor_number_formated.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniPowerPumpMotorNumber.setText(mini_power_pump_motor_number_formated);
                        }
                        if(mini_with_out_oht_motor_number_formated != null && !mini_with_out_oht_motor_number_formated.equalsIgnoreCase("")){
                            imagesLayoutBinding.miniWithOutOhtMotorNumber.setText(mini_with_out_oht_motor_number_formated);
                        }
                    }else {
                        imagesLayoutBinding.imagesLayout.setVisibility(View.GONE);
                    }
                    imagesLayoutBinding.connectionDetailsLayout.setVisibility(View.VISIBLE);
                    imagesLayoutBinding.ll1.setVisibility(View.GONE);

                }else {
                    onBackPressed();
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
}
