package com.sustowns.sustownsapp.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.helpers.ImageZoomUtils.PhotoView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.sustowns.sustownsapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;


public class Helper {

    Context context;
    String languageToLoad;
    String cityName = null;
    SweetAlertDialog mprogressDialog;

    PreferenceUtils preferenceUtils;

    public Helper(Context context) {
        this.context = context;
        preferenceUtils = new PreferenceUtils(context);
    }


    public boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // For Complete Address using lat & log
    public Address getCompleteAddressString(Double LATITUDE, Double LONGITUDE) {
        String strAdd = "";
        Address returnedAddress = new Address(Locale.ENGLISH);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                if (strAdd.trim().isEmpty()) {
                    String address = addresses.get(0).getAddressLine(0);
                 /*   String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();*/

                    strAdd = address;
                }
                Log.d("Current location add", "" + strReturnedAddress.toString());
            } else {
                Log.d("My Current location add", "No Address returned!");
            }
        } catch (Exception e) {
            // If exception is due to Android Version
            // Use this method to get the address
            Log.e("getCompleteAddressSt: ", e.getMessage());
        }
        return returnedAddress;
    }

    // Glide Imageview
    public void getImage(final Context context, final String url, final ImageView imageView) {
        try {
            Picasso.get()
                    .load(url)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    // Check if google play services is installed on the device
    public boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                showToast(context, "Please download Google play services.");
            else
                showToast(context, "This device does not support Google play services.");
            return false;
        }
        return true;
    }

    // slide the view from below itself to the current position
    public void slideUp(View view, boolean isFill) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                view.getHeight());                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(isFill);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view, boolean isFill) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(isFill);
        view.startAnimation(animate);
    }

    public void stopShimmer(ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

    @SuppressLint("NewApi")
    public void showImageAlert(final Context context, final String imageUri) {
        try {
            if (imageUri != null) {
                this.context = context;
                Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.BLACK));
                final PhotoView imageView = new PhotoView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setCropToPadding(true);
                if (imageUri.contains("http")) {
                    Picasso.get().load(imageUri)
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(imageView);
                } else {
                    Glide.with(context).load(imageUri).into(imageView);
                }
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.setContentView(imageView);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hideLoader();
        }
    }
    // Show alert dialog
    public void showDialog(Activity context, int type, String title, String message, SweetAlertDialog.OnSweetClickListener listener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        if (cancelListener != null) {
            new SweetAlertDialog(context, type)
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText("Yes")
                    .setCancelText("No")
                    .showCancelButton(true)
                    .setConfirmClickListener(listener)
                    .setCancelClickListener(cancelListener)
                    .show();
        }
    }

    // Show alert dialog
    public void showPayDialog(Activity context, int type, String title, String message, SweetAlertDialog.OnSweetClickListener listener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        if (cancelListener != null) {
            new SweetAlertDialog(context, type)
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText("Pay Now")
                    .setCancelText("Cancel")
                    .showCancelButton(true)
                    .setConfirmClickListener(listener)
                    .setCancelClickListener(cancelListener)
                    .show();
        }
    }

    public void singleClickAlert(Activity context, int type, String title, String message, SweetAlertDialog.OnSweetClickListener listener) {
        new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .showCancelButton(false)
                .setConfirmClickListener(listener)
                .show();
    }

    // Show progress bar
    public void showLoader(final Context context, final String title, final String message) {
        try {
            if (mprogressDialog != null) {
                mprogressDialog.dismiss();
            }
            mprogressDialog = null;
            mprogressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mprogressDialog.getProgressHelper().setBarColor(Color.parseColor("#1a9263"));
            if (title.isEmpty())
                mprogressDialog.setTitleText("Loading");
            else
                mprogressDialog.setTitleText(title);
            mprogressDialog.setContentText(message);
            mprogressDialog.setCancelable(false);
            mprogressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Hide progress Bar
    public void hideLoader() {
        try {
            if (mprogressDialog != null && mprogressDialog.isShowing())
                mprogressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
