package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrdersApi {

    @GET(DZ_URL.STORE_MY_ORDER_SER)
    Call<JsonElement> myOrders(@Query("user_id") String user_id);

    @GET(DZ_URL.STORE_RECEIVED_ORDERS)
    Call<JsonElement> receivedOrders(@Query("user_id") String user_id);

    @GET(DZ_URL.CONFIRM_ORDERS)
    Call<JsonElement> confirmOrderSubmit(@Query("orderid") String orderid, @Query("seller_name") String seller_name, @Query("seller_address") String seller_address,
                                         @Query("seller_number") String seller_number, @Query("seller_country") String seller_country, @Query("seller_zipcode") String seller_zipcode);
    @GET(DZ_URL.CANCEL_ORDER)
    Call<JsonElement> cancelOrder(@Query("orderid") String orderid);

    @GET(DZ_URL.STORE_ORDER_DETAILS)
    Call<JsonElement> orderDetails(@Query("id") String id);

    @GET(DZ_URL.GET_MARKET_LIST)
    Call<JsonElement> getMarketList(@Query("category") String category, @Query("subcategory") String subcategory, @Query("year") String year,
                                    @Query("month") String month, @Query("day") String day);

}
