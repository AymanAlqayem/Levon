package com.tarificompany.android_ass1;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    private int id;
    private String name;
    private String description;
    private int imageId;
    private double price;
    private int stock;
    private String category;

    public Item(int id, String name, String description, int imageId, double price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageId() {
        return imageId;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("name", name);
        json.put("description", description);
        json.put("imageId", imageId);
        json.put("price", price);
        json.put("stock", stock);
        json.put("category", category);

        return json;
    }

    public static Item fromJson(JSONObject json) throws JSONException {
        return new Item(
                json.getInt("id"),
                json.getString("name"),
                json.getString("description"),
                json.getInt("imageId"),
                json.getDouble("price"),
                json.getInt("stock"),
                json.getString("category")
        );
    }
}