package com.example.recyclerviewlazyload.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapterWithViewModel extends RecyclerView.Adapter<ItemAdapterWithViewModel.ItemViewHolder> {
    private final List<String> items;

    public ItemAdapterWithViewModel(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Add items to the list without duplicating existing items
    public void addItems(List<String> newItems) {
        int startPosition = items.size();
        items.clear();
        items.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}