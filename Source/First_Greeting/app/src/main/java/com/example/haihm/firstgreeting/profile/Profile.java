package com.example.haihm.firstgreeting.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haihm.firstgreeting.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by haihm on 8/8/2017.
 */

public class Profile extends Fragment {

    Button btnLogOut;
    TextView tvUserName, tvEmail, tvGender, tvBirthday;
    ImageView imgCover, imgAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_tab, container, false);

        btnLogOut = (Button) rootView.findViewById(R.id.btnLogOut);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        imgAvatar = (ImageView) rootView.findViewById(R.id.imgAvatar);
        imgCover = (ImageView) rootView.findViewById(R.id.imgCover);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
        tvGender = (TextView) rootView.findViewById(R.id.tvGender);
        tvBirthday = (TextView) rootView.findViewById(R.id.tvBirthday);
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(800);
        final Animation animAvatar = AnimationUtils.loadAnimation(this.getActivity(), R.anim.anim_rotate2);

        tvEmail.setText(getArguments().getString("fbEmail"));
        tvGender.setText(getArguments().getString("fbGender"));
        tvBirthday.setText(getArguments().getString("fbBirthday"));
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

        imgCover.startAnimation(anim);
        imgAvatar.startAnimation(animAvatar);

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
