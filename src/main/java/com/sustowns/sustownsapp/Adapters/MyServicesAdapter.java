package com.sustowns.sustownsapp.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.AddShippingServices;
import com.sustowns.sustownsapp.Activities.EditTransportServices;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.TransportGetService;

import java.util.ArrayList;

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email, user_id;
    PreferenceUtils preferenceUtils;
    String[] service;
    ArrayList<TransportGetService> transportGetServices;
    ProgressDialog progressDialog;
    TextView transport_service_name, category_name, transport_type, vehicle_type, load_type, from_date, to_date,
            crate_min_charge, box_charge_km, box_text, box_min_charge;
    Button close_dialog;
    String StoreMgmt;

    public MyServicesAdapter(Context context, ArrayList<TransportGetService> transportGetServices,String StoreMgmt) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.StoreMgmt = StoreMgmt;
        this.transportGetServices = transportGetServices;
    }

    @Override
    public MyServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myservices_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new MyServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyServicesAdapter.ViewHolder viewHolder, final int position) {

        if (transportGetServices.get(position) != null) {
            viewHolder.ser_prod_name.setText(transportGetServices.get(position).getPr_title());
            viewHolder.ser_category.setText(transportGetServices.get(position).getCategory());
            viewHolder.ser_transport_type.setText(transportGetServices.get(position).getTransport_type());
            viewHolder.ser_prod_price.setText(transportGetServices.get(position).getVehicle_type());

        }
        viewHolder.update_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StoreMgmt.equalsIgnoreCase("0")){
                    Intent i = new Intent(context, EditTransportServices.class);
                    i.putExtra("ServiceId",transportGetServices.get(position).getId());
                    i.putExtra("Update","1");
                    i.putExtra("Name",transportGetServices.get(position).getPr_title());
                    i.putExtra("TranportType",transportGetServices.get(position).getTransport_type());
                    i.putExtra("VehicleType",transportGetServices.get(position).getVehicle_type());
                    i.putExtra("FromDate",transportGetServices.get(position).getFromdate());
                    i.putExtra("ToDate",transportGetServices.get(position).getTodate());
                    i.putExtra("Category",transportGetServices.get(position).getCategory());
                    i.putExtra("CatId",transportGetServices.get(position).getCategoryid());
                    i.putExtra("VehicleIdNumber",transportGetServices.get(position).getVehicle_id());
                    i.putExtra("SubCategory",transportGetServices.get(position).getSubcategory());
                    i.putExtra("SubCatId",transportGetServices.get(position).getPr_subcatid());
                    i.putExtra("VehicleId",transportGetServices.get(position).getVehicleid());
                    i.putExtra("TransId",transportGetServices.get(position).getTransid());
                    i.putExtra("MinLoad",transportGetServices.get(position).getMinimumload());
                    i.putExtra("MaxLoad",transportGetServices.get(position).getMaximumload());
                    i.putExtra("ContainerType",transportGetServices.get(position).getContainertype());
                    i.putExtra("Length",transportGetServices.get(position).getVehicle_length());
                    i.putExtra("Width",transportGetServices.get(position).getVehicle_width());
                    i.putExtra("Height",transportGetServices.get(position).getVehicle_height());
                    i.putExtra("TransportTax",transportGetServices.get(position).getTransport_tax());
                    i.putExtra("TransportDis",transportGetServices.get(position).getTransport_dis());
                    i.putExtra("ServiceRadius",transportGetServices.get(position).getService_area_radius());
                    i.putExtra("ServiceExtendRadius",transportGetServices.get(position).getRadi_exp());
                    i.putExtra("PointSourceLoc",transportGetServices.get(position).getPoint_source_locaiton());
                    i.putExtra("PointDesLoc",transportGetServices.get(position).getPoint_des_location());
                    i.putExtra("LoadType",transportGetServices.get(position).getLoad_type());
                    context.startActivity(i);
                   // ((ServiceManagementActivity) context).editProduct(position);
                }
                else if(StoreMgmt.equalsIgnoreCase("1")){
                    Intent i = new Intent(context,AddShippingServices.class);
                    i.putExtra("ServiceId",transportGetServices.get(position).getId());
                    i.putExtra("Update","1");
                    i.putExtra("Name",transportGetServices.get(position).getPr_title());
                    i.putExtra("TranportType",transportGetServices.get(position).getTransport_type());
                    i.putExtra("VehicleType",transportGetServices.get(position).getVehicle_type());
                    i.putExtra("FromDate",transportGetServices.get(position).getFromdate());
                    i.putExtra("ToDate",transportGetServices.get(position).getTodate());
                    i.putExtra("Category",transportGetServices.get(position).getCategory());
                    i.putExtra("CatId",transportGetServices.get(position).getCategoryid());
                    i.putExtra("VehicleIdNumber",transportGetServices.get(position).getVehicle_id());
                    i.putExtra("SubCategory",transportGetServices.get(position).getSubcategory());
                    i.putExtra("SubCatId",transportGetServices.get(position).getPr_subcatid());
                    i.putExtra("VehicleId",transportGetServices.get(position).getVehicleid());
                    i.putExtra("TransId",transportGetServices.get(position).getTransid());
                    i.putExtra("MinLoad",transportGetServices.get(position).getMinimumload());
                    i.putExtra("MaxLoad",transportGetServices.get(position).getMaximumload());
                    i.putExtra("ContainerType",transportGetServices.get(position).getContainertype());
                    i.putExtra("Length",transportGetServices.get(position).getVehicle_length());
                    i.putExtra("Width",transportGetServices.get(position).getVehicle_width());
                    i.putExtra("Height",transportGetServices.get(position).getVehicle_height());
                    i.putExtra("TransportTax",transportGetServices.get(position).getTransport_tax());
                    i.putExtra("TransportDis",transportGetServices.get(position).getTransport_dis());
                    i.putExtra("ServiceRadius",transportGetServices.get(position).getService_area_radius());
                    i.putExtra("ServiceExtendRadius",transportGetServices.get(position).getRadi_exp());
                    i.putExtra("PointSourceLoc",transportGetServices.get(position).getPoint_source_locaiton());
                    i.putExtra("PointDesLoc",transportGetServices.get(position).getPoint_des_location());
                    i.putExtra("LoadType",transportGetServices.get(position).getLoad_type());
                    context.startActivity(i);
                    //((AddShippingServices) context).editProductShipping(position);
                }
            }
        });
        viewHolder.view_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create dialog for View Service Details
                final Dialog customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.get_service_details);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                transport_service_name = (TextView) customdialog.findViewById(R.id.transport_service_name);
                category_name = (TextView) customdialog.findViewById(R.id.category_name);
                transport_type = (TextView) customdialog.findViewById(R.id.transport_type);
                vehicle_type = (TextView) customdialog.findViewById(R.id.vehicle_type);
                load_type = (TextView) customdialog.findViewById(R.id.load_type);
                from_date = (TextView) customdialog.findViewById(R.id.from_date);
                to_date = (TextView) customdialog.findViewById(R.id.to_date);

                transport_service_name.setText(transportGetServices.get(position).getPr_title());
                category_name.setText(transportGetServices.get(position).getCategory());
                transport_type.setText(transportGetServices.get(position).getTransport_type());
                vehicle_type.setText(transportGetServices.get(position).getVehicle_type());
                load_type.setText(transportGetServices.get(position).getLoad_type());
                from_date.setText(transportGetServices.get(position).getFromdate());
                to_date.setText(transportGetServices.get(position).getTodate());
                close_dialog = (Button) customdialog.findViewById(R.id.close_dialog);
                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportGetServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ser_prod_name, ser_prod_price, ser_category, ser_transport_type,update_service,view_service;

        public ViewHolder(View view) {
            super(view);
            ser_prod_name = (TextView) view.findViewById(R.id.ser_prod_name);
            ser_prod_price = (TextView) view.findViewById(R.id.ser_prod_price);
            ser_category = (TextView) view.findViewById(R.id.ser_category);
            ser_transport_type = (TextView) view.findViewById(R.id.ser_transport_type);
            update_service = (TextView) view.findViewById(R.id.update_service);
            view_service = (TextView) view.findViewById(R.id.view_service);
        }
    }
}

