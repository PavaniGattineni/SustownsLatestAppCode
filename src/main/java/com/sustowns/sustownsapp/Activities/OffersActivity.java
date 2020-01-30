package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Adapters.BuyOffersAdapter;

public class OffersActivity extends AppCompatActivity {
    ImageView backarrow;
    RecyclerView recycler_buy_offers,recycler_sell_offers ;
    BuyOffersAdapter buyOffersAdapter;
    Button buyOffersBtn,sellOffersBtn;
    String[] order = {"Offer 1","Offer2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_offers);

        recycler_buy_offers = (RecyclerView) findViewById(R.id.recycler_buy_offers);
        recycler_sell_offers = (RecyclerView) findViewById(R.id.recycler_sell_offers);
        buyOffersBtn = (Button) findViewById(R.id.buy_offers_btn);
        sellOffersBtn = (Button) findViewById(R.id.sell_offers_btn);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_sell_offers.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_buy_offers.setLayoutManager(layoutManager1);
        recycler_buy_offers.setVisibility(View.VISIBLE);
        recycler_sell_offers.setVisibility(View.GONE);
        sellOffersBtn.setTextColor(getResources().getColor(R.color.black));
        buyOffersBtn.setTextColor(getResources().getColor(R.color.white));
        buyOffersBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
        sellOffersBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
        buyOffersAdapter = new BuyOffersAdapter(OffersActivity.this,order);
        recycler_buy_offers.setAdapter(buyOffersAdapter);


        buyOffersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_buy_offers.setVisibility(View.VISIBLE);
                recycler_sell_offers.setVisibility(View.GONE);
                sellOffersBtn.setTextColor(getResources().getColor(R.color.black));
                buyOffersBtn.setTextColor(getResources().getColor(R.color.white));
                buyOffersBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                sellOffersBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                buyOffersAdapter = new BuyOffersAdapter(OffersActivity.this,order);
                recycler_buy_offers.setAdapter(buyOffersAdapter);
            }
        });
        sellOffersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_buy_offers.setVisibility(View.GONE);
                recycler_sell_offers.setVisibility(View.VISIBLE);
                buyOffersBtn.setTextColor(getResources().getColor(R.color.black));
                sellOffersBtn.setTextColor(getResources().getColor(R.color.white));
                sellOffersBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                buyOffersBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                buyOffersAdapter = new BuyOffersAdapter(OffersActivity.this,order);
                recycler_sell_offers.setAdapter(buyOffersAdapter);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
