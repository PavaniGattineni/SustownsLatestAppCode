package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.MyServiceContractActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;

public class ServiceContractAdapter extends RecyclerView.Adapter<ServiceContractAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String[] contracts;
   // ArrayList<CartListModel> cartListModels;
    ProgressDialog progressDialog;


    public ServiceContractAdapter(MyServiceContractActivity context, String[] contracts) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contracts = contracts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_contract_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return contracts.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,decrease,increase;
        TextView name,dollar,quantity_tv,remove,update;
        Button addtocart;
        EditText quantity_edit;

        public ViewHolder(View view) {
            super(view);

        }
    }
}
