package com.example.debatetimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RefereeActivity extends AppCompatActivity {
    private String ipAddress;
    private int isStart = 0;
    private int recLen;
    private int period = 0;
    private Button btnNext, btnStart, btnStop, btnRefresh;
    private TextView txtIP, txtPeriod, txtTime;
    private int timeOpen, timeFree, timeClose;
    private String periodName[];
    private int timeArr[];
    private CountDownTimer timer;
    private boolean startflag = false;
    private String msgToSend;
    private ServerSocket mServerSocket;
    private Socket mSocket;
    private StringBuffer sb = new StringBuffer();
    private OutputStream mOutStream;
    private DataOutputStream writer;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {

        @Override

        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            if (msg.what == 1) {

                Bundle data = msg.getData();

                sb.append(data.getString("msg"));

                sb.append("\n");

                //tv_msg.setText(sb.toString());

            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee);

        try {

            mServerSocket = new ServerSocket(12345);

        } catch (IOException e) {

            e.printStackTrace();

        }


        SocketAcceptThread socketAcceptThread = new SocketAcceptThread();

        socketAcceptThread.start();

        txtPeriod = findViewById(R.id.textView);
        txtTime = findViewById(R.id.textViewTime);
        txtIP = findViewById(R.id.textViewIP);
        btnNext = findViewById(R.id.btnNext);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnRefresh = findViewById(R.id.btnRefresh);

        txtIP.setText("Your IP address is: " + getlocalip());


        periodName = new String[6];
        periodName[0] = "Opening statement from the pro";
        periodName[1] = "Opening statement from the con";
        periodName[2] = "Free debate";
        periodName[3] = "Closing statement from the con";
        periodName[4] = "Closing statement from the pro";
        periodName[5] = "Closing statement from the pro";

        timeArr = new int[6];
        timeArr[0] = getIntent().getIntExtra("timeofopen", 180);
        timeArr[1] = timeArr[0];
        timeArr[2] = getIntent().getIntExtra("timeoffree", 480);
        timeArr[3] = getIntent().getIntExtra("timeofclose", 150);
        timeArr[4] = timeArr[3];
        timeArr[5] = timeArr[4];
        recLen = timeArr[0];

        txtTime.setText("" + timeArr[period] + " seconds");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                }
                period++;
                recLen = timeArr[period];
                isStart = 0;
                txtPeriod.setText(periodName[period]);
                txtTime.setText("" + timeArr[period] + " seconds");
                if (period == 4) {
                    btnNext.setText("EXIT");
                }
                if (period == 5) {
                    msgToSend = String.valueOf(period) + String.valueOf(isStart) + String.format("%03d", recLen);
                    send(msgToSend);
                    finish();
                }
                msgToSend = String.valueOf(period) + String.valueOf(isStart) + String.format("%03d", recLen);
                send(msgToSend);
                startflag = false;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startflag == false) {
                    isStart = 1;
                    timer = new CountDownTimer(recLen * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            txtTime.setText((recLen - 1) + " seconds");
                            recLen--;
                            if (recLen <= 0) {
                                recLen = 0;
                            }
                        }

                        @Override
                        public void onFinish() {
                            isStart = 0;
                            txtTime.setText("Time is up!");
                        }
                    };
                    timer.start();
                    startflag = true;
                    msgToSend = String.valueOf(period) + String.valueOf(isStart) + String.format("%03d", recLen);
                    send(msgToSend);
                    Log.i("message", msgToSend);
                }

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                }
                txtTime.setText("" + recLen + " seconds");
                isStart = 0;
                startflag = false;
                msgToSend = String.valueOf(period) + String.valueOf(isStart) + String.format("%03d", recLen);
                send(msgToSend);

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                }
                recLen = timeArr[period];
                txtTime.setText("" + timeArr[period] + " seconds");
                isStart = 0;
                startflag = false;
                msgToSend = String.valueOf(period) + String.valueOf(isStart) + String.format("%03d", recLen);
                send(msgToSend);

            }
        });
    }

    private String getlocalip() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        //  Log.d(Tag, "int ip "+ipAddress);
        if (ipAddress == 0) return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    class SocketAcceptThread extends Thread {

        @Override

        public void run() {

            try {
                mSocket = mServerSocket.accept();
               // mOutStream = mSocket.getOutputStream();
                writer = new DataOutputStream(mSocket.getOutputStream());
                msgToSend = String.valueOf(period) + String.valueOf(isStart) + String.format("%03d", recLen);
                send(msgToSend);


            } catch (IOException e) {

                e.printStackTrace();

                Log.i("info", "run: ==============" + "accept error");

                return;

            }

            Log.i("info", "accept success==================");

            //startReader(mSocket);

        }

    }

    private void startReader(final Socket socket) {

        new Thread() {

            @Override

            public void run() {

                DataInputStream reader;

                try {

                    reader = new DataInputStream(socket.getInputStream());

                    while (true) {

                        String msg = reader.readUTF();

                        //DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());

                        //writer.writeUTF("收到：" + msg);

                        Message message = new Message();

                        message.what = 1;

                        Bundle bundle = new Bundle();

                        bundle.putString("msg", msg);

                        message.setData(bundle);

                        handler.sendMessage(message);

                    }

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }.start();

    }

    public void send(final String str) {

        if (str.length() == 0) {

            return;

        }
        new Thread() {

            @Override

            public void run () {

                try {   //发送
                    writer.writeUTF(str);
                    writer.flush();
                    //mOutStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }.start();

    }









    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mServerSocket != null){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
