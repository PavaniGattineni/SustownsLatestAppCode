package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.AddToCartModel;

import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessShipping extends AppCompatActivity {
    ImageView backarrow, savearrow;
    PreferenceUtils preferenceUtils;
    Spinner spinner_country, spinner_country_billing;
    Button place_order_btn;
    String countryStr, mobile, countryStrBilling, order_id, title, id, quantity, price;
    String name, company_name, email, first_name, last_name, address1, address2, state, town, pincode, fax, country_st, user_id, selectedRadioBtn;
    String[] country = {"India", "Algeria", "USA", "UK"};
    ProgressDialog progressDialog;
    CheckBox checkbox;
    Integer selectedId;
    Realm realm;
    LinearLayout ll_bank_details;
    TextView acc_name, acc_no, acc_ifsccode, acc_address, acc_note;
    ArrayList<AddToCartModel> addToCartModels;
    String billing_company, billing_email, billing_fname, billing_lname, billing_address1, billing_address2, billing_postalcode,
            billing_state, billing_town, billing_mobile, billing_fax;
    EditText company_billing, email_billing, first_name_billing, last_name_billing, address1_billing, address2_billing,
            state_billing, city_billing, mobile_billing, pincode_billing, fax_billing;
    EditText name_address, company_address, email_address, first_name_address, last_name_address, address1_address, address2_address,
            address_state, address_town, pincode_address, fax_address, mobile_address;
    ArrayList<String> countries;
    ArrayList<AddToCartModel> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_shipping2);

        initializeUI();
        initializeValues();
    }


    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(BusinessShipping.this);
        realm = Realm.getDefaultInstance();
        addToCartModels = new ArrayList<>();
        myList = getIntent().getParcelableExtra("CartList");
        // cartList = (ArrayList<AddToCartModel>) getIntent().getSerializableExtra("CartList");

        //TODO Must write below code in your activity to set up initial context for PayU
    }

    private void initializeUI() {
        name_address = (EditText) findViewById(R.id.name_address);
        // shipping
        company_address = (EditText) findViewById(R.id.company_address);
        email_address = (EditText) findViewById(R.id.email_address);
        first_name_address = (EditText) findViewById(R.id.first_name_address);
        last_name_address = (EditText) findViewById(R.id.last_name_address);
        address1_address = (EditText) findViewById(R.id.address1_address);
        address2_address = (EditText) findViewById(R.id.address2_address);
        address_state = (EditText) findViewById(R.id.address_state);
        address_town = (EditText) findViewById(R.id.address_town);
        mobile_address = (EditText) findViewById(R.id.mobile_address);
        pincode_address = (EditText) findViewById(R.id.pincode_address);
        fax_address = (EditText) findViewById(R.id.fax_address);
        ll_bank_details = (LinearLayout) findViewById(R.id.ll_bank_details);
        acc_name = (TextView) findViewById(R.id.bank_account_name);
        acc_no = (TextView) findViewById(R.id.bank_account_no);
        acc_ifsccode = (TextView) findViewById(R.id.bank_ifsccode);
        acc_address = (TextView) findViewById(R.id.branch_address);
        acc_note = (TextView) findViewById(R.id.note_bank);

        // billing

      /*  shipping = (Button) findViewById(R.id.shipping_btn);
        biling = (Button) findViewById(R.id.billing_btn);*/
        backarrow = (ImageView) findViewById(R.id.backarrow);
        place_order_btn = (Button) findViewById(R.id.place_order_btn);
        spinner_country_billing = (Spinner) findViewById(R.id.spinner_country_billing);
        ArrayAdapter aa1 = new ArrayAdapter(BusinessShipping.this, android.R.layout.simple_spinner_item, country);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_country_billing.setAdapter(aa1);
        spinner_country_billing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_st = parent.getItemAtPosition(position).toString();
                preferenceUtils.saveString(PreferenceUtils.COUNTRY_BILLING, country_st);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        ArrayAdapter aa = new ArrayAdapter(BusinessShipping.this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_country.setAdapter(aa);
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_st = parent.getItemAtPosition(position).toString();
                preferenceUtils.saveString(PreferenceUtils.COUNTRY, country_st);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        edittextFocus();

        place_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_address.getText().toString().trim();
                company_name = company_address.getText().toString().trim();
                email = email_address.getText().toString().trim();
                first_name = first_name_address.getText().toString().trim();
                last_name = last_name_address.getText().toString().trim();
                address1 = address1_address.getText().toString().trim();
                address2 = address2_address.getText().toString().trim();
                state = address_state.getText().toString().trim();
                town = address_town.getText().toString().trim();
                mobile = mobile_address.getText().toString().trim();
                pincode = pincode_address.getText().toString().trim();
                fax = fax_address.getText().toString().trim();
                // billing
                billing_company = company_billing.getText().toString().trim();
                billing_email = email_billing.getText().toString().trim();
                billing_fname = first_name_billing.getText().toString().trim();
                billing_lname = last_name_billing.getText().toString().trim();
                billing_address1 = address1_billing.getText().toString().trim();
                billing_address2 = address2_billing.getText().toString().trim();
                billing_state = state_billing.getText().toString().trim();
                billing_town = city_billing.getText().toString().trim();
                billing_mobile = mobile_billing.getText().toString().trim();
                billing_postalcode = pincode_billing.getText().toString().trim();
                billing_fax = fax_billing.getText().toString().trim();

            }
        });
        savearrow = (ImageView) findViewById(R.id.savearrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getShippingAddressDetails();
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
        //
        company_billing.setFocusableInTouchMode(false);
        company_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                company_billing.setFocusableInTouchMode(true);
                company_billing.requestFocus();
                return false;
            }
        });
        email_billing.setFocusableInTouchMode(false);
        email_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                email_billing.setFocusableInTouchMode(true);
                email_billing.requestFocus();
                return false;
            }
        });
        first_name_billing.setFocusableInTouchMode(false);
        first_name_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                first_name_billing.setFocusableInTouchMode(true);
                first_name_billing.requestFocus();
                return false;
            }
        });
        last_name_billing.setFocusableInTouchMode(false);
        last_name_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                last_name_billing.setFocusableInTouchMode(true);
                last_name_billing.requestFocus();
                return false;
            }
        });
        address1_billing.setFocusableInTouchMode(false);
        address1_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address1_billing.setFocusableInTouchMode(true);
                address1_billing.requestFocus();
                return false;
            }
        });
        address2_billing.setFocusableInTouchMode(false);
        address2_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address2_billing.setFocusableInTouchMode(true);
                address2_billing.requestFocus();
                return false;
            }
        });
        mobile_billing.setFocusableInTouchMode(false);
        mobile_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mobile_billing.setFocusableInTouchMode(true);
                mobile_billing.requestFocus();
                return false;
            }
        });
        state_billing.setFocusableInTouchMode(false);
        state_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                state_billing.setFocusableInTouchMode(true);
                state_billing.requestFocus();
                return false;
            }
        });
        city_billing.setFocusableInTouchMode(false);
        city_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                city_billing.setFocusableInTouchMode(true);
                city_billing.requestFocus();
                return false;
            }
        });
        pincode_billing.setFocusableInTouchMode(false);
        pincode_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pincode_billing.setFocusableInTouchMode(true);
                pincode_billing.requestFocus();
                return false;
            }
        });
        fax_billing.setFocusableInTouchMode(false);
        fax_billing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fax_billing.setFocusableInTouchMode(true);
                fax_billing.requestFocus();
                return false;
            }
        });
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(BusinessShipping.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void getShippingAddressDetails() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getShippingAddress(user_id);

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
                                        Integer success;
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONObject jsonObject = root.getJSONObject("shipping_address");
                                            String id = jsonObject.getString("id");
                                            String user_id = jsonObject.getString("user_id");
                                            String name = jsonObject.getString("name");
                                            String companyname = jsonObject.getString("companyname");
                                            String fname = jsonObject.getString("fname");
                                            String lname = jsonObject.getString("lname");
                                            String email = jsonObject.getString("email");
                                            String address1 = jsonObject.getString("address1");
                                            String address2 = jsonObject.getString("address2");
                                            String zipcode = jsonObject.getString("zipcode");
                                            String country = jsonObject.getString("country");
                                            String state = jsonObject.getString("state");
                                            String city = jsonObject.getString("city");
                                            String mobile = jsonObject.getString("mobile");
                                            String fax = jsonObject.getString("fax");

                                            name_address.setText(name);
                                            company_address.setText(companyname);
                                            first_name_address.setText(fname);
                                            last_name_address.setText(lname);
                                            email_address.setText(email);
                                            address1_address.setText(address1);
                                            address2_address.setText(address2);
                                            address_state.setText(state);
                                            address_town.setText(city);
                                            mobile_address.setText(mobile);
                                            pincode_address.setText(zipcode);
                                            fax_address.setText(fax);
                                            preferenceUtils.saveString(PreferenceUtils.COUNTRY, country);
                                        } else {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            name_address.setText("");
                                            company_address.setText("");
                                            first_name_address.setText("");
                                            last_name_address.setText("");
                                            email_address.setText("");
                                            address1_address.setText("");
                                            address2_address.setText("");
                                            address_state.setText("");
                                            address_town.setText("");
                                            mobile_address.setText("");
                                            pincode_address.setText("");
                                            fax_address.setText("");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
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
}
