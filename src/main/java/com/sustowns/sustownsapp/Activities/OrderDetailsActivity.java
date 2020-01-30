package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.OrdersApi;
import com.sustowns.sustownsapp.Models.OrderDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView order_no,price_order_details,price_order,status_order,date_order,shipping_charge_tv,customer_email;
    PreferenceUtils preferenceUtils;
    ImageView image_orders_details,backarrow;
    ProgressDialog progressDialog;
    String image,orderId;
    Intent intent;
    RecyclerView recyclerview_orderdetails;
    ArrayList<OrderDetailsModel> orderDetailsModels;
    OrderDetailsAdapter orderDetailsAdapter;
    LinearLayout ll_shipping_charge;
    TextView order_id_tv,total_amount_shipping,name_tv,shipping_name,address_shipping,quantity_tv,customer_name_tv,address_customer_tv,total_amount_tv,address_customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_details);
        preferenceUtils = new PreferenceUtils(OrderDetailsActivity.this);
      //  intent = getIntent();
        orderId = getIntent().getStringExtra("OrderId");
        order_no = (TextView) findViewById(R.id.order_no);
        order_id_tv = (TextView) findViewById(R.id.order_id);
        image_orders_details = (ImageView) findViewById(R.id.image_orders_details);
        name_tv = (TextView) findViewById(R.id.order_name);
        price_order_details = (TextView) findViewById(R.id.price_order_details);
        quantity_tv = (TextView) findViewById(R.id.quantity_order_details);
        price_order = (TextView) findViewById(R.id.price_order);
        status_order = (TextView) findViewById(R.id.status_order);
        date_order = (TextView) findViewById(R.id.date_order);
        customer_name_tv = (TextView) findViewById(R.id.customer_name);
        address_customer_tv = (TextView) findViewById(R.id.address_customer);
        total_amount_tv = (TextView) findViewById(R.id.total_amount);
        shipping_charge_tv = (TextView) findViewById(R.id.shipping_charge);
        ll_shipping_charge = (LinearLayout) findViewById(R.id.ll_shipping_charge);
        shipping_name = (TextView) findViewById(R.id.shipping_name);
        address_shipping = (TextView) findViewById(R.id.address_shipping);
        customer_email = (TextView) findViewById(R.id.customer_email);
        total_amount_shipping = (TextView) findViewById(R.id.total_amount_shipping);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        recyclerview_orderdetails = (RecyclerView) findViewById(R.id.recyclerview_orderdetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerview_orderdetails.setLayoutManager(layoutManager);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getOrderDetails();
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(OrderDetailsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getOrderDetails() {
       // order_id = preferenceUtils.getStringFromPreference(PreferenceUtils.ORDER_ID,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrdersApi service = retrofit.create(OrdersApi.class);
        Call<JsonElement> callRetrofit = null;
        //callRetrofit = service.getOrderDetails("http://www.ixiono.com/deaquaticnw//welcome/orderdetail/25");
        callRetrofit = service.orderDetails(orderId);
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
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    progressDialog.dismiss();
                    if (response.body().toString() != null) {
                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String success = root.getString("success");
                                    String busdadgesimg = root.getString("busdadgesimg");
                                    if(success.equals("1")) {
                                        progressDialog.dismiss();
                                        JSONObject jsonObject = root.getJSONObject("orderitem");

                                        String id = jsonObject.getString("id");
                                        String user_id = jsonObject.getString("user_id");
                                        String invoice_no = jsonObject.getString("invoice_no");
                                        String display_name = jsonObject.getString("display_name");
                                        String bill_fname = jsonObject.getString("bill_fname");
                                        String bill_lname = jsonObject.getString("bill_lname");
                                        String pay_email = jsonObject.getString("pay_email");
                                        String pay_phone = jsonObject.getString("pay_phone");
                                        String pay_fax = jsonObject.getString("pay_fax");
                                        String pay_fname = jsonObject.getString("pay_fname");
                                        String pay_lname = jsonObject.getString("pay_lname");
                                        String bill_email = jsonObject.getString("bill_email");
                                        String bill_phone = jsonObject.getString("bill_phone");
                                        String bill_fax = jsonObject.getString("bill_fax");
                                        String bill_company = jsonObject.getString("bill_company");
                                        String bill_address1 = jsonObject.getString("bill_address1");
                                        String bill_address2 = jsonObject.getString("bill_address2");
                                        String bill_zipcode = jsonObject.getString("bill_zipcode");
                                        String bill_city = jsonObject.getString("bill_city");
                                        String bill_country = jsonObject.getString("bill_country");
                                        String bill_state = jsonObject.getString("bill_state");
                                        String pay_address1 = jsonObject.getString("pay_address1");
                                        String pay_address2 = jsonObject.getString("pay_address2");
                                        String pay_zipcode = jsonObject.getString("pay_zipcode");
                                        String pay_city = jsonObject.getString("pay_city");
                                        String pay_country = jsonObject.getString("pay_country");
                                        String pay_state = jsonObject.getString("pay_state");
                                        String pay_method = jsonObject.getString("pay_method");
                                        String currency_code = jsonObject.getString("currency_code");
                                        String order_status = jsonObject.getString("order_status");
                                        String order_date = jsonObject.getString("order_date");
                                        String on_date = jsonObject.getString("on_date");

                                        JSONArray jsonArray = root.getJSONArray("itemdetail");
                                        orderDetailsModels = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject Obj1 = jsonArray.getJSONObject(i);
                                            String id1 = Obj1.getString("id");
                                            String product_order_id = Obj1.getString("product_order_id");
                                            String product_id = Obj1.getString("product_id");
                                            String sample = Obj1.getString("sample");
                                            String quantity = Obj1.getString("quantity");
                                            String price = Obj1.getString("price");
                                            String totalprice = Obj1.getString("totalprice");
                                            String shipamount = Obj1.getString("shipamount");
                                            String pr_title = Obj1.getString("pr_title");
                                            String pr_image = Obj1.getString("pr_image");
                                            String pr_sku = Obj1.getString("pr_sku");
                                            image = busdadgesimg + pr_image;

                                            OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
                                            orderDetailsModel.setId(id1);
                                            orderDetailsModel.setProduct_order_id(product_order_id);
                                            orderDetailsModel.setQuantity(quantity);
                                            orderDetailsModel.setPrice(price);
                                            orderDetailsModel.setPr_title(pr_title);
                                            orderDetailsModel.setPr_image(image);
                                            orderDetailsModels.add(orderDetailsModel);
                                            if(image != null || !image.isEmpty()){
                                                Glide.with(OrderDetailsActivity.this)
                                                        .load(image)
                                                        .into(image_orders_details);
                                            }else{
                                                Glide.with(OrderDetailsActivity.this)
                                                        .load(R.drawable.no_image_available)
                                                        .into(image_orders_details);
                                            }
                                          //  order_id_tv.setText("Order Id : "+product_order_id);
                                            name_tv.setText(pr_title);
                                            price_order_details.setText(currency_code+" "+price);
                                            quantity_tv.setText(quantity);
                                            date_order.setText(order_date);
                                            price_order.setText(currency_code+" "+totalprice);

                                            if(order_status.equalsIgnoreCase("0")){
                                                status_order.setText("Pending");
                                            }else if(order_status.equalsIgnoreCase("1")){
                                                status_order.setText("Complete");
                                            }else if(order_status.equalsIgnoreCase("2")){
                                                status_order.setText("Cancelled");
                                            }else if(order_status.equalsIgnoreCase("3")){
                                                status_order.setText("Payment Processing");
                                            }
                                            customer_name_tv.setText("Name : "+bill_fname);
                                            customer_email.setText(bill_email);
                                            shipping_name.setText(pay_fname+" "+pay_lname);
                                            address_shipping.setText(pay_address1+","+pay_city+","+pay_state+","+pay_country);
                                            address_customer_tv.setText(bill_address1+","+bill_city+","+bill_state+","+bill_country);
                                            total_amount_tv.setText(currency_code +" "+totalprice);
                                            if(shipamount.equalsIgnoreCase("null")){
                                                ll_shipping_charge.setVisibility(View.GONE);
                                            }else  {
                                                shipping_charge_tv.setText(shipamount);
                                                int shipInt = Integer.parseInt(shipamount);
                                                int totalPriceInt = Integer.parseInt(totalprice);
                                                int TotalShippingAmount = shipInt + totalPriceInt;
                                                total_amount_shipping.setText(currency_code +" "+String.valueOf(TotalShippingAmount));
                                            }
                                           // total_amount_shipping.setText(currency_code +" "+totalprice);
                                            if(progressDialog.isShowing())
                                                progressDialog.dismiss();
                                        }
                                        if (orderDetailsModels != null) {
                                            orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderDetailsModels);
                                            recyclerview_orderdetails.setAdapter(orderDetailsAdapter);
                                            orderDetailsAdapter.notifyDataSetChanged();
                                        }
                                    }else{
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(OrderDetailsActivity.this, "Orders not available", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    // Toast.makeText(MyOrderDetails.this, "Not available", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
//                Toast.makeText(MyOrderDetails.this,"Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email,pro_id,user_id,user_role,order_status,order_id,pay_method;
        PreferenceUtils preferenceUtils;
        String[] order;
        ArrayList<OrderDetailsModel> orderModels;
        ProgressDialog progressDialog;
        public OrderDetailsAdapter(Context context, ArrayList<OrderDetailsModel> orderModels) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.orderModels = orderModels;
        }
        @Override
        public OrderDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_details_adapter_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new OrderDetailsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OrderDetailsAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.status.setText("Order Id");
            viewHolder.date_text.setText("Price");

            if(orderModels.get(position) != null){
                viewHolder.orderName.setText(orderModels.get(position).getPr_title());
                viewHolder.orderQuantity.setText(orderModels.get(position).getQuantity());
                viewHolder.orderDate.setText(orderModels.get(position).getPrice());
                viewHolder.orderStatus.setText(orderModels.get(position).getProduct_order_id());
            }
        }
        public void removeAt(int position) {
            //  notifyDataSetChanged();

        }
        @Override
        public int getItemCount() {
            return orderModels.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView,remove_product,increase;
            TextView orderName,orderQuantity,orderDate,orderStatus,date_text,status;
            Button add_payment_btn,add_transport_btn;
            public ViewHolder(View view) {
                super(view);
                orderName = (TextView) view.findViewById(R.id.order_name);
                orderQuantity = (TextView) view.findViewById(R.id.order_quantity);
                orderDate = (TextView) view.findViewById(R.id.order_date);
                orderStatus = (TextView) view.findViewById(R.id.order_status);
                add_payment_btn = (Button) view.findViewById(R.id.add_payment_btn);
                add_transport_btn = (Button) view.findViewById(R.id.add_transport_btn);
                date_text = (TextView) view.findViewById(R.id.date_text);
                status = (TextView) view.findViewById(R.id.status);
                // remove_product = (ImageView) view.findViewById(R.id.remove_product);
            }
        }
    }
}
