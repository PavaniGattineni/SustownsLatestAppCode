package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BidContractsApi {

    @GET(DZ_URL.BIDCONTRACTS_OPEN)
    Call<JsonElement> bidContOpenList(@Query("bidtype") String bidtype, @Query("user_id") String user_id);

    @POST(DZ_URL.OPEN_QUOTE)
    Call<JsonElement> openQuote(@Query("user_id") String user_id, @Query("job_id") String job_id, @Query("discription") String discription, @Query("instant") String instant,
                                @Query("currency") String currency, @Query("pay_type") String pay_type, @Query("image") String image, @Query("document") String document);
    @GET(DZ_URL.BID_CONTRACTS_QUOTED)
    Call<JsonElement> bidContQuoteList(@Query("user_id") String user_id, @Query("bidtype") String bidtype);

    @GET(DZ_URL.QUOTE_MY_QUOTE)
    Call<JsonElement> quoteMyQuote(@Query("user_id") String user_id, @Query("job_id") String job_id);

    @GET(DZ_URL.MY_PRODUCT_CONTRACTS)
    Call<JsonElement> myProductContracts(@Query("user_id") String user_id, @Query("post_contract_type") String post_contract_type);

   /* @GET(DZ_URL.GET_CURRENCY)
    Call<JsonElement> getCurrencyList();*/

    @GET(DZ_URL.STORE_SENT_OFFERS)
    Call<JsonElement> getSentOffersList(@Query("user_id") String user_id);

    @GET(DZ_URL.STORE_RECEIVED_OFFERS)
    Call<JsonElement> getReceivedOffersList(@Query("user_id") String user_id);

    @GET(DZ_URL.BID_CONTRACTS_COMPLETE)
    Call<JsonElement> bidContractsCompleteList(@Query("user_id") String user_id, @Query("bidtype") String bidtype);

    @GET(DZ_URL.BID_CONTRACTS_ADD_DOCUMENT)
    Call<JsonElement> bidContractsCompleteAddDocs(@Query("user_id") String user_id, @Query("job_id") String job_id, @Query("discription") String discription,
                                                  @Query("appattachment") String appattachment, @Query("busimage") String busimage, @Query("quote_id") String quote_id);

    @GET(DZ_URL.BID_CONTRACTS_APPROVE)
    Call<JsonElement> getBidContractsApprovedList(@Query("user_id") String user_id, @Query("bidtype") String bidtype);

    @GET(DZ_URL.BID_CONFIRM_APPROVE_CONTTRACT)
    Call<JsonElement> confirmApproveContract(@Query("jobid") String jobid, @Query("user_id") String user_id);

    @GET(DZ_URL.BID_CONTRACTS_COMPLETE_QUOTE_DETAILS)
    Call<JsonElement> bidContractCompleteQuoteDetails(@Query("user_id") String user_id, @Query("job_id") String job_id);
}
