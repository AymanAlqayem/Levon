package com.tarificompany.android_ass1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String[] CATEGORIES = {
            "Jewelry",
            "Ayman",
            "Mohammad"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ItemManager
        ItemManager.initializeItems(this);

        ListView categoryList = findViewById(R.id.category_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Create or reuse the view
                View view = super.getView(position, convertView, parent);

                // Find the TextView from the existing layout
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                // Set the text for the category
                textView.setText(CATEGORIES[position]);

                // Apply the background selector directly to the TextView
                textView.setBackgroundResource(R.drawable.item_selector);

                // Increase the text size (e.g., 20sp)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                // Apply text styling if necessary (optional)
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(null, Typeface.BOLD);

                // Increase padding (e.g., 20dp)
                int padding = (int) (16 * getResources().getDisplayMetrics().density); // 16dp to pixels
                textView.setPadding(padding, padding, padding, padding);

                return view;
            }
        };
        categoryList.setAdapter(adapter);



        categoryList.setOnItemClickListener((parent, view, position, id) -> {
            String category = CATEGORIES[position];
            Intent intent;
            switch (category) {
                case "Jewelry":
                    intent = new Intent(MainActivity.this, JewelryActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        });

    }
}