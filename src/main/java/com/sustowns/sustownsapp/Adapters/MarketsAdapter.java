package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.MarketModel;

import java.util.ArrayList;

public class MarketsAdapter extends RecyclerView.Adapter<MarketsAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    ArrayList<MarketModel> marketModels;
    ProgressDialog progressDialog;

    public MarketsAdapter(Context context, ArrayList<MarketModel> marketModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.marketModels = marketModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.markets_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.ll_existing_addresses.setVisibility(View.GONE);
        viewHolder.ll_markets.setVisibility(View.VISIBLE);
        if(marketModels.get(position) != null){
            viewHolder.location_market.setText(marketModels.get(position).getLocation());
            viewHolder.description_market.setText(marketModels.get(position).getDescription());
            viewHolder.price_market.setText(marketModels.get(position).getCurrency()+" "+marketModels.get(position).getDay());
        }else{
            viewHolder.location_market.setText("");
            viewHolder.price_market.setText("");
            viewHolder.description_market.setText("");

        }
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return marketModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView location_market,price_market,description_market;
        LinearLayout ll_existing_addresses,ll_markets;
        public ViewHolder(View view) {
            super(view);
            location_market = (TextView) view.findViewById(R.id.location_market);
            price_market = (TextView) view.findViewById(R.id.price_market);
            description_market = (TextView) view.findViewById(R.id.description_market);
            ll_existing_addresses = (LinearLayout) view.findViewById(R.id.ll_existing_addresses);
            ll_markets = (LinearLayout) view.findViewById(R.id.ll_markets);

        }
    }
}
