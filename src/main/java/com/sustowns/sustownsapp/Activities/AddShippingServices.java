package com.sustowns.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.Models.ImageModel;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Adapters.ImagesAdapter;
import com.sustowns.sustownsapp.Adapters.ServiceUnitListAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.TransportApi;
import com.sustowns.sustownsapp.Models.Data_Model;
import com.sustowns.sustownsapp.Models.ServiceUnitModel;
import com.sustowns.sustownsapp.Models.TransportGetService;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sustowns.sustownsapp.Activities.FileUtils.getPath;

public class AddShippingServices extends AppCompatActivity {
    public static String ServiceRadiusIn = "",ServiceExtRadius = "",P2pSource = "",P2pDes = "";
    public static final int RESULT_OK = -1;
    private static final int PICKFILE_RESULT_CODE = 3;
    final int CAMERA_CAPTURE = 2;
    final int PICK_IMAGE = 1;
    ImageView backarrow, three_dots_icon;
    Button save_service, liceneceImg, choose_vehicle_docs, choose_permit_img;
    PreferenceUtils preferenceUtils;
    String user_id, transportStr, vehicleStr,SubCatId, profileString, SelectedButton,categoryStr,subCategoryStr,CatId;
    LinearLayout ll_buyer_network, ll_custom_invoice, ll_customizations, ll_my_products, ll_shipping_type;
    RecyclerView recycler_view_myservices, multi_licence_img_recylerview, multi_permit_img_recylerview;
    String[] service = {"Service1", "Service2", "Service3", "Service4"};
    LinearLayout ll_crate_box, ll_add_service, ll_shipping_full_load, ll_prod_list, ll_choose_subcategory, ll_choose_category, ll_contracts;
    ProgressDialog progressDialog;
    int position;
    ArrayList<TransportGetService> transportGetServices;
    TextView shipping_transport_type, shipping_vehical_type, choose_category,title_shipping,choose_sub_category, document_name, from_date_text, to_date_text;
    RelativeLayout rl_capture, rl_gallery;
    List<String> transportList = new ArrayList<>();
    List<String> vehicleList = new ArrayList<>();
    List<String> categoriesList = new ArrayList<>(), categorySelectedList = new ArrayList<>(), loadArrayList = new ArrayList<>();
    List<String> subcategoriesList = new ArrayList<>(), subCategorySelectedList = new ArrayList<>(), actionArrayList = new ArrayList<>();
    List<Integer> categorySelectedPosition = new ArrayList<>(), subCategorySelectedPosition = new ArrayList<>();
    List<ServiceUnitModel> unitList = new ArrayList<>(), unitDisplayList = new ArrayList<>(), unitFullDisplayList = new ArrayList<>();
    List<String> categoryIds = new ArrayList<>();
    boolean isUpdate;
    AlertDialog alertDialog,alertDialog1;
    Helper helper;
    String transportId = "", vehicleId = "",VehicleIdNumber, ServiceName = "", VehicleId = "", MinLoad = "", MaxLoad = "", categoryId = "", subCateId = "";
    EditText service_name_edit, service_vehicle_id, min_load_service, max_load_service, length_service, width_service, height_service;
    EditText tax_service, discount_service;
    Spinner spinner_body_type,choose_subcategory;
    RecyclerView spinner_choose_subcategory, recyclerview_category;
    String loadValue = "", actionValue = "", imagePath, name_service_st, serviceId, transport_st, vehicle_st, bodyTypeStr, Lendth, Width;
    Button from_date_ser, to_date_ser, close_category;
    String[] BodyType = {"Choose Type", "Open", "Closed"};
    String[] BodyType1 = { "Open", "Closed"};
    String[] BodyType2 = {"Closed","Open"};
    Button shipping_add_partial, add_full_unit;
    RecyclerView shippingpartial_recyclerview, shippingfull_recyclerview;
    ServiceUnitListAdapter serviceUnitListAdapter;
    LinearLayout ll_shipping_partial, ll_shipping_full;
    CheckBox shipping_partial_checkbox, shipping_full_checkbox, radius_radiobtn, p2p_radiobtn;
    String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    int count = 0,countRadius = 0,countPartial = 0;
    Uri uri,returnUri;
    byte[] bytes;
    String ret,fromDate,toDate,UpdateStr,nameStr, fileString="", finalString, TitleStr,filename="";
    ArrayList<Data_Model> arrayList = new ArrayList<>();
    RecyclerView images_recyclerView, permit_images_recyclerView;
    ImageButton confirm_add_icon, confirm_permit_add_icon;
    List<ImageModel> imagesList = new ArrayList<>();
    List<String> fileList = new ArrayList<>();
    List<ImageModel> permitimagesList = new ArrayList<>();
    List<String> permitfileList = new ArrayList<>();
    List<String> documentArrayList = new ArrayList<>();
    Bitmap bitmap;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    String fromDateStr="", toDateStr="";
    String Category,DiscountStr ="",CheckedStr,SubCategory,CatIdStr,SubCatIdStr,VehicleIdStr,MinLoadStr,MaxLoadStr,ContainerType,Length,WidthStr,Height,TransportTax,TransportDis,ServiceRadius="",ServiceExtendRadius="",PointSourceLoc="",PointDesLoc="",LoadType;

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
        setContentView(R.layout.layout_shipping_type);
        try {
            initializeValues();
            initializeUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initializeValues() {
        serviceId = getIntent().getStringExtra("ServiceId");
        UpdateStr = getIntent().getStringExtra("Update");
        if(UpdateStr.equalsIgnoreCase("1")){
            TitleStr = "Edit Service";
        }else {
            TitleStr = "Add Service";
        }
        nameStr = getIntent().getStringExtra("Name");
        transportStr = getIntent().getStringExtra("TranportType");
        vehicleStr = getIntent().getStringExtra("VehicleType");
        fromDateStr = getIntent().getStringExtra("FromDate");
        toDateStr = getIntent().getStringExtra("ToDate");
        Category = getIntent().getStringExtra("Category");
        CatIdStr = getIntent().getStringExtra("CatId");
        SubCategory = getIntent().getStringExtra("SubCategory");
        SubCatIdStr = getIntent().getStringExtra("SubCatId");
        vehicleId = getIntent().getStringExtra("VehicleId");
        VehicleIdNumber = getIntent().getStringExtra("VehicleIdNumber");
        transportId = getIntent().getStringExtra("TransId");
        MinLoadStr = getIntent().getStringExtra("MinLoad");
        MaxLoadStr = getIntent().getStringExtra("MaxLoad");
        ContainerType = getIntent().getStringExtra("ContainerType");
        Length = getIntent().getStringExtra("Length");
        WidthStr = getIntent().getStringExtra("Width");
        Height = getIntent().getStringExtra("Height");
        TransportTax = getIntent().getStringExtra("TransportTax");
        TransportDis = getIntent().getStringExtra("TransportDis");
        ServiceRadius = getIntent().getStringExtra("ServiceRadius");
        ServiceExtendRadius = getIntent().getStringExtra("ServiceExtendRadius");
        PointSourceLoc = getIntent().getStringExtra("PointSourceLoc");
        PointDesLoc = getIntent().getStringExtra("PointDesLoc");
        LoadType = getIntent().getStringExtra("LoadType");
        isUpdate = false;
        helper = new Helper(AddShippingServices.this);
        preferenceUtils = new PreferenceUtils(AddShippingServices.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        checkPermissions();
    }
    private void initializeUI() {
        images_recyclerView = findViewById(R.id.images_recyclerView_shipping);
        images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        images_recyclerView.setLayoutManager(linearLayoutManager);
        images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        permit_images_recyclerView = findViewById(R.id.permit_images_recyclerView_shipping);
        permit_images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutPermit = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        permit_images_recyclerView.setLayoutManager(linearLayoutPermit);
        permit_images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        backarrow = (ImageView) findViewById(R.id.backarrow1);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               /* if (TitleStr.equalsIgnoreCase("Add Service") || TitleStr.equalsIgnoreCase("Edit Service")) {
                    Intent i = new Intent(AddShippingServices.this, StoreMyProductsActivity.class);
                    i.putExtra("Customizations","1");
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(AddShippingServices.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }*/
            }
        });
        ll_shipping_partial = (LinearLayout)findViewById(R.id.ll_shipping_partial);
        ll_shipping_full = (LinearLayout)findViewById(R.id.ll_shipping_full);
        shipping_partial_checkbox = findViewById(R.id.shipping_partial_checkbox);
        shipping_partial_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(TitleStr.equalsIgnoreCase("Add Service")) {
                        loadValue = "Partial";
                        loadArrayList.add(loadValue);
                        showPartialRecycelrview(true);
                    }else if(TitleStr.equalsIgnoreCase("Edit Service")){
                        if(countPartial > 0){
                            loadValue = "Partial";
                            loadArrayList.add(loadValue);
                            showPartialRecycelrview(true);
                        }else{

                        }
                        countPartial = countPartial+1;
                    }
                } else {
                    ll_shipping_partial.setVisibility(View.GONE);
                }
            }
        });
        shipping_full_checkbox = findViewById(R.id.shipping_full_checkbox);
        shipping_full_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(TitleStr.equalsIgnoreCase("Add Service")) {
                        loadValue = "Full_load";
                        loadArrayList.add(loadValue);
                        showPartialRecycelrview(false);
                    }else if(TitleStr.equalsIgnoreCase("Edit Service")){
                        if(countPartial > 0){
                            loadValue = "Full_load";
                            loadArrayList.add(loadValue);
                            showPartialRecycelrview(false);
                        }else{

                        }
                        countPartial = countPartial+1;
                    }
                }
                else {
                    ll_shipping_full.setVisibility(View.GONE);
                }
            }
        });
        confirm_add_icon = findViewById(R.id.confirm_add_icon_shipping);
        confirm_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton = "Licence";
                count = count + 1;
                final Dialog customdialog = new Dialog(AddShippingServices.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);

                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchServiceCamera(AddShippingServices.this, count, true);
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchLicenceGallery(AddShippingServices.this, count, true);
                        customdialog.dismiss();
                    }
                });

                customdialog.show();
