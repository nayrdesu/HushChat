package com.ansoft.chatapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Data.FriendList;
import com.ansoft.chatapp.Utils.LoadingAlertDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextInputLayout emailField;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        emailField = (TextInputLayout) findViewById(R.id.emailSearchField);
        addBtn = (Button) findViewById(R.id.addButton);

        final LoadingAlertDialog alert = new LoadingAlertDialog(AddFriendActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add friend by phone number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
                if (emailField.getEditText().getText().toString().isEmpty()) {
                    alert.dismiss();
                    emailField.setError("Please enter your friends phone number");
                } else {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            if (list != null) {
                                for (ParseUser user : list) {
                                    Log.e("USER EMAIL", user.getEmail());
                                    if (user.getUsername().equalsIgnoreCase(emailField.getEditText().getText().toString())) {
                                        ParseUser.getCurrentUser().add(PC.KEY_FRIENDS_ID, user.getObjectId().toString());
                                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                alert.dismiss();
                                                Intent in = new Intent(getApplicationContext(), FriendsActivity.class);
                                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(in);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            } else {
                                alert.dismiss();
                                Toast.makeText(AddFriendActivity.this, "There is no user related with this phone number", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });
    }

}
