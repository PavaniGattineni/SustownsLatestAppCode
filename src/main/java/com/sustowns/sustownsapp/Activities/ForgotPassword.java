package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {
    ImageView close_password;
    EditText email_forgotpassword,new_password,confirm_password;
    Button reset_password,reset_newpassword;
    PreferenceUtils preferenceUtils;
    String user_email,user_id;
    LinearLayout ll_email,ll_new_password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        try {
            preferenceUtils = new PreferenceUtils(ForgotPassword.this);
            user_email = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
            ll_email = (LinearLayout) findViewById(R.id.ll_email);
            ll_new_password = (LinearLayout) findViewById(R.id.ll_new_password);
            close_password = (ImageView) findViewById(R.id.close_password);
            email_forgotpassword = (EditText) findViewById(R.id.email_forgotpassword);
            reset_password = (Button) findViewById(R.id.reset_password);
            reset_newpassword = (Button) findViewById(R.id.reset_newpassword);
            new_password = findViewById(R.id.new_password);
            confirm_password = findViewById(R.id.confirm_password);
            reset_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(email_forgotpassword.getText().toString().isEmpty()){
                        Toast.makeText(ForgotPassword.this, "please enter your email id", Toast.LENGTH_SHORT).show();
                    }else if(email_forgotpassword.getText().toString().equalsIgnoreCase(user_email)){
                        ll_email.setVisibility(View.GONE);
                        ll_new_password.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(ForgotPassword.this, "Entered Email is wrong!please enter valid email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            reset_newpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(new_password.getText().toString().isEmpty()|| confirm_password.getText().toString().isEmpty()){
                        Toast.makeText(ForgotPassword.this, "please fill empty fields", Toast.LENGTH_SHORT).show();
                    }else if(new_password.getText().toString().equalsIgnoreCase(confirm_password.getText().toString())){
                        ResetPassword();
                    }else{
                        Toast.makeText(ForgotPassword.this, "Password and Confirm Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            close_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                finish();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void ResetPassword() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("uid",user_email);
            jsonObj.put("npsd",new_password.getText().toString());
            androidNetworkingPassword(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingPassword(JSONObject jsonObject){
         progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Checkoutservice/forgot/")
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
                                Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                                // ((MyContractOrdersActivity)context).getContractOrdersList();
                                Intent i = new Intent(ForgotPassword.this,SignInActivity.class);
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
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
                        Toast.makeText(ForgotPassword.this, "Something went wrong!please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
