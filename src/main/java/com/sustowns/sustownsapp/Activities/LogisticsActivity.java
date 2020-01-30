package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Adapters.CategoryExpandListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {
    LinearLayout home,news,store,bidcontracts,poultryprices;
    PreferenceUtils preferenceUtils;
    public static String username,useremail;
    ExpandableListView expListView;
    CategoryExpandListAdapter listAdapter;
    List<String> listDataHeader;
    ImageView backarrow;
    HashMap<String, List<String>> listDataChild;
    TextView home_text,news_text,store_text,contracts_text,market_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logistics);

        preferenceUtils = new PreferenceUtils(LogisticsActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName,"");
        useremail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
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
        expListView = (ExpandableListView) findViewById(R.id.expandableListView_logistics);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prepareListData();

        listAdapter = new CategoryExpandListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(LogisticsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(LogisticsActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(LogisticsActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(LogisticsActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(LogisticsActivity.this, MarketActivity.class);
                startActivity(i);              }
        });

        //hideAndShowItems();

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Cluster Services");
        listDataHeader.add("Freight Services");
        listDataHeader.add("Clearance Services");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Aggregation Services");
        top250.add("Distribution Services");
        top250.add("Quality Services");
        top250.add("Packing Services");
        top250.add("Processing Services");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Make Booking");
        nowShowing.add("Bookings");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Export Services");
        comingSoon.add("Import Services");

        listDataChild.put(listDataHeader.get(0), top250);
        listDataChild.put(listDataHeader.get(1), nowShowing); // Header, Child data
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}
