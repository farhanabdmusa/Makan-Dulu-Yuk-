package com.robot.mr.makanduluyuk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        if (obj.getString(Config.JSON_RESPONSE).equals("Success")){
                                            Toast.makeText(LoginActivity.this, obj.getString("result"), Toast.LENGTH_SHORT).show();
                                        } else{
                                            Toast.makeText(LoginActivity.this, obj.getString("response"), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

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

    }
}
