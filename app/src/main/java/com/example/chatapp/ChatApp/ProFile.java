package com.example.chatapp.ChatApp;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
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

import android.provider.MediaStore;
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
import com.example.chatapp.Service.ChatService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProFile extends AppCompatActivity {

    TextView txt_name, txt_dob, txt_hb, upAVT;
    ImageView avatar,addImg,chat_img;
    Bitmap bitmap;
    String encodeImg;
    String usn,usnCompare,status;
    ActivityResultLauncher<Intent> resultLauncher;
    private static final String URL = "http://10.0.2.2/API/Profile.php";
    private static final String URL1 = "http://10.0.2.2/API/avtUp.php";
    private static final String URL2 = "http://10.0.2.2/API/friendship.php";
    private static final String URL4 = "http://10.0.2.2/API/change_statusFr.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        addImg = findViewById(R.id.frShip);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);
        avatar = findViewById(R.id.avatar);
        chat_img = findViewById(R.id.imageView2);
        registerRs();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.act:
                    startActivity(new Intent(getApplicationContext(), ProFile.class));
                    overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), ChatList.class));
                    overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.other:
                    startActivity(new Intent(getApplicationContext(), Setting.class));
                    overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        txt_name = findViewById(R.id.txt_name);
        txt_dob = findViewById(R.id.txt_dob);
        upAVT = findViewById(R.id.uploadAVT);
        txt_hb = findViewById(R.id.txt_hobby);
        //ten duoc tim kiem
        Intent intent = getIntent();
        String usnIntend = intent.getStringExtra("username");


        if (usnIntend != null && !usnIntend.isEmpty()) {
            //ten search
            usnCompare = usnIntend;
            //ten login de gétFriendShip
            SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            usn = preferences.getString("usnLogin", "");
            getFriendShip();
            //usn = usnCompare; lay data của ten search
            getData(usnCompare);


        } else {
            //ten login
            addImg.setVisibility(View.INVISIBLE);
            chat_img.setVisibility(View.INVISIBLE);
            SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            usn = preferences.getString("usnLogin", "");
            getData(usn);
        }
        chat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profile.java
                Intent intent = new Intent(ProFile.this, ChatActivity.class);
                intent.putExtra("username", usn); // Thay "usernameValue" bằng giá trị thực tế
                intent.putExtra("othername", usnIntend); // Thay "othernameValue" bằng giá trị thực tế
                startActivity(intent);


            }
        });
        //error can fixed
        addImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getFriendShip();
                if (status == null || status.equals("")) {

                    addStatus(ProFile.this);
                }
                else if(status.equals("accepted")){
                    Toast.makeText(ProFile.this, "Cancel!", Toast.LENGTH_SHORT).show();
                    addStatus(ProFile.this);
                    System.out.println(status);
                }
                else if(status.equals("pending")){
                   // System.out.println(status);
                    addStatus(ProFile.this);
                    Toast.makeText(ProFile.this, "Accepted!", Toast.LENGTH_SHORT).show();


                }



            }
        });






        upAVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImg();
            }
        });
    }

    private boolean handleImage(Uri imageUri) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            // Resize the image
            int targetWidth = avatar.getWidth();
            int targetHeight = avatar.getHeight();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
            avatar.setImageBitmap(scaledBitmap);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void uploadAva() {
        if (encodeImg == null || encodeImg.isEmpty()) {
            Toast.makeText(ProFile.this, "No image to upload", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProFile.this, response, Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ProFile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usn", usn);
                params.put("avatar", encodeImg);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProFile.this);
        requestQueue.add(stringRequest);
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        encodeImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void getData(String usn) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String name = jsonObject.getString("Username");
                                String dob = jsonObject.getString("DateOfBirth");
                                String hb = jsonObject.getString("Interests");
                                String avatarUrl = jsonObject.getString("avatar");
                                if (!avatarUrl.isEmpty()) {
                                    String fullAvatarUrl = "http://10.0.2.2/API/Images/" + avatarUrl;
                                    System.out.println(fullAvatarUrl);
                                    Picasso.get().load(fullAvatarUrl).into(avatar);


                                }
                                else if(avatarUrl == null){
                                    avatar.setImageResource(R.drawable.user);
                                }


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
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usn", usn);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProFile.this);
        requestQueue.add(stringRequest);
    }
    public void getFriendShip() {
        System.out.println("Chay get friend");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                 status = jsonObject.getString("Status");
                                System.out.println(status);
                                 updateFriendshipIcon();




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name1", usn);
                params.put("name2",usnCompare);
                System.out.println(usn+"?"+usnCompare);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProFile.this);
        requestQueue.add(stringRequest);
    }

    private void pickImg() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void registerRs() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult rs) {
                        if (rs.getResultCode() == RESULT_OK && rs.getData() != null) {
                            Uri imgUri = rs.getData().getData();
                            if (handleImage(imgUri)) {
                                imageStore(bitmap);
                                uploadAva();
                            } else {
                                Toast.makeText(ProFile.this, "Failed to handle image", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ProFile.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    private void updateFriendshipIcon() {
        if (status.trim().equals("accepted")) {
            addImg.setImageResource(R.drawable.added);

        } else if (status.trim().equals("pending")) {
            addImg.setImageResource(R.drawable.accept);
        } else {
            addImg.setImageResource(R.drawable.addfr);
        }
    }
    private void updateFriendshipIcon2() {
        if (status == null || status.equals("")) {

            addImg.setImageResource(R.drawable.addfr);
        }
        else if (status.trim().equals("accepted")) {
            addImg.setImageResource(R.drawable.added);

        } else if (status.trim().equals("pending")) {
            addImg.setImageResource(R.drawable.accept);
        }
    }
    private void addStatus( Context context) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String statuss = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            if(statuss.equals("success"))
                            {
                                getFriendShip();
                                updateFriendshipIcon2();
                            }

                            Toast.makeText(ProFile.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProFile.this, "Response parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProFile.this, "Request error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name1", usn);
                params.put("name2", usnCompare);
               // params.put("status", status_changed);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProFile.this);
        requestQueue.add(stringRequest);
    }
}
