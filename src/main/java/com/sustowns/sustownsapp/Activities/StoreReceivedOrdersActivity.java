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
    String user_id,messageStr = "";
    TextView available_text,text_message;
    ArrayList<OrderModel> orderModels;
    Button my_orders_btn, received_orders_btn, cancel_btn, submit_btn;
    AlertDialog alertDialog;
    String sellernameStr, sellernoStr, sellerAddress, sellerCountry, sellerZipcode,actionStr = "";
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
        if(messageStr.equalsIgnoreCase("") || messageStr.isEmpty() ||  messageStr.equalsIgnoreCase("null")){
            text_message.setVisibility(View.GONE);
            myOrdersList();
        }else{
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
                if(actionStr.equalsIgnoreCase("myOrders")){
                    my_orders_btn.setTextColor(getResources().getColor(R.color.white));
                    received_orders_btn.setTextColor(getResources().getColor(R.color.black));
                    my_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    myOrdersList();
                }else if(actionStr.equalsIgnoreCase("receivedOrders")) {
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
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String order_date = jsonObject.getString("order_date");
                                                String order_status = jsonObject.getString("order_status");
                                                String on_date = jsonObject.getString("on_date");
                                                String pay_method = jsonObject.getString("pay_method");
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
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
                                                orderModel.setAddress(address);
                                                orderModels.add(orderModel);

                                            }
                                            if (orderModels != null) {
                                                storeReceivedOrdersAdapter = new StoreReceivedOrdersAdapter(StoreReceivedOrdersActivity.this, orderModels);
                                                recycler_view_received_orders.setAdapter(storeReceivedOrdersAdapter);
                                                storeReceivedOrdersAdapter.notifyDataSetChanged();
                                                available_text.setVisibility(View.GONE);
                                            }else{
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
                        }else{
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
    //    Adapter
    public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, pro_id, user_id, user_role, order_status, order_id, pay_method;
        PreferenceUtils preferenceUtils;
        String[] order;
        ArrayList<OrderModel> orderModels;
        ProgressDialog progressDialog;

        public ReceivedOrdersAdapter(StoreReceivedOrdersActivity context, ArrayList<OrderModel> orderModels) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.orderModels = orderModels;
        }
        @Override
        public ReceivedOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new ReceivedOrdersAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ReceivedOrdersAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.ll_paymentstatus.setVisibility(View.GONE);
            order_status = orderModels.get(position).getOrder_status();
            pay_method = orderModels.get(position).getPay_method();
            if (orderModels.get(position) != null) {
                viewHolder.orderName.setText(orderModels.get(position).getPr_title());
                viewHolder.order_no.setText(orderModels.get(position).getInvoice_no());
                viewHolder.orderDate.setText(orderModels.get(position).getOrder_date());
                viewHolder.order_price.setText(orderModels.get(position).getTotalprice());
                if(orderModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("")||orderModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("null")) {
                    if (order_status.equalsIgnoreCase("0")) {
                        viewHolder.confirm_order_btn.setVisibility(View.VISIBLE);
                        viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
                        viewHolder.ll_status.setVisibility(View.GONE);
                        // viewHolder.orderStatus.setText("Pending");
                    } else if (order_status.equalsIgnoreCase("1")) {
                        viewHolder.confirm_order_btn.setVisibility(View.GONE);
                        viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                        viewHolder.ll_status.setVisibility(View.GONE);
                        viewHolder.deliver_orderbtn.setVisibility(View.VISIBLE);
                       // viewHolder.orderStatus.setText("Completed");
                    } else if (order_status.equalsIgnoreCase("2")) {
                        viewHolder.confirm_order_btn.setVisibility(View.GONE);
                        viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                        viewHolder.ll_status.setVisibility(View.VISIBLE);
                        viewHolder.orderStatus.setText("Cancelled");
                    }else if(order_status.equalsIgnoreCase("4")){
                        viewHolder.orderStatus.setText("Delivered");
                    }else if(order_status.equalsIgnoreCase("5")){
                        viewHolder.orderStatus.setText("Completed");
                    }
                }else{
                    if (order_status.equalsIgnoreCase("0")) {
                        viewHolder.confirm_order_btn.setVisibility(View.GONE);
                        viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                        viewHolder.ll_status.setVisibility(View.VISIBLE);
                         viewHolder.orderStatus.setText("Payment Pending");
                    } else if (order_status.equalsIgnoreCase("1")) {
                        viewHolder.confirm_order_btn.setVisibility(View.GONE);
                        viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                        viewHolder.ll_status.setVisibility(View.GONE);
                        viewHolder.deliver_orderbtn.setVisibility(View.VISIBLE);
                        //viewHolder.orderStatus.setText("Completed");
                    } else if (order_status.equalsIgnoreCase("2")) {
                        viewHolder.confirm_order_btn.setVisibility(View.GONE);
                        viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                        viewHolder.ll_status.setVisibility(View.VISIBLE);
                        viewHolder.orderStatus.setText("Cancelled");
                    } else if (order_status.equalsIgnoreCase("3")) {
                        viewHolder.confirm_order_btn.setVisibility(View.VISIBLE);
                        viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
                        viewHolder.ll_status.setVisibility(View.GONE);
                        // viewHolder.orderStatus.setText("Pending");
                    }else if(order_status.equalsIgnoreCase("4")){
                        viewHolder.orderStatus.setText("Delivered");
                    }else if(order_status.equalsIgnoreCase("5")){
                        viewHolder.orderStatus.setText("Completed");
                    }
                }
            }
/*
            viewHolder.confirm_order_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order_id = orderModels.get(position).getId();
                    helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Are you sure you want to Confirm...?",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                   // sweetAlertDialog.dismissWithAnimation();
                                    customdialog = new Dialog(StoreReceivedOrdersActivity.this);
                                    customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    customdialog.setContentView(R.layout.confirm_order_dialog);
                                    customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                                    cancel_btn = (Button) customdialog.findViewById(R.id.cancel_btn);
                                    submit_btn = (Button) customdialog.findViewById(R.id.submit_btn);
                                    seller_name = (EditText) customdialog.findViewById(R.id.seller_name);
                                    seller_number = (EditText) customdialog.findViewById(R.id.seller_number);
                                    product_address = (EditText) customdialog.findViewById(R.id.product_address);
                                    product_country = (EditText) customdialog.findViewById(R.id.product_country);
                                    product_zipcode = (EditText) customdialog.findViewById(R.id.product_zipcode);

                                    seller_name.setText(orderModels.get(position).getFullname());
                                    seller_number.setText(orderModels.get(position).getPhone());
                                    product_address.setText(orderModels.get(position).getJob_location());
                                    product_country.setText(orderModels.get(position).getCountry());
                                    product_zipcode.setText(orderModels.get(position).getZipcode());

                                    submit_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            order_id = orderModels.get(position).getId();
                                            sellernameStr = seller_name.getText().toString().trim();
                                            sellernoStr = seller_number.getText().toString().trim();
                                            sellerAddress = product_address.getText().toString().trim();
                                            sellerCountry = product_country.getText().toString().trim();
                                            sellerZipcode = product_zipcode.getText().toString().trim();
                                            if (sellernameStr.equalsIgnoreCase("") || sellernoStr.equalsIgnoreCase("") || sellerAddress.equalsIgnoreCase("") ||
                                                    sellerCountry.equalsIgnoreCase("") || sellerZipcode.equalsIgnoreCase("")) {
                                                Toast.makeText(StoreReceivedOrdersActivity.this, "Please Fill Empty Fields", Toast.LENGTH_SHORT).show();
                                            } else {
                                                submitConfirmOrder();
                                            }//customdialog.dismiss();
                                        }
                                    });
                                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // addToCartDatabase();
                                            customdialog.dismiss();
                                        }
                                    });
                                }
                            }, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                }
            });
*/
            viewHolder.confirm_order_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are you sure you want to Confirm...?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    customdialog = new Dialog(StoreReceivedOrdersActivity.this);
                                    customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    customdialog.setContentView(R.layout.confirm_order_dialog);
                                    customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                                    cancel_btn = (Button) customdialog.findViewById(R.id.cancel_btn);
                                    submit_btn = (Button) customdialog.findViewById(R.id.submit_btn);
                                    seller_name = (EditText) customdialog.findViewById(R.id.seller_name);
                                    seller_number = (EditText) customdialog.findViewById(R.id.seller_number);
                                    product_address = (EditText) customdialog.findViewById(R.id.product_address);
                                    product_country = (EditText) customdialog.findViewById(R.id.product_country);
                                    product_zipcode = (EditText) customdialog.findViewById(R.id.product_zipcode);

                                    seller_name.setText(orderModels.get(position).getFullname());
                                    seller_number.setText(orderModels.get(position).getPhone());
                                    product_address.setText(orderModels.get(position).getJob_location());
                                    product_country.setText(orderModels.get(position).getCountry());
                                    product_zipcode.setText(orderModels.get(position).getZipcode());

                                    submit_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            order_id = orderModels.get(position).getId();
                                            sellernameStr = seller_name.getText().toString().trim();
                                            sellernoStr = seller_number.getText().toString().trim();
                                            sellerAddress = product_address.getText().toString().trim();
                                            sellerCountry = product_country.getText().toString().trim();
                                            sellerZipcode = product_zipcode.getText().toString().trim();
                                            if (sellernameStr.equalsIgnoreCase("") || sellernoStr.equalsIgnoreCase("") || sellerAddress.equalsIgnoreCase("") ||
                                                    sellerCountry.equalsIgnoreCase("") || sellerZipcode.equalsIgnoreCase("")) {
                                                Toast.makeText(StoreReceivedOrdersActivity.this, "Please Fill Empty Fields", Toast.LENGTH_SHORT).show();
                                            } else {
                                                submitConfirmOrder();
                                            }

                                            //customdialog.dismiss();
                                        }
                                    });
                                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // addToCartDatabase();
                                            customdialog.dismiss();
                                        }
                                    });
                                    customdialog.show();
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
            viewHolder.cancel_orderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order_id = orderModels.get(position).getId();
                    helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to cancel?",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    cancelOrder();
                                }
                            }, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                }
            });
            viewHolder.deliver_orderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order_id = orderModels.get(position).getId();
                    helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Are you sure you want to deliver...?",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    deliverOrder();
                                }
                            }, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                }
            });

            viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, OrderDetailsActivity.class);
                    i.putExtra("OrderId", orderModels.get(position).getId());
                    context.startActivity(i);
                }
            });
            viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AddPaymentActivity.class);
                    i.putExtra("OrderId",orderModels.get(position).getId());
                    i.putExtra("BankRandId",orderModels.get(position).getBank_thr_ran_id());
                    i.putExtra("RandId",orderModels.get(position).getProduct_order_id());
                    i.putExtra("ContractOrders","1");
                    context.startActivity(i);
                }
            });
        }
        public void removeAt(int position) {
            //  notifyDataSetChanged();
        }
        public void progressdialog() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        private void submitConfirmOrder() {
            // progressdialog();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DZ_URL.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            OrdersApi service = retrofit.create(OrdersApi.class);
            sellernameStr = seller_name.getText().toString().trim();
            sellernoStr = seller_number.getText().toString().trim();
            sellerAddress = product_address.getText().toString().trim();
            sellerCountry = product_country.getText().toString().trim();
            sellerZipcode = product_zipcode.getText().toString().trim();

            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.confirmOrderSubmit(order_id, sellernameStr, sellernoStr, sellerAddress, sellerCountry, sellerZipcode);
            callRetrofit.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        if (response.isSuccessful()) {
                            //  progressDialog.dismiss();
                            Log.d("Success Call", ">>>>" + call);
                            Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                    Log.d("Reg", "Response  >>" + searchResponse.toString());
                                    if (searchResponse != null) {
                                        JSONObject root = null;
                                        try {
                                            root = new JSONObject(searchResponse);
                                            String message;
                                            Integer success;
                                            message = root.getString("message");
                                            success = root.getInt("success");
                                            if (success == 1) {
                                                customdialog.dismiss();
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                receivedOrderList();
                                               /* Intent i = new Intent(context,StoreReceivedOrdersActivity.class);
                                                context.startActivity(i);*/
                                                //  progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                //  progressDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //  progressDialog.dismiss();
                                    }
                                }
                            }
                        } else {
                            // Toast.makeText(SignInActivity.this, "Service not responding", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d("Error Call", ">>>>" + call.toString());
                    Log.d("Error", ">>>>" + t.toString());
                    //    Toast.makeText(MakeOffer.this, "Please login again", Toast.LENGTH_SHORT).show();
                    // progressDialog.dismiss();
                }
            });
        }
        private void cancelOrder() {
            //  progressdialog();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DZ_URL.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            OrdersApi service = retrofit.create(OrdersApi.class);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.cancelOrder(order_id);
            callRetrofit.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        if (response.isSuccessful()) {
                            // progressDialog.dismiss();
                            Log.d("Success Call", ">>>>" + call);
                            Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                    Log.d("Reg", "Response  >>" + searchResponse.toString());
                                    if (searchResponse != null) {
                                        JSONObject root = null;
                                        try {
                                            root = new JSONObject(searchResponse);
                                            String message;
                                            Integer success;
                                            message = root.getString("message");
                                            success = root.getInt("success");
                                            if (success == 1) {
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                receivedOrderList();
                                                // progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                // progressDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //  progressDialog.dismiss();
                                    }
                                }
                            }
                        } else {
                            // Toast.makeText(SignInActivity.this, "Service not responding", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d("Error Call", ">>>>" + call.toString());
                    Log.d("Error", ">>>>" + t.toString());
                    Toast.makeText(StoreReceivedOrdersActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                    //  progressDialog.dismiss();
                }
            });
        }
        public void deliverOrder() {
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("orderid", order_id);
                androidNetworkingDeliverOrder(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void androidNetworkingDeliverOrder(JSONObject jsonObject){
            progressdialog();
            AndroidNetworking.post("https://www.sustowns.com/Storemanagementser/deliver")
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
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    receivedOrderList();
                                    /*Intent i = new Intent(context, StoreReceivedOrdersActivity.class);
                                    i.putExtra("Message","");
                                    startActivity(i);*/
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
        @Override
        public int getItemCount() {
            return orderModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView orderName, orderQuantity, orderDate, orderStatus,order_price,order_no;
            Button add_payment_btn, confirm_order_btn,view_invoice_btn;
            Button cancel_orderbtn,deliver_orderbtn;
            LinearLayout ll_paymentstatus,ll_status;
            public ViewHolder(View view) {
                super(view);
                orderName = (TextView) view.findViewById(R.id.order_name);
                orderQuantity = (TextView) view.findViewById(R.id.order_quantity);
                orderDate = (TextView) view.findViewById(R.id.order_date);
                orderStatus = (TextView) view.findViewById(R.id.order_status);
                add_payment_btn = (Button) view.findViewById(R.id.add_payment_btn);
                confirm_order_btn = (Button) view.findViewById(R.id.confirm_order_btn);
                cancel_orderbtn = (Button) view.findViewById(R.id.cancel_orderbtn);
                view_invoice_btn = (Button) view.findViewById(R.id.view_invoice_btn);
                order_price = (TextView) view.findViewById(R.id.order_price);
                order_no = (TextView) view.findViewById(R.id.order_no);
                ll_paymentstatus = (LinearLayout) view.findViewById(R.id.ll_paymentstatus);
                ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
                deliver_orderbtn = (Button) view.findViewById(R.id.deliver_orderbtn);
            }
        }
    }
}