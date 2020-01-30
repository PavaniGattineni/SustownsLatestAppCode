package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;

public class SettingsActivity extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    public static String username;
    ImageView backarrow;
    LinearLayout home,news,store,bidcontracts,poultryprices,ll_signout;
    LinearLayout ll_business_profile,ll_security_privacy,ll_terms_conditions;
    TextView home_text,news_text,store_text,contracts_text,market_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        preferenceUtils = new PreferenceUtils(SettingsActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName,"");

        ll_security_privacy = (LinearLayout) findViewById(R.id.ll_security_privacy);
        ll_business_profile = (LinearLayout) findViewById(R.id.ll_business_profile);
        ll_terms_conditions = (LinearLayout) findViewById(R.id.ll_terms_conditions);
        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);
        home_text = (TextView) findViewById(R.id.home_footer);
        news_text = (TextView) findViewById(R.id.news_footer);
        store_text = (TextView) findViewById(R.id.store_footer);
        contracts_text = (TextView) findViewById(R.id.contracts_footer);
        market_text = (TextView) findViewById(R.id.marketing_footer);
        ll_signout = (LinearLayout) findViewById(R.id.ll_signout);
        backarrow = (ImageView) findViewById(R.id.backarrowSettings);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
       // ll_vendor_manage = (LinearLayout) findViewById(R.id.ll_vendor_manage);
       // ll_vendor_payment = (LinearLayout) findViewById(R.id.ll_vendor_payment);

        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(SettingsActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(SettingsActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(SettingsActivity.this, BidContractsActivity.class);
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(SettingsActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        ll_security_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        ll_business_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, BusinessProfileActivity.class);
                startActivity(i);
            }
        });
        ll_terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, TermsConditionsActivity.class);
                startActivity(i);
            }
        });
        ll_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceUtils.saveString(PreferenceUtils.USER_EMAIL, "");
                preferenceUtils.saveString(PreferenceUtils.UserName,"");
                preferenceUtils.saveString(PreferenceUtils.USER_ID,"");
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
               /* preferenceUtils.saveString(PreferenceUtils.UserName, "");
                Intent i = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(i);*/
            }
        });
    }
}
