package com.ansoft.chatapp.Adapter;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ansoft.chatapp.Data.ChatData;
import com.ansoft.chatapp.R;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 1/30/2016.
 */
public class ChatListAdapter extends BaseAdapter{
    Activity activity;
    ArrayList<ChatData> chatDatas;

    public ChatListAdapter(Activity activity, ArrayList<ChatData> chatDatas) {
        this.activity = activity;
        this.chatDatas = chatDatas;
    }

    @Override
    public int getCount() {
        return chatDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return chatDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();
        final ChatData item=chatDatas.get(position);
        if (!item.isPhoto()){
            if (item.getSenderId().equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId().toString())){

                cv=inflater.inflate(R.layout.list_chat_receiver, parent, false);
            }else {
                cv=inflater.inflate(R.layout.list_chat_sender, parent, false);
                TextView senderName=(TextView)cv.findViewById(R.id.tvSenderName);
                if (item.isGroup()){
                    senderName.setVisibility(View.VISIBLE);
                    senderName.setText(item.getSenderName());
                }
            }

            TextView msg=(TextView)cv.findViewById(R.id.txtMsg);
            TextView timeStamp=(TextView)cv.findViewById(R.id.txtTimeStamp);
            msg.setText(item.getMsg());
            timeStamp.setText(item.getTimeStamp());
        }else {
            if (item.getSenderId().equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId().toString())){
                cv=inflater.inflate(R.layout.list_chat_receiver_photo, parent, false);
            }else {
                cv=inflater.inflate(R.layout.list_chat_sender_photo, parent, false);
                TextView senderName=(TextView)cv.findViewById(R.id.tvSenderName);
                if (item.isGroup()){
                    senderName.setVisibility(View.VISIBLE);
                    senderName.setText(item.getSenderName());
                }
            }

            TextView timeStamp=(TextView)cv.findViewById(R.id.txtTimeStamp);
            ImageView photoImg=(ImageView)cv.findViewById(R.id.sentImage);
            timeStamp.setText(item.getTimeStamp());
            Picasso.with(activity).load(item.getPhotoLink()).fit().centerCrop().placeholder(R.drawable.default_profile_photo).into(photoImg);
        }

        return cv;
    }


}
