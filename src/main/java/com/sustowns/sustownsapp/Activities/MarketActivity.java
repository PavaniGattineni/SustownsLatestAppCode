package com.sustowns.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.MarketsAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.OrdersApi;
import com.sustowns.sustownsapp.Models.MarketModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketActivity extends AppCompatActivity {
    TextView home_text,news_text,store_text,contracts_text,market_text,title_market;
    PreferenceUtils preferenceUtils;
    Toolbar toolbar;
    ImageView backarrow;
    public static String userName,userEmail,pro_id,image;
    LinearLayout home,news,store,bidcontracts,poultryprices;
    Button submit_btn;
    RecyclerView markets_recyclerview;
    Spinner spinner_category,spinner_subcategory,spinner_month,spinner_year,spinner_day;
    String categoryStr,SubCategoryStr,monthStr,yearStr,dayStr,category_id;
    MarketsAdapter marketsAdapter;
    ProgressDialog progressDialog;
    Button choose_date;
    ArrayList<MarketModel> marketModels;
    TextView market_price_available_text,selected_date;
    Calendar cal;
    private int month;
    private int day;
    private int year;
    String Year,Month,Day;
    String[] CategoryList = {"Egg"};
    String[] SubCategoryList = {"SubCategory","General Rates","NECC Rates"};
    String[] MonthList = {"Select Month","1","2","3","4","5","6","7","8","9","10","11","12"};
    String[] YearList = {"Year","2019"};
    String[] DayList = {"Select Day","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] location = {"Hyd","Hyd1","Hyd2","Hyd3","Hyd4","Hyd5","Hyd6"};
    String[] price = {"400","405","413","420","432","423","408"};
    static String[][] spaceProbes={
            {"Ahmedabad","2019","350","400"},
            {"Ajmer","2019","420","430"},
            {"Banglore","2019","423","425"},
            {"Chennai","2019","415","420"},
            {"Chittor","2019","430","420"},
            {"Cochin","2019","400","410"},
            {"Delhi(CC)","2019","436","425"},
            {"E.Godavari","2019","415","423"},
            {"Hyderabad","2019","393","425"},
            {"Miraj","2019","393","425"},
            {"Mumbai(CC)","2019","393","425"},
            {"Mysore","2019","393","425"},
            {"Nagapur","2019","393","425"},
            {"Pune","2019","393","425"},
            {"Punjab","2019","393","425"},
            {"Vijayawada","2019","393","425"},
            {"Vizag","2019","393","425"},
            {"W.Godavari","2019","393","425"},
            {"Warangal","2019","393","425"},
            {"Barwala","2019","393","425"},
            {"Bhopal","2019","393","425"},
            {"Hospet","2019","393","425"},
            {"Jabalpur","2019","393","425"},
            {"Kanpur","2019","393","425"},
            {"Kolkata(CC)","2019","393","425"},
            {"Luknow(CC)","2019","393","425"},
            {"Raipur","2019","393","425"},
            {"Varanasi(CC)","2019","393","425"}

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_market);
        preferenceUtils = new PreferenceUtils(MarketActivity.this);
        userName = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName,"");
        userEmail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
        choose_date = (Button) findViewById(R.id.choose_date);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);
        home_text = (TextView) findViewById(R.id.home_footer);
        news_text = (TextView) findViewById(R.id.news_footer);
        store_text = (TextView) findViewById(R.id.store_footer);
        market_price_available_text = (TextView) findViewById(R.id.market_price_available_text);
        contracts_text = (TextView) findViewById(R.id.contracts_footer);
        market_text = (TextView) findViewById(R.id.marketing_footer);
        market_text.setTextColor(getResources().getColor(R.color.appcolor));
        markets_recyclerview = (RecyclerView) findViewById(R.id.markets_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MarketActivity.this,LinearLayoutManager.VERTICAL,false);
        markets_recyclerview.setLayoutManager(layoutManager);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        selected_date = (TextView) findViewById(R.id.selected_date);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MarketActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MarketActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MarketActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MarketActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MarketActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog();

            }
            public void DateDialog(){

                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                    Year = String.valueOf(year);
                    Month = String.valueOf(monthOfYear+1);
                    Day = String.valueOf(dayOfMonth);

                        choose_date.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }};
                DatePickerDialog dpDialog=new DatePickerDialog(MarketActivity.this, listener, year, month, day);
               // dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dpDialog.show();

            }
        });


        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter category = new ArrayAdapter(this,android.R.layout.simple_spinner_item,CategoryList);
        category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_category.setAdapter(category);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_subcategory = (Spinner) findViewById(R.id.spinner_subcategory);
        ArrayAdapter subcategory = new ArrayAdapter(this,android.R.layout.simple_spinner_item,SubCategoryList);
        subcategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_subcategory.setAdapter(subcategory);
        spinner_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryStr = parent.getItemAtPosition(position).toString();
                if(SubCategoryStr.equalsIgnoreCase("General Rates")){
                    category_id = "1";

                }else if(SubCategoryStr.equalsIgnoreCase("NECC Rates")){
                    category_id = "2";
                }
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_month = (Spinner) findViewById(R.id.spinner_month);
        ArrayAdapter month = new ArrayAdapter(this,android.R.layout.simple_spinner_item,MonthList);
        month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_month.setAdapter(month);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
        final ArrayAdapter year = new ArrayAdapter(this,android.R.layout.simple_spinner_item,YearList);
        year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_year.setAdapter(year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_day = (Spinner) findViewById(R.id.spinner_day);
        ArrayAdapter day = new ArrayAdapter(this,android.R.layout.simple_spinner_item,DayList);
        day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_day.setAdapter(day);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SubCategoryStr.equalsIgnoreCase("SubCategory") || Year.equalsIgnoreCase("") ||
                           Month.equalsIgnoreCase("") || Day.equalsIgnoreCase("")){
                    Toast.makeText(MarketActivity.this, "Select Fields", Toast.LENGTH_SHORT).show();
                }else{
                    getMarketList();
                }
            }

        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MarketActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void progressdialog() {
        progressDialog = new ProgressDialog(MarketActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void getMarketList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrdersApi service = retrofit.create(OrdersApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getMarketList("1",category_id,Year,Month,Day);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));

                        System.out.println("----------------------------------------------------");

                        if (response.body().toString() != null) {

                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Categeries", "response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        JSONObject successObject = root.getJSONObject("success");
                                        String success = successObject.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("data");
                                            marketModels = new ArrayList<>();
                                            for(int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String location = jsonObject.getString("location");
                                                String description = jsonObject.getString("description");
                                                String day = jsonObject.getString("day");
                                                String month = jsonObject.getString("month");
                                                String year = jsonObject.getString("year");
                                                String currency = jsonObject.getString("currency");

                                                MarketModel marketModel = new MarketModel();
                                                marketModel.setLocation(location);
                                                marketModel.setDescription(description);
                                                marketModel.setDay(day);
                                                marketModel.setMonth(month);
                                                marketModel.setYear(year);
                                                marketModel.setCurrency(currency);
                                                marketModels.add(marketModel);

                                            }
                                            if(marketModels != null){
                                                market_price_available_text.setVisibility(View.GONE);
                                                marketsAdapter = new MarketsAdapter(MarketActivity.this,marketModels);
                                                markets_recyclerview.setAdapter(marketsAdapter);
                                                marketsAdapter.notifyDataSetChanged();
                                            }else{
                                                market_price_available_text.setVisibility(View.VISIBLE);
                                                markets_recyclerview.setVisibility(View.GONE);
                                             //   Toast.makeText(MarketActivity.this, "Market Prices Are Not Available", Toast.LENGTH_SHORT).show();
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                        else {
                                            market_price_available_text.setVisibility(View.VISIBLE);
                                            markets_recyclerview.setVisibility(View.GONE);
                                            // Toast.makeText(MarketActivity.this, "Market Prices Are Not Available", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    }
                    else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}
