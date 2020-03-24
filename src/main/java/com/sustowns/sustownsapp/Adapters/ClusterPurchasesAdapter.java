package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.Activities.OrderDetailsActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.OrderModel;
import com.sustowns.sustownsapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ClusterPurchasesAdapter extends RecyclerView.Adapter<ClusterPurchasesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String userid,order_status,order_id,pay_method,complete_amount_status,paymentStatus,TotalProdPrice;    
    ArrayList<OrderModel> orderModels;
    ProgressDialog progressDialog;
    float TotalPriceStr,SerChargeFinal;

    public ClusterPurchasesAdapter(Context context,ArrayList<OrderModel> orderModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.orderModels = orderModels;
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
        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_id = orderModels.get(position).getId();
                Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("OrderId",order_id);
                context.startActivity(i);
            }
        });
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderQuantity, orderDate, orderStatus,order_price,payment_status,order_no;
        Button add_payment_btn,add_transport_btn,confirm_order_btn,view_invoice_btn;
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
            payment_status = (TextView) view.findViewById(R.id.payment_status);
            add_transport_btn = (Button) view.findViewById(R.id.add_transport_btn);

        }
    }
}