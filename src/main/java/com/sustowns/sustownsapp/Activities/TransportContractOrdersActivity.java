package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sustowns.sustownsapp.Adapters.TransportReceivedOrdersAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.TransportApi;
import com.sustowns.sustownsapp.Api.WebServices;
import com.sustowns.sustownsapp.Models.TransportContractOrdersModel;
import com.sustowns.sustownsapp.Models.TransportOrdersModelObj;
import com.sustowns.sustownsapp.helpers.Helper;
import com.sustowns.sustownsapp.listeners.DataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransportContractOrdersActivity extends AppCompatActivity implements DataListener {

    RecyclerView recycler_view_transportorders;
    ImageView backarrow, cart_img;
    PreferenceUtils preferenceUtils;
    String user_id, ordersValue, s_zipcode, b_zipcode, chargePerKm, getkms, getpricekms, order_id, service_id,invoiceNoStr;
    ProgressDialog progressDialog;
    ArrayList<TransportContractOrdersModel> transportDetailsModels;
    TransportReceivedOrdersAdapter transportReceivedOrdersAdapter;
    TextView available_text, total_price, kilometers_text,order_vehicle_type,order_load_type,order_quote,order_charge_km,order_min_charge;
    Helper helper;
    AlertDialog alertDialog;
    WebServices webServices;
    Button radius_btn, extended_radius_btn;
    EditText charge_per_km;
    String OrderId,ServiceId,seller_zipcode,buyer_zipcode;
    TextView order_prod_name,order_category_type,order_weight,order_quantity,vendor_name_transport,invoice_no, order_packing_type,order_shipped_to,order_seller_address;
    TextView shipping_name,shipping_number,shipping_address,shipping_postalcode,seller_name_text,seller_number_text,seller_address_text,seller_postalcode_text;
    SwipeRefreshLayout swipeRefreshLayout;
    TableRow ll_weight,ll_pack_type,ll_category,ll_vendor_name;
    EditText driver_name,mobile_number,product_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transport_orders);
        preferenceUtils = new PreferenceUtils(TransportContractOrdersActivity.this);
        helper = new Helper(this);
        webServices = new WebServices(this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        ordersValue = "Radius";

        available_text = (TextView) findViewById(R.id.available_text);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        recycler_view_transportorders = (RecyclerView) findViewById(R.id.recycler_view_transportorders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TransportContractOrdersActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_transportorders.setLayoutManager(layoutManager);
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportContractOrdersActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportContractOrdersActivity.this, TradeManagementActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        radius_btn = (Button) findViewById(R.id.radius_btn);
        extended_radius_btn = (Button) findViewById(R.id.extended_radius_btn);
        radius_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersValue = "Radius";
                radius_btn.setTextColor(getResources().getColor(R.color.white));
                extended_radius_btn.setTextColor(getResources().getColor(R.color.black));
                radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                extended_radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractTransportOrdersList();
            }
        });
        extended_radius_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersValue = "Extended Radius";
                extended_radius_btn.setTextColor(getResources().getColor(R.color.white));
                radius_btn.setTextColor(getResources().getColor(R.color.black));
                extended_radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractTransportOrdersList();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ordersValue = "Radius";
                radius_btn.setTextColor(getResources().getColor(R.color.white));
                extended_radius_btn.setTextColor(getResources().getColor(R.color.black));
                radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                extended_radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractTransportOrdersList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        getContractTransportOrdersList();
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(TransportContractOrdersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void getContractTransportOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);
        TransportOrdersModelObj transportOrdersModel1 = new TransportOrdersModelObj();
        if (ordersValue.equalsIgnoreCase("Radius")) {
            transportOrdersModel1.setTransport_userid(user_id);
            transportOrdersModel1.setManual_automatic("automatic"); // Radius
        } else if (ordersValue.equalsIgnoreCase("Extended Radius")) {
            transportOrdersModel1.setTransport_userid(user_id);
            transportOrdersModel1.setManual_automatic("manual");// Extended Radius
        }
        Call<JsonObject> callRetrofit = null;
        callRetrofit = service.getTransportContractReceivedOrders(transportOrdersModel1);
        callRetrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));

                        System.out.println("----------------------------------------------------");

                        if (response.body().toString() != null) {
                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Categeries", "response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String status = root.getString("status");
                                        String error = root.getString("error");
                                        if (error.equalsIgnoreCase("false")) {
                                            JSONObject responseObj = root.getJSONObject("response");
                                            JSONArray dataArray = responseObj.getJSONArray("data");
                                            transportDetailsModels = new ArrayList<>();
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                order_id = jsonObject.getString("order_id");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String order_date = jsonObject.getString("order_date");
                                                String quantity = jsonObject.getString("quantity");
                                                service_id = jsonObject.getString("service_id");
                                                String service_name = jsonObject.getString("service_name");
                                                String trans_status = jsonObject.getString("trans_status");
                                                String trans_status_text = jsonObject.getString("trans_status_text");
                                                String manual_automatic = jsonObject.getString("manual_automatic");
                                                String confirm_allow = jsonObject.getString("confirm_allow");
                                                String quote_allow = jsonObject.getString("quote_allow");
                                                seller_zipcode = jsonObject.getString("seller_zipcode");
                                                buyer_zipcode = jsonObject.getString("buyer_zipcode");
                                                String chargeperkm = jsonObject.getString("chargeperkm");

                                                TransportContractOrdersModel transportOrderModel = new TransportContractOrdersModel();
                                                transportOrderModel.setId(id);
                                                transportOrderModel.setUser_id(user_id);
                                                transportOrderModel.setOrder_id(order_id);
                                                transportOrderModel.setService_id(service_id);
                                                transportOrderModel.setService_name(service_name);
                                                transportOrderModel.setPr_sku(pr_sku);
                                                transportOrderModel.setPr_title(pr_title);
                                                transportOrderModel.setPr_weight(pr_weight);
                                                transportOrderModel.setInvoice_no(invoice_no);
                                                transportOrderModel.setOrder_date(order_date);
                                                transportOrderModel.setQuantity(quantity);
                                                transportOrderModel.setTrans_status(trans_status);
                                                transportOrderModel.setTrans_status_text(trans_status_text);
                                                transportOrderModel.setManual_automatic(manual_automatic);
                                                transportOrderModel.setConfirm_allow(confirm_allow);
                                                transportOrderModel.setQuote_allow(quote_allow);
                                                transportOrderModel.setSeller_zipcode(seller_zipcode);
                                                transportOrderModel.setBuyer_zipcode(buyer_zipcode);
                                                transportOrderModel.setChargeperkm(chargeperkm);
                                                transportDetailsModels.add(transportOrderModel);
                                            }
                                            if (transportDetailsModels != null) {
                                                transportReceivedOrdersAdapter = new TransportReceivedOrdersAdapter(TransportContractOrdersActivity.this, null, transportDetailsModels);
                                                recycler_view_transportorders.setAdapter(transportReceivedOrdersAdapter);
                                                transportReceivedOrdersAdapter.notifyDataSetChanged();
                                                available_text.setVisibility(View.GONE);
                                            } else {
                                                recycler_view_transportorders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                                available_text.setText("Orders Are Not Available");
                                            }
                                            progressDialog.dismiss();
                                        } else {
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    } else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               Toast.makeText(TransportContractOrdersActivity.this, "Some thing went wrong! please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void confirmOrder(final int position) {
        helper.showDialog(TransportContractOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "Are you sure?", "Do you want to confirm?",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        showConfirmRejectDialog(true, position);
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }

    public void rejectOrder(final int position) {
        helper.showDialog(TransportContractOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "Are you sure?", "Do you want to reject?",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        showConfirmRejectDialog(false, position);
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }

    public void viewDetails(int position) {
         OrderId = transportDetailsModels.get(position).getInvoice_no();
         ServiceId = transportDetailsModels.get(position).getService_id();
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportContractOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_transport_view_details, null);
            dialogBuilder.setView(dialogView);
            ll_weight = dialogView.findViewById(R.id.ll_weight);
            ll_pack_type = dialogView.findViewById(R.id.ll_pack_type);
            ll_category = dialogView.findViewById(R.id.ll_category);
            ll_vendor_name = dialogView.findViewById(R.id.ll_vendor_name);
            ll_weight.setVisibility(View.VISIBLE);
            ll_pack_type.setVisibility(View.GONE);
            ll_category.setVisibility(View.VISIBLE);
            ll_vendor_name.setVisibility(View.VISIBLE);
            vendor_name_transport = dialogView.findViewById(R.id.vendor_name_transport);
            invoice_no = dialogView.findViewById(R.id.invoice_no);
            order_prod_name = dialogView.findViewById(R.id.order_prod_name);
            order_weight = dialogView.findViewById(R.id.order_weight);
            order_category_type = dialogView.findViewById(R.id.order_category_type);
            order_quantity = dialogView.findViewById(R.id.order_quantity);
            order_packing_type = dialogView.findViewById(R.id.order_packing_type);
            order_vehicle_type = dialogView.findViewById(R.id.order_vehicle_type);
            order_load_type = dialogView.findViewById(R.id.order_load_type);
            order_charge_km = dialogView.findViewById(R.id.order_charge_km);
            order_quote = dialogView.findViewById(R.id.order_quote);
            order_min_charge = dialogView.findViewById(R.id.order_min_charge);
            shipping_name = dialogView.findViewById(R.id.shipping_name);
            shipping_number = dialogView.findViewById(R.id.shipping_number);
            shipping_address = dialogView.findViewById(R.id.shipping_address);
            shipping_postalcode = dialogView.findViewById(R.id.shipping_postalcode);
            seller_name_text = dialogView.findViewById(R.id.seller_name);
            seller_number_text = dialogView.findViewById(R.id.seller_number);
            seller_address_text = dialogView.findViewById(R.id.seller_address);
            seller_postalcode_text = dialogView.findViewById(R.id.seller_postalcode);
            Button order_close = dialogView.findViewById(R.id.order_close);
            order_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            getTransportContractDetailsList();
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

    public void sendQuoteDetails(int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportContractOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.send_quote_dialog, null);
            dialogBuilder.setView(dialogView);
            LinearLayout ll_confirm = dialogView.findViewById(R.id.ll_confirm);
            ll_confirm.setVisibility(View.VISIBLE);
            ImageView close_send_quote = dialogView.findViewById(R.id.close_send_quote);
            TextView shipping_pincode = dialogView.findViewById(R.id.shipping_pincode);
            TextView delivery_pincode = dialogView.findViewById(R.id.delivery_pincode);
            kilometers_text = dialogView.findViewById(R.id.kilometers_text);
            total_price = dialogView.findViewById(R.id.total_price);
            charge_per_km = dialogView.findViewById(R.id.charge_per_km);
            driver_name = dialogView.findViewById(R.id.driver_name);
            mobile_number = dialogView.findViewById(R.id.mobile_number);
            product_address = dialogView.findViewById(R.id.product_address);
            charge_per_km.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    getKmsBasedPincodes();
                }
            });
            Button submit_sendquote = dialogView.findViewById(R.id.submit_sendquote);
            submit_sendquote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitSendQuoteJsonObject();
                }
            });
            s_zipcode = transportDetailsModels.get(position).getSeller_zipcode();
            b_zipcode = transportDetailsModels.get(position).getBuyer_zipcode();
            chargePerKm = transportDetailsModels.get(position).getChargeperkm();
            service_id = transportDetailsModels.get(position).getService_id();
            invoiceNoStr = transportDetailsModels.get(position).getInvoice_no();
            shipping_pincode.setText(s_zipcode);
            delivery_pincode.setText(b_zipcode);
            charge_per_km.setText(chargePerKm);
            // kilometers_text.setText(transportOrdersModels.get(position).getQuantity());
            // total_price.setText(transportOrdersModels.get(position).getPacktype());
            getKmsBasedPincodes();
            close_send_quote.setOnClickListener(new View.OnClickListener() {
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

    public void getKmsBasedPincodes() {
        //progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getContractKmsBasePincodes(s_zipcode, b_zipcode, charge_per_km.getText().toString());
        // callRetrofit = service.getKmsBasePincodes(s_zipcode,b_zipcode,charge_per_km.getText().toString());
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        // progressDialog.dismiss();
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));
                        System.out.println("----------------------------------------------------");
                        if (response.body().toString() != null) {
                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Categeries", "response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            getkms = root.getString("getkms");
                                            getpricekms = root.getString("getpricekms");
                                            kilometers_text.setText(getkms);
                                            total_price.setText(getpricekms);
                                        } else {
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
                                            // progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // progressDialog.dismiss();
                                }
                            }
                        }
                    } else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        // progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }
        });
    }

    public void submitSendQuoteJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("ccost", charge_per_km.getText().toString());
            jsonObj.put("km", getkms);
            jsonObj.put("total", getpricekms);
            jsonObj.put("oid", invoiceNoStr);
            jsonObj.put("sid", service_id);
            jsonObj.put("userid", user_id);
            jsonObj.put("driver_name",driver_name.getText().toString());
            jsonObj.put("driver_number",mobile_number.getText().toString());
            jsonObj.put("vehicle_number",product_address.getText().toString());

            androidNetworkingSubmitQuote(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void androidNetworkingSubmitQuote(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/sendquotesubmit/")
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
                            if (message.equalsIgnoreCase("Quote Submitted successfully")) {
                                progressDialog.dismiss();
                                Toast.makeText(TransportContractOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                getContractTransportOrdersList();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(TransportContractOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
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

    public void getTransportContractDetailsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportContractOrdersDetails(OrderId, ServiceId);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));

                        System.out.println("----------------------------------------------------");

                        if (response.body().toString() != null) {
                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Categeries", "response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success = root.getString("success");
                                        String message = root.getString("message");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONObject orderitemObj = root.getJSONObject("orderitem");
                                            String order_id = orderitemObj.getString("order_id");
                                           // String seller_name = orderitemObj.getString("seller_name");
                                           // String seller_number = orderitemObj.getString("seller_number");
                                          //  String seller_address = orderitemObj.getString("seller_address");
                                            String seller_zipcode = orderitemObj.getString("seller_zipcode");
                                            String display_name = orderitemObj.getString("display_name");
                                            String bill_phone = orderitemObj.getString("bill_phone");
                                            String bill_address1 = orderitemObj.getString("bill_address1");
                                            String bill_zipcode = orderitemObj.getString("bill_zipcode");
                                            String service_id = orderitemObj.getString("service_id");
                                            String contractname = orderitemObj.getString("contractname");
                                            String minqantity = orderitemObj.getString("minqantity");
                                            String invoice_noStr = orderitemObj.getString("invoice_no");
                                            String category = orderitemObj.getString("category");
                                            String vehicle_type = orderitemObj.getString("vehicle_type");
                                            String unit_code = orderitemObj.getString("unit_code");

                                            JSONObject vehicleObj = root.getJSONObject("vehicle");
                                            String service_id1 = vehicleObj.getString("service_id");
                                            String qchrg_km = vehicleObj.getString("qchrg_km");
                                            String qminchrg_km = vehicleObj.getString("qminchrg_km");
                                            String vehicle_type1 = vehicleObj.getString("vehicle_type");
                                            String service_name = vehicleObj.getString("service_name");
                                            String load = root.getString("load");

                                            invoice_no.setText(invoice_noStr);
                                            order_prod_name.setText(contractname);
                                            order_weight.setText(unit_code);
                                            vendor_name_transport.setText(service_name);
                                            order_category_type.setText(category);
                                            order_quantity.setText(minqantity);
                                            order_vehicle_type.setText(vehicle_type1);
                                            order_load_type.setText(load);
                                            order_charge_km.setText(qchrg_km);
                                            order_min_charge.setText(qminchrg_km);
                                            shipping_name.setText(display_name);
                                            shipping_number.setText(bill_phone);
                                            shipping_address.setText(bill_address1);
                                            shipping_postalcode.setText(bill_zipcode);
                                          //  seller_name_text.setText(seller_name);
                                           // seller_number_text.setText(seller_number);
                                          //  seller_address_text.setText(seller_address);
                                            seller_postalcode_text.setText(seller_zipcode);
                                            progressDialog.dismiss();
                                        } else {
                                            Toast.makeText(TransportContractOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    } else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void showConfirmRejectDialog(final boolean isConfirm, final int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportContractOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.confirm_trans_order_dialog, null);
            dialogBuilder.setView(dialogView);
            driver_name = dialogView.findViewById(R.id.driver_name);
            mobile_number = dialogView.findViewById(R.id.mobile_number);
            product_address = dialogView.findViewById(R.id.product_address);

            LinearLayout ll_reason_rejection = dialogView.findViewById(R.id.ll_reason_rejection);
            LinearLayout ll_confirm = dialogView.findViewById(R.id.ll_confirm);
            final EditText reason_rejection = dialogView.findViewById(R.id.reason_rejection);

            Button submit_button = dialogView.findViewById(R.id.submit_button);
            Button cancel_btn = dialogView.findViewById(R.id.cancel_btn);

            if (isConfirm) {
                ll_confirm.setVisibility(View.VISIBLE);
                ll_reason_rejection.setVisibility(View.GONE);
            } else {
                ll_confirm.setVisibility(View.GONE);
                ll_reason_rejection.setVisibility(View.VISIBLE);
            }

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConfirm) {
                        if (driver_name.getText().toString().trim().isEmpty() || mobile_number.getText().toString().trim().isEmpty() ||
                                product_address.getText().toString().trim().isEmpty()) {

                            helper.singleClickAlert(TransportContractOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "", "All fields are mandatoryr",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                        } else {
                            helper.showLoader(TransportContractOrdersActivity.this, "Loading", "Please wait for a while");

                            try {
                                JSONObject confirmObject = new JSONObject();
                                confirmObject.put("userId", user_id);
                                confirmObject.put("orderRandomId",transportDetailsModels.get(position).getInvoice_no());
                                confirmObject.put("serviceId",transportDetailsModels.get(position).getService_id());
                                confirmObject.put("orderStatus", "confirm");
                                confirmObject.put("driverName", driver_name.getText().toString().trim());
                                confirmObject.put("conactNumber", mobile_number.getText().toString().trim());
                                confirmObject.put("vehicleNumber", product_address.getText().toString().trim());

                                webServices.postJsonBodyAndGetJsonObject(DZ_URL.POST_TRANSPORT_CONTRACT_VENDOR_CONFIRM, confirmObject,"1");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (reason_rejection.getText().toString().trim().isEmpty()) {
                            helper.singleClickAlert(TransportContractOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "", "All fields are mandatoryr",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                        } else {
                            helper.showLoader(TransportContractOrdersActivity.this, "Loading", "Please wait for a while");
                            try {
                                JSONObject rejectOrder = new JSONObject();
                                rejectOrder.put("userId", user_id);
                                rejectOrder.put("orderRandomId", transportDetailsModels.get(position).getInvoice_no());
                                rejectOrder.put("serviceId", transportDetailsModels.get(position).getService_id());
                                rejectOrder.put("orderStatus", "reject");
                                rejectOrder.put("comment", reason_rejection.getText().toString().trim());

                                webServices.postJsonBodyAndGetJsonObject(DZ_URL.POST_TRANSPORT_CONTRACT_VENDOR_CONFIRM, rejectOrder,"1");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onDataRetrieved(Object data, String whichUrl) {
        helper.hideLoader();
        try {
            JSONObject response = (JSONObject) data;
            if (response.getString("status").equalsIgnoreCase("success")) {
                alertDialog.dismiss();
                helper.singleClickAlert(TransportContractOrdersActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Success!", "Your request has submitted successfully.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            } else {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            alertDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Object data) {
        helper.hideLoader();
        helper.singleClickAlert(TransportContractOrdersActivity.this, SweetAlertDialog.ERROR_TYPE, "Error!", "Something went wrong, Please try again later.",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }
}
