package com.example.trachax;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trachax.R;

public class ContactActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView messageRecyclerView;
    private EditText messageInput;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize the views
        toolbar = findViewById(R.id.toolbar);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Set up the Toolbar
        setSupportActionBar(toolbar);
        // Set the title of the toolbar to the other party's name (e.g., "Parent" or "Driver")
        getSupportActionBar().setTitle("Parent Name"); // Change this dynamically if needed

        // Set up the RecyclerView to display messages
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // You would typically set an adapter here to display messages in the RecyclerView
        // For now, it's a placeholder, so you should implement an adapter to manage the message list
        // messageRecyclerView.setAdapter(new MessageAdapter(messages));

        // Handle the send button click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Add the message to your message list and update the RecyclerView
                    // For demonstration, just show a toast
                    Toast.makeText(ContactActivity.this, "Message Sent: " + message, Toast.LENGTH_SHORT).show();

                    // Clear the input field after sending
                    messageInput.setText("");
                } else {
                    // Show an error if the message is empty
                    Toast.makeText(ContactActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Optional: Create a method to update the toolbar's title dynamically if needed
    private void updateToolbarTitle(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }
}
