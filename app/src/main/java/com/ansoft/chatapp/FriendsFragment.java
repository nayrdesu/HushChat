package com.ansoft.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ansoft.chatapp.Adapter.FriendListAdapter;
import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Data.FriendList;
import com.ansoft.chatapp.Data.FriendsListData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    ListView list;
    ParseUser currentUser;
    FriendListAdapter adapter;
    List<String> friendslist;
    ArrayList<FriendsListData> data;


    public FriendsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }


    public void getFriends() {
        for (int i = 0; i < friendslist.size(); i++) {
            FriendsListData d = new FriendsListData();
            d.setUserId(friendslist.get(i).toString());
            data.add(d);
        }
        adapter = new FriendListAdapter(data, getActivity());
        list.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cv = inflater.inflate(R.layout.fragment_friends, container, false);
        list = (ListView) cv.findViewById(R.id.listView);
        currentUser = ParseUser.getCurrentUser();
        data = new ArrayList<>();
        friendslist = currentUser.getList(PC.KEY_FRIENDS_ID);
        if (friendslist!=null){
        getFriends();}
        return cv;
    }


}
