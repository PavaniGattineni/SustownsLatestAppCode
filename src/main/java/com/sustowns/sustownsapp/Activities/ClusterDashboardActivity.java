package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;

public class ClusterDashboardActivity extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    public static String username, useremail, user_role;
    ImageView backarrow;
    LinearLayout home, news, store, bidcontracts, poultryprices,ll_received_orders,ll_deliveries,ll_stock;
    LinearLayout ll_vendor_list, ll_facilities, ll_services_list, ll_transport_details,ll_transport_contract_orders, ll_contract_orders, ll_logistic_orders;
    TextView home_text, news_text, store_text, contracts_text, market_text;
    int[] images = {R.drawable.register,R.drawable.register,R.id.register,R.id.register,R.id.register};
    String[] name = {"Add Vendors","Vendors List","Add Transporter","Add Facilities","Add Services"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard_layout);

        preferenceUtils = new PreferenceUtils(ClusterDashboardActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName, "");
        useremail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL, "");
        user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");
        home_text = (TextView) findViewById(R.id.home_footer);
        news_text = (TextView) findViewById(R.id.news_footer);
        store_text = (TextView) findViewById(R.id.store_footer);
        contracts_text = (TextView) findViewById(R.id.contracts_footer);
        market_text = (TextView) findViewById(R.id.marketing_footer);
        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);

        ll_vendor_list = (LinearLayout) findViewById(R.id.ll_vendor_list);
        ll_facilities = (LinearLayout) findViewById(R.id.ll_facilities);
       // ll_services_list = (LinearLayout) findViewById(R.id.ll_services_list);
        ll_received_orders = (LinearLayout) findViewById(R.id.ll_received_orders);
        ll_deliveries = (LinearLayout) findViewById(R.id.ll_deliveries);
        ll_stock = (LinearLayout) findViewById(R.id.ll_stock);
        
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ClusterDashboardActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ClusterDashboardActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ClusterDashboardActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ClusterDashboardActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ClusterDashboardActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        ll_vendor_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClusterDashboardActivity.this, ClusterVendorsListActivity.class);
                startActivity(i);
            }
        });
        ll_received_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClusterDashboardActivity.this, ReceivedOrdersClusterActivity.class);
                startActivity(i);
            }
        });
        ll_deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClusterDashboardActivity.this, OrdersDeliveriesClusterActivity.class);
                startActivity(i);
            }
        });
        ll_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClusterDashboardActivity.this, StockInventoriesActivity.class);
                startActivity(i);
            }
        });
        ll_facilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClusterDashboardActivity.this, ClusterFacilitiesListActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClusterDashboardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}