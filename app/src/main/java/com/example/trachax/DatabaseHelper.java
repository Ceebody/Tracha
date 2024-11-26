package com.example.trachax;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TrachaApp.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String COL_1 = "id";
    private static final String COL_2 = "full_name";
    private static final String COL_3 = "id_number";
    private static final String COL_4 = "contact_number";
    private static final String COL_5 = "email";
    private static final String COL_6 = "password";
    private static final String COL_7 = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS +
                " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT UNIQUE, " +
                COL_6 + " TEXT, " +
                COL_7 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String idNumber, String contactNumber, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, fullName);
        contentValues.put(COL_3, idNumber);
        contentValues.put(COL_4, contactNumber);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, password);
        contentValues.put(COL_7, role);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public Cursor getUsersByRoleCursor(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT " + COL_2 + " FROM " + TABLE_USERS + " WHERE " + COL_7 + "=?",
                new String[]{role}
        );
    }

    public List<String> getUsersByRole(String role) {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_2 + " FROM " + TABLE_USERS + " WHERE " + COL_7 + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{role});
        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public Boolean checkUsername(String usernameText) {
    }
}
