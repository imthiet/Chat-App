package com.example.chatapp.ChatApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Setting extends AppCompatActivity {
    String usn;
    TextView name,change_name,c_pass,c_hobby,other;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        usn = preferences.getString("usnLogin", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);
        name = findViewById(R.id.name);
        change_name = findViewById(R.id.changename);
        c_pass = findViewById(R.id.pass);
        c_hobby = findViewById(R.id.hobby);
        logout = findViewById(R.id.signout_btn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Setting.this)
                        .setTitle("Log Out")
                        .setMessage("Confirm Sign Out?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i =new Intent(Setting.this, Login.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();


            }
        });
        name.setText(usn);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {

                case R.id.other:
                    return true;
                case R.id.act:
                    startActivity(new Intent(getApplicationContext(), ProFile.class));
                    overridePendingTransition(R.anim.slide_in_rigth,R.anim.slide_out_left);
                    finish();

                    return true;
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), ChatList.class));
                    overridePendingTransition(R.anim.slide_in_rigth,R.anim.slide_out_left);
                    finish();

                    return true;

            }
            return false;
        });
    }
}