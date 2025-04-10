package com.tarificompany.android_ass1;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class CartItem {
    private Item item;
    private int quantity;

    public CartItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Convert to JSON for saving to SharedPreferences
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("item_id", item.getId());
        json.put("quantity", quantity);
        return json;
    }

    // Create from JSON when loading from SharedPreferences
    public static CartItem fromJson(JSONObject json, Context context) throws JSONException {
        int itemId = json.getInt("item_id");
        int quantity = json.getInt("quantity");
        Item item = null;
        for (Item i : ItemManager.getAllItems(context)) {
            if (i.getId() == itemId) {
                item = i;
                break;
            }
        }
        if (item == null) {
            throw new JSONException("Item with ID " + itemId + " not found");
        }
        return new CartItem(item, quantity);
    }
}