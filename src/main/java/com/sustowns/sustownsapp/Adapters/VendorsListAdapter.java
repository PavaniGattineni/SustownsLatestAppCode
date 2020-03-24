package com.sustowns.sustownsapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.Activities.CartActivity;
import com.sustowns.sustownsapp.Activities.ClusterVendorsListActivity;
import com.sustowns.sustownsapp.Api.CartApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.VendorsListModel;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Activities.AddPaymentActivity;
import com.sustowns.sustownsapp.Activities.ContractOrdersInvoice;
import com.sustowns.sustownsapp.Activities.LocationDialogActivity;
import com.sustowns.sustownsapp.Activities.MyContractOrdersActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.ContractPurchasesModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VendorsListAdapter extends RecyclerView.Adapter<VendorsListAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<VendorsListModel> vendorsListModels;
    String pro_id,image,userid,vendorId;
    PreferenceUtils preferenceUtils;
    Helper helper;
    ProgressDialog progressDialog;

    public VendorsListAdapter(Context context,ArrayList<VendorsListModel> vendorsListModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.vendorsListModels = vendorsListModels;
        helper = new Helper(context);
        preferenceUtils = new PreferenceUtils(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendor_list_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(vendorsListModels.get(position) != null){
            viewHolder.userName.setText(vendorsListModels.get(position).getUsername());
            viewHolder.emailText.setText(vendorsListModels.get(position).getEmail());
            viewHolder.phoneText.setText(vendorsListModels.get(position).getPhone());
            viewHolder.countryText.setText(vendorsListModels.get(position).getCountry());
            viewHolder.dateText.setText(vendorsListModels.get(position).getOn_date());
            viewHolder.vendor_type.setText(vendorsListModels.get(position).getAccess_token());
        }else{
            viewHolder.userName.setText("");
            viewHolder.emailText.setText("");
            viewHolder.phoneText.setText("");
            viewHolder.countryText.setText("");
            viewHolder.dateText.setText("");
            viewHolder.vendor_type.setText("");
        }
        viewHolder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorId = vendorsListModels.get(position).getUnique_id();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to remove from your list?",
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            removeVendorItem(position);
                        }
                    }, new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }); 
            }
        });
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void removeVendorItem(final int position) {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        CartApi service = retrofit.create(CartApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.removeShippingItem("https://www.sustowns.com/Clusterservices/remove_vendor/?uniqueid="+vendorId);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    // progressDialog.dismiss();
                    System.out.println("----------------------------------------------------");
                    Log.d("Call request", call.request().toString());
                    Log.d("Call request header", call.request().headers().toString());
                    Log.d("Response raw header", response.headers().toString());
                    Log.d("Response raw", String.valueOf(response.raw().body()));
                    Log.d("Response code", String.valueOf(response.code()));
                    System.out.println("----------------------------------------------------");
                    if (response.body().toString() != null) {
                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Categeries", "response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                Integer status_cart_remove;
                                String message_cart_remove;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equals("1")) {
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, vendorsListModels.size());
                                        notifyDataSetChanged();
                                        Intent i = new Intent(context, ClusterVendorsListActivity.class);
                                        context.startActivity(i);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }

                        }
                    }
                } else {
                    //progressDialog.dismiss();
                    // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return vendorsListModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, emailText, phoneText, countryText,dateText,vendor_type,remove_item;
        public ViewHolder(View view) {
            super(view);
             userName = (TextView) view.findViewById(R.id.username_text);
             emailText = (TextView) view.findViewById(R.id.email_text);
             phoneText = (TextView) view.findViewById(R.id.phone_text);
             countryText = (TextView) view.findViewById(R.id.country_text);
             dateText = (TextView) view.findViewById(R.id.date_text);
            vendor_type = (TextView) view.findViewById(R.id.vendor_type);
            remove_item = (TextView) view.findViewById(R.id.remove_item);
        }
    }

}
