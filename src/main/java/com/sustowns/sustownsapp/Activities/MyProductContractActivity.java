package com.sustowns.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.ExistingAddressAdapterContract;
import com.sustowns.sustownsapp.Adapters.ProductContractAdapter;
import com.sustowns.sustownsapp.Api.BidContractsApi;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.PostContractsApi;
import com.sustowns.sustownsapp.Api.TransportApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.GetAddressModel;
import com.sustowns.sustownsapp.Models.OpenRequestModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sustowns.sustownsapp.Activities.FileUtils.getPath;

public class MyProductContractActivity extends AppCompatActivity {
    public static String Product_Detail_Address_Map = "";
    ImageView backarrow,poultry_editprod_contract_image;
    LinearLayout ll_addprod_contracts,linear_pro_contracts,ll_poultry_add_contract_product,linearlayout;
    RecyclerView recycler_view_post_contracts,recycler_added_contracts,recyclerview_saved_addresses;
    ProductContractAdapter productContractAdapter;
    String[] contracts = {"Contract 1","Contract 2","Contract 3"};
    Button add_product_request,todate,fromdate,submit_general_contract_product,poultryfromdate,poultrytodate,close_drop_dialog,choose_document_btn;
    TextView fromDateText,toDateText,poultryfromDateText,poultrytoDateText,textheading;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    SparseBooleanArray mSelectedItemsListIds = new SparseBooleanArray();
    Spinner spinner_choose_category,spinner_subcategory,spinner_packing_type,spinner_unit,spinner_weight,spinner_quantity,spinner_packing,
            poultry_spinner_choose_name,poultry_spinner_choose_quality,poultry_spinner_choose_location,quantity_unit_spinner;
    Spinner spinner_location,poultry_spinner_choose_category,poultry_spinner_choose_subcategory,poultry_spinner_choose_subsubcategory;
    String[] category = {"Choose Category","Energy","Gadgets","Food","Health","Textiles and Apparels","Crafts","Machinery","Consumables",
            "Miscellaneous","Live Stock"};
    String[] packingType = {"Other","Bulk","Can","Drum","Gift Packing","Mason Jar","Tank","Vaccum Pack","Carton","Bags","Pallet"};
    String[] unit = {"Crate","Box"};
    String[] weight = {"lt","gm","kg","ml","mg","tn"};
    String[] quantity = {"mm","cm","kg","pc","pk","tn"};
    String[] cargoPacking = {"select packing type","Bags","Bulk","Carton","Drum","Pallet","Tank","Other"};
    String[] location = {"Select Location Type","Buyer's Location","Vendor's Location"};
    String[] poultryCat = {"LiveStock"};
    String[] poultrySubCat = {"Poultry"};
    String[] poultrySubSubCat = {"Egg"};
    String[] poultryProdName = {"Eggs","organic"};
    String[] poultryQuality = {"AA","A","B"};
    String[] poultryLocation = {"seller","buyers"};
    String[] QuantityUnits = {"Crate","Box"};
    String[] country = {"India","Indonesia","Iceland","Australia","Algeria","Malaysia","Saudi Arabia","Singapore","USA","UK","Uganda"};
    ProgressDialog progressDialog;
    RelativeLayout rl_capture, rl_gallery;
    public String PickedImgPath = null;
    ArrayList<OpenRequestModel> openRequestModels;
    PreferenceUtils preferenceUtils;
    EditText poultry_contract_heading,poultry_quantity_edit;
    Button poultry_choose_image_btn,poultry_choosedoc_btn,submit_add_prod_contract,choose_image,save_address,save_address_btn;
    CircleImageView prod_contract_image,poultry_prod_contract_image;
    String user_role,profileString = "",imagePath,user_id,PoultryFromDate,PoultryToDate,PoultryProdName,PoultryQuality,PoultryLocation;
    final int CAMERA_CAPTURE = 1;
    final int PICK_IMAGE = 100;
    Dialog customdialog;
    EditText name_address,company_address, email_address, first_name_address,last_name_address, address1_address, address2_address,mobile_address, pincode_address, fax_address;
    String quantityUnitStr,countryId,stateId, cityId, firstnameAddress, lastnameAddress, address1Address, address2Address, addressState, addressTown, mobileAddress, pincodeAddress, faxAddress,AddressId,countryAddress,Action="",Latitude,Longitude;
    TextView address_txt_map_dialog,saved_address_text,document_text,address_town;
    LinearLayout ll_shipping_details,ll_existing_address,ll_dates,ll_poultry_edit_contract_product;
    RadioGroup radioGroup;
    RadioButton existing_radiobtn, new_radiobtn;
    ExistingAddressAdapterContract existingAddressAdapter;
    ArrayList<GetAddressModel> getAddressModels;
    String PoultryQuantity,PoultryName,actionValue="",id,job_id,PoultryEditToDate,filename,ret,fileString = "",unitId,headingValue = "";
    RadioGroup radio_group;
    RadioButton open_radiobtn,range_radiobtn;
    boolean isUpdate;
    int position;
    TextView edit_poultry_cat,edit_poultry_subcat,edit_poultry_subsubcat,edit_poultry_contractheading,edit_poultry_prodname,edit_poultry_quantity,
            edit_poultry_unitquantity,edit_poultry_quality,edit_poultry_location,spinner_country,address_state,not_availabletext;
    Button poultryeditfromdate,poultryedittodate,submit_edit_prod_contract;
    private static final int PICKFILE_RESULT_CODE = 3;
    Uri returnUri;
    byte[] bytes;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> statesList = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
    AlertDialog alertDialog;
    int textlength = 0;
    ArrayList<String> selectedCountryList = new ArrayList<String>();
    ArrayList<String> selectedCountryIdList = new ArrayList<String>();
    Helper helper;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static String getEncodedImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encodedImagePatientImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImagePatientImage;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_product_contract);
        try {
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getMyProductContractList();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            helper = new Helper(MyProductContractActivity.this);
            isUpdate = false;
            preferenceUtils = new PreferenceUtils(MyProductContractActivity.this);
            user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");
            user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
            Latitude = preferenceUtils.getStringFromPreference(PreferenceUtils.LATITUDE,"");
            Longitude = preferenceUtils.getStringFromPreference(PreferenceUtils.LONGITUDE,"");
            recycler_view_post_contracts = (RecyclerView) findViewById(R.id.recycler_view_post_contracts);
            LinearLayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_view_post_contracts.setLayoutManager(lManager);

            recycler_added_contracts = (RecyclerView) findViewById(R.id.recycler_added_contracts);
            LinearLayoutManager lManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_view_post_contracts.setLayoutManager(lManager1);
            not_availabletext = (TextView) findViewById(R.id.not_availabletext);
            ll_poultry_add_contract_product = (LinearLayout) findViewById(R.id.ll_poultry_add_contract_product);
            poultry_spinner_choose_category = (Spinner) findViewById(R.id.poultry_spinner_choose_category);
            poultry_spinner_choose_subcategory = (Spinner) findViewById(R.id.poultry_spinner_choose_subcategory);
            poultry_spinner_choose_subsubcategory = (Spinner) findViewById(R.id.poultry_spinner_choose_subsubcategory);
            poultry_contract_heading = (EditText) findViewById(R.id.poultry_contract_heading);
            poultry_quantity_edit = (EditText) findViewById(R.id.poultry_quantity_edit);
            poultry_spinner_choose_name = (Spinner) findViewById(R.id.poultry_spinner_choose_name);
            ll_addprod_contracts = (LinearLayout) findViewById(R.id.ll_addprod_contracts);
            linear_pro_contracts = (LinearLayout) findViewById(R.id.linear_pro_contracts);
            ll_poultry_edit_contract_product = (LinearLayout) findViewById(R.id.ll_poultry_edit_contract_product);
            ll_dates = (LinearLayout) findViewById(R.id.ll_dates);
            radio_group = (RadioGroup) findViewById(R.id.radio_group);
            open_radiobtn = (RadioButton) findViewById(R.id.open_radiobtn);
            range_radiobtn = (RadioButton) findViewById(R.id.range_radiobtn);
            poultry_spinner_choose_quality = (Spinner) findViewById(R.id.poultry_spinner_choose_quality);
            poultry_spinner_choose_location = (Spinner) findViewById(R.id.poultry_spinner_choose_location);
            poultry_choose_image_btn = (Button) findViewById(R.id.poultry_choose_image_btn);
            choose_image = (Button) findViewById(R.id.choose_image);
            choose_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraDialog();
                }
            });
            poultry_choose_image_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraDialog();
                }
            });
            prod_contract_image = (CircleImageView) findViewById(R.id.prod_contract_image);
            poultry_prod_contract_image = (CircleImageView) findViewById(R.id.poultry_prod_contract_image);
            submit_add_prod_contract = (Button) findViewById(R.id.submit_add_prod_contract);
            submit_general_contract_product = (Button) findViewById(R.id.submit_general_contract_product);
            linear_pro_contracts.setVisibility(View.VISIBLE);
            ll_addprod_contracts.setVisibility(View.GONE);
            ll_poultry_add_contract_product.setVisibility(View.GONE);
            ll_poultry_edit_contract_product.setVisibility(View.GONE);
            backarrow = (ImageView) findViewById(R.id.backarrow);
            fromDateText = (TextView) findViewById(R.id.from_date);
            toDateText = (TextView) findViewById(R.id.to_date);
            fromdate = (Button) findViewById(R.id.fromdatebtn);
            todate = (Button) findViewById(R.id.todatebtn);
            poultryfromDateText = (TextView) findViewById(R.id.poultryfromDateText);
            poultrytoDateText = (TextView) findViewById(R.id.poultrytoDateText);
            poultrytodate = (Button) findViewById(R.id.poultrytodate);
            poultryfromdate = (Button) findViewById(R.id.poultryfromdate);
            textheading = (TextView) findViewById(R.id.textheading);
