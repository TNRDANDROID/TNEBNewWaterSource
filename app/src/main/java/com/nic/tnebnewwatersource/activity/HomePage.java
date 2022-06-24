package com.nic.tnebnewwatersource.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.tnebnewwatersource.ImageZoom.ImageMatrixTouchHandler;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.HomeScreenBinding;
import com.nic.tnebnewwatersource.dialog.MyDialog;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.ProgressHUD;
import com.nic.tnebnewwatersource.utils.CameraUtils;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.os.Build.VERSION_CODES.M;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class HomePage extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener,
        MyDialog.myOnClickListener, PopupMenu.OnMenuItemClickListener {
    private HomeScreenBinding homeScreenBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private String isHome;
    JSONObject dataset = new JSONObject();
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private ProgressHUD progressHUD;
    String localLanguage ="";
    String UserProfile ="";
    Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeScreenBinding = DataBindingUtil.setContentView(this, R.layout.home_screen);
        homeScreenBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        localLanguage =prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);
        homeScreenBinding.pendingLayout.setVisibility(View.GONE);
        homeScreenBinding.countTextLayout.setVisibility(View.GONE);
        homeScreenBinding.img5.setVisibility(View.GONE);


        homeScreenBinding.editTextField.setText(getResources().getString(R.string.edit_photo));
        homeScreenBinding.chooseLanguage.setText(getResources().getString(R.string.choose_language));
        homeScreenBinding.viewWorksTv.setText(getResources().getString(R.string.go));
        if(localLanguage.equals("ta")){
            homeScreenBinding.tvName.setText(getResources().getString(R.string.name)+" - "+prefManager.getName());
            homeScreenBinding.designation.setText(getResources().getString(R.string.designation)+" - "+prefManager.getDESIG_NAME_TA());
            homeScreenBinding.districtName.setText(getResources().getString(R.string.district)+" - "+prefManager.getDISTRICT_NAME_TA());
            homeScreenBinding.blockName.setText(getResources().getString(R.string.block)+" - "+prefManager.getBLOCK_NAME_TA());
            homeScreenBinding.villageName.setText(getResources().getString(R.string.village)+" - "+prefManager.getPV_NAME_TA());

        }
        else {
            homeScreenBinding.tvName.setText(getResources().getString(R.string.name)+" - "+prefManager.getName());
            homeScreenBinding.designation.setText(getResources().getString(R.string.designation)+" - "+prefManager.getDesignation());
            homeScreenBinding.districtName.setText(getResources().getString(R.string.district)+" - "+prefManager.getDistrictName());
            homeScreenBinding.blockName.setText(getResources().getString(R.string.block)+" - "+prefManager.getBlockName());
            homeScreenBinding.villageName.setText(getResources().getString(R.string.village)+" - "+prefManager.getPvName());

        }
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isHome = bundle.getString("Home","");
        }
        if (Utils.isOnline() && !isHome.equalsIgnoreCase("Home")) {
            fetchAllResponseFromApi();
        }
        homeScreenBinding.chooseLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomePage.this, v);
                popup.setOnMenuItemClickListener(HomePage.this);
                popup.inflate(R.menu.language_menu);
                popup.show();
            }
        });
        syncButtonVisibility();

        homeScreenBinding.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandedImage(homeScreenBinding.userImg.getDrawable());
            }
        });

       /* if(prefManager.getProfile_image_found().equals("Y")){
            homeScreenBinding.userImg.setImageBitmap(StringToBitMap(prefManager.getProfile_image()));
        }
        else {
            homeScreenBinding.userImg.setImageResource(R.drawable.user_color);
        }*/
    }
    private void ExpandedImage(Drawable profile) {
        try {
            String bitmap="";
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);

            zoomImage.setImageDrawable(profile);

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(this);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
            dialogBuilder.setView(ImagePopupLayout);

            final androidx.appcompat.app.AlertDialog alert = dialogBuilder.create();
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
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Language: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.english:
                // do your code
                prefManager.setLocalLanguage("en");
                 localLanguage ="en";
                //Utils.setLocale(localLanguage,this);
                finish();
                startActivity(getIntent());
                return true;
            case R.id.tamil:
                // do your code
                prefManager.setLocalLanguage("ta");
                 localLanguage = "ta";
                //Utils.setLocale(localLanguage,this);
                finish();
                startActivity(getIntent());
                return true;
            default:
                return false;
        }}


    public void fetchAllResponseFromApi() {
        getProfileImage();
        getVillageList();
        getHabList();
        getMotorTypeList();
        getConnectionStatusList();
        getConnectionPurposeList();
        getConnectionList();
        getNoOfBankDetailsCollectedCount();
        get_village_bank_accounts_list();
        get_Tn_eb_Motor_Hp_List();
        get_Tn_eb_Tank_Capacity_List();
        get_Key_drinking_water_source_type();
        get_no_of_water_source_photos();
        get_drinking_water_supply_timing();
        get_drinking_water_session();
        get_drinking_water_type();
        get_server_drinking_water_details();
        get_water_supply_reason();
        get_min_max_date();
        get_daily_drinking_water_supply_status_v2_view();
//        getDistrictList();
//        getBlockList();

    }

    public void syncButtonVisibility() {
        dbData.open();
        ArrayList<TNEBSystem> workImageCount = dbData.getSavedWorkDetailsNew();
        ArrayList<TNEBSystem> BankDetailsCount = dbData.getAllBankDetails("");
        int count=workImageCount.size()+BankDetailsCount.size();

        if (workImageCount.size() > 0|| BankDetailsCount.size()>0) {
            homeScreenBinding.pendingLayout.setVisibility(View.VISIBLE);
            homeScreenBinding.countTextLayout.setVisibility(View.VISIBLE);
            homeScreenBinding.img5.setVisibility(View.VISIBLE);
            homeScreenBinding.syncLayout.setVisibility(View.VISIBLE);
            homeScreenBinding.pendingCount.setText(""+count);
        }else {
            homeScreenBinding.pendingLayout.setVisibility(View.GONE);
            homeScreenBinding.countTextLayout.setVisibility(View.GONE);
            homeScreenBinding.img5.setVisibility(View.GONE);
            homeScreenBinding.syncLayout.setVisibility(View.GONE);
            homeScreenBinding.pendingCount.setText("0");
        }
    }

    public void openPendingScreen() {
        dbData.open();
        ArrayList<TNEBSystem> workImageCount = dbData.getSavedWorkDetailsNew();
        ArrayList<TNEBSystem> BankDetailsCount = dbData.getAllBankDetails("");
        int count=workImageCount.size()+BankDetailsCount.size();


        if (count > 0) {
            Intent intent = new Intent(this, PendingScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else {

        }

    }
    public void getPerMissionCapture(){
        if (Build.VERSION.SDK_INT >= M) {
            if (CameraUtils.checkPermissions(HomePage.this)) {
                selectImage();

            } else {
                requestCameraPermission(MEDIA_TYPE_IMAGE);
            }
//                            checkPermissionForCamera();
        } else {
            selectImage();

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
                                selectImage();
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
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.permissions_required))
                .setMessage(getResources().getString(R.string.camera_needs_few_permissions))
                .setPositiveButton(getResources().getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(HomePage.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void selectImage() {
        final CharSequence[] options = { getResources().getString(R.string.take_photo),getResources().getString(R.string.choose_from_gallery),getResources().getString(R.string.cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getResources().getString(R.string.take_photo)))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    }else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        if (file != null) {
                            imageStoragePath = file.getAbsolutePath();
                        }

                        Uri fileUri = CameraUtils.getOutputMediaFileUri(HomePage.this, file);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intent, 1);
                    }
                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/
                }
                else if (options[item].equals(getResources().getString(R.string.choose_from_gallery)))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /*if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    homeScreenBinding.userImg.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } */
            if(requestCode == 1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap i = (Bitmap) data.getExtras().get("data");
                    imageStoragePath=getRealPathFromURI(getImageUri(getApplicationContext(),i));
                    previewCapturedImage(i);
                    uploadProfile();

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
            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap thumbnail1 = resizeBitmap(thumbnail);
                Log.w("path of img gallery", picturePath+"");
                UserProfile=BitMapToString(thumbnail1);

                homeScreenBinding.userImg.setImageBitmap(thumbnail1);
                uploadProfile();

            }
        }
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
    public Bitmap resizeBitmap(Bitmap bitmap) {
        Canvas canvas = new Canvas();
        Bitmap resizedBitmap = null;
        if (bitmap != null) {
            int h = bitmap.getHeight();
            int w = bitmap.getWidth();
            int newWidth = 0;
            int newHeight = 0;

            if (h > w) {
                newWidth = 600;
                newHeight = 800;
            }

            if (w > h) {
                newWidth = 800;
                newHeight = 600;
            }

            float scaleWidth = ((float) newWidth) / w;
            float scaleHeight = ((float) newHeight) / h;


            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.preScale(scaleWidth, scaleHeight);

            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);


            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);

            canvas.drawBitmap(resizedBitmap, matrix, paint);


        }


        return resizedBitmap;
    }
    public Uri getImageUri1(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public  Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,"IMG_" + Calendar.getInstance().getTime(),null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
            homeScreenBinding.userImg.setImageBitmap(rotatedBitmap);

            UserProfile=BitMapToString(rotatedBitmap);
            uploadProfile();
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return  rotatedBitmap;
    }
    public String BitMapToString(Bitmap bitmap){
        String temp="";
        try {
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            temp= Base64.encodeToString(b, Base64.DEFAULT);
        }
        catch (Exception e){
        }
        return temp;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
    private void getConnectionList() {
        try {
            new ApiService(this).makeJSONObjectRequest("ConnectionList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), connectionListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getNoOfBankDetailsCollectedCount() {
        try {
            new ApiService(this).makeJSONObjectRequest("NoOfAccountCount", Api.Method.POST, UrlGenerator.getBankServiceUrl(), getNoOfBankDetailsCount(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public JSONObject getNoOfBankDetailsCount() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.getNoOfBankDetailsCount(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("BankDetailsCount", "" + dataSet);
        return dataSet;
    }

    private void get_village_bank_accounts_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("village_bank_accounts", Api.Method.POST, UrlGenerator.getBankServiceUrl(), get_village_bank_accounts_list_json(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public JSONObject get_village_bank_accounts_list_json() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.get_village_bank_accounts_list_json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("village_bank_accounts", "" + dataSet);
        return dataSet;
    }




    public void getDistrictList() {
        try {
            new ApiService(this).makeJSONObjectRequest("DistrictList", Api.Method.POST, UrlGenerator.getServicesListUrl(), districtListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBlockList() {
        try {
            new ApiService(this).makeJSONObjectRequest("BlockList", Api.Method.POST, UrlGenerator.getServicesListUrl(), blockListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getVillageList() {
        try {
            new ApiService(this).makeJSONObjectRequest("VillageList", Api.Method.POST, UrlGenerator.getServicesListUrl(), villageListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getConnectionStatusList() {
        try {
            new ApiService(this).makeJSONObjectRequest("ConnectionStatusList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), connectionStatusListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getProfileImage() {
        try {
            new ApiService(this).makeJSONObjectRequest("ProfileImage", Api.Method.POST, UrlGenerator.getServicesListUrl(), ProfileImageJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void uploadProfile() {
        if(!UserProfile.equalsIgnoreCase("")){
            try {
                new ApiService(this).makeJSONObjectRequest("uploadProfile", Api.Method.POST, UrlGenerator.getServicesListUrl(), uploadProfileJsonParams(), "not cache", this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            Utils.showAlert(this,"Please select profile image");
        }
    }
    public void getConnectionPurposeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("ConnectionPurposeList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), connectionPurposeListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getHabList() {
        try {
            new ApiService(this).makeJSONObjectRequest("HabitationList", Api.Method.POST, UrlGenerator.getServicesListUrl(), habitationListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getMotorTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotorTypeList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), motorTypeListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void get_Tn_eb_Motor_Hp_List() {
        try {
            new ApiService(this).makeJSONObjectRequest("tneb_motor_hp", Api.Method.POST, UrlGenerator.getMainServiceUrl(), tneb_motor_hpJSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_Tn_eb_Tank_Capacity_List() {
        try {
            new ApiService(this).makeJSONObjectRequest("tneb_tank_capacity", Api.Method.POST, UrlGenerator.getMainServiceUrl(), tneb_tank_capacity_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_Key_drinking_water_source_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("drinking_water_source_type", Api.Method.POST, UrlGenerator.getMainServiceUrl(), drinking_water_source_type_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_no_of_water_source_photos() {
        try {
            new ApiService(this).makeJSONObjectRequest("no_of_water_source_photos", Api.Method.POST, UrlGenerator.getMainServiceUrl(), no_of_water_source_photos_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void get_drinking_water_supply_timing() {
        try {
            new ApiService(this).makeJSONObjectRequest("drinking_water_supply_timing", Api.Method.POST, UrlGenerator.getMainServiceUrl(), drinking_water_supply_timing_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_drinking_water_session() {
        try {
            new ApiService(this).makeJSONObjectRequest("drinking_water_session", Api.Method.POST, UrlGenerator.getMainServiceUrl(), drinking_water_session_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_drinking_water_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("drinking_water_type", Api.Method.POST, UrlGenerator.getMainServiceUrl(), drinking_water_type_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject connectionListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.connectionListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("districtList", "" + authKey);
        return dataSet;
    }
    public JSONObject districtListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.districtListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("districtList", "" + authKey);
        return dataSet;
    }

    public JSONObject blockListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.blockListDistrictWiseJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("blockListDistrictWise", "" + authKey);
        return dataSet;
    }

    public JSONObject villageListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.villageListDistrictBlockWiseJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("villageListDistrictWise", "" + dataSet);
        return dataSet;
    }
    public JSONObject connectionStatusListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.connectionStatusListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("connectionStatusJson", "" + dataSet);
        return dataSet;
    }
    public JSONObject ProfileImageJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.ProfileImageParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("ProfileImage", "" + dataSet);
        return dataSet;
    }
    public JSONObject uploadProfileJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.uploadProfileJsonParams(this,UserProfile).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("uploadProfile", "" + dataSet);
        return dataSet;
    }
    public JSONObject connectionPurposeListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.conPurposeListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("conPurposeListJson", "" + dataSet);
        return dataSet;
    }


    public JSONObject habitationListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.HabitationListDistrictBlockVillageWiseJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("HabitationList", "" + dataSet);
        return dataSet;
    }
    public JSONObject motorTypeListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.motorTypeListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("motorTypeListJsonParams", "" + dataSet);
        return dataSet;
    }
    public JSONObject tneb_motor_hpJSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.tneb_motor_hp_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("tnebmotorhpJSONParams", "" + dataSet);
        return dataSet;
    }
    public JSONObject tneb_tank_capacity_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.tneb_tank_capacity_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("tnebtankcapacityJSON", "" + dataSet);
        return dataSet;
    }
    public JSONObject drinking_water_source_type_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.drinking_water_source_type_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_source_type", "" + dataSet);
        return dataSet;
    }
    public JSONObject no_of_water_source_photos_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.no_of_water_source_photos_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_source_type", "" + dataSet);
        return dataSet;
    }

    public JSONObject drinking_water_supply_timing_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.drinking_water_supply_timing_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_supply_timing", "" + dataSet);
        return dataSet;
    }
    public JSONObject drinking_water_session_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.drinking_water_session_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_session", "" + dataSet);
        return dataSet;
    }
    public JSONObject drinking_water_type_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.drinking_water_type_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_type", "" + dataSet);
        return dataSet;
    }

    public void get_server_drinking_water_details() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_server_drinking_water_details", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_server_drinking_water_details_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_server_drinking_water_details_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), get_server_drinking_water_details_normal_json().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("server_drinking", "" + dataSet);
        return dataSet;
    }
    public static JSONObject get_server_drinking_water_details_normal_json() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "drinking_water_source_village_level_view");
        Log.d("server_drinking", "" + dataSet);
        return dataSet;
    }

    public void get_water_supply_reason() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_water_supply_reason", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_water_supply_reason_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_water_supply_reason_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.get_water_supply_reasonJsonParams(0).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_supply_reason", "" + dataSet);
        return dataSet;
    }
    public void get_daily_drinking_water_supply_status_v2_view() {
        try {
            new ApiService(this).makeJSONObjectRequest("water_supply_status_v2", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_daily_drinking_water_supply_status_v2_view_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_daily_drinking_water_supply_status_v2_view_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.daily_drinking_water_supply_status_v2_view_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_supply_status_v2", "" + dataSet);
        return dataSet;
    }

    public void get_min_max_date() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_min_max_date", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_min_max_date_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_min_max_date_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.min_max_dateJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("min_max_date", "" + dataSet);
        return dataSet;
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            /*if ("BlockList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertBlockTask().execute(jsonObject);
                }
                Log.d("BlockList", "" + responseDecryptedBlockKey);
            }
            if ("DistrictList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertDistrictTask().execute(jsonObject);
                }
                Log.d("DistrictList", "" + responseDecryptedBlockKey);
            }

            */
            if ("VillageList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertVillageTask().execute(jsonObject);
                }
                Log.d("VillageList", "" + responseDecryptedBlockKey);
            }

            if ("HabitationList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertHabTask().execute(jsonObject);
                }
                Log.d("HabitationList", "" + responseDecryptedBlockKey);
            }
            if ("MotorTypeList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertMotorTypeListTask().execute(jsonObject);
                }
                Log.d("MotorTypeList", "" + responseDecryptedBlockKey);
            }
            if ("ConnectionStatusList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("ConnectionStatusList", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertConnectionStatusTask().execute(jsonObject);
                }

            }
            if ("ConnectionPurposeList".equals(urlType) && responseObj != null) {
                 String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("ConnectionPurposeList", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertConnectionPurposeListTask().execute(jsonObject);
                }

            }
            if ("ConnectionList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertConnectionListTask().execute(jsonObject);
                }
                Log.d("ConnectionList", "" + responseDecryptedBlockKey);
            }
            if ("uploadProfile".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                   Utils.showAlert(this,"Profile Image uploaded Successfully!");
                   getProfileImage();

                }
                Log.d("ConnectionList", "" + responseDecryptedBlockKey);
            }
            if ("ProfileImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                String profile="";
                String profile_image_found="";
                Log.d("ProfileImage", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1 = jsonObject.getJSONObject("DATA");
                    profile_image_found=jsonObject1.getString("profile_image_found");
                    if(profile_image_found.equals("Y")){
                        profile=jsonObject1.getString("profile_image");
                        Bitmap bitmap=StringToBitMap(profile);
                        homeScreenBinding.userImg.setImageBitmap(bitmap);
                    }
                    else {
                        homeScreenBinding.userImg.setImageDrawable(this.getResources().getDrawable(R.drawable.user_color));
                    }







                }else {
                    homeScreenBinding.userImg.setImageDrawable(this.getResources().getDrawable(R.drawable.user_color));
                }

            }
            if ("NoOfAccountCount".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                String profile="";
                String no_of_bank_details_count="";
                Log.d("NoOfAccountCount", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //JSONObject jsonObject1=jsonObject.getJSONObject(AppConstant.JSON_DATA);
                    new InsertBankTypesListTask().execute(jsonObject);
                }
                else {

                }

            }
            if ("village_bank_accounts".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                String profile="";
                String no_of_bank_details_count="";
                Log.d("villagebankAcc", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //JSONObject jsonObject1=jsonObject.getJSONObject(AppConstant.JSON_DATA);
                    new Insert_village_bank_accounts().execute(jsonObject);
                }
                else {

                }

            }
            if ("tneb_motor_hp".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //new InsertMotorTypeListTask().execute(jsonObject);
                    new Inserttneb_motor_hp_ListTask().execute(jsonObject);
                }
                Log.d("tneb_motor_hp", "" + responseDecryptedBlockKey);
            }
            if ("tneb_tank_capacity".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //new InsertMotorTypeListTask().execute(jsonObject);
                    new Insert_tneb_tank_capacity__ListTask().execute(jsonObject);
                }
                Log.d("tneb_tank_capacity", "" + responseDecryptedBlockKey);
            }
            if ("drinking_water_source_type".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_drinking_water_source_type().execute(jsonObject);
                }
                Log.d("water_source_type", "" + responseDecryptedBlockKey);
            }
            if ("no_of_water_source_photos".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                        prefManager.setkey_no_of_photos(jsonObject.getString("no_of_photos"));
                }
                else {
                    prefManager.setkey_no_of_photos("0");
                }
                Log.d("no_of_photos", "" + responseDecryptedBlockKey);
            }

            if ("drinking_water_supply_timing".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_drinking_water_supply_timing().execute(jsonObject);
                }
                Log.d("water_supply_timing", "" + responseDecryptedBlockKey);
            }
            if ("drinking_water_session".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_drinking_water_session().execute(jsonObject);
                }
                Log.d("water_session", "" + responseDecryptedBlockKey);
            }
            if ("drinking_water_type".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_drinking_water_type().execute(jsonObject);
                }
                Log.d("water_type", "" + responseDecryptedBlockKey);
            }

            if ("get_server_drinking_water_details".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("server_drinking", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_server_drinking_water_details().execute(jsonObject);
                }


            }

            if ("get_water_supply_reason".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("get_water_supply_reason", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_water_supply_reason().execute(jsonObject);
                }

            }
            if ("water_supply_status_v2".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("water_supply_status_v2", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_new_drinking_water_details_server1().execute(jsonObject);
                }

            }

            if ("get_min_max_date".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    String minimum_date = jsonObject.getString("min_date");
                    String maximum_date = jsonObject.getString("max_date");
                    TNEBSystem tnebSystem = new TNEBSystem();
                    tnebSystem.setMinimum_date(minimum_date);
                    tnebSystem.setMaximum_date(maximum_date);
                    dbData.open();
                    dbData.delete_MINIMUM_MAXIMUM_DATE();
                    dbData.Insert_minimum_maximum_date(tnebSystem);
                }
                Log.d("get_min_max_date", "" + responseDecryptedBlockKey);
            }



        } catch (JSONException e) {
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

    /*public class InsertDistrictTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> districtlist_count = dbData.getAll_District();
            if (districtlist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem districtListValue = new TNEBSystem();
                        try {
                            districtListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            districtListValue.setDistrictName(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_NAME));

                            dbData.insertDistrict(districtListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }

    public class InsertBlockTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> blocklist_count = dbData.getAll_Block();
            if (blocklist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem blocktListValue = new TNEBSystem();
                        try {
                            blocktListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            blocktListValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            blocktListValue.setBlockName(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_NAME));

                            dbData.insertBlock(blocktListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }
*/

    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public class InsertConnectionListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.deleteConnectionTable();
            Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.CONNECTION_LIST +" where pvcode = " + prefManager.getPvCode(), null);
            if (cursor.getCount() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setId(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ID));
                            ListValue.setRegion_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_REGION_CODE));
                            ListValue.setCircle_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CIRCLE_CODE));
                            ListValue.setSection_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_SECTION_CODE));
                            ListValue.setDistribution_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_DISTRIBUTION_CODE));
                            ListValue.setConsumer_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONSUMER_CODE));
                            ListValue.setCuscode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CUSCODE));
                            ListValue.setConsumer_name(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONSUMER_NAME));
                            ListValue.setConnection_number(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONNECTION_NO));
                            ListValue.setLocation(jsonArray.getJSONObject(i).getString(AppConstant.KEY_LOCATION));
                            ListValue.setTariff(jsonArray.getJSONObject(i).getString(AppConstant.KEY_TARIFF));
                            ListValue.setTariff_desc(jsonArray.getJSONObject(i).getString(AppConstant.KEY_TARIFF_DESC));
                            ListValue.setPurpose_as_per_tneb(jsonArray.getJSONObject(i).getString(AppConstant.KEY_PURPOSE_AS_PER_TNEB));
                            ListValue.setPHOTO_SAVED_STATUS(jsonArray.getJSONObject(i).getString("data_taken"));

                            ListValue.setPvCode(prefManager.getPvCode());
                            dbData.insertConnectionList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }

    }

    public class InsertVillageTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> villagelist_count = dbData.getAll_Village("","","");
            if (villagelist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem villageListValue = new TNEBSystem();
                        try {
                            villageListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            villageListValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            villageListValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                            villageListValue.setPvName(jsonArray.getJSONObject(i).getString(AppConstant.PV_NAME));

                            dbData.insertVillage(villageListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }

    public class InsertHabTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> hablist_count = dbData.getAll_Habitation(prefManager.getDistrictCode(),prefManager.getBlockCode());
            if (hablist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem habListValue = new TNEBSystem();
                        try {
                            habListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            habListValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            habListValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                            habListValue.setHabitation_code(jsonArray.getJSONObject(i).getString(AppConstant.HABB_CODE));
                            habListValue.setHabitation_name(jsonArray.getJSONObject(i).getString(AppConstant.HABITATION_NAME));
                            habListValue.setHabitation_name_ta(jsonArray.getJSONObject(i).getString(AppConstant.HABITATION_NAME_TA));

                            dbData.insertHabitation(habListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }
    }
    public class InsertConnectionStatusTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> list_count = dbData.getAll_ConnectionStatus();
            if (list_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setConncetion_id(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONNECTION_ID));
                            ListValue.setConnection_status(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONNECTION_STATUS));
                            dbData.insertConnectionStatus(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }
    }
    public class InsertMotorTypeListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> list_count = dbData.getAll_MotorTypeList();
            if (list_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setId(jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTOR_TYPE));
                            ListValue.setMotor_type(jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTOR_TYPE_NAME));
                            dbData.insertMotorTypeList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }
    }
    public class InsertConnectionPurposeListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> list_count = dbData.getAll_ConnectionPurposeList();
            if (list_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setConncetion_id(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONNECTION_ID));
                            ListValue.setConncetion_name(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONNECTION_NAME));
                            ListValue.setConnection_name_ta(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONNECTION_NAME_TA));

                            dbData.insertConnectionPurpose(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }
    }

    public class InsertBankTypesListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.delete_tn_eb_type_of_bank_details_save_tableTable();
            Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE, null);
            if (cursor.getCount() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setAccount_id(jsonArray.getJSONObject(i).getString("account_id"));
                            ListValue.setAccount_name(jsonArray.getJSONObject(i).getString("account_name"));
                            ListValue.setAccount_id_display_order(jsonArray.getJSONObject(i).getString("account_id_display_order"));
                            ListValue.setAccount_saved_status(jsonArray.getJSONObject(i).getString("data_entered"));
                            //ListValue.setAccount_saved_status("N");
                            dbData.insertBankTypeList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }

    }
    public class Insert_village_bank_accounts extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.delete_TN_EB_BANK_SERVER_DETAILS_TABLE();
            Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.TN_EB_BANK_SERVER_DETAILS_TABLE, null);
            if (cursor.getCount() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setAccount_id(jsonArray.getJSONObject(i).getString("account_id"));
                            ListValue.setBank_name(jsonArray.getJSONObject(i).getString("bank_name"));
                            ListValue.setBranch_name(jsonArray.getJSONObject(i).getString("branchname"));
                            ListValue.setBranch_id(jsonArray.getJSONObject(i).getString("branch_id"));
                            ListValue.setBank_id(jsonArray.getJSONObject(i).getString("bank_id"));
                            ListValue.setAccount_no(jsonArray.getJSONObject(i).getString("account_no"));
                            ListValue.setIfsc_code(jsonArray.getJSONObject(i).getString("ifsc_code"));
                            ListValue.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                            ListValue.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                            ListValue.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                            ListValue.setBank_account_id(jsonArray.getJSONObject(i).getString("bank_account_id"));
                            //ListValue.setAccount_saved_status("N");
                            dbData.insertSavedBankList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }

    }

    public class Inserttneb_motor_hp_ListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> list_count = dbData.getAll_Tneb_Horse_Power_List();
            if (list_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setTneb_hp_id(jsonArray.getJSONObject(i).getString("id"));
                            ListValue.setTneb_horse_power(jsonArray.getJSONObject(i).getString("horse_power"));
                            dbData.insert_tneb_motor_hpList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }
    }

    public class Insert_tneb_tank_capacity__ListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<TNEBSystem> list_count = dbData.getAll_Tneb_Tank_Capacity_List();
            if (list_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setTneb_tank_capacity__id(jsonArray.getJSONObject(i).getString("id"));
                            ListValue.setTneb_tank_capacity_purpose_id(jsonArray.getJSONObject(i).getString("purpose_id"));
                            ListValue.setTneb_tank_capacity(jsonArray.getJSONObject(i).getString("capacity"));
                            dbData.insert_tneb_tank_capacity_List(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;


        }
    }
    public class Insert_drinking_water_source_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
                if (params.length > 0) {
                    dbData.open();
                    dbData.delete_DRINKING_WATER_SOURCE_TABLE();
                    ArrayList<TNEBSystem> list_count = dbData.getAll_drinking_water_source_type();
                    if (list_count.size() <= 0) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TNEBSystem ListValue = new TNEBSystem();
                            try {
                                ListValue.setWater_source_type_id(jsonArray.getJSONObject(i).getString("water_source_type_id"));
                                ListValue.setWater_source_type_name(jsonArray.getJSONObject(i).getString("water_source_type_name"));
                                dbData.Insert_drinking_water_source_type(ListValue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }


            return null;


        }
    }
    public class Insert_drinking_water_supply_timing extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_DRINKING_WATER_TIMING_DETAILS();
                ArrayList<TNEBSystem> list_count = dbData.getAll_drinking_water_supply_timing();
                if (list_count.size() <= 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setSupply_timing_id(jsonArray.getJSONObject(i).getString("supply_timing_id"));
                            ListValue.setSupply_timing(jsonArray.getJSONObject(i).getString("supply_timing"));
                            dbData.Insert_drinking_water_supply_timing(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }


            return null;


        }
    }
    public class Insert_drinking_water_session extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_DRINKING_WATER_SESSION();
                ArrayList<TNEBSystem> list_count = dbData.getAll_drinking_water_session();
                if (list_count.size() <= 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setSession_id(jsonArray.getJSONObject(i).getString("session_id"));
                            ListValue.setSession_name(jsonArray.getJSONObject(i).getString("session_name"));
                            dbData.Insert_drinking_water_session(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }


            return null;


        }
    }
    public class Insert_drinking_water_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_DRINKING_WATER_TYPE();
                ArrayList<TNEBSystem> list_count = dbData.getAll_drinking_water_type();
                if (list_count.size() <= 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setWater_type_id(jsonArray.getJSONObject(i).getString("water_type_id"));
                            ListValue.setWater_type(jsonArray.getJSONObject(i).getString("water_type"));
                            dbData.Insert_drinking_water_type(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }


            return null;


        }
    }
    public class Insert_water_supply_reason extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_DRINKING_WATER_SUPPLY_REASON();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    TNEBSystem ListValue = new TNEBSystem();
                    try {
                        ListValue.setId(jsonArray.getJSONObject(i).getString("id"));
                        ListValue.setReason_for_supply(jsonArray.getJSONObject(i).getString("reason_for_supply"));
                        ListValue.setReason_type(jsonArray.getJSONObject(i).getString("type"));
                        dbData.Insert_water_supply_reason(ListValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
    @SuppressLint("StaticFieldLeak")
    public class Insert_server_drinking_water_details extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_DRINKING_WATER_SOURCE_SERVER_DATA();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    TNEBSystem ListValue = new TNEBSystem();
                    try {
                        ListValue.setWater_source_details_id(jsonArray.getJSONObject(i).getString("water_source_details_id"));
                        ListValue.setWater_source_type_name(jsonArray.getJSONObject(i).getString("water_source_type_name"));
                        ListValue.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                        ListValue.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                        ListValue.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                        ListValue.setHabitation_code(jsonArray.getJSONObject(i).getString("hab_code"));
                        ListValue.setHabitation_name(jsonArray.getJSONObject(i).getString("habitation_name"));
                        ListValue.setWater_source_type_id(jsonArray.getJSONObject(i).getString("water_source_type_id"));
                        ListValue.setKEY_LAND_MARK(jsonArray.getJSONObject(i).getString("landmark"));
                        if(jsonArray.getJSONObject(i).isNull("photo_file_name_1")||jsonArray.getJSONObject(i).getString("photo_file_name_1").equals("")){
                            ListValue.setImage_1_lat("");
                            ListValue.setImage_1_long("");
                            ListValue.setImage_1("");
                        }
                        else {
                            ListValue.setImage_1_lat(jsonArray.getJSONObject(i).getString("photo_file_name_1_lat"));
                            ListValue.setImage_1_long(jsonArray.getJSONObject(i).getString("photo_file_name_1_long"));
                            ListValue.setImage_1(jsonArray.getJSONObject(i).getString("image_1"));
                        }
                        if(jsonArray.getJSONObject(i).isNull("photo_file_name_2")||jsonArray.getJSONObject(i).getString("photo_file_name_2").equals("")){
                            ListValue.setImage_2_lat("");
                            ListValue.setImage_2_long("");
                            ListValue.setImage_2("");
                        }
                        else {
                            ListValue.setImage_2_lat(jsonArray.getJSONObject(i).getString("photo_file_name_2_lat"));
                            ListValue.setImage_2_long(jsonArray.getJSONObject(i).getString("photo_file_name_2_long"));
                            ListValue.setImage_2(jsonArray.getJSONObject(i).getString("image_2"));
                        }
                        dbData.Insert_drinking_water_source_server_data(ListValue);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public class Insert_new_drinking_water_details_server1 extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_NEW_DRINKING_WATER_DETAILS_SERVER_1();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    TNEBSystem ListValue = new TNEBSystem();
                    try {
                        ListValue.setEntry_date(jsonArray.getJSONObject(i).getString("entry_dt"));
                        ListValue.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                        ListValue.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                        ListValue.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                        ListValue.setHabitation_code(jsonArray.getJSONObject(i).getString("habcode"));
                        ListValue.setWater_supply_status_id(jsonArray.getJSONObject(i).getString("water_supply_status"));
                        ListValue.setWater_supplied_reason_id(jsonArray.getJSONObject(i).getString("no_supply_reason"));
                        ListValue.setSession_fn_water_type_id(jsonArray.getJSONObject(i).getString("session_fn_water_type"));
                        ListValue.setSession_fn_timing_id(jsonArray.getJSONObject(i).getString("session_fn_timing"));
                        ListValue.setSession_fn_src_id(jsonArray.getJSONObject(i).getString("session_fn_src_id"));
                        ListValue.setSession_an_water_type_id(jsonArray.getJSONObject(i).getString("session_an_water_type"));
                        ListValue.setSession_an_timing_id(jsonArray.getJSONObject(i).getString("session_an_timing"));
                        ListValue.setSession_an_src_id(jsonArray.getJSONObject(i).getString("session_an_src_id"));

                        dbData.Insert_new_drinking_water_details_server1(ListValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void viewDashboard() {
        ArrayList<TNEBSystem> workImageCount = dbData.getSavedWorkDetailsNew();
        if(workImageCount.size()>5) {
            Utils.showAlert(HomePage.this,getResources().getString(R.string.please_uplaod_all_data));
        }
        else {
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    public void closeApplication() {
        new MyDialog(this).exitDialog(this, getResources().getString(R.string.are_you_sure_logout), "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, getResources().getString(R.string.are_you_sure_exit), "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            finishAffinity();
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }
    public void logout() {
        dbData.open();
        ArrayList<TNEBSystem> activityCount = dbData.getSavedWorkDetailsNew();
        if (!Utils.isOnline()) {
            Utils.showAlert(this, getResources().getString(R.string.logging_out_loss_data));
        } else {
            if (!(activityCount.size() > 0 )) {
                closeApplication();
            }else{
                Utils.showAlert(this,getResources().getString(R.string.sync_all_the_data_before_logout));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //syncButtonVisibility();
    }
}
