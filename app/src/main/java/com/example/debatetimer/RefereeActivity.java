package com.example.debatetimer;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RefereeActivity extends AppCompatActivity {
    private String ipAddress;
    private boolean isStart = false;
    private int recLen;
    private int period = 0;
    private Button btnNext,btnStart,btnStop,btnRefresh;
    private TextView txtIP,txtPeriod,txtTime;
    private int timeOpen,timeFree,timeClose;
    private String periodName[];
    private int timeArr[];
    private CountDownTimer timer;
    private boolean startflag = false;


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

        txtIP.setText("Your IP address is: " + getlocalip());


        periodName=new String[6];
        periodName[0]="Opening statement from the pro";
        periodName[1]="Opening statement from the con";
        periodName[2]="Free debate";
        periodName[3]="Closing statement from the con";
        periodName[4]="Closing statement from the pro";
        periodName[5]="Closing statement from the pro";

        timeArr = new int[6];
        timeArr[0]=getIntent().getIntExtra("timeofopen",180);
        timeArr[1]=timeArr[0];
        timeArr[2]=getIntent().getIntExtra("timeoffree",480);
        timeArr[3]=getIntent().getIntExtra("timeofclose",150);
        timeArr[4]=timeArr[3];
        timeArr[5]=timeArr[4];
        recLen = timeArr[0];

        txtTime.setText(""+timeArr[period]+" seconds");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                }
                period++;
                recLen = timeArr[period];
                txtPeriod.setText(periodName[period]);
                txtTime.setText(""+timeArr[period]+" seconds");
                if(period == 4){
                    btnNext.setText("EXIT");
                }
                if(period == 5){
                    finish();
                }
                startflag = false;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startflag == false){
                    isStart = true;
                    timer = new CountDownTimer(recLen*1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            txtTime.setText((recLen-1) + " seconds");
                            recLen--;
                            if(recLen<=0){
                                recLen = 0;
                            }
                        }
                        @Override
                        public void onFinish() {
                            isStart = false;
                            txtTime.setText("Time is up!");
                        }
                    };
                    timer.start();
                    startflag=true;
                }

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                }
                txtTime.setText(""+recLen+" seconds");
                isStart = false;
                startflag = false;

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                }
                recLen = timeArr[period];
                txtTime.setText(""+timeArr[period]+" seconds");
                startflag = false;

            }
        });
    }

    private String getlocalip() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        //  Log.d(Tag, "int ip "+ipAddress);
        if (ipAddress == 0) return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
