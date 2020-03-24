package com.sustowns.sustownsapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.CartActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Api.CartApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Models.AddToCartModel;
import com.sustowns.sustownsapp.Models.CartServerModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    String[] names;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,CartId,cart_id;
    PreferenceUtils preferenceUtils;
    public static String QuantityStr;
    public static String quantity;
    ArrayList<AddToCartModel> addToCartModels;
    List<CartServerModel> cartServerModelList;
    ProgressDialog progressDialog;
    Helper helper;

    public CartAdapter(Context context, List<CartServerModel> cartServerModelList) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.cartServerModelList = cartServerModelList;
        helper = new Helper(context);
        preferenceUtils = new PreferenceUtils(context);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.quantity_edit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                viewHolder.quantity_edit.setFocusableInTouchMode(true);
                viewHolder.quantity_edit.requestFocus();
                return false;
            }
        });
        viewHolder.quantity_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                quantity = viewHolder.quantity_edit.getText().toString().trim();
            CartId = cartServerModelList.get(position).getCart_id();
            }
        });
        if(cartServerModelList.get(position) != null){
            Picasso.get().load(cartServerModelList.get(position).getPro_img())
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(viewHolder.imageView);

            viewHolder.name.setText(cartServerModelList.get(position).getName());
            viewHolder.quantity_edit.setText(cartServerModelList.get(position).getQty());
            viewHolder.quantity_text.setText(cartServerModelList.get(position).getQty());
            viewHolder.dollar.setText("Price : INR "+cartServerModelList.get(position).getPrice_qty());
          /*  if(user_id.equalsIgnoreCase(cartServerModelList.get(position).getPr_userid())){
                if(cartServerModelList.get(position).getDiscount().equalsIgnoreCase("")||cartServerModelList.get(position).getDiscount().equalsIgnoreCase("null")){
                    viewHolder.prod_price1.setVisibility(View.GONE);
                    viewHolder.dollar.setText("Price : INR "+cartServerModelList.get(position).getPrice_qty());
                }else {
                    int OriginalPrice = Integer.parseInt(cartServerModelList.get(position).getMrp());
                    int DiscountStr = Integer.parseInt(cartServerModelList.get(position).getDiscount());
                    int DiscountPrice = OriginalPrice * DiscountStr;
                    float DisPriceStr = Float.parseFloat(String.valueOf(DiscountPrice)) / 100;
                    float TotalPriceStr = Float.parseFloat(cartServerModelList.get(position).getMrp()) - DisPriceStr;
                    float PriceQty = Float.valueOf(cartServerModelList.get(position).getQty())*TotalPriceStr;
                    String Price = new DecimalFormat("##.##").format(PriceQty);
                    viewHolder.dollar.setText("Price : INR " +Price);
                }
            }else{
                    if (cartServerModelList.get(position).getDiscount().equalsIgnoreCase("") || cartServerModelList.get(position).getDiscount().equalsIgnoreCase("null")) {
                        viewHolder.prod_price1.setVisibility(View.GONE);
                        if(cartServerModelList.get(position).getProductser_chargeAmount().equalsIgnoreCase("0") || cartServerModelList.get(position).getProductser_chargeAmount().equalsIgnoreCase("")) {
                            viewHolder.dollar.setText("Price : INR " + cartServerModelList.get(position).getPrice_qty());
                        }else{
                            Float ProdPriceF = Float.valueOf(cartServerModelList.get(position).getMrp());
                            Float ServiceChargeF = Float.valueOf(cartServerModelList.get(position).getProductser_chargeAmount());
                            Float finalServiceCharge = (ProdPriceF * ServiceChargeF) / 100;
                            Float totalProdPriceF = Float.valueOf(cartServerModelList.get(position).getMrp());
                            float SerChargeFinal = totalProdPriceF + finalServiceCharge;
                            float TotalPrice = Float.valueOf(cartServerModelList.get(position).getQty())*SerChargeFinal;
                            String TotalProdPrice = new DecimalFormat("##.##").format(TotalPrice);
                            viewHolder.dollar.setText("Price : INR " + TotalProdPrice);
                        }
                    } else {
                        if (cartServerModelList.get(position).getProductser_chargeAmount().equalsIgnoreCase("0") || cartServerModelList.get(position).getProductser_chargeAmount().equalsIgnoreCase("")) {
                            int OriginalPrice = Integer.parseInt(cartServerModelList.get(position).getMrp());
                            int DiscountStr = Integer.parseInt(cartServerModelList.get(position).getDiscount());
                            int DiscountPrice = OriginalPrice * DiscountStr;
                            float DisPriceStr = Float.parseFloat(String.valueOf(DiscountPrice)) / 100;
                            float TotalPriceStr = Float.parseFloat(cartServerModelList.get(position).getMrp()) - DisPriceStr;
                            float quantityInt = Float.parseFloat(cartServerModelList.get(position).getQty());
                            float ProdPriceStrFloat = quantityInt * TotalPriceStr;
                            String Price = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                            viewHolder.dollar.setText("Price : INR " + Price);
                        } else {
                            int OriginalPrice = Integer.parseInt(cartServerModelList.get(position).getMrp());
                            int DiscountStr = Integer.parseInt(cartServerModelList.get(position).getDiscount());
                            int DiscountPrice = OriginalPrice * DiscountStr;
                            float DisPriceStr = Float.parseFloat(String.valueOf(DiscountPrice)) / 100;
                            float TotalPriceStr = Float.parseFloat(cartServerModelList.get(position).getMrp()) - DisPriceStr;
                            float SerChargeFinal = TotalPriceStr + Float.valueOf(cartServerModelList.get(position).getProductser_chargeAmount());
                            float quantityInt = Float.parseFloat(cartServerModelList.get(position).getQty());
                            float ProdPriceStrFloat = quantityInt * SerChargeFinal;
                            String Price = new DecimalFormat("##.##").format(ProdPriceStrFloat);
                            viewHolder.dollar.setText("Price : INR " + Price);
                        }
                    }
            }*/
            viewHolder.cart_prod_code.setText("Product Code : "+cartServerModelList.get(position).getPro_code());
            //    if(cartServerModelList.get(position).getShipping_type().equalsIgnoreCase("1")){
            if(cartServerModelList.get(position).getShiping_amount().equalsIgnoreCase("null")){
                viewHolder.shipping_cart_layout.setVisibility(View.GONE);
                viewHolder.view_cart.setVisibility(View.GONE);
                viewHolder.quantity_text.setVisibility(View.GONE);
                viewHolder.quantity_edit.setVisibility(View.VISIBLE);
                viewHolder.update.setVisibility(View.VISIBLE);
            } else {
                viewHolder.shipping_cart_layout.setVisibility(View.VISIBLE);
                viewHolder.view_cart.setVisibility(View.VISIBLE);
                viewHolder.quantity_text.setVisibility(View.VISIBLE);
                viewHolder.quantity_edit.setVisibility(View.GONE);
                viewHolder.update.setVisibility(View.GONE);
                Picasso.get().load(R.drawable.shipping_image)
                        .placeholder(R.drawable.shipping_image)
                        .error(R.drawable.shipping_image)
                        .into(viewHolder.shipping_image);
                viewHolder.shipping_name.setText(cartServerModelList.get(position).getName());
                viewHolder.shipping_price.setText("Amount : INR "+cartServerModelList.get(position).getShiping_amount());
                viewHolder.ship_prod_code.setText("Product Code : "+cartServerModelList.get(position).getShiping_id());
            }
        }
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //helper.showLoader(context, "Removing..", "Please wait for a while");
                cart_id = cartServerModelList.get(position).getCart_id();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to remove the product?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                removeCartItem(position);
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                //((CartActivity) context).removeItemServer(position);
//                ((CartActivity)context).removeFromCart(cartServerModelList.get(position).getCart_id(), position);
            }
        });
        viewHolder.remove_shipping_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //helper.showLoader(context, "Removing..", "Please wait for a while");
                cart_id = cartServerModelList.get(position).getShiping_id();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to remove the shipping of product?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                removeShippingItem();
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                // removeShippingItem();
               // ((CartActivity) context).removeShippingItemServer(position);
            }
        });
        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Qunatity = cartServerModelList.get(position).getPr_min();
                String Stock = cartServerModelList.get(position).getPr_stocks();
                Integer quantity_int = Integer.parseInt(Qunatity);
                Integer MinPrice = Integer.parseInt(quantity);
                Integer MinStock = Integer.parseInt(Stock);
                if(quantity_int > MinPrice){
                    Toast.makeText(context, "Value must be greater than or equals to"+quantity_int, Toast.LENGTH_SHORT).show();
                }else if(MinStock > MinPrice) {
                    helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to Update the Quantity?",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    setJsonObject();
                                }
                            }, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                }
                else{
                    Toast.makeText(context, "Value must be less than or equals to"+MinStock, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void setJsonObject() {
        try {
            JSONObject useridObj = new JSONObject();
            useridObj.put("userid",user_id);

            JSONArray quantityArray = new JSONArray();
            for (CartServerModel cartServerModel : cartServerModelList) {

                JSONObject quantityObject = new JSONObject();
               /* quantityObject.put("cart_id","763");
                quantityObject.put("qty","15");*/
                if(CartId.equalsIgnoreCase(cartServerModel.getCart_id())) {
                    quantityObject.put("cart_id", cartServerModel.getCart_id());
                    quantityObject.put("qty", quantity);
                }else{

                }
                quantityArray.put(quantityObject);
            }
            useridObj.put("quantityArray",quantityArray);
            Log.e("ProductDetailsArray", ""+useridObj);

            androidNetworking(useridObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworking(JSONObject jsonObject) {
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/updatecart")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response" , "JSON : "+response);
                        try {
                            String message = response.getString("message");
                            String success = response.getString("success");
                            if(success.equalsIgnoreCase("1")){
                                ((CartActivity)context).getCartListItems();
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error" , "ANError : "+error);
                    }
                });

    }
    public void removeShippingItem() {
        //  row_id = preferenceUtils.getStringFromPreference(PreferenceUtils.RowIdCart,"");
        //helper.showLoader(context, "Removing..", "Please wait for a while");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartApi service = retrofit.create(CartApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.removeShippingItem("https://www.sustowns.com/Sustownsservice/remove_shipcart/?cart_id="+cart_id);
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
                                Integer status_cart_remove;
                                String message_cart_remove;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equals("1")) {
                                        Intent i = new Intent(context, CartActivity.class);
                                        context.startActivity(i);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        // ((CartActivity) context).getCartListItems();
                                       /* notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, cartServerModelList.size());
                                        notifyDataSetChanged();*/
                                        // editor.putBoolean("loginstatus", true);
                                        // editor.putString("cart_id", cart_id);
                                        // editor.commit();
                                        // Toast.makeText(context, message_cart, Toast.LENGTH_SHORT).show();
                                        //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    } else {
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
                    progressDialog.dismiss();
                    // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void removeCartItem(final int position) {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CartApi service = retrofit.create(CartApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.removeShippingItem("https://www.sustowns.com/Sustownsservice/remove_cartforsingle?cart_id="+cart_id);
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
                                        ((CartActivity) context).cartCount();
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, cartServerModelList.size());
                                        notifyDataSetChanged();
                                        Intent i = new Intent(context, CartActivity.class);
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
        return cartServerModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,shipping_image;
        TextView name,dollar,ship_prod_code,prod_price1,remove,update,shipping_name, shipping_price,remove_shipping_item,quantity_text,cart_prod_code;
        LinearLayout shipping_cart_layout;
        View view_cart;
        public EditText quantity_edit;
        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.cart_image);
            name = (TextView) view.findViewById(R.id.name_cart);
            dollar = (TextView) view.findViewById(R.id.dollars_cart);
            quantity_edit = (EditText) view.findViewById(R.id.quantity_edit);
            remove = (TextView) view.findViewById(R.id.remove_item);
            update = (TextView) view.findViewById(R.id.update_item);
            name = (TextView) view.findViewById(R.id.name_cart);

            shipping_cart_layout = view.findViewById(R.id.shipping_cart_layout);
            shipping_image = (ImageView) view.findViewById(R.id.shipping_image);
            shipping_name = (TextView) view.findViewById(R.id.shipping_name);
            ship_prod_code = (TextView) view.findViewById(R.id.ship_prod_code);
            shipping_price = view.findViewById(R.id.shipping_price);
            view_cart = view.findViewById(R.id.view);
            remove_shipping_item = (TextView) view.findViewById(R.id.remove_shipping_item);
            quantity_text = (TextView) view.findViewById(R.id.quantity_text);
            cart_prod_code = (TextView) view.findViewById(R.id.cart_prod_code);
            prod_price1 = (TextView) view.findViewById(R.id.prod_price_discount);
           /* name = (TextView) view.findViewById(R.id.name_cart);*/
        }
    }
}
