package com.sustowns.sustownsapp.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Api.BidContractsApi;
import com.sustowns.sustownsapp.Api.DZ_URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.sustowns.sustownsapp.Activities.FileUtils.getPath;

public class QuoteNowActivity extends AppCompatActivity {

    ImageView backarrow,close,profile_image,close_quote;
    RadioButton radio_advance,radio_escrow,radio_split;
    RadioGroup radiogroup;
    EditText advance_amt_edit,description_et;
    TextView quote_title,quote_pay_type,quote_amount,quote_description,document_upload_text,quote_document;
    Spinner spinner_advance_amount;
    Button submit_btn,choosefile_btn,close_btn,choose_document;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String user_id,currency_sp_str,advance_amount,description_st,radio_btn_str,job_id,profileString,authValue,quote_title_st,paymentTypeStr;
    String[] currency = {"INR"};
    private Uri picUri;
    Intent intent;
    final int CHOOSE_CAMERA = 232;
    RelativeLayout rl_capture,rl_gallery;
    public String PickedImgPath = null;
    LinearLayout ll_quote_my_quote,ll_open_quote;
    String imagePath,uriString,ret,fileString,filename,QuoteImage;
    final int CAMERA_CAPTURE = 1;
    final int PICK_IMAGE = 2;
    private static final int PICKFILE_RESULT_CODE = 3;
    Uri returnUri;
    byte[] bytes;
    CircleImageView quote_image,quote_image_quote;
    final String URL9 = "http://www.appsapk.com/downloading/latest/UC-Browser.apk";

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
        setContentView(R.layout.activity_quote_now);
        preferenceUtils = new PreferenceUtils(QuoteNowActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        ll_open_quote = (LinearLayout) findViewById(R.id.ll_open_quote);
        ll_quote_my_quote = (LinearLayout) findViewById(R.id.ll_quote_my_quote);
        intent = getIntent();
        authValue = intent.getStringExtra("AuthValue");
        quote_title_st = intent.getStringExtra("QuoteTitle");
        job_id = intent.getStringExtra("JobId");
        if(authValue.equalsIgnoreCase("1")) {
            ll_open_quote.setVisibility(View.VISIBLE);
            ll_quote_my_quote.setVisibility(View.GONE);
        }else{
            ll_open_quote.setVisibility(View.GONE);
            ll_quote_my_quote.setVisibility(View.VISIBLE);
            myQuote();
        }
        quote_document = (TextView) findViewById(R.id.quote_document);
        quote_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= Uri.parse(URL9);
                DownloadManager downloadManager = (DownloadManager) QuoteNowActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                Toast.makeText(QuoteNowActivity.this, "File Downloading...", Toast.LENGTH_SHORT).show();
// set title and description
                request.setTitle("Data Download");
                request.setDescription("Android Data download using DownloadManager.");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"downloadfileName");
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
            }
        });

        quote_image = (CircleImageView) findViewById(R.id.quote_image);
        quote_image_quote = (CircleImageView) findViewById(R.id.quote_image_quote);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radio_advance = (RadioButton) findViewById(R.id.radio_advance);
        radio_escrow = (RadioButton) findViewById(R.id.radio_escrow);
        radio_split = (RadioButton) findViewById(R.id.radio_split);
        advance_amt_edit = (EditText) findViewById(R.id.advance_amt_edit);
        description_et = (EditText) findViewById(R.id.description_et);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        choosefile_btn = (Button) findViewById(R.id.choose_image);
        quoteInit();
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_advance:
                        paymentTypeStr = "instant";
                      /*  Intent i = new Intent(QuoteNowActivity.this, MapsActivity.class);
                        i.putExtra("activity", "service");
                        i.putExtra("type", "radius");
                        startActivity(i);*/
                        break;
                   /* case R.id.p2p_radiobtn:
                        actionValue = "point to point";
                        Intent intent = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                        intent.putExtra("activity", "service");
                        intent.putExtra("type", "p2p");
                        startActivity(intent);
                        break;*/
                }
               // Toast.makeText(getApplicationContext(), radio_btn_str, Toast.LENGTH_SHORT).show();
            }
        });
        spinner_advance_amount = (Spinner) findViewById(R.id.spinner_advance_amount);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,currency);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_advance_amount.setAdapter(aa);
        spinner_advance_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency_sp_str = parent.getItemAtPosition(position).toString();
                //preferenceUtils.saveString(PreferenceUtils.QUOTE_CURRENCY,currency_sp_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        choosefile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customdialog = new Dialog(QuoteNowActivity.this);
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

        backarrow = (ImageView) findViewById(R.id.backarrow);
        close = (ImageView) findViewById(R.id.close);
        document_upload_text = (TextView) findViewById(R.id.document_upload_text);
        advance_amt_edit.setFocusableInTouchMode(false);
        advance_amt_edit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                advance_amt_edit.setFocusableInTouchMode(true);
                advance_amt_edit.requestFocus();
                return false;
            }
        });
        description_et.setFocusableInTouchMode(false);
        description_et.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                description_et.setFocusableInTouchMode(true);
                description_et.requestFocus();
                return false;
            }
        });
        choose_document = (Button) findViewById(R.id.choose_document);
        choose_document.setOnClickListener(new View.OnClickListener() {
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
//                Intent getFile = new Intent(Intent.ACTION_GET_CONTENT);
//                getFile.setType("*/*");
//                startActivityForResult(getFile, PICKFILE_RESULT_CODE);
             /*   Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);*/
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJsonObject();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void quoteInit() {
        close_btn = (Button) findViewById(R.id.close_btn);
        quote_title = (TextView) findViewById(R.id.quote_title);
        quote_pay_type = (TextView) findViewById(R.id.quote_pay_type);
        quote_amount = (TextView) findViewById(R.id.quote_amount);
        quote_description = (TextView) findViewById(R.id.quote_description);
        close_quote = (ImageView) findViewById(R.id.close_quote);
        close_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
//                    profile_img.setImageURI(picUri);
                    quote_image.setImageBitmap(bitmapImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                profile_img.setImageURI(Uri.parse(imagePath));
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
                                document_upload_text.setText(filename);
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
    private Uri getOutputImageFileUri(int mediaTypeImage) {
        return Uri.fromFile(getOutputImageFile(mediaTypeImage));
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
        quote_image.setImageBitmap(bmp);
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(QuoteNowActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void setJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("job_id", job_id);
            jsonObj.put("discription", description_et.getText().toString());
            jsonObj.put("instant", advance_amt_edit.getText().toString());
            jsonObj.put("currency", "INR");
            jsonObj.put("pay_type", paymentTypeStr);
            jsonObj.put("image", profileString);
            jsonObj.put("document", fileString);

            androidNetworkingQuote(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingQuote(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Bidcontractservice/bidopenquote")
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
                                Toast.makeText(QuoteNowActivity.this, "Your Quote Submitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(QuoteNowActivity.this, BidContractsActivity.class);
                                i.putExtra("Processed","2");
                                startActivity(i);
                            } else {
                                Toast.makeText(QuoteNowActivity.this, "Service not added", Toast.LENGTH_SHORT).show();
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

    private void myQuote() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BidContractsApi service = retrofit.create(BidContractsApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.quoteMyQuote(user_id,job_id);

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
                                        String image_doc_path = root.getString("image_doc_path");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            JSONObject jsonObject = root.getJSONObject("attach");
                                            String id = jsonObject.getString("id");
                                            String job_id = jsonObject.getString("job_id");
                                            String user_id = jsonObject.getString("user_id");
                                            String description = jsonObject.getString("description");
                                            String image = jsonObject.getString("image");
                                            QuoteImage = image_doc_path+image;
                                            String appattachment = jsonObject.getString("appattachment");
                                            String approve_status = jsonObject.getString("approve_status");
                                            quote_title.setText(quote_title_st);
                                            quote_description.setText(description);

                                            JSONObject jsonObject1 = root.getJSONObject("qujob");
                                            if(jsonObject1 != null && !jsonObject1.toString().equals("null")) {
                                                String id1 = jsonObject1.getString("id");
                                                String job_id1 = jsonObject1.getString("job_id");
                                                String user_id1 = jsonObject1.getString("user_id");
                                                String instant_amount = jsonObject1.getString("instant_amount");
                                                String after_amount = jsonObject1.getString("after_amount");
                                                String payment = jsonObject1.getString("payment");
                                                String currency = jsonObject1.getString("currency");
                                                String bid_pay_type = jsonObject1.getString("bid_pay_type");
                                                String bid_pay = jsonObject1.getString("bid_pay");
                                                String milestoneapp = jsonObject1.getString("milestoneapp");
                                                String status = jsonObject1.getString("status");
                                                String complete = jsonObject1.getString("complete");
                                                String milestonecom = jsonObject1.getString("milestonecom");
                                                String on_date = jsonObject1.getString("on_date");

                                                quote_title.setText(quote_title_st);
                                                quote_pay_type.setText(payment);
                                                quote_amount.setText(currency+" "+instant_amount);
                                                quote_description.setText(description);
                                                quote_document.setText(appattachment);
                                                if(image != null) {
                                                    Picasso.get().load(QuoteImage)
                                                            .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(quote_image_quote);
                                                }else{
                                                    Picasso.get().load(R.drawable.no_image_available)
                                                            .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(quote_image_quote);
                                                }
                                            }else{
                                                quote_title.setText(quote_title_st);
                                                quote_pay_type.setText("");
                                                quote_amount.setText(""+" "+"");
                                                quote_description.setText(description);
                                            }
                                            progressDialog.dismiss();

                                        } else {
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
                // Toast.makeText(QuoteNowActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
