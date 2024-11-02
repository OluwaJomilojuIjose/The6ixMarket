package com.example.afinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.afinal.model.User;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insert user information
    public long insertUser(String username, String email, String phone, String userType) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, username);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_USER_TYPE, userType);

        long result = -1;

        try {
            result = db.insertOrThrow(DatabaseHelper.TABLE_USERS, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting user", e);
        } finally {
            db.close();
        }

        return result;
    }

    // Load user by ID
    public User getUserById(long userId) {
        User user = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_NAME));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_PHONE));
            String userType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_TYPE));
            user = new User(userId, username, email, phone, userType);
            cursor.close();
        }

        db.close();
        return user;
    }
}
