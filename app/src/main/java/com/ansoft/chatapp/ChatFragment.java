package com.ansoft.chatapp;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ansoft.chatapp.Adapter.ChatAdapter;
import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Data.ChatlistData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {

    ListView listView;
    ParseUser currentUser;
    ChatAdapter adapter;
    ArrayList<ChatlistData> data;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cv=inflater.inflate(R.layout.fragment_chat, container, false);
        listView=(ListView)cv.findViewById(R.id.listView);
        currentUser=ParseUser.getCurrentUser();
        data=new ArrayList<>();
        refresh();
        return cv;
    }

    public void refresh(){
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>(PC.KEY_CHAT_OBJECT);
        List list=new ArrayList();
        list.add(currentUser.getObjectId());
        query.whereContainedIn(PC.KEY_CHAT_BETWEEN, list);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null) {
                        data.clear();
                        for (ParseObject chat : list) {
                            ChatlistData d = new ChatlistData();
                            d.setIsSeen(chat.getBoolean(PC.KEY_CHAT_IS_SEEN));
                            List<String> msgs = chat.getList(PC.KEY_CHAT_MESSAGES);
                            for (int i = 0; i < msgs.size(); i++) {
                                if (i == msgs.size() - 1) {
                                    String[] array = msgs.get(i).split("-");
                                    d.setLastMsg(array[0]);
                                }
                            }
                            List<String> users = chat.getList(PC.KEY_CHAT_BETWEEN);
                            for (String userId : users) {
                                if (!userId.equalsIgnoreCase(currentUser.getObjectId())) {
                                    d.setOtherId(userId);
                                }
                            }
                            if (chat.getBoolean(PC.KEY_CHAT_IS_GROUP)) {
                                d.setIsGroup(true);
                                d.setGroupName(chat.getString(PC.KEY_CHAT_GROUP_NAME));
                            } else {
                                d.setIsGroup(false);
                            }
                            Date date = chat.getUpdatedAt();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            CharSequence timeego = DateUtils.getRelativeTimeSpanString(
                                    cal.getTimeInMillis(),
                                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                            d.setTimeStamp(timeego.toString());
                            d.setChat(chat);
                            data.add(d);
                        }
                        adapter = new ChatAdapter(data, getActivity());
                        listView.setAdapter(adapter);
                    }

            }
        });
    }

}
