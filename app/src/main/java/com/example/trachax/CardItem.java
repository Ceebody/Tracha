package com.example.trachax;

public class CardItem {
    private String title;
    private int icon; // Resource ID for the icon
    private int id; // Unique identifier for the card action

    public CardItem(String title, int icon, int id) {
        this.title = title;
        this.icon = icon;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }
}