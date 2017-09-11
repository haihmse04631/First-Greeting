package com.example.haihm.firstgreeting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.opentok.android.Publisher;
import com.opentok.android.Session;
import com.opentok.android.Subscriber;


/**
 * Created by haihm on 8/8/2017.
 */

public class VideoCall extends Fragment {

    private static String API_KEY = "45956472";
    private static String SESSION_ID = "2_MX40NTk1NjQ3Mn5-MTUwNTEyOTYxNDM5MH5OZWlHVzBLVHp1Wks1YmVGeVNnN0FhaGh-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NTk1NjQ3MiZzaWc9YWZmY2NmNDMwMjI4ZDJkYTVlYzY4YTZhZDIzNzNiMWNiMmI2OGE0ODpzZXNzaW9uX2lkPTJfTVg0ME5UazFOalEzTW41LU1UVXdOVEV5T1RZeE5ETTVNSDVPWldsSFZ6QkxWSHAxV2tzMVltVkdlVk5uTjBGaGFHaC1mZyZjcmVhdGVfdGltZT0xNTA1MTI5NjQzJm5vbmNlPTAuNzc0NjcwOTAzMDU0OTk1MiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTA1MTMzMjQyJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_call_tab, container, false);
        return rootView;

    }
}

