package com.tarificompany.android_ass1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class ItemManager {
    private static final String KEY_ALL_ITEMS = "AllItems";
    private static ArrayList<Item> allItems;

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    /**
     * setUpSharedPref method that will initialize the shared preferences objects.
     *
     * @param context The context required to access SharedPreferences.
     */
    public static void setUpSharedPref(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }

    /**
     * setUpItems method that will initialize the items for each category.
     *
     * @param context The context required to access SharedPreferences.
     */
    public static void setUpItems(Context context) {
        // Ensure SharedPreferences is initialized
        if (pref == null) {
            setUpSharedPref(context);
        }

        String itemsJson = pref.getString(KEY_ALL_ITEMS, null);

        if (itemsJson == null) {
            allItems = new ArrayList<>();
            // Jewelry
            allItems.add(new Item(16, "Necklace", "Gold necklace with pendant", R.drawable.ic_launcher_background, 199.99, 10, "Jewelry"));
            allItems.add(new Item(17, "Earrings", "Diamond stud earrings", R.drawable.ic_launcher_background, 299.99, 8, "Jewelry"));
            allItems.add(new Item(18, "Bracelet", "Silver charm bracelet", R.drawable.ic_launcher_background, 149.99, 12, "Jewelry"));
            saveItemsToSharedPref();
        } else {
            loadItemsFromSharedPref();
        }
    }

    /**
     * loadItemsFromSharedPref method that will load categories items from shared preferences.
     */
    private static void loadItemsFromSharedPref() {
        String itemsJson = pref.getString(KEY_ALL_ITEMS, "[]");
        allItems = new ArrayList<>();

        // Simplified: Assuming Item.fromJson handles JSON parsing internally
        org.json.JSONArray jsonArray;
        try {
            jsonArray = new org.json.JSONArray(itemsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject jsonItem = jsonArray.getJSONObject(i);
                allItems.add(Item.fromJson(jsonItem));
            }
        } catch (org.json.JSONException e) {
            // Simplified error handling: reset to default items if loading fails
            setUpItems(null);
        }
    }

    /**
     * saveItemsToSharedPref method that will save items into shared preferences.
     */
    public static void saveItemsToSharedPref() {
        org.json.JSONArray jsonArray = new org.json.JSONArray();
        try {
            for (Item item : allItems) {
                jsonArray.put(item.toJson());
            }
            editor.putString(KEY_ALL_ITEMS, jsonArray.toString());
            editor.commit();
        } catch (org.json.JSONException e) {
            // Handle the exception by clearing the data and resetting items
            editor.putString(KEY_ALL_ITEMS, "[]");
            editor.commit();
        }
    }

    /**
     * getAllItems method that will get all categories items.
     *
     * @param context The context required to access SharedPreferences.
     */
    public static ArrayList<Item> getAllItems(Context context) {
        if (allItems == null) {
            setUpItems(context);
        }
        return allItems;
    }
}