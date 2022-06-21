package com.nic.tnebnewwatersource.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.BankAccountDetailsAdapter;
import com.nic.tnebnewwatersource.adapter.BankAccountDetailsAdapterServer;
import com.nic.tnebnewwatersource.adapter.TypeOfBankDetailsAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.BankDetailsActivityBinding;
import com.nic.tnebnewwatersource.databinding.BankDetailsListAdapterBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.ProgressHUD;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by J.DILEEPKUMAR on 22/10/2021.
 */
public class BankDetailActivity extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener{
    private BankDetailsActivityBinding bankDetailsActivityBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    BankAccountDetailsAdapter bankAccountDetailsAdapter;
    TypeOfBankDetailsAdapter typeOfBankDetailsAdapter;
    BankAccountDetailsAdapterServer bankAccountDetailsAdapterServer;
    ArrayList<TNEBSystem> list=new ArrayList<>();
    ArrayList<TNEBSystem> TypesOfBankList=new ArrayList<>();
    ArrayList<TNEBSystem> BankListServerList=new ArrayList<>();
    ArrayList<TNEBSystem> BankListServerListInfo=new ArrayList<>();
    String acc_no="";
    String confirm_acc_no="";
    String account_id="";
    String bank_id="";
    String branch_id="";
    String account_name="";

