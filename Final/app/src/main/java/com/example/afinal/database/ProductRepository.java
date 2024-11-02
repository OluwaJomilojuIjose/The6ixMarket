package com.example.afinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.afinal.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static final String TAG = "ProductRepository";
    private DatabaseHelper dbHelper;

    public ProductRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    public long insertProduct(String name, double price, String condition, String description, double latitude, double longitude, String imageUri, long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_CONDITION, condition);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, imageUri);
        values.put(DatabaseHelper.COLUMN_USER_ID, userId);

        long result = -1;

        try {
            result = db.insertOrThrow(DatabaseHelper.TABLE_PRODUCTS, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting product", e);
        } finally {
            db.close();
        }

        return result;
    }

    // Get all products method
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = getProductFromCursor(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return productList;
    }

    // Get products by user ID
    public List<Product> getProductsByUser(long userId) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COLUMN_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = getProductFromCursor(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return productList;
    }

    // Update product method
    public boolean updateProduct(long productId, String name, double price, String condition, String description, double latitude, double longitude, String imageUri) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_CONDITION, condition);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, imageUri);

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(productId)};

        int rowsAffected = db.update(DatabaseHelper.TABLE_PRODUCTS, values, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }

    // Delete product method
    public boolean deleteProduct(long productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(productId)};

        int rowsDeleted = db.delete(DatabaseHelper.TABLE_PRODUCTS, selection, selectionArgs);
        db.close();

        return rowsDeleted > 0;
    }

    // Helper method to create a Product object from a cursor
    private Product getProductFromCursor(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE));
        String condition = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONDITION));
        String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
        double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE));
        String imageUri = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URI));
        long userId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));

        return new Product(name, price, condition, description, latitude, longitude, imageUri, userId);
    }
}
