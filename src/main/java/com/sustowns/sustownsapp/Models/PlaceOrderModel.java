package com.sustowns.sustownsapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlaceOrderModel {
    
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