//                imagePath = FileUtils.launchServiceCamera(ServiceManagementActivity.this, count, true);
            }
        });
        confirm_permit_add_icon = findViewById(R.id.confirm_permit_add_icon_shipping);
        confirm_permit_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton = "Permit";
                count = count + 1;
                final Dialog customdialog = new Dialog(AddShippingServices.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);
                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchPermitCamera(AddShippingServices.this, count, true);
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchPermitGallery(AddShippingServices.this, count, true);
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        shippingpartial_recyclerview = findViewById(R.id.shippingpartial_recyclerview);
        LinearLayoutManager linearLayoutManagerq1 = new LinearLayoutManager(AddShippingServices.this, LinearLayoutManager.VERTICAL, false);
        shippingpartial_recyclerview.setLayoutManager(linearLayoutManagerq1);
        shippingpartial_recyclerview.setHasFixedSize(true);
        shippingpartial_recyclerview.setItemAnimator(new DefaultItemAnimator());
        shippingfull_recyclerview = findViewById(R.id.shippingfull_recyclerview);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(AddShippingServices.this, LinearLayoutManager.VERTICAL, false);
        shippingfull_recyclerview.setLayoutManager(linearLayoutManager1);
        shippingfull_recyclerview.setHasFixedSize(true);
        shippingfull_recyclerview.setItemAnimator(new DefaultItemAnimator());
        service_name_edit = (EditText) findViewById(R.id.service_name_edit);
        service_vehicle_id = (EditText) findViewById(R.id.service_vehicle_id);
        min_load_service = (EditText) findViewById(R.id.min_load_service);
        max_load_service = (EditText) findViewById(R.id.max_load_service);
        length_service = (EditText) findViewById(R.id.length_service);
        width_service = (EditText) findViewById(R.id.width_service);
        height_service = (EditText) findViewById(R.id.height_service);
        tax_service = (EditText) findViewById(R.id.tax_service);
        discount_service = (EditText) findViewById(R.id.discount_service);
        title_shipping = (TextView) findViewById(R.id.title_shipping);
        document_name = (TextView) findViewById(R.id.document_name);
        from_date_text = (TextView) findViewById(R.id.from_date_text);
        to_date_text = (TextView) findViewById(R.id.to_date_text);
        from_date_ser = (Button) findViewById(R.id.from_date_ser);
        from_date_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog();

            }
        });
        to_date_ser = (Button) findViewById(R.id.to_date_ser);
        to_date_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog1();
            }
        });
        choose_vehicle_docs = (Button) findViewById(R.id.choose_vehicle_docs);
        choose_vehicle_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getFile = new Intent(Intent.ACTION_GET_CONTENT);
                getFile.setType("*/*");
                startActivityForResult(getFile, PICKFILE_RESULT_CODE);
            }
        });
        shipping_transport_type = (TextView) findViewById(R.id.shipping_transport_type);
        shipping_transport_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Transport API
                getTransportTypeList();
            }
        });
        shipping_vehical_type = (TextView) findViewById(R.id.shipping_vehical_type);
        shipping_vehical_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Vehicle API
                getVehicleTypeList();
            }
        });
        ll_choose_subcategory = (LinearLayout) findViewById(R.id.ll_choose_subcategory);
        choose_category = (TextView) findViewById(R.id.choose_category);
        choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesList = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                //In response data
                categoriesList.add("Live Stock");
                idList.add("144");
                showAlertDialog1(true, categoriesList, idList);
            }
        });
        choose_sub_category = (TextView) findViewById(R.id.choose_sub_category);
        choose_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJsonObject(true);
            }
        });
        CheckedStr = "2";
        radius_radiobtn = (CheckBox) findViewById(R.id.radius_radiobtn);
        p2p_radiobtn = (CheckBox) findViewById(R.id.p2p_radiobtn);
        radius_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(TitleStr.equalsIgnoreCase("Add Service")){
                        actionValue = "Radius";
                        actionArrayList.add(actionValue);
                        Intent i = new Intent(AddShippingServices.this, MapsActivity.class);
                        i.putExtra("activity", "service");
                        i.putExtra("type", "radius");
                        i.putExtra("ServiceRadius", "");
                        i.putExtra("ServiceExtendRadius", "");
                        startActivity(i);
                    }else if(TitleStr.equalsIgnoreCase("Edit Service")) {
                        if (CheckedStr.equalsIgnoreCase("0")) {
                            if(countRadius > 0){
                                actionValue = "Radius";
                                actionArrayList.add(actionValue);
                                Intent i = new Intent(AddShippingServices.this, MapsActivity.class);
                                i.putExtra("activity", "service");
                                i.putExtra("type", "radius");
                                i.putExtra("ServiceRadius", ServiceRadius);
                                i.putExtra("ServiceExtendRadius", ServiceExtendRadius);
                                startActivity(i);
                            }else{
                            }
                            countRadius = countRadius+1;
                        } else if (CheckedStr.equalsIgnoreCase("1")) {

                        }
                    }
                }
            }
        });
        p2p_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(TitleStr.equalsIgnoreCase("Add Service")){
                        actionValue = "Point to Point";
                        actionArrayList.add(actionValue);
                        Intent intent = new Intent(AddShippingServices.this, MapsActivity.class);
                        intent.putExtra("activity", "service");
                        intent.putExtra("type", "p2p");
                        intent.putExtra("PointSourceLoc", "");
                        intent.putExtra("PointDesLoc", "");
                        startActivity(intent);
                    }else if(TitleStr.equalsIgnoreCase("Edit Service")) {
                        if (CheckedStr.equalsIgnoreCase("0")) {
                            if (countRadius > 0) {
                                actionValue = "Point to Point";
                                actionArrayList.add(actionValue);
                                Intent intent = new Intent(AddShippingServices.this, MapsActivity.class);
                                intent.putExtra("activity", "service");
                                intent.putExtra("type", "p2p");
                                intent.putExtra("PointSourceLoc", PointSourceLoc);
                                intent.putExtra("PointDesLoc", PointDesLoc);
                                startActivity(intent);
                            } else {
                            }
                            countRadius = countRadius+1;
                        }else if (CheckedStr.equalsIgnoreCase("1")) {

                        }
                    }
                }
            }
        });
        save_service = (Button) findViewById(R.id.save_service);
        save_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceName = service_name_edit.getText().toString().trim();
                VehicleId = service_vehicle_id.getText().toString().trim();
                MinLoad = min_load_service.getText().toString().trim();
                MaxLoad = max_load_service.getText().toString().trim();
                Lendth = length_service.getText().toString().trim();
                Width = width_service.getText().toString().trim();
                DiscountStr = discount_service.getText().toString();
                if(!isUpdate){
                    if (transportId.equalsIgnoreCase("") || vehicleId.equalsIgnoreCase("")|| service_name_edit.getText().toString().equalsIgnoreCase("") || loadArrayList.isEmpty() || actionValue.equalsIgnoreCase("") || MinLoad.equalsIgnoreCase("") || MaxLoad.equalsIgnoreCase("") || Lendth.equalsIgnoreCase("")
                            || Width.equalsIgnoreCase("") || height_service.getText().toString().equalsIgnoreCase("") || bodyTypeStr.equalsIgnoreCase("Choose Type")) {
                        Toast.makeText(AddShippingServices.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    }
                    else if (actionValue.equalsIgnoreCase("radius") && MapsActivity.radius_edit.getText().toString().trim().isEmpty()) {
                        Toast.makeText(AddShippingServices.this, "Please select to address", Toast.LENGTH_SHORT).show();
                    } else if (!actionValue.isEmpty() && actionValue.equalsIgnoreCase("Point to Point")
                            && MapsActivity.fromPincode.isEmpty()&& MapsActivity.toPincode.isEmpty()) {
                        Toast.makeText(AddShippingServices.this, "Please select from address", Toast.LENGTH_SHORT).show();
                    }/*else if(unitDisplayList.get(position).getCharge().isEmpty() || unitDisplayList.get(position).getMin_charge().isEmpty()||
                            unitDisplayList.get(position).getMax_load().isEmpty() ||unitDisplayList.get(position).getMin_load().isEmpty()||
                            unitFullDisplayList.get(position).getCharge().isEmpty() ||unitFullDisplayList.get(position).getMax_load().isEmpty() ||unitDisplayList.get(position).getMin_charge().isEmpty()){
                        Toast.makeText(ServiceManagementActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    }*/
                    else {
                        setAddServiceJsonObject();
                    }
                }else {
                    editServiceJsonObject();
                }
            }
        });
        shipping_add_partial = findViewById(R.id.shipping_add_partial);
        shipping_add_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = unitDisplayList.size();
                if (size < unitList.size()) {
                    for (int i = 0; i < unitList.size(); i++) {
                        if (unitDisplayList.contains(unitList.get(i))) {
                            // Do nothing
                        } else {
                            unitDisplayList.add(unitList.get(i));
                            serviceUnitListAdapter.notifyItemInserted(unitFullDisplayList.size() - 1);
                            shippingpartial_recyclerview.scrollToPosition(unitFullDisplayList.size() - 1);
                            shippingpartial_recyclerview.setVisibility(View.VISIBLE);
                            if (serviceUnitListAdapter != null) {
                                serviceUnitListAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });
        add_full_unit = findViewById(R.id.add_full_unit);
        add_full_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = unitFullDisplayList.size();
                if (size < unitList.size()) {
                    for (int i = 0; i < unitList.size(); i++) {
                        if (unitFullDisplayList.contains(unitList.get(i))) {
                            // Do nothing
                        } else {
                            unitFullDisplayList.add(unitList.get(i));
                            shippingfull_recyclerview.setVisibility(View.VISIBLE);
                            if (serviceUnitListAdapter != null) {
                                serviceUnitListAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });
        spinner_body_type = (Spinner) findViewById(R.id.spinner_body_type);
        final ArrayAdapter bodyType = new ArrayAdapter(AddShippingServices.this, android.R.layout.simple_spinner_item, BodyType);
        bodyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_body_type.setAdapter(bodyType);
        spinner_body_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyTypeStr = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (ContainerType != null || !ContainerType.isEmpty()) {
            if(ContainerType.equalsIgnoreCase("closed")) {
                bodyTypeStr = "open";
                final ArrayAdapter bodyType1 = new ArrayAdapter(AddShippingServices.this, android.R.layout.simple_spinner_item, BodyType1);
                bodyType1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_body_type.setAdapter(bodyType1);
            }else{
                bodyTypeStr = "closed";
                final ArrayAdapter bodyType2 = new ArrayAdapter(AddShippingServices.this, android.R.layout.simple_spinner_item, BodyType2);
                bodyType2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_body_type.setAdapter(bodyType2);
            }
        }
        if(UpdateStr.equalsIgnoreCase("1")){
            ll_choose_subcategory.setVisibility(View.VISIBLE);
            setJsonObject(true);
            save_service.setText("Edit Service");
            TitleStr = "Edit Service";
            title_shipping.setText(TitleStr);
            isUpdate = true;
            if(PointSourceLoc.equalsIgnoreCase("null")||PointDesLoc.equalsIgnoreCase("null")){
                p2p_radiobtn.setChecked(false);
                CheckedStr = "1";
            }else{
                CheckedStr = "0";
                countRadius = 0;
                p2p_radiobtn.setChecked(true);
            }
            if(LoadType.equalsIgnoreCase("Partial")){
                shipping_partial_checkbox.setChecked(true);
                shipping_full_checkbox.setChecked(false);
                // showPartialRecycelrview(true);
            }else if(LoadType.equalsIgnoreCase("Full_load")){
                shipping_partial_checkbox.setChecked(false);
                shipping_full_checkbox.setChecked(true);
                // showPartialRecycelrview(false);
            } else if(LoadType.equalsIgnoreCase("Partial,Full_load")){
                shipping_partial_checkbox.setChecked(true);
                shipping_full_checkbox.setChecked(true);
                //showPartialRecycelrview(true);
                //showPartialRecycelrview(false);
            }
            if(ServiceRadius.equalsIgnoreCase("null") || ServiceExtendRadius.equalsIgnoreCase("null")){
                radius_radiobtn.setChecked(false);
                CheckedStr = "1";
            }
            CheckedStr = "0";
            countRadius = 0;
            service_name_edit.setText(nameStr);
            shipping_transport_type.setText(transportStr);
            shipping_vehical_type.setText(vehicleStr);
            from_date_ser.setText(fromDateStr);
            to_date_ser.setText(toDateStr);
            service_vehicle_id.setText(VehicleIdNumber);
            min_load_service.setText(MinLoadStr);
            max_load_service.setText(MaxLoadStr);
            choose_category.setText("Live Stock");
            subCategorySelectedList.add(SubCatIdStr);
            categorySelectedList.add("144");
            choose_sub_category.setText(SubCategory);
            setJsonObject(true);
            width_service.setText(WidthStr);
            length_service.setText(Length);
            height_service.setText(Height);
            tax_service.setText(TransportTax);
            discount_service.setText(TransportDis);
            radius_radiobtn.setChecked(true);
        }
        ll_choose_subcategory.setVisibility(View.VISIBLE);
        // spinner_body_type.setSelection(ContainerType);
        //editProductShipping();
        //  }
        ll_contracts = (LinearLayout) findViewById(R.id.ll_contracts);
        ll_my_products = (LinearLayout) findViewById(R.id.ll_my_products);
        ll_customizations = (LinearLayout) findViewById(R.id.ll_customizations);
        ll_prod_list = (LinearLayout) findViewById(R.id.ll_prod_list);
        ll_buyer_network = findViewById(R.id.ll_buyer_network);
        ll_add_service = (LinearLayout) findViewById(R.id.ll_add_service);


        three_dots_icon = findViewById(R.id.three_dots_icon);
        recycler_view_myservices = (RecyclerView) findViewById(R.id.recycler_view_myservices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddShippingServices.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_myservices.setLayoutManager(layoutManager);
        three_dots_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddShippingServices.this, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.service_customizations_list, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.buyer_network) {
                            ll_buyer_network.setVisibility(View.VISIBLE);
                            ll_custom_invoice.setVisibility(View.GONE);


                        } else if (id == R.id.custom_invoice) {
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        });
        service_vehicle_id.setFocusableInTouchMode(false);
        service_vehicle_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                service_vehicle_id.setFocusableInTouchMode(true);
                service_vehicle_id.requestFocus();
                return false;
            }
        });
        service_name_edit.setFocusableInTouchMode(false);
        service_name_edit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                service_name_edit.setFocusableInTouchMode(true);
                service_name_edit.requestFocus();
                return false;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (new File(imagePath).exists()) {
                    helper.showLoader(AddShippingServices.this, "Loading image", "Please wait...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imagePath = FileUtils.processImage(imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        helper.hideLoader();
                                        Bitmap bitmapImage = null;
                                        try {
                                            bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(imagePath)));
                                            profileString = getEncodedImage(bitmapImage);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (SelectedButton.equalsIgnoreCase("Licence")) {
                                            ImageModel imageModel = new ImageModel(profileString,"");
                                            imagesList.add(imageModel);
                                           // imagesList.add(profileString);
                                            // fileList.add(imagePath);
                                            setUpRecyclerView(imagePath);
                                        } else {
                                            ImageModel imageModel = new ImageModel(profileString,"");
                                            permitimagesList.add(imageModel);
                                            // permitfileList.add(imagePath);
                                            setUpRecyclerView(imagePath);
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    String errorMessage = "Image not found!";
                    helper.showToast(AddShippingServices.this, errorMessage);
                }
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
                            String path = getRealPathFromURI(AddShippingServices.this, uri);
                        }
                    }
                }
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
                            document_name.setText(filename);
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
                        documentArrayList.add(fileString);

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
//                    profile_img.setImageURI(picUri);
            if (SelectedButton.equalsIgnoreCase("Licence")) {
                ImageModel imageModel = new ImageModel(profileString,"");
                imagesList.add(imageModel);
                setUpRecyclerView(imagePath);
            } else {
                ImageModel imageModel = new ImageModel(profileString,"");
                permitimagesList.add(imageModel);
                setUpRecyclerView(imagePath);
            }
            // licence_image.setImageBitmap(bitmapImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
