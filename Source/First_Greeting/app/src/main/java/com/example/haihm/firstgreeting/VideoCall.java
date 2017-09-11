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
//        requestPermissions();
        return rootView;

    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
//    private void requestPermissions() {
//        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            // initialize view objects from your layout
//            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
//            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
//
//
//            // initialize and connect to the session
//            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
//            mSession.setSessionListener((Session.SessionListener) this);
//            mSession.connect(TOKEN);
//
//
//        } else {
//            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
//        }
//    }
//
//    // SessionListener methods
//
//    @Override
//    public void onConnected(Session session) {
//        Log.i(LOG_TAG, "Session Connected");
//
//        mPublisher = new Publisher.Builder(this).build();
//        mPublisher.setPublisherListener(this);
//
//        mPublisherViewContainer.addView(mPublisher.getView());
//        mSession.publish(mPublisher);
//    }
//
//
//    @Override
//    public void onDisconnected(Session session) {
//        Log.i(LOG_TAG, "Session Disconnected");
//    }
//
//    @Override
//    public void onStreamReceived(Session session, Stream stream) {
//        Log.i(LOG_TAG, "Stream Received");
//
//        if (mSubscriber == null) {
//            mSubscriber = new Subscriber.Builder(this, stream).build();
//            mSession.subscribe(mSubscriber);
//            mSubscriberViewContainer.addView(mSubscriber.getView());
//        }
//    }
//
//
//    @Override
//    public void onStreamDropped(Session session, Stream stream) {
//        Log.i(LOG_TAG, "Stream Dropped");
//
//        if (mSubscriber != null) {
//            mSubscriber = null;
//            mSubscriberViewContainer.removeAllViews();
//        }
//    }
//
//    @Override
//    public void onError(Session session, OpentokError opentokError) {
//        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
//    }
//
//    // PublisherListener methods
//
//    @Override
//    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
//        Log.i(LOG_TAG, "Publisher onStreamCreated");
//    }
//
//    @Override
//    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
//        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
//    }
//
//    @Override
//    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
//        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
//    }
}

