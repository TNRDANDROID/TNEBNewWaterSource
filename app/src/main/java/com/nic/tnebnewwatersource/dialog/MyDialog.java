package com.nic.tnebnewwatersource.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.Utils;


/**
 * Created by user on 04-06-2016.
 */
public class MyDialog {
    public myOnClickListener myListener;
    private dbData dbData;
    private PrefManager prefManager;

    public MyDialog(Activity context) {
        prefManager         = new PrefManager(context);
        this.myListener = (myOnClickListener) context;
        dbData = new dbData(context);

    }

    // This is my interface //
    public interface myOnClickListener {
        void onButtonClick(AlertDialog alertDialog, String type);

    }

    public void exitDialog(final Activity activity, String message, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.setCancelable(false);
        alertDialog.show();

       TextView tv_message = (TextView) dialogView.findViewById(R.id.tv_message);
        tv_message.setText(message);

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onButtonClick(alertDialog, type);
//                if(!type.equalsIgnoreCase("Exit")) {
//                    prefManager.clearSession();
//                    Crashlytics.setUserEmail(null);
//                    Crashlytics.setUserName(null);
//                    Crashlytics.setUserIdentifier(null);
//                    DatabaseHandler db = new DatabaseHandler(activity);
//                    db.deleteAll();
//                    ImageLoader imgLoader = new ImageLoader(activity);
//                    imgLoader.clearCache();
//                }
                if(type.equals("Logout")) {
                    dbData.open();
                    dbData.deleteAll();
                    Utils.clearApplicationData(activity);
                    prefManager.clearSession();
                }


            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
