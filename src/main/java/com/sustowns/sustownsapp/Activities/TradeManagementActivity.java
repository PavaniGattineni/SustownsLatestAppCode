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

public class TradeManagementActivity extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    public static String username, useremail, user_role;
    ImageView backarrow;
    LinearLayout home, news, store, bidcontracts, poultryprices, ll_signout;
    LinearLayout ll_cart, ll_orders, ll_offers, ll_transport_orders, ll_transport_details,ll_transport_contract_orders, ll_contract_orders, ll_logistic_orders;
    TextView home_text, news_text, store_text, contracts_text, market_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.trade_management_layout);

        preferenceUtils = new PreferenceUtils(TradeManagementActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName, "");
        useremail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL, "");
        user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");

        ll_cart = (LinearLayout) findViewById(R.id.ll_cart);
        ll_orders = (LinearLayout) findViewById(R.id.ll_orders);
        ll_offers = (LinearLayout) findViewById(R.id.ll_offers);
        ll_transport_orders = (LinearLayout) findViewById(R.id.ll_transport_orders);
        ll_logistic_orders = (LinearLayout) findViewById(R.id.ll_logistic_orders);
        ll_transport_details = (LinearLayout) findViewById(R.id.ll_transport_details);
        ll_contract_orders = (LinearLayout) findViewById(R.id.ll_contract_orders);
        ll_transport_contract_orders =(LinearLayout) findViewById(R.id.ll_transport_contract_orders);

        if (user_role.equalsIgnoreCase("poultry")) {
            ll_cart.setVisibility(View.VISIBLE);
            ll_orders.setVisibility(View.VISIBLE);
            ll_offers.setVisibility(View.VISIBLE);
            ll_transport_orders.setVisibility(View.GONE);
            ll_transport_details.setVisibility(View.GONE);
            ll_transport_contract_orders.setVisibility(View.GONE);
            ll_contract_orders.setVisibility(View.VISIBLE);
        } else if (user_role.equalsIgnoreCase("transport")) {
            ll_cart.setVisibility(View.VISIBLE);
            ll_orders.setVisibility(View.GONE);
            ll_offers.setVisibility(View.GONE);
            ll_transport_orders.setVisibility(View.VISIBLE);
            ll_transport_details.setVisibility(View.GONE);
            ll_transport_contract_orders.setVisibility(View.VISIBLE);
            ll_contract_orders.setVisibility(View.GONE);
            ll_logistic_orders.setVisibility(View.GONE);
        } else {
            ll_cart.setVisibility(View.VISIBLE);
            ll_orders.setVisibility(View.VISIBLE);
            ll_offers.setVisibility(View.VISIBLE);
            ll_transport_orders.setVisibility(View.GONE);
            ll_transport_details.setVisibility(View.GONE);
            ll_transport_contract_orders.setVisibility(View.GONE);
            ll_contract_orders.setVisibility(View.VISIBLE);
        }

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
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(TradeManagementActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(TradeManagementActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(TradeManagementActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(TradeManagementActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(TradeManagementActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        ll_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        ll_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, StoreReceivedOrdersActivity.class);
                i.putExtra("Message","");
                startActivity(i);
            }
        });
        ll_contract_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, MyContractOrdersActivity.class);
                i.putExtra("Message","");
                i.putExtra("PurchasedOrders","0");
                startActivity(i);
            }
        });
        ll_transport_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, TransportOrdersActivity.class);
                startActivity(i);
            }
        });
        ll_logistic_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, LogisticsOrdersActivity.class);
                i.putExtra("Message","");
                i.putExtra("Logistics","1");
                startActivity(i);
            }
        });
        ll_transport_contract_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, TransportContractOrdersActivity.class);
                startActivity(i);
            }
        });
      /*  ll_logistic_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, TransportDetailsActivity.class);
                startActivity(i);
            }
        });*/
        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TradeManagementActivity.this, StoreOffersActivity.class);
                i.putExtra("Message","");
                i.putExtra("SentOffers","");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TradeManagementActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
