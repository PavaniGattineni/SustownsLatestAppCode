package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.QuoteNowActivity;
import com.sustowns.sustownsapp.Models.OpenRequestModel;

import java.util.ArrayList;

public class OpenRequestAdapter extends RecyclerView.Adapter<OpenRequestAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,job_id;
    PreferenceUtils preferenceUtils;
    String[] text;
    ArrayList<OpenRequestModel> openRequestModels;
    ProgressDialog progressDialog;

    public OpenRequestAdapter(Context context,ArrayList<OpenRequestModel> openRequestModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.openRequestModels = openRequestModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.open_request_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(openRequestModels.get(position) != null){
            if(openRequestModels.get(position).getMy_bid_id().equalsIgnoreCase("0")){
                viewHolder.card_view.setVisibility(View.VISIBLE);
                viewHolder.title.setText(openRequestModels.get(position).getContractname());
                viewHolder.bid_prod_name.setText(openRequestModels.get(position).getJob_name());
                viewHolder.bid_quantity.setText(openRequestModels.get(position).getMinqantity()+" "+openRequestModels.get(position).getQnt_weight());
                viewHolder.bid_quote.setText(openRequestModels.get(position).getEnd_date());
                viewHolder.buyer_name.setText(openRequestModels.get(position).getFullname());
                viewHolder.bid_location.setText(openRequestModels.get(position).getJob_location());
                viewHolder.bid_close_in.setText(openRequestModels.get(position).getJob_date());
                Picasso.get()
                        .load(openRequestModels.get(position).getImage())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.bid_image);
            /*  Glide.with(context)
                    .load(openRequestModels.get(position).getImage())
                    .into(viewHolder.bid_image);*/
            }else {
                viewHolder.card_view.setVisibility(View.GONE);
            }
        }
        viewHolder.quote_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = openRequestModels.get(position).getId();
                Intent i = new Intent(context, QuoteNowActivity.class);
                i.putExtra("AuthValue","1");
                i.putExtra("JobId",job_id);
                context.startActivity(i);
            }
        });
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return openRequestModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bid_image;
        TextView title,bid_prod_name,bid_quantity,bid_quote,buyer_name,bid_close_in,bid_location;
        Button quote_now_btn;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            bid_image = (ImageView) view.findViewById(R.id.bid_image);
            title = (TextView) view.findViewById(R.id.title);
            bid_prod_name = (TextView) view.findViewById(R.id.bid_prod_name);
            bid_quantity = (TextView) view.findViewById(R.id.bid_quantity);
            bid_quote = (TextView) view.findViewById(R.id.bid_quote);
            buyer_name = (TextView) view.findViewById(R.id.buyer_name);
            bid_location = (TextView) view.findViewById(R.id.bid_location);
            bid_close_in = (TextView) view.findViewById(R.id.bid_close_in);
            quote_now_btn = (Button) view.findViewById(R.id.quote_now_btn);
            card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }
}
