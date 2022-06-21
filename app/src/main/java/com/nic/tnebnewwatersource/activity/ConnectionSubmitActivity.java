package com.nic.tnebnewwatersource.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.tnebnewwatersource.ImageZoom.ImageMatrixTouchHandler;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.CommonAdapter;
import com.nic.tnebnewwatersource.adapter.ConnectionStatusAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ConnectionSubmitActivityBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.MyLocationListener;
import com.nic.tnebnewwatersource.utils.CameraUtils;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.os.Build.VERSION_CODES.N;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by J.DILEEPKUMAR on 22/10/2021.
 */

public class ConnectionSubmitActivity extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {
    private ConnectionSubmitActivityBinding connectionSubmitActivityBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    private List<TNEBSystem> connectionPurposeList = new ArrayList<>();
    final ArrayList<Integer> mconPurItems = new ArrayList<>();
    String[] connPurStrings;
    boolean[] connPurCheckedItems;
    ConnectionStatusAdapter connectionStatusAdapter;
    private List<TNEBSystem> ConnectionStatus = new ArrayList<>();
    private List<TNEBSystem> MotorTypeList = new ArrayList<>();
    private List<TNEBSystem> GlrMotorTypeList = new ArrayList<>();
    private List<TNEBSystem> MiniMotorTypeList = new ArrayList<>();
    private List<TNEBSystem> MiniPowerPumpMotorTypeList = new ArrayList<>();
    private List<TNEBSystem> MiniWithOutOhtMotorTypeList = new ArrayList<>();
    private List<TNEBSystem> Habitation = new ArrayList<>();
    private List<TNEBSystem> TNEBMiniOhtHorsePowerList = new ArrayList<>();
    private List<TNEBSystem> TNEBOhtHorsePowerList = new ArrayList<>();
    private List<TNEBSystem> TNEBGLRHorsePowerList = new ArrayList<>();
    private List<TNEBSystem> TNEBMiniPowerPumpHorsePowerList = new ArrayList<>();
    private List<TNEBSystem> TNEBMiniWithOutOhtHorsePowerList = new ArrayList<>();
    private List<TNEBSystem> TNEBOhtTankCapacityList = new ArrayList<>();
    private List<TNEBSystem> TNEBMiniOhtTankCapacityList = new ArrayList<>();
    private List<TNEBSystem> TNEBGLRTankCapacityList = new ArrayList<>();
    private List<TNEBSystem> TNEBMiniPowerPumpTankCapacityList = new ArrayList<>();

    ///camera Clickable
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int OHT_LAND_MARK_SPEECH_REQUEST_CODE = 111;
    private static final int MINI_OHT_LAND_MARK_SPEECH_REQUEST_CODE = 112;
    private static final int GLR_LAND_MARK_SPEECH_REQUEST_CODE = 113;
    private static final int MINI_POWER_PUMP_LAND_MARK_SPEECH_REQUEST_CODE = 114;
    private static final int MINI_WITH_OUT_OHT_LAND_MARK_SPEECH_REQUEST_CODE = 115;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static String imageStoragePath;
    Double offlatTextValue =0.0, offlongTextValue =0.0;
    Double miniohtofflatTextValue =0.0, miniohtofflongTextValue =0.0;
    Double ohtofflatTextValue =0.0, ohtofflongTextValue =0.0;
    Double tankofflatTextValue =0.0, tankofflongTextValue =0.0;
    Double minitankofflatTextValue =0.0, minitankofflongTextValue =0.0;
    Double glrMotorofflatTextValue =0.0, glrMotorofflongTextValue =0.0;
    Double minipowerPumpofflatTextValue =0.0, minipowerPumpglrMotorofflongTextValue =0.0;
    Double miniwithOutOhtofflatTextValue =0.0, miniwithOutOhtMotorofflongTextValue =0.0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    String image_flag="";
    String eb_meter_image_string="";
    String last_eb_bill_image_string="";
    String eb_card_image_string="";
    String oht_tank_image_string="";
    String mini_oht_tank_image_string="";
    String oht_motor_image_string="";
    String glr_motor_image_string="";
    String mini_power_pump_motor_image_string="";
    String mini_oht_motor_image_string="";
    String mini_with_out_oht_motor_image_string="";

    byte[] eb_meter_image_byteArray;
    byte[] last_eb_bill_image_byteArray;
    byte[] eb_card_image_byteArray;
    byte[] oht_tank_image_byteArray;
    byte[] mini_oht_tank_image_byteArray;
    byte[] oht_motor_image_byteArray;
    byte[] glr_motor_image_byteArray;
    byte[] mini_power_pump_motor_image_byteArray;
    byte[] mini_oht_motor_image_byteArray;
    byte[] mini_with_out_oht_motor_image_byteArray;

    String mini_oht_tank_count="";
    String oht_tank_count="";

    String tank_capacity_oht="";
    String tank_capacity_mini_oht="";
    String tank_capacity_glr="";
    String tank_capacity_power_pump="";

    ///Bundle Values
    String id="";;
    String region_code="";;
    String circle_code="";;
    String section_code="";;
    String distribution_code="";;
    String consumer_code="";;
    String cuscode="";;
    String consumer_name="";;
    String location="";;
    String tariff="";;
    String tariff_desc="";;
    String purpose_as_per_tneb="";;
    String connection_number="";;
    String status_code="";

    String back_press_flag="";
    int selectedId=0;
    int selectedCount=0;
    int selectedStreetCount=0;
    String eb_meter_available_status="";
    String eb_meter_available_status_clicked_status="";

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    Double wayLatitude = 0.0, wayLongitude = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        connectionSubmitActivityBinding = DataBindingUtil.setContentView(this, R.layout.connection_submit_activity);
        connectionSubmitActivityBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String localLanguage = prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);
        prefManager.setHabCode("");
        prefManager.setKeyMotorType("");
        prefManager.setPurposeCodeJson(new JSONArray());
        loadConnectionStatusList();
        loadConnectionPurposeList();

        loadMotorTypeList();
        loadMiniMotorTypeList();
        loadGlrMotorTypeList();
        loadMiniPowerPumpMotorTypeList();
        loadMiniWithOutOhtMotorTypeList();

        loadMiniTNEBHorsePowerList();
        loadOhtTNEBHorsePowerList();
        loadTNEBGLRHorsePowerList();
        loadTNEBMiniPowerPumpHorsePowerList();
        loadTNEBMiniWithOutOhtHorsePowerList();

        habitationFilterSpinner(prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode());
        getBundleValues();
        //checkPermission();
        Utils.setupUI(connectionSubmitActivityBinding.parentLayout,this);
