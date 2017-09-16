package com.example.haihm.firstgreeting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import org.json.JSONException;
import org.json.JSONObject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RoomVideoCall extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener {

    Bundle bund;
    Intent intent;

    private static String API_KEY = "";
    private static String SESSION_ID = "";
    private static String TOKEN = "";
    private static final String LOG_TAG = RoomVideoCall.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    public static Publisher mPublisher;
    public static Subscriber mSubscriber1;
    public static Subscriber mSubscriber2;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer1;
    private FrameLayout mSubscriberViewContainer2;

    private String fbName1;
    private String fbName2;
    private String fbName3;

    private TextView name1;
    private TextView name2;
    private TextView name3;


    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_video_call);

        mSocket = VideoCall.mSocket;
        name1 = (TextView) findViewById(R.id.tvUser1);
        name2 = (TextView) findViewById(R.id.tvUser2);
        name3 = (TextView) findViewById(R.id.tvUser3);

        intent = getIntent();
        bund = intent.getBundleExtra("UserInfo");

        JSONObject user = new JSONObject();
        try {
            user.put("fbId", bund.getString("fbId"));
            user.put("role", bund.getString("role"));
            user.put("name", bund.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("get-session-id", user);
        mSocket.on("return-session-id", returnSessionId);

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
                        fbName1 = data.getString("name1");
                        fbName2 = data.getString("name2");
                        fbName3 = data.getString("name3");

                        Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_LONG).show();
                        API_KEY = "45956472";
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

    public void fetchSessionConnectionData() {
        mSession = new Session.Builder(RoomVideoCall.this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(RoomVideoCall.this);
        mSession.connect(TOKEN);

        name1.setText(fbName1);
        name2.setText(fbName2);
        name3.setText(fbName3);
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

        mPublisher = new Publisher.Builder(this)
//                .audioTrack(false)
//                .frameRate(Publisher.CameraCaptureFrameRate.FPS_30)
//                .resolution(Publisher.CameraCaptureResolution.MEDIUM)
//                .videoTrack(true)
                .build();
        mPublisher.setPublisherListener(this);
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        View view = mPublisher.getView();
        view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
        mPublisherViewContainer.addView(view);
        mSession.publish(mPublisher);
    }


    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.e("Data", "Stream Received");
        Log.e("Data", session.getCapabilities().toString());

        if (mSubscriber1 == null) {

            Log.e("data: ", "stream 1: " + stream.getName() + " | " + stream.getStreamId());

            mSubscriber1 = new Subscriber.Builder(this, stream).build();
            mSubscriber1.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber1);
            mSubscriber1.setSubscriberListener(new SubscriberKit.SubscriberListener() {
                @Override
                public void onConnected(SubscriberKit subscriberKit) {
                    View view = mSubscriber1.getView();
                    view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
                    mSubscriberViewContainer1.addView(view);
                }

                @Override
                public void onDisconnected(SubscriberKit subscriberKit) {

                }

                @Override
                public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
                    Log.e("Data: ", "Can't subscribe!");
                }
            });


        } else if (mSubscriber2 == null) {

            Log.e("data: ", "stream 2: " + stream.getName() + " | " + stream.getStreamId());

            mSubscriber2 = new Subscriber.Builder(this, stream).build();
            mSubscriber2.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber2);
            mSubscriber2.setSubscriberListener(new SubscriberKit.SubscriberListener() {
                @Override
                public void onConnected(SubscriberKit subscriberKit) {
                    View view = mSubscriber2.getView();
                    view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
                    mSubscriberViewContainer2.addView(view);
                }

                @Override
                public void onDisconnected(SubscriberKit subscriberKit) {

                }

                @Override
                public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
                    Log.e("Data: ", "Can't subscribe!");
                }
            });
        }
    }


    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber1 != null && mSubscriber1.getStream() == stream) {
            mSubscriber1 = null;
            mSubscriberViewContainer1.removeAllViews();
        }
        if (mSubscriber2 != null && mSubscriber2.getStream() == stream) {
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


    // Back button
    @Override
    public void onBackPressed() {
        if (mPublisher != null) {
            Intent data = new Intent();

            if (getParent() == null) {
                setResult(Activity.RESULT_OK, data);
            } else {
                getParent().setResult(Activity.RESULT_OK, data);
            }
        }
        finish();
    }
}
