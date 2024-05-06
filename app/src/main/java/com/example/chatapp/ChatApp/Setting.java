package com.example.chatapp.ChatApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.act);

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