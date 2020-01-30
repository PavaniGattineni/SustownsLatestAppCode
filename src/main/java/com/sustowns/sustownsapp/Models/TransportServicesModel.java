package com.sustowns.sustownsapp.Models;

public class TransportServicesModel {
    String transport_vendor_name;
    String service_name;
    String service_pincode;
    String distance;
    String service_area_radius;
    String radius_extended;
    String type_of_radius;
    String category;
    String rating;
    String transport_type;
    String vehicle_type;
    String load_type;
    String distance_inkms;
    String partial_charge_perkm;
    String partial_minimum_charge;
    String partial_total_price;
    String full_charge_perkm;
    String full_minimum_charge;
    String full_total_price;
    String docs;
    String service_id;
    String transport_user;
    String transport_booking_status;
    String buyer_uid;
    String manual_automatic;

    public TransportServicesModel(String transport_vendor_name, String service_name, String service_pincode,String distance,String service_area_radius,String radius_extended,String type_of_radius,String category, String rating, String transport_type, String vehicle_type, String load_type, String distance_inkms,
                                  String partial_charge_perkm,String partial_minimum_charge,
                                  String partial_total_price,String full_charge_perkm,
                                  String full_minimum_charge,String full_total_price,String docs,
                                  String service_id,String transport_user, String transport_booking_status,String buyer_uid,String manual_automatic) {
        this.transport_vendor_name = transport_vendor_name;
        this.service_name = service_name;
        this.service_pincode = service_pincode;
        this.distance = distance;
        this.service_area_radius = service_area_radius;
        this.radius_extended = radius_extended;
        this.type_of_radius = type_of_radius;
        this.category = category;
        this.rating = rating;
        this.transport_type = transport_type;
        this.vehicle_type = vehicle_type;
        this.load_type = load_type;
        this.distance_inkms = distance_inkms;
        this.partial_charge_perkm = partial_charge_perkm;
        this.partial_minimum_charge = partial_minimum_charge;
        this.partial_total_price = partial_total_price;
        this.full_charge_perkm = full_charge_perkm;
        this.full_minimum_charge = full_minimum_charge;
        this.full_total_price = full_total_price;
        this.docs = docs;
        this.transport_user = transport_user;
        this.service_id = service_id;
        this.transport_booking_status = transport_booking_status;
        this.buyer_uid = buyer_uid;
        this.manual_automatic = manual_automatic;
    }

    public String getTransport_user() {
        return transport_user;
    }

    public void setTransport_user(String transport_user) {
        this.transport_user = transport_user;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }


    public String getTransport_vendor_name() {
        return transport_vendor_name;
    }

    public void setTransport_vendor_name(String transport_vendor_name) {
        this.transport_vendor_name = transport_vendor_name;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getLoad_type() {
        return load_type;
    }

    public void setLoad_type(String load_type) {
        this.load_type = load_type;
    }

    public String getDistance_inkms() {
        return distance_inkms;
    }

    public void setDistance_inkms(String distance_inkms) {
        this.distance_inkms = distance_inkms;
    }

    public String getTransport_booking_status() {
        return transport_booking_status;
    }

    public void setTransport_booking_status(String transport_booking_status) {
        this.transport_booking_status = transport_booking_status;
    }

    public String getBuyer_uid() {
        return buyer_uid;
    }

    public void setBuyer_uid(String buyer_uid) {
        this.buyer_uid = buyer_uid;
    }

    public String getService_pincode() {
        return service_pincode;
    }

    public void setService_pincode(String service_pincode) {
        this.service_pincode = service_pincode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getService_area_radius() {
        return service_area_radius;
    }

    public void setService_area_radius(String service_area_radius) {
        this.service_area_radius = service_area_radius;
    }

    public String getRadius_extended() {
        return radius_extended;
    }

    public void setRadius_extended(String radius_extended) {
        this.radius_extended = radius_extended;
    }

    public String getType_of_radius() {
        return type_of_radius;
    }

    public void setType_of_radius(String type_of_radius) {
        this.type_of_radius = type_of_radius;
    }

    public String getPartial_charge_perkm() {
        return partial_charge_perkm;
    }

    public void setPartial_charge_perkm(String partial_charge_perkm) {
        this.partial_charge_perkm = partial_charge_perkm;
    }

    public String getPartial_minimum_charge() {
        return partial_minimum_charge;
    }

    public void setPartial_minimum_charge(String partial_minimum_charge) {
        this.partial_minimum_charge = partial_minimum_charge;
    }

    public String getPartial_total_price() {
        return partial_total_price;
    }

    public void setPartial_total_price(String partial_total_price) {
        this.partial_total_price = partial_total_price;
    }

    public String getFull_charge_perkm() {
        return full_charge_perkm;
    }

    public void setFull_charge_perkm(String full_charge_perkm) {
        this.full_charge_perkm = full_charge_perkm;
    }

    public String getFull_minimum_charge() {
        return full_minimum_charge;
    }

    public void setFull_minimum_charge(String full_minimum_charge) {
        this.full_minimum_charge = full_minimum_charge;
    }

    public String getFull_total_price() {
        return full_total_price;
    }

    public void setFull_total_price(String full_total_price) {
        this.full_total_price = full_total_price;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getManual_automatic() {
        return manual_automatic;
    }

    public void setManual_automatic(String manual_automatic) {
        this.manual_automatic = manual_automatic;
    }
}
