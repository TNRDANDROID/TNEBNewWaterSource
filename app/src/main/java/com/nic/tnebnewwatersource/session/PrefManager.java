package com.nic.tnebnewwatersource.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.nic.tnebnewwatersource.constant.AppConstant;

import org.json.JSONArray;


/**
 * Created by M.Kavitha on 22/10/2021.
 */
public class PrefManager {

    private static final String KEY_STREET_JSON = "street_json";
    private static final String KEY_PURPOSE_CODE_JSON = "purpose_code_json_json";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String APP_KEY = "AppKey";
    private static final String KEY_USER_AUTH_KEY = "auth_key";
    private static final String KEY_USER_PASS_KEY = "pass_key";
    private static final String KEY_ENCRYPT_PASS = "pass";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PASSWORD = "UserPassword";
    private static final String KEY_DISTRICT_CODE = "District_Code";
    private static final String KEY_PARTICULAR_DISTRICT_CODE = "Particular_District_Code";
    private static final String KEY_BLOCK_CODE = "Block_Code";
    private static final String KEY_PV_CODE = "Pv_Code";
    private static final String KEY_PV_NAME = "Pv_Name";
    private static final String KEY_DISTRICT_NAME = "District_Name";
    private static final String KEY_DESIGNATION = "Designation";
    private static final String KEY_NAME = "Name";
    private static final String KEY_BLOCK_NAME = "Block_Name";
    private static final String KEY_DISTRICT_CODE_JSON = "district_code_json";
    private static final String KEY_VILLAGE_CODE_JSON = "village_code_json";
    private static final String DELETE_ADAPTER_POSITION = "delete_adapter_position";
    private static final String KEY_HAB_CODE = "Hab_Code";
    private static final String KEY_HAB_NAME = "Hab_Name";
    private static final String KEY_HAB_NAME_TA = "Hab_Name_Ta";
    private static final String KEY_MOTOR_TYPE="motor_type";
    private static final String KEY_MINI_MOTOR_TYPE="mini_motor_type";
    private static final String KEY_GLR_MOTOR_TYPE="glr_motor_type";
    private static final String KEY_MINI_POWER_PUMP_MOTOR_TYPE="mini_power_pump_motor_type";
    private static final String KEY_MINI_WITH_OUT_OHT_MOTOR_TYPE="mini_with_out_oht_motor_type";
    private static final String KEY_HORSE_POWER ="horse_power";
    private static final String LOCAL_LANGUAGE= "localLanguage";
    private static final String profile_image_found= "profile_image_found";
    private static final String profile_image= "profile_image";
    private static final String oht_motor_horse_power= "oht_motor_horse_power";
    private static final String glr_motor_horse_power= "glr_oht_motor_horse_power";
    private static final String mini_power_pump_horse_power= "mini_power_pump_horse_power";
    private static final String mini_with_out__oht_horse_power= "mini_with_out_oht_horse_power";
    private static final String mini_oht_motor_horse_power= "mini_oht_motor_horse_power";
    private static final String mini_oht_motor_tank_capacity= "mini_oht_motor_tank_capacity";
    private static final String oht_motor_tank_capacity= "oht_motor_tank_capacity";
    private static final String glr_motor_tank_capacity= "glr_motor_tank_capacity";
    private static final String mini_power_pump_tank_capacity= "mini_power_pump_tank_capacity";
    private static final String key_no_of_photos= "no_of_photos";
    private static final String key_levels= "levels";

    /////Log in details Tamil//
    public static String BLOCK_NAME_TA = "bname_ta";
    public static String PV_NAME_TA = "pvname_ta";
    public static String NAME_TA = "name_ta";
    public static String DESIG_NAME_TA = "desig_name_ta";
    public static String DISTRICT_NAME_TA = "dname_ta";

    public void setBLOCK_NAME_TA(String userName) {
        editor.putString(BLOCK_NAME_TA, userName);
        editor.commit();
    }
    public String getBLOCK_NAME_TA() { return pref.getString(BLOCK_NAME_TA, null); }

    public void setPV_NAME_TA(String userName) {
            editor.putString(PV_NAME_TA, userName);
            editor.commit();
        }
    public String getPV_NAME_TA() { return pref.getString(PV_NAME_TA, null); }


    public String getDESIG_NAME_TA() { return pref.getString(DESIG_NAME_TA, null); }

    public void setDESIG_NAME_TA(String userName) {
        editor.putString(DESIG_NAME_TA, userName);
        editor.commit();
    }
    public void setNAME_TA(String userName) {
        editor.putString(NAME_TA, userName);
        editor.commit();
    }
    public String getNAME_TA() { return pref.getString(NAME_TA, null); }

    public void setDISTRICT_NAME_TA(String userName) {
        editor.putString(DISTRICT_NAME_TA, userName);
        editor.commit();
    }
    public String getDISTRICT_NAME_TA() { return pref.getString(DISTRICT_NAME_TA, null); }




    ////

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setAppKey(String appKey) {
        editor.putString(APP_KEY, appKey);
        editor.commit();
    }

