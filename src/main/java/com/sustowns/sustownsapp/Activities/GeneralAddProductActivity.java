package com.sustowns.sustownsapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.GeneralProductApi;
import com.sustowns.sustownsapp.Api.ProductsApi;
import com.sustowns.sustownsapp.Api.TransportApi;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.ServiceUnitModel;
import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sustowns.sustownsapp.Activities.MainActivity.user_role;

public class GeneralAddProductActivity extends AppCompatActivity {
    TextView choose_unit,choose_sampleunit,choose_category,choose_sub_category,choose_currency,choose_shipping_sizes;
    LinearLayout ll_choose_subcategory,ll_international_freight,ll_domestiic_freight;
    CheckBox checkbox_domestic,checkbox_international;
    ProgressDialog progressDialog;
    ArrayList<String> unitNameLIst,sampleunitNameLIst,shippingList;
    List<String> categoriesList = new ArrayList<>();
    List<String> categorySelectedPosition = new ArrayList<>();
    List<Integer> subCategorySelectedPosition = new ArrayList<Integer>();
    List<String> selectedCurrencyList = new ArrayList<>();
    List<String> subcategoriesList = new ArrayList<>(),categorySelectedList = new ArrayList<>(),subCategorySelectedList = new ArrayList<>();
    AlertDialog alertDialog;
    int textlength = 0;
    android.support.v7.app.AlertDialog alertDialog1;
    String clickedSearch,unitId,sampleUnitId,currecyCode;
    ArrayList<String> currencyCodes,CountryCodes;
    Helper helper;
    List<Integer> shippingSelectedPosition = new ArrayList<>();
    List<String> shippingSelectedList = new ArrayList<>();
    ArrayList<String> selectedItems = new ArrayList<String>();
    SparseBooleanArray mSelectedContinents = new SparseBooleanArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_general_add_product);
        initializeValues();
        initializeUI();
    }
    private void initializeValues() {
        helper = new Helper(GeneralAddProductActivity.this);
    }
    private void initializeUI() {
        choose_shipping_sizes = findViewById(R.id.choose_shipping_sizes);
        choose_unit = (TextView) findViewById(R.id.choose_unit);
        choose_sampleunit = (TextView) findViewById(R.id.choose_sampleunit);
        choose_category = (TextView) findViewById(R.id.choose_category);
        ll_choose_subcategory = (LinearLayout) findViewById(R.id.ll_choose_subcategory);
        choose_sub_category = (TextView) findViewById(R.id.choose_sub_category);
        choose_currency = (TextView) findViewById(R.id.choose_currency);
        ll_international_freight = findViewById(R.id.ll_international_freight);
        ll_domestiic_freight = findViewById(R.id.ll_domestic_freight);
        checkbox_international = findViewById(R.id.checkbox_international);
        checkbox_domestic = findViewById(R.id.checkbox_domestic);
        choose_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnitsList();
            }
        });
        choose_sampleunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSampleUnitsList();
            }
        });
        choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategoriesList();
            }
        });
        choose_sub_category = (TextView) findViewById(R.id.choose_sub_category);
        choose_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJsonObject(true);
            }
        });
        choose_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrencyList();
            }
        });
        choose_shipping_sizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShippingSizesList();
            }
        });
        checkbox_domestic.setChecked(false);
        checkbox_international.setChecked(false);
        checkbox_international.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_international_freight.setVisibility(View.VISIBLE);
                }else{
                    ll_international_freight.setVisibility(View.GONE);
                }
            }
        });
        checkbox_domestic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_domestiic_freight.setVisibility(View.VISIBLE);
                }else{
                    ll_domestiic_freight.setVisibility(View.GONE);
                }
            }
        });
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(GeneralAddProductActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getUnitsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        GeneralProductApi service = retrofit.create(GeneralProductApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getGenWeightUnitList();

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
                                    JSONArray jsonArray = root.getJSONArray("weight_unit");
                                    unitNameLIst = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        idList.add(jsonObject.getString("id"));
                                        unitNameLIst.add(jsonObject.getString("weight_unit"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialogUnit(false,unitNameLIst, idList);
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
    private void getSampleUnitsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        GeneralProductApi service = retrofit.create(GeneralProductApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getGenSampleWeightUnitList();

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
                                    JSONArray jsonArray = root.getJSONArray("sampleweight_unit");
                                    sampleunitNameLIst = new ArrayList<>();
                                    List<String> sampleUnitidList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        sampleUnitidList.add(jsonObject.getString("id"));
                                        sampleunitNameLIst.add(jsonObject.getString("weight_unit"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialogUnit(true,sampleunitNameLIst, sampleUnitidList);
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
    private void showAlertDialogUnit(final boolean isSample,final List<String> unitList,
                                      final List<String> idList){
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeneralAddProductActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);
            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            if(isSample){
                title.setText("Choose Sample Unit");
            }else {
                title.setText("Choose Unit");
            }
            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
            inputSearch.setVisibility(View.GONE);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
            alertDialog = dialogBuilder.create();
            if (unitList.size() == 0) {
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GeneralAddProductActivity.this,
                R.layout.simple_list_item, R.id.list_item_txt, unitList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if(isSample){
                        String itemValue = unitList.get(position);
                        choose_sampleunit.setText(itemValue);
                        sampleUnitId = idList.get(position);
                        alertDialog.dismiss();
                    }else {
                        String itemValue = unitList.get(position);
                        choose_unit.setText(itemValue);
                        unitId = idList.get(position);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getCategoriesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCategoriesList();

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
                                String status = root.getString("status");
                                String error = root.getString("error");
                                JSONObject responseObj = root.getJSONObject("response");
                                if (error.equalsIgnoreCase("false")) {
                                    JSONArray jsonArray = responseObj.getJSONArray("data");
                                    categoriesList = new ArrayList<>();
                                    List<String> categoryIds = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject catObj = jsonArray.getJSONObject(i);
                                        categoryIds.add(catObj.getString("id"));
                                        categoriesList.add(catObj.getString("title"));
                                    }
                                    showAlertDialog1(true, categoriesList, categoryIds);
                                } else {
                                    Toast.makeText(GeneralAddProductActivity.this, "No Categories ", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void showAlertDialog1 ( final boolean isCategory, final List<String> categoryList,
                                    final List<String> categoryIdList){
        try {
            final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(GeneralAddProductActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_category, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            Button submitCat_btn = (Button) dialogView.findViewById(R.id.submitCat_btn);
            if (isCategory) {
                title.setText("Choose Category");
                ll_choose_subcategory.setVisibility(View.VISIBLE);
            }
            else {
                title.setText("Choose SubCategory");
            }
            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
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
                Toast.makeText(GeneralAddProductActivity.this, "No Items Available", Toast.LENGTH_SHORT).show();
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
                        categoryListView.setItemChecked(Integer.parseInt(categorySelectedPosition.get(pos)), true);
                    }
                }
            } else {
                if (subCategorySelectedPosition.size() > 0) {
                    for (int pos = 0; pos < subCategorySelectedPosition.size(); pos++) {
                        categoryListView.setItemChecked(subCategorySelectedPosition.get(pos), true);
                    }
                }
            }
            submitCat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                   // String itemValue = categoryList.get(position);
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
                                categorySelectedList.add(categoryIdList.get(position));
                                categorySelectedPosition.add(String.valueOf(position));
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
                            choose_category.setText(stringBuilder.toString());
                            choose_sub_category.setText("");
                            choose_sub_category.setHint("Choose Sub Category");
                            alertDialog1.dismiss();
                            setJsonObject(false);
                        } else {
                            Toast.makeText(GeneralAddProductActivity.this, "Please select one category!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                        ArrayList<String> selectedItems1 = new ArrayList<String>();
                        subCategorySelectedPosition = new ArrayList<Integer>();
                        subCategorySelectedList = new ArrayList<>();
                        for (int i = 0; i < checked.size(); i++) {
                            // Item position in adapter
                            int position = checked.keyAt(i);
                            // Add sport if it is checked i.e.) == TRUE!
                            if (checked.valueAt(i)) {
                                selectedItems1.add(adapter.getItem(position));
                                subCategorySelectedList.add(categoryIdList.get(position));
                                subCategorySelectedPosition.add(position);
                            }
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        if (selectedItems1.size() > 0) {
                            for (int i = 0; i < selectedItems1.size(); i++) {
                                if (i == selectedItems1.size() - 1) {
                                    stringBuilder.append(selectedItems1.get(i));
                                } else {
                                    stringBuilder.append(selectedItems1.get(i) + ", ");
                                }
                            }
                            choose_sub_category.setText(stringBuilder.toString());
                            alertDialog1.dismiss();
                        } else {
                            Toast.makeText(GeneralAddProductActivity.this, "Please select atleast one subcategory!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            alertDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setJsonObject(boolean isAlert) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray Product_ids = new JSONArray();
            for (int i = 0; i < categorySelectedList.size(); i++) {
                JSONObject CatidObj = new JSONObject();
                CatidObj.put("Cat_id", categorySelectedList.get(i));
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
                            }
                            showAlertDialog1(false, subcategoriesList, subcategoryIds);
                        } else {
                            // progressDialog.dismiss();
                            Toast.makeText(GeneralAddProductActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // progressDialog.dismiss();
                        Toast.makeText(GeneralAddProductActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ANError error) {
                    Log.d("Error", "ANError : " + error);
                    // progressDialog.dismiss();
                }
            });
    }
        public void getCurrencyList() {
             progressdialog();
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            GeneralProductApi service = retrofit.create(GeneralProductApi.class);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.getCurrencyList();
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
                          progressDialog.dismiss();
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
                                        CountryCodes = new ArrayList<String>();
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray msg = root.getJSONArray("currency");
                                            for (int i = 0; i < msg.length(); i++) {
                                                JSONObject Obj = msg.getJSONObject(i);

                                                String CurrencyCode = Obj.getString("CurrencyCode");
                                                String CountryCode = Obj.getString("CountryCode");
                                                currencyCodes.add(CurrencyCode);
                                                CountryCodes.add(CountryCode);
                                                   progressDialog.dismiss();
                                            }
                                            showCurrencyCodes(currencyCodes);
                                        } else {
                                             progressDialog.dismiss();
                                            Toast.makeText(GeneralAddProductActivity.this, "No Currency Available.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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
                     progressDialog.dismiss();
//                Toast.makeText(ProductCategories.this,"Server not responding", Toast.LENGTH_SHORT).show();
                }
            });
        }
    private void showCurrencyCodes(final List<String> currencyList){
        try {
            final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(GeneralAddProductActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_layout_register, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose Currency");

            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final EditText inputSearch = (EditText) dialogView.findViewById(R.id.inputSearch);
            alertDialog1 = dialogBuilder.create();
            if (currencyList.size() == 0) {
                if (alertDialog1 != null)
                    alertDialog1.dismiss();
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
                R.layout.simple_list_item, R.id.list_item_txt, currencyList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    clickedSearch = "clicked";
                    textlength = inputSearch.getText().length();
                    selectedCurrencyList.clear();
                   // selectedCountryIdList.clear();
                    for (int i = 0; i < currencyList.size(); i++) {
                        if (textlength <= currencyList.get(i).length()) {
                            Log.d("ertyyy", currencyList.get(i).toLowerCase().trim());
                            if (currencyList.get(i).toLowerCase().trim().contains(
                                inputSearch.getText().toString().toLowerCase().trim())) {
                                selectedCurrencyList.add(currencyList.get(i));
                               // selectedCountryIdList.add(countryList.get(i));
                            }
                        }
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(GeneralAddProductActivity.this,
                        R.layout.simple_list_item, R.id.list_item_txt, selectedCurrencyList);
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
                        currecyCode = selectedCurrencyList.get(position);
                        choose_currency.setText(currecyCode);
                        //currecyCode = selectedCurrencyList.get(position);
                        alertDialog1.dismiss();
                    }else{
                        currecyCode = currencyList.get(position);
                        choose_currency.setText(currecyCode);
                       // currecyCode = selectedCurrencyList.get(position);
                        alertDialog1.dismiss();
                    }
                }
            });
            alertDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getShippingSizesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DZ_URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        GeneralProductApi service = retrofit.create(GeneralProductApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getGenShippingTypes();
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
                                        JSONArray jsonArray = root.getJSONArray("portlist");
                                        shippingList = new ArrayList<>();
                                        List<String> idList = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            idList.add(jsonObject.getString("id"));
                                            shippingList.add(jsonObject.getString("type"));
                                        }
                                        //In response data
                                        progressDialog.dismiss();
                                        if(shippingList.size()>0) {
                                            showAlertDialogShipping(shippingList, idList,mSelectedContinents);
                                            progressDialog.dismiss();
                                        }else{
                                            Toast.makeText(GeneralAddProductActivity.this, "Categories not available", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GeneralAddProductActivity.this,"Something went wrong!please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialogShipping(final List<String> shippingList, final List<String> shippingIdList,SparseBooleanArray mSelectedContinents) {
        try {
            final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(GeneralAddProductActivity.this);
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
            alertDialog1 = dialogBuilder.create();
            if (shippingList.size() == 0) {
                if (alertDialog1 != null)
                    alertDialog1.dismiss();
                Toast.makeText(GeneralAddProductActivity.this, "No Categories Available", Toast.LENGTH_SHORT).show();
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
                android.R.layout.simple_list_item_multiple_choice, shippingList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
      /*      if(!isUpdate) {
                if (shippingList.contains(service_name)) {
                    shippingSelectedList.add(shippingIdList.get(position));
                    shippingSelectedPosition.add(position);
                }
            }*/
            if (shippingSelectedPosition.size() > 0) {
                for (int pos = 0; pos < shippingSelectedPosition.size(); pos++) {
                    categoryListView.setItemChecked(shippingSelectedPosition.get(pos), true);
                }
            }
            submitCat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                    //String itemValue = shippingList.get(position);
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
                        choose_shipping_sizes.setText(stringBuilder.toString());
                        alertDialog1.dismiss();
                    } else {
                        Toast.makeText(GeneralAddProductActivity.this, "Please select one shipping!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
