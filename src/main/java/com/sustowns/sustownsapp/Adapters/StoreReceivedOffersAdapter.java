package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.StoreOffersActivity;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Models.StoreSentOffersModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreReceivedOffersAdapter extends RecyclerView.Adapter<StoreReceivedOffersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,makeId,makeProId,makeStatus;
    PreferenceUtils preferenceUtils;
    String[] order;
    ArrayList<StoreSentOffersModel> storeSentOffersModels;
    ProgressDialog progressDialog;

    public StoreReceivedOffersAdapter(Context context, ArrayList<StoreSentOffersModel> storeSentOffersModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.storeSentOffersModels = storeSentOffersModels;
        preferenceUtils = new PreferenceUtils(context);
      /*  makeId = preferenceUtils.getStringFromPreference(PreferenceUtils.MAKE_ID,"");
        makeProId = preferenceUtils.getStringFromPreference(PreferenceUtils.OFF_PRO_ID,"");
        makeStatus = preferenceUtils.getStringFromPreference(PreferenceUtils.MAKE_STATUS,"");*/
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receive_offers_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(storeSentOffersModels.get(position) != null){
            viewHolder.prod_name.setText(storeSentOffersModels.get(position).getPr_title());
            viewHolder.offer_prod_price.setText("INR "+storeSentOffersModels.get(position).getPr_price());
            viewHolder.offer_price.setText(storeSentOffersModels.get(position).getMakepeice());
            viewHolder.offer_quantity.setText(storeSentOffersModels.get(position).getMakeqty());
            viewHolder.offered_by.setText(storeSentOffersModels.get(position).getFullname());
            makeStatus = storeSentOffersModels.get(position).getStatus();
            if(makeStatus.equalsIgnoreCase("1")){

            }else if(makeStatus.equalsIgnoreCase("2")){
                viewHolder.offer_accepted.setVisibility(View.VISIBLE);
                viewHolder.accept_offer_img.setVisibility(View.GONE);
                viewHolder.remove_offer.setVisibility(View.GONE);
            }else if(makeStatus.equalsIgnoreCase("0")){
                viewHolder.accept_offer_img.setVisibility(View.GONE);
                viewHolder.remove_offer.setVisibility(View.GONE);
                viewHolder.offer_accepted.setVisibility(View.VISIBLE);
                viewHolder.offer_accepted.setText("Offer Rejected");
            }
        }
        viewHolder.accept_offer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeId = storeSentOffersModels.get(position).getId();
                makeProId = storeSentOffersModels.get(position).getProd_id();
                acceptMakeOffer();
            }
        });
        viewHolder.remove_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeId = storeSentOffersModels.get(position).getId();
               // makeStatus = storeSentOffersModels.get(position).getStatus();
                storeSentOffersModels.remove(position);
                removeOffer(position);
                removeAt(position);
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
    public void acceptMakeOffer() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.acceptMakeOffer(makeId,makeProId,"2");
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
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
                                    if (success.equals("1")) {
                                        progressDialog.dismiss();
                                         Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        ((StoreOffersActivity)context).receivedOffersList();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
    public void removeOffer(final int position) {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.deleteOffer(makeId,"0");
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
                                   // String message = root.getString("message");
                                    String success = root.getString("success");

                                    if (success.equals("1")) {
                                        progressDialog.dismiss();
                                        //((CartActivity) context).getCartTotals();
                                        Toast.makeText(context, "successfully removed", Toast.LENGTH_SHORT).show();
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, storeSentOffersModels.size());
                                        notifyDataSetChanged();
                                        ((StoreOffersActivity)context).receivedOffersList();
                                        // editor.putBoolean("loginstatus", true);
                                        // editor.putString("cart_id", cart_id);
                                        // editor.commit();
                                        // Toast.makeText(context, message_cart, Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }

                        }
                    }
                } else {
                    progressDialog.dismiss();
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
        return storeSentOffersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView remove_offer,accept_offer_img;
        TextView prod_name,offer_prod_price,offer_quantity,offer_price,offered_by,offer_accepted;
        Button offer_btn;
        public ViewHolder(View view) {
            super(view);
            prod_name = (TextView) view.findViewById(R.id.prod_name);
            offer_prod_price = (TextView) view.findViewById(R.id.offer_prod_price);
            offer_quantity = (TextView) view.findViewById(R.id.offer_quantity);
            offer_price = (TextView) view.findViewById(R.id.offer_price);
            offered_by = (TextView) view.findViewById(R.id.offered_by);
            remove_offer = (ImageView) view.findViewById(R.id.remove_offer);
            accept_offer_img = (ImageView) view.findViewById(R.id.accept_offer_img);
            offer_accepted = (TextView) view.findViewById(R.id.offer_accepted);
        }
    }
}