// edit product
            edit_poultry_cat = (TextView) findViewById(R.id.edit_poultry_cat);
            edit_poultry_subcat = (TextView) findViewById(R.id.edit_poultry_subcat);
            edit_poultry_subsubcat = (TextView) findViewById(R.id.edit_poultry_subsubcat);
            edit_poultry_contractheading = (TextView) findViewById(R.id.edit_poultry_contractheading);
            edit_poultry_prodname = (TextView) findViewById(R.id.edit_poultry_prodname);
            poultry_editprod_contract_image = (ImageView) findViewById(R.id.poultry_editprod_contract_image);
            edit_poultry_quantity = (TextView) findViewById(R.id.edit_poultry_quantity);
            edit_poultry_unitquantity = (TextView) findViewById(R.id.edit_poultry_unitquantity);
            edit_poultry_quality = (TextView) findViewById(R.id.edit_poultry_quality);
            edit_poultry_location = (TextView) findViewById(R.id.edit_poultry_location);
            poultryeditfromdate = (Button) findViewById(R.id.poultryeditfromdate);
            poultryedittodate = (Button) findViewById(R.id.poultryedittodate);
            submit_edit_prod_contract = (Button) findViewById(R.id.submit_edit_prod_contract);
            spinner_choose_category = (Spinner) findViewById(R.id.spinner_choose_category);
            spinner_subcategory = (Spinner) findViewById(R.id.spinner_subcategory);
            spinner_packing_type = (Spinner) findViewById(R.id.spinner_packing_type);
            spinner_unit = (Spinner) findViewById(R.id.spinner_unit);
            spinner_weight = (Spinner) findViewById(R.id.spinner_weight);
            spinner_quantity = (Spinner) findViewById(R.id.spinner_quantity);
            spinner_packing = (Spinner) findViewById(R.id.spinner_packing);
            spinner_location = (Spinner) findViewById(R.id.spinner_location);
            quantity_unit_spinner = (Spinner) findViewById(R.id.quantity_unit_spinner);
            document_text = (TextView) findViewById(R.id.document_text);
            choose_document_btn = (Button) findViewById(R.id.choose_document_btn);
            poultry_choosedoc_btn = (Button) findViewById(R.id.poultry_choosedoc_btn);
            poultry_choosedoc_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    //intent.putExtra("browseCoa", itemToBrowse);
                    //Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                    try {
                        //startActivityForResult(chooser, FILE_SELECT_CODE);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),PICKFILE_RESULT_CODE);
                    } catch (Exception ex) {
                        System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                    }
                }
            });

            poultrySpinner();
            // choose category
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_choose_category.setAdapter(aa);
            spinner_subcategory.setAdapter(aa);
            spinner_choose_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // For packing type
            ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, packingType);
            aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_packing_type.setAdapter(aa1);
            spinner_packing_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // spinner for unit
            ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unit);
            aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_unit.setAdapter(aa2);
            spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String unit = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //  spinner weight
            ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weight);
            aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_weight.setAdapter(aa3);
            spinner_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // spinner quantity
            ArrayAdapter aa4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quantity);
            aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_quantity.setAdapter(aa4);
            spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // cargo packing
            ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cargoPacking);
            aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_packing.setAdapter(aa5);
            spinner_packing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // spinner location
            ArrayAdapter aa6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, location);
            aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_location.setAdapter(aa6);
            spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
