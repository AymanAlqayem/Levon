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
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private TextView totalPriceView;
    private TextView emptyCartMessage;
    private Button checkoutButton;
    private Button previousOrdersButton;
    private ArrayList<CartItem> cartItems;
    private ArrayList<String> displayItems;
    private ArrayAdapter<CartItem> adapter;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "CartPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    private static final String KEY_ORDERS = "Orders";

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

    private void setUpViews() {
        cartListView = findViewById(R.id.cart_list);
        totalPriceView = findViewById(R.id.total_price);
        emptyCartMessage = findViewById(R.id.empty_cart_message);
        checkoutButton = findViewById(R.id.checkout_button);
        previousOrdersButton = findViewById(R.id.previous_orders_button);
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

        // Save the order before clearing the cart
        saveOrder();

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
        updateCartDisplay();

        Toast.makeText(this, "Checkout successful", Toast.LENGTH_SHORT).show();
    }

    /**
     * Save the current cart as an order
     */
    private void saveOrder() {
        try {
            // Get current orders
            JSONArray ordersArray = new JSONArray(pref.getString(KEY_ORDERS, "[]"));

            // Create new order
            JSONObject order = new JSONObject();

            // Add timestamp
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            order.put("timestamp", timeStamp);

            // Add items
            JSONArray itemsArray = new JSONArray();
            double totalPrice = 0.0;
            for (CartItem cartItem : cartItems) {
                JSONObject itemJson = cartItem.toJson();
                itemsArray.put(itemJson);
                totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();
            }
            order.put("items", itemsArray);
            order.put("total", totalPrice);

            // Add order to orders array
            ordersArray.put(order);

            // Save back to preferences
            editor.putString(KEY_ORDERS, ordersArray.toString());
            editor.commit();
        } catch (JSONException e) {
            Toast.makeText(this, "Error saving order", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpListeners() {
        checkoutButton.setOnClickListener(v -> handleCheckoutClick());
        previousOrdersButton.setOnClickListener(v -> showPreviousOrders());
    }

    /**
     * Show previous orders dialog
     */
    /**
     * Show previous orders dialog
     */
    private void showPreviousOrders() {
        try {
            JSONArray ordersArray = new JSONArray(pref.getString(KEY_ORDERS, "[]"));

            if (ordersArray.length() == 0) {
                Toast.makeText(this, "No previous orders found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a layout to display orders
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);

            // Add each order to the layout
            for (int i = 0; i < ordersArray.length(); i++) {
                try {
                    JSONObject order = ordersArray.getJSONObject(i);
                    String timestamp = order.getString("timestamp");
                    double total = order.getDouble("total");
                    JSONArray items = order.getJSONArray("items");

                    TextView orderView = new TextView(this);
                    orderView.setText(String.format(Locale.getDefault(),
                            "Order #%d - %s\nTotal: $%.2f\nItems: %d",
                            i + 1, timestamp, total, items.length()));
                    orderView.setTextColor(Color.WHITE);
                    orderView.setPadding(0, 8, 0, 8);

                    // Add click listener to show order details
                    int finalI = i;
                    orderView.setOnClickListener(v -> {
                        try {
                            showOrderDetails(ordersArray.getJSONObject(finalI));
                        } catch (JSONException e) {
                            Toast.makeText(CartActivity.this, "Error loading order details", Toast.LENGTH_SHORT).show();
                        }
                    });

                    layout.addView(orderView);

                    // Add divider except for last item
                    if (i < ordersArray.length() - 1) {
                        View divider = new View(this);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 1));
                        divider.setBackgroundColor(Color.GRAY);
                        layout.addView(divider);
                    }
                } catch (JSONException e) {
                    Toast.makeText(this, "Error processing order #" + (i + 1), Toast.LENGTH_SHORT).show();
                    continue;
                }
            }

            // Show the orders in a dialog
            new AlertDialog.Builder(this)
                    .setTitle("Previous Orders")
                    .setView(layout)
                    .setPositiveButton("Close", null)
                    .show();

        } catch (JSONException e) {
            Toast.makeText(this, "Error loading orders", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show details of a specific order
     */
    private void showOrderDetails(JSONObject order) throws JSONException {
        StringBuilder details = new StringBuilder();

        String timestamp = order.getString("timestamp");
        double total = order.getDouble("total");
        JSONArray items = order.getJSONArray("items");

        details.append("Order Date: ").append(timestamp).append("\n\n");
        details.append("Items:\n");

        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            int itemId = itemJson.getInt("item_id");
            int quantity = itemJson.getInt("quantity");

            Item item = null;
            for (Item it : ItemManager.getAllItems(this)) {
                if (it.getId() == itemId) {
                    item = it;
                    break;
                }
            }

            if (item != null) {
                details.append(String.format(Locale.getDefault(),
                        "- %s x%d @ $%.2f each = $%.2f\n",
                        item.getName(), quantity, item.getPrice(), item.getPrice() * quantity));
            }
        }

        details.append(String.format(Locale.getDefault(), "\nTotal: $%.2f", total));

        TextView detailsView = new TextView(this);
        detailsView.setText(details.toString());
        detailsView.setTextColor(Color.WHITE);
        detailsView.setPadding(16, 16, 16, 16);

        new AlertDialog.Builder(this)
                .setTitle("Order Details")
                .setView(detailsView)
                .setPositiveButton("Close", null)
                .show();
    }
}