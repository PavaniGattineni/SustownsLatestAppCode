package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.sustowns.sustownsapp.Models.ImageSliderModel;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.GridAdapter;
import com.sustowns.sustownsapp.Adapters.SlideImageAdapter1;
import com.sustowns.sustownsapp.Adapters.SlidingImageAdapter;
import com.sustowns.sustownsapp.Adapters.ViewPagerAdapter;
import com.sustowns.sustownsapp.Adapters.ViewPagerAdapter1;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.GetHomeProducts;
import com.sustowns.sustownsapp.helpers.Helper;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    public static NavigationView navigationView;
    ViewPager viewPager,viewpager1;
    Toolbar toolbar;
    ImageView cart_img;
    private static ViewPager mPager,mPager1;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static int currentPage1 = 0;
    private static int NUM_PAGES1 = 0;
    ArrayList<ImageSliderModel> imageSliderModels;
    Integer[] add_imgs = {R.drawable.slide1, R.drawable.slider5};
    private static final Integer[] IMAGES= {R.drawable.slide1, R.drawable.slider2,R.drawable.slide_image_6,R.drawable.eggs_1_image, R.drawable.slide3, R.drawable.slider5,};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<Integer> ImagesArray1 = new ArrayList<Integer>();
    RecyclerView gridView;
    TextView home_text,news_text,store_text,contracts_text,market_text,cart_count;
    PreferenceUtils preferenceUtils;
    public static String userName,userEmail,pro_id,image,fullname,user_role,userType;
    LinearLayout home,news,store,bidcontracts,poultryprices;
    ArrayList<GetHomeProducts> getHomeProducts;
    GridAdapter gridAdapter;
    ShimmerFrameLayout shimmer_grid_container;
    Helper helper;
    String cartcount,user_id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public static void hideAndShowItems() {
        Menu navMenu = navigationView.getMenu();
        if(user_role.equalsIgnoreCase("general")){
            navMenu.findItem(R.id.service_management_nav).setVisible(false);
            navMenu.findItem(R.id.storemanagement_nav).setVisible(true);
            navMenu.findItem(R.id.cluster_dashboard_nav).setVisible(false);
           // navMenu.findItem(R.id.warehouse_management_nav).setVisible(false);
        }
        if (user_role.equalsIgnoreCase("poultry")) {
            navMenu.findItem(R.id.service_management_nav).setVisible(false);
            navMenu.findItem(R.id.storemanagement_nav).setVisible(true);
            navMenu.findItem(R.id.cluster_dashboard_nav).setVisible(false);
           // navMenu.findItem(R.id.warehouse_management_nav).setVisible(false);
        } else if(user_role.equalsIgnoreCase("transport")){
            navMenu.findItem(R.id.service_management_nav).setVisible(true);
            navMenu.findItem(R.id.storemanagement_nav).setVisible(false);
            navMenu.findItem(R.id.cluster_dashboard_nav).setVisible(false);
            //navMenu.findItem(R.id.warehouse_management_nav).setVisible(false);
        }
        if(userType.equalsIgnoreCase("franchise")){
            navMenu.findItem(R.id.cluster_dashboard_nav).setVisible(true);
            navMenu.findItem(R.id.service_management_nav).setVisible(false);
            navMenu.findItem(R.id.storemanagement_nav).setVisible(true);
           // navMenu.findItem(R.id.warehouse_management_nav).setVisible(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        preferenceUtils = new PreferenceUtils(MainActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        helper = new Helper(MainActivity.this);
        checkAndRequestPermissions();
        user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE,"");
        userName = preferenceUtils.getStringFromPreference(PreferenceUtils.UserName,"");
        fullname = preferenceUtils.getStringFromPreference(PreferenceUtils.FULL_NAME,"");
        userEmail = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_EMAIL,"");
        userType = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_TYPE,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        home = (LinearLayout) findViewById(R.id.ll_home);
        news = (LinearLayout) findViewById(R.id.ll_news);
        store = (LinearLayout) findViewById(R.id.ll_store);
        bidcontracts = (LinearLayout) findViewById(R.id.ll_bidcontracts);
        poultryprices = (LinearLayout) findViewById(R.id.ll_poultryprices);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        home_text = (TextView) findViewById(R.id.home_footer);
        news_text = (TextView) findViewById(R.id.news_footer);
        store_text = (TextView) findViewById(R.id.store_footer);
        contracts_text = (TextView) findViewById(R.id.contracts_footer);
        market_text = (TextView) findViewById(R.id.marketing_footer);
        home_text.setTextColor(getResources().getColor(R.color.appcolor));
        cart_count = (TextView) findViewById(R.id.cart_count);
       /* BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(false);*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        TextView username_tv = (TextView) headerview.findViewById(R.id.user_name);
        TextView useremail_tv = (TextView) headerview.findViewById(R.id.user_email);
        if (userName.isEmpty() || userName == "") {
            username_tv.setText("Welcome User");
        }else{
            username_tv.setText("Welcome"+" "+fullname);
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewpager1 = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager1(viewpager1);

        shimmer_grid_container = findViewById(R.id.shimmer_grid_container);
        gridView = (RecyclerView) findViewById(R.id.gridview);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(MainActivity.this,2,LinearLayoutManager.VERTICAL,false);
        gridView.setLayoutManager(linearLayoutManager);
        // implement setOnItemClickListener event on GridView
     /*   gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pro_id = preferenceUtils.getStringFromPreference(PreferenceUtils.PROD_ID,"");
                image = preferenceUtils.getStringFromPreference(PreferenceUtils.IMAGE,"");
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                intent.putExtra("Pro_Id",pro_id);
                intent.putExtra("Image",image);
                startActivity(intent);
            }
        });*/
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        bidcontracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contracts_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MainActivity.this, BidContractsActivity.class);
                i.putExtra("Processed","0");
                startActivity(i);
            }
        });
        poultryprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_text.setTextColor(getResources().getColor(R.color.appcolor));
                Intent i = new Intent(MainActivity.this, MarketActivity.class);
                startActivity(i);
            }
        });
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
       // init1();
        hideAndShowItems();
        getProducts();
        cartCount();
        getSliderImages();
        init();
    }

    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    /*@Override
        public void onBackPressed() {
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }*/
/*
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
*/
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
    private void setupViewPager1(ViewPager viewPager1) {
        ViewPagerAdapter1 adapter1 = new ViewPagerAdapter1(getSupportFragmentManager());
        viewPager1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }
    private void init() {

        mPager = (ViewPager) findViewById(R.id.viewpager);
        //  mPager.setAdapter(new SlidingImageAdapter(MainActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator1);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

       /* NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
/*
    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(new SlidingImageAdapter(MainActivity.this, ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
*/
/*
    private void init1() {
        for (int i = 0; i < add_imgs.length; i++)
            ImagesArray1.add(add_imgs[i]);

        mPager1 = (ViewPager) findViewById(R.id.viewpager1);
        mPager1.setAdapter(new SlideImageAdapter1(MainActivity.this, ImagesArray1));
        NUM_PAGES1 = add_imgs.length;

        // Auto start of viewpager
        final Handler handler1 = new Handler();
        final Runnable Update1 = new Runnable() {
            public void run() {
                if (currentPage1 == NUM_PAGES1) {
                    currentPage = 0;
                }
                mPager1.setCurrentItem(currentPage1++, true);
            }
        };
        Timer swipeTimer1 = new Timer();
        swipeTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(Update1);
            }
        }, 3000, 3000);
    }
*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_nav) {
            toolbar.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(0);
        }
        else if (id == R.id.store_nav) {
            Intent i = new Intent(MainActivity.this, ProductsActivity.class);
            startActivity(i);
        }
      /*  else if (id == R.id.profile_nav) {
            Intent i = new Intent(MainActivity.this,BusinessProfileActivity.class);
            startActivity(i);
        }*/
        else if (id == R.id.trade_management_nav) {
            Intent i = new Intent(MainActivity.this, TradeManagementActivity.class);
            startActivity(i);
        }
        else if(id == R.id.contracts_nav){
            Intent i = new Intent(MainActivity.this, BidContractsActivity.class);
            i.putExtra("Processed","0");
            startActivity(i);
        }
        else if (id == R.id.storemanagement_nav) {
            Intent i = new Intent(MainActivity.this, StoreMyProductsActivity.class);
            i.putExtra("Customizations","0");
            startActivity(i);
        }else if(id == R.id.cluster_dashboard_nav){
            Intent i = new Intent(MainActivity.this, ClusterDashboardActivity.class);
            startActivity(i);
        }
        else if (id == R.id.service_management_nav) {
            Intent i = new Intent(MainActivity.this, ServiceManagementActivity.class);
            startActivity(i);
        }
        else if (id == R.id.logistics_nav) {
            Intent i = new Intent(MainActivity.this, LogisticsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.cart_nav) {
            Intent i = new Intent(MainActivity.this, CartActivity.class);
            startActivity(i);
        }
        else if (id == R.id.wishlist_nav) {
            Toast.makeText(this, "WishList", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.contactus_nav) {
            Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(i);
        }/*else if (id == R.id.career_nav) {
            Intent i = new Intent(MainActivity.this,CareerOppurtunitiesActivity.class);
            startActivity(i);
        }*/else if (id == R.id.offers_nav) {
            Intent i = new Intent(MainActivity.this, OffersActivity.class);
            startActivity(i);
        }else if(id == R.id.aboutus_nav){
            Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(i);
        }
    /*    else if (id == R.id.terms_conditions_nav) {
            Toast.makeText(MainActivity.this, "terms and conditions", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.privacy_policy_nav) {
            Toast.makeText(this, "privacy and policy", Toast.LENGTH_SHORT).show();

        }*/
        else if(id == R.id.settings_nav){
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }/*else if(id == R.id.vend_management_nav){

        }
        else if(id == R.id.vend_payment_nav){

        }
        else if(id == R.id.shipping_nav){
            Toast.makeText(this, "Shipping", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.notification_nav){
            Intent i = new Intent(MainActivity.this,NotificationActivity.class);
            startActivity(i);
        }*/else if(id == R.id.signin_nav){
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
        }
  /*      else if (id == R.id.signout_nav) {
            preferenceUtils.saveString(PreferenceUtils.UserName,"");
            Intent i = new Intent(MainActivity.this,MainActivity.class);
            startActivity(i);
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getProducts() {
        shimmer_grid_container.startShimmerAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        if(user_role.equalsIgnoreCase("general")){
            callRetrofit = service.getGeneralHomeProducts();
        }else if(user_role.equalsIgnoreCase("poultry")){
            callRetrofit = service.getHomeProducts();
        }else{
            callRetrofit = service.getGeneralHomeProducts();
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
                                            JSONArray jsonArray = root.getJSONArray("getallproduct");
                                            getHomeProducts = new ArrayList<GetHomeProducts>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String pr_bussid = jsonObject.getString("pr_bussid");
                                                String product_master_category = jsonObject.getString("product_master_category");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_quality = jsonObject.getString("pr_quality");
                                                String pr_eggtype = jsonObject.getString("pr_eggtype");
                                                String pr_image= jsonObject.getString("pr_image");
                                                String pr_descri = jsonObject.getString("pr_descri");
                                                String pr_catid = jsonObject.getString("pr_catid");
                                                String cust_cat = jsonObject.getString("cust_cat");
                                                String pr_price = jsonObject.getString("pr_price");
                                                String pr_summary = jsonObject.getString("pr_summary");
                                                String pr_currency = jsonObject.getString("pr_currency");
                                                String pr_discount = jsonObject.getString("pr_discount");
                                                String pr_stocks = jsonObject.getString("pr_stocks");
                                                String pr_min = jsonObject.getString("pr_min");
                                                String pr_status = jsonObject.getString("pr_status");
                                                String service_charge = jsonObject.getString("service_charge");
                                                String pr_enterdate = jsonObject.getString("pr_enterdate");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String countryorogin = jsonObject.getString("countryorogin");
                                                String job_location = jsonObject.getString("job_location");
                                                String country = jsonObject.getString("country");
                                                String state = jsonObject.getString("state");
                                                String imagepath = jsonObject.getString("imagepath");
                                                String weight_unit = jsonObject.getString("weight_unit");
                                                String country_name = jsonObject.getString("country_name");
                                                String city_name = jsonObject.getString("city_name");
                                                String prod_image = imagepath + pr_image;

                                                preferenceUtils.saveString(PreferenceUtils.PROD_ID,id);
                                                preferenceUtils.saveString(PreferenceUtils.IMAGE,prod_image);
                                                GetHomeProducts getHomeProductsModel = new GetHomeProducts();
                                                getHomeProductsModel.setId(id);
                                                getHomeProductsModel.setPr_userid(pr_userid);
                                                getHomeProductsModel.setPr_bussid(pr_bussid);
                                                getHomeProductsModel.setPr_title(pr_title);
                                                getHomeProductsModel.setJob_location(job_location);
                                                getHomeProductsModel.setCountry(country);
                                                getHomeProductsModel.setState(state);
                                                getHomeProductsModel.setPr_price(pr_price);
                                                getHomeProductsModel.setService_charge(service_charge);
                                                getHomeProductsModel.setPr_discount(pr_discount);
                                                getHomeProductsModel.setPr_currency(pr_currency);
                                                getHomeProductsModel.setPr_weight(pr_weight);
                                                getHomeProductsModel.setImagepath(imagepath);
                                                getHomeProductsModel.setPr_image(prod_image);
                                                getHomeProductsModel.setWeight_unit(weight_unit);
                                                getHomeProductsModel.setCountry_name(country_name);
                                                getHomeProductsModel.setCity_name(city_name);
                                                getHomeProducts.add(getHomeProductsModel);
                                            }
                                            if(getHomeProducts != null) {
                                                gridAdapter = new GridAdapter(getApplicationContext(), getHomeProducts);
                                                gridView.setAdapter(gridAdapter);
                                                gridAdapter.notifyDataSetChanged();
                                            }
                                            helper.stopShimmer(shimmer_grid_container);
                                        }

                                    } catch (JSONException e) {
                                        helper.stopShimmer(shimmer_grid_container);
                                        e.printStackTrace();
                                    }
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
                Toast.makeText(MainActivity.this, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();
                helper.stopShimmer(shimmer_grid_container);
            }
        });
    }
    private void getSliderImages() {
        //  progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);

        Call<JsonObject> callRetrofit = null;

        callRetrofit = service.getSliderImages();
        callRetrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
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
                                        String image_path = root.getString("image_path");
                                        String success = root.getString("success");
                                        if(success.equalsIgnoreCase("1")) {
                                            JSONArray jsonArray = root.getJSONArray("slider_image");
                                            imageSliderModels = new ArrayList();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject Obj = jsonArray.getJSONObject(i);

                                                String id = Obj.getString("id");
                                                String image = Obj.getString("image");
                                                String slider_image = image_path + "/" + image;
                                                String status = Obj.getString("status");

                                                ImageSliderModel imageSliderModel = new ImageSliderModel();
                                                imageSliderModel.setId(id);
                                                imageSliderModel.setImage(slider_image);
                                                imageSliderModel.setStatus(status);
                                                imageSliderModels.add(imageSliderModel);
                                            }
                                        }
                                        if (imageSliderModels != null) {
                                            mPager.setAdapter(new SlidingImageAdapter(MainActivity.this, imageSliderModels));
                                        }
                                        final Handler handler = new Handler();
                                        final Runnable Update = new Runnable() {
                                            public void run() {
                                                if (currentPage == imageSliderModels.size()) {
                                                    currentPage = 0;
                                                }
                                                mPager.setCurrentItem(currentPage++, true);
                                            }
                                        };
                                        Timer swipeTimer = new Timer();
                                        swipeTimer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                handler.post(Update);
                                            }
                                        }, 3000, 3000);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // progressDialog.dismiss();
                           /*     if(Categeries != null) {
                                    gridViewAdapter = new GridViewAdapter(getActivity(),Categeries);
                                    gridView.setAdapter(gridViewAdapter);
                                    gridViewAdapter.notifyDataSetChanged();
                                }*/

                                }

                            }
                        }
                    } else {
//                        Toast.makeText(MainActivity.this, "Service not responding", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();
                // progressDialog.dismiss();
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
}
