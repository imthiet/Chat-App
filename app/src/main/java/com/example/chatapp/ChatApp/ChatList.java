package com.example.chatapp.ChatApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.Class.User;
import com.example.chatapp.Adapter.Chat_list_adapter;
import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatList extends AppCompatActivity {
    ListView listView;
    Chat_list_adapter adapter;
    ImageView avatar;
    TextView etFr, label;
    Button btnF;
    User user;
    String usn;
    String URL = "http://10.0.2.2/API/findF.php";
    String URL2 = "http://10.0.2.2/API/chatlist.php";
    public static ArrayList<User> usersArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_get);

        etFr = findViewById(R.id.etFr);
        btnF = findViewById(R.id.btnFind);
        label = findViewById(R.id.label);
        listView = findViewById(R.id.listView_list);

        adapter = new Chat_list_adapter(this, usersArrayList);
        listView.setAdapter(adapter);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        usn = preferences.getString("usnLogin", "");

        getData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), ChatList.class));
                    overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.act:
                    startActivity(new Intent(getApplicationContext(), ProFile.class));
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User itemValue = (User) listView.getItemAtPosition(position);

                Intent intent = new Intent(ChatList.this, ChatActivity.class);
                intent.putExtra("username", usn);
                intent.putExtra("othername", itemValue.getName());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                User itemValue = (User) listView.getItemAtPosition(position);

                // Hiển thị AlertDialog xác nhận xóa
                new AlertDialog.Builder(ChatList.this)
                        .setTitle("View Profile")
                        .setMessage("View Profile "+itemValue.getName().toString()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent_profile = new Intent(ChatList.this, ProFile.class);
                                intent_profile.putExtra("username", itemValue.getName());
                                startActivity(intent_profile);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        etFr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btnF.setText("Cancel");
                    getFData();
                }
            }
        });

        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFr.clearFocus();
                etFr.setText("");
                getData();
            }
        });

        etFr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<User> filteredList = new ArrayList<>();
                for (User u : usersArrayList) {
                    if (u.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredList.add(u);
                    }
                }
                adapter.updateData(filteredList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2,
                response -> {
                    usersArrayList.clear();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("Username");
                            String avatarUrl = jsonObject.getString("avatar");

                            if (!avatarUrl.isEmpty()) {
                                avatarUrl = "http://10.0.2.2/API/Images/" + avatarUrl;
                            }

                            user = new User(name, "", "", "", "", avatarUrl);
                            usersArrayList.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ChatList.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nameF", usn);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ChatList.this);
        requestQueue.add(stringRequest);
    }

    public void getFData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("Username");
                            String avatarUrl = jsonObject.getString("avatar");

                            if (!avatarUrl.isEmpty()) {
                                avatarUrl = "http://10.0.2.2/API/Images/" + avatarUrl;
                            }

                            user = new User(name, "", "", "", "", avatarUrl);
                            usersArrayList.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ChatList.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nameF", etFr.getText().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ChatList.this);
        requestQueue.add(stringRequest);
    }


}
