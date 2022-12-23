package com.example.uas_pppb1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public static ArrayList<News> bookmarkList = new ArrayList<>();
    public static BookmarkAdapter bookmarkAdapter;

    public static DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        databaseHelper = new DatabaseHelper(this);
        getAllBookmark();

        bookmarkAdapter = new BookmarkAdapter(this, bookmarkList);
        recyclerView = findViewById(R.id.rv_bookmark);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookmarkAdapter);
    }

    public static void getAllBookmark() {
        bookmarkList.clear();
        ArrayList<News> allData = databaseHelper.getAllData();
        bookmarkList.addAll(allData);
    }
}