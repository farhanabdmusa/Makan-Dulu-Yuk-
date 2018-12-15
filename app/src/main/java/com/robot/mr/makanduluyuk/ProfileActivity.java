package com.robot.mr.makanduluyuk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    HashMap<String, String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager sessionManager = new SessionManager(this);
        user = sessionManager.getUsers();

        TextView username = findViewById(R.id.profile_username);
        TextView nama = findViewById(R.id.profile_nama);
        TextView jenisKelamin = findViewById(R.id.profile_jenisKelamin);
        TextView dob = findViewById(R.id.profile_tanggalLahir);

        username.setText(user.get(Config.USERNAME));
        nama.setText(user.get(Config.NAME));
        jenisKelamin.setText(user.get(Config.JENIS_KELAMIN));
        dob.setText(user.get(Config.DOB));
    }
}
