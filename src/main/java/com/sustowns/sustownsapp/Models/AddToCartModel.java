package com.sustowns.sustownsapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AddToCartModel extends RealmObject {
    @PrimaryKey
    private String itemId;
    private String userId;
    private String image;
    private String title;
    private String price;
    private String quantity;
    private String currency;
    private String totalItemCost;
    private boolean isItemChekOut;

    public AddToCartModel() {
        // Empty Constructor
    }

    public AddToCartModel(String itemId, String userId, String image, String title, String price, String quantity, String currency, String totalItemCost, boolean isItemChekOut) {
        this.itemId = itemId;
        this.userId = userId;
        this.image = image;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.currency = currency;
        this.totalItemCost = totalItemCost;
        this.isItemChekOut = isItemChekOut;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalItemCost() {
        return totalItemCost;
    }

    public void setTotalItemCost(String totalItemCost) {
        this.totalItemCost = totalItemCost;
    }

    public boolean isItemChekOut() {
        return isItemChekOut;
    }

    public void setItemChekOut(boolean itemChekOut) {
        isItemChekOut = itemChekOut;
    }

}
