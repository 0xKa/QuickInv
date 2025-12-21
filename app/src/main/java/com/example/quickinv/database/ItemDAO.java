package com.example.quickinv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quickinv.models.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemDAO {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ItemDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public long addItem(Item item) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_USER_ID, item.getUserId());
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, item.getName());
        values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, item.getQuantity());
        values.put(DatabaseHelper.COLUMN_ITEM_PRICE, item.getPrice());
        values.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, item.getDescription());

        long result = db.insert(DatabaseHelper.TABLE_ITEMS, null, values);
        close();
        return result;
    }

    public int updateItem(Item item) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, item.getName());
        values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, item.getQuantity());
        values.put(DatabaseHelper.COLUMN_ITEM_PRICE, item.getPrice());
        values.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, item.getDescription());

        String selection = DatabaseHelper.COLUMN_ITEM_ID + " = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        int result = db.update(DatabaseHelper.TABLE_ITEMS, values, selection, selectionArgs);
        close();
        return result;
    }

    public int deleteItem(int itemId) {
        open();
        String selection = DatabaseHelper.COLUMN_ITEM_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};

        int result = db.delete(DatabaseHelper.TABLE_ITEMS, selection, selectionArgs);
        close();
        return result;
    }

    public List<Item> getAllItemsByUser(int userId) {
        open();
        List<Item> items = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_ITEM_ID,
                DatabaseHelper.COLUMN_ITEM_USER_ID,
                DatabaseHelper.COLUMN_ITEM_NAME,
                DatabaseHelper.COLUMN_ITEM_QUANTITY,
                DatabaseHelper.COLUMN_ITEM_PRICE,
                DatabaseHelper.COLUMN_ITEM_DESCRIPTION,
                DatabaseHelper.COLUMN_ITEM_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_ITEM_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String orderBy = DatabaseHelper.COLUMN_ITEM_CREATED_AT + " DESC";

        Cursor cursor = db.query(DatabaseHelper.TABLE_ITEMS, columns, selection, selectionArgs,
                null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = new Item(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return items;
    }

    public List<Item> searchItems(int userId, String searchQuery) {
        open();
        List<Item> items = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_ITEM_ID,
                DatabaseHelper.COLUMN_ITEM_USER_ID,
                DatabaseHelper.COLUMN_ITEM_NAME,
                DatabaseHelper.COLUMN_ITEM_QUANTITY,
                DatabaseHelper.COLUMN_ITEM_PRICE,
                DatabaseHelper.COLUMN_ITEM_DESCRIPTION,
                DatabaseHelper.COLUMN_ITEM_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_ITEM_USER_ID + " = ? AND " +
                DatabaseHelper.COLUMN_ITEM_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(userId), "%" + searchQuery + "%"};
        String orderBy = DatabaseHelper.COLUMN_ITEM_CREATED_AT + " DESC";

        Cursor cursor = db.query(DatabaseHelper.TABLE_ITEMS, columns, selection, selectionArgs,
                null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = new Item(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return items;
    }

    public Item getItemById(int itemId) {
        open();
        String[] columns = {
                DatabaseHelper.COLUMN_ITEM_ID,
                DatabaseHelper.COLUMN_ITEM_USER_ID,
                DatabaseHelper.COLUMN_ITEM_NAME,
                DatabaseHelper.COLUMN_ITEM_QUANTITY,
                DatabaseHelper.COLUMN_ITEM_PRICE,
                DatabaseHelper.COLUMN_ITEM_DESCRIPTION,
                DatabaseHelper.COLUMN_ITEM_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_ITEM_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_ITEMS, columns, selection, selectionArgs,
                null, null, null);

        Item item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new Item(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getDouble(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
        }
        close();
        return item;
    }
}
