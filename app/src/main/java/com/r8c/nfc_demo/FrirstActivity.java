package com.r8c.nfc_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FrirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frirst);
        findViewById(R.id.btUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrirstActivity.this,UserLoginActivity.class));

            }
        });
        findViewById(R.id.btMan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrirstActivity.this,ManLoginActivity.class));


            }
        });

    }
}
