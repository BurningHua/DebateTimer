package com.example.debatetimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RefereeActivity extends AppCompatActivity {
    private String ipAddress;
    private boolean isStart;
    private int recLen;
    private int period = 0;
    private Button btnNext,btnStart,btnStop,btnRefresh;
    private TextView txtIP,txtPeriod,txtTime;
    private int timeOpen,timeFree,timeClose;
    private String periodName[];
    private int timeArr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee);

        txtPeriod = findViewById(R.id.textView);
        txtTime = findViewById(R.id.textViewTime);
        txtIP = findViewById(R.id.textViewIP);
        btnNext = findViewById(R.id.btnNext);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnRefresh = findViewById(R.id.btnRefresh);


        periodName=new String[6];
        periodName[0]="Opening statement from the pro";
        periodName[1]="Opening statement from the con";
        periodName[2]="Free debate";
        periodName[3]="Closing statement from the con";
        periodName[4]="Closing statement from the pro";
        periodName[5]="Closing statement from the pro";

        timeArr = new int[5];

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                period++;
                txtPeriod.setText(periodName[period]);
                if(period == 4){
                    btnNext.setText("EXIT");
                }
                if(period == 5){
                    finish();
                }




            }
        });
    }
}
