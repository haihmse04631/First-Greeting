package com.example.haihm.firstgreeting.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.haihm.firstgreeting.R;
import com.example.haihm.firstgreeting.message.ChatTab;
import com.example.haihm.firstgreeting.new_feed.NewsFeedTab;
import com.example.haihm.firstgreeting.profile.Profile;
import com.example.haihm.firstgreeting.video_call.VideoCallTab;

public class FirstGreetingMain extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private Intent intent;
    private Bundle bund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_greeting_main);
        overridePendingTransition(R.anim.side_in_right, R.anim.side_out_left);

        intent = getIntent();
        bund = intent.getBundleExtra("MyPackage");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        createTabIcons();

    }

    private void createTabIcons() {
        RelativeLayout videoCall = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_videocall, null);
        tabLayout.getTabAt(0).setCustomView(videoCall);
        RelativeLayout newsfeed = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_news_feed, null);
        tabLayout.getTabAt(1).setCustomView(newsfeed);
        RelativeLayout chat = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_chat, null);
        tabLayout.getTabAt(2).setCustomView(chat);
        RelativeLayout profile = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_profile, null);
        tabLayout.getTabAt(3).setCustomView(profile);
    }

    @Override
    public void onBackPressed() {
        // do nothing.
        Intent  intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_greeting_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(FirstGreetingMain.this, SettingActivity.class);
            intent.putExtra("MyPackage", bund);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    VideoCallTab videoCallTab = new VideoCallTab();
                    videoCallTab.setArguments(bund);
                    return videoCallTab;
                case 1:
                    NewsFeedTab newsFeedTab = new NewsFeedTab();
                    newsFeedTab.setArguments(bund);
                    return newsFeedTab;
                case 2:
                    ChatTab chatTab = new ChatTab();
                    chatTab.setArguments(bund);
                    return chatTab;
                case 3:
                    Profile profileTab = new Profile();
                    profileTab.setArguments(bund);
                    return profileTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Video Call";
                case 1:
                    return "News Feed";
                case 2:
                    return "ChatTab";
                case 3:
                    return "Profile";
            }
            return null;
        }
    }
}
