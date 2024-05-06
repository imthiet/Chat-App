package com.example.chatapp.ChatApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.chatapp.Fragment.ChatFragment;
import com.example.chatapp.Login_SignUp.SignUp;
import com.example.chatapp.R;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Login extends AppCompatActivity {

    private Intent intent;
    private String URL = "http://10.0.2.2/LoginRegister/login.php";
    String usn,pwd,crossUsn;

    private EditText usn_edit,pwd_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn = findViewById(R.id.login_btn);
         pwd_edit = findViewById(R.id.usn_edt);
         usn_edit = findViewById(R.id.pwd_edt);
         //TextView uget = findViewById(R.id.usn_vtxt);
        TextView to_signup = findViewById(R.id.to_signup);
        handleSSLHandshake();
        to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUp.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                usn = usn_edit.getText().toString().trim();
                pwd = pwd_edit.getText().toString().trim();
                if(!usn.isEmpty() && !pwd.isEmpty())
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.trim().equals("success")){
                                        Intent intent_login = new Intent(Login.this, ProFile.class);
                                        intent_login.putExtra("username", usn);
                                        // Lưu tên người dùng khi đăng nhập thành công


                                        startActivity(intent_login);
                                        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("usn", usn);
                                        editor.apply();


                                    }
                                    else if (response.trim().equals("failure"))
                                    {
                                        Toast.makeText(Login.this,"Invalid Information!",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        System.out.println(response);
                                        Toast.makeText(Login.this,response.trim(),Toast.LENGTH_LONG).show();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();

                                }


                            })
                    {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> data = new HashMap<>();
                            data.put("Username",usn);
                            data.put("Password",pwd);

                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }
                else {
                    Toast.makeText(Login.this,"Cannot be empty!",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    public void On_Login()
    {

    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


}