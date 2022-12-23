package com.example.uas_pppb1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "news-db";
    private static final int DATABASE_VERSION = 1;

    private static final String NEWS_TABLE = "news";
    private static final String NEWS_ID = "id";
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_CATEGORY = "category";
    private static final String NEWS_RELEASEDATE = "releaseDate";
    private static final String NEWS_MINIMUMAGE = "minimumAge";
    private static final String NEWS_CONTENT = "content";
    private static final String NEWS_USERID = "userId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_NEWS = "CREATE TABLE " + NEWS_TABLE + "(" + NEWS_ID + " INTEGER PRIMARY KEY, " +
                NEWS_TITLE + " TEXT, " + NEWS_CATEGORY + " TEXT, " + NEWS_RELEASEDATE + " TEXT, " +
                NEWS_MINIMUMAGE + " INTEGER, " + NEWS_CONTENT + " TEXT, " + NEWS_USERID + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertRecord(News news) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NEWS_TITLE, news.getTitle());
        values.put(NEWS_CATEGORY, news.getCategory());
        values.put(NEWS_RELEASEDATE, news.getReleaseDate());
        values.put(NEWS_MINIMUMAGE, news.getMinimumAge());
        values.put(NEWS_CONTENT, news.getContent());
        values.put(NEWS_USERID, news.getUserId());

        db.insert(NEWS_TABLE, null, values);
        db.close();
    }

    public ArrayList<News> getAllData() {
        ArrayList<News> allBookmarks = new ArrayList<News>();

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + NEWS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setId(cursor.getInt(0));
                news.setTitle(cursor.getString(1));
                news.setCategory(cursor.getString(2));
                news.setReleaseDate(cursor.getString(3));
                news.setMinimumAge(cursor.getInt(4));
                news.setContent(cursor.getString(5));
                news.setUserId(cursor.getString(6));

                allBookmarks.add(news);
            } while (cursor.moveToNext());
        }

        return allBookmarks;
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NEWS_TABLE, NEWS_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }
}
