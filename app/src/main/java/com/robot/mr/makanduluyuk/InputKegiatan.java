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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class InputKegiatan extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText eTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kegiatan);

        myDB = new DatabaseHelper(this);

        eTanggal = findViewById(R.id.tanggal_et);

        final EditText eNama = findViewById(R.id.nama_et);

        eTanggal.setInputType(InputType.TYPE_NULL);
        eTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateShow();
            }
        });

        findViewById(R.id.calendar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateShow();
            }
        });

        final EditText eMulai =  findViewById(R.id.mulai_et);
        eMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(InputKegiatan.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(minutes < 10){
                            eMulai.setText(hourOfDay+":0"+minutes);
                        } else {
                            eMulai.setText(hourOfDay+":"+minutes);
                        }
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });

        final EditText eAkhir = findViewById(R.id.akhir_et);
        eAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(InputKegiatan.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(minutes < 10){
                            eAkhir.setText(hourOfDay+":0"+minutes);
                        } else {
                            eAkhir.setText(hourOfDay+":"+minutes);
                        }
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;
                if(eAkhir.getText().toString().isEmpty()){
                    eAkhir.setError("Waktu mulai harus diisi");
                    eAkhir.requestFocus();
                    status = false;
                }
                if(eMulai.getText().toString().isEmpty()){
                    eMulai.setError("Waktu mulai harus diisi");
                    eMulai.requestFocus();
                    status = false;
                }
                if(eTanggal.getText().toString().isEmpty()){
                    eTanggal.setError("Tanggal harus diisi");
                    eTanggal.requestFocus();
                    status = false;
                }
                if(eNama.getText().toString().isEmpty()){
                    eNama.setError("Nama kegiatan harus diisi");
                    eNama.requestFocus();
                    status = false;
                }
                if(status){
                    boolean isInserted = myDB.insertData(eNama.getText().toString(),
                            eTanggal.getText().toString(),
                            eMulai.getText().toString(),
                            eAkhir.getText().toString());
                    if(isInserted){
                        Toast.makeText(InputKegiatan.this, "Data telah disimpan", Toast.LENGTH_LONG).show();
                        finish();
                    } else{
                        Toast.makeText(InputKegiatan.this, "Data tidak tersimpan", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void dateShow(){
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
    };
}
