package com.example.chatapp.ChatApp;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public  class ProFile extends AppCompatActivity {


    TextView txt_name, txt_dob, txt_hb;
    ImageView menu_btn;

    String usn;

    private static final String URL = "http://10.0.2.2/LoginRegister/ProFile.php";
    //String usn = "quang"; // Đặt giá trị username tại đây hoặc lấy từ đâu đó khác trong ứng dụng của bạn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.act:
                    return true;
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), ChatList.class));
                    overridePendingTransition(R.anim.slide_in_rigth,R.anim.slide_out_left);
                    finish();

                    return true;
                case R.id.other:
                    startActivity(new Intent(getApplicationContext(), ProFile.class));
                    overridePendingTransition(R.anim.slide_in_rigth,R.anim.slide_out_left);
                    finish();

                    return true;

            }
            return false;
        });
        bottomNavigationView.setVisibility(View.GONE);
        menu_btn  =findViewById(R.id.floatingIcon);
        menu_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                if(bottomNavigationView.isShown() )
                {
                    bottomNavigationView.setVisibility(View.GONE);
                    Animation slideInAnimation = AnimationUtils.loadAnimation(ProFile.this, R.anim.menu_disappear);
                    bottomNavigationView.startAnimation(slideInAnimation);
                }
                else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    Animation slideInAnimation = AnimationUtils.loadAnimation(ProFile.this, R.anim.menu_appear);
                    bottomNavigationView.startAnimation(slideInAnimation);

                }

            }
        });
        txt_name = findViewById(R.id.txt_name);
        txt_dob = findViewById(R.id.txt_dob);

        txt_hb = findViewById(R.id.txt_hobby);
       SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        usn = preferences.getString("usn", "quang");

         if(!usn.equals(""))
         {

             Toast.makeText(ProFile.this,usn,Toast.LENGTH_SHORT).show();
            System.out.println(usn);
             getData();
         }
        else {
            Toast.makeText(ProFile.this,"An Error Has Come!!",Toast.LENGTH_SHORT).show();
        }


    }
    public  void getData() {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // Lặp qua từng đối tượng JSON
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Lấy giá trị từ từng cột trong đối tượng JSON
                                String name = jsonObject.getString("Username");
                                String dob = jsonObject.getString("DateOfBirth");
                                String hb = jsonObject.getString("Interests");
                                System.out.println(name + dob + hb);
                                // Hiển thị giá trị trong các TextView tương ứng

                                txt_name.setText("Name: " + name);
                                txt_dob.setText("Dob: " + dob);
                                txt_hb.setText("Hobby: " + hb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi khi yêu cầu không thành công
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usn",usn);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProFile.this);
        requestQueue.add(stringRequest);
    }

}