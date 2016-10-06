package com.ansoft.chatapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.FriendsActivity;
import com.ansoft.chatapp.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by user on 1/27/2016.
 */
public class YesNoAlertDialogAddFriend {
    Activity activity;
    Dialog builder;
    ParseUser user;
    String userId;
    MenuItem item;

    public YesNoAlertDialogAddFriend(Activity activity, ParseUser user, String userId, MenuItem item) {
        this.activity = activity;
        this.user = user;
        this.userId = userId;
        this.item = item;
    }

    public void show(){
        builder = new Dialog(activity,
                R.style.CustomDialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_yes_no_add_friend,
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

                user.add(PC.KEY_FRIENDS_ID, userId);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        builder.dismiss();
                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                    }
                });

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
