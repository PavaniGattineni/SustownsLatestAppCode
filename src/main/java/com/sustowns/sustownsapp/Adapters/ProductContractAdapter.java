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
import com.sustowns.sustownsapp.Activities.MyProductContractActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.ReceivedContracts;
import com.sustowns.sustownsapp.Models.OpenRequestModel;

import java.util.ArrayList;

public class ProductContractAdapter extends RecyclerView.Adapter<ProductContractAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,Status,job_id,contractor_id;
    PreferenceUtils preferenceUtils;
    String[] contracts;
    ArrayList<OpenRequestModel> openRequestModels;
    ProgressDialog progressDialog;

    public ProductContractAdapter(MyProductContractActivity context, ArrayList<OpenRequestModel> openRequestModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.openRequestModels = openRequestModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_contract_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(openRequestModels.get(position) != null){
          /*  Glide.with(context)
                    .load(openRequestModels.get(position).getImage())
                    .into(viewHolder.bid_image);*/
            viewHolder.contract_name.setText("Contract Name : " +openRequestModels.get(position).getContractname());
            viewHolder.post_prod_name.setText(openRequestModels.get(position).getJob_name());
            viewHolder.post_quantity.setText(openRequestModels.get(position).getJob_location());
            viewHolder.post_validity.setText(openRequestModels.get(position).getEnd_date()+","+openRequestModels.get(position).getEtime());
            viewHolder.posted_on.setText(openRequestModels.get(position).getOn_date());
            viewHolder.category_post.setText(openRequestModels.get(position).getTitle());
            Status = openRequestModels.get(position).getStatus();
            if(Status.equalsIgnoreCase("0") || Status.equalsIgnoreCase("1")){
                viewHolder.received_contract_btn.setText("Received Contract");
            }else if(Status.equalsIgnoreCase("2")){
                viewHolder.received_contract_btn.setText("Approved Contract");
            }else if(Status.equalsIgnoreCase("3")){
                viewHolder.received_contract_btn.setText("Completed Contract");
            }else{
                viewHolder.received_contract_btn.setText("Process Contract");
            }
        //    viewHolder.bid_close_in.setText(openRequestModels.get(position).getEnd_date()+","+openRequestModels.get(position).getEtime());

        }
        viewHolder.received_contract_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = openRequestModels.get(position).getId();
                Intent intent = new Intent(context, ReceivedContracts.class);
                intent.putExtra("JobId",job_id);
                context.startActivity(intent);
            }
        });
        viewHolder.edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyProductContractActivity) context).editProduct(position);

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
        ImageView bid_image,edit_product;
        TextView contract_name,post_prod_name,post_quantity,post_validity,posted_on,category_post;
        Button received_contract_btn;

        public ViewHolder(View view) {
            super(view);
            bid_image = (ImageView) view.findViewById(R.id.bid_image);
            contract_name = (TextView) view.findViewById(R.id.contract_name);
            post_prod_name = (TextView) view.findViewById(R.id.post_prod_name);
            post_quantity = (TextView) view.findViewById(R.id.post_quantity);
            post_validity = (TextView) view.findViewById(R.id.post_validity);
            posted_on = (TextView) view.findViewById(R.id.posted_on);
            category_post = (TextView) view.findViewById(R.id.category_post);
            received_contract_btn = (Button) view.findViewById(R.id.received_contract_btn);
            edit_product = (ImageView) view.findViewById(R.id.edit_product);
          //  bid_close_in = (TextView) view.findViewById(R.id.bid_close_in);
          // quote_now_btn = (Button) view.findViewById(R.id.quote_now_btn);

        }
    }
}
