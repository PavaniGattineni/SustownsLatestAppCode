package com.sustowns.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.FilterCategoryExpaListAdapter;
import com.sustowns.sustownsapp.Adapters.ProductsAdapter;
import com.sustowns.sustownsapp.Adapters.SearchProductsAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.FiltersApi;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.ContinentModel;
import com.sustowns.sustownsapp.Models.CountryModel;
import com.sustowns.sustownsapp.Models.PoultryProductsModel;
import com.sustowns.sustownsapp.Models.SearchProductsModel;
import com.sustowns.sustownsapp.Models.StateModel;
import com.sustowns.sustownsapp.helpers.Helper;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ProductsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static String username, useremail;
    GridView products_list;
    PreferenceUtils preferenceUtils;
    LinearLayout home, news, store, bidcontracts, poultryprices, choose_category, ll_filter, ll_seekbar, ll_products, filter_layout;
    ImageView filter_icon, cart_img;
    LinearLayout navigation;
    EditText search_et;
    TextView home_text, news_text, store_text,cart_count, contracts_text, market_text, empty, price_text, not_available, btn_clear;
    LinearLayout location_tv;
    ProgressDialog progressDialog;
    String search_st, locationTypeStr, l_type = "", continent_values, country_radiobtn, continenetIds;
    ProductsAdapter productsAdapter;
    ImageView search_img, backarrow;
    ArrayList<PoultryProductsModel> poultryProductsModels;
    ArrayList<SearchProductsModel> searchProductsModels;
    SearchProductsAdapter searchProductsAdapter;
    LinearLayout ll_category, ll_price, ll_listing_type, ll_prod_location, category_layout, ll_continent, ll_states, ll_country, ll_toolbar;
    View view1, view2, view3, view4, view_sector, view_country, view_state;
    RecyclerView filter_recyclerview;
    EditText min_price, max_price;
    FilterCategoryAdapter filterCategoryAdapter = new FilterCategoryAdapter();
    FilterListTypeAdapter filterListTypeAdapter = new FilterListTypeAdapter();
    FilterContinentAdapter filterContinentAdapter = new FilterContinentAdapter();
    Button cancel_filter, apply_filter, filter_btn;
    String[] sector_type = {"All", "B2B", "Buyer Network"};
    String[] LocationType = {"select", "Product Location", "Vendor Location", "Product Origin"};
    ShimmerFrameLayout shimmer_grid_container;
    Helper helper;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SparseBooleanArray mSelectedItemsCatIds = new SparseBooleanArray();
    SparseBooleanArray mSelectedItemsListIds = new SparseBooleanArray();
    SparseBooleanArray mSelectedContinents = new SparseBooleanArray();
    SparseBooleanArray mSelectedStates = new SparseBooleanArray();
    Spinner spinner_location_type;
    ArrayList<ContinentModel> continentModels;
    FilterCountryAdapter filterCountryAdapter = new FilterCountryAdapter();
    ArrayList<CountryModel> countryModels;
    ArrayList<StateModel> stateModels;
    FilterStatesAdapter filterStatesAdapter = new FilterStatesAdapter();
    ExpandableListView category_exp_list;
    FilterCategoryExpaListAdapter listAdapter = new FilterCategoryExpaListAdapter();
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> subSubCatHash;
    String cartcount,user_role,user_id,catSlug,subCatId,subCatTitle,subCatSlug,subSubCatId,subSubCatTitle,subSubCatSlug,minPrice = "",maxPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products);
        preferenceUtils = new PreferenceUtils(ProductsActivity.this);
        username = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName, "");
        useremail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL, "");
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE,"");
        helper = new Helper(this);
        products_list = (GridView) findViewById(R.id.products_list_grid);
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
        store_text.setTextColor(getResources().getColor(R.color.appcolor));
        ll_toolbar = (LinearLayout) findViewById(R.id.ll_toolbar);
        ll_toolbar.setVisibility(View.VISIBLE);
        ll_filter = (LinearLayout) findViewById(R.id.ll_filter);
        ll_seekbar = (LinearLayout) findViewById(R.id.ll_seekbar);
        ll_products = (LinearLayout) findViewById(R.id.ll_products);
        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);
        navigation = (LinearLayout) findViewById(R.id.navigation);
       // seekBar = (RangeSeekBar) findViewById(R.id.rangeSeekbar);
        not_available = (TextView) findViewById(R.id.not_available);
        price_text = (TextView) findViewById(R.id.price_text);
        cart_count = (TextView) findViewById(R.id.cart_count);
        category_exp_list = (ExpandableListView) findViewById(R.id.category_exp_list);
        // selected item is expanding and others are collapsed in expandable listview
        category_exp_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    category_exp_list.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        // filterssss
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        location_tv = (LinearLayout) findViewById(R.id.location_tv);
        category_layout = (LinearLayout) findViewById(R.id.category_layout);
        ll_continent = (LinearLayout) findViewById(R.id.ll_continent);
        ll_country = (LinearLayout) findViewById(R.id.ll_country);
        ll_states = (LinearLayout) findViewById(R.id.ll_states);

        min_price = (EditText) findViewById(R.id.min_price);
        max_price = (EditText) findViewById(R.id.max_price);
        btn_clear = (TextView) findViewById(R.id.btn_clear);
        filter_btn = (Button) findViewById(R.id.filter_btn);
        ll_category = (LinearLayout) findViewById(R.id.ll_category);
        ll_price = (LinearLayout) findViewById(R.id.ll_price);
        ll_listing_type = (LinearLayout) findViewById(R.id.ll_listing_type);
        ll_prod_location = (LinearLayout) findViewById(R.id.ll_prod_location);
        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);
        view3 = (View) findViewById(R.id.view3);
        view4 = (View) findViewById(R.id.view4);
        view_sector = (View) findViewById(R.id.view_sector);
        view_country = (View) findViewById(R.id.view_country);
        view_state = (View) findViewById(R.id.view_state);
        cancel_filter = (Button) findViewById(R.id.cancel_filter);
        apply_filter = (Button) findViewById(R.id.apply_filter);
        filter_recyclerview = (RecyclerView) findViewById(R.id.filter_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductsActivity.this, LinearLayoutManager.VERTICAL, false);
        filter_recyclerview.setLayoutManager(layoutManager);
        filter_recyclerview.setHasFixedSize(true);
        /*filterCategoryAdapter = new FilterCategoryAdapter(ProductsActivity.this, categories, mSelectedItemsCatIds);
        filter_recyclerview.setAdapter(filterCategoryAdapter);*/
        category_layout.setVisibility(View.VISIBLE);
        ll_seekbar.setVisibility(View.GONE);
        location_tv.setVisibility(View.GONE);
        shimmer_grid_container = findViewById(R.id.shimmer_grid_container);
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                 min_price.setText(String.valueOf(minValue));
                 max_price.setText(String.valueOf(maxValue));
                 minPrice = String.valueOf(minValue);
                 maxPrice = String.valueOf(maxValue);
            }
        });
// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));

            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItemsListIds.clear();
                mSelectedItemsCatIds.clear();
                mSelectedContinents.clear();
                mSelectedStates.clear();
                ll_toolbar.setVisibility(View.VISIBLE);
                ll_products.setVisibility(View.VISIBLE);
                filter_layout.setVisibility(View.GONE);
            }
        });
        cancel_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_toolbar.setVisibility(View.VISIBLE);
                ll_products.setVisibility(View.VISIBLE);
                filter_layout.setVisibility(View.GONE);
            }
        });
        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_toolbar.setVisibility(View.VISIBLE);
                ll_products.setVisibility(View.VISIBLE);
                filter_layout.setVisibility(View.GONE);
                getFiltersProductsList();
                //Toast.makeText(ProductsActivity.this, "Apply", Toast.LENGTH_SHORT).show();
            }
        });
        ll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_seekbar.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);
                location_tv.setVisibility(View.GONE);

                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view_sector.setVisibility(View.GONE);
                view_country.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);

                ll_category.setBackgroundColor(getResources().getColor(R.color.white));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
                getFilterCategories();
              /*  filterCategoryAdapter = new FilterCategoryAdapter(ProductsActivity.this, categories, mSelectedItemsCatIds);
                filter_recyclerview.setAdapter(filterCategoryAdapter);*/
            }
        });
        ll_listing_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_layout.setVisibility(View.VISIBLE);
                ll_seekbar.setVisibility(View.GONE);
                location_tv.setVisibility(View.GONE);

                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view_sector.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view_country.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);
                ll_category.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.white));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
                filterListTypeAdapter = new FilterListTypeAdapter(ProductsActivity.this, sector_type, mSelectedItemsListIds);
                filter_recyclerview.setAdapter(filterListTypeAdapter);
            }
        });
        ll_prod_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_layout.setVisibility(View.VISIBLE);
                ll_seekbar.setVisibility(View.GONE);
                location_tv.setVisibility(View.GONE);

                view3.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view_sector.setVisibility(View.GONE);
                view4.setVisibility(View.VISIBLE);
                view_country.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);
                ll_category.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.white));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
            }
        });

        ll_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_layout.setVisibility(View.GONE);
                ll_seekbar.setVisibility(View.VISIBLE);
                location_tv.setVisibility(View.GONE);

                view_sector.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view_country.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);
                ll_price.setBackgroundColor(getResources().getColor(R.color.white));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_category.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
            }
        });
        ll_continent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_seekbar.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);
                location_tv.setVisibility(View.GONE);

                view3.setVisibility(View.GONE);
                view_sector.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view_country.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);

                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.white));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_category.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
                getFilterContinents();

            }
        });
        spinner_location_type = (Spinner) findViewById(R.id.spinner_location_type);
        ArrayAdapter category = new ArrayAdapter(ProductsActivity.this, android.R.layout.simple_spinner_item, LocationType);
        category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_location_type.setAdapter(category);
        spinner_location_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationTypeStr = parent.getItemAtPosition(position).toString();
                if (locationTypeStr.equalsIgnoreCase("Product Location")) {
                    l_type = "1";
                } else if (locationTypeStr.equalsIgnoreCase("Vendor Location")) {
                    l_type = "2";
                } else if (locationTypeStr.equalsIgnoreCase("Product Origin")) {
                    l_type = "3";
                } else {
                    l_type = "";
                }
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ll_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_seekbar.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);
                location_tv.setVisibility(View.GONE);

                view_sector.setVisibility(View.GONE);
                view_country.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);

                ll_country.setBackgroundColor(getResources().getColor(R.color.white));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_category.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                getCountries();
                //updateData();
            }
        });
        ll_states.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterCities();
                ll_seekbar.setVisibility(View.GONE);
                category_layout.setVisibility(View.VISIBLE);
                location_tv.setVisibility(View.GONE);

                view_sector.setVisibility(View.GONE);
                view_state.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view_country.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);

                ll_states.setBackgroundColor(getResources().getColor(R.color.white));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_category.setBackgroundColor(getResources().getColor(R.color.light_black));
            }
        });

        //  filter_icon = (ImageView) findViewById(R.id.filter_icon);
        search_et = (EditText) findViewById(R.id.search_et);
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    search_et.setCursorVisible(true);
                    search_st = search_et.getText().toString().trim();
                    if (search_st.equalsIgnoreCase("")) {
                        Toast.makeText(ProductsActivity.this, "please type suggestion and search", Toast.LENGTH_SHORT).show();
                    } else {
                        searchProducts();
                    }
                    return true;
                }
                return false;
            }
        });
        search_img = (ImageView) findViewById(R.id.search_img);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        empty = (TextView) findViewById(R.id.empty);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                search_et.setCursorVisible(true);
                search_st = search_et.getText().toString().trim();
                if (search_st.equalsIgnoreCase("")) {
                    Toast.makeText(ProductsActivity.this, "please type suggestion and search", Toast.LENGTH_SHORT).show();
                } else {
                    searchProducts();
                }
            }
        });
        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_toolbar.setVisibility(View.GONE);
                ll_products.setVisibility(View.GONE);
                filter_layout.setVisibility(View.VISIBLE);
                navigation.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                view_sector.setVisibility(View.GONE);
                view_country.setVisibility(View.GONE);
                view_state.setVisibility(View.GONE);

                ll_category.setBackgroundColor(getResources().getColor(R.color.white));
                ll_continent.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_price.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_listing_type.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_prod_location.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_states.setBackgroundColor(getResources().getColor(R.color.light_black));
                ll_country.setBackgroundColor(getResources().getColor(R.color.light_black));
                getFilterCategories();
              /*  filterCategoryAdapter = new FilterCategoryAdapter(ProductsActivity.this, categories, mSelectedItemsCatIds);
                filter_recyclerview.setAdapter(filterCategoryAdapter);*/
            }
        });
        choose_category = (LinearLayout) findViewById(R.id.choose_category);
        choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsActivity.this, CategoryExpandListActivity.class);
                startActivity(i);
            }
        });
        search_et.setFocusableInTouchMode(false);
        search_et.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                search_et.setFocusableInTouchMode(true);
                search_et.requestFocus();
                return false;
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ProductsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ProductsActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ProductsActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ProductsActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(ProductsActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        getPoultryProducts();
        cartCount();
    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        search_et.setText("");
        search_et.setFocusableInTouchMode(false);
        mSwipeRefreshLayout.setRefreshing(false);
        getPoultryProducts();
        cartCount();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProductsActivity.this,MainActivity.class);
        startActivity(i);
        finish();
       /* Intent intent = new Intent(ProductsActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }
    private void getPoultryProducts() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        if(user_role.equalsIgnoreCase("general")){
            callRetrofit = service.StoreGeneralProducts(user_id);
        }else if(user_role.equalsIgnoreCase("poultry")){
            callRetrofit = service.StorePoultryProducts(user_id);
        }else{ 
            callRetrofit = service.StoreGeneralProducts(user_id);
        }
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONArray jsonArray = root.getJSONArray("getproduct");
                                            poultryProductsModels = new ArrayList<PoultryProductsModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String userid = jsonObject.getString("userid");
                                                String product_master_category = jsonObject.getString("product_master_category");
                                                String pr_bussid = jsonObject.getString("pr_bussid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_quality = jsonObject.getString("pr_quality");
                                                String pr_image = jsonObject.getString("pr_image");
                                                String pr_eggtype = jsonObject.getString("pr_eggtype");
                                                String job_location = jsonObject.getString("job_location");
                                                String pr_catid = jsonObject.getString("pr_catid");
                                                String pr_price = jsonObject.getString("pr_price");
                                                String pr_currency = jsonObject.getString("pr_currency");
                                                String pr_discount = jsonObject.getString("pr_discount");
                                                String pr_stocks = jsonObject.getString("pr_stocks");
                                                String pr_min = jsonObject.getString("pr_min");
                                                String service_charge = jsonObject.getString("service_charge");
                                                String pr_weight_unit = jsonObject.getString("pr_weight_unit");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String pr_type = jsonObject.getString("pr_type");
                                                String pr_packtype = jsonObject.getString("pr_packtype");
                                                String city = jsonObject.getString("city");
                                                String state = jsonObject.getString("state");
                                                String country = jsonObject.getString("country");
                                                String pr_other_type = jsonObject.getString("pr_other_type");
                                                String makeoffer = jsonObject.getString("makeoffer");
                                                String zipcode = jsonObject.getString("zipcode");
                                                String pr_gweight = jsonObject.getString("pr_gweight");
                                                String pr_gweight_unit = jsonObject.getString("pr_gweight_unit");
                                                String shipingprovide = jsonObject.getString("shipingprovide");
                                                String days = jsonObject.getString("days");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String weight_unit = jsonObject.getString("weight_unit");
                                                String country_name = jsonObject.getString("country_name");
                                                String city_name = jsonObject.getString("city_name");
                                                String imagepath = jsonObject.getString("imagepath");
                                                String prod_image = imagepath + pr_image;
                                                preferenceUtils.saveString(PreferenceUtils.PRO_ID, "");

                                                PoultryProductsModel poultryProductsModel = new PoultryProductsModel();
                                                poultryProductsModel.setId(id);
                                                poultryProductsModel.setUserid(userid);
                                                poultryProductsModel.setProduct_master_category(product_master_category);
                                                poultryProductsModel.setPr_bussid(pr_bussid);
                                                poultryProductsModel.setPr_title(pr_title);
                                                poultryProductsModel.setPr_quality(pr_quality);
                                                poultryProductsModel.setPr_eggtype(pr_eggtype);
                                                poultryProductsModel.setJob_location(job_location);
                                                poultryProductsModel.setPr_catid(pr_catid);
                                                poultryProductsModel.setPr_price(pr_price);
                                                poultryProductsModel.setPr_currency(pr_currency);
                                                poultryProductsModel.setPr_discount(pr_discount);
                                                poultryProductsModel.setService_charge(service_charge);
                                                poultryProductsModel.setPr_stocks(pr_stocks);
                                                poultryProductsModel.setPr_min(pr_min);
                                                poultryProductsModel.setPr_weight_unit(pr_weight_unit);
                                                poultryProductsModel.setPr_weight(pr_weight);
                                                poultryProductsModel.setImagepath(imagepath);
                                                poultryProductsModel.setPr_image(prod_image);
                                                poultryProductsModel.setCity(city);
                                                poultryProductsModel.setCountry(country);
                                                poultryProductsModel.setState(state);
                                                poultryProductsModel.setMakeoffer(makeoffer);
                                                poultryProductsModel.setPr_gweight(pr_gweight);
                                                poultryProductsModel.setPr_gweight_unit(pr_gweight_unit);
                                                poultryProductsModel.setDays(days);
                                                poultryProductsModel.setPr_sku(pr_sku);
                                                poultryProductsModel.setWeight_unit(weight_unit);
                                                poultryProductsModel.setCountry_name(country_name);
                                                poultryProductsModel.setCity_name(city_name);
                                                poultryProductsModels.add(poultryProductsModel);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        helper.stopShimmer(shimmer_grid_container);
                                        e.printStackTrace();
                                    }
                                    if (poultryProductsModels != null) {
                                        productsAdapter = new ProductsAdapter(ProductsActivity.this, poultryProductsModels);
                                        products_list.setAdapter(productsAdapter);
                                        productsAdapter.notifyDataSetChanged();
                                    }
                                    helper.stopShimmer(shimmer_grid_container);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    helper.stopShimmer(shimmer_grid_container);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                Toast.makeText(ProductsActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }

    private void searchProducts() {
        shimmer_grid_container.setVisibility(View.VISIBLE);
        shimmer_grid_container.startShimmerAnimation();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);
        search_st = search_et.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.searchStoreProducts(search_st);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                if (response.isSuccessful()) {
                    search_et.setFocusableInTouchMode(false);
                    if (response.body().toString() != null) {
                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    Integer success;
                                    success = root.getInt("success");
                                    String imagepath = root.getString("imagepath");
                                    if (success == 1) {
                                        JSONArray msg = root.getJSONArray("viewproducts");
                                        searchProductsModels = new ArrayList<SearchProductsModel>();
                                        for (int i = 0; i < msg.length(); i++) {
                                            JSONObject Obj = msg.getJSONObject(i);
                                            String id = Obj.getString("id");
                                            String pr_userid = Obj.getString("pr_userid");
                                            String franchise_id = Obj.getString("franchise_id");
                                            String pr_title = Obj.getString("pr_title");
                                            String pr_image = Obj.getString("pr_image");
                                            String pr_price = Obj.getString("pr_price");
                                            String pr_currency = Obj.getString("pr_currency");
                                            String pr_min = Obj.getString("pr_min");
                                            String pr_weight = Obj.getString("pr_weight");
                                            String pr_weight_unit = Obj.getString("weight_unit");
                                            String job_location = Obj.getString("job_location");
                                            String state = Obj.getString("state");
                                            String country = Obj.getString("country");
                                            String country_name = Obj.getString("country_name");
                                            String city_name = Obj.getString("city_name");
                                            preferenceUtils.saveString(PreferenceUtils.PRO_ID, "");

                                            SearchProductsModel poultryProductsModel = new SearchProductsModel();
                                            poultryProductsModel.setId(id);
                                            poultryProductsModel.setPr_title(pr_title);
                                            poultryProductsModel.setPr_price(pr_price);
                                            poultryProductsModel.setPr_currency(pr_currency);
                                            poultryProductsModel.setPr_min(pr_min);
                                            poultryProductsModel.setPr_weight_unit(pr_weight_unit);
                                            poultryProductsModel.setPr_weight(pr_weight);
                                            poultryProductsModel.setImagepath(pr_image);
                                            poultryProductsModel.setPr_image(imagepath+pr_image);
                                            poultryProductsModel.setCountry_name(country_name);
                                            poultryProductsModel.setCity_name(city_name);
                                            poultryProductsModel.setJob_location(job_location);
                                            searchProductsModels.add(poultryProductsModel);
                                        }
                                        if (searchProductsModels != null) {
                                            products_list.setVisibility(View.VISIBLE);
                                            empty.setVisibility(View.GONE);
                                            searchProductsAdapter = new SearchProductsAdapter(ProductsActivity.this, searchProductsModels);
                                            products_list.setAdapter(searchProductsAdapter);
                                        }
                                        helper.stopShimmer(shimmer_grid_container);
                                    } else if (success == 0) {
                                        products_list.setVisibility(View.GONE);
                                        empty.setVisibility(View.VISIBLE);
                                        helper.stopShimmer(shimmer_grid_container);
                                    }
                                } catch (JSONException e) {
                                    helper.stopShimmer(shimmer_grid_container);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    helper.stopShimmer(shimmer_grid_container);
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }
    private void getFilterContinents() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FiltersApi service = retrofit.create(FiltersApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFilterContinentList(l_type);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONArray jsonArray = root.getJSONArray("continent");
                                            continentModels = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String continent_code = jsonObject.getString("continent_code");
                                                String continent_name = jsonObject.getString("continent_name");

                                                ContinentModel continentModel = new ContinentModel();
                                                continentModel.setContinent_code(continent_code);
                                                continentModel.setContinent_name(continent_name);
                                                continentModels.add(continentModel);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        helper.stopShimmer(shimmer_grid_container);
                                        e.printStackTrace();
                                    }
                                    if (continentModels != null) {
                                        filterContinentAdapter = new FilterContinentAdapter(ProductsActivity.this, continentModels, mSelectedContinents);
                                        filter_recyclerview.setAdapter(filterContinentAdapter);
                                        filterContinentAdapter.notifyDataSetChanged();
                                    }
                                    helper.stopShimmer(shimmer_grid_container);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    helper.stopShimmer(shimmer_grid_container);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }

    private void getCountries() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FiltersApi service = retrofit.create(FiltersApi.class);

        Call<JsonElement> callRetrofit = null;
        if (filterContinentAdapter != null) {
            callRetrofit = service.getFilterCountriesList(filterContinentAdapter.getContinentIds());

            callRetrofit.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.d("Success Call", ">>>>" + call);
                            Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                    Log.d("Reg", "Response  >>" + searchResponse.toString());

                                    if (searchResponse != null) {
                                        JSONObject root = null;
                                        try {
                                            root = new JSONObject(searchResponse);
                                            String message;
                                            Integer success;
                                            success = root.getInt("success");
                                            //   message = root.getString("message");
                                            if (success == 1) {
                                                JSONArray jsonArray = root.getJSONArray("country");
                                                countryModels = new ArrayList<>();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    String countryCode = jsonObject.getString("countryCode");
                                                    String country_name = jsonObject.getString("country_name");

                                                    CountryModel countryModel = new CountryModel();
                                                    countryModel.setCountryCode(countryCode);
                                                    countryModel.setCountry_name(country_name);
                                                    countryModels.add(countryModel);
                                                }
                                            }

                                        } catch (JSONException e) {
                                            helper.stopShimmer(shimmer_grid_container);
                                            e.printStackTrace();
                                        }
                                        if (filterContinentAdapter != null)
                                            filterContinentAdapter.removeList();
                                        filterCountryAdapter = new FilterCountryAdapter(ProductsActivity.this, countryModels,mSelectedItemsListIds);
                                        filter_recyclerview.setAdapter(filterCountryAdapter);
                                        filterCountryAdapter.notifyDataSetChanged();
//                                        filterCountryAdapter.updateReceiptsList(countryModels);
                                        helper.stopShimmer(shimmer_grid_container);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        helper.stopShimmer(shimmer_grid_container);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d("Error Call", ">>>>" + call.toString());
                    Log.d("Error", ">>>>" + t.toString());
                    //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                    helper.stopShimmer(shimmer_grid_container);
                }
            });
        } else {
            Toast.makeText(this, "Please choose continent", Toast.LENGTH_SHORT).show();
        }
    }
    private void getFilterCities() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FiltersApi service = retrofit.create(FiltersApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFilterCitiesList(filterCountryAdapter.selectedCountry);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONArray jsonArray = root.getJSONArray("states");
                                            stateModels = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String state_code = jsonObject.getString("state_code");
                                                String state_name = jsonObject.getString("state_name");
                                                StateModel stateModel = new StateModel();
                                                stateModel.setState_code(state_code);
                                                stateModel.setState_name(state_name);
                                                stateModels.add(stateModel);
                                            }
                                        }
                                        if (stateModels != null) {
                                            filterStatesAdapter = new FilterStatesAdapter(ProductsActivity.this, stateModels, mSelectedStates);
                                            filter_recyclerview.setAdapter(filterStatesAdapter);
                                            filterStatesAdapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        helper.stopShimmer(shimmer_grid_container);
                                        e.printStackTrace();
                                    }

                                    helper.stopShimmer(shimmer_grid_container);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    helper.stopShimmer(shimmer_grid_container);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }

    private void getFilterCategories() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FiltersApi service = retrofit.create(FiltersApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFilterCategoriesList();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        Integer success;
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            listDataHeader = new ArrayList<>();
                                            JSONArray productcatArray = root.getJSONArray("productcat");
                                            listDataChild = new HashMap<String, List<String>>();
                                            subSubCatHash = new HashMap<String, List<String>>();

                                            if(productcatArray.length() > 0) {
                                                for (int i = 0; i < productcatArray.length(); i++) {
                                                    JSONObject catObj = productcatArray.getJSONObject(i);
                                                    catSlug = catObj.getString("slug");
                                                    listDataHeader.add(catSlug);
                                                    try {
                                                        JSONArray subCatArray = catObj.getJSONArray("sub_cat");
                                                        List<String> subCatList = new ArrayList<>();
                                                        if (subCatArray.length() > 0) {
                                                            for (int j = 0; j < subCatArray.length(); j++) {
                                                                JSONObject subCatObj = subCatArray.getJSONObject(j);
                                                                subCatSlug = subCatObj.getString("slug");
                                                                subCatList.add(subCatSlug);

                                                                try {
                                                                    JSONArray subSubCatArray = subCatObj.getJSONArray("sub_cat");
                                                                    List<String> subSubCatList = new ArrayList<>();
                                                                    if (subSubCatArray.length() > 0) {
                                                                        for (int k = 0; k < subSubCatArray.length(); k++) {
                                                                            JSONObject subSubCatObj = subSubCatArray.getJSONObject(k);
                                                                            subSubCatSlug = subSubCatObj.getString("slug");
                                                                            subSubCatList.add(subSubCatSlug);
                                                                        }
                                                                        subSubCatHash.put(subCatSlug, subSubCatList);
                                                                    } else {
                                                                        // add sub cat as sub sub cat
                                                                        subSubCatList.add(subCatSlug);
                                                                        subSubCatHash.put(subCatSlug, subSubCatList);
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    List<String> subSubCatList = new ArrayList<>();
                                                                    subSubCatList.add(subCatSlug);
                                                                    subSubCatHash.put(subCatSlug, subSubCatList);
                                                                }
                                                            }
                                                            listDataChild.put(catSlug, subCatList);
                                                        } else {
                                                            // Add prod cat as sub cat & sub-sub cat
                                                            subCatList.add(catSlug);
                                                            listDataChild.put(catSlug, subCatList);
                                                            List<String> subSubCatList = new ArrayList<>();
                                                            subSubCatList.add(catSlug);
                                                            subSubCatHash.put(catSlug, subSubCatList);
                                                        }
                                                    } catch (Exception e){
                                                        e.printStackTrace();
                                                        List<String> subCatList = new ArrayList<>();
                                                        subCatList.add(catSlug);
                                                        listDataChild.put(catSlug, subCatList);
                                                        List<String> subSubCatList = new ArrayList<>();
                                                        subSubCatList.add(catSlug);
                                                        subSubCatHash.put(catSlug, subSubCatList);
                                                    }
                                                }
                                            }
                                            /*ViewGroup.LayoutParams params = category_exp_list.getLayoutParams();
                                            params.height = listDataHeader.size() * 20;
                                            category_exp_list.setLayoutParams(params);
                                            category_exp_list.requestLayout();*/
                                            listAdapter = new FilterCategoryExpaListAdapter(ProductsActivity.this, listDataHeader, listDataChild);
                                            category_exp_list.setAdapter(listAdapter);
                                            listAdapter.notifyDataSetChanged();
                                            helper.stopShimmer(shimmer_grid_container);

                                        }

                                    }catch (JSONException e) {
                                        helper.stopShimmer(shimmer_grid_container);
                                        e.printStackTrace();
                                    }
                                    helper.stopShimmer(shimmer_grid_container);

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    helper.stopShimmer(shimmer_grid_container);
                    e.printStackTrace();
                }
                helper.stopShimmer(shimmer_grid_container);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }

    private void getFiltersProductsList() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FiltersApi service = retrofit.create(FiltersApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFilterProducts(l_type, filterContinentAdapter.getContinentIds(), filterCountryAdapter.selectedCountry,
                    filterStatesAdapter.getStateIds(), filterCategoryAdapter.selectedCategory, minPrice, maxPrice);
        //  callRetrofit = service.getFilterProducts("1","AS,AF","IN","GJ,AP","vegetables","0","9000");
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success;
                                        success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("getallproduct");
                                            poultryProductsModels = new ArrayList<PoultryProductsModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String userid = jsonObject.getString("pr_userid");
                                                String product_master_category = jsonObject.getString("product_master_category");
                                                String pr_bussid = jsonObject.getString("pr_bussid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_quality = jsonObject.getString("pr_quality");
                                                String pr_image = jsonObject.getString("pr_image");
                                                String pr_eggtype = jsonObject.getString("pr_eggtype");
                                                String job_location = jsonObject.getString("job_location");
                                                String pr_catid = jsonObject.getString("pr_catid");
                                                String pr_price = jsonObject.getString("pr_price");
                                                String pr_currency = jsonObject.getString("pr_currency");
                                                String pr_discount = jsonObject.getString("pr_discount");
                                                String pr_stocks = jsonObject.getString("pr_stocks");
                                                String pr_min = jsonObject.getString("pr_min");
                                                String service_charge = jsonObject.getString("service_charge");
                                                String pr_weight_unit = jsonObject.getString("pr_weight_unit");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String pr_type = jsonObject.getString("pr_type");
                                                String pr_packtype = jsonObject.getString("pr_packtype");
                                                String city = jsonObject.getString("city");
                                                String state = jsonObject.getString("state");
                                                String country = jsonObject.getString("country");
                                                String pr_other_type = jsonObject.getString("pr_other_type");
                                                String makeoffer = jsonObject.getString("makeoffer");
                                                String pr_gweight = jsonObject.getString("pr_gweight");
                                                String pr_gweight_unit = jsonObject.getString("pr_gweight_unit");
                                                String days = jsonObject.getString("days");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String imagepath = jsonObject.getString("imagepath");
                                                String prod_image = imagepath + pr_image;
                                                preferenceUtils.saveString(PreferenceUtils.PRO_ID, "");

                                                PoultryProductsModel poultryProductsModel = new PoultryProductsModel();
                                                poultryProductsModel.setId(id);
                                                poultryProductsModel.setUserid(userid);
                                                poultryProductsModel.setProduct_master_category(product_master_category);
                                                poultryProductsModel.setPr_bussid(pr_bussid);
                                                poultryProductsModel.setPr_title(pr_title);
                                                poultryProductsModel.setPr_quality(pr_quality);
                                                poultryProductsModel.setPr_eggtype(pr_eggtype);
                                                poultryProductsModel.setJob_location(job_location);
                                                poultryProductsModel.setPr_catid(pr_catid);
                                                poultryProductsModel.setPr_price(pr_price);
                                                poultryProductsModel.setPr_currency(pr_currency);
                                                poultryProductsModel.setPr_discount(pr_discount);
                                                poultryProductsModel.setPr_stocks(pr_stocks);
                                                poultryProductsModel.setPr_min(pr_min);
                                                poultryProductsModel.setPr_weight_unit(pr_weight_unit);
                                                poultryProductsModel.setPr_weight(pr_weight);
                                                poultryProductsModel.setImagepath(imagepath);
                                                poultryProductsModel.setPr_image(prod_image);
                                                poultryProductsModel.setCity(city);
                                                poultryProductsModel.setCountry(country);
                                                poultryProductsModel.setState(state);
                                                poultryProductsModel.setMakeoffer(makeoffer);
                                                poultryProductsModel.setPr_gweight(pr_gweight);
                                                poultryProductsModel.setPr_gweight_unit(pr_gweight_unit);
                                                poultryProductsModel.setDays(days);
                                                poultryProductsModel.setPr_sku(pr_sku);
                                                poultryProductsModels.add(poultryProductsModel);
                                            }
                                            if (poultryProductsModels != null) {
                                                productsAdapter = new ProductsAdapter(ProductsActivity.this, poultryProductsModels);
                                                products_list.setAdapter(productsAdapter);
                                                productsAdapter.notifyDataSetChanged();
                                            }else{
                                                products_list.setVisibility(View.GONE);
                                                Toast.makeText(ProductsActivity.this, "No Products are available.", Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(ProductsActivity.this, "appliedddddddddddd", Toast.LENGTH_SHORT).show();
                                        }else{
                                            products_list.setVisibility(View.GONE);
                                            Toast.makeText(ProductsActivity.this, "No Products are available.", Toast.LENGTH_SHORT).show();
                                        }

                                    }catch (JSONException e) {
                                        products_list.setVisibility(View.GONE);
                                        Toast.makeText(ProductsActivity.this, "No Products are available.", Toast.LENGTH_SHORT).show();
                                        helper.stopShimmer(shimmer_grid_container);
                                        e.printStackTrace();
                                    }
                                    helper.stopShimmer(shimmer_grid_container);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    helper.stopShimmer(shimmer_grid_container);
                    e.printStackTrace();
                }
                helper.stopShimmer(shimmer_grid_container);
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                Toast.makeText(ProductsActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }
    public void cartCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.cartCount(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());
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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success = root.getString("success");
                                        //   message = root.getString("message");
                                        if (success.equalsIgnoreCase("1")) {
                                            cartcount = root.getString("cartcount");
                                            cart_count.setText(cartcount);
                                        }else{
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //Toast.makeText(MainActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openSubSubCatList(String childText) {
        if(subSubCatHash != null){
            List<String> subSubCatlist = subSubCatHash.get(childText);
            filterCategoryAdapter = new FilterCategoryAdapter(ProductsActivity.this, subSubCatlist, mSelectedItemsCatIds);
            filter_recyclerview.setAdapter(filterCategoryAdapter);
            filterCategoryAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "There are no sub categories.", Toast.LENGTH_SHORT).show();
        }
    }
    //  My Services Adapter
    public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, user_id,selectedCategory = "";
        PreferenceUtils preferenceUtils;
        List<String> categories;
        ProgressDialog progressDialog;
        private SparseBooleanArray mSelectedItemsCatIds;
        private int lastCheckedPosition = -1;
        public FilterCategoryAdapter(Context context, List<String> categories, SparseBooleanArray mSelectedItemsCatIds) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.categories = categories;
            this.mSelectedItemsCatIds = mSelectedItemsCatIds;
        }
        public FilterCategoryAdapter(){
            //Empty Constructor
        }
        @Override
        public FilterCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_list, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new FilterCategoryAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final FilterCategoryAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.category_text.setText(categories.get(position));
            viewHolder.category_text.setChecked(position == lastCheckedPosition);
            if (mSelectedItemsCatIds.get(position)) {
                if(selectedCategory.equalsIgnoreCase("")){
                    viewHolder.category_text.setChecked(false);
                }else {
                    viewHolder.category_text.setChecked(true);
                }
            }
            viewHolder.category_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedCategory = viewHolder.category_text.getText().toString();
                        mSelectedItemsCatIds.put(position, true);
                    } else {
                        mSelectedItemsCatIds.delete(position);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return categories.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox category_text;
            public ViewHolder(View view) {
                super(view);
                category_text = (CheckBox) view.findViewById(R.id.category_text);
                category_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastCheckedPosition = getAdapterPosition();
                        //because of this blinking problem occurs so
                        //i have a suggestion to add notifyDataSetChanged();
                        notifyItemRangeChanged(0, categories.size());//blink list problem
                        notifyDataSetChanged();

                    }
                });

            }
        }
    }
    public class FilterListTypeAdapter extends RecyclerView.Adapter<FilterListTypeAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, user_id;
        PreferenceUtils preferenceUtils;
        String[] categories;
        // ArrayList<CartListModel> cartListModels;
        ProgressDialog progressDialog;
        SparseBooleanArray mSelectedItemsListIds;
        private int lastCheckedPosition = -1;
        public FilterListTypeAdapter(Context context, String[] categories, SparseBooleanArray mSelectedItemsListIds) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.categories = categories;
            this.mSelectedItemsListIds = mSelectedItemsListIds;
        }
        public FilterListTypeAdapter(){
            //Empty Constructor
        }
        @Override
        public FilterListTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_type_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new FilterListTypeAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final FilterListTypeAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.category_text.setText(categories[position]);
            viewHolder.category_text.setChecked(position == lastCheckedPosition);
            if (mSelectedItemsListIds.get(position)) {
                viewHolder.category_text.setChecked(true);
            }
            viewHolder.category_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mSelectedItemsListIds.put(position, true);
                    } else {
                        mSelectedItemsListIds.delete(position);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return categories.length;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox category_text;
            LinearLayout ll_categories;
            public ViewHolder(View view) {
                super(view);
                category_text = view.findViewById(R.id.category_text);
                ll_categories = (LinearLayout) findViewById(R.id.ll_categories);
                category_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastCheckedPosition = getAdapterPosition();
                        //because of this blinking problem occurs so
                        //i have a suggestion to add notifyDataSetChanged();
                        notifyItemRangeChanged(0, categories.length);//blink list problem
                        notifyDataSetChanged();

                    }
                });
            }
        }
    }
    public class FilterContinentAdapter extends RecyclerView.Adapter<FilterContinentAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, user_id, continent_cb;
        PreferenceUtils preferenceUtils;
        String[] categories;
        ArrayList<ContinentModel> continentModels;
        ProgressDialog progressDialog;
        SparseBooleanArray mSelectedItemsListIds;
        ArrayList<String> selectedContinents = new ArrayList<>();

        public FilterContinentAdapter(Context context, ArrayList<ContinentModel> continentModels, SparseBooleanArray mSelectedItemsListIds) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.continentModels = continentModels;
            this.mSelectedItemsListIds = mSelectedItemsListIds;
        }

        public FilterContinentAdapter(){
            //Empty Constructor
        }
        @Override
        public FilterContinentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_type_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new FilterContinentAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final FilterContinentAdapter.ViewHolder viewHolder, final int position) {

            viewHolder.continent_text.setText(continentModels.get(position).getContinent_name());
            if (mSelectedContinents.get(position)) {
                viewHolder.continent_text.setChecked(true);
                selectedContinents.add(continentModels.get(position).getContinent_code());
            }
            viewHolder.continent_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    continent_cb = continentModels.get(position).getContinent_code();
                    if (isChecked) {
                        selectedContinents.add(continent_cb);
                        mSelectedContinents.put(position, true);
                    } else {
                        mSelectedContinents.delete(position);
                        if (selectedContinents.toString().contains(continent_cb))
                            selectedContinents.remove(continent_cb);
                    }
                }
            });
        }

        public String getContinentIds() {
            if(selectedContinents != null || !selectedContinents.toString().isEmpty()) {
                return selectedContinents.toString().replace("[", "").replace("]", "");
            }else{
               return "";
            }
        }

        @Override
        public int getItemCount() {
            return continentModels.size();
        }

        public void removeList() {
            continentModels.clear();
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox continent_text;
            LinearLayout ll_categories;

            public ViewHolder(View view) {
                super(view);
                continent_text = view.findViewById(R.id.category_text);
                ll_categories = (LinearLayout) findViewById(R.id.ll_categories);
            }
        }
    }
    public class FilterCountryAdapter extends RecyclerView.Adapter<FilterCountryAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, user_id,selectedCountry = "";
        PreferenceUtils preferenceUtils;
        ArrayList<CountryModel> countryModels;
        ProgressDialog progressDialog;
        SparseBooleanArray mSelectedItemsListIds;
        private int lastCheckedPosition = -1;

        public FilterCountryAdapter(Context context, ArrayList<CountryModel> countryModels, SparseBooleanArray mSelectedItemsListIds) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.countryModels = countryModels;
            this.mSelectedItemsListIds = mSelectedItemsListIds;
            setHasStableIds(true);
        }
        public FilterCountryAdapter(){
            //Empty Constructor
        }
        @Override
        public FilterCountryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.continents_filter_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new FilterCountryAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final FilterCountryAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.country_radiobtn.setText(countryModels.get(position).getCountry_name());
            viewHolder.country_radiobtn.setChecked(position == lastCheckedPosition);
            if (mSelectedItemsListIds.get(position)) {
                viewHolder.country_radiobtn.setChecked(true);
            }
            viewHolder.country_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mSelectedItemsListIds.put(position, true);
                        if(selectedCountry != null || !selectedCountry.isEmpty())
                        selectedCountry = countryModels.get(position).getCountryCode();
                    } else {
                        mSelectedItemsListIds.delete(position);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return countryModels.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton country_radiobtn;
            public ViewHolder(View view) {
                super(view);
                country_radiobtn = view.findViewById(R.id.country_radiobtn);
                country_radiobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastCheckedPosition = getAdapterPosition();
                        //because of this blinking problem occurs so
                        //i have a suggestion to add notifyDataSetChanged();
                        notifyItemRangeChanged(0, countryModels.size());//blink list problem
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
    public class FilterStatesAdapter extends RecyclerView.Adapter<FilterStatesAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email, user_id, state_cb;
        PreferenceUtils preferenceUtils;
        ArrayList<StateModel> stateModels;
        ProgressDialog progressDialog;
        SparseBooleanArray mSelectedStates;
        ArrayList<String> selectedStates = new ArrayList<>();
        public FilterStatesAdapter(Context context, ArrayList<StateModel> stateModels, SparseBooleanArray mSelectedStates) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.stateModels = stateModels;
            this.mSelectedStates = mSelectedStates;
        }
        public FilterStatesAdapter(){
            //Empty Constructor
        }
        @Override
        public FilterStatesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_type_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new FilterStatesAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final FilterStatesAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.continent_text.setText(stateModels.get(position).getState_name());
            if (mSelectedStates.get(position)) {
                viewHolder.continent_text.setChecked(true);
                selectedStates.add(stateModels.get(position).getState_code());
            }
            viewHolder.continent_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    state_cb = stateModels.get(position).getState_code();
                    if (isChecked) {
                        selectedStates.add(state_cb);
                        mSelectedStates.put(position, true);
                    } else {
                        mSelectedStates.delete(position);
                        if (selectedStates.toString().contains(state_cb))
                            selectedStates.remove(state_cb);
                    }
                }
            });
        }
        public String getStateIds() {
            if(selectedStates != null ||!selectedStates.isEmpty()) {
                return selectedStates.toString().replace("[", "").replace("]", "");
            }else {
                return "";
            }
        }
        @Override
        public int getItemCount() {
            return stateModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox continent_text;
            LinearLayout ll_categories;

            public ViewHolder(View view) {
                super(view);
                continent_text = view.findViewById(R.id.category_text);
                ll_categories = (LinearLayout) findViewById(R.id.ll_categories);
            }
        }
    }

}






