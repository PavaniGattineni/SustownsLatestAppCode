package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;

public class BusinessProfileActivity extends AppCompatActivity {
    ImageView backarrow,iv_icon,profile_image,savearrow,edit_image,business_profile;
    RelativeLayout rl_capture,rl_gallery;
    public String PickedImgPath = null;
    String profileString,user_name,user_mobile,gal_images,image_url,banner_image;
    LinearLayout ll_categories,ll_reviews,ll_gallery_image,ll_business_badges,shipping,bankdetails,ll_your_categories;
    PreferenceUtils preferenceUtils;
    TextView title_name,title_mobile;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_profile);
        preferenceUtils = new PreferenceUtils(BusinessProfileActivity.this);
        user_name = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName,"");
        user_mobile = preferenceUtils.getStringFromPreference(PreferenceUtils.MOBILE,"");

        backarrow = (ImageView) findViewById(R.id.backarrow);
        savearrow = (ImageView) findViewById(R.id.savearrow);
        edit_image= (ImageView) findViewById(R.id.edit_image);
        business_profile = (ImageView) findViewById(R.id.business_profile);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        ll_categories = (LinearLayout) findViewById(R.id.ll_categories);
        ll_reviews = (LinearLayout) findViewById(R.id.ll_reviews);
        ll_gallery_image = (LinearLayout) findViewById(R.id.ll_gallery_image);
        ll_business_badges = (LinearLayout) findViewById(R.id.ll_business_badges);
        shipping = (LinearLayout) findViewById(R.id.ll_shipping);
        bankdetails = (LinearLayout) findViewById(R.id.ll_bank_details);
        ll_your_categories = (LinearLayout) findViewById(R.id.ll_your_categories);
        title_name = (TextView) findViewById(R.id.title_user_name);
        title_mobile = (TextView) findViewById(R.id.title_user_mobile);
        title_name.setText(user_name);
        title_mobile.setText(user_mobile);
        profile_image = (ImageView) findViewById(R.id.profile_image);

        business_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BusinessProfileDetails.class);
                startActivity(i);
            }
        });
        ll_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BusinessCategory.class);
                startActivity(i);
            }
        });
        ll_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BusinessReviews.class);
                startActivity(i);
            }
        });
        ll_gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BusinessGallery.class);
                i.putExtra("Image",gal_images);
                startActivity(i);
            }
        });
        ll_business_badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, AvailableBadges.class);
                i.putExtra("AvailableBadge",image_url);
                startActivity(i);
            }
        });
        shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, ShippingAddress.class);
                startActivity(i);
            }
        });
        bankdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BankDetailsActivity.class);
                startActivity(i);
            }
        });
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BusinessProfileDetails.class);
                startActivity(i);
            }
        });
        ll_your_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this, BusinessYourCategory.class);
                startActivity(i);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileActivity.this,SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(BusinessProfileActivity.this,SettingsActivity.class);
        startActivity(i);
        finish();
    }
}
