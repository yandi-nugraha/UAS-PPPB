package com.example.uas_pppb1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNewsBtn;

    private RecyclerView recyclerView;
    private ArrayList<News> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private ArrayList<News> filteredNews = new ArrayList<>();

    DatabaseReference databaseReference;

    String category;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference(News.class.getSimpleName());

        addNewsBtn = findViewById(R.id.addNews_btn);
        addNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewsActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
        category = sharedPref.getString(DetailUserActivity.CATEGORY_EXTRA, "Entertainment");
        age = Integer.parseInt(sharedPref.getString(DetailUserActivity.AGE_EXTRA, "0"));

        newsAdapter = new NewsAdapter(this, filteredNews);
        recyclerView = findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);

        getAllData();
    }

    public void getAllData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        News news = new News();
                        news = currentData.getValue(News.class);
                        newsList.add(news);
                    }
                    filterList(age, category);
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bookmark:
                showBookmarkActivity();
                break;
            case R.id.menu_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterList(int age, String category) {
        for (News news : newsList) {
            if (news.getMinimumAge()<=age && news.getCategory().equals(category)) {
                filteredNews.add(news);
            }
        }
    }

    private void showBookmarkActivity() {
        Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
        startActivity(intent);
    }

    private void logout() {
        StartActivity activity = new StartActivity();
        activity.logout();
        LoginActivity activity1 = new LoginActivity();
        activity1.resetFilled();
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
    }
}