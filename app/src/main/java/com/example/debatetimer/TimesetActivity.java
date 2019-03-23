package com.example.debatetimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class TimesetActivity extends AppCompatActivity {
    private Spinner openSet,freeSet,closeSet;
    private String openstr,freestr,closestr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeset);

        Button btnOK =(Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TimesetActivity.this, RefereeActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
