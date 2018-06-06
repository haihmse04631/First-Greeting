package com.example.haihm.firstgreeting.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.haihm.firstgreeting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("MyPackage");
        final String id = bundle.getString("fbId");
        final RadioGroup radio = (RadioGroup) findViewById(R.id.grbtn);
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("User").child(id).child("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String role = (String) dataSnapshot.getValue();
                if (role.equals("Member")) {
                    radio.check(R.id.rbtnNewbie);
                } else if (role.equals("Admin")) {
                    radio.clearCheck();
                } else {
                    radio.check(R.id.rbtnMember);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);
                switch (index) {
                    case 0: // first button
                        mData.child("User").child(id).child("role").setValue("Member");
                        Toast.makeText(getApplicationContext(), "Set your role is Newbie", Toast.LENGTH_LONG).show();
                        break;
                    case 1: // secondbutton
                        mData.child("User").child(id).child("role").setValue("OldMember");
                        Toast.makeText(getApplicationContext(), "Set your role is Old member", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }
}
