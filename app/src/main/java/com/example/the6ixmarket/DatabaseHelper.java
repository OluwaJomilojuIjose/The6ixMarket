package com.example.the6ixmarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Market.db";
    private static final int DATABASE_VERSION = 6; // Incremented from 5 to 6

    // Listings table and columns
    public static final String TABLE_LISTINGS = "listings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URI = "image_uri";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SELLER = "seller";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_STATUS = "status"; // New column

    // User table and columns
    public static final String TABLE_USER = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LISTINGS_TABLE = "CREATE TABLE " + TABLE_LISTINGS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_PRICE + " TEXT, "
                + COLUMN_IMAGE_URI + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_SELLER + " TEXT, "
                + COLUMN_COUNTRY + " TEXT, "
                + COLUMN_POSTAL_CODE + " TEXT, "
                + COLUMN_STATUS + " TEXT)"; // New column
        db.execSQL(CREATE_LISTINGS_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // For simplicity, drop and recreate the tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
