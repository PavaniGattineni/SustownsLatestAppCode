package com.sustowns.sustownsapp.Models;

public class ImageModel {
    String image_base64;
    String isPrimary;

    public ImageModel(String image_base64, String isPrimary) {
        this.image_base64 = image_base64;
        this.isPrimary = isPrimary;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

}
