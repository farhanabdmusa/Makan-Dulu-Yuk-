package com.robot.mr.makanduluyuk;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

public class InputKegiatan extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText eTanggal;
    private RadioGroup jenis;
    Calendar cAlarm;

    boolean status = true;
    int jamMulai,jamAkhir, minMulai, minAkhir, jamSekarang, minSekarang, tglSekarang, blnSekarang, thnSekarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kegiatan);

        SessionManager sessionManager = new SessionManager(this);
        final HashMap<String, String> user = sessionManager.getUsers();

        myDB = new DatabaseHelper(this);

        jenis = findViewById(R.id.jenisRadioGroup);

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
                        jamMulai = hourOfDay;
                        minMulai = minutes;
                        cAlarm = Calendar.getInstance();
                        cAlarm.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cAlarm.set(Calendar.MINUTE, minutes);
                        cAlarm.set(Calendar.SECOND, 0);
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
                        jamAkhir = hourOfDay;
                        minAkhir = minutes;

                        if(jamAkhir<jamMulai){
                            eAkhir.setError("Waktu akhir tidak boleh kurang dari waktu mulai");
                            eAkhir.requestFocus();
                            status = false;
                        } else if(jamAkhir==jamMulai && minAkhir<=minMulai){
                            eAkhir.setError("Waktu akhir tidak boleh kurang dari waktu mulai");
                            eAkhir.requestFocus();
                            status = false;
                        } else {
                            eAkhir.setError(null);
                            status = true;
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

                int selectedId = jenis.getCheckedRadioButtonId();
                String aktivitas;
                if(selectedId == R.id.makan_rb)
                    aktivitas = "makan";
                else
                    aktivitas = "lain";

                if(eAkhir.getText().toString().isEmpty()){
                    eAkhir.setError("Waktu akhir harus diisi");
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
                            eAkhir.getText().toString(),
                            user.get(Config.USERNAME));
                    if(aktivitas.equals("makan")){
                        System.out.println("Set alarm : " + cAlarm.get(Calendar.HOUR_OF_DAY) + ":"+ cAlarm.get(Calendar.MINUTE));
                        startAlarm(cAlarm);
                    }
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
        DatePickerDialog picker = new DatePickerDialog(InputKegiatan.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        eTanggal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        if(year<cldr.get(Calendar.YEAR) || monthOfYear<cldr.get(Calendar.MONTH) ||
                                dayOfMonth<cldr.get(Calendar.DAY_OF_MONTH)){
                            eTanggal.setError("Tanggal sudah lewat");
                            eTanggal.requestFocus();
                            status = false;
                        } else {
                            eTanggal.setError(null);
                            status = true;
                        }
                    }
                }, year, month, day);
        picker.show();
    };

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cAlarm.getTimeInMillis(),pendingIntent);
        }
    }
}
