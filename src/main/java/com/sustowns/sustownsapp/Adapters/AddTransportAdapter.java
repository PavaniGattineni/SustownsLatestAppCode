package com.sustowns.sustownsapp.Adapters;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.AddTransportActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.OrderModel;
import com.sustowns.sustownsapp.Models.TransportServicesModel;

import java.util.ArrayList;
import java.util.List;

public class AddTransportAdapter extends RecyclerView.Adapter<AddTransportAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email, pro_id, user_id, user_role, order_status, order_id, pay_method,booking_status;
    PreferenceUtils preferenceUtils;
    List<TransportServicesModel> transportServiceList;
    ArrayList<OrderModel> orderModels;
    ProgressDialog progressDialog;
    Button close_dialog;
    int row_index = -1;
    TextView transport_vendor_name, transport_service_name, transport_distance, transport_type, vehicle_type, load_type, charge_per_km,
            minimum_charge, total_price,docs,fullcharge_per_km,fullminimum_charge,fulltotal_price;
    SparseBooleanArray mSelectedItem;
    Boolean isCancel;
    final String URL2,URL9;
    LinearLayout ll_full,ll_partial;

    public AddTransportAdapter(Context context, List<TransportServicesModel> transportServicesModels) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.transportServiceList = transportServicesModels;
        this.mSelectedItem = new SparseBooleanArray();
        this.isCancel = true;
        URL2 = "http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk";
        URL9 = "http://www.appsapk.com/downloading/latest/UC-Browser.apk";
    }

    @Override
    public AddTransportAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transport_service_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new AddTransportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddTransportAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.service_name.setText(transportServiceList.get(position).getService_name());
//      viewHolder.ratingBarProduct.setRating(Float.parseFloat(transportServiceList.get(position).getRating()));
        viewHolder.minimum_charge_Service.setText(transportServiceList.get(position).getPartial_minimum_charge());
        viewHolder.total_Price_service.setText(transportServiceList.get(position).getPartial_total_price());
        booking_status = transportServiceList.get(position).getTransport_booking_status();
        if(booking_status.equalsIgnoreCase("") && transportServiceList.get(position).getManual_automatic().equalsIgnoreCase("null")){
            viewHolder.book_service_btn.setVisibility(View.VISIBLE);
        }else if(booking_status.equalsIgnoreCase("0") && transportServiceList.get(position).getManual_automatic().equalsIgnoreCase("automatic")){
           viewHolder.text_status.setVisibility(View.VISIBLE);
            viewHolder.text_status.setText("Service Request Sent");
            viewHolder.book_service_btn.setVisibility(View.GONE);
        }else if(booking_status.equalsIgnoreCase("0") && transportServiceList.get(position).getManual_automatic().equalsIgnoreCase("manual")){
            viewHolder.book_service_btn.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.text_status.setVisibility(View.VISIBLE);
            viewHolder.text_status.setText("Process");
            viewHolder.book_service_btn.setVisibility(View.GONE);
        }
        viewHolder.viewservice_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create dialog
                final Dialog customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.transport_service_details_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                transport_vendor_name = (TextView) customdialog.findViewById(R.id.transport_vendor_name);
                transport_service_name = (TextView) customdialog.findViewById(R.id.transport_service_name);
                transport_distance = (TextView) customdialog.findViewById(R.id.transport_distance);
                transport_type = (TextView) customdialog.findViewById(R.id.transport_type);
                vehicle_type = (TextView) customdialog.findViewById(R.id.vehicle_type);
                load_type = (TextView) customdialog.findViewById(R.id.load_type);
                charge_per_km = (TextView) customdialog.findViewById(R.id.charge_per_km);
                minimum_charge = (TextView) customdialog.findViewById(R.id.minimum_charge);
                total_price = (TextView) customdialog.findViewById(R.id.total_price);
                fullcharge_per_km = (TextView) customdialog.findViewById(R.id.fullcharge_per_km);
                fullminimum_charge = (TextView) customdialog.findViewById(R.id.fullminimum_charge);
                fulltotal_price = (TextView) customdialog.findViewById(R.id.fulltotal_price);
                docs = (TextView) customdialog.findViewById(R.id.docs);
                close_dialog = (Button) customdialog.findViewById(R.id.close_dialog);
                ll_partial = (LinearLayout) customdialog.findViewById(R.id.ll_partial);
                ll_full = (LinearLayout) customdialog.findViewById(R.id.ll_full);
                transport_vendor_name.setText(transportServiceList.get(position).getTransport_vendor_name());
                transport_service_name.setText(transportServiceList.get(position).getService_name());
                transport_distance.setText(transportServiceList.get(position).getDistance());
                transport_type.setText(transportServiceList.get(position).getTransport_type());
                vehicle_type.setText(transportServiceList.get(position).getVehicle_type());
                load_type.setText(transportServiceList.get(position).getLoad_type());
                if(transportServiceList.get(position).getPartial_charge_perkm().isEmpty()||transportServiceList.get(position).getPartial_minimum_charge().isEmpty()||
                        transportServiceList.get(position).getPartial_total_price().isEmpty()){

                }else {
                    ll_partial.setVisibility(View.VISIBLE);
                    charge_per_km.setText(transportServiceList.get(position).getPartial_charge_perkm());
                    minimum_charge.setText(transportServiceList.get(position).getPartial_minimum_charge());
                    total_price.setText(transportServiceList.get(position).getPartial_total_price());
                }
                if(transportServiceList.get(position).getFull_charge_perkm().isEmpty()||transportServiceList.get(position).getFull_minimum_charge().isEmpty()||
                        transportServiceList.get(position).getFull_total_price().isEmpty()){

                }else{
                    ll_full.setVisibility(View.VISIBLE);
                    fullcharge_per_km.setText(transportServiceList.get(position).getFull_charge_perkm());
                    fullminimum_charge.setText(transportServiceList.get(position).getFull_minimum_charge());
                    fulltotal_price.setText(transportServiceList.get(position).getFull_total_price());
                }
                docs.setText(transportServiceList.get(position).getDocs());
                docs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri= Uri.parse(URL9);
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE);
                        Toast.makeText(context, "File Downloading...", Toast.LENGTH_SHORT).show();
