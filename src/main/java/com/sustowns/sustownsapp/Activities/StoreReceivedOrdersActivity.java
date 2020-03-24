package com.sustowns.sustownsapp.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.Adapters.ReceivedOrdersAdapter;
import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.StoreReceivedOrdersAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.OrdersApi;
import com.sustowns.sustownsapp.Models.OrderModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreReceivedOrdersActivity extends AppCompatActivity {
    RecyclerView recycler_view_received_orders;
    ImageView backarrow, cart_img;
    StoreReceivedOrdersAdapter storeReceivedOrdersAdapter;
    ReceivedOrdersAdapter receivedOrdersAdapter;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String user_id, messageStr = "";
    TextView available_text, text_message;
    ArrayList<OrderModel> orderModels;
    Button my_orders_btn, received_orders_btn, cancel_btn, submit_btn;
    AlertDialog alertDialog;
    String sellernameStr, sellernoStr, sellerAddress, sellerCountry, sellerZipcode, actionStr = "";
    EditText seller_name, seller_number, product_address, product_country, product_zipcode;
    Dialog customdialog;
    Helper helper;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_received_orders);
        preferenceUtils = new PreferenceUtils(StoreReceivedOrdersActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        helper = new Helper(this);
        messageStr = getIntent().getStringExtra("Message");
        text_message = (TextView) findViewById(R.id.text_message);
        if (messageStr.equalsIgnoreCase("") || messageStr.isEmpty() || messageStr.equalsIgnoreCase("null")) {
            text_message.setVisibility(View.GONE);
            myOrdersList();
        } else {
            text_message.setVisibility(View.VISIBLE);
            text_message.setText(messageStr);
            myOrdersList();
        }
        my_orders_btn = (Button) findViewById(R.id.my_orders_btn);
        received_orders_btn = (Button) findViewById(R.id.received_orders_btn);
        recycler_view_received_orders = (RecyclerView) findViewById(R.id.recycler_view_received_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_received_orders.setLayoutManager(layoutManager);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        available_text = (TextView) findViewById(R.id.available_text);
        my_orders_btn.setTextColor(getResources().getColor(R.color.white));
        received_orders_btn.setTextColor(getResources().getColor(R.color.black));
        my_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
        actionStr = "myOrders";
        my_orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStr = "myOrders";
                text_message.setVisibility(View.GONE);
                my_orders_btn.setTextColor(getResources().getColor(R.color.white));
                received_orders_btn.setTextColor(getResources().getColor(R.color.black));
                my_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                myOrdersList();
            }
        });
        received_orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStr = "receivedOrders";
                text_message.setVisibility(View.GONE);
                received_orders_btn.setTextColor(getResources().getColor(R.color.white));
                my_orders_btn.setTextColor(getResources().getColor(R.color.black));
                received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                my_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                receivedOrderList();
            }
        });
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StoreReceivedOrdersActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StoreReceivedOrdersActivity.this, TradeManagementActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (actionStr.equalsIgnoreCase("myOrders")) {
                    my_orders_btn.setTextColor(getResources().getColor(R.color.white));
                    received_orders_btn.setTextColor(getResources().getColor(R.color.black));
                    my_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    myOrdersList();
                } else if (actionStr.equalsIgnoreCase("receivedOrders")) {
                    received_orders_btn.setTextColor(getResources().getColor(R.color.white));
                    my_orders_btn.setTextColor(getResources().getColor(R.color.black));
                    received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    my_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    receivedOrderList();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StoreReceivedOrdersActivity.this, TradeManagementActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(StoreReceivedOrdersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void myOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        OrdersApi service = retrofit.create(OrdersApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.myOrders(user_id);
        // callRetrofit = service.myOrders("453");
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
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
                                        //   String message = root.getString("message");
                                        String success = root.getString("success");
                                        String busdadgesimg = root.getString("busdadgesimg");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("orderitems");
                                            orderModels = new ArrayList<OrderModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String product_order_id = jsonObject.getString("product_order_id");
                                                String product_id = jsonObject.getString("product_id");
                                                String sproduct_id = jsonObject.getString("sproduct_id");
                                                String mproduct_id = jsonObject.getString("mproduct_id");
                                                String sample = jsonObject.getString("sample");
                                                String quantity = jsonObject.getString("quantity");
                                                String price = jsonObject.getString("price");
                                                String totalprice = jsonObject.getString("totalprice");
                                                String shipamount = jsonObject.getString("shipamount");
                                                String discount = jsonObject.getString("discount");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String address = jsonObject.getString("address");
                                                String zipcode = jsonObject.getString("zipcode");
                                                String display_name = jsonObject.getString("display_name");
                                                String bill_fname = jsonObject.getString("bill_fname");
                                                String bill_lname = jsonObject.getString("bill_lname");
                                                String pay_email = jsonObject.getString("pay_email");
                                                String pay_phone = jsonObject.getString("pay_phone");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String service_charge = jsonObject.getString("service_charge");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String order_date = jsonObject.getString("order_date");
                                                String order_status = jsonObject.getString("order_status");
                                                String on_date = jsonObject.getString("on_date");
                                                String pay_method = jsonObject.getString("pay_method");
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
                                                String complete_amount_status = jsonObject.getString("complete_amount_status");
                                                String payment_status = jsonObject.getString("payment_status");
                                                String payu_status = jsonObject.getString("payu_status");
                                                preferenceUtils.saveString(PreferenceUtils.ORDER_ID, order_id);
                                                preferenceUtils.saveString(PreferenceUtils.TRANSACTION_ID, bank_thr_ran_id);
                                                OrderModel orderModel = new OrderModel();
                                                orderModel.setId(id);
                                                orderModel.setUser_id(user_id);
                                                orderModel.setProduct_order_id(product_order_id);
                                                orderModel.setProduct_id(product_id);
                                                orderModel.setSample(sample);
                                                orderModel.setQuantity(quantity);
                                                orderModel.setPrice(price);
                                                orderModel.setTotalprice(totalprice);
                                                orderModel.setDiscount(discount);
                                                orderModel.setInvoice_no(invoice_no);
                                                orderModel.setDisplay_name(display_name);
                                                orderModel.setPr_title(pr_title);
                                                orderModel.setService_charge(service_charge);
                                                orderModel.setPr_sku(pr_sku);
                                                orderModel.setPr_userid(pr_userid);
                                                orderModel.setOrder_date(order_date);
                                                orderModel.setOrder_status(order_status);
                                                orderModel.setOn_date(on_date);
                                                orderModel.setShipamount(shipamount);
                                                orderModel.setPay_method(pay_method);
                                                orderModel.setBank_thr_ran_id(bank_thr_ran_id);
                                                orderModel.setComplete_amount_status(complete_amount_status);
                                                orderModel.setPayment_status(payment_status);
                                                orderModel.setZipcode(zipcode);
                                                orderModel.setPayu_status(payu_status);
                                                orderModel.setAddress(address);
                                                orderModels.add(orderModel);

                                            }
                                            if (orderModels != null) {
                                                storeReceivedOrdersAdapter = new StoreReceivedOrdersAdapter(StoreReceivedOrdersActivity.this, orderModels);
                                                recycler_view_received_orders.setAdapter(storeReceivedOrdersAdapter);
                                                storeReceivedOrdersAdapter.notifyDataSetChanged();
                                                available_text.setVisibility(View.GONE);
                                            } else {
                                                recycler_view_received_orders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                                available_text.setText("Orders Are Not Available");
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StoreReceivedOrdersActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void receivedOrderList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        OrdersApi service = retrofit.create(OrdersApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.receivedOrders(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
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
                                        //   String message = root.getString("message");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("items");
                                            orderModels = new ArrayList<OrderModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String product_order_id = jsonObject.getString("product_order_id");
                                                String product_id = jsonObject.getString("product_id");
                                                String sproduct_id = jsonObject.getString("sproduct_id");
                                                String mproduct_id = jsonObject.getString("mproduct_id");
                                                String sample = jsonObject.getString("sample");
                                                String quantity = jsonObject.getString("quantity");
                                                String price = jsonObject.getString("price");
                                                String totalprice = jsonObject.getString("totalprice");
                                                String discount = jsonObject.getString("discount");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String display_name = jsonObject.getString("display_name");
                                                String bill_fname = jsonObject.getString("bill_fname");
                                                String bill_lname = jsonObject.getString("bill_lname");
                                                String pay_email = jsonObject.getString("pay_email");
                                                String pay_phone = jsonObject.getString("pay_phone");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String service_charge = jsonObject.getString("service_charge");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String order_date = jsonObject.getString("order_date");
                                                String order_status = jsonObject.getString("order_status");
                                                String on_date = jsonObject.getString("on_date");
                                                String pay_method = jsonObject.getString("pay_method");
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
                                                String complete_amount_status = jsonObject.getString("complete_amount_status");
                                                String job_location = jsonObject.getString("job_location");
                                                String country = jsonObject.getString("country");
                                                String zipcode = jsonObject.getString("zipcode");
                                                String fullname = jsonObject.getString("fullname");
                                                String phone = jsonObject.getString("phone");
                                                String payu_status = jsonObject.getString("payu_status");

                                                preferenceUtils.saveString(PreferenceUtils.ORDER_ID, order_id);
                                                preferenceUtils.saveString(PreferenceUtils.TRANSACTION_ID, bank_thr_ran_id);
                                                OrderModel orderModel = new OrderModel();
                                                orderModel.setId(id);
                                                orderModel.setUser_id(user_id);
                                                orderModel.setProduct_order_id(product_order_id);
                                                orderModel.setProduct_id(product_id);
                                                orderModel.setSample(sample);
                                                orderModel.setQuantity(quantity);
                                                orderModel.setPrice(price);
                                                orderModel.setTotalprice(totalprice);
                                                orderModel.setDiscount(discount);
                                                orderModel.setInvoice_no(invoice_no);
                                                orderModel.setDisplay_name(display_name);
                                                orderModel.setPr_title(pr_title);
                                                orderModel.setService_charge(service_charge);
                                                orderModel.setPr_sku(pr_sku);
                                                orderModel.setPr_userid(pr_userid);
                                                orderModel.setOrder_date(order_date);
                                                orderModel.setOrder_status(order_status);
                                                orderModel.setOn_date(on_date);
                                                orderModel.setPay_method(pay_method);
                                                orderModel.setBank_thr_ran_id(bank_thr_ran_id);
                                                orderModel.setJob_location(job_location);
                                                orderModel.setCountry(country);
                                                orderModel.setZipcode(zipcode);
                                                orderModel.setFullname(fullname);
                                                orderModel.setPhone(phone);
                                                orderModel.setPayu_status(payu_status);
                                                orderModels.add(orderModel);
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                        if (orderModels != null) {
                            receivedOrdersAdapter = new ReceivedOrdersAdapter(StoreReceivedOrdersActivity.this, orderModels);
                            recycler_view_received_orders.setAdapter(receivedOrdersAdapter);
                            receivedOrdersAdapter.notifyDataSetChanged();
                            available_text.setVisibility(View.GONE);
                        } else {
                            recycler_view_received_orders.setVisibility(View.GONE);
                            available_text.setVisibility(View.VISIBLE);
                            available_text.setText("Received Orders Are Not Available");
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
                Toast.makeText(StoreReceivedOrdersActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}