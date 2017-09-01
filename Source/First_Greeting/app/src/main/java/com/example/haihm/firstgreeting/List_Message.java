package com.example.haihm.firstgreeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class List_Message extends AppCompatActivity {

    private ImageButton btnBackToNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__message);
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);

        btnBackToNewsFeed = (ImageButton) findViewById(R.id.btnBackToNewsFeed);

        btnBackToNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List_Message.this, FirstGreetingMain.class);
                startActivity(intent);
            }
        });

    }
}
