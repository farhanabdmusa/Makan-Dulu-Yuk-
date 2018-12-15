package com.robot.mr.makanduluyuk.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robot.mr.makanduluyuk.R;
import com.robot.mr.makanduluyuk.SessionManager;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    HashMap<String, String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager sessionManager = new SessionManager(this);
        user = sessionManager.getUsers();
        
        findViewById(R.id.profile_username)
    }
}
