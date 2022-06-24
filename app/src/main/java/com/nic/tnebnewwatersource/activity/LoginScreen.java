package com.nic.tnebnewwatersource.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.databinding.LoginScreenBinding;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.ProgressHUD;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by M.Kavitha on 22/10/2021.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private String randString;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;

    private PrefManager prefManager;
    private ProgressHUD progressHUD;
    private int setPType;

    public LoginScreenBinding loginScreenBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // this will remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.login_screen);
        loginScreenBinding.setActivity(this);
        intializeUI();


    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        loginScreenBinding.btnBuy.setOnClickListener(this);

        loginScreenBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
       /* loginScreenBinding.inputLayoutEmail.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
        loginScreenBinding.inputLayoutPassword.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
        loginScreenBinding.btnSignIn.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));
        loginScreenBinding.inputLayoutEmail.setHintTextAppearance(R.style.InActive);
        loginScreenBinding.inputLayoutPassword.setHintTextAppearance(R.style.InActive);*/

        loginScreenBinding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });
        loginScreenBinding.password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Avenir-Roman.ttf"));
        randString = Utils.randomChar();


        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            loginScreenBinding.tvVersionNumber.setText(getApplicationContext().getResources().getString(R.string.version) + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setPType = 1;
//        loginScreenBinding.redEye.setOnClickListener(this);
    }

    public void showPassword() {
        if (setPType == 1) {
            setPType = 0;
            loginScreenBinding.password.setTransformationMethod(null);
            if (loginScreenBinding.password.getText().length() > 0) {
                loginScreenBinding.password.setSelection(loginScreenBinding.password.getText().length());
//                loginScreenBinding.redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24px);
            }
        } else {
            setPType = 1;
            loginScreenBinding.password.setTransformationMethod(new PasswordTransformationMethod());
            if (loginScreenBinding.password.getText().length() > 0) {
                loginScreenBinding.password.setSelection(loginScreenBinding.password.getText().length());
//                loginScreenBinding.redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_24px);
            }
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = loginScreenBinding.username.getText().toString().trim();
        prefManager.setUserName(username);
        String password = loginScreenBinding.password.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, getResources().getString(R.string.please_enter_the_user_name));
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, getResources().getString(R.string.please_enter_the_password));
        }
        return valid;
    }

    public void checkLoginScreen() {
        loginScreenBinding.username.setText("aritmnrvp25u2"); //loc
        loginScreenBinding.password.setText("test123#$");
        /*loginScreenBinding.username.setText("aritmnrvp25u2"); //prod
        loginScreenBinding.password.setText("rdas566#$");*/
        /*loginScreenBinding.username.setText("mdumduevp34u2"); //prod
        loginScreenBinding.password.setText("rdas673#$");*/
        /*loginScreenBinding.username.setText("tvrtrvrvp3u2"); //prod
        loginScreenBinding.password.setText("rdas600#$");*/
        /*loginScreenBinding.username.setText("nmknpetvp10u3"); //prod
        loginScreenBinding.password.setText("rdas481#$");*/
        final String username = loginScreenBinding.username.getText().toString().trim();
        final String password = loginScreenBinding.password.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getLoginUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, getResources().getString(R.string.please_enter_the_user_name_and_password));
            }
        } else {
            //Utils.showAlert(this, getResources().getString(R.string.no_internet));
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    LoginScreen.this);
            ab.setMessage(getResources().getString(R.string.please_turn_on_network_or_continue_offline));
            ab.setPositiveButton(getResources().getString(R.string.settings),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            ab.setNegativeButton(getResources().getString(R.string.continue_with_off_line),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            offline_mode(username, password);
                        }
                    });
            ab.show();
        }
    }


    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");


        String random = Utils.randomChar();

        params.put(AppConstant.USER_LOGIN_KEY, random);
        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

        String encryptUserPass = Utils.md5(loginScreenBinding.password.getText().toString().trim());
        prefManager.setEncryptPass(encryptUserPass);
        Log.d("md5", "" + encryptUserPass);

        String userPass = encryptUserPass.concat(random);
        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(userPass);
        Log.d("sha", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);


        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

Log.d("params",""+params);
        return params;
    }

    //The method for opening the registration page and another processes or checks for registering




    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = loginResponse.getString(AppConstant.KEY_STATUS);
            String response = loginResponse.getString(AppConstant.KEY_RESPONSE);

            if ("LoginScreen".equals(urlType)) {
                if (status.equalsIgnoreCase("OK")) {
                    if (response.equals("LOGIN_SUCCESS")) {
                        String key = loginResponse.getString(AppConstant.KEY_USER);
                        String user_data = loginResponse.getString(AppConstant.USER_DATA);
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);
                        JSONArray districtCodeJsonArray = new JSONArray();
                        districtCodeJsonArray.put(jsonObject.get(AppConstant.DISTRICT_CODE));
                        prefManager.setDistrictCodeJson(districtCodeJsonArray);
                        prefManager.setDistrictCode(jsonObject.get(AppConstant.DISTRICT_CODE));
                        prefManager.setBlockCode(jsonObject.get(AppConstant.BLOCK_CODE));
                        prefManager.setPvCode(jsonObject.get(AppConstant.PV_CODE));
                        prefManager.setPvName(jsonObject.get(AppConstant.PV_NAME));
                        prefManager.setDistrictName(jsonObject.get(AppConstant.DISTRICT_NAME));
                        prefManager.setBlockName(jsonObject.get(AppConstant.BLOCK_NAME));
                        prefManager.setkey_levels(jsonObject.getString("levels"));
                        prefManager.setkey_levels(jsonObject.getString("levels"));
                        prefManager.setDesignation(jsonObject.get(AppConstant.DESIG_NAME));
                        prefManager.setName(String.valueOf(jsonObject.get(AppConstant.NAME)));
                        prefManager.setPV_NAME_TA(String.valueOf(jsonObject.get(AppConstant.PV_NAME_TA)));
                        prefManager.setDESIG_NAME_TA(String.valueOf(jsonObject.get(AppConstant.DESIG_NAME_TA)));
                        prefManager.setDISTRICT_NAME_TA(String.valueOf(jsonObject.get(AppConstant.DISTRICT_NAME_TA)));
                        prefManager.setBLOCK_NAME_TA(String.valueOf(jsonObject.get(AppConstant.BLOCK_NAME_TA)));
                        prefManager.setProfile_image_found(String.valueOf(jsonObject.get("profile_image_found")));
                        prefManager.setProfile_image(String.valueOf(jsonObject.get("profile_image")));

                        Log.d("userdata", "" + prefManager.getDistrictCode() + prefManager.getBlockCode() + prefManager.getPvCode() + prefManager.getDistrictName() + prefManager.getBlockName()+prefManager.getName());
                        prefManager.setUserPassKey(decryptedKey);
                        showHomeScreen();
                    } else {
                        if (response.equals("LOGIN_FAILED")) {
                            Utils.showAlert(this, getResources().getString(R.string.invalid_user_name_or_password));
                        }
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, getResources().getString(R.string.log_in_again));
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showHomeScreen();
//    }

    private void showHomeScreen() {
        prefManager.setLocalLanguage("ta");
        Intent intent = new Intent(LoginScreen.this, HomePage.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void offline_mode(String name, String pass) {
        String userName = prefManager.getUserName();
        String password = prefManager.getUserPassword();
        if (name.equals(userName) && pass.equals(password)) {
            showHomeScreen();
        } else {
            Utils.showAlert(this, getResources().getString(R.string.no_data_available_for_offline));
        }
    }

}
