package com.tarificompany.android_ass1;

import android.content.Intent;
import android.os.Bundle;
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

        // Load items from ItemManager and filter for Jewelry
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
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                Item item = getItem(position);
                textView.setText(String.format("%s - %d in stock", item.getName(), item.getStock()));
                return textView;
            }
        };

        ListView itemsList = findViewById(R.id.category_items);
        itemsList.setAdapter(adapter);

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