package com.example.chatapp.ChatApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.R;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
     EditText usn_edt,fn_edt,pwd_edt,re_pwd,hb_edt,dob_edt;
     Button btn_signup;
     String URL = "http://10.0.2.2/API/signup.php";
    String usn_get,fn_get,pwd_get,hb_str,dob_str,re_pwd_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usn_edt = findViewById(R.id.usn_edt);
        fn_edt = findViewById(R.id.fullname_edt);
        pwd_edt = findViewById(R.id.pwd_edt);
        re_pwd = findViewById(R.id.retype_pwd_edt);
        btn_signup = findViewById(R.id.signup_btn);
        hb_edt= findViewById(R.id.hobby_edt);
        dob_edt = findViewById(R.id.dob_edt);
        ;


        TextView to_login = findViewById(R.id.to_login);
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usn_get = usn_edt.getText().toString().trim();
                fn_get = fn_edt.getText().toString().trim();
                pwd_get = pwd_edt.getText().toString().trim();
                re_pwd_str = re_pwd.getText().toString().trim();
                hb_str = hb_edt.getText().toString().trim();
                dob_str=dob_edt.getText().toString().trim();

                if(!pwd_get.equals(re_pwd_str))
                {
                    Toast.makeText(SignUp.this,"not Match Password",Toast.LENGTH_SHORT).show();
                } else if (!usn_get.equals("") && !fn_get.equals("") && !pwd_get.equals("")) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.trim().equals("success")){
                                        Toast.makeText(SignUp.this,"Sign Up Successfull!",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, Login.class);
                                        startActivity(intent);

                                    }
                                    else if (response.trim().equals("failure"))
                                    {
                                        Toast.makeText(SignUp.this,"Fail to Sign Up! UserName Existed",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(SignUp.this,response,Toast.LENGTH_SHORT).show();
                                        System.out.println(response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SignUp.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
                                     error.printStackTrace(); // In ra stack trace của lỗi
                                    error.printStackTrace();
                                }


                            })
                    {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> data = new HashMap<>();
                            data.put("username",usn_get);
                            data.put("password",pwd_get);
                            data.put("fullname",fn_get);
                            data.put("dob",dob_str);
                            data.put("hb",hb_str);

                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
    }


}