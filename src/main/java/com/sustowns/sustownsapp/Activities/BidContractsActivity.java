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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.ApprovedRequestAdapter;
import com.sustowns.sustownsapp.Adapters.CompletedRequestAdapter;
import com.sustowns.sustownsapp.Adapters.OpenRequestAdapter;
import com.sustowns.sustownsapp.Adapters.QuotedRequestAdapter;
import com.sustowns.sustownsapp.Adapters.ServiceContractAdapter;
import com.sustowns.sustownsapp.Api.BidContractsApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.CompleteRequestModel;
import com.sustowns.sustownsapp.Models.OpenRequestModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BidContractsActivity extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    public static String username,useremail,user_id;
    Button open,quoted,approved,completed,bid_contracts_btn,post_contracts_btn,addProdRequest;
    LinearLayout home,news,store,bidcontracts,poultryprices,ll_requests_contracts,ll_search_contracts,ll_post_contracts,ll_contracts;
    RecyclerView recyclerView;
    LinearLayout ll_myproductcontracts,ll_myservicecontracts;
    OpenRequestAdapter openRequestAdapter;
    QuotedRequestAdapter quotedRequestAdapter;
    ServiceContractAdapter serviceContractAdapter;
    ApprovedRequestAdapter approvedRequestAdapter;
    CompletedRequestAdapter completedRequestAdapter;
    String[] text = {"0 Open Requests"};
    TextView contracts_title,home_text,news_text,store_text,contracts_text,market_text,not_availabletext;
    ImageView backarrow;
    ProgressDialog progressDialog;
    ArrayList<OpenRequestModel> openrequestModels;
    ArrayList<CompleteRequestModel> completeRequestModels;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String clickAction = "",ProcessStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bid_contracts);

        preferenceUtils = new PreferenceUtils(BidContractsActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName,"");
        useremail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        ProcessStr = getIntent().getStringExtra("Processed");
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(BidContractsActivity.this, MainActivity.class);
               startActivity(i);
            }
        });
        not_availabletext = (TextView) findViewById(R.id.not_availabletext);
        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);
        home_text = (TextView) findViewById(R.id.home_footer);
        news_text = (TextView) findViewById(R.id.news_footer);
        store_text = (TextView) findViewById(R.id.store_footer);
        contracts_text = (TextView) findViewById(R.id.contracts_footer);
        market_text = (TextView) findViewById(R.id.marketing_footer);
        contracts_text.setTextColor(getResources().getColor(R.color.appcolor));

        ll_contracts = (LinearLayout) findViewById(R.id.ll_contracts);
        ll_search_contracts = (LinearLayout) findViewById(R.id.ll_search_contracts);
      //  filter_icon = (ImageView) findViewById(R.id.filter_icon);
        contracts_title = (TextView) findViewById(R.id.contracts_title);
        ll_requests_contracts = (LinearLayout) findViewById(R.id.ll_requests_contracts);
        ll_post_contracts = (LinearLayout) findViewById(R.id.ll_post_contracts);
        ll_contracts.setVisibility(View.VISIBLE);
        ll_post_contracts.setVisibility(View.GONE);
        ll_requests_contracts.setVisibility(View.GONE);
      //  filter_icon.setVisibility(View.GONE);
        bid_contracts_btn = (Button) findViewById(R.id.bid_contracts_btn);
        post_contracts_btn = (Button) findViewById(R.id.post_contracts_btn);
        addProdRequest = (Button) findViewById(R.id.add_product_request);
        open = (Button) findViewById(R.id.open_btn);
        quoted = (Button) findViewById(R.id.quoted_btn);
        approved = (Button) findViewById(R.id.approved_btn);
        completed = (Button) findViewById(R.id.completed_btn);
        ll_myproductcontracts = (LinearLayout) findViewById(R.id.ll_myproductcontracts);
        ll_myservicecontracts = (LinearLayout) findViewById(R.id.ll_myservicecontracts);
        ll_requests_contracts.setVisibility(View.VISIBLE);
        contracts_title.setText("Bid Contracts");
        ll_post_contracts.setVisibility(View.GONE);
        bid_contracts_btn.setTextColor(getResources().getColor(R.color.white));
        post_contracts_btn.setTextColor(getResources().getColor(R.color.black));
        bid_contracts_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        post_contracts_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
        ll_myproductcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BidContractsActivity.this, MyProductContractActivity.class);
                startActivity(i);
            }
        });
        ll_myservicecontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BidContractsActivity.this, MyServiceContractActivity.class);
                startActivity(i);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_bid_contracts);
        LinearLayoutManager lManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lManager);
        if(ProcessStr.equalsIgnoreCase("1")){
            clickAction = "Approve";
            approved.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
            quoted.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            open.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            approved.setTextColor(getResources().getColor(R.color.white));
            quoted.setTextColor(getResources().getColor(R.color.black));
            open.setTextColor(getResources().getColor(R.color.black));
            completed.setTextColor(getResources().getColor(R.color.black));
            bidApproveRequests();
        }else if(ProcessStr.equalsIgnoreCase("2")){
            clickAction = "Quote";
            quoted.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
            open.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            approved.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            quoted.setTextColor(getResources().getColor(R.color.white));
            open.setTextColor(getResources().getColor(R.color.black));
            approved.setTextColor(getResources().getColor(R.color.black));
            completed.setTextColor(getResources().getColor(R.color.black));
            bidQuoteRequests();
        }else{
            bidOpenRequests();
        }

        bid_contracts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // filter_icon.setVisibility(View.VISIBLE);
                ll_requests_contracts.setVisibility(View.VISIBLE);
                contracts_title.setText("Bid Contracts");
                ll_post_contracts.setVisibility(View.GONE);
                bid_contracts_btn.setTextColor(getResources().getColor(R.color.white));
                post_contracts_btn.setTextColor(getResources().getColor(R.color.black));
                bid_contracts_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                post_contracts_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        post_contracts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // filter_icon.setVisibility(View.GONE);
                ll_post_contracts.setVisibility(View.VISIBLE);
                contracts_title.setText("Post Contracts");
                ll_requests_contracts.setVisibility(View.GONE);
                bid_contracts_btn.setTextColor(getResources().getColor(R.color.black));
                post_contracts_btn.setTextColor(getResources().getColor(R.color.white));
                post_contracts_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                bid_contracts_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(BidContractsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(BidContractsActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(BidContractsActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(BidContractsActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(BidContractsActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ll_contracts.setVisibility(View.GONE);
                clickAction = "Open";
                open.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                quoted.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                approved.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                open.setTextColor(getResources().getColor(R.color.white));
                quoted.setTextColor(getResources().getColor(R.color.black));
                approved.setTextColor(getResources().getColor(R.color.black));
                completed.setTextColor(getResources().getColor(R.color.black));
                bidOpenRequests();

            }
        });
        quoted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction = "Quote";
                quoted.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                open.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                approved.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                quoted.setTextColor(getResources().getColor(R.color.white));
                open.setTextColor(getResources().getColor(R.color.black));
                approved.setTextColor(getResources().getColor(R.color.black));
                completed.setTextColor(getResources().getColor(R.color.black));
                bidQuoteRequests();
            }
        });
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction = "Approve";
                approved.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                quoted.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                open.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                approved.setTextColor(getResources().getColor(R.color.white));
                quoted.setTextColor(getResources().getColor(R.color.black));
                open.setTextColor(getResources().getColor(R.color.black));
                completed.setTextColor(getResources().getColor(R.color.black));
                bidApproveRequests();
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction = "Complete";
                completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                quoted.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                approved.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                open.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                completed.setTextColor(getResources().getColor(R.color.white));
                quoted.setTextColor(getResources().getColor(R.color.black));
                approved.setTextColor(getResources().getColor(R.color.black));
                open.setTextColor(getResources().getColor(R.color.black));
                bidCompleteRequests();
            }
        });
        //   hideAndShowItems();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(clickAction.equalsIgnoreCase("Open")){
                    bidOpenRequests();
                }else if(clickAction.equalsIgnoreCase("Quote")){
                    bidQuoteRequests();
                }else if(clickAction.equalsIgnoreCase("Approve")){
                    bidApproveRequests();
                }else if(clickAction.equalsIgnoreCase("Complete")){
                    bidCompleteRequests();
                }else if(clickAction.equalsIgnoreCase("")){
                    bidOpenRequests();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(BidContractsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(BidContractsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void bidOpenRequests() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.bidContOpenList("buy",user_id);
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
                                        String image_path = root.getString("image_path");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("openjob");
                                            openrequestModels = new ArrayList<OpenRequestModel>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String job_name = jsonObject.getString("job_name");
                                                String post_contract_type = jsonObject.getString("post_contract_type");
                                                String user_id = jsonObject.getString("user_id");
                                                String category_id = jsonObject.getString("category_id");
                                                String subcat_id = jsonObject.getString("subcat_id");
                                                String subsubcat_id = jsonObject.getString("subsubcat_id");
                                                String job_date = jsonObject.getString("job_date");
                                                String end_date = jsonObject.getString("end_date");
                                                String etime = jsonObject.getString("etime");
                                                String contractname = jsonObject.getString("contractname");
                                                String image = jsonObject.getString("image");
                                                String minqantity = jsonObject.getString("minqantity");
                                                String qnt_weight = jsonObject.getString("qnt_weight");
                                                String job_location = jsonObject.getString("job_location");
                                                String contractor_id = jsonObject.getString("contractor_id");
                                                String on_date = jsonObject.getString("on_date");
                                                String bid_id = jsonObject.getString("bid_id");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String title = jsonObject.getString("title");
                                                String fullname = jsonObject.getString("fullname");
                                                String my_bid_id = jsonObject.getString("my_bid_id");
                                                String Image = image_path + image;

                                                preferenceUtils.saveString(PreferenceUtils.JOB_ID,id);

                                                OpenRequestModel openRequestModel = new OpenRequestModel();
                                                openRequestModel.setId(id);
                                                openRequestModel.setJob_name(job_name);
                                                openRequestModel.setPost_contract_type(post_contract_type);
                                                openRequestModel.setUser_id(user_id);
                                                openRequestModel.setCategory_id(category_id);
                                                openRequestModel.setSubcat_id(subcat_id);
                                                openRequestModel.setSubsubcat_id(subsubcat_id);
                                                openRequestModel.setEnd_date(end_date);
                                                openRequestModel.setEtime(etime);

                                                openRequestModel.setContractname(contractname);
                                                openRequestModel.setImage(Image);
                                                openRequestModel.setMinqantity(minqantity);
                                                openRequestModel.setQnt_weight(qnt_weight);
                                                openRequestModel.setContractor_id(contractor_id);
                                                openRequestModel.setJob_location(job_location);
                                                openRequestModel.setTitle(title);
                                                openRequestModel.setFullname(fullname);
                                                openRequestModel.setMy_bid_id(my_bid_id);
                                                openRequestModel.setJob_date(job_date);
                                                openrequestModels.add(openRequestModel);

                                            }
                                            if(openrequestModels != null || openrequestModels.size()>0){
                                                openRequestAdapter = new OpenRequestAdapter(BidContractsActivity.this,openrequestModels);
                                                recyclerView.setAdapter(openRequestAdapter);
                                                openRequestAdapter.notifyDataSetChanged();
                                                progressDialog.dismiss();
                                            }else{
                                                progressDialog.dismiss();
                                                not_availabletext.setVisibility(View.VISIBLE);
                                                recyclerView.setVisibility(View.GONE);
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                        else {
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
    public void bidQuoteRequests() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.bidContQuoteList(user_id,"buy");// user_id = 453
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
                                        String image_path = root.getString("image_path");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("quton");
                                            openrequestModels = new ArrayList<OpenRequestModel>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String job_name = jsonObject.getString("job_name");
                                                String post_contract_type = jsonObject.getString("post_contract_type");
                                                String user_id = jsonObject.getString("user_id");
                                                String jobprovider = jsonObject.getString("jobprovider");
                                                String category_id = jsonObject.getString("category_id");
                                                String subcat_id = jsonObject.getString("subcat_id");
                                                String subsubcat_id = jsonObject.getString("subsubcat_id");
                                                String shedule = jsonObject.getString("shedule");
                                                String job_date = jsonObject.getString("job_date");
                                                String end_date = jsonObject.getString("end_date");
                                                String stime = jsonObject.getString("stime");
                                                String etime = jsonObject.getString("etime");
                                                String detail = jsonObject.getString("detail");
                                                String contractname = jsonObject.getString("contractname");
                                                String image = jsonObject.getString("image");
                                                String minqantity = jsonObject.getString("minqantity");
                                                String qnt_weight = jsonObject.getString("qnt_weight");
                                                String job_location = jsonObject.getString("job_location");
                                                String contractor_id = jsonObject.getString("contractor_id");
                                                String on_date = jsonObject.getString("on_date");
                                                String bid_id = jsonObject.getString("bid_id");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String title = jsonObject.getString("title");
                                                String fullname = jsonObject.getString("fullname");
                                                String delivery_term = jsonObject.getString("delivery_term");
                                                String cargo_pack = jsonObject.getString("cargo_pack");
                                                String cargotype = jsonObject.getString("cargotype");
                                                String Image = image_path + image;

                                                preferenceUtils.saveString(PreferenceUtils.JOB_ID,id);

                                                OpenRequestModel openRequestModel = new OpenRequestModel();
                                                openRequestModel.setId(id);
                                                openRequestModel.setJob_name(job_name);
                                                openRequestModel.setPost_contract_type(post_contract_type);
                                                openRequestModel.setUser_id(user_id);
                                                openRequestModel.setCategory_id(category_id);
                                                openRequestModel.setSubcat_id(subcat_id);
                                                openRequestModel.setSubsubcat_id(subsubcat_id);
                                                openRequestModel.setEnd_date(end_date);
                                                openRequestModel.setEtime(etime);
                                                openRequestModel.setContractname(contractname);
                                                openRequestModel.setImage(Image);
                                                openRequestModel.setMinqantity(minqantity);
                                                openRequestModel.setQnt_weight(qnt_weight);
                                                openRequestModel.setContractor_id(contractor_id);
                                                openRequestModel.setJob_location(job_location);
                                                openRequestModel.setTitle(title);
                                                openRequestModel.setFullname(fullname);
                                                openRequestModel.setJob_date(job_date);
                                                openrequestModels.add(openRequestModel);

                                            }
                                            if(openrequestModels != null){
                                                quotedRequestAdapter = new QuotedRequestAdapter(BidContractsActivity.this, openrequestModels);
                                                recyclerView.setAdapter(quotedRequestAdapter);
                                                quotedRequestAdapter.notifyDataSetChanged();
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                        else {
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

    public void bidCompleteRequests() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.bidContractsCompleteList(user_id,"buy");
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
                                        String image_path = root.getString("image_path");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("complete");
                                            completeRequestModels = new ArrayList<>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String instant_amount = jsonObject.getString("instant_amount");
                                                String after_amount = jsonObject.getString("after_amount");
                                                String payment = jsonObject.getString("payment");
                                                String currency = jsonObject.getString("currency");
                                                String id = jsonObject.getString("id");
                                                String job_name = jsonObject.getString("job_name");
                                                String post_contract_type = jsonObject.getString("post_contract_type");
                                                String user_id = jsonObject.getString("user_id");
                                                String category_id = jsonObject.getString("category_id");
                                                String subcat_id = jsonObject.getString("subcat_id");
                                                String subsubcat_id = jsonObject.getString("subsubcat_id");
                                                String job_date = jsonObject.getString("job_date");
                                                String end_date = jsonObject.getString("end_date");
                                                String detail = jsonObject.getString("detail");
                                                String contractname = jsonObject.getString("contractname");
                                                String image = jsonObject.getString("image");
                                                String Image = image_path + image;
                                                String minqantity = jsonObject.getString("minqantity");
                                                String qnt_weight = jsonObject.getString("qnt_weight");
                                                String job_location = jsonObject.getString("job_location");
                                                String city = jsonObject.getString("city");
                                                String country = jsonObject.getString("country");
                                                String contractor_id = jsonObject.getString("contractor_id");
                                                String on_date = jsonObject.getString("on_date");
                                                String bid_id = jsonObject.getString("bid_id");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String title = jsonObject.getString("title");
                                                String fullname = jsonObject.getString("fullname");
                                                String phone = jsonObject.getString("phone");
                                                String email = jsonObject.getString("email");

                                                preferenceUtils.saveString(PreferenceUtils.JOB_ID,id);

                                                CompleteRequestModel completeRequestModel = new CompleteRequestModel();
                                                completeRequestModel.setInstant_amount(instant_amount);
                                                completeRequestModel.setAfter_amount(after_amount);
                                                completeRequestModel.setPayment(payment);
                                                completeRequestModel.setCurrency(currency);
                                                completeRequestModel.setId(id);
                                                completeRequestModel.setJob_name(job_name);
                                                completeRequestModel.setPost_contract_type(post_contract_type);
                                                completeRequestModel.setCategory_id(category_id);
                                                completeRequestModel.setSubcat_id(subcat_id);
                                                completeRequestModel.setSubsubcat_id(subsubcat_id);
                                                completeRequestModel.setJob_date(job_date);
                                                completeRequestModel.setEnd_date(end_date);
                                                completeRequestModel.setContractname(contractname);
                                                completeRequestModel.setImage(Image);
                                                completeRequestModel.setMinqantity(minqantity);
                                                completeRequestModel.setQnt_weight(qnt_weight);
                                                completeRequestModel.setJob_location(job_location);
                                                completeRequestModel.setCity(city);
                                                completeRequestModel.setCountry(country);
                                                completeRequestModel.setBid_id(bid_id);
                                                completeRequestModel.setInvoice_no(invoice_no);
                                                completeRequestModel.setTitle(title);
                                                completeRequestModel.setFullname(fullname);
                                                completeRequestModel.setPhone(phone);
                                                completeRequestModel.setEmail(email);
                                                completeRequestModels.add(completeRequestModel);

                                            }
                                            if(completeRequestModels != null){
                                                completedRequestAdapter = new CompletedRequestAdapter(BidContractsActivity.this,completeRequestModels);
                                                recyclerView.setAdapter(completedRequestAdapter);
                                                completedRequestAdapter.notifyDataSetChanged();
                                            }else {
                                                recyclerView.setVisibility(View.GONE);
                                                 Toast.makeText(BidContractsActivity.this, "No Requests", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }

                                        else {
                                            recyclerView.setVisibility(View.GONE);
                                            Toast.makeText(BidContractsActivity.this, "No Requests", Toast.LENGTH_SHORT).show();
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
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(BidContractsActivity.this, "No Requests", Toast.LENGTH_SHORT).show();
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
    public void bidApproveRequests() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBidContractsApprovedList(user_id,"buy");// user_id = "446"
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
                                        String approve_Requests = root.getString("approve Requests");
                                        String image_doc_path = root.getString("image_doc_path");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("approve");
                                            completeRequestModels = new ArrayList<>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String job_name = jsonObject.getString("job_name");
                                                String post_contract_type = jsonObject.getString("post_contract_type");
                                                String user_id = jsonObject.getString("user_id");
                                                String category_id = jsonObject.getString("category_id");
                                                String subcat_id = jsonObject.getString("subcat_id");
                                                String subsubcat_id = jsonObject.getString("subsubcat_id");
                                                String shedule = jsonObject.getString("shedule");
                                                String date_type = jsonObject.getString("date_type");
                                                String job_date = jsonObject.getString("job_date");
                                                String end_date = jsonObject.getString("end_date");
                                                String detail = jsonObject.getString("detail");
                                                String contractname = jsonObject.getString("contractname");
                                                String spequality = jsonObject.getString("spequality");
                                                String image = jsonObject.getString("image");
                                                String attachment = jsonObject.getString("attachment");
                                                String minqantity = jsonObject.getString("minqantity");
                                                String qnt_weight = jsonObject.getString("qnt_weight");
                                                String job_location = jsonObject.getString("job_location");
                                                String zipcode = jsonObject.getString("zipcode");
                                                String city = jsonObject.getString("city");
                                                String country = jsonObject.getString("country");
                                                String state = jsonObject.getString("state");
                                                String status = jsonObject.getString("status");
                                                String job_type = jsonObject.getString("job_type");
                                                String contractor_id = jsonObject.getString("contractor_id");
                                                String on_date = jsonObject.getString("on_date");
                                                String bid_id = jsonObject.getString("bid_id");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String job_id = jsonObject.getString("job_id");
                                                String after_amount = jsonObject.getString("after_amount");
                                                String payment = jsonObject.getString("payment");
                                                String currency = jsonObject.getString("currency");
                                                String title = jsonObject.getString("title");
                                                String fullname = jsonObject.getString("fullname");
                                                String phone = jsonObject.getString("phone");
                                                String email = jsonObject.getString("email");
                                                String usercity = jsonObject.getString("usercity");
                                                String usercountry = jsonObject.getString("usercountry");
                                                String ImageStr = image_doc_path + image;

                                                preferenceUtils.saveString(PreferenceUtils.JOB_ID,id);

                                                CompleteRequestModel completeRequestModel = new CompleteRequestModel();
                                                completeRequestModel.setAfter_amount(after_amount);
                                                completeRequestModel.setPayment(payment);
                                                completeRequestModel.setCurrency(currency);
                                                completeRequestModel.setId(id);
                                                completeRequestModel.setJob_name(job_name);
                                                completeRequestModel.setPost_contract_type(post_contract_type);
                                                completeRequestModel.setCategory_id(category_id);
                                                completeRequestModel.setSubcat_id(subcat_id);
                                                completeRequestModel.setSubsubcat_id(subsubcat_id);
                                                completeRequestModel.setEnd_date(end_date);
                                                completeRequestModel.setJob_date(job_date);
                                                completeRequestModel.setContractname(contractname);
                                                completeRequestModel.setImage(ImageStr);
                                                completeRequestModel.setMinqantity(minqantity);
                                                completeRequestModel.setJob_location(job_location);
                                                completeRequestModel.setCity(city);
                                                completeRequestModel.setCountry(country);
                                                completeRequestModel.setBid_id(bid_id);
                                                completeRequestModel.setInvoice_no(invoice_no);
                                                completeRequestModel.setTitle(title);
                                                completeRequestModel.setFullname(fullname);
                                                completeRequestModel.setPhone(phone);
                                                completeRequestModel.setEmail(email);
                                                completeRequestModel.setStatus(status);
                                                completeRequestModel.setJob_id(job_id);
                                                completeRequestModel.setQnt_weight(qnt_weight);
                                                completeRequestModels.add(completeRequestModel);
                                            }
                                            if(completeRequestModels != null){
                                                approvedRequestAdapter = new ApprovedRequestAdapter(BidContractsActivity.this,completeRequestModels);
                                                recyclerView.setAdapter(approvedRequestAdapter);
                                                approvedRequestAdapter.notifyDataSetChanged();
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                        else {
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
}
