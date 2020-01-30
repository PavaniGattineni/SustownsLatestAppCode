package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MakeOffer extends AppCompatActivity {

    TextView make_offer_name,make_offer_price,make_offer_unit;
    EditText price_et,quantity_et;
    Button close_btn,submit_btn;
    ImageView close_make_offer;
    String price,quantity,user_id,min_quantity,title,unit,pro_id,MinPriceStr;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    Integer quantity_int,min_quant_int;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_make_offer);

        InitValues();
        InitializeUI();
    }
    private void InitializeUI() {
        make_offer_name = (TextView) findViewById(R.id.make_offer_name);
        make_offer_price = (TextView) findViewById(R.id.make_offer_price);
        make_offer_unit = (TextView) findViewById(R.id.make_offer_unit);
        price_et = (EditText) findViewById(R.id.price_et);
        quantity_et = (EditText) findViewById(R.id.quantity_et);
        close_btn = (Button) findViewById(R.id.close_btn);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        close_make_offer = (ImageView) findViewById(R.id.close_make_offer);
        if((!min_quantity.isEmpty() || min_quantity != null) || (!title.isEmpty() || title != null) || (!unit.isEmpty() || unit != null) ||
                (!price.isEmpty() || price != null)){
            make_offer_name.setText(title);
            make_offer_price.setText(price);
            make_offer_unit.setText(unit);
            quantity_et.setText(min_quantity);
            price_et.setText(MinPriceStr);
        }else{
            make_offer_name.setText("");
            make_offer_price.setText("");
            make_offer_unit.setText("");
            quantity_et.setText("");
            price_et.setText("");
        }
        close_make_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = price_et.getText().toString().trim();
                quantity_int = Integer.parseInt(price);
                Integer MinPrice = Integer.parseInt(MinPriceStr);
                if(quantity_int > MinPrice){
                    Toast.makeText(MakeOffer.this, "Value must be less than "+"or equals to "+MinPrice, Toast.LENGTH_SHORT).show();
                }else {
                    submitMakeOffer();
                }
            }
        });
        price_et.setFocusableInTouchMode(false);
        price_et.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                price_et.setFocusableInTouchMode(true);
                price_et.requestFocus();
                return false;
            }
        });
    }
    private void InitValues() {
        preferenceUtils = new PreferenceUtils(MakeOffer.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        intent = getIntent();
        min_quantity = intent.getStringExtra("MinQuantity");
        min_quant_int = Integer.parseInt(min_quantity);
        title = intent.getStringExtra("Title");
        unit = intent.getStringExtra("Unit");
        price = intent.getStringExtra("Price");
        pro_id = intent.getStringExtra("ProID");
        MinPriceStr = intent.getStringExtra("MinPrice");
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(MakeOffer.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void submitMakeOffer() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);
        price = price_et.getText().toString().trim();
        quantity = quantity_et.getText().toString().trim();

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.submitMakeOffer(user_id,quantity,pro_id,price);

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
                                        message = root.getString("message");
                                        if (success == 1) {
                                            Toast.makeText(MakeOffer.this, "Your Offer Sent Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                            progressDialog.dismiss();

                                        } else {
                                            message = root.getString("message");
                                            Toast.makeText(MakeOffer.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
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
            //    Toast.makeText(MakeOffer.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
