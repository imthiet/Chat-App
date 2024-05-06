package com.example.chatapp.Login_SignUp;

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
import com.example.chatapp.ChatApp.Login;
import com.example.chatapp.R;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private EditText usn_edt,nn_edt,pwd_edt,re_pwd;
    private Button btn_signup;
    private String URL = "http://10.0.2.2/LoginRegister/signup.php";
    String usn,nn,pwd,repwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usn_edt = findViewById(R.id.usn_edt);
        nn_edt = findViewById(R.id.cfm_edit);
        pwd_edt = findViewById(R.id.pwd_edt);
        re_pwd = findViewById(R.id.nn_edt);
        btn_signup = findViewById(R.id.signup_btn);
        usn = nn = pwd = repwd = "";


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
                usn = usn_edt.getText().toString().trim();
                nn = nn_edt.getText().toString().trim();
                pwd = pwd_edt.getText().toString().trim();
                repwd = re_pwd.getText().toString().trim();
                if(!pwd.equals(repwd))
                {
                    Toast.makeText(SignUp.this,"not Match Password",Toast.LENGTH_SHORT).show();
                } else if (!usn.equals("") && !nn.equals("") && !pwd.equals("")) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(SignUp.this,"Sign Up Successfull!",Toast.LENGTH_SHORT).show();
                                    }
                                    else if (response.equals("failure"))
                                    {
                                        Toast.makeText(SignUp.this,"Fail to Sign Up!",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(SignUp.this,response,Toast.LENGTH_SHORT).show();
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
                            data.put("username",usn);
                            data.put("password",pwd);
                            data.put("fullname",nn);

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