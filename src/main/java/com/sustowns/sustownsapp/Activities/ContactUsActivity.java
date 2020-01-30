package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ContactUsActivity extends AppCompatActivity {
    ImageView backarrow;
    EditText contact_name,contact_mobile,contact_email,contact_message;
    Button contact_btn;
    ProgressDialog progressDialog;
    String contactName,contactEmail,contactMessage,contactMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contact_us);

        contact_name = (EditText) findViewById(R.id.contact_name);
        contact_mobile = (EditText) findViewById(R.id.contact_mobile);
        contact_email = (EditText) findViewById(R.id.contact_email);
        contact_message = (EditText) findViewById(R.id.contact_message);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        contact_btn = (Button) findViewById(R.id.contact_btn);
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactName = contact_name.getText().toString().trim();
                contactMobile = contact_mobile.getText().toString().trim();
                contactEmail = contact_email.getText().toString().trim();
                contactMessage = contact_message.getText().toString().trim();
                ContactUs();
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        contact_name.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                contact_name.setFocusableInTouchMode(true);
                contact_name.requestFocus();
                return false;
            }
        });
        contact_mobile.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                contact_mobile.setFocusableInTouchMode(true);
                contact_mobile.requestFocus();
                return false;
            }
        });
        contact_email.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                contact_email.setFocusableInTouchMode(true);
                contact_email.requestFocus();
                return false;
            }
        });
        contact_message.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                contact_message.setFocusableInTouchMode(true);
                contact_message.requestFocus();
                return false;
            }
        });


    }

    public void progressDialog() {
        progressDialog = new ProgressDialog(ContactUsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void ContactUs() {
        progressDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        contactName = contact_name.getText().toString().trim();
        contactMobile = contact_mobile.getText().toString().trim();
        contactEmail = contact_email.getText().toString().trim();
        contactMessage = contact_message.getText().toString().trim();

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.contactUs(contactName,contactMobile,contactEmail,contactMessage);

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
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        message = root.getString("message");
                                        if (success == 1) {
                                           /* preferenceUtils.saveString(PreferenceUtils.UserName, username);
                                            preferenceUtils.saveString(PreferenceUtils.USER_EMAIL, email);
                                            preferenceUtils.saveString(PreferenceUtils.MOBILE, phone);
                                            preferenceUtils.saveString(PreferenceUtils.USER_ID, id);
*/
                                           /* Intent intent = new Intent(ContactUsActivity.this, MainActivity.class);
                                            startActivity(intent);*/
                                           Toast.makeText(ContactUsActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                            progressDialog.dismiss();

                                        } else {
                                            Toast.makeText(ContactUsActivity.this, message, Toast.LENGTH_SHORT).show();

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
              //  Toast.makeText(ContactUsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
