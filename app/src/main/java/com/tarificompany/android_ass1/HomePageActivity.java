package com.tarificompany.android_ass1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {
    private static final String[] CATEGORIES = {
            "Jewelry",
            "Ayman",
            "Mohammad"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize ItemManager.
        ItemManager.initializeItems(this);

        // Setup the categories list,
        ListView categoryList = setUpCategories();


        // Handle ListView item clicks
        handleCategoryItemOnClick(categoryList);

        // Handle BottomNavigationView
        handleNavigationView();
    }

    /**
     * setUpCategories method that will Set up a ListView for categories
     */
    public ListView setUpCategories() {
        // Set up the ListView for categories
        ListView categoryList = findViewById(R.id.category_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(CATEGORIES[position]);
                textView.setBackgroundResource(R.drawable.item_selector);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(null, Typeface.BOLD);
                int padding = (int) (16 * getResources().getDisplayMetrics().density);
                textView.setPadding(padding, padding, padding, padding);
                return view;
            }
        };
        categoryList.setAdapter(adapter);
        return categoryList;
    }

    /**
     * handleCategoryItemOnClick method that will show a list of items for the selected category.
     */
    public void handleCategoryItemOnClick(ListView categoryList) {
        categoryList.setOnItemClickListener((parent, view, position, id) -> {
            String category = CATEGORIES[position];
            Intent intent;
            switch (category) {
                case "Jewelry":
                    intent = new Intent(HomePageActivity.this, JewelryActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });
    }

    /**
     * handleNavigationView method that will handle the the views in the nav.
     */
    public void handleNavigationView() {
        // Set up the BottomNavigationView
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_search) {
                Toast.makeText(HomePageActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_favorite) {
                Toast.makeText(HomePageActivity.this, "Favorite clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_cart) {
                Toast.makeText(HomePageActivity.this, "Cart clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}