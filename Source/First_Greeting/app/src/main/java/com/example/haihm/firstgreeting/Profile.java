package com.example.haihm.firstgreeting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by haihm on 8/8/2017.
 */

public class Profile extends Fragment {

    Button btnLogOut;
    TextView tvUserName;
    ImageView imgCover, imgAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_tab, container, false);

        btnLogOut = (Button) rootView.findViewById(R.id.btnLogOut);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        imgAvatar = (ImageView) rootView.findViewById(R.id.imgAvatar);
        imgCover = (ImageView) rootView.findViewById(R.id.imgCover);

        tvUserName.setText(getArguments().getString("fbName"));
        String fbImage = getArguments().getString("fbImage");
        Picasso.with(getApplicationContext()).load(fbImage).into(imgAvatar);
        String fbCover = getArguments().getString("fbCover");
        Picasso.with(getApplicationContext()).load(fbCover).into(imgCover);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return rootView;
    }

    private void goLoginScreen(){
        getActivity().finish();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}

