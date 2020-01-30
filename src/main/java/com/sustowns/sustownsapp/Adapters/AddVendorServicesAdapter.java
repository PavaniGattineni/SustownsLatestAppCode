package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.AddProductVendorServices;

import java.util.ArrayList;

public class AddVendorServicesAdapter  extends RecyclerView.Adapter<AddVendorServicesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email, user_id;
    PreferenceUtils preferenceUtils;
    String[] categories;
    String ServiceId;
    ArrayList<AddProductVendorServices> addProductVendorServices;
    ProgressDialog progressDialog;
    private SparseBooleanArray mSelectedItemsCatIds;


    public AddVendorServicesAdapter(Context context, ArrayList<AddProductVendorServices> addProductVendorServices, SparseBooleanArray mSelectedItemsCatIds) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.addProductVendorServices = addProductVendorServices;
        this.mSelectedItemsCatIds = mSelectedItemsCatIds;
    }

    @Override
    public AddVendorServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_list, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new AddVendorServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddVendorServicesAdapter.ViewHolder viewHolder, final int position) {
        // viewHolder.category_text.setTag("zero");
        viewHolder.category_text.setText(addProductVendorServices.get(position).getPr_title());

        if(mSelectedItemsCatIds.get(position)){
            viewHolder.category_text.setChecked(true);
        }
/*
        viewHolder.category_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                continent_cb = continentModels.get(position).getContinent_code();
                if (isChecked) {
                    selectedContinents.add(continent_cb);
                    mSelectedContinents.put(position, true);
                } else {
                    mSelectedContinents.delete(position);
                    if (selectedContinents.toString().contains(continent_cb))
                        selectedContinents.remove(continent_cb);
                }
            }
        });
*/

        viewHolder.category_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < addProductVendorServices.size(); i++) {
                    if (viewHolder.category_text.isChecked()) {
                        ServiceId = addProductVendorServices.get(position).getId();
                        // viewHolder.category_text.
                        // Toast.makeText(context, "Checked :" + categories[position], Toast.LENGTH_SHORT).show();
                        mSelectedItemsCatIds.put(position, true);
                    } else {
                        mSelectedItemsCatIds.delete(position);
                    }
                }
            }
        });
    }
/*
    public String getVendorShippingServices() {
        if(selectedContinents != null || !selectedContinents.toString().isEmpty()) {
            return selectedContinents.toString().replace("[", "").replace("]", "");
        }else{
            return "";
        }
    }
*/

    @Override
    public int getItemCount() {
        return addProductVendorServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox category_text;
        LinearLayout ll_categories;
        RadioGroup radioGroup;

        public ViewHolder(View view) {
            super(view);
            category_text = (CheckBox) view.findViewById(R.id.category_text);

        }
    }
}
