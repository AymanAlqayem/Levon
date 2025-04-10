package com.tarificompany.android_ass1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ItemDetailActivity extends AppCompatActivity {
    private Item item;
    private int quantity = 1; // Default quantity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Get item details from intent
        Intent intent = getIntent();
        int itemId = intent.getIntExtra("item_id", -1);
        String itemName = intent.getStringExtra("item_name");
        String itemDesc = intent.getStringExtra("item_desc");
        int itemImage = intent.getIntExtra("item_image", 0);
        double itemPrice = intent.getDoubleExtra("item_price", 0.0);

        // Find the item in ItemManager to get the stock
        item = null;
        for (Item i : ItemManager.getAllItems(this)) {
            if (i.getId() == itemId) {
                item = i;
                break;
            }
        }

        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        ImageView itemImageView = findViewById(R.id.item_image);
        TextView itemNameView = findViewById(R.id.item_name);
        TextView itemDescView = findViewById(R.id.item_description);
        TextView itemPriceView = findViewById(R.id.item_price);
        TextView stockMessageView = findViewById(R.id.stock_message);
        TextView quantityText = findViewById(R.id.quantity_text);
        ImageButton minusButton = findViewById(R.id.minus_button);
        ImageButton plusButton = findViewById(R.id.plus_button);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        Button favoriteButton = findViewById(R.id.favorite_button);

        // Set item details
        itemImageView.setImageResource(itemImage);
        itemNameView.setText(itemName);
        itemDescView.setText(itemDesc);
        itemPriceView.setText(String.format("$%.2f", itemPrice));
        stockMessageView.setText("In stock: " + item.getStock());
        quantityText.setText(String.valueOf(quantity));

        // Handle minus button
        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        // Handle plus button
        plusButton.setOnClickListener(v -> {
            if (quantity < item.getStock()) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Cannot exceed available stock", Toast.LENGTH_SHORT).show();
            }
        });

        // Add to cart
        addToCartButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String cartJson = prefs.getString("CartItems", "[]");
            ArrayList<CartItem> cartItems = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(cartJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    cartItems.add(CartItem.fromJson(jsonItem, ItemDetailActivity.this));
                }

                boolean itemExists = false;
                for (CartItem cartItem : cartItems) {
                    if (cartItem.getItem().getId() == item.getId()) {
                        int newQuantity = cartItem.getQuantity() + quantity;
                        if (newQuantity > item.getStock()) {
                            Toast.makeText(this, "Not enough stock available", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cartItem.setQuantity(newQuantity);
                        itemExists = true;
                        break;
                    }
                }

                if (!itemExists) {
                    cartItems.add(new CartItem(item, quantity));
                }

                JSONArray updatedArray = new JSONArray();
                for (CartItem cartItem : cartItems) {
                    updatedArray.put(cartItem.toJson());
                }
                editor.putString("CartItems", updatedArray.toString());
                editor.apply();

                Toast.makeText(this, item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                android.util.Log.e("ItemDetailActivity", "Error updating cart", e);
                Toast.makeText(this, "Error adding to cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Add to favorites
        favoriteButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("FavoritesPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Set<String> favoriteSet = new HashSet<>(prefs.getStringSet("FavoriteItems", new HashSet<>()));

            if (favoriteSet.contains(String.valueOf(item.getId()))) {
                favoriteSet.remove(String.valueOf(item.getId()));
                Toast.makeText(this, item.getName() + " removed from favorites", Toast.LENGTH_SHORT).show();
                favoriteButton.setText("Add to Favorites");
            } else {
                favoriteSet.add(String.valueOf(item.getId()));
                Toast.makeText(this, item.getName() + " added to favorites", Toast.LENGTH_SHORT).show();
                favoriteButton.setText("Remove from Favorites");
            }

            editor.putStringSet("FavoriteItems", favoriteSet);
            editor.apply();
        });

        // Update favorite button text based on current state
        SharedPreferences prefs = getSharedPreferences("FavoritesPrefs", MODE_PRIVATE);
        Set<String> favoriteSet = prefs.getStringSet("FavoriteItems", new HashSet<>());
        if (favoriteSet.contains(String.valueOf(item.getId()))) {
            favoriteButton.setText("Remove from Favorites");
        } else {
            favoriteButton.setText("Add to Favorites");
        }
    }
}