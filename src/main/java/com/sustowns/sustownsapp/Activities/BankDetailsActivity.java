package com.sustowns.sustownsapp.Activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonElement;
import com.payu.custombrowser.Bank;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.ImageModel;
import com.sustowns.sustownsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sustowns.sustownsapp.Activities.FileUtils.getPath;

public class BankDetailsActivity extends AppCompatActivity {

    EditText bank_acc_name,bank_acc_address,bank_account_no,bank_name,bank_country,branch_name,branch_address,swift_number,iban_number,national_routing_no,
               business_number,tax_id_no;
    Button choose_file_bank,save_Details_bank,update_Details_bank;
    Integer REQUEST_CODE_DOC = 1;
    private static final int PICKFILE_RESULT_CODE = 3;
    Uri returnUri;
    String filename,ret,fileString,user_id;
    TextView document_name;
    byte[] bytes;
    List<String> documentArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    ImageView backarrow_bank;
    String URL2 = "http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk";
    String URL9 = "http://www.appsapk.com/downloading/latest/UC-Browser.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bank_details);
        preferenceUtils = new PreferenceUtils(BankDetailsActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        bank_acc_name = (EditText) findViewById(R.id.bank_acc_name);
        bank_acc_address = (EditText) findViewById(R.id.bank_acc_address);
        bank_account_no = (EditText) findViewById(R.id.bank_account_no);
        bank_name = (EditText) findViewById(R.id.bank_name);
        bank_country = (EditText) findViewById(R.id.bank_country);
        branch_name = (EditText) findViewById(R.id.branch_name);
        branch_address = (EditText) findViewById(R.id.branch_address);
        swift_number = (EditText) findViewById(R.id.swift_number);
        iban_number = (EditText) findViewById(R.id.iban_number);
        national_routing_no = (EditText) findViewById(R.id.national_routing_no);
        business_number = (EditText) findViewById(R.id.business_number);
        tax_id_no = (EditText) findViewById(R.id.tax_id_no);
        choose_file_bank = (Button) findViewById(R.id.choose_file_bank);
        document_name = (TextView) findViewById(R.id.document_name);
        save_Details_bank = (Button) findViewById(R.id.save_Details_bank);
        update_Details_bank = (Button) findViewById(R.id.update_Details_bank);
        backarrow_bank = (ImageView) findViewById(R.id.backarrow_bank);
        choose_file_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getFile = new Intent(Intent.ACTION_GET_CONTENT);
                getFile.setType("*/*");
                startActivityForResult(getFile, PICKFILE_RESULT_CODE);
            }
        });
        save_Details_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bank_acc_name.getText().toString().isEmpty()||bank_acc_address.getText().toString().isEmpty()||bank_account_no.getText().toString().isEmpty()||bank_name.getText().toString().isEmpty()||
                        bank_country.getText().toString().isEmpty()||branch_name.getText().toString().isEmpty()||branch_address.getText().toString().isEmpty()||national_routing_no.getText().toString().isEmpty()){
                    Toast.makeText(BankDetailsActivity.this, "Please fill mandatory(*) fields", Toast.LENGTH_SHORT).show();
                }else {
                        setJsonObject();
                }
            }
        });
        update_Details_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bank_acc_name.getText().toString().isEmpty()||bank_acc_address.getText().toString().isEmpty()||bank_account_no.getText().toString().isEmpty()||bank_name.getText().toString().isEmpty()||
                        bank_country.getText().toString().isEmpty()||branch_name.getText().toString().isEmpty()||branch_address.getText().toString().isEmpty()||national_routing_no.getText().toString().isEmpty()){
                    Toast.makeText(BankDetailsActivity.this, "Please fill mandatory(*) fields", Toast.LENGTH_SHORT).show();
                }else {
                        setUpdateJsonObject();
                    }
            }
        });
        document_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= Uri.parse(URL9);
                DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                Toast.makeText(BankDetailsActivity.this, "File Downloading...", Toast.LENGTH_SHORT).show();
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
        backarrow_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBankDetails();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void progressdialog() {
        progressDialog = new ProgressDialog(BankDetailsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getBankDetails() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBankDetailsList(user_id);
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
                                        String busdadgesimg = root.getString("busdadgesimg");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONObject jsonObject = root.getJSONObject("bankdetails");
                                            if (jsonObject != null) {
                                                save_Details_bank.setVisibility(View.GONE);
                                                update_Details_bank.setVisibility(View.VISIBLE);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String accname = jsonObject.getString("accname");
                                                String accadd = jsonObject.getString("accadd");
                                                String accountno = jsonObject.getString("accountno");
                                                String bankName = jsonObject.getString("bank_name");
                                                String bankCountry = jsonObject.getString("bank_country");
                                                String branchName = jsonObject.getString("branch_name");
                                                String branch_addr = jsonObject.getString("branch_addr");
                                                String swift = jsonObject.getString("swift");
                                                String iban = jsonObject.getString("iban");
                                                String ifsc = jsonObject.getString("ifsc");
                                                String bus_num = jsonObject.getString("bus_num");
                                                String txa_id = jsonObject.getString("txa_id");
                                                String documentation = jsonObject.getString("documentation");
                                                String status = jsonObject.getString("status");

                                                bank_acc_name.setText(accname);
                                                bank_acc_address.setText(accadd);
                                                bank_account_no.setText(accountno);
                                                bank_name.setText(bankName);
                                                bank_country.setText(bankCountry);
                                                branch_name.setText(branchName);
                                                branch_address.setText(branch_addr);
                                                swift_number.setText(swift);
                                                iban_number.setText(iban);
                                                national_routing_no.setText(ifsc);
                                                business_number.setText(bus_num);
                                                tax_id_no.setText(txa_id);
                                                document_name.setText(documentation);
                                                progressDialog.dismiss();
                                            } else {
                                                save_Details_bank.setVisibility(View.VISIBLE);
                                                update_Details_bank.setVisibility(View.GONE);
                                                bank_acc_name.setText("");
                                                bank_acc_address.setText("");
                                                bank_account_no.setText("");
                                                bank_name.setText("");
                                                bank_country.setText("");
                                                branch_name.setText("");
                                                branch_address.setText("");
                                                swift_number.setText("");
                                                iban_number.setText("");
                                                national_routing_no.setText("");
                                                business_number.setText("");
                                                tax_id_no.setText("");
                                                document_name.setText("");
                                                progressDialog.dismiss();
                                            }
                                        }
                                        else {
                                            progressDialog.dismiss();
                                           // Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BankDetailsActivity.this, "Something went wrong!Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void setJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("bank_name", bank_name.getText().toString());
            jsonObj.put("accname",bank_acc_name.getText().toString());
            jsonObj.put("accadd",bank_acc_address.getText().toString());
            jsonObj.put("accountno",bank_account_no.getText().toString());
            jsonObj.put("bank_country", bank_country.getText().toString());
            jsonObj.put("branch_name",branch_name.getText().toString());
            jsonObj.put("branch_addr",branch_address.getText().toString());
            jsonObj.put("swift",swift_number.getText().toString());
            jsonObj.put("iban",iban_number.getText().toString());
            jsonObj.put("ifsc",national_routing_no.getText().toString());
            jsonObj.put("bus_num",business_number.getText().toString());
            jsonObj.put("txa_id",tax_id_no.getText().toString());
            jsonObj.put("image",fileString);
            androidNetworkingSaveBankDetails(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setUpdateJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userid", user_id);
            jsonObj.put("bank_name", bank_name.getText().toString());
            jsonObj.put("accname",bank_acc_name.getText().toString());
            jsonObj.put("accadd",bank_acc_address.getText().toString());
            jsonObj.put("accountno",bank_account_no.getText().toString());
            jsonObj.put("bank_country", bank_country.getText().toString());
            jsonObj.put("branch_name",branch_name.getText().toString());
            jsonObj.put("branch_addr",branch_address.getText().toString());
            jsonObj.put("swift",swift_number.getText().toString());
            jsonObj.put("iban",iban_number.getText().toString());
            jsonObj.put("ifsc",national_routing_no.getText().toString());
            jsonObj.put("bus_num",business_number.getText().toString());
            jsonObj.put("txa_id",tax_id_no.getText().toString());
            jsonObj.put("image",fileString);
            androidNetworkingUpdateBankDetails(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingSaveBankDetails(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/bank_details")
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
                                JSONObject responseOj = response.getJSONObject("response");
                                String message = responseOj.getString("message");
                                Toast.makeText(BankDetailsActivity.this, "Bank details saved successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BankDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
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
    public void androidNetworkingUpdateBankDetails(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/update_bankdetails")
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
                                JSONObject responseOj = response.getJSONObject("response");
                                String message = responseOj.getString("message");
                                Toast.makeText(BankDetailsActivity.this, "Bank details Updated successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BankDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
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
}
