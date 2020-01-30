package com.sustowns.sustownsapp.Models;

public class ServiceUnitModel {
    String unit_id;
    String unit_name;
    String charge;
    String min_charge;
    String min_load;
    String max_load;

    public ServiceUnitModel(String unit_id, String unit_name, String charge, String min_charge, String min_load, String max_load) {
        this.unit_id = unit_id;
        this.unit_name = unit_name;
        this.charge = charge;
        this.min_charge = min_charge;
        this.min_load = min_load;
        this.max_load = max_load;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getMin_charge() {
        return min_charge;
    }

    public void setMin_charge(String min_charge) {
        this.min_charge = min_charge;
    }

    public String getMin_load() {
        return min_load;
    }

    public void setMin_load(String min_load) {
        this.min_load = min_load;
    }

    public String getMax_load() {
        return max_load;
    }

    public void setMax_load(String max_load) {
        this.max_load = max_load;
    }

}
