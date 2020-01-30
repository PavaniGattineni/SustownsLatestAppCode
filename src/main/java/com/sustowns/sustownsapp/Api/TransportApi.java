package com.sustowns.sustownsapp.Api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sustowns.sustownsapp.Models.TransportOrdersModelObj;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TransportApi {

    @GET(DZ_URL.TRANSPORT_REGISTRATION)
    Call<JsonElement> transportVendorRegister(@Query("username") String username, @Query("phone") String phone, @Query("email") String email, @Query("role") String role,
                                              @Query("fullname") String fullname, @Query("businessname") String businessname, @Query("town") String town, @Query("password") String password, @Query("country") String country,
                                              @Query("state") String state, @Query("city") String city, @Query("locarea") String locarea);

    @GET(DZ_URL.GET_TRANS_SERVICES)
    Call<JsonElement> getTransportServices(@Query("userid") String userid);

    @GET(DZ_URL.ADD_TRANSPORT)
    Call<JsonElement> addTransportService(@Query("invoice_id") String invoice_id, @Query("order_id") String order_id, @Query("pickupdatefrom") String pickupdatefrom, @Query("pickupdateto") String pickupdateto);

    @GET(DZ_URL.ADD_TRANSPORT_PROD_DETAILS)
    Call<JsonElement> getAddTransportProductDetails(@Query("invoice") String invoice,@Query("orderid") String orderid);

    @GET(DZ_URL.GET_CONTRACT_TRANSPORT_DETAILS)
    Call<JsonElement> getContractTransportProdDetails(@Query("invoice_no") String invoice_no,@Query("order_id") String order_id);

    @GET(DZ_URL.GET_CONTRACT_REQUEST_SERVICES)
    Call<JsonElement> getContractRequestServicesList(@Query("invoice_id") String invoice_id, @Query("order_id") String order_id, @Query("pickupdatefrom") String pickupdatefrom, @Query("pickupdateto") String pickupdateto);

    @GET(DZ_URL.GET_KMS_BASED_PINCODES)
    Call<JsonElement> getKmsBasePincodes(@Query("pickup") String pickup, @Query("drop") String drop, @Query("kilometerprice") String kilometerprice);

    @GET(DZ_URL.GET_CONTRACT_KMS_BASED_PINCODES)
    Call<JsonElement> getContractKmsBasePincodes(@Query("pickup") String pickup, @Query("drop") String drop, @Query("kilometerprice") String kilometerprice);

    /*(Old Transport received orders)
    @GET
    Call<JsonObject> getTransportReceivedOrders(@Url String url);
*/
    // transport received orders latest one
    @POST(DZ_URL.GET_TRANSPORT_RECEIVED_ORDERS)
    Call<JsonObject> getTransportReceivedOrders(@Body TransportOrdersModelObj transportOrdersModel1);

    @POST(DZ_URL.GET_TRANSPORT_CONTRACT_RECEIVEDORDERS)
    Call<JsonObject> getTransportContractReceivedOrders(@Body TransportOrdersModelObj transportOrdersModel1);

    @GET(DZ_URL.GET_TRANSPORT_ORDERDETAILS_LIST)
    Call<JsonElement> getTransportOrdersDetails(@Query("orid") String orid,@Query("serid") String serid);

    @GET(DZ_URL.GET_TRANSPORT_Contract_ORDERDETAILS_LIST)
    Call<JsonElement> getTransportContractOrdersDetails(@Query("orid") String orid,@Query("serid") String serid);

    @GET
    Call<JsonObject> getBuyerTransportDetails(@Url String url);

    @GET(DZ_URL.GET_TRANSPORT_TYPE)
    Call<JsonElement> getTransportType();

    @GET(DZ_URL.GET_VEHICLE_TYPE)
    Call<JsonElement> getVehicleType(@Query(("trans")) String trans);

    @GET(DZ_URL.TRANSPORT_REQUEST_QUOTE)
    Call<JsonElement> transportRequestQuote(@Query(("user_id")) String user_id, @Query(("invoice")) String invoice, @Query(("order_ranid")) String order_ranid, @Query(("service_id")) String service_id, @Query(("trans_userid")) String trans_userid,
                                              @Query(("pick_date")) String pick_date, @Query(("qchrg_km")) String qchrg_km, @Query(("qminchrg_km")) String qminchrg_km,@Query("totalpricetransport") String totalpricetransport,
                                              @Query("qchrg_km_full_load") String qchrg_km_full_load,@Query("qminchrg_km_full_load") String qminchrg_km_full_load,@Query("total_charge_full_load") String total_charge_full_load,@Query("manual_automatic") String manual_automatic);

    @GET(DZ_URL.ADD_SERVICE)
    Call<JsonElement> addService(@Query("service") String service, @Query("prd_userid") String prd_userid, @Query("trans") String trans, @Query("vehicle") String vehicle,
                                 @Query("load") String load, @Query("taxfield") String taxfield, @Query("radi_perkm") String radi_perkm, @Query("discountfild") String discountfild,
                                 @Part MultipartBody.Part product_img, @Query("action") String action, @Query("egg") String egg, @Query("prd_bussid") String prd_bussid,
                                 @Query("service_pincode") String service_pincode, @Query("legal_address") String legal_address, @Query("cname") String cname,
                                 @Query("cunit") String cunit, @Query("ccost") String ccost, @Query("crmincharge") String crmincharge, @Query("trans_ser_latitude") String trans_ser_latitude, @Query("trans_ser_langitude") String trans_ser_langitude,
                                 @Query("sourcelocation") String sourcelocation, @Query("trans_ser_org_latitude") String trans_ser_org_latitude, @Query("trans_ser_org_langitude") String trans_ser_org_langitude,
                                 @Query("trans_ser_des_langitude") String trans_ser_des_langitude, @Query("point_destin_latitude") String point_destin_latitude, @Query("deslocation") String deslocation, @Query("cmincharge") String cmincharge, @Query("pincode") String pincode,
                                 @Query("pin") String pin);

    @GET(DZ_URL.ADD_NEW_ADDRESS)
    Call<JsonElement> addNewAddress(@Query("userid") String userid, @Query("pr_userid") String pr_userid, @Query("pr_id") String pr_id,
                                    @Query("pr_zipcode") String pr_zipcode, @Query("pr_latitude") String pr_latitude, @Query("pr_longitude") String pr_longitude,@Query("displayname") String displayname, @Query("companyname") String companyname, @Query("fname") String fname,
                                    @Query("lname") String lname, @Query("email") String email, @Query("address1") String address1,
                                    @Query("address2") String address2, @Query("zipcode") String zipcode, @Query("country") String country, @Query("state") String state,
                                    @Query("city") String city, @Query("phone") String phone, @Query("fax") String fax,
                                    @Query("drop_latitude") String drop_latitude, @Query("drop_logitude") String drop_logitude);

    @GET(DZ_URL.EXISTING_ADDRESS)
    Call<JsonElement> getExistingAddress(@Query("userid") String userid);

    @GET(DZ_URL.SENT_ADDRESS)
    Call<JsonElement> sentAddress(@Query("userid") String userid, @Query("pr_userid") String pr_userid, @Query("pr_id") String pr_id,
                                  @Query("pr_zipcode") String pr_zipcode, @Query("pr_latitude") String pr_latitude, @Query("pr_longitude") String pr_longitude,
                                  @Query("addres_id") String addres_id,@Query("zipcode") String zipcode);

    @GET(DZ_URL.GET_CATEGORIES_LIST)
    Call<JsonElement> getCategoriesList();

    @GET(DZ_URL.GET_CONTRACT_LOGISTICS_ORDERS_LIST)
    Call<JsonElement> getContractLogisticsOrdersList(@Query("userid") String userid);

    @GET
    Call<JsonElement> getLogisticsOrders(@Url String url);
}
