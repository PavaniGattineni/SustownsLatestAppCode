package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.sustowns.sustownsapp.Adapters.VendorsListAdapter;
import com.sustowns.sustownsapp.Api.ClusterApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.VendorsListModel;
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


public class ClusterVendorsListActivity extends AppCompatActivity {
    ImageView backarrow;
    RecyclerView recyclerview_vendors_list;
    ProgressDialog progressDialog;
    String user_id;
    Button add_vendor_btn;
    TextView not_available_text;
    PreferenceUtils preferenceUtils;
    ArrayList<VendorsListModel> vendorsListModels;
    VendorsListAdapter vendorsListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cluster_vendors_list);
        preferenceUtils = new PreferenceUtils(ClusterVendorsListActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        try {
            not_available_text = (TextView) findViewById(R.id.not_available_text);
            add_vendor_btn = (Button) findViewById(R.id.add_vendor_btn); 
            recyclerview_vendors_list = (RecyclerView) findViewById(R.id.recyclerview_vendors_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerview_vendors_list.setLayoutManager(layoutManager);
            backarrow = (ImageView) findViewById(R.id.backarrow_vendors);
            backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            add_vendor_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ClusterVendorsListActivity.this, SignUpVendorActivity.class);
                    i.putExtra("Cluster","2");
                    startActivity(i);
                }
            });
            clusterVendorsList();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(ClusterVendorsListActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void clusterVendorsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        ClusterApi service = retrofit.create(ClusterApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.clusterVendorsList(user_id);
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
                                            JSONArray vendorlistArray = root.getJSONArray("vendorlist");
                                            vendorsListModels = new ArrayList<VendorsListModel>();
                                            for (int i = 0; i < vendorlistArray.length(); i++) {
                                                JSONObject jsonObject = vendorlistArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String access_token = jsonObject.getString("access_token");
                                                String vendorsubcat = jsonObject.getString("vendorsubcat");
                                                String unique_id = jsonObject.getString("unique_id");
                                                String username = jsonObject.getString("username");
                                                String fullname = jsonObject.getString("fullname");
                                                String email = jsonObject.getString("email");
                                                String phone = jsonObject.getString("phone");
                                                String country = jsonObject.getString("country");
                                                String franchise_id = jsonObject.getString("franchise_id");
                                                String on_date = jsonObject.getString("on_date");
                                               
                                                VendorsListModel vendorsListModel = new VendorsListModel();
                                                vendorsListModel.setId(id);
                                                vendorsListModel.setAccess_token(access_token);
                                                vendorsListModel.setVendorsubcat(vendorsubcat);
                                                vendorsListModel.setUsername(fullname);
                                                vendorsListModel.setEmail(email);
                                                vendorsListModel.setPhone(phone);
                                                vendorsListModel.setCountry(country);
                                                vendorsListModel.setOn_date(on_date);
                                                vendorsListModels.add(vendorsListModel);
                                            }
                                            if (vendorsListModels != null) {
                                                vendorsListAdapter = new VendorsListAdapter(ClusterVendorsListActivity.this, vendorsListModels);
                                                recyclerview_vendors_list.setAdapter(vendorsListAdapter);
                                                vendorsListAdapter.notifyDataSetChanged();
                                                not_available_text.setVisibility(View.GONE);
                                            }else{
                                                recyclerview_vendors_list.setVisibility(View.GONE);
                                                not_available_text.setVisibility(View.VISIBLE);
                                                not_available_text.setText("Vendors Not Available");
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
                Toast.makeText(ClusterVendorsListActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
