 package com.robot.mr.makanduluyuk;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
    private EditText dateOfBirth;
    private DatePickerDialog.OnDateSetListener tanggal;

    private Button register;


     private static final String TAG = "RegisterActivity";
     private static final String URL_FOR_REGISTRATION = "https://XXX.XXX.X.XX/android_login_example/register.php";
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
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String birthDateFormat = DateFormat.getDateInstance().format(cal.getTime());

        EditText tanggalLahir = (EditText) findViewById(R.id.dateEditText);
        tanggalLahir.setText(birthDateFormat);
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

        if (name.getText().toString().isEmpty()){

            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.getText().toString().isEmpty()){
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }else  if (username.getText().toString().length() < 6){

            Toast.makeText(this, "Username minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.getText().toString().length()<6){

            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }else if (!isValidPassword(password.getText().toString())) {
            Toast.makeText(this, "Password tidak valid", Toast.LENGTH_SHORT).show();

            return;
        }

        if (!confirmPassword.getText().toString().equals(password.getText().toString())){
            Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = gender.getCheckedRadioButtonId();
        String userGender;
        if(selectedId == R.id.female_radio_btn)
            userGender = "Female";
        else
            userGender = "Male";


        if (dateOfBirth.getText().toString().isEmpty()){
            Toast.makeText(this, "Isi Tanggal Lahir", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUser(name.getText().toString(),
                username.getText().toString(),
                password.getText().toString(),
                userGender,
                dateOfBirth.getText().toString());
    }

    private void registerUser(final String name, final String username, final String password, final String gender, final String dob) {

        /*
        String data = name + " | " + username + " | " + password + " | " + gender + " | " + dob;
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("dataUser",data);
        startActivity(intent);
        */
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Hi " + user +", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", username);
                params.put("password", password);
                params.put("gender", gender);
                params.put("age", dob);
                return params;
            }
        };
        // Adding request to request queue
       AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

     private void showDialog() {
         if (!progressDialog.isShowing())
             progressDialog.show();
     }

     private void hideDialog() {
         if (progressDialog.isShowing())
             progressDialog.dismiss();
     }

 }

