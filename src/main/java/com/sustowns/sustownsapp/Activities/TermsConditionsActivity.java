package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sustowns.sustownsapp.R;

public class TermsConditionsActivity extends AppCompatActivity {
    ImageView backarrow;
    Button terms_conditions,privacy_policy;
    LinearLayout ll_terms_conditions,ll_privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_terms_conditions);

        backarrow = (ImageView) findViewById(R.id.backarrow);
        terms_conditions = (Button) findViewById(R.id.terms_conditions);
        privacy_policy = (Button) findViewById(R.id.privacy_policy);
        ll_terms_conditions = (LinearLayout) findViewById(R.id.ll_terms_conditions);
        ll_privacy_policy = (LinearLayout) findViewById(R.id.ll_privacy_policy);


        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_terms_conditions.setVisibility(View.VISIBLE);
                ll_privacy_policy.setVisibility(View.GONE);
                terms_conditions.setTextColor(getResources().getColor(R.color.white));
                privacy_policy.setTextColor(getResources().getColor(R.color.black));
                terms_conditions.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                privacy_policy.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_privacy_policy.setVisibility(View.VISIBLE);
                ll_terms_conditions.setVisibility(View.GONE);
                terms_conditions.setTextColor(getResources().getColor(R.color.black));
                privacy_policy.setTextColor(getResources().getColor(R.color.white));
                privacy_policy.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                terms_conditions.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
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
