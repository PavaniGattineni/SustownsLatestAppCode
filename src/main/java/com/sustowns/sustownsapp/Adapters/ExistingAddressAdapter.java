package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Models.GetAddressModel;

import java.util.ArrayList;

import static com.sustowns.sustownsapp.Activities.ProductDetailsActivity.drop_location;

public class ExistingAddressAdapter extends RecyclerView.Adapter<ExistingAddressAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String[] address;
    String[] name;
    ArrayList<GetAddressModel> getAddressModels;
    ProgressDialog progressDialog;
    private final ArrayList<Integer> selected = new ArrayList<>();
    private int lastCheckedPosition = -1;
    SparseBooleanArray mSelectedItemsListIds;
    String[] addresses;
    public static String radioText = "";
    public ExistingAddressAdapter(Context context, ArrayList<GetAddressModel> getAddressModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.getAddressModels = getAddressModels;
        preferenceUtils = new PreferenceUtils(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.markets_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.ll_existing_addresses.setVisibility(View.VISIBLE);
        viewHolder.ll_markets.setVisibility(View.GONE);

        if(getAddressModels.get(position) != null){
            viewHolder.name_address.setText(getAddressModels.get(position).getName());
            viewHolder.address_text.setText(getAddressModels.get(position).getAddress1()+"\n"+getAddressModels.get(position).getCity_name()+","+
                                            getAddressModels.get(position).getState()+"\n"+getAddressModels.get(position).getCountry_name()+","+
                                             getAddressModels.get(position).getZipcode());
        }
                viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=group.getCheckedRadioButtonId();
                String addressId = String.valueOf(id);
             //   preferenceUtils.saveString(PreferenceUtils.AddressId,addressId);

                radioText = viewHolder.name_address.getText().toString();
                drop_location.setText(radioText);

                // Toast.makeText(context, radioText, Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return getAddressModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton name_address;
        RadioGroup radioGroup;
        LinearLayout ll_existing_addresses,ll_markets;
        TextView address_text;
        public ViewHolder(View view) {
            super(view);
            name_address = (RadioButton) view.findViewById(R.id.name_address);
            radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
            ll_existing_addresses = (LinearLayout) view.findViewById(R.id.ll_existing_addresses);
            ll_markets = (LinearLayout) view.findViewById(R.id.ll_markets);
            address_text = (TextView) view.findViewById(R.id.address_text);
            // Handle item click and set the selection

        }
    }
}
