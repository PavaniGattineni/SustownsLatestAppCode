package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Adapters.NotificationAdapter;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView notification_recyclerview;
    NotificationAdapter notificationAdapter;
    String[] notifications = {"Notification1","notification2","notification3","notification4"};
    String[] not_des = {"notification description","notification description 1","notification description 3","notification description 4"};
    ImageView backarrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        notification_recyclerview = (RecyclerView) findViewById(R.id.notification_recyclerview);
        LinearLayoutManager lManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        notification_recyclerview.setLayoutManager(lManager);
        notificationAdapter = new NotificationAdapter(NotificationActivity.this,notifications,not_des);
        notification_recyclerview.setAdapter(notificationAdapter);
    }
}
