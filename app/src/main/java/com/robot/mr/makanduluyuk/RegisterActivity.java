 package com.robot.mr.makanduluyuk;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText name;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private RadioGroup gender;
    private Button dateCalendar;
    private EditText dateOfBirth, jawaban;
    private DatePickerDialog.OnDateSetListener tanggal;

    private Button register;
    private Spinner pertanyaanSpinner;
    private String pertanyaan;


     private static final String TAG = "RegisterActivity";
     ProgressDialog progressDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.nameEditText);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.rePasswordEditText);
        gender = findViewById(R.id.genderRadioGroup);
        dateCalendar = findViewById(R.id.calendar);
        dateOfBirth = findViewById(R.id.dateEditText);
        register = findViewById(R.id.daftarButton);
        pertanyaanSpinner = findViewById(R.id.pertanyaan);
        jawaban = findViewById(R.id.jawaban);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        dateCalendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        pertanyaanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pertanyaan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        /*
        String birthDateFormat = DateFormat.getDateInstance().format(cal.getTime());
        */

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(cal.getTime());

        EditText tanggalLahir = findViewById(R.id.dateEditText);
        tanggalLahir.setText(strDate);

    }

    private static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void submitForm() {
        boolean status = true;
        if (name.getText().toString().isEmpty()){
            name.setError("Nama tidak boleh kosong");
            name.requestFocus();
            status = false;
//            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
//            return;
        }

        if (username.getText().toString().isEmpty()){
            username.setError("Username tidak boleh kosong");
            status = false;
//            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
//            return;
        }else  if (username.getText().toString().length() < 6){
            username.setError("Username minimal 6 karakter");
            username.requestFocus();
            status = false;
//            Toast.makeText(this, "Username minimal 6 karakter", Toast.LENGTH_SHORT).show();
//            return;
        }

        if (password.getText().toString().length()<6){
            password.setError("Password minimal 6 karakter, mengandung huruf besar, kecil, angka, dan simbol");
            password.requestFocus();
            status = false;
//            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
//            return;
        }else if (!isValidPassword(password.getText().toString())) {
            password.setError("Password minimal 6 karakter, mengandung huruf besar, kecil, angka, dan simbol");
            password.requestFocus();
            status = false;
//            Toast.makeText(this, "Password tidak valid", Toast.LENGTH_SHORT).show();
//            return;
        }

        if(!confirmPassword.getText().toString().equals(password.getText().toString())){
            confirmPassword.setError("Password tidak sesuai");
            confirmPassword.requestFocus();
            status = false;
//            Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
//            return;
        }

        int selectedId = gender.getCheckedRadioButtonId();
        String userGender;
        if(selectedId == R.id.female_radio_btn)
            userGender = "P";
        else
            userGender = "L";


        if (dateOfBirth.getText().toString().isEmpty()){
            dateOfBirth.setError("Tanggal harus diisi");
            dateOfBirth.requestFocus();
            status = false;
//            Toast.makeText(this, "Isi Tanggal Lahir", Toast.LENGTH_SHORT).show();
//            return;
        }
        if (jawaban.getText().toString().isEmpty()){
            jawaban.setError("Jawaban harus diisi");
            jawaban.requestFocus();
            status = false;
        }

        if(status){
            progressDialog.setMessage("Membuat akun");
            showDialog();
            registerUser(name.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString(),
                    userGender,
                    dateOfBirth.getText().toString(), pertanyaan, jawaban.getText().toString());
        }
    }

    private void registerUser(final String name, final String usernames, final String password,
                              final String gender, final String dob, final String pertanyaan, final String jawaban){
        String cancel_req_tag = "register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.REGISTRATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("Register result : " + obj.getString(Config.JSON_RESPONSE)
                            .equals("Success"));

                    if (obj.getString(Config.JSON_RESPONSE).equals("Success")) {
//                        String user = obj.getString("name");
//                        Toast.makeText(RegisterActivity.this, "Hi " + user +", You are successfully Added!", Toast.LENGTH_SHORT)
//                                .show();
                        finish();
                        hideDialog();
                        // Launch login activity
                    } else {
                        String errorMsg = obj.getString(Config.JSON_RESPONSE);
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                        username.setError(obj.getString(Config.JSON_RESPONSE));
                        username.requestFocus();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Log.i("register","register error");
                    Log.i("register err", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.NAME, name);
                params.put(Config.USERNAME, usernames);
                params.put(Config.PASSWORD, password);
                params.put(Config.JENIS_KELAMIN, gender);
                params.put(Config.DOB, dob);
                params.put(Config.PERTANYAAN, pertanyaan);
                params.put(Config.JAWABAN, jawaban);
                return params;
            }
        };
        // Adding request to request queue
       //AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        Volley.newRequestQueue(RegisterActivity.this).add(strReq);
    }

     private void showDialog() {
         if (!progressDialog.isShowing())
             System.out.println("Dialog show : " + progressDialog.isShowing());
             progressDialog.show();
     }

     private void hideDialog() {
         if (progressDialog.isShowing()) {
             System.out.println("Dialog hide : " + progressDialog.isShowing());
             progressDialog.dismiss();
         }
     }

 }

