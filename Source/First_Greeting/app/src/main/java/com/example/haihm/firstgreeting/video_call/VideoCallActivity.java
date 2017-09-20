package com.example.haihm.firstgreeting.video_call;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haihm.firstgreeting.R;
import com.example.haihm.firstgreeting.message.ChatTab;
import com.example.haihm.firstgreeting.message.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallActivity extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener {

    Bundle bund;
    Intent intent;
    ImageButton btnOpenSetting, btnBackToChatRoom;


    private static String API_KEY = "";
    private static String SESSION_ID = "";
    private static String TOKEN = "";
    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    public static Session mSession;
    public static Publisher mPublisher;
    public static Subscriber mSubscriber1;
    public static Subscriber mSubscriber2;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer1;
    private FrameLayout mSubscriberViewContainer2;

    private static String fbId1;
    private static String fbId2;
    private static String fbId3;
    private String fbName1;
    private String fbName2;
    private String fbName3;
    private String fbImg1;
    private String fbImg2;
    private String fbImg3;
    private String room;

    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView roomNumber;

    private DatabaseReference mData;
    private Socket mSocket;

    ArrayList<Room> roomList;
    ListMemberAdapter roomAdapter;
    ListView lvRoom;
    int click = 0;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_video_call);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_room_call);

        init();

        btnOpenSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionDialog();
            }
        });

        if (mSession == null) {
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
        } else {
            loadExistData();
        }
    }

    private void init() {
        mData = FirebaseDatabase.getInstance().getReference();

        if (mSocket == null) {
            mSocket = VideoCallTab.mSocket;
        }

        intent = getIntent();
        bund = intent.getBundleExtra("UserInfo");

        name1 = (TextView) findViewById(R.id.tvUserChat1);
        name2 = (TextView) findViewById(R.id.tvUserChat2);
        name3 = (TextView) findViewById(R.id.tvUserChat3);
        roomNumber = (TextView) findViewById(R.id.tvRoomNumber);

        mPublisherViewContainer = (FrameLayout) findViewById(R.id.frUser1);
        mSubscriberViewContainer1 = (FrameLayout) findViewById(R.id.frUser2);
        mSubscriberViewContainer2 = (FrameLayout) findViewById(R.id.frUser3);

        btnOpenSetting = (ImageButton) findViewById(R.id.btnOpenSetting);
    }

    private void setNames() {
        fbName1 = bund.getString("name");
        fbImg1 = bund.getString("fbImage");

        for (User user : ChatTab.userList) {
            if (user.getId().equals(fbId2)) {
                fbName2 = user.getName();
                fbImg2 = user.getLinkAvatar();
            }
            if (user.getId().equals(fbId3)) {
                fbName3 = user.getName();
                fbImg3 = user.getLinkAvatar();
            }
        }

        name1.setText(fbName1);
        name2.setText(fbName2);
        name3.setText(fbName3);
    }

    private Emitter.Listener returnSessionId = new Emitter.Listener() {
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("data nhan duoc", ": ngon");
                    JSONObject data = (JSONObject) args[0];
                    String session;
                    String token;

                    try {
                        session = data.getString("sessionId");
                        token = data.getString("token");
                        fbId1 = bund.getString("fbId");
                        fbId2 = data.getString("id2");
                        fbId3 = data.getString("id3");

                        setNames();

                        room = data.getString("indexSession");
                        roomNumber.setText("Room " + (room + 1));

                        Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_LONG).show();

                        API_KEY = "45961352";
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

    public void loadExistData() {
        setNames();
        {
            View view = mPublisher.getView();
            view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
            mPublisherViewContainer.addView(view);
        }
        if (mSubscriber1 != null) {
            View view = mSubscriber1.getView();
            view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
            mSubscriberViewContainer1.addView(view);
        }
        if (mSubscriber2 != null) {
            View view = mSubscriber1.getView();
            view.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_video_border));
            mSubscriberViewContainer2.addView(view);
        }
    }


    private void actionDialog() {
        dialog = new Dialog(VideoCallActivity.this);
        dialog.setTitle("Rooms Information");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_setting_room_alert);

        Button btnStart = (Button) dialog.findViewById(R.id.btnStart);

        if (bund.getString("role").equals("Admin")) {
            btnStart.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.GONE);
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DaTa: ", "CLicked! " + click);
                if (click % 2 == 0) {
                    mSocket.emit("start-call", "");
                }
                click++;
            }
        });

        lvRoom = dialog.findViewById(R.id.lvListRoomChat);
        roomList = new ArrayList<Room>();
        roomAdapter = new ListMemberAdapter(dialog.getContext(), R.layout.row_list_room_chat, roomList);
        lvRoom.setAdapter(roomAdapter);

        mSocket.emit("get-info-rooms", "đã nhận req");
        mSocket.on("return-info", returnInfoRooms);

        dialog.show();
    }


    private Emitter.Listener returnInfoRooms = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    roomList.clear();
                    int index = 0;
                    String roomNumber = "";
                    String userId1 = "";
                    String userId2 = "";
                    String userId3 = "";
                    final JSONArray data = (JSONArray) args[0];
                    JSONObject aRoomObj;
                    try {
                        while (true) {
                            aRoomObj = null;
                            try {
                                aRoomObj = data.getJSONObject(index);
                            } catch (Exception ex) {
                                Log.e("Room: ", "no more");
                            }
                            if (aRoomObj == null) {
                                break;
                            }

                            roomNumber = aRoomObj.getString("roomNumber");
                            Log.e("Room : ", roomNumber);

                            try {
                                userId1 = aRoomObj.getString("userId1");
                            } catch (Exception e) {
                                Log.e("Name 1 : ", "Unkown");
                            }

                            try {
                                userId2 = aRoomObj.getString("userId2");
                            } catch (Exception e) {
                                Log.e("Name 2 : ", "Unkown");
                            }

                            try {
                                userId3 = aRoomObj.getString("userId3");
                            } catch (Exception e) {
                                Log.e("Name 3 : ", "Unkown");
                            }

                            Log.e("index : ", Integer.toString(index));
                            Log.e("Name 1 : ", userId1);
                            Log.e("Name 2 : ", userId2);
                            Log.e("Name 3 : ", userId3);

                            Member mem1 = new Member("", "");
                            Member mem2 = new Member("", "");
                            Member mem3 = new Member("", "");

                            if (userId1.equals(fbId1)) {
                                mem1 = new Member(fbName1, fbImg1);
                            } else {
                                for (User user : ChatTab.userList) {
                                    if (userId1.equals(user.getId())) {
                                        mem1 = new Member(user.getName(), user.getLinkAvatar());
                                    }
                                }
                            }

                            if (userId2.equals(fbId1)) {
                                mem2 = new Member(fbName1, fbImg1);
                            } else {
                                for (User user : ChatTab.userList) {
                                    if (userId2.equals(user.getId())) {
                                        mem2 = new Member(user.getName(), user.getLinkAvatar());
                                    }
                                }
                            }

                            if (userId3.equals(fbId1)) {
                                mem3 = new Member(fbName1, fbImg1);
                            } else {
                                for (User user : ChatTab.userList) {
                                    if (userId3.equals(user.getId())) {
                                        mem3 = new Member(user.getName(), user.getLinkAvatar());
                                    }
                                }
                            }

                            Room aRoom = new Room(mem1, mem2, mem3);
//                            Log.e("DAta: ", aRoom.toString());
                            roomList.add(aRoom);

                            index++;
                        }
                    } catch (JSONException e)

                    {
                        Log.e("data", "No more room");
                    }


                    roomAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    public void fetchSessionConnectionData() {
        mPublisher = null;
        mSubscriber1 = null;
        mSubscriber2 = null;
        mSession = new Session.Builder(VideoCallActivity.this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(VideoCallActivity.this);
        mSession.connect(TOKEN);
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
        if (mPublisher != null) {
            VideoCallActivity.mPublisher.destroy();
        }
        mPublisher = null;
        System.gc();
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
                    Log.e("Data: ", "Can't subscribe for view 1!");
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
                    Log.e("Data: ", "Can't subscribe for view 2!");
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

            if (getParent() != null) {
                setResult(Activity.RESULT_OK, data);
            } else {
                getParent().setResult(Activity.RESULT_OK, data);
            }
        }
        if (mPublisherViewContainer != null) mPublisherViewContainer.removeAllViews();
        if (mSubscriberViewContainer1 != null) mSubscriberViewContainer1.removeAllViews();
        if (mSubscriberViewContainer2 != null) mSubscriberViewContainer2.removeAllViews();
        System.gc();
        finish();
    }
}
