package com.tarificompany.android_ass1;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemManager {
    private static final String PREFS_NAME = "ItemPrefs";
    private static final String KEY_ALL_ITEMS = "AllItems";
    private static ArrayList<Item> allItems;

    public static void initializeItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String itemsJson = prefs.getString(KEY_ALL_ITEMS, null);

        if (itemsJson == null) {
            allItems = new ArrayList<>();
            // Jewelry
            allItems.add(new Item(16, "Necklace", "Gold necklace with pendant", R.drawable.ic_launcher_background, 199.99, 10, "Jewelry"));
            allItems.add(new Item(17, "Earrings", "Diamond stud earrings", R.drawable.ic_launcher_background, 299.99, 8, "Jewelry"));
            allItems.add(new Item(18, "Bracelet", "Silver charm bracelet", R.drawable.ic_launcher_background, 149.99, 12, "Jewelry"));
            saveItems(context);
        } else {
            loadItems(context);
        }
    }

    private static void loadItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String itemsJson = prefs.getString(KEY_ALL_ITEMS, "[]");
        allItems = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(itemsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                allItems.add(Item.fromJson(jsonItem));
            }
        } catch (JSONException e) {
            android.util.Log.e("ItemManager", "Error loading items", e);
            initializeItems(context);
        }
    }

    public static void saveItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        try {
            for (Item item : allItems) {
                jsonArray.put(item.toJson());
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_ALL_ITEMS, jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            android.util.Log.e("ItemManager", "Error saving items", e);
        }
    }

    public static ArrayList<Item> getAllItems(Context context) {
        if (allItems == null) {
            initializeItems(context);
        }
        return allItems;
    }
}