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
import android.widget.LinearLayout;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Adapters.ServiceContractAdapter;

public class MyServiceContractActivity extends AppCompatActivity {
    RecyclerView recycler_view_service_contracts;
    ServiceContractAdapter serviceContractAdapter;
    String[] contracts = {"Contract 1","Contract 2"};
    Button add_service_request;
    LinearLayout ll_addservice_contracts,ll_service_contracts;
    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_service_contract);
        recycler_view_service_contracts = (RecyclerView) findViewById(R.id.recycler_view_service_contracts);

        LinearLayoutManager lManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_view_service_contracts.setLayoutManager(lManager);
        serviceContractAdapter = new ServiceContractAdapter(MyServiceContractActivity.this, contracts);
        recycler_view_service_contracts.setAdapter(serviceContractAdapter);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        ll_addservice_contracts = (LinearLayout) findViewById(R.id.ll_addservice_contracts);
        ll_service_contracts = (LinearLayout) findViewById(R.id.ll_service_contracts);
        ll_addservice_contracts.setVisibility(View.GONE);
        ll_service_contracts.setVisibility(View.VISIBLE);
        add_service_request = (Button) findViewById(R.id.add_service_request);
        add_service_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_addservice_contracts.setVisibility(View.VISIBLE);
                ll_service_contracts.setVisibility(View.GONE);
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
