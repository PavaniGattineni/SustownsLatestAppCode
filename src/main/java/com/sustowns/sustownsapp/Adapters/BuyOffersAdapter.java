package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;

public class BuyOffersAdapter extends RecyclerView.Adapter<BuyOffersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String[] order;
   // ArrayList<CartListModel> cartListModels;
    ProgressDialog progressDialog;

    public BuyOffersAdapter(Context context, String[] order) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.order = order;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sent_offers_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.pro_name.setText(order[position]);
     /*   viewHolder.orderQuantity.setText(order[position]);
        viewHolder.orderDate.setText(order[position]);
        viewHolder.orderStatus.setText(order[position]);*/

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return order.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,decrease,increase;
        TextView pro_name,description_text,location_text,posted_on,stock_text;
        public ViewHolder(View view) {
            super(view);
            pro_name = (TextView) view.findViewById(R.id.pro_name);
            description_text = (TextView) view.findViewById(R.id.description_text);
            location_text = (TextView) view.findViewById(R.id.location_text);
            posted_on = (TextView) view.findViewById(R.id.posted_on);
            stock_text = (TextView) view.findViewById(R.id.stock_text);

        }
    }
}