    public String getAppKey() {
        return pref.getString(APP_KEY, null);
    }
    public void setLocalLanguage(String localLanguage) {
        editor.putString(LOCAL_LANGUAGE, localLanguage);
        editor.commit();
    }

    public String getLocalLanguage() {
        return pref.getString(LOCAL_LANGUAGE, null);
    }


    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void setUserAuthKey(String userAuthKey) {
        editor.putString(KEY_USER_AUTH_KEY, userAuthKey);
        editor.commit();
    }

    public String getUserAuthKey() {
        return pref.getString(KEY_USER_AUTH_KEY, null);
    }

    public void setUserPassKey(String userPassKey) {
        editor.putString(KEY_USER_PASS_KEY, userPassKey);
        editor.commit();
    }

    public String getUserPassKey() {
        return pref.getString(KEY_USER_PASS_KEY, null);
    }


    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() { return pref.getString(KEY_USER_NAME, null); }
    public void setkey_no_of_photos(String no_of_photos) {
        editor.putString(key_no_of_photos, no_of_photos);
        editor.commit();
    }

    public String getkey_no_of_photos() { return pref.getString(key_no_of_photos, null); }
    public void setkey_levels(String level) {
        editor.putString(key_levels, level);
        editor.commit();
    }

    public String getkey_levels() { return pref.getString(key_levels, null); }

