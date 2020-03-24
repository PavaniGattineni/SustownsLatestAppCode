package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeneralProductApi {
    
    @GET(DZ_URL.GET_GEN_WEIGHT_UNIT_LIST)
    Call<JsonElement> getGenWeightUnitList();

    @GET(DZ_URL.GET_GEN_SAMPLE_WEIGHT_UNITLIST)
    Call<JsonElement> getGenSampleWeightUnitList();
    
    @GET(DZ_URL.GET_CURRENCY_LIST)
    Call<JsonElement> getCurrencyList();

    @GET(DZ_URL.GET_GEN_SHIPPING_TYPES)
    Call<JsonElement> getGenShippingTypes();

    @GET(DZ_URL.GET_GEN_AIRLIST)
    Call<JsonElement> getGenAitList();
    
}
