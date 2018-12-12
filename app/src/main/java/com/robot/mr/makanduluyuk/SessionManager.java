package com.robot.mr.makanduluyuk;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "hasdajg";
    public static final String LOGIN = "logged";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String name, String dob, String jenisKelamin){
        editor.putBoolean(LOGIN, true);
        editor.putString(Config.NAME, name);
        editor.putString(Config.DOB, dob);
        editor.putString(Config.JENIS_KELAMIN, jenisKelamin);
        editor.commit();
    }

    public HashMap<String, String> getUsers(){
        HashMap<String, String> user = new HashMap<>();
        user.put(Config.NAME, sharedPreferences.getString(Config.NAME, null));
        user.put(Config.DOB, sharedPreferences.getString(Config.DOB, null));
        user.put(Config.JENIS_KELAMIN, sharedPreferences.getString(Config.JENIS_KELAMIN, null));
        return user;
    }

    public boolean checkLogin(){
        if (!this.sharedPreferences.getBoolean(LOGIN, false)) return false;
        else return true;
    }
}
