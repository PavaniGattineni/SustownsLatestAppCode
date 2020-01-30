package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.sustowns.sustownsapp.Adapters.TransportReceivedOrdersAdapter;
import com.sustowns.sustownsapp.Api.WebServices;
import com.sustowns.sustownsapp.Models.TransportDetailsModel;
import com.sustowns.sustownsapp.helpers.Helper;
import com.sustowns.sustownsapp.listeners.DataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TransportDetailsActivity extends AppCompatActivity implements DataListener {
    RecyclerView recycler_view_transportorders;
    ImageView backarrow, cart_img;
    PreferenceUtils preferenceUtils;
    String user_id;
    ProgressDialog progressDialog;
    ArrayList<TransportDetailsModel> transportDetailsList;
    TransportReceivedOrdersAdapter transportReceivedOrdersAdapter;
    TextView available_text;
    Helper helper;
    WebServices webServices;
    AlertDialog alertDialog;

    String transactionId, merchantKey, surl, furl, service_provider, hashKeyValue, userCredentials, salt;
    String paymentType = "";
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    // Used when generating hash from SDK
    private PayUChecksum checksum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transport_orders);
        preferenceUtils = new PreferenceUtils(TransportDetailsActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        helper = new Helper(this);
        webServices = new WebServices(this);
        Payu.setInstance(this);

        TextView title_store = findViewById(R.id.title_store);
        title_store.setText("Transport Details");
        available_text = (TextView) findViewById(R.id.available_text);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        recycler_view_transportorders = (RecyclerView) findViewById(R.id.recycler_view_transportorders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TransportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_transportorders.setLayoutManager(layoutManager);
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportDetailsActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportDetailsActivity.this, TradeManagementActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
       // getTransportOrdersList();
    }

/*
    private void getTransportOrdersList() {
        helper.showLoader(TransportDetailsActivity.this, "Loading", "Please wait for a while...");

        webServices.getJsonArray(DZ_URL.GET_TRANSPORT_DETAILS_LIST+user_id);
        // webServices.getJsonArray(DZ_URL.GET_TRANSPORT_DETAILS_LIST+"445");
    }
*/

/*
    @Override
    public void onDataRetrieved(Object data, String whichUrl) {
        try {
            JSONArray responseArray = (JSONArray) data;

            transportDetailsList = new ArrayList<>();
            if(responseArray.length() > 0){
                for(int i=0; i < responseArray.length(); i++){
                    JSONObject jsonObject = responseArray.getJSONObject(i);

                    TransportDetailsModel transportDetailsModel = new TransportDetailsModel(
                            jsonObject.getString("id"),
                            jsonObject.getString("user_id"),
                            jsonObject.getString("order_id"),
                            jsonObject.getString("product_order_id"),
                            jsonObject.getString("product_id"),
                            jsonObject.getString("quantity"),
                            jsonObject.getString("pr_price"),
                            jsonObject.getString("driver_name"),
                            jsonObject.getString("driver_number"),
                            jsonObject.getString("vehicle_number"),
                            jsonObject.getString("order_ranid"),
                            jsonObject.getString("pr_title"),
                            jsonObject.getString("totalprice"),
                            jsonObject.getString("order_date"),
                            jsonObject.getString("seller_name"),
                            jsonObject.getString("seller_number"),
                            jsonObject.getString("seller_address"),
                            jsonObject.getString("seller_zipcode"),
                            jsonObject.getString("display_name"),
                            jsonObject.getString("pay_phone"),
                            jsonObject.getString("pay_address1"),
                            jsonObject.getString("pay_zipcode"),
                            jsonObject.getString("pr_weight"),
                            jsonObject.getString("pr_packtype"),
                            jsonObject.getString("invoice_no")
                    );
                    transportDetailsList.add(transportDetailsModel);
                }

                helper.hideLoader();
                setUpRecyclerView();
            } else {
                helper.hideLoader();
                helper.singleClickAlert(TransportDetailsActivity.this, SweetAlertDialog.NORMAL_TYPE, "", "No orders found.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            }
        } catch (Exception e){
            helper.hideLoader();
            e.printStackTrace();
        }
    }
*/

/*
    private void setUpRecyclerView() {
        if(transportReceivedOrdersAdapter == null){
            transportReceivedOrdersAdapter = new TransportReceivedOrdersAdapter(TransportDetailsActivity.this, null, transportDetailsList);
            recycler_view_transportorders.setAdapter(transportReceivedOrdersAdapter);
        }
        transportReceivedOrdersAdapter.notifyDataSetChanged();
    }
*/

    @Override
    public void onDataRetrieved(Object data, String whichUrl) {

    }

    @Override
    public void onError(Object data) {
        helper.hideLoader();
        helper.singleClickAlert(TransportDetailsActivity.this, SweetAlertDialog.ERROR_TYPE, "Error!", "Something went wrong, Please try again later.",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }

/*
    public void payOrder(final int position) {
        helper.showPayDialog(TransportDetailsActivity.this, SweetAlertDialog.WARNING_TYPE, "", "Please pay full Amount \n" + transportDetailsList.get(position).getTotal_price()+" /-",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        // Go to payment
                        navigateToBaseActivity(position);
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }
*/

/*
    public void contactDetails(int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportDetailsActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.view_transport_contact_details_dialog, null);
            dialogBuilder.setView(dialogView);

            TextView driver_name = dialogView.findViewById(R.id.driver_name);
            TextView driver_number = dialogView.findViewById(R.id.driver_number);
            TextView vehicle_number = dialogView.findViewById(R.id.vehicle_number);
            Button close_btn = dialogView.findViewById(R.id.close_btn);

            driver_name.setText(transportDetailsList.get(position).getDriver_name());
            driver_number.setText(transportDetailsList.get(position).getDriver_number());
            vehicle_number.setText(transportDetailsList.get(position).getVehicle_number());

            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = dialogBuilder.create();
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

/*
    public void viewDetails(int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportDetailsActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_transport_view_details1, null);
            dialogBuilder.setView(dialogView);

            TextView invoice_no = dialogView.findViewById(R.id.invoice_no);
            TextView order_prod_name = dialogView.findViewById(R.id.order_prod_name);
            TextView order_weight = dialogView.findViewById(R.id.order_weight);
            TextView order_quantity = dialogView.findViewById(R.id.order_quantity);
            TextView order_packing_type = dialogView.findViewById(R.id.order_packing_type);
            TextView order_shipped_to = dialogView.findViewById(R.id.order_shipped_to);
            TextView order_seller_address = dialogView.findViewById(R.id.order_seller_address);
            Button order_close = dialogView.findViewById(R.id.order_close);

            invoice_no.setText(transportDetailsList.get(position).getInvoice_number());
            order_prod_name.setText(transportDetailsList.get(position).getPr_title());
            order_weight.setText(transportDetailsList.get(position).getPr_weight());
            order_quantity.setText(transportDetailsList.get(position).getQuantity());
            order_packing_type.setText(transportDetailsList.get(position).getPr_packtype());
            order_shipped_to.setText(transportDetailsList.get(position).getDisplay_name()+"\n"+
                    transportDetailsList.get(position).getPay_phone()+"\n"+
                    transportDetailsList.get(position).getPay_address1()+"\n"+
                    transportDetailsList.get(position).getPay_zipcode());

            order_seller_address.setText(transportDetailsList.get(position).getSeller_name()+"\n"+
                    transportDetailsList.get(position).getSeller_number()+"\n"+
                    transportDetailsList.get(position).getSeller_address()+"\n"+
                    transportDetailsList.get(position).getSeller_zipcode());

            invoice_no.setText(transportDetailsList.get(position).getInvoice_number());
            order_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = dialogBuilder.create();
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public void navigateToBaseActivity(int position) {
        // merchantKey="";
//        int environment = PayuConstants.PRODUCTION_ENV;
//        merchantKey = "swpahz"; // Production Env
//        String email = "sruthi@xookey.com";
//        userCredentials = merchantKey + ":" +"super";
        merchantKey = "gtKFFx"; // Test
        int environment = PayuConstants.STAGING_ENV;
        String email = "payutest@gmail.com";
        userCredentials = merchantKey + ":" + email;

        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();
        /**
         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id
         */
        mPaymentParams.setKey(merchantKey);
     /*   mPaymentParams.setAmount(transportDetailsList.get(position).getTotal_price());
        mPaymentParams.setProductInfo(transportDetailsList.get(position).getPr_title());
        mPaymentParams.setFirstName(transportDetailsList.get(position).getDisplay_name());
        mPaymentParams.setEmail(email);
        mPaymentParams.setPhone(transportDetailsList.get(position).getPay_phone());
*/

        /*
         * Transaction Id should be kept unique for each transaction.
         * */
        mPaymentParams.setTxnId("" + System.currentTimeMillis());

        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */
        mPaymentParams.setSurl(" https://payuresponse.firebaseapp.com/success");
        mPaymentParams.setFurl("https://payuresponse.firebaseapp.com/failure");
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        // Production environment
//        mPaymentParams.setSurl("https://www.sustowns.com/Checkoutservice/suspayuSuccess");
//        mPaymentParams.setFurl("https://www.sustowns.com/Checkoutservice/suspayuFailure");
//        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        /*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */
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
        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        //   payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        generateHashFromServer(mPaymentParams);

        /**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * */
        if (environment == PayuConstants.STAGING_ENV) {
            salt = "13p0PXZk";
        } else {
            //Production Env
            salt = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCy5wHp70T8ki3519OqAtS7nijr/PiS/obvUaTKNGmvanjxNm1IZxYiZreDY6i6Q3wCUscio5fLYbe3Qg6Ox78Z16sc2jzzGoQYOaJERKuZkNUwpGuTnuTxWOU58DbecJ3OfzG0DvB2o++07xSKMazeCKT8CYqf5TlrS43BSfvBQRgGtLDc/3UBsjHIe+GtTQ8k9Z8yDfLCcBEPfJBGglDn9cThrod6W6vNi5osvhayGvWK+7VB6TxkDxcO9e5tpSEcf3y0q3RTUCRfckntQzV1GLJeYyyK/0HbDFa/P6EIyaFP7ddxz3H7PZU+zzWeqR/dA7kVMtNlM/0awAM/ud97AgMBAAECggEALNvR1gfCA5bf4mLk1x9omjbZSfsflTQvP3j4Wh90VKDc/JbKJjp7CVs6Lr7hgvsC8E4zUpM0r7Gr8E6m+dRKLb+vT0fFuqtubeidsIKLiLERU+YWd2iDPaUtrS53g6U3XJz2KJEKaKVIqNS+ELzN/MqsG+qEkP1YPXgvbkUP1HADO55E1jwe1CA6HVJXMB1HaX52JepwFefb3L8PEjC/K29CoqkqD1ivyWiL+jcGjEgGKAL8B1ajpaXYxY6a079ks8Rb5DBUDyd7Uy16pvHAxiGgcNY1Oc0bYHrcKF4V6y0WSNvRbIHPCeUN0ir27wYulOLaRYmmV+6xIE9U6lLbQQKBgQDrCkOb3jg1OBel8SiS1kjrgnHZPPjhtYiyfKcpGeKwnjxN9M3nXR1fd34NblOhSdUus73wKKoGcX/1qHs1HYQw8wCwjAZChLvCQ4+bFDWUomvAqMkhG2nOMfYuXdCOXOeX4IRaS4mxfjaI4UnZFVXpyk3BkLKp174hhUpSu7KKGwKBgQDC2yyLpetz0MjTck7UOxGrK6dIM9qpwpx42EyIsfZEg0Ow0N7ek2DdVqfBI0E96YiF/MB820P+V/uAv9zIsvBnhXgioEC9uYZW8cuxIZx0mP+ANqSnEGUAC0IMOhQ7E+ZgRSVwHvJuGUsJct3gXHEcwdIpTNMahqLw1S8RrwNWIQKBgQC1tfK9D1kvM9V8kmwG0aoAgZHZHH1hqoIrU5m8eem7GCqRLmD9rQxnYS9P3OrE+Brbh1Sh1h5U6uy9lGrkjpRDb6sp0qeIR6wNLURZ5Y7jAYsCoLsWYGY1ZoToJdl2JrdZwNcyI9IpKeWpf1sjfI42OBRTQLYP/t5cdfsW6UvH3wKBgBggsYRdOClwGd3s0ov/AHIUhrMvEI1itqNUDS4D8z2Kj3AufNpMn3roxv7oUaTL7QbCREdxkxrEZDUODNWhKpl28mg/NaUIevT7HcDCK7BYXZLPsVWqdwcpXbVL38Ns9GLJTRh0DIrLgPobihPE/pCUA2TvyVgIgdaWXyyuYhhhAoGAUkBzpwSGDEJeHQqrQKild/HKLSKTyVrk1dO7H6a6dnyOk567yC/QuKlkIJKxa2/Ma0FOreKVpiAxk6nyLbRZFr8fZ6uigDLrkYXYufvkKPDuUnTnEtxPYtmLywcy84buVeEpKqqDXhmh8Vo6jo5CCJR+X2ol+1I0bSgM87gsFbE=";
        }
//        String salt = "eCwWELxi";
        // String salt = "13p0PXZk";
        // String salt = "1b1b0";
        //
//        generateHashFromSDK(mPaymentParams, salt);
    }

    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.SALT, salt);
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
                //                new AlertDialog.Builder(this)
                //                        .setCancelable(false)
                //                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                //                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                //                            public void onClick(DialogInterface dialog, int whichButton) {
                //                                dialog.dismiss();
                //                            }
                //                        }).show();

                helper.singleClickAlert(TransportDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Success", "Payment has completed successfully.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            } else {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TransportDetailsActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                //   URL url = new URL("https://tsd.payu.in/GetHash");
//                URL url = new URL("https://secure.payu.in/_payment");
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }
}