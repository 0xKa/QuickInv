package com.example.quickinv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickinv.R;
import com.example.quickinv.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemEdit(Item item);
        void onItemDelete(Item item);
    }

    public ItemAdapter(List<Item> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.quantityTextView.setText("Qty: " + item.getQuantity());
        holder.priceTextView.setText("$" + String.format("%.2f", item.getPrice()));
        holder.descriptionTextView.setText(item.getDescription());

        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemEdit(item));
        holder.deleteButton.setOnClickListener(v -> onItemClickListener.onItemDelete(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView quantityTextView;
        TextView priceTextView;
        TextView descriptionTextView;
        Button editButton;
        Button deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name_text);
            quantityTextView = itemView.findViewById(R.id.quantity_text);
            priceTextView = itemView.findViewById(R.id.price_text);
            descriptionTextView = itemView.findViewById(R.id.description_text);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
