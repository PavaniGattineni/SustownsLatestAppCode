package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpClusterActivity extends AppCompatActivity {
    TextView registered;
    Spinner spin_selector;
    ArrayList<String> statesList = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
    EditText name_signup,bus_name_signup,mobile_signup,user_name,email_vendor,password_signup,confirm_password_signup,pincode_signup;
    Button signup_rural_producer,close_dialog2,close_dialog,register_btn;
    ProgressDialog progressDialog;
    String business,fullname,mobile,userName,email,password,emailPattern,confirm_password,selectedSector,countryId="IN",stateId="",cityId = "",categoryId = "",selectedString="";
    PreferenceUtils preferenceUtils;
    CheckBox checkbox_agree;
    ImageView close_icon,close_icon1,eye_img,eye_img1;
    TextView terms_conditions_text,sp_country,tv_sign_in,spinner_vendor_category,address_state, address_town;
    LinearLayout select_sector_ll,ll_vendor_category,ll_payment_gateway_temscond,ll_registersucessmsg,ll_logistics_tems_conditions,ll_vendor_tems_conditions,ll_register_details,linear_login;
    AlertDialog alertDialog;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> CategoriesList = new ArrayList<>();
    Helper helper;
    String clickedSearch,refreshedToken,device_id,coutry,clusterReg;
    int textlength = 0;
    ArrayList<String> selectedCountryList = new ArrayList<String>();
    ArrayList<String> selectedCountryIdList = new ArrayList<String>();
    ArrayList<String> selectedCategoryList = new ArrayList<String>();
    boolean texttype = false;
    Spinner spinner_country_reg;
    String[] Country = {"India"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vendor);
        preferenceUtils = new PreferenceUtils(SignUpClusterActivity.this);
        helper = new Helper(SignUpClusterActivity.this);
        try {
            ll_register_details = (LinearLayout) findViewById(R.id.ll_register_details);
            ll_registersucessmsg = (LinearLayout) findViewById(R.id.ll_registersucessmsg);
            select_sector_ll = (LinearLayout) findViewById(R.id.select_sector_ll);
            select_sector_ll.setVisibility(View.GONE);
            register_btn = (Button) findViewById(R.id.register_btn);
            signup_rural_producer = (Button) findViewById(R.id.signup_rural_producer);
            linear_login = (LinearLayout) findViewById(R.id.linear_login);
            tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
            name_signup = (EditText) findViewById(R.id.name_signup);
            bus_name_signup = (EditText) findViewById(R.id.bus_name_signup);
            mobile_signup = (EditText) findViewById(R.id.mobile_signup);
            user_name = (EditText) findViewById(R.id.user_name);
            email_vendor = (EditText) findViewById(R.id.email_vendor);
            password_signup = (EditText) findViewById(R.id.password_signup);
            pincode_signup = (EditText) findViewById(R.id.pincode_signup);
            confirm_password_signup = (EditText) findViewById(R.id.confirm_password_signup);
            registered = (TextView) findViewById(R.id.registered_login);
            registered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SignUpClusterActivity.this,SignInActivity.class);
                    startActivity(i);
                }
            });
            address_state = (TextView) findViewById(R.id.address_state);
            address_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getStatesList();
                }
            });
            address_town = (TextView) findViewById(R.id.address_town);
            address_town.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCityList();
                }
            });
            eye_img = (ImageView) findViewById(R.id.eye_img);
            eye_img1 = (ImageView) findViewById(R.id.eye_img1);
            checkbox_agree = (CheckBox) findViewById(R.id.checkbox_agree);
            ll_vendor_category = (LinearLayout) findViewById(R.id.ll_vendor_category);
            sp_country = (TextView) findViewById(R.id.spinner_country);
          /*  sp_country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCountryList();
                }
            });*/
            spinner_country_reg = (Spinner) findViewById(R.id.spinner_country_reg);
            ArrayAdapter country = new ArrayAdapter(SignUpClusterActivity.this, android.R.layout.simple_spinner_item, Country);
            country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_country_reg.setAdapter(country);
            spinner_country_reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    coutry = parent.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            terms_conditions_text = (TextView) findViewById(R.id.terms_conditions_text);
            terms_conditions_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog customdialog = new Dialog(SignUpClusterActivity.this);
                    customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    customdialog.setContentView(R.layout.terms_conditions_dialog);
                    customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                    ll_payment_gateway_temscond = (LinearLayout) customdialog.findViewById(R.id.ll_payment_gateway_temscond);
                    ll_logistics_tems_conditions = (LinearLayout) customdialog.findViewById(R.id.ll_logistics_tems_conditions);
                    ll_vendor_tems_conditions = (LinearLayout) customdialog.findViewById(R.id.ll_vendor_tems_conditions);
                    ll_payment_gateway_temscond.setVisibility(View.GONE);
                    ll_logistics_tems_conditions.setVisibility(View.VISIBLE);
                    ll_vendor_tems_conditions.setVisibility(View.GONE);
                    close_icon = (ImageView) customdialog.findViewById(R.id.close_icon);
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customdialog.dismiss();
                        }
                    });
                    close_dialog = (Button) customdialog.findViewById(R.id.close_dialog);
                    close_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customdialog.dismiss();
                        }
                    });
                    customdialog.show();
                }
            });
            eye_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (texttype) {
                        password_signup.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        texttype = false;
                    } else {
                        password_signup.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        texttype = true;

                    }
                }
            });
            eye_img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (texttype) {
                        confirm_password_signup.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        texttype = false;
                    } else {
                        confirm_password_signup.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        texttype = true;

                    }
                }
            });
            name_signup.setFocusableInTouchMode(false);
            name_signup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    name_signup.setFocusableInTouchMode(true);
                    name_signup.requestFocus();
                    return false;
                }
            });
            bus_name_signup.setFocusableInTouchMode(false);
            bus_name_signup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    bus_name_signup.setFocusableInTouchMode(true);
                    bus_name_signup.requestFocus();
                    return false;
                }
            });
            mobile_signup.setFocusableInTouchMode(false);
            mobile_signup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mobile_signup.setFocusableInTouchMode(true);
                    mobile_signup.requestFocus();
                    return false;
                }
            });
            user_name.setFocusableInTouchMode(false);
            user_name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    user_name.setFocusableInTouchMode(true);
                    user_name.requestFocus();
                    return false;
                }
            });
            email_vendor.setFocusableInTouchMode(false);
            email_vendor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    email_vendor.setFocusableInTouchMode(true);
                    email_vendor.requestFocus();
                    return false;
                }
            });
            password_signup.setFocusableInTouchMode(false);
            password_signup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    password_signup.setFocusableInTouchMode(true);
                    password_signup.requestFocus();
                    return false;
                }
            });
            pincode_signup.setFocusableInTouchMode(false);
            pincode_signup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pincode_signup.setFocusableInTouchMode(true);
                    pincode_signup.requestFocus();
                    return false;
                }
            });
            register_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 Intent i = new Intent(SignUpClusterActivity.this,ClusterDashboardActivity.class);
                 startActivity(i);
                }
            });
            signup_rural_producer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullname = name_signup.getText().toString().trim();
                    business = bus_name_signup.getText().toString().trim();
                    mobile = mobile_signup.getText().toString().trim();
                    userName = user_name.getText().toString().trim();
                    email = email_vendor.getText().toString().trim();
                    password = password_signup.getText().toString().trim();
                    confirm_password = confirm_password_signup.getText().toString().trim();
                    emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                        if ((fullname.equalsIgnoreCase("")) || (mobile.equalsIgnoreCase("") || (userName.equalsIgnoreCase("")) || (pincode_signup.getText().toString().equalsIgnoreCase("")) ||
                            (email.equalsIgnoreCase("")) || (password.equalsIgnoreCase("")) || (countryId.equalsIgnoreCase("")))) {
                            Toast.makeText(SignUpClusterActivity.this, "Please Fill Mandatory(*) Fields", Toast.LENGTH_LONG).show();
                        } else if (fullname.matches("\\d+(?:\\.\\d+)?") || business.matches("\\d+(?:\\.\\d+)?") || userName.matches("\\d+(?:\\.\\d+)?")) {
                            Toast.makeText(SignUpClusterActivity.this, "Name should be in Alaphabets..(ex:a-z)", Toast.LENGTH_SHORT).show();
                        } else if (mobile_signup.getText().toString().length() < 10) {
                            Toast.makeText(SignUpClusterActivity.this, "Invalid Mobile Number.Please enter valid number", Toast.LENGTH_SHORT).show();
                        } else if (pincode_signup.getText().toString().length() < 6 || pincode_signup.getText().toString().length() > 6) {
                            Toast.makeText(SignUpClusterActivity.this, "Invalid Pincode.Please enter valid pincode", Toast.LENGTH_SHORT).show();
                        } else if (!password.equalsIgnoreCase(confirm_password)) {
                            Toast.makeText(SignUpClusterActivity.this, "Please Enter Same Password", Toast.LENGTH_SHORT).show();
                        } else if ((email.matches(emailPattern)) && (password.length() > 5) && (password.length() < 15)) {
                            if (checkbox_agree.isChecked()) {
                                clusterRegistration();
                            } else {
                                Toast.makeText(SignUpClusterActivity.this, "To continue,you should agree terms and conditions", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpClusterActivity.this, "Invalid email or Check password length must be greater than 5", Toast.LENGTH_LONG).show();
                        }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(SignUpClusterActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getStatesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
       // callRetrofit = service.getStates(countryId);
        callRetrofit = service.getStates("IN");
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
                                    showAlertDialogState(statesList, idList);
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
        callRetrofit = service.getCities("IN",stateId);

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
    private void showAlertDialogState(final List<String> cityList,
                                     final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpClusterActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpClusterActivity.this,
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpClusterActivity.this,
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
                        address_state.setText(itemValue);
                        stateId = selectedCountryIdList.get(position);
                        alertDialog.dismiss();
                    }else{
                        String itemValue = statesList.get(position);
                        address_state.setText(itemValue);
                        stateId = idList.get(position);
                        alertDialog.dismiss();
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
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpClusterActivity.this);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpClusterActivity.this,
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpClusterActivity.this,
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
/*
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
                                    showAlertDialog(countryList, idList);
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
                Toast.makeText(SignUpVendorActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
*/
    private void getVendorCategoryList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getVendorCategoriesList();
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
                                    JSONArray jsonArray = root.getJSONArray("vendor_subcat");
                                    CategoriesList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        idList.add(jsonObject.getString("id"));
                                        CategoriesList.add(jsonObject.getString("vendorsubcat"));
                                    }
                                    progressDialog.dismiss();
                                    showAlertDialogVendorCategories(CategoriesList, idList);
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
                Toast.makeText(SignUpClusterActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void showAlertDialog(final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpClusterActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
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
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.simple_list_item, R.id.list_item_txt, countryList);
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpClusterActivity.this,
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
                    // selectedCountryList.add(position);
                    if (clickedSearch.equalsIgnoreCase("clicked")) {
                        String itemValue = selectedCountryList.get(position);
                        sp_country.setText(itemValue);
                        countryId = selectedCountryIdList.get(position);
                        alertDialog.dismiss();
                    }else{
                        String itemValue = countryList.get(position);
                        sp_country.setText(itemValue);
                        countryId = idList.get(position);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlertDialogVendorCategories(final List<String> categoryList, final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpClusterActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose Category");
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
            if (categoryList.size() == 0) {
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
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.simple_list_item, R.id.list_item_txt, categoryList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    clickedSearch = "clicked";
                    textlength = inputSearch.getText().length();
                    selectedCategoryList.clear();
                    selectedCountryIdList.clear();
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (textlength <= categoryList.get(i).length()) {
                            Log.d("ertyyy", categoryList.get(i).toLowerCase().trim());
                            if (categoryList.get(i).toLowerCase().trim().contains(
                                    inputSearch.getText().toString().toLowerCase().trim())) {
                                selectedCategoryList.add(categoryList.get(i));
                                selectedCountryIdList.add(idList.get(i));
                            }
                        }
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpClusterActivity.this,
                            R.layout.simple_list_item, R.id.list_item_txt, selectedCategoryList);
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
                    // selectedCountryList.add(position);
                    if (clickedSearch.equalsIgnoreCase("clicked")) {
                        String itemValue = selectedCategoryList.get(position);
                        spinner_vendor_category.setText(itemValue);
                        categoryId = selectedCategoryList.get(position);
                        alertDialog.dismiss();
                    }else{
                        String itemValue = categoryList.get(position);
                        spinner_vendor_category.setText(itemValue);
                        categoryId = categoryList.get(position);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void clusterRegistration() {
        progressdialog();
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        UserApi service = retrofit.create(UserApi.class);
        business = bus_name_signup.getText().toString().trim();
        fullname = name_signup.getText().toString().trim();
        mobile = mobile_signup.getText().toString().trim();
        userName = user_name.getText().toString().trim();
        email = email_vendor.getText().toString().trim();
        password = password_signup.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.clusterRegistration(fullname,userName,mobile,email,business,password,countryId,stateId,cityId,pincode_signup.getText().toString());
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
                                String success = null,message = null;
                                success = root.getString("success");
                                message = root.getString("message");
                                if (success.equalsIgnoreCase("1")) {
                                    String email = root.getString("email");
                                   /* tv_sign_in.setVisibility(View.VISIBLE);
                                    linear_login.setVisibility(View.VISIBLE);
                                    ll_register_details.setVisibility(View.GONE);
                                    ll_registersucessmsg.setVisibility(View.VISIBLE);*/
                                    //  registered.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(SignUpClusterActivity.this,SignInActivity.class);
                                    startActivity(i);
                                    Toast.makeText(SignUpClusterActivity.this, message, Toast.LENGTH_SHORT).show();
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    preferenceUtils.saveString(PreferenceUtils.USER_EMAIL,email);
                                }
                                else if (success.equalsIgnoreCase("0")) {
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(SignUpClusterActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignUpClusterActivity.this, "Something went wrong!please try again later", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }
}


