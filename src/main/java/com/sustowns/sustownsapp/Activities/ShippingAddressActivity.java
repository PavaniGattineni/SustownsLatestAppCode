package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Api.WebServices;
import com.sustowns.sustownsapp.Models.AddToCartModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShippingAddressActivity extends AppCompatActivity {
    ImageView backarrow, savearrow;
    PreferenceUtils preferenceUtils;
    Spinner spinner_country, spinner_country_billing;
    Button place_order_btn, close_dialog;
    String countryStr, mobile, countryStrBilling, order_id, title, id, quantity, price;
    String name, company_name, email, first_name, last_name, address1, address2, state, town, pincode, fax, country_st, user_id, selectedRadioBtn;
    String[] country = {"India", "Algeria", "USA", "UK"};
    RadioButton radioButton,cards_checkbox,netbanking_checkbox,upi_checkbox,payumoney_checkbox;
    RadioGroup radioGroup,payu_radigroup;
    ProgressDialog progressDialog;
    CheckBox checkbox, checkbox_agree;
    Integer selectedId;
    Realm realm;
    LinearLayout ll_bank_details, ll_payment_gateway_temscond, ll_logistics_tems_conditions, ll_vendor_tems_conditions;
    TextView paybybank_orderstatus,acc_name, acc_no, acc_ifsccode, acc_address,terms_conditions, acc_note,name_payment,email_payment,phone_payment,total_items_txt,total_amount_txt;;
    ArrayList<AddToCartModel> addToCartModels;
    String billing_company, billing_email, billing_fname, billing_lname, billing_address1, billing_address2, billing_postalcode,
            billing_state, billing_town, billing_mobile, billing_fax;
    EditText company_billing, email_billing, first_name_billing, last_name_billing, address1_billing, address2_billing,
            state_billing, city_billing, mobile_billing, pincode_billing, fax_billing;
    EditText name_address, company_address, email_address, first_name_address, last_name_address, address1_address, address2_address,
            address_state, address_town, pincode_address, fax_address, mobile_address;
    ArrayList<String> countries;
    ArrayList<AddToCartModel> myList;
    RadioButton paybank_radiobtn, payu_radiobutton;
    String paymentType = "",userName,userEmail,userMobile,totalAmount,totalItems,PayUorderid;

    private String merchantKey = "swpahz", salt = "h7dXPGlF", transactionId, userCredentials;
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private PayUChecksum checksum;
    EditText note_orders;
    WebServices webServices;
    Helper helper;
    LinearLayout ll_payu_options;
    String bankCode="",mihpayid="",mode="",status,txnid="" ,amount = "",net_amount_debit="",firstname,phone,hash,payment_source,PG_TYPE="",bank_ref_num="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shipping_address);

        try {
            initializeUI();
            initializeValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // getCountryList();

    }

    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(ShippingAddressActivity.this);
        helper = new Helper(ShippingAddressActivity.this);
        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);

        int environment = PayuConstants.PRODUCTION_ENV;
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        mPaymentParams = new PaymentParams();
    }

    private void initializeUI() {
        userName = preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,"");
        userEmail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
        userMobile = preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,"");
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        // realm = Realm.getDefaultInstance();
        myList = getIntent().getParcelableExtra("CartList");
        totalAmount = getIntent().getStringExtra("TotalAmount");
        totalItems = getIntent().getStringExtra("TotalItems");
        name_payment = (TextView) findViewById(R.id.name_payment);
        email_payment = (TextView) findViewById(R.id.email_payment);
        phone_payment = (TextView) findViewById(R.id.phone_payment);
        name_payment.setText(userName);
        email_payment.setText(userEmail);
        phone_payment.setText(userMobile);
        total_items_txt = (TextView) findViewById(R.id.total_items_txt);
        total_amount_txt = (TextView) findViewById(R.id.total_amount_txt);
        total_items_txt.setText(totalItems);
        total_amount_txt.setText(totalAmount);
        note_orders = (EditText) findViewById(R.id.note_orders);
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
//        ll_payu_options = (LinearLayout) findViewById(R.id.ll_payu_options);
        acc_name = (TextView) findViewById(R.id.bank_account_name);
        acc_no = (TextView) findViewById(R.id.bank_account_no);
        acc_ifsccode = (TextView) findViewById(R.id.bank_ifsccode);
        acc_address = (TextView) findViewById(R.id.branch_address);
        acc_note = (TextView) findViewById(R.id.note_bank);
        paybybank_orderstatus = (TextView) findViewById(R.id.paybybank_orderstatus);
        paybybank_orderstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShippingAddressActivity.this,StoreReceivedOrdersActivity.class);
                i.putExtra("Message","");
                startActivity(i);
            }
        });
        terms_conditions = (TextView) findViewById(R.id.terms_conditions);
        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(ShippingAddressActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.terms_conditions_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                ll_payment_gateway_temscond = (LinearLayout) customdialog.findViewById(R.id.ll_payment_gateway_temscond);
                ll_logistics_tems_conditions = (LinearLayout) customdialog.findViewById(R.id.ll_logistics_tems_conditions);
                ll_vendor_tems_conditions = (LinearLayout) customdialog.findViewById(R.id.ll_vendor_tems_conditions);
                ll_payment_gateway_temscond.setVisibility(View.VISIBLE);
                ll_logistics_tems_conditions.setVisibility(View.GONE);
                ll_vendor_tems_conditions.setVisibility(View.GONE);
                close_dialog = (Button) customdialog.findViewById(R.id.close_dialog1);
                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        checkbox_agree = (CheckBox) findViewById(R.id.checkbox_agree);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    company_billing.setText(company_address.getText().toString().trim());
                    email_billing.setText(email_address.getText().toString().trim());
                    first_name_billing.setText(first_name_address.getText().toString().trim());
                    last_name_billing.setText(last_name_address.getText().toString().trim());
                    address1_billing.setText(address1_address.getText().toString().trim());
                    address2_billing.setText(address2_address.getText().toString().trim());
                    state_billing.setText(address_state.getText().toString().trim());
                    city_billing.setText(address_town.getText().toString().trim());
                    mobile_billing.setText(mobile_address.getText().toString().trim());
                    pincode_billing.setText(pincode_address.getText().toString().trim());
                    fax_billing.setText(fax_address.getText().toString().trim());
                }
            }
        });
        cards_checkbox = (RadioButton) findViewById(R.id.cards_checkbox);
        netbanking_checkbox = (RadioButton) findViewById(R.id.netbanking_checkbox);
        upi_checkbox = (RadioButton) findViewById(R.id.upi_checkbox);
        payumoney_checkbox = (RadioButton) findViewById(R.id.payumoney_checkbox);

        company_billing = (EditText) findViewById(R.id.company_billing);
        email_billing = (EditText) findViewById(R.id.email_billing);
        first_name_billing = (EditText) findViewById(R.id.first_name_billing);
        last_name_billing = (EditText) findViewById(R.id.last_name_billing);
        address1_billing = (EditText) findViewById(R.id.address1_billing);
        address2_billing = (EditText) findViewById(R.id.address2_billing);
        pincode_billing = (EditText) findViewById(R.id.postal_code_billing);
        state_billing = (EditText) findViewById(R.id.state_billing);
        city_billing = (EditText) findViewById(R.id.city_billing);
        mobile_billing = (EditText) findViewById(R.id.mobile_billing);
        fax_billing = (EditText) findViewById(R.id.fax_billing);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        payu_radigroup = (RadioGroup) findViewById(R.id.payu_radigroup);
        payu_radiobutton = (RadioButton) findViewById(R.id.pay_u_radiobtn);
        paybank_radiobtn = (RadioButton) findViewById(R.id.paybank_radiobtn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cards_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.netbanking_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.upi_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.payumoney_checkbox:
                        ll_bank_details.setVisibility(View.GONE);
                        // do operations specific to this selection
                        paymentType = "online";
                        break;
                    case R.id.paybank_radiobtn:
                        ll_bank_details.setVisibility(View.VISIBLE);
                        paymentType = "bank";
                        getBankDetailsList();
                        // do operations specific to this selection
                        break;
                }
            }
        });
        backarrow = (ImageView) findViewById(R.id.backarrow);
        place_order_btn = (Button) findViewById(R.id.place_order_btn);
        spinner_country_billing = (Spinner) findViewById(R.id.spinner_country_billing);
        ArrayAdapter aa1 = new ArrayAdapter(ShippingAddressActivity.this, android.R.layout.simple_spinner_item, country);
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
        ArrayAdapter aa = new ArrayAdapter(ShippingAddressActivity.this, android.R.layout.simple_spinner_item, country);
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
        place_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                // selectedRadioBtn = radioButton.getText().toString();
                if (selectedId < 0) {
                    Toast.makeText(ShippingAddressActivity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                } /*else if (name.equalsIgnoreCase("") || company_name.equalsIgnoreCase("") || email.equalsIgnoreCase("") ||
                        first_name.equalsIgnoreCase("") || last_name.equalsIgnoreCase("") || address1.equalsIgnoreCase("") || address2.equalsIgnoreCase("") || state.equalsIgnoreCase("") || town.equalsIgnoreCase("") ||
                        mobile.equalsIgnoreCase("") || pincode.equalsIgnoreCase("") || billing_company.equalsIgnoreCase("") || billing_email.equalsIgnoreCase("") ||
                        billing_fname.equalsIgnoreCase("") || billing_lname.equalsIgnoreCase("") || billing_address1.equalsIgnoreCase("") || billing_address2.equalsIgnoreCase("") ||
                        billing_state.equalsIgnoreCase("") || billing_town.equalsIgnoreCase("") || billing_mobile.equalsIgnoreCase("") || billing_postalcode.equalsIgnoreCase("")) {
                    Toast.makeText(ShippingAddressActivity.this, "Please Fill Empty Fields", Toast.LENGTH_SHORT).show();
                }*/ else if (checkbox_agree.isChecked()) {
                    if (paymentType.equalsIgnoreCase("online")) {
                        /*setParameter();
                        generateHashFromSDK(mPaymentParams, salt);*/
                        helper.showPayDialog(ShippingAddressActivity.this, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with online payment?",
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        // PayuBiz
                                        setPayuObject();
                                        setParameter();
                                        generateHashFromSDK(mPaymentParams, salt);
                                    }
                                }, new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                    } else {
                        //setJsonObject();
                        helper.showDialog(ShippingAddressActivity.this, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with pay by bank?",
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        setJsonObject();
    //                                        getHashFromSustownServer();
                                    }
                                }, new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                        //  payByBankDetails();
                    }
                } else {
                    Toast.makeText(ShippingAddressActivity.this, "To continue,you should agree terms and condditions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        savearrow = (ImageView) findViewById(R.id.savearrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(ShippingAddressActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void getBankDetailsList() {
        progressdialog();
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
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    JSONArray jsonArray = root.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
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
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    }

                                } else if (success.equalsIgnoreCase("0")) {
                                    if (progressDialog.isShowing())
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

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //Toast.makeText(BusinessCategory.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }
    public void setJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("note", note_orders.getText().toString());
            androidNetworkingQuote(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingQuote(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Shopping/paybybankapp")
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
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, "Thank you for Choosing Pay by Bank Option and further process to Check My Orders", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ShippingAddressActivity.this, StoreReceivedOrdersActivity.class);
                                i.putExtra("Message","Thank you for Choosing Pay by Bank Option and further process to Check My Orders");
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void setPayuObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            androidNetworkingPayU(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayU(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/placeorderserv/")
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
                            PayUorderid = responseObj.getString("orderid");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                progressDialog.dismiss();
                               /* Toast.makeText(ShippingAddressActivity.this, "Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ShippingAddressActivity.this, StoreReceivedOrdersActivity.class);
                                i.putExtra("Message","Thank you for Transaction and Sustowns Team Will Verify Transaction within 48 Hours");
                                startActivity(i);*/
                               // progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void setPayUSuccess() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",txnid);
            jsonObj.put("orderid",PayUorderid);
            jsonObj.put("amount",amount);
            jsonObj.put("mode",mode);
            jsonObj.put("mihpayid",mihpayid);
            jsonObj.put("discount","");
            jsonObj.put("net_amount",net_amount_debit);
            jsonObj.put("firstname",preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,""));
            jsonObj.put("email",preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,""));
            jsonObj.put("phone",preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,""));
            jsonObj.put("PG_TYPE",PG_TYPE);
            jsonObj.put("bank_ref_num",bank_ref_num);
            jsonObj.put("bankcode",bankCode);
            androidNetworkingPayUSuccess(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayUSuccess(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/suspayuSuccess/")
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
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, "", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ShippingAddressActivity.this, StoreReceivedOrdersActivity.class);
                                i.putExtra("Message","");
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void setPayUFailure() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("txnid",transactionId);
            jsonObj.put("orderid",PayUorderid);
            jsonObj.put("amount",totalAmount);
            jsonObj.put("mode","DC");
            jsonObj.put("mihpayid","");
            jsonObj.put("discount","");
            jsonObj.put("net_amount",net_amount_debit);
            jsonObj.put("firstname",preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,""));
            jsonObj.put("email",preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,""));
            jsonObj.put("phone",preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,""));
            jsonObj.put("PG_TYPE","");
            jsonObj.put("bank_ref_num","");
            jsonObj.put("bankcode","CC");
            androidNetworkingPayUFailure(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPayUFailure(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/suspayuFailure/")
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
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, "Payment fail", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ShippingAddressActivity.this, ShippingAddressActivity.class);
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ShippingAddressActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void setParameter() {
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(totalAmount);
        mPaymentParams.setProductInfo("Sustowns Product");

        mPaymentParams.setFirstName(preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME, ""));
        mPaymentParams.setEmail(preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL, ""));
        mPaymentParams.setPhone(preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE, ""));
        mPaymentParams.setAddress1(preferenceUtils.getStringFromPreference(PreferenceUtils.Cust_Address, ""));
        mPaymentParams.setZipCode(preferenceUtils.getStringFromPreference(PreferenceUtils.ZIPCODE, ""));
        //Set Your transaction ID. example- "USER ID "+System.currentTimeMillis()
        transactionId = System.currentTimeMillis()+"";
        mPaymentParams.setTxnId(transactionId);
        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */

        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

        mPaymentParams.setUdf1("udf1");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");
        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        userCredentials = merchantKey + ":" + "super";
        mPaymentParams.setUserCredentials(userCredentials);
        //mPaymentParams.setUserCredentials(PayuConstants.DEFAULT);
    }
    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());
        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }
        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();
        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }
        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }
        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }
        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }
    public void launchSdkUI(PayuHashes payuHashes) {
        mPaymentParams.setHash(payuHashes.getPaymentHash());
        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
                Log.d("PayU Data :\n", data.getStringExtra("result"));
                Log.d("PayU Full Data :\n", data+"");
                Map<String, String> map = Splitter.on(";").withKeyValueSeparator("=").split(data.getStringExtra("result").replace("&amp", ""));

                bankCode = map.get("bankcode");
                mihpayid = map.get("mihpayid");
                mode = map.get("mode");
                status = map.get("status");
                txnid  = map.get("txnid");
                amount = map.get("amount");
                net_amount_debit = map.get("net_amount_debit");
                firstname = map.get("firstname");
                email = map.get("email");
                phone = map.get("phone");
                hash = map.get("hash");
                payment_source = map.get("payment_source");
                PG_TYPE = map.get("PG_TYPE");
                bank_ref_num = map.get("bank_ref_num");
                Helper helper = new Helper(ShippingAddressActivity.this);
                helper.singleClickAlert(ShippingAddressActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Success", "Payment is successfully completed.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                setPayUSuccess();
                                //Write the next set of code in the success
                            }
                        });
            } else {
                setPayUFailure();
                // Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                */
/**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * *//*
                Log.d("PayU Data :\n", data.getStringExtra("result"));
                Log.d("PayU Full Data :\n", data+"");
                Map<String, String> map = Splitter.on(";").withKeyValueSeparator("=").split(data.getStringExtra("result").replace("&amp", ""));
                bankCode = map.get("bankcode");
                mihpayid = map.get("mihpayid");
                mode = map.get("mode");
                status = map.get("status");
                txnid  = map.get("txnid");
                amount = map.get("amount");
                net_amount_debit = map.get("net_amount_debit");
                firstname = map.get("firstname");
                email = map.get("email");
                phone = map.get("phone");
                hash = map.get("hash");
                payment_source = map.get("payment_source");
                PG_TYPE = map.get("PG_TYPE");
                bank_ref_num = map.get("bank_ref_num");
                Helper helper = new Helper(ShippingAddressActivity.this);
                helper.singleClickAlert(ShippingAddressActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Success", "Payment is successfully completed.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                setPayUSuccess();
                                //Write the next set of code in the success
                            }
                        });
            } else {
                setPayUFailure();
               // Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }
*/
}
