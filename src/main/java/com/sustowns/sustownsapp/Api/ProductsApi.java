package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductsApi {

    @GET(DZ_URL.MY_PRODUCTS)
    Call<JsonElement> getMyProducts(@Query("userid") String userid);

    @GET(DZ_URL.REMOVE_PRODUCT)
    Call<JsonElement> removeMyProducts(@Query("status") String status, @Query("pro_id") String pro_id);

    @GET(DZ_URL.COPY_PRODUCT)
    Call<JsonElement> copyMyProduct(@Query("productid") String productid, @Query("user_id") String user_id);

    @GET(DZ_URL.STORE_EDIT_PRODUCT)
    Call<JsonElement> storeEditProduct(@Query("productid") String productid, @Query("pr_title") String pr_title, @Query("pr_catid") String pr_catid, @Query("pr_eggtype") String pr_eggtype, @Query("shipingprovide") String shipingprovide,
                                       @Query("pr_userid") String pr_userid, @Query("pr_bussid") String pr_bussid, @Query("pr_price") String pr_price, @Query("pr_currency") String pr_currency,
                                       @Query("pr_stocks") String pr_stocks, @Query("pr_min") String pr_min, @Query("pr_type") String pr_type, @Query("pr_quality") String pr_quality,
                                       @Query("packaging") String packaging, @Query("job_location") String job_location, @Query("city") String city, @Query("state") String state,
                                       @Query("country") String country, @Query("weight") String weight, @Query("weight_unit") String weight_unit, @Query("makeoffer") String makeoffer,
                                       @Query("zipcode") String zipcode, @Query("sampleweightunit") String sampleweightunit, @Query("sgweight") String sgweight, @Query("sgweight_unit") String sgweight_unit,
                                       @Query("saprd_pack") String saprd_pack, @Query("squantity") String squantity, @Query("samplecost") String samplecost, @Query("scurrency") String scurrency,
                                       @Query("sampleweight_unit") String sampleweight_unit, @Query("day") String day);

    @GET(DZ_URL.ACCEPT_OFFER)
    Call<JsonElement> acceptMakeOffer(@Query("make_id") String make_id, @Query("product_id") String product_id, @Query("status") String status);

    @GET(DZ_URL.SUBMIT_MAKE_OFFER)
    Call<JsonElement> submitMakeOffer(@Query("user_id") String user_id, @Query("makeqty") String makeqty, @Query("product_id") String product_id,
                                      @Query("makeprice") String makeprice);

    @GET(DZ_URL.DELETE_OFFER)
    Call<JsonElement> deleteOffer(@Query("make_id") String make_id, @Query("status") String status);

    @GET(DZ_URL.ADD_REVIEW)
    Call<JsonElement> addReview(@Query("userid") String userid, @Query("productid") String productid, @Query("comment") String comment,
                                @Query("ratting") String ratting);

    @GET(DZ_URL.SEARCH_STORE)
    Call<JsonElement> searchStoreProducts(@Query("search") String search);

    @GET(DZ_URL.GET_VENDOR_SERVICES_ADD_PRODUCT)
    Call<JsonElement> getVendorServices(@Query("userid") String userid);
}
