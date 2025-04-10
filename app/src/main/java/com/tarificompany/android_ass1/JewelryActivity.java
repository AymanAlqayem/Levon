package com.tarificompany.android_ass1;

//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//
//public class JewelryActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_category);
//
//        TextView title = findViewById(R.id.category_title);
//        title.setText("Jewelry");
//
//        // Load items from ItemManager and filter for Jewelry
//        ArrayList<Item> jewelryItems = new ArrayList<>();
//        for (Item item : ItemManager.getAllItems(this)) {
//            // Jewelry items have IDs 16, 17, 18
//            if (item.getId() >= 16 && item.getId() <= 18) {
//                jewelryItems.add(item);
//            }
//        }
//
//        // Create a custom ArrayAdapter to display item name and stock
//
//        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, jewelryItems) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                // Create or reuse the view
//                View view = super.getView(position, convertView, parent);
//
//                // Find the TextView from the existing layout (default simple_list_item_1)
//                TextView textView = (TextView) view.findViewById(android.R.id.text1);
//
//                // Get the item data
//                Item item = getItem(position);
//
//                // Set the formatted text (e.g., "Item Name - 5 in stock")
//                textView.setText(String.format("%s - %d in stock", item.getName(), item.getStock()));
//
//                // Apply background selector to the text (without creating a new XML file)
//                textView.setBackgroundResource(R.drawable.item_selector);
//
//                // Increase text size
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//
//                // Apply text color and styling
//                textView.setTextColor(Color.WHITE);
//                textView.setTypeface(null, Typeface.BOLD);
//
//                // Increase padding around the text (optional)
//                int padding = (int) (16 * getResources().getDisplayMetrics().density); // 16dp to pixels
//                textView.setPadding(padding, padding, padding, padding);
//
//                return view;
//            }
//        };
//
//        ListView itemsList = findViewById(R.id.category_items);
//        itemsList.setAdapter(adapter);
//
//        itemsList.setOnItemClickListener((parent, view, position, id) -> {
//            Item selectedItem = jewelryItems.get(position);
//            Intent intent = new Intent(JewelryActivity.this, ItemDetailActivity.class);
//            intent.putExtra("item_id", selectedItem.getId());
//            intent.putExtra("item_name", selectedItem.getName());
//            intent.putExtra("item_desc", selectedItem.getDescription());
//            intent.putExtra("item_image", selectedItem.getImageId());
//            intent.putExtra("item_price", selectedItem.getPrice());
//            startActivity(intent);
//        });
//    }
//}

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class JewelryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        TextView title = findViewById(R.id.category_title);
        title.setText("Jewelry");

        // Load items from ItemManager and filter for Jewelry.
        ArrayList<Item> jewelryItems = new ArrayList<>();
        for (Item item : ItemManager.getAllItems(this)) {
            // Jewelry items have IDs 16, 17, 18
            if (item.getId() >= 16 && item.getId() <= 18) {
                jewelryItems.add(item);
            }
        }

        // Create a custom ArrayAdapter to display item name and stock
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, jewelryItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Get the TextView from the existing layout
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                // Get the item data
                Item item = getItem(position);

                // Set the formatted text (e.g., "Item Name - 5 in stock")
                textView.setText(String.format("%s - %d in stock", item.getName(), item.getStock()));

                // Apply background selector
                textView.setBackgroundResource(R.drawable.item_selector);

                // Set text size
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                // Set text color and style
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(null, Typeface.BOLD);

                // Set padding.
                int padding = (int) (16 * getResources().getDisplayMetrics().density); // 16dp to pixels
                textView.setPadding(padding, padding, padding, padding);

                return view;
            }
        };

        // Set the adapter on the ListView
        ListView itemsList = findViewById(R.id.category_items);
        itemsList.setAdapter(adapter);

        // Handle item clicks
        itemsList.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = jewelryItems.get(position);
            Intent intent = new Intent(JewelryActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", selectedItem.getId());
            intent.putExtra("item_name", selectedItem.getName());
            intent.putExtra("item_desc", selectedItem.getDescription());
            intent.putExtra("item_image", selectedItem.getImageId());
            intent.putExtra("item_price", selectedItem.getPrice());
            startActivity(intent);
        });
    }
}
