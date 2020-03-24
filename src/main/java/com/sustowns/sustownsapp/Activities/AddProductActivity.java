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
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Adapters.ImagesAdapter;
import com.sustowns.sustownsapp.Adapters.MyServicesAdapter;
import com.sustowns.sustownsapp.Adapters.StoreMyProductsAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.AddProductVendorServices;
import com.sustowns.sustownsapp.Models.GetCurrencyModel;
import com.sustowns.sustownsapp.Models.ImageModel;
import com.sustowns.sustownsapp.Models.MyProductsModel;
import com.sustowns.sustownsapp.Models.TransportGetService;
import com.sustowns.sustownsapp.R;
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

public class AddProductActivity extends AppCompatActivity {
    public static String Product_Address_Map = "";
    ImageView backarrow;
    CircleImageView profile_img,imageview;
    boolean isUpdate;
    Button save_product, choosefile_btn,addservice_btn;
    RelativeLayout rl_capture, rl_gallery;
    public static List<MyProductsModel> myProductsModelList;
    TextView title_store,not_available_text;
    public static List<ImageModel> imageModelList = new ArrayList<>();
    String sample_price_str, TitleStr = "", shippingId = "",service_id,service_name;
    LinearLayout ll_shipping_type, ll_buyer_network,linearlayout,ll_custom_invoice, ll_customizations, ll_my_products;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> statesList = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
    public static LinearLayout ll_add_products_store, ll_prod_list, ll_addproduct_general;
    Spinner spinner_eggs_types, spinner_quality, spinner_prod_category, spinner_sector, spinner_list_type, spinner_sample_gross_weight;
    Spinner spinner_unit, spinner_price, spinner_sample_pack_type, spinner_shipping;
    EditText title_add_prod, unit_edit, price_edittext, min_order_et, stock_et, discount_et, sample_unit_edit, pincode_et, gross_weight_et;
    String[] eggsType = {"Types Of Eggs","Regular/Layer Eggs", "Organic Eggs", "Duck Eggs", "Quail Eggs"};
    String[] quality = {"select quality", "AA", "A", "B"};
    String[] prod_category = {"Category","Egg", "Poultry"},EggCat = {"Egg","Poultry"},PoultryCat = {"Poultry","Egg"};
    String[] sector = {"Sector","B2B(Business to Business)", "Buyer network"},business = {"B2B(Business to Business)", "Buyer network"},buyerSp = {"Buyer network","B2B(Business to Business)"};
    String[] listingtype = {"Listing Type","Product", "Service"},productSp = {"Product","Service"},serviceSp = {"Service","Product"};
    String[] unit = {"Units","Crate", "Box"},Crate = {"Crate","Box"},Box = {"Box","Crate"};
    String[] price = {"INR"};
    String[] samplepacktype = {"12 Pack Crate", "20 Pack Crate", "30 Pack Crate", "210 Pack Box"};
    String[] country = {"India", "Indonesia", "Iceland", "Australia", "Algeria", "Malaysia", "Saudi Arabia", "Singapore", "USA", "UK", "Uganda"};
    String[] city = {"Hyderabad", "Mumbai", "Chennai", "Kolkata", "Pune"};
    String[] state = {"Telangana", "AP", "Punjab", "UP", "Kerala", "Delhi"};
    String[] shipping = {"no","yes"};
    String[] shippingEdit = {"yes","no"};
    String[] Country = {"India"};
    String[] gross_weight_unit = {"weight unit", "Crate", "Box"};
    String[] receivedOffers = {"Select Recevied Offers", "Yes", "No"},accepted={"Yes","No"},rejected = {"No","Yes"};
    String[] packingSize = {"mm", "cm", "in", "ft"};
    String[] DeliveryType = {"EXW", "FOB", "CIF", "Door to Door", "Door to Port", "Port to Door", "Port to Port"};
    String add_prod_st, unit_st, price_st, min_order_st,stock_st, discount_st,eggsType_st,sample_unit_st, user_id, user_role, prod_id, buss_id, pincode_st;
    PreferenceUtils preferenceUtils;
    String imagePath, sample_gross_weight_sp="", gross_weight_st, quantity_st, sample_price_st, receivedOffersStr;
    EditText max_quantity_et, sample_price_edittext, delivery_lead_time,sample_gross_weight_et;
    ProgressDialog progressDialog;
    Spinner spinner_sample_unit, spinner_pack_type, spinner_received_offers;
    String sample_weight_unit_sp,UnitIdSample="", packTypeStr, delivery_time, profileString, currency, country_string, received_offers_st, pr_bussid, productId;
    String eggs_type, quality_st, prodCategory, sector_st,ListingTypeId,listingtype_st, unit_sp_st, price_sp_st, sample_packtype="", shipping_st, country_st, state_st, city_st;
    Spinner spinner_delivery_type,spinner_country_reg;
    Button dis_start_date, dis_end_date;
    TextView dis_start_date_text, dis_end_date_text,spinner_shipping_type,sp_country,sp_state, sp_city,address_txt_map;
    Spinner spinner_sample_price, spinner_packing_size_general, spinnersample_packingsize_general, spinner_sample_gross_unit, spinner_country_origin;
    RecyclerView spinner_choose_shipping_types, shipping_list_recyclerview, recyclerview_shipping_sizes, images_recyclerView;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    RadioGroup mapRadioGroup;
    TextView textview_address,address_get_map;
    LinearLayout ll_my_Product_contracts, ll_choose_shipping_types, ll_vendor_services, ll_shipping_types_list, ll_add_vendor_areas;
    int position,CatIdInt;
    String Pr_Id="",Pr_Catid="",Pr_Name="",Pr_Quality="",Pr_EggsType="",Pr_packType="",Pr_Country="",Pr_State="",Pr_City="",Pr_Price="",Pr_Quantity="",Pr_Discount="",Zipcode,Pr_Image="",
            Pr_unit="",Pr_Stocks="",Pr_SUnit="",Pr_SQuantity="",Pr_SPrice="",Pr_Days="",Pr_SGWeight="",Pr_Address="",Pr_StartDate="",Pr_EndDate="",Pr_SPackType="", Pr_Shipping="",Pr_MakeOffer="",Pr_PrType="",Pr_ListingType="",Pr_WUnit="",Pr_SWUnit="",Pr_SGWUnit="";
    protected static final int CAMERA_CAPTURE = 2;
    protected static final int PICK_IMAGE = 1;
    EditText title_general, summary_general, description_general, general_length, general_width, general_height, tax_general, sample_general_min_order,
     sample_general_length, sample_general_width, sample_general_height, sample_gross_unit_weight;
    String packingSizeStr, sampackingSizeStr, sampleGrossStr, countyOriginStr, deliveryTypeStr,CustomizationKey = "";
    CheckBox checkbox_international, checkbox_domestic;
    LinearLayout ll_international_freight, ll_domestiic_freight,ll_eggs_type,ll_quality,ll_packtype,ll_samplepacktype;
    RelativeLayout google_maps_img;
    ImageButton confirm_add_icon;
    int count = 0;
    List<ImageModel> imagesList = new ArrayList<>();
    List<String> fileList = new ArrayList<>();
    List<String> shippingList = new ArrayList<>();
    AlertDialog alertDialog;
    List<Integer> shippingSelectedPosition = new ArrayList<>();
    List<String> shippingSelectedList = new ArrayList<>();
    Helper helper;
    String imagepath,UpdateStr="",Discount="",AddressMap="",shippingStr="",UnitId,CategoryId,SectorId,countryId = "IN",clickedSearch = "",stateId = "",cityId = "",clickAction = "",StartDateStr="",EndDateStr="";
    int textlength = 0;
    Intent intent;
    ArrayList<String> selectedCountryList = new ArrayList<String>();
    ArrayList<String> selectedCountryIdList = new ArrayList<String>();
    ArrayList<String> selectedItems = new ArrayList<String>();
    TextView text_eggs_type,text_quality,text_pack_type,text_samplepacktype;
    public String PickedImgPath = null;
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
        setContentView(R.layout.activity_add_product);
        try {
         /*   AddressMap = getIntent().getStringExtra("AddressStr");
            textview_address = (TextView) findViewById(R.id.textview_address);
            if(AddressMap.equalsIgnoreCase("")){
                textview_address.setText("choose from map");   
            }else {
                textview_address.setText(AddressMap);
            }*/
            initializeValues();
            initializeUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(AddProductActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");
        Pr_Id = getIntent().getStringExtra("productid");
        UpdateStr = getIntent().getStringExtra("Update");
        Pr_Name = getIntent().getStringExtra("Name");
        Pr_Catid = getIntent().getStringExtra("CatId");
        Pr_Quality = getIntent().getStringExtra("Quality");
        Pr_EggsType = getIntent().getStringExtra("EggsType");
        Pr_packType = getIntent().getStringExtra("PackType");
        Pr_Country = getIntent().getStringExtra("Country");
        Pr_State = getIntent().getStringExtra("State");
        Pr_City = getIntent().getStringExtra("City");
        Pr_Price = getIntent().getStringExtra("Price");
        Pr_Quantity = getIntent().getStringExtra("Quantity");
        Pr_Discount = getIntent().getStringExtra("Discount");
        Pr_unit = getIntent().getStringExtra("Unit");
        Pr_Stocks = getIntent().getStringExtra("Stocks");
        Pr_SUnit = getIntent().getStringExtra("SampleUnit");
        Pr_SQuantity = getIntent().getStringExtra("SampleMaxQuantity");
        Pr_SPrice = getIntent().getStringExtra("SamplePrice");
        Pr_Days = getIntent().getStringExtra("Days");
        Pr_SGWeight = getIntent().getStringExtra("SampleGrossWeight");
        Pr_StartDate  = getIntent().getStringExtra("StartDate");
        Pr_EndDate = getIntent().getStringExtra("EndDate");
        Pr_SPackType = getIntent().getStringExtra("SamplePackType");
        Pr_Shipping = getIntent().getStringExtra("Shipping");
        Pr_MakeOffer = getIntent().getStringExtra("MakeOffer");
        Pr_PrType = getIntent().getStringExtra("PrType");
        Pr_ListingType = getIntent().getStringExtra("ListingType");
        Pr_WUnit = getIntent().getStringExtra("WeightUnit");
        Pr_SWUnit = getIntent().getStringExtra("SampleWeightUnit");
        Pr_SGWUnit = getIntent().getStringExtra("SampleGrossWeightUnit");
        Pr_Image = getIntent().getStringExtra("Image");
        Zipcode = getIntent().getStringExtra("Zipcode");
    }
    private void initializeUI() {
        isUpdate = false;
        AddressMap = getIntent().getStringExtra("AddressStr");
        textview_address = (TextView) findViewById(R.id.textview_address);
        if(AddressMap.equalsIgnoreCase("")){
            textview_address.setText("choose from map");
        }else {
            textview_address.setText(AddressMap);
        }
        address_txt_map = (TextView)findViewById(R.id.address_get_map);
        google_maps_img = findViewById(R.id.google_maps_img);
        images_recyclerView = findViewById(R.id.images_recyclerView);
        images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        images_recyclerView.setLayoutManager(linearLayoutManager);
        images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        confirm_add_icon = findViewById(R.id.confirm_add_icon);
        confirm_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 1;
                final Dialog customdialog = new Dialog(AddProductActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);
                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        capture();
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchLicenceGallery(AddProductActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        ll_choose_shipping_types = (LinearLayout) findViewById(R.id.ll_choose_shipping_types);
        spinner_eggs_types = (Spinner) findViewById(R.id.spinner_eggs_types);
        // ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, eggsType);
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(AddProductActivity.this,android.R.layout.simple_spinner_item,eggsType){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_eggs_types.setAdapter(aa);
        spinner_eggs_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eggs_type = parent.getItemAtPosition(position).toString();
                if(eggs_type.equalsIgnoreCase("Regular/Layer Eggs")){
                    eggs_type = "regular";
                }else{
                    eggs_type = parent.getItemAtPosition(position).toString();
                }
                preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //  spinner quality
        spinner_quality = (Spinner) findViewById(R.id.spinner_quality);
        // ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quality);
        final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(AddProductActivity.this,android.R.layout.simple_spinner_item,quality){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_quality.setAdapter(aa1);
        spinner_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quality_st = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_prod_category = (Spinner) findViewById(R.id.spinner_prod_category);
        //   ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, prod_category);
        final ArrayAdapter<String> aa2 = new ArrayAdapter<String>(AddProductActivity.this,android.R.layout.simple_spinner_item,prod_category){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_prod_category.setAdapter(aa2);
        spinner_prod_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prodCategory = parent.getItemAtPosition(position).toString();
                if(prodCategory.equalsIgnoreCase("Egg")){
                    CategoryId = "146";
                }else if(prodCategory.equalsIgnoreCase("Poultry")){
                    CategoryId = "145";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_sector = (Spinner) findViewById(R.id.spinner_sector);
        ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sector);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_sector.setAdapter(aa3);
        spinner_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sector_st = parent.getItemAtPosition(position).toString();
                if(sector_st.equalsIgnoreCase("B2B(Business to Business)")){
                    SectorId = "1";
                }else if(sector_st.equalsIgnoreCase("Buyer network")){
                    SectorId = "2";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_list_type = (Spinner) findViewById(R.id.spinner_list_type);
        ArrayAdapter aa4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listingtype);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_list_type.setAdapter(aa4);
        spinner_list_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listingtype_st = parent.getItemAtPosition(position).toString();
                if(listingtype_st.equalsIgnoreCase("Product")){
                    ListingTypeId = "0";
                }else if(listingtype_st.equalsIgnoreCase("Service")){
                    ListingTypeId = "1";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_unit = (Spinner) findViewById(R.id.spinner_unit);
        // UnitId = "16";
        ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unit);
        aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_unit.setAdapter(aa5);
        spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit_sp_st = parent.getItemAtPosition(position).toString();
                if(unit_sp_st.equalsIgnoreCase("Crate")){
                    UnitId = "16";
                }else  if(unit_sp_st.equalsIgnoreCase("Box")){
                    UnitId = "17";
                }else{
                    UnitId = "16";
                }
                // preferenceUtils.saveString(PreferenceUtils.UNIT, unit_sp_st);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_price = (Spinner) findViewById(R.id.spinner_price);
        ArrayAdapter aa6 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,price);
        aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_price.setAdapter(aa6);
        spinner_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                price_sp_st = parent.getItemAtPosition(position).toString();
                preferenceUtils.saveString(PreferenceUtils.PRICE,price_sp_st);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //spinner_received_offers
        spinner_received_offers = (Spinner) findViewById(R.id.spinner_received_offers);
        ArrayAdapter received_offer_sp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, receivedOffers);
        received_offer_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_received_offers.setAdapter(received_offer_sp);
        spinner_received_offers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                received_offers_st = parent.getItemAtPosition(position).toString();
                if(received_offers_st.equalsIgnoreCase("Yes")){
                    receivedOffersStr = "1";
                }else  if(received_offers_st.equalsIgnoreCase("No")){
                    receivedOffersStr = "0";
                }
                preferenceUtils.saveString(PreferenceUtils.ReceivedOffersList, received_offers_st);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // sample pack type
        spinner_sample_pack_type = (Spinner) findViewById(R.id.spinner_sample_pack_type);
        ArrayAdapter aa7 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, samplepacktype);
        aa7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sample_pack_type.setAdapter(aa7);
        spinner_sample_pack_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sample_packtype = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_shipping_type = (TextView) findViewById(R.id.spinner_shipping_type);
        spinner_shipping_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Vehicle API
                getVendorServicesList();
            }
        });
        spinner_shipping = (Spinner) findViewById(R.id.spinner_shipping);
        ArrayAdapter aa8 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shipping);
        aa8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_shipping.setAdapter(aa8);
        spinner_shipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shipping_st = parent.getItemAtPosition(position).toString();
                if (shipping_st.equalsIgnoreCase("yes")) {
                    ll_choose_shipping_types.setVisibility(View.VISIBLE);
                } else {
                    ll_choose_shipping_types.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_sample_gross_weight = (Spinner) findViewById(R.id.spinner_sample_gross_weight);
        ArrayAdapter aa_sam_gross_wt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gross_weight_unit);
        aa_sam_gross_wt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAd
        // adapter data on the Spinner
        spinner_sample_gross_weight.setAdapter(aa_sam_gross_wt);
        spinner_sample_gross_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sample_gross_weight_sp = parent.getItemAtPosition(position).toString();
                if(sample_gross_weight_sp.equalsIgnoreCase("Crate")){
                    UnitIdSample = "17";
                }else if(sample_gross_weight_sp.equalsIgnoreCase("Box")){
                    UnitIdSample = "18";
                }else{
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_sample_unit = (Spinner) findViewById(R.id.spinner_sample_unit);
        ArrayAdapter sample_unit_weight = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gross_weight_unit);
        sample_unit_weight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sample_unit.setAdapter(sample_unit_weight);
        spinner_sample_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sample_weight_unit_sp = parent.getItemAtPosition(position).toString();
                if(sample_weight_unit_sp.equalsIgnoreCase("Crate")){
                    UnitIdSample = "17";
                }else  if(sample_weight_unit_sp.equalsIgnoreCase("Box")){
                    UnitIdSample = "18";
                }else{
                    UnitIdSample = "17";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_pack_type = (Spinner) findViewById(R.id.spinner_pack_type);
        ArrayAdapter pack_type = new ArrayAdapter(this, android.R.layout.simple_spinner_item, samplepacktype);
        pack_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAd
        // adapter data on the Spinner
        spinner_pack_type.setAdapter(pack_type);
        spinner_pack_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                packTypeStr = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        google_maps_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressMap = "true";
                Intent mapIntent = new Intent(AddProductActivity.this, MapsActivity.class);
                mapIntent.putExtra("activity", "store");
                mapIntent.putExtra("type", "none");
                startActivity(mapIntent);
            }
        });
        spinner_country_reg = (Spinner) findViewById(R.id.spinner_country_reg);
        ArrayAdapter countryaa = new ArrayAdapter(AddProductActivity.this, android.R.layout.simple_spinner_item, Country);
        countryaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_country_reg.setAdapter(countryaa);
        spinner_country_reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp_state = (TextView) findViewById(R.id.spinner_state);
        sp_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getStatesList();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        sp_city = (TextView) findViewById(R.id.spinner_city);
        sp_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getCityList();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dis_start_date = (Button) findViewById(R.id.dis_start_date);
        dis_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog();
            }
        });
        dis_end_date = (Button) findViewById(R.id.dis_end_date);
        dis_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog1();
            }
        });
        save_product = (Button)findViewById(R.id.save_product_btn);
        save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickActionButton();
            }
        });
        backarrow = (ImageView) findViewById(R.id.backarrow_Add_product);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_add_prod = (EditText) findViewById(R.id.title_add_prod);
        unit_edit = (EditText) findViewById(R.id.unit_edit);
        price_edittext = (EditText) findViewById(R.id.price_edittext);
        min_order_et = (EditText) findViewById(R.id.min_order_et);
        stock_et = (EditText) findViewById(R.id.stock_et);
        discount_et = (EditText) findViewById(R.id.discount_et);
        sample_unit_edit = (EditText) findViewById(R.id.sample_unit_edit);
        pincode_et = (EditText) findViewById(R.id.pincode_et);
        gross_weight_et = (EditText) findViewById(R.id.sample_gross_weight_et);
        max_quantity_et = (EditText) findViewById(R.id.max_quantity_et);
        sample_price_edittext = (EditText) findViewById(R.id.sample_price_edittext);
        delivery_lead_time = (EditText) findViewById(R.id.delivery_lead_time);
        title_store = (TextView) findViewById(R.id.title_store);
        text_eggs_type = (TextView) findViewById(R.id.text_eggs_type);
        text_quality = findViewById(R.id.text_quality);
        text_pack_type = findViewById(R.id.text_pack_type);
        text_samplepacktype = findViewById(R.id.text_samplepacktype);
        ll_eggs_type = findViewById(R.id.ll_eggs_type);
        ll_quality = findViewById(R.id.ll_quality);
        ll_packtype = findViewById(R.id.ll_packtype);
        ll_samplepacktype = findViewById(R.id.ll_samplepacktype);
        if(UpdateStr.equalsIgnoreCase("1")){
            isUpdate = true;
            //String MapText = AddressMap;
            //address_txt_map.setText(MapText);
            TitleStr = "Edit Product";
            title_store.setText(TitleStr);
            ImageModel imageModel = new ImageModel(Pr_Image, "yes");
            imagesList.add(imageModel);
            confirm_add_icon.setVisibility(View.GONE);
            editProduct();
        }
        spinner_sample_price = (Spinner) findViewById(R.id.spinner_sample_price);
        ArrayAdapter sampleprice = new ArrayAdapter(this, android.R.layout.simple_spinner_item, price);
        sampleprice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_sample_price.setAdapter(sampleprice);
        spinner_sample_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sample_price_str = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sample_gross_weight_et = (EditText) findViewById(R.id.sample_gross_weight_et);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        spinner_choose_shipping_types = (RecyclerView) findViewById(R.id.spinner_choose_shipping_types);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(AddProductActivity.this, LinearLayoutManager.VERTICAL, false);
        spinner_choose_shipping_types.setLayoutManager(layoutManager1);
        shipping_list_recyclerview = (RecyclerView) findViewById(R.id.shipping_list_recyclerview);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(AddProductActivity.this, LinearLayoutManager.VERTICAL, false);
        shipping_list_recyclerview.setLayoutManager(layoutManager2);
      /*  recyclerview_shipping_sizes = (RecyclerView) findViewById(R.id.recyclerview_shipping_sizes);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(AddProductActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerview_shipping_sizes.setLayoutManager(layoutManager3);*/
        ll_vendor_services = (LinearLayout) findViewById(R.id.ll_vendor_services);
        ll_add_products_store = (LinearLayout) findViewById(R.id.ll_add_products_store);
        //ll_prod_list = (LinearLayout) findViewById(R.id.ll_prod_list);
        ll_addproduct_general = (LinearLayout) findViewById(R.id.ll_addproduct_general);
        ll_shipping_types_list = (LinearLayout) findViewById(R.id.ll_shipping_types_list);
        ll_add_vendor_areas = (LinearLayout) findViewById(R.id.ll_add_vendor_areas);
      //  profile_img = (CircleImageView) findViewById(R.id.profile_img);
        dis_start_date_text = (TextView) findViewById(R.id.dis_start_date_text);
        dis_end_date_text = (TextView) findViewById(R.id.dis_end_date_text);
        title_general = (EditText) findViewById(R.id.title_general);
        summary_general = (EditText) findViewById(R.id.summary_general);
        description_general = (EditText) findViewById(R.id.description_general);
        general_length = (EditText) findViewById(R.id.general_length);
        general_width = (EditText) findViewById(R.id.general_width);
        general_height = (EditText) findViewById(R.id.general_height);
        tax_general = (EditText) findViewById(R.id.tax_general);
        sample_general_min_order = (EditText) findViewById(R.id.sample_general_min_order);
        sample_general_length = (EditText) findViewById(R.id.sample_general_length);
        sample_general_width = (EditText) findViewById(R.id.sample_general_width);
        sample_general_height = (EditText) findViewById(R.id.sample_general_height);
        sample_gross_unit_weight = (EditText) findViewById(R.id.sample_gross_unit_weight);
        ll_international_freight = (LinearLayout) findViewById(R.id.ll_international_freight);
        ll_domestiic_freight = (LinearLayout) findViewById(R.id.ll_domestiic_freight);
        checkbox_international = (CheckBox) findViewById(R.id.checkbox_international);
        checkbox_domestic = (CheckBox) findViewById(R.id.checkbox_domestic);
        choosefile_btn = (Button) findViewById(R.id.choosefile_btn);
        ll_customizations = (LinearLayout) findViewById(R.id.ll_customizations);
        ll_my_products = (LinearLayout) findViewById(R.id.ll_my_products);
        ll_shipping_type = (LinearLayout) findViewById(R.id.ll_shipping_type);
        ll_buyer_network = (LinearLayout) findViewById(R.id.ll_buyer_network);
        ll_custom_invoice = (LinearLayout) findViewById(R.id.ll_custom_invoice);
        imageview = (CircleImageView) findViewById(R.id.imageview);
        spinner_packing_size_general = (Spinner) findViewById(R.id.spinner_packing_size_general);
        ArrayAdapter size = new ArrayAdapter(this, android.R.layout.simple_spinner_item, packingSize);
        size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_packing_size_general.setAdapter(size);
        spinner_packing_size_general.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                packingSizeStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnersample_packingsize_general = (Spinner) findViewById(R.id.spinnersample_packingsize_general);
        ArrayAdapter samplesize = new ArrayAdapter(this, android.R.layout.simple_spinner_item, packingSize);
        samplesize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersample_packingsize_general.setAdapter(samplesize);
        spinnersample_packingsize_general.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sampackingSizeStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_sample_gross_unit = (Spinner) findViewById(R.id.spinner_sample_gross_unit);
        ArrayAdapter samplegross = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gross_weight_unit);
        samplegross.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_sample_gross_unit.setAdapter(samplegross);
        spinner_sample_gross_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sampleGrossStr = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_country_origin = (Spinner) findViewById(R.id.spinner_country_origin);
        ArrayAdapter country_origin = new ArrayAdapter(AddProductActivity.this, android.R.layout.simple_spinner_item, country);
        country_origin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_country_origin.setAdapter(country_origin);
        spinner_country_origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countyOriginStr = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_delivery_type = (Spinner) findViewById(R.id.spinner_delivery_type);
        ArrayAdapter deliveryType = new ArrayAdapter(this, android.R.layout.simple_spinner_item, DeliveryType);
        deliveryType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_delivery_type.setAdapter(deliveryType);
        spinner_delivery_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deliveryTypeStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
       /* if (user_role.equalsIgnoreCase("general")) {
            ll_add_products_store.setVisibility(View.GONE);
            ll_addproduct_general.setVisibility(View.VISIBLE);
            TitleStr = "Add Product";
            title_store.setText(TitleStr);
            //  ll_prod_list.setVisibility(View.GONE);

        } else if (user_role.equalsIgnoreCase("poultry")) {
            ll_add_products_store.setVisibility(View.VISIBLE);
            ll_addproduct_general.setVisibility(View.GONE);
            //address_txt_map.setText("Choose From Map");
          *//*  address_txt_map.setText("Choose From Map");
            address_txt_map.setTextColor(getResources().getColor(R.color.appcolor));*//*
            // getCurrencyList();
            TitleStr = "Add Product";
            title_store.setText(TitleStr);
            // ll_prod_list.setVisibility(View.GONE);
        }*/
        checkbox_international.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_international_freight.setVisibility(View.VISIBLE);
                } else {
                    ll_international_freight.setVisibility(View.GONE);
                }
            }
        });
        checkbox_domestic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_domestiic_freight.setVisibility(View.VISIBLE);
                } else {
                    ll_domestiic_freight.setVisibility(View.GONE);
                }
            }
        });
        mapRadioGroup = (RadioGroup) findViewById(R.id.mapRadioGroup);
        mapRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radius_radiobtn:
                        Intent i = new Intent(AddProductActivity.this, MapsActivity.class);
                        i.putExtra("activity", "store");
                        i.putExtra("type", "radius");
                        startActivity(i);
                        break;
                    case R.id.p2p_radiobtn:
                        Intent intent = new Intent(AddProductActivity.this, MapsActivity.class);
                        intent.putExtra("activity", "store");
                        intent.putExtra("type", "p2p");
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    private void capture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            //In case you need image's absolute path
                            String path = getRealPathFromURI(AddProductActivity.this, uri);
                        }
                    }
                }
            }
        }
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(getApplicationContext().getFilesDir().getPath(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            PickedImgPath = destination.getAbsolutePath();
            Log.e("Camera Path", destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        profileString = getEncodedImage(bmp);
        ImageModel imageModel = new ImageModel(
                profileString,
                "no");
        imagesList.add(imageModel);
        setUpRecyclerView(PickedImgPath);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(contentUri, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        imagePath = c.getString(columnIndex);
        Bitmap bitmapImage = null;
        try {
            bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentUri);
            profileString = getEncodedImage(bitmapImage);
            ImageModel imageModel = new ImageModel(
                    profileString,
                    "no"
            );
            imagesList.add(imageModel);
            setUpRecyclerView(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Selected Image path: ", imagePath);
        c.close();
        return "";
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
    public void clickActionButton() {
        if (!isUpdate) {
            if (title_add_prod.getText().toString().isEmpty() || min_order_et.getText().toString().isEmpty() ||
                    eggs_type.equalsIgnoreCase("Types Of Eggs") || unit_edit.getText().toString().isEmpty() ||
                    unit_sp_st.equalsIgnoreCase("select unit") || price_edittext.getText().toString().isEmpty() || packTypeStr.equalsIgnoreCase("sample pack type") ||
                    received_offers_st.equalsIgnoreCase("Select Recevied Offers") || stock_et.getText().toString().isEmpty()
                    || prodCategory.equalsIgnoreCase("Category") || sector_st.equalsIgnoreCase("Sector") || listingtype_st.equalsIgnoreCase("Listing Type") || unit_sp_st.equalsIgnoreCase("Units")) {
                Toast.makeText(AddProductActivity.this, "Please fill Mandatory(*) fields", Toast.LENGTH_SHORT).show();
            } else if (imagesList.size() == 0 || ImagesAdapter.selectedPrimary.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Required! Please Select Atleast One Image", Toast.LENGTH_SHORT).show();
            } else if (countryId.equalsIgnoreCase("") || stateId.equalsIgnoreCase("") || cityId.equalsIgnoreCase("") || countryId.isEmpty() || stateId.isEmpty() || cityId.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Please fill mandatory(*) fields", Toast.LENGTH_SHORT).show();
            } else if (Product_Address_Map.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
            } else if (shipping_st.equalsIgnoreCase("yes")) {
                if (shippingList.size() > 0) {
                    try {
                        setAddProductJsonObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddProductActivity.this, "Shipping Services not available", Toast.LENGTH_SHORT).show();
                }
            }else if (!discount_et.getText().toString().isEmpty()) {
                if (StartDateStr.isEmpty() || EndDateStr.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Please Choose Discount start and End Date", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        setAddProductJsonObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    setAddProductJsonObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                if(address_txt_map.getText().toString().equalsIgnoreCase("Choose from Map")){
                    Toast.makeText(AddProductActivity.this, "Please select Address", Toast.LENGTH_SHORT).show();
                }else {
                    setEditProductJsonObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void editProduct() {
        try {
            if (!Pr_EggsType.equalsIgnoreCase("Types Of Eggs")) {
                ll_eggs_type.setVisibility(View.VISIBLE);
                spinner_eggs_types.setVisibility(View.GONE);
                text_eggs_type.setText(Pr_EggsType);
                if(Pr_EggsType.equalsIgnoreCase("regular")){
                    eggs_type = "regular";
                }else if(Pr_EggsType.equalsIgnoreCase("Organic Eggs")){
                    eggs_type = "Organic Eggs";
                }else if(Pr_EggsType.equalsIgnoreCase("Quail Eggs")){
                    eggs_type = "Quail Eggs";
                }else if(Pr_EggsType.equalsIgnoreCase("Duck Eggs")){
                    eggs_type = "Duck Eggs";
                }
            }
            if (!Pr_Quality.equalsIgnoreCase("select quality")) {
                ll_quality.setVisibility(View.VISIBLE);
                spinner_quality.setVisibility(View.GONE);
                text_quality.setText(Pr_Quality);
                if(Pr_Quality.equalsIgnoreCase("AA")){
                    quality_st = "AA";
                }else if(Pr_Quality.equalsIgnoreCase("A")){
                    quality_st = "A";
                }else if(Pr_Quality.equalsIgnoreCase("B")){
                    quality_st = "B";
                }
            }
            if (!Pr_packType.equalsIgnoreCase("sample pack type")) {
                ll_packtype.setVisibility(View.VISIBLE);
                spinner_pack_type.setVisibility(View.GONE);
                text_pack_type.setText(Pr_packType);
                if(Pr_packType.equalsIgnoreCase("12 Pack Crate")){
                    packTypeStr = "12 Pack Crate";
                }else if(Pr_packType.equalsIgnoreCase("20 Pack Crate")){
                    packTypeStr = "20 Pack Crate";
                }else if(Pr_packType.equalsIgnoreCase("30 Pack Crate")){
                    packTypeStr = "30 Pack Crate";
                }else{
                    packTypeStr = "210 Pack Crate";
                }
            }
            if (!Pr_SPackType.equalsIgnoreCase("sample pack type")) {
                ll_samplepacktype.setVisibility(View.VISIBLE);
                spinner_sample_pack_type.setVisibility(View.GONE);
                text_samplepacktype.setText(Pr_SPackType);
                if(Pr_SPackType.equalsIgnoreCase("12 Pack Crate")){
                    sample_packtype = "12 Pack Crate";
                }else if(Pr_SPackType.equalsIgnoreCase("20 Pack Crate")){
                    sample_packtype = "20 Pack Crate";
                }else if(Pr_SPackType.equalsIgnoreCase("30 Pack Crate")){
                    sample_packtype = "30 Pack Crate";
                }else{
                    sample_packtype = "210 Pack Crate";
                }
            }
            if (Pr_Shipping.equalsIgnoreCase("yes")) {
                ll_choose_shipping_types.setVisibility(View.VISIBLE);
                ArrayAdapter adapterShip = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shippingEdit);
                adapterShip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_shipping.setAdapter(adapterShip);
                spinner_shipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        shipping_st = parent.getItemAtPosition(position).toString();
                        if (shipping_st.equalsIgnoreCase("yes")) {
                            ll_choose_shipping_types.setVisibility(View.VISIBLE);
                        } else {
                            ll_choose_shipping_types.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_MakeOffer.equalsIgnoreCase("0")) {
                ArrayAdapter received_offer_sp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accepted);
                received_offer_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_received_offers.setAdapter(received_offer_sp);
                receivedOffersStr = "0";
                spinner_received_offers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        received_offers_st = parent.getItemAtPosition(position).toString();
                        if (received_offers_st.equalsIgnoreCase("Yes")) {
                            receivedOffersStr = "1";
                        } else if (received_offers_st.equalsIgnoreCase("No")) {
                            receivedOffersStr = "0";
                        }
                        preferenceUtils.saveString(PreferenceUtils.ReceivedOffersList, received_offers_st);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_MakeOffer.equalsIgnoreCase("1")) {
                ArrayAdapter received_offer_sp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, rejected);
                received_offer_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_received_offers.setAdapter(received_offer_sp);
                receivedOffersStr = "1";
                spinner_received_offers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        received_offers_st = parent.getItemAtPosition(position).toString();
                        if (received_offers_st.equalsIgnoreCase("Yes")) {
                            receivedOffersStr = "1";
                        } else if (received_offers_st.equalsIgnoreCase("No")) {
                            receivedOffersStr = "0";
                        }
                        preferenceUtils.saveString(PreferenceUtils.ReceivedOffersList, received_offers_st);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_Catid.equalsIgnoreCase("146")) {
                ArrayAdapter adapterCat = new ArrayAdapter(this, android.R.layout.simple_spinner_item, EggCat);
                adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_prod_category.setAdapter(adapterCat);
                CategoryId = "146";
                spinner_prod_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        prodCategory = parent.getItemAtPosition(position).toString();
                        if (prodCategory.equalsIgnoreCase("Egg")) {
                            CategoryId = "146";
                        } else if (prodCategory.equalsIgnoreCase("Poultry")) {
                            CategoryId = "145";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_Catid.equalsIgnoreCase("145")) {
                ArrayAdapter adapterCat = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PoultryCat);
                adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_prod_category.setAdapter(adapterCat);
                CategoryId = "145";
                spinner_prod_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        prodCategory = parent.getItemAtPosition(position).toString();
                        if (prodCategory.equalsIgnoreCase("Egg")) {
                            CategoryId = "146";
                        } else if (prodCategory.equalsIgnoreCase("Poultry")) {
                            CategoryId = "145";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_PrType.equalsIgnoreCase("1")) {
                ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, business);
                aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_sector.setAdapter(aa3);
                SectorId = "1";
                spinner_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sector_st = parent.getItemAtPosition(position).toString();
                        if (sector_st.equalsIgnoreCase("B2B(Business to Business)")) {
                            SectorId = "1";
                        } else if (sector_st.equalsIgnoreCase("Buyer network")) {
                            SectorId = "2";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_PrType.equalsIgnoreCase("2")) {
                ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, buyerSp);
                aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_sector.setAdapter(aa3);
                SectorId = "2";
                spinner_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sector_st = parent.getItemAtPosition(position).toString();
                        if (sector_st.equalsIgnoreCase("B2B(Business to Business)")) {
                            SectorId = "1";
                        } else if (sector_st.equalsIgnoreCase("Buyer network")) {
                            SectorId = "2";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_ListingType.equalsIgnoreCase("0")) {
                ArrayAdapter aa4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, productSp);
                aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_list_type.setAdapter(aa4);
                ListingTypeId = "0";
                spinner_list_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        listingtype_st = parent.getItemAtPosition(position).toString();
                        if (listingtype_st.equalsIgnoreCase("Product")) {
                            ListingTypeId = "0";
                        } else if (listingtype_st.equalsIgnoreCase("Service")) {
                            ListingTypeId = "1";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_ListingType.equalsIgnoreCase("1")) {
                ArrayAdapter aa4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceSp);
                aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_list_type.setAdapter(aa4);
                ListingTypeId = "1";
                spinner_list_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        listingtype_st = parent.getItemAtPosition(position).toString();
                        if (listingtype_st.equalsIgnoreCase("Product")) {
                            ListingTypeId = "0";
                        } else if (listingtype_st.equalsIgnoreCase("Service")) {
                            ListingTypeId = "1";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_WUnit.equalsIgnoreCase("Crate")) {
                ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Crate);
                aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_unit.setAdapter(aa5);
                UnitId = "16";
                spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit_sp_st = parent.getItemAtPosition(position).toString();
                        if (unit_sp_st.equalsIgnoreCase("Crate")) {
                            UnitId = "16";
                        } else if (unit_sp_st.equalsIgnoreCase("Box")) {
                            UnitId = "17";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_WUnit.equalsIgnoreCase("Box")) {
                ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Box);
                aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_unit.setAdapter(aa5);
                UnitId = "17";
                spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit_sp_st = parent.getItemAtPosition(position).toString();
                        if (unit_sp_st.equalsIgnoreCase("Crate")) {
                            UnitId = "16";
                        } else if (unit_sp_st.equalsIgnoreCase("Box")) {
                            UnitId = "17";
                        } else {
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_SWUnit.equalsIgnoreCase("17")) {
                ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Crate);
                aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_sample_unit.setAdapter(aa5);
                UnitId = "17";
                spinner_sample_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit_sp_st = parent.getItemAtPosition(position).toString();
                        if (unit_sp_st.equalsIgnoreCase("Crate")) {
                            UnitId = "17";
                        } else if (unit_sp_st.equalsIgnoreCase("Box")) {
                            UnitId = "18";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_SWUnit.equalsIgnoreCase("18")) {
                ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Box);
                aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sample_unit.setAdapter(aa5);
                UnitId = "18";
                spinner_sample_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit_sp_st = parent.getItemAtPosition(position).toString();
                        if (unit_sp_st.equalsIgnoreCase("Crate")) {
                            UnitId = "17";
                        } else if (unit_sp_st.equalsIgnoreCase("Box")) {
                            UnitId = "18";
                        } else {
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if (Pr_SGWUnit.equalsIgnoreCase("17")) {
                ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Crate);
                aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sample_gross_weight.setAdapter(aa5);
                UnitId = "17";
                spinner_sample_gross_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit_sp_st = parent.getItemAtPosition(position).toString();
                        if (unit_sp_st.equalsIgnoreCase("Crate")) {
                            UnitId = "17";
                        } else if (unit_sp_st.equalsIgnoreCase("Box")) {
                            UnitId = "18";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else if (Pr_SGWUnit.equalsIgnoreCase("18")) {
                ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Box);
                aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sample_gross_weight.setAdapter(aa5);
                UnitId = "18";
                spinner_sample_gross_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit_sp_st = parent.getItemAtPosition(position).toString();
                        if (unit_sp_st.equalsIgnoreCase("Crate")) {
                            UnitId = "17";
                        } else if (unit_sp_st.equalsIgnoreCase("Box")) {
                            UnitId = "18";
                        } else {
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
          //  Log.e("Address",AddressMap);
            stock_et.setText(Pr_Stocks);
            sp_state.setText(Pr_State);
            sp_city.setText(Pr_City);
            title_add_prod.setText(Pr_Name);
            price_edittext.setText(Pr_Price);
            min_order_et.setText(Pr_Quantity);
            discount_et.setText(Pr_Discount);
            unit_edit.setText(Pr_unit);
            pincode_et.setText(Zipcode);
            delivery_lead_time.setText(Pr_Days);
            if (!Pr_StartDate.isEmpty() || !Pr_EndDate.isEmpty() || Pr_StartDate != null || Pr_EndDate != null) {
                dis_start_date.setText(Pr_StartDate);
                dis_end_date.setText(Pr_EndDate);
            } else {
                dis_start_date.setText("Discount Start Date");
                dis_end_date.setText("Discount End Date");
            }
            setUpRecyclerView1(Pr_Image);
            sp_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AddProductActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
            sp_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AddProductActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
            ll_eggs_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_eggs_type.setVisibility(View.GONE);
                    spinner_eggs_types.setVisibility(View.VISIBLE);
                }
            });
            ll_quality.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_quality.setVisibility(View.GONE);
                    spinner_quality.setVisibility(View.VISIBLE);
                }
            });
            ll_packtype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_packtype.setVisibility(View.GONE);
                    spinner_pack_type.setVisibility(View.VISIBLE);
                }
            });
            ll_samplepacktype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_samplepacktype.setVisibility(View.GONE);
                    spinner_sample_pack_type.setVisibility(View.VISIBLE);
                }
            });
            discount_et.setText(Pr_Discount);
            min_order_et.setText(Pr_Quantity);
            price_edittext.setText(Pr_Price);
            sample_unit_edit.setText(Pr_SUnit);
            max_quantity_et.setText(Pr_SQuantity);
            sample_price_edittext.setText(Pr_SPrice);
            sample_gross_weight_et.setText(Pr_SGWeight);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void getVendorServicesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);
        Call<JsonElement> callRetrofit = null;
