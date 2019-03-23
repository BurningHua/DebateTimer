package com.example.debatetimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterIPActivity extends AppCompatActivity {
    private EditText ipAddress;
    private String ipServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ip);
        ipAddress = findViewById(R.id.ipaddress);

        Button btnOK =(Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipServer = ipAddress.getText().toString().trim();
                if(ipServer.equals("")){
                    Toast.makeText(EnterIPActivity.this,"please input IP address",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(EnterIPActivity.this, DebateActivity.class);
                    intent.putExtra("ipaddress", ipServer);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