    public void setUserPassword(String userPassword) {
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String getUserPassword() { return pref.getString(KEY_USER_PASSWORD, null); }


    public void setEncryptPass(String pass) {
        editor.putString(KEY_ENCRYPT_PASS, pass);
        editor.commit();
    }

    public String getEncryptPass() {
        return pref.getString(KEY_ENCRYPT_PASS, null);
    }

    public Object setDistrictCode(Object key) {
        editor.putString(KEY_DISTRICT_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictCode() {
        return pref.getString(KEY_DISTRICT_CODE, null);
    }


    public Object setBlockCode(Object key) {
        editor.putString(KEY_BLOCK_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockCode() {
        return pref.getString(KEY_BLOCK_CODE, null);
    }



    public Object setPvCode(Object key) {
        editor.putString(KEY_PV_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvCode() {
        return pref.getString(KEY_PV_CODE, null);

    } public Object setPvName(Object key) {
        editor.putString(KEY_PV_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvName() {
        return pref.getString(KEY_PV_NAME, null);
    }

    public Object setDistrictName(Object key) {
        editor.putString(KEY_DISTRICT_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictName() {
        return pref.getString(KEY_DISTRICT_NAME, null);
    }

    public Object setBlockName(Object key) {
        editor.putString(KEY_BLOCK_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockName() {
        return pref.getString(KEY_BLOCK_NAME, null);
    }


    public void setDeleteAdapterPosition(Integer LocationId) {
        editor.putInt(DELETE_ADAPTER_POSITION,LocationId);
        editor.commit();
    }

    public Integer getDeleteAdapterPosition() {
        return pref.getInt(DELETE_ADAPTER_POSITION,0);
    }


    public Object setDesignation(Object key) {
        editor.putString(KEY_DESIGNATION, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDesignation() {
        return pref.getString(KEY_DESIGNATION, null);
    }



    public void setName(String userName) {
        editor.putString(KEY_NAME, userName);
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, null);
    }
    public void setProfile_image(String userName) {
        editor.putString(profile_image, userName);
        editor.commit();
    }

    public String getProfile_image() {
        return pref.getString(profile_image, null);
    }
    public void setProfile_image_found(String userName) {
        editor.putString(profile_image_found, userName);
        editor.commit();
    }

    public String getProfile_image_found() {
        return pref.getString(profile_image_found, null);
    }


    public void clearSharedPreferences(Context context) {
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor.clear();
        editor.apply();
    }


    public void setDistrictCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_DISTRICT_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getDistrictCodeJsonList() {
        return pref.getString(KEY_DISTRICT_CODE_JSON, null);
    }

    public JSONArray getDistrictCodeJson() {
        JSONArray jsonData = null;
        String strJson = getDistrictCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefBlockJson",""+jsonData);
        return jsonData;
    }

    public void setVillagePvCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_VILLAGE_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getVillagePvCodeJsonList() {
        return pref.getString(KEY_VILLAGE_CODE_JSON, null);
    }

    public void setKeyHorsePower(String horsePower) {
        editor.putString(KEY_HORSE_POWER, horsePower);
        editor.commit();
    }

    public String getKeyHorsePower() {
        return pref.getString(KEY_HORSE_POWER, null);
    }

    public void setKeyMotorType(String motorType) {
        editor.putString(KEY_MOTOR_TYPE, motorType);
        editor.commit();
    }

    public String getKeyMotorType() {
        return pref.getString(KEY_MOTOR_TYPE, null);
    }

    public void setKEY_GLR_MOTOR_TYPE(String motorType) {
        editor.putString(KEY_GLR_MOTOR_TYPE, motorType);
        editor.commit();
    }
     public String getKEY_MINI_WITH_OUT_OHT_MOTOR_TYPE() {
            return pref.getString(KEY_MINI_WITH_OUT_OHT_MOTOR_TYPE, null);
        }

        public void setKEY_MINI_WITH_OUT_OHT_MOTOR_TYPE(String motorType) {
            editor.putString(KEY_MINI_WITH_OUT_OHT_MOTOR_TYPE, motorType);
            editor.commit();
        }
    public String getKEY_GLR_MOTOR_TYPE() {
        return pref.getString(KEY_GLR_MOTOR_TYPE, null);
    }

    public void setKEY_MINI_POWER_PUMP_MOTOR_TYPE(String motorType) {
            editor.putString(KEY_MINI_POWER_PUMP_MOTOR_TYPE, motorType);
            editor.commit();
        }

    public String getKEY_MINI_POWER_PUMP_MOTOR_TYPE() {
            return pref.getString(KEY_MINI_POWER_PUMP_MOTOR_TYPE, null);
        }

    public void setMiniKeyMotorType(String motorType) {
        editor.putString(KEY_MINI_MOTOR_TYPE, motorType);
        editor.commit();
    }

    public String getMiniKeyMotorType() {
        return pref.getString(KEY_MINI_MOTOR_TYPE, null);
    }


    public Object setHabCode(Object key) {
        editor.putString(KEY_HAB_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getHabCode() {
        return pref.getString(KEY_HAB_CODE, null);
    }
    public Object setHabName(Object key) {
        editor.putString(KEY_HAB_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getHabName() {
        return pref.getString(KEY_HAB_NAME, null);
    }
    public Object setHabNameTa(Object key) {
        editor.putString(KEY_HAB_NAME_TA, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getHabNameta() {
        return pref.getString(KEY_HAB_NAME_TA, null);
    }



    public JSONArray getVillagePvCodeJson() {
        JSONArray jsonData = null;
        String strJson = getVillagePvCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefVillageJson",""+jsonData);
        return jsonData;
    }

    public void setFinYearJson(JSONArray jsonarray) {
        editor.putString(KEY_STREET_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getFinYearJsonList() {
        return pref.getString(KEY_STREET_JSON, null);
    }

    public JSONArray getFinYearJson() {
        JSONArray jsonData = null;
        String strJson = getFinYearJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefJson",""+jsonData);
        return jsonData;
    }

    public void setPurposeCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_PURPOSE_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getPurposeCodJsonList() {
        return pref.getString(KEY_PURPOSE_CODE_JSON, null);
    }

    public JSONArray getPurposeCodeJson() {
        JSONArray jsonData = null;
        String strJson = getPurposeCodJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefBlockJson",""+jsonData);
        return jsonData;
    }

    public void setoht_motor_horse_power(String motorType) {
        editor.putString(oht_motor_horse_power, motorType);
        editor.commit();
    }

    public String getoht_motor_horse_power() {
        return pref.getString(oht_motor_horse_power, null);
    }

    public void setglr_motor_horse_power(String motorType) {
        editor.putString(glr_motor_horse_power, motorType);
        editor.commit();
    }

    public String getglr_motor_horse_power() {
        return pref.getString(glr_motor_horse_power, null);
    }
    public void setmini_power_pump_horse_power(String motorType) {
        editor.putString(mini_power_pump_horse_power, motorType);
        editor.commit();
    }

    public String getmini_power_pump_horse_power() {
        return pref.getString(mini_power_pump_horse_power, null);
    }
    public void setmini_with_out__oht_horse_power(String motorType) {
        editor.putString(mini_with_out__oht_horse_power, motorType);
        editor.commit();
    }

    public String getmini_with_out__oht_horse_power() {
        return pref.getString(mini_with_out__oht_horse_power, null);
    }

    public void setmini_oht_motor_horse_power(String motorType) {
        editor.putString(mini_oht_motor_horse_power, motorType);
        editor.commit();
    }

    public String getmini_oht_motor_horse_power() {
        return pref.getString(mini_oht_motor_horse_power, null);
    }

    public void setmini_oht_motor_tank_capacity(String motorType) {
        editor.putString(mini_oht_motor_tank_capacity, motorType);
        editor.commit();
    }

    public String getmini_oht_motor_tank_capacity() {
        return pref.getString(mini_oht_motor_tank_capacity, null);
    }
    public void setoht_motor_tank_capacity(String motorType) {
        editor.putString(oht_motor_tank_capacity, motorType);
        editor.commit();
    }

    public String getoht_motor_tank_capacity() {
        return pref.getString(oht_motor_tank_capacity, null);
    }
    public void setglr_motor_tank_capacity(String motorType) {
        editor.putString(glr_motor_tank_capacity, motorType);
        editor.commit();
    }

    public String getglr_motor_tank_capacity() {
        return pref.getString(glr_motor_tank_capacity, null);
    }

    public void setmini_power_pump_tank_capacity(String motorType) {
        editor.putString(mini_power_pump_tank_capacity, motorType);
        editor.commit();
    }

    public String getmini_power_pump_tank_capacity() {
        return pref.getString(mini_power_pump_tank_capacity, null);
    }

}
