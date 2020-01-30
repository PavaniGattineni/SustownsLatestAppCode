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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.PostContractsApi;
import com.sustowns.sustownsapp.Models.ContractPurchasesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContractOrdersInvoice extends AppCompatActivity {
    RecyclerView recyclerview_invoice_orders;
    PreferenceUtils preferenceUtils;
    Intent intent;
    String order_id,invoiceNo,StatusStr;
    TextView invoice_no_text,date_order,shipping_name,address_shipping,contact_shipping,total_amount_tv,shipping_charge_tv,bill_name;
    ProgressDialog progressDialog;
    String subtotal,firstname,country,state,address,city,orderstatus,orderdate,orderId,user_role;
    ArrayList<ContractPurchasesModel> ContractinvoiceModels;
    InvoiceDetailsAdapter invoiceDetailsAdapter;
    ImageView backarrow;
    TextView bill_address,customer_name,customer_email,contact_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contract_orders_invoice);


        preferenceUtils = new PreferenceUtils(ContractOrdersInvoice.this);
        user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE,"");
        intent = getIntent();
        order_id = intent.getStringExtra("OrderId");
        invoiceNo = intent.getStringExtra("InvoiceNo");
        StatusStr = intent.getStringExtra("Status");
        recyclerview_invoice_orders = (RecyclerView) findViewById(R.id.recyclerview_invoice_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ContractOrdersInvoice.this,LinearLayoutManager.VERTICAL,false);
        recyclerview_invoice_orders.setLayoutManager(layoutManager);
        invoice_no_text = (TextView) findViewById(R.id.invoice_no);
        date_order = (TextView) findViewById(R.id.date_order);
        shipping_name = (TextView) findViewById(R.id.shipping_name);
        address_shipping = (TextView) findViewById(R.id.address_shipping);
        contact_shipping = (TextView) findViewById(R.id.contact_shipping);
        shipping_charge_tv = (TextView) findViewById(R.id.shipping_charge);
        total_amount_tv = (TextView) findViewById(R.id.total_amount);
        bill_name = (TextView) findViewById(R.id.bill_name);
        bill_address = (TextView) findViewById(R.id.bill_address);
        customer_name = (TextView) findViewById(R.id.customer_name);
        customer_email = (TextView) findViewById(R.id.customer_email);
        contact_customer = (TextView) findViewById(R.id.contact_customer);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getContractOrderInvoiceDetails();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(ContractOrdersInvoice.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getContractOrderInvoiceDetails() {
        // order_id = preferenceUtils.getStringFromPreference(PreferenceUtils.ORDER_ID,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostContractsApi service = retrofit.create(PostContractsApi.class);
        Call<JsonElement> callRetrofit = null;
       // callRetrofit = service.getContractsOrdersinvoice(order_id,invoiceNo);
        callRetrofit = service.getContractsOrdersinvoice(order_id,invoiceNo);
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
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    String busdadgesimg = root.getString("busdadgesimg");
                                    if(success.equals("1")) {
                                        JSONObject jsonObject = root.getJSONObject("orderitem");

                                        String id = jsonObject.getString("id");
                                        String user_id = jsonObject.getString("user_id");
                                        String order_id = jsonObject.getString("order_id");
                                        String job_id = jsonObject.getString("job_id");
                                        String pay_type = jsonObject.getString("pay_type");
                                        String totalprice = jsonObject.getString("totalprice");
                                        String quantity = jsonObject.getString("quantity");
                                        String invoice_no = jsonObject.getString("invoice_no");
                                        String bill_fname = jsonObject.getString("bill_fname");
                                        String bill_address1 = jsonObject.getString("bill_address1");
                                        String bill_city = jsonObject.getString("bill_city");
                                        String bill_zipcode = jsonObject.getString("bill_zipcode");
                                        String pay_fname = jsonObject.getString("pay_fname");
                                        String pay_lname = jsonObject.getString("pay_lname");
                                        String pay_address1 = jsonObject.getString("pay_address1");
                                        String pay_city = jsonObject.getString("pay_city");
                                        String pay_zipcode = jsonObject.getString("pay_zipcode");
                                        String pay_email = jsonObject.getString("pay_email");
                                        String pay_phone = jsonObject.getString("pay_phone");
                                        String display_name = jsonObject.getString("display_name");
                                        String order_date = jsonObject.getString("order_date");
                                        String order_status = jsonObject.getString("order_status");

                                        JSONArray jsonArray = root.getJSONArray("itemdetail");
                                        ContractinvoiceModels = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject Obj1 = jsonArray.getJSONObject(i);

                                            String job_name = Obj1.getString("job_name");
                                            String image = Obj1.getString("image");
                                            String quantity1 = Obj1.getString("quantity");
                                            String price = Obj1.getString("price");
                                            String status = Obj1.getString("status");
                                            int total = Integer.parseInt(price)*Integer.parseInt(quantity1);
                                            String Total = String.valueOf(total);
                                            String Image = busdadgesimg+image;

                                            ContractPurchasesModel invoiceModel = new ContractPurchasesModel();
                                            invoiceModel.setJob_name(job_name);
                                            invoiceModel.setImage(Image);
                                            invoiceModel.setQuantity(quantity1);
                                            invoiceModel.setTotalprice(price);
                                            invoiceModel.setTotal(Total);
                                            invoiceModel.setOrder_status(status);
                                            ContractinvoiceModels.add(invoiceModel);

                                        }
                                        invoice_no_text.setText(invoice_no);
                                        date_order.setText(order_date);
                                        shipping_name.setText(pay_fname+" "+pay_lname);
                                        address_shipping.setText(pay_address1+","+pay_city+","+pay_zipcode);
                                        bill_name.setText(bill_fname);
                                        bill_address.setText(bill_address1+","+bill_city+","+bill_zipcode);
                                        customer_name.setText(display_name);
                                        customer_email.setText(pay_email);
                                        contact_customer.setText(pay_phone);
                                        total_amount_tv.setText(totalprice);
                                        shipping_charge_tv.setText("Free");
                                        if(ContractinvoiceModels != null){
                                            invoiceDetailsAdapter = new InvoiceDetailsAdapter(ContractOrdersInvoice.this,ContractinvoiceModels,StatusStr);
                                            recyclerview_invoice_orders.setAdapter(invoiceDetailsAdapter);
                                            invoiceDetailsAdapter.notifyDataSetChanged();
                                        }
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    }else{
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(ContractOrdersInvoice.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }  catch (JSONException e) {
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




    public class InvoiceDetailsAdapter extends RecyclerView.Adapter<InvoiceDetailsAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email,pro_id,user_id,user_role,order_status,order_id,pay_method;
        PreferenceUtils preferenceUtils;
        String StatusStr;
        ArrayList<ContractPurchasesModel> orderModels;
        ProgressDialog progressDialog;
        Integer priceInt,qtyInt,totalAmount;

        public InvoiceDetailsAdapter(Context context, ArrayList<ContractPurchasesModel> orderModels,String StatusStr) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.orderModels = orderModels;
            this.StatusStr = StatusStr;
        }

        @Override
        public InvoiceDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_order_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new InvoiceDetailsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final InvoiceDetailsAdapter.ViewHolder viewHolder, final int position) {
            if (orderModels.get(position) != null) {
                viewHolder.order_name.setText(orderModels.get(position).getJob_name());
                viewHolder.invoice_price.setText(orderModels.get(position).getTotalprice());
                viewHolder.order_quantity.setText(orderModels.get(position).getQuantity());
                if(StatusStr.equalsIgnoreCase("0")) {
                    viewHolder.order_status.setText("Pending");
                }else  if(StatusStr.equalsIgnoreCase("1")) {
                    viewHolder.order_status.setText("Completed");
                }else{
                    viewHolder.order_status.setText("Cancelled");
                }
              /*  if(orderModels.get(position).getOrder_status().equalsIgnoreCase("0")) {
                    viewHolder.order_status.setText("Pending");
                }else  if(orderModels.get(position).getOrder_status().equalsIgnoreCase("1")) {
                    viewHolder.order_status.setText("Complete");
                }else{
                    viewHolder.order_status.setText("Cancel");
                }*/
                viewHolder.invoice_totals.setText(orderModels.get(position).getTotal());
                Picasso.get()
                        .load(orderModels.get(position).getImage())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.order_image);
            }

        }

        @Override
        public int getItemCount() {
            return orderModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView order_name,invoice_price,order_quantity,invoice_totals,order_status,total_amount;
            ImageView order_image;
            public ViewHolder(View view) {
                super(view);
                order_name = (TextView) view.findViewById(R.id.order_name);
                invoice_price = (TextView) view.findViewById(R.id.invoice_price);
                order_quantity = (TextView) view.findViewById(R.id.order_quantity);
                order_status = (TextView) view.findViewById(R.id.order_status);
                invoice_totals = (TextView) view.findViewById(R.id.invoice_totals);
                order_image = (ImageView) view.findViewById(R.id.order_image);
            }
        }
    }


}

