package com.example.debatetimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DebateActivity extends AppCompatActivity {
    private String ipAddress;
    private boolean isStart;
    private int recLen;
    private int period;
    private String periodName[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate);
        ipAddress = getIntent().getStringExtra("ipaddress");
        Log.i("tag", ipAddress);

        periodName=new String[6];
        periodName[0]="Opening statement from the pro:";
        periodName[1]="Opening statement from the con:";
        periodName[2]="Free debate:";
        periodName[3]="Closing statement from the con:";
        periodName[4]="Closing statement from the pro:";
        periodName[5]="Closing statement from the pro:";

        Button btnExit =(Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
