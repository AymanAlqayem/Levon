package com.tarificompany.android_ass1;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class CartItem {
    private Item item;
    private int quantity;

    public CartItem(Item item, int quantity) {
        this.item = item;
        this.quantity = Math.max(1, quantity); // Ensure quantity is at least 1
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity); // Prevent negative or zero quantities
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("item_id", item.getId());
        json.put("quantity", quantity);
        return json;
    }

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