package com.example.uas_pppb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button loginbtn, registerBtn;

    private boolean isLogin = false;

    private static SharedPreferences sharedPreferences;
    private final String sharedPrefFile = "com.example.uas_pppb1";

    private static final String LOGIN_KEY = "login-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean(LOGIN_KEY, false);

        if (isLogin) {
            toMainActivity();
        }

        loginbtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public static void saveLogin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_KEY, true);
        editor.apply();
    }

    public static void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_KEY, false);
        editor.apply();
    }
}