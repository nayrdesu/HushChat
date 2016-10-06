package com.ansoft.chatapp;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansoft.chatapp.Adapter.ChatListAdapter;
import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Data.ChatData;
import com.ansoft.chatapp.Utils.CircleTransformWithoutStroke;
import com.ansoft.chatapp.Utils.StringCombo;
import com.ansoft.chatapp.Utils.YesNoAlertDialog;
import com.ansoft.chatapp.Utils.YesNoAlertDialogAddFriend;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    EditText msgField;
    Button sendBtn;
    boolean objectLoaded;
    String UNIQ;
    List<String> messages;
    ArrayList<ChatData> data;
    public int APP_REFRESH_TIME_IN_SEC=1;
    ChatListAdapter adapter;
    ParseUser anotherUser;
    int totalMessages = 0;
    ListView list;
    ParseObject msgObject;
    TextView seenTxt;
    public String MSGOBJECTID;
    boolean firstmsgsent = false;
    String otherId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        data = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        seenTxt = (TextView) findViewById(R.id.seenTxt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(8);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.list);
        msgField = (EditText) findViewById(R.id.msgField);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        msgObject = PC.getCurrentChat();


        if (PC.getCurrentChat() != null) {
            if (!PC.getCurrentChat().getBoolean(PC.KEY_CHAT_IS_GROUP)) {

                List<String> btns = PC.getCurrentChat().getList(PC.KEY_CHAT_BETWEEN);
                for (String s : btns) {
                    if (!s.equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                        otherId = s;
                    }
                }
            }
        }
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        final int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        final ActionBar ab = getSupportActionBar();
        if (!getIntent().hasExtra("OBJECTID")) {
            if (msgObject.getBoolean(PC.KEY_CHAT_IS_GROUP)) {
                getSupportActionBar().setTitle(msgObject.getString(PC.KEY_CHAT_GROUP_NAME));
            } else {

                ArrayList<String> ids = (ArrayList) msgObject.getList(PC.KEY_CHAT_BETWEEN);
                for (String s : ids) {
                    if (!s.equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.getInBackground(s, new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                anotherUser = user;
                                getSupportActionBar().setTitle(user.getString(PC.KEY_DISPLAY_NAME));
                                Picasso.with(ChatActivity.this)
                                        .load(user.getParseFile(PC.KEY_PROFILE_PHOTO).getUrl().toString())
                                        .transform(new CircleTransformWithoutStroke())
                                        .resize(mActionBarSize - 15, mActionBarSize - 15)
                                        .placeholder(R.drawable.default_profile_photo_circle)
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                Drawable d = new BitmapDrawable(getResources(), bitmap);
                                                ab.setIcon(d);
                                                ab.setDisplayShowHomeEnabled(true);
                                                ab.setDisplayHomeAsUpEnabled(true);
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                            }
                                        });
                            }
                        });

                    }
                }
            }
        } else {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(getIntent().getStringExtra("OBJECTID"), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    anotherUser = user;
                    getSupportActionBar().setTitle(user.getString(PC.KEY_DISPLAY_NAME));
                    String userPhoto;
                    if (user.getParseFile(PC.KEY_PROFILE_PHOTO) == null) {
                        userPhoto = PC.KEY_DEFAULT_PROFILE_PHOTO_LINK;
                    } else {
                        userPhoto = user.getParseFile(PC.KEY_PROFILE_PHOTO).getUrl().toString();
                    }
                    Picasso.with(ChatActivity.this)
                            .load(userPhoto)
                            .transform(new CircleTransformWithoutStroke())
                            .resize(mActionBarSize - 15, mActionBarSize - 15)
                            .placeholder(R.drawable.default_profile_photo_circle)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                                    ab.setIcon(d);
                                    ab.setDisplayShowHomeEnabled(true);
                                    ab.setDisplayHomeAsUpEnabled(true);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            });
                }
            });
        }
        if (!getIntent().hasExtra("OBJECTID")) {
            MSGOBJECTID = msgObject.getObjectId();
            messages = PC.getCurrentChat().getList(PC.KEY_CHAT_MESSAGES);
            if (messages.size() > totalMessages) {
                totalMessages = messages.size();
                parseMessages();
            }

            if (msgObject.getString(PC.KEY_CHAT_LAST_MSGID).equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                if (msgObject.getBoolean(PC.KEY_CHAT_IS_SEEN)) {
                    seenTxt.setVisibility(View.VISIBLE);
                } else {
                    seenTxt.setVisibility(View.GONE);
                }
            } else {
                seenTxt.setVisibility(View.GONE);
            }
            retrievesMessages();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (adapter != null) {
                    if (firstmsgsent) {
                        MSGOBJECTID = msgObject.getObjectId();
                        messages = PC.getCurrentChat().getList(PC.KEY_CHAT_MESSAGES);
                        if (messages.size() > totalMessages) {
                            totalMessages = messages.size();
                            parseMessages();
                        }

                        if (msgObject.getString(PC.KEY_CHAT_LAST_MSGID).equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                            if (msgObject.getBoolean(PC.KEY_CHAT_IS_SEEN)) {
                                seenTxt.setVisibility(View.VISIBLE);
                            } else {
                                seenTxt.setVisibility(View.GONE);
                            }
                        } else {
                            seenTxt.setVisibility(View.GONE);
                        }
                        retrievesMessages();
                    }
                }
                handler.postDelayed(this, APP_REFRESH_TIME_IN_SEC * 1000);
            }
        }, APP_REFRESH_TIME_IN_SEC * 1000);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formattedDate = Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
                if (!msgField.getText().toString().isEmpty()) {
                    String message = msgField.getText().toString() + "-" +
                            ParseUser.getCurrentUser().getObjectId().toString() + "-" +
                            ParseUser.getCurrentUser().getString(PC.KEY_DISPLAY_NAME) + "-" +
                            getIntent().getStringExtra("OBJECTID") + "-" +
                            "zero" + "-" + formattedDate + "-" +
                            "hello";
                    if (adapter == null) {
                        final ParseObject msg = new ParseObject(PC.KEY_CHAT_OBJECT);
                        StringCombo sc=new StringCombo(ParseUser.getCurrentUser().getObjectId().toString(), getIntent().getStringExtra("OBJECTID"));

                        msg.put(PC.KEY_CHAT_UNIQUE_ID, sc.getEncryptedValue());
                        List btns = new ArrayList();
                        btns.add(ParseUser.getCurrentUser().getObjectId());
                        btns.add(getIntent().getStringExtra("OBJECTID"));
                        msg.put(PC.KEY_CHAT_LAST_MSG_SENDER_NAME, ParseUser.getCurrentUser().getString(PC.KEY_DISPLAY_NAME));
                        msg.put(PC.KEY_CHAT_IS_GROUP, false);
                        msg.put(PC.KEY_CHAT_BETWEEN, btns);
                        msg.add(PC.KEY_CHAT_MESSAGES, message);
                        msg.put(PC.KEY_CHAT_LAST_MSGID, ParseUser.getCurrentUser().getObjectId());
                        msg.put(PC.KEY_CHAT_IS_SEEN, false);
                        msg.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    PC.setCurrentChat(msg);
                                    msgObject = msg;
                                    firstmsgsent = true;
                                    MSGOBJECTID = msg.getObjectId();
                                } else {
                                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        totalMessages = 1;
                        String[] array = message.split("-");
                        ChatData d = new ChatData();
                        d.setMsg(array[0]);
                        d.setSenderId(array[1]);
                        d.setReceiverId(array[2]);
                        d.setIsSeen(false);
                        d.setIsPhoto(false);
                        d.setTimeStamp(array[4]);
                        d.setPhotoLink(array[5]);
                        data.add(d);
                        adapter = new ChatListAdapter(ChatActivity.this, data);
                        list.setAdapter(adapter);
                    } else {
                        seenTxt.setVisibility(View.GONE);
                        String[] array = message.split("-");
                        ChatData d = new ChatData();
                        d.setMsg(array[0]);
                        d.setSenderId(array[1]);
                        d.setReceiverId(array[3]);
                        d.setIsPhoto(false);
                        d.setTimeStamp(array[5]);
                        d.setPhotoLink(array[6]);
                        d.setIsSeen(false);
                        data.add(d);
                        adapter.notifyDataSetChanged();

                        totalMessages += 1;
                        msgObject.add(PC.KEY_CHAT_MESSAGES, message);
                        msgObject.put(PC.KEY_CHAT_IS_SEEN, false);
                        msgObject.put(PC.KEY_CHAT_LAST_MSGID, ParseUser.getCurrentUser().getObjectId());
                        msgObject.saveInBackground();

                    }
                    msgField.setText("");
                }


            }
        });

    }

    public void retrievesMessages() {
        msgObject = PC.getCurrentChat();
        messages = PC.getCurrentChat().getList(PC.KEY_CHAT_MESSAGES);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PC.KEY_CHAT_OBJECT);
        query.getInBackground(MSGOBJECTID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject chat, ParseException e) {
                if (chat!=null) {
                    msgObject = chat;
                    messages = chat.getList(PC.KEY_CHAT_MESSAGES);

                    if (messages.size() > totalMessages) {
                        totalMessages = messages.size();
                        chat.put(PC.KEY_CHAT_IS_SEEN, true);
                        chat.saveInBackground();
                        parseMessages();
                    }
                    if (chat.getString(PC.KEY_CHAT_LAST_MSGID).equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                        if (chat.getBoolean(PC.KEY_CHAT_IS_SEEN)) {
                            seenTxt.setVisibility(View.VISIBLE);
                        } else {
                            seenTxt.setVisibility(View.GONE);
                        }
                    } else {
                        seenTxt.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    public void parseMessages() {
        firstmsgsent = true;
        data.clear();
        for (int i = 0; i < messages.size(); i++) {
            String msg = messages.get(i);
            String[] array = msg.split("-");
            ChatData d = new ChatData();
            d.setMsg(array[0]);
            if (msgObject.getBoolean(PC.KEY_CHAT_IS_GROUP)) {
                d.setIsGroup(true);
                d.setSenderName(array[2]);
            } else {
                d.setIsGroup(false);
            }
            d.setSenderId(array[1]);
            d.setReceiverId(array[3]);
            if (array[4].equalsIgnoreCase("zero")) {
                d.setIsPhoto(false);
            } else {
                d.setIsPhoto(true);
            }
            d.setTimeStamp(array[5]);
            d.setPhotoLink(array[6]);
            if (i == messages.size() - 1) {

                d.setIsSeen(msgObject.getBoolean(PC.KEY_CHAT_IS_SEEN));
                if (array[2].equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                    if (!msgObject.getBoolean(PC.KEY_CHAT_IS_SEEN)) {
                        msgObject.put(PC.KEY_CHAT_IS_SEEN, true);
                        msgObject.saveInBackground();
                    }
                }
            }

            data.add(d);
        }

        adapter = new ChatListAdapter(ChatActivity.this, data);
        list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_delete_chat:
                YesNoAlertDialog alert = new YesNoAlertDialog(ChatActivity.this, msgObject);
                alert.show();
                break;

            case R.id.action_add_as_friend:
                String userId="";
                if (PC.getCurrentChat()!=null){
                    List<String> users=PC.getCurrentChat().getList(PC.KEY_CHAT_BETWEEN);
                    for (String s: users){
                        if(!s.equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId().toString())){
                            userId=s;
                        }
                    }
                }else {
                    userId=getIntent().getStringExtra("OBJECTID");
                }
                YesNoAlertDialogAddFriend alerts = new YesNoAlertDialogAddFriend(ChatActivity.this, ParseUser.getCurrentUser(), userId, item);
                alerts.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.action_add_as_friend);

        List<String> friendsId = ParseUser.getCurrentUser().getList(PC.KEY_FRIENDS_ID);
        if (!getIntent().hasExtra("OBJECTID")) {
            if (!PC.getCurrentChat().getBoolean(PC.KEY_CHAT_IS_GROUP)) {
                if (friendsId != null) {
                    if (friendsId.contains(otherId)) {
                        registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                        registrar.setEnabled(false);
                    } else {
                        registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    }
                } else {
                    registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                }
            } else {
                registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }
        } else {
            if (friendsId != null) {
                if (friendsId.contains(getIntent().getStringExtra("OBJECTID"))) {
                    registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                    registrar.setEnabled(false);
                } else {
                    registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                }
            } else {
                registrar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        return true;
    }
}
