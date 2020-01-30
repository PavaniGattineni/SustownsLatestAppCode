package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @GET(DZ_URL.LOGIN)
    Call<JsonElement> Login(@Query("username") String username, @Query("password") String password);

    @GET(DZ_URL.HOME_PRODUCTS)
    Call<JsonElement> getHomeProducts();

    @GET(DZ_URL.CART_COUNT)
    Call<JsonElement> cartCount(@Query("userid") String userid);

    @POST(DZ_URL.VENDOR_SIGNUP)
    Call<JsonElement> vendorSignup(@Query("name") String name, @Query("username") String username, @Query("phone") String phone, @Query("email") String email,
                                   @Query("businessname") String businessname, @Query("password") String password, @Query("role") String role, @Query("country") String country,@Query("pincode") String pincode,@Query("vendorsubcat") String vendorsubcat);

    @GET(DZ_URL.STORE_POULTRY)
    Call<JsonElement> StorePoultryProducts();

    @GET(DZ_URL.PRODUCT_DETAILS)
    Call<JsonElement> productDetails(@Query("userid") String userid, @Query("pid") String pid);

    @GET(DZ_URL.NEWS)
    Call<JsonElement> getNews();

    @GET(DZ_URL.VIDEOS)
    Call<JsonElement> getVideos();

    @GET(DZ_URL.GET_CATEGORIES)
    Call<JsonElement> getCategories();

    @GET(DZ_URL.CONTACT_US)
    Call<JsonElement> contactUs(@Query("name") String name, @Query("phone") String phone, @Query("email") String email, @Query("message") String message);

    @GET(DZ_URL.GET_VENDOR_CATEGORIES_LIST)
    Call<JsonElement> getVendorCategoriesList();

    @GET(DZ_URL.GET_COUNTRY)
    Call<JsonElement> getCountries();

    @GET(DZ_URL.GET_STATES)
    Call<JsonElement> getStates(@Query("country") String country);

    @GET(DZ_URL.GET_CITIES)
    Call<JsonElement> getCities(@Query("countrycode") String countrycode,@Query("state") String state);

    @GET(DZ_URL.GET_CURRENCY)
    Call<JsonElement> getCurrencyCodes();

    @GET(DZ_URL.VENDOR_PROFILE)
    Call<JsonElement> getVendorProfile(@Query("user_id") String user_id);

    @GET(DZ_URL.VENDOR_CATEGORY)
    Call<JsonElement> vendorCategoryList(@Query("user_id") String user_id);

    @GET(DZ_URL.VENDOR_RATING_REVIEW)
    Call<JsonElement> vendorRatingReviews(@Query("user_id") String user_id);

    @GET(DZ_URL.VENDOR_OUR_PRODUCTS)
    Call<JsonElement> vendorOurProductsList(@Query("user_id") String user_id);

    @GET(DZ_URL.GET_SHIPPING_ADDRESS)
    Call<JsonElement> getShippingAddress(@Query("user_id") String user_id);

    @GET(DZ_URL.GET_PAYMENT_ORDER)
    Call<JsonElement> getCartListPaymentOrders(@Query("order_id") String order_id, @Query("banktransactionid") String banktransactionid, @Query("user_id") String user_id);

    @GET(DZ_URL.SUBMIT_ADD_PAYMENT)
    Call<JsonElement> submitInAddPayment(@Query("user_id") String user_id, @Query("order_id") String order_id, @Query("bankrandomid") String bankrandomid,
                                         @Query("cheque_onlineid") String cheque_onlineid, @Query("amount") String amount, @Query("paydate") String paydate);

    @GET(DZ_URL.SUBMIT_CONTRACT_REVIEW)
    Call<JsonElement> submitContractReviews(@Query("user_id") String user_id, @Query("buss_id") String buss_id, @Query("review_com_reply") String review_com_reply,
                                            @Query("review_id") String review_id);

    @GET(DZ_URL.GET_BANK_DETAILS)
    Call<JsonElement> getBankDetails();

    @GET(DZ_URL.GET_SLIDER_IMAGES)
    Call<JsonObject> getSliderImages();

    @GET(DZ_URL.GET_BANK_DETAILS_PROFILE)
    Call<JsonElement> getBankDetailsList(@Query("user_id") String user_id);
}
