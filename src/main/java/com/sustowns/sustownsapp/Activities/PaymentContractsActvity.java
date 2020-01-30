package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.PostContractsApi;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentContractsActvity  extends AppCompatActivity {
    ImageView backarrow,close_payment_dialog,close_send_quote;
    PreferenceUtils preferenceUtils;
    Button pay_now_btn;
    String countryStr, mobile, countryStrBilling, order_id, title, id, quantity, price;
    String name, company_name, email, first_name, last_name, address1, address2, state, town, pincode, fax, country_st, user_id, selectedRadioBtn;
    String[] country = {"India", "Algeria", "USA", "UK"};
    RadioButton radioButton;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;
    CheckBox checkbox, checkbox_agree;
    Integer selectedId;
    LinearLayout ll_bank_details, ll_order_placed_text,ll_payment,ll_quote_received_details,linearlayout;
    RadioButton paybank_radiobtn, cards_checkbox,netbanking_checkbox,upi_checkbox,payumoney_checkbox;
    String paymentType = "",userName,userEmail,userMobile;
    CardView received_orders_cardview;
    private String merchantKey = "swpahz", salt = "h7dXPGlF", transactionId, userCredentials;
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;
    private PayuConfig payuConfig;
    private PayUChecksum checksum;
    Helper helper;
    String amountStr,jobQuoteId,quoteId,jobId,logisticsStr,orderRandId,serviceId,transportId,KmRangeStr,ChargePerKmStr,makeOfferIdStr;
    TextView my_orders_text,km_range,charge_per_km,total_charge,amount_tobe_paid;
    String bankCode,mihpayid,mode,status,txnid ,amount,net_amount_debit,firstname,phone,hash,payment_source,PG_TYPE,bank_ref_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.payment_dialog);

        try {
            initializeUI();
            initializeValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(PaymentContractsActvity.this);
        helper = new Helper(PaymentContractsActvity.this);
        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);

        int environment = PayuConstants.PRODUCTION_ENV;
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        mPaymentParams = new PaymentParams();
    }

    private void initializeUI() {
        userName = preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,"");
        userEmail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
        userMobile = preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,"");
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        // realm = Realm.getDefaultInstance();
        amountStr = getIntent().getStringExtra("Amount");
        jobQuoteId = getIntent().getStringExtra("JobQuoteId");
        quoteId = getIntent().getStringExtra("QuotedId");
        jobId = getIntent().getStringExtra("JobId");
        logisticsStr = getIntent().getStringExtra("Logistics");
        orderRandId = getIntent().getStringExtra("OrderRandId");
        serviceId = getIntent().getStringExtra("ServiceId");
        transportId = getIntent().getStringExtra("TransportId");
        KmRangeStr =  getIntent().getStringExtra("KmRange");
        ChargePerKmStr = getIntent().getStringExtra("ChargePerKm");
        makeOfferIdStr = getIntent().getStringExtra("makeOfferId");
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        ll_quote_received_details = (LinearLayout) findViewById(R.id.ll_quote_received_details);
        close_payment_dialog = (ImageView)findViewById(R.id.close_payment_dialog);
        close_send_quote = (ImageView) findViewById(R.id.close_send_quote);
        km_range = (TextView) findViewById(R.id.km_range);
        charge_per_km = (TextView) findViewById(R.id.charge_per_km);
        total_charge = (TextView) findViewById(R.id.total_charge);
        amount_tobe_paid = (TextView) findViewById(R.id.amount_tobe_paid);
        if(logisticsStr.equalsIgnoreCase("1")){
            ll_quote_received_details.setVisibility(View.VISIBLE);
            close_payment_dialog.setVisibility(View.GONE);
            km_range.setText(KmRangeStr);
            charge_per_km.setText(ChargePerKmStr);
            total_charge.setText(amountStr);
            amount_tobe_paid.setText(amountStr);
        }else if(logisticsStr.equalsIgnoreCase("0")){
            ll_quote_received_details.setVisibility(View.GONE);
            close_payment_dialog.setVisibility(View.VISIBLE);
        }else if(logisticsStr.equalsIgnoreCase("2")){
            ll_quote_received_details.setVisibility(View.GONE);
            close_payment_dialog.setVisibility(View.VISIBLE);
        }else if(logisticsStr.equalsIgnoreCase("Offers")){
            ll_quote_received_details.setVisibility(View.GONE);
            close_payment_dialog.setVisibility(View.VISIBLE);
        }else if(logisticsStr.equalsIgnoreCase("3")){
            ll_quote_received_details.setVisibility(View.GONE);
            close_payment_dialog.setVisibility(View.VISIBLE);
        }
        received_orders_cardview = (CardView) findViewById(R.id.received_orders_cardview);
        ll_payment = (LinearLayout) findViewById(R.id.ll_payment);
        ll_order_placed_text = (LinearLayout) findViewById(R.id.ll_order_placed_text);
        ll_bank_details = (LinearLayout) findViewById(R.id.ll_bank_details);
        pay_now_btn = (Button) findViewById(R.id.pay_now_btn);
        pay_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                // selectedRadioBtn = radioButton.getText().toString();
                if (selectedId < 0) {
                    Toast.makeText(PaymentContractsActvity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                } else if (paymentType.equalsIgnoreCase("online")) {
                    helper = new Helper(PaymentContractsActvity.this);
                    helper.showDialog(PaymentContractsActvity.this, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with online payment?",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    // PayuBiz
                                    if(logisticsStr.equalsIgnoreCase("1")){
                                        setParameter();
                                        generateHashFromSDK(mPaymentParams, salt);
                                    }else if(logisticsStr.equalsIgnoreCase("0")) {
                                        makePaymentBank();
                                        setParameter();
                                        generateHashFromSDK(mPaymentParams, salt);
                                    }else if(logisticsStr.equalsIgnoreCase("2")){
                                        setParameter();
                                        generateHashFromSDK(mPaymentParams, salt);
                                    }else if(logisticsStr.equalsIgnoreCase("Offers")){
                                        makeOfferPaymentBank();
                                        setParameter();
                                        generateHashFromSDK(mPaymentParams, salt);
                                    }else if(logisticsStr.equalsIgnoreCase("3")){
                                        setParameter();
                                        generateHashFromSDK(mPaymentParams, salt);
                                    }

                                }
                            }, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                } else {
                    helper.showDialog(PaymentContractsActvity.this, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with pay by bank?",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    if(logisticsStr.equalsIgnoreCase("1")){
                                        setJsonbject();
                                    }else if(logisticsStr.equalsIgnoreCase("0")) {
                                        makePaymentBank();
                                    }else if(logisticsStr.equalsIgnoreCase("2")){
                                        setJsonbject();
                                    }else if(logisticsStr.equalsIgnoreCase("Offers")){
                                        makeOfferPaymentBank();
                                    }else if(logisticsStr.equalsIgnoreCase("3")){
                                        setContractPayByBankJsonbject();
                                    }
                                }
                            }, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                }
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        cards_checkbox = (RadioButton) findViewById(R.id.cards_checkbox);
        netbanking_checkbox = (RadioButton) findViewById(R.id.netbanking_checkbox);
        upi_checkbox = (RadioButton) findViewById(R.id.upi_checkbox);
        payumoney_checkbox = (RadioButton) findViewById(R.id.payumoney_checkbox);
        paybank_radiobtn = (RadioButton) findViewById(R.id.pay_by_bank_radiobtn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cards_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.netbanking_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.upi_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.payumoney_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.pay_by_bank_radiobtn:
                        ll_bank_details.setVisibility(View.VISIBLE);
                        paymentType = "bank";
                        // do operations specific to this selection
                        break;
                }
            }
        });
        my_orders_text = (TextView) findViewById(R.id.my_orders_text);
        my_orders_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logisticsStr.equalsIgnoreCase("1")){
                    Intent i = new Intent(PaymentContractsActvity.this,LogisticsOrdersActivity.class);
                    i.putExtra("Logistics","0");
                    i.putExtra("Message","");
                    startActivity(i);
                }else if(logisticsStr.equalsIgnoreCase("0")) {
                    Intent i = new Intent(PaymentContractsActvity.this, MyContractOrdersActivity.class);
                    i.putExtra("PurchasedOrders","0");
                    i.putExtra("Message","");
                    startActivity(i);
                }
            }
        });
        close_payment_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        close_send_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(PaymentContractsActvity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void makePaymentBank() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        PostContractsApi service = retrofit.create(PostContractsApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.makePaymentByBank(user_id, "instant", amountStr, quoteId, jobQuoteId, paymentType, jobId);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("Success Call ", ">>>>" + response.body().toString());

                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d(
                        "Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                Log.d("Success Call", ">>>>" + call);

                if (response.body().toString() != null) {

                    if (response != null) {
                        String searchResponse = response.body().toString();
                        Log.d("Reg", "Response  >>" + searchResponse.toString());

                        if (searchResponse != null) {
                            JSONObject root = null;
                            try {
                                root = new JSONObject(searchResponse);
                                String success = null;
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    String message = root.getString("message");
                                    if(paymentType.equalsIgnoreCase("online")){
                                      //  Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(PaymentContractsActvity.this, "Thank you for Choosing Pay by Bank Option and further process to Check My Orders", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(PaymentContractsActvity.this, MyContractOrdersActivity.class);
                                        i.putExtra("Message","Thank you for Choosing Pay by Bank Option and further process to Check My Orders");
                                        i.putExtra("PurchasedOrders","1");
                                        startActivity(i);
                                        progressDialog.dismiss();
                                    }
                                } else if (success.equalsIgnoreCase("0")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(PaymentContractsActvity.this, "", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //Toast.makeText(BusinessCategory.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }
    public void makeOfferPaymentBank() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("action", paymentType);
            jsonObj.put("makeofferid",makeOfferIdStr);

            androidNetworkingMakeOfferPayment(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingMakeOfferPayment(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/makepayment/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject jsonObject1 = response.getJSONObject("response");
                            String message = jsonObject1.getString("message");
                            String success = jsonObject1.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if(paymentType.equalsIgnoreCase("online")){
                                   // Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                                }else {
                                    Snackbar snackbar = Snackbar
                                            .make(linearlayout,"Thank you for Choosing Pay by Bank Option and further process to Check My Orders", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    Intent i = new Intent(PaymentContractsActvity.this, StoreOffersActivity.class);
                                    i.putExtra("Message", "Thank you for Choosing Pay by Bank Option and further process to Check My Orders");
                                    i.putExtra("SentOffers", "1");
                                    startActivity(i);
                                   // progressDialog.dismiss();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }

    public void setJsonbject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("order_ran", orderRandId);
            jsonObj.put("service_id",serviceId);
            jsonObj.put("action","bank");

            androidNetworkingpayByBank(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingpayByBank(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Transportservices/transportbankpayment/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String message = response.getString("message");
                            String success = response.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,"Thank you for Choosing Pay by Bank Option and further process to Check My Orders", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Intent i = new Intent(PaymentContractsActvity.this, LogisticsOrdersActivity.class);
                                i.putExtra("Message","Thank you for Choosing Pay by Bank Option and further process to Check My Orders");
                                i.putExtra("Logistics","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setContractPayByBankJsonbject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("order_ran", orderRandId);
            jsonObj.put("service_id",serviceId);
            jsonObj.put("action","bank");

            androidNetworkingContractpayByBank(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingContractpayByBank(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/transportbankpayment/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String message = response.getString("message");
                            String success = response.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,"Thank you for Choosing Pay by Bank Option and further process to Check My Orders", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Intent i = new Intent(PaymentContractsActvity.this, LogisticsOrdersActivity.class);
                                i.putExtra("Message","Thank you for Choosing Pay by Bank Option and further process to Check My Orders");
                                i.putExtra("Logistics","0");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUSuccess() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("job_id",jobId);
            jsonObj.put("quotedid",quoteId);
            jsonObj.put("pay_type","instant");
            jsonObj.put("jobquatid",jobQuoteId);
            jsonObj.put("amount",amount);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("firstname",preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,""));
            jsonObj.put("country","India");
            jsonObj.put("email",preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,""));
            jsonObj.put("phone",preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,""));
            jsonObj.put("bankcode",bankCode);
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            androidNetworkingPayUSuccess(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayUSuccess(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/quotepayuSuccess/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, "", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PaymentContractsActvity.this, ReceivedContracts.class);
                               // i.putExtra("Message","");
                               // i.putExtra("PurchasedOrders","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUFailure() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("job_id",jobId);
            jsonObj.put("quotedid",quoteId);
            jsonObj.put("pay_type","instant");
            jsonObj.put("jobquatid",jobQuoteId);
            jsonObj.put("amount",amount);
            jsonObj.put("mode","");
            jsonObj.put("mihpayid","");
            jsonObj.put("firstname",preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,""));
            jsonObj.put("country","India");
            jsonObj.put("email",preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,""));
            jsonObj.put("phone",preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,""));
            jsonObj.put("bankcode","");
            jsonObj.put("PG_TYPE","");
            jsonObj.put("bank_ref_num","");
            androidNetworkingPayUFailure(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayUFailure(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/quotepayuFailure/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Intent i = new Intent(PaymentContractsActvity.this,ReceivedContracts.class);
                                i.putExtra("Message","");
                                startActivity(i);
                               // Toast.makeText(PaymentContractsActvity.this, message,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                /*ll_payment.setVisibility(View.VISIBLE);
                                ll_order_placed_text.setVisibility(View.GONE);*/
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayULogisticsSuccess() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("order_ran",orderRandId);
            jsonObj.put("service_id",serviceId);
            jsonObj.put("amount",amount);
            jsonObj.put("transport_id",transportId);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingLogisticsPayUSuccess(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingLogisticsPayUSuccess(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Transportservices/transportpayuSuccess")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, "", Toast.LENGTH_SHORT).show();
                                ll_payment.setVisibility(View.GONE);
                                Intent i = new Intent(PaymentContractsActvity.this, LogisticsOrdersActivity.class);
                                i.putExtra("Message","");
                                i.putExtra("Logistics","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUContractsLogisticsSuccess() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("order_ran",orderRandId);
            jsonObj.put("service_id",serviceId);
            jsonObj.put("amount",amount);
            jsonObj.put("transport_id",transportId);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingContractLogisticsPayUSuccess(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingContractLogisticsPayUSuccess(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Transportservices/transportpayuSuccess")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, "", Toast.LENGTH_SHORT).show();
                                ll_payment.setVisibility(View.GONE);
                                Intent i = new Intent(PaymentContractsActvity.this, LogisticsOrdersActivity.class);
                                i.putExtra("Message","");
                                i.putExtra("Logistics","0");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUFailureLogistics() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("order_ran",orderRandId);
            jsonObj.put("service_id",serviceId);
            jsonObj.put("amount",amount);
            jsonObj.put("transport_id",transportId);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingPayUFailureLogistics(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayUFailureLogistics(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Transportservices/transportpayuFailure")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                               // Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PaymentContractsActvity.this,ReceivedContracts.class);
                                i.putExtra("Message","");
                                i.putExtra("Logistics","1");
                                startActivity(i);
                                /*ll_payment.setVisibility(View.VISIBLE);
                                ll_order_placed_text.setVisibility(View.GONE);*/
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUContractFailure() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("order_ran",orderRandId);
            jsonObj.put("service_id",serviceId);
            jsonObj.put("amount",amount);
            jsonObj.put("transport_id",transportId);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingPayUFailureContractLogistics(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayUFailureContractLogistics(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservices/transportpayuFailure")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                // Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PaymentContractsActvity.this,ReceivedContracts.class);
                                i.putExtra("Message","");
                                i.putExtra("Logistics","0");
                                startActivity(i);
                                /*ll_payment.setVisibility(View.VISIBLE);
                                ll_order_placed_text.setVisibility(View.GONE);*/
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUMakeOfferSuccess() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("amount",amount);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("discount","");
            jsonObj.put("net_amount",amount);
            jsonObj.put("makeofferid",makeOfferIdStr);
            jsonObj.put("firstname",preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,""));
            jsonObj.put("email",preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,""));
            jsonObj.put("phone",preferenceUtils.getStringFromPreference(preferenceUtils.MOBILE,""));
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingMakeOfferPayUSuccess(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingMakeOfferPayUSuccess(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/makeofpayuSuccess/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, "", Toast.LENGTH_SHORT).show();
                                ll_payment.setVisibility(View.GONE);
                                Intent i = new Intent(PaymentContractsActvity.this, StoreOffersActivity.class);
                                i.putExtra("Message","");
                                i.putExtra("SentOffers","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setPayUMakeOfferFailure() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("amount",amount);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("discount","");
            jsonObj.put("net_amount",amount);
            jsonObj.put("makeofferid",makeOfferIdStr);
            jsonObj.put("firstname",preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,""));
            jsonObj.put("email",preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,""));
            jsonObj.put("phone",preferenceUtils.getStringFromPreference(preferenceUtils.MOBILE,""));
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingMakeOfferPayUFailure(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingMakeOfferPayUFailure(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/makeofpayuFailure/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                // Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                                ll_payment.setVisibility(View.GONE);
                                Intent i = new Intent(PaymentContractsActvity.this, StoreOffersActivity.class);
                                i.putExtra("Message","");
                                i.putExtra("SentOffers","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentContractsActvity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setParameter() {
        mPaymentParams = new PaymentParams();
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(amountStr);
        mPaymentParams.setProductInfo("Sustowns Product");

        mPaymentParams.setFirstName(preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME, ""));

        mPaymentParams.setEmail(preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL, ""));
        mPaymentParams.setPhone(preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE, ""));
        mPaymentParams.setAddress1(preferenceUtils.getStringFromPreference(PreferenceUtils.Cust_Address, ""));
        mPaymentParams.setZipCode(preferenceUtils.getStringFromPreference(PreferenceUtils.ZIPCODE, ""));
        //Set Your transaction ID. example- "USER ID "+System.currentTimeMillis()
        transactionId = System.currentTimeMillis()+"";
        mPaymentParams.setTxnId(transactionId);
        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */

        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        mPaymentParams.setUdf1("udf1");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");

        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        userCredentials = merchantKey + ":" + "super";
        mPaymentParams.setUserCredentials(userCredentials);
        //mPaymentParams.setUserCredentials(PayuConstants.DEFAULT);
    }
    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();

        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }
        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }
        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }
        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }

    public void launchSdkUI(PayuHashes payuHashes) {
        mPaymentParams.setHash(payuHashes.getPaymentHash());
        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
                Log.d("PayU Data :\n", data.getStringExtra("result"));
                Log.d("PayU Full Data :\n", data+"");
                Map<String, String> map = Splitter.on(";").withKeyValueSeparator("=").split(data.getStringExtra("result").replace("&amp", ""));

                bankCode = map.get("bankcode");
                mihpayid = map.get("mihpayid");
                mode = map.get("mode");
                status = map.get("status");
                txnid  = map.get("txnid");
                amount = map.get("amount");
                net_amount_debit = map.get("net_amount_debit");
                firstname = map.get("firstname");
                email = map.get("email");
                phone = map.get("phone");
                hash = map.get("hash");
                payment_source = map.get("payment_source");
                PG_TYPE = map.get("PG_TYPE");
                bank_ref_num = map.get("bank_ref_num");

                Helper helper = new Helper(PaymentContractsActvity.this);
                helper.singleClickAlert(PaymentContractsActvity.this, SweetAlertDialog.SUCCESS_TYPE, "Success", "Payment is successfully completed.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                if(logisticsStr.equalsIgnoreCase("1")){
                                    setPayULogisticsSuccess();
                                }else if(logisticsStr.equalsIgnoreCase("0")) {
                                    setPayUSuccess();
                                }else if(logisticsStr.equalsIgnoreCase("2")){
                                    setPayULogisticsSuccess();
                                }else if(logisticsStr.equalsIgnoreCase("Offers")){
                                    setPayUMakeOfferSuccess();
                                }else if(logisticsStr.equalsIgnoreCase("3")){
                                    setPayUContractsLogisticsSuccess();
                                }
                                //Write the next set of code in the success
                            }
                        });
            } else {
                if(logisticsStr.equalsIgnoreCase("1")){
                    setPayUFailureLogistics();
                }else if(logisticsStr.equalsIgnoreCase("0")) {
                    setPayUFailure();
                }else if(logisticsStr.equalsIgnoreCase("2")){
                    setPayUFailureLogistics();
                }else if(logisticsStr.equalsIgnoreCase("Offers")){
                    setPayUMakeOfferFailure();
                }else if(logisticsStr.equalsIgnoreCase("3")){
                    setPayUContractFailure();
                }
               // setPayUFailure();
                // Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }

}
