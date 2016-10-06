package com.ansoft.chatapp;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Utils.LoadingAlertDialog;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout displayName, emailField, pwField, confirmPwField;
    Button proceedBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final LoadingAlertDialog alert = new LoadingAlertDialog(SignUpActivity.this);
        displayName = (TextInputLayout) findViewById(R.id.displayNameField);
        emailField = (TextInputLayout) findViewById(R.id.emailField);
        pwField = (TextInputLayout) findViewById(R.id.passwordField);
        confirmPwField = (TextInputLayout) findViewById(R.id.confirmPasswordField);
        proceedBtn = (Button) findViewById(R.id.proceedButton);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
                String DN = displayName.getEditText().getText().toString();
                String EM = emailField.getEditText().getText().toString();
                String PW = pwField.getEditText().getText().toString();
                String CPW = confirmPwField.getEditText().getText().toString();
                if (DN.isEmpty()) {
                    alert.dismiss();
                    displayName.setError("This field is required!");
                } else if (EM.isEmpty()) {
                    alert.dismiss();
                    emailField.setError("This field is required!");

                } else if (PW.isEmpty()) {
                    alert.dismiss();
                    pwField.setError("This field is required!");
                } else if (CPW.isEmpty()) {
                    alert.dismiss();
                    confirmPwField.setError("Please re-enter your password");
                } else if (!PW.equalsIgnoreCase(CPW)) {
                    alert.dismiss();
                    confirmPwField.setError("Password did not match");
                } else {
                    Calendar cal = Calendar.getInstance();
                    ParseUser newUser = new ParseUser();
                    newUser.put(PC.KEY_DISPLAY_NAME, DN);
                    newUser.setEmail(EM+"@gmail.com");
                    newUser.put(PC.KEY_LAST_ONLINE, cal.getTime());
                    newUser.setUsername(EM);
                    newUser.setPassword(CPW);
                    newUser.put(PC.KEY_USER_STATUS, getResources().getString(R.string.default_user_status));
                    newUser.put(PC.KEY_USER_ISONLINE, false);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            alert.dismiss();
                            if (e == null) {
                                Intent in = new Intent(getApplicationContext(), UploadPhotoActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(in);
                                finish();
                            } else {
                                if (e.getCode()==ParseException.EMAIL_TAKEN){
                                    Toast.makeText(SignUpActivity.this, "This phonenumber is already registered", Toast.LENGTH_LONG).show();
                                }else if (e.getCode()==ParseException.USERNAME_TAKEN){
                                    Toast.makeText(SignUpActivity.this, "This phonenumber is already registered", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
