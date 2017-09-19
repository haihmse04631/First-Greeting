package com.example.haihm.firstgreeting.new_feed;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.haihm.firstgreeting.R;

/**
 * Created by DuyNguyen on 9/19/2017.
 */

public class CommentDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_comment_alert, null);
        return view;
    }
}
