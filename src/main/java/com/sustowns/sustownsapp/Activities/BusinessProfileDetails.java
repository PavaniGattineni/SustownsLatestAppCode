package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Api.BusinessProfileApi;
import com.sustowns.sustownsapp.Api.DZ_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessProfileDetails extends AppCompatActivity {

    public String PickedImgPath = null;
    ImageView backarrow, iv_icon;
    CircleImageView profile_image;
    RelativeLayout rl_capture, rl_gallery;
    String profileString, user_name, user_mobile, user_id, authValue = "",name,email,address,website;
    String user_email,user_address,user_website,banner_image,image;
    RelativeLayout rl_business_profile;
    PreferenceUtils preferenceUtils;
    TextView title_name, title_mobile, et_mobile, title_toolbar,et_profile_email,et_profile_address,unique_id_text;
    EditText et_profile_name,et_website;
    LinearLayout ll_bus_profile_details, ll_manage_category;
    ProgressDialog progressDialog;
    Button save_btn;
    private Uri picUri;

    public static String getEncodedImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encodedImagePatientImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImagePatientImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_profile_details);
        preferenceUtils = new PreferenceUtils(BusinessProfileDetails.this);
        image = preferenceUtils.getStringFromPreference(PreferenceUtils.Image,"");
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        user_name = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName, "");
        user_mobile = preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE, "");
        user_email = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
        user_address = preferenceUtils.getStringFromPreference(PreferenceUtils.ADDRESS,"");
        user_website = preferenceUtils.getStringFromPreference(PreferenceUtils.WEBSITE,"");
        title_toolbar = (TextView) findViewById(R.id.title_name);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        et_profile_name = (EditText) findViewById(R.id.et_profile_name);
        et_profile_email = (TextView) findViewById(R.id.et_profile_email);
        et_profile_address = (TextView) findViewById(R.id.et_profile_address);
        et_website = (EditText) findViewById(R.id.et_website);
        et_mobile = (TextView) findViewById(R.id.et_mobile);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        ll_bus_profile_details = (LinearLayout) findViewById(R.id.ll_bus_profile_details);
        save_btn = (Button) findViewById(R.id.save_btn);
        unique_id_text = (TextView) findViewById(R.id.unique_id_text);

        title_name = (TextView) findViewById(R.id.title_user_name);
        title_mobile = (TextView) findViewById(R.id.title_user_mobile);
        title_name.setText(user_name);
        title_mobile.setText(user_mobile);
        et_website.setText(user_website);
        et_profile_name.setText(user_name);
        et_profile_address.setText(user_address);
        /*et_profile_name.setText(user_name);
        et_profile_email.setText(user_email);
        et_website.setText(user_website);*/
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
      /*  if(image != null || !image.isEmpty()){
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.profile_ic)
                    .error(R.drawable.profile_ic)
                    .into(profile_image);
          *//*  Glide.with(BusinessProfileDetails.this)
                    .load(image)
                    //.placeholder(R.drawable.profile_ic)
                    .into(profile_image);*//*
        }else{
            Picasso.get()
                    .load(R.drawable.profile_ic)
                    .placeholder(R.drawable.profile_ic)
                    .error(R.drawable.profile_ic)
                    .into(profile_image);
            *//*Glide.with(BusinessProfileDetails.this)
                    .load(R.drawable.no_image_available)
                    //.placeholder(R.drawable.profile_ic)
                    .into(profile_image);*//*
        }*/
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(BusinessProfileDetails.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);

                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        capture();
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        browse();
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_profile_name.getText().toString().trim();
                email = et_profile_email.getText().toString().trim();
                address = et_profile_address.getText().toString();
                website = et_website.getText().toString().trim();
                editBusinessProfile();
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_profile_name.setFocusableInTouchMode(false);
        et_profile_name.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_profile_name.setFocusableInTouchMode(true);
                et_profile_name.requestFocus();
                return false;
            }
        });
        et_profile_email.setFocusableInTouchMode(false);
        et_profile_email.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_profile_email.setFocusableInTouchMode(true);
                et_profile_email.requestFocus();
                return false;
            }
        });
        et_profile_address.setFocusableInTouchMode(false);
        et_profile_address.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_profile_address.setFocusableInTouchMode(true);
                et_profile_address.requestFocus();
                return false;
            }
        });
        et_website.setFocusableInTouchMode(false);
        et_website.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_website.setFocusableInTouchMode(true);
                et_website.requestFocus();
                return false;
            }
        });
        getBusinessProfile();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    private void browse() {
        try {
            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery_intent, FileUtils.GALLERY_IMAGE);
            /*Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery_intent, FileUtils.GALLERY_IMAGE);*/
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    private void capture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, FileUtils.CAMERA_CAPTURE);
            // PickedImgPath = FileUtils.launchCamera(MyAccount.this, "", true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FileUtils.CAMERA_CAPTURE) {  // Camera intent
                onCaptureImageResult(data);
            }
            // Gallery intent
            else if (requestCode == FileUtils.GALLERY_IMAGE && data != null && data.getData() != null) {
                try {
                    PickedImgPath = FileUtils.getFilePath(BusinessProfileDetails.this, data.getData());
                    Uri uri = data.getData();
                    File file = new File(PickedImgPath);
                    if (file.exists())
                        try {
                            Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), picUri);
                            profileString = getEncodedImage(bitmapImage);
                            profile_image.setImageURI(picUri);
                            Glide.with(BusinessProfileDetails.this)
                                    .load(uri)
                                    .into(profile_image);
                          //  Picasso.get().load(uri).into(profile_image);
                            //update_profile_service();
                        } catch (Exception e) {
                            Glide.with(BusinessProfileDetails.this)
                                    .load(uri)
                                    .into(profile_image);
                          //  Picasso.get().load("file://" + uri).placeholder(R.mipmap.ic_launcher_round).into(profile_image);
                        }
                    else
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    //  helper.showErrorDialog(this, "Image path error!" + "\n" + getResources().getString(R.string.provider_image_not_found));
                }
               //   preferenceUtils.saveString(PreferenceUtils.Image, PickedImgPath);
            } else
                PickedImgPath = "NO";
        } else {
            PickedImgPath = "NO";
        }
    }
    public void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(getApplicationContext().getFilesDir().getPath(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            PickedImgPath = destination.getAbsolutePath();
            Log.e("Camera Path", destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        profileString = getEncodedImage(bmp);

        profile_image.setImageBitmap(bmp);
        //imageView_profile.setImageBitmap(null);
        //   profile_img.setImageBitmap(thumbnail);
       //   preferenceUtils.saveString(PreferenceUtils.Image, PickedImgPath);
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(BusinessProfileDetails.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getBusinessProfile() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BusinessProfileApi service = retrofit.create(BusinessProfileApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBusinessProfile(user_id);

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
                                String success = null, message = null;
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("busdetails");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String myproduct = jsonObject.getString("myproduct");
                                        String storestatic = jsonObject.getString("storestatic");
                                        String orderstore = jsonObject.getString("orderstore");
                                        String mypay = jsonObject.getString("mypay");
                                        String mymoneystore = jsonObject.getString("mymoneystore");
                                        String moneycontract = jsonObject.getString("moneycontract");
                                        String approvejob = jsonObject.getString("approvejob");
                                        String complatejob = jsonObject.getString("complatejob");
                                        String fullname = jsonObject.getString("fullname");
                                        String id = jsonObject.getString("id");
                                        String email = jsonObject.getString("email");
                                        String phone = jsonObject.getString("phone");
                                        String uniqueid = jsonObject.getString("uniqueid");
                                        String bussinessname = jsonObject.getString("bussinessname");
                                        String location = jsonObject.getString("location");
                                        String service_area = jsonObject.getString("service_area");
                                        String website = jsonObject.getString("website");
                                        banner_image = jsonObject.getString("banner_image");
                                        String busi_detail = jsonObject.getString("busi_detail");
                                        String city = jsonObject.getString("city");
                                        String country = jsonObject.getString("country");
                                        String bid = jsonObject.getString("bid");
                                        String rating = jsonObject.getString("rating");

                                        preferenceUtils.saveString(PreferenceUtils.Image,banner_image);
                                        et_profile_name.setText(fullname);
                                        et_mobile.setText(phone);
                                        unique_id_text.setText(uniqueid);
                                        et_profile_email.setText(email);
                                        et_profile_address.setText(city + "," + country);
                                        if(!banner_image.isEmpty() || banner_image != null){
                                            Picasso.get()
                                                    .load(banner_image)
                                                    .placeholder(R.drawable.profile_ic)
                                                    .error(R.drawable.profile_ic)
                                                    .into(profile_image);
                                          /*  Glide.with(BusinessProfileDetails.this)
                                                    .load(banner_image)
                                                    //.placeholder(R.drawable.profile_ic)
                                                    .into(profile_image);*/
                                        }else{
                                            Picasso.get()
                                                    .load(R.drawable.profile_ic)
                                                    .placeholder(R.drawable.profile_ic)
                                                    .error(R.drawable.profile_ic)
                                                    .into(profile_image);
                                           /* Glide.with(BusinessProfileDetails.this)
                                                    .load(R.drawable.no_image_available)
                                                    //.placeholder(R.drawable.profile_ic)
                                                    .into(profile_image);*/
                                        }
                                    }
                                    JSONArray jsonArray1 = root.getJSONArray("reviews");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    }
                                    JSONArray jsonArray2 = root.getJSONArray("proreviews");
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                    }
                                    JSONArray categorydetails = root.getJSONArray("categorydetails");
                                    for (int i1 = 0; i1 < categorydetails.length(); i1++) {
                                        JSONObject jsonObject3 = categorydetails.getJSONObject(i1);
                                        String id = jsonObject3.getString("id");
                                        String categoryid = jsonObject3.getString("categoryid");
                                        String title = jsonObject3.getString("title");
                                    }

                                    JSONArray imagegallery = root.getJSONArray("imagegallery");
                                    for (int i2 = 0; i2 < imagegallery.length(); i2++) {
                                        JSONObject image = imagegallery.getJSONObject(i2);
                                        String gal_images = image.getString("gal_images");
                                    }
                                    JSONArray businessbadges = root.getJSONArray("businessbadges");
                                    for (int i3 = 0; i3 < businessbadges.length(); i3++) {
                                        JSONObject image = businessbadges.getJSONObject(i3);
                                    }
                                    JSONArray categoryArray = root.getJSONArray("category");
                                    for (int i4 = 0; i4 < businessbadges.length(); i4++) {
                                        JSONObject categoryObj = businessbadges.getJSONObject(i4);
                                    }
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                   /* Intent i = new Intent(BusinessProfileDetails.this,SignInActivity.class);
                                    startActivity(i);*/
                                    //  Toast.makeText(SignUpVendorActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else if (success.equalsIgnoreCase("0")) {
                                    message = root.getString("message");
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(BusinessProfileDetails.this, message, Toast.LENGTH_SHORT).show();
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
               // Toast.makeText(BusinessProfileDetails.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void editBusinessProfile() {
        progressdialog();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        BusinessProfileApi service = retrofit.create(BusinessProfileApi.class);
        name = et_profile_name.getText().toString().trim();
        website = et_website.getText().toString().trim();
        preferenceUtils.saveString(PreferenceUtils.UserName,name);
        preferenceUtils.saveString(PreferenceUtils.WEBSITE,website);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.editBusinessProfile(user_id,name,website,profileString);

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
                                String success = null, message = null;
                                message = root.getString("message");
                                success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    Intent i = new Intent(BusinessProfileDetails.this, BusinessProfileDetails.class);
                                    startActivity(i);
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(BusinessProfileDetails.this, message, Toast.LENGTH_SHORT).show();
                                } else if (success.equalsIgnoreCase("0")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(BusinessProfileDetails.this, message, Toast.LENGTH_SHORT).show();
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
               // Toast.makeText(BusinessProfileDetails.this, "please signup again", Toast.LENGTH_SHORT).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }


}