//        callRetrofit = service.getVendorServices(user_id);
        callRetrofit = service.getVendorServices(user_id);
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
                                    if (success.equalsIgnoreCase("1")) {
                                        JSONArray jsonArray = root.getJSONArray("shippingtypes");
                                        shippingList = new ArrayList<>();
                                        List<String> idList = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            idList.add(jsonObject.getString("id"));
                                            shippingList.add(jsonObject.getString("pr_title"));
                                        }
                                        //In response data
                                        progressDialog.dismiss();
                                        if(shippingList.size()>0) {
                                            showAlertDialogShipping(shippingList, idList,mSelectedContinents);
                                            progressDialog.dismiss();
                                        }else{
                                            Toast.makeText(AddProductActivity.this, "Categories not available", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } else {
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
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
                Toast.makeText(AddProductActivity.this,"Something went wrong!please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialogShipping(final List<String> shippingList, final List<String> shippingIdList,SparseBooleanArray mSelectedContinents) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView1 = inflater.inflate(R.layout.custom_list_category, null);
            dialogBuilder.setView(dialogView1);

            Button submitCat_btn = (Button) dialogView1.findViewById(R.id.submitCat_btn);
            final ListView categoryListView = (ListView) dialogView1.findViewById(R.id.categoryList);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView1.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.setVisibility(View.GONE);
        /*    final ShimmerFrameLayout shimmerFrameLayout = dialogView1.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);*/
            alertDialog = dialogBuilder.create();
            if (shippingList.size() == 0) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                Toast.makeText(AddProductActivity.this, "No Categories Available", Toast.LENGTH_SHORT).show();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice, shippingList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            if(!isUpdate) {
                if (shippingList.contains(service_name)) {
                    shippingSelectedList.add(shippingIdList.get(position));
                    shippingSelectedPosition.add(position);
                }
            }
            if (shippingSelectedPosition.size() > 0) {
                for (int pos = 0; pos < shippingSelectedPosition.size(); pos++) {
                    categoryListView.setItemChecked(shippingSelectedPosition.get(pos), true);
                }
            }
            submitCat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    String itemValue = shippingList.get(position);
                    SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                    // SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                    //  ArrayList<String> selectedItems = new ArrayList<String>();
                    // shippingSelectedPosition = new ArrayList<>();
                    // shippingSelectedList = new ArrayList<>();
                    for (int i = 0; i < checked.size(); i++) {
                        // Item position in adapter
                        int position = checked.keyAt(i);
                        // Add sport if it is checked i.e.) == TRUE!
                        if (checked.valueAt(i)) {
                            selectedItems.add(adapter.getItem(position));
                            shippingSelectedList.add(shippingIdList.get(position));
                            shippingSelectedPosition.add(position);
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    if (selectedItems.size() > 0) {
                        for (int i = 0; i < selectedItems.size(); i++) {
                            if (i == selectedItems.size() - 1) {
                                stringBuilder.append(selectedItems.get(i));
                            } else {
                                stringBuilder.append(selectedItems.get(i) + ", ");
                            }
                        }
                        spinner_shipping_type.setText(stringBuilder.toString());
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Please select one shipping type!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAddProductJsonObject() {
        String imageBase64;
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("pr_title", title_add_prod.getText().toString());
            jsonObj.put("pr_catid", CategoryId);
            jsonObj.put("pr_eggtype", eggs_type);
            jsonObj.put("shipingprovide", shipping_st);
            // shipping type array
            JSONArray shippingTypeArray = new JSONArray();
            for (int j = 0; j < shippingSelectedList.size(); j++) {
                JSONObject shippingObj = new JSONObject();
                shippingObj.put("service_id", shippingSelectedList.get(j));
                shippingTypeArray.put(shippingObj);
            }
            jsonObj.put("shipping_type", shippingTypeArray);
            Log.e("shippingTypeArray", "" + jsonObj);
            jsonObj.put("pr_userid", user_id);
            // jsonObj.put("pr_bussid", "");
            jsonObj.put("pr_price", price_edittext.getText().toString());
            jsonObj.put("pr_currency", "INR");
            jsonObj.put("pr_stocks", stock_et.getText().toString());
            jsonObj.put("pr_min", min_order_et.getText().toString());
            jsonObj.put("prd_discount",discount_et.getText().toString());
            // Image Array
            JSONArray imageArray = new JSONArray();
            for (int j = 0; j < imagesList.size(); j++) {
                JSONObject imageObj = new JSONObject();
                imageBase64 = imagesList.get(j).getImage_base64();
                //imageObj.put("img","data:image/jpeg;base64,"+imageBase64.replace("\\",""));
                imageObj.put("img","data:image/jpeg;base64,"+imageBase64.replaceAll("\\\\",""));
                imageObj.put("isPrimary", imagesList.get(j).getIsPrimary());
                imageArray.put(imageObj);
            }
            jsonObj.put("image", imageArray);
            Log.e("image_Array", "" + jsonObj);
            //    JsonParser jsonParser = new JsonParser();
            jsonObj.put("pr_type", SectorId);
            jsonObj.put("pr_quality", quality_st);
            jsonObj.put("listing_type",ListingTypeId);
            jsonObj.put("packaging", packTypeStr);
            jsonObj.put("job_location", Product_Address_Map);
            jsonObj.put("city", cityId);
            jsonObj.put("state", stateId);
            jsonObj.put("country", countryId);
            jsonObj.put("weight", unit_edit.getText().toString());
            jsonObj.put("weight_unit", UnitId);
            jsonObj.put("makeoffer", receivedOffersStr);
            if(pincode_et.getText().toString() != null) {
                jsonObj.put("zipcode", pincode_et.getText().toString());
            }else{
                jsonObj.put("zipcode", "");
            }
            if(sample_unit_edit.getText().toString().isEmpty()) {
                jsonObj.put("sampleweightunit", "");
            }else{
                jsonObj.put("sampleweightunit", sample_unit_edit.getText().toString()); }
            if(sample_gross_weight_et.getText().toString().isEmpty()) {
                jsonObj.put("sgweight", "");
            }else{
                jsonObj.put("sgweight", sample_gross_weight_et.getText().toString());
            }
            jsonObj.put("sgweight_unit", UnitIdSample);
            jsonObj.put("saprd_pack", sample_packtype);
            if( max_quantity_et.getText().toString().isEmpty() || sample_price_edittext.getText().toString().isEmpty()) {
                jsonObj.put("squantity", "");
                jsonObj.put("samplecost", "");
            }else{
                jsonObj.put("squantity", max_quantity_et.getText().toString());
                jsonObj.put("samplecost", sample_price_edittext.getText().toString());
            }
            jsonObj.put("scurrency", "INR");
            jsonObj.put("sampleweight_unit", UnitIdSample);
            if(delivery_lead_time.getText().toString().isEmpty()) {
                jsonObj.put("day", "");
            }else{
                jsonObj.put("day", delivery_lead_time.getText().toString());
            }
            jsonObj.put("discount_from",StartDateStr);
            jsonObj.put("discount_to",EndDateStr);

            androidNetworkingAdd(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingAdd(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/addproduct")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String message = response.getString("message");
                            String success = response.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,message, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                // progressDialog.dismiss();
                                Intent i = new Intent(AddProductActivity.this, StoreMyProductsActivity.class);
                                i.putExtra("Customizations","0");
                                startActivity(i);
                                progressDialog.dismiss();
                                //Toast.makeText(StoreMyProductsActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddProductActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    public void setEditProductJsonObject() {
        String imageBase64;
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("productid",Pr_Id);
            jsonObj.put("pr_title", title_add_prod.getText().toString());
            jsonObj.put("pr_catid", CategoryId);
            jsonObj.put("pr_eggtype", eggs_type);
            jsonObj.put("shipingprovide", shipping_st);
            // shipping type array
            JSONArray shippingTypeArray = new JSONArray();
            for (int j = 0; j < shippingSelectedList.size(); j++) {
                JSONObject shippingObj = new JSONObject();
                shippingObj.put("service_id", shippingSelectedList.get(j));
                shippingTypeArray.put(shippingObj);
            }
            jsonObj.put("shipping_type", shippingTypeArray);
            Log.e("shippingTypeArray", "" + jsonObj);

            jsonObj.put("pr_userid", user_id);
            // jsonObj.put("pr_bussid", "");
            jsonObj.put("pr_price", price_edittext.getText().toString());
            jsonObj.put("pr_currency", "INR");
            jsonObj.put("pr_stocks", stock_et.getText().toString());
            jsonObj.put("pr_min", min_order_et.getText().toString());
            Discount = discount_et.getText().toString();
            jsonObj.put("prd_discount",Discount);
            // Image Array
            JSONArray imageArray = new JSONArray();
            for (int j = 0; j < imagesList.size(); j++) {
                JSONObject imageObj = new JSONObject();
                imageBase64 = imagesList.get(j).getImage_base64();
                //  imageObj.put("img","data:image/jpeg;base64,"+imageBase64.replace("\\", ""));
                imageObj.put("img",imageBase64.replaceAll("\\\\", ""));
                imageObj.put("isPrimary", imagesList.get(j).getIsPrimary());
                imageArray.put(imageObj);
            }
            jsonObj.put("image", imageArray);
            Log.e("image_Array", "" + jsonObj);
            jsonObj.put("pr_type", SectorId);
            jsonObj.put("pr_quality", quality_st);
            jsonObj.put("listing_type",ListingTypeId);
            jsonObj.put("packaging", packTypeStr);
            if(Product_Address_Map.isEmpty() || Product_Address_Map.equalsIgnoreCase("null")){
                jsonObj.put("job_location",AddressMap);
            }else {
                jsonObj.put("job_location", Product_Address_Map);
            }
        /*    jsonObj.put("city", cityId);
            jsonObj.put("state", stateId);
            jsonObj.put("country", countryId);*/
            jsonObj.put("weight", unit_edit.getText().toString());
            jsonObj.put("weight_unit", UnitId);
            jsonObj.put("makeoffer", receivedOffersStr);
            jsonObj.put("zipcode", pincode_et.getText().toString());
            jsonObj.put("sampleweightunit", sample_unit_edit.getText().toString());
            jsonObj.put("sgweight", sample_gross_weight_et.getText().toString());
            jsonObj.put("sgweight_unit", UnitIdSample);
            jsonObj.put("saprd_pack", sample_packtype);
            jsonObj.put("squantity", max_quantity_et.getText().toString());
            jsonObj.put("samplecost", sample_price_edittext.getText().toString());
            jsonObj.put("scurrency", "INR");
            jsonObj.put("sampleweight_unit", UnitIdSample);
            jsonObj.put("day", delivery_lead_time.getText().toString());
            jsonObj.put("discount_from",StartDateStr);
            jsonObj.put("discount_to",EndDateStr);
            androidNetworkingEdit(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingEdit(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Storemanagementser/edittoproduct")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String message = response.getString("message");
                            String success = response.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(AddProductActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent i = new Intent(AddProductActivity.this, StoreMyProductsActivity.class);
                                i.putExtra("Customizations","0");
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddProductActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }
    private void getStatesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getStates(countryId);
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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("states");
                                    statesList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        statesList.add(jsonObject.getString("subdivision_1_name"));
                                        idList.add(jsonObject.getString("subdivision_1_iso_code"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(false,statesList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                progressDialog.dismiss();
            }
        });
    }
    private void getCityList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCities(countryId,stateId);
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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("cities");
                                    citiesList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        idList.add(jsonObject.getString("city_id"));
                                        citiesList.add(jsonObject.getString("city_name"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialogCity(citiesList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void showAlertDialog( final boolean isCountry, final List<String> countryList,
                                  final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);
            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            if (isCountry)
                title.setText("Choose Country");
            else
                title.setText("Choose State");
            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
          /*  final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);*/
            alertDialog = dialogBuilder.create();
            if (countryList.size() == 0) {
                if (alertDialog != null)
                    alertDialog.dismiss();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.simple_list_item, R.id.list_item_txt, countryList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    clickedSearch = "clicked";
                    if (isCountry) {
                        textlength = inputSearch.getText().length();
                        selectedCountryList.clear();
                        selectedCountryIdList.clear();
                        for (int i = 0; i < countryList.size(); i++) {
                            if (textlength <= countryList.get(i).length()) {
                                Log.d("ertyyy", countryList.get(i).toLowerCase().trim());
                                if (countryList.get(i).toLowerCase().trim().contains(
                                        inputSearch.getText().toString().toLowerCase().trim())) {
                                    selectedCountryList.add(countryList.get(i));
                                    selectedCountryIdList.add(idList.get(i));
                                }
                            }
                        }
                    }else{
                        textlength = inputSearch.getText().length();
                        selectedCountryList.clear();
                        selectedCountryIdList.clear();
                        for (int i = 0; i < statesList.size(); i++) {
                            if (textlength <= statesList.get(i).length()) {
                                Log.d("ertyyy", statesList.get(i).toLowerCase().trim());
                                if (statesList.get(i).toLowerCase().trim().contains(
                                        inputSearch.getText().toString().toLowerCase().trim())) {
                                    selectedCountryList.add(statesList.get(i));
                                    selectedCountryIdList.add(idList.get(i));
                                }
                            }
                        }
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProductActivity.this,
                            R.layout.simple_list_item, R.id.list_item_txt, selectedCountryList);
                    // Assign adapter to ListView
                    categoryListView.setAdapter(adapter);
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
            clickedSearch = "not clicked";
            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (clickedSearch.equalsIgnoreCase("clicked")) {
                        if (isCountry) {
                            String itemValue = selectedCountryList.get(position);
                            sp_country.setText(itemValue);
                            countryId = selectedCountryIdList.get(position);
                            sp_country.setText(itemValue);
                            sp_state.setText("");
                            sp_state.setHint("Choose State");
                            //  countryId = idList.get(position);
                            alertDialog.dismiss();
                        } else {
                            String itemValue = selectedCountryList.get(position);
                       /* sp_state.setText(itemValue);
                        stateId = idList.get(position);*/
                            sp_state.setText(itemValue);
                            stateId = selectedCountryIdList.get(position);
                            alertDialog.dismiss();
                        }
                    }else{
                        if (isCountry) {
                            String itemValue = countryList.get(position);
                            sp_country.setText(itemValue);
                            sp_state.setText("");
                            sp_state.setHint("Choose State");
                            countryId = idList.get(position);
                            alertDialog.dismiss();
                        } else {
                            String itemValue = countryList.get(position);
                            sp_state.setText(itemValue);
                            stateId = idList.get(position);
                            alertDialog.dismiss();
                        }
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlertDialogCity(final List<String> cityList,
                                     final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddProductActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose City");

            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
            alertDialog = dialogBuilder.create();
            if (cityList.size() == 0) {
                if (alertDialog != null)
                    alertDialog.dismiss();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.simple_list_item, R.id.list_item_txt, cityList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    clickedSearch = "clicked";
                    textlength = inputSearch.getText().length();
                    selectedCountryList.clear();
                    selectedCountryIdList.clear();
                    for (int i = 0; i < cityList.size(); i++) {
                        if (textlength <= cityList.get(i).length()) {
                            Log.d("ertyyy", cityList.get(i).toLowerCase().trim());
                            if (cityList.get(i).toLowerCase().trim().contains(
                                    inputSearch.getText().toString().toLowerCase().trim())) {
                                selectedCountryList.add(cityList.get(i));
                                selectedCountryIdList.add(idList.get(i));
                            }
                        }
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProductActivity.this,
                            R.layout.simple_list_item, R.id.list_item_txt, selectedCountryList);
                    // Assign adapter to ListView
                    categoryListView.setAdapter(adapter);
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
            clickedSearch = "not clicked";
            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (clickedSearch.equalsIgnoreCase("clicked")) {
                        String itemValue = selectedCountryList.get(position);
                        sp_city.setText(itemValue);
                        cityId = selectedCountryIdList.get(position);
                        alertDialog.dismiss();
                    }else{
                        String itemValue = cityList.get(position);
                        sp_city.setText(itemValue);
                        cityId = idList.get(position);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(AddressMap.equalsIgnoreCase("true")){
            textview_address.setVisibility(View.GONE);
            address_txt_map.setVisibility(View.VISIBLE);
            address_txt_map.setText(Product_Address_Map);   
        }else{
            textview_address.setVisibility(View.VISIBLE);
        }
    }
    private void setUpRecyclerView(String image) {
        if (imagesList.size() > 0) {
            fileList.add(image);
            ImagesAdapter imagesAdapter = new ImagesAdapter(AddProductActivity.this, fileList,imagesList, true,"add");
            images_recyclerView.setAdapter(imagesAdapter);
            imagesAdapter.notifyDataSetChanged();
        } else {
            images_recyclerView.setVisibility(View.GONE);
        }
    }
    private void setUpRecyclerView1(String image) {
        if(Pr_Image != null) {
            fileList.add(image);
            ImagesAdapter imagesAdapter = new ImagesAdapter(AddProductActivity.this, fileList,imageModelList, true,"edit");
            images_recyclerView.setAdapter(imagesAdapter);
            imagesAdapter.notifyDataSetChanged();
        } else{
            images_recyclerView.setVisibility(View.GONE);
        }
    }
}
