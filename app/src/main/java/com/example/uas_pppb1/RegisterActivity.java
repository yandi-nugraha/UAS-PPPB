package com.example.uas_pppb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    ImageView backBtn;
    EditText usernameEditText, emailEditText, passwordEditText;
    Button registerBtn;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());

        backBtn = findViewById(R.id.back_btn_R);
        usernameEditText = findViewById(R.id.username_editText_R);
        emailEditText = findViewById(R.id.email_editText_R);
        passwordEditText = findViewById(R.id.password_editText_R);
        registerBtn = findViewById(R.id.register_btn_R);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                toLoginActivity();
            }
        });
    }

    private void insertData() {
        User user = new User();
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username != "" && email != "" && password != "") {
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            databaseReference.push().setValue(user);
            Toast.makeText(this, "Your account has been successfully created!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}