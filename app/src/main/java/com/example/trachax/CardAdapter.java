package com.example.trachax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends BaseAdapter {
    private Context context;
    private List<CardItem> cardItems;

    public CardAdapter(Context context, List<CardItem> cardItems) {
        this.context = context;
        this.cardItems = cardItems;
    }

    @Override
    public int getCount() {
        return cardItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cardItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_card_adapter, parent, false);
        }

        CardItem cardItem = cardItems.get(position);

        ImageView iconImageView = convertView.findViewById(R.id.card_icon);
        TextView titleTextView = convertView.findViewById(R.id.card_title);

        iconImageView.setImageResource(cardItem.getIcon());
        titleTextView.setText(cardItem.getTitle());

        return convertView;
    }
}