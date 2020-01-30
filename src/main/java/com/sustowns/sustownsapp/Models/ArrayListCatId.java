package com.sustowns.sustownsapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArrayListCatId {
    @SerializedName("cat_id")
    @Expose
    private ArrayList<String> cat_id;

    public ArrayListCatId(ArrayList<String> cat_id) {
        this.cat_id=cat_id;
    }
}
