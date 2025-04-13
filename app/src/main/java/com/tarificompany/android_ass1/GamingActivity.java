package com.tarificompany.android_ass1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GamingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        loadItems();
    }

    /**
     * loadItems method that will load gaming items.
     */
    public void loadItems() {
        TextView title = findViewById(R.id.category_title);
        title.setText("Gaming");

        // Load items from ItemManager and filter for Jewelry.
        ArrayList<Item> itemArrayList = new ArrayList<>();
        for (Item item : ItemManager.getAllItems(this)) {
            if (item.getId() >= 25 && item.getId() <= 51) {
                itemArrayList.add(item);
            }
        }

        // Create a custom ArrayAdapter to display item details with image
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.item_list_layout, itemArrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Inflate the custom layout if convertView is null
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_layout, parent, false);
                }

                // Get the item data
                Item item = getItem(position);

                // Bind data to the views
                ImageView itemImage = convertView.findViewById(R.id.item_image);
                TextView itemName = convertView.findViewById(R.id.item_name);
                TextView itemDescription = convertView.findViewById(R.id.item_description);
                TextView itemPrice = convertView.findViewById(R.id.item_price);

                // Set the item details
                itemName.setText(item.getName());
                itemDescription.setText(item.getDescription());
                itemPrice.setText(String.format("$%.2f", item.getPrice()));

                // Set the image (using the resource ID stored in the Item)
                itemImage.setImageResource(item.getImageId());

                // Optional: Apply additional styling
                itemName.setTextColor(Color.WHITE);
                itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                itemName.setTypeface(null, Typeface.BOLD);

                itemDescription.setTextColor(Color.WHITE);
                itemDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                itemPrice.setTextColor(Color.WHITE);
                itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                return convertView;
            }
        };

        // Set the adapter on the ListView
        ListView itemsList = findViewById(R.id.category_items);
        itemsList.setAdapter(adapter);

        // Handle item clicks
        itemsList.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = itemArrayList.get(position);
            Intent intent = new Intent(GamingActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", selectedItem.getId());
            intent.putExtra("item_name", selectedItem.getName());
            intent.putExtra("item_desc", selectedItem.getDescription());
            intent.putExtra("item_image", selectedItem.getImageId());
            intent.putExtra("item_price", selectedItem.getPrice());
            startActivity(intent);
        });
    }
}