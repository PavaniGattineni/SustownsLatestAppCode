package com.sustowns.sustownsapp.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.Activities.AddProductActivity;
import com.sustowns.sustownsapp.Activities.EditTransportServices;
import com.sustowns.sustownsapp.Activities.FileUtils;
import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.ProductDetailsActivity;
import com.sustowns.sustownsapp.Activities.StoreMyProductsActivity;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Models.ImageModel;
import com.sustowns.sustownsapp.Models.MyProductsModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StoreMyProductsAdapter extends RecyclerView.Adapter<StoreMyProductsAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,user_id,user_role,pro_id,image;
    String prod_id,prod_status;
    PreferenceUtils preferenceUtils;
    String[] order;
    List<MyProductsModel> myProductsModels;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Helper helper;
    ImageView remove_product;
    EditText edit_price;
    Button save_price;

    public StoreMyProductsAdapter(StoreMyProductsActivity context, List<MyProductsModel> myProductsModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.myProductsModels = myProductsModels;
        preferenceUtils = new PreferenceUtils(context);
        helper = new Helper(context);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        //prod_id = preferenceUtils.getStringFromPreference(PreferenceUtils.STORE_PRO_ID,"");
        //  prod_status = preferenceUtils.getStringFromPreference(PreferenceUtils.STATUS,"");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_myproducts_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        prod_status = myProductsModels.get(position).getStatus();
        if(myProductsModels.get(position) != null){
            if(myProductsModels.get(position).getProd_image().isEmpty() || myProductsModels.get(position).getProd_image().equalsIgnoreCase(""))
            {
                Picasso.get()
                        .load(R.drawable.no_image_available)
                     //   .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.imageView);
            }else {
                Picasso.get()
                        .load(myProductsModels.get(position).getProd_image())
                      //  .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.imageView);
            }
            viewHolder.prod_name.setText(myProductsModels.get(position).getPr_title());
            viewHolder.prod_price.setText("INR "+myProductsModels.get(position).getPr_price());
            viewHolder.prod_quantity.setText(myProductsModels.get(position).getPr_min()+" "+myProductsModels.get(position).getWeight_unit());
            viewHolder.prod_status.setText(myProductsModels.get(position).getPr_sku());
        }
        viewHolder.update_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ((StoreMyProductsActivity)context).editProduct(position);
                Intent i = new Intent(context, AddProductActivity.class);
                i.putExtra("productid",myProductsModels.get(position).getId());
                i.putExtra("Update","1");
                i.putExtra("Name",myProductsModels.get(position).getPr_title());
                i.putExtra("CatId",myProductsModels.get(position).getCatid());
                i.putExtra("Quality",myProductsModels.get(position).getQuality());
                i.putExtra("EggsType",myProductsModels.get(position).getEgg_type());
                i.putExtra("PackType",myProductsModels.get(position).getPr_packtype());
                i.putExtra("Country",myProductsModels.get(position).getCountry());
                i.putExtra("State",myProductsModels.get(position).getState());
                i.putExtra("City",myProductsModels.get(position).getCity());
                i.putExtra("Price",myProductsModels.get(position).getPr_price());
                i.putExtra("Quantity",myProductsModels.get(position).getPr_min());
                i.putExtra("Discount",myProductsModels.get(position).getPr_discount());
                i.putExtra("Unit",myProductsModels.get(position).getPr_weight());
                i.putExtra("Stocks",myProductsModels.get(position).getStocks());
                i.putExtra("SampleUnit",myProductsModels.get(position).getSweight());
                i.putExtra("SampleMaxQuantity",myProductsModels.get(position).getSmaxquan());
                i.putExtra("SamplePrice",myProductsModels.get(position).getSprice());
                i.putExtra("Days",myProductsModels.get(position).getDays());
                i.putExtra("SampleGrossWeight",myProductsModels.get(position).getSgweight());
                i.putExtra("AddressStr",myProductsModels.get(position).getAddress());
                i.putExtra("StartDate",myProductsModels.get(position).getDate_started());
                i.putExtra("EndDate",myProductsModels.get(position).getDate_end());
                i.putExtra("SamplePackType",myProductsModels.get(position).getSpack_type());
                i.putExtra("Shipping",myProductsModels.get(position).getShippingStr());
                i.putExtra("MakeOffer",myProductsModels.get(position).getMakeoffer());
                i.putExtra("PrType",myProductsModels.get(position).getPr_type());
                i.putExtra("ListingType",myProductsModels.get(position).getListing_type());
                i.putExtra("WeightUnit",myProductsModels.get(position).getWeight_unit());
                i.putExtra("SampleWeightUnit",myProductsModels.get(position).getSweight_unit());
                i.putExtra("SampleGrossWeightUnit",myProductsModels.get(position).getSgweight_unit());
                i.putExtra("Zipcode",myProductsModels.get(position).getZipcode());
                i.putExtra("Image",myProductsModels.get(position).getProd_image());
                context.startActivity(i);
            }
        });
        viewHolder.remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prod_status = myProductsModels.get(position).getStatus();
                prod_id = myProductsModels.get(position).getId();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Are you sure You want to Delete the Product..?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                removeMyProduct(position);
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            }
        });
        viewHolder.copy_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prod_status = myProductsModels.get(position).getStatus();
                prod_id = myProductsModels.get(position).getId();
                copyMyProducts();
            }
        });
        viewHolder.ll_store_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_id = myProductsModels.get(position).getId();
                image = myProductsModels.get(position).getProd_image();
                Intent i = new Intent(context, ProductDetailsActivity.class);
                i.putExtra("Pro_Id",pro_id);
                i.putExtra("Image",image);
                i.putExtra("Status","2");
                i.putExtra("StoreMgmt","2");
                context.startActivity(i);
            }
        });
        viewHolder.edit_price_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.edit_price_dialog);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                remove_product = (ImageView) customdialog.findViewById(R.id.remove_product);
                edit_price = (EditText) customdialog.findViewById(R.id.edit_price);
                save_price = (Button) customdialog.findViewById(R.id.save_price);
                remove_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                save_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prod_id = myProductsModels.get(position).getId();
                        EditPrice();
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
    }
    public void removeAt(int position) {
        notifyDataSetChanged();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void EditPrice() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("productid",prod_id);
            jsonObj.put("pr_userid", user_id);
            jsonObj.put("pr_price", edit_price.getText().toString());
            androidNetworkingEdit(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingEdit(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Storemanagementser/editprice")
            .addJSONObjectBody(jsonObject) // posting java object
            .setTag("test")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", "JSON : " + response);
                    try {
                        String message = response.getString("message");
                        String success = response.getString("success");
                        if (success.equalsIgnoreCase("1")) {
                            Toast.makeText(context, "Price Updated Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent i = new Intent(context, StoreMyProductsActivity.class);
                            i.putExtra("Customizations","0");
                            context.startActivity(i);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onError(ANError error) {
                    Log.d("Error", "ANError : " + error);
                    Toast.makeText(context, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
    }
    public void removeMyProduct(final int position) {
        prod_status = myProductsModels.get(position).getStatus();
        prod_id = myProductsModels.get(position).getId();

        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.removeMyProducts("0",prod_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
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
                                try {
                                    root = new JSONObject(searchResponse);
                                    String message = root.getString("message");
                                    String success = root.getString("success");

                                    if (success.equalsIgnoreCase("1")) {
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        myProductsModels.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(context, "Product is not deleted", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        }
                    }
                } else {
                    // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
    public void copyMyProducts() {
        //   user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.copyMyProduct(prod_id,user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
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
                                try {
                                    root = new JSONObject(searchResponse);
                                    String message = root.getString("message");
                                    String success = root.getString("success");

                                    if (success.equalsIgnoreCase("1")) {
                                        Intent i = new Intent(context, StoreMyProductsActivity.class);
                                        i.putExtra("Customizations","0");
                                        context.startActivity(i);
                                        Toast.makeText(context, "Product Copied Successfully", Toast.LENGTH_SHORT).show();
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    } else {
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(context, "Product Not Copied", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }

                        }
                    }
                } else {
                    // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public int getItemCount() {
        return myProductsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,remove_product,edit_price_only;
        TextView prod_name,prod_quantity,prod_price,prod_status,copy_prod,update_prod;
        LinearLayout ll_store_product;
        public ViewHolder(View view) {
            super(view);
            prod_name = (TextView) view.findViewById(R.id.prod_name);
            prod_quantity = (TextView) view.findViewById(R.id.prod_quantity);
            prod_price = (TextView) view.findViewById(R.id.prod_price);
            prod_status = (TextView) view.findViewById(R.id.prod_status);
            imageView = (ImageView) view.findViewById(R.id.prod_image);
            remove_product = (ImageView) view.findViewById(R.id.remove_product);
            copy_prod = (TextView) view.findViewById(R.id.copy_prod);
            update_prod = (TextView) view.findViewById(R.id.update_prod);
            ll_store_product = (LinearLayout) view.findViewById(R.id.ll_store_product);
            edit_price_only = (ImageView) view.findViewById(R.id.edit_price_only);

        }
    }
}
