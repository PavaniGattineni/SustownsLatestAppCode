package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
    TextView franchise_signup, vendor_signup, forgot_password,transport_vendor_signup;
    CheckBox checkbox;
    ImageView eye_img;
    Button signinbtn;
    EditText login_username, login_password;
    String username, password, emailPattern,refreshedToken,device_id;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    LinearLayout ll_sign_up;
    boolean texttype = false;
    public static boolean isConnectingToInternet(android.content.Context context) {
        android.net.ConnectivityManager connectivity =
                (android.net.ConnectivityManager) context.getSystemService(
                        android.content.Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            android.net.NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == android.net.NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        preferenceUtils = new PreferenceUtils(SignInActivity.this);

        initializeUI();
        clickActions();
    }
    private void initializeUI() {
        franchise_signup = (TextView) findViewById(R.id.franchise_signup);
        vendor_signup = (TextView) findViewById(R.id.vendor_signup);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        signinbtn = (Button) findViewById(R.id.signinbtn);
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        ll_sign_up = (LinearLayout) findViewById(R.id.ll_sign_up);
        eye_img = (ImageView) findViewById(R.id.eye_img);
        login_username.setFocusableInTouchMode(false);
        login_username.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                login_username.setFocusableInTouchMode(true);
                login_username.requestFocus();
                return false;
            }
        });
        login_password.setFocusableInTouchMode(false);
        login_password.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                login_password.setFocusableInTouchMode(true);
                login_password.requestFocus();
                return false;
            }
        });

    }
    private void clickActions() {
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(SignInActivity.this, "CheckBox is clicked", Toast.LENGTH_LONG).show();
                    preferenceUtils.saveString(PreferenceUtils.isRemember, "yes");
                } else {
                    preferenceUtils.saveString(PreferenceUtils.isRemember, "no");
                }
            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = login_username.getText().toString().trim();
                password = login_password.getText().toString().trim();
                //  emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                preferenceUtils.saveString(PreferenceUtils.UserName, username);
                preferenceUtils.saveString(preferenceUtils.Password, password);
                if ((username.equalsIgnoreCase("")) || (password.equalsIgnoreCase(""))) {
                    Toast.makeText(SignInActivity.this, "enter all fields", Toast.LENGTH_LONG).show();
                } else if((password.length() > 5) && (password.length() < 15)) {
                    SignIn();
                }else{
                    Toast.makeText(SignInActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        eye_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (texttype) {
                    login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    texttype = false;
                } else {
                    login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    texttype = true;

                }
            }
        });
        ll_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpVendorActivity.class);
                startActivity(i);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(SignInActivity.this, ForgotPassword.class);
            startActivity(i);
        }
    });
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void SignIn() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        username = login_username.getText().toString().trim();
        password = login_password.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.Login(username, password);
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
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONObject jsonObject = root.getJSONObject("getuser");
                                            String id = jsonObject.getString("id");
                                            String access_token = jsonObject.getString("access_token");
                                            String unique_id = jsonObject.getString("unique_id");
                                            String fullname = jsonObject.getString("fullname");
                                            String username = jsonObject.getString("username");
                                            String email = jsonObject.getString("email");
                                            String password = jsonObject.getString("password");
                                            String phone = jsonObject.getString("phone");
                                            String branch = jsonObject.getString("branch");
                                            String account = jsonObject.getString("account");
                                            String ifsc = jsonObject.getString("ifsc");
                                            String aadharpan = jsonObject.getString("aadharpan");
                                            String address = jsonObject.getString("address");
                                            String city = jsonObject.getString("city");
                                            String state = jsonObject.getString("state");
                                            String remember = jsonObject.getString("remember");
                                            String town = jsonObject.getString("town");
                                            String country = jsonObject.getString("country");
                                            String zipcode = jsonObject.getString("zipcode");
                                            String location_id = jsonObject.getString("location_id");

                                            preferenceUtils.saveString(PreferenceUtils.UserName, username);
                                            preferenceUtils.saveString(PreferenceUtils.USER_EMAIL, email);
                                            preferenceUtils.saveString(PreferenceUtils.MOBILE, phone);
                                            preferenceUtils.saveString(PreferenceUtils.USER_ID, id);
                                            preferenceUtils.saveString(PreferenceUtils.FULL_NAME,fullname);
                                            preferenceUtils.saveString(PreferenceUtils.USER_ROLE,access_token);
                                            preferenceUtils.saveString(PreferenceUtils.Cust_Address,address);
                                            preferenceUtils.saveString(PreferenceUtils.ZIPCODE,zipcode);

                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            //  Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                            progressDialog.dismiss();
                                        } else {
                                            message = root.getString("message");
                                            Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    } else {
                        // Toast.makeText(SignInActivity.this, "Service not responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignInActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
