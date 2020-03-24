
package com.sustowns.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Adapters.ImagesAdapter;
import com.sustowns.sustownsapp.Adapters.MyServicesAdapter;
import com.sustowns.sustownsapp.Adapters.StoreMyProductsAdapter;
import com.sustowns.sustownsapp.Api.CustomizationsApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.AddProductVendorServices;
import com.sustowns.sustownsapp.Models.GetCurrencyModel;
import com.sustowns.sustownsapp.Models.ImageModel;
import com.sustowns.sustownsapp.Models.MyProductsModel;
import com.sustowns.sustownsapp.Models.TransportGetService;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.sustowns.sustownsapp.Activities.AddProductActivity.myProductsModelList;

public class StoreMyProductsActivity extends AppCompatActivity {
    public static RecyclerView recycler_view_products_store;
    ImageView backarrow, three_dots_icon;
    CircleImageView profile_img,imageview;
    boolean isUpdate;
    StoreMyProductsAdapter storeMyProductsAdapter;
    Button add_product_store, save_product, choosefile_btn;
    RelativeLayout rl_capture, rl_gallery;
    public static List<MyProductsModel> myProductsModelList;
    TextView title_store,not_available_text;
    public static List<ImageModel> imageModelList = new ArrayList<>();
    public String PickedImgPath = null;
    String currency_st, TitleStr = "", shippingId = "",service_id,service_name;
    LinearLayout ll_shipping_type, ll_buyer_network,linearlayout,ll_custom_invoice, ll_customizations, ll_contracts;
    ArrayList<String> currencyCodes;
    public static LinearLayout ll_prod_list;
    Spinner spinner_eggs_types, spinner_quality, spinner_prod_category, spinner_sector, spinner_list_type, spinner_sample_gross_weight;
    Spinner spinner_unit, spinner_price, spinner_sample_pack_type, spinner_shipping;
    EditText title_add_prod, unit_edit, price_edittext, min_order_et, stock_et, discount_et, sample_unit_edit, pincode_et, gross_weight_et;
    String[] price = {"INR"};
    String[] samplepacktype = {"sample pack type", "12 Pack Crate", "20 Pack Crate", "30 Pack Crate", "210 Pack Box"};
    String[] country = {"India", "Indonesia", "Iceland", "Australia", "Algeria", "Malaysia", "Saudi Arabia", "Singapore", "USA", "UK", "Uganda"};
    String[] city = {"Hyderabad", "Mumbai", "Chennai", "Kolkata", "Pune"};
    String[] state = {"Telangana", "AP", "Punjab", "UP", "Kerala", "Delhi"};
    String[] shipping = {"no","yes"};
    String[] shippingEdit = {"yes","no"};
    String[] gross_weight_unit = {"weight unit", "Crate", "Box"};
    String[] receivedOffers = {"Select Recevied Offers", "Yes", "No"},accepted={"Yes","No"},rejected = {"No","Yes"};
    String[] packingSize = {"mm", "cm", "in", "ft"};
    String[] DeliveryType = {"EXW", "FOB", "CIF", "Door to Door", "Door to Port", "Port to Door", "Port to Port"};
    String add_prod_st, unit_st, price_st, min_order_st,stock_st, discount_st,eggsType_st,sample_unit_st, user_id, user_role, prod_id, buss_id, pincode_st;
    PreferenceUtils preferenceUtils;
    String address_st, sample_gross_weight_sp="", gross_weight_st, quantity_st, sample_price_st, receivedOffersStr;
    EditText max_quantity_et, sample_price_edittext, delivery_lead_time;
    ProgressDialog progressDialog;
    Spinner spinner_sample_unit, spinner_pack_type, spinner_received_offers;
    String sample_weight_unit_sp,UnitIdSample, packTypeStr, delivery_time, profileString, currency, country_string, received_offers_st, pr_bussid, productId;
    String eggs_type, quality_st, prodCategory, sector_st,ListingTypeId,listingtype_st, unit_sp_st, price_sp_st, sample_packtype, shipping_st, country_st, state_st, city_st;
    ArrayList<GetCurrencyModel> getCurrencyModels;
    ArrayList<TransportGetService> transportGetServices;
    Button dis_start_date, dis_end_date;
    Spinner spinner_sample_price, spinner_packing_size_general, spinnersample_packingsize_general, spinner_sample_gross_unit, spinner_country_origin;
    RecyclerView spinner_choose_shipping_types, shipping_list_recyclerview, recyclerview_shipping_sizes, images_recyclerView;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    Button my_products_btn, customizations_btn,addservice_btn;
    RadioGroup mapRadioGroup;
    LinearLayout ll_my_Product_contracts, ll_choose_shipping_types, ll_vendor_services, ll_shipping_types_list, ll_add_vendor_areas;
    int position;
    String imagePath, CountryCodeStr,serviceNameStr, serviceIdStr;
    protected static final int CAMERA_CAPTURE = 2;
    protected static final int PICK_IMAGE = 1;
    MyServicesAdapter myServicesAdapter;
    EditText title_general, summary_general, description_general, general_length, general_width, general_height, tax_general, sample_general_min_order,
            sample_general_length, sample_general_width, sample_general_height, sample_gross_unit_weight;
    String packingSizeStr, sampackingSizeStr, sampleGrossStr, countyOriginStr, deliveryTypeStr,CustomizationKey = "";
    int count = 0;
    AlertDialog alertDialog;
    Helper helper;
    String imagepath,product_image,is_primary,prod_image,shippingStr="",UnitId,CategoryId,SectorId,countryId = "",clickedSearch = "",stateId = "",cityId = "",clickAction = "",StartDateStr="",EndDateStr="";
    SwipeRefreshLayout swipeRefreshLayout;
    String egg_type,quality_sp,catid,stocks,date_started,date_end,pr_packtype,sweight, sweight_unit,sgweight,sgweight_unit,spack_type,smaxquan,sprice;
    public static String getEncodedImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encodedImagePatientImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImagePatientImage;
    }
    SparseBooleanArray mSelectedContinents = new SparseBooleanArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_my_products);
        try {
            preferenceUtils = new PreferenceUtils(StoreMyProductsActivity.this);
            user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");
           
            CustomizationKey = getIntent().getStringExtra("Customizations");
            helper = new Helper(this);
            isUpdate = false;
            user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
            user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");
            recycler_view_products_store = (RecyclerView) findViewById(R.id.recycler_view_products_store);
            LinearLayoutManager layoutManager = new LinearLayoutManager(StoreMyProductsActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_view_products_store.setLayoutManager(layoutManager);
            shipping_list_recyclerview = (RecyclerView) findViewById(R.id.shipping_list_recyclerview);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(StoreMyProductsActivity.this, LinearLayoutManager.VERTICAL, false);
            shipping_list_recyclerview.setLayoutManager(layoutManager2);
            ll_customizations = (LinearLayout) findViewById(R.id.ll_customizations);
            three_dots_icon = (ImageView) findViewById(R.id.three_dots_icon);
            ll_choose_shipping_types = (LinearLayout) findViewById(R.id.ll_choose_shipping_types);
            ll_vendor_services = (LinearLayout) findViewById(R.id.ll_vendor_services);
            ll_prod_list = (LinearLayout) findViewById(R.id.ll_prod_list);
            ll_shipping_types_list = (LinearLayout) findViewById(R.id.ll_shipping_types_list);
            ll_shipping_type = findViewById(R.id.ll_shipping_type);
            ll_add_vendor_areas = (LinearLayout) findViewById(R.id.ll_add_vendor_areas);
            ll_shipping_type = (LinearLayout) findViewById(R.id.ll_shipping_type);
            ll_buyer_network = (LinearLayout) findViewById(R.id.ll_buyer_network);
            ll_custom_invoice = (LinearLayout) findViewById(R.id.ll_custom_invoice);
            my_products_btn = (Button) findViewById(R.id.my_products_btn);
            customizations_btn = (Button) findViewById(R.id.customizations_btn);
            my_products_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction = "Store";
                    ll_prod_list.setVisibility(View.VISIBLE);
                    ll_customizations.setVisibility(View.GONE);
                    my_products_btn.setTextColor(getResources().getColor(R.color.white));
                    customizations_btn.setTextColor(getResources().getColor(R.color.black));
                    my_products_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    getMyProductList();
                }
            });
            customizations_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction = "Customizations";
                    three_dots_icon.setVisibility(View.VISIBLE);
                    ll_shipping_types_list.setVisibility(View.VISIBLE);
                    ll_prod_list.setVisibility(View.GONE);
                    ll_shipping_type.setVisibility(View.GONE);
                    ll_buyer_network.setVisibility(View.GONE);
                    ll_custom_invoice.setVisibility(View.GONE);
                    ll_add_vendor_areas.setVisibility(View.GONE);
                    ll_customizations.setVisibility(View.VISIBLE);
                    customizations_btn.setTextColor(getResources().getColor(R.color.white));
                    my_products_btn.setTextColor(getResources().getColor(R.color.black));
                    customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                    my_products_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                    getCustomizationList();
                }
            });
            if (CustomizationKey.equalsIgnoreCase("1")) {
                clickAction = "Customizations";
                three_dots_icon.setVisibility(View.VISIBLE);
                ll_shipping_types_list.setVisibility(View.VISIBLE);
                ll_shipping_type.setVisibility(View.GONE);
                ll_buyer_network.setVisibility(View.GONE);
                ll_custom_invoice.setVisibility(View.GONE);
                ll_add_vendor_areas.setVisibility(View.GONE);
                ll_customizations.setVisibility(View.VISIBLE);
                ll_prod_list.setVisibility(View.GONE);
                customizations_btn.setTextColor(getResources().getColor(R.color.white));
                my_products_btn.setTextColor(getResources().getColor(R.color.black));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                my_products_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getCustomizationList();
            } else {
                clickAction = "Store";
                ll_prod_list.setVisibility(View.VISIBLE);
                ll_customizations.setVisibility(View.GONE);
                my_products_btn.setTextColor(getResources().getColor(R.color.white));
                customizations_btn.setTextColor(getResources().getColor(R.color.black));
                my_products_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getMyProductList();
            }
            not_available_text = (TextView) findViewById(R.id.not_available_text);
            addservice_btn = (Button) findViewById(R.id.addCustomisationservice_btn);
            addservice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(StoreMyProductsActivity.this, AddShippingServices.class);
                    i.putExtra("Update", "0");
                    i.putExtra("Name", "");
                    i.putExtra("TranportType", "");
                    i.putExtra("VehicleType", "");
                    i.putExtra("FromDate", "");
                    i.putExtra("ToDate", "");
                    i.putExtra("Category", "");
                    i.putExtra("CatId", "");
                    i.putExtra("VehicleIdNumber", "");
                    i.putExtra("SubCategory", "");
                    i.putExtra("SubCatId", "");
                    i.putExtra("VehicleId", "");
                    i.putExtra("TransId", "");
                    i.putExtra("MinLoad", "");
                    i.putExtra("MaxLoad", "");
                    i.putExtra("ContainerType", "");
                    i.putExtra("Length", "");
                    i.putExtra("Width", "");
                    i.putExtra("Height", "");
                    i.putExtra("TransportTax", "");
                    i.putExtra("TransportDis", "");
                    i.putExtra("ServiceRadius", "");
                    i.putExtra("ServiceExtendRadius", "");
                    i.putExtra("PointSourceLoc", "");
                    i.putExtra("PointDesLoc", "");
                    i.putExtra("LoadType", "");
                    startActivity(i);
                }
            });
            backarrow = (ImageView) findViewById(R.id.backarrow_store);
            add_product_store = (Button) findViewById(R.id.add_product_store);
            add_product_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_role.equalsIgnoreCase("general")) {
                        Intent i = new Intent(StoreMyProductsActivity.this, GeneralAddProductActivity.class);
                        startActivity(i);
                    } else if (user_role.equalsIgnoreCase("poultry")) {
                        Intent i = new Intent(StoreMyProductsActivity.this, AddProductActivity.class);
                        i.putExtra("productid", "");
                        i.putExtra("Update", "0");
                        i.putExtra("Name", "");
                        i.putExtra("CatId", "");
                        i.putExtra("Quality", "");
                        i.putExtra("EggsType", "");
                        i.putExtra("PackType", "");
                        i.putExtra("Country", "");
                        i.putExtra("State", "");
                        i.putExtra("City", "");
                        i.putExtra("Price", "");
                        i.putExtra("Quantity", "");
                        i.putExtra("Discount", "");
                        i.putExtra("Unit", "");
                        i.putExtra("Stocks", "");
                        i.putExtra("SampleUnit", "");
                        i.putExtra("SampleMaxQuantity", "");
                        i.putExtra("SamplePrice", "");
                        i.putExtra("Days", "");
                        i.putExtra("SampleGrossWeight", "");
                        i.putExtra("AddressStr", "");
                        i.putExtra("StartDate", "");
                        i.putExtra("EndDate", "");
                        i.putExtra("SamplePackType", "");
                        i.putExtra("Shipping", "");
                        i.putExtra("MakeOffer", "");
                        i.putExtra("PrType", "");
                        i.putExtra("ListingType", "");
                        i.putExtra("WeightUnit", "");
                        i.putExtra("SampleWeightUnit", "");
                        i.putExtra("SampleGrossWeightUnit", "");
                        startActivity(i);
                    }
                }
            });
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
            swipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (clickAction.equalsIgnoreCase("Store")) {
                        getMyProductList();
                    } else if (clickAction.equalsIgnoreCase("Customizations")) {
                        getCustomizationList();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(StoreMyProductsActivity.this,MainActivity.class);
                    startActivity(i);
                }
            });
            three_dots_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(StoreMyProductsActivity.this, v);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.customization_list, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.shipping_type) {
                                Intent i = new Intent(StoreMyProductsActivity.this, AddShippingServices.class);
                                startActivity(i);
                           /* ll_shipping_type.setVisibility(View.VISIBLE);
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.GONE);
                            ll_shipping_types_list.setVisibility(View.GONE);
                            ll_add_vendor_areas.setVisibility(View.GONE);*/
                            } else if (id == R.id.shipping_types_list) {
                                ll_shipping_types_list.setVisibility(View.VISIBLE);
                                ll_shipping_type.setVisibility(View.GONE);
                                ll_buyer_network.setVisibility(View.GONE);
                                ll_custom_invoice.setVisibility(View.GONE);
                                ll_add_vendor_areas.setVisibility(View.GONE);
                                getCustomizationList();
                            } else if (id == R.id.add_vendor_area) {
                                ll_add_vendor_areas.setVisibility(View.VISIBLE);
                                ll_shipping_type.setVisibility(View.GONE);
                                ll_buyer_network.setVisibility(View.GONE);
                                ll_custom_invoice.setVisibility(View.GONE);
                                ll_shipping_types_list.setVisibility(View.GONE);
                            } else if (id == R.id.buyer_network) {
                                ll_shipping_type.setVisibility(View.GONE);
                                ll_buyer_network.setVisibility(View.VISIBLE);
                                ll_custom_invoice.setVisibility(View.GONE);
                                ll_shipping_types_list.setVisibility(View.GONE);
                                ll_add_vendor_areas.setVisibility(View.GONE);
                            } else if (id == R.id.custom_invoice) {
                                ll_shipping_type.setVisibility(View.GONE);
                                ll_buyer_network.setVisibility(View.GONE);
                                ll_custom_invoice.setVisibility(View.VISIBLE);
                                ll_shipping_types_list.setVisibility(View.GONE);
                                ll_add_vendor_areas.setVisibility(View.GONE);
                            }
                            return true;
                        }
                    });
                    popup.show(); //showing popup menu
                }
            });
          //  ll_prod_list.setVisibility(View.VISIBLE);
            title_store = (TextView) findViewById(R.id.title_store);
           // profile_img = (CircleImageView) findViewById(R.id.profile_img);
            // general
            imageview = (CircleImageView) findViewById(R.id.imageview);
            mapRadioGroup = (RadioGroup) findViewById(R.id.mapRadioGroup);
            mapRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radius_radiobtn:
                            Intent i = new Intent(StoreMyProductsActivity.this, MapsActivity.class);
                            i.putExtra("activity", "store");
                            i.putExtra("type", "radius");
                            startActivity(i);
                            break;
                        case R.id.p2p_radiobtn:
                            Intent intent = new Intent(StoreMyProductsActivity.this, MapsActivity.class);
                            intent.putExtra("activity", "store");
                            intent.putExtra("type", "p2p");
                            startActivity(intent);
                            break;
                    }
                }
            });
            //getMyProductList();
            // getVendorServicesList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       finish();
    }
    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                StartDateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                dis_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void DateDialog1() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                EndDateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                dis_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(StoreMyProductsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    // get list of products
    public void getMyProductList() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getMyProducts(user_id);// user_id : 453

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));
                        System.out.println("----------------------------------------------------");

                        if(response.body().toString() != null) {
                            if(response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Reg", "Response  >>" + searchResponse.toString());
                                if(searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONArray jsonArray = root.getJSONArray("getmyproduct");
                                            myProductsModelList = new ArrayList<MyProductsModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                productId = jsonObject.getString("id");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                pr_bussid = jsonObject.getString("pr_bussid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String pr_price = jsonObject.getString("pr_price");
                                                String pr_min = jsonObject.getString("pr_min");
                                                String pr_discount = jsonObject.getString("pr_discount");
                                                String weight_unit = jsonObject.getString("weight_unit");
                                                imagepath = jsonObject.getString("imagepath");
                                                JSONArray pr_imageArray = jsonObject.getJSONArray("pr_image");
                                                imageModelList = new ArrayList<>();
                                                for (int i1 = 0; i1 < pr_imageArray.length(); i1++){
                                                    JSONObject Object = pr_imageArray.getJSONObject(i1);
                                                    String product_id = Object.getString("product_id");
                                                    product_image = Object.getString("product_image");
                                                    is_primary = Object.getString("is_primary");
                                                    //  prod_image = imagepath + product_image;
                                                    if(is_primary.equalsIgnoreCase("y")){
                                                        prod_image = imagepath + product_image;
                                                    }/*else {
                                                        ImageModelEdit imageModelEdit = new ImageModelEdit();
                                                        imageModelEdit.setProductid(product_id);
                                                        imageModelEdit.setProductImage(imagepath + product_image);
                                                        imageModelEdit.setIsPrimary(is_primary);
                                                        imageModelList.add(imageModelEdit);
                                                    }*/
                                                    ImageModel imageModel = new ImageModel(imagepath + product_image, is_primary);
                                                    imageModelList.add(imageModel);
                                                }
                                                String pr_type = jsonObject.getString("pr_type");
                                                String makeoffer = jsonObject.getString("makeoffer");
                                                egg_type = jsonObject.getString("egg_type");
                                                quality_sp = jsonObject.getString("quality");
                                                catid = jsonObject.getString("catid");
                                                stocks = jsonObject.getString("stocks");
                                                date_started = jsonObject.getString("date_started");
                                                date_end = jsonObject.getString("date_end");
                                                pr_packtype = jsonObject.getString("pr_packtype");
                                                sweight = jsonObject.getString("sweight");
                                                sweight_unit = jsonObject.getString("sweight_unit");
                                                sgweight = jsonObject.getString("sgweight");
                                                sgweight_unit = jsonObject.getString("sgweight_unit");
                                                spack_type = jsonObject.getString("spack_type");
                                                smaxquan = jsonObject.getString("smaxquan");
                                                sprice = jsonObject.getString("sprice");
                                                String status = jsonObject.getString("status");
                                                String addressStr = jsonObject.getString("address");
                                                String listing_type = jsonObject.getString("listing_type");
                                                String days = jsonObject.getString("days");
                                                shippingStr = jsonObject.getString("shipping");
                                                String country = jsonObject.getString("country");
                                                String state = jsonObject.getString("state");
                                                String city = jsonObject.getString("city");
                                                String zipcode = jsonObject.getString("zipcode");
                                                JSONArray travelsArray = jsonObject.getJSONArray("travels");
                                                for (int j = 0; j < travelsArray.length(); j++){
                                                    JSONObject Object = travelsArray.getJSONObject(j);
                                                    String id = Object.getString("id");
                                                    String user_id = Object.getString("user_id");
                                                    String product_id = Object.getString("product_id");
                                                    service_id = Object.getString("service_id");
                                                    service_name = Object.getString("service_name");
                                                }
                                                preferenceUtils.saveString(PreferenceUtils.STORE_PRO_ID, productId);
                                                preferenceUtils.saveString(PreferenceUtils.USER_ID, user_id);
                                                preferenceUtils.saveString(PreferenceUtils.Buss_ID, pr_bussid);
                                                preferenceUtils.saveString(PreferenceUtils.STATUS, status);

                                                MyProductsModel myProductsModel = new MyProductsModel();
                                                myProductsModel.setId(productId);
                                                myProductsModel.setPr_userid(pr_userid);
                                                myProductsModel.setPr_bussid(pr_bussid);
                                                myProductsModel.setPr_title(pr_title);
                                                myProductsModel.setPr_sku(pr_sku);
                                                myProductsModel.setPr_weight(pr_weight);
                                                myProductsModel.setPr_price(pr_price);
                                                myProductsModel.setPr_min(pr_min);
                                                myProductsModel.setPr_discount(pr_discount);
                                                myProductsModel.setWeight_unit(weight_unit);
                                                myProductsModel.setPr_type(pr_type);
                                                myProductsModel.setMakeoffer(makeoffer);
                                                myProductsModel.setProd_image(prod_image);
                                                myProductsModel.setIs_primary(is_primary);
                                                myProductsModel.setStatus(status);
                                                myProductsModel.setAddress(addressStr);
                                                myProductsModel.setListing_type(listing_type);
                                                myProductsModel.setDays(days);
                                                myProductsModel.setShippingStr(shippingStr);
                                                myProductsModel.setEgg_type(egg_type);
                                                myProductsModel.setQuality(quality_sp);
                                                myProductsModel.setCatid(catid);
                                                myProductsModel.setStocks(stocks);
                                                myProductsModel.setDate_started(date_started);
                                                myProductsModel.setDate_end(date_end);
                                                myProductsModel.setPr_packtype(pr_packtype);
                                                myProductsModel.setSweight(sweight);
                                                myProductsModel.setSweight_unit(sweight_unit);
                                                myProductsModel.setSgweight(sgweight);
                                                myProductsModel.setSgweight_unit(sgweight_unit);
                                                myProductsModel.setSpack_type(spack_type);
                                                myProductsModel.setSmaxquan(smaxquan);
                                                myProductsModel.setSprice(sprice);
                                                myProductsModel.setCountry(country);
                                                myProductsModel.setState(state);
                                                myProductsModel.setCity(city);
                                                myProductsModel.setZipcode(zipcode);
                                                myProductsModel.setImageModelList(imageModelList);
                                                myProductsModelList.add(myProductsModel);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(myProductsModelList.size() > 0) {
                                        if (myProductsModelList != null || !myProductsModelList.isEmpty()) {
                                            not_available_text.setVisibility(View.GONE);
                                            recycler_view_products_store.setVisibility(View.VISIBLE);
                                            storeMyProductsAdapter = new StoreMyProductsAdapter(StoreMyProductsActivity.this, myProductsModelList);
                                            recycler_view_products_store.setAdapter(storeMyProductsAdapter);
                                            storeMyProductsAdapter.notifyDataSetChanged();
                                            progressDialog.dismiss();
                                        }else {
                                            not_available_text.setVisibility(View.VISIBLE);
                                            recycler_view_products_store.setVisibility(View.GONE);
                                            progressDialog.dismiss();
                                            // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        not_available_text.setVisibility(View.VISIBLE);
                                        recycler_view_products_store.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                        // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    not_available_text.setVisibility(View.VISIBLE);
                    recycler_view_products_store.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                Toast.makeText(StoreMyProductsActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void getCurrencyList() {
        // progresDailog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCurrencyCodes();
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
                    //  progressDialog.dismiss();
                    if (response.body().toString() != null) {
                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());
                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String success = root.getString("success");
                                    currencyCodes = new ArrayList<String>();
                                    if (success.equalsIgnoreCase("1")) {
                                        JSONArray msg = root.getJSONArray("currency");
                                        for (int i = 0; i < msg.length(); i++) {
                                            JSONObject Obj = msg.getJSONObject(i);

                                            String CurrencyCode = Obj.getString("CurrencyCode");
                                            currencyCodes.add(CurrencyCode);
                                            //   progressDialog.dismiss();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                spinner_sample_price = (Spinner) findViewById(R.id.spinner_sample_price);
                                ArrayAdapter<String> adapter;
                                spinner_price = (Spinner) findViewById(R.id.spinner_price);
                                if (currencyCodes != null) {
                                    adapter = new ArrayAdapter<String>(StoreMyProductsActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_price.setAdapter(adapter);
                                    //  progressDialog.dismiss();
                                    spinner_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            currency_st = spinner_price.getSelectedItem().toString();
                                            preferenceUtils.saveString(PreferenceUtils.CURRENCY_CODE, currency_st);
                                            //categories = getCategoriesListModels.get(pos).getId();
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                } else {
                                }
                                spinner_sample_price = (Spinner) findViewById(R.id.spinner_sample_price);
                                ArrayAdapter<String> adapter1;
                                if (currencyCodes != null) {
                                    adapter1 = new ArrayAdapter<String>(StoreMyProductsActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_sample_price.setAdapter(adapter1);
                                    //  progressDialog.dismiss();
                                    spinner_sample_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            currency_st = spinner_sample_price.getSelectedItem().toString();
                                            preferenceUtils.saveString(PreferenceUtils.CURRENCY_CODE, currency_st);
                                            //categories = getCategoriesListModels.get(pos).getId();
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    });
                                } else {
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                // progressDialog.dismiss();
//                Toast.makeText(ProductCategories.this,"Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCustomizationList() {
        progressdialog();
        //OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CustomizationsApi service = retrofit.create(CustomizationsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCustmizationTypesList(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
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
                                            progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("getmyservice");
                                            transportGetServices = new ArrayList<TransportGetService>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String category = jsonObject.getString("category");
                                                String categoryid = jsonObject.getString("categoryid");
                                                String subcategory = jsonObject.getString("subcategory");
                                                String transport_type = jsonObject.getString("transport_type");
                                                String vehicle_type = jsonObject.getString("vehicle_type");
                                                String vehicle_id =jsonObject.getString("vehicle_id");
                                                String fromdate = jsonObject.getString("fromdate");
                                                String todate = jsonObject.getString("todate");
                                                String vehicleid = jsonObject.getString("vehicleid");
                                                String transid = jsonObject.getString("transid");
                                                String minimumload = jsonObject.getString("minimumload");
                                                String maximumload = jsonObject.getString("maximumload");
                                                String containertype = jsonObject.getString("containertype");
                                                String vehicle_length = jsonObject.getString("vehicle_length");
                                                String vehicle_width = jsonObject.getString("vehicle_width");
                                                String vehicle_height = jsonObject.getString("vehicle_height");
                                                String transport_tax = jsonObject.getString("transport_tax");
                                                String transport_dis = jsonObject.getString("transport_dis");
                                                String pr_subcatid = jsonObject.getString("pr_subcatid");
                                                String service_area_radius = jsonObject.getString("service_area_radius");
                                                String radi_exp = jsonObject.getString("radi_exp");
                                                String point_source_locaiton = jsonObject.getString("point_source_locaiton");
                                                String point_des_location = jsonObject.getString("point_des_location");
                                                String load_type = jsonObject.getString("load_type");
                                                String pr_status = jsonObject.getString("pr_status");

                                                TransportGetService transportGetService = new TransportGetService();
                                                transportGetService.setId(id);
                                                transportGetService.setPr_userid(pr_userid);
                                                transportGetService.setPr_title(pr_title);
                                                transportGetService.setTransport_type(transport_type);
                                                transportGetService.setVehicle_type(vehicle_type);
                                                transportGetService.setVehicle_id(vehicle_id);
                                                transportGetService.setLoad_type(load_type);
                                                transportGetService.setCategory(category);
                                                transportGetService.setFromdate(fromdate);
                                                transportGetService.setTodate(todate);
                                                transportGetService.setCategoryid(categoryid);
                                                transportGetService.setSubcategory(subcategory);
                                                transportGetService.setVehicleid(vehicleid);
                                                transportGetService.setTransid(transid);
                                                transportGetService.setMinimumload(minimumload);
                                                transportGetService.setMaximumload(maximumload);
                                                transportGetService.setContainertype(containertype);
                                                transportGetService.setVehicle_length(vehicle_length);
                                                transportGetService.setVehicle_width(vehicle_width);
                                                transportGetService.setVehicle_height(vehicle_height);
                                                transportGetService.setTransport_tax(transport_tax);
                                                transportGetService.setTransport_dis(transport_dis);
                                                transportGetService.setPr_subcatid(pr_subcatid);
                                                transportGetService.setService_area_radius(service_area_radius);
                                                transportGetService.setRadi_exp(radi_exp);
                                                transportGetService.setPoint_source_locaiton(point_source_locaiton);
                                                transportGetService.setPoint_des_location(point_des_location);
                                                transportGetServices.add(transportGetService);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    if (transportGetServices != null) {
                                        progressDialog.dismiss();
                                        myServicesAdapter = new MyServicesAdapter(StoreMyProductsActivity.this, transportGetServices,"1");
                                        shipping_list_recyclerview.setAdapter(myServicesAdapter);
                                        myServicesAdapter.notifyDataSetChanged();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(StoreMyProductsActivity.this, "No Services", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                Toast.makeText(StoreMyProductsActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
