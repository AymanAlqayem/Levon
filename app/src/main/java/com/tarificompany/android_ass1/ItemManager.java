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

            //SetUp watches items.
            watchesItem();

            //SetUp gaming items
            gamingItems();


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

    /**
     * watchesItem method that will setUp the watches items.
     */
    public static void watchesItem() {
        allItems.add(new Item(1, "Timex Mens Expedition Arcadia", "Timex Mens Expedition Arcadia Brown Fabric Strap Watch - TW4B08200", R.drawable.w1, 195, 45, "Watches"));
        allItems.add(new Item(2, "LN LENQIN Mens Watches Analog Quartz", "LN LENQIN Mens Watches Analog Quartz Sports Unisex 30M Waterproof Nurse Watch for Men Women Medical Professionals Military Time with Second Hand Glowing Easy to Read Dial", R.drawable.w2, 155, 45, "Watches"));
        allItems.add(new Item(3, "LN LENQIN Mens Watches Stainless Steel", "LN LENQIN Mens Watches Stainless Steel Waterproof Analog Quartz Watch for Men with Date Luminous Fashion Wristwatch", R.drawable.w3, 155, 45, "Watches"));
        allItems.add(new Item(3, "LN LENQIN Mens Watches Stainless Steel", "LN LENQIN Mens Watches Stainless Steel Waterproof Analog Quartz Watch for Men with Date Luminous Fashion Wristwatch", R.drawable.w4, 155, 45, "Watches"));
        allItems.add(new Item(4, "Stuhrling Original Men's Stainless Steel", "Stuhrling Original Men's Stainless Steel Jubilee Bracelet GMT Watch Quartz, Dual Time, Quickset Date with Screw Down Crown, Water Resistant up to 10 ATM", R.drawable.w17, 1500, 25, "Watches"));
        allItems.add(new Item(5, "Stuhrling Original Men's Watch Dive Watch Silver", "Stuhrling Original Men's Watch Dive Watch Silver 42 MM Case with Screw Down Crown Rubber Strap Water Resistant to 330 FT", R.drawable.w5, 750, 15, "Watches"));
        allItems.add(new Item(6, "Vaer Men’s Field Watch", "Vaer Men’s Field Watch, Quartz Movement for Accurate Timing, Scratch Resistant Sapphire Crystal, Locking Screw-Down Crown, Iconic Replica of The A-11 Military Watch for Men of WW2 - Gift Set for Men", R.drawable.w6, 90, 50, "Watches"));
        allItems.add(new Item(7, "Rolex Datejust", "Rolex Datejust 36mm Watch 16233 Custom Silver Diamond Dial & 3CT Diamond Bezel", R.drawable.w7, 5500, 12, "Watches"));
        allItems.add(new Item(8, "LOREO Mens", "LOREO Mens Quartz Gold Watch Diamond Dial Stainless Steel Bracelet Sapphire Glass Dress Classic Date Waterproof Watches", R.drawable.w8, 999, 14, "Watches"));
        allItems.add(new Item(9, "Mini Focus Men's Watch", "Mini Focus Men's Watch Fashion Sport Wrist Watches", R.drawable.w9, 80, 33, "Watches"));
        allItems.add(new Item(10, "Anne Klein Women's ", "Anne Klein Women's Genuine Diamond Dial Bangle Watch", R.drawable.w10, 150, 48, "Watches"));
        allItems.add(new Item(11, "ManChDa Diamond Watch ", "ManChDa Diamond Watch for Women Iced Out Watch Bling Rhinestone Watches", R.drawable.w11, 955, 23, "Watches"));
        allItems.add(new Item(12, "Anne Klein ", "Anne Klein Women's Bracelet Watch", R.drawable.w12, 900, 40, "Watches"));
        allItems.add(new Item(13, "Anne Klein", "Anne Klein Women's Bangle Watch and Bracelet Set", R.drawable.w13, 850, 14, "Watches"));
        allItems.add(new Item(14, "GUESS Stainless Steel ", "GUESS Stainless Steel + Pink Crystal Bracelet Watch. Color: Silver-Tone (Model: U1062L2)", R.drawable.w14, 560, 26, "Watches"));
        allItems.add(new Item(15, "Women's Diamond Automatic Mechanical", "Mini Focus Men's Watch Fashion Sport Wrist Watches", R.drawable.w15, 660, 50, "Watches"));
        allItems.add(new Item(16, "OLEVS Watches for Women", "OLEVS Watches for Women Automatic Ladies Watch Mechanical Leather Strap Self-Winding Diamond dial Waterproof", R.drawable.w16, 850, 19, "Watches"));
        allItems.add(new Item(17, "Apple Watch Series 10  ", "Apple Watch Series 10 [GPS 42mm case] Smartwatch with Rose Gold Aluminium Case with Plum Sport Loop. Fitness Tracker, ECG App, Always-On Retina Display, Carbon Neutra", R.drawable.w18, 1650, 33, "Watches"));
        allItems.add(new Item(18, "Apple Watch Series 9", "Apple Watch Series 9 [GPS + Cellular 45mm] Smartwatch with Graphite Stainless Steel Case with Midnight Sport Band M/L. Fitness Tracker, Blood Oxygen & ECG Apps, Always-On Retina Display", R.drawable.w19, 1350, 36, "Watches"));
        allItems.add(new Item(19, "Apple Watch Series 9 ", "Apple Watch Series 9 [GPS 41mm] Smartwatch with Pink Aluminum Case with Light Pink Sport Loop One Size. Fitness Tracker, ECG Apps, Always-On Retina Display, Carbon Neutral", R.drawable.w20, 1550, 36, "Watches"));
        allItems.add(new Item(20, "Apple Watch Ultra 2 ", "Apple Watch Ultra 2 [GPS + Cellular 49mm] Smartwatch with Rugged Titanium Case & Blue Ocean Band One Size. Fitness Tracker, Precision GPS, Action Button, Bright Retina Display", R.drawable.w21, 1850, 26, "Watches"));
        allItems.add(new Item(21, "Apple Watch Series 7 ", "Apple Watch Series 7 [GPS 41mm] Smart Watch w/Starlight Aluminum Case with Starlight Sport Band. Fitness Tracker, Blood Oxygen & ECG Apps, Always-On Retina Display, Water Resistant", R.drawable.w22, 850, 30, "Watches"));
        allItems.add(new Item(22, "Samsung Galaxy Watch 7", "Samsung Galaxy Watch 7 40mm Bluetooth AI Smartwatch w/Energy Score, Wellness Tips, Heart Rate Tracking, Sleep Monitor, Fitness Tracker, 2024, Cream [US Version, 1Yr Manufacturer Warranty]", R.drawable.w23, 550, 80, "Watches"));
        allItems.add(new Item(23, "Samsung Galaxy Watch Ultra ", "Samsung Galaxy Watch Ultra 47mm LTE AI Smartwatch w/Energy Score, Wellness Tips, Heart Rate Tracking, Sleep Monitor, Fitness Tracker, GPS, 2024,Titanium Silver [US Version, 1Yr Manufacturer Warranty]", R.drawable.w24, 850, 41, "Watches"));
        allItems.add(new Item(24, "Samsung Galaxy Watch FE  ", "Samsung Galaxy Watch FE (40mm) AI Smartwatch w/ 1.2\" Amoled Screen, Wear OS 5, Wi-Fi, Bluetooth, Heart Rate, International Model R861N (Black)", R.drawable.w25, 650, 66, "Watches"));
    }

    public static void gamingItems(){
        allItems.add(new Item(25, "Timex Mens Expedition Arcadia", "Timex Mens Expedition Arcadia Brown Fabric Strap Watch - TW4B08200", R.drawable.w1, 195, 45, "Watches"));
        allItems.add(new Item(26, "LN LENQIN Mens Watches Analog Quartz", "LN LENQIN Mens Watches Analog Quartz Sports Unisex 30M Waterproof Nurse Watch for Men Women Medical Professionals Military Time with Second Hand Glowing Easy to Read Dial", R.drawable.w2, 155, 45, "Watches"));
        allItems.add(new Item(27, "LN LENQIN Mens Watches Stainless Steel", "LN LENQIN Mens Watches Stainless Steel Waterproof Analog Quartz Watch for Men with Date Luminous Fashion Wristwatch", R.drawable.w3, 155, 45, "Watches"));
        allItems.add(new Item(28, "LN LENQIN Mens Watches Stainless Steel", "LN LENQIN Mens Watches Stainless Steel Waterproof Analog Quartz Watch for Men with Date Luminous Fashion Wristwatch", R.drawable.w4, 155, 45, "Watches"));
        allItems.add(new Item(29, "Stuhrling Original Men's Stainless Steel", "Stuhrling Original Men's Stainless Steel Jubilee Bracelet GMT Watch Quartz, Dual Time, Quickset Date with Screw Down Crown, Water Resistant up to 10 ATM", R.drawable.w17, 1500, 25, "Watches"));
        allItems.add(new Item(30, "Stuhrling Original Men's Watch Dive Watch Silver", "Stuhrling Original Men's Watch Dive Watch Silver 42 MM Case with Screw Down Crown Rubber Strap Water Resistant to 330 FT", R.drawable.w5, 750, 15, "Watches"));
        allItems.add(new Item(31, "Vaer Men’s Field Watch", "Vaer Men’s Field Watch, Quartz Movement for Accurate Timing, Scratch Resistant Sapphire Crystal, Locking Screw-Down Crown, Iconic Replica of The A-11 Military Watch for Men of WW2 - Gift Set for Men", R.drawable.w6, 90, 50, "Watches"));
        allItems.add(new Item(32, "Rolex Datejust", "Rolex Datejust 36mm Watch 16233 Custom Silver Diamond Dial & 3CT Diamond Bezel", R.drawable.w7, 5500, 12, "Watches"));
        allItems.add(new Item(33, "LOREO Mens", "LOREO Mens Quartz Gold Watch Diamond Dial Stainless Steel Bracelet Sapphire Glass Dress Classic Date Waterproof Watches", R.drawable.w8, 999, 14, "Watches"));

    }
}