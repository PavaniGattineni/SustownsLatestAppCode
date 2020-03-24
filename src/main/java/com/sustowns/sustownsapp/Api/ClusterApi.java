package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClusterApi {
    
    @GET(DZ_URL.CLUSTER_VENDORS_LIST)
    Call<JsonElement> clusterVendorsList(@Query("userid") String userid);

    @GET(DZ_URL.VENDOR_IDS_LIST)
    Call<JsonElement> vendorIDSList(@Query("cityid") String cityid,@Query("userid") String userid);

    @GET(DZ_URL.GET_VENDORS_DATA)
    Call<JsonElement> getVendorsDetails(@Query("uniqueid") String uniqueid);

    @GET(DZ_URL.ASSIGN_VENDOR)
    Call<JsonElement> assignvendors(@Query("userid") String userid,@Query("uniqueid") String uniqueid);

    @GET(DZ_URL.TRANSPORTERS_LIST)
    Call<JsonElement> transporterIDSList(@Query("cityid") String cityid,@Query("userid") String userid);

    @GET(DZ_URL.VENDOR_SALES)
    Call<JsonElement> vendorSalesList(@Query("userid") String userid);

    @GET(DZ_URL.VENDOR_PURCHASES)
    Call<JsonElement> vendorPurchasesList(@Query("userid") String userid);
}
