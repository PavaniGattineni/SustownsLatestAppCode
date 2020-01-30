package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.sustowns.sustownsapp.Adapters.CartAdapter;
import com.sustowns.sustownsapp.Api.CartApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Api.WebServices;
import com.sustowns.sustownsapp.Models.AddToCartModel;
import com.sustowns.sustownsapp.Models.CartServerModel;
import com.sustowns.sustownsapp.helpers.Helper;
import com.sustowns.sustownsapp.listeners.DataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sustowns.sustownsapp.Adapters.CartAdapter.quantity;

public class CartActivity extends AppCompatActivity implements DataListener {
    public static String username, useremail;
    public static Double cartAmountTotal = 0.0;
    RecyclerView recyclerview_cart;
    PreferenceUtils preferenceUtils;
    CartAdapter cartAdapter;
    String user_id,quantityStr,order_total,total_items;
    Button checkout;
    ImageView backarrow;
    LinearLayout home, news, store, bidcontracts, poultryprices,cart_text;
    ProgressDialog progressDialog;
    ArrayList<AddToCartModel> addToCartModels = new ArrayList<>();
    Realm realm;
    TextView home_text, news_text,cart_title,store_text, contracts_text, market_text, cart_total_amount,remove_all_items,cart_shoplink,update_cart;
    WebServices webServices;
    Helper helper;
    List<CartServerModel> cartServerList;
    int removePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        try {
            initializeValues();
            initializeUI();
            //getCartlist();
            //getCartFromServer();
            getCartListItems();
           // getQuantityStr();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
   /* public void getQuantityStr(){
        cartAdapter.getQuantity(quantity);
        quantityStr = quantity;
    }*/

    private void initializeUI() {
        recyclerview_cart = (RecyclerView) findViewById(R.id.recyclerview_cart);
        LinearLayoutManager lManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerview_cart.setLayoutManager(lManager);
        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);
        home_text = (TextView) findViewById(R.id.home_footer);
        news_text = (TextView) findViewById(R.id.news_footer);
        store_text = (TextView) findViewById(R.id.store_footer);
        contracts_text = (TextView) findViewById(R.id.contracts_footer);
        market_text = (TextView) findViewById(R.id.marketing_footer);
        cart_text = (LinearLayout) findViewById(R.id.cart_text);
        cart_shoplink = (TextView) findViewById(R.id.cart_shoplink);
        cart_total_amount = (TextView) findViewById(R.id.cart_total_amount);
        remove_all_items = (TextView) findViewById(R.id.remove_all_items);
        update_cart = (TextView) findViewById(R.id.update_cart);
        checkout = (Button) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, ShippingAddressActivity.class);
                i.putExtra("TotalAmount",order_total);
                i.putExtra("TotalItems",total_items);
                startActivity(i);
            }
        });
        remove_all_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.showDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE, "Clear the Cart", "Do you want to clear cart?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                clearCartItems();
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            }
        });
        cart_shoplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ProductsActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(CartActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(CartActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(CartActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(CartActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(CartActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        backarrow = (ImageView) findViewById(R.id.backarrowcart);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cart_title = (TextView) findViewById(R.id.cart_title);
        cartCount();
    }
    @Override
    public void onBackPressed() {
        finish();
        }
    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(CartActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName, "");
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        useremail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL, "");
        cartAmountTotal = 0.0;

        webServices = new WebServices(CartActivity.this);
        helper = new Helper(CartActivity.this);
        realm = Realm.getDefaultInstance();
    }
    private void getCartlist() {
        progressdialog();
        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.beginTransaction();
        RealmResults<AddToCartModel> cartList = realm.where(AddToCartModel.class).equalTo("userId", user_id).equalTo("isItemChekOut", false).findAll();
        if (cartList.size() > 0) {
            checkout.setVisibility(View.VISIBLE);
            cart_text.setVisibility(View.GONE);
            cart_total_amount.setVisibility(View.VISIBLE);
            for (AddToCartModel addToCartModel : cartList) {
                addToCartModels.add(addToCartModel);
                cartAmountTotal = Double.parseDouble(addToCartModel.getTotalItemCost()) + cartAmountTotal;
            }
            cart_total_amount.setText("Total Amount : INR " + String.valueOf(cartAmountTotal));
        } else {
            checkout.setVisibility(View.GONE);
            cart_text.setVisibility(View.VISIBLE);
            recyclerview_cart.setVisibility(View.GONE);
            cart_total_amount.setVisibility(View.GONE);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            // Toast.makeText(CartActivity.this, "There are no items in cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCartFromServer() {
        helper.showLoader(CartActivity.this, "Loading.", "Please wait while we fetch your cart.");
       // webServices.getJsonObjectURL("http://sustowns.com/Sustownsservice/viewcart/?userid="+ user_id);
        webServices.getJsonObjectURL("https://www.sustowns.com/Sustownsservice/viewcart/?userid="+ user_id);

    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
/*
    public void removeDataRealm(String itemId, int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
       // Toast.makeText(CartActivity.this, "Product is removed!", Toast.LENGTH_SHORT).show();
    }
*/
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
                                        String cartcount = root.getString("cartcount");
                                        cart_title.setText("Cart"+"("+cartcount+")");
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

    public void removeFromCart(String itemId, int position) {
        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.beginTransaction();
        AddToCartModel addToCartModel = realm.where(AddToCartModel.class).equalTo("itemId", itemId).findFirst();
        cartAmountTotal = cartAmountTotal - Double.parseDouble(addToCartModel.getTotalItemCost());
        cart_total_amount.setText("Total Amount : INR " + String.valueOf(cartAmountTotal));
        addToCartModel.deleteFromRealm();

        addToCartModels.remove(position);
        cartAdapter.notifyItemRemoved(position);
        realm.commitTransaction();
        Toast.makeText(CartActivity.this, "Product is removed!", Toast.LENGTH_SHORT).show();
    }

    public void updateCart(String itemId, String quantity, int position) {
        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.beginTransaction();
        AddToCartModel addToCartModel = realm.where(AddToCartModel.class).equalTo("itemId", itemId).findFirst();
        cartAmountTotal = cartAmountTotal - Double.parseDouble(addToCartModel.getTotalItemCost());
        Double totalAmount = Double.parseDouble(quantity) * Double.parseDouble(addToCartModel.getPrice());
        cartAmountTotal = cartAmountTotal + totalAmount;
        cart_total_amount.setText("Total Amount : INR " + String.valueOf(cartAmountTotal));
        addToCartModel.setQuantity(quantity);
        addToCartModel.setTotalItemCost(String.valueOf(totalAmount));
        realm.copyToRealmOrUpdate(addToCartModel);

        addToCartModels.get(position).setQuantity(quantity);
        cartAdapter.notifyItemChanged(position);
        realm.commitTransaction();
        Toast.makeText(CartActivity.this, "Cart is updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataRetrieved(Object data, String whichUrl) {
        try {
            JSONObject response = (JSONObject) data;
            if (response.getString("success").equalsIgnoreCase("1")) {
                if (whichUrl.contains(DZ_URL.GET_CART_LIST_SERVER)) {
                    JSONArray cartArray = response.getJSONArray("get_cat");
                    if (cartArray != null && cartArray.length() > 0) {
                        cartServerList = new ArrayList<>();
                        checkout.setVisibility(View.VISIBLE);
                        cart_text.setVisibility(View.GONE);
                        cart_total_amount.setVisibility(View.VISIBLE);
                        if (cartArray.length() > 0) {
                            for (int i = 0; i < cartArray.length(); i++) {
                                JSONObject cartObject = cartArray.getJSONObject(i);

/*
                                CartServerModel cartServerModel = new CartServerModel(
                                        cartObject.getString("cart_id"),
                                        cartObject.getString("user_id"),
                                        cartObject.getString("product_id"),
                                        cartObject.getString("qty"),
                                        cartObject.getString("shamount"),
                                        cartObject.getString("price"),
                                        cartObject.getString("name"),
                                        cartObject.getString("productser_chargeAmount"),
                                        cartObject.getString("weight_produnit"),
                                        cartObject.getString("pro_code"),
                                        cartObject.getString("pro_weight"),
                                        response.getString("imagepath") + cartObject.getString("pro_img"),
                                        cartObject.getString("tax"),
                                        cartObject.getString("discount"),
                                        cartObject.getString("mrp"),
                                        cartObject.getString("currency"),
                                        cartObject.getString("sampleproduct"),
                                        cartObject.getString("shipping_type"),
                                        cartObject.getString("ven_service_id"),
                                        cartObject.getString("kmrange"),
                                        cartObject.getString("price_qty"),
                                        cartObject.getString("c_id"),
                                        cartObject.getString("pr_title"),
                                        cartObject.getString("id")
                                );
*/

                               // cartServerList.add(cartServerModel);
                            }
                            cartAmountTotal = Double.parseDouble(response.getString("order_total"));
                            cart_total_amount.setText("Total Amount : INR " + response.getString("order_total"));
                            helper.hideLoader();
                        } else {
                            helper.singleClickAlert(CartActivity.this, SweetAlertDialog.NORMAL_TYPE, "", "Cart is empty",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();

                                            cartServerList.remove(removePosition);
                                        }
                                    });
                        }
                    } else {
                        checkout.setVisibility(View.GONE);
                        cart_text.setVisibility(View.VISIBLE);
                        recyclerview_cart.setVisibility(View.GONE);
                        cart_total_amount.setVisibility(View.GONE);
                    }
                }
                        else {
                        helper.hideLoader();
                        helper.singleClickAlert(CartActivity.this, SweetAlertDialog.NORMAL_TYPE, "", response.getString("message"),
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        cartAmountTotal = cartAmountTotal - Double.parseDouble(cartServerList.get(removePosition).getPrice_qty());
                                        cart_total_amount.setText("Total Amount : INR " + cartAmountTotal);
                                        if(cartAmountTotal > 0.0){
                                            checkout.setVisibility(View.VISIBLE);
                                            cart_text.setVisibility(View.GONE);
                                            remove_all_items.setVisibility(View.VISIBLE);
                                            cart_total_amount.setVisibility(View.VISIBLE);
                                        }else{
                                            checkout.setVisibility(View.GONE);
                                            cart_text.setVisibility(View.VISIBLE);
                                            recyclerview_cart.setVisibility(View.GONE);
                                            remove_all_items.setVisibility(View.GONE);
                                            cart_total_amount.setVisibility(View.GONE);
                                        }

                                        cartServerList.remove(removePosition);
                                        cartAdapter.notifyItemRemoved(removePosition);
                                    }
                                });
                    }

            }else {
                helper.hideLoader();
                helper.singleClickAlert(CartActivity.this, SweetAlertDialog.ERROR_TYPE, "", response.getString("message"),
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
            helper.hideLoader();
        }
    }

    @Override
    public void onError(Object data) {
        helper.hideLoader();
        helper.singleClickAlert(CartActivity.this, SweetAlertDialog.ERROR_TYPE, "", "Something went wrong. Please try again later.",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }

    public void removeItemServer(int position) {
        removePosition = position;
        helper.showLoader(CartActivity.this, "Removing..", "Please wait for a while");
        webServices.getJsonObjectURL("http://dev2.sustowns.com/Sustownsservice/remove_cartforsingle?cart_id="+ cartServerList.get(position).getCart_id());
       // webServices.getJsonObjectURL("https://www.sustowns.com/Sustownsservice/remove_cartforsingle/?cart_id=" + cartServerList.get(position).getCart_id());
    }

  /*  public void removeShippingItemServer(int position) {
        removePosition = position;
        helper.showLoader(CartActivity.this, "Removing..", "Please wait for a while");
        webServices.getJsonObjectURL("http://dev2.sustowns.com/Sustownsservice/remove_cartforsingle?cart_id="+ cartServerList.get(position).getCart_id());
        // webServices.getJsonObjectURL("https://www.sustowns.com/Sustownsservice/remove_cartforsingle/?cart_id=" + cartServerList.get(position).getCart_id());
    }
*/
    public void progresDialog() {
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void getCartListItems() {
        //  user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progresDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CartApi service = retrofit.create(CartApi.class);
        Call<JsonElement> callRetrofit = null;
      //  callRetrofit = service.getCartList("https://www.sustowns.com/Sustownsservice/viewcart/?userid="+user_id);
         callRetrofit = service.getCartList("https://www.sustowns.com/Sustownsservice/viewcart?userid="+user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().toString() != null) {
                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    order_total = root.getString("order_total");
                                    total_items = root.getString("total_items");
                                    String imagepath = root.getString("imagepath");
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equals("1")) {
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        JSONArray msg = root.getJSONArray("get_cat");
                                        cartServerList = new ArrayList<>();
                                        for (int i = 0; i < msg.length(); i++) {
                                            JSONObject Obj = msg.getJSONObject(i);
                                            String cart_id = Obj.getString("cart_id");
                                            String user_id = Obj.getString("user_id");
                                            String product_id = Obj.getString("product_id");
                                            String qty = Obj.getString("qty");
                                            String shamount = Obj.getString("shamount");
                                            String price = Obj.getString("price");
                                            String name = Obj.getString("name");
                                            String productser_chargeAmount = Obj.getString("productser_chargeAmount");
                                            String weight_produnit = Obj.getString("weight_produnit");
                                            String pro_code = Obj.getString("pro_code");
                                            String pro_weight = Obj.getString("pro_weight");
                                            String pro_img = Obj.getString("pro_img");
                                            String tax = Obj.getString("tax");
                                            String discount = Obj.getString("discount");
                                            String mrp = Obj.getString("mrp");
                                            String currency = Obj.getString("currency");
                                            String sampleproduct = Obj.getString("sampleproduct");
                                            String shipping_type = Obj.getString("shipping_type");
                                            String ven_service_id = Obj.getString("ven_service_id");
                                            String kmrange = Obj.getString("kmrange");
                                            String price_qty = Obj.getString("price_qty");
                                            String c_id = Obj.getString("c_id");
                                            String pr_title = Obj.getString("pr_title");
                                            String id = Obj.getString("id");
                                            String shiping_id = Obj.getString("shiping_id");
                                            String shiping_amount = Obj.getString("shiping_amount");
                                            String shipping_ven_service_id = Obj.getString("shipping_ven_service_id");
                                            String pr_stocks = Obj.getString("pr_stocks");
                                            String pr_min = Obj.getString("pr_min");
                                            String image = imagepath + pro_img;

                                            CartServerModel cartListModel = new CartServerModel();
                                            cartListModel.setCart_id(cart_id);
                                            cartListModel.setUser_id(user_id);
                                            cartListModel.setProduct_id(product_id);
                                            cartListModel.setQty(qty);
                                            cartListModel.setShamount(shamount);
                                            cartListModel.setPrice(price);
                                            cartListModel.setName(name);
                                            cartListModel.setProductser_chargeAmount(productser_chargeAmount);
                                            cartListModel.setWeight_produnit(weight_produnit);
                                            cartListModel.setPro_code(pro_code);
                                            cartListModel.setPro_weight(pro_weight);
                                            cartListModel.setDiscount(discount);
                                            cartListModel.setMrp(mrp);
                                            cartListModel.setCurrency(currency);
                                            cartListModel.setSampleproduct(sampleproduct);
                                            cartListModel.setShipping_type(shipping_type);
                                            cartListModel.setVen_service_id(ven_service_id);
                                            cartListModel.setKmrange(kmrange);
                                            cartListModel.setPrice_qty(price_qty);
                                            cartListModel.setC_id(c_id);
                                            cartListModel.setPro_img(image);
                                            cartListModel.setId(id);
                                            cartListModel.setShiping_id(shiping_id);
                                            cartListModel.setShiping_amount(shiping_amount);
                                            cartListModel.setShipping_ven_service_id(shipping_ven_service_id);
                                            cartListModel.setPr_min(pr_min);
                                            cartListModel.setPr_stocks(pr_stocks);
                                            cartServerList.add(cartListModel);
                                        }
                                        cartAmountTotal = Double.parseDouble(order_total);
                                        cart_total_amount.setText("Total Amount : INR " + order_total);
                                        }else{
                                        checkout.setVisibility(View.GONE);
                                        cart_text.setVisibility(View.VISIBLE);
                                        recyclerview_cart.setVisibility(View.GONE);
                                        cart_total_amount.setVisibility(View.GONE);
                                            progressDialog.dismiss();
                                           // Toast.makeText(CartActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                                        }
                                        if(cartServerList != null){
                                            progressDialog.dismiss();
                                            cart_text.setVisibility(View.GONE);
                                            checkout.setVisibility(View.VISIBLE);
                                            cart_total_amount.setVisibility(View.VISIBLE);
                                            remove_all_items.setVisibility(View.VISIBLE);
                                         //   update_cart.setVisibility(View.VISIBLE);
                                            cartAdapter = new CartAdapter(CartActivity.this, cartServerList);
                                            recyclerview_cart.setAdapter(cartAdapter);
                                            cartAdapter.notifyDataSetChanged();
                                        }else{
                                            progressDialog.dismiss();
                                            checkout.setVisibility(View.GONE);
                                            cart_text.setVisibility(View.VISIBLE);
                                            recyclerview_cart.setVisibility(View.GONE);
                                            cart_total_amount.setVisibility(View.GONE);
                                            remove_all_items.setVisibility(View.GONE);
                                           // update_cart.setVisibility(View.GONE);
                                        }
                                }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
            }
            @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                    Log.d("Error", ">>>>" + t.toString());
                    progressDialog.dismiss();
                Toast.makeText(CartActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
        public void clearCartItems() {
        //  user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        progresDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartApi service = retrofit.create(CartApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.clearCartItems(user_id);
        // callRetrofit = service.getCartList("http://www.deaquatic.com/welcome/cartdetails/1");
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().toString() != null) {
                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    //String full = root.getString("full");
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equalsIgnoreCase("1")) {
                                        //getCartListItems();
                                           Intent i = new Intent(CartActivity.this, CartActivity.class);
                                           startActivity(i);
                                           finish();
                                         progressDialog.dismiss();
                                        // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                           // cartAdapter.notifyItemRemoved();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                               progressDialog.dismiss();
                            }
                        }
                    }
                } else {
                    // Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
                Toast.makeText(CartActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setJsonObject() {
        try {
            JSONObject useridObj = new JSONObject();
            useridObj.put("userid",user_id);
            JSONArray quantityArray = new JSONArray();
            for (CartServerModel cartServerModel : cartServerList) {
                JSONObject quantityObject = new JSONObject();
              /*  quantityObject.put("cart_id","763");
                quantityObject.put("qty","15");*/
               quantityObject.put("cart_id",cartServerModel.getCart_id());
               quantityObject.put("qty",quantity);
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
                            Log.d("success", "success : " + response.getString("success"));
                            Log.d("message", "message : " + response.getString("message"));
                        } catch (JSONException e) {
                        }
                        try {
                            String message = response.getString("message");
                            String success = response.getString("success");
                            if(success.equalsIgnoreCase("1")){
                                getCartListItems();
                              //  Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
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
/*
    public void updateCartList() {
        //  user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        //  progresDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartApi service = retrofit.create(CartApi.class);
        quantity_edit
        try {
            JSONObject useridObj = new JSONObject();
            useridObj.put("userid","446");
            JSONArray quantityArray = new JSONArray();
            for (CartServerModel cartServerModel : cartServerList) {
                JSONObject quantityObject = new JSONObject();
                quantityObject.put("cart_id",cartServerModel.getCart_id());
                quantityObject.put("qty",cartServerModel.getQty());
                quantityArray.put(quantityObject);
            }
            useridObj.put("quantityArray",quantityArray);
            Log.e("ProductDetailsArray", ""+useridObj);
           androidNetworking(customisedObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.clearCartItems(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                if (response.isSuccessful()) {
                    // progressDialog.dismiss();
                    if (response.body().toString() != null) {
                 if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    //String full = root.getString("full");
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equalsIgnoreCase("1")) {
                                        //getCartListItems();
                                        // progressDialog.dismiss();
                                        Intent i = new Intent(CartActivity.this,CartActivity.class);
                                        startActivity(i);
                                        finish();
                                        // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                        // cartAdapter.notifyItemRemoved();
                                    }else{
                                        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                                // progressDialog.dismiss();
                            }
                        }
                    }
                } else {
                    // Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    // progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //progressDialog.dismiss();
                //   Toast.makeText(CartActivity.this,"Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
}
