package com.ansoft.chatapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansoft.chatapp.ChatActivity;
import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Data.FriendsListData;
import com.ansoft.chatapp.R;
import com.ansoft.chatapp.Utils.CircleImageView;
import com.ansoft.chatapp.Utils.CircleTransform;
import com.ansoft.chatapp.Utils.LoadingAlertDialog;
import com.ansoft.chatapp.Utils.StringCombo;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FriendListAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<FriendsListData> data;

    public FriendListAdapter(ArrayList<FriendsListData> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {
        LayoutInflater infalter = activity.getLayoutInflater();
        final FriendsListData item = data.get(position);


        cv = infalter.inflate(R.layout.list_friends, parent, false);
        final RelativeLayout rl = (RelativeLayout) cv.findViewById(R.id.rl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LoadingAlertDialog alert=new LoadingAlertDialog(activity);
                alert.show();
                StringCombo sc=new StringCombo(ParseUser.getCurrentUser().getObjectId(),item.getUserId() );
                ParseQuery<ParseObject> query=new ParseQuery<>(PC.KEY_CHAT_OBJECT);
                query.whereEqualTo(PC.KEY_CHAT_UNIQUE_ID, sc.getEncryptedValue());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        alert.dismiss();
                        if (list != null) {
                            if (list.size()!=0) {
                                Intent in = new Intent(activity, ChatActivity.class);
                                PC.setCurrentChat(list.get(0));
                                activity.startActivity(in);
                            }else {
                                Intent in = new Intent(activity, ChatActivity.class);
                                in.putExtra("OBJECTID", item.getUserId());
                                activity.startActivity(in);
                            }
                        } else {
                            Intent in = new Intent(activity, ChatActivity.class);
                            in.putExtra("OBJECTID", item.getUserId());
                            activity.startActivity(in);
                        }
                    }
                });

            }
        });
        final ImageView profileImg = (ImageView) cv.findViewById(R.id.profImg);
        final TextView username = (TextView) cv.findViewById(R.id.username);
        final TextView userstatus = (TextView) cv.findViewById(R.id.userstatus);
        final ImageView onlineIcon = (ImageView) cv.findViewById(R.id.onlineicon);
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.getInBackground(item.getUserId(), new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                if (user != null) {
                    username.setText(user.getString(PC.KEY_DISPLAY_NAME));
                    ParseFile file = user.getParseFile(PC.KEY_PROFILE_PHOTO);
                    if (file == null) {
                        Picasso.with(activity).load(PC.KEY_DEFAULT_PROFILE_PHOTO_LINK).fit().centerCrop().transform(new CircleTransform()).placeholder(R.drawable.default_profile_photo_circle).into(profileImg);
                    } else {
                        Picasso.with(activity).load(file.getUrl()).fit().centerCrop().transform(new CircleTransform()).placeholder(R.drawable.default_profile_photo_circle).into(profileImg);
                    }
                    Calendar cal = Calendar.getInstance();
                    long diff = cal.getTimeInMillis() - user.getDate(PC.KEY_LAST_ONLINE).getTime();
                    long diffinsec = diff / 1000 % 60;
                    if (diffinsec < 30) {
                        onlineIcon.setImageResource(R.drawable.ic_online);
                    } else {
                        onlineIcon.setImageResource(R.drawable.ic_offline);
                    }
                    final Handler h = new Handler();
                    final int delay2 = 6000;
                    h.postDelayed(new Runnable() {
                        public void run() {
                            Calendar cal = Calendar.getInstance();
                            long diff = cal.getTimeInMillis() - user.getDate(PC.KEY_LAST_ONLINE).getTime();
                            long diffinsec = diff / 1000 % 60;
                            if (diffinsec < 30) {
                                onlineIcon.setImageResource(R.drawable.ic_online);
                            } else {
                                onlineIcon.setImageResource(R.drawable.ic_offline);
                            }
                            h.postDelayed(this, delay2);
                        }
                    }, delay2);
                    userstatus.setText(user.getString(PC.KEY_USER_STATUS));
                }
            }
        });


        return cv;
    }
}
