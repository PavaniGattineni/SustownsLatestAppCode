package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostContractsApi {

    @GET(DZ_URL.CONTRACTS_PURCHASES)
    Call<JsonElement> getContractsPurchases(@Query("user_id") String user_id);

    @GET(DZ_URL.CONTRACTS_ORDERS)
    Call<JsonElement> getContractsOrders(@Query("user_id") String user_id);

    @GET(DZ_URL.CONTRACT_ORDER_INVOICE)
    Call<JsonElement> getContractsOrdersinvoice(@Query("order_id") String order_id, @Query("invoice_id") String invoice_id);

    @GET(DZ_URL.ADD_PROD_CONTRACT_REQUEST)
    Call<JsonElement> addProductContract(@Query("user_id") String user_id, @Query("v_from") String v_from, @Query("attachment") String attachment,
                                         @Query("v_to") String v_to, @Query("catid") String catid, @Query("subcat") String subcat, @Query("ssubcat") String ssubcat,
                                         @Query("job_name") String job_name, @Query("quality") String quality, @Query("quantity") String quantity, @Query("contname") String contname,
                                         @Query("slocation") String slocation, @Query("post_contract_type") String post_contract_type, @Query("image") String image, @Query("addres_id") String addres_id,
                                         @Query("displayname") String displayname, @Query("companyname") String companyname, @Query("fname") String fname, @Query("lname") String lname, @Query("email") String email,
                                         @Query("address1") String address1, @Query("address2") String address2, @Query("zipcode") String zipcode, @Query("country") String country, @Query("state") String state,
                                         @Query("city") String city, @Query("phone") String phone, @Query("fax") String fax, @Query("addrs_latitude") String addrs_latitude, @Query("addrs_longitude") String addrs_longitude, @Query("action") String action, @Query("qnt_unit") String qnt_unit);

    @GET(DZ_URL.APPROVE_QUOTE_RECEIVED_CONTRACT)
    Call<JsonElement> approveReceivedContract(@Query("jobid") String jobid, @Query("conid") String conid, @Query("user_id") String user_id, @Query("bid_id") String bid_id);

    @GET(DZ_URL.GET_RECEIVED_CONTRACTS)
    Call<JsonElement> getReceivedContracts(@Query("user_id") String user_id, @Query("job_id") String job_id);

    @GET(DZ_URL.CONFIRM_QUOTE_RECEIVED_CONTRACT)
    Call<JsonElement> confirmQuoteReceivedContract(@Query("bidid") String bidid, @Query("jobid") String jobid, @Query("conid") String conid, @Query("userid") String userid);

    @GET(DZ_URL.EDIT_POULTRY_PRODUCT_CONTRACT)
    Call<JsonElement> editPoultryProdContract(@Query("job_id") String job_id, @Query("v_to") String v_to);

    @GET(DZ_URL.MAKE_PAYMENT_BANK_APPROVE)
    Call<JsonElement> makePaymentByBank(@Query("user_id") String user_id, @Query("pay_type") String pay_type,@Query("amount") String amount,
                                        @Query("quotedid") String quotedid,@Query("jobquatid") String jobquatid,@Query("action") String action,@Query("jobid") String jobid);
}
