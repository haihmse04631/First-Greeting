package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton btnLoginFacebook;
    String fbId, fbName, fbImage, fbCover, fbEmail, fbGender, fbBirthday;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook = findViewById(R.id.login_button);

        checkFacebookLogin();
//        Log.e("email", fbEmail);
//        Log.e("birthday", fbBirthday);
    }

    //Check login already or not
    void checkFacebookLogin() {
        if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
            loadData();
        } else {
            processLogin();
        }
    }

    // Proccessing login by facebook account
    public void processLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Login successed. Move to LoginWithFacebook.class
                        loadData();
                    }

                    @Override
                    public void onCancel() {
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void transfer() {
        Intent intent = new Intent(MainActivity.this, FirstGreetingMain.class);
        Bundle bund = new Bundle();
        bund.putString("fbId", fbId);
        bund.putString("fbName", fbName);
        bund.putString("fbImage", fbImage);
        bund.putString("fbCover", fbCover);
        intent.putExtra("MyPackage", bund);
        startActivity(intent);
        pushFirebase();
        processLogin();
        overridePendingTransition(R.anim.side_in_right, R.anim.side_out_left);
    }

    // Get facebook data
    public void loadData() {
        Bundle params = new Bundle();

        params.putString("fields", "id,name,picture.role(large),cover");
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            String userDetail = response.getRawResponse();
                            FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                            try {
                                JSONObject jsonObject = new JSONObject(userDetail);
                                fbId = jsonObject.getString("id");
                                fbName = jsonObject.getString("name");
                                Log.e("object", jsonObject.toString());
                                fbImage = "https://graph.facebook.com/" + fbId + "/picture?width=960&height=960";
                                if (jsonObject.has("cover")) {
                                    String getInitialCover = jsonObject.getString("cover");

                                    if (getInitialCover.equals("null")) {
                                        jsonObject = null;
                                    } else {
                                        JSONObject JOCover = jsonObject.optJSONObject("cover");

                                        if (JOCover.has("source")) {

                                            fbCover = JOCover.getString("source");
                                        } else {
                                            fbCover = null;
                                        }
                                    }
                                } else {
                                    fbCover = null;
                                }

                                fbEmail = jsonObject.getString("email");
                                fbGender = jsonObject.getString("gender");
                                fbBirthday = jsonObject.getString("birthday");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        transfer();
                    }
                }).executeAsync();
    }

    public void pushFirebase() {
        final Message friends = new Message();
        /* Temporary Data */
//        ArrayList messageList = new ArrayList();
//        messageList.add(new SingleMessage(new Date(), "Hello guy!", fbImage));
//        messageList.add(new SingleMessage(new Date(), "Hello dude!", fbImage));
//        friends.put("1234", messageList);
//        friends.put("2222", messageList);
        /*------------------------------*/

        final User user = new User(fbName, fbImage, fbId, "Member");

        mData = FirebaseDatabase.getInstance().getReference();

        mData.child("User").child(fbId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mData.child("User").child(fbId).setValue(user);

                    mData.child("Message").child(fbId).setValue(friends);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
