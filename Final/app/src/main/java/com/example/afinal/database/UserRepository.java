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
    private final DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insert a new user
    // Updated insertUser to handle the password field
    public long insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, user.getPhone());
        values.put(DatabaseHelper.COLUMN_USER_TYPE, user.getUserType());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword()); // Ensure this is inserted even if empty

        long result = -1;
        try {
            result = db.insertOrThrow(DatabaseHelper.TABLE_USERS, null, values);
            Log.i(TAG, "User inserted successfully with ID: " + result);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting user", e);
        } finally {
            db.close();
        }
        return result;
    }


    // Retrieve a user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_USER_NAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_PHONE,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_PASSWORD // Include password in query
        };
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
                String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE));
                String userType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_TYPE));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));

                user = new User(id, username, userEmail, phone, userType, password); // Updated constructor call
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving user by email", e);
        } finally {
            db.close();
        }

        return user;
    }

    // Retrieve a user by ID
    public User getUserById(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_USER_NAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_PHONE,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_PASSWORD // Include password in query
        };
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        try (Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE));
                String userType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_TYPE));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));

                user = new User(id, username, email, phone, userType, password); // Updated constructor call
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving user by ID", e);
        } finally {
            db.close();
        }

        return user;
    }

    // Delete a user by ID
    public boolean deleteUser(long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        try {
            rowsDeleted = db.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            Log.i(TAG, "Rows deleted: " + rowsDeleted);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting user", e);
        } finally {
            db.close();
        }

        return rowsDeleted > 0;
    }
}
