package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Activities.NotificationActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    String[] notifications;
    String[] not_des;
    String[] content;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    // ArrayList<CartListModel> cartListModels;
    ProgressDialog progressDialog;

    public NotificationAdapter(NotificationActivity context, String[] notifications, String[] not_des) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notifications = notifications;
        this.not_des = not_des;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.notification.setText(notifications[position]);
       // viewHolder.not_description.setText(date[position]);
      //  viewHolder.not_date.setText(content[position]);

    }
    @Override
    public int getItemCount() {
        return notifications.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification, not_description, not_date;

        public ViewHolder(View view) {
            super(view);
            notification = (TextView) view.findViewById(R.id.notification);
            not_description = (TextView) view.findViewById(R.id.not_description);
            not_date = (TextView) view.findViewById(R.id.not_date);


        }
    }
}