    private ProgressHUD progressHUD;
    String localLanguage ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankDetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.bank_details_activity);
        bankDetailsActivityBinding.setActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        localLanguage =prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);
        Utils.setupUI(bankDetailsActivityBinding.parentLayout,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(getApplicationContext(),1);
        bankDetailsActivityBinding.viewDetailsRecycler.setLayoutManager(mLayoutManager);
        bankDetailsActivityBinding.viewDetailsRecycler.setItemAnimator(new DefaultItemAnimator());
        bankDetailsActivityBinding.viewDetailsRecycler.setHasFixedSize(true);
        bankDetailsActivityBinding.viewDetailsRecycler.setNestedScrollingEnabled(false);
        bankDetailsActivityBinding.viewDetailsRecycler.setFocusable(false);

        bankDetailsActivityBinding.typesOfBankRecycler.setLayoutManager(mLayoutManager1);
        bankDetailsActivityBinding.typesOfBankRecycler.setItemAnimator(new DefaultItemAnimator());
        bankDetailsActivityBinding.typesOfBankRecycler.setHasFixedSize(true);
        bankDetailsActivityBinding.typesOfBankRecycler.setNestedScrollingEnabled(false);
        bankDetailsActivityBinding.typesOfBankRecycler.setFocusable(false);

        bankDetailsActivityBinding.bankName.setFocusable(false);
        bankDetailsActivityBinding.bankBranch.setFocusable(false);

        viewTypesOfBanks();
        loadOfflineTypesOfBankDBValues();
        loadOfflineBankAccountListInfo();


        bankDetailsActivityBinding.viewAccountDetailsLayout.setOnClickListener(this::onClick);
        bankDetailsActivityBinding.addAccountDetailsLayout.setOnClickListener(this::onClick);
        bankDetailsActivityBinding.addDetailsBtn.setOnClickListener(this::onClick);
        bankDetailsActivityBinding.checkIfscIcon.setOnClickListener(this::onClick);
        bankDetailsActivityBinding.closeIcon.setOnClickListener(this::onClick);

        bankDetailsActivityBinding.bankConfirmAccNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bankDetailsActivityBinding.confirmLayout.setBackground(getResources().getDrawable(R.drawable.bank_acc_deatils_bg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirm_acc_no=charSequence.toString();
                if(confirm_acc_no.equals(acc_no)){
                    bankDetailsActivityBinding.confirmLayout.setBackground(getResources().getDrawable(R.drawable.bank_acc_deatils_bg));
                    bankDetailsActivityBinding.accNoLayout.setBackground(getResources().getDrawable(R.drawable.bank_acc_deatils_bg));
                }
                else {
                    bankDetailsActivityBinding.confirmLayout.setBackground(getResources().getDrawable(R.drawable.red_line_bg));
                    bankDetailsActivityBinding.accNoLayout.setBackground(getResources().getDrawable(R.drawable.red_line_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                confirm_acc_no=editable.toString();
                if(confirm_acc_no.equals(acc_no)){
                    bankDetailsActivityBinding.confirmLayout.setBackground(getResources().getDrawable(R.drawable.bank_acc_deatils_bg));
                    bankDetailsActivityBinding.accNoLayout.setBackground(getResources().getDrawable(R.drawable.bank_acc_deatils_bg));
                }
                else {
                    bankDetailsActivityBinding.confirmLayout.setBackground(getResources().getDrawable(R.drawable.red_line_bg));
                    bankDetailsActivityBinding.accNoLayout.setBackground(getResources().getDrawable(R.drawable.red_line_bg));
                }
            }
        });
        bankDetailsActivityBinding.bankAccNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                acc_no=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        bankDetailsActivityBinding.cardCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankDetailsActivityBinding.cardView.setVisibility(View.GONE);
            }
        });


    }


    public void getBankandBranchName(String ifsc_code) {
        try {
            new ApiService(this).makeJSONObjectRequest("BankandBranchName", Api.Method.POST, UrlGenerator.getBankServiceUrl(), getbankbranchNameEncrypted(ifsc_code), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject bankbranchNameListJsonParams(String ifsc_code) throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_BRANCH_DETAIL_BY_IFSC_CODE);
        dataSet.put(AppConstant.IFSC_CODE,ifsc_code);
        Log.d("IFSCValidate", "" + dataSet);
        return dataSet;
    }
    public JSONObject getbankbranchNameEncrypted(String ifsc_code) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), bankbranchNameListJsonParams(ifsc_code).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("IFSCValidate", "" + dataSet);
        return dataSet;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_account_details_layout:

                viewTypesOfBanks();
                break;

            case R.id.view_account_details_layout:
                viewBankDetailsView();
                break;

            case R.id.add_details_btn:
                if(checkSubmitCondition()){
                    saveAccountDetailsLocal();
                }
                else {
                    //Utils.showAlert(this,getResources().getString(R.string.failed));
                }
                break;

            case R.id.check_ifsc_icon:
                if(!bankDetailsActivityBinding.ifsc.getText().toString().equals("")){
                    Utils.hideSoftKeyboard(BankDetailActivity.this);
                    getBankandBranchName(bankDetailsActivityBinding.ifsc.getText().toString());
                }
                else {
                    Utils.hideSoftKeyboard(BankDetailActivity.this);
                    Utils.showAlert(this,"Please Enter the IFSC Code");
                }
                break;

            case R.id.close_icon:
                bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
                break;
        }
    }
    public void gotoHomePage(){
        Intent homepage=new Intent(this,HomePage.class);
        homepage.putExtra("Home", "Login");
        startActivity(homepage);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            String status="";
            String response="";
            if ("SaveBankInfo".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                String profile="";
                String no_of_bank_details_count="";
                Log.d("SaveBankInfo", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                    Utils.showAlert(this,"Successfully Saved");

                }
                else {

                }

            }
            if ("BankandBranchName".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                JSONArray jsonArray = new JSONArray();
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    jsonArray=jsonObject.getJSONArray(AppConstant.JSON_DATA);

                    String branch="";
                    String bank_name="";
                    String ifsc;
                    for(int i=0;i<jsonArray.length();i++){
                        bank_id=jsonArray.getJSONObject(i).getString("bank_id");
                        branch_id=jsonArray.getJSONObject(i).getString("branch_id");
                        branch=jsonArray.getJSONObject(i).getString("branchname");
                        bank_name=jsonArray.getJSONObject(i).getString("bank_name");
                        ifsc=jsonArray.getJSONObject(i).getString("ifsc_code");
                    }


                    bankDetailsActivityBinding.bankName.setText(bank_name);
                    bankDetailsActivityBinding.bankBranch.setText(branch);


                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")) {
                    //Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                    //Utils.showAlert(RegisterScreen.this,responseObj.getString(AppConstant.KEY_MESSAGE));
                    Utils.showAlert(this,"Enter the valid IFSC!");
                    bankDetailsActivityBinding.bankName.setText("");
                    bankDetailsActivityBinding.bankBranch.setText("");

                }
                Log.d("BankBranchList", "" + jsonObject.toString());
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


    @Override
    public void onBackPressed() {
        if(bankDetailsActivityBinding.editDetailsLayout.getVisibility()==View.VISIBLE){
            bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
        }
        else if(bankDetailsActivityBinding.cardView.getVisibility()==View.VISIBLE) {
            bankDetailsActivityBinding.cardView.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    public void addBankDetailsView(int position){
        account_id=TypesOfBankList.get(position).getAccount_id();
        account_name=TypesOfBankList.get(position).getAccount_name();
        bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.VISIBLE);

    }
    public void viewBankDetailsView(){

        bankDetailsActivityBinding.viewDetailsText.setTextColor(getResources().getColor(R.color.white));

        bankDetailsActivityBinding.addDetailsText.setTextColor(getResources().getColor(R.color.grey_5));
        bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
        bankDetailsActivityBinding.typesOfBankRecycler.setVisibility(View.GONE);
        bankDetailsActivityBinding.cardView.setVisibility(View.GONE);
        bankDetailsActivityBinding.viewDetailsRecycler.setVisibility(View.VISIBLE);
        bankDetailsActivityBinding.viewViewId.setVisibility(View.VISIBLE);
        bankDetailsActivityBinding.addViewId.setVisibility(View.GONE);

        loadOfflineBankAccountList();
    }

    public boolean checkSubmitCondition(){
        //if(!bankDetailsActivityBinding.userName.getText().toString().equals("")){
            if(!bankDetailsActivityBinding.ifsc.getText().toString().equals("")) {
                if (!bankDetailsActivityBinding.bankName.getText().toString().equals("")) {
                    if (!bankDetailsActivityBinding.bankAccNo.getText().toString().equals("")) {
                        if (!bankDetailsActivityBinding.bankConfirmAccNo.getText().toString().equals("")) {
                            if (bankDetailsActivityBinding.bankAccNo.getText().toString().equals(bankDetailsActivityBinding.bankConfirmAccNo.getText().toString())) {
                                return true;
                            } else {
                                Utils.showAlert(this, getResources().getString(R.string.please_check_the_account_number));
                                return false;
                            }
                        } else {
                            Utils.showAlert(this, getResources().getString(R.string.please_enetr_confirm_account_number));
                            return false;
                        }
                    } else {
                        Utils.showAlert(this, getResources().getString(R.string.please_enetr_bank_account_number));
                        return false;
                    }
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.please_valiadte_ifsc_code));
                    return false;
                }
            }
            else {
                Utils.showAlert(this,getResources().getString(R.string.please_enetr_ifsc_code));
                return false;
            }
        /*}
        else {
            Utils.showAlert(this,getResources().getString(R.string.please_enter_bank_account_name));
            return false;
        }*/
    }

    public void saveAccountDetailsLocal(){
        String AccountHolderName=bankDetailsActivityBinding.userName.getText().toString();
        String IFSCCode=bankDetailsActivityBinding.ifsc.getText().toString();
        String BankBranchName=bankDetailsActivityBinding.bankBranch.getText().toString();
        String BankName=bankDetailsActivityBinding.bankName.getText().toString();
        String AccountNumber=bankDetailsActivityBinding.bankAccNo.getText().toString();
        String whereClause="";
        String[] whereArgs = null;
        long id=0;

        ContentValues contentValues=new ContentValues();
        try{
            contentValues.put("account_id",account_id);
            contentValues.put("bank_id",bank_id);
            contentValues.put("branch_id",branch_id);
            contentValues.put("account_no",AccountNumber);
            contentValues.put("ifsc_code",IFSCCode);
            contentValues.put("branch_name",BankBranchName);
            contentValues.put("bank_name",BankName);
            contentValues.put("account_name",account_name);

            whereClause = "account_id = ? ";
            whereArgs = new String[]{account_id};
            dbData.open();
            ArrayList<TNEBSystem> listsize = dbData.getSaveParticularBankDetails(account_id);

            if(listsize.size() < 1) {
                id = db.insert(DBHelper.TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE,null,contentValues);
                if(id>0){
                    Utils.showAlert(this,getResources().getString(R.string.values_inserted_successfully));
                    bankDetailsActivityBinding.userName.setText("");
                    bankDetailsActivityBinding.ifsc.setText("");
                    bankDetailsActivityBinding.bankBranch.setText("");
                    bankDetailsActivityBinding.bankName.setText("");
                    bankDetailsActivityBinding.bankAccNo.setText("");
                    bankDetailsActivityBinding.bankConfirmAccNo.setText("");
                    bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.failed));
                    bankDetailsActivityBinding.userName.setText("");
                    bankDetailsActivityBinding.ifsc.setText("");
                    bankDetailsActivityBinding.bankBranch.setText("");
                    bankDetailsActivityBinding.bankName.setText("");
                    bankDetailsActivityBinding.bankAccNo.setText("");
                    bankDetailsActivityBinding.bankConfirmAccNo.setText("");
                }
            }
            else {
                id = db.update(DBHelper.TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE, contentValues, whereClause, whereArgs);
                if(id>0){
                    Utils.showAlert(this,getResources().getString(R.string.values_updated_successfully));
                    bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
                    bankDetailsActivityBinding.userName.setText("");
                    bankDetailsActivityBinding.ifsc.setText("");
                    bankDetailsActivityBinding.bankBranch.setText("");
                    bankDetailsActivityBinding.bankName.setText("");
                    bankDetailsActivityBinding.bankAccNo.setText("");
                    bankDetailsActivityBinding.bankConfirmAccNo.setText("");
                }
                else {
                    Utils.showAlert(this,getResources().getString(R.string.failed));
                    bankDetailsActivityBinding.userName.setText("");
                    bankDetailsActivityBinding.ifsc.setText("");
                    bankDetailsActivityBinding.bankBranch.setText("");
                    bankDetailsActivityBinding.bankName.setText("");
                    bankDetailsActivityBinding.bankAccNo.setText("");
                    bankDetailsActivityBinding.bankConfirmAccNo.setText("");
                }

            }
        }catch (Exception e){
            Utils.showAlert(this,getResources().getString(R.string.failed));
            bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
            bankDetailsActivityBinding.userName.setText("");
            bankDetailsActivityBinding.ifsc.setText("");
            bankDetailsActivityBinding.bankBranch.setText("");
            bankDetailsActivityBinding.bankName.setText("");
            bankDetailsActivityBinding.bankAccNo.setText("");
            bankDetailsActivityBinding.bankConfirmAccNo.setText("");
        }



    }




    public void viewTypesOfBanks(){
        bankDetailsActivityBinding.addDetailsText.setTextColor(getResources().getColor(R.color.white));
        bankDetailsActivityBinding.viewDetailsText.setTextColor(getResources().getColor(R.color.grey_5));
        bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
        bankDetailsActivityBinding.typesOfBankRecycler.setVisibility(View.VISIBLE);
        bankDetailsActivityBinding.viewDetailsRecycler.setVisibility(View.GONE);
        bankDetailsActivityBinding.cardView.setVisibility(View.GONE);
        bankDetailsActivityBinding.viewViewId.setVisibility(View.GONE);
        bankDetailsActivityBinding.addViewId.setVisibility(View.VISIBLE);

    }

    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    public void loadOfflineTypesOfBankDBValues() {
        Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE, null);
        TypesOfBankList.clear();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String account_id = cursor.getString(cursor.getColumnIndexOrThrow("account_id"));
                    String account_name = cursor.getString(cursor.getColumnIndexOrThrow("account_name"));
                    String account_id_display_order = cursor.getString(cursor.getColumnIndexOrThrow("account_id_display_order"));
                    String account_saved_status = cursor.getString(cursor.getColumnIndexOrThrow("account_saved_status"));


                    list.setAccount_id(account_id);
                    list.setAccount_name(account_name);
                    list.setAccount_id_display_order(account_id_display_order);
                    list.setAccount_saved_status(account_saved_status);


                    TypesOfBankList.add(list);
                    //   Log.d("ConnectionList", "" + ConnectionList);
                } while (cursor.moveToNext());
            }

        }
        if(TypesOfBankList.size()>0){
            typeOfBankDetailsAdapter=new TypeOfBankDetailsAdapter(this,TypesOfBankList,dbData);
            bankDetailsActivityBinding.typesOfBankRecycler.setAdapter(typeOfBankDetailsAdapter);

        }
        else {

        }

    }
    public void loadOfflineBankAccountList() {
        Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.TN_EB_BANK_SERVER_DETAILS_TABLE, null);
        BankListServerList.clear();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String bank_account_id = cursor.getString(cursor.getColumnIndexOrThrow("bank_account_id"));
                    String account_id = cursor.getString(cursor.getColumnIndexOrThrow("account_id"));
                    String bank_id = cursor.getString(cursor.getColumnIndexOrThrow("bank_id"));
                    String branch_id = cursor.getString(cursor.getColumnIndexOrThrow("branch_id"));
                    String ifsc_code = cursor.getString(cursor.getColumnIndexOrThrow("ifsc_code"));
                    String account_no = cursor.getString(cursor.getColumnIndexOrThrow("account_no"));
                    String dcode = cursor.getString(cursor.getColumnIndexOrThrow("dcode"));
                    String bcode = cursor.getString(cursor.getColumnIndexOrThrow("bcode"));
                    String pvcode = cursor.getString(cursor.getColumnIndexOrThrow("pvcode"));
                    String bank_name = cursor.getString(cursor.getColumnIndexOrThrow("bank_name"));
                    String branchname = cursor.getString(cursor.getColumnIndexOrThrow("branchname"));


                    list.setBank_account_id(bank_account_id);
                    list.setAccount_id(account_id);
                    list.setBank_id(bank_id);
                    list.setBranch_id(branch_id);
                    list.setIfsc_code(ifsc_code);
                    list.setAccount_no(account_no);
                    list.setDistictCode(dcode);
                    list.setBlockCode(bcode);
                    list.setPvCode(pvcode);
                    list.setBank_name(bank_name);
                    list.setBranch_name(branchname);


                    BankListServerList.add(list);
                    //   Log.d("ConnectionList", "" + ConnectionList);
                } while (cursor.moveToNext());
            }

        }
        if(BankListServerList.size()>0){
            bankAccountDetailsAdapterServer=new BankAccountDetailsAdapterServer(this,BankListServerList,dbData);
            bankDetailsActivityBinding.viewDetailsRecycler.setAdapter(bankAccountDetailsAdapterServer);

        }
        else {

        }

    }
    public void loadOfflineBankAccountListInfo() {
        Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.TN_EB_BANK_SERVER_DETAILS_TABLE, null);
        BankListServerListInfo.clear();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String bank_account_id = cursor.getString(cursor.getColumnIndexOrThrow("bank_account_id"));
                    String account_id = cursor.getString(cursor.getColumnIndexOrThrow("account_id"));
                    String bank_id = cursor.getString(cursor.getColumnIndexOrThrow("bank_id"));
                    String branch_id = cursor.getString(cursor.getColumnIndexOrThrow("branch_id"));
                    String ifsc_code = cursor.getString(cursor.getColumnIndexOrThrow("ifsc_code"));
                    String account_no = cursor.getString(cursor.getColumnIndexOrThrow("account_no"));
                    String dcode = cursor.getString(cursor.getColumnIndexOrThrow("dcode"));
                    String bcode = cursor.getString(cursor.getColumnIndexOrThrow("bcode"));
                    String pvcode = cursor.getString(cursor.getColumnIndexOrThrow("pvcode"));
                    String bank_name = cursor.getString(cursor.getColumnIndexOrThrow("bank_name"));
                    String branchname = cursor.getString(cursor.getColumnIndexOrThrow("branchname"));


                    list.setBank_account_id(bank_account_id);
                    list.setAccount_id(account_id);
                    list.setBank_id(bank_id);
                    list.setBranch_id(branch_id);
                    list.setIfsc_code(ifsc_code);
                    list.setAccount_no(account_no);
                    list.setDistictCode(dcode);
                    list.setBlockCode(bcode);
                    list.setPvCode(pvcode);
                    list.setBank_name(bank_name);
                    list.setBranch_name(branchname);


                    BankListServerListInfo.add(list);
                    //   Log.d("ConnectionList", "" + ConnectionList);
                } while (cursor.moveToNext());
            }

        }


    }
    public  void showPopUp(Context activity, String account_id){
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_bank_info);

            TextView account_number=(TextView)dialog.findViewById(R.id.account_number) ;
            TextView bank_branch_value=(TextView)dialog.findViewById(R.id.bank_branch_value) ;
            TextView ifsc_code_value=(TextView)dialog.findViewById(R.id.ifsc_code_value) ;
            ImageView card_close_icon=(ImageView)dialog.findViewById(R.id.card_close_icon) ;
            String bank_name="";
            String branch_name="";
            String acc_no="";
            String ifsc_code="";
            boolean flag=false;
           for (int i=0;i<BankListServerListInfo.size();i++){
                if(account_id.equals(BankListServerListInfo.get(i).getAccount_id())){
                    flag=true;
                    bank_name=BankListServerListInfo.get(i).getBank_name();
                    branch_name=BankListServerListInfo.get(i).getBranch_name();
                    acc_no=BankListServerListInfo.get(i).getAccount_no();
                    ifsc_code=BankListServerListInfo.get(i).getIfsc_code();
                    break;
                }
                else {
                    flag=false;
                }
            }
            if(flag){
                account_number.setText(acc_no);
                bank_branch_value.setText(bank_name+" & "+branch_name);
                ifsc_code_value.setText(ifsc_code);

            }
            else {
                Utils.showAlert(this,"No Information added for this account");
            }
            card_close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showInformationAccount(String account_id){
        String bank_name="";
        String branch_name="";
        String acc_no="";
        String ifsc_code="";
        boolean flag=false;
        bankDetailsActivityBinding.editDetailsLayout.setVisibility(View.GONE);
        bankDetailsActivityBinding.cardView.setVisibility(View.VISIBLE);
        for (int i=0;i<BankListServerListInfo.size();i++){
            if(account_id.equals(BankListServerListInfo.get(i).getAccount_id())){
                flag=true;
                bank_name=BankListServerListInfo.get(i).getBank_name();
                branch_name=BankListServerListInfo.get(i).getBranch_name();
                acc_no=BankListServerListInfo.get(i).getAccount_no();
                ifsc_code=BankListServerListInfo.get(i).getIfsc_code();
                break;
            }
            else {
                flag=false;
            }
        }
        if(flag){
            bankDetailsActivityBinding.accountNumber.setText(acc_no);
            bankDetailsActivityBinding.bankBranchValue.setText(bank_name+" & "+branch_name);
            bankDetailsActivityBinding.ifscCodeValue.setText(ifsc_code);

        }
        else {
            bankDetailsActivityBinding.cardView.setVisibility(View.GONE);
            Utils.showAlert(this,"No Information added for this account");
        }

    }


}
