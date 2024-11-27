package com.example.afinal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "marketplace.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MESSAGES = "messages";
    public static final String TABLE_LISTINGS = "listings";

    // Product Table Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CONDITION = "condition";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_IMAGE_URI = "image_uri";
    public static final String COLUMN_USER_ID = "user_id";

    // User Table Columns
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_TYPE = "user_type";

    // Message Table Columns
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_RECEIVER_ID = "receiver_id";
    public static final String COLUMN_MESSAGE_CONTENT = "message_content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Listings Table Columns
    public static final String COLUMN_LISTING_TITLE = "title";
    public static final String COLUMN_LISTING_DESCRIPTION = "description";
    public static final String COLUMN_LISTING_PRICE = "price";
    public static final String COLUMN_LISTING_IMAGE_URL = "image_url";

    // Table Create Statements
    private static final String CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_CONDITION + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_IMAGE_URI + " TEXT, " +
                    COLUMN_USER_ID + " INTEGER NOT NULL)";

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_USER_PHONE + " TEXT, " +
                    COLUMN_USER_TYPE + " TEXT NOT NULL)";

    private static final String CREATE_MESSAGES_TABLE =
            "CREATE TABLE " + TABLE_MESSAGES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SENDER_ID + " INTEGER NOT NULL, " +
                    COLUMN_RECEIVER_ID + " INTEGER NOT NULL, " +
                    COLUMN_MESSAGE_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String CREATE_LISTINGS_TABLE =
            "CREATE TABLE " + TABLE_LISTINGS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LISTING_TITLE + " TEXT NOT NULL, " +
                    COLUMN_LISTING_DESCRIPTION + " TEXT, " +
                    COLUMN_LISTING_PRICE + " REAL NOT NULL, " +
                    COLUMN_LISTING_IMAGE_URL + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_LISTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTINGS);
        onCreate(db);
    }
}
