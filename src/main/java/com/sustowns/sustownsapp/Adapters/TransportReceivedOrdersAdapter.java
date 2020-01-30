package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.TransportContractOrdersActivity;
import com.sustowns.sustownsapp.Activities.TransportDetailsActivity;
import com.sustowns.sustownsapp.Activities.TransportOrdersActivity;
import com.sustowns.sustownsapp.Models.TransportContractOrdersModel;
import com.sustowns.sustownsapp.Models.TransportOrdersModel;

import java.util.ArrayList;
import java.util.List;

public class TransportReceivedOrdersAdapter extends RecyclerView.Adapter<TransportReceivedOrdersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    PreferenceUtils preferenceUtils;
    ArrayList<TransportOrdersModel> orderModels;
    List<TransportContractOrdersModel> transportDetailsList;
    public static final int TRANSPORT_ORDER = 1;
    public static final int TRANSPORT_CONTRACT_DETAILS = 2;

    public TransportReceivedOrdersAdapter(Context context, ArrayList<TransportOrdersModel> orderModels, List<TransportContractOrdersModel> transportDetailsList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.orderModels = orderModels;
        this.transportDetailsList = transportDetailsList;
    }

    @Override
    public TransportReceivedOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transport_order_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new TransportReceivedOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TransportReceivedOrdersAdapter.ViewHolder viewHolder, final int position) {
        try {
            switch (viewHolder.getItemViewType()) {
                case TRANSPORT_ORDER:
                    viewHolder.order_weight.setText(orderModels.get(position).getPr_title());
                    viewHolder.product_code.setText(orderModels.get(position).getPr_sku());
                    viewHolder.order_name.setText(orderModels.get(position).getService_name());
                    viewHolder.order_number.setText(orderModels.get(position).getInvoice_no());
                    viewHolder.order_date.setText(orderModels.get(position).getOrder_date());
                    if(orderModels.get(position).getTrans_status().equalsIgnoreCase("1")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Confirmed");
                    }else if(orderModels.get(position).getTrans_status().equalsIgnoreCase("0")&& orderModels.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                        viewHolder.ll_confirm_reject.setVisibility(View.VISIBLE);
                        viewHolder.order_quote.setVisibility(View.VISIBLE);
                        viewHolder.order_confirm.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.GONE);
                    }
                    else if(orderModels.get(position).getTrans_status().equalsIgnoreCase("0")&& orderModels.get(position).getManual_automatic().equalsIgnoreCase("automatic")){
                        viewHolder.ll_confirm_reject.setVisibility(View.VISIBLE);
                        viewHolder.order_quote.setVisibility(View.GONE);
                        viewHolder.order_confirm.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setVisibility(View.GONE);
                    }else if(orderModels.get(position).getTrans_status().equalsIgnoreCase("2")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Cancelled");
                    }else if(orderModels.get(position).getTrans_status().equalsIgnoreCase("3") && orderModels.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Quote Submitted");
                    }else if(orderModels.get(position).getTrans_status().equalsIgnoreCase("4") && orderModels.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Quote Confirmed");
                    }else if(orderModels.get(position).getTrans_status().equalsIgnoreCase("7")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Awaiting for Product Pickup");
                    }
                    viewHolder.order_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportOrdersActivity) context).confirmOrder(position);
                        }
                    });
                    viewHolder.order_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportOrdersActivity) context).rejectOrder(position);
                        }
                    });
                    viewHolder.order_view_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportOrdersActivity) context).viewDetails(position);
                        }
                    });
                    viewHolder.order_quote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportOrdersActivity) context).sendQuoteDetails(position);
                        }
                    });
                    break;
                case TRANSPORT_CONTRACT_DETAILS:
                    viewHolder.order_weight.setText(transportDetailsList.get(position).getPr_title());
                    viewHolder.product_code.setText(transportDetailsList.get(position).getPr_sku());
                    viewHolder.order_name.setText(transportDetailsList.get(position).getService_name());
                    viewHolder.order_number.setText(transportDetailsList.get(position).getInvoice_no());
                    viewHolder.order_date.setText(transportDetailsList.get(position).getOrder_date());
                    if(transportDetailsList.get(position).getTrans_status().equalsIgnoreCase("1")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Confirmed");
                    }else if(transportDetailsList.get(position).getTrans_status().equalsIgnoreCase("0")&& transportDetailsList.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                        viewHolder.ll_confirm_reject.setVisibility(View.VISIBLE);
                        viewHolder.order_quote.setVisibility(View.VISIBLE);
                        viewHolder.order_confirm.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.GONE);
                    }
                    else if(transportDetailsList.get(position).getTrans_status().equalsIgnoreCase("0")&& transportDetailsList.get(position).getManual_automatic().equalsIgnoreCase("automatic")){
                        viewHolder.ll_confirm_reject.setVisibility(View.VISIBLE);
                        viewHolder.order_quote.setVisibility(View.GONE);
                        viewHolder.order_confirm.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setVisibility(View.GONE);
                    }else if(transportDetailsList.get(position).getTrans_status().equalsIgnoreCase("2")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Cancelled");
                    }else if(transportDetailsList.get(position).getTrans_status().equalsIgnoreCase("3") && transportDetailsList.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Quote Submitted");
                    }else if(transportDetailsList.get(position).getTrans_status().equalsIgnoreCase("4") && transportDetailsList.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                        viewHolder.ll_confirm_reject.setVisibility(View.GONE);
                        viewHolder.status_text.setVisibility(View.VISIBLE);
                        viewHolder.status_text.setText("Quote Confirmed");
                    }
                    viewHolder.order_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportContractOrdersActivity) context).confirmOrder(position);
                        }
                    });
                    viewHolder.order_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportContractOrdersActivity) context).rejectOrder(position);
                        }
                    });
                    viewHolder.order_view_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportContractOrdersActivity) context).viewDetails(position);
                        }
                    });
                    viewHolder.order_quote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportContractOrdersActivity) context).sendQuoteDetails(position);
                        }
                    });
                    break;
               /* case TRANSPORT_DETAILS:
                    viewHolder.order_name.setText(transportDetailsList.get(position).getPr_title());
                    viewHolder.order_number.setText(transportDetailsList.get(position).getInvoice_no());
                    viewHolder.order_date.setText(transportDetailsList.get(position).getOrder_date());
                    viewHolder.order_weight.setText("Quantity : " + transportDetailsList.get(position).getQuantity());

                    viewHolder.order_confirm.setText("Pay");
                    viewHolder.order_reject.setText("Contact Details");
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.order_reject.getLayoutParams();
                    viewHolder.order_reject.setLayoutParams(params);

                    viewHolder.order_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // ((TransportDetailsActivity) context).payOrder(position);
                        }
                    });

                    viewHolder.order_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TransportDetailsActivity) context).contactDetails(position);
                        }
                    });

                    viewHolder.order_view_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  ((TransportDetailsActivity) context).viewDetails(position);
                        }
                    });
                    break;*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        if (context instanceof TransportContractOrdersActivity)
            return transportDetailsList.size();
        else
            return orderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_weight, order_date, order_name, order_number, order_status,status_text,product_code,product_name;
        Button order_confirm, order_reject, order_view_details,order_quote;
        LinearLayout ll_confirm_reject;

        public ViewHolder(View view) {
            super(view);
            order_weight = (TextView) view.findViewById(R.id.order_quantity);
            order_date = (TextView) view.findViewById(R.id.order_date);
            order_name = (TextView) view.findViewById(R.id.order_name);
            order_number = view.findViewById(R.id.order_number);
            order_status = view.findViewById(R.id.order_status);
            order_confirm = view.findViewById(R.id.order_confirm);
            order_reject = view.findViewById(R.id.order_reject);
            order_quote = view.findViewById(R.id.order_quote);
            order_view_details = view.findViewById(R.id.order_view_details);
            ll_confirm_reject = (LinearLayout) view.findViewById(R.id.ll_confirm_reject);
            status_text = (TextView) view.findViewById(R.id.status_text);
            product_code = (TextView) view.findViewById(R.id.product_code);
            product_name = (TextView) view.findViewById(R.id.product_name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;
        if (context instanceof TransportContractOrdersActivity) {
            viewType = TRANSPORT_CONTRACT_DETAILS;
        } else {
            viewType = TRANSPORT_ORDER;
        }
        return viewType;
    }

    @Override
    public long getItemId(int position) {
        if (context instanceof TransportDetailsActivity) {
            return transportDetailsList.size();
        } else {
            return orderModels.size();
        }
    }
}
