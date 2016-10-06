package com.ansoft.chatapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ansoft.chatapp.Constants.PC;
import com.parse.ParseUser;

public class UpdateStatusActivity extends AppCompatActivity {
    private Toolbar toolbar;
    TextInputLayout statusField;
    Button updateBtn;
    ListView list;
    String[] statuses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list=(ListView)findViewById(R.id.list);
        setSupportActionBar(toolbar);
        statuses=getResources().getStringArray(R.array.list_default_status);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateStatusActivity.this, android.R.layout.simple_list_item_1, statuses);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseUser.getCurrentUser().put(PC.KEY_USER_STATUS, statuses[position]);
                ParseUser.getCurrentUser().saveInBackground();
                Intent in=new Intent(UpdateStatusActivity.this, FriendsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        });
        getSupportActionBar().setTitle("Update Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        statusField=(TextInputLayout)findViewById(R.id.statusField);
        updateBtn=(Button)findViewById(R.id.updateBtn);
        list=(ListView)findViewById(R.id.list);
        statusField.getEditText().setText(ParseUser.getCurrentUser().getString(PC.KEY_USER_STATUS));
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!statusField.getEditText().getText().toString().isEmpty()){
                    ParseUser.getCurrentUser().put(PC.KEY_USER_STATUS, statusField.getEditText().getText().toString());
                    ParseUser.getCurrentUser().saveInBackground();
                    Intent in=new Intent(UpdateStatusActivity.this, FriendsActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }
            }
        });
    }
}
