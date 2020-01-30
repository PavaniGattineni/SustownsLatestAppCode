package com.sustowns.sustownsapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.sustowns.sustownsapp.helpers.Helper;

import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, LocationListener {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    public static Double lat = 0.0, lon = 0.0;
    public static String address = "", pincode = "", toPincode = "", fromPincode = "";
    public static LatLng fromLatLon = new LatLng(0, 0), toLatLon = new LatLng(0, 0);
    public static EditText map_address_edit, radius_edit,extradius_edit;
    public static TextView from_address_txt, to_address_edit;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    final int PLACE_AUTOCOMPLETE_FROM_CODE = 2;
    final int PLACE_AUTOCOMPLETE_TO_CODE = 3;
    public String TAG = "PLaceManish";
    GoogleMap googleMap1;
    TextView txt_toolbar_add_address, map_address_txt, txt_search;
    Button btn_map_save_add;
    ImageView pointer, map_img_back, map_img_search;
    Helper helper;
    String mapType = "", activityName = "",ServiceExtendRadius="",ServiceRadius="",PointSourceLoc="",PointDesLoc="";
    ImageView radius_select;
    Polyline greenPolyLine;
    Marker frommarker, tomarker;
    LinearLayout location_txt_add_layout, location_edit_add_layout, location_from_add_layout, location_to_add_layout,location_extradius_layout,location_radius_layout;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Circle circle;
    //private String API_KEY = "AIzaSyCONi2p2KPGgrvDIF3uUq359UwEuk9Lit4";
    private String API_KEY = "AIzaSyBq3lLqjiOU_K8ksL-NNyZqtDPOd_4dk-Q";
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        preferenceUtils = new PreferenceUtils(MapsActivity.this);
        initializeUI();
        initializeValues();
    }

    private void initializeUI() {
        txt_search = (TextView) findViewById(R.id.txt_search);
        txt_toolbar_add_address = findViewById(R.id.txt_toolbar_add_address);
        map_address_txt = findViewById(R.id.map_address_txt);
        btn_map_save_add = findViewById(R.id.btn_map_save_add);
        pointer = findViewById(R.id.pointer);
        map_img_back = findViewById(R.id.map_img_back);
        map_img_search = findViewById(R.id.map_img_search);
        location_txt_add_layout = findViewById(R.id.location_txt_add_layout);
        location_edit_add_layout = findViewById(R.id.location_edit_add_layout);
        location_from_add_layout = findViewById(R.id.location_from_add_layout);
        location_to_add_layout = findViewById(R.id.location_to_add_layout);
        from_address_txt = findViewById(R.id.from_address_txt);
        to_address_edit = findViewById(R.id.to_address_edit);
        map_address_edit = findViewById(R.id.map_address_edit);
        location_radius_layout = findViewById(R.id.location_radius_layout);
        location_extradius_layout = findViewById(R.id.location_extradius_layout);

        radius_edit = findViewById(R.id.radius_edit);
        extradius_edit = findViewById(R.id.extradius_edit);
        radius_select = findViewById(R.id.radius_select);
       /* if(ServiceRadius.equalsIgnoreCase("null") || ServiceRadius.isEmpty()||ServiceExtendRadius.equalsIgnoreCase("null")||
        ServiceExtendRadius.isEmpty()||PointSourceLoc.equalsIgnoreCase("null")||PointSourceLoc.isEmpty()||PointDesLoc.equalsIgnoreCase("null")||PointDesLoc.isEmpty()){
            radius_edit.setText("");
            extradius_edit.setText("");
            from_address_txt.setText("");
            to_address_edit.setText("");
        }else {
            radius_edit.setText(ServiceRadius);
            extradius_edit.setText(ServiceExtendRadius);
            from_address_txt.setText(PointSourceLoc);
            to_address_edit.setText(PointDesLoc);
        }*/
        // Map
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.current_location);
        supportMapFragment.getMapAsync(this);
        setZoomControlPosition(supportMapFragment);

        map_img_search.setOnClickListener(this);
        map_img_back.setOnClickListener(this);
        btn_map_save_add.setOnClickListener(this);
        radius_select.setOnClickListener(this);
        from_address_txt.setOnClickListener(this);
        to_address_edit.setOnClickListener(this);
    }

    private void initializeValues() {
        if (!Places.isInitialized())
            Places.initialize(getApplicationContext(), API_KEY);

        helper = new Helper(this);
        if (getIntent() != null && getIntent().getStringExtra("activity") != null) {
            activityName = getIntent().getStringExtra("activity");
            mapType = getIntent().getStringExtra("type");
            ServiceRadius = getIntent().getStringExtra("ServiceRadius");
            ServiceExtendRadius = getIntent().getStringExtra("ServiceExtendRadius");
            PointSourceLoc = getIntent().getStringExtra("PointSourceLoc");
            PointDesLoc = getIntent().getStringExtra("PointDesLoc");
        }
        if (mapType.equalsIgnoreCase("p2p")) {
            // For map type point to point
            location_txt_add_layout.setVisibility(View.GONE);
            pointer.setVisibility(View.GONE);
            map_img_search.setVisibility(View.GONE);
            location_edit_add_layout.setVisibility(View.GONE);
            location_radius_layout.setVisibility(View.GONE);
            location_extradius_layout.setVisibility(View.GONE);

            location_from_add_layout.setVisibility(View.VISIBLE);
            location_to_add_layout.setVisibility(View.VISIBLE);
            if(PointSourceLoc.equalsIgnoreCase("null")||PointSourceLoc.isEmpty() || PointDesLoc.isEmpty()||PointDesLoc.equalsIgnoreCase("null")){

            }else{
                from_address_txt.setText(PointSourceLoc);
                to_address_edit.setText(PointDesLoc);
            }

        } else if (mapType.equalsIgnoreCase("radius")) {
            // For map type radius
            location_from_add_layout.setVisibility(View.GONE);
            location_to_add_layout.setVisibility(View.GONE);

            location_txt_add_layout.setVisibility(View.VISIBLE);
            location_edit_add_layout.setVisibility(View.VISIBLE);
            pointer.setVisibility(View.VISIBLE);
            map_img_search.setVisibility(View.VISIBLE);
            location_radius_layout.setVisibility(View.VISIBLE);
            location_extradius_layout.setVisibility(View.VISIBLE);

            if (23 <= Build.VERSION.SDK_INT && checkPermissions()) {
                locationSettingsChecker();
            } else {
                locationSettingsChecker();
            }
        } else if (mapType.equalsIgnoreCase("none")) {
            // For map type radius
            location_from_add_layout.setVisibility(View.GONE);
            location_to_add_layout.setVisibility(View.GONE);

            location_edit_add_layout.setVisibility(View.GONE);
            location_radius_layout.setVisibility(View.GONE);
            location_extradius_layout.setVisibility(View.GONE);
            location_txt_add_layout.setVisibility(View.VISIBLE);
            pointer.setVisibility(View.VISIBLE);
            map_img_search.setVisibility(View.VISIBLE);

            if (23 <= Build.VERSION.SDK_INT && checkPermissions()) {
                locationSettingsChecker();
            } else {
                locationSettingsChecker();
            }
          /*  if(ServiceRadius.equalsIgnoreCase("null") || ServiceRadius.isEmpty()){

            }else{
                radius_edit.setText(ServiceRadius);
                extradius_edit.setText(ServiceExtendRadius);
                //showCircleRadius1();
            }*/
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap1 = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap1.setMyLocationEnabled(true);
        googleMap1.getUiSettings().setZoomControlsEnabled(true);
        googleMap1.getUiSettings().setZoomGesturesEnabled(true);
        googleMap1.getUiSettings().setCompassEnabled(true);
        googleMap1.getUiSettings().setRotateGesturesEnabled(true);

        try {
            locationSettingsChecker();
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap1.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mapType.equalsIgnoreCase("none")) {
                    lat = cameraPosition.target.latitude;
                    lon = cameraPosition.target.longitude;
                    address = (helper.getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude)).getAddressLine(0);
                    pincode = (helper.getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude)).getPostalCode();
                    map_address_txt.setText(address);
                    map_address_edit.setText(address);
                    preferenceUtils.saveString(PreferenceUtils.LATITUDE, String.valueOf(lat));
                    preferenceUtils.saveString(PreferenceUtils.LONGITUDE, String.valueOf(lon));

                }else if (mapType.equalsIgnoreCase("radius")) {
                    lat = cameraPosition.target.latitude;
                    lon = cameraPosition.target.longitude;
                    address = (helper.getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude)).getAddressLine(0);
                    pincode = (helper.getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude)).getPostalCode();
                    map_address_txt.setText(address);
                    map_address_edit.setText(address);
                    preferenceUtils.saveString(PreferenceUtils.LATITUDE, String.valueOf(lat));
                    preferenceUtils.saveString(PreferenceUtils.LONGITUDE, String.valueOf(lon));
                    if(ServiceRadius.equalsIgnoreCase("null") || ServiceRadius.isEmpty()){

                    }else{
                        radius_edit.setText(ServiceRadius);
                        extradius_edit.setText(ServiceExtendRadius);
                        showCircleRadius1();
                    }
                } else if (mapType.equalsIgnoreCase("p2p")) {
                    if (PointSourceLoc.equalsIgnoreCase("null") || PointSourceLoc.isEmpty() || PointDesLoc.isEmpty() || PointDesLoc.equalsIgnoreCase("null")) {

                    } else {
                        from_address_txt.setText(PointSourceLoc);
                        to_address_edit.setText(PointDesLoc);
                        googleMap1.moveCamera(CameraUpdateFactory.newLatLng(helper.getLocationFromAddress(MapsActivity.this, PointSourceLoc)));
                        fromLatLon = helper.getLocationFromAddress(MapsActivity.this, PointSourceLoc);
                        from_address_txt.setText(PointSourceLoc);
                        fromPincode = (helper.getCompleteAddressString(fromLatLon.latitude, fromLatLon.longitude)).getPostalCode();

                        if (frommarker != null) {
                            // frommarker.remove();
                        }
                        frommarker = googleMap1.addMarker(new MarkerOptions().position(fromLatLon).title(PointSourceLoc));
                        frommarker.showInfoWindow();
                        toLatLon = helper.getLocationFromAddress(MapsActivity.this, PointDesLoc);
                        toPincode = (helper.getCompleteAddressString(toLatLon.latitude, toLatLon.longitude)).getPostalCode();
                        to_address_edit.setText(PointDesLoc);
                        if (tomarker != null) {
                            //tomarker.remove();
                        }
                        tomarker = googleMap1.addMarker(new MarkerOptions().position(toLatLon).title(PointDesLoc));
                        tomarker.showInfoWindow();
                        googleMap1.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(toLatLon)
                                .zoom(10)
                                .bearing(30)
                                .tilt(45)
                                .build()));

                        drawPolyLine();
                    }
                }
                }

        });

    }
    private void setZoomControlPosition(SupportMapFragment mapFragment) {
        int ZoomControl_id = 0x1;
        int MyLocation_button_id = 0x2;

        // Find ZoomControl view
        View zoomControls = mapFragment.getView().findViewById(ZoomControl_id);

        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

            // Align it to - parent top|left
            if (getIntent() != null && getIntent().getStringExtra("type") != null) {
                if (getIntent().getStringExtra("type").equalsIgnoreCase("p2p")) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                }
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
            }

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_img_search:
                showPlaceAutoComplete(PLACE_AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.map_img_back:
                finish();
                break;
            case R.id.btn_map_save_add:
                // Dismiss the page with intent
                if (activityName.equalsIgnoreCase("store")) {
                    AddProductActivity.Product_Address_Map = map_address_txt.getText().toString();
                } else if (activityName.equalsIgnoreCase("product")) {
                  //  ProductDetailsActivity.Address = map_address_txt.getText().toString();
                    LocationDialogActivity.Address = map_address_txt.getText().toString();
                } else if(activityName.equalsIgnoreCase("productContract")){
                    MyProductContractActivity.Product_Detail_Address_Map = map_address_txt.getText().toString();
                } else if(activityName.equalsIgnoreCase("service")){
                    AddShippingServices.ServiceRadiusIn = radius_edit.getText().toString();
                    AddShippingServices.ServiceExtRadius = extradius_edit.getText().toString();
                    AddShippingServices.P2pSource = from_address_txt.getText().toString();
                    AddShippingServices.P2pDes = to_address_edit.getText().toString();
                }else if(activityName.equalsIgnoreCase("service1")){
                    ServiceManagementActivity.ServiceRadiusIn = radius_edit.getText().toString();
                    ServiceManagementActivity.ServiceExtRadius = extradius_edit.getText().toString();
                    ServiceManagementActivity.P2pSource = from_address_txt.getText().toString();
                    ServiceManagementActivity.P2pDes = to_address_edit.getText().toString();
                }else if(activityName.equalsIgnoreCase("shipping")){
                    ShippingAddress.ShippingAddress = map_address_txt.getText().toString();
                }
                finish();
                break;
            case R.id.radius_select:
                // Show Circle radius
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(radius_edit.getWindowToken(), 0);
                showCircleRadius();
                break;
            case R.id.from_address_txt:
                showPlaceAutoComplete(PLACE_AUTOCOMPLETE_FROM_CODE);
                break;
            case R.id.to_address_edit:
                showPlaceAutoComplete(PLACE_AUTOCOMPLETE_TO_CODE);
                break;
        }
    }
    private void showPlaceAutoComplete(int code) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, code);
    }
    private void showCircleRadius() {
        if (circle != null) {
            circle.remove();
            circle = googleMap1.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .strokeColor(getResources().getColor(R.color.appcolor))
                    .strokeWidth(5)
                    .fillColor(getResources().getColor(R.color.radius_color))
                    .radius(Double.parseDouble(radius_edit.getText().toString()) * 1000));
        } else {
            circle = googleMap1.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .strokeColor(getResources().getColor(R.color.appcolor))
                    .strokeWidth(5)
                    .fillColor(getResources().getColor(R.color.radius_color))
                    .radius(Double.parseDouble(radius_edit.getText().toString()) * 1000)); //For converting the miles to meters
        }
    }
    private void showCircleRadius1() {
        if (circle != null) {
           // circle.remove();
            circle = googleMap1.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .strokeColor(getResources().getColor(R.color.appcolor))
                    .strokeWidth(5)
                    .fillColor(getResources().getColor(R.color.radius_color))
                    .radius(Double.parseDouble(ServiceRadius) * 1000));
        } else {
            circle = googleMap1.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .strokeColor(getResources().getColor(R.color.appcolor))
                    .strokeWidth(5)
                    .fillColor(getResources().getColor(R.color.radius_color))
                    .radius(Double.parseDouble(ServiceRadius) * 1000)); //For converting the miles to meters
        }
    }

    private void locationSettingsChecker() {
        // Check permission for location
        if (helper.checkPlayServices(MapsActivity.this)) {
            startFusedLocation();
            registerRequestUpdate(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        helper.showLoader(MapsActivity.this, "", "Please wait while we retrieve your location.");
                        if (helper.checkPlayServices(MapsActivity.this)) {
                            startFusedLocation();
                            registerRequestUpdate(this);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Warning!");
                        builder.setMessage("Please allow Sustowns to access your location for further process.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                dialog.dismiss();
                                if (helper.checkPlayServices(MapsActivity.this)) {
                                    startFusedLocation();
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();*/
                        break;
                    default:
                        break;
                }
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    googleMap1.moveCamera(CameraUpdateFactory.newLatLng(helper.getLocationFromAddress(MapsActivity.this, place.getName())));
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    // TODO: Handle the error.
                    Log.i(TAG, status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case PLACE_AUTOCOMPLETE_FROM_CODE:
                // Show from marker in the map
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    fromLatLon = helper.getLocationFromAddress(MapsActivity.this, place.getName());
                        from_address_txt.setText(place.getName());
                    fromPincode = (helper.getCompleteAddressString(fromLatLon.latitude, fromLatLon.longitude)).getPostalCode();

                    if (frommarker != null) {
                        frommarker.remove();
                    }
                    frommarker = googleMap1.addMarker(new MarkerOptions().position(fromLatLon).title(place.getName()));
                    frommarker.showInfoWindow();
                    googleMap1.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLatLon, 10));
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    // TODO: Handle the error.
                    Log.i(TAG, status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case PLACE_AUTOCOMPLETE_TO_CODE:
                // Show to marker along with the
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    toLatLon = helper.getLocationFromAddress(MapsActivity.this, place.getName());
                    toPincode = (helper.getCompleteAddressString(toLatLon.latitude, toLatLon.longitude)).getPostalCode();
                        to_address_edit.setText(place.getName());
                    if (tomarker != null) {
                        tomarker.remove();
                    }
                    tomarker = googleMap1.addMarker(new MarkerOptions().position(toLatLon).title(place.getName()));
                    tomarker.showInfoWindow();
                    googleMap1.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(toLatLon)
                            .zoom(10)
                            .bearing(30)
                            .tilt(45)
                            .build()));

                    drawPolyLine();

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    // TODO: Handle the error.
                    Log.i(TAG, status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
        }
    }

    private void drawPolyLine() {
        final PolylineOptions greyOptions = new PolylineOptions();
        greyOptions.width(10);
        greyOptions.color(getResources().getColor(R.color.polyline));
        greyOptions.startCap(new SquareCap());
        greyOptions.endCap(new SquareCap());
        greyOptions.jointType(ROUND);
        if (greenPolyLine != null) {
            greenPolyLine.remove();
        }
        greenPolyLine = googleMap1.addPolyline(greyOptions.add(fromLatLon, toLatLon));
    }

    // The rest of the code is for getting geo location
    public void startFusedLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnectionSuspended(int cause) {
                        }

                        @Override
                        public void onConnected(Bundle connectionHint) {
                        }
                    }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                        }
                    }).build();
            mGoogleApiClient.connect();
            locationChecker(mGoogleApiClient, MapsActivity.this);
        } else {
            mGoogleApiClient.connect();
            locationChecker(mGoogleApiClient, MapsActivity.this);
        }
    }

    public void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // Requests here.
                        startFusedLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public void registerRequestUpdate(final LocationListener listener) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100000 * 60 * 60); // every second

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    helper.hideLoader();
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!isGoogleApiClientConnected()) {
                        mGoogleApiClient.connect();
                    }
                    registerRequestUpdate(listener);
                }
            }
        }, 500);
    }

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    @Override
    public void onLocationChanged(Location location) {
        helper.hideLoader();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();
        lon = location.getLongitude();
        googleMap1.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        address = (helper.getCompleteAddressString(location.getLatitude(), location.getLongitude())).getAddressLine(0);
        pincode = (helper.getCompleteAddressString(location.getLatitude(), location.getLongitude())).getPostalCode();
        map_address_txt.setText(address);
    }

    private boolean checkPermissions() {
        final Context context = MapsActivity.this;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                int i = 0;
                if (grantResults != null && grantResults.length > 0) {
                    for (i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            break;
                        }
                    }
                    if (i == 0 || i == grantResults.length) {
                        locationSettingsChecker();
                    } else {
                        // The user was asked to change settings, but chose not to
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Warning!");
                        builder.setMessage("Please allow Sustowns to access your location for further process.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                dialog.dismiss();
                                checkPermissions();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
