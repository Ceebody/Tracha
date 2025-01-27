package com.example.trachax;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "message_notifications";

    private EditText messageInput;
    private Button sendMessageButton;
    private ListView messagesList;

    private String parentId;
    private String senderId;

    private DatabaseReference messagesRef;
    private ArrayList<HashMap<String, String>> messagesListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize UI elements
        messageInput = findViewById(R.id.message_input);
        sendMessageButton = findViewById(R.id.send_message_button);
        messagesList = findViewById(R.id.messages_list);

        // Retrieve user IDs from Intent
        parentId = getIntent().getStringExtra("PARENT_ID");
        senderId = getIntent().getStringExtra("SENDER_ID");

        // Check if user IDs are valid
        if (parentId == null || senderId == null) {
            Toast.makeText(this, "Invalid user IDs", Toast.LENGTH_LONG).show();
            finish(); // Close activity if IDs are missing
            return;
        }

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");

        messagesListData = new ArrayList<>();

        // Set up notification channel
        createNotificationChannel();

        // Load messages for the conversation
        loadMessages();

        // Send message button listener
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
        // Generate a unique conversation ID
        String conversationId = generateConversationId(senderId, parentId);

        // Generate a unique message ID
        String messageId = messagesRef.child(conversationId).push().getKey();

        if (messageId != null) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("senderId", senderId);
            message.put("receiverId", parentId);
            message.put("messageText", messageText);
            message.put("timestamp", System.currentTimeMillis());

            // Save message to Firebase
            messagesRef.child(conversationId).child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                        messageInput.setText(""); // Clear the input field
                        showNotification("New Message", "Message sent successfully!");
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show());
        }
    }

    private String generateConversationId(String sender, String receiver) {
        // Ensure both sender and receiver are not null
        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender or Receiver ID cannot be null");
        }
        // Generate a unique conversation ID based on user IDs
        return (sender.compareTo(receiver) < 0) ? sender + "_" + receiver : receiver + "_" + sender;
    }

    private void loadMessages() {
        // Generate the conversation ID
        String conversationId = generateConversationId(senderId, parentId);

        // Listen for changes in the conversation messages
        messagesRef.child(conversationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                messagesListData.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    HashMap<String, String> message = new HashMap<>();
                    message.put("messageText", messageSnapshot.child("messageText").getValue(String.class));
                    message.put("timestamp", messageSnapshot.child("timestamp").getValue(String.class));
                    messagesListData.add(message);
                }
                updateMessageList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ContactActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMessageList() {
        // Update ListView with the latest messages
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                messagesListData,
                android.R.layout.simple_list_item_2,
                new String[]{"messageText", "timestamp"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        messagesList.setAdapter(adapter);
    }

    private void createNotificationChannel() {
        // Create notification channel for devices running Android O or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Message Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for new messages");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification(String title, String message) {
        // Display a notification
        Intent intent = new Intent(this, ContactActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo) // Ensure `logo` drawable exists
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
