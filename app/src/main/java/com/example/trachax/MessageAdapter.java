package com.example.trachax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trachax.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<String> messageList;

    public MessageAdapter(List<String> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each message item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = messageList.get(position);
        holder.messageText.setText(message);  // Set the message to the TextView
    }

    @Override
    public int getItemCount() {
        return messageList.size();  // Return the size of the message list
    }

    // ViewHolder class to hold references to the views in each item
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(View itemView) {
            super(itemView);
            // Initialize the TextView for displaying the message
            messageText = itemView.findViewById(R.id.messageText);
        }
    }
}
