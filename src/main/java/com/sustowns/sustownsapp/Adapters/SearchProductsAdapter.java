package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.ProductDetailsActivity;
import com.sustowns.sustownsapp.Models.SearchProductsModel;

import java.util.ArrayList;

public class SearchProductsAdapter extends BaseAdapter {
    Context context;
    Integer[] images;
    LayoutInflater inflate;
    ArrayList<SearchProductsModel> searchProductsModels;
    String pro_id, image;

    public SearchProductsAdapter(Context context, ArrayList<SearchProductsModel> searchProductsModels) {
        this.context = context;
        this.searchProductsModels = searchProductsModels;
        this.inflate = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return searchProductsModels.size();

    }

    @Override
    public Object getItem(int i) {
        return searchProductsModels;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View vi = convertView;

        if (convertView == null)
            vi = inflate.inflate(R.layout.product_list_item, null);
        ImageView imageView = (ImageView) vi.findViewById(R.id.imageView);
        TextView pro_name = (TextView) vi.findViewById(R.id.pro_name);
        TextView pr_currency = (TextView) vi.findViewById(R.id.pr_currency);
        TextView pr_price = (TextView) vi.findViewById(R.id.pr_price);
        TextView pr_weight = (TextView) vi.findViewById(R.id.pr_weight);
        TextView pr_moq = (TextView) vi.findViewById(R.id.pr_moq);
        TextView pr_min_unit = (TextView) vi.findViewById(R.id.pr_min_unit);
        TextView country_text = (TextView) vi.findViewById(R.id.country_text);
        TextView lcation_text = (TextView) vi.findViewById(R.id.lcation_text);
        LinearLayout ll_product = (LinearLayout) vi.findViewById(R.id.grid_view_items);
        if (searchProductsModels.get(i) != null) {
            if (searchProductsModels.get(i).getPr_image().isEmpty() || searchProductsModels.get(i).getPr_image().equalsIgnoreCase("")) {
                Picasso.get()
                        .load(R.drawable.no_image_available)
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(imageView);
            }else{
                Picasso.get()
                        .load(searchProductsModels.get(i).getPr_image())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(imageView);
            }
            pro_name.setText(searchProductsModels.get(i).getPr_title());
            pr_currency.setText(searchProductsModels.get(i).getPr_currency());
            pr_price.setText(searchProductsModels.get(i).getPr_price()+" /");
            pr_weight.setText(searchProductsModels.get(i).getPr_weight()+" "+searchProductsModels.get(i).getPr_weight_unit());
            pr_moq.setText("MOQ : "+searchProductsModels.get(i).getPr_min());
            pr_min_unit.setText("* (Unit : "+searchProductsModels.get(i).getPr_weight()+" "+searchProductsModels.get(i).getPr_weight_unit()+")");
            country_text.setText(searchProductsModels.get(i).getCountry_name());
            lcation_text.setText(searchProductsModels.get(i).getCity_name());
         /*   pro_name.setText(searchProductsModels.get(i).getPr_title());
            pr_currency.setText(searchProductsModels.get(i).getPr_currency());
            pr_price.setText(searchProductsModels.get(i).getPr_price() + " /");
            pr_weight.setText(searchProductsModels.get(i).getPr_weight());
            pr_moq.setText("MOQ : " + searchProductsModels.get(i).getPr_min());
            pr_min_unit.setText("* (Unit : " + searchProductsModels.get(i).getPr_gweight_unit() + ")");
            country_text.setText(searchProductsModels.get(i).getCountry());
            lcation_text.setText(searchProductsModels.get(i).getJob_location());*/

        } else {
            imageView.setImageResource(R.drawable.no_image_available);
            pr_currency.setText("");
            pr_price.setText("");
            pro_name.setText("");
            pr_moq.setText("");
            pr_min_unit.setText("");
            pr_weight.setText("");
            country_text.setText("");
            lcation_text.setText("");
        }
        ll_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_id = searchProductsModels.get(i).getId();
                image = searchProductsModels.get(i).getPr_image();
                Intent i = new Intent(context, ProductDetailsActivity.class);
                i.putExtra("Pro_Id", pro_id);
                i.putExtra("Image", image);
                i.putExtra("Status","0");
                i.putExtra("StoreMgmt","0");
                context.startActivity(i);
            }
        });
        return vi;
    }

}