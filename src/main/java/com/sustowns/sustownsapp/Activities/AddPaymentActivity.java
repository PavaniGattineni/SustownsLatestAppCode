package com.sustowns.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.CartListModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPaymentActivity extends AppCompatActivity {

    RecyclerView recycler_view_add_payment;
    ImageView backarrow;
    TextView total_amount_add_payment,add_payment_msg,product_code,date,order_status_add,product_name,amount;
    EditText cheque_no;
    Button paymentDate,submit_btn;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    LinearLayout ll_bank_details,linearlayout,ll_add_payment;
    TextView acc_name,acc_no,acc_ifsccode,acc_address,acc_note,cheque_text;
    ProgressDialog progressDialog;
    AddPaymentAdapter addPaymentAdapter;
    ArrayList<CartListModels> paymentModels;
    PreferenceUtils preferenceUtils;
    String user_id,orderId,order_id,transation_id,totalprice,order_status,contractOrders,Amount,ChequeNo,paymentType,bankRandId,randId,SelectedDate;
    RadioButton online_radiobtn, cheque_radiobtn,radioButton;
    RadioGroup radioGroup;
    Integer selectedId;
    String ProName,ProCode,ProPrice,ProDate,ProStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_payment);
        preferenceUtils = new PreferenceUtils(AddPaymentActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        orderId = getIntent().getStringExtra("OrderId");
        bankRandId = getIntent().getStringExtra("BankRandId");
        randId = getIntent().getStringExtra("RandId");
        contractOrders = getIntent().getStringExtra("ContractOrders");
        ProName = getIntent().getStringExtra("ContractName");
        ProCode = getIntent().getStringExtra("UniqueCode");
        ProPrice = getIntent().getStringExtra("Amount");
        ProDate = getIntent().getStringExtra("Date");
        ProStatus = getIntent().getStringExtra("Status");
        ll_add_payment = (LinearLayout) findViewById(R.id.ll_add_payment);
        product_name = (TextView) findViewById(R.id.product_name);
        product_code = (TextView) findViewById(R.id.product_code);
        date = (TextView) findViewById(R.id.date);
        order_status_add = (TextView) findViewById(R.id.order_status_add);
        add_payment_msg = (TextView) findViewById(R.id.add_payment_msg);
       /* product_name.setText(ProName);
        product_code.setText(ProCode);
        date.setText(ProDate);
        order_status_add.setText(ProStatus);*/
        total_amount_add_payment = (TextView) findViewById(R.id.total_amount_add_payment);
        total_amount_add_payment.setText("Total Order Amount : "+ProPrice);
        amount = (TextView) findViewById(R.id.amount_payment);
        amount.setText(ProPrice);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        recycler_view_add_payment = (RecyclerView) findViewById(R.id.recycler_view_add_payment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddPaymentActivity.this,LinearLayoutManager.VERTICAL,false);
        recycler_view_add_payment.setLayoutManager(layoutManager);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        cheque_no = (EditText) findViewById(R.id.cheque_no);
        cheque_text = (TextView) findViewById(R.id.cheque_text);
        paymentDate = (Button) findViewById(R.id.payment_date);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        ll_bank_details = (LinearLayout) findViewById(R.id.ll_bank_details);
        acc_name = (TextView) findViewById(R.id.bank_account_name);
        acc_no = (TextView) findViewById(R.id.bank_account_no);
        acc_ifsccode = (TextView) findViewById(R.id.bank_ifsccode);
        acc_address = (TextView) findViewById(R.id.branch_address);
        acc_note = (TextView) findViewById(R.id.note_bank);
        cheque_no.setFocusableInTouchMode(false);
        cheque_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cheque_no.setFocusableInTouchMode(true);
                cheque_no.requestFocus();
                return false;
            }
        });
        paymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                // selectedRadioBtn = radioButton.getText().toString();
                if (selectedId < 0) {
                    Toast.makeText(AddPaymentActivity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }else if(SelectedDate.equalsIgnoreCase("")|| cheque_no.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddPaymentActivity.this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
                }else if(contractOrders.equalsIgnoreCase("1")){
                    setJsonObject();
                }else if(contractOrders.equalsIgnoreCase("2")){
                    setContractJsonObject();
                }else if(contractOrders.equalsIgnoreCase("0")){
                    setLogisticsJsonObject();
                    // TO DO FOR LOGISTICS ORDERS
                }else if(contractOrders.equalsIgnoreCase("3")){
                    setContractsLogisticsJsonObject();
                }
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        cheque_radiobtn = (RadioButton) findViewById(R.id.cheque_radiobtn);
        online_radiobtn = (RadioButton) findViewById(R.id.online_radiobtn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cheque_radiobtn:
                        paymentType = "cheque";
                        cheque_text.setText("Cheque No");
                        break;
                    case R.id.online_radiobtn:
                        paymentType = "online";
                        cheque_text.setText("Online Transaction ID");
                        break;
                }
            }
        });
        if(contractOrders.equalsIgnoreCase("1")){
            recycler_view_add_payment.setVisibility(View.VISIBLE);
            ll_add_payment.setVisibility(View.GONE);
            getStorePaymentList();
        }else if(contractOrders.equalsIgnoreCase("2")){
            ll_add_payment.setVisibility(View.VISIBLE);
            recycler_view_add_payment.setVisibility(View.GONE);
            product_name.setText(ProName);
            product_code.setText(randId);
            date.setText(ProDate);
            if(ProStatus.equalsIgnoreCase("0")){
                order_status_add.setText("Pending");
            }else if(ProStatus.equalsIgnoreCase("1")){
                order_status_add.setText("Complete");
            }else if(ProStatus.equalsIgnoreCase("2")){
                order_status_add.setText("Cancelled");
            }else if(ProStatus.equalsIgnoreCase("3")){
                order_status_add.setText("Payment Processing");
            }
        }else if(contractOrders.equalsIgnoreCase("0")){
            ll_add_payment.setVisibility(View.VISIBLE);
            recycler_view_add_payment.setVisibility(View.GONE);
            product_name.setText(ProName);
            product_code.setText(randId);
            date.setText(ProDate);
            if(ProStatus.equalsIgnoreCase("0")){
                order_status_add.setText("Pending");
            }else if(ProStatus.equalsIgnoreCase("1")){
                order_status_add.setText("Complete");
            }
        }
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBankDetailsList();
    }
    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                SelectedDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                paymentDate.setText(SelectedDate);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpDialog.show();

    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(AddPaymentActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void getStorePaymentList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCartListPaymentOrders(orderId,bankRandId,user_id);
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
                                        JSONObject success = root.getJSONObject("success");
                                        String successStr = success.getString("success");
                                        if (successStr.equalsIgnoreCase("1")) {
                                            if(progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("row");
                                            paymentModels = new ArrayList<>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String product_order_id = jsonObject.getString("product_order_id");
                                                totalprice = jsonObject.getString("totalprice");
                                                String order_status = jsonObject.getString("order_status");
                                                String pr_title = jsonObject.getString("pr_title");
                                                total_amount_add_payment.setText("Total Order Amount : "+totalprice);

                                                CartListModels paymentModel = new CartListModels();
                                                paymentModel.setId(id);
                                                paymentModel.setUser_id(user_id);
                                                paymentModel.setOrder_id(order_id);
                                                paymentModel.setProduct_order_id(product_order_id);
                                                paymentModel.setTotalprice(totalprice);
                                                paymentModel.setOrder_status(order_status);
                                                paymentModel.setPr_title(pr_title);
                                                paymentModels.add(paymentModel);
                                            }
                                            if(paymentModels != null){
                                                addPaymentAdapter = new AddPaymentAdapter(AddPaymentActivity.this,paymentModels);
                                                recycler_view_add_payment.setAdapter(addPaymentAdapter);
                                                addPaymentAdapter.notifyDataSetChanged();
                                                progressDialog.dismiss();
                                            }
                                            JSONObject cart_amount = root.getJSONObject("cart_amount");
                                            String carttlprice = cart_amount.getString("carttlprice");
                                            JSONObject chk_payttotal = root.getJSONObject("chk_payttotal");
                                            String totalamt = chk_payttotal.getString("totalamt");
                                            String status = chk_payttotal.getString("status");
                                            String paid_orderstatus = root.getString("paid_orderstatus");
                                            JSONObject final_info = root.getJSONObject("final_info");
                                            String msg = final_info.getString("msg");
                                            String cartamount = final_info.getString("cartamount");
                                            String user_paidamount = final_info.getString("user_paidamount");
                                            String due_or_extra_amount = final_info.getString("due_or_extra_amount");
                                            String amount_alert = final_info.getString("amount_alert");

                                            add_payment_msg.setText(amount_alert+"("+due_or_extra_amount+")");
                                            progressDialog.dismiss();
                                        }
                                        else {
                                             Toast.makeText(AddPaymentActivity.this, " failure ", Toast.LENGTH_SHORT).show();
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
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
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
    public void setJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("transactionType", paymentType);
            jsonObj.put("bankrandid", bankRandId);
            jsonObj.put("transactionNo",cheque_no.getText().toString());
            jsonObj.put("paydate",SelectedDate);
            jsonObj.put("amount",totalprice);
            jsonObj.put("payId", orderId);
            jsonObj.put("ranid",randId);
            jsonObj.put("userid",user_id);

            androidNetworkingpayByBank(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingpayByBank(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/paybybank_api")
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
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,"Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                progressDialog.dismiss();
                                Intent i = new Intent(AddPaymentActivity.this, StoreReceivedOrdersActivity.class);
                                i.putExtra("Message","Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddPaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setContractJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("payId", orderId);
            jsonObj.put("ranid",randId);
            jsonObj.put("userid",user_id);
            jsonObj.put("bankrandid", bankRandId);
            jsonObj.put("amount", ProPrice);
            jsonObj.put("paydate",SelectedDate);
            jsonObj.put("transactionNo",cheque_no.getText().toString());
            jsonObj.put("transactionType", paymentType);

            androidNetworkingContractpayByBank(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingContractpayByBank(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/contractpaybybank_api/")
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
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,"Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                progressDialog.dismiss();
                                Intent i = new Intent(AddPaymentActivity.this, MyContractOrdersActivity.class);
                                i.putExtra("Message","Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours");
                                i.putExtra("PurchasedOrders","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddPaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setLogisticsJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("payId", orderId);
            jsonObj.put("ranid",randId);
            jsonObj.put("userid",user_id);
            jsonObj.put("bankrandid", bankRandId);
            jsonObj.put("amount", ProPrice);
            jsonObj.put("paydate",SelectedDate);
            jsonObj.put("transactionNo",cheque_no.getText().toString());
            jsonObj.put("transactionType", paymentType);

            androidNetworkingLogisticpayByBank(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingLogisticpayByBank(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/transportpaybybank_api/")
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
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,"Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                progressDialog.dismiss();
                                Intent i = new Intent(AddPaymentActivity.this, LogisticsOrdersActivity.class);
                                i.putExtra("Message","Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours");
                                i.putExtra("Logistics","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddPaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setContractsLogisticsJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("payId", orderId);
            jsonObj.put("ranid",randId);
            jsonObj.put("userid",user_id);
            jsonObj.put("bankrandid", bankRandId);
            jsonObj.put("amount", ProPrice);
            jsonObj.put("paydate",SelectedDate);
            jsonObj.put("transactionNo",cheque_no.getText().toString());
            jsonObj.put("transactionType", paymentType);

            androidNetworkingContractsLogisticpayByBank(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingContractsLogisticpayByBank(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/transportpaybybank_api/")
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
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,"Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                progressDialog.dismiss();
                                Intent i = new Intent(AddPaymentActivity.this, LogisticsOrdersActivity.class);
                                i.putExtra("Message","Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours");
                                i.putExtra("Logistics","0");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddPaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    private void getBankDetailsList() {
       // progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBankDetails();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("Success Call ", ">>>>" + response.body().toString());

                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                Log.d("Success Call", ">>>>" + call);

                if (response.body().toString() != null) {

                    if (response != null) {
                        String searchResponse = response.body().toString();
                        Log.d("Reg", "Response  >>" + searchResponse.toString());

                        if (searchResponse != null) {
                            JSONObject root = null;
                            try {
                                root = new JSONObject(searchResponse);
                                String success = null;
                                JSONObject jsonObject = root.getJSONObject("success");
                                success = jsonObject.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                 /*   if (progressDialog.isShowing())
                                        progressDialog.dismiss();*/
                                    JSONArray jsonArray = root.getJSONArray("data");
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String name = jsonObject1.getString("Account_Name");
                                        String number = jsonObject1.getString("Account_No");
                                        String ifsccode = jsonObject1.getString("IFSC_Code");
                                        String address = jsonObject1.getString("Branch_Address");
                                        String Note = jsonObject1.getString("Note");

                                        acc_name.setText(name);
                                        acc_no.setText(number);
                                        acc_ifsccode.setText(ifsccode);
                                        acc_address.setText(address);
                                        acc_note.setText(Note);
                                      /*  if (progressDialog.isShowing())
                                            progressDialog.dismiss();*/
                                    }

                                } else if (success.equalsIgnoreCase("0")) {
                                  /*  if (progressDialog.isShowing())
                                        progressDialog.dismiss();*/
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           /* if (progressDialog.isShowing())
                                progressDialog.dismiss();  */                      }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //Toast.makeText(BusinessCategory.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
               // progressDialog.dismiss();
            }
        });
    }


    // Adapter
    public class AddPaymentAdapter extends RecyclerView.Adapter<AddPaymentAdapter.ViewHolder> {
        //    public static HashMap<Integer, String> issuehashmap = new HashMap<>();
        ArrayList<CartListModels> paymentModels;
        Context context;
        PreferenceUtils preferenceUtils;
        private int adapterPosition = -1;

        public AddPaymentAdapter(Context context, ArrayList<CartListModels> paymentModels) {
            this.context = context;
            this.paymentModels = paymentModels;
        }

        @NonNull
        @Override
        public AddPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.my_contract_orders_item, parent, false);
            return new AddPaymentAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final AddPaymentAdapter.ViewHolder holder, final int position) {
             holder.ll_orders.setVisibility(View.GONE);
             holder.ll_add_payment.setVisibility(View.VISIBLE);
            order_status = paymentModels.get(position).getOrder_status();
            if(paymentModels.get(position) != null){
             holder.order_name_add.setText(paymentModels.get(position).getPr_title());
             holder.order_no_add.setText(paymentModels.get(position).getProduct_order_id());
             holder.order_amount_add.setText(paymentModels.get(position).getTotalprice());
                if(order_status.equalsIgnoreCase("0")){
                    holder.order_status_add.setText("Pending");
                }else if(order_status.equalsIgnoreCase("1")){
                    holder.order_status_add.setText("Complete");
                }
            }

        }

        @Override
        public int getItemCount() {
            return paymentModels.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_orders,ll_add_payment;
            TextView order_name_add,order_no_add,order_status_add,order_amount_add;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_orders = (LinearLayout) itemView.findViewById(R.id.ll_orders);
                ll_add_payment = (LinearLayout)itemView.findViewById(R.id.ll_add_payment);
                order_name_add = (TextView) itemView.findViewById(R.id.order_name_add);
                order_no_add = (TextView) itemView.findViewById(R.id.order_no_add);
                order_status_add = (TextView) itemView.findViewById(R.id.order_status_add);
                order_amount_add = (TextView) itemView.findViewById(R.id.order_amount_add);
            }
        }
    }


}
