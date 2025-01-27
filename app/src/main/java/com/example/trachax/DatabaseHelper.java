package com.example.trachax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    // Parent Table
    private static final String TABLE_PARENTS = "parents";
    private static final String COL_PARENT_NAME = "parent_name";
    private static final String COL_PARENT_PHONE = "parent_phone";


    // Drivers Table
    private static final String TABLE_DRIVERS = "drivers";
    private static final String COL_CHILD_NAME = "child_name";
    private static final String COL_PHONE_NUMBER = "phone_number";
    private static final String COL_DRIVER_NAME = "driver_name";
    private static final String COL_DRIVER_PHONE = "driver_phone";
    private static final String COL_PARENT_ID = "parent_id";  // Foreign Key


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

    // Add constants for the Children table
    private static final String TABLE_CHILDREN = "children";
    private static final String COL_CHILD_ID = "child_id";
    private static final String COL_CHILD_AGE = "child_age";
    private static final String COL_CHILD_CLASS = "child_class";
    private static final String COL_CHILD_PHOTO = "child_photo";
    private static final String COL_CHILD_EMERGENCY_CONTACTS = "child_emergency_contacts";
    private static final String COL_CHILD_GENDER = "gender";


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


        // Create the Parents Table
        String createParentsTable = "CREATE TABLE " + TABLE_PARENTS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PARENT_NAME + " TEXT NOT NULL, "
                + COL_PARENT_PHONE + " TEXT NOT NULL);";
        db.execSQL(createParentsTable);

        //create driver table
        String createDriversTable = "CREATE TABLE " + TABLE_DRIVERS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DRIVER_NAME + " TEXT NOT NULL, "
                + COL_DRIVER_PHONE + " TEXT NOT NULL, "
                + COL_PARENT_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COL_PARENT_ID + ") REFERENCES " + TABLE_PARENTS + "(" + COL_ID + "));";

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

        // Create Children Table
        String createChildrenTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CHILDREN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "gender TEXT, " +
                "child_class INTEGER, " +
                "school TEXT, " +
                "age INTEGER, " +
                "name TEXT, " +
                "parent_id INTEGER, " +
                "FOREIGN KEY(parent_id) REFERENCES " + TABLE_USERS + "(id));";
        db.execSQL(createChildrenTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
   /* @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Alter tables or drop and recreate necessary tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_LOCATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);

            onCreate(db);
        }
    }


    // Insert a new child
    public boolean insertChild(int parentId, String name, int age, int childClass, String photo, String emergencyContacts, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PARENT_ID, parentId);
        contentValues.put(COL_CHILD_NAME, name);
        contentValues.put(COL_CHILD_AGE, age);
        contentValues.put(COL_CHILD_CLASS, childClass);  // Changed to Integer
        contentValues.put(COL_CHILD_PHOTO, photo);
        contentValues.put(COL_CHILD_EMERGENCY_CONTACTS, emergencyContacts);
        contentValues.put(COL_CHILD_GENDER, gender);
        long result = db.insert(TABLE_CHILDREN, null, contentValues);
        return result != -1;
    }


    // Insert a new user
    public long insertUser(String fullName, String idNumber, String contactNumber, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Hash the password before storing it
        String hashedPassword = hashPassword(password);

        contentValues.put("full_name", fullName);
        contentValues.put("id_number", idNumber);
        contentValues.put("contact_number", contactNumber);
        contentValues.put("email", email);
        contentValues.put("password", hashedPassword); // Store hashed password
        contentValues.put("role", role);

        // Insert the record and return the row ID
        return db.insert("users", null, contentValues);
    }



    // Hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes); // Convert byte array to hex string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // In case hashing fails, return original password (not recommended)
        }
    }

    // Convert byte array to hexadecimal string
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    //insert parent
    public boolean insertParent(String parentName, String phoneNumber, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PARENT_NAME, parentName);
        values.put(COL_PHONE_NUMBER, phoneNumber);
        values.put(COL_EMAIL, email);

        long result = db.insert(TABLE_PARENTS, null, values);
        return result != -1;
    }



    // Insert Driver
    public boolean insertDriver(String driverName, String driverPhone, int parentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Insert values
        values.put(COL_DRIVER_NAME, driverName);
        values.put(COL_DRIVER_PHONE, driverPhone);
        values.put(COL_PARENT_ID, parentId);

        // Attempt to insert the record
        long result = db.insert(TABLE_DRIVERS, null, values);

        // Return true if successful, false otherwise
        return result != -1;
    }


    // Retrieve All Parents
    public List<String> getAllParents() {
        List<String> parents = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_PARENT_NAME + " FROM " + TABLE_PARENTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                parents.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_PARENT_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return parents;
    }


    // Retrieve All Drivers for a Parent
    public List<String> getDriversByParentId(int parentId) {
        List<String> drivers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_DRIVER_NAME + " FROM " + TABLE_DRIVERS + " WHERE " + COL_PARENT_ID + " = ?";

        // Correctly pass the parentId as a String array parameter
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(parentId)});

        if (cursor.moveToFirst()) {
            do {
                drivers.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_DRIVER_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return drivers;
    }


    // Retrieve Parent by Email
    public int getParentIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_ID + " FROM " + TABLE_PARENTS + " WHERE " + COL_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        int parentId = -1;
        if (cursor.moveToFirst()) {
            parentId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        }
        cursor.close();
        return parentId;
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

        // Method to delete a user by ID
        public void deleteUser(int id) {
            SQLiteDatabase db = this.getWritableDatabase();

            // Delete user from the database using the id
            db.delete(TABLE_USERS, COL_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
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
        return db.insert(TABLE_DRIVERS, null, values);
    }



    // Method to get users by role as a list
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_ROLE + " = ?", new String[]{role});

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

            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;  // Return null if no user found
        }
    }

    // Update child details
    public boolean updateChild(int childId, String name, int age, String childClass, String photo, String emergencyContacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CHILD_NAME, name);
        contentValues.put(COL_CHILD_AGE, age);
        contentValues.put(COL_CHILD_CLASS, childClass);
        contentValues.put(COL_CHILD_PHOTO, photo);
        contentValues.put(COL_CHILD_EMERGENCY_CONTACTS, emergencyContacts);
        int result = db.update(TABLE_CHILDREN, contentValues, COL_CHILD_ID + " = ?", new String[]{String.valueOf(childId)});
        return result > 0;
    }

    public void deleteChild(int childId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Ensure you are using the correct column name
        String query = "DELETE FROM children WHERE id = ?";
        db.execSQL(query, new Object[]{childId});
    }


    public List<ChildModel> getAllChildren() {
        List<ChildModel> children = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("children", null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                String gender = cursor.getString(cursor.getColumnIndex("gender"));
                String school = cursor.getString(cursor.getColumnIndex("school"));
                String childClass = cursor.getString(cursor.getColumnIndex("child_class"));

                ChildModel child = new ChildModel(id, name, age, gender, school, childClass);
                children.add(child);
            }
            cursor.close();
        }
        return children;
    }

    public long addChild(ChildModel child) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", child.getName());
        values.put("age", child.getAge());
        values.put("gender", child.getGender());
        values.put("school", child.getSchool());
        values.put("child_class", child.getChildClass());

        long id = db.insert("children", null, values);
        db.close();
        return id;
    }

    public Cursor getDriverDetailsByPhone(String phoneNumber) {
        return null;
    }

    public class Driver {
        private String childName;
        private String parentName;
        private String phoneNumber;

        public Driver(String childName, String parentName, String phoneNumber) {
            this.childName = childName;
            this.parentName = parentName;
            this.phoneNumber = phoneNumber;
        }

        public String getChildName() {
            return childName;
        }

        public String getParentName() {
            return parentName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
        public Cursor getDriverDetailsByPhone(String phoneNumber) {


            SQLiteDatabase db = this.getReadableDatabase(); // Use 'this' to refer to the current instance of DatabaseHelper
            String query = "SELECT " + COL_PARENT_NAME + ", " + COL_CHILD_NAME + " FROM " + TABLE_DRIVERS + " WHERE " + COL_PHONE_NUMBER + " = ?";

            // Execute the query and return the cursor
            return db.rawQuery(query, new String[]{phoneNumber});
        }

        private SQLiteDatabase getReadableDatabase() {
            return null;
        }

        public String getName() {
            return "";
        }
    }
    public boolean validateParentLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, "email = ?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPasswordHash = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));

            // Compare the entered password (hashed) with the stored password hash
            return storedPasswordHash.equals(hashPassword(password));  // Make sure hashPassword method is implemented
        }
        return false;
    }

    public boolean validateDriverLogin(String email, String password, String carNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DRIVERS, null, "email = ? AND car_number = ?", new String[]{email, carNumber}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPasswordHash = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));

            // Compare the entered password (hashed) with the stored password hash
            return storedPasswordHash.equals(hashPassword(password));  // Make sure hashPassword method is implemented
        }
        return false;
    }
*/




