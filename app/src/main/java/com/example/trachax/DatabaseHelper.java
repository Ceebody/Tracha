package com.example.trachax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TrachaApp.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema changes

    private static final String TABLE_USERS = "users";
    private static final String COL_1 = "id";
    private static final String COL_2 = "full_name";
    private static final String COL_3 = "id_number";
    private static final String COL_4 = "contact_number";
    private static final String COL_5 = "email";
    private static final String COL_6 = "password";
    private static final String COL_7 = "role"; // New column for user role

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS +
                " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT UNIQUE, " + // Email should be unique
                COL_6 + " TEXT, " +
                COL_7 + " TEXT)"; // Role column
        db.execSQL(createTable);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Insert a new user with role
    public boolean insertUser(String fullName, String idNumber, String contactNumber, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, fullName);
        contentValues.put(COL_3, idNumber);
        contentValues.put(COL_4, contactNumber);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1; // Returns true if insertion is successful
    }

    // Check if a user exists with given email and password
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_5 + "=? AND " + COL_6 + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Retrieve role of a user based on email and password
    public String getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_7 + " FROM " + TABLE_USERS + " WHERE " + COL_5 + "=? AND " + COL_6 + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(COL_7));
        }
        cursor.close();
        return role;
    }

    // Get users by role (for Admin Dashboard)
    public List<String> getUsersByRole(String role) {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_2 + " FROM " + TABLE_USERS + " WHERE " + COL_7 + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{role});
        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(0)); // Add full_name to the list
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }
}
