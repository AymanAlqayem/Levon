package com.tarificompany.android_ass1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {
    private ArrayList<Item> favoriteItems;
    private CustomFavoritesAdapter adapter;
    private ListView favoritesListView;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize views
        favoritesListView = findViewById(R.id.favorites_list);
        emptyMessage = findViewById(R.id.empty_favorites_message);

        // Load favorite items
        favoriteItems = new ArrayList<>();
        loadFavorites();

        adapter = new CustomFavoritesAdapter(this, favoriteItems);
        favoritesListView.setAdapter(adapter);

        // Handle item clicks to go to ItemDetailActivity
        favoritesListView.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = favoriteItems.get(position);
            Intent intent = new Intent(FavoritesActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", selectedItem.getId());
            intent.putExtra("item_name", selectedItem.getName());
            intent.putExtra("item_desc", selectedItem.getDescription());
            intent.putExtra("item_image", selectedItem.getImageId());
            intent.putExtra("item_price", selectedItem.getPrice());
            startActivity(intent);
        });
    }

    private void loadFavorites() {
        SharedPreferences prefs = getSharedPreferences("FavoritesPrefs", MODE_PRIVATE);
        Set<String> favoriteSet = prefs.getStringSet("FavoriteItems", new HashSet<>());
        favoriteItems.clear();

        for (String idStr : favoriteSet) {
            try {
                int id = Integer.parseInt(idStr);
                for (Item item : ItemManager.getAllItems(this)) {
                    if (item.getId() == id) {
                        favoriteItems.add(item);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                android.util.Log.e("FavoritesActivity", "Error parsing favorite item ID: " + idStr, e);
            }
        }

        // Update visibility of empty message and list view
        if (favoriteItems.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
            favoritesListView.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            favoritesListView.setVisibility(View.VISIBLE);
        }
    }

    private void saveFavorites() {
        SharedPreferences prefs = getSharedPreferences("FavoritesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> favoriteSet = new HashSet<>();
        for (Item item : favoriteItems) {
            favoriteSet.add(String.valueOf(item.getId()));
        }
        editor.putStringSet("FavoriteItems", favoriteSet);
        editor.apply();
    }

    private void removeItem(int position) {
        Item removedItem = favoriteItems.get(position);
        favoriteItems.remove(position);
        saveFavorites();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, removedItem.getName() + " removed from favorites", Toast.LENGTH_SHORT).show();
    }

    private class CustomFavoritesAdapter extends ArrayAdapter<Item> {
        public CustomFavoritesAdapter(FavoritesActivity context, ArrayList<Item> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_item_layout, parent, false);
            }

            Item item = getItem(position);

            TextView itemText = convertView.findViewById(R.id.cart_item_text);
            itemText.setText(String.format("%s - $%.2f (%d in stock)", item.getName(), item.getPrice(), item.getStock()));

            ImageView deleteIcon = convertView.findViewById(R.id.delete_icon);
            deleteIcon.setOnClickListener(v -> {
                new AlertDialog.Builder(FavoritesActivity.this)
                        .setTitle("Remove from Favorites")
                        .setMessage("Are you sure you want to remove " + item.getName() + " from your favorites?")
                        .setPositiveButton("Yes", (dialog, which) -> removeItem(position))
                        .setNegativeButton("No", null)
                        .show();
            });

            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
        adapter.notifyDataSetChanged();
    }
}
