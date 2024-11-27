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
    private final DatabaseHelper dbHelper;

    public ProductRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insert a new product
    public long insertProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, product.getName());
        values.put(DatabaseHelper.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseHelper.COLUMN_CONDITION, product.getCondition());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, product.getDescription());
        values.put(DatabaseHelper.COLUMN_LATITUDE, product.getLatitude());
        values.put(DatabaseHelper.COLUMN_LONGITUDE, product.getLongitude());
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, product.getImageUri());
        values.put(DatabaseHelper.COLUMN_USER_ID, product.getUserId());

        long result = -1;
        try {
            result = db.insertOrThrow(DatabaseHelper.TABLE_PRODUCTS, null, values);
            Log.i(TAG, "Product inserted successfully with ID: " + result);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting product", e);
        } finally {
            db.close();
        }
        return result;
    }

    // Retrieve all products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Product product = getProductFromCursor(cursor);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving products", e);
        } finally {
            db.close();
        }

        return productList;
    }

    // Helper to parse product data from the cursor
    private Product getProductFromCursor(Cursor cursor) {
        return new Product(
                cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONDITION)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URI)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
        );
    }

    // Delete a product by ID
    public boolean deleteProduct(long productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        try {
            rowsDeleted = db.delete(DatabaseHelper.TABLE_PRODUCTS, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
            Log.i(TAG, "Rows deleted: " + rowsDeleted);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting product", e);
        } finally {
            db.close();
        }
        return rowsDeleted > 0;
    }
}
