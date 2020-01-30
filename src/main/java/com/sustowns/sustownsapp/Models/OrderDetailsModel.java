package com.sustowns.sustownsapp.Models;

public class OrderDetailsModel {

    private String id;
    private String user_id;
    private String order_id;
    private String product_order_id;
    private String quantity;
    private String price;
    private String totalprice;
    private String pr_title;
    private String pr_image;
    private String order_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_order_id() {
        return product_order_id;
    }

    public void setProduct_order_id(String product_order_id) {
        this.product_order_id = product_order_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPr_title() {
        return pr_title;
    }

    public void setPr_title(String pr_title) {
        this.pr_title = pr_title;
    }

    public String getPr_image() {
        return pr_image;
    }

    public void setPr_image(String pr_image) {
        this.pr_image = pr_image;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
