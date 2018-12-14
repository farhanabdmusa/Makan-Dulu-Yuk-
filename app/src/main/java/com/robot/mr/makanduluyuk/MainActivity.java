package com.robot.mr.makanduluyuk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

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

        AppCompatButton notif = findViewById(R.id.notifButton);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationCall();
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view_sekarang);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, mNama, mJamWal, mJamKhir);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void openActivityAgung(){
        Intent intent = new Intent(MainActivity.this, InputKegiatan.class);
        startActivity(intent);
    }

    public void notificationCall(){
        Intent intent = new Intent(this, InputKegiatan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent snoozeIntent = new Intent(this, InputKegiatan.class);
        snoozeIntent.setAction(Intent.ACTION_WEB_SEARCH);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Hallo, im notification")
                .setContentText("Click Me!")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_person_outline_black_24dp)
                // Set the intent that will fire when the user taps the notification
                .addAction(R.drawable.ic_date_range_black_24dp, "SNOOZE", pendingIntent)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}