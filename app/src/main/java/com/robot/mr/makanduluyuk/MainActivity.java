package com.robot.mr.makanduluyuk;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private static final String TAG = "MainActivity";

    private ArrayList<String> mNama = new ArrayList<>();
    private ArrayList<String> mJamWal = new ArrayList<>();
    private ArrayList<String> mJamKhir = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        Log.d(TAG, "onCreate: started.");

        FloatingActionButton tambah = findViewById(R.id.floatingActionButton);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAgung();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initKegiatan();
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

    public void openActivityAgung(){
        Intent intent = new Intent(MainActivity.this, InputKegiatan.class);
        startActivity(intent);
    }
}