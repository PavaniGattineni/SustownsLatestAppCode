package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface CartApi {

   /* @GET(DZ_URL.ADD_TO_CART)
    Call<JsonElement> addToCart(@Query("userid") String userid, @Query("productid") String productid,@Query("qtys") String qtys,
                                @Query("shamount") String shamount,@Query("kmrange") String kmrange,@Query("ship_type") String ship_type,
                                        @Query("serv_id") String serv_id,@Query("price") String price);*/
   @GET(DZ_URL.ADD_TO_CART)
   Call<JsonElement> addToCart(@Query("userid") String userid, @Query("productid") String productid, @Query("qtys") String qtys,
                               @Query("ship_type") String ship_type, @Query("price_qty") String price_qty, @Query("serv_id") String serv_id,@Query("shamount") String shamount);

  @GET(DZ_URL.GET_SHIPPING_CHARGE)
  Call<JsonElement> getShippingCharge(@Query("pickup") String pickup,@Query("drop") String drop);


  @GET
  Call<JsonElement> getCartList(@Url String url);

    @GET(DZ_URL.ADD_PRODUCT)
    Call<JsonElement> addProduct(@Query("pr_title") String pr_title, @Query("pr_catid") String pr_catid, @Query("pr_eggtype") String pr_eggtype, @Query("shipingprovide") String shipingprovide,
                                 @Query("pr_userid") String pr_userid, @Query("pr_bussid") String pr_bussid, @Query("pr_price") String pr_price, @Query("pr_currency") String pr_currency,
                                 @Query("pr_stocks") String pr_stocks, @Query("pr_min") String pr_min, @Query("image") String image, @Query("pr_type") String pr_type, @Query("pr_quality") String pr_quality,
                                 @Query("packaging") String packaging, @Query("job_location") String job_location, @Query("city") String city, @Query("state") String state,
                                 @Query("country") String country, @Query("weight") String weight, @Query("weight_unit") String weight_unit, @Query("makeoffer") String makeoffer,
                                 @Query("zipcode") String zipcode, @Query("sampleweightunit") String sampleweightunit, @Query("sgweight") String sgweight, @Query("sgweight_unit") String sgweight_unit,
                                 @Query("saprd_pack") String saprd_pack, @Query("squantity") String squantity, @Query("samplecost") String samplecost, @Query("scurrency") String scurrency,
                                 @Query("sampleweight_unit") String sampleweight_unit, @Query("day") String day);

    @GET(DZ_URL.CAREER_LIST)
    Call<JsonElement> getCareerList();

    @GET(DZ_URL.REMOVE_CART)
    Call<JsonElement> removeCart(@Query("cart_id") String cart_id);

    @GET(DZ_URL.CLEAR_CART)
    Call<JsonElement> clearCartItems(@Query("userid") String userid);

    @GET
    Call<JsonElement> removeShippingItem(@Url String url);

}