// set title and description
                        request.setTitle("Data Download");
                        request.setDescription("Android Data download using DownloadManager.");

                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//set the local destination for download file to a path within the application's external files directory
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"downloadfileName");
                        request.setMimeType("*/*");
                        downloadManager.enqueue(request);
                    }
                });
                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        viewHolder.book_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((AddTransportActivity) context).sendRequestQuote(position, viewHolder);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return transportServiceList.size();
    }

    public void updateBookSuccess(int position, ViewHolder viewHolder) {
        if (viewHolder.book_service_btn.getText().toString().equalsIgnoreCase("Book Now")) {
            isCancel = false;
            mSelectedItem.put(position, true);
        } else {
            isCancel = true;
            mSelectedItem.delete(position);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView service_name, text_status, minimum_charge_Service, total_Price_service,cancelled_text;
        Button book_service_btn, cancel_booking_btn,pay_now,vehicle_details,viewservice_details;
        LinearLayout ll_pay_vehicle;
        RatingBar ratingBarProduct;

        public ViewHolder(View view) {
            super(view);
            service_name = (TextView) view.findViewById(R.id.service_name);
            ratingBarProduct = (RatingBar) view.findViewById(R.id.ratingBarProduct);
            minimum_charge_Service = (TextView) view.findViewById(R.id.minimum_charge_Service);
            total_Price_service = (TextView) view.findViewById(R.id.total_Price_service);
            book_service_btn = (Button) view.findViewById(R.id.book_service_btn);
            cancel_booking_btn = (Button) view.findViewById(R.id.cancel_booking_btn);
            cancelled_text = (TextView) view.findViewById(R.id.cancelled_text);
            ll_pay_vehicle = (LinearLayout) view.findViewById(R.id.ll_pay_vehicle);
            pay_now = (Button) view.findViewById(R.id.pay_now);
            vehicle_details = (Button) view.findViewById(R.id.vehicle_details);
            viewservice_details = (Button)view.findViewById(R.id.viewservice_details);
            text_status = (TextView) view.findViewById(R.id.text_status);

        }
    }
}
