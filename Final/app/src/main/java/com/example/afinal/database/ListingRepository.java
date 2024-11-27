package com.example.afinal.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.afinal.model.Listing;

import java.util.ArrayList;
import java.util.List;

public class ListingRepository {

    private final DatabaseHelper dbHelper;

    public ListingRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_LISTINGS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LISTING_TITLE));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LISTING_PRICE));

                listings.add(new Listing(id, title, "", price, ""));

            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return listings;
    }
}