/*
            radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.open_radiobtn:
                            actionValue = "open";
                            ll_dates.setVisibility(View.GONE);
                            break;
                        case R.id.range_radiobtn:
                            actionValue = "range";
                            ll_dates.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });
*/
            poultryedittodate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialogedit();
                }
            });
            fromdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialog();
                }
            });
            todate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialog1();
                    poultryDateDialogEdit();
                }
            });
            poultryfromdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    poultryDateDialog1();

                }
            });
            poultrytodate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    poultryDateDialog2();
                }
            });
            choose_document_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
                }
            });
           submit_general_contract_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
            submit_add_prod_contract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isUpdate) {
                        if(poultry_contract_heading.getText().toString().isEmpty() || profileString.isEmpty() || poultry_quantity_edit.getText().toString().isEmpty()||
                                PoultryFromDate.isEmpty()||PoultryToDate.isEmpty()){
                            Toast.makeText(MyProductContractActivity.this, "Please Fill Empty Fileds", Toast.LENGTH_SHORT).show();
                        }
                        // addProduct();
                        else if(PoultryLocation.equalsIgnoreCase("buyers")) {
                            if(Action.equalsIgnoreCase("existing")) {
                                setJsonObject();
                            }else{
                                setJsonObject1();
                            }
                        }else {
                            setJsonObject2();
                        }
                    } else {
                        editPoultryProdContractRequest();
                    }

                }
            });
            submit_edit_prod_contract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editPoultryProdContractRequest();
                }
            });

            add_product_request = (Button) findViewById(R.id.add_product_request);
            add_product_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     headingValue = "Add Product Request";
                     textheading.setText(headingValue);
                    if (user_role.equalsIgnoreCase("poultry")) {
                        linear_pro_contracts.setVisibility(View.GONE);
                        ll_addprod_contracts.setVisibility(View.GONE);
                        ll_poultry_add_contract_product.setVisibility(View.VISIBLE);
                    } else if (user_role.equalsIgnoreCase("transport")) {
                        linear_pro_contracts.setVisibility(View.GONE);
                        ll_addprod_contracts.setVisibility(View.GONE);
                        ll_poultry_add_contract_product.setVisibility(View.VISIBLE);
                    } else if (user_role.equalsIgnoreCase("")) {
                        linear_pro_contracts.setVisibility(View.GONE);
                        ll_addprod_contracts.setVisibility(View.VISIBLE);
                        ll_poultry_add_contract_product.setVisibility(View.GONE);
                    }
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(headingValue.equalsIgnoreCase("Add Product Request") || headingValue.equalsIgnoreCase("Edit Product Request")){
                        Intent i = new Intent(MyProductContractActivity.this, MyProductContractActivity.class);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(MyProductContractActivity.this, BidContractsActivity.class);
                        i.putExtra("Processed","0");
                        startActivity(i);
                        finish();
                    }
                }
            });
            getMyProductContractList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if(headingValue.equalsIgnoreCase("Add Product Request") || headingValue.equalsIgnoreCase("Edit Product Request")){
            Intent i = new Intent(MyProductContractActivity.this, MyProductContractActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(MyProductContractActivity.this, BidContractsActivity.class);
            i.putExtra("Processed","0");
            startActivity(i);
            finish();
        }
    }

    private void cameraDialog(){
        final Dialog customdialog = new Dialog(MyProductContractActivity.this);
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
                browse();
                customdialog.dismiss();
            }
        });
        customdialog.show();
    }
    private void poultrySpinner() {
        // choose poultry category
        ArrayAdapter sp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,poultryCat);
        sp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        poultry_spinner_choose_category.setAdapter(sp1);
        poultry_spinner_choose_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // choose poultry subcategory
        ArrayAdapter sp2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,poultrySubCat);
        sp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        poultry_spinner_choose_subcategory.setAdapter(sp2);
        poultry_spinner_choose_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // choose poultry subsubcategory
        ArrayAdapter sp3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,poultrySubSubCat);
        sp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        poultry_spinner_choose_subsubcategory.setAdapter(sp3);
        poultry_spinner_choose_subsubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // choose poultry product name
        ArrayAdapter sp4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,poultryProdName);
        sp4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        poultry_spinner_choose_name.setAdapter(sp4);
        poultry_spinner_choose_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PoultryProdName = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // choose poultry_spinner_choose_quality
        ArrayAdapter sp5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,poultryQuality);
        sp5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        poultry_spinner_choose_quality.setAdapter(sp5);
        poultry_spinner_choose_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PoultryQuality = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // choose poultry quantity unit
        ArrayAdapter quantity_sp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,QuantityUnits);
        quantity_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        quantity_unit_spinner.setAdapter(quantity_sp);
        quantity_unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quantityUnitStr = parent.getItemAtPosition(position).toString();
                if(quantityUnitStr.equalsIgnoreCase("Crate")){
                    unitId = "16";
                }else{
                    unitId = "17";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // choose poultry_spinner_choose_location
        ArrayAdapter sp6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,poultryLocation);
        sp6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        poultry_spinner_choose_location.setAdapter(sp6);
        poultry_spinner_choose_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PoultryLocation = parent.getItemAtPosition(position).toString();
                if(PoultryLocation.equalsIgnoreCase("buyers")) {
                    customdialog = new Dialog(MyProductContractActivity.this);
                    customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    customdialog.setContentView(R.layout.drop_location_dialog);
                    customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                    ll_shipping_details = (LinearLayout) customdialog.findViewById(R.id.ll_shipping_details);
                    ll_existing_address = (LinearLayout) customdialog.findViewById(R.id.ll_existing_address);
                    recyclerview_saved_addresses = (RecyclerView) customdialog.findViewById(R.id.recyclerview_saved_addresses);
                    LinearLayoutManager layoutManager1 = new LinearLayoutManager(MyProductContractActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerview_saved_addresses.setLayoutManager(layoutManager1);
                    saved_address_text = (TextView) customdialog.findViewById(R.id.saved_address_text);
                    save_address_btn = (Button) customdialog.findViewById(R.id.save_address_btn);
                    save_address_btn.setVisibility(View.GONE);
                    name_address = (EditText) customdialog.findViewById(R.id.name_address);
                    company_address = (EditText) customdialog.findViewById(R.id.company_address);
                    email_address = (EditText) customdialog.findViewById(R.id.email_address);
                    first_name_address = (EditText) customdialog.findViewById(R.id.first_name_address);
                    spinner_country = (TextView) customdialog.findViewById(R.id.spinner_countrydialog);
                    spinner_country.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getCountryList();
                        }
                    });
                    last_name_address = (EditText) customdialog.findViewById(R.id.last_name_address);
                    address_txt_map_dialog = customdialog.findViewById(R.id.address_txt_map_dialog);
                    address_txt_map_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mapIntent = new Intent(MyProductContractActivity.this, MapsActivity.class);
                            mapIntent.putExtra("activity", "productContract");
                            mapIntent.putExtra("type", "none");
                            startActivity(mapIntent);
                        }
                    });
                    address2_address = (EditText) customdialog.findViewById(R.id.address2_address);
                    address_state = (TextView) customdialog.findViewById(R.id.address_state);
                    address_state.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getStatesList();
                        }
                    });
                    address_town = (TextView) customdialog.findViewById(R.id.address_town);
                    address_town.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getCityList();
                        }
                    });
                    mobile_address = (EditText) customdialog.findViewById(R.id.mobile_address);
                    pincode_address = (EditText) customdialog.findViewById(R.id.pincode_address);
                    fax_address = (EditText) customdialog.findViewById(R.id.fax_address);
                    radioGroup = (RadioGroup) customdialog.findViewById(R.id.radioGroup);
                    existing_radiobtn = (RadioButton) customdialog.findViewById(R.id.existing_radiobtn);
                    new_radiobtn = (RadioButton) customdialog.findViewById(R.id.new_radiobtn);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.existing_radiobtn:
                                    Action = "existing";
                                    ll_existing_address.setVisibility(View.VISIBLE);
                                    ll_shipping_details.setVisibility(View.GONE);
                                    getExistingAddresses();
                                    // do operations specific to this selection
                                    break;
                                case R.id.new_radiobtn:
                                    Action = "new";
                                    AddressId = "";
                                    ll_shipping_details.setVisibility(View.VISIBLE);
                                    ll_existing_address.setVisibility(View.GONE);
                                    // do operations specific to this selection
                                    break;
                            }
                        }
                    });
                    save_address = (Button) customdialog.findViewById(R.id.save_address);
                    save_address.setVisibility(View.GONE);
                  /*  save_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  saveExistingAddress();
                        }
                    });*/
                    close_drop_dialog = (Button) customdialog.findViewById(R.id.close_drop_dialog);
                    close_drop_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Action.equalsIgnoreCase("new")){
/*                                nameAddress = name_address.getText().toString().trim();
                                companyAddress = company_address.getText().toString().trim();
                                emailAddress = email_address.getText().toString().trim();
                                firstnameAddress = first_name_address.getText().toString().trim();
                                lastnameAddress = last_name_address.getText().toString().trim();
                                address2Address = address2_address.getText().toString().trim();
                                addressState = address_state.getText().toString().trim();
                                addressTown = address_town.getText().toString().trim();
                                mobileAddress = mobile_address.getText().toString().trim();
                                pincodeAddress = pincode_address.getText().toString().trim();
                                faxAddress = fax_address.getText().toString().trim();*/
                            }
                            customdialog.dismiss();
                        }
                    });
                    customdialog.show();
                }else if(PoultryLocation.equalsIgnoreCase("seller")) {
                    PoultryLocation = "seller";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                fromDateText.setText("From Date : "+dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void DateDialog1(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                toDateText.setText("To Date : "+dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void DateDialogedit(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                poultryedittodate.setText("To Date : "+dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void poultryDateDialog1(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                PoultryFromDate = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                poultryfromdate.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();

    }
    public void poultryDateDialog2(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                PoultryToDate = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                poultrytodate.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void poultryDateDialogEdit(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                PoultryEditToDate = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                poultryedittodate.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

            }};

        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();

    }
    private void browse() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
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
                imagePath = null;
                Uri picUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(picUri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                imagePath = c.getString(columnIndex);

                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), picUri);
                    profileString = getEncodedImage(bitmapImage);
                    if(user_role.equalsIgnoreCase("")){
                        prod_contract_image.setImageBitmap(bitmapImage);
                    }else {
                        poultry_prod_contract_image.setImageBitmap(bitmapImage);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Selected Image path: ", imagePath);

                c.close();

            }
        }
        else {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK) {

                    try {
                        returnUri = data.getData();
                   /*     if (filesize >= FILE_SIZE_LIMIT) {
                            Toast.makeText(this,"The selected file is too large. Selet a new file with size less than 2mb",Toast.LENGTH_LONG).show();
                        } else {*/
                        String mimeType = getContentResolver().getType(returnUri);
                        if (mimeType == null) {
                            String path = getPath(this, returnUri);
                            if (path == null) {
                                //   filename = FileUtils.getName(uri.toString());
                            } else {
                                File file = new File(path);
                                filename = file.getName();
                            }
                        } else {
                            returnUri = data.getData();
                            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            filename = returnCursor.getString(nameIndex);
                            document_text.setText(filename);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }
                        File fileSave = getExternalFilesDir(null);
                        String sourcePath = getExternalFilesDir(null).toString();
                        // convert file to base64 string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        String path1 = ret;
                        InputStream in = getContentResolver().openInputStream(returnUri);

                        bytes = getBytes(in);
                        Log.d("data", "onActivityResult: bytes size=" + bytes.length);

                        Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
                        fileString = Base64.encodeToString(bytes, Base64.DEFAULT);
                        //documentArrayList.add(fileString);

                        try {
                            copyFileStream(new File(sourcePath + "/" + filename), returnUri,this);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
        if(user_role.equalsIgnoreCase("")){
            prod_contract_image.setImageBitmap(bmp);
        }else {
            poultry_prod_contract_image.setImageBitmap(bmp);
        }
    }

    public void editProduct(int positionValue){
        headingValue = "Edit Product Request";
        textheading.setText(headingValue);
        linear_pro_contracts.setVisibility(View.GONE);
        ll_poultry_add_contract_product.setVisibility(View.GONE);
        ll_poultry_edit_contract_product.setVisibility(View.VISIBLE);
        ll_addprod_contracts.setVisibility(View.GONE);
        isUpdate = true;
        position = positionValue;
        String onDate = openRequestModels.get(position).getOn_date();
        String[] splitToDate = onDate.split(" ");
        String validDate = openRequestModels.get(position).getEnd_date();
        String[] splitToValid = validDate.split(" ");
        // openRequestModels.get(position).getTitle();
        edit_poultry_cat.setText("LiveStock");
        edit_poultry_subcat.setText("Poultry");
        edit_poultry_subsubcat.setText("Egg");
        edit_poultry_contractheading.setText(openRequestModels.get(position).getContractname());
        edit_poultry_prodname.setText(openRequestModels.get(position).getJob_name());
        edit_poultry_quantity.setText(openRequestModels.get(position).getMinqantity());
        edit_poultry_unitquantity.setText("Crate");
        edit_poultry_quality.setText(openRequestModels.get(position).getSpequality());
        poultryfromdate.setText(splitToDate[0]);
        poultryedittodate.setText(splitToValid[0]);
        edit_poultry_location.setText(openRequestModels.get(position).getJob_location());
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(MyProductContractActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getMyProductContractList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BidContractsApi service = retrofit.create(BidContractsApi.class);
        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.myProductContracts("453","buy");// user_id : 453
        callRetrofit = service.myProductContracts(user_id,"buy");// user_id : 453
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
                                            JSONArray jsonArray = root.getJSONArray("productmyjob");
                                            openRequestModels = new ArrayList<OpenRequestModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                id = jsonObject.getString("id");
                                                String job_name = jsonObject.getString("job_name");
                                                String post_contract_type = jsonObject.getString("post_contract_type");
                                                String user_id = jsonObject.getString("user_id");
                                                String category_id = jsonObject.getString("category_id");
                                                String subcat_id = jsonObject.getString("subcat_id");
                                                String subsubcat_id = jsonObject.getString("subsubcat_id");
                                                String end_date = jsonObject.getString("end_date");
                                                String etime = jsonObject.getString("etime");
                                                String contractname = jsonObject.getString("contractname");
                                                String image = jsonObject.getString("image");
                                                String minqantity = jsonObject.getString("minqantity");
                                                String job_location = jsonObject.getString("job_location");
                                                String contractor_id = jsonObject.getString("contractor_id");
                                                String on_date = jsonObject.getString("on_date");
                                                String bid_id = jsonObject.getString("bid_id");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String title = jsonObject.getString("title");
                                                String status = jsonObject.getString("status");

                                                OpenRequestModel openRequestModel = new OpenRequestModel();
                                                openRequestModel.setId(id);
                                                openRequestModel.setJob_name(job_name);
                                                openRequestModel.setPost_contract_type(post_contract_type);
                                                openRequestModel.setUser_id(user_id);
                                                openRequestModel.setCategory_id(category_id);
                                                openRequestModel.setSubcat_id(subcat_id);
                                                openRequestModel.setSubsubcat_id(subsubcat_id);
                                                openRequestModel.setEnd_date(end_date);
                                                openRequestModel.setEtime(etime);
                                                openRequestModel.setContractname(contractname);
                                                openRequestModel.setImage(image);
                                                openRequestModel.setMinqantity(minqantity);
                                                openRequestModel.setContractor_id(contractor_id);
                                                openRequestModel.setJob_location(job_location);
                                                openRequestModel.setTitle(title);
                                                openRequestModel.setOn_date(on_date);
                                                openRequestModel.setStatus(status);
                                                openRequestModels.add(openRequestModel);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(openRequestModels != null || openRequestModels.size()>0) {
                                        progressDialog.dismiss();
                                        productContractAdapter = new ProductContractAdapter(MyProductContractActivity.this,openRequestModels);
                                        recycler_view_post_contracts.setAdapter(productContractAdapter);
                                        productContractAdapter.notifyDataSetChanged();
                                    }else{
                                        not_availabletext.setVisibility(View.VISIBLE);
                                        recycler_view_post_contracts.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                        // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
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
    public void setJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("v_from", PoultryFromDate.replaceAll("\\\\",""));
            jsonObj.put("v_to",PoultryToDate.replaceAll("\\\\",""));
            jsonObj.put("catid","144");
            jsonObj.put("subcat","145");
            jsonObj.put("ssubcat","146");
            jsonObj.put("job_name",PoultryProdName);
            jsonObj.put("quality",PoultryQuality);
            jsonObj.put("quantity",poultry_quantity_edit.getText().toString());
            jsonObj.put("contname",poultry_contract_heading.getText().toString());
            jsonObj.put("qnt_unit",unitId);
            jsonObj.put("slocation",PoultryLocation);
            jsonObj.put("post_contract_type","buy");
            jsonObj.put("action",Action);
            jsonObj.put("addres_id",AddressId);
            jsonObj.put("displayname",name_address.getText().toString());
            jsonObj.put("companyname",company_address.getText().toString());
            jsonObj.put("fname",first_name_address.getText().toString());
            jsonObj.put("lname",last_name_address.getText().toString());
            jsonObj.put("email",email_address.getText().toString());
            jsonObj.put("address1",Product_Detail_Address_Map);
            jsonObj.put("address2",address2_address.getText().toString());
            jsonObj.put("zipcode",pincode_address.getText().toString());
            jsonObj.put("country",countryAddress);
            jsonObj.put("state",address_state.getText().toString());
            jsonObj.put("city",address_town.getText().toString());
            jsonObj.put("phone",mobile_address.getText().toString());
            jsonObj.put("fax",fax_address.getText().toString());
            jsonObj.put("addrs_latitude",Latitude);
            jsonObj.put("addrs_longitude",Longitude);
            jsonObj.put("image",profileString);
            jsonObj.put("attachment", fileString);
            poultryAddProductContractRequest(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setJsonObject1() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("v_from", PoultryFromDate.replaceAll("\\\\",""));
            jsonObj.put("v_to",PoultryToDate.replaceAll("\\\\",""));
            jsonObj.put("catid","144");
            jsonObj.put("subcat","145");
            jsonObj.put("ssubcat","146");
            jsonObj.put("job_name",PoultryProdName);
            jsonObj.put("quality",PoultryQuality);
            jsonObj.put("quantity",poultry_quantity_edit.getText().toString());
            jsonObj.put("contname",poultry_contract_heading.getText().toString());
            jsonObj.put("qnt_unit",unitId);
            jsonObj.put("slocation",PoultryLocation);
            jsonObj.put("post_contract_type","buy");
            jsonObj.put("action",Action);
            jsonObj.put("addres_id",AddressId);
            jsonObj.put("displayname",name_address.getText().toString());
            jsonObj.put("companyname",company_address.getText().toString());
            jsonObj.put("fname",first_name_address.getText().toString());
            jsonObj.put("lname",last_name_address.getText().toString());
            jsonObj.put("email",email_address.getText().toString());
            jsonObj.put("address1",Product_Detail_Address_Map);
            jsonObj.put("address2",address2_address.getText().toString());
            jsonObj.put("zipcode",pincode_address.getText().toString());
            jsonObj.put("country",countryAddress);
            jsonObj.put("state",address_state.getText().toString());
            jsonObj.put("city",address_town.getText().toString());
            jsonObj.put("phone",mobile_address.getText().toString());
            jsonObj.put("fax",fax_address.getText().toString());
            jsonObj.put("addrs_latitude",Latitude);
            jsonObj.put("addrs_longitude",Longitude);
            jsonObj.put("image",profileString);
            jsonObj.put("attachment", fileString);
            poultryAddProductContractRequest(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setJsonObject2() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("v_from", PoultryFromDate.replaceAll("\\\\",""));
            jsonObj.put("v_to",PoultryToDate.replaceAll("\\\\",""));
            jsonObj.put("catid","144");
            jsonObj.put("subcat","145");
            jsonObj.put("ssubcat","146");
            jsonObj.put("job_name",PoultryProdName);
            jsonObj.put("quality",PoultryQuality);
            jsonObj.put("quantity",poultry_quantity_edit.getText().toString());
            jsonObj.put("contname",poultry_contract_heading.getText().toString());
            jsonObj.put("qnt_unit",unitId);
            jsonObj.put("slocation",PoultryLocation);
            jsonObj.put("post_contract_type","buy");
            jsonObj.put("action","");
            jsonObj.put("addres_id","");
            jsonObj.put("displayname","");
            jsonObj.put("companyname","");
            jsonObj.put("fname","");
            jsonObj.put("lname","");
            jsonObj.put("email","");
            jsonObj.put("address1","");
            jsonObj.put("address2","");
            jsonObj.put("zipcode","");
            jsonObj.put("country","");
            jsonObj.put("state","");
            jsonObj.put("city","");
            jsonObj.put("phone","");
            jsonObj.put("fax","");
            jsonObj.put("addrs_latitude","");
            jsonObj.put("addrs_longitude","");
            jsonObj.put("image",profileString);
            jsonObj.put("attachment", fileString);
            poultryAddProductContractRequest(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void poultryAddProductContractRequest(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/addproductrequest")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String status = response.getString("status");
                            String error = response.getString("error");
                            if (error.equalsIgnoreCase("false")) {
                                progressDialog.dismiss();
                                JSONObject responseObj = response.getJSONObject("response");
                                String message = responseObj.getString("message");
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,message, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                Toast.makeText(MyProductContractActivity.this, "Product Contract Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MyProductContractActivity.this, MyProductContractActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(MyProductContractActivity.this, "Product Contract not added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
    private void editPoultryProdContractRequest() {
        //user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        job_id = openRequestModels.get(position).getId();
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostContractsApi service = retrofit.create(PostContractsApi.class);
        Call<JsonElement> callRetrofit = null;
            callRetrofit = service.editPoultryProdContract(job_id,PoultryEditToDate);

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
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            linear_pro_contracts.setVisibility(View.VISIBLE);
                                            ll_addprod_contracts.setVisibility(View.GONE);
                                            ll_poultry_add_contract_product.setVisibility(View.GONE);
                                            ll_poultry_edit_contract_product.setVisibility(View.GONE);

                                            /*Intent i = new Intent(MyProductContractActivity.this,MyProductContractActivity.class);
                                            startActivity(i);*/
                                            Toast.makeText(MyProductContractActivity.this, message, Toast.LENGTH_SHORT).show();
                                            // finish();
                                            progressDialog.dismiss();

                                        } else {
                                            Toast.makeText(MyProductContractActivity.this, message, Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    } else {
                        // Toast.makeText(SignInActivity.this, "Service not responding", Toast.LENGTH_SHORT).show();
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
                //   // Toast.makeText(StoreMyProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getExistingAddresses() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.productDetails(user_id, pro_id);
        callRetrofit = service.getExistingAddress(user_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if (progressDialog.isShowing())
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
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("existing");
                                            getAddressModels = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                AddressId = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String name = jsonObject.getString("name");
                                                String address1 = jsonObject.getString("address1");
                                                String zipcode = jsonObject.getString("zipcode");
                                                String country_name = jsonObject.getString("country_name");
                                                String state = jsonObject.getString("state");
                                                String city_name = jsonObject.getString("city_name");

                                                GetAddressModel getAddressModel = new GetAddressModel();
                                                getAddressModel.setId(AddressId);
                                                getAddressModel.setUser_id(user_id);
                                                getAddressModel.setName(name);
                                                getAddressModel.setAddress1(address1);
                                                getAddressModel.setZipcode(zipcode);
                                                getAddressModel.setCountry_name(country_name);
                                                getAddressModel.setState(state);
                                                getAddressModel.setCity_name(city_name);
                                                getAddressModels.add(getAddressModel);

                                            }
                                            if (getAddressModels != null) {
                                                saved_address_text.setVisibility(View.GONE);
                                                existingAddressAdapter = new ExistingAddressAdapterContract(MyProductContractActivity.this,getAddressModels,mSelectedItemsListIds);
                                                recyclerview_saved_addresses.setAdapter(existingAddressAdapter);
                                                existingAddressAdapter.notifyDataSetChanged();
                                            }else{
                                                saved_address_text.setVisibility(View.VISIBLE);
                                            }

                                        } else {
                                            saved_address_text.setVisibility(View.VISIBLE);

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
/*
    public void saveExistingAddress() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.sentAddress(user_id,user_id,"408","500038","5.76765656","8.765645",AddressId);
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
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            progressDialog.dismiss();
                                            customdialog.dismiss();
                                            */
/*Intent i = new Intent(ProductDetailsActivity.this,ProductDetailsActivity.class);
                                            startActivity(i);*//*

                                            //drop_location.setText(radioText);
                                            Toast.makeText(MyProductContractActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MyProductContractActivity.this, message, Toast.LENGTH_SHORT).show();
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
*/
    @Override
    protected void onResume() {
        super.onResume();
        //   drop_location.setText(radioText);
        if(address_txt_map_dialog != null){
            address_txt_map_dialog.setText(Product_Detail_Address_Map);
        }
    }
    private void getCountryList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCountries();

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
                                    JSONArray jsonArray = root.getJSONArray("country");
                                    countryList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        countryList.add(jsonObject.getString("country_name"));
                                        idList.add(jsonObject.getString("CountryCode"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(true, countryList, idList);
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
    private void showAlertDialog(final boolean isCountry, final List<String> countryList,
                                 final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyProductContractActivity.this);
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
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
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
                    // When user changed the Text
                    // adapter.getFilter().filter(cs);
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyProductContractActivity.this,
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
            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String itemValue = selectedCountryList.get(position);
                    if (isCountry) {
                        //String itemValue = selectedCountryList.get(position);
                        spinner_country.setText(itemValue);
                        countryId = selectedCountryIdList.get(position);
                        address_state.setText("");
                        address_state.setHint("Choose State");
                        //  countryId = idList.get(position);
                        alertDialog.dismiss();
                    } else {
                       /* sp_state.setText(itemValue);
                        stateId = idList.get(position);*/
                        address_state.setText(itemValue);
                        stateId = selectedCountryIdList.get(position);
                        alertDialog.dismiss();
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
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyProductContractActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);
            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose City");
            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
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
                    // adapter.getFilter().filter(cs);
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
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyProductContractActivity.this,
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
            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String itemValue = selectedCountryList.get(position);
                    address_town.setText(itemValue);
                    cityId = selectedCountryIdList.get(position);
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
