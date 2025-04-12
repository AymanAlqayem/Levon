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
    private TextView emptyCartMessage;
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
     * setUpViews method that will initialize the views..
     */
    private void setUpViews() {
        cartListView = findViewById(R.id.cart_list);
        totalPriceView = findViewById(R.id.total_price);
        emptyCartMessage = findViewById(R.id.empty_cart_message); // Initialize the TextView
        checkoutButton = findViewById(R.id.checkout_button);
    }

    /**
     * setUpSharedPref method that setup the shared preferences.
     */
    private void setUpSharedPref() {
        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * setUpCartItems method that will initialize the cart items.
     */
    private void setUpCartItems() {
        cartItems = new ArrayList<>();
        displayItems = new ArrayList<>();

        String cartJson = pref.getString(KEY_CART_ITEMS, "[]");
        try {
            JSONArray jsonArray = new JSONArray(cartJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                CartItem cartItem = CartItem.fromJson(jsonItem, this);
                if (cartItem.getQuantity() <= 0) {
                    continue;
                }
                cartItems.add(cartItem);
            }
            saveCart();
        } catch (JSONException e) {
            Toast.makeText(this, "Error loading cart", Toast.LENGTH_SHORT).show();
            editor.putString(KEY_CART_ITEMS, "[]");
            editor.commit();
        }
        updateCartDisplay();
    }

    /**
     * setUpListView method that will initialize the cart items list view.
     */
    private void setUpListView() {
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
                itemText.setText(String.format("%s - $%.2f x %d = $%.2f (Stock: %d)",
                        item.getName(), item.getPrice(), quantity, itemTotal, item.getStock()));

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
                    int currentStock = item.getStock();
                    int totalQuantityInCart = cartItem.getQuantity() + 1;
                    if (totalQuantityInCart > currentStock) {
                        Toast.makeText(CartActivity.this, "Not enough stock available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cartItem.setQuantity(totalQuantityInCart);
                    saveCart();
                    updateCartDisplay();
                    notifyDataSetChanged();
                });

                ImageView deleteIcon = convertView.findViewById(R.id.delete_icon);
                deleteIcon.setOnClickListener(v -> {
                    TextView messageTextView = new TextView(CartActivity.this);
                    messageTextView.setText("Are you sure you want to remove " + item.getName() + " from the cart?");
                    messageTextView.setTextColor(Color.WHITE);
                    messageTextView.setPadding(32, 32, 32, 32);
                    messageTextView.setTextSize(16);

                    new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Remove Item")
                            .setView(messageTextView)
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

    private void setUpListeners() {
        checkoutButton.setOnClickListener(v -> handleCheckoutClick());
    }

    /**
     * handleCheckoutClick method that will handle the checkout button.
     */
    private void handleCheckoutClick() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        showCheckoutStep1();
    }

    /**
     * updateCartDisplay method that wil update the cart view.
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

        // Toggle visibility of empty cart message, cart list, total price, and checkout button
        if (cartItems.isEmpty()) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            cartListView.setVisibility(View.GONE);
            totalPriceView.setVisibility(View.GONE); // Hide total price when cart is empty
            checkoutButton.setVisibility(View.GONE); // Hide checkout button when cart is empty
        } else {
            emptyCartMessage.setVisibility(View.GONE);
            cartListView.setVisibility(View.VISIBLE);
            totalPriceView.setVisibility(View.VISIBLE); // Show total price when cart has items
            checkoutButton.setVisibility(View.VISIBLE); // Show checkout button when cart has items
        }
    }

    /**
     * removeItem method that will remove a specific item from the cart.
     */
    private void removeItem(int position) {
        cartItems.remove(position);
        saveCart();
        updateCartDisplay();
        adapter.notifyDataSetChanged();
    }

    /**
     * saveCart method that will save the cart items.
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
            editor.putString(KEY_CART_ITEMS, "[]");
            editor.commit();
            Toast.makeText(this, "Error saving cart", Toast.LENGTH_SHORT).show();
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

        TextView messageTextView = new TextView(this);
        messageTextView.setText(summary.toString());
        messageTextView.setTextColor(Color.WHITE);
        messageTextView.setPadding(16, 16, 16, 16);

        new AlertDialog.Builder(this)
                .setTitle("Step 1: Review Cart")
                .setView(messageTextView)
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
        TextView messageTextView = new TextView(this);
        messageTextView.setText("Are you sure you want to complete the purchase?\n\nThis will update the stock and clear your cart.");
        messageTextView.setTextColor(Color.WHITE);
        messageTextView.setPadding(16, 16, 16, 16);

        new AlertDialog.Builder(this)
                .setTitle("Step 3: Confirm Checkout")
                .setView(messageTextView)
                .setPositiveButton("Confirm", (dialog, which) -> finalizeCheckout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * finalizeCheckout method that will finish the checkout process.
     */
    private void finalizeCheckout() {
        // Verify stock availability before finalizing
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            int currentStock = item.getStock();
            if (quantity > currentStock) {
                Toast.makeText(this, "Not enough stock for " + item.getName(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Update stock
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            int newStock = item.getStock() - quantity;
            if (newStock < 0) {
                newStock = 0; // Prevent negative stock
            }
            item.setStock(newStock);
        }

        ItemManager.saveItemsToSharedPref();

        editor.putString(KEY_CART_ITEMS, "[]");
        editor.commit();

        cartItems.clear();
        displayItems.clear();
        adapter.notifyDataSetChanged();
        totalPriceView.setText("Total: $0.00");

        Toast.makeText(this, "Checkout successful", Toast.LENGTH_SHORT).show();
    }
}