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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sustowns.sustownsapp.Api.BusinessProfileApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.BusinessCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessYourCategory extends AppCompatActivity {
    BusinessYourCategoryAdapter businessYourCategoryAdapter;
    RecyclerView recyclerview_manage_category;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    ImageView backarrow;
    TextView save;
    String user_id,buss_id;
    private RecyclerView.Adapter mAdapter;
    ArrayList<BusinessCategoryModel> businessCategoryModels;
    public static ArrayList<String> selectable_name = new ArrayList<>();
    public static ArrayList<String> selectable_id = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_category);
        preferenceUtils = new PreferenceUtils(BusinessYourCategory.this);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        save = (TextView) findViewById(R.id.save);
        recyclerview_manage_category = (RecyclerView) findViewById(R.id.recyclerview_manage_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview_manage_category.setLayoutManager(layoutManager);
        recyclerview_manage_category.setHasFixedSize(true);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setVisibility(View.GONE);
        getManageCategory();
    }

            public void progressdialog() {
                progressDialog = new ProgressDialog(BusinessYourCategory.this);
                progressDialog.setMessage("please wait...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            private void getManageCategory() {
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
                                                String myproduct = jsonObject.getString("myproduct");
                                                String storestatic = jsonObject.getString("storestatic");
                                                String orderstore = jsonObject.getString("orderstore");
                                                String mypay = jsonObject.getString("mypay");
                                                String mymoneystore = jsonObject.getString("mymoneystore");
                                                String moneycontract = jsonObject.getString("moneycontract");
                                                String approvejob = jsonObject.getString("approvejob");
                                                String complatejob = jsonObject.getString("complatejob");
                                                String fullname = jsonObject.getString("fullname");
                                                String id = jsonObject.getString("id");
                                                String email = jsonObject.getString("email");
                                                String phone = jsonObject.getString("phone");
                                                String bussinessname = jsonObject.getString("bussinessname");
                                                String location = jsonObject.getString("location");
                                                String service_area = jsonObject.getString("service_area");
                                                String website = jsonObject.getString("website");
                                                String banner_image = jsonObject.getString("banner_image");
                                                String busi_detail = jsonObject.getString("busi_detail");
                                                String city = jsonObject.getString("city");
                                                String country = jsonObject.getString("country");
                                                String bid = jsonObject.getString("bid");
                                                String rating = jsonObject.getString("rating");

                                                preferenceUtils.saveString(PreferenceUtils.BUSINESS_ID, bid);
                                            }
                                            JSONArray jsonArray1 = root.getJSONArray("reviews");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            }
                                            JSONArray jsonArray2 = root.getJSONArray("proreviews");
                                            for (int k = 0; k < jsonArray2.length(); k++) {
                                                JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                            }
                                            JSONArray categorydetails = root.getJSONArray("categorydetails");
                                            businessCategoryModels = new ArrayList<>();
                                            for (int i1 = 0; i1 < categorydetails.length(); i1++) {
                                                JSONObject jsonObject3 = categorydetails.getJSONObject(i1);
                                                String id = jsonObject3.getString("id");
                                                String categoryid = jsonObject3.getString("categoryid");
                                                String title = jsonObject3.getString("title");

                                                preferenceUtils.saveString(PreferenceUtils.Category_ID,categoryid);

                                                BusinessCategoryModel businessCategoryModel = new BusinessCategoryModel();
                                                businessCategoryModel.setId(id);
                                                businessCategoryModel.setCategoryid(categoryid);
                                                businessCategoryModel.setTitle(title);
                                                businessCategoryModels.add(businessCategoryModel);
                                            }
                                            businessYourCategoryAdapter = new BusinessYourCategoryAdapter(BusinessYourCategory.this, businessCategoryModels);
                                            recyclerview_manage_category.setAdapter(businessYourCategoryAdapter);
                                            //businessManageCategoryAdapter.notifyDataSetChanged();

                                            JSONArray imagegallery = root.getJSONArray("imagegallery");
                                            for (int i2 = 0; i2 < imagegallery.length(); i2++) {
                                                JSONObject image = imagegallery.getJSONObject(i2);
                                                String gal_images = image.getString("gal_images");
                                            }
                                            JSONArray businessbadges = root.getJSONArray("businessbadges");
                                            for (int i3 = 0; i3 < businessbadges.length(); i3++) {
                                                JSONObject image = businessbadges.getJSONObject(i3);
                                            }
                                            businessCategoryModels = new ArrayList<>();
                                            JSONArray categoryArray = root.getJSONArray("category");
                                            for (int i4 = 0; i4 < categoryArray.length(); i4++) {
                                                JSONObject categoryObj = categoryArray.getJSONObject(i4);
                                           /*     String id = categoryObj.getString("id");
                                                String title = categoryObj.getString("title");
                                                BusinessCategoryModel businessCategoryModel = new BusinessCategoryModel();
                                                businessCategoryModel.setId(id);
                                                businessCategoryModel.setTitle(title);
                                                businessCategoryModels.add(businessCategoryModel);*/
                                            }
                                        /*    businessManageCategoryAdapter = new BusinessManageCategoryAdapter(BusinessYourCategory.this, businessCategoryModels);
                                            recyclerview_manage_category.setAdapter(businessManageCategoryAdapter);*/
                                        } else if (success.equalsIgnoreCase("0")) {
                                            message = root.getString("message");
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            Toast.makeText(BusinessYourCategory.this, message, Toast.LENGTH_SHORT).show();
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
            public class BusinessYourCategoryAdapter extends RecyclerView.Adapter<BusinessYourCategoryAdapter.ViewHolder> {
                //    public static HashMap<Integer, String> issuehashmap = new HashMap<>();
                ArrayList<BusinessCategoryModel> arrayList;
                Context context;
                PreferenceUtils preferenceUtils;
                private int adapterPosition = -1;

                public BusinessYourCategoryAdapter(Context context, ArrayList<BusinessCategoryModel> arrayListIssues) {
                    this.context = context;
                    this.arrayList = arrayListIssues;
                }

                @NonNull
                @Override
                public BusinessYourCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    View v = inflater.inflate(R.layout.checkbox_manageyour_category, parent, false);
                    return new ViewHolder(v);
                }

                @Override
                public void onBindViewHolder(@NonNull final BusinessYourCategoryAdapter.ViewHolder holder, final int position) {
                    preferenceUtils = new PreferenceUtils(context);
                    holder.category_name.setText(arrayList.get(position).getTitle());
                }

                @Override
                public int getItemCount() {
                    return arrayList.size();
                }

                class ViewHolder extends RecyclerView.ViewHolder {
                    TextView category_name;

                    public ViewHolder(View itemView) {
                        super(itemView);
                        category_name = (TextView) itemView.findViewById(R.id.category_name);
                    }
                }
            }

}
