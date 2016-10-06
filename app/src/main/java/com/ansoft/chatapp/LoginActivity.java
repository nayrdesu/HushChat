package com.ansoft.chatapp;

import android.content.Intent;
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

import com.ansoft.chatapp.Utils.ClickEffect;
import com.ansoft.chatapp.Utils.LoadingAlertDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout usernameField, passwordField;
    Button loginBtn;
    TextView createAcc;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LoadingAlertDialog alert=new LoadingAlertDialog(LoginActivity.this);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        usernameField=(TextInputLayout)findViewById(R.id.usernameField);
        passwordField=(TextInputLayout)findViewById(R.id.passwordField);
        loginBtn=(Button)findViewById(R.id.loginButton);
        createAcc=(TextView)findViewById(R.id.tvCreateAccount);
        ClickEffect.Opacity(createAcc);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
                String UN = usernameField.getEditText().getText().toString();
                String PW = passwordField.getEditText().getText().toString();
                if (UN.isEmpty()) {
                    alert.dismiss();
                    usernameField.setError("This Field is required!");
                } else if (PW.isEmpty()) {
                    alert.dismiss();
                    passwordField.setError("This Field is required!");
                } else {
                    ParseUser.logInInBackground(UN, PW, new LogInCallback() {

                        @Override


                        public void done(ParseUser parseUser, ParseException e) {
                            alert.dismiss();

                            if (e == null) {
                                Intent in=new Intent(getApplicationContext(), FriendsActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(in);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }
}
