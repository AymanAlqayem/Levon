package com.tarificompany.android_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String[] CATEGORIES = {
            "Jewelry"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ItemManager
        ItemManager.initializeItems(this);

        ListView categoryList = findViewById(R.id.category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CATEGORIES);
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