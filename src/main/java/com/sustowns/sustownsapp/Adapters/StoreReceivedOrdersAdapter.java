package com.sustowns.sustownsapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.AddPaymentActivity;
import com.sustowns.sustownsapp.Activities.AddTransportActivity;
import com.sustowns.sustownsapp.Activities.LocationDialogActivity;
import com.sustowns.sustownsapp.Activities.OrderDetailsActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.StoreReceivedOrdersActivity;
import com.sustowns.sustownsapp.Models.OrderModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StoreReceivedOrdersAdapter extends RecyclerView.Adapter<StoreReceivedOrdersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,userid,user_role,order_status,order_id,pay_method,complete_amount_status,paymentStatus,TotalProdPrice;
    PreferenceUtils preferenceUtils;
    ArrayList<OrderModel> orderModels;
    ProgressDialog progressDialog;
    Helper helper;
    float TotalPriceStr,SerChargeFinal;

    public StoreReceivedOrdersAdapter(Context context, ArrayList<OrderModel> orderModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.orderModels = orderModels;
        helper = new Helper(context);
        preferenceUtils = new PreferenceUtils(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        order_status = orderModels.get(position).getOrder_status();
        pay_method = orderModels.get(position).getPay_method();
        complete_amount_status = orderModels.get(position).getComplete_amount_status();
        paymentStatus = orderModels.get(position).getPayment_status();
        if(orderModels.get(position) != null){
            viewHolder.orderName.setText(orderModels.get(position).getPr_title());
            viewHolder.order_no.setText(orderModels.get(position).getInvoice_no());
            viewHolder.orderDate.setText(orderModels.get(position).getOrder_date());
            if(userid.equalsIgnoreCase(orderModels.get(position).getPr_userid())) {
                if (orderModels.get(position).getDiscount().equalsIgnoreCase("null") || orderModels.get(position).getDiscount().equalsIgnoreCase("")) {
                    viewHolder.order_price.setText(orderModels.get(position).getTotalprice());
                   /* float quantityInt = Float.parseFloat(orderModels.get(position).getQuantity());
                    float priceInt = Float.parseFloat(orderModels.get(position).getTotalprice());
                    float ProdPriceStrFloat = priceInt * quantityInt;
                    TotalProdPrice = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                    viewHolder.order_price.setText(TotalProdPrice);*/
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
            if(order_status.equalsIgnoreCase("0") && orderModels.get(position).getPayu_status().equalsIgnoreCase("success")){
                viewHolder.orderStatus.setText("Pending");
               // viewHolder.add_payment_btn.setVisibility(View.GONE);
               // viewHolder.add_transport_btn.setVisibility(View.GONE);
               // viewHolder.ll_paymentstatus.setVisibility(View.GONE);
            }else  if(order_status.equalsIgnoreCase("0") && orderModels.get(position).getPayu_status().equalsIgnoreCase("failure")) {
                viewHolder.orderStatus.setText("Payment Failed");
            }
            else if(order_status.equalsIgnoreCase("1")){
               // viewHolder.add_payment_btn.setVisibility(View.GONE);
                if(orderModels.get(position).getShipamount().equalsIgnoreCase("")||orderModels.get(position).getShipamount().equalsIgnoreCase("null")) {
                    viewHolder.add_transport_btn.setVisibility(View.VISIBLE);
                }
                viewHolder.orderStatus.setText("Waiting for Deliver");
            }else if(order_status.equalsIgnoreCase("2")){
              //  viewHolder.add_payment_btn.setVisibility(View.GONE);
               // viewHolder.add_transport_btn.setVisibility(View.GONE);
              //  viewHolder.ll_paymentstatus.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Rejected");
            }else if(order_status.equalsIgnoreCase("3")){
                viewHolder.orderStatus.setText("Pending");
            }else if(order_status.equalsIgnoreCase("4")){
                viewHolder.ll_status.setVisibility(View.GONE);
                viewHolder.deliver_orderbtn.setVisibility(View.VISIBLE);
                viewHolder.deliver_orderbtn.setText("Received");
            }else if(order_status.equalsIgnoreCase("5")){
                viewHolder.orderStatus.setText("Completed");
            }
            if(complete_amount_status.equalsIgnoreCase("0") && !orderModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("null")) {
                viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
               // viewHolder.ll_paymentstatus.setVisibility(View.GONE);
               // viewHolder.add_transport_btn.setVisibility(View.GONE);
            }else if(paymentStatus.equalsIgnoreCase("2")){
                viewHolder.ll_paymentstatus.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setText("Payment Received Successfully");
            }else if(paymentStatus.equalsIgnoreCase("1")){
                viewHolder.ll_paymentstatus.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setText("payment in process");
            }else if(paymentStatus.equalsIgnoreCase("0")){
                viewHolder.ll_paymentstatus.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setText("Payment Process was failed");
            }else if(paymentStatus.equalsIgnoreCase("null")){
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);
            }
        }
        viewHolder.add_transport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderModels.get(position).getZipcode().equalsIgnoreCase("") || orderModels.get(position).getZipcode().equalsIgnoreCase("null"))
                {
                    Intent i = new Intent(context, LocationDialogActivity.class);
                    i.putExtra("ContractLocation", "2");
                    i.putExtra("OrderId", orderModels.get(position).getId());
                    i.putExtra("InvoiceNo", orderModels.get(position).getInvoice_no());
                    i.putExtra("RandId", orderModels.get(position).getProduct_order_id());
               /* i.putExtra("OrderId",orderModels.get(position).getProduct_order_id());
                i.putExtra("InvoiceNo",orderModels.get(position).getInvoice_no());
                i.putExtra("ContractTransport","0");
                */
                    context.startActivity(i);
                }else{
                    Intent i = new Intent(context, AddTransportActivity.class);
                    i.putExtra("OrderId",orderModels.get(position).getProduct_order_id());
                    i.putExtra("InvoiceNo",orderModels.get(position).getInvoice_no());
                   // i.putExtra("RandId", orderModels.get(position).getProduct_order_id());
                    i.putExtra("ContractTransport","0");
                    context.startActivity(i);
                }
            }
        });
        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_id = orderModels.get(position).getId();
                Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("OrderId",order_id);
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
        viewHolder.deliver_orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_id = orderModels.get(position).getId();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Received the product ....?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                receivedOrder();
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
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
    public void receivedOrder() {
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
        AndroidNetworking.post("https://www.sustowns.com/Storemanagementser/received")
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
                                ((StoreReceivedOrdersActivity)context).myOrdersList();
                               /* Intent i = new Intent(context, StoreReceivedOrdersActivity.class);
                                i.putExtra("Message","");
                                context.startActivity(i);*/
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
        ImageView imageView,remove_product,increase;
        TextView orderName,order_no,orderDate,orderStatus,order_price,payment_status;
        Button add_payment_btn,add_transport_btn,view_invoice_btn,deliver_orderbtn;
        LinearLayout ll_paymentstatus,ll_status;
        public ViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.order_name);
            order_no = (TextView) view.findViewById(R.id.order_no);
            orderDate = (TextView) view.findViewById(R.id.order_date);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            add_payment_btn = (Button) view.findViewById(R.id.add_payment_btn);
            add_transport_btn = (Button) view.findViewById(R.id.add_transport_btn);
            order_price = (TextView) view.findViewById(R.id.order_price);
            view_invoice_btn = (Button) view.findViewById(R.id.view_invoice_btn);
            payment_status = (TextView) view.findViewById(R.id.payment_status);
            ll_paymentstatus = (LinearLayout) view.findViewById(R.id.ll_paymentstatus);
            deliver_orderbtn = (Button) view.findViewById(R.id.deliver_orderbtn);
            ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
        }
    }
}