package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.AddPaymentActivity;
import com.sustowns.sustownsapp.Activities.AddTransportActivity;
import com.sustowns.sustownsapp.Activities.ContractOrdersInvoice;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.ContractPurchasesModel;

import java.util.ArrayList;

public class MyContractPurchasesAdapter extends RecyclerView.Adapter<MyContractPurchasesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,complete_amount_status,order_status,paymentStatus;
    PreferenceUtils preferenceUtils;
    String[] order;
    ArrayList<ContractPurchasesModel> contractPurchasesModels;
    ProgressDialog progressDialog;

    public MyContractPurchasesAdapter(Context context, ArrayList<ContractPurchasesModel> contractPurchasesModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contractPurchasesModels = contractPurchasesModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if(contractPurchasesModels.get(position) != null) {
            viewHolder.orderName.setText(contractPurchasesModels.get(position).getJob_name());
            viewHolder.order_no.setText(contractPurchasesModels.get(position).getInvoice_no());
            viewHolder.orderDate.setText(contractPurchasesModels.get(position).getOrder_date());
            viewHolder.order_price.setText(contractPurchasesModels.get(position).getTotalprice());
            order_status = contractPurchasesModels.get(position).getOrder_status();
            paymentStatus = contractPurchasesModels.get(position).getPayment_status();
            complete_amount_status = contractPurchasesModels.get(position).getComplete_amount_status();
            if(order_status.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Pending");
               /* viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);*/
            }else if(order_status.equalsIgnoreCase("1") && contractPurchasesModels.get(position).getJob_location().equalsIgnoreCase("buyers")){
               /* viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.VISIBLE);// if Transport is required
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);*/
                viewHolder.add_transport_btn.setVisibility(View.VISIBLE);
                viewHolder.orderStatus.setText("Complete");
            }else if(order_status.equalsIgnoreCase("2")){
              /*  viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);*/
                viewHolder.orderStatus.setText("Rejected");
            }
            if(complete_amount_status.equalsIgnoreCase("0") && !contractPurchasesModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("")) {
                viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
               /* viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.ll_paymentstatus.setVisibility(View.GONE);*/
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
            }else{

            }
   /*         if(order_status.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Pending");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                if(complete_amount_status.equalsIgnoreCase("0") && !contractPurchasesModels.get(position).getBank_thr_ran_id().isEmpty()){
                    viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                }else{
                    viewHolder.add_payment_btn.setVisibility(View.GONE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                }
            }else if(order_status.equalsIgnoreCase("1")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                //   viewHolder.add_transport_btn.setVisibility(View.VISIBLE);(for transport in contracts)
                viewHolder.orderStatus.setText("Complete");
            }else if(order_status.equalsIgnoreCase("2")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Cancelled");
            }else if(order_status.equalsIgnoreCase("3")){
                viewHolder.orderStatus.setText("Payment Processing");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }
            if(paymentStatus.equalsIgnoreCase("2")){
                viewHolder.orderStatus.setText("Payment Received Successfully");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }else if(paymentStatus.equalsIgnoreCase("1")){
                viewHolder.orderStatus.setText("payment in process");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }else if(paymentStatus.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Payment Process was failed");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }*/
           /* if(complete_amount_status.equalsIgnoreCase("0") && !contractPurchasesModels.get(position).getBank_thr_ran_id().isEmpty()){
                viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
            }
            if(order_status.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Pending");
            }else if(order_status.equalsIgnoreCase("1")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
             //   viewHolder.add_transport_btn.setVisibility(View.VISIBLE);(for transport in contracts)
                viewHolder.orderStatus.setText("Complete");
            }else if(order_status.equalsIgnoreCase("2")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Cancelled");
            }else if(order_status.equalsIgnoreCase("3")){
                viewHolder.orderStatus.setText("Payment Processing");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }*/
        }
        viewHolder.add_transport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddTransportActivity.class);
                i.putExtra("OrderId",contractPurchasesModels.get(position).getOrder_id());
                i.putExtra("InvoiceNo",contractPurchasesModels.get(position).getInvoice_no());
                i.putExtra("ContractTransport","1");
                context.startActivity(i);
            }
        });

        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_id = contractPurchasesModels.get(position).getOrder_id();
                String invoice_no = contractPurchasesModels.get(position).getInvoice_no();
                Intent i = new Intent(context, ContractOrdersInvoice.class);
                i.putExtra("OrderId",order_id);
                i.putExtra("InvoiceNo",invoice_no);
                i.putExtra("Status",contractPurchasesModels.get(position).getOrder_status());
                context.startActivity(i);
            }
        });
        viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddPaymentActivity.class);
                i.putExtra("OrderId",contractPurchasesModels.get(position).getOrder_id());
                i.putExtra("BankRandId",contractPurchasesModels.get(position).getBank_thr_ran_id());
                i.putExtra("RandId",contractPurchasesModels.get(position).getInvoice_no());
                i.putExtra("ContractName",contractPurchasesModels.get(position).getJob_name());
                i.putExtra("UniqueCode",contractPurchasesModels.get(position).getUnique_id());
                i.putExtra("Amount",contractPurchasesModels.get(position).getTotalprice());
                i.putExtra("Date",contractPurchasesModels.get(position).getOrder_date());
                i.putExtra("Status",contractPurchasesModels.get(position).getOrder_status());
                i.putExtra("ContractOrders","2");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contractPurchasesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,remove_product,increase;
        TextView orderName,order_no,orderDate,orderStatus,order_price,payment_status;
        Button add_payment_btn,add_transport_btn,view_invoice_btn;
        LinearLayout ll_paymentstatus;
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
        }
    }
}
