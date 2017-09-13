package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RoomVideoCall extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener {

    TextView txtInput;
    Button btnSend;
    Bundle bund;
    Intent intent;

    private static String API_KEY = "";
    private static String SESSION_ID = "";
    private static String TOKEN = "";
    private static final String LOG_TAG = RoomVideoCall.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber1;
    private Subscriber mSubscriber2;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer1;
    private FrameLayout mSubscriberViewContainer2;

    public void fetchSessionConnectionData() {

        mSession = new Session.Builder(RoomVideoCall.this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(RoomVideoCall.this);
        mSession.connect(TOKEN);

    }

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://192.168.0.17:3000");
        } catch (URISyntaxException e) {
        }
    }

    private Emitter.Listener returnSessionId = new Emitter.Listener() {

        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String session;
                    String token;

                    try {
                        session = data.getString("sessionId");
                        token = data.getString("token");

                        Toast.makeText(getApplicationContext(), session, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                        API_KEY = "45956802";
                        SESSION_ID = session;
                        TOKEN = token;

                        requestPermissions();

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_video_call);

        intent = getIntent();
        bund = intent.getBundleExtra("UserInfo");
        mSocket.connect();

//        mSocket.on("server-send", onNewMessage_DangKyUN);

        mSocket.emit("client-send", "Successful!");
        JSONObject user = new JSONObject();
        try {
            user.put("fbId", bund.getString("fbId"));
            user.put("fbType", bund.getString("fbType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("get-session-id", user);
        mSocket.on("return-session-id", returnSessionId);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout) findViewById(R.id.frUser1);
            mSubscriberViewContainer1 = (FrameLayout) findViewById(R.id.frUser2);
            mSubscriberViewContainer2 = (FrameLayout) findViewById(R.id.frUser3);

            // initialize and connect to the session
            fetchSessionConnectionData();

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }


// SessionListener methods

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }


    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber1 == null) {
            mSubscriber1 = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber1);
            mSubscriberViewContainer1.addView(mSubscriber1.getView());
        }
        if (mSubscriber2 == null) {
            mSubscriber2 = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber2);
            mSubscriberViewContainer2.addView(mSubscriber2.getView());
        }
    }


    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber1 != null) {
            mSubscriber1 = null;
            mSubscriberViewContainer1.removeAllViews();
        }
        if (mSubscriber2 != null) {
            mSubscriber2 = null;
            mSubscriberViewContainer2.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

// PublisherListener methods

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }

}
