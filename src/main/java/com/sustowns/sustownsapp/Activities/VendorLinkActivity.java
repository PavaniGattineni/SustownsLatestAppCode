package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.sustowns.sustownsapp.Adapters.GetReviewsAdapter;
import com.sustowns.sustownsapp.Adapters.ProductsAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.GetReviewsModel;
import com.sustowns.sustownsapp.Models.PoultryProductsModel;
import com.sustowns.sustownsapp.Models.VendorCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VendorLinkActivity extends AppCompatActivity {
    ImageView backarrow,profile_img;
    PreferenceUtils preferenceUtils;
    LinearLayout ll_about_eggs,ll_services,ll_our_products,ll_photos,ll_ratings_reviews,ll_vendor_review;
    String user_id,imagepath,ratingStr,review;
    ProgressDialog progressDialog;
    TextView vendor_prod_title,vendor_address;
    EditText name_review;
    RecyclerView about_eggs_recyclerview,vendor_rating_recyclerview,our_products_recyclerview;
    ArrayList<VendorCategoryModel> vendorCategoryModels;
    ArrayList<GetReviewsModel> getReviewsModels;
    ArrayList<PoultryProductsModel> poultryProductsModels;
    VendorCategoryAdapter vendorCategoryAdapter;
    GetReviewsAdapter getReviewsAdapter;
    ProductsAdapter productsAdapter;
    RatingBar ratingBarSubmit;
    Button submit_review_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vendor_link);

        initializeUI();
        initializeValues();

    }

    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(VendorLinkActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");

    }

    private void initializeUI() {
        backarrow = (ImageView) findViewById(R.id.backarrow);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        ll_about_eggs = (LinearLayout) findViewById(R.id.ll_about_eggs);
        ll_services = (LinearLayout) findViewById(R.id.ll_services);
        ll_our_products = (LinearLayout) findViewById(R.id.ll_our_products);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
        ll_ratings_reviews = (LinearLayout) findViewById(R.id.ll_ratings_reviews);
        ll_vendor_review = (LinearLayout) findViewById(R.id.ll_vendor_review);
        vendor_address = (TextView) findViewById(R.id.vendor_address);
        vendor_prod_title = (TextView) findViewById(R.id.vendor_prod_title);
        // about eggs
        about_eggs_recyclerview = (RecyclerView) findViewById(R.id.about_eggs_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(VendorLinkActivity.this,LinearLayoutManager.VERTICAL,false);
        about_eggs_recyclerview.setLayoutManager(layoutManager);
        // rating and review
        vendor_rating_recyclerview = (RecyclerView) findViewById(R.id.vendor_rating_recyclerview);
        vendor_rating_recyclerview.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(VendorLinkActivity.this,LinearLayoutManager.VERTICAL,false);
        vendor_rating_recyclerview.setLayoutManager(layoutManager1);
        // our products
        our_products_recyclerview = (RecyclerView) findViewById(R.id.our_products_recyclerview);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(VendorLinkActivity.this,LinearLayoutManager.VERTICAL,false);
        our_products_recyclerview.setLayoutManager(layoutManager2);

        ratingBarSubmit = (RatingBar) findViewById(R.id.ratingBarSubmit);
        name_review = (EditText) findViewById(R.id.name_review);
        submit_review_btn = (Button) findViewById(R.id.submit_review_btn);
        submit_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingStr = String.valueOf(ratingBarSubmit.getRating());
                // Toast.makeText(ProductDetailsActivity.this, ratingStr, Toast.LENGTH_LONG).show();
                review = name_review.getText().toString().trim();
                if(ratingStr.equalsIgnoreCase("0.0")){
                    Toast.makeText(VendorLinkActivity.this, "please give rating", Toast.LENGTH_SHORT).show();
                }else if(review.equalsIgnoreCase("")){
                    Toast.makeText(VendorLinkActivity.this, "please write a review", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(VendorLinkActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll_about_eggs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorViewCategories();
            }
        });
        ll_ratings_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorRatingReviewList();
            }
        });
        ll_our_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorOurProducts();
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getVendorProfile();
    }


    public void progressdialog() {
        progressDialog = new ProgressDialog(VendorLinkActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getVendorProfile() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getVendorProfile("1");

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
                                String success = null;
                                imagepath = root.getString("imagepath");
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONObject jsonObject = root.getJSONObject("contractor");
                                    String bus_name = jsonObject.getString("bus_name");
                                    String address = jsonObject.getString("address");
                                    String country = jsonObject.getString("country");
                                    String user_image = jsonObject.getString("user_image");
                                    String image = imagepath+user_image;

                                    if(image != null || !image.isEmpty()){
                                        Glide.with(VendorLinkActivity.this)
                                                .load(image)
                                                //.placeholder(R.drawable.profile_ic)
                                                .into(profile_img);
                                    }else{
                                        Glide.with(VendorLinkActivity.this)
                                                .load(R.drawable.profile_ic)
                                                //.placeholder(R.drawable.profile_ic)
                                                .into(profile_img);
                                    }
                                    vendor_prod_title.setText(bus_name);
                                    vendor_address.setText(address+","+country);
                                } else if (success.equalsIgnoreCase("0")) {
                                    String message = root.getString("message");
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(VendorLinkActivity.this, message, Toast.LENGTH_SHORT).show();
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
              //  Toast.makeText(VendorLinkActivity.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }
    private void vendorViewCategories() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.vendorCategoryList("1");

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
                                String success = null;
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    about_eggs_recyclerview.setVisibility(View.VISIBLE);
                                    ll_vendor_review.setVisibility(View.GONE);
                                    vendor_rating_recyclerview.setVisibility(View.GONE);
                                    our_products_recyclerview.setVisibility(View.GONE);
                                    JSONArray jsonArray = root.getJSONArray("categories");
                                    vendorCategoryModels = new ArrayList<>();
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String id = jsonObject.getString("id");
                                        String title = jsonObject.getString("title");
                                        String description = jsonObject.getString("description");
                                        String image = jsonObject.getString("image");
                                       // String image = imagepath+user_image;
                                        VendorCategoryModel vendorCategoryModel = new VendorCategoryModel();
                                        vendorCategoryModel.setId(id);
                                        vendorCategoryModel.setTitle(title);
                                        vendorCategoryModel.setDescription(description);
                                        vendorCategoryModel.setImage(image);
                                        vendorCategoryModels.add(vendorCategoryModel);
                                    }
                                    if(vendorCategoryModels != null){
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        vendorCategoryAdapter = new VendorCategoryAdapter(VendorLinkActivity.this,vendorCategoryModels);
                                        about_eggs_recyclerview.setAdapter(vendorCategoryAdapter);
                                        vendorCategoryAdapter.notifyDataSetChanged();
                                    }

                                } else if (success.equalsIgnoreCase("0")) {
                                    String message = root.getString("message");
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(VendorLinkActivity.this, message, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(VendorLinkActivity.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void vendorRatingReviewList() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.vendorRatingReviews("1");

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
                                String success = null;
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    ll_vendor_review.setVisibility(View.VISIBLE);
                                    vendor_rating_recyclerview.setVisibility(View.VISIBLE);
                                    about_eggs_recyclerview.setVisibility(View.GONE);
                                    our_products_recyclerview.setVisibility(View.GONE);
                                    JSONArray jsonArray = root.getJSONArray("reviews");
                                    getReviewsModels = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String id = jsonObject.getString("id");
                                        String comment = jsonObject.getString("comment");
                                        String rating = jsonObject.getString("rating");
                                        String on_date = jsonObject.getString("on_date");
                                        String fullname = jsonObject.getString("fullname");
                                        // String image = imagepath+user_image;
                                        GetReviewsModel getReviewsModel = new GetReviewsModel();
                                        getReviewsModel.setReview(comment);
                                        getReviewsModel.setRatting(rating);
                                        getReviewsModel.setOndate(on_date);
                                        getReviewsModel.setFullname(fullname);
                                        getReviewsModels.add(getReviewsModel);
                                    }
                                    if(getReviewsModels != null) {
                                        progressDialog.dismiss();
                                        getReviewsAdapter = new GetReviewsAdapter(VendorLinkActivity.this, getReviewsModels);
                                        vendor_rating_recyclerview.setAdapter(getReviewsAdapter);
                                        getReviewsAdapter.notifyDataSetChanged();
                                    }

                                } else  {
                                    String message = root.getString("message");
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(VendorLinkActivity.this, message, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(VendorLinkActivity.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void vendorOurProducts() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.vendorOurProductsList("1");

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
                                String success = null;
                                success = root.getString("success");
                                imagepath = root.getString("imagepath");
                                if (success.equalsIgnoreCase("1")) {
                                    ll_vendor_review.setVisibility(View.GONE);
                                    vendor_rating_recyclerview.setVisibility(View.GONE);
                                    about_eggs_recyclerview.setVisibility(View.GONE);
                                    our_products_recyclerview.setVisibility(View.VISIBLE);
                                    JSONArray jsonArray = root.getJSONArray("allproduct");
                                    vendorCategoryModels = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String id = jsonObject.getString("id");
                                        String pr_title = jsonObject.getString("pr_title");
                                        String pr_image = jsonObject.getString("pr_image");
                                        String pr_descri = jsonObject.getString("pr_descri");
                                        String image = imagepath+pr_image;
                                        VendorCategoryModel vendorCategoryModel = new VendorCategoryModel();
                                        vendorCategoryModel.setId(id);
                                        vendorCategoryModel.setTitle(pr_title);
                                        vendorCategoryModel.setImage(image);
                                        vendorCategoryModel.setDescription(pr_descri);
                                        vendorCategoryModels.add(vendorCategoryModel);
                                    }
                                    if(vendorCategoryModels != null) {
                                        if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                        vendorCategoryAdapter = new VendorCategoryAdapter(VendorLinkActivity.this,vendorCategoryModels);
                                        our_products_recyclerview.setAdapter(vendorCategoryAdapter);
                                        vendorCategoryAdapter.notifyDataSetChanged();
                                    }

                                } else  {
                                    String message = root.getString("message");
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(VendorLinkActivity.this, message, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(VendorLinkActivity.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }


    public class VendorCategoryAdapter extends RecyclerView.Adapter<VendorCategoryAdapter.ViewHolder> {
        //    public static HashMap<Integer, String> issuehashmap = new HashMap<>();
        ArrayList<VendorCategoryModel> vendorCategoryModels;
        Context context;
        PreferenceUtils preferenceUtils;
        private int adapterPosition = -1;

        public VendorCategoryAdapter(Context context, ArrayList<VendorCategoryModel> vendorCategoryModels) {
            this.context = context;
            this.vendorCategoryModels = vendorCategoryModels;
        }

        @NonNull
        @Override
        public VendorCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.vendor_category_iten, parent, false);
            return new VendorCategoryAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final VendorCategoryAdapter.ViewHolder holder, final int position) {
            if(vendorCategoryModels.get(position) != null){
                holder.category_name.setText(vendorCategoryModels.get(position).getTitle());
                holder.category_description.setText(vendorCategoryModels.get(position).getDescription());
            }

        }

        @Override
        public int getItemCount() {
            return vendorCategoryModels.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView category_description,category_name;

            public ViewHolder(View itemView) {
                super(itemView);
                category_description = (TextView) itemView.findViewById(R.id.category_description);
                category_name = (TextView) itemView.findViewById(R.id.category_name);
            }
        }
    }


}
