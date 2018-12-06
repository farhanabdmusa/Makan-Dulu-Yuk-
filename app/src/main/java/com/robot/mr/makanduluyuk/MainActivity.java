package com.robot.mr.makanduluyuk;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton tambah = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAgung();
            }
        });
    }

    public void openActivityAgung(){
        Intent intent = new Intent(this, InputKegitatan);
        startActivity(intent);
    }
}