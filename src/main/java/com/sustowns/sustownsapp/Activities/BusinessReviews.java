package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.BuyOffersAdapter;
import com.sustowns.sustownsapp.Api.BusinessProfileApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.BusinessReviewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessReviews extends AppCompatActivity {

    ImageView backarrow;
    RecyclerView recycler_contract_reviews, recycler_store_reviews;
    BuyOffersAdapter buyOffersAdapter;
    Button contract_reviews_btn, store_reviews_btn;
    String[] order = {"Review 1", "Review 2"};
    ContractReviewsAdapter contractReviewsAdapter;
    StoreReviewsAdapter storeReviewsAdapter;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String user_id,city,country,bid,business_id;
    ArrayList<BusinessReviewsModel> getReviewsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_reviews);

        initializeUI();
        intializeValues();

    }

    private void intializeValues() {
        preferenceUtils = new PreferenceUtils(BusinessReviews.this);

    }

    private void initializeUI() {
        recycler_contract_reviews = (RecyclerView) findViewById(R.id.recycler_contract_reviews);
        recycler_store_reviews = (RecyclerView) findViewById(R.id.recycler_store_reviews);
        contract_reviews_btn = (Button) findViewById(R.id.contract_reviews_btn);
        store_reviews_btn = (Button) findViewById(R.id.store_reviews_btn);
        backarrow = (ImageView) findViewById(R.id.backarrow);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_contract_reviews.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_store_reviews.setLayoutManager(layoutManager1);
        recycler_contract_reviews.setVisibility(View.VISIBLE);
        recycler_store_reviews.setVisibility(View.GONE);
        store_reviews_btn.setTextColor(getResources().getColor(R.color.black));
        contract_reviews_btn.setTextColor(getResources().getColor(R.color.white));
        contract_reviews_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        store_reviews_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));

        contract_reviews_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_contract_reviews.setVisibility(View.VISIBLE);
                recycler_store_reviews.setVisibility(View.GONE);
                store_reviews_btn.setTextColor(getResources().getColor(R.color.black));
                contract_reviews_btn.setTextColor(getResources().getColor(R.color.white));
                contract_reviews_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                store_reviews_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getContractReviews();
            }
        });
        store_reviews_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_contract_reviews.setVisibility(View.GONE);
                recycler_store_reviews.setVisibility(View.VISIBLE);
                contract_reviews_btn.setTextColor(getResources().getColor(R.color.black));
                store_reviews_btn.setTextColor(getResources().getColor(R.color.white));
                store_reviews_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                contract_reviews_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getStoreReviews();
                /*contractReviewsAdapter = new ContractReviewsAdapter(BusinessReviews.this, order);
                recycler_store_reviews.setAdapter(contractReviewsAdapter);*/
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getContractReviews();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(BusinessReviews.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getContractReviews() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BusinessProfileApi service = retrofit.create(BusinessProfileApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBusinessProfile(user_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("Success Call ", ">>>>" + response.body().toString());

                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
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
                                String success = null, message = null;
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    JSONArray jsonArray = root.getJSONArray("busdetails");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        city = jsonObject.getString("city");
                                        country = jsonObject.getString("country");
                                        bid = jsonObject.getString("bid");
                                        String rating = jsonObject.getString("rating");

                                        preferenceUtils.saveString(PreferenceUtils.BUSINESS_ID, bid);
                                    }
                                    JSONArray jsonArray1 = root.getJSONArray("reviews");
                                    getReviewsModels = new ArrayList<>();
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                        String image = jsonObject1.getString("review_image");
                                        String review_rating = jsonObject1.getString("review_rating");
                                        String review_ondate = jsonObject1.getString("review_ondate");
                                        String review_comment = jsonObject1.getString("review_comment");
                                        String review_fullname = jsonObject1.getString("review_fullname");
                                        String review_id = jsonObject1.getString("review_id");

                                        BusinessReviewsModel getReviewsModel = new BusinessReviewsModel();
                                        getReviewsModel.setReview_image(image);
                                        getReviewsModel.setReview_rating(review_rating);
                                        getReviewsModel.setReview_ondate(review_ondate);
                                        getReviewsModel.setReview_comment(review_comment);
                                        getReviewsModel.setReview_fullname(review_fullname);
                                        getReviewsModel.setReview_id(review_id);
                                        getReviewsModels.add(getReviewsModel);

                                    }
                                    if(getReviewsModels != null){
                                        contractReviewsAdapter = new ContractReviewsAdapter(BusinessReviews.this, getReviewsModels);
                                        recycler_contract_reviews.setAdapter(contractReviewsAdapter);
                                        contractReviewsAdapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(BusinessReviews.this, "Not Available", Toast.LENGTH_SHORT).show();
                                    }
                                    JSONArray jsonArray2 = root.getJSONArray("proreviews");
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);

                                    }

                                    JSONArray categorydetails = root.getJSONArray("categorydetails");
                                    for (int i1 = 0; i1 < categorydetails.length(); i1++) {
                                        JSONObject jsonObject3 = categorydetails.getJSONObject(i1);
                                        /*String id = jsonObject3.getString("id");
                                        String categoryid = jsonObject3.getString("categoryid");
                                        String title = jsonObject3.getString("title");

                                        preferenceUtils.saveString(PreferenceUtils.Category_ID,categoryid);

                                        BusinessCategoryModel businessCategoryModel = new BusinessCategoryModel();
                                        businessCategoryModel.setId(id);
                                        businessCategoryModel.setCategoryid(categoryid);
                                        businessCategoryModel.setTitle(title);
                                        businessCategoryModels.add(businessCategoryModel);*/
                                    }
                                    JSONArray imagegallery = root.getJSONArray("imagegallery");
                                    for (int i2 = 0; i2 < imagegallery.length(); i2++) {
                                        JSONObject image = imagegallery.getJSONObject(i2);
                                        String gal_images = image.getString("gal_images");
                                    }
                                    JSONArray businessbadges = root.getJSONArray("businessbadges");
                                    for (int i3 = 0; i3 < businessbadges.length(); i3++) {
                                        JSONObject image = businessbadges.getJSONObject(i3);
                                    }
                                    JSONArray categoryArray = root.getJSONArray("category");
                                    for (int i4 = 0; i4 < businessbadges.length(); i4++) {
                                        JSONObject categoryObj = businessbadges.getJSONObject(i4);
                                    }
                                } else if (success.equalsIgnoreCase("0")) {
                                    message = root.getString("message");
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(BusinessReviews.this, message, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(BusinessCategory.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void getStoreReviews() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BusinessProfileApi service = retrofit.create(BusinessProfileApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBusinessProfile(user_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("Success Call ", ">>>>" + response.body().toString());

                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
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
                                String success = null, message = null;
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    JSONArray jsonArray = root.getJSONArray("busdetails");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        city = jsonObject.getString("city");
                                        country = jsonObject.getString("country");
                                        bid = jsonObject.getString("bid");
                                        String rating = jsonObject.getString("rating");

                                        preferenceUtils.saveString(PreferenceUtils.BUSINESS_ID, bid);
                                    }
                                    JSONArray jsonArray1 = root.getJSONArray("reviews");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    }
                                    JSONArray jsonArray2 = root.getJSONArray("proreviews");
                                    getReviewsModels = new ArrayList<>();
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                        String image = jsonObject2.getString("image");
                                        String review_rating = jsonObject2.getString("review_rating");
                                        String review_ondate = jsonObject2.getString("review_ondate");
                                        String review_comment = jsonObject2.getString("review_comment");
                                        String review_fullname = jsonObject2.getString("review_fullname");
                                        String review_id = jsonObject2.getString("review_id");
                                        String review_country = jsonObject2.getString("review_country");
                                        String review_city = jsonObject2.getString("review_city");

                                        BusinessReviewsModel getReviewsModel = new BusinessReviewsModel();
                                        getReviewsModel.setReview_image(image);
                                        getReviewsModel.setReview_rating(review_rating);
                                        getReviewsModel.setReview_ondate(review_ondate);
                                        getReviewsModel.setReview_comment(review_comment);
                                        getReviewsModel.setReview_fullname(review_fullname);
                                        getReviewsModel.setReview_country(review_country);
                                        getReviewsModel.setReview_city(review_city);
                                        getReviewsModel.setReview_id(review_id);
                                        getReviewsModels.add(getReviewsModel);

                                    }
                                    if(getReviewsModels != null){
                                        storeReviewsAdapter = new StoreReviewsAdapter(BusinessReviews.this, getReviewsModels);
                                        recycler_store_reviews.setAdapter(storeReviewsAdapter);
                                        storeReviewsAdapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(BusinessReviews.this, "Not Available", Toast.LENGTH_SHORT).show();
                                    }
                                    JSONArray categorydetails = root.getJSONArray("categorydetails");
                                    for (int i1 = 0; i1 < categorydetails.length(); i1++) {
                                        JSONObject jsonObject3 = categorydetails.getJSONObject(i1);
                                        /*String id = jsonObject3.getString("id");
                                        String categoryid = jsonObject3.getString("categoryid");
                                        String title = jsonObject3.getString("title");

                                        preferenceUtils.saveString(PreferenceUtils.Category_ID,categoryid);

                                        BusinessCategoryModel businessCategoryModel = new BusinessCategoryModel();
                                        businessCategoryModel.setId(id);
                                        businessCategoryModel.setCategoryid(categoryid);
                                        businessCategoryModel.setTitle(title);
                                        businessCategoryModels.add(businessCategoryModel);*/
                                    }
                                    JSONArray imagegallery = root.getJSONArray("imagegallery");
                                    for (int i2 = 0; i2 < imagegallery.length(); i2++) {
                                        JSONObject image = imagegallery.getJSONObject(i2);
                                        String gal_images = image.getString("gal_images");
                                    }
                                    JSONArray businessbadges = root.getJSONArray("businessbadges");
                                    for (int i3 = 0; i3 < businessbadges.length(); i3++) {
                                        JSONObject image = businessbadges.getJSONObject(i3);
                                    }
                                } else if (success.equalsIgnoreCase("0")) {
                                    message = root.getString("message");
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(BusinessReviews.this, message, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(BusinessCategory.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }





    //    Adapter
    public class ContractReviewsAdapter extends RecyclerView.Adapter<ContractReviewsAdapter.ViewHolder> {
        //    public static HashMap<Integer, String> issuehashmap = new HashMap<>();
        ArrayList<BusinessReviewsModel> getReviewsModels;
        Context context;
        PreferenceUtils preferenceUtils;
        String reviewStr,reviewID;

        public ContractReviewsAdapter(Context context, ArrayList<BusinessReviewsModel> getReviewsModels) {
            this.context = context;
            this.getReviewsModels = getReviewsModels;
        }

        @NonNull
        @Override
        public BusinessReviews.ContractReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.reviews_item, parent, false);
            return new BusinessReviews.ContractReviewsAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final BusinessReviews.ContractReviewsAdapter.ViewHolder holder, final int position) {

            if(getReviewsModels.get(position) != null){
                holder.ratingBar.setRating(Float.parseFloat(getReviewsModels.get(position).getReview_rating()));
                holder.review_text.setText(getReviewsModels.get(position).getReview_comment());
                holder.reviews_date.setText("Date : "+getReviewsModels.get(position).getReview_ondate());
                holder.review_by.setText("Review By : "+getReviewsModels.get(position).getReview_fullname());
                if(getReviewsModels.get(position).getReview_image() != null){
                    Glide.with(context)
                            .load(getReviewsModels.get(position).getReview_image())
                           // .placeholder(R.drawable.no_image_available)
                            .into(holder.review_image);
                }else{
                    Glide.with(context)
                            .load(R.drawable.no_image_available)
                            //.placeholder(R.drawable.no_image_available)
                            .into(holder.review_image);
                }

            }
            holder.reply_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ll_send_review.setVisibility(View.VISIBLE);
                }
            });
            holder.send_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    reviewID = getReviewsModels.get(position).getReview_id();
                    reviewStr = holder.review_et.getText().toString().trim();
                    if(reviewStr.equalsIgnoreCase("")){
                        Toast.makeText(context, "Please Enter Review", Toast.LENGTH_SHORT).show();
                    }else{
                        submitContractReview();
                    }
                }
            });
        }

        public void submitContractReview() {
            user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
            business_id = preferenceUtils.getStringFromPreference(PreferenceUtils.BUSINESS_ID,"");
            progressdialog();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DZ_URL.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserApi service = retrofit.create(UserApi.class);

            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.submitContractReviews(user_id,bid,reviewStr,reviewID);
            callRetrofit.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
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
                                        String message = root.getString("message");
                                        String success = root.getString("success");
                                        if (success.equals("1")) {
                                            Intent i = new Intent(context, BusinessReviews.class);
                                            startActivity(i);
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    } else {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
        }







        @Override
        public int getItemCount() {
            return getReviewsModels.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView review_text,reviews_date,review_by;
            ImageView review_image,reply_image;
            RatingBar ratingBar;
            LinearLayout ll_send_review;
            EditText review_et;
            Button send_reply;

            public ViewHolder(View itemView) {
                super(itemView);
                review_text = (TextView) itemView.findViewById(R.id.review_text);
                review_image = (ImageView) itemView.findViewById(R.id.review_image);
                ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
                reviews_date = (TextView) itemView.findViewById(R.id.reviews_date);
                ll_send_review = (LinearLayout) itemView.findViewById(R.id.ll_send_review);
                review_et = (EditText) itemView.findViewById(R.id.review_et);
                send_reply = (Button) itemView.findViewById(R.id.send_reply);
                review_by = (TextView) itemView.findViewById(R.id.review_by);
                reply_image = (ImageView) itemView.findViewById(R.id.reply_image);


            }
        }
    }


    // 2nd adapter
    public class StoreReviewsAdapter extends RecyclerView.Adapter<StoreReviewsAdapter.ViewHolder> {
        //    public static HashMap<Integer, String> issuehashmap = new HashMap<>();
        ArrayList<BusinessReviewsModel> getReviewsModels;
        Context context;
        PreferenceUtils preferenceUtils;
        String reviewStr,reviewID;

        public StoreReviewsAdapter(Context context, ArrayList<BusinessReviewsModel> getReviewsModels) {
            this.context = context;
            this.getReviewsModels = getReviewsModels;
        }

        @NonNull
        @Override
        public BusinessReviews.StoreReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.reviews_item, parent, false);
            return new BusinessReviews.StoreReviewsAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final BusinessReviews.StoreReviewsAdapter.ViewHolder holder, final int position) {
            holder.reply_image.setVisibility(View.GONE);
            holder.ll_send_review.setVisibility(View.GONE);
            if(getReviewsModels.get(position) != null){
                holder.ratingBar.setRating(Float.parseFloat(getReviewsModels.get(position).getReview_rating()));
                holder.review_text.setText(getReviewsModels.get(position).getReview_comment());
                holder.reviews_date.setText("Date : "+getReviewsModels.get(position).getReview_ondate());
                holder.review_by.setText("Review By : "+getReviewsModels.get(position).getReview_fullname());
                if(getReviewsModels.get(position).getReview_image() != null){
                    Glide.with(context)
                            .load(getReviewsModels.get(position).getReview_image())
                            //.placeholder(R.drawable.no_image_available)
                            .into(holder.review_image);
                }else{
                    Glide.with(context)
                            .load(R.drawable.no_image_available)
                            //.placeholder(R.drawable.no_image_available)
                            .into(holder.review_image);
                }

            }
        }

        @Override
        public int getItemCount() {
            return getReviewsModels.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView review_text,reviews_date,review_by;
            ImageView review_image,reply_image;
            RatingBar ratingBar;
            LinearLayout ll_send_review;
            EditText review_et;
            Button send_reply;

            public ViewHolder(View itemView) {
                super(itemView);
                review_text = (TextView) itemView.findViewById(R.id.review_text);
                review_image = (ImageView) itemView.findViewById(R.id.review_image);
                ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
                reviews_date = (TextView) itemView.findViewById(R.id.reviews_date);
                ll_send_review = (LinearLayout) itemView.findViewById(R.id.ll_send_review);
                review_et = (EditText) itemView.findViewById(R.id.review_et);
                send_reply = (Button) itemView.findViewById(R.id.send_reply);
                review_by = (TextView) itemView.findViewById(R.id.review_by);
                reply_image = (ImageView) itemView.findViewById(R.id.reply_image);


            }
        }
    }

}
