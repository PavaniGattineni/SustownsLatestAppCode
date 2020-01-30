package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
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

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.StoreReceivedOffersAdapter;
import com.sustowns.sustownsapp.Adapters.StoreSentOffersAdapter;
import com.sustowns.sustownsapp.Api.BidContractsApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.StoreSentOffersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreOffersActivity extends AppCompatActivity {

    ImageView backarrow;
    RecyclerView recycler_received_offers;
    StoreReceivedOffersAdapter storeReceivedOffersAdapter;
    StoreSentOffersAdapter storeSentOffersAdapter;
    Button receivedBtn,sentBtn;
    String[] order = {"Offer 1","Offer2"};
    TextView available_text,status_text;
    ProgressDialog progressDialog;
    String user_id,MessageStr,SentOffersStr,actionStr = "";
    ArrayList<StoreSentOffersModel> storeSentOffersModels;
    PreferenceUtils preferenceUtils;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_offers);
        preferenceUtils = new PreferenceUtils(StoreOffersActivity.this);
        MessageStr = getIntent().getStringExtra("Message");
        SentOffersStr = getIntent().getStringExtra("SentOffers");
        status_text = (TextView) findViewById(R.id.status_text);
        //status_text.setText(MessageStr);
        recycler_received_offers = (RecyclerView) findViewById(R.id.recycler_received_offers);
      //  recycler_sent_offers = (RecyclerView) findViewById(R.id.recycler_sent_offers);
        receivedBtn = (Button) findViewById(R.id.received_offers_btn);
        sentBtn = (Button) findViewById(R.id.sent_offers_btn);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        available_text = (TextView) findViewById(R.id.available_text);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_received_offers.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
      //  recycler_sent_offers.setLayoutManager(layoutManager1);
        recycler_received_offers.setVisibility(View.VISIBLE);
      //  recycler_sent_offers.setVisibility(View.GONE);
        sentBtn.setTextColor(getResources().getColor(R.color.black));
        receivedBtn.setTextColor(getResources().getColor(R.color.white));
        receivedBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        sentBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
        actionStr = "received";
        receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status_text.setVisibility(View.GONE);
                actionStr = "received";
                // recycler_received_offers.setVisibility(View.VISIBLE);
               // recycler_sent_offers.setVisibility(View.GONE);
                sentBtn.setTextColor(getResources().getColor(R.color.black));
                receivedBtn.setTextColor(getResources().getColor(R.color.white));
                receivedBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                sentBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                receivedOffersList();
             /*   storeReceivedOffersAdapter = new StoreReceivedOffersAdapter(StoreOffersActivity.this,order);
                recycler_received_offers.setAdapter(storeReceivedOffersAdapter);*/
            }
        });
        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status_text.setVisibility(View.GONE);
                actionStr = "sent";
                // recycler_received_offers.setVisibility(View.GONE);
              //  recycler_sent_offers.setVisibility(View.VISIBLE);
                receivedBtn.setTextColor(getResources().getColor(R.color.black));
                sentBtn.setTextColor(getResources().getColor(R.color.white));
                sentBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                receivedBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                sentOffersList();
            }
        });
        if(MessageStr.equalsIgnoreCase("") || MessageStr.isEmpty()||  MessageStr.equalsIgnoreCase("null")){
            status_text.setVisibility(View.GONE);
            receivedOffersList();
        }else{
            status_text.setVisibility(View.VISIBLE);
            status_text.setText(MessageStr);
            receivedBtn.setTextColor(getResources().getColor(R.color.black));
            sentBtn.setTextColor(getResources().getColor(R.color.white));
            sentBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
            receivedBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            sentOffersList();
        }
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(actionStr.equalsIgnoreCase("sent")){
                    receivedBtn.setTextColor(getResources().getColor(R.color.black));
                    sentBtn.setTextColor(getResources().getColor(R.color.white));
                    sentBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    receivedBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    sentOffersList();
                }else if(actionStr.equalsIgnoreCase("received")) {
                    sentBtn.setTextColor(getResources().getColor(R.color.black));
                    receivedBtn.setTextColor(getResources().getColor(R.color.white));
                    receivedBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    sentBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    receivedOffersList();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(StoreOffersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void sentOffersList() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BidContractsApi service = retrofit.create(BidContractsApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getSentOffersList(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if(progressDialog.isShowing())
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
                                            JSONArray jsonArray = root.getJSONArray("makeoffer");
                                            storeSentOffersModels = new ArrayList<>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String prod_id = jsonObject.getString("prod_id");
                                                String makepeice = jsonObject.getString("makepeice");
                                                String makeqty = jsonObject.getString("makeqty");
                                                Integer Price = Integer.parseInt(makepeice)*Integer.parseInt(makeqty);
                                                String TotalPrice = String.valueOf(Price);
                                                String user_id = jsonObject.getString("user_id");
                                                String status = jsonObject.getString("status");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pid = jsonObject.getString("pid");
                                                String fullname = jsonObject.getString("fullname");
                                                preferenceUtils.saveString(PreferenceUtils.OFF_PRO_ID,prod_id);

                                                StoreSentOffersModel storeSentOffersModel = new StoreSentOffersModel();
                                                                                      storeSentOffersModel.setId(id);
                                                storeSentOffersModel.setProd_id(prod_id);
                                                storeSentOffersModel.setMakepeice(TotalPrice);
                                                storeSentOffersModel.setMakeqty(makeqty);
                                                storeSentOffersModel.setUser_id(user_id);
                                                storeSentOffersModel.setPr_title(pr_title);
                                                storeSentOffersModel.setStatus(status);
                                                storeSentOffersModel.setPr_userid(pr_userid);
                                                storeSentOffersModel.setPid(pid);
                                                storeSentOffersModel.setFullname(fullname);
                                                storeSentOffersModels.add(storeSentOffersModel);
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                            }
                                            if(storeSentOffersModels != null){
                                                available_text.setVisibility(View.GONE);
                                                storeSentOffersAdapter = new StoreSentOffersAdapter(StoreOffersActivity.this,storeSentOffersModels);
                                                recycler_received_offers.setAdapter(storeSentOffersAdapter);
                                                storeSentOffersAdapter.notifyDataSetChanged();
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                            }else{
                                                available_text.setVisibility(View.VISIBLE);
                                                recycler_received_offers.setVisibility(View.GONE);
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            if(progressDialog.isShowing())
                                                progressDialog.dismiss();
                                        }
                                        else {
                                            available_text.setText("Sent Offers Are Not Available");
                                            available_text.setVisibility(View.VISIBLE);
                                            recycler_received_offers.setVisibility(View.GONE);
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
                    }
                    else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                catch (Exception e){
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

    public void receivedOffersList() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getReceivedOffersList(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if(progressDialog.isShowing())
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
                                            JSONArray jsonArray = root.getJSONArray("makeoffer");
                                            storeSentOffersModels = new ArrayList<>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String prod_id = jsonObject.getString("prod_id");
                                                String makepeice = jsonObject.getString("makepeice");
                                                String makeqty = jsonObject.getString("makeqty");
                                                String user_id = jsonObject.getString("user_id");
                                                String status = jsonObject.getString("status");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_price = jsonObject.getString("pr_price");
                                                String pid = jsonObject.getString("pid");
                                                String fullname = jsonObject.getString("fullname");
                                                preferenceUtils.saveString(PreferenceUtils.OFF_PRO_ID,prod_id);
                                                preferenceUtils.saveString(PreferenceUtils.MAKE_ID,id);
                                                preferenceUtils.saveString(PreferenceUtils.MAKE_STATUS,status);

                                                StoreSentOffersModel storeSentOffersModel = new StoreSentOffersModel();
                                                storeSentOffersModel.setId(id);
                                                storeSentOffersModel.setProd_id(prod_id);
                                                storeSentOffersModel.setMakepeice(makepeice);
                                                storeSentOffersModel.setMakeqty(makeqty);
                                                storeSentOffersModel.setUser_id(user_id);
                                                storeSentOffersModel.setPr_title(pr_title);
                                                storeSentOffersModel.setPr_price(pr_price);
                                                storeSentOffersModel.setStatus(status);
                                                storeSentOffersModel.setPr_userid(pr_userid);
                                                storeSentOffersModel.setPid(pid);
                                                storeSentOffersModel.setFullname(fullname);
                                                storeSentOffersModels.add(storeSentOffersModel);
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                            }
                                            if(storeSentOffersModels != null){
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                                available_text.setVisibility(View.GONE);
                                                storeReceivedOffersAdapter = new StoreReceivedOffersAdapter(StoreOffersActivity.this,storeSentOffersModels);
                                                recycler_received_offers.setAdapter(storeReceivedOffersAdapter);
                                                storeReceivedOffersAdapter.notifyDataSetChanged();
                                            }else{
                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();
                                                available_text.setVisibility(View.VISIBLE);
                                                recycler_received_offers.setVisibility(View.GONE);
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                        else {
                                            available_text.setVisibility(View.VISIBLE);
                                            recycler_received_offers.setVisibility(View.GONE);
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();                                }

                            }
                        }
                    }
                    else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}
