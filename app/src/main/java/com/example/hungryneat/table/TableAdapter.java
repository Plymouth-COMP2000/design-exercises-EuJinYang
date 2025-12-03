package com.example.hungryneat.table;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

import com.example.hungryneat.R;

public class TableAdapter extends BaseAdapter {

    private Context context;
    private List<Table> tables;
    private LayoutInflater inflater;

    public TableAdapter(Context context, List<Table> tables) {
        this.context = context;
        this.tables = tables;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tables.size();
    }

    @Override
    public Object getItem(int position) {
        return tables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.table_item_layout, parent, false);
            holder = new ViewHolder();
            holder.cardView = convertView.findViewById(R.id.tableCardView);
            holder.tableNumber = convertView.findViewById(R.id.tableNumber);
            holder.tableStatus = convertView.findViewById(R.id.tableStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Table table = tables.get(position);
        holder.tableNumber.setText(table.getName());
        holder.tableStatus.setText(table.getStatus().getDisplayName());

        // Set background color from enum
        holder.cardView.setCardBackgroundColor(Color.parseColor(table.getStatus().getColorCode()));

        return convertView;
    }

    private static class ViewHolder {
        MaterialCardView cardView;
        TextView tableNumber;
        TextView tableStatus;
    }
}