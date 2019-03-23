package com.example.debatetimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TimesetActivity extends AppCompatActivity {
    private Spinner openSet,freeSet,closeSet;
    private String openstr,freestr,closestr;
    private int opentime,freetime,closetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeset);
        openSet = findViewById(R.id.spinneropen);
        freeSet = findViewById(R.id.spinnerfree);
        closeSet = findViewById(R.id.spinnerclose);



        Button btnOK =(Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openstr = (String) openSet.getSelectedItem();
                openstr = openstr.substring(0,3);
                opentime = Integer.parseInt(openstr);
                freestr = (String) freeSet.getSelectedItem();
                freestr = freestr.substring(0,3);
                freetime = Integer.parseInt(freestr);
                closestr = (String) closeSet.getSelectedItem();
                closestr = closestr.substring(0,3);
                closetime = Integer.parseInt(closestr);
                Intent intent = new Intent();
                intent.setClass(TimesetActivity.this, RefereeActivity.class);
                intent.putExtra("timeofopen",opentime);
                intent.putExtra("timeoffree",freetime);
                intent.putExtra("timeofclose",closetime);
                startActivity(intent);
                finish();

            }
        });

    }
}
