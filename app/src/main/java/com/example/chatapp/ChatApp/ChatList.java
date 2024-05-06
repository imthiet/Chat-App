package com.example.chatapp.ChatApp;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChatList extends AppCompatActivity {
    ListView listView;
    Chat_list_adapter adapter;
    TextView etFr,label;
    Button btnF;


    String URL = "http://10.0.2.2/LoginRegister/retrieve.php";
    public static ArrayList<User>usersArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_get);
        etFr = findViewById(R.id.etFr);
        btnF = findViewById(R.id.btnFind);
        label = findViewById(R.id.label);
        listView = findViewById(R.id.listView_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {

                case R.id.chat:
                    return true;
                case R.id.act:
                    startActivity(new Intent(getApplicationContext(), ProFile.class));
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

//            adapter = new Chat_list_adapter(this,usersArrayList);
//            listView.setAdapter(adapter);
//            retrieveData();
            Toast.makeText(ChatList.this,"Cheked",Toast.LENGTH_SHORT).show();

        etFr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // EditText được focus
                    btnF.setText("Cancel");
                    label.setText("Result");
//                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                        @Override
//                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                            if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                                // Xử lý khi người dùng nhấn phím "Enter"
//                                return true;
//                            }
//                            return false;
//                        }
//                    });

                    btnF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etFr.setText("");
                            etFr.clearFocus();
                            label.setText("Chat list");
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    });
                } else {

                    btnF.setText("Find");
                }
            }
        });

       

    }

    public void retrieveData()
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                usersArrayList.clear();
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(success.equals("1"))
                    {
                        for(int i = 0;i < jsonArray.length();i++)
                        {
                            JSONObject object= jsonArray.getJSONObject(i);
                            String fn = object.getString("fn");
                            String nn = object.getString("usn");
                            String password = object.getString("pwd");
                            String dob = object.getString("dob");
                            String hobby = object.getString("hobby");

                            User new_user = new User(nn,"",fn,dob,hobby);
                            //User new_user = new User("quang","thiet","quang thiet","13/7/2003","codeing");
                            usersArrayList.add(new_user);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(testGet.this,error.toString(),Toast.LENGTH_SHORT).show();
              error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}