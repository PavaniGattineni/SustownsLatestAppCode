package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.R;

public class ClusterWarehouseAdapter extends RecyclerView.Adapter<ClusterWarehouseAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String[] Name,width,height,location;
   // ArrayList<CartListModel> cartListModels;
    ProgressDialog progressDialog;

    public ClusterWarehouseAdapter(Context context, String[] Name, String[] width, String[] height, String[] location) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.Name = Name;
        this.width = width;
        this.height = height;
        this.location = location;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cluster_warehouse_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.name_warehouse.setText(Name[position]);
        viewHolder.width_warehouse.setText(width[position]);
        viewHolder.height_warehouse.setText(height[position]);
        viewHolder.location_warehouse.setText(location[position]);
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return Name.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_warehouse, width_warehouse, height_warehouse, location_warehouse;
        public ViewHolder(View view) {
            super(view);
            name_warehouse = (TextView) view.findViewById(R.id.name_warehouse);
            width_warehouse = (TextView) view.findViewById(R.id.width_warehouse);
            height_warehouse = (TextView) view.findViewById(R.id.height_warehouse);
            location_warehouse = (TextView) view.findViewById(R.id.location_warehouse);
        }
    }
}
