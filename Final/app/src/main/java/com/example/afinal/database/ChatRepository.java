package com.example.afinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.afinal.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private final DatabaseHelper dbHelper;

    public ChatRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SENDER_ID, message.getSenderId());
        values.put(DatabaseHelper.COLUMN_RECEIVER_ID, message.getReceiverId());
        values.put(DatabaseHelper.COLUMN_MESSAGE_CONTENT, message.getMessageContent());

        long result = db.insert(DatabaseHelper.TABLE_MESSAGES, null, values);
        db.close();
        return result;
    }

    public List<Message> getChatMessages(long senderId, long receiverId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Message> messages = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_MESSAGES +
                " WHERE (" + DatabaseHelper.COLUMN_SENDER_ID + " = ? AND " + DatabaseHelper.COLUMN_RECEIVER_ID + " = ?) " +
                " OR (" + DatabaseHelper.COLUMN_SENDER_ID + " = ? AND " + DatabaseHelper.COLUMN_RECEIVER_ID + " = ?) " +
                " ORDER BY " + DatabaseHelper.COLUMN_TIMESTAMP + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(senderId), String.valueOf(receiverId),
                String.valueOf(receiverId), String.valueOf(senderId)});

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MESSAGE_CONTENT));
                String timestamp = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP));

                messages.add(new Message(id, senderId, receiverId, content, timestamp));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return messages;
    }
}
