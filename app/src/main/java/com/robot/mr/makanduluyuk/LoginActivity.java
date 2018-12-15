package com.robot.mr.makanduluyuk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.robot.mr.makanduluyuk.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private TextView lupaPassword;
    private TextView daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SessionManager sessionManager = new SessionManager(this);

        if(sessionManager.checkLogin()){
            finish();
            Intent intent = new Intent(this, NavigationDrawer.class);
            startActivity(intent);
        }

        final ProgressBar progressBar;

        username= findViewById(R.id.username);
        password = findViewById(R.id.password);

        progressBar =findViewById(R.id.progressBar);

        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernames = username.getText().toString();
                final String pass = password.getText().toString();

                if(usernames.isEmpty()){
                    username.setError("Please enter your username");
                    username.requestFocus();
                } else if(pass.isEmpty()){
                    password.setError("Please enter your username");
                    password.requestFocus();
                } else{
                    loginButton.setEnabled(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        System.out.println("Login result : " + obj.getString(Config.JSON_RESPONSE)
                                        .equals("Success"));

                                        if (obj.getString(Config.JSON_RESPONSE).equals("Success")){
                                            JSONArray userDetail = obj.getJSONArray("result");
//                                            Toast.makeText(LoginActivity.this, obj.getString("response"), Toast.LENGTH_SHORT).show();
                                            User user = new User();
                                            user.setNama(userDetail.getJSONObject(0).getString(Config.NAME));
                                            user.setDob(userDetail.getJSONObject(0).getString(Config.DOB));
                                            user.setJenisKelamin(userDetail.getJSONObject(0).getString(Config.JENIS_KELAMIN));
                                            sessionManager.createLoginSession(usernames, user.getNama(), user.getDob(), user.getJenisKelamin());
//                                            Toast.makeText(LoginActivity.this, obj.getJSONArray("result").getString(0), Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent = new Intent(LoginActivity.this, NavigationDrawer.class);
                                            startActivity(intent);
                                        } else{
                                            Snackbar.make(loginButton, response, 8000).show();
                                            loginButton.setEnabled(true);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        loginButton.setEnabled(true);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loginButton.setEnabled(true);
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put(Config.USERNAME, usernames);
                            params.put(Config.PASSWORD, pass);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
                }
            }
        });

        lupaPassword = findViewById(R.id.lupa_password);
        //lupa password
        /*lupaPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //kode
                return true;
            }
        });*/



        findViewById(R.id.daftar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationAvtivity();
            }
        });
    }

    public void registrationAvtivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}