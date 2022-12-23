package com.example.uas_pppb1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ImageView backBtn;
    EditText usernameEditText, passwordEditText;
    Button loginBtn;

    User user;
    DatabaseReference databaseReference;
    public static String key;

    private Boolean filledData = false;

    private static SharedPreferences sharedPreferences;
    public static final String sharedPrefFile = "com.example.loginsharedpref";

    private static final String FILLED_KEY = "filled-key";
    public static final String USER_ID = "user-id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        filledData = sharedPreferences.getBoolean(FILLED_KEY, false);

        backBtn = findViewById(R.id.back_btn);
        usernameEditText = findViewById(R.id.username_editText);
        passwordEditText = findViewById(R.id.password_editText);
        loginBtn = findViewById(R.id.login_btn_L);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }

    private void checkUser() {
        user = new User();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        StartActivity activity = new StartActivity();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        if (currentData.child("username").getValue().toString().equals(username) &&
                        currentData.child("password").getValue().toString().equals(password)) {
                            activity.saveLogin();
                            key = currentData.getKey();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USER_ID, key);
                            editor.apply();
                            if (filledData) {
                                showMainActivity();
                            } else {
                                showDetailUserActivity();
                            }
                            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showDetailUserActivity() {
        Intent intent = new Intent(LoginActivity.this, DetailUserActivity.class);
        startActivity(intent);
    }

    private void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void saveFilled() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FILLED_KEY, true);
        editor.apply();
    }

    public static void resetFilled() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FILLED_KEY, false);
        editor.apply();
    }
}