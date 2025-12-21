package com.example.quickinv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quickinv.R;
import com.example.quickinv.database.ItemDAO;
import com.example.quickinv.models.Item;
import com.example.quickinv.utils.SessionManager;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText itemNameEditText;
    private EditText quantityEditText;
    private EditText priceEditText;
    private EditText descriptionEditText;
    private Button saveButton;

    private ItemDAO itemDAO;
    private SessionManager sessionManager;
    private int currentUserId;
    private int itemId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize views
        itemNameEditText = findViewById(R.id.item_name_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        saveButton = findViewById(R.id.save_button);

        // Initialize managers
        itemDAO = new ItemDAO(this);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        // Check if editing
        if (getIntent().hasExtra("edit_mode")) {
            isEditMode = getIntent().getBooleanExtra("edit_mode", false);
            if (isEditMode) {
                itemId = getIntent().getIntExtra("item_id", -1);
                loadItemForEdit();
                setTitle("Edit Item");
            } else {
                setTitle("Add New Item");
            }
        } else {
            setTitle("Add New Item");
        }

        saveButton.setOnClickListener(v -> saveItem());
    }

    private void loadItemForEdit() {
        Item item = itemDAO.getItemById(itemId);
        if (item != null) {
            itemNameEditText.setText(item.getName());
            quantityEditText.setText(String.valueOf(item.getQuantity()));
            priceEditText.setText(String.valueOf(item.getPrice()));
            descriptionEditText.setText(item.getDescription());
        }
    }

    private void saveItem() {
        String name = itemNameEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            itemNameEditText.setError("Item name is required");
            return;
        }

        if (quantityStr.isEmpty()) {
            quantityEditText.setError("Quantity is required");
            return;
        }

        if (priceStr.isEmpty()) {
            priceEditText.setError("Price is required");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);

            if (quantity < 0) {
                quantityEditText.setError("Quantity cannot be negative");
                return;
            }

            if (price < 0) {
                priceEditText.setError("Price cannot be negative");
                return;
            }

            if (isEditMode) {
                // Update existing item
                Item updatedItem = new Item(itemId, currentUserId, name, quantity, price, description, "");
                int result = itemDAO.updateItem(updatedItem);

                if (result > 0) {
                    Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add new item
                Item newItem = new Item(currentUserId, name, quantity, price, description);
                long result = itemDAO.addItem(newItem);

                if (result != -1) {
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for quantity and price", Toast.LENGTH_SHORT).show();
        }
    }
}
