package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Api.WebServices;
import com.sustowns.sustownsapp.Models.AddToCartModel;
import com.sustowns.sustownsapp.helpers.Helper;

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

public class ShippingAddress extends AppCompatActivity {
    ImageView backarrow;
    PreferenceUtils preferenceUtils;
    AlertDialog alertDialog,alertDialog1;
    Button save_address_btn,close_dialog;
    String countryStr, mobile, clickedSearch = "", order_id, title, id, quantity, price;
    String name, company_name, email, state, user_id="",countryId="",stateId="",cityId="",selectedAddress = "";
    String[] country = {"India", "Algeria", "USA", "UK"};
    ProgressDialog progressDialog;
    CheckBox checkbox;
    TextView address_town, address_state, spinner_countrydialog, acc_address, acc_note;
    ArrayList<AddToCartModel> addToCartModels;
    String nameAddress="",companyAddress="", emailAddress="", firstnameAddress="", lastnameAddress="", address2Address="", addressState="", addressTown="", mobileAddress="", pincodeAddress="", faxAddress="";
    EditText name_address, company_address, email_address, first_name_address, last_name_address, address1_address, address2_address, pincode_address, fax_address, mobile_address;
    Helper helper;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> statesList = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
    int textlength = 0;
    ArrayList<String> selectedCountryList = new ArrayList<String>();
    ArrayList<String> selectedCountryIdList = new ArrayList<String>();
    public static String ShippingAddress = "";
    public static TextView address_txt_map_shipping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shipping_address2);

        try {
            initializeUI();
            initializeValues();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeValues() {
        helper = new Helper(ShippingAddress.this);
        preferenceUtils = new PreferenceUtils(ShippingAddress.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
       // user_id = getIntent().getStringExtra("userId");
    }

    private void initializeUI() {
        name_address = (EditText) findViewById(R.id.name_address_shipping);
        company_address = (EditText) findViewById(R.id.company_address_shipping);
        email_address = (EditText) findViewById(R.id.email_address_shipping);
        first_name_address = (EditText) findViewById(R.id.first_name_address_shipping);
        last_name_address = (EditText) findViewById(R.id.last_name_address_shipping);
        address2_address = (EditText) findViewById(R.id.address2_address_shipping);
        mobile_address = (EditText) findViewById(R.id.mobile_address_shipping);
        pincode_address = (EditText) findViewById(R.id.pincode_address_shipping);
        fax_address = (EditText) findViewById(R.id.fax_address_shipping);
        spinner_countrydialog = (TextView) findViewById(R.id.spinner_country);
        spinner_countrydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCountryList();
            }
        });
        address_town = (TextView) findViewById(R.id.address_town_shipping);
        address_town.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityList();
            }
        });
        address_state = (TextView) findViewById(R.id.address_state_shipping);
        address_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatesList();
            }
        });
        address_txt_map_shipping = (TextView) findViewById(R.id.address_txt_map_shipping);
        address_txt_map_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ShippingAddress.this, MapsActivity.class);
                mapIntent.putExtra("activity", "shipping");
                mapIntent.putExtra("type", "none");
                mapIntent.putExtra("ServiceRadius", "");
                mapIntent.putExtra("ServiceExtendRadius", "");
                startActivity(mapIntent);
            }
        });
        backarrow = findViewById(R.id.backarrow_shipping);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShippingAddress.this,BusinessProfileActivity.class);
                startActivity(i);
            }
        });
        save_address_btn = (Button) findViewById(R.id.save_address_shipping);
        save_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAddress = name_address.getText().toString().trim();
                companyAddress = company_address.getText().toString().trim();
                emailAddress = email_address.getText().toString().trim();
                firstnameAddress = first_name_address.getText().toString().trim();
                lastnameAddress = last_name_address.getText().toString().trim();
                address2Address = address2_address.getText().toString().trim();
                mobileAddress = mobile_address.getText().toString().trim();
                pincodeAddress = pincode_address.getText().toString().trim();
                faxAddress = fax_address.getText().toString().trim();
                if (firstnameAddress.isEmpty() || lastnameAddress.isEmpty() || address2Address.isEmpty() || mobileAddress.isEmpty() || pincodeAddress.isEmpty() ||
                        emailAddress.isEmpty()||ShippingAddress.isEmpty()) {
                    Toast.makeText(ShippingAddress.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveNewAddress();
                }
            }
        });
        edittextFocus();
    }

    private void edittextFocus() {
        name_address.setFocusableInTouchMode(false);
        name_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                name_address.setFocusableInTouchMode(true);
                name_address.requestFocus();
                return false;
            }
        });
        company_address.setFocusableInTouchMode(false);
        company_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                company_address.setFocusableInTouchMode(true);
                company_address.requestFocus();
                return false;
            }
        });
        email_address.setFocusableInTouchMode(false);
        email_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                email_address.setFocusableInTouchMode(true);
                email_address.requestFocus();
                return false;
            }
        });
        first_name_address.setFocusableInTouchMode(false);
        first_name_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                first_name_address.setFocusableInTouchMode(true);
                first_name_address.requestFocus();
                return false;
            }
        });
        last_name_address.setFocusableInTouchMode(false);
        last_name_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                last_name_address.setFocusableInTouchMode(true);
                last_name_address.requestFocus();
                return false;
            }
        });
        address1_address.setFocusableInTouchMode(false);
        address1_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address1_address.setFocusableInTouchMode(true);
                address1_address.requestFocus();
                return false;
            }
        });
        address2_address.setFocusableInTouchMode(false);
        address2_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address2_address.setFocusableInTouchMode(true);
                address2_address.requestFocus();
                return false;
            }
        });
        mobile_address.setFocusableInTouchMode(false);
        mobile_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mobile_address.setFocusableInTouchMode(true);
                mobile_address.requestFocus();
                return false;
            }
        });
        address_state.setFocusableInTouchMode(false);
        address_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address_state.setFocusableInTouchMode(true);
                address_state.requestFocus();
                return false;
            }
        });
        address_town.setFocusableInTouchMode(false);
        address_town.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address_town.setFocusableInTouchMode(true);
                address_town.requestFocus();
                return false;
            }
        });
        pincode_address.setFocusableInTouchMode(false);
        pincode_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pincode_address.setFocusableInTouchMode(true);
                pincode_address.requestFocus();
                return false;
            }
        });
        fax_address.setFocusableInTouchMode(false);
        fax_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fax_address.setFocusableInTouchMode(true);
                fax_address.requestFocus();
                return false;
            }
        });
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(ShippingAddress.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
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
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShippingAddress.this);
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
           /* final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);*/
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShippingAddress.this,
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShippingAddress.this,
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
    private void showAlertDialogCity(final List<String> cityList, final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShippingAddress.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose City");

            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
          /*  final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);*/
            alertDialog1 = dialogBuilder.create();
            if (cityList.size() == 0) {
                if (alertDialog1 != null)
                    alertDialog1.dismiss();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog1.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShippingAddress.this,
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShippingAddress.this,
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
                        alertDialog1.dismiss();
                    }else{
                        String itemValue = cityList.get(position);
                        address_town.setText(itemValue);
                        cityId = idList.get(position);
                        alertDialog1.dismiss();
                    }
                }
            });
            alertDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveNewAddress() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid",user_id);
            jsonObj.put("displayname",nameAddress);
            jsonObj.put("companyname",companyAddress);
            jsonObj.put("fname",firstnameAddress);
            jsonObj.put("lname",lastnameAddress);
            jsonObj.put("email",emailAddress);
            jsonObj.put("address1",address2Address);
            jsonObj.put("address2",ShippingAddress);
            jsonObj.put("zipcode",pincodeAddress);
            jsonObj.put("country",countryId);
            jsonObj.put("state",stateId);
            jsonObj.put("city",cityId);
            jsonObj.put("phone",mobileAddress);
            jsonObj.put("fax",faxAddress);

            androidNetworkingAddress(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingAddress(JSONObject jsonObject){
        // progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/addressdetails")
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
                                Toast.makeText(ShippingAddress.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                                // ((MyContractOrdersActivity)context).getContractOrdersList();
                                Intent i = new Intent(ShippingAddress.this,ShippingAddress.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(ShippingAddress.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        Toast.makeText(ShippingAddress.this, "Something went wrong!please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(ShippingAddress != null || !ShippingAddress.isEmpty()) {
            address_txt_map_shipping.setText(ShippingAddress);
        }
    }
}
