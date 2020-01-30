package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Adapters.CategoryExpandListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeBookingActivity extends AppCompatActivity {
    Spinner spinner_freight_type,spinner_freight_provider;
    ExpandableListView expListView;
    CategoryExpandListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageView backarrow;
    String[] country = {"Road", "Sea", "Air", "Rail"};
    String[] string = {"DHL", "Freight"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_make_booking);

        spinner_freight_type=  (Spinner) findViewById(R.id.spinner_freight_type);
        spinner_freight_provider = (Spinner) findViewById(R.id.spinner_freight_provider);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_freight_type.setAdapter(aa);
        spinner_freight_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,string);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_freight_provider.setAdapter(aa1);
        spinner_freight_provider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}
