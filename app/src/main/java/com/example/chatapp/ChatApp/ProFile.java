package com.example.chatapp.ChatApp;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public  class ProFile extends AppCompatActivity {


    TextView txt_name, txt_dob, txt_hb,upAVT;
    ImageView avatar;
    ImageView menu_btn;
    Bitmap bitmap;
    String encodeImg;
    String usn;

    private static final String URL = "http://10.0.2.2/API/Profile.php";
    private static final String URL1 = "http://10.0.2.2/API/AvatarUpLoad.php";
    //String usn = "quang"; // Đặt giá trị username tại đây hoặc lấy từ đâu đó khác trong ứng dụng của bạn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);
        avatar = findViewById(R.id.avatar);
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
                    startActivity(new Intent(getApplicationContext(), Setting.class));
                    overridePendingTransition(R.anim.slide_in_rigth,R.anim.slide_out_left);
                    finish();

                    return true;

            }
            return false;
        });



        txt_name = findViewById(R.id.txt_name);
        txt_dob = findViewById(R.id.txt_dob);
        upAVT = findViewById(R.id.uploadAVT);
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
        upAVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Dexter.withActivity(ProFile.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Choose Avatar"),1);
                           // startActivityForResult(Intent.createChooser(intent,"Choose Image",1));

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
            }
        });


    }
    private void uploadAva()
    {StringRequest stringRequest= new StringRequest(Request.Method.POST, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   Toast.makeText(ProFile.this,response,Toast.LENGTH_SHORT).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Xử lý lỗi khi yêu cầu không thành công
                    error.printStackTrace();
                    Toast.makeText(ProFile.this, error.toString(),Toast.LENGTH_SHORT).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("avatar",encodeImg);
            return params;
        }
    };

        RequestQueue requestQueue = Volley.newRequestQueue(ProFile.this);
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();
            try  {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                avatar.setImageBitmap(bitmap);
                imageStore(bitmap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void imageStore(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imageBytes = stream.toByteArray();
        encodeImg = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public  void getData() {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
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