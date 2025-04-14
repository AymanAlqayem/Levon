package com.tarificompany.android_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {
    private EditText searchQuery;
    private Spinner categorySpinner;
    private RadioGroup sortRadioGroup;
    private CheckBox inStockCheckbox;

    private Button searchButton;
    private Switch caseSensitiveSwitch;
    private ListView searchResultsListView;
    private ArrayList<Item> allItems;
    private ArrayList<Item> filteredItems;
    private ArrayAdapter<Item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpViews();
        loadAllItems();
        setupCategorySpinner();
        setupSearchResultsAdapter();
        handleSearchButton();
        handleResultListView();
    }

    /**
     * setUpViews method that will initialize the hooks.
     */
    private void setUpViews() {
        searchQuery = findViewById(R.id.search_query);
        categorySpinner = findViewById(R.id.category_spinner);
        sortRadioGroup = findViewById(R.id.sort_radio_group);
        inStockCheckbox = findViewById(R.id.in_stock_checkbox);
        caseSensitiveSwitch = findViewById(R.id.case_sensitive_switch);
        searchResultsListView = findViewById(R.id.search_results);
        searchButton = findViewById(R.id.search_button);
    }

    /**
     * loadAllItems method that will load categories items.
     */
    private void loadAllItems() {
        allItems = ItemManager.getAllItems(this);
        filteredItems = new ArrayList<>();
    }

    /**
     * setupCategorySpinner method that will set the spinner category.
     */
    private void setupCategorySpinner() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("All Categories");
        categories.add("Watches");
        categories.add("Gaming");
        categories.add("Fitness");
        categories.add("Perfume");
        categories.add("T-shirt");
        categories.add("Jeans");
        categories.add("Jacket");
        categories.add("Shoes");
        categories.add("Home");
        categories.add("Laptops");
        categories.add("Playstation consoles");
        categories.add("Playstation console games");
        categories.add("Airpods");
        categories.add("Phone cases");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_white, categories);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_white);
        categorySpinner.setAdapter(categoryAdapter);
    }

    /**
     * that will setup the search Results ListView
     */
    private void setupSearchResultsAdapter() {
        adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, filteredItems) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                Item item = getItem(position);
                textView.setText(String.format("%s - $%.2f (%d in stock)", item.getName(), item.getPrice(), item.getStock()));
                return textView;
            }
        };
        searchResultsListView.setAdapter(adapter);
    }

    /**
     * handleSearchButton method that will handle the search button.
     */
    private void handleSearchButton() {
        searchButton.setOnClickListener(v -> performSearch());
    }

    /**
     * handleResultListView method that will handle the the result list view.
     */
    private void handleResultListView() {
        searchResultsListView.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = filteredItems.get(position);
            Intent intent = new Intent(SearchActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", selectedItem.getId());
            intent.putExtra("item_name", selectedItem.getName());
            intent.putExtra("item_desc", selectedItem.getDescription());
            intent.putExtra("item_image", selectedItem.getImageId());
            intent.putExtra("item_price", selectedItem.getPrice());
            startActivity(intent);
        });
    }

    /**
     * performSearch method that will find items for user search input.
     */
    private void performSearch() {
        String query = searchQuery.getText().toString().trim();
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        boolean inStockOnly = inStockCheckbox.isChecked();
        boolean caseSensitive = caseSensitiveSwitch.isChecked();
        boolean sortByPrice = sortRadioGroup.getCheckedRadioButtonId() == R.id.sort_price;

        filteredItems.clear();

        filterItems(query, selectedCategory, inStockOnly, caseSensitive);
        sortItems(sortByPrice);

        adapter.notifyDataSetChanged();
    }

    private void filterItems(String query, String selectedCategory, boolean inStockOnly, boolean caseSensitive) {
        for (Item item : allItems) {
            // Category filter
            if (!selectedCategory.equals("All Categories") && !item.getCategory().equals(selectedCategory)) {
                continue;
            }

            // Stock filter
            if (inStockOnly && item.getStock() <= 0) {
                continue;
            }

            // Search query filter with case sensitivity
            String itemName = item.getName();
            String searchText = query;

            // If caseSensitive is false, convert both to lowercase for comparison
            if (!caseSensitive) {
                itemName = itemName.toLowerCase();
                searchText = searchText.toLowerCase();
            }

            // Check if the item name starts with the search text (case-sensitive or insensitive)
            if (query.isEmpty() || itemName.startsWith(searchText)) {
                filteredItems.add(item);
            }
        }
    }

    /**
     * sortItems method that will sort items bases on price or availability.
     */
    private void sortItems(boolean sortByPrice) {
        if (sortByPrice) {
            Collections.sort(filteredItems, (item1, item2) -> Double.compare(item1.getPrice(), item2.getPrice()));
        } else {
            Collections.sort(filteredItems, (item1, item2) -> item1.getName().compareTo(item2.getName()));
        }
    }
}
