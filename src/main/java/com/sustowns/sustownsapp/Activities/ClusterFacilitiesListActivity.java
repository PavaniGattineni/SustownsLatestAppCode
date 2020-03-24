package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sustowns.sustownsapp.Adapters.ClusterWarehouseAdapter;
import com.sustowns.sustownsapp.R;


public class ClusterFacilitiesListActivity extends AppCompatActivity {
    ImageView backarrow;
    RecyclerView recyclerview_facilities_list;
    Button add_facility_btn;
    TextView not_available_text;
    ClusterWarehouseAdapter clusterWarehouseAdapter;
    String[] Name = {"Warehouse 1","Warehouse 2"};
    String[] Width = {"12","13"};
    String[] Height = {"14","15"};
    String[] Location = {"Madhapur,Hyderabad","Kukatpally,Hyderabad"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cluster_facilities_list);
        not_available_text = (TextView) findViewById(R.id.not_available_text); 
        recyclerview_facilities_list = (RecyclerView) findViewById(R.id.recyclerview_facilities_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ClusterFacilitiesListActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerview_facilities_list.setLayoutManager(linearLayoutManager);
        clusterWarehouseAdapter = new ClusterWarehouseAdapter(ClusterFacilitiesListActivity.this,Name,Width,Height,Location);
        recyclerview_facilities_list.setAdapter(clusterWarehouseAdapter);
        add_facility_btn = (Button) findViewById(R.id.add_facility_btn);
        add_facility_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClusterFacilitiesListActivity.this,AddFacilitiesActivity.class);
                startActivity(i);
            }
        });
        backarrow = (ImageView) findViewById(R.id.backarrow_vendors);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
