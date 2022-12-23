package com.example.uas_pppb1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNewsActivity extends AppCompatActivity {

    EditText titleEditText, releaseDateEditText, minimumAgeEditText, contentEditText;
    Spinner spinner;
    Button addBtn;

    DatabaseReference databaseReference;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        databaseReference = FirebaseDatabase.getInstance().getReference(News.class.getSimpleName());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("User");

        titleEditText = findViewById(R.id.title_editText);
        releaseDateEditText = findViewById(R.id.realeaseDate_editText);
        minimumAgeEditText = findViewById(R.id.minimumAge_editText);
        contentEditText = findViewById(R.id.content_editText);
        spinner = findViewById(R.id.spinner);
        addBtn = findViewById(R.id.add_btn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                showMainActivity();
            }
        });
    }

    private void insertData() {
        News news = new News();

        SharedPreferences sharedPref = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
        String userId = sharedPref.getString(LoginActivity.USER_ID, "user");

        String title = titleEditText.getText().toString();
        String category = spinner.getSelectedItem().toString();
        String releaseDate = releaseDateEditText.getText().toString();
        int minimumAge = Integer.parseInt(minimumAgeEditText.getText().toString());
        String content = contentEditText.getText().toString();

        if (title!="" && category!="" && releaseDate!="" && minimumAge!=0 && content!="" && userId!="") {
            news.setTitle(title);
            news.setCategory(category);
            news.setReleaseDate(releaseDate);
            news.setMinimumAge(minimumAge);
            news.setContent(content);
            news.setUserId(userId);

            databaseReference.push().setValue(news);
            Toast.makeText(this, "News data added", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMainActivity() {
        Intent intent = new Intent(AddNewsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}