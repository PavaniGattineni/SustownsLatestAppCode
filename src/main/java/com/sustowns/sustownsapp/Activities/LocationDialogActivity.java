package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.TransportApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.GetAddressModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationDialogActivity extends AppCompatActivity {
    ImageView backarrow_dialog;
    RecyclerView recyclerview_saved_addresses;
    Helper helper;
    public static TextView address_txt_map_dialog;
    TextView saved_address_text,title_address_change,spinner_countrydialog,address_state, address_town;
    String nameAddress="",companyAddress="", emailAddress="", firstnameAddress="", lastnameAddress="", address2Address="", addressState="", addressTown="", mobileAddress="", pincodeAddress="", faxAddress="";
    EditText name_address,company_address, email_address, first_name_address,last_name_address, address1_address, address2_address,mobile_address, pincode_address, fax_address;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> statesList = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
    int textlength = 0;
    ArrayList<String> selectedCountryList = new ArrayList<String>();
    ArrayList<String> selectedCountryIdList = new ArrayList<String>();
    PreferenceUtils preferenceUtils;
    String user_email,pro_id,user_id,user_role,order_status,pay_method,orderid,contractLocationStr="",InvoiceNoStr="",RandIdStr = "",clickedSearch = "";
    public static String Address = "";
    LinearLayout ll_shipping_details,ll_existing_address;
    RadioButton existing_radiobtn, new_radiobtn;
    RadioGroup radioGroup;
    Button save_address_btn,save_address,close_drop_dialog;
    String actionValue = "",countryId="",stateId="",cityId="",selectedAddress = "",UserId,productZipcode;
    ArrayList<GetAddressModel> getAddressModels;
    ExistingAddressAdapterContract existingAddressAdapter;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    SparseBooleanArray mSelectedItemsListIds = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.drop_location_dialog);
        try {
            initializeValues();
            initializeUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(LocationDialogActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        helper = new Helper(LocationDialogActivity.this);
        orderid = getIntent().getStringExtra("OrderId");
        contractLocationStr = getIntent().getStringExtra("ContractLocation");
        InvoiceNoStr = getIntent().getStringExtra("InvoiceNo");
        RandIdStr = getIntent().getStringExtra("RandId");
        UserId = getIntent().getStringExtra("UserId");
        productZipcode = getIntent().getStringExtra("productZipcode");
    }
    private void initializeUI() {
        ll_shipping_details = (LinearLayout) findViewById(R.id.ll_shipping_details);
        ll_existing_address = (LinearLayout) findViewById(R.id.ll_existing_address);
        recyclerview_saved_addresses = (RecyclerView) findViewById(R.id.recyclerview_saved_addresses);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(LocationDialogActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerview_saved_addresses.setLayoutManager(layoutManager1);
        saved_address_text = (TextView) findViewById(R.id.saved_address_text);
        save_address_btn = (Button) findViewById(R.id.save_address_btn);
        name_address = (EditText) findViewById(R.id.name_address);
        company_address = (EditText) findViewById(R.id.company_address);
        email_address = (EditText) findViewById(R.id.email_address);
        first_name_address = (EditText) findViewById(R.id.first_name_address);
        title_address_change = (TextView) findViewById(R.id.title_address_change);
        title_address_change.setVisibility(View.GONE);
        spinner_countrydialog = (TextView) findViewById(R.id.spinner_countrydialog);
        spinner_countrydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCountryList();
            }
        });
        last_name_address = (EditText) findViewById(R.id.last_name_address);
        address_txt_map_dialog = findViewById(R.id.address_txt_map_dialog);
        address_txt_map_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(LocationDialogActivity.this, MapsActivity.class);
                mapIntent.putExtra("activity", "product");
                mapIntent.putExtra("type", "none");
                mapIntent.putExtra("ServiceRadius", "");
                mapIntent.putExtra("ServiceExtendRadius", "");
                startActivity(mapIntent);
            }
        });
        address2_address = (EditText) findViewById(R.id.address2_address);
        address_state = (TextView) findViewById(R.id.address_state);
        address_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatesList();
            }
        });
        backarrow_dialog = (ImageView) findViewById(R.id.backarrow_dialog);
        address_town = (TextView) findViewById(R.id.address_town);
        address_town.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityList();
            }
        });
        mobile_address = (EditText) findViewById(R.id.mobile_address);
        pincode_address = (EditText) findViewById(R.id.pincode_address);
        fax_address = (EditText) findViewById(R.id.fax_address);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        existing_radiobtn = (RadioButton) findViewById(R.id.existing_radiobtn);
        existing_radiobtn.setChecked(true);
        ll_existing_address.setVisibility(View.VISIBLE);
        ll_shipping_details.setVisibility(View.GONE);
        getExistingAddresses();
        new_radiobtn = (RadioButton) findViewById(R.id.new_radiobtn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.existing_radiobtn:
                        actionValue = "existing";
                        ll_existing_address.setVisibility(View.VISIBLE);
                        ll_shipping_details.setVisibility(View.GONE);
                        getExistingAddresses();
                        // do operations specific to this selection
                        break;
                    case R.id.new_radiobtn:
                        actionValue = "new";
                        ll_shipping_details.setVisibility(View.VISIBLE);
                        ll_existing_address.setVisibility(View.GONE);
                        // do operations specific to this selection
                        break;
                }
            }
        });
        save_address = (Button) findViewById(R.id.save_address);
        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contractLocationStr.equalsIgnoreCase("1")) {
                    if(selectedAddress.equalsIgnoreCase("")||selectedAddress.equalsIgnoreCase("null")){
                        Toast.makeText(LocationDialogActivity.this, "Please select address", Toast.LENGTH_SHORT).show();
                    }else {
                        submitConfirmOrder();
                    }
                }else if(contractLocationStr.equalsIgnoreCase("0")){
                    if(selectedAddress.equalsIgnoreCase("")||selectedAddress.equalsIgnoreCase("null")){
                        Toast.makeText(LocationDialogActivity.this, "Please select address", Toast.LENGTH_SHORT).show();
                    }else {
                        saveExistingAddress();
                    }
                }else if(contractLocationStr.equalsIgnoreCase("2")){
                    if(orderid.equalsIgnoreCase("")||order_status.equalsIgnoreCase("null")){
                        Toast.makeText(LocationDialogActivity.this, "Please select address", Toast.LENGTH_SHORT).show();
                    }else {
                        submitConfirmTransportAddress();
                    }
                }
            }
        });
        save_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAddress = name_address.getText().toString().trim();
                companyAddress = company_address.getText().toString().trim();
                emailAddress = email_address.getText().toString().trim();
                firstnameAddress = first_name_address.getText().toString().trim();
                lastnameAddress = last_name_address.getText().toString().trim();
                address2Address = address2_address.getText().toString().trim();
                addressState = address_state.getText().toString().trim();
                addressTown = address_town.getText().toString().trim();
                mobileAddress = mobile_address.getText().toString().trim();
                pincodeAddress = pincode_address.getText().toString().trim();
                faxAddress = fax_address.getText().toString().trim();
                if(contractLocationStr.equalsIgnoreCase("1")) {
                    if (actionValue.equalsIgnoreCase("new")) {
                        if (firstnameAddress.isEmpty() || lastnameAddress.isEmpty() || address2Address.isEmpty() || mobileAddress.isEmpty() || pincodeAddress.isEmpty() ||
                                emailAddress.isEmpty() || Address.isEmpty()) {
                            Toast.makeText(LocationDialogActivity.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
                        } else {
                            submitConfirmOrder();
                        }
                    } else {
                        if (firstnameAddress.isEmpty() || lastnameAddress.isEmpty() || address2Address.isEmpty() || mobileAddress.isEmpty() || pincodeAddress.isEmpty() ||
                                emailAddress.isEmpty()) {
                            Toast.makeText(LocationDialogActivity.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
                        } else {
                            submitConfirmOrder();
                        }
                    }
                }else if(contractLocationStr.equalsIgnoreCase("0")){
                    if (firstnameAddress.isEmpty() || lastnameAddress.isEmpty() || address2Address.isEmpty() || mobileAddress.isEmpty() || pincodeAddress.isEmpty() ||
                            emailAddress.isEmpty() || Address.isEmpty()) {
                        Toast.makeText(LocationDialogActivity.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
                    } else {
                        saveNewAddress();
                    }
                }else if(contractLocationStr.equalsIgnoreCase("2")){
                    if (actionValue.equalsIgnoreCase("new")) {
                        if (firstnameAddress.isEmpty() || lastnameAddress.isEmpty() || address2Address.isEmpty() || mobileAddress.isEmpty() || pincodeAddress.isEmpty() ||
                                emailAddress.isEmpty() || Address.isEmpty()) {
                            Toast.makeText(LocationDialogActivity.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
                        } else {
                            submitConfirmTransportAddress();
                        }
                    }
                    else {
                        if (firstnameAddress.isEmpty() || lastnameAddress.isEmpty() || address2Address.isEmpty() || mobileAddress.isEmpty() || pincodeAddress.isEmpty() ||
                                emailAddress.isEmpty()) {
                            Toast.makeText(LocationDialogActivity.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
                        } else {
                            submitConfirmTransportAddress();
                        }
                    }

                }
            }
        });
        close_drop_dialog = (Button) findViewById(R.id.close_drop_dialog);
        close_drop_dialog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        backarrow_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void submitConfirmOrder() {
        if(actionValue.equalsIgnoreCase("")){
            actionValue = "existing";
        }
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("orderid", orderid);
            jsonObj.put("action",actionValue);
            jsonObj.put("addres_id",selectedAddress);
            jsonObj.put("seller_zipcode",pincodeAddress);
            jsonObj.put("seller_address",address2Address);
            jsonObj.put("userid",user_id);
            jsonObj.put("displayname",nameAddress);
            jsonObj.put("companyname",companyAddress);
            jsonObj.put("fname",firstnameAddress);
            jsonObj.put("lname",lastnameAddress);
            jsonObj.put("email",emailAddress);
            jsonObj.put("address1",address2Address);
            jsonObj.put("address2",Address);
            jsonObj.put("zipcode",pincodeAddress);
            jsonObj.put("country",countryId);
            jsonObj.put("state",stateId);
            jsonObj.put("city",cityId);
            jsonObj.put("phone",mobileAddress);
            jsonObj.put("fax",faxAddress);

            androidNetworkingConfirm(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingConfirm(JSONObject jsonObject){
        //progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/confirmorder")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(LocationDialogActivity.this, message, Toast.LENGTH_SHORT).show();
                                // ((MyContractOrdersActivity)context).getContractOrdersList();
                                Intent i = new Intent(LocationDialogActivity.this,MyContractOrdersActivity.class);
                                i.putExtra("Message","");
                                startActivity(i);
                            } else {
                                Toast.makeText(LocationDialogActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                    }
                });
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(LocationDialogActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void submitConfirmTransportAddress() {
        if(actionValue.equalsIgnoreCase("")){
            actionValue = "existing";
        }
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("orderid", orderid);
            jsonObj.put("ran_id",RandIdStr);
            jsonObj.put("action",actionValue);
            jsonObj.put("addres_id",selectedAddress);
            jsonObj.put("buyer_zipcode",pincodeAddress);
            jsonObj.put("buyer_address",address2Address);
            jsonObj.put("userid",user_id);
            jsonObj.put("displayname",nameAddress);
            jsonObj.put("companyname",companyAddress);
            jsonObj.put("fname",firstnameAddress);
            jsonObj.put("lname",lastnameAddress);
            jsonObj.put("email",emailAddress);
            jsonObj.put("address1",address2Address);
            jsonObj.put("address2",Address);
            jsonObj.put("zipcode",pincodeAddress);
            jsonObj.put("country",countryId);
            jsonObj.put("state",stateId);
            jsonObj.put("city",cityId);
            jsonObj.put("phone",mobileAddress);
            jsonObj.put("fax",faxAddress);

            androidNetworkingTransportAddress(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingTransportAddress(JSONObject jsonObject){
        // progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Storemanagementser/store_addtransport")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(LocationDialogActivity.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                                // ((MyContractOrdersActivity)context).getContractOrdersList();
                                Intent i = new Intent(LocationDialogActivity.this,AddTransportActivity.class);
                                i.putExtra("OrderId",RandIdStr);
                                i.putExtra("InvoiceNo",InvoiceNoStr);
                                i.putExtra("ContractTransport","0");
                                startActivity(i);
                            } else {
                                Toast.makeText(LocationDialogActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                    }
                });
    }
    private void getExistingAddresses() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.productDetails(user_id, pro_id);
        callRetrofit = service.getExistingAddress(user_id);

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
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("existing");
                                            getAddressModels = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                nameAddress = jsonObject.getString("name");
                                                companyAddress =jsonObject.getString("companyname");
                                                firstnameAddress = jsonObject.getString("fname");
                                                lastnameAddress = jsonObject.getString("lname");
                                                emailAddress = jsonObject.getString("email");
                                                address2Address = jsonObject.getString("address1");
                                                String address2 = jsonObject.getString("address2");
                                                pincodeAddress = jsonObject.getString("zipcode");
                                                countryId = jsonObject.getString("country");
                                                stateId = jsonObject.getString("state");
                                                cityId = jsonObject.getString("city");
                                                mobileAddress = jsonObject.getString("mobile");
                                                faxAddress = jsonObject.getString("fax");
                                                String country_name = jsonObject.getString("country_name");
                                                String city_name = jsonObject.getString("city_name");

                                                GetAddressModel getAddressModel = new GetAddressModel();
                                                getAddressModel.setId(id);
                                                getAddressModel.setUser_id(user_id);
                                                getAddressModel.setName(nameAddress);
                                                getAddressModel.setCompanyname(companyAddress);
                                                getAddressModel.setFname(firstnameAddress);
                                                getAddressModel.setLname(lastnameAddress);
                                                getAddressModel.setEmail(emailAddress);
                                                getAddressModel.setAddress1(address2Address);
                                                getAddressModel.setAddress2(address2);
                                                getAddressModel.setZipcode(pincodeAddress);
                                                getAddressModel.setCountry(countryId);
                                                getAddressModel.setState(stateId);
                                                getAddressModel.setCity(cityId);
                                                getAddressModel.setMobile(mobileAddress);
                                                getAddressModel.setFax(faxAddress);
                                                getAddressModel.setCountry_name(country_name);
                                                getAddressModel.setCity_name(city_name);
                                                getAddressModels.add(getAddressModel);

                                            }
                                            if (getAddressModels != null) {
                                                saved_address_text.setVisibility(View.GONE);
                                                existingAddressAdapter = new ExistingAddressAdapterContract(LocationDialogActivity.this,getAddressModels,mSelectedItemsListIds);
                                                recyclerview_saved_addresses.setAdapter(existingAddressAdapter);
                                                existingAddressAdapter.notifyDataSetChanged();
                                            }else{
                                                saved_address_text.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            saved_address_text.setVisibility(View.VISIBLE);
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
    private void saveNewAddress() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);
        nameAddress = name_address.getText().toString().trim();
        companyAddress = company_address.getText().toString().trim();
        emailAddress = email_address.getText().toString().trim();
        firstnameAddress = first_name_address.getText().toString().trim();
        lastnameAddress = last_name_address.getText().toString().trim();
        address2Address = address2_address.getText().toString().trim();
        addressState = address_state.getText().toString().trim();
        addressTown = address_town.getText().toString().trim();
        mobileAddress = mobile_address.getText().toString().trim();
        pincodeAddress = pincode_address.getText().toString().trim();
        faxAddress = fax_address.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.addNewAddress(user_id,UserId,orderid,productZipcode,"","",nameAddress,companyAddress,firstnameAddress, lastnameAddress,
                emailAddress,Address,address2Address,pincodeAddress,countryId,stateId,cityId,mobileAddress,faxAddress,"","");

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
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            progressDialog.dismiss();
                                            Intent i = new Intent(LocationDialogActivity.this,ProductDetailsActivity.class);
                                            i.putExtra("Pro_Id",orderid);
                                            i.putExtra("Image","");
                                            i.putExtra("Status","2");
                                            i.putExtra("StoreMgmt","");
                                            i.putExtra("PincodeAddress",pincodeAddress);
                                            startActivity(i);
                                            Toast.makeText(LocationDialogActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(LocationDialogActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void saveExistingAddress() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.sentAddress(user_id,UserId,orderid,pincodeAddress,"","",selectedAddress,productZipcode);

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
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            progressDialog.dismiss();
                                            // customdialog.dismiss();
                                            Intent i = new Intent(LocationDialogActivity.this,ProductDetailsActivity.class);
                                            i.putExtra("Pro_Id",orderid);
                                            i.putExtra("Image","");
                                            i.putExtra("Status","2");
                                            i.putExtra("StoreMgmt","");
                                            startActivity(i);
                                            //drop_location.setText(radioText);
                                            Toast.makeText(LocationDialogActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(LocationDialogActivity.this, message, Toast.LENGTH_SHORT).show();
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
    private void getCountryList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCountries();

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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("country");
                                    countryList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        countryList.add(jsonObject.getString("country_name"));
                                        idList.add(jsonObject.getString("CountryCode"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(true, countryList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
    private void getStatesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getStates(countryId);

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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("states");
                                    statesList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        statesList.add(jsonObject.getString("subdivision_1_name"));
                                        idList.add(jsonObject.getString("subdivision_1_iso_code"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(false,statesList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                progressDialog.dismiss();
            }
        });
    }
    private void getCityList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCities(countryId,stateId);

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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("cities");
                                    citiesList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        idList.add(jsonObject.getString("city_id"));
                                        citiesList.add(jsonObject.getString("city_name"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialogCity(citiesList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
    private void showAlertDialog( final boolean isCountry, final List<String> countryList,
                                  final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LocationDialogActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);
            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            if (isCountry)
                title.setText("Choose Country");
            else
                title.setText("Choose State");
            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
            alertDialog = dialogBuilder.create();
            if (countryList.size() == 0) {
                if (alertDialog != null)
                    alertDialog.dismiss();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(LocationDialogActivity.this,
                    R.layout.simple_list_item, R.id.list_item_txt, countryList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    // adapter.getFilter().filter(cs);
                    clickedSearch = "clicked";
                    if (isCountry) {
                        textlength = inputSearch.getText().length();
                        selectedCountryList.clear();
                        selectedCountryIdList.clear();
                        for (int i = 0; i < countryList.size(); i++) {
                            if (textlength <= countryList.get(i).length()) {
                                Log.d("ertyyy", countryList.get(i).toLowerCase().trim());
                                if (countryList.get(i).toLowerCase().trim().contains(
                                        inputSearch.getText().toString().toLowerCase().trim())) {
                                    selectedCountryList.add(countryList.get(i));
                                    selectedCountryIdList.add(idList.get(i));
                                }
                            }
                        }
                    }else{
                        textlength = inputSearch.getText().length();
                        selectedCountryList.clear();
                        selectedCountryIdList.clear();
                        for (int i = 0; i < statesList.size(); i++) {
                            if (textlength <= statesList.get(i).length()) {
                                Log.d("ertyyy", statesList.get(i).toLowerCase().trim());
                                if (statesList.get(i).toLowerCase().trim().contains(
                                        inputSearch.getText().toString().toLowerCase().trim())) {
                                    selectedCountryList.add(statesList.get(i));
                                    selectedCountryIdList.add(idList.get(i));
                                }
                            }
                        }
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(LocationDialogActivity.this,
                            R.layout.simple_list_item, R.id.list_item_txt, selectedCountryList);
                    // Assign adapter to ListView
                    categoryListView.setAdapter(adapter);
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
            clickedSearch = "not clicked";
            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (clickedSearch.equalsIgnoreCase("clicked")) {
                        if (isCountry) {
                            String itemValue = selectedCountryList.get(position);
                            spinner_countrydialog.setText(itemValue);
                            countryId = selectedCountryIdList.get(position);
                            // sp_country.setText(itemValue);
                            address_state.setText("");
                            address_state.setHint("Choose State");
                            //  countryId = idList.get(position);
                            alertDialog.dismiss();
                        } else {
                            String itemValue = selectedCountryList.get(position);
                       /* sp_state.setText(itemValue);
                        stateId = idList.get(position);*/
                            address_state.setText(itemValue);
                            stateId = selectedCountryIdList.get(position);
                            alertDialog.dismiss();
                        }
                    }else{
                        if (isCountry) {
                            String itemValue = countryList.get(position);
                            spinner_countrydialog.setText(itemValue);
                            address_state.setText("");
                            address_state.setHint("Choose State");
                            countryId = idList.get(position);
                            alertDialog.dismiss();
                        } else {
                            String itemValue = countryList.get(position);
                            address_state.setText(itemValue);
                            stateId = idList.get(position);
                            alertDialog.dismiss();
                        }
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlertDialogCity(final List<String> cityList,
                                     final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LocationDialogActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose City");

            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
            alertDialog = dialogBuilder.create();
            if (cityList.size() == 0) {
                if (alertDialog != null)
                    alertDialog.dismiss();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(LocationDialogActivity.this,
                    R.layout.simple_list_item, R.id.list_item_txt, cityList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    clickedSearch = "clicked";
                    textlength = inputSearch.getText().length();
                    selectedCountryList.clear();
                    selectedCountryIdList.clear();
                    for (int i = 0; i < cityList.size(); i++) {
                        if (textlength <= cityList.get(i).length()) {
                            Log.d("ertyyy", cityList.get(i).toLowerCase().trim());
                            if (cityList.get(i).toLowerCase().trim().contains(
                                    inputSearch.getText().toString().toLowerCase().trim())) {
                                selectedCountryList.add(cityList.get(i));
                                selectedCountryIdList.add(idList.get(i));
                            }
                        }
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(LocationDialogActivity.this,
                            R.layout.simple_list_item, R.id.list_item_txt, selectedCountryList);
                    // Assign adapter to ListView
                    categoryListView.setAdapter(adapter);
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
            clickedSearch = "not clicked";
            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (clickedSearch.equalsIgnoreCase("clicked")) {
                        String itemValue = selectedCountryList.get(position);
                        address_town.setText(itemValue);
                        cityId = selectedCountryIdList.get(position);
                        alertDialog.dismiss();
                    }else{
                        String itemValue = cityList.get(position);
                        address_town.setText(itemValue);
                        cityId = idList.get(position);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        address_txt_map_dialog.setText(Address);
    }
    public class ExistingAddressAdapterContract extends RecyclerView.Adapter<LocationDialogActivity.ExistingAddressAdapterContract.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, user_id;
        PreferenceUtils preferenceUtils;
        ArrayList<GetAddressModel> countryModels;
        ProgressDialog progressDialog;
        SparseBooleanArray mSelectedItemsListIds;
        private int lastCheckedPosition = -1;

        public ExistingAddressAdapterContract(Context context, ArrayList<GetAddressModel> countryModels, SparseBooleanArray mSelectedItemsListIds) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.countryModels = countryModels;
            this.mSelectedItemsListIds = mSelectedItemsListIds;
            setHasStableIds(true);
        }
        public ExistingAddressAdapterContract(){
            //Empty Constructor
        }
        @Override
        public LocationDialogActivity.ExistingAddressAdapterContract.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.markets_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new LocationDialogActivity.ExistingAddressAdapterContract.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final LocationDialogActivity.ExistingAddressAdapterContract.ViewHolder viewHolder, final int position) {
            viewHolder.ll_existing_addresses.setVisibility(View.VISIBLE);
            viewHolder.ll_markets.setVisibility(View.GONE);
            viewHolder.name_address.setText(getAddressModels.get(position).getName());
            viewHolder.address_text.setText(getAddressModels.get(position).getAddress1()+"\n"+getAddressModels.get(position).getCity_name()+","+
                    getAddressModels.get(position).getState()+"\n"+getAddressModels.get(position).getCountry_name()+","+
                    getAddressModels.get(position).getZipcode());
            viewHolder.name_address.setChecked(position == lastCheckedPosition);
            if (mSelectedItemsListIds.get(position)) {
                viewHolder.name_address.setChecked(true);
            }
            viewHolder.name_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mSelectedItemsListIds.put(position, true);
                        if(selectedAddress != null || !selectedAddress.isEmpty())
                            selectedAddress = countryModels.get(position).getId();
                    } else {
                        mSelectedItemsListIds.delete(position);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return countryModels.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton name_address;
            RadioGroup radioGroup;
            LinearLayout ll_existing_addresses,ll_markets;
            TextView address_text;
            public ViewHolder(View view) {
                super(view);
                radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
                ll_existing_addresses = (LinearLayout) view.findViewById(R.id.ll_existing_addresses);
                ll_markets = (LinearLayout) view.findViewById(R.id.ll_markets);
                address_text = (TextView) view.findViewById(R.id.address_text);
                name_address = view.findViewById(R.id.name_address);
                name_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastCheckedPosition = getAdapterPosition();
                        //because of this blinking problem occurs so
                        //i have a suggestion to add notifyDataSetChanged();
                        notifyItemRangeChanged(0, countryModels.size());//blink list problem
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
