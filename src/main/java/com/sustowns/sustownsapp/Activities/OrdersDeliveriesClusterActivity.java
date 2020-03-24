package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sustowns.sustownsapp.Adapters.ClusterDeliveriesAdapter;
import com.sustowns.sustownsapp.R;

public class OrdersDeliveriesClusterActivity extends AppCompatActivity {
    ImageView backarrow;
    RecyclerView recycler_view_products_store;
    ClusterDeliveriesAdapter clusterDeliveriesAdapter;
    String[] orderName = {"Order 1","Order 2"};
    String[] orderNo = {"10032112","13102211"};
    String[] orderPrice = {"4000","5000"};
    String[] orderStatus = {"Delivered","Received"};
    String[] orderDate = {"12-3-2020","10-3-2020"};
    TextView not_available_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_orders_delivery_cluster);

        not_available_text = (TextView) findViewById(R.id.not_available_text);
        //not_available_text.setVisibility(View.VISIBLE);
        recycler_view_products_store = (RecyclerView) findViewById(R.id.recycler_view_products_store);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_products_store.setLayoutManager(layoutManager);
        clusterDeliveriesAdapter = new ClusterDeliveriesAdapter(OrdersDeliveriesClusterActivity.this,orderName,orderPrice,orderNo,orderStatus,orderDate);
        recycler_view_products_store.setAdapter(clusterDeliveriesAdapter);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
