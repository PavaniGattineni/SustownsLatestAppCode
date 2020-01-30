package com.sustowns.sustownsapp.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.BidContractsActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.ProductDocumentsActivity;
import com.sustowns.sustownsapp.Api.BidContractsApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.CompleteRequestModel;
import com.sustowns.sustownsapp.helpers.Helper;
import com.sustowns.sustownsapp.listeners.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApprovedRequestAdapter extends RecyclerView.Adapter<ApprovedRequestAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,Status,appattachment1;
    PreferenceUtils preferenceUtils;
    ArrayList<CompleteRequestModel> completeRequestModels;
    ProgressDialog progressDialog;
    TextView title,payment_type_quote,price_quote,description_quote,quote_document;
    Button close_dialog,add_document;
    LinearLayout layout_complete_myquote,layout_complete_documents;
    Helper helper;
    TextView complete_description, complete_image, complete_document, close_dialog1;
    private PaymentParams mPaymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private PayUChecksum checksum;
    String transactionId, merchantKey, surl, furl, service_provider, hashKeyValue, userCredentials, salt,job_id;
    ImageView complete_quote_img,complete_quote_img1;
    private static String dirPath;
    final String URL2,URL9;
    public ApprovedRequestAdapter(Context context, ArrayList<CompleteRequestModel> completeRequestModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.completeRequestModels = completeRequestModels;
        helper = new Helper(context);
        preferenceUtils = new PreferenceUtils(context);
        dirPath = Utils.getRootDirPath(context);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        URL2 = "http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk";
        URL9 = "http://www.appsapk.com/downloading/latest/UC-Browser.apk";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.approved_request_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(completeRequestModels.get(position) != null){
            viewHolder.title.setText(completeRequestModels.get(position).getContractname());
            viewHolder.bid_prod_name.setText(completeRequestModels.get(position).getJob_name());
            viewHolder.bid_quantity.setText(completeRequestModels.get(position).getJob_date());
            viewHolder.bid_quote.setText(completeRequestModels.get(position).getEnd_date());
            viewHolder.buyer_name.setText(completeRequestModels.get(position).getFullname());
            viewHolder.bid_location.setText(completeRequestModels.get(position).getJob_location());
            viewHolder.cust_det_fullname.setText(completeRequestModels.get(position).getFullname());
            viewHolder.cust_det_emailid.setText(completeRequestModels.get(position).getEmail());
            viewHolder.cust_det_phno.setText(completeRequestModels.get(position).getPhone());
            viewHolder.required_quantity_approve.setText(completeRequestModels.get(position).getMinqantity()+" "+completeRequestModels.get(position).getQnt_weight());
            viewHolder.cust_det_city.setText(completeRequestModels.get(position).getCity()+" "+completeRequestModels.get(position).getCountry());
           if(completeRequestModels.get(position).getImage().isEmpty() || completeRequestModels.get(position).getImage() == null){
               Picasso.get()
                       .load(R.drawable.no_image_available)
                       .placeholder(R.drawable.no_image_available)
                       .error(R.drawable.no_image_available)
                       .into(viewHolder.bid_image);
           }else {
               Picasso.get()
                       .load(completeRequestModels.get(position).getImage())
                       .placeholder(R.drawable.no_image_available)
                       .error(R.drawable.no_image_available)
                       .into(viewHolder.bid_image);
           }
            Status = completeRequestModels.get(position).getStatus();
            if(Status.equalsIgnoreCase("5")){
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.payment_button.setVisibility(View.VISIBLE);
                viewHolder.process_documents.setVisibility(View.GONE);
                viewHolder.my_process_documents.setVisibility(View.GONE);
                viewHolder.waiting_confirmation.setVisibility(View.GONE);
            }else if(Status.equalsIgnoreCase("6")){
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.payment_button.setVisibility(View.GONE);
                viewHolder.process_documents.setVisibility(View.VISIBLE);
                viewHolder.status_for_process_documents.setText("Please Upload the Process Documents to complete the contract");
                viewHolder.my_process_documents.setVisibility(View.GONE);
                viewHolder.waiting_confirmation.setVisibility(View.GONE);
            }else if(Status.equalsIgnoreCase("7")){
                viewHolder.confirm_contract.setVisibility(View.VISIBLE);
                viewHolder.payment_button.setVisibility(View.GONE);
                viewHolder.process_documents.setVisibility(View.GONE);
                viewHolder.my_process_documents.setVisibility(View.VISIBLE);
                viewHolder.waiting_confirmation.setVisibility(View.GONE);
            }else if(Status.equalsIgnoreCase("8")){
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.waiting_confirmation.setVisibility(View.VISIBLE);
                viewHolder.process_documents.setVisibility(View.GONE);
                viewHolder.my_process_documents.setVisibility(View.VISIBLE);
                viewHolder.payment_button.setVisibility(View.GONE);
            }
           /* Glide.with(context)
                    .load(completeRequestModels.get(position).getImage())
                    .into(viewHolder.bid_image);*/
        }
        viewHolder.myquote_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = completeRequestModels.get(position).getJob_id();
                // create dialog completed my quote
                final Dialog customdialog1 = new Dialog(context);
                customdialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog1.setContentView(R.layout.completed_myquote_dialog);
                customdialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog1.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                layout_complete_myquote = (LinearLayout) customdialog1.findViewById(R.id.layout_complete_myquote);
                layout_complete_documents = (LinearLayout) customdialog1.findViewById(R.id.layout_complete_documents);
                layout_complete_myquote.setVisibility(View.VISIBLE);
                layout_complete_documents.setVisibility(View.GONE);
                title = (TextView) customdialog1.findViewById(R.id.title);
                complete_quote_img = (ImageView) customdialog1.findViewById(R.id.complete_quote_img);
                payment_type_quote = (TextView) customdialog1.findViewById(R.id.payment_type_quote);
                price_quote = (TextView) customdialog1.findViewById(R.id.price_quote);
                description_quote = (TextView) customdialog1.findViewById(R.id.description_quote);
                close_dialog = (Button) customdialog1.findViewById(R.id.close_dialog);
                quote_document = (TextView) customdialog1.findViewById(R.id.quote_document);
                getQuoteDetails();
                quote_document.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri= Uri.parse(URL9);
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE);
                        Toast.makeText(context, "File Downloading...", Toast.LENGTH_SHORT).show();
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
                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog1.dismiss();
                    }
                });
                customdialog1.show();
            }
        });
        viewHolder.process_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDocumentsActivity.class);
                i.putExtra("Title", completeRequestModels.get(position).getJob_name());
                i.putExtra("JobId", completeRequestModels.get(position).getJob_id());
                i.putExtra("BidId",completeRequestModels.get(position).getId());
                i.putExtra("Documents",completeRequestModels.get(position).getAttachment());
                context.startActivity(i);
            }
        });

        viewHolder.confirm_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = completeRequestModels.get(position).getJob_id();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Are you sure you want to Complete the contract..?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
