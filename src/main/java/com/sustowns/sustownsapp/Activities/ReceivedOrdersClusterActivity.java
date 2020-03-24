package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.ClusterPurchasesAdapter;
import com.sustowns.sustownsapp.Adapters.ClusterSalesAdapter;
import com.sustowns.sustownsapp.Adapters.ReceivedOrdersAdapter;
import com.sustowns.sustownsapp.Adapters.StoreReceivedOrdersAdapter;
import com.sustowns.sustownsapp.Api.ClusterApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.OrdersApi;
import com.sustowns.sustownsapp.Models.OrderModel;
import com.sustowns.sustownsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceivedOrdersClusterActivity extends AppCompatActivity {
    ImageView backarrow;
    RecyclerView recycler_view_products_store;
    ClusterSalesAdapter clusterSalesAdapter;
    ClusterPurchasesAdapter clusterPurchasesAdapter;
    ReceivedOrdersAdapter receivedOrdersAdapter;
    String[] orderName = {"Order 1","Order 2"};
    String[] orderNo = {"10032112","13102211"};
    String[] orderPrice = {"4000","5000"};
    String[] orderStatus = {"Completed","Received"};
    String[] orderDate = {"12-3-2020","10-3-2020"};
    StoreReceivedOrdersAdapter storeReceivedOrdersAdapter;
    TextView not_available_text;
    Button cluster_purchased_orders,cluster_received_orders;
    ProgressDialog progressDialog;
    String actionStr = "",user_id;
    ArrayList<OrderModel> orderModels;
    PreferenceUtils preferenceUtils;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_received_orders_cluster);
        preferenceUtils = new PreferenceUtils(ReceivedOrdersClusterActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        not_available_text = (TextView) findViewById(R.id.not_available_text);
        cluster_purchased_orders = (Button) findViewById(R.id.cluster_purchased_orders);
        cluster_received_orders = (Button) findViewById(R.id.cluster_received_orders);
        //not_available_text.setVisibility(View.VISIBLE);
        recycler_view_products_store = (RecyclerView) findViewById(R.id.recycler_clusterorders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_products_store.setLayoutManager(layoutManager);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        cluster_received_orders.setTextColor(getResources().getColor(R.color.white));
        cluster_purchased_orders.setTextColor(getResources().getColor(R.color.black));
        cluster_received_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        cluster_purchased_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
        actionStr = "myOrders";
        cluster_received_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStr = "myOrders";
                cluster_received_orders.setTextColor(getResources().getColor(R.color.white));
                cluster_purchased_orders.setTextColor(getResources().getColor(R.color.black));
                cluster_received_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                cluster_purchased_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                salesVendorsList();
            }
        });
        cluster_purchased_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStr = "receivedOrders";
                cluster_purchased_orders.setTextColor(getResources().getColor(R.color.white));
                cluster_received_orders.setTextColor(getResources().getColor(R.color.black));
                cluster_purchased_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                cluster_received_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                vendorPurchasedList();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (actionStr.equalsIgnoreCase("myOrders")) {
                    cluster_purchased_orders.setTextColor(getResources().getColor(R.color.white));
                    cluster_received_orders.setTextColor(getResources().getColor(R.color.black));
                    cluster_purchased_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    cluster_received_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    vendorPurchasedList();
                } else if (actionStr.equalsIgnoreCase("receivedOrders")) {
                    cluster_received_orders.setTextColor(getResources().getColor(R.color.white));
                    cluster_purchased_orders.setTextColor(getResources().getColor(R.color.black));
                    cluster_received_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    cluster_purchased_orders.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    salesVendorsList();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            });
        salesVendorsList();
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    public void progressdialog() {
        progressDialog = new ProgressDialog(ReceivedOrdersClusterActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void salesVendorsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        ClusterApi service = retrofit.create(ClusterApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.vendorSalesList(user_id);
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
                                            JSONArray jsonArray = root.getJSONArray("receivedlist");
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
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
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
                                                String payu_status = jsonObject.getString("payu_status");
                                                String pay_method = jsonObject.getString("pay_method");
                                                String complete_amount_status = jsonObject.getString("complete_amount_status");
                                                String payment_status = jsonObject.getString("payment_status");
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
                                                receivedOrdersAdapter = new ReceivedOrdersAdapter(ReceivedOrdersClusterActivity.this, orderModels);
                                                recycler_view_products_store.setAdapter(receivedOrdersAdapter);
                                                receivedOrdersAdapter.notifyDataSetChanged();
                                               /* clusterSalesAdapter = new ClusterSalesAdapter(ReceivedOrdersClusterActivity.this,orderModels);
                                                recycler_view_products_store.setAdapter(clusterSalesAdapter);
                                                clusterSalesAdapter.notifyDataSetChanged();*/
                                                not_available_text.setVisibility(View.GONE);
                                            } else {
                                                recycler_view_products_store.setVisibility(View.GONE);
                                                not_available_text.setVisibility(View.VISIBLE);
                                                not_available_text.setText("Orders Are Not Available");
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
                Toast.makeText(ReceivedOrdersClusterActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void vendorPurchasedList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        ClusterApi service = retrofit.create(ClusterApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.vendorPurchasesList(user_id);
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
                                            JSONArray jsonArray = root.getJSONArray("purchasedlist");
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
                                                storeReceivedOrdersAdapter = new StoreReceivedOrdersAdapter(ReceivedOrdersClusterActivity.this, orderModels);
                                                recycler_view_products_store.setAdapter(storeReceivedOrdersAdapter);
                                                storeReceivedOrdersAdapter.notifyDataSetChanged();
                                             /*   clusterPurchasesAdapter = new ClusterPurchasesAdapter(ReceivedOrdersClusterActivity.this,orderModels);
                                                recycler_view_products_store.setAdapter(clusterPurchasesAdapter);
                                                clusterPurchasesAdapter.notifyDataSetChanged();*/
                                                not_available_text.setVisibility(View.GONE);
                                            } else {
                                                recycler_view_products_store.setVisibility(View.GONE);
                                                not_available_text.setVisibility(View.VISIBLE);
                                                not_available_text.setText("Orders Are Not Available");
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
                Toast.makeText(ReceivedOrdersClusterActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
