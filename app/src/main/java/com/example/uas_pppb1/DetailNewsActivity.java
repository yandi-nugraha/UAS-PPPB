package com.example.uas_pppb1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailNewsActivity extends AppCompatActivity {

    ImageView imgNews;
    TextView txtCategory, txtReleaseDate, txtTitle, txtContent;
    String category, releaseDate, title, content, userId, position;

    FloatingActionButton editBtn, deleteBtn;

    DatabaseReference databaseReference;

    String key;
    String minimumAge;

    static DatabaseHelper databaseHelper;

    public static final String RELEASEDATE_EXTRA = "RELEASEDATE_KEY";
    public static final String TITLE_EXTRA = "TITLE_KEY";
    public static final String CATEGORY_EXTRA = "CATEGORY_KEY";
    public static final String CONTENT_EXTRA = "CONTENT_KEY";
    public static final String USERID_EXTRA = "USERID_KEY";
    public static final String NEWSID_EXTRA = "NEWSID_KEY";
    public static final String MINIMUMAGE_EXTRA = "MINIMUMAGE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        databaseReference = FirebaseDatabase.getInstance().getReference("News");

        imgNews = findViewById(R.id.img_news1);
        txtCategory = findViewById(R.id.txt_category1);
        txtReleaseDate = findViewById(R.id.txt_releaseDate1);
        txtTitle = findViewById(R.id.txt_title1);
        txtContent = findViewById(R.id.txt_content1);
        editBtn = findViewById(R.id.edit_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        databaseHelper = new DatabaseHelper(this);

        editBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        category = intent.getStringExtra(NewsAdapter.CATEGORY_EXTRA);
        releaseDate = intent.getStringExtra(NewsAdapter.RELEASEDATE_EXTRA);
        title = intent.getStringExtra(NewsAdapter.TITLE_EXTRA);
        content = intent.getStringExtra(NewsAdapter.CONTENT_EXTRA);
        userId = intent.getStringExtra(NewsAdapter.USERID_EXTRA);

        txtCategory.setText(category);
        txtReleaseDate.setText(releaseDate);
        txtTitle.setText(title);
        txtContent.setText(content);

        SharedPreferences sharedPref = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
        String userID = sharedPref.getString(LoginActivity.USER_ID, "user");
        if (userId.equals(userID)) {
            editBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        if (currentData.child("title").getValue().toString().equals(title)) {
                            key = currentData.getKey();
                            minimumAge = currentData.child("minimumAge").getValue().toString();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), EditNewsActivity.class);
                intent1.putExtra(RELEASEDATE_EXTRA, releaseDate);
                intent1.putExtra(TITLE_EXTRA, title);
                intent1.putExtra(CATEGORY_EXTRA, category);
                intent1.putExtra(CONTENT_EXTRA, content);
                intent1.putExtra(USERID_EXTRA, userId);
                intent1.putExtra(NEWSID_EXTRA, key);
                intent1.putExtra(MINIMUMAGE_EXTRA, minimumAge);
                startActivity(intent1);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
                showMainActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookmark_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bookmark_btn) {
            insertDataLocal();
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertDataLocal() {
        News news = new News();
        news.setTitle(title);
        news.setCategory(category);
        news.setReleaseDate(releaseDate);
        news.setMinimumAge(Integer.parseInt(minimumAge));
        news.setContent(content);
        news.setUserId(userId);

        databaseHelper.insertRecord(news);
        Toast.makeText(DetailNewsActivity.this, "News successfully moved to Bookmarks!", Toast.LENGTH_SHORT).show();
    }

    private void deleteData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        if (currentData.child("title").getValue().toString().equals(title)) {
                            databaseReference.child(currentData.getKey()).removeValue();
                            Toast.makeText(DetailNewsActivity.this, "News deleted", Toast.LENGTH_SHORT).show();
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

    private void showMainActivity() {
        Intent intent = new Intent(DetailNewsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}