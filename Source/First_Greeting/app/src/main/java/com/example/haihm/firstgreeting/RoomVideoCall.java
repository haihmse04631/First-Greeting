package com.example.haihm.firstgreeting;

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

public class RoomVideoCall extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener {

    Bundle bund;
    Intent intent;
    ImageButton btnOpenSetting, btnBackToChatRoom;


    private static String API_KEY = "";
    private static String SESSION_ID = "";
    private static String TOKEN = "";
    private static final String LOG_TAG = RoomVideoCall.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    public static Session mSession;
    public static Publisher mPublisher;
    public static Subscriber mSubscriber1;
    public static Subscriber mSubscriber2;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer1;
    private FrameLayout mSubscriberViewContainer2;

    private String fbName1;
    private String fbName2;
    private String fbName3;
    private String room;

    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView roomNumber;

    private DatabaseReference mData;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_video_call);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_room_call);

        mData = FirebaseDatabase.getInstance().getReference();
        btnOpenSetting = (ImageButton) findViewById(R.id.btnOpenSetting);

        actionDialog();

        if (mSocket == null) {
            mSocket = VideoCall.mSocket;
        }
        name1 = (TextView) findViewById(R.id.tvUserChat1);
        name2 = (TextView) findViewById(R.id.tvUserChat2);
        name3 = (TextView) findViewById(R.id.tvUserChat3);
        roomNumber = (TextView) findViewById(R.id.tvRoomNumber);

        mPublisherViewContainer = (FrameLayout) findViewById(R.id.frUser1);
        mSubscriberViewContainer1 = (FrameLayout) findViewById(R.id.frUser2);
        mSubscriberViewContainer2 = (FrameLayout) findViewById(R.id.frUser3);

        intent = getIntent();
        bund = intent.getBundleExtra("UserInfo");

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

//            mSocket.on("resp", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    String data = args[0].toString();
//                    Log.e("respon: ", data);
//                }
//            });

            getData();

//            mSocket.on("return-session-id", returnSessionId);
        } else {
            loadExistData();
        }
    }

    private void getData() {
        API_KEY = "45961352";
        mSocket.on("return-name1", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                fbName1 = args[0].toString();
                name1.setText(fbName1);
            }
        });
        mSocket.on("return-name2", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                fbName2 = args[0].toString();
                name2.setText(fbName2);
            }
        });
        mSocket.on("return-name3", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                fbName3 = args[0].toString();
                name3.setText(fbName3);
            }
        });
        mSocket.on("return-room", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                room = args[0].toString();
                roomNumber.setText("Room " + (room + 1));
            }
        });
        mSocket.on("return-session-id", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SESSION_ID = args[0].toString();
                Log.e("session: ", SESSION_ID);
            }
        });
        mSocket.on("return-token-id", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                TOKEN = args[0].toString();
                Log.e("session: ", TOKEN);
                requestPermissions();
            }
        });


    }

    public void loadExistData() {
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

    ArrayList<RoomMembers> roomList;
    RoomMemberAdapter roomAdapter;
    ListView lvRoom;
    int click = 0;

    Dialog dialog;

    private void actionDialog() {
        btnOpenSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(RoomVideoCall.this);
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
//                ImageButton btnBackToChatRoom = (ImageButton) dialog.findViewById(R.id.btnBackToChatRoom);
//                btnBackToChatRoom.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.cancel();
//                    }
//                });

                lvRoom = dialog.findViewById(R.id.lvListRoomChat);
                roomList = new ArrayList<RoomMembers>();
                roomAdapter = new RoomMemberAdapter(dialog.getContext(), R.layout.row_list_room_chat, roomList);
                lvRoom.setAdapter(roomAdapter);

                mSocket.emit("get-info-rooms", "đã nhận req");
                mSocket.on("return-info", returnInfoRooms);

                dialog.show();
            }
        });
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
                    String userName1 = "";
                    String userName2 = "";
                    String userName3 = "";
                    final JSONArray data = (JSONArray) args[0];
                    JSONObject aRoomObj = null;
                    try {
                        while (true) {
                            aRoomObj = data.getJSONObject(index);
                            roomNumber = aRoomObj.getString("roomNumber");
                            if (roomNumber.isEmpty()) {
                                break;
                            }
                            userName1 = aRoomObj.getString("userName1");
                            userName2 = aRoomObj.getString("userName2");
                            userName3 = aRoomObj.getString("userName3");

                            userId1 = aRoomObj.getString("userId1");
                            userId2 = aRoomObj.getString("userId2");
                            userId3 = aRoomObj.getString("userId3");

                            RoomMembers aRoom = new RoomMembers(userName1, userName2, userName3);
                            Log.e("DAta: ", aRoom.toString());
                            roomList.add(aRoom);
                            Log.e("DAta: ", roomNumber);
                            index++;
                        }
                    } catch (JSONException e) {

                        Log.e("data", "Error!!!!!!!!!");
                    }


                    roomAdapter.notifyDataSetChanged();
                }
            });
        }
    };

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
                        fbName1 = data.getString("name1");
                        fbName2 = data.getString("name2");
                        fbName3 = data.getString("name3");
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

    public void fetchSessionConnectionData() {
        mPublisher = null;
        mSubscriber1 = null;
        mSubscriber2 = null;
        mSession = new Session.Builder(RoomVideoCall.this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(RoomVideoCall.this);
        mSession.connect(TOKEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
//        if (!dialog.isShowing()) {
//            Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_LONG).show();
//        }

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

            if (getParent() == null) {
                setResult(Activity.RESULT_OK, data);
            } else {
                getParent().setResult(Activity.RESULT_OK, data);
            }
        }
        if (mPublisherViewContainer != null) mPublisherViewContainer.removeAllViews();
        if (mSubscriberViewContainer1 != null) mSubscriberViewContainer1.removeAllViews();
        if (mSubscriberViewContainer2 != null) mSubscriberViewContainer2.removeAllViews();
        finish();
    }
}
