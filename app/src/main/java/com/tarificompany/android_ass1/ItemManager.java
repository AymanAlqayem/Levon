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

            //SetUp gaming items.
            gamingItems();

            //Setup fitness items.
            fitnessItems();


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

    /**
     * gamingItems method that will setUp the gaming items.
     */
    public static void gamingItems() {
        allItems.add(new Item(25, "BINNUNE Wireless", "BINNUNE Wireless Gaming Headset with 2.4GHz for PS5, PS4, PC, Switch, FPS Gamers, 120Hrs+ Bluetooth 5.3 Gaming Headsets with Noise Canceling Mic, 50MM Headphones for Laptop, Mobile, Mac", R.drawable.g1, 50, 40, "Gaming"));
        allItems.add(new Item(26, "Gaming Headset", "Gaming Headset for PC, Ps5, Switch, Mobile, Gaming Headphones for Nintendo with Noise Canceling Mic, Deep Bass Stereo Sound", R.drawable.g2, 110, 36, "Gaming"));
        allItems.add(new Item(27, "ZIUMIER Gaming Headset", "ZIUMIER Gaming Headset with Microphone, Compatible with PS4 PS5 Xbox One PC Laptop, Over-Ear Headphones with LED RGB Light, Noise Canceling Mic, 7.1 Stereo Surround Sound", R.drawable.g3, 120, 29, "Gaming"));
        allItems.add(new Item(28, "EKSA E900 Pro ", "EKSA E900 Pro USB Gaming Headset for PC - Computer Headset with Detachable Noise Cancelling Mic, 7.1 Surround Sound, 50MM Driver - Headphones with Microphone for PS4/PS5, Xbox One, Laptop, Office", R.drawable.g4, 160, 36, "Gaming"));
        allItems.add(new Item(29, "EKSA E1000 ", "EKSA E1000 USB Gaming Headset for PC, Computer Headphones with Microphone/Mic Noise Cancelling, 7.1 Surround Sound, RGB Light - Wired Headphones for PS4, PS5 Console, Laptop, Call Center", R.drawable.g5, 80, 40, "Gaming"));
        allItems.add(new Item(30, "PHOINIKAS ", "PHOINIKAS PS4 Gaming Headset with 7.1 Surround Sound, PC Headset with Noise Canceling Mic & LED Light, H3 Over Ear Headphones for Nintendo Switch, PS5, Xbox One, Laptop", R.drawable.g6, 160, 44, "Watches"));
        allItems.add(new Item(31, "DEEBOX Gaming Headset", "DEEBOX Gaming Headset for PC, PS5, PS4, Xbox, Gaming Headphone with Detachable Noise Canceling Mic, Wired Gaming Headsets 3.5mm for PC, Laptop, Mac & Nintendo NES Games", R.drawable.g7, 90, 44, "Gaming"));
        allItems.add(new Item(32, "YOTMS H6", "YOTMS H6 Gaming Headset with Microphone, Stereo Headset for PS4 PS5 PC Xbox One Switch, Wired Gaming Headphonesfor Laptop/Tablet, with 3.5mm Audio Jack, LED Light", R.drawable.g8, 90, 36, "Gaming"));
        allItems.add(new Item(33, "Logitech G502 HERO", " Logitech G502 HERO High Performance Wired Gaming Mouse, HERO 25K Sensor, 25,600 DPI, RGB, Adjustable Weights, 11 Programmable Buttons, On-Board Memory, PC / Mac", R.drawable.g9, 60, 28, "Gaming"));
        allItems.add(new Item(34, "Redragon Wireless", "Redragon Wireless Gaming Mouse, Tri-Mode 2.4G/USB-C/Bluetooth Ergonomic Mouse Gaming, 8000 DPI, RGB Backlit Programmable Wireless Mouse, Rechargeable, 250 Hrs for Laptop PC Mac, M814", R.drawable.g10, 40, 30, "Gaming"));
        allItems.add(new Item(35, "Redragon M686 Wireless", "Redragon M686 Wireless Gaming Mouse, 16000 DPI Wired/Wireless Gamer Mouse with Professional Sensor, 45-Hour Reliable Power Capacity, Customizable Macro and RGB Backlight for PC/Mac/Laptop ", R.drawable.g11, 40, 50, "Gaming"));
        allItems.add(new Item(36, "BENGOO Gaming Mouse Wired", " BENGOO Gaming Mouse Wired, Ergonomic Gamer Laptop PC Optical Computer Mice with RGB Backlit, 4 Adjustable DPI Up to 3600, 6 Programmable Buttons for Windows 7/8/10/XP Vista Linux ", R.drawable.g12, 55, 30, "Gaming"));
        allItems.add(new Item(37, "LED Wireless Mouse", " LED Wireless Mouse, Slim Silent Computer Mouse 2.4G Portable Mobile Optical Office Mouse with USB & Type-c Receiver, 3 Adjustable DPI Levels for Notebook, PC, Laptop, Computer, MacBook", R.drawable.g13, 10, 19, "Gaming"));
        allItems.add(new Item(38, "Wireless Mouse", " Wireless Mouse Chargeable Portable Silent USB and Type-C Dual Mode Wireless Mouse 3 Adjustable DPI for Laptop, Mac, MacBook, Android, PC", R.drawable.g14, 15, 20, "Gaming"));
        allItems.add(new Item(39, "Redragon M913", " Redragon M913 Impact Elite Wireless Gaming Mouse, 16000 DPI Wired/Wireless RGB Mouse with 16 Programmable Buttons, 45 Hr Battery and Pro Optical Sensor, 12 Side Buttons MMO Mous", R.drawable.g15, 45, 36, "Gaming"));
        allItems.add(new Item(40, "Razer Basilisk V3", " Razer Basilisk V3 Customizable Ergonomic Gaming Mouse: Fastest Gaming Mouse Switch - Chroma RGB Lighting - 26K DPI Optical Sensor - 11 Programmable Buttons - HyperScroll Tilt Wheel ", R.drawable.g16, 55, 33, "Gaming"));
        allItems.add(new Item(41, "Gaming Mouse Wired", " Gaming Mouse Wired,6 Buttons, 4 Adjustable DPI Up to 3200 DPI, 7 Circular & Breathing LED Light, Multifunction Wired Mouse Used for Games and Office", R.drawable.g17, 12, 32, "Gaming"));
        allItems.add(new Item(42, "Sweetcrispy Computer Gaming Desk Chair", " Sweetcrispy Computer Gaming Desk Chair - Ergonomic PU Leather with Comfy Lumbar Support, Height Adjustable Rolling Desk with Flip-up Armrests, for Home and Office", R.drawable.g18, 165, 28, "Gaming"));
        allItems.add(new Item(43, "GTRACING Gaming Chair ", " GTRACING Gaming Chair Racing Office Computer Ergonomic Video Game Chair Backrest and Seat Height Adjustable Swivel Recliner with Headrest and Lumbar Pillow Esports Chair", R.drawable.g19, 250, 36, "Gaming"));
        allItems.add(new Item(44, "AutoFull C3 Gaming Chair", " AutoFull C3 Gaming Chair, Ergonomic Wingless Cushion Computer Chair,PU Leather Racing Style Office Chair with Lumbar Support Pillow and Footrest", R.drawable.g20, 365, 32, "Gaming"));
        allItems.add(new Item(45, "Gaming Chairs with Footrest", " Gaming Chairs with Footrest Office Chair Computer Chair Ergonomic Racing Style PU Leather Reclining Desk Chair with Massager 350LBS", R.drawable.g21, 220, 33, "Gaming"));
        allItems.add(new Item(46, "OneGame Gaming Chair", " OneGame Gaming Chair for Adults, Ergonomic Computer Gamer Chair, Racing Style Swivel Office Desk Chair with Headrest and Lumbar Support, Blackorange", R.drawable.g22, 155, 23, "Gaming"));
        allItems.add(new Item(47, "Aheaplus L Shaped Gaming Desk", " Aheaplus L Shaped Gaming Desk with Power Outlets & LED Lights, Small L- Shaped Desk Computer Corner Desk with Monitor Stand & Storage Shelf, Home Office Desk Writing Desk with Storage Bag", R.drawable.g23, 120, 144, "Gaming"));
        allItems.add(new Item(48, "Lufeiya Computer Desk", " Lufeiya Computer Desk with Fabric Drawers, 40 Inch Reversible Gaming Desk for Small Space Home Office, Modern Simple Study Writing Table PC Desks for Bedroom", R.drawable.g24, 90, 36, "Gaming"));
        allItems.add(new Item(49, "STGAubron Gaming PC", " STGAubron Gaming PC Bundle with 24Inch FHD LED Monitor-Intel core I7 up to 3.9G, RX 580 8G, 16G, 512G SSD, RGB Mouse Pad, RGB Sound Bar, Windows 10 Home", R.drawable.g25, 750, 5, "Gaming"));
        allItems.add(new Item(50, "YEYIAN Tanto Prebuilt Gaming PC", " YEYIAN Tanto Prebuilt Gaming PC, Ryzen 7 5700X 4.6GHz, RTX 4060,16GB RGB Memory 1TB SSD, Desktop Computer VR & AI Ready, Win 11 Home Pre Built Tower PC Computers", R.drawable.g26, 1500, 4, "Gaming"));
        allItems.add(new Item(51, "iBUYPOWER Trace 7 Mesh Gaming PC ", " iBUYPOWER Trace 7 Mesh Gaming PC Computer Desktop TMA7N3501 (AMD Ryzen 7 5700, RTX 3050 6GB, 16GB DDR4 RGB 3200MHz (8x2), 1TB NVMe, WiFi Ready, Windows 11 Home Advanced)", R.drawable.g27, 1400, 6, "Gaming"));
    }

    /**
     * fitnessItems method that will setUp the fitness items.
     */
    public static void fitnessItems() {
        allItems.add(new Item(52, "FEIERDUN Adjustable Dumbbells", "FEIERDUN Adjustable Dumbbells, 20/30/40/45/70/90lbs Free Weight Set with Connector, 5 in1 Dumbbells Set Used as Barbell, Kettlebells, Push up Stand, Weight Plate, Fitness Exercises for Home Gym Suitable Men/Women", R.drawable.f1, 198, 100, "fitness"));
        allItems.add(new Item(53, "Adjustable Dumbbells", "Adjustable Dumbbells Set 12.5LB/22.5LB Pair, 1-Sec Fast Adjustable 5 Weights by Turning Handle, Premium Comfort Non-slip Metal Dumbbells with Tray, Compact Size for Full Body Home Workout", R.drawable.f2, 130, 100, "fitness"));
        allItems.add(new Item(54, "25/55 lbs Pair Adjustable Dumbbell Set", "25/55 lbs Pair Adjustable Dumbbell Set, Adjust Dumbbell Weight for Exercises Pair Dumbbells for Men and Women in Home", R.drawable.f3, 159, 100, "fitness"));
        allItems.add(new Item(55, "Northdeer V2.0 Upgraded Adjustable Steel Dumbbells", "Northdeer V2.0 Upgraded Adjustable Steel Dumbbells 40LB / 60LB - 2 in 1 Dumbbell Barbell Set for Home Gym Workout - Compatible with V1.0 Dumbbells", R.drawable.f4, 177, 90, "fitness"));
        allItems.add(new Item(56, "PHOEROS Adjustable Dumbbell Set", "PHOEROS Adjustable Dumbbell Set - 44/66/88 LBS weights set, Dumbbell Barbell 3 in 1, Steel Dumbbells Pair with Connecting Rod for Adults Women Men Fitness Workout, Home Gym Exercise Training Equipment", R.drawable.f5, 199, 120, "fitness"));
        allItems.add(new Item(57, "Weighted Vest for Men Women", "Weighted Vest for Men Women 20lb/30lb/40lb/50lb/60lb Adjustable Weight Vest with Steel Plates Weights for Walking Strength Training Running Workout", R.drawable.f6, 160, 110, "fitness"));
        allItems.add(new Item(58, "Wrist Arm Weights", "Wrist Arm Weights, Adjustable Wrist Weights, Removable Wrist Ankle Weights for Men Women, for Fitness, Walking, Jogging, Workout, Running, 1Pair 2 Pack", R.drawable.f7, 89, 60, "fitness"));
        allItems.add(new Item(59, "Wrist & Ankle Weights", "Wrist & Ankle Weights set of 2 (1lb each), Adjustable Silicone Weighted Bangles for Men and Women, Suitable for Arm & Leg Workout, Yoga, Pilates, Dance, Barre, Gym, Aerobics.", R.drawable.f8, 30, 60, "fitness"));
        allItems.add(new Item(60, "HOTWAVE Weighted Vest", "HOTWAVE Weighted Vest, 2-18 lbs Weighted Vest with Ankle Weights Woman Men Adjustable - Weight Vest with Reflective for Men Women, Body Light Walk Weight Vest for Running Walking Workout Fitness", R.drawable.f9, 99, 50, "fitness"));
        allItems.add(new Item(61, "Unbrokenshop.com Adjustable Weighted Vest", "Unbrokenshop.com Adjustable Weighted Vest, WOD Cross Fitness, Weight Plates Carrier for Strength and Endurance Training and Running. For Men or for Women, 2 Patches Included", R.drawable.f10, 120, 60, "fitness"));
        allItems.add(new Item(62, "VINSGUIR Ab Roller Wheel", "VINSGUIR Ab Roller Wheel, Abs & Core Workout Equipment for Home Gym with Knee Pad Accessories, Abdominal Wheel for Full-body Strength Training", R.drawable.f11, 40, 30, "fitness"));
        allItems.add(new Item(63, "Jump Rope", "Jump Rope, Tangle-Free Rapid Speed Jumping Rope Cable with Ball Bearings for Women, Men, and Kids, Adjustable Steel Jump Rope Workout with Foam Handles for Fitness, Home Exercise & Slim Body", R.drawable.f12, 9, 50, "fitness"));
        allItems.add(new Item(64, "Redify Jump Rope", "Redify Jump Rope,Jump Ropes for Fitness for Women Men and Kids,Speed Jumping Rope for Workout with Ball Bearings,Adjustable Skipping Rope for Exercise&Slim Body at Home School Gym", R.drawable.f13, 17, 36, "fitness"));
        allItems.add(new Item(65, "LAUS Ab Wheel Roller", "LAUS Ab Wheel Roller - with Knee Pad Mat, Automatic Rebound and Multiple Angles Core Workouts", R.drawable.f14, 33, 40, "fitness"));
        allItems.add(new Item(66, "Ab Roller Wheel", "Ab Roller Wheel, 4d Ab Roller with Elbow Support and Knee Pad, Elbow Support Automatic Rebound Abdominal Wheel for Core Workout, Plank Exercise Wheel for Abdominal Core Strength Training", R.drawable.f15, 60, 19, "fitness"));
        allItems.add(new Item(67, "YOSUDA Magnetic/Water Rowing Machine", "YOSUDA Magnetic/Water Rowing Machine 350 LB Weight Capacity - Foldable Rower for Home Use with Bluetooth, App Supported, Tablet Holder and Comfortable Seat Cushion", R.drawable.f16, 220, 36, "fitness"));
        allItems.add(new Item(68, "YOSUDA Indoor Cycling Bike Brake", "YOSUDA Indoor Cycling Bike Brake Pad/Magnetic Stationary Bike - Cycle Bike with Tablet Mount & Comfortable Seat Cushion", R.drawable.f17, 260, 20, "fitness"));
        allItems.add(new Item(69, "YOSUDA PRO Magnetic Exercise Bike", "YOSUDA PRO Magnetic Exercise Bike 350 lbs Weight Capacity - Indoor Cycling Bike Stationary with Comfortable Seat Cushion, Silent Belt Drive", R.drawable.f18, 360, 32, "fitness"));
        allItems.add(new Item(70, "CURSOR FITNESS Recumbent Exercise Bike", "CURSOR FITNESS Recumbent Exercise Bike, Stationary Bikes for Home with Adjustable Seat, 16-Level Magnetic Resistance Recumbent Exercise Bike for Seniors", R.drawable.f19, 320, 32, "fitness"));
    }
}