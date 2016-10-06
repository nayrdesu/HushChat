package com.ansoft.chatapp;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ansoft.chatapp.Constants.PC;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FriendsActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        if (ParseUser.getCurrentUser()==null){
            Intent in=new Intent(getApplicationContext(), LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(in);
            finish();
        }else {
            if (isInternetAvailable()) {
                Calendar cal = Calendar.getInstance();
                ParseUser.getCurrentUser().put(PC.KEY_LAST_ONLINE, cal.getTime());
                ParseUser.getCurrentUser().saveInBackground();
                final Handler h = new Handler();
                final int delay2 = 30000;
                h.postDelayed(new Runnable() {
                    public void run() {
                        Calendar cal = Calendar.getInstance();
                        ParseUser.getCurrentUser().put(PC.KEY_LAST_ONLINE, cal.getTime());
                        ParseUser.getCurrentUser().saveInBackground();
                        h.postDelayed(this, delay2);
                    }
                }, delay2);
                addBtn = (FloatingActionButton) findViewById(R.id.fab);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(), AddFriendActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(in);
                    }
                });
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(ParseUser.getCurrentUser().getString(PC.KEY_DISPLAY_NAME));
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                viewPager = (ViewPager) findViewById(R.id.viewpager);
                setupViewPager(viewPager);

                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
            }else {
                Intent in=new Intent(FriendsActivity.this, NoConnection.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_upload_photo:
                startActivity(new Intent(FriendsActivity.this, UploadPhotoActivity.class));
                break;

            case R.id.action_update_status:
                startActivity(new Intent(FriendsActivity.this, UpdateStatusActivity.class));
                break;

            case R.id.action_create_group:
                startActivity(new Intent(FriendsActivity.this, CreateGroupActivity.class));
                break;
        }
        return true;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friends, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "FRIENDS");
        adapter.addFragment(new ChatFragment(), "CHAT");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