//                                        getHashFromSustownServer();
                                confirmContract();
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

            }
        });
/*
        viewHolder.payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShippingAddressActivity.class);
                context.startActivity(intent);
              */
/*  helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with online payment?",
                    new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
//                                        getHashFromSustownServer();
                }
            }, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });*//*

        }
        });
*/

        viewHolder.my_process_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = completeRequestModels.get(position).getJob_id();
                final Dialog customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.completed_myquote_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                layout_complete_myquote = (LinearLayout) customdialog.findViewById(R.id.layout_complete_myquote);
                layout_complete_documents = (LinearLayout) customdialog.findViewById(R.id.layout_complete_documents);
                layout_complete_myquote.setVisibility(View.GONE);
                layout_complete_documents.setVisibility(View.VISIBLE);
                complete_description = (TextView) customdialog.findViewById(R.id.complete_description);
                complete_image = (TextView) customdialog.findViewById(R.id.complete_image);
                complete_document = (TextView) customdialog.findViewById(R.id.complete_document);
                complete_document.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri= Uri.parse(URL9);
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE);
                        Toast.makeText(context, "File Downloading...", Toast.LENGTH_SHORT).show();
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
                close_dialog1 = (Button) customdialog.findViewById(R.id.close_dialog1);
                add_document = (Button) customdialog.findViewById(R.id.add_document);
                complete_quote_img1 = (ImageView) customdialog.findViewById(R.id.complete_quote_img1);
                getCompleteDocDetails();

              /*  add_document.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDocumentsActivity.class);
                        i.putExtra("Title", completeRequestModels.get(position).getJob_name());
                        i.putExtra("JobId", completeRequestModels.get(position).getId());
                        context.startActivity(i);
                    }
                });*/
                close_dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();

            }
        });
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void getQuoteDetails() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.bidContractCompleteQuoteDetails(user_id, job_id);
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
                                        String image_docpath = root.getString("image_docpath");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONObject myjobdetailsObj = root.getJSONObject("myjobdetails");
                                            String instant_amount = myjobdetailsObj.getString("instant_amount");
                                            String payment = myjobdetailsObj.getString("payment");
                                            String currency = myjobdetailsObj.getString("currency");
                                            String contractname = myjobdetailsObj.getString("contractname");
                                            JSONObject comjobquoteObj = root.getJSONObject("comjobquote");
                                            String id = comjobquoteObj.getString("id");
                                            String job_id = comjobquoteObj.getString("job_id");
                                            String user_id = comjobquoteObj.getString("user_id");
                                            String description = comjobquoteObj.getString("description");
                                            String image = comjobquoteObj.getString("image");
                                            String quoteImage = image_docpath + image;
                                            String appattachment = comjobquoteObj.getString("appattachment");
                                            String approve_status = comjobquoteObj.getString("approve_status");
                                            try {
                                                JSONObject comjobcomObj = root.getJSONObject("comjobcom");
                                                if(comjobcomObj != null) {
                                                    String id1 = comjobcomObj.getString("id");
                                                    String job_id1 = comjobcomObj.getString("job_id");
                                                    String user_id1 = comjobcomObj.getString("user_id");
                                                    String description1 = comjobcomObj.getString("description");
                                                    String image1 = comjobcomObj.getString("image");
                                                    String quoteImage1 = image_docpath + image;
                                                    String appattachment1 = comjobcomObj.getString("appattachment");
                                                    String approve_status1 = comjobcomObj.getString("approve_status");
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            title.setText(contractname);
                                            payment_type_quote.setText(payment);
                                            price_quote.setText(currency + " "+ instant_amount);
                                            description_quote.setText(description);
                                            quote_document.setText(appattachment);
                                            if (quoteImage != null && !quoteImage.isEmpty()) {
                                                Picasso.get()
                                                        .load(quoteImage)
                                                        .placeholder(R.drawable.no_image_available)
                                                        .error(R.drawable.no_image_available)
                                                        .into(complete_quote_img);
                                            } else {
                                                Picasso.get()
                                                        .load(R.drawable.no_image_available)
                                                        .placeholder(R.drawable.no_image_available)
                                                        .error(R.drawable.no_image_available)
                                                        .into(complete_quote_img);
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
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void getCompleteDocDetails() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.bidContractCompleteQuoteDetails(user_id, job_id);
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
                                        String image_docpath = root.getString("image_docpath");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONObject myjobdetailsObj = root.getJSONObject("myjobdetails");
                                            String instant_amount = myjobdetailsObj.getString("instant_amount");
                                            String payment = myjobdetailsObj.getString("payment");
                                            String currency = myjobdetailsObj.getString("currency");
                                            String contractname = myjobdetailsObj.getString("contractname");
                                            JSONObject comjobquoteObj = root.getJSONObject("comjobquote");

                                            JSONObject comjobcomObj = root.getJSONObject("comjobcom");
                                            String id1 = comjobcomObj.getString("id");
                                            String job_id1 = comjobcomObj.getString("job_id");
                                            String user_id1 = comjobcomObj.getString("user_id");
                                            String description1 = comjobcomObj.getString("description");
                                            String image1 = comjobcomObj.getString("image");
                                            String documentImage = image_docpath + image1;
                                            appattachment1 = comjobcomObj.getString("appattachment");
                                            String approve_status1 = comjobcomObj.getString("approve_status");

                                            complete_description.setText(description1);
                                            complete_document.setText(appattachment1);
                                            if (documentImage != null && !documentImage.isEmpty()) {
                                                Picasso.get()
                                                        .load(documentImage)
                                                        .placeholder(R.drawable.no_image_available)
                                                        .error(R.drawable.no_image_available)
                                                        .into(complete_quote_img1);
                                            } else {
                                                Picasso.get()
                                                        .load(R.drawable.no_image_available)
                                                        .placeholder(R.drawable.no_image_available)
                                                        .error(R.drawable.no_image_available)
                                                        .into(complete_quote_img1);
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
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void confirmContract() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BidContractsApi service = retrofit.create(BidContractsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.confirmApproveContract(job_id,user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
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
                                Integer status_cart_remove;
                                String message_cart_remove;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equals("1")) {
                                        Intent i = new Intent(context, BidContractsActivity.class);
                                        i.putExtra("Processed","0");
                                        context.startActivity(i);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
/*
    private void payBankWithOrderID() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        order_id = preferenceUtils.getStringFromPreference(PreferenceUtils.ORDER_ID, "");
        title = preferenceUtils.getStringFromPreference(PreferenceUtils.CartTitle, "");
        id = preferenceUtils.getStringFromPreference(PreferenceUtils.CartId, "");
        quantity = preferenceUtils.getStringFromPreference(PreferenceUtils.CartQuantity, "");
        price = preferenceUtils.getStringFromPreference(PreferenceUtils.CartPrice, "");

        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);
        JsonArray productArray = new JsonArray();
        RealmResults<AddToCartModel> cartList = realm.where(AddToCartModel.class).equalTo("userId", user_id).equalTo("isItemChekOut", false).findAll();
        for (int i = 0; i < cartList.size(); i++) {
            JsonObject object = new JsonObject();
             */
/*   object.addProperty("user_id", "471");
                object.addProperty("order_id", "348");
                object.addProperty("product_id", "308");
                object.addProperty("sproduct_id", "0");
                object.addProperty("quantity", "122");
                object.addProperty("price", "5");*//*

            object.addProperty("user_id", user_id);
            object.addProperty("order_id", order_id);
            object.addProperty("product_id", cartList.get(i).getItemId());
            object.addProperty("sproduct_id", "0");
            object.addProperty("quantity", cartList.get(i).getQuantity());
            object.addProperty("price", cartList.get(i).getPrice());

            productArray.add(object);
        }
        //  }
        //  }
        Log.e("Category Array", "" + productArray);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.payByBankWithOrderId(productArray);

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
                                String success = null, msg = null;
                                String result = root.getString("result");
                                msg = root.getString("msg");
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    if (realm.isInTransaction())
                                        realm.cancelTransaction();
                                    realm.beginTransaction();
                                    realm.deleteAll();
                                    realm.commitTransaction();
                                    Intent i = new Intent(context, StoreReceivedOrdersActivity.class);
                                    context.startActivity(i);
                                  */
/*  Intent i = new Intent(ShippingAddressActivity.this,BusinessProfileActivity.class);
                                    startActivity(i);*//*

                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                } else if (success.equalsIgnoreCase("0")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(BusinessCategory.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }
*/


    @Override
    public int getItemCount() {
        return completeRequestModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bid_image;
        TextView title,bid_prod_name,bid_quantity,bid_quote,buyer_name,bid_close_in,bid_location,cust_det_fullname,cust_det_phno,
                cust_det_emailid,cust_det_city,payment_button,waiting_confirmation,required_quantity_approve,status_for_process_documents;
        Button myquote_completed,process_documents,confirm_contract,my_process_documents;

        public ViewHolder(View view) {
            super(view);
            bid_image = (ImageView) view.findViewById(R.id.bid_image);
            title = (TextView) view.findViewById(R.id.title);
            bid_prod_name = (TextView) view.findViewById(R.id.bid_prod_name);
            bid_quantity = (TextView) view.findViewById(R.id.bid_quantity);
            bid_quote = (TextView) view.findViewById(R.id.bid_quote);
            buyer_name = (TextView) view.findViewById(R.id.buyer_name);
            bid_location = (TextView) view.findViewById(R.id.bid_location);
            myquote_completed = (Button) view.findViewById(R.id.myquote_completed);
            payment_button = (TextView) view.findViewById(R.id.payment_button);
            cust_det_fullname = (TextView) view.findViewById(R.id.cust_det_fullname);
            cust_det_phno = (TextView) view.findViewById(R.id.cust_det_phno);
            cust_det_emailid = (TextView) view.findViewById(R.id.cust_det_emailid);
            cust_det_city = (TextView) view.findViewById(R.id.cust_det_city);
            process_documents = (Button) view.findViewById(R.id.process_documents);
            confirm_contract = (Button) view.findViewById(R.id.confirm_contract);
            my_process_documents = (Button) view.findViewById(R.id.my_process_documents);
            waiting_confirmation = (TextView) view.findViewById(R.id.waiting_confirmation);
            required_quantity_approve = (TextView) view.findViewById(R.id.required_quantity_approve);
            status_for_process_documents = (TextView) view.findViewById(R.id.status_for_process_documents);
        }
    }

}

