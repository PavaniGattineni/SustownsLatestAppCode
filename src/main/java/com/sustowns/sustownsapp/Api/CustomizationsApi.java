package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CustomizationsApi {

    @GET(DZ_URL.GET_CUSTOMIZATION_LIST)
    Call<JsonElement> getCustmizationTypesList(@Query("userid") String userid);
}
