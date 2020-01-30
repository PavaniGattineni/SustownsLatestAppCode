package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.ProductDetailsActivity;
import com.sustowns.sustownsapp.Models.GetHomeProducts;

import java.util.ArrayList;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    Context context;
    int images[];
    LayoutInflater inflter;
    LinearLayout grid_view_items;
    String pro_id,image;
    ArrayList<GetHomeProducts> getHomeProducts;

    public GridAdapter(Context context, ArrayList<GetHomeProducts> getHomeProducts) {
        this.context = context;
        this.getHomeProducts = getHomeProducts;
        inflter = (LayoutInflater.from(context));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if(getHomeProducts.get(i) != null)
        {
            if(getHomeProducts.get(i).getPr_image().isEmpty() || getHomeProducts.get(i).getPr_image().equalsIgnoreCase("")){
                Picasso.get()
                        .load(R.drawable.no_image_available)
                       // .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.imageView);
            }else{
                Picasso.get()
                        .load(getHomeProducts.get(i).getPr_image())
                       // .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.imageView);
            }
            viewHolder.pro_name.setText(getHomeProducts.get(i).getPr_title());
            viewHolder.country_text.setText(getHomeProducts.get(i).getCountry_name());
            viewHolder.location_text.setText(getHomeProducts.get(i).getCity_name());
            viewHolder.pr_currency.setText(getHomeProducts.get(i).getPr_currency());
            viewHolder.pr_price.setText(getHomeProducts.get(i).getPr_price()+" /");
            viewHolder.pr_weight.setText(getHomeProducts.get(i).getPr_weight()+" "+getHomeProducts.get(i).getWeight_unit());
        }else{
            viewHolder.imageView.setImageResource(R.drawable.no_image_available);
            viewHolder.pro_name.setText("");
            viewHolder.country_text.setText("");
            viewHolder.location_text.setText("");
            viewHolder.pr_currency.setText("");
            viewHolder.pr_price.setText("");
            viewHolder.pr_weight.setText("");
        }
        grid_view_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image = getHomeProducts.get(i).getPr_image();
                pro_id = getHomeProducts.get(i).getId();
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Pro_Id",pro_id);
                intent.putExtra("Image",image);
                intent.putExtra("Status","1");
                intent.putExtra("StoreMgmt","1");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getHomeProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,shipping_image;
        TextView pro_name,country_text,location_text,pr_currency,pr_price,pr_weight;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            pro_name = (TextView) view.findViewById(R.id.pro_name);
            country_text = (TextView) view.findViewById(R.id.country_text);
            location_text = (TextView) view.findViewById(R.id.location_text);
            pr_currency = (TextView) view.findViewById(R.id.pr_currency);
            pr_price = (TextView) view.findViewById(R.id.pr_price);
            pr_weight = (TextView) view.findViewById(R.id.pr_weight);
            grid_view_items = (LinearLayout) view.findViewById(R.id.grid_view_items);
        }
    }
}



















/*
public class GridAdapter extends BaseAdapter {
    Context context;
    int images[];
    LayoutInflater inflter;
    LinearLayout grid_view_items;
    String pro_id,image;
    ArrayList<GetHomeProducts> getHomeProducts;
    public GridAdapter(Context context, ArrayList<GetHomeProducts> getHomeProducts) {
        this.context = context;
        this.getHomeProducts = getHomeProducts;
        inflter = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return getHomeProducts.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.gridview_item, null); // inflate the layout
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView pro_name = (TextView) view.findViewById(R.id.pro_name);
        TextView country_text = (TextView) view.findViewById(R.id.country_text);
        TextView location_text = (TextView) view.findViewById(R.id.location_text);
        TextView pr_currency = (TextView) view.findViewById(R.id.pr_currency);
        TextView pr_price = (TextView) view.findViewById(R.id.pr_price);
        TextView pr_weight = (TextView) view.findViewById(R.id.pr_weight);
        grid_view_items = (LinearLayout) view.findViewById(R.id.grid_view_items);
        if(getHomeProducts.get(i) != null)
        {
            if(getHomeProducts.get(i).getPr_image().isEmpty() || getHomeProducts.get(i).getPr_image().equalsIgnoreCase("")){
                Picasso.get()
                        .load(R.drawable.no_image_available)
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(imageView);
            }else{
                Picasso.get()
                        .load(getHomeProducts.get(i).getPr_image())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(imageView);
            }
            pro_name.setText(getHomeProducts.get(i).getPr_title());
            country_text.setText(getHomeProducts.get(i).getCountry());
            location_text.setText(getHomeProducts.get(i).getState());
            pr_currency.setText(getHomeProducts.get(i).getPr_currency());
            pr_price.setText(getHomeProducts.get(i).getPr_price()+" /");
            pr_weight.setText(getHomeProducts.get(i).getPr_weight()+" mm");
        }else{
            imageView.setImageResource(R.drawable.no_image_available);
            pro_name.setText("");
            country_text.setText("");
            location_text.setText("");
            pr_currency.setText("");
            pr_price.setText("");
            pr_weight.setText("");
        }
        grid_view_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image = getHomeProducts.get(i).getPr_image();
                pro_id = getHomeProducts.get(i).getId();
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Pro_Id",pro_id);
                intent.putExtra("Image",image);
                context.startActivity(intent);
            }
        });
//        imageView.setImageResource(images[i]); // set logo images
        return view;
    }
}
*/
