package com.example.debatetimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStartD =(Button)findViewById(R.id.btnStartDebater);
        btnStartD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EnterIPActivity.class);
                startActivity(intent);

            }
        });

        Button btnStartR =(Button)findViewById(R.id.btnStartReferee);
        btnStartR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TimesetActivity.class);
                startActivity(intent);

            }
        });

        Button btnExit =(Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
                System.exit(0);
            }
        });
    }
}
