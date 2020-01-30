package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sustowns.sustownsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvailableBadges extends AppCompatActivity {
    ImageView backarrow,profile_image;
    RelativeLayout rl_capture,rl_gallery;
    TextView savearrow;
    Button choosefile_btn;
    public String PickedImgPath = null;
    String profileString,image,user_id,buss_id,badge_image_st,gal_images,image_url;
    private Uri picUri;
    final int CHOOSE_CAMERA = 232;
    EditText badge_title;
    Intent intent;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    RecyclerView availablebadge_recyclerview;

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
        setContentView(R.layout.activity_available_badges);
        preferenceUtils = new PreferenceUtils(AvailableBadges.this);
        image = preferenceUtils.getStringFromPreference(PreferenceUtils.BadgeImage,"");
        availablebadge_recyclerview = (RecyclerView) findViewById(R.id.availablebadge_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AvailableBadges.this,LinearLayoutManager.HORIZONTAL,false);
        availablebadge_recyclerview.setLayoutManager(layoutManager);
        profile_image = (ImageView) findViewById(R.id.profile_image);
     /*   if(image != null || !image.equalsIgnoreCase("")){
            Glide.with(AvailableBadges.this)
                    .load(image)
                    .placeholder(R.drawable.profile_ic)
                    .into(profile_image);
        }else{
            Glide.with(AvailableBadges.this)
                    .load(R.drawable.profile_ic)
                    .placeholder(R.drawable.profile_ic)
                    .into(profile_image);
        }*/
        backarrow = (ImageView) findViewById(R.id.backarrow);
        savearrow = (TextView) findViewById(R.id.savearrow);
        choosefile_btn = (Button) findViewById(R.id.choosefile_btn);
        //intent = getIntent();
       // image = intent.getStringExtra("AvailableBadge");

        badge_title = (EditText) findViewById(R.id.badge_title);
        badge_title.setFocusableInTouchMode(false);
        badge_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                badge_title.setFocusableInTouchMode(true);
                badge_title.requestFocus();
                return false;
            }
        });
        choosefile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(AvailableBadges.this);
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
        savearrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badge_title.getText().toString().trim();
                saveBadge();
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AvailableBadges.this, BusinessProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
        badge_title.setFocusableInTouchMode(false);
        badge_title.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                badge_title.setFocusableInTouchMode(true);
                badge_title.requestFocus();
                return false;
            }
        });
      //  getBusinessBadge();
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
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CHOOSE_CAMERA);
            /*  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, FileUtils.CAMERA_CAPTURE);*/
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
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
                    PickedImgPath = FileUtils.getFilePath(AvailableBadges.this, data.getData());
                    Uri uri = data.getData();
                    File file = new File(PickedImgPath);
                    if (file.exists())
                        try {
                            Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), picUri);
                            profileString = getEncodedImage(bitmapImage);
                            profile_image.setImageURI(picUri);
                            Glide.with(AvailableBadges.this)
                                    .load(uri)
                                    .into(profile_image);
                          //  Picasso.get().load(uri).placeholder(R.mipmap.ic_launcher_round).into(profile_image);
                            //update_profile_service();
                        } catch (Exception e) {
                            Glide.with(AvailableBadges.this)
                                    .load(uri)
                                    .into(profile_image);
                           // Picasso.get().load("file://" + uri).placeholder(R.mipmap.ic_launcher_round).into(profile_image);
                        }
                    else
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    //  helper.showErrorDialog(this, "Image path error!" + "\n" + getResources().getString(R.string.provider_image_not_found));
                }
                  preferenceUtils.saveString(PreferenceUtils.BadgeImage, profileString);
            } else
                PickedImgPath = "NO";
        } else {
            PickedImgPath = "NO";
        }
    }

    private void onCaptureImageResult(Intent data) {
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

        // profile_image.setImageBitmap(bmp);
        //imageView_profile.setImageBitmap(null);
        preferenceUtils.saveString(PreferenceUtils.BadgeImage, profileString);
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(AvailableBadges.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getBusinessBadge() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
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
                                        String bussinessname = jsonObject.getString("bussinessname");
                                        String location = jsonObject.getString("location");
                                        String service_area = jsonObject.getString("service_area");
                                        String website = jsonObject.getString("website");
                                        String banner_image = jsonObject.getString("banner_image");
                                        String busi_detail = jsonObject.getString("busi_detail");
                                        String city = jsonObject.getString("city");
                                        String country = jsonObject.getString("country");
                                        String bid = jsonObject.getString("bid");
                                        String rating = jsonObject.getString("rating");

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
                                        gal_images = image.getString("gal_images");
                                        preferenceUtils.saveString(PreferenceUtils.GAL_IMAGE,gal_images);
                                    }
                                    JSONArray businessbadges = root.getJSONArray("businessbadges");
                                    for (int i3 = 0; i3 < businessbadges.length(); i3++) {
                                        JSONObject badgeImage = businessbadges.getJSONObject(i3);
                                        image_url = badgeImage.getString("image_url");
                                        if(image_url != null || !image_url.isEmpty()) {
                                            Glide.with(AvailableBadges.this)
                                                    .load(image_url)
                                                    //.placeholder(R.drawable.profile_ic)
                                                    .into(profile_image);
                                        }
                                        preferenceUtils.saveString(PreferenceUtils.BadgeImage,image_url);
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
                                    Toast.makeText(AvailableBadges.this, message, Toast.LENGTH_SHORT).show();
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

    private void saveBadge() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        buss_id = preferenceUtils.getStringFromPreference(PreferenceUtils.BUSINESS_ID, "");
        image = preferenceUtils.getStringFromPreference(PreferenceUtils.Image,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BusinessProfileApi service = retrofit.create(BusinessProfileApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.businessBadge(image,"442","355");

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
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Intent i = new Intent(AvailableBadges.this, AvailableBadges.class);
                                    startActivity(i);
                                    Toast.makeText(AvailableBadges.this, message, Toast.LENGTH_SHORT).show();
                                } else if (success.equalsIgnoreCase("0")) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    Toast.makeText(AvailableBadges.this, message, Toast.LENGTH_SHORT).show();
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

}
