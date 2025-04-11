package com.tarificompany.android_ass1;

import android.content.SharedPreferences;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private TextView totalPriceView;
    private Button checkoutButton;

    private ArrayList<CartItem> cartItems;
    private ArrayList<String> displayItems;
    private ArrayAdapter<CartItem> adapter;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "CartPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setUpViews();
        setUpSharedPref();
        setUpCartItems();
        setUpListView();
        setUpListeners();
    }

    /**
     * setUpViews method that will initialize the UI components.
     */
    private void setUpViews() {
        cartListView = findViewById(R.id.cart_list);
        totalPriceView = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);
    }

    /**
     * setUpSharedPref method that will initialize the shared preferences objects.
     */
    private void setUpSharedPref() {
        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * setUpCartItems method that will load cart items from shared preferences.
     */
    private void setUpCartItems() {
        cartItems = new ArrayList<>();
        displayItems = new ArrayList<>();

        String cartJson = pref.getString(KEY_CART_ITEMS, "[]");
        try {
            JSONArray jsonArray = new JSONArray(cartJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                cartItems.add(CartItem.fromJson(jsonItem, this));
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error loading cart", Toast.LENGTH_SHORT).show();
            editor.putString(KEY_CART_ITEMS, "[]"); // Reset to empty array
            editor.commit();
        }

        updateCartDisplay();
    }

    /**
     * setUpListView method that will initialize the ListView and its adapter.
     */
    private void setUpListView() {
        // Define the adapter inline using an anonymous class
        adapter = new ArrayAdapter<CartItem>(this, 0, cartItems) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CartActivity.this).inflate(R.layout.cart_item_layout_no_add, parent, false);
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
        };
        cartListView.setAdapter(adapter);
    }

    /**
     * setUpListeners method that will set up click listeners for UI elements.
     */
    private void setUpListeners() {
        checkoutButton.setOnClickListener(v -> handleCheckoutClick());
    }

    /**
     * handleCheckoutClick method that will handle the checkout button click event.
     */
    private void handleCheckoutClick() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        showCheckoutStep1();
    }

    /**
     * updateCartDisplay method that will update the cart display and total price.
     */
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
        JSONArray jsonArray = new JSONArray();
        try {
            for (CartItem cartItem : cartItems) {
                jsonArray.put(cartItem.toJson());
            }
            editor.putString(KEY_CART_ITEMS, jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
            editor.putString(KEY_CART_ITEMS, "[]"); // Reset to empty array
            editor.commit();
            Toast.makeText(this, "Error saving cart", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the first step of the checkout process, showing a summary of cart items
     * and the total price in a dialog with white text color.
     */
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

        // Create a TextView to customize the message text color
        TextView messageTextView = new TextView(this);
        messageTextView.setText(summary.toString());
        messageTextView.setTextColor(Color.WHITE); // Set text color to white
        messageTextView.setPadding(16, 16, 16, 16);

        new AlertDialog.Builder(this)
                .setTitle("Step 1: Review Cart")
                .setView(messageTextView) // Use custom TextView instead of setMessage
                .setPositiveButton("Proceed", (dialog, which) -> showShippingStep())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Displays the second step of the checkout process, prompting the user to enter
     * their shipping address in a dialog.
     */
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

    /**
     * Displays the final step of the checkout process, asking the user to confirm
     * the purchase in a dialog with white text color.
     */
    private void showCheckoutStep2() {
        // Create a TextView to customize the message text color
        TextView messageTextView = new TextView(this);
        messageTextView.setText("Are you sure you want to complete the purchase?\n\nThis will update the stock and clear your cart.");
        messageTextView.setTextColor(Color.WHITE); // Set text color to white
        messageTextView.setPadding(16, 16, 16, 16);

        new AlertDialog.Builder(this)
                .setTitle("Step 3: Confirm Checkout")
                .setView(messageTextView) // Use custom TextView instead of setMessage
                .setPositiveButton("Confirm", (dialog, which) -> finalizeCheckout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * finalizeCheckout method that will complete the checkout process by updating stock and clearing the cart.
     */
    private void finalizeCheckout() {
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            item.setStock(item.getStock() - quantity);
        }

        // Save the updated items to SharedPreferences using the correct method name
        ItemManager.saveItemsToSharedPref();

        // Clear the cart in SharedPreferences
        editor.putString(KEY_CART_ITEMS, "[]");
        editor.commit();

        // Clear the local cart data and update the UI
        cartItems.clear();
        displayItems.clear();
        adapter.notifyDataSetChanged();
        totalPriceView.setText("Total: $0.00");

        Toast.makeText(this, "Checkout successful", Toast.LENGTH_SHORT).show();
    }
}