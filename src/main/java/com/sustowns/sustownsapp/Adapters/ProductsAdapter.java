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
import com.sustowns.sustownsapp.Models.PoultryProductsModel;

import java.util.ArrayList;

public class ProductsAdapter extends BaseAdapter {
    Context context;
    Integer[] images;
    LayoutInflater inflate;
    ArrayList<PoultryProductsModel> poultryProductsModels;
    String pro_id,image;

    public ProductsAdapter(Context context, ArrayList<PoultryProductsModel> poultryProductsModels) {
        this.context=context;
        this.poultryProductsModels = poultryProductsModels;
        this.inflate = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return poultryProductsModels.size();

    }

    @Override
    public Object getItem(int i) {
        return poultryProductsModels;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View vi=convertView;

        if(convertView==null)
            vi = inflate.inflate(R.layout.product_list_item, null);
        ImageView imageView = (ImageView) vi.findViewById(R.id.imageView);
        TextView pro_name = (TextView) vi.findViewById(R.id.pro_name);
        TextView pr_currency = (TextView) vi.findViewById(R.id.pr_currency);
        TextView pr_price = (TextView) vi.findViewById(R.id.pr_price);
        TextView pr_weight = (TextView) vi.findViewById(R.id.pr_weight);
        TextView pr_moq = (TextView) vi.findViewById(R.id.pr_moq);
        TextView pr_min_unit = (TextView) vi.findViewById(R.id.pr_min_unit);
        TextView country_text =(TextView) vi.findViewById(R.id.country_text);
        TextView lcation_text = (TextView) vi.findViewById(R.id.lcation_text);
        LinearLayout ll_product = (LinearLayout) vi.findViewById(R.id.grid_view_items);
        if(poultryProductsModels.get(i) != null){
            if(poultryProductsModels.get(i).getPr_image().isEmpty() || poultryProductsModels.get(i).getPr_image().equalsIgnoreCase("")){
                Picasso.get()
                        .load(R.drawable.no_image_available)
                        //.placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(imageView);
            }else{
                Picasso.get()
                        .load(poultryProductsModels.get(i).getPr_image())
                        //.placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(imageView);
            }
            pro_name.setText(poultryProductsModels.get(i).getPr_title());
            pr_currency.setText(poultryProductsModels.get(i).getPr_currency());
            pr_price.setText(poultryProductsModels.get(i).getPr_price()+" /");
            pr_weight.setText(poultryProductsModels.get(i).getPr_weight()+" "+poultryProductsModels.get(i).getWeight_unit());
            pr_moq.setText("MOQ : "+poultryProductsModels.get(i).getPr_min());
            pr_min_unit.setText("* (Unit : "+poultryProductsModels.get(i).getPr_weight()+")");
            country_text.setText(poultryProductsModels.get(i).getCountry_name());
            lcation_text.setText(poultryProductsModels.get(i).getCity_name());

        }else{
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
                pro_id = poultryProductsModels.get(i).getId();
                image = poultryProductsModels.get(i).getPr_image();
                Intent i = new Intent(context, ProductDetailsActivity.class);
                i.putExtra("Pro_Id",pro_id);
                i.putExtra("Image",image);
                i.putExtra("Status","0");
                i.putExtra("StoreMgmt","0");
                context.startActivity(i);
            }
        });
        return vi;
    }

}
