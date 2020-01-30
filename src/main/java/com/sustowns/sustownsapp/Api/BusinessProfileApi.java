package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BusinessProfileApi {

    @GET(DZ_URL.GET_BUSINESS_PROFILE)
    Call<JsonElement> getBusinessProfile(@Query("userid") String userid);

    @GET(DZ_URL.EDIT_BUSINESS_PROFILE)
    Call<JsonElement> editBusinessProfile(@Query("userid") String userid, @Query("details") String details, @Query("website") String website,
                                          @Query("profile_img") String profile_img);

    /*  @FormUrlEncoded
      @POST(DZ_URL.BUSINESS_CATEGORY)
      Call<JsonElement> businessCategory(@Field("user_id") String user_id, @Body ArrayList<String> cat_id, @Field("buss_id") String buss_id);
  */
  //  @FormUrlEncoded
    @POST(DZ_URL.BUSINESS_CATEGORY)
    Call<JsonElement> businessCategory(@Body JsonArray jsonArray);

    @GET(DZ_URL.BUSINESS_GALLERY)
    Call<JsonElement> businessGallery(@Query("gal_image") String gal_image, @Query("user_id") String user_id, @Query("buss_id") String buss_id,
                                      @Query("img_title") String img_title, @Query("img_desc") String img_desc);
    @GET(DZ_URL.BUSINESS_BADGE)
    Call<JsonElement> businessBadge(@Query("bus_badimg") String bus_badimg, @Query("user_id") String user_id, @Query("buss_id") String buss_id);

    @GET(DZ_URL.BUSINESS_CONTRACT_REVIEWS)
    Call<JsonObject> businessContractReviews(@Query("bus_badimg") String bus_badimg, @Query("user_id") String user_id, @Query("buss_id") String buss_id);

    @GET(DZ_URL.BUSINESS_STORE_REVIEWS)
    Call<JsonObject> businessStoreReviews(@Query("bus_badimg") String bus_badimg, @Query("user_id") String user_id, @Query("buss_id") String buss_id);
}
