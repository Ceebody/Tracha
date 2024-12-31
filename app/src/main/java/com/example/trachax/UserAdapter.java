package com.example.trachax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    private List<User> userList;
    private final OnItemClickListener onItemClickListener;

    public UserAdapter(List<User> userList, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public void removeUser(User user) {
        int position = userList.indexOf(user);
        if (position >= 0) {
            userList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateUserList(List<User> newUserList) {
        this.userList = newUserList;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView userNameTextView;
        private final TextView userRoleTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userRoleTextView = itemView.findViewById(R.id.userRoleTextView);
        }

        public void bind(User user, OnItemClickListener listener) {
            if (user != null) {
                userNameTextView.setText(user.getFullName());
                userRoleTextView.setText(user.getRole());
                itemView.setOnClickListener(v -> listener.onItemClick(user));
            }
        }
    }
}
