package com.robot.mr.makanduluyuk;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText newPass, confirm;
    Button change;
    boolean status = true;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        username = intent.getStringExtra(Config.USERNAME);

        newPass = findViewById(R.id.reset_password);
        confirm = findViewById(R.id.reset_confirm);
        change = findViewById(R.id.change_password);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getText().toString().isEmpty()){
                    newPass.setError("Password tidak boleh kosong");
                    newPass.requestFocus();
                    status = false;
                } else if(!isValidPassword(newPass.getText().toString())){
                    newPass.setError("Password minimal 6 karakter, mengandung huruf besar, kecil, angka, dan simbol");
                    newPass.requestFocus();
                    status = false;
                }
                if(confirm.getText().toString().isEmpty()){
                    confirm.setError("Konfirmasi password tidak boleh kosong");
                    confirm.requestFocus();
                    status = false;
                } else if(!confirm.getText().toString().equals(newPass.getText().toString())){
                    confirm.setError("Password tidak sama");
                    confirm.requestFocus();
                    status = false;
                }

                if (status){
                    change.setEnabled(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RESET_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString(Config.JSON_RESPONSE).equals("Success")) {
                                            finish();
                                        } else{
                                            Snackbar.make(change, obj.getString(Config.JSON_RESPONSE), 8000)
                                                    .show();
                                            change.setEnabled(true);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        change.setEnabled(true);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            change.setEnabled(true);
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put(Config.USERNAME, username);
                            params.put(Config.PASSWORD, newPass.getText().toString());
                            return params;
                        }
                    };
                    Volley.newRequestQueue(ResetPasswordActivity.this).add(stringRequest);
                }
            }
        });
    }

    private static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
