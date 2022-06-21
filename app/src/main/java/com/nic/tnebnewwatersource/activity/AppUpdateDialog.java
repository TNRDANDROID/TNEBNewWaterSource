package com.nic.tnebnewwatersource.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.tnebnewwatersource.BuildConfig;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.databinding.AppUpdateDialogBinding;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.MyCustomTextView;

/**
 * Created by J.DILEEPKUMAR on 22/10/2021.
 */
public class AppUpdateDialog extends AppCompatActivity implements View.OnClickListener {


    private MyCustomTextView btnSave;
    private AppUpdateDialogBinding appUpdateDialogBinding;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUpdateDialogBinding = DataBindingUtil.setContentView(this,R.layout.app_update_dialog);
        appUpdateDialogBinding.setActivity(this);
        prefManager = new PrefManager(this);
        String localLanguage = prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);
        intializeUI();

    }

    public void intializeUI() {
        appUpdateDialogBinding.tvMessage1.setText("நீங்கள் TNEB செயலியை புதுப்பிக்க வேண்டும்");
        appUpdateDialogBinding.tvMessage.setText("இந்த செயலியை இனி பயன்படுத்த முடியாது. அதிகாரப்பூர்வ தளத்திற்குச் சென்று புதிய செயலியை பதிவிறக்கம் செய்யவும்");
        appUpdateDialogBinding.btnOk.setText("புதுப்பிக்கவும்");
        appUpdateDialogBinding.btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                showGooglePlay();
                break;
        }
    }

    public void showGooglePlay() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(" https://drdpr.tn.gov.in/")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(" https://drdpr.tn.gov.in/")));
        }
    }
}
