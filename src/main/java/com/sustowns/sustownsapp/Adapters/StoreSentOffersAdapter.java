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
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.PaymentContractsActvity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.StoreSentOffersModel;

import java.util.ArrayList;

public class StoreSentOffersAdapter extends RecyclerView.Adapter<StoreSentOffersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,status,image;
    PreferenceUtils preferenceUtils;
    String[] order;
    ArrayList<StoreSentOffersModel> storeSentOffersModels;
    ProgressDialog progressDialog;

    public StoreSentOffersAdapter(Context context, ArrayList<StoreSentOffersModel> storeSentOffersModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.storeSentOffersModels = storeSentOffersModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sent_offers_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        status = storeSentOffersModels.get(position).getStatus();
        if(storeSentOffersModels.get(position) != null){
            viewHolder.prod_name.setText(storeSentOffersModels.get(position).getPr_title());
            viewHolder.offer_price.setText("INR "+storeSentOffersModels.get(position).getMakepeice());
            viewHolder.offer_quantity.setText(storeSentOffersModels.get(position).getMakeqty());
            viewHolder.offered_by.setText(storeSentOffersModels.get(position).getFullname());
            if(status.equalsIgnoreCase("1")){
                viewHolder.text_offer_status.setVisibility(View.VISIBLE);
                viewHolder.offer_btn.setVisibility(View.GONE);
                viewHolder.offer_accepted.setVisibility(View.GONE);
                viewHolder.text_offer_status.setText("Awaiting for Acceptence");
            }else if(status.equalsIgnoreCase("2")){
                viewHolder.text_offer_status.setVisibility(View.GONE);
                viewHolder.offer_btn.setVisibility(View.VISIBLE);
                viewHolder.offer_btn.setText("Proceed To Pay");
                viewHolder.offer_accepted.setVisibility(View.VISIBLE);
                viewHolder.offer_accepted.setText("Offer Accepted");
            }else if(status.equalsIgnoreCase("3")){
                viewHolder.text_offer_status.setVisibility(View.VISIBLE);
                viewHolder.offer_btn.setVisibility(View.GONE);
                viewHolder.offer_accepted.setVisibility(View.GONE);
                viewHolder.text_offer_status.setText("Paid");
            }else if(status.equalsIgnoreCase("0")){
                viewHolder.text_offer_status.setVisibility(View.VISIBLE);
                viewHolder.offer_btn.setVisibility(View.GONE);
                viewHolder.offer_accepted.setVisibility(View.GONE);
                viewHolder.text_offer_status.setText("Offer Rejected");
            }
        }
        viewHolder.offer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PaymentContractsActvity.class);
                i.putExtra("Logistics","Offers");
                i.putExtra("Amount",storeSentOffersModels.get(position).getMakepeice());
                i.putExtra("makeOfferId",storeSentOffersModels.get(position).getId());
                context.startActivity(i);
            }
        });
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return storeSentOffersModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView remove_offer,accept_offer_img;
        TextView prod_name,offer_prod_price,offer_quantity,offer_price,offered_by,text_offer_status,offer_accepted;
        Button offer_btn;
        public ViewHolder(View view) {
            super(view);
            prod_name = (TextView) view.findViewById(R.id.prod_name);
            offer_prod_price = (TextView) view.findViewById(R.id.offer_prod_price);
            offer_quantity = (TextView) view.findViewById(R.id.offer_quantity);
            offer_price = (TextView) view.findViewById(R.id.offer_price);
            offered_by = (TextView) view.findViewById(R.id.offered_by);
            remove_offer = (ImageView) view.findViewById(R.id.remove_offer);
            accept_offer_img = (ImageView) view.findViewById(R.id.accept_offer_img);
            offer_btn = (Button) view.findViewById(R.id.offer_btn);
            text_offer_status = (TextView) view.findViewById(R.id.text_offer_status);
            offer_accepted = (TextView) view.findViewById(R.id.offer_accepted);
        }
    }
}

