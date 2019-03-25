package com.example.debatetimer;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DebateActivity extends AppCompatActivity {
    private String ipAddress;
    private int isStart;
    private int recLen;
    private int period = 0;
    private TextView txtTime,txtPeriod;
    private CountDownTimer timer;
    private String timerState;
    private String periodName[];
    private Socket mSocket;
    private OutputStream mOutStream;
    private InputStream mInStream;

    //private SocketConnectThread socketConnectThread;

    private StringBuffer sb;

    @SuppressLint("HandlerLeak")

    public Handler handler = new Handler(){

        @Override

        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            if (msg.what == 1){
                sb = new StringBuffer("");
                if(timer!=null){
                    timer.cancel();
                }

                Bundle data = msg.getData();

                sb.append(data.getString("msg"));
                Log.i("servermsg", sb.toString());
                timerState = sb.toString();
                period = Integer.parseInt(timerState.substring(0,1));
                isStart = Integer.parseInt(timerState.substring(1,2));
                recLen = Integer.parseInt(timerState.substring(2,5));
                txtPeriod.setText(periodName[period]);
                if(isStart == 0){
                    txtTime.setText(recLen+" seconds");
                }
                else{
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
                }
                if(period == 5){
                    txtTime.setText("Debate is over!");
                    txtPeriod.setText(" ");
                }


                //sb.append("\n");

                //tv_msg.setText(sb.toString());

            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate);
        ipAddress = getIntent().getStringExtra("ipaddress");
        Log.i("tag", ipAddress);
        txtTime = findViewById(R.id.textViewTime);
        txtPeriod = findViewById(R.id.textViewPeriod);
        SocketConnectThread socketConnectThread = new SocketConnectThread();
        socketConnectThread.start();

        //Toast.makeText(DebateActivity.this, "The IP address is invalid.", Toast.LENGTH_SHORT).show();

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


    class SocketConnectThread extends Thread{
        @Override
        public void run(){
            Log.e("info", "run" );
            try {
                mSocket = new Socket(ipAddress, 12345);
               // txtTime.setText("REST TIME");
                if(mSocket != null){
                    mOutStream = mSocket.getOutputStream();
                    mInStream = mSocket.getInputStream();
                }else {
                    Toast.makeText(DebateActivity.this, "The IP address is invalid.", Toast.LENGTH_SHORT).show();
                    Log.e("info", "run: =========scoket==null");
                }
            } catch (Exception e) {
                Log.i("info", "invalid address");
                //Toast.makeText(DebateActivity.this, "Invalid address.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            Log.e("info","connect success========================================");
            startReader(mSocket);
        }
    }

    private void startReader(final Socket socket) {

        new Thread(){

            @Override

            public void run() {

                DataInputStream reader;

                try {

                    reader = new DataInputStream(socket.getInputStream());

                    while (true) {

                        String msg = reader.readUTF();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }





}
