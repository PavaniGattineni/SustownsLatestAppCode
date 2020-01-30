package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FiltersApi {

    @GET(DZ_URL.GET_FILTER_CONTINENTS)
    Call<JsonElement> getFilterContinentList(@Query("l_type") String l_type);

    @GET(DZ_URL.GET_FILTER_COUNTRIES)
    Call<JsonElement> getFilterCountriesList(@Query("conti") String conti);

    @GET(DZ_URL.GET_FILTER_CITIES)
    Call<JsonElement> getFilterCitiesList(@Query("cntry") String cntry);

    @GET(DZ_URL.GET_FILTER_CATEGORIES)
    Call<JsonElement> getFilterCategoriesList();

    @GET(DZ_URL.GET_FILTER_PRODUCTLIST)
    Call<JsonElement> getFilterProducts(@Query("l_type") String l_type, @Query("conti") String conti, @Query("cntry") String cntry, @Query("stat") String stat,
                                        @Query("slug") String slug, @Query("minval") String minval, @Query("maxval") String maxval);
}
