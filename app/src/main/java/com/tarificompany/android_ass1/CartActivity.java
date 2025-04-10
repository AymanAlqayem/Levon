package com.tarificompany.android_ass1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ArrayList<CartItem> cartItems;
    private ArrayList<String> displayItems;
    private CustomCartAdapter adapter;
    private TextView totalPriceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ListView cartListView = findViewById(R.id.cart_list);
        totalPriceView = findViewById(R.id.total_price);
        Button checkoutButton = findViewById(R.id.checkout_button);

        // Load cart items
        cartItems = new ArrayList<>();
        displayItems = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        String cartJson = prefs.getString("CartItems", "[]");

        try {
            JSONArray jsonArray = new JSONArray(cartJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                cartItems.add(CartItem.fromJson(jsonItem, this));
            }
        } catch (JSONException e) {
            android.util.Log.e("CartActivity", "Error loading cart", e);
            Toast.makeText(this, "Error loading cart", Toast.LENGTH_SHORT).show();
        }

        // Update display
        updateCartDisplay();

        adapter = new CustomCartAdapter(this, cartItems);
        cartListView.setAdapter(adapter);

        // Checkout button with steps
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            showCheckoutStep1();
        });
    }

    private void updateCartDisplay() {
        displayItems.clear();
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            double itemTotal = item.getPrice() * quantity;
            displayItems.add(String.format("%s - $%.2f x %d = $%.2f", item.getName(), item.getPrice(), quantity, itemTotal));
            totalPrice += itemTotal;
        }
        totalPriceView.setText(String.format("Total: $%.2f", totalPrice));
    }

    /**
     * removeItem method that will remove a specific item from cart.
     */
    private void removeItem(int position) {
        cartItems.remove(position);
        saveCart();
        updateCartDisplay();
        adapter.notifyDataSetChanged();
    }

    /**
     * saveCart method that will save cart info into cart shared preferences.
     */
    private void saveCart() {
        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();
        try {
            for (CartItem cartItem : cartItems) {
                jsonArray.put(cartItem.toJson());
            }
            editor.putString("CartItems", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            android.util.Log.e("CartActivity", "Error saving cart", e);
            Toast.makeText(this, "Error saving cart", Toast.LENGTH_SHORT).show();
        }
    }

    private class CustomCartAdapter extends ArrayAdapter<CartItem> {
        public CustomCartAdapter(CartActivity context, ArrayList<CartItem> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_layout_no_add, parent, false);
            }

            CartItem cartItem = getItem(position);
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            double itemTotal = item.getPrice() * quantity;

            TextView itemText = convertView.findViewById(R.id.cart_item_text);
            itemText.setText(String.format("%s - $%.2f x %d = $%.2f", item.getName(), item.getPrice(), quantity, itemTotal));

            ImageButton minusButton = convertView.findViewById(R.id.minus_button);
            minusButton.setOnClickListener(v -> {
                if (quantity > 1) {
                    cartItem.setQuantity(quantity - 1);
                    saveCart();
                    updateCartDisplay();
                    notifyDataSetChanged();
                } else {
                    new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Remove Item")
                            .setMessage("Are you sure you want to remove " + item.getName() + " from the cart?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                removeItem(position);
                                Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

            ImageButton plusButton = convertView.findViewById(R.id.plus_button);
            plusButton.setOnClickListener(v -> {
                if (quantity + 1 <= item.getStock()) {
                    cartItem.setQuantity(quantity + 1);
                    saveCart();
                    updateCartDisplay();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(CartActivity.this, "Not enough stock available", Toast.LENGTH_SHORT).show();
                }
            });

            ImageView deleteIcon = convertView.findViewById(R.id.delete_icon);
            deleteIcon.setOnClickListener(v -> {
                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Remove Item")
                        .setMessage("Are you sure you want to remove " + item.getName() + " from the cart?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            removeItem(position);
                            Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            return convertView;
        }
    }

    private void showCheckoutStep1() {
        StringBuilder summary = new StringBuilder("Please review your cart:\n\n");
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            double itemTotal = item.getPrice() * quantity;
            summary.append(String.format("%s - $%.2f x %d = $%.2f\n", item.getName(), item.getPrice(), quantity, itemTotal));
            totalPrice += itemTotal;
        }
        summary.append(String.format("\nTotal: $%.2f", totalPrice));

        new AlertDialog.Builder(this)
                .setTitle("Step 1: Review Cart")
                .setMessage(summary.toString())
                .setPositiveButton("Proceed", (dialog, which) -> showShippingStep())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showShippingStep() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Step 2: Shipping Details");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        final EditText addressInput = new EditText(this);
        addressInput.setHint("Enter your shipping address");
        layout.addView(addressInput);

        builder.setView(layout);

        builder.setPositiveButton("Proceed", (dialog, which) -> {
            String address = addressInput.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter a shipping address", Toast.LENGTH_SHORT).show();
                showShippingStep();
            } else {
                showCheckoutStep2();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showCheckoutStep2() {
        new AlertDialog.Builder(this)
                .setTitle("Step 3: Confirm Checkout")
                .setMessage("Are you sure you want to complete the purchase?\n\nThis will update the stock and clear your cart.")
                .setPositiveButton("Confirm", (dialog, which) -> finalizeCheckout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void finalizeCheckout() {
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            item.setStock(item.getStock() - quantity);
        }

        ItemManager.saveItems(this);

        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CartItems", "[]");
        editor.apply();

        cartItems.clear();
        displayItems.clear();
        adapter.notifyDataSetChanged();
        totalPriceView.setText("Total: $0.00");

        Toast.makeText(this, "Checkout successful", Toast.LENGTH_SHORT).show();
    }
}