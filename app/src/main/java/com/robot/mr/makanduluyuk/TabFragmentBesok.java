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

public class TabFragmentBesok extends Fragment {
    DatabaseHelper myDB;

    private static final String TAG = "MainActivity";

    private ArrayList<String> mNama = new ArrayList<>();
    private ArrayList<String> mJamWal = new ArrayList<>();
    private ArrayList<String> mJamKhir = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_besok_tab, container, false);
        myDB = new DatabaseHelper(getContext());

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_besok);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNama, mJamWal, mJamKhir);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        initKegiatan();
    }

    private void initKegiatan(){
        mNama.clear();
        mJamWal.clear();
        mJamKhir.clear();
        Cursor res = myDB.getDataBesok();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            mNama.add(res.getString(1));
            mJamWal.add(res.getString(3));
            mJamKhir.add(res.getString(4));
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
