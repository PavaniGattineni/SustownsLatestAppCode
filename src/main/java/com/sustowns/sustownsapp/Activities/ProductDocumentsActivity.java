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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;

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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sustowns.sustownsapp.Activities.FileUtils.getPath;

public class ProductDocumentsActivity extends AppCompatActivity {
    String imagePath,profileString = "",titleStr,descriptionStr,job_id,bidId,documentStr;
    final int CAMERA_CAPTURE = 1;
    final int PICK_IMAGE = 2;
    RelativeLayout rl_capture,rl_gallery;
    Intent intent;
    EditText description_edit;
    Button chooseimage_btn,choosedoc_btn,submit,close_dialog;
    CircleImageView profile_image;
    TextView title,approve_doc_text,complete_document;
    public String PickedImgPath = null;
    String name,user_id,Description,filename,ret,fileString;
    private int requestCode;
    private int resultCode;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    private static final int PICKFILE_RESULT_CODE = 3;
    Uri returnUri;
    byte[] bytes;
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
        setContentView(R.layout.add_document_dialog);
        try {
            preferenceUtils = new PreferenceUtils(ProductDocumentsActivity.this);
            user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
            // checkAndRequestPermissions();
            //checkAndroidVersion();
            titleStr = getIntent().getStringExtra("Title");
            job_id = getIntent().getStringExtra("JobId");
            bidId = getIntent().getStringExtra("BidId");
            documentStr = getIntent().getStringExtra("Documents");

            title = (TextView) findViewById(R.id.title);
            description_edit = (EditText) findViewById(R.id.description_edit);
            description_edit.setFocusableInTouchMode(false);
            description_edit.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    description_edit.setFocusableInTouchMode(true);
                    description_edit.requestFocus();
                    return false;
                }
            });
            chooseimage_btn = (Button) findViewById(R.id.chooseimage_btn);
            profile_image = (CircleImageView) findViewById(R.id.profile_image);
            choosedoc_btn = (Button) findViewById(R.id.choosedoc_btn);
            submit = (Button) findViewById(R.id.submit);
            close_dialog = (Button) findViewById(R.id.close_dialog);
            title.setText(titleStr);
            approve_doc_text = (TextView) findViewById(R.id.approve_doc_text);
            complete_document = (TextView) findViewById(R.id.complete_document);
            complete_document.setText(documentStr);
            complete_document.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri= Uri.parse(URL9);
                    DownloadManager downloadManager = (DownloadManager) ProductDocumentsActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                            DownloadManager.Request.NETWORK_MOBILE);
                    Toast.makeText(ProductDocumentsActivity.this, "File Downloading...", Toast.LENGTH_SHORT).show();
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

            chooseimage_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog customdialog = new Dialog(ProductDocumentsActivity.this);
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
            choosedoc_btn.setOnClickListener(new View.OnClickListener() {
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

            close_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Description = description_edit.getText().toString().trim();
                    if(Description.equalsIgnoreCase("")|| Description.equalsIgnoreCase(null)){
                        Toast.makeText(ProductDocumentsActivity.this, "Please fill Empty fields", Toast.LENGTH_SHORT).show();
                    }else {
                        setJsonObject();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
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
                    profile_image.setImageBitmap(bitmapImage);
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
                            approve_doc_text.setText(filename);
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
    /*  private Uri getOutputImageFileUri(int mediaTypeImage) {
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
        } return mediaFile;
    }*/

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
        profile_image.setImageBitmap(bmp);

        //imageView_profile.setImageBitmap(null);
        //   profile_img.setImageBitmap(thumbnail);
        // preferenceUtils.saveString(PreferenceUtils.Image, PickedImgPath);
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(ProductDocumentsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void setJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("user_id", user_id);
            jsonObj.put("job_id", job_id);
            jsonObj.put("discription", description_edit.getText().toString());
            jsonObj.put("quote_id",bidId);
            jsonObj.put("image", profileString);
            jsonObj.put("document", fileString);

            androidNetworkingApproveQuote(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingApproveQuote(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Bidcontractservice/bidapprovequote")
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
                                Toast.makeText(ProductDocumentsActivity.this, "Your Quote Submitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ProductDocumentsActivity.this, BidContractsActivity.class);
                                i.putExtra("Processed","1");
                                startActivity(i);
                            } else {
                                Toast.makeText(ProductDocumentsActivity.this, "Quote not submitted", Toast.LENGTH_SHORT).show();
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

}
