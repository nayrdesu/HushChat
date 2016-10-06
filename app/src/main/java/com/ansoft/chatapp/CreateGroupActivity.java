package com.ansoft.chatapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Utils.LoadingAlertDialog;
import com.ansoft.chatapp.Utils.StringCombo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CreateGroupActivity extends AppCompatActivity {

    TextInputLayout groupName;
    TextInputLayout ph1, ph2, ph3, ph4, ph5, ph6, ph7, ph8, ph9, ph10;
    Button createBtn;
    private Toolbar toolbar;
    boolean user1, user2, user3, user4, user5, user6, user7, user8, user9, user10;
    int totalfrns = 0;
    int totalvalid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create a Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        groupName = (TextInputLayout) findViewById(R.id.groupNameField);
        ph1 = (TextInputLayout) findViewById(R.id.groupUser1);
        ph2 = (TextInputLayout) findViewById(R.id.groupUser2);
        ph3 = (TextInputLayout) findViewById(R.id.groupUser3);
        ph4 = (TextInputLayout) findViewById(R.id.groupUser4);
        ph5 = (TextInputLayout) findViewById(R.id.groupUser5);
        ph6 = (TextInputLayout) findViewById(R.id.groupUser6);
        ph7 = (TextInputLayout) findViewById(R.id.groupUser7);
        ph8 = (TextInputLayout) findViewById(R.id.groupUser8);
        ph9 = (TextInputLayout) findViewById(R.id.groupUser9);
        ph10 = (TextInputLayout) findViewById(R.id.groupUser10);
        createBtn = (Button) findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupName.getEditText().getText().toString().isEmpty()) {
                    groupName.setError("Please enter group name");
                } else if (ph1.getEditText().getText().toString().isEmpty()) {
                    ph1.setError("Enter at least two friends phone number");
                } else if (ph2.getEditText().getText().toString().isEmpty()) {
                    ph2.setError("Enter at least two friends phone number");
                } else {
                    checkfrends();
                    final LoadingAlertDialog alert = new LoadingAlertDialog(CreateGroupActivity.this);
                    alert.show();
                    final ParseObject chat = new ParseObject(PC.KEY_CHAT_OBJECT);
                    String formattedDate = Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
                    final String message = "Hello" + "-" +
                            ParseUser.getCurrentUser().getObjectId().toString() + "-" +
                            ParseUser.getCurrentUser().getString(PC.KEY_DISPLAY_NAME) + "-" +
                            getIntent().getStringExtra("OBJECTID") + "-" +
                            "zero" + "-" + formattedDate + "-" +
                            "hello";
                    chat.put(PC.KEY_CHAT_UNIQUE_ID, "6767662");
                    final List btns = new ArrayList();
                    btns.add(ParseUser.getCurrentUser().getObjectId().toString());
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            int j = 1;
                            start:
                            for (int i = 0; i < list.size(); i++) {


                                if (list.get(i).getUsername().equalsIgnoreCase(getLayout(j).getEditText().getText().toString())) {
                                    setBoolTrue(j);
                                    j++;
                                    totalvalid += 1;
                                    btns.add(list.get(i).getObjectId());
                                    if (j < 11) {
                                        continue start;
                                    }
                                }
                            }
                                SaveChat(chat, btns, message, alert);

                        }
                    });


                }
            }
        });
    }

    public void SaveChat(ParseObject chat, List btns, String message, final LoadingAlertDialog alert) {
        chat.put(PC.KEY_CHAT_LAST_MSG_SENDER_NAME, ParseUser.getCurrentUser().getString(PC.KEY_DISPLAY_NAME));
        chat.put(PC.KEY_CHAT_IS_GROUP, true);
        chat.put(PC.KEY_CHAT_GROUP_NAME, groupName.getEditText().getText().toString());
        chat.put(PC.KEY_CHAT_BETWEEN, btns);
        chat.add(PC.KEY_CHAT_MESSAGES, message);
        chat.put(PC.KEY_CHAT_LAST_MSGID, ParseUser.getCurrentUser().getObjectId());
        chat.put(PC.KEY_CHAT_IS_SEEN, false);
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    alert.dismiss();
                    finish();
                    startActivity(new Intent(CreateGroupActivity.this, FriendsActivity.class));
                } else {
                    Toast.makeText(CreateGroupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public TextInputLayout getLayout(int x) {
        switch (x) {
            case 1:
                return ph1;
            case 2:
                return ph2;

            case 3:
                return ph3;

            case 4:
                return ph4;

            case 5:
                return ph5;
            case 6:
                return ph6;
            case 7:
                return ph7;
            case 8:
                return ph8;
            case 9:
                return ph9;
            case 10:
                return ph10;

        }
        return null;
    }

    public void checkfrends() {
        if (!ph1.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph2.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph3.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph4.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph5.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph6.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph7.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph8.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph9.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }
        if (!ph10.getEditText().getText().toString().isEmpty()) {
            totalfrns += 1;
        }


    }

    public void setBoolTrue(int x) {
        switch (x) {
            case 1:
                this.user1 = true;
                break;
            case 2:
                this.user2 = true;
                break;
            case 3:
                this.user3 = true;
                break;
            case 4:
                this.user4 = true;
                break;
            case 5:
                this.user5 = true;
                break;
            case 6:
                this.user6 = true;
                break;
            case 7:
                this.user7 = true;
                break;
            case 8:
                this.user8 = true;
                break;
            case 9:
                this. user9 = true;
                break;
            case 10:
                this.user10 = true;
                break;
        }
    }

    public boolean getBoolean(int x){
        switch (x){
            case 1:
                return user1;
            case 2:
                return user2;
            case 3:
                return user3;
            case 4:
                return user4;
            case 5:
                return user5;
            case 6:
                return user6;
            case 7:
                return user7;
            case 8:
                return user8;
            case 9:
                return user9;
            case 10:
                return user10;
        }
        return false;
    }
}
