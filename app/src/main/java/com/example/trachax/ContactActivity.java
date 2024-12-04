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

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView messageRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private List<String> messages;

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
        getSupportActionBar().setTitle("Parent Name"); // Change dynamically if needed

        // Initialize message list and adapter
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);

        // Handle send button click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Add the message to the list and update RecyclerView
                    messages.add(message);
                    messageAdapter.notifyItemInserted(messages.size() - 1);

                    // Show a toast (optional)
                    Toast.makeText(ContactActivity.this, "Message Sent: " + message, Toast.LENGTH_SHORT).show();

                    // Scroll to the bottom of the RecyclerView to show the latest message
                    messageRecyclerView.scrollToPosition(messages.size() - 1);

                    // Clear the input field after sending
                    messageInput.setText("");
                } else {
                    // Show an error if the message is empty
                    Toast.makeText(ContactActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
