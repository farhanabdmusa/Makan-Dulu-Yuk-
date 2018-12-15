package com.robot.mr.makanduluyuk;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class TabFragmentSekarang extends Fragment {
    DatabaseHelper myDB;

    private static final String TAG = "MainActivity";

    private ArrayList<String> mNama = new ArrayList<>();
    private ArrayList<String> mJamWal = new ArrayList<>();
    private ArrayList<String> mJamKhir = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sekarang_tab, container, false);
        myDB = new DatabaseHelper(getContext());
        initKegiatan(getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_sekarang);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNama, mJamWal, mJamKhir);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        initKegiatan(context);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        initKegiatan(getContext());
        super.onResume();
    }

    @Override
    public void onStart() {
        initKegiatan(getContext());
        super.onStart();
    }

    private void initKegiatan(Context context){
        Log.d(TAG, "initKegiatan: preparing bitmaps");

        mNama.clear();
        mJamWal.clear();
        mJamKhir.clear();
        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUsers();
        myDB = new DatabaseHelper(getContext());
        Cursor res = myDB.getDataSekarang(user.get(Config.USERNAME));
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            if(res.getString(5).equals(user.get(Config.USERNAME))) {
                mNama.add(res.getString(1));
                mJamWal.add(res.getString(3));
                mJamKhir.add(res.getString(4));
            }
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
