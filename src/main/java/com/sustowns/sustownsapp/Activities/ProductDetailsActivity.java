package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Adapters.GetReviewsAdapter;
import com.sustowns.sustownsapp.Api.CartApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.GetReviewsModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ProductDetailsActivity extends AppCompatActivity {
    public static String Address = "";
    Button reviews, descriptions, addtocart, make_offer, buy_sample, submit_review_btn;
    LinearLayout ll_description, ll_reviews, ll_product_details,ll_shipping_services,ll_discount;
    ImageView backarrow, image_product_details, image_full;
    EditText name_review, edit_quantity;
    String pro_id,status,user_id, image, quantity,service_charge, pr_title,userid, pr_price, pr_currency, makeoffer, pr_weight, weight_unit, review, sample_pro_id;
    Intent intent;
    Spinner spinner_shipping_services,spinner_thirdparty_shipping;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    TextView product_name, prod_price, prod_price1,prod_details_unit, packing_type, min_quantity, availability_stock, vendor_product_details;
    TextView delivery_time, reviews_text, reviews_count, product_size, vendor_discount,added_tocart,cart_count;
    public static TextView drop_location;
    Realm realm;
    ImageView close_buy_sample, cart_img;
    Button close_btn, addtocart_sample;
    TextView buysample_title, sample_name, sample_unit, sample_size, sample_pack_type, sample_price, product_price, pick_up_location;
    EditText quantity_et;
    String sid,ratingStr, shippingStr, discount, transportStr,shipping="",addressDrop,cartcount,getpricekms="";
    String weight_sample, weight_unit_sample, pack_type_sample, pr_title_sample, pr_price_sample, pr_currency_sample, pr_min_sample;
    RecyclerView review_recyclerview;
    ArrayList<GetReviewsModel> getReviewsModels;
    GetReviewsAdapter getReviewsAdapter;
    Button change_address_btn;
    LinearLayout ll_vendor_link;
    String[] transport = {"Select Shipping Type","Third Party Shipping","Vendor Shipping"};
    String[] transport1 = {"Select Shipping Type","Third Party Shipping"};
    String[] transport2 = {"Vendor Shipping","Third Party Shipping"};
    RatingBar ratingBarProduct, ratingBarSubmit;
    LinearLayout ll_select_shipping_services, ll_existing_address,ll_buysample;
    String[] address = {"Sustowns,jntu,india,Andhra Pradesh,Guntur,500072", "way web,saurashtra,university road,rajkot,india,Telangana,500018"};
    String[] name = {"Address1", "Address2"};
    TextView shipping_charge,spinner_countrydialog,address_state, address_town;
    Helper helper;
    String product_image_path,ServiceStr = "",vendorServiceIdStr = "",PincodeAddress,Activity,zipcode_drop,zipcode,product_zipcode;
    LinearLayout ll_vendor_shipping1;
    ArrayList<String> vendor_service_ids;
    ArrayList<String> vendor_service_names;
    Dialog customdialog;
    String ProdPriceStr, ShippingValue = "2";
    RelativeLayout relativelayout;
    AlertDialog alertDialog;
    float TotalPriceStr,SerChargeFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_details1);
        try {
            initializeValues();
            initializeUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // for refreshing data in activity
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    private void initializeValues() {
        realm = Realm.getDefaultInstance();
        preferenceUtils = new PreferenceUtils(ProductDetailsActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        intent = getIntent();
        helper = new Helper(ProductDetailsActivity.this);
        pro_id = intent.getStringExtra("Pro_Id");
        image = intent.getStringExtra("Image");
        status = intent.getStringExtra("Status");
        Activity = intent.getStringExtra("StoreMgmt");
    }
    private void initializeUI() {
        review_recyclerview = (RecyclerView) findViewById(R.id.review_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        review_recyclerview.setLayoutManager(layoutManager);
        ll_vendor_shipping1 = (LinearLayout) findViewById(R.id.ll_vendor_shipping1);
        make_offer = (Button) findViewById(R.id.make_offer);
        buy_sample = (Button) findViewById(R.id.buy_sample);
        reviews_text = (TextView) findViewById(R.id.reviews);
        ll_description = (LinearLayout) findViewById(R.id.ll_product_description);
        ll_reviews = (LinearLayout) findViewById(R.id.ll_product_reviews);
        reviews = (Button) findViewById(R.id.reviews_btn);
        descriptions = (Button) findViewById(R.id.descriptions_btn);
        ll_product_details = (LinearLayout) findViewById(R.id.ll_product_details);
        ll_vendor_link = (LinearLayout) findViewById(R.id.ll_vendor_link);
        ll_shipping_services = (LinearLayout) findViewById(R.id.ll_shipping_services);
        ratingBarProduct = (RatingBar) findViewById(R.id.ratingBarProduct);
        reviews_count = (TextView) findViewById(R.id.reviews_count);
        ratingBarSubmit = (RatingBar) findViewById(R.id.ratingBarSubmit);
        product_size = (TextView) findViewById(R.id.product_size);
        vendor_discount = (TextView) findViewById(R.id.vendor_discount);
        ll_discount = (LinearLayout) findViewById(R.id.ll_discount);
        pick_up_location = (TextView) findViewById(R.id.pick_up_location);
        drop_location = (TextView) findViewById(R.id.drop_location);
        product_price = (TextView) findViewById(R.id.product_price);
        change_address_btn = (Button) findViewById(R.id.change_address_btn);
        shipping_charge = (TextView) findViewById(R.id.shipping_charge_view);
        added_tocart = (TextView) findViewById(R.id.added_tocart);
        ll_select_shipping_services = (LinearLayout) findViewById(R.id.ll_select_shipping_services);
        spinner_thirdparty_shipping = (Spinner) findViewById(R.id.spinner_thirdparty_shipping);
        String name= null;
        if(spinner_thirdparty_shipping != null && spinner_thirdparty_shipping.getSelectedItem() !=null ) {
            name = (String)spinner_thirdparty_shipping.getSelectedItem();
            if(name.equalsIgnoreCase("Vendor Shipping")){
                ShippingValue = "1";
                ll_select_shipping_services.setVisibility(View.VISIBLE);
                ll_shipping_services.setVisibility(View.VISIBLE);
            }else{
                ShippingValue = "2";
                ll_select_shipping_services.setVisibility(View.GONE);
                ll_shipping_services.setVisibility(View.GONE);
            }
        } else  {
        }
        spinner_thirdparty_shipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    transportStr = parent.getItemAtPosition(position).toString();
                    if (position > 0) {
                        if (transportStr.equalsIgnoreCase("Vendor Shipping")) {
                            ll_select_shipping_services.setVisibility(View.VISIBLE);
                            ll_shipping_services.setVisibility(View.VISIBLE);
                            shipping = "1";
                            getShippingCharge();
                        } else {
                            ll_select_shipping_services.setVisibility(View.GONE);
                            ll_shipping_services.setVisibility(View.GONE);
                            shipping = "2";
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        spinner_shipping_services = (Spinner) findViewById(R.id.spinner_shipping_services);
        name_review = (EditText) findViewById(R.id.name_review);
        edit_quantity = (EditText) findViewById(R.id.edit_quantity);
        edit_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(discount.equalsIgnoreCase("") || discount.equalsIgnoreCase("null")){
                    if(service_charge.equalsIgnoreCase("0.00")||service_charge.equalsIgnoreCase("0")||service_charge.equalsIgnoreCase("")) {
                        if(!edit_quantity.getText().toString().isEmpty()) {
                            String quantity = edit_quantity.getText().toString();
                            Double quantityInt = Double.parseDouble(quantity);
                            Double priceInt = Double.parseDouble(pr_price);
                            Double ProdPriceStrFloat = priceInt * quantityInt;
                            ProdPriceStr = String.valueOf(ProdPriceStrFloat);
                            product_price.setText(ProdPriceStr);
                        }else{
                            if(!edit_quantity.getText().toString().isEmpty()) {
                                String quantity = edit_quantity.getText().toString();
                                Double quantityInt = Double.parseDouble(quantity);
                                pr_price = String.valueOf(SerChargeFinal);
                                Double priceInt = Double.parseDouble(pr_price);
                                Double ProdPriceStrFloat = priceInt * quantityInt;
                                ProdPriceStr = String.valueOf(ProdPriceStrFloat);
                                product_price.setText(ProdPriceStr);
                            }
                        }
                    }
                }else {
                    if(service_charge.equalsIgnoreCase("0.00")||service_charge.equalsIgnoreCase("0")||service_charge.equalsIgnoreCase("")) {
                        if (!edit_quantity.getText().toString().isEmpty()) {
                            String quantity = edit_quantity.getText().toString();
                            Double quantityInt = Double.parseDouble(quantity);
                            String TotalPriceStrDouble = String.valueOf(TotalPriceStr);
                            Double TotalPriceDouble = Double.parseDouble(TotalPriceStrDouble);
                            Double priceStr = TotalPriceDouble * quantityInt;
                            String ProdPriceString = String.valueOf(priceStr);
                            product_price.setText(ProdPriceString);
                        }
                    }else{
                        if (!edit_quantity.getText().toString().isEmpty()) {
                            String quantity = edit_quantity.getText().toString();
                            Double quantityInt = Double.parseDouble(quantity);
                            String TotalPriceStrDouble = String.valueOf(SerChargeFinal);
                            Double TotalPriceDouble = Double.parseDouble(TotalPriceStrDouble);
                            Double priceStr = TotalPriceDouble * quantityInt;
                            String ProdPriceString = String.valueOf(priceStr);
                            product_price.setText(ProdPriceString);
                        }
                    }
                }
            }
        });
        image_product_details = (ImageView) findViewById(R.id.image_product_details);
        product_name = (TextView) findViewById(R.id.product_name);
        prod_price = (TextView) findViewById(R.id.prod_price);
        prod_price1 = (TextView) findViewById(R.id.prod_price1);
        prod_details_unit = (TextView) findViewById(R.id.prod_details_unit);
        packing_type = (TextView) findViewById(R.id.packing_type);
        min_quantity = (TextView) findViewById(R.id.min_quantity);
        availability_stock = (TextView) findViewById(R.id.availability_stock);
        vendor_product_details = (TextView) findViewById(R.id.vendor_product_details);
        delivery_time = (TextView) findViewById(R.id.delivery_time);
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        addtocart = (Button) findViewById(R.id.addtocart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(edit_quantity.getText().toString());
                int number1 = Integer.parseInt(quantity);
                if(transportStr.equalsIgnoreCase("Select Shipping Type")){
                    Toast.makeText(ProductDetailsActivity.this, "Please Select Transport Option", Toast.LENGTH_SHORT).show();
                }else if(transportStr.equalsIgnoreCase("Vendor Shipping")){
                    if(ServiceStr.equalsIgnoreCase("null") || ServiceStr.equalsIgnoreCase(""))
                    {
                        Toast.makeText(ProductDetailsActivity.this, "Services not avaialble for shipping", Toast.LENGTH_SHORT).show();
                    }else if(number >= number1){
                        addToCart();
                    }else{
                        Toast.makeText(ProductDetailsActivity.this, "Minimum Quantity Required", Toast.LENGTH_SHORT).show();
                    }
                }else if(number >= number1){
                    addToCart();
                }else{
                    Toast.makeText(ProductDetailsActivity.this, "Minimum Quantity Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(Activity.equalsIgnoreCase("2")){
            addtocart.setVisibility(View.GONE);
            reviews_count.setVisibility(View.GONE);
        }
        submit_review_btn = (Button) findViewById(R.id.submit_review_btn);
        image_full = (ImageView) findViewById(R.id.image_full);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this,LocationDialogActivity.class);
                i.putExtra("ContractLocation","0");
                i.putExtra("OrderId",pro_id);
                i.putExtra("UserId",userid);
                i.putExtra("productZipcode",product_zipcode);
                startActivity(i);
            }
        });
        image_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.showImageAlert(ProductDetailsActivity.this, product_image_path);
            }
        });
        image_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_product_details.setVisibility(View.VISIBLE);
                image_full.setVisibility(View.GONE);
                image_product_details.setVisibility(View.VISIBLE);
                Glide.with(ProductDetailsActivity.this)
                        .load(image)
                        .into(image_product_details);
            }
        });
        ll_vendor_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this, VendorLinkActivity.class);
                startActivity(i);
            }
        });
        make_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this, MakeOffer.class);
                i.putExtra("MinQuantity", quantity);
                i.putExtra("Title", pr_title);
                i.putExtra("Unit", pr_weight + weight_unit);
                i.putExtra("Price", pr_currency + " " + pr_price);
                i.putExtra("MinPrice",pr_price);
                i.putExtra("ProID", pro_id);
                startActivity(i);
            }
        });
        buy_sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create dialog for buy sample
                final Dialog customdialog = new Dialog(ProductDetailsActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.buy_sample_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                ll_buysample = (LinearLayout) customdialog.findViewById(R.id.ll_buysample);
                buysample_title = (TextView) customdialog.findViewById(R.id.buysample_title);
                sample_name = (TextView) customdialog.findViewById(R.id.sample_name);
                sample_unit = (TextView) customdialog.findViewById(R.id.sample_unit);
                sample_size = (TextView) customdialog.findViewById(R.id.sample_size);
                sample_pack_type = (TextView) customdialog.findViewById(R.id.sample_pack_type);
                sample_price = (TextView) customdialog.findViewById(R.id.sample_price);
                quantity_et = (EditText) customdialog.findViewById(R.id.quantity_et);
              /*  quantity_et.setFocusableInTouchMode(false);
                quantity_et.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        quantity_et.setFocusableInTouchMode(true);
                        quantity_et.requestFocus();
                        return false;
                    }
                });*/
                addtocart_sample = (Button) customdialog.findViewById(R.id.addtocart_btn);
                close_btn = (Button) customdialog.findViewById(R.id.close_btn);
                buy_sample.setVisibility(View.VISIBLE);
                buysample_title.setText(pr_title_sample);
                sample_name.setText(pr_title_sample);
                sample_unit.setText(weight_sample + " " + weight_unit_sample);
                sample_size.setText("***");
                sample_pack_type.setText(pack_type_sample);
                sample_price.setText(pr_currency_sample + " " + pr_price_sample);
                quantity_et.setText(pr_min_sample);

                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                addtocart_sample.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buySampleAddToCart();
                        //customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        reviews_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_reviews.setVisibility(View.VISIBLE);
            }
        });
        submit_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingStr = String.valueOf(ratingBarSubmit.getRating());
                // Toast.makeText(ProductDetailsActivity.this, ratingStr, Toast.LENGTH_LONG).show();
                review = name_review.getText().toString().trim();
                if (ratingStr.equalsIgnoreCase("0.0")) {
                    Toast.makeText(ProductDetailsActivity.this, "please give rating", Toast.LENGTH_SHORT).show();
                } else if (review.equalsIgnoreCase("")) {
                    Toast.makeText(ProductDetailsActivity.this, "please write a review", Toast.LENGTH_SHORT).show();
                } else {
                    addProductReview();
                }
            }
        });
        edit_quantity.setFocusableInTouchMode(false);
        edit_quantity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edit_quantity.setFocusableInTouchMode(true);
                edit_quantity.requestFocus();
                return false;
            }
        });
        name_review.setFocusableInTouchMode(false);
        name_review.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                name_review.setFocusableInTouchMode(true);
                name_review.requestFocus();
                return false;
            }
        });
        cart_count = (TextView) findViewById(R.id.cart_count);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equalsIgnoreCase("1")){
                    Intent intent = new Intent(ProductDetailsActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(status.equalsIgnoreCase("0")){
                    Intent intent = new Intent(ProductDetailsActivity.this,ProductsActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ProductDetailsActivity.this,StoreMyProductsActivity.class);
                    intent.putExtra("Customizations","0");
                    startActivity(intent);
                }
            }
        });
        cartCount();
        getProductDetails();
    }
    /*
    private void addToCartDatabase() {
        if (realm.isInTransaction())
            realm.cancelTransaction();

        realm.beginTransaction();

            if (edit_quantity.getText().toString().trim().length() > 0) {
                Double totalAmount = Double.parseDouble(edit_quantity.getText().toString()) * Double.parseDouble(pr_price);
                AddToCartModel addToCartModel = new AddToCartModel(pro_id, user_id, image, pr_title, pr_price, quantity, pr_currency, String.valueOf(totalAmount), false);
                Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                realm.copyToRealmOrUpdate(addToCartModel);
            }
            realm.commitTransaction();
            Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

    }
*/
    @Override
    public void onBackPressed() {
        if(status.equalsIgnoreCase("1")){
            Intent intent = new Intent(ProductDetailsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(status.equalsIgnoreCase("0")){
            Intent intent = new Intent(ProductDetailsActivity.this,ProductsActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(ProductDetailsActivity.this,StoreMyProductsActivity.class);
            intent.putExtra("Customizations","0");
            startActivity(intent);
        }
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getProductDetails() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.productDetails("446", "540");
        callRetrofit = service.productDetails(user_id, pro_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());
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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        String reviewcount = root.getString("reviewcount");
                                        String rattingcount = root.getString("rattingcount");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("getproduct");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                pro_id = jsonObject.getString("id");
                                                userid = jsonObject.getString("userid");
                                                if(userid.equalsIgnoreCase(user_id)){
                                                    addtocart.setVisibility(View.GONE);
                                                }
                                                pr_title = jsonObject.getString("pr_title");
                                                String pr_image = jsonObject.getString("pr_image");
                                                pr_price = jsonObject.getString("pr_price");
                                                pr_currency = jsonObject.getString("pr_currency");
                                                discount = jsonObject.getString("discount");
                                                String pr_stocks = jsonObject.getString("pr_stocks");
                                                if(pr_stocks.equalsIgnoreCase("")||pr_stocks.equalsIgnoreCase("0")){
                                                    availability_stock.setText("Out Of Stock");
                                                    availability_stock.setTextColor(getResources().getColor(R.color.red_btn_bg_color));
                                                    addtocart.setVisibility(View.GONE);
                                                }else{
                                                    availability_stock.setText("In Stock");
                                                }
                                                quantity = jsonObject.getString("pr_min");
                                                service_charge = jsonObject.getString("service_charge");
                                                if (discount.equalsIgnoreCase("null")|| discount.equalsIgnoreCase("")) {
                                                    ll_discount.setVisibility(View.GONE);
                                                    vendor_discount.setText("No Discount");
                                                    if(service_charge.equalsIgnoreCase("0.00")||service_charge.equalsIgnoreCase("0")||service_charge.equalsIgnoreCase("")) {
                                                        prod_price.setText(pr_currency + " " + pr_price);
                                                        float quantityInt = Float.parseFloat(quantity);
                                                        float priceInt = Float.parseFloat(pr_price);
                                                        float ProdPriceStrFloat = priceInt*quantityInt ;
                                                        ProdPriceStr = String.valueOf(ProdPriceStrFloat);
                                                        product_price.setText(ProdPriceStr);
                                                    }else{
                                                        Float ProdPriceF = Float.valueOf(pr_price);
                                                        Float ServiceChargeF = Float.valueOf(service_charge);
                                                        Float finalServiceCharge = (ProdPriceF * ServiceChargeF)/100;
                                                        Float ProdPriceFinal = Float.valueOf(pr_price);
                                                        SerChargeFinal = ProdPriceFinal + finalServiceCharge;
                                                        prod_price.setText(pr_currency + " " + String.valueOf(SerChargeFinal));
                                                        float quantityInt = Float.parseFloat(quantity);
                                                        float ProdPriceStrFloat = quantityInt * SerChargeFinal ;
                                                        ProdPriceStr = String.valueOf(ProdPriceStrFloat);
                                                        product_price.setText(ProdPriceStr);
                                                    }

                                                } else {
                                                    ll_discount.setVisibility(View.VISIBLE);
                                                    vendor_discount.setText(discount + "%");
                                                    int OriginalPrice = Integer.parseInt(pr_price);
                                                    int DiscountStr = Integer.parseInt(discount);
                                                    int DiscountPrice = OriginalPrice * DiscountStr;
                                                    float DisPriceStr = Float.parseFloat(String.valueOf(DiscountPrice))/100;
                                                    TotalPriceStr = Float.parseFloat(pr_price)-DisPriceStr;
                                                    prod_price1.setVisibility(View.VISIBLE);
                                                    if(service_charge.equalsIgnoreCase("0.00")||service_charge.equalsIgnoreCase("0")||service_charge.equalsIgnoreCase("")) {
                                                        prod_price.setText(pr_currency+" "+String.valueOf(TotalPriceStr));
                                                        prod_price1.setText(pr_currency+" "+pr_price);
                                                        float quantityInt = Float.parseFloat(quantity);
                                                        float ProdPriceStrFloat = quantityInt * TotalPriceStr ;
                                                        ProdPriceStr = String.valueOf(ProdPriceStrFloat);
                                                        product_price.setText(ProdPriceStr);
                                                    }else{
                                                        Float ProdPriceF = Float.valueOf(pr_price);
                                                        Float ServiceChargeF = Float.valueOf(service_charge);
                                                        Float finalServiceCharge = (ProdPriceF * ServiceChargeF)/100;
                                                        Float totalProdPriceF = Float.valueOf(TotalPriceStr);
                                                        SerChargeFinal = totalProdPriceF + finalServiceCharge;
                                                        float quantityInt = Float.parseFloat(quantity);
                                                        float ProdPriceStrFloat = quantityInt * SerChargeFinal ;
                                                        ProdPriceStr = String.valueOf(ProdPriceStrFloat);
                                                        product_price.setText(ProdPriceStr);
                                                        prod_price.setText(pr_currency+" "+String.valueOf(SerChargeFinal));
                                                        Float ProdPriceFinal = Float.valueOf(pr_price);
                                                        Float TProdPrice = ProdPriceFinal + finalServiceCharge;
                                                        prod_price1.setText(pr_currency+" "+String.valueOf(TProdPrice));
                                                    }

                                                }
                                                pr_weight = jsonObject.getString("pr_weight");
                                                String pr_packtype = jsonObject.getString("pr_packtype");
                                                String days = jsonObject.getString("days");
                                                String stock_status_name = jsonObject.getString("stock_status_name");
                                                weight_unit = jsonObject.getString("weight_unit");
                                                String bus_name = jsonObject.getString("bus_name");
                                                String imagepath = jsonObject.getString("imagepath");
                                                String shipingprovide = jsonObject.getString("shipingprovide");
                                                spinner_thirdparty_shipping.setPrompt("Select Shipping Type");
                                                if(status.equalsIgnoreCase("2")){
                                                    ll_select_shipping_services.setVisibility(View.VISIBLE);
                                                    ll_shipping_services.setVisibility(View.VISIBLE);
                                                    ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport2);
                                                    thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_thirdparty_shipping.setAdapter(thirdparty);
                                                    if (transportStr != null) {
                                                        int spinnerPosition = thirdparty.getPosition(transportStr);
                                                        spinner_thirdparty_shipping.setSelection(spinnerPosition);
                                                    }
                                                }else{
                                                    if(shipingprovide.equalsIgnoreCase("yes")){
                                                        //  ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport);
                                                        final ArrayAdapter<String> thirdparty = new ArrayAdapter<String>(ProductDetailsActivity.this,android.R.layout.simple_spinner_item,transport){
                                                            @Override
                                                            public boolean isEnabled(int position){
                                                                if(position == 0)
                                                                {
                                                                    // Disable the first item from Spinner
                                                                    // First item will be use for hint
                                                                    return false;
                                                                }
                                                                else
                                                                {
                                                                    return true;
                                                                }
                                                            }
                                                            @Override
                                                            public View getDropDownView(int position, View convertView,
                                                                                        ViewGroup parent) {
                                                                View view = super.getDropDownView(position, convertView, parent);
                                                                TextView tv = (TextView) view;
                                                                if(position == 0){
                                                                    // Set the hint text color gray
                                                                    tv.setTextColor(Color.GRAY);
                                                                }
                                                                else {
                                                                    tv.setTextColor(Color.BLACK);
                                                                }
                                                                return view;
                                                            }
                                                        };
                                                        thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spinner_thirdparty_shipping.setAdapter(thirdparty);
                                                        if (transportStr != null) {
                                                            int spinnerPosition = thirdparty.getPosition(transportStr);
                                                            spinner_thirdparty_shipping.setSelection(spinnerPosition);
                                                        }
                                                        // spinner_thirdparty_shipping.setSelection(1);
                                                    }
                                                    else{
                                                        //  ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport1);
                                                        final ArrayAdapter<String> thirdparty = new ArrayAdapter<String>(ProductDetailsActivity.this,android.R.layout.simple_spinner_item,transport1){
                                                            @Override
                                                            public boolean isEnabled(int position){
                                                                if(position == 0)
                                                                {
                                                                    // Disable the first item from Spinner
                                                                    // First item will be use for hint
                                                                    return false;
                                                                }
                                                                else
                                                                {
                                                                    return true;
                                                                }
                                                            }
                                                            @Override
                                                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                                                View view = super.getDropDownView(position, convertView, parent);
                                                                TextView tv = (TextView) view;
                                                                if(position == 0){
                                                                    // Set the hint text color gray
                                                                    tv.setTextColor(Color.GRAY);
                                                                }
                                                                else {
                                                                    tv.setTextColor(Color.BLACK);
                                                                }
                                                                return view;
                                                            }
                                                        };
                                                        thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spinner_thirdparty_shipping.setAdapter(thirdparty);
                                                        if (transportStr != null) {
                                                            int spinnerPosition = thirdparty.getPosition(transportStr);
                                                            spinner_thirdparty_shipping.setSelection(spinnerPosition);
                                                        }
                                                    }
                                                }
                                                String city = jsonObject.getString("city");
                                                String state = jsonObject.getString("state");
                                                String country = jsonObject.getString("country");
                                                zipcode = jsonObject.getString("zipcode");
                                                makeoffer = jsonObject.getString("makeoffer");
                                                product_zipcode = jsonObject.getString("zipcode");
                                                String prod_image = imagepath + pr_image;
                                                product_image_path = prod_image;
                                                if (makeoffer.equalsIgnoreCase("1") && !userid.equalsIgnoreCase(user_id)) {
                                                    make_offer.setVisibility(View.VISIBLE);
                                                }else{
                                                    make_offer.setVisibility(View.GONE);
                                                }
                                                if (prod_image != null && !prod_image.isEmpty()) {
                                                    Picasso.get()
                                                            .load(prod_image)
                                                          //  .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(image_product_details);
                                                }else {
                                                    Picasso.get()
                                                            .load(product_image_path)
                                                         //   .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(image_product_details);
                                                }
                                                product_name.setText(pr_title);
                                                prod_details_unit.setText(pr_weight +" "+weight_unit);
                                                packing_type.setText(pr_packtype);
                                                min_quantity.setText(quantity);
                                                vendor_product_details.setText(bus_name);
                                                delivery_time.setText(days + " Days");
                                                edit_quantity.setText(quantity);
                                                if(reviewcount.isEmpty()|| reviewcount != null){
                                                    reviews_count.setText("No Reviews");
                                                }
                                                reviews_count.setText("Reviews" + "(" + reviewcount + ")");
                                                pick_up_location.setText(city+","+state+"\n"+country+","+zipcode);
                                                // ratingBar.setRating(Float.parseFloat(rattingcount));
                                            }
                                            JSONArray jsonArray1 = root.getJSONArray("getproductsamdel");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                JSONObject object = jsonArray1.getJSONObject(j);
                                                String weight = object.getString("weight");
                                                String weight_unit = object.getString("weight_unit");
                                                String pack_type = object.getString("pack_type");
                                                String price = object.getString("price");
                                                String maxquan = object.getString("maxquan");
                                                String scurrency = object.getString("scurrency");
                                            }
                                            vendor_service_ids = new ArrayList<String>();
                                            vendor_service_names = new ArrayList<String>();
                                            JSONArray vendor = root.getJSONArray("vendor");
                                            for (int k = 0; k < vendor.length(); k++) {
                                                if (vendor.length() > 0) {
                                                    JSONObject jsonObjectVendor = vendor.getJSONObject(k);
                                                    String id = jsonObjectVendor.getString("id");
                                                    String service_id = jsonObjectVendor.getString("service_id");
                                                    String pr_title = jsonObjectVendor.getString("pr_title");
                                                    vendor_service_ids.add(service_id);
                                                    vendor_service_names.add(pr_title);
                                                } else {
                                                    // Toast.makeText(CartActivity.this, "User", Toast.LENGTH_SHORT).show();
                                                }
                                                ArrayAdapter<String> adapter1;
                                                if (vendor_service_names != null) {
                                                    adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, vendor_service_names);
                                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_shipping_services.setAdapter(adapter1);
                                                } else {
                                                   /* adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, shippingServices);
                                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_shipping_services.setAdapter(adapter1);*/
                                                    // Toast.makeText(ProductDetailsActivity.this, "Users not available", Toast.LENGTH_SHORT).show();
                                                    //  progressDialog.dismiss();
                                                }
                                                spinner_shipping_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                                        ServiceStr = spinner_shipping_services.getSelectedItem().toString();
                                                        //user_id_cust = customer_dealers.get(pos);
                                                        vendorServiceIdStr = vendor_service_ids.get(pos);
                                                    }
                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            }
                                            try {
                                                JSONObject delidetails = root.getJSONObject("delidetails");
                                                if(delidetails != null) {
                                                    String address1 = delidetails.getString("address1");
                                                    String address2 = delidetails.getString("address2");
                                                    String city_drop = delidetails.getString("city_name");
                                                    String state_drop = delidetails.getString("subdivision_1_name");
                                                    String country_drop = delidetails.getString("country_name");
                                                    zipcode_drop = delidetails.getString("zipcode");
                                                    getShippingCharge();
                                                    if (address1 != null || !address1.isEmpty()) {
                                                        drop_location.setText(address1+"\n" + city_drop + "," + state_drop + "\n" + country_drop + "," + zipcode_drop);
                                                    } else {
                                                        change_address_btn.setText("Add New Address");
                                                        //  drop_location.setText(address1 + "," + city_drop + "," + state_drop + "," + country_drop + "," + zipcode_drop);
                                                    }
                                                }else{
                                                    change_address_btn.setText("Add New Address");
                                                }
                                            }catch (Exception e){
                                                change_address_btn.setText("Add New Address");
                                                e.printStackTrace();
                                            }
                                            JSONArray jsonArray2 = root.getJSONArray("ratting");
                                            for (int j1 = 0; j1 < jsonArray2.length(); j1++) {
                                                JSONObject object = jsonArray2.getJSONObject(j1);
                                                String prid = object.getString("prid");
                                                String product_id = object.getString("product_id");
                                                String user_id = object.getString("user_id");
                                                String review = object.getString("review");
                                                String com_reply = object.getString("com_reply");
                                                String ratting = object.getString("ratting");
                                                String ondate = object.getString("ondate");
                                                String status = object.getString("status");
                                                String avg = object.getString("avg");
                                                String fullname = object.getString("fullname");
                                                String city1 = object.getString("city");
                                                String country1 = object.getString("country");
                                                // ratingBarProduct.setRating(Float.parseFloat(String.valueOf(Integer.parseInt(ratting))));
                                            }
                                            JSONArray jsonArray3 = root.getJSONArray("review");
                                            getReviewsModels = new ArrayList<>();
                                            for (int j2 = 0; j2 < jsonArray3.length(); j2++) {
                                                JSONObject object = jsonArray3.getJSONObject(j2);
                                                String prid = object.getString("prid");
                                                String product_id = object.getString("product_id");
                                                String user_id = object.getString("user_id");
                                                String review = object.getString("review");
                                                String com_reply = object.getString("com_reply");
                                                String ratting = object.getString("ratting");
                                                String ondate = object.getString("ondate");
                                                String status = object.getString("status");
                                                String fullname = object.getString("fullname");
                                                String city1 = object.getString("city");
                                                String country1 = object.getString("country");

                                                GetReviewsModel getReviewsModel = new GetReviewsModel();
                                                getReviewsModel.setOndate(ondate);
                                                getReviewsModel.setRatting(ratting);
                                                getReviewsModel.setReview(review);
                                                getReviewsModel.setFullname(fullname);
                                                getReviewsModel.setCity(city1);
                                                getReviewsModel.setCountry(country1);
                                                getReviewsModels.add(getReviewsModel);
                                            }
                                            if (getReviewsModels != null) {
                                                getReviewsAdapter = new GetReviewsAdapter(ProductDetailsActivity.this, getReviewsModels);
                                                review_recyclerview.setAdapter(getReviewsAdapter);
                                                getReviewsAdapter.notifyDataSetChanged();
                                            }
                                            JSONObject object = root.getJSONObject("sampledel");
                                            sid = object.getString("sid");
                                            String productid = object.getString("productid");
                                            String user_id = object.getString("user_id");
                                            String sampleproduct = object.getString("sampleproduct");
                                            weight_sample = object.getString("weight");
                                            weight_unit_sample = object.getString("weight_unit");
                                            pack_type_sample = object.getString("pack_type");
                                            pr_title_sample = object.getString("pr_title");
                                            pr_price_sample = object.getString("price");
                                            pr_currency_sample = object.getString("pr_currency");
                                            pr_min_sample = object.getString("pr_min");
                                            preferenceUtils.getStringFromPreference(PreferenceUtils.SampleProdId, sid);
                                            if ((object != null || !object.equals("")) && !userid.equalsIgnoreCase(user_id)) {
                                                buy_sample.setVisibility(View.VISIBLE);
                                            }else{
                                                buy_sample.setVisibility(View.GONE);
                                            }
                                        } else {
                                            ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport1);
                                            thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            //Setting the ArrayAd
                                            // adapter data on the Spinner
                                            spinner_thirdparty_shipping.setAdapter(thirdparty);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void getShippingCharge() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartApi service = retrofit.create(CartApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getShippingCharge(zipcode,zipcode_drop);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
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
                                        String getkms = root.getString("getkms");
                                        getpricekms = root.getString("getpricekms");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            shipping_charge.setText(getpricekms);
                                            //cartCount();
                                        } else {
                                            //Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                    else {
                       // Toast.makeText(ProductDetailsActivity.this, "Product Not Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addToCart() {
        user_id = preferenceUtils.getStringFromPreference(preferenceUtils.USER_ID,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CartApi service = retrofit.create(CartApi.class);

        Call<JsonElement> callRetrofit = null;
        if(shipping.equalsIgnoreCase("")){
            shipping = "1";
        }
        callRetrofit = service.addToCart(user_id,pro_id,quantity,shipping,ProdPriceStr,vendorServiceIdStr,getpricekms);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
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
                                        if (success.equalsIgnoreCase("1")) {
                                            cartCount();
                                            Snackbar snackbar = Snackbar
                                                    .make(relativelayout,message, Snackbar.LENGTH_LONG)
                                                    .setAction("GO TO CART", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent i = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                                            startActivity(i);
                                                        }
                                                    });
                                            snackbar.show();
                                            progressDialog.dismiss();
                                        } else {
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    }
                    else {
                        Toast.makeText(ProductDetailsActivity.this, "Product Not Added To Cart", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void addProductReview() {

        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);
        review = name_review.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.addReview(user_id, pro_id, review, ratingStr);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        message = root.getString("message");
                                        if (success == 1) {
                                            ll_reviews.setVisibility(View.GONE);
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void buySampleAddToCart() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("qtys",quantity);
            jsonObj.put("sampleid",sid);
            androidNetworkingbuySample(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingbuySample(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/AddtosampleCart")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String success = response.getString("success");
                            String message = response.getString("message");
                            if (success.equalsIgnoreCase("1")) {
                                Snackbar snackbar = Snackbar
                                        .make(ll_buysample,message, Snackbar.LENGTH_LONG)
                                        .setAction("GO TO CART", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                                startActivity(i);
                                            }
                                        });
                                snackbar.show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void cartCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.cartCount(user_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success = root.getString("success");
                                        //   message = root.getString("message");
                                        if (success.equalsIgnoreCase("1")) {
                                            cartcount = root.getString("cartcount");
                                            cart_count.setText(cartcount);
                                        }else{

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //Toast.makeText(MainActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    @Override
    protected void onResume() {
        super.onResume();

        if (address_txt_map_dialog != null) {
            address_txt_map_dialog.setText(Address);
        }
    }
*/
}
