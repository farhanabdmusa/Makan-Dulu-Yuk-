package com.robot.mr.makanduluyuk;

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

import java.util.ArrayList;

public class TabFragmentKemarin extends Fragment {
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

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_sekarang);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNama, mJamWal, mJamKhir);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        initKegiatan();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initKegiatan(){
        Log.d(TAG, "initKegiatan: preparing bitmaps");

        mNama.clear();
        mJamWal.clear();
        mJamKhir.clear();
        Cursor res = myDB.getAllData();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            mNama.add(res.getString(1));
            mJamWal.add(res.getString(3));
            mJamKhir.add(res.getString(4));
        }

//        mNama.add("Kegiatan 1");
//        mNama.add("Kegiatan 2");
//        mNama.add("Kegiatan 3");
//        mNama.add("Kegiatan 4");
//        mNama.add("Kegiatan 5");
//        mNama.add("Kegiatan 6");
//        mNama.add("Kegiatan 7");
//        mNama.add("Kegiatan 8");
//        mNama.add("Kegiatan 9");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, mNama, mJamWal, mJamKhir);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
