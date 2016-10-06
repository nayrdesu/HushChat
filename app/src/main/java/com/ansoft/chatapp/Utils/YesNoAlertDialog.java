package com.ansoft.chatapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ansoft.chatapp.FriendsActivity;
import com.ansoft.chatapp.R;
import com.parse.ParseObject;

/**
 * Created by user on 1/27/2016.
 */
public class YesNoAlertDialog {
    Activity activity;
    Dialog builder;
    ParseObject chat;

    public YesNoAlertDialog(Activity activity, ParseObject chat) {
        this.activity = activity;
        this.chat = chat;
    }

    public void show(){
        builder = new Dialog(activity,
                R.style.CustomDialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_yes_no,
                null);
        builder.setContentView(dialogView);
        Window window = builder.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.dimAmount = 0.8f;
        builder.setCancelable(false);
        builder.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);
        Button yes=(Button)dialogView.findViewById(R.id.yesBtn);
        Button no=(Button)dialogView.findViewById(R.id.noBtn);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat.deleteInBackground();
                activity.startActivity(new Intent(activity, FriendsActivity.class));
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();

    }

}
