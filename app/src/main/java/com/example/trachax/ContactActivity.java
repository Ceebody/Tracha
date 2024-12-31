package com.example.trachax;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ContactActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "message_notifications";
    private DatabaseHelper dbHelper;
    private EditText messageInput;
    private Button sendMessageButton;
    private ListView messagesList;
    private String parentId;
    private String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        dbHelper = new DatabaseHelper(this);
        messageInput = findViewById(R.id.message_input);
        sendMessageButton = findViewById(R.id.send_message_button);
        messagesList = findViewById(R.id.messages_list);

        parentId = getIntent().getStringExtra("PARENT_ID");
        senderId = getIntent().getStringExtra("SENDER_ID");

        createNotificationChannel();
        requestNotificationPermission();
        loadMessages();

        sendMessageButton.setOnClickListener(view -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String messageText) {
        long result = dbHelper.insertMessage(senderId, parentId, messageText);
        if (result != -1) {
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
            messageInput.setText("");
            loadMessages();
            showNotification("New Message", "Message sent successfully!");
        } else {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessages() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT id AS _id, sender_id, receiver_id, message_text, timestamp FROM messages",
                null
        );

        String[] fromColumns = {"message_text", "timestamp"};
        int[] toViews = {android.R.id.text1, android.R.id.text2};

        // Set the CursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                fromColumns,
                toViews,
                0
        );

        // Assign the adapter to the ListView
        messagesList.setAdapter(adapter);

        // Do NOT close the cursor here. Adapter will manage it.
    }


    private void requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Message Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for new messages");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
