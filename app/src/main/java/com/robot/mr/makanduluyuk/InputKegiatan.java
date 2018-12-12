package com.robot.mr.makanduluyuk;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class InputKegiatan extends AppCompatActivity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kegiatan);

        myDB = new DatabaseHelper(this);

        TextView judul = (TextView) findViewById(R.id.judul);

        TextView nama_tv = (TextView) findViewById(R.id.nama_tv);
        final EditText eNama = (EditText) findViewById(R.id.nama_et);

        TextView tanggal_tv = (TextView) findViewById(R.id.tanggal_tv);
        final EditText eTanggal = (EditText) findViewById(R.id.tanggal_et);
        eTanggal.setInputType(InputType.TYPE_NULL);
        eTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(InputKegiatan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eTanggal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        TextView mulai_tv = (TextView) findViewById(R.id.mulai_tv);
        final EditText eMulai = (EditText) findViewById(R.id.mulai_et);
        eMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(InputKegiatan.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        TextView akhir_tv = (TextView) findViewById(R.id.akhir_tv);
        final EditText eAkhir = (EditText) findViewById(R.id.akhir_et);
        eAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(InputKegiatan.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertData(eNama.getText().toString(),
                                                     eTanggal.getText().toString(),
                                                     eMulai.getText().toString(),
                                                     eAkhir.getText().toString());
                if(isInserted = true){
                    Toast.makeText(InputKegiatan.this, "Data telah disimpan", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(InputKegiatan.this, "Data tidak tersimpan", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
