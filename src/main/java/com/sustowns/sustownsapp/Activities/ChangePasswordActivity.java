package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sustowns.sustownsapp.R;

public class ChangePasswordActivity extends AppCompatActivity {
    ImageView backarrow;
    EditText old_password,new_password,confirm_password;
    String password_old_str,create_cart_store,cust_id,password_new_str,password_confirm_str,password;
    PreferenceUtils preferenceUtils;
    LinearLayout ll_change_password,ll_permissions;
    Button submit,permissions_btn,change_password_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);
        preferenceUtils = new PreferenceUtils(ChangePasswordActivity.this);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        submit = (Button) findViewById(R.id.submit);
        change_password_btn = (Button) findViewById(R.id.change_password_btn);
        permissions_btn = (Button) findViewById(R.id.permissions_btn);
        ll_change_password = (LinearLayout) findViewById(R.id.ll_change_password);
        ll_permissions = (LinearLayout) findViewById(R.id.ll_permissions);

        old_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                old_password.setFocusableInTouchMode(true);
                old_password.requestFocus();
                return false;
            }
        });
        new_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new_password.setFocusableInTouchMode(true);
                new_password.requestFocus();
                return false;
            }
        });
        confirm_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                confirm_password.setFocusableInTouchMode(true);
                confirm_password.requestFocus();
                return false;
            }
        });
        permissions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_permissions.setVisibility(View.VISIBLE);
                ll_change_password.setVisibility(View.GONE);
                permissions_btn.setTextColor(getResources().getColor(R.color.white));
                change_password_btn.setTextColor(getResources().getColor(R.color.black));
                permissions_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                change_password_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_change_password.setVisibility(View.VISIBLE);
                ll_permissions.setVisibility(View.GONE);
                permissions_btn.setTextColor(getResources().getColor(R.color.black));
                change_password_btn.setTextColor(getResources().getColor(R.color.white));
                change_password_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                permissions_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
