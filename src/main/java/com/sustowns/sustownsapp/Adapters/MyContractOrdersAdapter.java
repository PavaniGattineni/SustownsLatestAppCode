package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.sustowns.sustownsapp.Activities.AddPaymentActivity;
import com.sustowns.sustownsapp.Activities.ContractOrdersInvoice;
import com.sustowns.sustownsapp.Activities.LocationDialogActivity;
import com.sustowns.sustownsapp.Activities.MyContractOrdersActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.ContractPurchasesModel;
import com.sustowns.sustownsapp.helpers.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyContractOrdersAdapter extends RecyclerView.Adapter<MyContractOrdersAdapter.ViewHolder> {
    Context context;
    ImageView backarrow_dialog;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,addressId,user_role,order_status,pay_method,orderid,clickedSearch = "";
    PreferenceUtils preferenceUtils;
    String orderId,invoiceNo,complete_amount_status,countryId,stateId,cityId;
    ArrayList<ContractPurchasesModel> contractOrderModels;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    LinearLayout ll_shipping_details,ll_existing_address;
    RecyclerView recyclerview_saved_addresses;
    Helper helper;
    public static TextView address_txt_map_dialog;
    TextView saved_address_text,title_address_change,spinner_countrydialog,address_state, address_town;
    String id,nameAddress="",companyAddress="", emailAddress="", firstnameAddress="", lastnameAddress="", address2Address="", addressState="", addressTown="", mobileAddress="", pincodeAddress="", faxAddress="";
    EditText name_address,company_address, email_address, first_name_address,last_name_address, address1_address, address2_address,mobile_address, pincode_address, fax_address;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> statesList = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
    int textlength = 0;
    ArrayList<String> selectedCountryList = new ArrayList<String>();
    ArrayList<String> selectedCountryIdList = new ArrayList<String>();
    public static String Address = "";

    public MyContractOrdersAdapter(Context context, ArrayList<ContractPurchasesModel> contractOrderModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contractOrderModels = contractOrderModels;
        helper = new Helper(context);
        preferenceUtils = new PreferenceUtils(context);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        addressId = preferenceUtils.getStringFromPreference(PreferenceUtils.AddressId,"");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.add_payment_btn.setVisibility(View.GONE);
        viewHolder.add_transport_btn.setVisibility(View.GONE);
        viewHolder.view_invoice_btn.setVisibility(View.VISIBLE);
        order_status = contractOrderModels.get(position).getOrder_status();
        complete_amount_status = contractOrderModels.get(position).getComplete_amount_status();
        pay_method = contractOrderModels.get(position).getPay_type();
        if (contractOrderModels.get(position) != null) {
            viewHolder.orderName.setText(contractOrderModels.get(position).getJob_name());
            viewHolder.order_no.setText(contractOrderModels.get(position).getInvoice_no());
            viewHolder.orderDate.setText(contractOrderModels.get(position).getOrder_date());
            viewHolder.orderPrice.setText(contractOrderModels.get(position).getTotalprice());
            if (order_status.equalsIgnoreCase("0")) {
                viewHolder.confirm_order_btn.setVisibility(View.VISIBLE);
                viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
                viewHolder.orderStatus.setText("Pending");
            } else if (order_status.equalsIgnoreCase("1")) {
                viewHolder.confirm_order_btn.setVisibility(View.GONE);
                viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Completed");
            } else if (order_status.equalsIgnoreCase("2")) {
                viewHolder.orderStatus.setText("Cancelled");
            }
        }
        viewHolder.confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderid = contractOrderModels.get(position).getOrder_id();
                Intent i = new Intent(context, LocationDialogActivity.class);
                i.putExtra("ContractLocation","1");
                i.putExtra("OrderId",orderid);
                i.putExtra("InvoiceNo","");
                i.putExtra("RandId","");
                context.startActivity(i);
               /* customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.drop_location_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                ll_shipping_details = (LinearLayout) customdialog.findViewById(R.id.ll_shipping_details);
                ll_existing_address = (LinearLayout) customdialog.findViewById(R.id.ll_existing_address);
                recyclerview_saved_addresses = (RecyclerView) customdialog.findViewById(R.id.recyclerview_saved_addresses);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerview_saved_addresses.setLayoutManager(layoutManager1);
                saved_address_text = (TextView) customdialog.findViewById(R.id.saved_address_text);
                save_address_btn = (Button) customdialog.findViewById(R.id.save_address_btn);
                name_address = (EditText) customdialog.findViewById(R.id.name_address);
                company_address = (EditText) customdialog.findViewById(R.id.company_address);
                email_address = (EditText) customdialog.findViewById(R.id.email_address);
                first_name_address = (EditText) customdialog.findViewById(R.id.first_name_address);
                title_address_change = (TextView) customdialog.findViewById(R.id.title_address_change);
                title_address_change.setVisibility(View.GONE);
                spinner_countrydialog = (TextView) customdialog.findViewById(R.id.spinner_countrydialog);
                spinner_countrydialog.setOnClickListener(new View.OnClickListener() {
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
                        Intent mapIntent = new Intent(context, MapsActivity.class);
                        mapIntent.putExtra("activity", "product");
                        mapIntent.putExtra("type", "none");
                        context.startActivity(mapIntent);
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
                backarrow_dialog = (ImageView) customdialog.findViewById(R.id.backarrow_dialog);
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
                existing_radiobtn.setChecked(true);
                ll_existing_address.setVisibility(View.VISIBLE);
                ll_shipping_details.setVisibility(View.GONE);
                getExistingAddresses();
                new_radiobtn = (RadioButton) customdialog.findViewById(R.id.new_radiobtn);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.existing_radiobtn:
                                actionValue = "existing";
                                ll_existing_address.setVisibility(View.VISIBLE);
                                ll_shipping_details.setVisibility(View.GONE);
                                getExistingAddresses();
                                // do operations specific to this selection
                                break;
                            case R.id.new_radiobtn:
                                actionValue = "new";
                                ll_shipping_details.setVisibility(View.VISIBLE);
                                ll_existing_address.setVisibility(View.GONE);
                                // do operations specific to this selection
                                break;
                        }
                    }
                });
                save_address = (Button) customdialog.findViewById(R.id.save_address);
                save_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitConfirmOrder();
                    }
                });
                save_address_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameAddress = name_address.getText().toString().trim();
                        companyAddress = company_address.getText().toString().trim();
                        emailAddress = email_address.getText().toString().trim();
                        firstnameAddress = first_name_address.getText().toString().trim();
                        lastnameAddress = last_name_address.getText().toString().trim();
                        address2Address = address2_address.getText().toString().trim();
                        addressState = address_state.getText().toString().trim();
                        addressTown = address_town.getText().toString().trim();
                        mobileAddress = mobile_address.getText().toString().trim();
                        pincodeAddress = pincode_address.getText().toString().trim();
                        faxAddress = fax_address.getText().toString().trim();
                        submitConfirmOrder();
                    }
                });

                close_drop_dialog = (Button) customdialog.findViewById(R.id.close_drop_dialog);
                close_drop_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                backarrow_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();*/
            }
        });
        viewHolder.cancel_orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderid = contractOrderModels.get(position).getOrder_id();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure you want to Cancel...?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                submitCancelOrder();
                              //  customdialog.show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddPaymentActivity.class);
                i.putExtra("OrderId",contractOrderModels.get(position).getOrder_id());
                i.putExtra("BankRandId",contractOrderModels.get(position).getBank_thr_ran_id());
                i.putExtra("RandId",contractOrderModels.get(position).getInvoice_no());
                i.putExtra("ContractOrders","2");
                context.startActivity(i);
            }
        });
        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = contractOrderModels.get(position).getOrder_id();
                invoiceNo = contractOrderModels.get(position).getInvoice_no();
                Intent intent = new Intent(context, ContractOrdersInvoice.class);
                intent.putExtra("OrderId",orderId);
                intent.putExtra("InvoiceNo",invoiceNo);
                intent.putExtra("Status",contractOrderModels.get(position).getOrder_status());
                context.startActivity(intent);

            }
        });

    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
