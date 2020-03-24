package com.sustowns.sustownsapp.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Activities.AddPaymentActivity;
import com.sustowns.sustownsapp.Activities.OrderDetailsActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.StoreReceivedOrdersActivity;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.OrdersApi;
import com.sustowns.sustownsapp.Models.OrderModel;
import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email, pro_id, userid, user_role, order_status, order_id, pay_method,TotalProdPrice;
    PreferenceUtils preferenceUtils;
    String[] order;
    Button cancel_btn, submit_btn;
    Helper helper;
    ArrayList<OrderModel> orderModels;
    ProgressDialog progressDialog;
    float TotalPriceStr,SerChargeFinal;
    AlertDialog alertDialog;
    Dialog customdialog;
    String sellernameStr, sellernoStr, sellerAddress, sellerCountry, sellerZipcode,actionStr = "";
    EditText seller_name, seller_number, product_address, product_country, product_zipcode;
    
    public ReceivedOrdersAdapter(Context context, ArrayList<OrderModel> orderModels) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.orderModels = orderModels;
        preferenceUtils = new PreferenceUtils(context);
        helper = new Helper(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
    }
    @Override
    public ReceivedOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ReceivedOrdersAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ReceivedOrdersAdapter.ViewHolder viewHolder, final int position) {
        order_status = orderModels.get(position).getOrder_status();
        pay_method = orderModels.get(position).getPay_method();
        if (orderModels.get(position) != null) {
            viewHolder.orderName.setText(orderModels.get(position).getPr_title());
            viewHolder.order_no.setText(orderModels.get(position).getInvoice_no());
            viewHolder.orderDate.setText(orderModels.get(position).getOrder_date());
            //viewHolder.order_price.setText(orderModels.get(position).getTotalprice());
            if(userid.equalsIgnoreCase(orderModels.get(position).getPr_userid())) {
                if (orderModels.get(position).getDiscount().equalsIgnoreCase("null") || orderModels.get(position).getDiscount().equalsIgnoreCase("")) {
                    viewHolder.order_price.setText(orderModels.get(position).getTotalprice());
                } else {
                    int OriginalPrice = Integer.parseInt(orderModels.get(position).getPrice());
                    int DiscountStr = Integer.parseInt(orderModels.get(position).getDiscount());
                    int DiscountPrice = OriginalPrice * DiscountStr;
                    float DisPriceStr = Float.parseFloat(String.valueOf(DiscountPrice)) / 100;
                    TotalPriceStr = Float.parseFloat(orderModels.get(position).getPrice()) - DisPriceStr;
                    float quantityInt = Float.parseFloat(orderModels.get(position).getQuantity());
                    float ProdPriceStrFloat = quantityInt * TotalPriceStr;
                    TotalProdPrice = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                    viewHolder.order_price.setText(TotalProdPrice);
                }
            }else{
                if (orderModels.get(position).getDiscount().equalsIgnoreCase("null")|| orderModels.get(position).getDiscount().equalsIgnoreCase("")) {
                    if (orderModels.get(position).getService_charge().equalsIgnoreCase("0.00") || orderModels.get(position).getService_charge().equalsIgnoreCase("0") || orderModels.get(position).getService_charge().equalsIgnoreCase("")) {
                        float quantityInt = Float.parseFloat(orderModels.get(position).getQuantity());
                        float priceInt = Float.parseFloat(orderModels.get(position).getPrice());
                        float ProdPriceStrFloat = priceInt * quantityInt;
                        TotalProdPrice = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                        viewHolder.order_price.setText(TotalProdPrice);
                    } else {
                        Float ProdPriceF = Float.valueOf(orderModels.get(position).getPrice());
                        Float ServiceChargeF = Float.valueOf(orderModels.get(position).getService_charge());
                        Float finalServiceCharge = (ProdPriceF * ServiceChargeF) / 100;
                        Float ProdPriceFinal = Float.valueOf(orderModels.get(position).getPrice());
                        SerChargeFinal = ProdPriceFinal + finalServiceCharge;
                        float quantityInt = Float.parseFloat(orderModels.get(position).getQuantity());
                        float ProdPriceStrFloat = quantityInt * SerChargeFinal;
                        TotalProdPrice = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                        viewHolder.order_price.setText(TotalProdPrice);
                    }
                }else {
                    int OriginalPrice = Integer.parseInt(orderModels.get(position).getPrice());
                    int DiscountStr = Integer.parseInt(orderModels.get(position).getDiscount());
                    int DiscountPrice = OriginalPrice * DiscountStr;
                    float DisPriceStr = Float.parseFloat(String.valueOf(DiscountPrice)) / 100;
                    TotalPriceStr = Float.parseFloat(orderModels.get(position).getPrice()) - DisPriceStr;
                    if (orderModels.get(position).getService_charge().equalsIgnoreCase("0.00") || orderModels.get(position).getService_charge().equalsIgnoreCase("0") || orderModels.get(position).getService_charge().equalsIgnoreCase("")) {
                        float quantityInt = Float.parseFloat(orderModels.get(position).getQuantity());
                        float ProdPriceStrFloat = quantityInt * TotalPriceStr;
                        TotalProdPrice = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                        viewHolder.order_price.setText(TotalProdPrice);
                    } else {
                        Float ProdPriceF = Float.valueOf(orderModels.get(position).getPrice());
                        Float ServiceChargeF = Float.valueOf(orderModels.get(position).getService_charge());
                        Float finalServiceCharge = (ProdPriceF * ServiceChargeF) / 100;
                        Float totalProdPriceF = Float.valueOf(TotalPriceStr);
                        SerChargeFinal = totalProdPriceF + finalServiceCharge;
                        float quantityInt = Float.parseFloat(orderModels.get(position).getQuantity());
                        float ProdPriceStrFloat = quantityInt * SerChargeFinal;
                        TotalProdPrice = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                        viewHolder.order_price.setText(TotalProdPrice);
                    }
                }
            }
            if(orderModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("")||orderModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("null")) {
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);
                if (order_status.equalsIgnoreCase("0") && orderModels.get(position).getPayu_status().equalsIgnoreCase("success")) {
                    viewHolder.confirm_order_btn.setVisibility(View.VISIBLE);
                    //  viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
                    viewHolder.ll_status.setVisibility(View.GONE);
                    // viewHolder.orderStatus.setText("Pending");
                } else if (order_status.equalsIgnoreCase("0") && orderModels.get(position).getPayu_status().equalsIgnoreCase("failure")) {
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Payment Failed");
                    viewHolder.orderStatus.setTextColor(context.getResources().getColor(R.color.red));
                }else if (order_status.equalsIgnoreCase("1")) {
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
                    viewHolder.confirm_order_btn.setVisibility(View.GONE);
                    viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Delivered");
                    viewHolder.orderStatus.setTextColor(context.getResources().getColor(R.color.btn_background_color));
                }else if(order_status.equalsIgnoreCase("5")){
                    viewHolder.confirm_order_btn.setVisibility(View.GONE);
                    viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Completed");
                    viewHolder.orderStatus.setTextColor(context.getResources().getColor(R.color.btn_background_color));
                }
            }else{
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);
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
                    // viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
                    viewHolder.ll_status.setVisibility(View.GONE);
                    // viewHolder.orderStatus.setText("Pending");
                }else if(order_status.equalsIgnoreCase("4")){
                    viewHolder.confirm_order_btn.setVisibility(View.GONE);
                    viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Delivered");
                    viewHolder.orderStatus.setTextColor(context.getResources().getColor(R.color.btn_background_color));
                }else if(order_status.equalsIgnoreCase("5")){
                    viewHolder.confirm_order_btn.setVisibility(View.GONE);
                    viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Completed");
                    viewHolder.orderStatus.setTextColor(context.getResources().getColor(R.color.btn_background_color));
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
                            customdialog = new Dialog(context);
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
                                        Toast.makeText(context, "Please Fill Empty Fields", Toast.LENGTH_SHORT).show();
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
                                            ((StoreReceivedOrdersActivity)context).receivedOrderList();
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
                                            ((StoreReceivedOrdersActivity)context).receivedOrderList();
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
                Toast.makeText(context, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
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
                            ((StoreReceivedOrdersActivity)context).receivedOrderList();
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
