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
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String COL_1 = "id";
    private static final String COL_2 = "full_name";
    private static final String COL_3 = "id_number";
    private static final String COL_4 = "contact_number";
    private static final String COL_5 = "email";
    private static final String COL_6 = "password";
    private static final String COL_7 = "role";
    private static final String COL_8 = "photo";
    private static final String COL_9 = "address";

    // Constructor
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
                COL_7 + " TEXT, " +
                COL_8 + " TEXT, " +
                COL_9 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String fullName, String idNumber, String contactNumber, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, fullName);
        contentValues.put(COL_3, idNumber);
        contentValues.put(COL_4, contactNumber);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, password);
        contentValues.put(COL_7, role);
        contentValues.put(COL_8, ""); // Default value for photo
        contentValues.put(COL_9, ""); // Default value for address
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Retrieve a user by full name
    public User getUserByFullName(String fullName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_2 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{fullName});

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_4));
            String idNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_3));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_7));
            // Assuming the user's other details are not needed (like email, photo, address), if you need those, extract them too.
            user = new User(fullName, contactNumber, role, idNumber);
            cursor.close();
        }
        return user; // Return the user object or null if not found
    }

    // Helper method to retrieve users as a list based on role
    public List<User> getUsersByRoleAsList(String role) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE role = ?";
        Cursor cursor = db.rawQuery(query, new String[]{role});

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COL_2));
                    String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_4));
                    String idNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_3));
                    users.add(new User(fullName, contactNumber, role, idNumber));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return users;
    }

    // Method to check if a username (email) already exists in the database
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_5 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true; // Username exists
        } else {
            cursor.close();
            return false; // Username does not exist
        }
    }
    // Method to retrieve a cursor for users by role
    public Cursor getUsersByRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE role = ?";
        return db.rawQuery(query, new String[]{role});
    }

    // Other methods (updateUser, deleteUser, etc.) as required
}
