package com.example.dclv_v2126;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btn_login;
    private TextView link_regist;
    private ProgressBar loading;
    private static String URL_LOGIN = "http://192.168.1.4/DCLV/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        sessionManager = new SessionManager(this);

        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        link_regist = findViewById(R.id.link_regist);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if(!mEmail.isEmpty() || !mPass.isEmpty()){
                    Login(mEmail,mPass);

                }else{
                    email.setError("Please insert email");
                    password.setError("Please insert password");
                }
            }
        });
        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,register.class));
            }
        });
    }

    private void Login(String email, String password) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("login");


                            if(success.equals("1")){
                                for(int i = 0;i < jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();
                                    String admin = object.getString("admin").trim();

                                    if(admin.equals("1")){
                                        sessionManager.createSession(name,email,id);
                                        Intent intent1 = new Intent(LoginActivity.this,MainShopActivity.class);
                                        intent1.putExtra("name",name);
                                        intent1.putExtra("email",email);
                                        startActivity(intent1);
                                    }else {
                                        sessionManager.createSession(name,email,id);
                                        Intent intent2 = new Intent(LoginActivity.this,MainActivity.class);
                                        intent2.putExtra("name",name);
                                        intent2.putExtra("email",email);
                                        startActivity(intent2);
                                    }


                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this,"Error 12" + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,"Error 2" + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}