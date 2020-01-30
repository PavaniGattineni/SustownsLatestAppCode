package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyProfileActivity extends AppCompatActivity {

    ImageView backarrow,iv_icon,profile_image,savearrow;
    RelativeLayout rl_capture,rl_gallery;
    public String PickedImgPath = null;
    String profileString,username,useremail;
    EditText businessname,businesswebsie,businessdetails,name,email,mobile,address;
    Spinner spinner_country,spinner_state,spinner_city;
    TextView title_user_name,title_user_email;
    private Uri picUri;
    Intent intent;

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
        setContentView(R.layout.activity_my_profile);
        intent = getIntent();
        username = intent.getStringExtra("UserName");
        useremail = intent.getStringExtra("UserEmail");
        title_user_name = (TextView) findViewById(R.id.title_user_name);
        title_user_email = (TextView) findViewById(R.id.title_user_email);
        if(useremail.isEmpty() || useremail == null){
            title_user_email.setText("useremail");
            title_user_name.setText("username");
        }else{
            title_user_email.setText(useremail);
            title_user_name.setText(username);
        }
        businessname = (EditText) findViewById(R.id.et_business_name);
        businesswebsie = (EditText) findViewById(R.id.et_business_website);
        businessdetails = (EditText) findViewById(R.id.et_business_details);
        name = (EditText) findViewById(R.id.et_name);
       // email = (EditText) findViewById(R.id.et_profile_email);
        mobile = (EditText) findViewById(R.id.et_mobile);
        address = (EditText) findViewById(R.id.et_profile_address);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        savearrow = (ImageView) findViewById(R.id.savearrow);

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(MyProfileActivity.this);
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
        });
        savearrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void capture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, FileUtils.CAMERA_CAPTURE);
            /* PickedImgPath = FileUtils.launchCamera(MyAccount.this, "", true);*/
            // PickedImgPath = FileUtils.launchCamera(MyAccount.this, "", true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    private void browse() {
        try {
            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery_intent, FileUtils.GALLERY_IMAGE);
            /*Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery_intent, FileUtils.GALLERY_IMAGE);*/
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == FileUtils.CAMERA_CAPTURE) {  // Camera intent
                onCaptureImageResult(data);
            }
            // Gallery intent
            else if (requestCode == FileUtils.GALLERY_IMAGE && data != null && data.getData() != null) {
                try {
                    PickedImgPath = FileUtils.getFilePath(MyProfileActivity.this, data.getData());
                    Uri uri = data.getData();
                    File file = new File(PickedImgPath);
                    if (file.exists())
                        try {
                            Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), picUri);
                            profileString = getEncodedImage(bitmapImage);
                            profile_image.setImageURI(picUri);
                            Picasso.get().load(uri).placeholder(R.mipmap.ic_launcher_round).into(profile_image);
                            //update_profile_service();
                        } catch (Exception e) {
                            Picasso.get().load("file://" + uri).placeholder(R.mipmap.ic_launcher_round).into(profile_image);
                        }
                    else
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    //  helper.showErrorDialog(this, "Image path error!" + "\n" + getResources().getString(R.string.provider_image_not_found));
                }
                // preferenceUtils.saveString(PreferenceUtils.Image, PickedImgPath);
            } else
                PickedImgPath = "NO";
        } else {
            PickedImgPath = "NO";
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

        profile_image.setImageBitmap(bmp);
        //imageView_profile.setImageBitmap(null);
        //   profile_img.setImageBitmap(thumbnail);
        // preferenceUtils.saveString(PreferenceUtils.Image, PickedImgPath);
    }
}
