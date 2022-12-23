package com.example.uas_pppb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNewsActivity extends AppCompatActivity {

    EditText titleEditText, releaseDateEditText, minimumAgeEditText, contentEditText;
    Spinner mspinner;
    Button saveBtn;

    String category, releaseDate, title, content, userId, newsId, minimumAge;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);

        databaseReference = FirebaseDatabase.getInstance().getReference("News");

        titleEditText = findViewById(R.id.title_editText1);
        releaseDateEditText = findViewById(R.id.realeaseDate_editText1);
        minimumAgeEditText = findViewById(R.id.minimumAge_editText1);
        contentEditText = findViewById(R.id.content_editText1);
        mspinner = findViewById(R.id.spinner1);
        saveBtn = findViewById(R.id.save_btn1);

        Intent intent = getIntent();
        category = intent.getStringExtra(DetailNewsActivity.CATEGORY_EXTRA);
        releaseDate = intent.getStringExtra(DetailNewsActivity.RELEASEDATE_EXTRA);
        title = intent.getStringExtra(DetailNewsActivity.TITLE_EXTRA);
        content = intent.getStringExtra(DetailNewsActivity.CONTENT_EXTRA);
        userId = intent.getStringExtra(DetailNewsActivity.USERID_EXTRA);
        newsId = intent.getStringExtra(DetailNewsActivity.NEWSID_EXTRA);
        minimumAge = intent.getStringExtra(DetailNewsActivity.MINIMUMAGE_EXTRA);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinner.setAdapter(adapter);

        int selectionPosition = adapter.getPosition(category);

        titleEditText.setText(title);
        mspinner.setSelection(selectionPosition);
        releaseDateEditText.setText(releaseDate);
        minimumAgeEditText.setText(minimumAge);
        contentEditText.setText(content);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News updatedData = new News();
                updatedData.setTitle(titleEditText.getText().toString());
                updatedData.setCategory(mspinner.getSelectedItem().toString());
                updatedData.setReleaseDate(releaseDateEditText.getText().toString());
                updatedData.setMinimumAge(Integer.parseInt(minimumAgeEditText.getText().toString()));
                updatedData.setContent(contentEditText.getText().toString());
                updatedData.setUserId(userId);

                databaseReference.child(newsId).setValue(updatedData);

                showMainActivity();
            }
        });
    }

    private void showMainActivity() {
        Intent intent2 = new Intent(EditNewsActivity.this, MainActivity.class);
        startActivity(intent2);
    }
}