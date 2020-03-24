package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.R;

public class ClusterDeliveriesAdapter extends RecyclerView.Adapter<ClusterDeliveriesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String[] orderName,orderPrice,orderNo,orderStatus,orderDate;
    ProgressDialog progressDialog;

    public ClusterDeliveriesAdapter(Context context, String[] orderName, String[] orderPrice, String[] orderNo, String[] orderStatus, String[] orderDate) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cluster_deliveries_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.orderName.setText(orderName[position]);
        viewHolder.order_no.setText(orderNo[position]);
        viewHolder.orderDate.setText(orderDate[position]);
        viewHolder.order_price.setText(orderPrice[position]);
        viewHolder.orderStatus.setText(orderStatus[position]);

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return orderName.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
            order_price = (TextView) view.findViewById(R.id.order_price);
            order_no = (TextView) view.findViewById(R.id.order_no);

        }
    }
}
