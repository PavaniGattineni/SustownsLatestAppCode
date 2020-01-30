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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
import com.sustowns.sustownsapp.Adapters.ImagesAdapter;
import com.sustowns.sustownsapp.Adapters.MyServicesAdapter;
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
import java.net.FileNameMap;
import java.net.URLConnection;
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
import static com.sustowns.sustownsapp.Activities.FileUtils.getPath;

public class ServiceManagementActivity extends AppCompatActivity {
    public static final int RESULT_OK = -1;
    private static final int PICKFILE_RESULT_CODE = 3;
    final int CAMERA_CAPTURE = 2;
    final int PICK_IMAGE = 1;
    ImageView backarrow, three_dots_icon;
    Button my_Services_btn, customizations_btn, addservice_btn, save_service, liceneceImg, choose_vehicle_docs, choose_permit_img;
    PreferenceUtils preferenceUtils;
    String user_id, transportStr, vehicleStr, profileString, SelectedButton;
    LinearLayout ll_buyer_network, ll_custom_invoice, ll_customizations, ll_my_products, ll_shipping_type;
    RecyclerView recycler_view_myservices, multi_licence_img_recylerview, multi_permit_img_recylerview;
    MyServicesAdapter myServicesAdapter;
    String[] service = {"Service1", "Service2", "Service3", "Service4"};
    LinearLayout ll_crate_box, ll_add_service, ll_full_load, ll_prod_list, ll_choose_subcategory, ll_choose_category, ll_contracts;
    ProgressDialog progressDialog;
    int position;
    ArrayList<TransportGetService> transportGetServices;
    TextView spinner_transport_type, spinner_vehical_type, title_store, choose_category, choose_sub_category, document_name, from_date_text, to_date_text;
    RelativeLayout rl_capture, rl_gallery;
    List<String> categoriesList = new ArrayList<>(), categorySelectedList = new ArrayList<>(), loadArrayList = new ArrayList<>();
    List<String> subcategoriesList = new ArrayList<>(), subCategorySelectedList = new ArrayList<>(), actionArrayList = new ArrayList<>();
    List<Integer> categorySelectedPosition = new ArrayList<>(), subCategorySelectedPosition = new ArrayList<>();
    List<ServiceUnitModel> unitList = new ArrayList<>(), unitDisplayList = new ArrayList<>(), unitFullDisplayList = new ArrayList<>();
    boolean isUpdate, isTransport = false;
    AlertDialog alertDialog;
    Helper helper;
    String transportId = "", vehicleId = "", ServiceName = "", VehicleId = "", MinLoad = "", MaxLoad = "", categoryId = "", subCateId = "";
    EditText service_name_edit, service_vehicle_id, min_load_service, max_load_service, length_service, width_service, height_service;
    EditText tax_service, discount_service;
    Spinner spinner_body_type, spinner_choose_category;
    RecyclerView spinner_choose_subcategory, recyclerview_category;
    String loadValue = "", actionValue = "", imagePath, name_service_st, serviceId, transport_st, vehicle_st, bodyTypeStr, Lendth, Width;
    Button from_date_ser, to_date_ser, close_category;
    String[] BodyType = {"Choose Type", "Open", "Closed"};
    String[] BodyType1 = { "Open", "Closed"};
    String[] BodyType2 = {"Closed","Open"};
    Button add_partial, add_full_unit;
    RecyclerView partial_recyclerview, full_recyclerview;
    ServiceUnitListAdapter serviceUnitListAdapter;
    LinearLayout ll_partial, ll_full;
    CheckBox service_partial_checkbox, service_full_checkbox, radius_radiobtn, p2p_radiobtn;
    public static String ServiceRadiusIn = "",ServiceExtRadius = "",P2pSource = "",P2pDes = "";
    String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    Uri uri,returnUri;
    byte[] fileBytes;
    StringBuilder total = new StringBuilder();
    byte[] bytes;
    ByteArrayOutputStream output;
    String ret, uriString, fileString, finalString, fromDateStr="", toDateStr="", TitleStr="",filename;
    ArrayList<Data_Model> arrayList = new ArrayList<>();
    RecyclerView images_recyclerView, permit_images_recyclerView;
    ImageButton confirm_add_icon, confirm_permit_add_icon;
    int count = 0,countRadius = 0,countPartial = 0;
    List<String> imagesList = new ArrayList<>();
    List<String> fileList = new ArrayList<>();
    List<String> permitimagesList = new ArrayList<>();
    List<String> permitfileList = new ArrayList<>();
    List<String> documentArrayList = new ArrayList<>();
    Bitmap bitmap;
    List<String> imagesEncodedList = new ArrayList<>();
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    String Category,CheckedStr,SubCategory,CatIdStr,SubCatIdStr,VehicleIdStr,MinLoadStr,MaxLoadStr,ContainerType,Length,WidthStr,Height,TransportTax,TransportDis,ServiceRadius="",ServiceExtendRadius="",PointSourceLoc="",PointDesLoc="",LoadType;
    String CategorName="",SubCategoryName="",CategoryId="";
    public static String getEncodedImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encodedImagePatientImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImagePatientImage;
    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("MIME_TYPE_EXT", extension);
        if (extension != null && extension != "") {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //  Log.d("MIME_TYPE", type);
        } else {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            type = fileNameMap.getContentTypeFor(url);
        }
        return type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_service_management);

        try {
            initializeValues();
            initializeUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeValues() {
        isUpdate = false;
        helper = new Helper(this);
        preferenceUtils = new PreferenceUtils(ServiceManagementActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        getServices();
        checkPermissions();
    }

    private void initializeUI() {
        addservice_btn = (Button) findViewById(R.id.addservice_btn);
        recycler_view_myservices = (RecyclerView) findViewById(R.id.recycler_view_myservices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ServiceManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_myservices.setLayoutManager(layoutManager);
        from_date_ser = (Button) findViewById(R.id.from_date_ser);
        to_date_ser = (Button) findViewById(R.id.to_date_ser);
        document_name = (TextView) findViewById(R.id.document_name);
        // CheckedStr = "2";
        radius_radiobtn = (CheckBox) findViewById(R.id.radius_radiobtn);
        p2p_radiobtn = (CheckBox) findViewById(R.id.p2p_radiobtn_service);
        choose_sub_category = (TextView) findViewById(R.id.choose_sub_category);
        choose_category = (TextView) findViewById(R.id.choose_category);
        ll_contracts = (LinearLayout) findViewById(R.id.ll_contracts);
        my_Services_btn = (Button) findViewById(R.id.my_Services_btn);
        customizations_btn = (Button) findViewById(R.id.customizations_btn);
        title_store = (TextView) findViewById(R.id.title_store);
        ll_my_products = (LinearLayout) findViewById(R.id.ll_my_products);
        ll_customizations = (LinearLayout) findViewById(R.id.ll_customizations);
        ll_prod_list = (LinearLayout) findViewById(R.id.ll_prod_list);
        ll_buyer_network = findViewById(R.id.ll_buyer_network);
        ll_add_service = (LinearLayout) findViewById(R.id.ll_add_service);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        three_dots_icon = findViewById(R.id.three_dots_icon);
        service_name_edit = (EditText) findViewById(R.id.service_name_edit);
        service_vehicle_id = (EditText) findViewById(R.id.service_vehicle_id);
        choose_vehicle_docs = (Button) findViewById(R.id.choose_vehicle_docs);
        save_service = (Button) findViewById(R.id.save_service);
        addservice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceManagementActivity.this,EditTransportServices.class);
                i.putExtra("Update","0");
                i.putExtra("Name","");
                i.putExtra("TranportType","");
                i.putExtra("VehicleType","");
                i.putExtra("FromDate","");
                i.putExtra("ToDate","");
                i.putExtra("Category","");
                i.putExtra("CatId","");
                i.putExtra("VehicleIdNumber","");
                i.putExtra("SubCategory","");
                i.putExtra("SubCatId","");
                i.putExtra("VehicleId","");
                i.putExtra("TransId","");
                i.putExtra("MinLoad","");
                i.putExtra("MaxLoad","");
                i.putExtra("ContainerType","");
                i.putExtra("Length","");
                i.putExtra("Width","");
                i.putExtra("Height","");
                i.putExtra("TransportTax","");
                i.putExtra("TransportDis","");
                i.putExtra("ServiceRadius","");
                i.putExtra("ServiceExtendRadius","");
                i.putExtra("PointSourceLoc","");
                i.putExtra("PointDesLoc","");
                i.putExtra("LoadType","");
                startActivity(i);
               /* ll_contracts.setVisibility(View.GONE);
                ll_add_service.setVisibility(View.VISIBLE);
                recycler_view_myservices.setVisibility(View.GONE);
                addservice_btn.setVisibility(View.GONE);
                TitleStr = "Add Service";
                title_store.setText(TitleStr);*/
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceManagementActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        images_recyclerView = findViewById(R.id.images_recyclerView);
        images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        images_recyclerView.setLayoutManager(linearLayoutManager);
        images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        confirm_add_icon = findViewById(R.id.confirm_add_icon);
        confirm_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton = "Licence";
                count = count + 1;
                final Dialog customdialog = new Dialog(ServiceManagementActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);

                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchServiceCamera(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchLicenceGallery(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
//                imagePath = FileUtils.launchServiceCamera(ServiceManagementActivity.this, count, true);
            }
        });
        permit_images_recyclerView = findViewById(R.id.permit_images_recyclerView);
        permit_images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutPermit = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        permit_images_recyclerView.setLayoutManager(linearLayoutPermit);
        permit_images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        confirm_permit_add_icon = findViewById(R.id.confirm_permit_add_icon);
        confirm_permit_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton = "Permit";
                count = count + 1;
                final Dialog customdialog = new Dialog(ServiceManagementActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);
                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchPermitCamera(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchPermitGallery(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });

        if(isUpdate){
            from_date_ser.setText(fromDateStr);
            to_date_ser.setText(toDateStr);
        }
        min_load_service = (EditText) findViewById(R.id.min_load_service);
        max_load_service = (EditText) findViewById(R.id.max_load_service);
        spinner_body_type = (Spinner) findViewById(R.id.spinner_body_type);
        ArrayAdapter bodyType = new ArrayAdapter(ServiceManagementActivity.this, android.R.layout.simple_spinner_item, BodyType);
        bodyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_body_type.setAdapter(bodyType);
        spinner_body_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyTypeStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (ContainerType != null || !ContainerType.isEmpty()) {
            if(ContainerType.equalsIgnoreCase("closed")) {
                bodyTypeStr = "open";
                final ArrayAdapter bodyType1 = new ArrayAdapter(ServiceManagementActivity.this, android.R.layout.simple_spinner_item, BodyType1);
                bodyType1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_body_type.setAdapter(bodyType1);
            }else{
                bodyTypeStr = "closed";
                final ArrayAdapter bodyType2 = new ArrayAdapter(ServiceManagementActivity.this, android.R.layout.simple_spinner_item, BodyType2);
                bodyType2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner_body_type.setAdapter(bodyType2);
            }
        }
        length_service = (EditText) findViewById(R.id.length_service);
        width_service = (EditText) findViewById(R.id.width_service);
        height_service = (EditText) findViewById(R.id.height_service);
        tax_service = (EditText) findViewById(R.id.tax_service);
        discount_service = (EditText) findViewById(R.id.discount_service);
        ll_choose_subcategory = (LinearLayout) findViewById(R.id.ll_choose_subcategory);
        if(isUpdate){
            choose_category.setText(CategorName);
            choose_sub_category.setText(SubCategoryName);
            width_service.setText(WidthStr);
            length_service.setText(Length);
            height_service.setText(Height);
            tax_service.setText(TransportTax);
            discount_service.setText(TransportDis);
            ll_choose_subcategory.setVisibility(View.VISIBLE);
        }else{
            ll_choose_subcategory.setVisibility(View.GONE);
        }
        service_partial_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*                if (isChecked) {
                    loadValue = "Partial";
                    loadArrayList.add(loadValue);
                    showPartialRecycelrview(true);
                }*/
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
                }else {
                    ll_partial.setVisibility(View.GONE);
                }
            }
        });
        service_full_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*                if (isChecked) {
                    loadValue = "Full_load";
                    loadArrayList.add(loadValue);
                    showPartialRecycelrview(false);
                }*/
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
                    ll_full.setVisibility(View.GONE);
                }
            }
        });
        radius_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (TitleStr.equalsIgnoreCase("Add Service")) {
                        actionValue = "Radius";
                        actionArrayList.add(actionValue);
                        Intent i = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                        i.putExtra("activity", "service1");
                        i.putExtra("type", "radius");
                        i.putExtra("ServiceRadius", "");
                        i.putExtra("ServiceExtendRadius", "");
                        startActivity(i);
                    } else if (TitleStr.equalsIgnoreCase("Edit Service")) {
                        if (CheckedStr.equalsIgnoreCase("0")) {
                            if (countRadius > 0) {
                                actionValue = "Radius";
                                actionArrayList.add(actionValue);
                                Intent i = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                                i.putExtra("activity", "service1");
                                i.putExtra("type", "radius");
                                i.putExtra("ServiceRadius", ServiceRadius);
                                i.putExtra("ServiceExtendRadius", ServiceExtendRadius);
                                startActivity(i);
                            } else {

                            }
                            countRadius = countRadius + 1;
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
                    if (TitleStr.equalsIgnoreCase("Add Service")) {
                        actionValue = "Point to Point";
                        actionArrayList.add(actionValue);
                        Intent i = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                        i.putExtra("activity", "service1");
                        i.putExtra("type", "radius");
                        i.putExtra("PointSourceLoc", "");
                        i.putExtra("PointDesLoc", "");
                        startActivity(i);
                    } else if (TitleStr.equalsIgnoreCase("Edit Service")) {
                        if (CheckedStr.equalsIgnoreCase("0")) {
                            if (countRadius > 0) {
                                actionValue = "Point to Point";
                                actionArrayList.add(actionValue);
                                Intent i = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                                i.putExtra("activity", "service1");
                                i.putExtra("type", "radius");
                                i.putExtra("PointSourceLoc", PointSourceLoc);
                                i.putExtra("PointDesLoc", PointDesLoc);
                                startActivity(i);
                            } else {

                            }
                            countRadius = countRadius + 1;
                        } else if (CheckedStr.equalsIgnoreCase("1")) {

                        }
                    }
                }
            }
        });

        add_partial = findViewById(R.id.add_partial);
        add_full_unit = findViewById(R.id.add_full_unit);

        partial_recyclerview = findViewById(R.id.partial_recyclerview);
        LinearLayoutManager linearLayoutManagerq1 = new LinearLayoutManager(ServiceManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        partial_recyclerview.setLayoutManager(linearLayoutManagerq1);
        partial_recyclerview.setHasFixedSize(true);
        partial_recyclerview.setItemAnimator(new DefaultItemAnimator());

        full_recyclerview = findViewById(R.id.full_recyclerview);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ServiceManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        full_recyclerview.setLayoutManager(linearLayoutManager1);
        full_recyclerview.setHasFixedSize(true);
        full_recyclerview.setItemAnimator(new DefaultItemAnimator());
        ll_partial = findViewById(R.id.ll_partial);
        ll_full = findViewById(R.id.ll_full);
        service_partial_checkbox = findViewById(R.id.service_partial_checkbox);
        service_full_checkbox = findViewById(R.id.service_full_checkbox);

        add_partial.setOnClickListener(new View.OnClickListener() {
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
                            partial_recyclerview.scrollToPosition(unitFullDisplayList.size() - 1);
                            partial_recyclerview.setVisibility(View.VISIBLE);
                            if (serviceUnitListAdapter != null) {
                                serviceUnitListAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });

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
                            full_recyclerview.setVisibility(View.VISIBLE);
                            if (serviceUnitListAdapter != null) {
                                serviceUnitListAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });

        if(LoadType.equalsIgnoreCase("null")){

        }else{
            if(LoadType.equalsIgnoreCase("Partial")){
                service_partial_checkbox.setChecked(true);
                service_full_checkbox.setChecked(false);
                // showPartialRecycelrview(true);
            }else if(LoadType.equalsIgnoreCase("Full_load")){
                service_partial_checkbox.setChecked(false);
                service_full_checkbox.setChecked(true);
                // showPartialRecycelrview(false);
            } else if(LoadType.equalsIgnoreCase("Partial,Full_load")){
                service_partial_checkbox.setChecked(true);
                service_full_checkbox.setChecked(true);
                //showPartialRecycelrview(true);
                //showPartialRecycelrview(false);
            }else if(LoadType.equalsIgnoreCase("Full_load,Partial")){
                service_partial_checkbox.setChecked(true);
                service_full_checkbox.setChecked(true);
            }
        }
        my_Services_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_my_products.setVisibility(View.VISIBLE);
                ll_customizations.setVisibility(View.GONE);
                my_Services_btn.setTextColor(getResources().getColor(R.color.white));
                customizations_btn.setTextColor(getResources().getColor(R.color.black));
                my_Services_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        customizations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three_dots_icon.setVisibility(View.VISIBLE);
                ll_buyer_network.setVisibility(View.VISIBLE);
                ll_customizations.setVisibility(View.VISIBLE);
                ll_my_products.setVisibility(View.GONE);
                customizations_btn.setTextColor(getResources().getColor(R.color.white));
                my_Services_btn.setTextColor(getResources().getColor(R.color.black));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                my_Services_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });

        three_dots_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ServiceManagementActivity.this, v);
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
    private void showPartialRecycelrview(boolean isPartial) {
        if (unitList.size() > 0) {
            if (isPartial) {
                ll_partial.setVisibility(View.VISIBLE);
                unitDisplayList.add(unitList.get(0));
                partial_recyclerview.setVisibility(View.VISIBLE);
                serviceUnitListAdapter = new ServiceUnitListAdapter(ServiceManagementActivity.this, unitDisplayList, unitList, isPartial,"0");
                partial_recyclerview.setAdapter(serviceUnitListAdapter);
            } else {
                ll_full.setVisibility(View.VISIBLE);
                unitFullDisplayList.add(unitList.get(0));
                full_recyclerview.setVisibility(View.VISIBLE);
                serviceUnitListAdapter = new ServiceUnitListAdapter(ServiceManagementActivity.this, unitFullDisplayList, unitList, isPartial,"0");
                full_recyclerview.setAdapter(serviceUnitListAdapter);
            }

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (new File(imagePath).exists()) {
                    helper.showLoader(ServiceManagementActivity.this, "Loading image", "Please wait...");
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
                                            imagesList.add(profileString);
                                            // fileList.add(imagePath);
                                            setUpRecyclerView(imagePath);
                                        } else {
                                            permitimagesList.add(profileString);
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
                    helper.showToast(ServiceManagementActivity.this, errorMessage);
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
                            String path = getRealPathFromURI(ServiceManagementActivity.this, uri);
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
                imagesList.add(profileString);
                setUpRecyclerView(imagePath);
            } else {
                permitimagesList.add(profileString);
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
    private File getOutputImageFile(int mediaTypeImage) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraImage");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraImage.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (mediaTypeImage == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
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
    public void progressdialog() {
        progressDialog = new ProgressDialog(ServiceManagementActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getServices() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportServices(user_id);

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
                                                transportGetService.setPr_status(pr_status);
                                                transportGetServices.add(transportGetService);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (transportGetServices != null) {
                                        progressDialog.dismiss();
                                        myServicesAdapter = new MyServicesAdapter(ServiceManagementActivity.this, transportGetServices,"0");
                                        recycler_view_myservices.setAdapter(myServicesAdapter);
                                        myServicesAdapter.notifyDataSetChanged();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ServiceManagementActivity.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ServiceManagementActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                Toast.makeText(ServiceManagementActivity.this, "Some thing went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void hidePartialRecyclerview () {
        partial_recyclerview.setVisibility(View.GONE);
    }
    public void hideFullRecyclerview () {
        full_recyclerview.setVisibility(View.GONE);
    }
    private void setUpRecyclerView (String image){
        if (imagesList.size() > 0) {
            if (SelectedButton.equalsIgnoreCase("Licence")) {
                fileList.add(image);
                ImagesAdapter imagesAdapter = new ImagesAdapter(ServiceManagementActivity.this, fileList,false);
                images_recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.notifyDataSetChanged();
            } else if (SelectedButton.equalsIgnoreCase("Permit")) {
                permitfileList.add(image);
                ImagesAdapter imagesAdapter = new ImagesAdapter(ServiceManagementActivity.this, permitfileList,false);
                permit_images_recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.notifyDataSetChanged();
            }
        } else {
            images_recyclerView.setVisibility(View.GONE);
        }
    }
}
