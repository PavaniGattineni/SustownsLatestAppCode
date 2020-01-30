package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Adapters.ReceivedContractsAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.PostContractsApi;
import com.sustowns.sustownsapp.Models.ReceivedContractModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceivedContracts extends AppCompatActivity {
    ImageView received_head_image,backarrow;
    TextView contract_name,post_prod_name,delivery_location,post_validity,posted_on,category,uploaded_documents,description_received_dialog;
    RecyclerView recyclerview_receivedorders;
    TextView uploaded_documents_link,product_name,status_received_contracts;
    ReceivedContractsAdapter receivedOrdersAdapter;
    String[] ProdName = {"Eggs"};
    RatingBar ratingBarSubmit;
    EditText name_review;
    Button submit_review_btn,close_dialog_uploaded;
    String ratingStr,review,status,prod_image;
    CircleImageView uploaded_image;
    ProgressDialog progressDialog;
    ArrayList<ReceivedContractModel> receivedContractModels;
    String id1,user_id1,job_id,description,image_attach1,description_quote,attachImage_quote,attachImage,appattachment,approve_status,contractname,job_name,user_id,contractor_id;
    PreferenceUtils preferenceUtils;
    Intent intent;
    String user_id_quote,appattachment_quote;
    public static final String URL9 = "http://www.appsapk.com/downloading/latest/UC-Browser.apk";
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_received_contracts);
      //  URL2 = "http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk";
        initializeValues();
        initializeUI();
        getReceivedContractsList();
    }
    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(ReceivedContracts.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        job_id = getIntent().getStringExtra("JobId");
    }

    private void initializeUI() {
        backarrow = (ImageView) findViewById(R.id.backarrow);
        uploaded_documents = (TextView) findViewById(R.id.uploaded_documents);
        received_head_image = (ImageView) findViewById(R.id.received_head_image);
        contract_name = (TextView) findViewById(R.id.contract_name);
        status_received_contracts = (TextView) findViewById(R.id.status_received_contracts);
        category = (TextView) findViewById(R.id.category);
        post_prod_name = (TextView) findViewById(R.id.post_prod_name);
        delivery_location = (TextView) findViewById(R.id.delivery_location);
        post_validity = (TextView) findViewById(R.id.post_validity);
        posted_on = (TextView) findViewById(R.id.posted_on);
        recyclerview_receivedorders = (RecyclerView) findViewById(R.id.recyclerview_receivedorders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ReceivedContracts.this,LinearLayoutManager.VERTICAL,false);
        recyclerview_receivedorders.setLayoutManager(layoutManager);
        ratingBarSubmit = (RatingBar) findViewById(R.id.ratingBarSubmit);
        name_review = (EditText) findViewById(R.id.name_review);
        submit_review_btn = (Button) findViewById(R.id.submit_review_btn);

        submit_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingStr = String.valueOf(ratingBarSubmit.getRating());
                // Toast.makeText(ProductDetailsActivity.this, ratingStr, Toast.LENGTH_LONG).show();
                review = name_review.getText().toString().trim();
                if (ratingStr.equalsIgnoreCase("0.0")) {
                    Toast.makeText(ReceivedContracts.this, "please give rating", Toast.LENGTH_SHORT).show();
                } else if (review.equalsIgnoreCase("")) {
                    Toast.makeText(ReceivedContracts.this, "please write a review", Toast.LENGTH_SHORT).show();
                } else {
                  //  addProductReview();
                }
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        uploaded_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(ReceivedContracts.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.uploaded_documents_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                contract_name = (TextView) customdialog.findViewById(R.id.contract_name);
                description_received_dialog = (TextView) customdialog.findViewById(R.id.description_received_dialog);
                product_name = (TextView) customdialog.findViewById(R.id.product_name);
                uploaded_image = (CircleImageView) customdialog.findViewById(R.id.uploaded_image);
                if (attachImage != null && !attachImage.isEmpty()) {
                    Picasso.get()
                            .load(attachImage)
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(uploaded_image);
                } else {
                    Picasso.get()
                            .load(R.drawable.no_image_available)
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(uploaded_image);
                }
                contract_name.setText(contractname);
                product_name.setText(job_name);
                description_received_dialog.setText(description);
                uploaded_documents_link = (TextView) customdialog.findViewById(R.id.uploaded_documents_link);
                uploaded_documents_link.setText(appattachment);
                uploaded_documents_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri= Uri.parse(URL9);
                        DownloadManager downloadManager = (DownloadManager) ReceivedContracts.this.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE);
                        Toast.makeText(ReceivedContracts.this, "File Downloading...", Toast.LENGTH_SHORT).show();
// set title and description
                        request.setTitle("Data Download");
                        request.setDescription("Android Data download using DownloadManager.");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//set the local destination for download file to a path within the application's external files directory
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"downloadfileName");
                        request.setMimeType("*/*");
                        downloadManager.enqueue(request);
                    }
                });

                close_dialog_uploaded = (Button) customdialog.findViewById(R.id.close_dialog_uploaded);
                close_dialog_uploaded.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        /*swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReceivedContractsList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(ReceivedContracts.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getReceivedContractsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostContractsApi service = retrofit.create(PostContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.getReceivedContracts("446", "326");
        callRetrofit = service.getReceivedContracts(user_id, job_id);// user_id , job_id

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
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
                                        success = root.getInt("success");
                                        String busdadgesimg = root.getString("busdadgesimg");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONObject jsonObject = root.getJSONObject("jobdetail");
                                            if(jsonObject.length() > 0) {
                                                String id = jsonObject.getString("id");
                                                job_name = jsonObject.getString("job_name");
                                                String post_contract_type = jsonObject.getString("post_contract_type");
                                                String user_id = jsonObject.getString("user_id");
                                                String category_id = jsonObject.getString("category_id");
                                                String subcat_id = jsonObject.getString("subcat_id");
                                                String subsubcat_id = jsonObject.getString("subsubcat_id");
                                                String job_date = jsonObject.getString("job_date");
                                                String end_date = jsonObject.getString("end_date");
                                                contractname = jsonObject.getString("contractname");
                                                status = jsonObject.getString("status");
                                                contractor_id = jsonObject.getString("contractor_id");
                                                String image = jsonObject.getString("image");
                                                String attachment = jsonObject.getString("attachment");
                                                String job_location = jsonObject.getString("job_location");
                                                String title = jsonObject.getString("title");
                                                prod_image = busdadgesimg + image;
                                                if (prod_image != null && !prod_image.isEmpty()) {
                                                    Picasso.get()
                                                            .load(prod_image)
                                                            .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(received_head_image);
                                                } else {
                                                    Picasso.get()
                                                            .load(R.drawable.no_image_available)
                                                            .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(received_head_image);
                                                }
                                                posted_on.setText(job_date);
                                                contract_name.setText(contractname);
                                                post_prod_name.setText(job_name);
                                                delivery_location.setText(job_location);
                                                post_validity.setText(end_date);
                                                category.setText(title);
                                            }else{

                                            }
                                            try {
                                                JSONObject comattachObj = root.getJSONObject("comattach");
                                                if (comattachObj != null || !comattachObj.equals("")) {
                                                    String comid = comattachObj.getString("id");
                                                    String job_id = comattachObj.getString("job_id");
                                                    user_id1 = comattachObj.getString("user_id");
                                                    description = comattachObj.getString("description");
                                                    String image_attach = comattachObj.getString("image");
                                                    attachImage = busdadgesimg + image_attach;
                                                    appattachment = comattachObj.getString("appattachment");
                                                    approve_status = comattachObj.getString("approve_status");

                                                    uploaded_documents.setVisibility(View.VISIBLE);
                                                    if (prod_image.isEmpty() || prod_image.equalsIgnoreCase(null)) {
                                                        Picasso.get()
                                                                .load(R.drawable.no_image_available)
                                                                .placeholder(R.drawable.no_image_available)
                                                                .error(R.drawable.no_image_available)
                                                                .into(received_head_image);
                                                    } else {
                                                        Picasso.get()
                                                                .load(prod_image)
                                                                .placeholder(R.drawable.no_image_available)
                                                                .error(R.drawable.no_image_available)
                                                                .into(received_head_image);
                                                    }

                                                } else {
                                                    uploaded_documents.setVisibility(View.GONE);
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                           /* JSONArray attachArray = root.getJSONArray("attach");
                                            for (int i1 = 0; i1 < attachArray.length(); i1++) {
                                            JSONObject attachObject = attachArray.getJSONObject(i1);
                                                if (attachObject.length() > 0) {
                                                    id1 = attachObject.getString("id");
                                                    String job_id1 = attachObject.getString("job_id");
                                                    user_id1 = attachObject.getString("user_id");
                                                    description_quote = attachObject.getString("description");
                                                    image_attach1 = attachObject.getString("image");
                                                    attachImage_quote = busdadgesimg + image_attach1;
                                                    appattachment_quote = attachObject.getString("appattachment");
                                                    approve_status = attachObject.getString("approve_status");
                                                }
                                            }*/
                                            JSONArray quoteArray = root.getJSONArray("jobquate");
                                            receivedContractModels = new ArrayList<>();
                                            for (int i = 0; i < quoteArray.length(); i++) {
                                                    JSONObject quoteObj = quoteArray.getJSONObject(i);
                                                    String id2 = quoteObj.getString("id");
                                                    String job_id1 = quoteObj.getString("job_id");
                                                    user_id_quote = quoteObj.getString("user_id");
                                                    String instant_amount = quoteObj.getString("instant_amount");
                                                    String payment = quoteObj.getString("payment");
                                                    String currency = quoteObj.getString("currency");
                                                    //String status = quoteObj.getString("status");
                                                String bidid = quoteObj.getString("bidid");
                                                    description_quote = quoteObj.getString("description");
                                                    image_attach1 = quoteObj.getString("image");
                                                    attachImage_quote = busdadgesimg + image_attach1;
                                                    approve_status = quoteObj.getString("approve_status");
                                                    appattachment_quote = quoteObj.getString("appattachment");
                                                    String bus_name = quoteObj.getString("bus_name");
                                                    String busi_detail = quoteObj.getString("busi_detail");
                                                    String banner_image = quoteObj.getString("banner_image");
                                                    String bid = quoteObj.getString("bid");
                                                    String phone = quoteObj.getString("phone");
                                                    String fullname = quoteObj.getString("fullname");
                                                    String email = quoteObj.getString("email");
                                                    String city = quoteObj.getString("city");
                                                    String country = quoteObj.getString("country");
                                                    String avg = quoteObj.getString("avg");
                                                    String count = quoteObj.getString("count");

                                                    ReceivedContractModel receivedContractModel = new ReceivedContractModel();
                                                    receivedContractModel.setId(id2);
                                                    receivedContractModel.setJob_id(job_id1);
                                                    receivedContractModel.setUser_id(user_id_quote);
                                                    receivedContractModel.setInstant_amount(instant_amount);
                                                    receivedContractModel.setPayment(payment);
                                                    receivedContractModel.setCurrency(currency);
                                                    receivedContractModel.setStatus(status);
                                                    receivedContractModel.setBus_name(bus_name);
                                                    receivedContractModel.setBusi_detail(busi_detail);
                                                    receivedContractModel.setBid(bid);
                                                    receivedContractModel.setBidid(bidid);
                                                    receivedContractModel.setPhone(phone);
                                                    receivedContractModel.setFullname(fullname);
                                                    receivedContractModel.setEmail(email);
                                                    receivedContractModel.setCity(city);
                                                    receivedContractModel.setCountry(country);
                                                    receivedContractModel.setAvg(avg);
                                                    receivedContractModel.setCount(count);
                                                    receivedContractModel.setImage(attachImage_quote);
                                                    receivedContractModel.setDescription(description_quote);
                                                    receivedContractModel.setAppattachment(appattachment_quote);
                                                    receivedContractModels.add(receivedContractModel);
                                                }
                                            if(quoteArray.equals("") || quoteArray.length() == 0) {
                                                status_received_contracts.setText("No Status");
                                            }else {
                                                if (contractor_id.equalsIgnoreCase(user_id_quote)) {
                                                    if (status.equalsIgnoreCase("1")) {
                                                        status_received_contracts.setText("Selected");
                                                    } else if (status.equalsIgnoreCase("2")|| status.equalsIgnoreCase("6") || status.equalsIgnoreCase("7")) {
                                                        status_received_contracts.setText("work in process");
                                                    } else if (status.equalsIgnoreCase("3")) {
                                                        status_received_contracts.setText("Completed");
                                                    } else if (status.equalsIgnoreCase("5")) {
                                                        status_received_contracts.setText("Approved");
                                                    }
                                                }else{
                                                    if (status.equalsIgnoreCase("0")) {
                                                        status_received_contracts.setText("Quoted");
                                                    } else if (status.equalsIgnoreCase("5")){
                                                        status_received_contracts.setText("Approved");
                                                    }
                                                }
                                            }
                                                JSONArray businessbadgesArray = root.getJSONArray("businessbadges");
                                                for (int j = 0; j < businessbadgesArray.length(); j++) {

                                                }
                                        }
                                        if (receivedContractModels != null) {
                                            receivedOrdersAdapter = new ReceivedContractsAdapter(ReceivedContracts.this,receivedContractModels);
                                            recyclerview_receivedorders.setAdapter(receivedOrdersAdapter);
                                            receivedOrdersAdapter.notifyDataSetChanged();
                                        }else{
                                            recyclerview_receivedorders.setVisibility(View.INVISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}