//        connectionSubmitActivityBinding.captureIcon.setVisibility(View.GONE);
        connectionSubmitActivityBinding.visibleGoneLayouts.setVisibility(View.GONE);
        connectionSubmitActivityBinding.selectedTv.setVisibility(View.GONE);
        connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.GONE);
        /*connectionSubmitActivityBinding.selectMotorType.setVisibility(View.GONE);
        connectionSubmitActivityBinding.motorTypeLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.enterMotorHp.setVisibility(View.GONE);
        connectionSubmitActivityBinding.motorHpLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.enterTankCapacity.setVisibility(View.GONE);
        connectionSubmitActivityBinding.tankCapacityLayout.setVisibility(View.GONE);
*/
        connectionSubmitActivityBinding.enterNoStreetLight.setVisibility(View.GONE);
        connectionSubmitActivityBinding.noStreetLightLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniMotorLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.ohtMotorLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.tankImageLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniTankImageLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.glrTankImageLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniPowerPumpGlrTankImageLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.glrMotorLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniPowerPumpMotorLayout.setVisibility(View.GONE);
        connectionSubmitActivityBinding.newOht.setVisibility(View.GONE);

        //speech Icon Visibility
        connectionSubmitActivityBinding.ohtCloseIcon.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniOhtCloseIcon.setVisibility(View.GONE);
        connectionSubmitActivityBinding.glrCloseIcon.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniPowerPumpCloseIcon.setVisibility(View.GONE);
        connectionSubmitActivityBinding.miniWithOutOhtCloseIcon.setVisibility(View.GONE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        connectionSubmitActivityBinding.connectionStatusRecycler.setLayoutManager(mLayoutManager);
        connectionSubmitActivityBinding.connectionStatusRecycler.setItemAnimator(new DefaultItemAnimator());
        connectionSubmitActivityBinding.connectionStatusRecycler.setHasFixedSize(true);
        connectionSubmitActivityBinding.connectionStatusRecycler.setNestedScrollingEnabled(false);
        connectionSubmitActivityBinding.connectionStatusRecycler.setFocusable(false);

        connectionStatusAdapter=new ConnectionStatusAdapter(this,ConnectionStatus,dbData);
        connectionSubmitActivityBinding.connectionStatusRecycler.setAdapter(connectionStatusAdapter);

        connectionSubmitActivityBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        connectionSubmitActivityBinding.habitationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setHabCode(Habitation.get(position).getHabitation_code());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        connectionSubmitActivityBinding.motorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setKeyMotorType(MotorTypeList.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniMotorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setMiniKeyMotorType(MiniMotorTypeList.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.glrMotorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setKEY_GLR_MOTOR_TYPE(GlrMotorTypeList.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniPowerPumpGlrMotorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setKEY_MINI_POWER_PUMP_MOTOR_TYPE(MiniPowerPumpMotorTypeList.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniWithOutOhtMotorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setKEY_MINI_WITH_OUT_OHT_MOTOR_TYPE(MiniWithOutOhtMotorTypeList.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        connectionSubmitActivityBinding.miniOhtMotorHorserPowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setmini_oht_motor_horse_power(TNEBMiniOhtHorsePowerList.get(position).getTneb_horse_power());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.ohtMotorHorserPowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setoht_motor_horse_power(TNEBOhtHorsePowerList.get(position).getTneb_horse_power());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.glrOhtMotorHorserPowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setglr_motor_horse_power(TNEBGLRHorsePowerList.get(position).getTneb_horse_power());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniPowerPumpGlrOhtMotorHorserPowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setmini_power_pump_horse_power(TNEBMiniPowerPumpHorsePowerList.get(position).getTneb_horse_power());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniWithOutOhtMotorHorserPowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    prefManager.setmini_with_out__oht_horse_power(TNEBMiniWithOutOhtHorsePowerList.get(position).getTneb_horse_power());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        connectionSubmitActivityBinding.ohtMotorTankCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tank_capacity_oht="Changed";
                    prefManager.setoht_motor_tank_capacity(TNEBOhtTankCapacityList.get(position).getTneb_tank_capacity());
                }
                else {
                    tank_capacity_oht="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniOhtMotorTankCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tank_capacity_mini_oht="Changed";
                    prefManager.setmini_oht_motor_tank_capacity(TNEBMiniOhtTankCapacityList.get(position).getTneb_tank_capacity());
                }
                else {
                    tank_capacity_mini_oht="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.glrOhtMotorTankCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tank_capacity_glr="Changed";
                    prefManager.setglr_motor_tank_capacity(TNEBGLRTankCapacityList.get(position).getTneb_tank_capacity());
                }
                else {
                    tank_capacity_glr="Changed";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connectionSubmitActivityBinding.miniPowerPumpGlrOhtMotorTankCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tank_capacity_power_pump="Changed";
                    prefManager.setmini_power_pump_tank_capacity(TNEBMiniPowerPumpTankCapacityList.get(position).getTneb_tank_capacity());
                }
                else {
                    tank_capacity_power_pump="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*
        connectionSubmitActivityBinding.ebMeterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mconPurItems.clear();
                    mOHTItems.clear();
                    mPowerPumpItems.clear();
                     ebMeterTypeCode= EBMeterType.get(position).getEb_meter_type_code();
                     ebMeterTypeName= EBMeterType.get(position).getEb_meter_type_name();
                    getEBMeterItemList();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        connectionSubmitActivityBinding.ebLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connecPurposeCheckbox();
            }
        });

        connectionSubmitActivityBinding.captureEbMeterLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.captureEbBillLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.captureEbCardLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.closeIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.btnsubmit.setOnClickListener(this);
        connectionSubmitActivityBinding.homeIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.editEbMeterIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.editLastEbCardIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.editLastEbBillIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.miniTankCaptureLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.tankCaptureLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.miniCaptureMotorLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.captureMotorLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.miniEditCaptureMotorIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.miniTankEditCaptureIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.editCaptureMotorIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.tankEditCaptureIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.glrCaptureMotorLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.glrEditCaptureMotorIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorLayout.setOnClickListener(this);
        connectionSubmitActivityBinding.miniPowerPumpGlrEditCaptureMotorIcon.setOnClickListener(this);

        connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorLayout.setOnClickListener(this);
        //connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.setOnClickListener(this);
        connectionSubmitActivityBinding.miniWithOutOhtEditCaptureMotorIcon.setOnClickListener(this);


        ///speech listener
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        /*connectionSubmitActivityBinding.audio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        connectionSubmitActivityBinding.tankCapacityEt.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        connectionSubmitActivityBinding.tankCapacityEt.setText("");
                        connectionSubmitActivityBinding.tankCapacityEt.setHint("Listening...");
                        break;
                }
                return false;
            }
        });*/




        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    connectionSubmitActivityBinding.tankCapacityEt.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        connectionSubmitActivityBinding.ohtSpeechIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                speechToText("OHT");
            }
        });
        connectionSubmitActivityBinding.miniOhtSpeechIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                speechToText("MINI_OHT");
            }
        });
        connectionSubmitActivityBinding.glrOhtSpeechIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                speechToText("GLR");
            }
        });
        connectionSubmitActivityBinding.miniPowerPumpGlrOhtSpeechIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                speechToText("MINI_POWER_PUMP");
            }
        });
        connectionSubmitActivityBinding.miniWithOutOhtSpeechIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                speechToText("MINI_WITH_OUT_OHT");
            }
        });
        connectionSubmitActivityBinding.ohtCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                connectionSubmitActivityBinding.ohtCloseIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.ohtSpeechIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.landMarkEt.setText("");
            }
        });
        connectionSubmitActivityBinding.miniOhtCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                connectionSubmitActivityBinding.miniOhtCloseIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniOhtSpeechIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniLandMarkEt.setText("");
            }
        });
        connectionSubmitActivityBinding.glrCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                connectionSubmitActivityBinding.glrCloseIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.glrOhtSpeechIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.glrLandMarkEt.setText("");
            }
        });
        connectionSubmitActivityBinding.miniPowerPumpCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                connectionSubmitActivityBinding.miniPowerPumpCloseIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniPowerPumpGlrOhtSpeechIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniPowerPumpGlrLandMarkEt.setText("");
            }
        });
        connectionSubmitActivityBinding.miniWithOutOhtCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                connectionSubmitActivityBinding.miniWithOutOhtCloseIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniWithOutOhtSpeechIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniWithOutOhtLandMarkEt.setText("");
            }
        });

        eb_meter_available_status_clicked_status="N";
        connectionSubmitActivityBinding.radioYN.clearCheck();
        connectionSubmitActivityBinding.ebMeterAvailableStatusYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    eb_meter_available_status="Y";
                    eb_meter_available_status_clicked_status="Y";
                    connectionSubmitActivityBinding.ebMeterAvailableStatusNo.setChecked(false);
                }
            }
        });
        connectionSubmitActivityBinding.ebMeterAvailableStatusNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    eb_meter_available_status="N";
                    eb_meter_available_status_clicked_status="Y";
                    connectionSubmitActivityBinding.ebMeterAvailableStatusYes.setChecked(false);
                }
            }
        });

        //isInCampus(13.159037, 80.302183, 13.158910, 80.302132);
        //isInCampus(13.158837, 80.302098, 13.158910, 80.302132);
        //isInCampus(13.159037, 80.302183, 13.158986, 80.302143);
        //isInCampus(13.159037, 80.302183, 13.158910, 80.302132);
        //isInCampus(13.159037, 80.302183, 13.158910, 80.302132);
        //isInCampus(13.159037, 80.302183, 13.158972, 80.302157);

        //isInCampus(13.159037, 80.302183, 13.158961, 80.302193);
        //geofenve();

    }

    public void getBundleValues(){
        Bundle bundle = getIntent().getExtras();
        id=bundle.getString("id","");
        region_code=bundle.getString("region_code","");
        circle_code=bundle.getString("circle_code","");
        section_code=bundle.getString("section_code","");
        distribution_code=bundle.getString("distribution_code","");
        consumer_code=bundle.getString("consumer_code","");
        cuscode=bundle.getString("cuscode","");
        consumer_name=bundle.getString("consumer_name","");
        location=bundle.getString("location","");
        tariff=bundle.getString("tariff","");
        tariff_desc=bundle.getString("tariff_desc","");
        purpose_as_per_tneb=bundle.getString("purpose_as_per_tneb","");
        connection_number=bundle.getString("connection_number","");

        connectionSubmitActivityBinding.connectionNumber.setText(" "+connection_number);
        connectionSubmitActivityBinding.name.setText(consumer_name);
        connectionSubmitActivityBinding.locationValue.setText(location);
        connectionSubmitActivityBinding.purposeValue.setText(purpose_as_per_tneb);
    }
    public void habitationFilterSpinner(String dcode, String bcode, String pvcode) {
        Cursor HABList = null;
        HABList = db.rawQuery("SELECT * FROM " + DBHelper.HABITATION_TABLE_NAME + " where dcode = '" + dcode + "'and bcode = '" + bcode + "' and pvcode = '" + pvcode + "' order by habitation_name asc", null);

        Habitation.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setHabitation_name("Select Habitation");
        habitationListValue.setHabitation_name_ta("குக்கிராமத்தைத் தேர்ந்தெடுக்கவும்");
        Habitation.add(habitationListValue);
        if (HABList.getCount() > 0) {
            if (HABList.moveToFirst()) {
                do {
                    TNEBSystem habList = new TNEBSystem();
                    String districtCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String blockCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.BLOCK_CODE));
                    String pvCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String habCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABB_CODE));
                    String habName = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABITATION_NAME));
                    String habName_ta = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABITATION_NAME_TA));

                    habList.setDistictCode(districtCode);
                    habList.setBlockCode(blockCode);
                    habList.setPvCode(pvCode);
                    habList.setHabitation_code(habCode);
                    habList.setHabitation_name(habName);
                    habList.setHabitation_name_ta(habName_ta);



                    Habitation.add(habList);
                    Log.d("spinnersize", "" + Habitation.size());
                } while (HABList.moveToNext());
            }
        }
        connectionSubmitActivityBinding.habitationSpinner.setAdapter(new CommonAdapter(this, Habitation, "HabitationList"));
    }

    public void loadMotorTypeList() {
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + DBHelper.MOTOR_TYPE_LIST , null);

        MotorTypeList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setMotor_type(getResources().getString(R.string.select_motor_type_));
        MotorTypeList.add(habitationListValue);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String MotorType = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE));

                    tnebSystem.setId(id);
                    tnebSystem.setMotor_type(MotorType);

                    MotorTypeList.add(tnebSystem);
                    Log.d("spinnersize", "" + MotorTypeList.size());
                } while (cursor.moveToNext());
            }
        }
        connectionSubmitActivityBinding.motorTypeSpinner.setAdapter(new CommonAdapter(this, MotorTypeList, "MotorTypeList"));
    }
    public void loadGlrMotorTypeList() {
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + DBHelper.MOTOR_TYPE_LIST , null);

        GlrMotorTypeList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setMotor_type(getResources().getString(R.string.select_motor_type_));
        GlrMotorTypeList.add(habitationListValue);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String MotorType = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE));

                    tnebSystem.setId(id);
                    tnebSystem.setMotor_type(MotorType);

                    GlrMotorTypeList.add(tnebSystem);
                    Log.d("spinnersize", "" + GlrMotorTypeList.size());
                } while (cursor.moveToNext());
            }
        }
        connectionSubmitActivityBinding.glrMotorTypeSpinner.setAdapter(new CommonAdapter(this, GlrMotorTypeList, "GlrMotorTypeList"));
    }
    public void loadMiniPowerPumpMotorTypeList() {
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + DBHelper.MOTOR_TYPE_LIST , null);

        MiniPowerPumpMotorTypeList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setMotor_type(getResources().getString(R.string.select_motor_type_));
        MiniPowerPumpMotorTypeList.add(habitationListValue);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String MotorType = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE));

                    tnebSystem.setId(id);
                    tnebSystem.setMotor_type(MotorType);

                    MiniPowerPumpMotorTypeList.add(tnebSystem);
                    Log.d("spinnersize", "" + MiniPowerPumpMotorTypeList.size());
                } while (cursor.moveToNext());
            }
        }
        connectionSubmitActivityBinding.miniPowerPumpGlrMotorTypeSpinner.setAdapter(new CommonAdapter(this, MiniPowerPumpMotorTypeList, "MiniPowerPumpMotorTypeList"));
    }
    public void loadMiniWithOutOhtMotorTypeList() {
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + DBHelper.MOTOR_TYPE_LIST , null);

        MiniWithOutOhtMotorTypeList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setMotor_type(getResources().getString(R.string.select_motor_type_));
        MiniWithOutOhtMotorTypeList.add(habitationListValue);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String MotorType = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE));

                    tnebSystem.setId(id);
                    tnebSystem.setMotor_type(MotorType);

                    MiniWithOutOhtMotorTypeList.add(tnebSystem);
                    Log.d("spinnersize", "" + MiniWithOutOhtMotorTypeList.size());
                } while (cursor.moveToNext());
            }
        }
        connectionSubmitActivityBinding.miniWithOutOhtMotorTypeSpinner.setAdapter(new CommonAdapter(this, MiniWithOutOhtMotorTypeList, "MiniWithOutOhtMotorTypeList"));
    }
    public void loadMiniMotorTypeList() {
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + DBHelper.MOTOR_TYPE_LIST , null);

        MiniMotorTypeList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setMotor_type(getResources().getString(R.string.select_motor_type_));
        MiniMotorTypeList.add(habitationListValue);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String MotorType = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE));

                    tnebSystem.setId(id);
                    tnebSystem.setMotor_type(MotorType);

                    MiniMotorTypeList.add(tnebSystem);
                    Log.d("spinnersize", "" + MiniMotorTypeList.size());
                } while (cursor.moveToNext());
            }
        }
        connectionSubmitActivityBinding.miniMotorTypeSpinner.setAdapter(new CommonAdapter(this, MiniMotorTypeList, "MiniMotorTypeList"));
    }

    public void viewDetail(String code) {
        status_code=code;
        if (code.equalsIgnoreCase("1")){
            connectionSubmitActivityBinding.visibleGoneLayouts.setVisibility(View.VISIBLE);
            //connectionSubmitActivityBinding.ebMeterLayout.setVisibility(View.VISIBLE);
        }else if (code.equalsIgnoreCase("2")){
            connectionSubmitActivityBinding.visibleGoneLayouts.setVisibility(View.GONE);
            //connectionSubmitActivityBinding.ebMeterLayout.setVisibility(View.GONE);
        }else if (code.equalsIgnoreCase("3")){
            connectionSubmitActivityBinding.visibleGoneLayouts.setVisibility(View.GONE);
            //connectionSubmitActivityBinding.ebMeterLayout.setVisibility(View.GONE);
        }


    }
    public void loadConnectionStatusList() {
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + DBHelper.CONNECTION_STATUS , null);

        ConnectionStatus.clear();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String connectionCode = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_ID));
                    String connectionStatus = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_STATUS));

                    list.setConncetion_id(connectionCode);
                    list.setConnection_status(connectionStatus);

                    ConnectionStatus.add(list);

                    Log.d("ConnectionStatus", "" + ConnectionStatus.size());
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(ConnectionStatus, (lhs, rhs) -> lhs.getConncetion_id().compareTo(rhs.getConncetion_id()));
    }

    public void loadMiniTNEBHorsePowerList(){
        TNEBMiniOhtHorsePowerList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_hp_id("0");
        tnebSystem.setTneb_horse_power(getResources().getString(R.string.select_motor_hp_));
        TNEBMiniOhtHorsePowerList.add(tnebSystem);
        TNEBMiniOhtHorsePowerList.addAll(dbData.getAll_Tneb_Horse_Power_List());
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBMiniOhtHorsePowerList.sort((o1, o2)
                    -> o1.getTneb_hp_id().compareTo(
                    o2.getTneb_hp_id()));
        }*/
        Collections.sort(TNEBMiniOhtHorsePowerList, (lhs, rhs) -> lhs.getTneb_hp_id().compareTo(rhs.getTneb_hp_id()));


        connectionSubmitActivityBinding.miniOhtMotorHorserPowerSpinner.setAdapter(new CommonAdapter(this, TNEBMiniOhtHorsePowerList, "TNEBMiniOhtHorsePower"));
    }
    public void loadOhtTNEBHorsePowerList(){
        TNEBOhtHorsePowerList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_hp_id("0");
        tnebSystem.setTneb_horse_power(getResources().getString(R.string.select_motor_hp_));
        TNEBOhtHorsePowerList.add(tnebSystem);
        TNEBOhtHorsePowerList.addAll(dbData.getAll_Tneb_Horse_Power_List());
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBOhtHorsePowerList.sort((o1, o2)
                    -> o1.getTneb_hp_id().compareTo(
                    o2.getTneb_hp_id()));
        }*/
        Collections.sort(TNEBOhtHorsePowerList, (lhs, rhs) -> lhs.getTneb_hp_id().compareTo(rhs.getTneb_hp_id()));
        connectionSubmitActivityBinding.ohtMotorHorserPowerSpinner.setAdapter(new CommonAdapter(this, TNEBOhtHorsePowerList, "TNEBOhtHorsePower"));
    }
    public void loadTNEBGLRHorsePowerList(){
        TNEBGLRHorsePowerList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_hp_id("0");
        tnebSystem.setTneb_horse_power(getResources().getString(R.string.select_motor_hp_));
        TNEBGLRHorsePowerList.add(tnebSystem);
        TNEBGLRHorsePowerList.addAll(dbData.getAll_Tneb_Horse_Power_List());
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBGLRHorsePowerList.sort((o1, o2)
                    -> o1.getTneb_hp_id().compareTo(
                    o2.getTneb_hp_id()));
        }*/
        Collections.sort(TNEBGLRHorsePowerList, (lhs, rhs) -> lhs.getTneb_hp_id().compareTo(rhs.getTneb_hp_id()));
        connectionSubmitActivityBinding.glrOhtMotorHorserPowerSpinner.setAdapter(new CommonAdapter(this, TNEBGLRHorsePowerList, "TNEBGLRHorsePowerList"));
    }
    public void loadTNEBMiniPowerPumpHorsePowerList(){
        TNEBMiniPowerPumpHorsePowerList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_hp_id("0");
        tnebSystem.setTneb_horse_power(getResources().getString(R.string.select_motor_hp_));
        TNEBMiniPowerPumpHorsePowerList.add(tnebSystem);
        TNEBMiniPowerPumpHorsePowerList.addAll(dbData.getAll_Tneb_Horse_Power_List());
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBMiniPowerPumpHorsePowerList.sort((o1, o2)
                    -> o1.getTneb_hp_id().compareTo(
                    o2.getTneb_hp_id()));
        }*/
        Collections.sort(TNEBMiniPowerPumpHorsePowerList, (lhs, rhs) -> lhs.getTneb_hp_id().compareTo(rhs.getTneb_hp_id()));
        connectionSubmitActivityBinding.miniPowerPumpGlrOhtMotorHorserPowerSpinner.setAdapter(new CommonAdapter(this, TNEBMiniPowerPumpHorsePowerList, "TNEBMiniPowerPumpHorsePowerList"));
    }
    public void loadTNEBMiniWithOutOhtHorsePowerList(){
        TNEBMiniWithOutOhtHorsePowerList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_hp_id("0");
        tnebSystem.setTneb_horse_power(getResources().getString(R.string.select_motor_hp_));
        TNEBMiniWithOutOhtHorsePowerList.add(tnebSystem);
        TNEBMiniWithOutOhtHorsePowerList.addAll(dbData.getAll_Tneb_Horse_Power_List());
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBMiniPowerPumpHorsePowerList.sort((o1, o2)
                    -> o1.getTneb_hp_id().compareTo(
                    o2.getTneb_hp_id()));
        }*/
        Collections.sort(TNEBMiniWithOutOhtHorsePowerList, (lhs, rhs) -> lhs.getTneb_hp_id().compareTo(rhs.getTneb_hp_id()));
        connectionSubmitActivityBinding.miniWithOutOhtMotorHorserPowerSpinner.setAdapter(new CommonAdapter(this, TNEBMiniWithOutOhtHorsePowerList, "TNEBMiniWithOutOhtHorsePowerList"));
    }

    public void loadTNEBOhtTankCapacityList(String id){
        TNEBOhtTankCapacityList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_tank_capacity__id("0");
        tnebSystem.setTneb_tank_capacity_purpose_id("0");
        tnebSystem.setTneb_tank_capacity(getResources().getString(R.string.select_tank_capacity_));
        TNEBOhtTankCapacityList.add(tnebSystem);
        TNEBOhtTankCapacityList.addAll(dbData.get_Particular_Tneb_Tank_Capacity_List(id));
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBOhtTankCapacityList.sort((o1, o2)
                    -> o1.getTneb_tank_capacity__id().compareTo(
                    o2.getTneb_tank_capacity__id()));
        }*/
        Collections.sort(TNEBOhtTankCapacityList, (lhs, rhs) -> lhs.getTneb_tank_capacity__id().compareTo(rhs.getTneb_tank_capacity__id()));
        connectionSubmitActivityBinding.ohtMotorTankCapacitySpinner.setAdapter(new CommonAdapter(this, TNEBOhtTankCapacityList, "TNEBOhtTankCapacityList"));
    }
    public void loadTNEBGLRTankCapacityList(String id){
        TNEBGLRTankCapacityList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_tank_capacity__id("0");
        tnebSystem.setTneb_tank_capacity_purpose_id("0");
        tnebSystem.setTneb_tank_capacity(getResources().getString(R.string.select_tank_capacity_));
        TNEBGLRTankCapacityList.add(tnebSystem);
        TNEBGLRTankCapacityList.addAll(dbData.get_Particular_Tneb_Tank_Capacity_List(id));
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBGLRTankCapacityList.sort((o1, o2)
                    -> o1.getTneb_tank_capacity__id().compareTo(
                    o2.getTneb_tank_capacity__id()));
        }*/
        Collections.sort(TNEBGLRTankCapacityList, (lhs, rhs) -> lhs.getTneb_tank_capacity__id().compareTo(rhs.getTneb_tank_capacity__id()));
        connectionSubmitActivityBinding.glrOhtMotorTankCapacitySpinner.setAdapter(new CommonAdapter(this, TNEBGLRTankCapacityList, "TNEBGLRTankCapacityList"));
    }
    public void loadTNEBMiniOhtTankCapacityList(String id){
        TNEBMiniOhtTankCapacityList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_tank_capacity__id("0");
        tnebSystem.setTneb_tank_capacity_purpose_id("0");
        tnebSystem.setTneb_tank_capacity(getResources().getString(R.string.select_tank_capacity_));
        TNEBMiniOhtTankCapacityList.add(tnebSystem);
        TNEBMiniOhtTankCapacityList.addAll(dbData.get_Particular_Tneb_Tank_Capacity_List(id));
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBMiniOhtTankCapacityList.sort((o1, o2)
                    -> o1.getTneb_tank_capacity__id().compareTo(
                    o2.getTneb_tank_capacity__id()));
        }*/
        Collections.sort(TNEBMiniOhtTankCapacityList, (lhs, rhs) -> lhs.getTneb_tank_capacity__id().compareTo(rhs.getTneb_tank_capacity__id()));
        connectionSubmitActivityBinding.miniOhtMotorTankCapacitySpinner.setAdapter(new CommonAdapter(this, TNEBMiniOhtTankCapacityList, "TNEBMiniOhtTankCapacityList"));
    }
    public void loadTNEBMiniPowerPumpTankCapacityList(String id){
        TNEBMiniPowerPumpTankCapacityList.clear();
        dbData.open();
        TNEBSystem tnebSystem=new TNEBSystem();
        tnebSystem.setTneb_tank_capacity__id("0");
        tnebSystem.setTneb_tank_capacity_purpose_id("0");
        tnebSystem.setTneb_tank_capacity(getResources().getString(R.string.select_tank_capacity_));
        TNEBMiniPowerPumpTankCapacityList.add(tnebSystem);
        TNEBMiniPowerPumpTankCapacityList.addAll(dbData.get_Particular_Tneb_Tank_Capacity_List(id));
        /*if (Build.VERSION.SDK_INT >= N) {
            TNEBMiniPowerPumpTankCapacityList.sort((o1, o2)
                    -> o1.getTneb_tank_capacity__id().compareTo(
                    o2.getTneb_tank_capacity__id()));
        }*/
        Collections.sort(TNEBMiniPowerPumpTankCapacityList, (lhs, rhs) -> lhs.getTneb_tank_capacity__id().compareTo(rhs.getTneb_tank_capacity__id()));
        connectionSubmitActivityBinding.miniPowerPumpGlrOhtMotorTankCapacitySpinner.setAdapter(new CommonAdapter(this, TNEBMiniPowerPumpTankCapacityList, "TNEBMiniPowerPumpTankCapacityList"));
    }


    @Override
    public void onBackPressed() {
        if(connectionSubmitActivityBinding.capturedImageLayout.getVisibility()==View.VISIBLE){
            connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.GONE);
        }
        else {
            if(back_press_flag.equals("yes")){
                gotoHomePage();
            }
            else {
                super.onBackPressed();
                setResult(Activity.RESULT_CANCELED);
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            }
        }

    }

    public void gotoHomePage(){
        Intent homepage=new Intent(this,HomePage.class);
        homepage.putExtra("Home", "Login");
        startActivity(homepage);
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }

    public void loadConnectionPurposeList() {
        Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.CONNECTION_PURPOSE_LIST, null);
        connectionPurposeList.clear();
        final ArrayList<String> list = new ArrayList<String>();


        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_ID));
                    String conn_name = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NAME));
                    String conn_name_ta = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NAME_TA));
                    tnebSystem.setConncetion_id(id);
                    tnebSystem.setConncetion_name(conn_name);
                    tnebSystem.setConnection_name_ta(conn_name_ta);
                    connectionPurposeList.add(tnebSystem);
                    //   Log.d("finyeardb", "" + finyearList);
                } while (cursor.moveToNext());
            }

        }

        for (int i = 0; i < connectionPurposeList.size(); i++) {
            if(prefManager.getLocalLanguage().equals("en")){
            list.add(connectionPurposeList.get(i).getConncetion_name());
            }
            if(prefManager.getLocalLanguage().equals("ta")){
                list.add(connectionPurposeList.get(i).getConnection_name_ta());
            }

        }
        String[] mStringArray = new String[list.size()];
        connPurStrings = list.toArray(mStringArray);
        connPurCheckedItems = new boolean[connPurStrings.length];

    }
    public void connecPurposeCheckbox(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConnectionSubmitActivity.this);
        mBuilder.setTitle(getResources().getString(R.string.select_purpose_type));
        mBuilder.setMultiChoiceItems(connPurStrings, connPurCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    if (!mconPurItems.contains(position)) {
                        mconPurItems.add(position);

                    }
                } else if (mconPurItems.contains(position)) {
                    mconPurItems.remove(Integer.valueOf(position));

                }

                try {
                    JSONArray purposecodeJsonArray = new JSONArray();

                    for (int i = 0; i < mconPurItems.size(); i++) {
                        //purposecodeJsonArray.put(connPurStrings[mconPurItems.get(i)]);
                        purposecodeJsonArray.put(Integer.parseInt(connectionPurposeList.get(mconPurItems.get(i)).getConncetion_id()));
                        prefManager.setPurposeCodeJson(purposecodeJsonArray);
                        Log.d("purpose_code", "" + purposecodeJsonArray);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        mBuilder.setCancelable(false);
        //   final String[] finalFinYearStrings = finyearStrings;
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                selectedId=0;
                selectedCount=0;
                selectedStreetCount=0;
                for (int i = 0; i < mconPurItems.size(); i++) {
                    item = item + connPurStrings[mconPurItems.get(i)];
                    if (i != mconPurItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                if(mconPurItems.size() > 0){
                    connectionSubmitActivityBinding.selectedTv.setVisibility(View.VISIBLE);
                }else {
                    connectionSubmitActivityBinding.selectedTv.setVisibility(View.GONE);
                }
                String text = " <font color='#125d98'>"+"<b>"+item+"</b>"+"</font>";
                connectionSubmitActivityBinding.selectedTv.setText(Html.fromHtml(text));

                /*connectionSubmitActivityBinding.selectMotorType.setVisibility(View.GONE);
                connectionSubmitActivityBinding.motorTypeLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.enterMotorHp.setVisibility(View.GONE);
                connectionSubmitActivityBinding.motorHpLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.enterTankCapacity.setVisibility(View.GONE);
                connectionSubmitActivityBinding.tankCapacityLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.enterNoStreetLight.setVisibility(View.GONE);
                connectionSubmitActivityBinding.noStreetLightLayout.setVisibility(View.GONE);
                */
                //connectionSubmitActivityBinding.ohtMotorLayout.setVisibility(View.GONE);
                //connectionSubmitActivityBinding.miniMotorLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.enterNoStreetLight.setVisibility(View.GONE);
                connectionSubmitActivityBinding.noStreetLightLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniMotorLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.ohtMotorLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.glrMotorLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniPowerPumpMotorLayout.setVisibility(View.GONE);
                connectionSubmitActivityBinding.newOht.setVisibility(View.GONE);


                for (int i = 0; i < mconPurItems.size(); i++) {
                    selectedId=Integer.parseInt(connectionPurposeList.get(mconPurItems.get(i)).getConncetion_id());
                    /*if(selectedId==15 || selectedId==20){
                        selectedCount=selectedCount+1;
                        connectionSubmitActivityBinding.selectMotorType.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.motorTypeLayout.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.enterMotorHp.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.motorHpLayout.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.enterTankCapacity.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.tankCapacityLayout.setVisibility(View.VISIBLE);
                    }
                    else if(selectedId==34){
                        selectedStreetCount=selectedStreetCount+1;
                        connectionSubmitActivityBinding.enterNoStreetLight.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.noStreetLightLayout.setVisibility(View.VISIBLE);
                    }*/
                    if(selectedId==20){
                        if(tank_capacity_oht.equals("")) {
                            loadTNEBOhtTankCapacityList("20");
                        }
                        selectedCount=selectedCount+1;
                        connectionSubmitActivityBinding.ohtMotorLayout.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.tankImageLayout.setVisibility(View.GONE);

                    }
                    if(selectedId==9){
                        if(tank_capacity_glr.equals("")) {
                            loadTNEBGLRTankCapacityList("9");
                        }
                        selectedCount=selectedCount+1;
                        connectionSubmitActivityBinding.glrMotorLayout.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.glrTankImageLayout.setVisibility(View.GONE);

                    }
                    else if(selectedId==45){
                        if(tank_capacity_power_pump.equals("")) {
                            loadTNEBMiniPowerPumpTankCapacityList("45");
                        }
                        selectedCount=selectedCount+1;
                        connectionSubmitActivityBinding.miniPowerPumpMotorLayout.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniPowerPumpGlrTankImageLayout.setVisibility(View.GONE);

                    }
                    else if(selectedId==15){
                        if(tank_capacity_mini_oht.equals("")) {
                            loadTNEBMiniOhtTankCapacityList("15");
                        }
                        selectedCount=selectedCount+1;
                        connectionSubmitActivityBinding.miniMotorLayout.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniTankImageLayout.setVisibility(View.GONE);

                    }
                    else if(selectedId==46){
                        selectedCount=selectedCount+1;
                        connectionSubmitActivityBinding.newOht.setVisibility(View.VISIBLE);

                    }
                    else if(selectedId==34){
                        selectedStreetCount=selectedStreetCount+1;
                        connectionSubmitActivityBinding.enterNoStreetLight.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.noStreetLightLayout.setVisibility(View.VISIBLE);

                    }

                }
                if(selectedCount == 0){
                    connectionSubmitActivityBinding.motorTypeSpinner.setSelection(0);
                    prefManager.setKeyMotorType("");
                    connectionSubmitActivityBinding.motorHpEt.setText("");
                    connectionSubmitActivityBinding.tankCapacityEt.setText("");
                }
                if(selectedStreetCount == 0){
                    connectionSubmitActivityBinding.noStreetLightEt.setText("");
                }

            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < connPurCheckedItems.length; i++) {
                    connPurCheckedItems[i] = false;
                    mconPurItems.clear();
                    connectionSubmitActivityBinding.selectedTv.setText("");
                    connectionSubmitActivityBinding.selectedTv.setVisibility(View.GONE);
                     selectedId=0;
                     connectionSubmitActivityBinding.motorTypeSpinner.setSelection(0);
                    prefManager.setKeyMotorType("");
                    prefManager.setMiniKeyMotorType("");
                    prefManager.setoht_motor_horse_power("");
                    prefManager.setmini_oht_motor_horse_power("");
                    prefManager.setoht_motor_tank_capacity("");
                    prefManager.setmini_oht_motor_tank_capacity("");
                    connectionSubmitActivityBinding.motorHpEt.setText("");
                    connectionSubmitActivityBinding.tankCapacityEt.setText("");
                    connectionSubmitActivityBinding.noStreetLightEt.setText("");

                    /*connectionSubmitActivityBinding.selectMotorType.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.motorTypeLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.enterMotorHp.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.motorHpLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.enterTankCapacity.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.tankCapacityLayout.setVisibility(View.GONE);*/
                    connectionSubmitActivityBinding.enterNoStreetLight.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.noStreetLightLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.miniMotorLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.ohtMotorLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.glrMotorLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.miniPowerPumpMotorLayout.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.newOht.setVisibility(View.GONE);
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }


    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("saveTrackDataList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, getResources().getString(R.string.your_track_data_synchronized));
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }


        }catch (JSONException e){

        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public Uri getImageUri1(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public  Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,"IMG_" + Calendar.getInstance().getTime(),null);
        return Uri.parse(path);
    }
    public Bitmap previewCapturedImage(Bitmap i) {
        Bitmap rotatedBitmap = null;
        Bitmap bitmap = null;
        try {
            // hide video preview
            if(i != null){
                bitmap=i;
            }else {
                bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            }

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageStoragePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }

            if(image_flag.equals("capture_eb_meter_layout")){
                connectionSubmitActivityBinding.previewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.editEbMeterIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.ebMeterIcon.setImageBitmap(rotatedBitmap);
                eb_meter_image_string=BitMapToString(rotatedBitmap);
                eb_meter_image_byteArray=BitmaptoByteArray(rotatedBitmap);

            }
            else if(image_flag.equals("capture_eb_bill_layout")) {
                connectionSubmitActivityBinding.previewIcon1.setVisibility(View.GONE);
                connectionSubmitActivityBinding.lastEbBillIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.editLastEbBillIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.lastEbBillIcon.setImageBitmap(rotatedBitmap);
                last_eb_bill_image_string=BitMapToString(rotatedBitmap);
                last_eb_bill_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("capture_eb_card_layout")){
                connectionSubmitActivityBinding.previewIcon2.setVisibility(View.GONE);
                connectionSubmitActivityBinding.lastEbCardIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.editLastEbCardIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.lastEbCardIcon.setImageBitmap(rotatedBitmap);
                eb_card_image_string=BitMapToString(rotatedBitmap);
                eb_card_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("mini_capture_motor_layout")){
                connectionSubmitActivityBinding.miniCaptureMotorPreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                mini_oht_motor_image_string=BitMapToString(rotatedBitmap);
                mini_oht_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("mini_with_out_oht_capture_motor_layout")){
                connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorPreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniWithOutOhtEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                mini_with_out_oht_motor_image_string=BitMapToString(rotatedBitmap);
                mini_with_out_oht_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("capture_motor_layout")){
                connectionSubmitActivityBinding.captureMotorPreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.captureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.editCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.captureMotorIcon.setImageBitmap(rotatedBitmap);
                oht_motor_image_string=BitMapToString(rotatedBitmap);
                oht_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("glr_capture_motor_layout")){
                connectionSubmitActivityBinding.glrCaptureMotorPreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.glrCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.glrEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.glrCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                glr_motor_image_string=BitMapToString(rotatedBitmap);
                glr_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("mini_power_pump_capture_motor_layout")){
                connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorPreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniPowerPumpGlrEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                mini_power_pump_motor_image_string=BitMapToString(rotatedBitmap);
                mini_power_pump_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("tank_capture_layout")){
                connectionSubmitActivityBinding.tankCapturePreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.tankCaptureIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.tankEditCaptureIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.tankCaptureIcon.setImageBitmap(rotatedBitmap);
                oht_tank_image_string=BitMapToString(rotatedBitmap);
                oht_tank_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
            else if(image_flag.equals("mini_tank_capture_layout")){
                connectionSubmitActivityBinding.miniTankCapturePreviewIcon.setVisibility(View.GONE);
                connectionSubmitActivityBinding.miniTankCaptureIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.miniTankEditCaptureIcon.setVisibility(View.VISIBLE);
                connectionSubmitActivityBinding.lastEbCardIcon.setImageBitmap(rotatedBitmap);
                mini_oht_tank_image_string=BitMapToString(rotatedBitmap);
                mini_oht_tank_image_byteArray=BitmaptoByteArray(rotatedBitmap);
            }
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return  rotatedBitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            /*case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());
                    // Capture Image
                    if(image_flag.equals("capture_eb_meter_layout")){
                        connectionSubmitActivityBinding.previewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.ebMeterIcon.setImageURI(i);
                    }
                    else if(image_flag.equals("capture_eb_bill_layout")) {
                        connectionSubmitActivityBinding.previewIcon1.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.lastEbBillIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.lastEbBillIcon.setImageURI(i);
                    }
                    else if(image_flag.equals("capture_eb_card_layout")){
                        connectionSubmitActivityBinding.previewIcon2.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.lastEbCardIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.lastEbCardIcon.setImageURI(i);
                    }

                    File fdelete = new File(i.getPath());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + i.getPath());
                        } else {
                            System.out.println("file not Deleted :" + i.getPath());
                        }
                    }

                    //Start Crop Image
//                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), AddInspectionReportScreen.this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, ConnectionSubmitActivity.this);
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), ConnectionSubmitActivity.this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());
                    connectionSubmitActivityBinding.previewIcon.setVisibility(View.GONE);
                    connectionSubmitActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.ebMeterIcon.setImageURI(i);
                    File fdelete = new File(i.getPath());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + i.getPath());
                        } else {
                            System.out.println("file not Deleted :" + i.getPath());
                        }
                    }
                }
                break;
*/
            case  1213:
                if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
                    String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                    Bitmap rotatedBitmap = BitmapFactory.decodeFile(filePath);
                    rotatedBitmap = getResizedBitmap(rotatedBitmap,600);
                    if(image_flag.equals("capture_eb_meter_layout")){
                        connectionSubmitActivityBinding.previewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.editEbMeterIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.ebMeterIcon.setImageBitmap(rotatedBitmap);
                        eb_meter_image_string=BitMapToString(rotatedBitmap);
                        eb_meter_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("capture_eb_bill_layout")) {
                        connectionSubmitActivityBinding.previewIcon1.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.lastEbBillIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.editLastEbBillIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.lastEbBillIcon.setImageBitmap(rotatedBitmap);
                        last_eb_bill_image_string=BitMapToString(rotatedBitmap);
                        last_eb_bill_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("capture_eb_card_layout")){
                        connectionSubmitActivityBinding.previewIcon2.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.lastEbCardIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.editLastEbCardIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.lastEbCardIcon.setImageBitmap(rotatedBitmap);
                        eb_card_image_string=BitMapToString(rotatedBitmap);
                        eb_card_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("mini_capture_motor_layout")){
                        connectionSubmitActivityBinding.miniCaptureMotorPreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.miniCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                        mini_oht_motor_image_string=BitMapToString(rotatedBitmap);
                        mini_oht_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("mini_with_out_oht_capture_motor_layout")){
                        connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorPreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniWithOutOhtEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                        mini_with_out_oht_motor_image_string=BitMapToString(rotatedBitmap);
                        mini_with_out_oht_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("capture_motor_layout")){
                        connectionSubmitActivityBinding.captureMotorPreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.captureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.editCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.captureMotorIcon.setImageBitmap(rotatedBitmap);
                        oht_motor_image_string=BitMapToString(rotatedBitmap);
                        oht_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("glr_capture_motor_layout")){
                        connectionSubmitActivityBinding.glrCaptureMotorPreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.glrCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.glrEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.glrCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                        glr_motor_image_string=BitMapToString(rotatedBitmap);
                        glr_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("mini_power_pump_capture_motor_layout")){
                        connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorPreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniPowerPumpGlrEditCaptureMotorIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorIcon.setImageBitmap(rotatedBitmap);
                        mini_power_pump_motor_image_string=BitMapToString(rotatedBitmap);
                        mini_power_pump_motor_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("tank_capture_layout")){
                        connectionSubmitActivityBinding.tankCapturePreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.tankCaptureIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.tankEditCaptureIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.tankCaptureIcon.setImageBitmap(rotatedBitmap);
                        oht_tank_image_string=BitMapToString(rotatedBitmap);
                        oht_tank_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("mini_tank_capture_layout")){
                        connectionSubmitActivityBinding.miniTankCapturePreviewIcon.setVisibility(View.GONE);
                        connectionSubmitActivityBinding.miniTankCaptureIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.miniTankEditCaptureIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.lastEbCardIcon.setImageBitmap(rotatedBitmap);
                        mini_oht_tank_image_string=BitMapToString(rotatedBitmap);
                        mini_oht_tank_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }

                }

                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT > N) {
                        Bitmap i = (Bitmap) data.getExtras().get("data");
                        imageStoragePath=getRealPathFromURI(getImageUri(getApplicationContext(),i));
                        previewCapturedImage(i);

                      /*  if(image_flag.equals("capture_eb_meter_layout")){
                            connectionSubmitActivityBinding.previewIcon.setVisibility(View.GONE);
                            connectionSubmitActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.editEbMeterIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.ebMeterIcon.setImageBitmap(i);
                            eb_meter_image_string=BitMapToString(i);
                        }
                        else if(image_flag.equals("capture_eb_bill_layout")) {
                            connectionSubmitActivityBinding.previewIcon1.setVisibility(View.GONE);
                            connectionSubmitActivityBinding.lastEbBillIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.editLastEbBillIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.lastEbBillIcon.setImageBitmap(i);
                            last_eb_bill_image_string=BitMapToString(i);
                        }
                        else if(image_flag.equals("capture_eb_card_layout")){
                            connectionSubmitActivityBinding.previewIcon2.setVisibility(View.GONE);
                            connectionSubmitActivityBinding.lastEbCardIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.editLastEbCardIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.lastEbCardIcon.setImageBitmap(i);
                            eb_card_image_string=BitMapToString(i);
                        }*/
                    }else {
                        // Refreshing the gallery
                        CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                        // successfully captured the image
                        // display it in image view
                        Bitmap i=null;
                        previewCapturedImage(i);
                        //
                    }
                }
                else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.user_canceled_image_capture), Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.sorry_failed_to_capture_image), Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case OHT_LAND_MARK_SPEECH_REQUEST_CODE:

                    if (resultCode == RESULT_OK && data != null) {
                        ArrayList<String> result = data.getStringArrayListExtra(
                                RecognizerIntent.EXTRA_RESULTS);
                        connectionSubmitActivityBinding.landMarkEt.setText(
                                Objects.requireNonNull(result).get(0));
                        connectionSubmitActivityBinding.ohtCloseIcon.setVisibility(View.VISIBLE);
                        connectionSubmitActivityBinding.ohtSpeechIcon.setVisibility(View.GONE);
                    }

                break;

            case MINI_OHT_LAND_MARK_SPEECH_REQUEST_CODE:

                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    connectionSubmitActivityBinding.miniLandMarkEt.setText(
                            Objects.requireNonNull(result).get(0));
                    connectionSubmitActivityBinding.miniOhtCloseIcon.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.miniOhtSpeechIcon.setVisibility(View.GONE);
                }

                break;
            case GLR_LAND_MARK_SPEECH_REQUEST_CODE:

                            if (resultCode == RESULT_OK && data != null) {
                                ArrayList<String> result = data.getStringArrayListExtra(
                                        RecognizerIntent.EXTRA_RESULTS);
                                connectionSubmitActivityBinding.glrLandMarkEt.setText(
                                        Objects.requireNonNull(result).get(0));
                                connectionSubmitActivityBinding.glrCloseIcon.setVisibility(View.VISIBLE);
                                connectionSubmitActivityBinding.glrOhtSpeechIcon.setVisibility(View.GONE);
                            }

                            break;

            case MINI_POWER_PUMP_LAND_MARK_SPEECH_REQUEST_CODE:

                            if (resultCode == RESULT_OK && data != null) {
                                ArrayList<String> result = data.getStringArrayListExtra(
                                        RecognizerIntent.EXTRA_RESULTS);
                                connectionSubmitActivityBinding.miniPowerPumpGlrLandMarkEt.setText(
                                        Objects.requireNonNull(result).get(0));
                                connectionSubmitActivityBinding.miniPowerPumpCloseIcon.setVisibility(View.VISIBLE);
                                connectionSubmitActivityBinding.miniPowerPumpGlrOhtSpeechIcon.setVisibility(View.GONE);
                            }

                            break;
            case MINI_WITH_OUT_OHT_LAND_MARK_SPEECH_REQUEST_CODE:

                                        if (resultCode == RESULT_OK && data != null) {
                                            ArrayList<String> result = data.getStringArrayListExtra(
                                                    RecognizerIntent.EXTRA_RESULTS);
                                            connectionSubmitActivityBinding.miniWithOutOhtLandMarkEt.setText(
                                                    Objects.requireNonNull(result).get(0));
                                            connectionSubmitActivityBinding.miniWithOutOhtCloseIcon.setVisibility(View.VISIBLE);
                                            connectionSubmitActivityBinding.miniWithOutOhtSpeechIcon.setVisibility(View.GONE);
                                        }

                                        break;

            default:
                break;
        }
    }



    private void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(ConnectionSubmitActivity.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT > N) {
                if (ActivityCompat.checkSelfPermission(ConnectionSubmitActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ConnectionSubmitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(ConnectionSubmitActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ConnectionSubmitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ConnectionSubmitActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                Log.d("Accuracy",""+MyLocationListener.accuracy);
                if (Build.VERSION.SDK_INT > N) {
                    if (CameraUtils.checkPermissions(ConnectionSubmitActivity.this)) {
                        captureImage1();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            } else {
                Utils.showAlert(ConnectionSubmitActivity.this, getResources().getString(R.string.satelite_communication_not_available));
            }
        } else {
            Utils.showAlert(ConnectionSubmitActivity.this, getResources().getString(R.string.gps_is_not_turned_on));
        }
    }





    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage1();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.permissions_required))
                .setMessage(getResources().getString(R.string.camera_needs_few_permissions))
                .setPositiveButton(getResources().getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(ConnectionSubmitActivity.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void getPerMissionCapture(){
        if (Build.VERSION.SDK_INT > N) {
            if (CameraUtils.checkPermissions(ConnectionSubmitActivity.this)) {
                captureImage1();

            } else {
                requestCameraPermission(MEDIA_TYPE_IMAGE);
            }
//                            checkPermissionForCamera();
        } else {
            requestCameraPermission(MEDIA_TYPE_IMAGE);

        }

    }

    private void captureImage1() {
       /* if (Build.VERSION.SDK_INT > N) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }*/
       /* if (MyLocationListener.latitude > 0) {
            if(image_flag.equals("capture_eb_meter_layout")){
                offlatTextValue = MyLocationListener.latitude;
                offlongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("mini_capture_motor_layout")){
                miniohtofflatTextValue = MyLocationListener.latitude;
                miniohtofflongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("capture_motor_layout")){
                ohtofflatTextValue = MyLocationListener.latitude;
                ohtofflongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("tank_capture_layout")){
                tankofflatTextValue = MyLocationListener.latitude;
               tankofflongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("mini_tank_capture_layout")){
                minitankofflatTextValue = MyLocationListener.latitude;
                minitankofflongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("glr_capture_motor_layout")){
                glrMotorofflatTextValue = MyLocationListener.latitude;
                glrMotorofflongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("mini_power_pump_capture_motor_layout")){
                minipowerPumpofflatTextValue = MyLocationListener.latitude;
                minipowerPumpglrMotorofflongTextValue = MyLocationListener.longitude;
            }
            if(image_flag.equals("mini_with_out_oht_capture_motor_layout")){
                miniwithOutOhtofflatTextValue = MyLocationListener.latitude;
                miniwithOutOhtMotorofflongTextValue = MyLocationListener.longitude;
            }

        }*/

        if(wayLatitude!=0.0 && wayLongitude!=0.0) {
            if (image_flag.equals("capture_eb_meter_layout")) {
                offlatTextValue = wayLatitude;
                offlongTextValue = wayLongitude;
            }
            if (image_flag.equals("mini_capture_motor_layout")) {
                miniohtofflatTextValue = wayLatitude;
                miniohtofflongTextValue = wayLongitude;
            }
            if (image_flag.equals("capture_motor_layout")) {
                ohtofflatTextValue = wayLatitude;
                ohtofflongTextValue = wayLongitude;
            }
            if (image_flag.equals("tank_capture_layout")) {
                tankofflatTextValue = wayLatitude;
                tankofflongTextValue = wayLongitude;
            }
            if (image_flag.equals("mini_tank_capture_layout")) {
                minitankofflatTextValue = wayLatitude;
                minitankofflongTextValue = wayLongitude;
            }
            if (image_flag.equals("glr_capture_motor_layout")) {
                glrMotorofflatTextValue = wayLatitude;
                glrMotorofflongTextValue = wayLongitude;
            }
            if (image_flag.equals("mini_power_pump_capture_motor_layout")) {
                minipowerPumpofflatTextValue = wayLatitude;
                minipowerPumpglrMotorofflongTextValue = wayLongitude;
            }
            if (image_flag.equals("mini_with_out_oht_capture_motor_layout")) {
                miniwithOutOhtofflatTextValue = wayLongitude;
                miniwithOutOhtMotorofflongTextValue = wayLongitude;
            }
            launchCamera();
        }
        else {
            getExactLocation();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.capture_eb_meter_layout:
                image_flag="capture_eb_meter_layout";
                 if(connectionSubmitActivityBinding.ebMeterIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }

                break;
                case R.id.edit_eb_meter_icon:
                image_flag="capture_eb_meter_layout";
                    getExactLocation();
                break;

                case R.id.mini_with_out_oht_capture_motor_layout:
                image_flag="mini_with_out_oht_capture_motor_layout";
                 if(connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.getDrawable() == null){
                     getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.miniWithOutOhtCaptureMotorIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }

                break;
                case R.id.mini_with_out_oht_edit_capture_motor_icon:
                image_flag="mini_with_out_oht_capture_motor_layout";
                    getExactLocation();
                break;

            case R.id.capture_eb_bill_layout:
                image_flag="capture_eb_bill_layout";
                if(connectionSubmitActivityBinding.lastEbBillIcon.getDrawable() == null){
                    getPerMissionCapture();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.lastEbBillIcon.getDrawable());
                    /*connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.lastEbBillIcon.getDrawable());
               */ }
                break;
                case R.id.edit_last_eb_bill_icon:
                image_flag="capture_eb_bill_layout";
                    getPerMissionCapture();
                break;
            case R.id.capture_eb_card_layout:
                image_flag="capture_eb_card_layout";
                if(connectionSubmitActivityBinding.lastEbCardIcon.getDrawable() == null){
                    getPerMissionCapture();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.lastEbCardIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.lastEbCardIcon.getDrawable());
*/
                }
                break;
                case R.id.edit_last_eb_card_icon:
                image_flag="capture_eb_card_layout";
                    getPerMissionCapture();
                break;

            case R.id.close_icon:
                connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.GONE);
                break;

            case R.id.home_icon:
                gotoHomePage();
                break;

            case R.id.btnsubmit:
                if(!status_code.equals("")){

                    if(!status_code.equals("1")){
                        saveDETAILS();
                    }
                    else {
                        if(eb_meter_available_status_clicked_status.equals("Y")) {
                            if (!eb_meter_image_string.equals("")) {

                                if (!last_eb_bill_image_string.equals("")) {

//                                if(!eb_card_image_string.equals("")){

                                    if (connectionSubmitActivityBinding.habitationSpinner.getSelectedItemPosition() != 0) {

                                        if (!connectionSubmitActivityBinding.selectedTv.getText().toString().equals("")) {
                                            motorLayoutVisibleCondition();

                                        } else {
                                            Utils.showAlert(this, getResources().getString(R.string.please_select_purpose_type));
                                        }

                                    } else {
                                        Utils.showAlert(this, getResources().getString(R.string.please_select_habitation));
                                    }

                                /*}
                                else {
                                    Utils.showAlert(this,"Please Capture EB Card!");
                                }*/

                                } else {
                                    Utils.showAlert(this, getResources().getString(R.string.please_capture_last_eb_bill));
                                }

                            } else {
                                Utils.showAlert(this, getResources().getString(R.string.please_capture_eb_meter));
                            }
                        }
                        else {
                            Utils.showAlert(this, getResources().getString(R.string.please_choose_eb_meter_available_status));
                        }
                    }

                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.choose_connection_status));
                }
                break;

            case R.id.mini_capture_motor_layout:
                image_flag="mini_capture_motor_layout";
                if(connectionSubmitActivityBinding.miniCaptureMotorIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.miniCaptureMotorIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }
                break;
            case R.id.capture_motor_layout:
                image_flag="capture_motor_layout";
                if(connectionSubmitActivityBinding.captureMotorIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.captureMotorIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }
                break;

            case R.id.glr_capture_motor_layout:
                image_flag="glr_capture_motor_layout";
                if(connectionSubmitActivityBinding.glrCaptureMotorIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.glrCaptureMotorIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }
                break;
            case R.id.mini_power_pump_glr_capture_motor_layout:
                image_flag="mini_power_pump_capture_motor_layout";
                if(connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.miniPowerPumpGlrCaptureMotorIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }
                break;
            case R.id.tank_capture_layout:
                image_flag="tank_capture_layout";
                if(connectionSubmitActivityBinding.tankCaptureIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.tankCaptureIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }
                break;
            case R.id.mini_tank_capture_layout:
                image_flag="mini_tank_capture_layout";
                if(connectionSubmitActivityBinding.miniTankCaptureIcon.getDrawable() == null){
                    getExactLocation();
                }
                else {
                    ExpandedImage(connectionSubmitActivityBinding.miniTankCaptureIcon.getDrawable());
                   /* connectionSubmitActivityBinding.capturedImageLayout.setVisibility(View.VISIBLE);
                    connectionSubmitActivityBinding.capturedImage.setImageDrawable(connectionSubmitActivityBinding.ebMeterIcon.getDrawable());
*/
                }
                break;

            case R.id.tank_edit_capture_icon:
                image_flag="tank_capture_layout";
                getExactLocation();

                break;
            case R.id.mini_tank_edit_capture_icon:
                image_flag="mini_tank_capture_layout";

                getExactLocation();

                break;
            case R.id.edit_capture_motor_icon:
                image_flag="capture_motor_layout";
                getExactLocation();


                break;

            case R.id.glr_edit_capture_motor_icon:
                image_flag="glr_capture_motor_layout";
                getExactLocation();
                break;

            case R.id.mini_power_pump_glr_edit_capture_motor_icon:
                image_flag="miniPowerPumpGlrCaptureMotorIcon";
                getExactLocation();
                break;

            case R.id.mini_edit_capture_motor_icon:
                image_flag="mini_capture_motor_layout";
                getExactLocation();


                break;



        }
    }
    private void ExpandedImage(Drawable profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageDrawable(profile);

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
    public String BitMapToString(Bitmap bitmap){
        String temp="";
        try {
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
        }
        catch (Exception e){
        }
        return temp;
    }
    public byte[] BitmaptoByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //bmp.recycle();
        return byteArray;
    }

    //old save details
   /* public void saveDETAILS() {
        dbData.open();
        String whereClause = "";String[] whereArgs = null;
        long id1 = 0;
        String mini_oht_motor_image=mini_oht_motor_image_string;
        String mini_oht_tank_image=mini_oht_tank_image_string;
        String oht_tank_image=oht_tank_image_string;
        String oht_motor_image=oht_motor_image_string;
        Double miniohtlatTextValue=miniohtofflatTextValue;
        Double ohtlatTextValue=ohtofflatTextValue;
        Double miniohtlongTextValue=miniohtofflongTextValue;
        Double ohtlongTextValue=ohtofflongTextValue;
        Double ohttanklatValue=tankofflatTextValue;
        Double ohttanklongValue=tankofflongTextValue;
        Double miniohttanklatValue=minitankofflatTextValue;
        Double miniohttanklongValue=minitankofflongTextValue;
        String land_mark="";

        try {
            ContentValues values = new ContentValues();
            values.put(AppConstant.KEY_ID,id);
            values.put(AppConstant.KEY_CUSCODE,cuscode);
            values.put(AppConstant.KEY_CONNECTION_NO,connection_number);
            values.put(AppConstant.KEY_CONNECTION_STATUS_ID,status_code);
            values.put(AppConstant.KEY_HAB_CODE,prefManager.getHabCode());
            values.put(AppConstant.KEY_METER_IMAGE,eb_meter_image_string);
            values.put(AppConstant.KEY_LAST_BILL_IMAGE,last_eb_bill_image_string);
            values.put(AppConstant.KEY_EB_CARD_IMAGE,eb_card_image_string);
            values.put(AppConstant.KEY_METER_IMAGE_LAT,offlatTextValue);
            values.put(AppConstant.KEY_METER_IMAGE_LONG,offlongTextValue);
            values.put(AppConstant.KEY_PURPOSE_IDS,prefManager.getPurposeCodeJson().toString());
            values.put(AppConstant.KEY_MOTOR_TYPE,prefManager.getKeyMotorType());
            //values.put(AppConstant.KEY_HORSE_POWER,connectionSubmitActivityBinding.motorHpEt.getText().toString());
            values.put(AppConstant.KEY_HORSE_POWER,prefManager.getoht_motor_horse_power());
            //values.put(AppConstant.KEY_TANK_CAPACITY,connectionSubmitActivityBinding.tankCapacityEt.getText().toString());
            values.put(AppConstant.KEY_TANK_CAPACITY,prefManager.getoht_motor_tank_capacity());
            values.put(AppConstant.KEY_STREET_LIGHT_COUNT,connectionSubmitActivityBinding.noStreetLightEt.getText().toString());
            values.put(AppConstant.KEY_MINI_OHT_MOTOR_IMAGE,mini_oht_motor_image_string);
            values.put(AppConstant.KEY_MINI_OHT_TANK_IMAGE,mini_oht_tank_image_string);
            values.put(AppConstant.KEY_OHT_TANK_IMAGE,oht_tank_image_string);
            values.put(AppConstant.KEY_OHT_MOTOR_IMAGE,oht_motor_image_string);
            values.put(AppConstant.KEY_MINI_OHT_LATITUDE,miniohtofflatTextValue);
            values.put(AppConstant.KEY_OHT_LATITUDE,ohtofflatTextValue);
            values.put(AppConstant.KEY_MINI_OHT_LANGTITUDE,miniohtofflongTextValue);
            values.put(AppConstant.KEY_OHT_LANGTITUDE,ohtofflongTextValue);
            values.put(AppConstant.KEY_OHT_TANK_LATITUDE,tankofflatTextValue);
            values.put(AppConstant.KEY_OHT_TANK_LANGTITUDE,tankofflongTextValue);
            values.put(AppConstant.KEY_MINI_OHT_TANK_LATITUDE,minitankofflatTextValue);
            values.put(AppConstant.KEY_MINI_OHT_TANK_LANGTITUDE,minitankofflongTextValue);
            values.put(AppConstant.KEY_LAND_MARK,connectionSubmitActivityBinding.landMarkEt.getText().toString());
            values.put(AppConstant.KEY_MINI_LAND_MARK,connectionSubmitActivityBinding.miniLandMarkEt.getText().toString());
            values.put(AppConstant.KEY_MINI_MOTOR_TYPE,prefManager.getMiniKeyMotorType());
            //values.put(AppConstant.KEY_MINI_MOTOR_HP,connectionSubmitActivityBinding.miniMotorHpEt.getText().toString());
            values.put(AppConstant.KEY_MINI_MOTOR_HP,prefManager.getmini_oht_motor_horse_power());
            //values.put(AppConstant.KEY_MINI_MOTOR_TANK_CAPACITY,connectionSubmitActivityBinding.miniTankCapacityEt.getText().toString());
            values.put(AppConstant.KEY_MINI_MOTOR_TANK_CAPACITY,prefManager.getmini_oht_motor_tank_capacity());
            values.put(AppConstant.KEY_OHT_MOTOR_TANK_COUNT,connectionSubmitActivityBinding.tankCountEt.getText().toString());
            values.put(AppConstant.KEY_METER_AVAILABLE,eb_meter_available_status);
            values.put(AppConstant.KEY_MINI_MOTOR_TANK_COUNT,"1");

            //glr motor
            values.put(AppConstant.KEY_GLR_MOTOR_TANK_COUNT,connectionSubmitActivityBinding.glrTankCountEt.getText().toString());
            values.put(AppConstant.KEY_GLR_TANK_CAPACITY,prefManager.getglr_motor_tank_capacity());
            values.put(AppConstant.KEY_GLR_HORSE_POWER,prefManager.getglr_motor_horse_power());
            values.put(AppConstant.KEY_GLR_MOTOR_TYPE,prefManager.getKEY_GLR_MOTOR_TYPE());
            values.put(AppConstant.KEY_GLR_LAND_MARK,connectionSubmitActivityBinding.glrLandMarkEt.getText().toString());
            values.put(AppConstant.KEY_GLR_LONGTITUDE,glrMotorofflongTextValue);
            values.put(AppConstant.KEY_GLR_LATITUDE,glrMotorofflatTextValue);
            values.put(AppConstant.KEY_GLR_MOTOR_IMAGE,glr_motor_image_string);

            ///Mini Power Pump
            values.put(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT,connectionSubmitActivityBinding.miniPowerPumpGlrTankCountEt.getText().toString());
            values.put(AppConstant.KEY_MINI_POWER_PUMP_TANK_CAPACITY,prefManager.getmini_power_pump_tank_capacity());
            values.put(AppConstant.KEY_MINI_POWER_PUMP_HORSE_POWER,prefManager.getmini_power_pump_horse_power());
            values.put(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TYPE,prefManager.getKEY_MINI_POWER_PUMP_MOTOR_TYPE());
            values.put(AppConstant.KEY_MINI_POWER_PUMP_LAND_MARK,connectionSubmitActivityBinding.miniPowerPumpGlrLandMarkEt.getText().toString());
            values.put(AppConstant.KEY_MINI_POWER_PUMP_LONGTITUDE,minipowerPumpglrMotorofflongTextValue);
            values.put(AppConstant.KEY_MINI_POWER_PUMP_LATITUDE,minipowerPumpofflatTextValue);
            values.put(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_IMAGE,mini_power_pump_motor_image_string);

            ///Mini With Out Oht Motor
            values.put(AppConstant.KEY_MINI_WITH_OUT_OHT_HORSE_POWER,prefManager.getmini_with_out__oht_horse_power());
            values.put(AppConstant.KEY_mini_with_out_oht_MOTOR_TYPE,prefManager.getKEY_MINI_WITH_OUT_OHT_MOTOR_TYPE());
            values.put(AppConstant.KEY_mini_with_out_oht_LAND_MARK,connectionSubmitActivityBinding.miniWithOutOhtLandMarkEt.getText().toString());
            values.put(AppConstant.KEY_mini_with_out_oht_LONGTITUDE,miniwithOutOhtMotorofflongTextValue);
            values.put(AppConstant.KEY_mini_with_out_oht_LATITUDE,miniwithOutOhtofflatTextValue);
            values.put(AppConstant.KEY_mini_with_out_oht_MOTOR_IMAGE,mini_with_out_oht_motor_image_string);

            ArrayList<TNEBSystem> workCount = dbData.getConnectionDetailsNew(id);

                if(workCount.size() < 1) {
                    id1=db.insert(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW, null, values);
                    if(id1 > 0){
                        Toast.makeText(this, getResources().getString(R.string.values_inserted_successfully), Toast.LENGTH_SHORT).show();
                        back_press_flag="yes";
                        onBackPressed();
                    }

                }
                else {
                    whereClause = "connection_number = ? ";
                    whereArgs = new String[]{connection_number};
                    id1 = db.update(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW, values, whereClause, whereArgs);
                    if(id1 > 0){
                        Toast.makeText(this, getResources().getString(R.string.values_updated_successfully), Toast.LENGTH_SHORT).show();
                        back_press_flag="yes";
                        onBackPressed();
                    }

                }

                Log.d("insIdDetails", String.valueOf(id));






        } catch (Exception e) {
            Utils.showAlert(this,getResources().getString(R.string.failed));
        }
    }*/

   public void saveDETAILS() {
       dbData.open();
       String whereClause = "";String[] whereArgs = null;
       long id1 = 0;
       String mini_oht_motor_image=mini_oht_motor_image_string;
       String mini_oht_tank_image=mini_oht_tank_image_string;
       String oht_tank_image=oht_tank_image_string;
       String oht_motor_image=oht_motor_image_string;
       Double miniohtlatTextValue=miniohtofflatTextValue;
       Double ohtlatTextValue=ohtofflatTextValue;
       Double miniohtlongTextValue=miniohtofflongTextValue;
       Double ohtlongTextValue=ohtofflongTextValue;
       Double ohttanklatValue=tankofflatTextValue;
       Double ohttanklongValue=tankofflongTextValue;
       Double miniohttanklatValue=minitankofflatTextValue;
       Double miniohttanklongValue=minitankofflongTextValue;
       String land_mark="";

       try {
           ContentValues values = new ContentValues();
           ContentValues imageValues = new ContentValues();
           values.put(AppConstant.KEY_ID,id);
           imageValues.put(AppConstant.KEY_ID,id);
           values.put(AppConstant.KEY_CUSCODE,cuscode);
           imageValues.put(AppConstant.KEY_CUSCODE,cuscode);
           values.put(AppConstant.KEY_CONNECTION_NO,connection_number);
           imageValues.put(AppConstant.KEY_CONNECTION_NO,connection_number);
           values.put(AppConstant.KEY_CONNECTION_STATUS_ID,status_code);
           imageValues.put(AppConstant.KEY_CONNECTION_STATUS_ID,status_code);
           values.put(AppConstant.KEY_HAB_CODE,prefManager.getHabCode());
           imageValues.put(AppConstant.KEY_HAB_CODE,prefManager.getHabCode());
           imageValues.put(AppConstant.KEY_METER_IMAGE,eb_meter_image_byteArray);
           imageValues.put(AppConstant.KEY_LAST_BILL_IMAGE,last_eb_bill_image_byteArray);
           imageValues.put(AppConstant.KEY_EB_CARD_IMAGE,eb_card_image_byteArray);
           values.put(AppConstant.KEY_METER_IMAGE_LAT,offlatTextValue);
           values.put(AppConstant.KEY_METER_IMAGE_LONG,offlongTextValue);
           values.put(AppConstant.KEY_PURPOSE_IDS,prefManager.getPurposeCodeJson().toString());
           values.put(AppConstant.KEY_MOTOR_TYPE,prefManager.getKeyMotorType());
           //values.put(AppConstant.KEY_HORSE_POWER,connectionSubmitActivityBinding.motorHpEt.getText().toString());
           values.put(AppConstant.KEY_HORSE_POWER,prefManager.getoht_motor_horse_power());
           //values.put(AppConstant.KEY_TANK_CAPACITY,connectionSubmitActivityBinding.tankCapacityEt.getText().toString());
           values.put(AppConstant.KEY_TANK_CAPACITY,prefManager.getoht_motor_tank_capacity());
           values.put(AppConstant.KEY_STREET_LIGHT_COUNT,connectionSubmitActivityBinding.noStreetLightEt.getText().toString());
           imageValues.put(AppConstant.KEY_MINI_OHT_MOTOR_IMAGE,mini_oht_motor_image_byteArray);
           imageValues.put(AppConstant.KEY_MINI_OHT_TANK_IMAGE,mini_oht_tank_image_byteArray);
           imageValues.put(AppConstant.KEY_OHT_TANK_IMAGE,oht_tank_image_byteArray);
           imageValues.put(AppConstant.KEY_OHT_MOTOR_IMAGE,oht_motor_image_byteArray);
           values.put(AppConstant.KEY_MINI_OHT_LATITUDE,miniohtofflatTextValue);
           values.put(AppConstant.KEY_OHT_LATITUDE,ohtofflatTextValue);
           values.put(AppConstant.KEY_MINI_OHT_LANGTITUDE,miniohtofflongTextValue);
           values.put(AppConstant.KEY_OHT_LANGTITUDE,ohtofflongTextValue);
           values.put(AppConstant.KEY_OHT_TANK_LATITUDE,tankofflatTextValue);
           values.put(AppConstant.KEY_OHT_TANK_LANGTITUDE,tankofflongTextValue);
           values.put(AppConstant.KEY_MINI_OHT_TANK_LATITUDE,minitankofflatTextValue);
           values.put(AppConstant.KEY_MINI_OHT_TANK_LANGTITUDE,minitankofflongTextValue);
           values.put(AppConstant.KEY_LAND_MARK,connectionSubmitActivityBinding.landMarkEt.getText().toString());
           values.put(AppConstant.KEY_MINI_LAND_MARK,connectionSubmitActivityBinding.miniLandMarkEt.getText().toString());
           values.put(AppConstant.KEY_MINI_MOTOR_TYPE,prefManager.getMiniKeyMotorType());
           //values.put(AppConstant.KEY_MINI_MOTOR_HP,connectionSubmitActivityBinding.miniMotorHpEt.getText().toString());
           values.put(AppConstant.KEY_MINI_MOTOR_HP,prefManager.getmini_oht_motor_horse_power());
           //values.put(AppConstant.KEY_MINI_MOTOR_TANK_CAPACITY,connectionSubmitActivityBinding.miniTankCapacityEt.getText().toString());
           values.put(AppConstant.KEY_MINI_MOTOR_TANK_CAPACITY,prefManager.getmini_oht_motor_tank_capacity());
           values.put(AppConstant.KEY_OHT_MOTOR_TANK_COUNT,connectionSubmitActivityBinding.tankCountEt.getText().toString());
           values.put(AppConstant.KEY_METER_AVAILABLE,eb_meter_available_status);
           values.put(AppConstant.KEY_MINI_MOTOR_TANK_COUNT,"1");

           //glr motor
           values.put(AppConstant.KEY_GLR_MOTOR_TANK_COUNT,connectionSubmitActivityBinding.glrTankCountEt.getText().toString());
           values.put(AppConstant.KEY_GLR_TANK_CAPACITY,prefManager.getglr_motor_tank_capacity());
           values.put(AppConstant.KEY_GLR_HORSE_POWER,prefManager.getglr_motor_horse_power());
           values.put(AppConstant.KEY_GLR_MOTOR_TYPE,prefManager.getKEY_GLR_MOTOR_TYPE());
           values.put(AppConstant.KEY_GLR_LAND_MARK,connectionSubmitActivityBinding.glrLandMarkEt.getText().toString());
           values.put(AppConstant.KEY_GLR_LONGTITUDE,glrMotorofflongTextValue);
           values.put(AppConstant.KEY_GLR_LATITUDE,glrMotorofflatTextValue);
           imageValues.put(AppConstant.KEY_GLR_MOTOR_IMAGE,glr_motor_image_byteArray);

           ///Mini Power Pump
           values.put(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT,connectionSubmitActivityBinding.miniPowerPumpGlrTankCountEt.getText().toString());
           values.put(AppConstant.KEY_MINI_POWER_PUMP_TANK_CAPACITY,prefManager.getmini_power_pump_tank_capacity());
           values.put(AppConstant.KEY_MINI_POWER_PUMP_HORSE_POWER,prefManager.getmini_power_pump_horse_power());
           values.put(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TYPE,prefManager.getKEY_MINI_POWER_PUMP_MOTOR_TYPE());
           values.put(AppConstant.KEY_MINI_POWER_PUMP_LAND_MARK,connectionSubmitActivityBinding.miniPowerPumpGlrLandMarkEt.getText().toString());
           values.put(AppConstant.KEY_MINI_POWER_PUMP_LONGTITUDE,minipowerPumpglrMotorofflongTextValue);
           values.put(AppConstant.KEY_MINI_POWER_PUMP_LATITUDE,minipowerPumpofflatTextValue);
           imageValues.put(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_IMAGE,mini_power_pump_motor_image_byteArray);

           ///Mini With Out Oht Motor
           values.put(AppConstant.KEY_MINI_WITH_OUT_OHT_HORSE_POWER,prefManager.getmini_with_out__oht_horse_power());
           values.put(AppConstant.KEY_mini_with_out_oht_MOTOR_TYPE,prefManager.getKEY_MINI_WITH_OUT_OHT_MOTOR_TYPE());
           values.put(AppConstant.KEY_mini_with_out_oht_LAND_MARK,connectionSubmitActivityBinding.miniWithOutOhtLandMarkEt.getText().toString());
           values.put(AppConstant.KEY_mini_with_out_oht_LONGTITUDE,miniwithOutOhtMotorofflongTextValue);
           values.put(AppConstant.KEY_mini_with_out_oht_LATITUDE,miniwithOutOhtofflatTextValue);
           imageValues.put(AppConstant.KEY_mini_with_out_oht_MOTOR_IMAGE,mini_with_out_oht_motor_image_byteArray);

           ArrayList<TNEBSystem> workCount = dbData.getConnectionDetailsNew(id);
           ArrayList<TNEBSystem> imageCount = dbData.getConnectionDetailsImages(id);

           if(imageCount.size() < 1) {
               id1=db.insert(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES, null, imageValues);
               if(id1 > 0){
                   Toast.makeText(this, getResources().getString(R.string.images_inserted_successfully), Toast.LENGTH_SHORT).show();
               }

           }
           else {
               whereClause = "connection_number = ? ";
               whereArgs = new String[]{connection_number};
               id1 = db.update(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES, imageValues, whereClause, whereArgs);
               if(id1 > 0){
                   Toast.makeText(this, getResources().getString(R.string.images_updated_successfully), Toast.LENGTH_SHORT).show();
               }

           }


           if(workCount.size() < 1) {
               id1=db.insert(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW, null, values);
               if(id1 > 0){
                   Toast.makeText(this, getResources().getString(R.string.values_inserted_successfully), Toast.LENGTH_SHORT).show();
                   back_press_flag="yes";
                   onBackPressed();
               }

           }
           else {
               whereClause = "connection_number = ? ";
               whereArgs = new String[]{connection_number};
               id1 = db.update(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW, values, whereClause, whereArgs);
               if(id1 > 0){
                   Toast.makeText(this, getResources().getString(R.string.values_updated_successfully), Toast.LENGTH_SHORT).show();
                   back_press_flag="yes";
                   onBackPressed();
               }

           }

           Log.d("insIdDetails", String.valueOf(id));






       } catch (Exception e) {
           Utils.showAlert(this,getResources().getString(R.string.failed));
       }
   }

   public JSONObject uploadDataSever(){
       JSONObject latLongData =new JSONObject();
       JSONObject dataSetTrack =null;
       JSONArray itemArray = new JSONArray();
       JSONArray saveLatLongArray = new JSONArray();
       JSONArray purposeArray = new JSONArray();
       itemArray = prefManager.getPurposeCodeJson();
        {
            try {
                latLongData.put("id", id);
                latLongData.put("connection_number", connection_number);
                latLongData.put("cuscode", cuscode);
                latLongData.put("connection_status_id", status_code);
                if (status_code.equals("1")) {
                    latLongData.put("connection_habcode", prefManager.getHabCode());
                    latLongData.put("meter_image", eb_meter_image_string);
                    latLongData.put("last_bill_image", last_eb_bill_image_string);
                    latLongData.put("eb_card_image", eb_card_image_string);
                    latLongData.put("meter_image_lat", offlatTextValue);
                    latLongData.put("meter_image_long", offlongTextValue);
                    latLongData.put("meter_available", eb_meter_available_status);
//                                latLongData.put("purpose_ids", list.get(i).getKEY_PURPOSE_IDS());

                    try {
                        // jsonString is a string variable that holds the JSON

                        for (int j = 0; j < itemArray.length(); j++) {

                            JSONObject itemA = new JSONObject();
                            int value = itemArray.getInt(j);
                            itemA.put("purpose_id", value);

                            if (value == 34) {
                                itemA.put("no_of_street_lights", connectionSubmitActivityBinding.noStreetLightEt.getText().toString());
                            }
                            else if (value == 20) {
                                itemA.put("motor_type", prefManager.getKeyMotorType());
                                itemA.put("tank_capacity", prefManager.getoht_motor_tank_capacity());
                                itemA.put("motor_hp", prefManager.getoht_motor_horse_power());
                                itemA.put("motor_image",oht_motor_image_string);
                                itemA.put("motor_lat", ohtofflatTextValue);
                                itemA.put("motor_long", ohtofflongTextValue);
                                itemA.put("land_mark", connectionSubmitActivityBinding.landMarkEt.getText().toString());
                                itemA.put("no_of_tanks", connectionSubmitActivityBinding.tankCountEt.getText().toString());
                            }
                            else if (value == 9) {
                                itemA.put("motor_type", prefManager.getKEY_GLR_MOTOR_TYPE());
                                itemA.put("tank_capacity", prefManager.getglr_motor_tank_capacity());
                                itemA.put("motor_hp", prefManager.getglr_motor_horse_power());
                                itemA.put("motor_image", glr_motor_image_string);
                                itemA.put("motor_lat", glrMotorofflatTextValue);
                                itemA.put("motor_long", glrMotorofflongTextValue);
                                itemA.put("land_mark", connectionSubmitActivityBinding.glrLandMarkEt.getText().toString());
                                itemA.put("no_of_tanks", connectionSubmitActivityBinding.glrTankCountEt.getText().toString());
                            }
                            else if (value == 46) {
                                itemA.put("motor_type", prefManager.getKEY_MINI_WITH_OUT_OHT_MOTOR_TYPE());
                                itemA.put("motor_hp", prefManager.getmini_with_out__oht_horse_power());
                                itemA.put("motor_image", mini_with_out_oht_motor_image_string);
                                itemA.put("motor_lat", miniwithOutOhtofflatTextValue);
                                itemA.put("motor_long", miniwithOutOhtMotorofflongTextValue);
                                itemA.put("land_mark", connectionSubmitActivityBinding.miniWithOutOhtLandMarkEt.getText().toString());

                            }
                            else if (value == 45) {
                                itemA.put("motor_type", prefManager.getKEY_MINI_POWER_PUMP_MOTOR_TYPE());
                                itemA.put("tank_capacity", prefManager.getmini_power_pump_tank_capacity());
                                itemA.put("motor_hp", prefManager.getmini_power_pump_horse_power());
                                itemA.put("motor_image", mini_power_pump_motor_image_string);
                                itemA.put("motor_lat",minipowerPumpofflatTextValue);
                                itemA.put("motor_long", minipowerPumpglrMotorofflongTextValue);
                                itemA.put("land_mark", connectionSubmitActivityBinding.miniPowerPumpGlrLandMarkEt.getText().toString());
                                itemA.put("no_of_tanks", connectionSubmitActivityBinding.miniPowerPumpGlrTankCountEt.getText().toString());
                            }
                            else if (value == 15) {
                                itemA.put("motor_type", prefManager.getMiniKeyMotorType());
                                itemA.put("tank_capacity", prefManager.getmini_oht_motor_tank_capacity());
                                itemA.put("motor_hp", prefManager.getmini_oht_motor_horse_power());
                                itemA.put("motor_image", mini_oht_motor_image_string);
                                itemA.put("motor_lat", miniohtofflatTextValue);
                                itemA.put("motor_long", miniohtofflongTextValue);
                                itemA.put("land_mark", connectionSubmitActivityBinding.miniLandMarkEt.getText().toString());
                                itemA.put("no_of_tanks", "1");

                            }
                            //Log.e("json", i + "=" + value);
                            purposeArray.put(itemA);


                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                latLongData.put("purpose_details", purposeArray);
                saveLatLongArray.put(latLongData);

                dataSetTrack = new JSONObject();
                try {
                    dataSetTrack.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_TN_EB_CONNECTION_STATUS_UPDATE);
                    dataSetTrack.put(AppConstant.KEY_TRACK_DATA, saveLatLongArray);
                    Log.d("trackData",dataSetTrack.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            catch (JSONException e){

            }

       }
       return dataSetTrack;
   }
    public void save_and_delete_alert(JSONObject saveDetailsDataSet,String connection_number1,String save_delete){
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if(save_delete.equals("save")) {
                text.setText(getResources().getString(R.string.do_u_want_to_upload));
            }
            else if(save_delete.equals("delete")){
                text.setText(getResources().getString(R.string.do_u_want_to_delete));
            }

            Button yesButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button noButton = (Button) dialog.findViewById(R.id.btn_cancel);
            noButton.setVisibility(View.VISIBLE);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(save_delete.equals("save")) {
                        syncTrackData(saveDetailsDataSet, connection_number1);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void syncTrackData(JSONObject saveDetailsDataSet, String connection_number1) {
        connection_number=connection_number1;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveDetailsDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveTrackDataList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveTrackDataList", "" + saveDetailsDataSet);
        Log.d("request", "" + dataSet);
    }



    public void motorLayoutVisibleCondition(){
        if(connectionSubmitActivityBinding.ohtMotorLayout.getVisibility()==View.VISIBLE){
            if (connectionSubmitActivityBinding.motorTypeSpinner.getSelectedItemPosition()>0){
            if (connectionSubmitActivityBinding.ohtMotorHorserPowerSpinner.getSelectedItemPosition()>0){
                //if(!connectionSubmitActivityBinding.motorHpEt.getText().toString().equals("")){
                if(connectionSubmitActivityBinding.ohtMotorTankCapacitySpinner.getSelectedItemPosition()>0){
                    //if(!connectionSubmitActivityBinding.tankCapacityEt.getText().toString().equals("")){
                        if(!oht_motor_image_string.equals("")){
//                            if(!oht_tank_image_string.equals("")){
                                if(!connectionSubmitActivityBinding.landMarkEt.getText().toString().equals("")){
                                    if(!connectionSubmitActivityBinding.tankCountEt.getText().toString().equals("")){
                                        glrmotorLayoutVisibleCondition();
                                    }
                                    else {
                                        Utils.showAlert(this,getResources().getString(R.string.please_enter_oht_connected_tank_counts));
                                    }

                                }
                                else {
                                    Utils.showAlert(this,getResources().getString(R.string.please_enter_land_mark));
                                }

                           /* }
                            else {
                                Utils.showAlert(this,"Please Capture  OHT Tank");
                            }
*/
                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.please_capture_oht_motor));
                        }
                    }
                    else {
                        Utils.showAlert(this,getResources().getString(R.string.please_enter_tank_capacity));
                    }
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.please_enter_horse_power));
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.please_select_motor_type));
            }
        }
        else {
            glrmotorLayoutVisibleCondition();
        }

    }
    public void glrmotorLayoutVisibleCondition(){
        if(connectionSubmitActivityBinding.glrMotorLayout.getVisibility()==View.VISIBLE){
            if (connectionSubmitActivityBinding.glrMotorTypeSpinner.getSelectedItemPosition()>0){
                if (connectionSubmitActivityBinding.glrOhtMotorHorserPowerSpinner.getSelectedItemPosition()>0){
                    //if(!connectionSubmitActivityBinding.motorHpEt.getText().toString().equals("")){
                    if(connectionSubmitActivityBinding.glrOhtMotorTankCapacitySpinner.getSelectedItemPosition()>0){
                        //if(!connectionSubmitActivityBinding.tankCapacityEt.getText().toString().equals("")){
                        if(!glr_motor_image_string.equals("")){
//                            if(!oht_tank_image_string.equals("")){
                            if(!connectionSubmitActivityBinding.glrLandMarkEt.getText().toString().equals("")){
                                if(!connectionSubmitActivityBinding.glrTankCountEt.getText().toString().equals("")){
                                    miniPowerPumpmotorLayoutVisibleCondition();
                                }
                                else {
                                    Utils.showAlert(this,getResources().getString(R.string.please_enter_glr_motor_connected_tank_counts));
                                }

                            }
                            else {
                                Utils.showAlert(this,getResources().getString(R.string.please_enter_land_mark));
                            }

                           /* }
                            else {
                                Utils.showAlert(this,"Please Capture  OHT Tank");
                            }
*/
                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.please_capture_glr_motor));
                        }
                    }
                    else {
                        Utils.showAlert(this,getResources().getString(R.string.please_enter_tank_capacity));
                    }
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.please_enter_horse_power));
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.please_select_motor_type));
            }
        }
        else {
            miniPowerPumpmotorLayoutVisibleCondition();
        }

    }
    public void miniPowerPumpmotorLayoutVisibleCondition(){
        if(connectionSubmitActivityBinding.miniPowerPumpMotorLayout.getVisibility()==View.VISIBLE){
            if (connectionSubmitActivityBinding.miniPowerPumpGlrMotorTypeSpinner.getSelectedItemPosition()>0){
                if (connectionSubmitActivityBinding.miniPowerPumpGlrOhtMotorHorserPowerSpinner.getSelectedItemPosition()>0){
                    //if(!connectionSubmitActivityBinding.motorHpEt.getText().toString().equals("")){
                    if(connectionSubmitActivityBinding.miniPowerPumpGlrOhtMotorTankCapacitySpinner.getSelectedItemPosition()>0){
                        //if(!connectionSubmitActivityBinding.tankCapacityEt.getText().toString().equals("")){
                        if(!mini_power_pump_motor_image_string.equals("")){
//                            if(!oht_tank_image_string.equals("")){
                            if(!connectionSubmitActivityBinding.miniPowerPumpGlrLandMarkEt.getText().toString().equals("")){
                                if(!connectionSubmitActivityBinding.miniPowerPumpGlrTankCountEt.getText().toString().equals("")){
                                    minimotorLayoutVisibleCondition();
                                }
                                else {
                                    Utils.showAlert(this,getResources().getString(R.string.please_enter_mini_power_pump_connected_tank_counts));
                                }

                            }
                            else {
                                Utils.showAlert(this,getResources().getString(R.string.please_enter_land_mark));
                            }

                           /* }
                            else {
                                Utils.showAlert(this,"Please Capture  OHT Tank");
                            }
*/
                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.please_capture_mini_power_pump_motor));
                        }
                    }
                    else {
                        Utils.showAlert(this,getResources().getString(R.string.please_enter_tank_capacity));
                    }
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.please_enter_horse_power));
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.please_select_motor_type));
            }
        }
        else {
            minimotorLayoutVisibleCondition();
        }

    }
    public void minimotorLayoutVisibleCondition(){
        if(connectionSubmitActivityBinding.miniMotorLayout.getVisibility()==View.VISIBLE){
            if (connectionSubmitActivityBinding.miniMotorTypeSpinner.getSelectedItemPosition()>0){
                if (connectionSubmitActivityBinding.miniOhtMotorHorserPowerSpinner.getSelectedItemPosition()>0){
                    //if(!connectionSubmitActivityBinding.miniMotorHpEt.getText().toString().equals("")){
                    if(connectionSubmitActivityBinding.miniOhtMotorTankCapacitySpinner.getSelectedItemPosition()>0){
                        //if(!connectionSubmitActivityBinding.miniTankCapacityEt.getText().toString().equals("")){
                        if(!mini_oht_motor_image_string.equals("")){
//                            if(!mini_oht_tank_image_string.equals("")){
                            if(!connectionSubmitActivityBinding.miniLandMarkEt.getText().toString().equals("")){
                                miniwithOutOhtmotorLayoutVisibleCondition();
                            }else {
                                Utils.showAlert(this,getResources().getString(R.string.please_enter_land_mark));
                            }

                          /*  }
                            else {
                                Utils.showAlert(this,"Please Capture Mini OHT Tank");
                            }*/

                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.please_capture_mini_oht_motor));
                        }


                    }
                    else {
                        Utils.showAlert(this,getResources().getString(R.string.please_enter_tank_capacity));
                    }
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.please_enter_horse_power));
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.please_select_motor_type));
            }
        }
        else {
            miniwithOutOhtmotorLayoutVisibleCondition();
        }

    }
    public void miniwithOutOhtmotorLayoutVisibleCondition(){
        if(connectionSubmitActivityBinding.newOht.getVisibility()==View.VISIBLE){
            if (connectionSubmitActivityBinding.miniWithOutOhtMotorTypeSpinner.getSelectedItemPosition()>0){
                if (connectionSubmitActivityBinding.miniWithOutOhtMotorHorserPowerSpinner.getSelectedItemPosition()>0){
                        if(!mini_with_out_oht_motor_image_string.equals("")){
                            if(!connectionSubmitActivityBinding.miniWithOutOhtLandMarkEt.getText().toString().equals("")){
                                streetLightCondition();
                            }else {
                                Utils.showAlert(this,getResources().getString(R.string.please_enter_land_mark));
                            }


                        }
                        else {
                            Utils.showAlert(this,getResources().getString(R.string.please_capture_mini_oht_motor));
                        }



                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.please_enter_horse_power));
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.please_select_motor_type));
            }
        }
        else {
            streetLightCondition();
        }

    }
    public void streetLightCondition(){
        if(connectionSubmitActivityBinding.noStreetLightLayout.getVisibility()==View.VISIBLE){
            if(!connectionSubmitActivityBinding.noStreetLightEt.getText().toString().equals("")){
                saveDETAILS();
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.enter_no_of_street_lights));
            }

        }
        else {
            saveDETAILS();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= N) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }



    public void withinDisteance(){
        float[] results = new float[1];
        Location.distanceBetween(offlatTextValue, offlongTextValue, ohtofflatTextValue, ohtofflongTextValue, results);
        float distanceInMeters = results[0];
        boolean isWithin10km = distanceInMeters < 10000;
        Log.d("distance", String.valueOf(distanceInMeters));
    }
    // the following two function checks if the current location is in geo-fence area
    // function to find distance between two latitude and longitude
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(Math.abs(lat2 - lat1));
        double lonDistance = Math.toRadians(Math.abs(lon2 - lon1));

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // distance in meter

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    private boolean isInCampus(double lat,double longtitude,double x,double y){

        // the lat and long of : Webel-IT Park
        final double previous_lat = lat;
        final double previous_long = longtitude;

        // radiusToCheck is defined as 200
        // radius up to 200 m is checked
        if(getDistance(previous_lat,previous_long,x,y) <= 15.0){
            Log.d("SuccessMeter",""+getDistance(previous_lat,previous_long,x,y));
            Utils.showAlert(this,"This location is inside of circle!");
            return true;
        }
        else{
            Log.d("failedMeter",""+getDistance(previous_lat,previous_long,x,y));
            Utils.showAlert(this,"This location is out of circle!");
            return false;
        }
    }

    /*public void geofenve(){
        GeofenceModel mestalla = new GeofenceModel.Builder("id_mestalla")
                .setTransition(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setLatitude(13.070834)
                .setLongitude(80.194651)
                .setRadius(10)
                .build();

        SmartLocation.with(this).geofencing()
                .add(mestalla)
                .remove("already_existing_geofence_id")
                .start(new OnGeofencingTransitionListener() {
                    @Override
                    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {
                        transitionGeofence.getTransitionType();
                        transitionGeofence.getGeofenceModel().getRadius();
                        Utils.showAlert(ConnectionSubmitActivity.this,"This location is inside of circle!");
                    }
                });

    }*/

    public void speechToText(String request_id){
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "ta-IND");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {
            if(request_id.equals("OHT")){
                startActivityForResult(intent, OHT_LAND_MARK_SPEECH_REQUEST_CODE);
            }
            else if(request_id.equals("MINI_OHT")) {
                startActivityForResult(intent, MINI_OHT_LAND_MARK_SPEECH_REQUEST_CODE);
            }
            else if(request_id.equals("GLR")) {
                startActivityForResult(intent, GLR_LAND_MARK_SPEECH_REQUEST_CODE);
            }
            else if(request_id.equals("MINI_POWER_PUMP")) {
                startActivityForResult(intent, MINI_POWER_PUMP_LAND_MARK_SPEECH_REQUEST_CODE);
            }
            else if(request_id.equals("MINI_WITH_OUT_OHT")) {
                startActivityForResult(intent, MINI_WITH_OUT_OHT_LAND_MARK_SPEECH_REQUEST_CODE);
            }

        }
        catch (Exception e) {
            Toast
                    .makeText(ConnectionSubmitActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void getExactLocation() {

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(ConnectionSubmitActivity.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // check permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, CAMERA},
                        1000);
                // reuqest for permission

            }
            else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                //locationRequest.setInterval(0);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        Log.d("LocationAccuracy", "" + location.getAccuracy());
                        Log.d("Locations", "" + wayLatitude + "," + wayLongitude);
                        if (CameraUtils.checkPermissions(ConnectionSubmitActivity.this)) {
                            captureImage1();
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    } else {
                        Utils.showAlert(ConnectionSubmitActivity.this, getResources().getString(R.string.satelite_communication_not_available));

                    }
                });
            }
        }
        else {
            Utils.showAlert(ConnectionSubmitActivity.this, getResources().getString(R.string.gps_is_not_turned_on));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    //locationRequest.setInterval(0);

                    mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    mlocListener = new MyLocationListener();


                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(ConnectionSubmitActivity.this,
                            ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                    }

                    if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                Log.d("LocationAccuracy", "" + location.getAccuracy());
                                Log.d("Locations", "" + wayLatitude + "," + wayLongitude);
                                if (CameraUtils.checkPermissions(ConnectionSubmitActivity.this)) {
                                    captureImage1();
                                } else {
                                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                                }
                            } else {
                                Utils.showAlert(ConnectionSubmitActivity.this, getResources().getString(R.string.satelite_communication_not_available));
                            }
                        });
                    }
                    else {
                        Utils.showAlert(ConnectionSubmitActivity.this, getResources().getString(R.string.gps_is_not_turned_on));
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }


                break;
            }
        }
    }
    private void launchCamera() {

        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CROP, false);//default is false
        startActivityForResult(intent, 1213);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
