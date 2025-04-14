package com.tarificompany.android_ass1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView userNameText, userTitleText, totalOrdersText, totalSpentText;
    private Button logoutButton;
    private SharedPreferences cartPrefs, loginPrefs;
    private static final String CART_PREFS_NAME = "CartPrefs";
    private static final String KEY_ORDERS = "Orders";
    private static final String LOGIN_PREFS_NAME = "NAME";
    public static final String FLAG = "FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        userNameText = findViewById(R.id.user_name);
        userTitleText = findViewById(R.id.user_title);
        totalOrdersText = findViewById(R.id.total_orders);
        totalSpentText = findViewById(R.id.total_spent);
        logoutButton = findViewById(R.id.logout_button);

        // Initialize SharedPreferences
        cartPrefs = getSharedPreferences(CART_PREFS_NAME, MODE_PRIVATE);
        loginPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up logout button listener
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        // Load user data
        loadUserData();
    }

    /**
     * loadUserData method that will load the user info and data.
     */
    private void loadUserData() {
        // Get user name and premium status from login preferences
        String userName = loginPrefs.getString(LOGIN_PREFS_NAME, "User");

        // Update user info UI
        userNameText.setText(userName);

        // Calculate total orders and total spent
        int totalOrders = 0;
        double totalSpent = 0.0;

        String ordersJson = cartPrefs.getString(KEY_ORDERS, "[]");
        try {
            JSONArray ordersArray = new JSONArray(ordersJson);
            totalOrders = ordersArray.length();
            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject order = ordersArray.getJSONObject(i);
                totalSpent += order.getDouble("total");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update stats UI
        totalOrdersText.setText(String.valueOf(totalOrders));

        // Format totalSpent: Use K for thousands, M for millions, etc.
        String formattedSpent;
        if (totalSpent >= 1_000_000) {
            formattedSpent = String.format("$%.2fM", totalSpent / 1_000_000); // e.g., $1.23M
        } else if (totalSpent >= 1_000) {
            formattedSpent = String.format("$%.2fK", totalSpent / 1_000); // e.g., $9.94K
        } else {
            formattedSpent = String.format("$%.2f", totalSpent); // e.g., $123.45
        }
        totalSpentText.setText(formattedSpent);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("YES", (dialogInterface, which) -> performLogout())
                .setNegativeButton("NO", (dialogInterface, which) -> dialogInterface.dismiss())
                .setCancelable(true)
                .create();

        dialog.show();

        // Customize button text color to white
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.white));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.white));
    }

    private void performLogout() {
        // Clear login flag in SharedPreferences
        SharedPreferences.Editor editor = loginPrefs.edit();
        editor.putBoolean(FLAG, false);
        editor.apply();

        // Navigate to LoginActivity.
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}