package com.example.quickinv.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickinv.R;
import com.example.quickinv.adapters.ItemAdapter;
import com.example.quickinv.database.ItemDAO;
import com.example.quickinv.models.Item;
import com.example.quickinv.utils.SessionManager;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.quickinv.utils.CurrencyConverter;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private RecyclerView itemsRecyclerView;
    private Button addItemButton;
    private TextView emptyStateTextView;
    private ItemAdapter itemAdapter;
    private ItemDAO itemDAO;
    private SessionManager sessionManager;
    private List<Item> itemList;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize views
        itemsRecyclerView = findViewById(R.id.items_recycler_view);
        addItemButton = findViewById(R.id.add_item_button);
        emptyStateTextView = findViewById(R.id.empty_state_text);

        // Initialize managers
        sessionManager = new SessionManager(this);
        itemDAO = new ItemDAO(this);
        currentUserId = sessionManager.getUserId();

        // Setup RecyclerView
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, this);
        itemsRecyclerView.setAdapter(itemAdapter);

        // Load items
        loadItems();

        // Set up button listener
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });

        // Currency converter button
        Button currencyButton = findViewById(R.id.currency_button);
        currencyButton.setOnClickListener(v -> showCurrencyConverter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        itemList.clear();
        List<Item> items = itemDAO.getAllItemsByUser(currentUserId);
        itemList.addAll(items);
        itemAdapter.notifyDataSetChanged();

        if (itemList.isEmpty()) {
            emptyStateTextView.setText("No items yet. Add your first item!");
        } else {
            emptyStateTextView.setText("");
        }
    }

    private void searchItems(String query) {
        itemList.clear();
        List<Item> searchResults = itemDAO.searchItems(currentUserId, query);
        itemList.addAll(searchResults);
        itemAdapter.notifyDataSetChanged();

        if (itemList.isEmpty()) {
            emptyStateTextView.setText("No items found matching your search");
        } else {
            emptyStateTextView.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadItems();
                } else {
                    searchItems(newText);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            sessionManager.logout();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(InventoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemEdit(Item item) {
        Intent intent = new Intent(InventoryActivity.this, AddEditItemActivity.class);
        intent.putExtra("item_id", item.getId());
        intent.putExtra("edit_mode", true);
        startActivity(intent);
    }

    @Override
    public void onItemDelete(Item item) {
        int result = itemDAO.deleteItem(item.getId());
        if (result > 0) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            loadItems();
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCurrencyConverter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create custom view
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
        android.view.View dialogView = inflater.inflate(R.layout.dialog_currency_converter, null);

        EditText baseCurrencyEditText = dialogView.findViewById(R.id.base_currency);
        EditText targetCurrencyEditText = dialogView.findViewById(R.id.target_currency);
        EditText amountEditText = dialogView.findViewById(R.id.amount_to_convert);
        ProgressBar progressBar = dialogView.findViewById(R.id.conversion_progress);
        TextView resultTextView = dialogView.findViewById(R.id.conversion_result);
        Button convertButton = dialogView.findViewById(R.id.convert_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        convertButton.setOnClickListener(v -> {
            String baseCurrency = baseCurrencyEditText.getText().toString().trim();
            String targetCurrency = targetCurrencyEditText.getText().toString().trim();
            String amountStr = amountEditText.getText().toString().trim();

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(InventoryActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                progressBar.setVisibility(android.view.View.VISIBLE);
                resultTextView.setText("");
                convertButton.setEnabled(false);

                CurrencyConverter converter = new CurrencyConverter(InventoryActivity.this,
                        new CurrencyConverter.OnConversionListener() {
                            @Override
                            public void onSuccess(String result) {
                                runOnUiThread(() -> {
                                    progressBar.setVisibility(android.view.View.GONE);
                                    resultTextView.setText(result);
                                    resultTextView.setTextColor(getResources().getColor(R.color.success_color));
                                    convertButton.setEnabled(true);
                                });
                            }

                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> {
                                    progressBar.setVisibility(android.view.View.GONE);
                                    resultTextView.setText(error);
                                    resultTextView.setTextColor(getResources().getColor(R.color.error_color));
                                    convertButton.setEnabled(true);
                                    Toast.makeText(InventoryActivity.this, error, Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                );

                converter.convertCurrency(baseCurrency, targetCurrency, amount);

            } catch (NumberFormatException e) {
                Toast.makeText(InventoryActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
