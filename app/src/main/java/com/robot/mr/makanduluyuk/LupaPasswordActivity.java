package com.robot.mr.makanduluyuk;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LupaPasswordActivity extends AppCompatActivity {

    EditText lupa_username, dob, lupa_jawaban;
    Spinner pertanyaanSpiner;
    Button validate, calendar;
    String checkPertanyaan;
    boolean status = true;
    Calendar cal;
    private DatePickerDialog.OnDateSetListener dlistener;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        lupa_username = findViewById(R.id.lupa_username);
        dob = findViewById(R.id.lupa_dob);
        calendar = findViewById(R.id.lupa_calendar);
        lupa_jawaban = findViewById(R.id.lupa_jawaban);
        pertanyaanSpiner = findViewById(R.id.lupa_pertanyaan);
        validate = findViewById(R.id.check_username);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        dlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                year = y;
                month = m+1;
                if (d<10){
                    dob.setText(year+"-"+month+"-0"+d);
                } else {
                    day = d;
                    dob.setText(year+"-"+month+"-"+day);
                }

            }
        };

        pertanyaanSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPertanyaan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lupa_username.getText().toString().isEmpty()){
                    lupa_username.setError("Username tidak boleh kosong");
                    lupa_username.requestFocus();
                    status = false;
                }
                if (dob.getText().toString().isEmpty()){
                    dob.setError("Tanggal Lahir tidak boleh kosong");
                    dob.requestFocus();
                    status = false;
                }
                if (lupa_jawaban.getText().toString().isEmpty()){
                    lupa_jawaban.setError("Jawaban tidak boleh kosong");
                    lupa_jawaban.requestFocus();
                    status = false;
                }
                if (status){
                    validate.setEnabled(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CHECK_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString(Config.JSON_RESPONSE).equals("Success")) {
                                            finish();
                                            Intent intent = new Intent(LupaPasswordActivity.this, ResetPasswordActivity.class);
                                            intent.putExtra(Config.USERNAME, lupa_username.getText().toString());
                                            startActivity(intent);
                                        } else{
                                            Toast.makeText(LupaPasswordActivity.this, "Gagal : " +
                                                    obj.getString(Config.JSON_RESPONSE), Toast.LENGTH_SHORT)
                                                    .show();
                                            validate.setEnabled(true);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        validate.setEnabled(true);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    validate.setEnabled(true);
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put(Config.USERNAME, lupa_username.getText().toString());
                            params.put(Config.DOB, dob.getText().toString());
                            params.put(Config.PERTANYAAN, checkPertanyaan);
                            params.put(Config.JAWABAN, lupa_jawaban.getText().toString());
                            return params;
                        }
                    };
                    Volley.newRequestQueue(LupaPasswordActivity.this).add(stringRequest);
                }
            }
        });
    }

    private void chooseDate(){
        cal = Calendar.getInstance(Locale.getDefault());
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(LupaPasswordActivity.this,
                dlistener, year, month, day);
        datePickerDialog.show();
    };
}
