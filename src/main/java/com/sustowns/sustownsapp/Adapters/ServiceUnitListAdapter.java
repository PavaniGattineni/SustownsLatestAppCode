package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sustowns.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.sustowns.sustownsapp.Activities.AddShippingServices;
import com.sustowns.sustownsapp.Activities.ServiceManagementActivity;
import com.sustowns.sustownsapp.Models.ServiceUnitModel;
import com.sustowns.sustownsapp.helpers.Helper;

import java.util.ArrayList;
import java.util.List;

public class ServiceUnitListAdapter extends RecyclerView.Adapter<ServiceUnitListAdapter.ServiceViewHolder> {
    Context context;
    List<ServiceUnitModel> unitDisplayList;
    List<ServiceUnitModel> unitList;
    Helper helper;
    boolean isPartial;
    AlertDialog alertDialog;
    String Shipping;

    public ServiceUnitListAdapter(Context context, List<ServiceUnitModel> unitDisplayList, List<ServiceUnitModel> unitList, boolean isPartial, String Shipping) {
        this.context = context;
        this.unitDisplayList = unitDisplayList;
        this.unitList = unitList;
        this.isPartial = isPartial;
        this.Shipping = Shipping;
        helper = new Helper(context);
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item, viewGroup, false);
        return new ServiceUnitListAdapter.ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceViewHolder serviceViewHolder, final int position) {
        serviceViewHolder.unit_item_name.setText(unitDisplayList.get(position).getUnit_name());

        if (unitList.get(position) != null) {
            for (int i = 0; i < unitDisplayList.size(); i++) {
                if (isPartial) {
                    serviceViewHolder.ll_min_load.setVisibility(View.VISIBLE);
                    serviceViewHolder.charge_km_edit.getText().toString();
                    serviceViewHolder.minimum_charge_edit.getText().toString();
                    serviceViewHolder.min_load_edit.getText().toString();
                    serviceViewHolder.max_load_edit.getText().toString();
                    serviceViewHolder.charge_km_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setCharge(serviceViewHolder.charge_km_edit.getText().toString());
                        }
                    });
                    serviceViewHolder.minimum_charge_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setMin_charge(serviceViewHolder.minimum_charge_edit.getText().toString());

                        }
                    });
                    serviceViewHolder.min_load_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setMin_load(serviceViewHolder.min_load_edit.getText().toString());

                        }
                    });
                    serviceViewHolder.max_load_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setMax_load(serviceViewHolder.max_load_edit.getText().toString());

                        }
                    });


                } else {
                    serviceViewHolder.ll_min_load.setVisibility(View.GONE);
                    serviceViewHolder.charge_km_edit.getText().toString();
                    serviceViewHolder.minimum_charge_edit.getText().toString();
                    serviceViewHolder.max_load_edit.getText().toString();
                    serviceViewHolder.charge_km_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setCharge(serviceViewHolder.charge_km_edit.getText().toString());
                        }
                    });
                    serviceViewHolder.minimum_charge_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setMin_charge(serviceViewHolder.minimum_charge_edit.getText().toString());

                        }
                    });
                    serviceViewHolder.max_load_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            unitDisplayList.get(position).setMax_load(serviceViewHolder.max_load_edit.getText().toString());

                        }
                    });
                }
                //serviceViewHolder
            }
        }
        serviceViewHolder.delete_unit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemByPosition(position);
              /*  unitDisplayList.remove(position);
                notifyItemRemoved(position);

                if (unitDisplayList.size() == 0) {
                    if (isPartial) {
                        ((ServiceManagementActivity) context).hidePartialRecyclerview();
                    } else {
                        ((ServiceManagementActivity) context).hideFullRecyclerview();
                    }
                }*/
            }
        });

        serviceViewHolder.unit_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitNameAlert(serviceViewHolder, position);
            }
        });

    }
    private boolean removeItemByPosition(int position){
        try{
            unitDisplayList.remove(position);
            notifyDataSetChanged();
            if (unitDisplayList.size() == 0) {
                if (isPartial) {
                    if(Shipping.equalsIgnoreCase("1")){
                        ((AddShippingServices) context).hidePartialRecyclerview();
                    }else {
                        ((ServiceManagementActivity) context).hidePartialRecyclerview();
                    }
                } else {
                    if(Shipping.equalsIgnoreCase("1")){
                        ((AddShippingServices) context).hideFullRecyclerview();
                    }else {
                        ((ServiceManagementActivity) context).hideFullRecyclerview();
                    }
                }
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public int getItemCount() {
        return unitDisplayList.size();
    }

    private void showUnitNameAlert(final ServiceViewHolder serviceViewHolder, final int itemPosition) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.custom_list_layout, null);
            dialogBuilder.setView(dialogView);

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
            title.setText("Choose Unit");

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
            final List<String> unitNameList = new ArrayList<>();
            for (ServiceUnitModel unitModel : unitList) {
                unitNameList.add(unitModel.getUnit_name());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.simple_list_item, R.id.list_item_txt, unitNameList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);

            // ListView Item Click Listener
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String itemValue = unitNameList.get(position);
                    if (isNameAvailable(itemValue)) {
                        Toast.makeText(context, "Unit already exists. Please select another unit.", Toast.LENGTH_SHORT).show();
                    } else {
                        serviceViewHolder.unit_item_name.setText(itemValue);
                        unitDisplayList.remove(itemPosition);
                        unitDisplayList.add(unitList.get(position));
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNameAvailable(String name) {
        for (int i = 0; i < unitDisplayList.size(); i++) {
            if (unitDisplayList.get(i).getUnit_name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView unit_item_name;
        ImageView delete_unit_item;
        EditText charge_km_edit, minimum_charge_edit, min_load_edit, max_load_edit;
        LinearLayout ll_min_load;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            unit_item_name = itemView.findViewById(R.id.unit_item_name);
            delete_unit_item = itemView.findViewById(R.id.delete_unit_item);
            charge_km_edit = itemView.findViewById(R.id.charge_km_edit);
            minimum_charge_edit = itemView.findViewById(R.id.minimum_charge_edit);
            min_load_edit = itemView.findViewById(R.id.min_load_edit);
            max_load_edit = itemView.findViewById(R.id.max_load_edit);
            ll_min_load = itemView.findViewById(R.id.ll_min_load);
        }

    }
}
