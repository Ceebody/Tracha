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
    private static final int DATABASE_VERSION = 3;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_ID_NUMBER = "id_number";
    private static final String COL_CONTACT_NUMBER = "contact_number";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private static final String COL_PHOTO = "photo";
    private static final String COL_ADDRESS = "address";

    // Drivers Table
    private static final String TABLE_DRIVERS = "drivers";
    private static final String COL_PARENT_NAME = "parent_name";
    private static final String COL_CHILD_NAME = "child_name";
    private static final String COL_PHONE_NUMBER = "phone_number";

    // Bus Location Table
    private static final String TABLE_BUS_LOCATION = "bus_location";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";

    // Messages Table
    private static final String TABLE_MESSAGES = "messages";
    private static final String COL_SENDER_ID = "sender_id";
    private static final String COL_RECEIVER_ID = "receiver_id";
    private static final String COL_MESSAGE_TEXT = "message_text";
    private static final String COL_TIMESTAMP = "timestamp";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULL_NAME + " TEXT, " +
                COL_ID_NUMBER + " TEXT, " +
                COL_CONTACT_NUMBER + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT, " +
                COL_PHOTO + " TEXT, " +
                COL_ADDRESS + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Drivers Table
        String createDriversTable = "CREATE TABLE " + TABLE_DRIVERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PARENT_NAME + " TEXT, " +
                COL_CHILD_NAME + " TEXT, " +
                COL_PHONE_NUMBER + " TEXT UNIQUE)";
        db.execSQL(createDriversTable);

        // Create Bus Location Table
        String createBusLocationTable = "CREATE TABLE " + TABLE_BUS_LOCATION + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LATITUDE + " REAL, " +
                COL_LONGITUDE + " REAL)";
        db.execSQL(createBusLocationTable);

        // Create Messages Table
        String createMessagesTable = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SENDER_ID + " TEXT, " +
                COL_RECEIVER_ID + " TEXT, " +
                COL_MESSAGE_TEXT + " TEXT, " +
                COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createMessagesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String fullName, String idNumber, String contactNumber, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULL_NAME, fullName);
        contentValues.put(COL_ID_NUMBER, idNumber);
        contentValues.put(COL_CONTACT_NUMBER, contactNumber);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_ROLE, role);
        contentValues.put(COL_PHOTO, ""); // Default photo
        contentValues.put(COL_ADDRESS, ""); // Default address
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Insert a new driver
    public boolean insertDriver(String parentName, String childName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PARENT_NAME, parentName);
        contentValues.put(COL_CHILD_NAME, childName);
        contentValues.put(COL_PHONE_NUMBER, phoneNumber);
        long result = db.insert(TABLE_DRIVERS, null, contentValues);
        return result != -1;
    }

    // Retrieve driver details by phone number
    public Cursor getDriverDetailsByPhone(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_PARENT_NAME + ", " + COL_CHILD_NAME + " FROM " + TABLE_DRIVERS + " WHERE " + COL_PHONE_NUMBER + " = ?";
        return db.rawQuery(query, new String[]{phoneNumber});
    }

    // Insert or update bus location
    public boolean updateBusLocation(double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUS_LOCATION, null);
        ContentValues values = new ContentValues();
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);

        if (cursor.getCount() > 0) {
            long result = db.update(TABLE_BUS_LOCATION, values, COL_ID + " = ?", new String[]{"1"});
            cursor.close();
            return result != -1;
        } else {
            long result = db.insert(TABLE_BUS_LOCATION, null, values);
            cursor.close();
            return result != -1;
        }
    }

    // Get bus location
    public double[] getBusLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_LATITUDE + ", " + COL_LONGITUDE + " FROM " + TABLE_BUS_LOCATION + " LIMIT 1", null);
        double[] location = new double[2];

        if (cursor.moveToFirst()) {
            location[0] = cursor.getDouble(0); // Latitude
            location[1] = cursor.getDouble(1); // Longitude
        }
        cursor.close();
        return location;
    }

    // Insert a message
    public long insertMessage(String senderId, String receiverId, String messageText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SENDER_ID, senderId);
        values.put(COL_RECEIVER_ID, receiverId);
        values.put(COL_MESSAGE_TEXT, messageText);
        return db.insert(TABLE_MESSAGES, null, values);
    }

    // Retrieve messages
    public Cursor getMessages(String senderId, String receiverId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " WHERE (" + COL_SENDER_ID + " = ? AND " + COL_RECEIVER_ID + " = ?) " +
                        "OR (" + COL_SENDER_ID + " = ? AND " + COL_RECEIVER_ID + " = ?) ORDER BY " + COL_TIMESTAMP + " ASC",
                new String[]{senderId, receiverId, receiverId, senderId});
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)));
                user.setIdNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_ID_NUMBER)));
                user.setContactNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_NUMBER)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                user.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO)));
                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    // Validate user login
    public boolean validateUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // Get user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)));
            user.setIdNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_ID_NUMBER)));
            user.setContactNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_NUMBER)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
            user.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
        }
        cursor.close();
        return user;
    }

    // Update user profile
    public boolean updateUserProfile(int userId, String fullName, String contactNumber, String address, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULL_NAME, fullName);
        contentValues.put(COL_CONTACT_NUMBER, contactNumber);
        contentValues.put(COL_ADDRESS, address);
        contentValues.put(COL_PHOTO, photo);
        int result = db.update(TABLE_USERS, contentValues, COL_ID + " = ?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

        // Other methods...

        // Method to delete a user by ID
        public void deleteUser(int id) {
            SQLiteDatabase db = this.getWritableDatabase();

            // Delete user from the database using the id
            db.delete(TABLE_USERS, COL_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
        }

        // Other methods...




            // Method to get users by role as a list
            public List<User> getUsersByRoleAsList(String role) {
                List<User> users = new ArrayList<>();

                // Query to select users by role
                String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_ROLE + " = ?";
                SQLiteDatabase db = this.getReadableDatabase();

                // Execute the query and get the result
                Cursor cursor = db.rawQuery(query, new String[]{role});

                // Loop through the result and create User objects
                // Inside your DatabaseHelper class
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                        String fullName = cursor.getString(cursor.getColumnIndex(COL_FULL_NAME));
                        String contactNumber = cursor.getString(cursor.getColumnIndex(COL_CONTACT_NUMBER));
                        String email = cursor.getString(cursor.getColumnIndex(COL_EMAIL));
                        String userRole = cursor.getString(cursor.getColumnIndex(COL_ROLE));
                        String idNumber = cursor.getString(cursor.getColumnIndex(COL_ID_NUMBER));

                        // Create the User object using the correct constructor
                        User user = new User(id, fullName, contactNumber, email, userRole, idNumber, "", "");
                        users.add(user);
                    } while (cursor.moveToNext());
                }


                cursor.close();
                db.close();

                return users;
            }

            // Other methods...
            public boolean checkUsername(String username) {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(TABLE_USERS, new String[]{COL_FULL_NAME}, COL_FULL_NAME + "=?",
                        new String[]{username}, null, null, null);
                boolean exists = cursor.getCount() > 0;  // If the cursor count is greater than 0, the username exists
                cursor.close();
                db.close();
                return exists;
            }

    public long insertDriverData(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_DRIVERS, null, values); // Replace TABLE_DRIVER with your actual table name
        db.close();
        return result;  // Returns the row ID of the newly inserted row, or -1 if an error occurred
    }

    public Cursor getUsersByRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to select users based on their role
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_ROLE + " = ?";

        // Execute the query and return the Cursor
        return db.rawQuery(query, new String[]{role});
    }

    public User getUserByFullName(String fullName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to select the user by their full name
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_FULL_NAME + " = ?";

        // Execute the query and get a cursor
        Cursor cursor = db.rawQuery(query, new String[]{fullName});

        // If the cursor has results, create a User object from the first result
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String contactNumber = cursor.getString(cursor.getColumnIndex(COL_CONTACT_NUMBER));
            String email = cursor.getString(cursor.getColumnIndex(COL_EMAIL));
            String role = cursor.getString(cursor.getColumnIndex(COL_ROLE));
            String idNumber = cursor.getString(cursor.getColumnIndex(COL_ID_NUMBER));
            String photo = cursor.getString(cursor.getColumnIndex(COL_PHOTO));
            String address = cursor.getString(cursor.getColumnIndex(COL_ADDRESS));

            // Create a User object using the data retrieved from the cursor
            User user = new User(id, fullName, contactNumber, email, role, idNumber, photo, address);

            cursor.close();  // Always close the cursor
            return user;
        } else {
            cursor.close();
            return null;  // Return null if no user found
        }
    }

}