//                profile_img.setImageURI(Uri.parse(imagePath));
        Log.d("Selected Image path: ", imagePath);
        c.close();
        return "";
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
    public void setJsonObject(boolean isAlert) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray Product_ids = new JSONArray();
            for (int i = 0; i < categorySelectedList.size(); i++) {
                JSONObject CatidObj = new JSONObject();
                CatidObj.put("Cat_id", "144");
                Product_ids.put(CatidObj);
            }
            jsonObj.put("Product_ids", Product_ids);
            Log.e("Product_ids", "" + jsonObj);

            androidNetworking(jsonObj,isAlert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworking(JSONObject jsonObject, final boolean isAlert) {
        // progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/get_subcategory")
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
                                // progressDialog.dismiss();
                                JSONObject responseObj = response.getJSONObject("response");
                                JSONObject dataObj = responseObj.getJSONObject("data");
                                JSONArray getunitArray = dataObj.getJSONArray("getunit");
                                JSONArray subcategorytypeArray = dataObj.getJSONArray("subcategorytype");
                                subcategoriesList = new ArrayList<>();
                                unitList = new ArrayList<>();
                                List<String> subcategoryIds = new ArrayList<>();
                                for (int i = 0; i < subcategorytypeArray.length(); i++) {
                                    JSONObject catObj = subcategorytypeArray.getJSONObject(i);
                                    JSONArray SubCatArray = catObj.getJSONArray("sub_cat");
                                    for (int i2 = 0; i2 < SubCatArray.length(); i2++) {
                                        JSONObject subcatObj = SubCatArray.getJSONObject(i2);
                                    }
                                    subcategoryIds.add(catObj.getString("id"));
                                    subcategoriesList.add(catObj.getString("title"));

                                }
                                for (int j = 0; j < getunitArray.length(); j++) {
                                    JSONObject unitObj = getunitArray.getJSONObject(j);

                                    ServiceUnitModel serviceUnitModel = new ServiceUnitModel(
                                            unitObj.getString("um_id"),
                                            unitObj.getString("unit_name"),
                                            "", "", "", ""
                                    );

                                    unitList.add(serviceUnitModel);
                                }
                                if (isAlert)
                                    if(UpdateStr.equalsIgnoreCase("1")){
                                        if(LoadType.equalsIgnoreCase("Partial")){
                                            shipping_partial_checkbox.setChecked(true);
                                            shipping_full_checkbox.setChecked(false);
                                            showPartialRecycelrview(true);
                                        }else if(LoadType.equalsIgnoreCase("Full_load")){
                                            shipping_partial_checkbox.setChecked(false);
                                            shipping_full_checkbox.setChecked(true);
                                            showPartialRecycelrview(false);
                                        } else if(LoadType.equalsIgnoreCase("Partial,Full_load")){
                                            shipping_partial_checkbox.setChecked(true);
                                            shipping_full_checkbox.setChecked(true);
                                            showPartialRecycelrview(true);
                                            showPartialRecycelrview(false);
                                        }
                                    }else {
                                        showAlertDialog1(false, subcategoriesList, subcategoryIds);
                                    }
                            } else {
                                // progressDialog.dismiss();
                                Toast.makeText(AddShippingServices.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // progressDialog.dismiss();
                            Toast.makeText(AddShippingServices.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        // progressDialog.dismiss();
                    }
                });

    }

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fromDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                from_date_ser.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
                toDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                to_date_ser.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    private void showPartialRecycelrview(boolean isPartial) {
        if (unitList.size() > 0) {
            if (isPartial) {
                ll_shipping_partial.setVisibility(View.VISIBLE);
                unitDisplayList.add(unitList.get(0));
                shippingpartial_recyclerview.setVisibility(View.VISIBLE);
                serviceUnitListAdapter = new ServiceUnitListAdapter(AddShippingServices.this, unitDisplayList, unitList, isPartial,"1");
                shippingpartial_recyclerview.setAdapter(serviceUnitListAdapter);
            } else {
                ll_shipping_full.setVisibility(View.VISIBLE);
                unitFullDisplayList.add(unitList.get(0));
                shippingfull_recyclerview.setVisibility(View.VISIBLE);
                serviceUnitListAdapter = new ServiceUnitListAdapter(AddShippingServices.this, unitFullDisplayList, unitList, isPartial,"1");
                shippingfull_recyclerview.setAdapter(serviceUnitListAdapter);
            }
        }
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(AddShippingServices.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getTransportTypeList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportType();

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
                                    JSONArray jsonArray = root.getJSONArray("transport");
                                    transportList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        transportList.add(jsonObject.getString("transport_type"));
                                        idList.add(jsonObject.getString("id"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(true, transportList, idList);
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
    private void getVehicleTypeList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getVehicleType(transportId);
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
                                    JSONArray jsonArray = root.getJSONArray("vehicletype");
                                    vehicleList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        idList.add(jsonObject.getString("id"));
                                        vehicleList.add(jsonObject.getString("vehicle_type"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(false, vehicleList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
/*
                        if (response.body().toString() != null) {
                            vehicleList = new ArrayList<>();
                            List<String> idList = new ArrayList<>();

                            //In response data
                            vehicleList.add("Auto");
                            idList.add("1");
                            vehicleList.add("Pickup/Ace");
                            idList.add("2");
                            vehicleList.add("Truck/DCM");
                            idList.add("3");

                            progressDialog.dismiss();
                            showAlertDialog(false, vehicleList, idList);
                        }
*/
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
    private void showAlertDialog ( final boolean isTransport, final List<String> transportList,
                                   final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddShippingServices.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            if (isTransport)
                title.setText("Choose Transport Type");
            else
                title.setText("Choose Vehicle Type");

            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
            alertDialog = dialogBuilder.create();
            if (transportList.size() == 0) {
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
                    R.layout.simple_list_item, R.id.list_item_txt, transportList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);

            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String itemValue = transportList.get(position);
                    if (isTransport) {
                        shipping_transport_type.setText(itemValue);
                        shipping_vehical_type.setText("");
                        shipping_vehical_type.setHint("Choose Vehicle Type");
                        transportId = idList.get(position);
                        alertDialog.dismiss();
                    } else {
                        shipping_vehical_type.setText(itemValue);
                        vehicleId = idList.get(position);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlertDialog1 ( final boolean isCategory, final List<String> categoryList,
                                    final List<String> categoryIdList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddShippingServices.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView1 = inflater.inflate(R.layout.custom_list_category, null);
            dialogBuilder.setView(dialogView1);

            TextView title = (TextView) dialogView1.findViewById(R.id.customDialogTitle);
            Button submitCat_btn = (Button) dialogView1.findViewById(R.id.submitCat_btn);
            if (isCategory)
                title.setText("Choose Category");
            else
                title.setText("Choose SubCategory");

            final ListView categoryListView = (ListView) dialogView1.findViewById(R.id.categoryList);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView1.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
            alertDialog1 = dialogBuilder.create();
            if (categoryList.size() == 0) {
                if (alertDialog1 != null)
                    alertDialog1.dismiss();
                Toast.makeText(AddShippingServices.this, "No Items Available", Toast.LENGTH_SHORT).show();
            }
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog1.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice, categoryList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            if (isCategory) {
                if (categorySelectedPosition.size() > 0) {
                    for (int pos = 0; pos < categorySelectedPosition.size(); pos++) {
                        categoryListView.setItemChecked(categorySelectedPosition.get(pos), true);
                    }
                }
            } else {
            }
            submitCat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_choose_subcategory.setVisibility(View.VISIBLE);
                    alertDialog1.dismiss();
                    String itemValue = categoryList.get(position);
                    if (isCategory) {
                        SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                        ArrayList<String> selectedItems = new ArrayList<String>();
                        categorySelectedPosition = new ArrayList<>();
                        categorySelectedList = new ArrayList<>();
                        for (int i = 0; i < checked.size(); i++) {
                            // Item position in adapter
                            int position = checked.keyAt(i);
                            // Add sport if it is checked i.e.) == TRUE!
                            if (checked.valueAt(i)) {
                                selectedItems.add(adapter.getItem(position));
                                categorySelectedList.add("144");
                                categorySelectedPosition.add(position);
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
                            choose_category.setText("Live Stock");
                            choose_sub_category.setText("");
                            choose_sub_category.setHint("Choose Sub Category");
                            alertDialog1.dismiss();
                            setJsonObject(false);
                        } else {
                            Toast.makeText(AddShippingServices.this, "Please select one category!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                        ArrayList<String> selectedItems = new ArrayList<String>();
                        subCategorySelectedPosition = new ArrayList<>();
                        subCategorySelectedList = new ArrayList<>();
                        for (int i = 0; i < checked.size(); i++) {
                            // Item position in adapter
                            int position = checked.keyAt(i);
                            // Add sport if it is checked i.e.) == TRUE!
                            if (checked.valueAt(i)) {
                                selectedItems.add(adapter.getItem(position));
                                subCategorySelectedList.add(categoryIdList.get(position));
                                subCategorySelectedPosition.add(position);
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
                            choose_sub_category.setText(stringBuilder.toString());
                            alertDialog1.dismiss();
                        } else {
                            Toast.makeText(AddShippingServices.this, "Please select atleast one subcategory!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            alertDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Add Shipping Service API
    public void setAddServiceJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("service", service_name_edit.getText().toString());
            jsonObj.put("prd_userid", user_id);
            jsonObj.put("prd_bussid", "");
            jsonObj.put("vehicle_id", service_vehicle_id.getText().toString());
            jsonObj.put("trans", transportId);
            jsonObj.put("vehicle", vehicleId);
            jsonObj.put("minimumload", min_load_service.getText().toString());
            jsonObj.put("maximumload", max_load_service.getText().toString());
            jsonObj.put("containertype", bodyTypeStr);
            jsonObj.put("vehicle_length", length_service.getText().toString());
            jsonObj.put("vehicle_width", width_service.getText().toString());
            jsonObj.put("vehicle_height", height_service.getText().toString());
            jsonObj.put("taxfield", tax_service.getText().toString());
            jsonObj.put("discountfild", DiscountStr);
            if(fromDateStr != null || toDateStr != null) {
                jsonObj.put("fromdate", fromDateStr);
                jsonObj.put("todate", toDateStr);
            }else{
                jsonObj.put("fromdate", "");
                jsonObj.put("todate", "");
            }
// subcategory Array
            JSONArray subcategoryArray = new JSONArray();
            for (int i = 0; i < subCategorySelectedList.size(); i++) {
                JSONObject SubCatidObj = new JSONObject();
                SubCatidObj.put("subcat_name", subCategorySelectedList.get(i));
                subcategoryArray.put(SubCatidObj);
            }
            jsonObj.put("subcategory", subcategoryArray);
            Log.e("subcategory", "" + jsonObj);
// load Array
            JSONArray loadArray = new JSONArray();
            for (int i1 = 0; i1 < loadArrayList.size(); i1++) {
                JSONObject loadObj = new JSONObject();
                loadObj.put("load_type", loadArrayList.get(i1));
                loadArray.put(loadObj);
            }
            jsonObj.put("load", loadArray);
            Log.e("load", "" + jsonObj);
//  unit_partial Array
            JSONArray unit_partialArray = new JSONArray();
            for (int j = 0; j < unitDisplayList.size(); j++) {
                JSONObject unitPartialObj = new JSONObject();
                unitPartialObj.put("unit_id", unitDisplayList.get(j).getUnit_id());
                unitPartialObj.put("charge", unitDisplayList.get(j).getCharge());
                unitPartialObj.put("min_charge", unitDisplayList.get(j).getMin_charge());
                unitPartialObj.put("min_load", unitDisplayList.get(j).getMin_load());
                unitPartialObj.put("max_load", unitDisplayList.get(j).getMax_load());
                unit_partialArray.put(unitPartialObj);
            }
            jsonObj.put("unit_partial", unit_partialArray);
            Log.e("unit_partial", "" + jsonObj);
            // unit_full Array
            JSONArray unit_fullArray = new JSONArray();
            for (int j1 = 0; j1 < unitFullDisplayList.size(); j1++) {
                JSONObject unitFullObj = new JSONObject();
                unitFullObj.put("unit_id", unitFullDisplayList.get(j1).getUnit_id());
                unitFullObj.put("charge", unitFullDisplayList.get(j1).getCharge());
                unitFullObj.put("min_charge", unitFullDisplayList.get(j1).getMin_charge());
                unitFullObj.put("max_load", unitFullDisplayList.get(j1).getMax_load());
                unit_fullArray.put(unitFullObj);
            }
            jsonObj.put("unit_full", unit_fullArray);
            Log.e("unit_full", "" + jsonObj);
// action Array
            JSONArray actionArray = new JSONArray();
            for (int k = 0; k < actionArrayList.size(); k++) {
                JSONObject actionObj = new JSONObject();
                actionObj.put("action_type", actionArrayList.get(k));
                actionArray.put(actionObj);
            }
            jsonObj.put("action", actionArray);
            Log.e("action", "" + jsonObj);
            jsonObj.put("country", "");
            jsonObj.put("state", "");
            jsonObj.put("city", "");
            jsonObj.put("startingpoint", "");
            jsonObj.put("endingpoint", "");
            jsonObj.put("serv", "");
            jsonObj.put("radi_perkm", MapsActivity.radius_edit.getText().toString());
            jsonObj.put("radi_exp", MapsActivity.extradius_edit.getText().toString());
            jsonObj.put("service_pincode", MapsActivity.fromPincode);
            jsonObj.put("service_pincode_test", MapsActivity.toPincode);
            jsonObj.put("trans_ser_latitude", String.valueOf(MapsActivity.lat));
            jsonObj.put("trans_ser_langitude", String.valueOf(MapsActivity.lon));
            jsonObj.put("transport_address", MapsActivity.address);
            jsonObj.put("legal_address", MapsActivity.map_address_edit.getText().toString().trim());
            jsonObj.put("sourcelocation", MapsActivity.from_address_txt.getText().toString().trim());
            jsonObj.put("deslocation", MapsActivity.to_address_edit.getText().toString());
            jsonObj.put("type", "on");
            jsonObj.put("trans_ser_org_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
            jsonObj.put("trans_ser_org_langitude", String.valueOf(MapsActivity.fromLatLon.longitude));
            jsonObj.put("trans_ser_des_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
            jsonObj.put("trans_ser_des_langitude", String.valueOf(MapsActivity.toLatLon.longitude));
            jsonObj.put("originPlaceId", "");
            jsonObj.put("destinationPlaceId", "");
            // image Array
            JSONArray permit_imgArray = new JSONArray();
            for (int k = 0; k < permitimagesList.size(); k++) {
                JSONObject permitObj = new JSONObject();
                String image = permitimagesList.get(k).getImage_base64();
                permitObj.put("image", image.replaceAll("\\\\",""));
                permit_imgArray.put(permitObj);
            }
            jsonObj.put("permit_img", permit_imgArray);
            Log.e("permit_img", "" + jsonObj);
            // licence_img Array
            JSONArray licence_imgArray = new JSONArray();
            for (int k = 0; k < imagesList.size(); k++) {
                JSONObject licenceObj = new JSONObject();
                String licimage = imagesList.get(k).getImage_base64();
                licenceObj.put("image", licimage.replaceAll("\\\\",""));
                licence_imgArray.put(licenceObj);
            }
            jsonObj.put("licence_img", licence_imgArray);
            Log.e("licence_img", "" + jsonObj);
            // vehicle_documents
            JSONArray vehicle_documentsArray = new JSONArray();
            for (int k = 0; k < documentArrayList.size(); k++) {
                JSONObject documentObj = new JSONObject();
                String document = documentArrayList.get(k);
                documentObj.put("file",document.replaceAll("\\\\",""));
                actionArray.put(documentObj);
            }
            jsonObj.put("vehicle_documents", vehicle_documentsArray);
            Log.e("vehicle_documents", "" + jsonObj);

            androidNetworkingAdd(jsonObj);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void androidNetworkingAdd (JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Customizationsserv/add_vendor_service")
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
                                Toast.makeText(AddShippingServices.this, "service added successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddShippingServices.this, StoreMyProductsActivity.class);
                                i.putExtra("Customizations","1");
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddShippingServices.this, "Service not added", Toast.LENGTH_SHORT).show();
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
    public void editServiceJsonObject () {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("old_id", serviceId);
            jsonObj.put("service", service_name_edit.getText().toString());
            jsonObj.put("prd_userid", user_id);
            jsonObj.put("prd_bussid", "");
            jsonObj.put("vehicle_id", service_vehicle_id.getText().toString());
            jsonObj.put("trans", transportId);
            jsonObj.put("vehicle", vehicleId);
            jsonObj.put("minimumload", min_load_service.getText().toString());
            jsonObj.put("maximumload", max_load_service.getText().toString());
            jsonObj.put("containertype", bodyTypeStr);
            jsonObj.put("vehicle_length", length_service.getText().toString());
            jsonObj.put("vehicle_width", width_service.getText().toString());
            jsonObj.put("vehicle_height", height_service.getText().toString());
            jsonObj.put("taxfield", tax_service.getText().toString());
            jsonObj.put("discountfild", DiscountStr);
            if(fromDateStr != null || toDateStr != null) {
                jsonObj.put("fromdate", fromDateStr);
                jsonObj.put("todate", toDateStr);
            }else{
                jsonObj.put("fromdate", "");
                jsonObj.put("todate", "");
            }
// category Array
          /*  JSONArray product_categoryArray = new JSONArray();
            for (int i = 0; i < categorySelectedList.size(); i++) {
                JSONObject CatidObj = new JSONObject();
                CatidObj.put("cat_name", categorySelectedList.get(i));
                product_categoryArray.put(CatidObj);
            }
            jsonObj.put("product_category", product_categoryArray);
            Log.e("product_category", "" + jsonObj);*/
// subcategory Array
            JSONArray subcategoryArray = new JSONArray();
            for (int i = 0; i < subCategorySelectedList.size(); i++) {
                JSONObject SubCatidObj = new JSONObject();
                SubCatidObj.put("subcat_name", subCategorySelectedList.get(i));
                subcategoryArray.put(SubCatidObj);
            }
            jsonObj.put("subcategory", subcategoryArray);
            Log.e("subcategory", "" + jsonObj);
// load Array
            JSONArray loadArray = new JSONArray();
            for (int i1 = 0; i1 < loadArrayList.size(); i1++) {
                JSONObject loadObj = new JSONObject();
                loadObj.put("load_type", loadArrayList.get(i1));
                loadArray.put(loadObj);
            }
            jsonObj.put("load", loadArray);
            Log.e("load", "" + jsonObj);
//  unit_partial Array
            JSONArray unit_partialArray = new JSONArray();
            for (int j = 0; j < unitDisplayList.size(); j++) {
                JSONObject unitPartialObj = new JSONObject();
                unitPartialObj.put("unit_id", unitDisplayList.get(j).getUnit_id());
                unitPartialObj.put("charge", unitDisplayList.get(j).getCharge());
                unitPartialObj.put("min_charge", unitDisplayList.get(j).getMin_charge());
                unitPartialObj.put("min_load", unitDisplayList.get(j).getMin_load());
                unitPartialObj.put("max_load", unitDisplayList.get(j).getMax_load());
                unit_partialArray.put(unitPartialObj);
            }
            jsonObj.put("unit_partial", unit_partialArray);
            Log.e("unit_partial", "" + jsonObj);
            // unit_full Array
            JSONArray unit_fullArray = new JSONArray();
            for (int j1 = 0; j1 < unitFullDisplayList.size(); j1++) {
                JSONObject unitFullObj = new JSONObject();
                unitFullObj.put("unit_id", unitFullDisplayList.get(j1).getUnit_id());
                unitFullObj.put("charge", unitFullDisplayList.get(j1).getCharge());
                unitFullObj.put("min_charge", unitFullDisplayList.get(j1).getMin_charge());
                unitFullObj.put("max_load", unitFullDisplayList.get(j1).getMax_load());
                unit_fullArray.put(unitFullObj);
            }
            jsonObj.put("unit_full", unit_fullArray);
            Log.e("unit_full", "" + jsonObj);
// action Array
            JSONArray actionArray = new JSONArray();
            for (int k = 0; k < actionArrayList.size(); k++) {
                JSONObject actionObj = new JSONObject();
                actionObj.put("action_type", actionArrayList.get(k));
                actionArray.put(actionObj);
            }
            jsonObj.put("action", actionArray);
            Log.e("action", "" + jsonObj);

            jsonObj.put("country", "");
            jsonObj.put("state", "");
            jsonObj.put("city", "");
            jsonObj.put("startingpoint", "");
            jsonObj.put("endingpoint", "");
            jsonObj.put("serv", "");
            if( ServiceRadiusIn.equalsIgnoreCase("")){
                jsonObj.put("radi_perkm", ServiceRadius);
                jsonObj.put("radi_exp", ServiceExtendRadius);
            }else{
                jsonObj.put("radi_perkm",ServiceRadiusIn);
                jsonObj.put("radi_exp",ServiceExtRadius);
            }
            jsonObj.put("service_pincode", MapsActivity.fromPincode);
            jsonObj.put("service_pincode_test", MapsActivity.toPincode);
            jsonObj.put("trans_ser_latitude", String.valueOf(MapsActivity.lat));
            jsonObj.put("trans_ser_langitude", String.valueOf(MapsActivity.lon));
            jsonObj.put("transport_address", "10");
            jsonObj.put("legal_address", "10");
            if(P2pSource.equalsIgnoreCase("")|| P2pSource.isEmpty()||
                    P2pDes.equalsIgnoreCase("")|| P2pDes.isEmpty()){
                jsonObj.put("sourcelocation", PointSourceLoc);
                jsonObj.put("deslocation", PointDesLoc);
            }else{
                jsonObj.put("sourcelocation", P2pSource);
                jsonObj.put("deslocation", P2pDes);
            }

            jsonObj.put("type", "105");
            jsonObj.put("trans_ser_org_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
            jsonObj.put("trans_ser_org_langitude", String.valueOf(MapsActivity.fromLatLon.longitude));
            jsonObj.put("trans_ser_des_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
            jsonObj.put("trans_ser_des_langitude", String.valueOf(MapsActivity.toLatLon.longitude));
            jsonObj.put("originPlaceId", "");
            jsonObj.put("destinationPlaceId", "");

            // image Array
            JSONArray permit_imgArray = new JSONArray();
            for (int k = 0; k < permitimagesList.size(); k++) {
                JSONObject permitObj = new JSONObject();
                String image = permitimagesList.get(k).getImage_base64();
                permitObj.put("image", image.replaceAll("\\\\",""));
                permit_imgArray.put(permitObj);
            }
            jsonObj.put("permit_img", permit_imgArray);
            Log.e("permit_img", "" + jsonObj);

            // licence_img Array
            JSONArray licence_imgArray = new JSONArray();
            for (int k = 0; k < imagesList.size(); k++) {
                JSONObject licenceObj = new JSONObject();
                String licimage = imagesList.get(k).getImage_base64();
                licenceObj.put("image", licimage.replaceAll("\\\\",""));
                licence_imgArray.put(licenceObj);
            }
            jsonObj.put("licence_img", licence_imgArray);
            Log.e("licence_img", "" + jsonObj);

            // vehicle_documents
            JSONArray vehicle_documentsArray = new JSONArray();
            for (int k = 0; k < documentArrayList.size(); k++) {
                JSONObject documentObj = new JSONObject();
                String document = documentArrayList.get(k);
                documentObj.put("file",document.replaceAll("\\\\",""));
                actionArray.put(documentObj);
            }
            jsonObj.put("vehicle_documents", vehicle_documentsArray);
            Log.e("vehicle_documents", "" + jsonObj);

            androidNetworkingEdit(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingEdit(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Customizationsserv/edit_vendor_service")
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
                                Toast.makeText(AddShippingServices.this, "Service Updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddShippingServices.this, StoreMyProductsActivity.class);
                                i.putExtra("Customizations","1");
                                startActivity(i);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddShippingServices.this, "Service not Updated", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }

    private void setUpRecyclerView (String image){
        if (imagesList.size() > 0) {
            if (SelectedButton.equalsIgnoreCase("Licence")) {
                fileList.add(image);
                ImagesAdapter imagesAdapter = new ImagesAdapter(AddShippingServices.this, fileList,imagesList,false,"add");
                images_recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.notifyDataSetChanged();
            } else if (SelectedButton.equalsIgnoreCase("Permit")) {
                permitfileList.add(image);
                ImagesAdapter imagesAdapter = new ImagesAdapter(AddShippingServices.this, permitfileList,imagesList,false,"add");
                permit_images_recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.notifyDataSetChanged();
            }
        } else {
            images_recyclerView.setVisibility(View.GONE);
        }
    }
    public void hidePartialRecyclerview () {
        shippingpartial_recyclerview.setVisibility(View.GONE);
    }
    public void hideFullRecyclerview () {
        shippingfull_recyclerview.setVisibility(View.GONE);
    }
}
