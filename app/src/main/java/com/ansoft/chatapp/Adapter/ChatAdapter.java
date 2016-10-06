package com.ansoft.chatapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ansoft.chatapp.ChatActivity;
import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Data.ChatlistData;
import com.ansoft.chatapp.R;
import com.ansoft.chatapp.Utils.CircleTransform;
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
import java.util.Date;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    ArrayList<ChatlistData> datas;
    Activity activity;

    public ChatAdapter(ArrayList<ChatlistData> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {
        cv = activity.getLayoutInflater().inflate(R.layout.list_chats, parent, false);
        final ChatlistData item = datas.get(position);
        final RelativeLayout rl = (RelativeLayout) cv.findViewById(R.id.rl);
        if (item.isSeen()) {
            rl.setBackgroundResource(R.drawable.bg_parent_rounded_corner);
        } else {
            rl.setBackgroundResource(R.drawable.bg_parent_rounded_corner_notseen);
        }
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, ChatActivity.class);
                PC.setCurrentChat(item.getChat());
                activity.startActivity(in);
            }
        });
        final LinearLayout groupProfile = (LinearLayout) cv.findViewById(R.id.group_profile);
        final TextView firstLtr = (TextView) cv.findViewById(R.id.tvFirstLetter);
        final TextView othersName = (TextView) cv.findViewById(R.id.username);
        final ImageView profImg = (ImageView) cv.findViewById(R.id.profImgforReal);
        final TextView lastMsg = (TextView) cv.findViewById(R.id.userstatus);
        final TextView timeStamp = (TextView) cv.findViewById(R.id.timeStamp);
        lastMsg.setText(item.getLastMsg());
        timeStamp.setText(item.getTimeStamp());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                update(position, rl, timeStamp, lastMsg);
                handler.postDelayed(this, 15 * 1000);
            }
        }, 15 * 1000);
        if (!item.isGroup()) {
            groupProfile.setVisibility(View.GONE);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(item.getOtherId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    othersName.setText(user.getString(PC.KEY_DISPLAY_NAME));
                    ParseFile file = user.getParseFile(PC.KEY_PROFILE_PHOTO);
                    if (file == null) {
                        Picasso.with(activity).load(PC.KEY_DEFAULT_PROFILE_PHOTO_LINK).fit().centerCrop().transform(new CircleTransform()).placeholder(R.drawable.default_profile_photo_circle).into(profImg);
                    } else {
                        Picasso.with(activity).load(file.getUrl()).fit().centerCrop().transform(new CircleTransform()).placeholder(R.drawable.default_profile_photo_circle).into(profImg);

                    }
                }
            });
        } else {
            profImg.setVisibility(View.GONE);
            othersName.setText(item.getGroupName());
            String first = String.valueOf(item.getGroupName().charAt(0));
            firstLtr.setText(first.toUpperCase());

        }
        return cv;
    }

    public void update(final int s, final RelativeLayout rl, final TextView timeStamp, final TextView lastMsg) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PC.KEY_CHAT_OBJECT);
        List list = new ArrayList();
        list.add(ParseUser.getCurrentUser().getObjectId());
        query.whereContainedIn(PC.KEY_CHAT_BETWEEN, list);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null) {
                    ParseObject chat = list.get(s);
                    if (chat.getBoolean(PC.KEY_CHAT_IS_SEEN)) {
                        rl.setBackgroundResource(R.drawable.bg_parent_rounded_corner);
                    } else {
                        Date date = chat.getUpdatedAt();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        CharSequence timeego = DateUtils.getRelativeTimeSpanString(cal.getTimeInMillis(),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                        timeStamp.setText(timeego.toString());
                        List<String> msgs = chat.getList(PC.KEY_CHAT_MESSAGES);
                        if (chat.getBoolean(PC.KEY_CHAT_IS_GROUP)) {
                            lastMsg.setText(chat.getString(PC.KEY_CHAT_LAST_MSG_SENDER_NAME) + " - " + msgs.get(msgs.size() - 1).split("-")[0]);
                        } else {
                            lastMsg.setText(msgs.get(msgs.size() - 1).split("-")[0]);
                        }
                        if (chat.getString(PC.KEY_CHAT_LAST_MSGID).equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())) {
                            rl.setBackgroundResource(R.drawable.bg_parent_rounded_corner);
                        } else {
                            rl.setBackgroundResource(R.drawable.bg_parent_rounded_corner_notseen);
                        }

                    }
                }
            }
        });
    }
}
