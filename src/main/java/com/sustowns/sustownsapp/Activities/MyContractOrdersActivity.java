package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.MyContractOrdersAdapter;
import com.sustowns.sustownsapp.Adapters.MyContractPurchasesAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.PostContractsApi;
import com.sustowns.sustownsapp.Models.ContractPurchasesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyContractOrdersActivity extends AppCompatActivity {
    RecyclerView recycler_view_orders;
    MyContractOrdersAdapter myContractOrdersAdapter;
    String[] order = {"Order 1","Order 2","Order 3"};
    ImageView backarrow;
    ProgressDialog progressDialog;
    ArrayList<ContractPurchasesModel> contractOrderModels;
    TextView available_text,text_message;
    PreferenceUtils preferenceUtils;
    String user_id,messageStr = "",purchasedOrders;
    Button received_orders_btn,purchased_orders_btn;
    ArrayList<ContractPurchasesModel> contractPurchasesModels;
    MyContractPurchasesAdapter myContractPurchasesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_contract_orders);
        preferenceUtils = new PreferenceUtils(MyContractOrdersActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        messageStr = getIntent().getStringExtra("Message");
        purchasedOrders = getIntent().getStringExtra("PurchasedOrders");
        text_message = (TextView) findViewById(R.id.text_message);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        recycler_view_orders = (RecyclerView) findViewById(R.id.recycler_view_orders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_view_orders.setLayoutManager(linearLayoutManager);
        available_text = (TextView) findViewById(R.id.available_text);
        received_orders_btn = (Button) findViewById(R.id.received_orders_btn);
        purchased_orders_btn =(Button) findViewById(R.id.purchased_orders_btn);
        received_orders_btn.setTextColor(getResources().getColor(R.color.white));
        purchased_orders_btn.setTextColor(getResources().getColor(R.color.black));
        received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        purchased_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
        received_orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_message.setVisibility(View.GONE);
                received_orders_btn.setTextColor(getResources().getColor(R.color.white));
                purchased_orders_btn.setTextColor(getResources().getColor(R.color.black));
                received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                purchased_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractOrdersList();
            }
        });
        purchased_orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_message.setVisibility(View.GONE);
                purchased_orders_btn.setTextColor(getResources().getColor(R.color.white));
                received_orders_btn.setTextColor(getResources().getColor(R.color.black));
                purchased_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractPurchasesList();
            }
        });
        if(messageStr.equalsIgnoreCase("") || messageStr.isEmpty()||  messageStr.equalsIgnoreCase("null")){
            text_message.setVisibility(View.GONE);
            getContractOrdersList();
        }else{
            text_message.setVisibility(View.VISIBLE);
            text_message.setText(messageStr);
            purchased_orders_btn.setTextColor(getResources().getColor(R.color.white));
            received_orders_btn.setTextColor(getResources().getColor(R.color.black));
            purchased_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
            received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            getContractPurchasesList();
        }
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyContractOrdersActivity.this, TradeManagementActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                received_orders_btn.setTextColor(getResources().getColor(R.color.white));
                purchased_orders_btn.setTextColor(getResources().getColor(R.color.black));
                received_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                purchased_orders_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractOrdersList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyContractOrdersActivity.this, TradeManagementActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public void progressdialog() {
        progressDialog = new ProgressDialog(MyContractOrdersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void getContractOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostContractsApi service = retrofit.create(PostContractsApi.class);

        Call<JsonElement> callRetrofit = null;
       // callRetrofit = service.getContractsOrders("445");// user_id
        callRetrofit = service.getContractsOrders(user_id);// user_id
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
                                        String message = root.getString("message");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray ordersArray = root.getJSONArray("joborderitem");
                                            contractOrderModels = new ArrayList<>();
                                            for (int i = 0; i < ordersArray.length(); i++) {
                                                JSONObject jsonObject = ordersArray.getJSONObject(i);

                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String job_id = jsonObject.getString("job_id");
                                                String pay_type = jsonObject.getString("pay_type");
                                                String quantity = jsonObject.getString("quantity");
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
                                                String totalprice = jsonObject.getString("totalprice");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String order_status = jsonObject.getString("order_status");
                                                String complete_amount_status = jsonObject.getString("complete_amount_status");
                                                String order_date = jsonObject.getString("order_date");
                                                String job_name = jsonObject.getString("contractname");

                                                ContractPurchasesModel contractOrderModel = new ContractPurchasesModel();
                                                contractOrderModel.setId(id);
                                                contractOrderModel.setUser_id(user_id);
                                                contractOrderModel.setOrder_id(order_id);
                                                contractOrderModel.setJob_id(job_id);
                                                contractOrderModel.setPay_type(pay_type);
                                                contractOrderModel.setQuantity(quantity);
                                                contractOrderModel.setBank_thr_ran_id(bank_thr_ran_id);
                                                contractOrderModel.setTotalprice(totalprice);
                                                contractOrderModel.setInvoice_no(invoice_no);
                                                contractOrderModel.setOrder_status(order_status);
                                                contractOrderModel.setComplete_amount_status(complete_amount_status);
                                                contractOrderModel.setOrder_date(order_date);
                                                contractOrderModel.setJob_name(job_name);
                                                contractOrderModels.add(contractOrderModel);
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                            }
                                            if (contractOrderModels.isEmpty()) {
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                                recycler_view_orders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                            } else {
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                                recycler_view_orders.setVisibility(View.VISIBLE);
                                                available_text.setVisibility(View.GONE);
                                                myContractOrdersAdapter = new MyContractOrdersAdapter(MyContractOrdersActivity.this,contractOrderModels);
                                                recycler_view_orders.setAdapter(myContractOrdersAdapter);
                                                myContractOrdersAdapter.notifyDataSetChanged();
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();                                }
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
    public void getContractPurchasesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostContractsApi service = retrofit.create(PostContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.getContractsPurchases("446");// user_id
        callRetrofit = service.getContractsPurchases(user_id);// user_id
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
                                        String message = root.getString("message");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray puchasesArray = root.getJSONArray("joborderitem");
                                            contractPurchasesModels = new ArrayList<>();
                                            for (int i = 0; i < puchasesArray.length(); i++) {
                                                JSONObject jsonObject = puchasesArray.getJSONObject(i);

                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String job_id = jsonObject.getString("job_id");
                                                String pay_type = jsonObject.getString("pay_type");
                                                String quantity = jsonObject.getString("quantity");
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
                                                String totalprice = jsonObject.getString("totalprice");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String order_status = jsonObject.getString("order_status");
                                                String complete_amount_status = jsonObject.getString("complete_amount_status");
                                                String order_date = jsonObject.getString("order_date");
                                                String unique_id = jsonObject.getString("unique_id");
                                                String contractname = jsonObject.getString("contractname");
                                                String job_location = jsonObject.getString("job_location");
                                                String payment_status = jsonObject.getString("payment_status");

                                                ContractPurchasesModel contractPurchasesModel = new ContractPurchasesModel();
                                                contractPurchasesModel.setId(id);
                                                contractPurchasesModel.setUser_id(user_id);
                                                contractPurchasesModel.setOrder_id(order_id);
                                                contractPurchasesModel.setJob_id(job_id);
                                                contractPurchasesModel.setPay_type(pay_type);
                                                contractPurchasesModel.setQuantity(quantity);
                                                contractPurchasesModel.setBank_thr_ran_id(bank_thr_ran_id);
                                                contractPurchasesModel.setTotalprice(totalprice);
                                                contractPurchasesModel.setInvoice_no(invoice_no);
                                                contractPurchasesModel.setOrder_status(order_status);
                                                contractPurchasesModel.setComplete_amount_status(complete_amount_status);
                                                contractPurchasesModel.setOrder_date(order_date);
                                                contractPurchasesModel.setUnique_id(unique_id);
                                                contractPurchasesModel.setJob_name(contractname);
                                                contractPurchasesModel.setJob_location(job_location);
                                                contractPurchasesModel.setPayment_status(payment_status);
                                                contractPurchasesModels.add(contractPurchasesModel);
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                            }
                                            if (contractPurchasesModels.isEmpty()) {
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                                recycler_view_orders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                            } else {
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                                available_text.setVisibility(View.GONE);
                                                recycler_view_orders.setVisibility(View.VISIBLE);
                                                myContractPurchasesAdapter = new MyContractPurchasesAdapter(MyContractOrdersActivity.this, contractPurchasesModels);
                                                recycler_view_orders.setAdapter(myContractPurchasesAdapter);
                                                myContractPurchasesAdapter.notifyDataSetChanged();
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();                                }
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
                Toast.makeText(MyContractOrdersActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