*/
  /*  @Override
    protected void onResume() {
        super.onResume();
        address_txt_map_dialog.setText(Address);
    }
*/
    public void submitCancelOrder() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("orderid", orderid);
            androidNetworkingCancel(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingCancel(JSONObject jsonObject){
        //progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/cancelorder")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                ((MyContractOrdersActivity)context).getContractOrdersList();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                    }
                });
    }

    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return contractOrderModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, order_no, orderDate, orderStatus,orderPrice;
        Button add_payment_btn, confirm_order_btn;
        Button cancel_orderbtn,add_transport_btn,view_invoice_btn;
        LinearLayout ll_status;

        public ViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.order_name);
            order_no = (TextView) view.findViewById(R.id.order_no);
            orderDate = (TextView) view.findViewById(R.id.order_date);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            add_payment_btn = (Button) view.findViewById(R.id.add_payment_btn);
            confirm_order_btn = (Button) view.findViewById(R.id.confirm_order_btn);
            add_transport_btn = (Button) view.findViewById(R.id.add_transport_btn);
            cancel_orderbtn = (Button) view.findViewById(R.id.cancel_orderbtn);
            orderPrice = (TextView) view.findViewById(R.id.order_price);
            ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
            view_invoice_btn = (Button) view.findViewById(R.id.view_invoice_btn);

        }
    }

}
