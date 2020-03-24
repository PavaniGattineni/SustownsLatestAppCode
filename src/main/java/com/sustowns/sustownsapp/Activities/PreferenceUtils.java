package com.sustowns.sustownsapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String UserName = "username";
    public static final String FULL_NAME = "fullname";
    public static final String USER_ROLE = "user_role";
    public static final String USER_TYPE = "user_type";
    public static final String ORDERID = "orderid";
    public static final String Password = "password";
    public static final String USER_EMAIL = "user_email";
    public static final String MOBILE = "mobile";
    public static final String USER_ID = "user_id";
    public static final String PRO_ID = "pro_id";
    public static final String STORE_PRO_ID = "store_pro_id";
    public static final String PRO_QUANTITY = "pro_quantity";
    public static final String STATUS = "status";
    public static final String Category_ID = "category_id_business";
    public static final String Image = "image";
    public static final String GAL_IMAGE = "gallery_image";
    public static final String BadgeImage = "badge_image";
    public static final String ADDRESS = "address";
    public static final String WEBSITE = "website";
    public static final String BUSINESS_ID = "buss_id";
    public static final String GALLERY_TITLE = "gal_image";
    public static final String GALLERY_DESCRIPTION = "gal_description";
    public static final String Buss_ID = "buss_id";
    public static final String AddressMap = "mapAddress";
    public static final String JOB_ID = "job_id";
    public static final String ORDER_ID = "order_id";
    public static final String VIDEO = "video";
    public static final String OFF_PRO_ID = "offers_prod_id";
    public static final String MAKE_OFFER = "make_offer";
    public static final String MAKE_ID = "make_id";
    public static final String MAKE_STATUS = "make_status";
    public static final String IMAGE = "image_home";
    public static final String PROD_ID = "productid";
    public static final String COUNTRY_BILLING = "billing_country";
    public static final String ADDRESS_ID = "addressId";
    public static final String Freight_Type = "freight_type";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String Cust_Address = "CustomerAdd";
    public static final String ZIPCODE = "pincode";
    public static final String CartTitle = "cart_title";
    public static final String CartId = "cart_id";
    public static final String CartQuantity = "cart_quantity";
    public static final String CartPrice = "cart_price";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String SampleProdId = "sampleprodid";
    public static final String REVIEW_ID = "reviewid";

    // spinner for storage management
    public static final String EGGS_TYPE = "eggs_type";
    public static final String AddressId = "addressId";
    public static final String PROD_CAT = "prod_cat";
    public static final String Sector = "sector";
    public static final String ListingType = "listingType";
    public static final String UNIT = "unit";
    public static final String PRICE = "price";
    public static final String Sample_PackType = "samplepacktype";
    public static final String Shipping = "shipping";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String SAMPLE_GROSS_WEIGHT = "samplegrossweight";
    public static final String SAMPLE_UNIT_WEIGHT = "sampleunitweight";
    public static final String PackagingType = "packtype";
    public static final String QUOTE_CURRENCY = "currency";
    public static final String ReceivedOffersList = "received_offers";
    public static final String CURRENCY_CODE = "currencycode";
    public static final String isRemember = "no";


    private static SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    public PreferenceUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        edit = preferences.edit();
    }

    public void saveString(String strKey, String strValue) {
        edit.putString(strKey, strValue);
        edit.commit();
    }

    public void saveBoolean(String strKey, boolean value) {
        edit.putBoolean(strKey, value);
        edit.commit();
    }

    public static String getStringFromPreference(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getbooleanFromPreference(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

}
