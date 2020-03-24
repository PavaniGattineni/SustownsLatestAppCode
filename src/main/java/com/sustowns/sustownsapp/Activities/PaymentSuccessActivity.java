package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.sustowns.sustownsapp.R;

public class PaymentSuccessActivity extends AppCompatActivity {
    TextView paybybank_orderstatus,text_message;
    ImageView backarrow_payment;
    String messageStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment_success);
        messageStr = getIntent().getStringExtra("Message");
        paybybank_orderstatus = (TextView) findViewById(R.id.paybybank_orderstatus);
        text_message = (TextView) findViewById(R.id.text_message);
        if(messageStr.equalsIgnoreCase("") || messageStr.isEmpty() ||  messageStr.equalsIgnoreCase("null")){
            text_message.setVisibility(View.GONE);
        }else{
            text_message.setVisibility(View.VISIBLE);
            text_message.setText(messageStr);
        }
        backarrow_payment = (ImageView) findViewById(R.id.backarrow_payment);
        paybybank_orderstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentSuccessActivity.this, StoreReceivedOrdersActivity.class);
                i.putExtra("Message","");
                startActivity(i);
            }
        });
        backarrow_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentSuccessActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(PaymentSuccessActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}