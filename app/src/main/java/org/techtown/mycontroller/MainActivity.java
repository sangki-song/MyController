package org.techtown.mycontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    public String btSt;
    SeekBar seekbar;
    SeekBar seekBar1;
    TextView textView;
    TextView textView1;
    TextView textView2;
    int lSpd;
    int rSpd;
    int lSpd0;
    int rSpd0;
    String data = "10!10!0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new BluetoothSPP(this);
        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textViewL);
        textView2 = findViewById(R.id.textViewR);
        seekbar = findViewById(R.id.Seekbar1);
        seekBar1 = findViewById(R.id.Seekbar2);
        final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 1;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_CONNECT },
                    REQUEST_BLUETOOTH_CONNECT_PERMISSION);
        }

        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "블루투스 사용 불가", Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , name + "에 연결됨" + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                textView.setText(name + "에 연결됨, " + address);
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext(), "연결 해제", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_SHORT).show();
            }
        });


        Button btnConnect = findViewById(R.id.Conbutton);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        Button btnServ = findViewById(R.id.buttonServ);
        btnServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send(setup(lSpd0,rSpd0,1));
                Log.d("","\n"+"[서보모터 제어]");
                setup(lSpd0,rSpd0,0);
                Send(data);
            }
        });

        Button btnHalt = findViewById(R.id.buttonHalt);
        btnHalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "10!" +
                        "" +
                        "0!0";
                seekbar.setProgress(10);
                seekBar1.setProgress(10);
                Send(data);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //TODO [SeekBar 터치 이벤트 종료]

                lSpd = seekBar.getProgress();
                lSpd0 = lSpd;
                int lSpd1 = lSpd*10-100;
                textView1.setText(String.valueOf(lSpd1));

                setup(rSpd0,lSpd,0);

                Log.d("---","터치 시퀀스 작동");
                Log.d("//===========//","================================================");
                Log.d("","\n"+"[A_DisplayLight > onProgressChanged() 메소드 : 왼쪽 SeekBar 터치 이벤트 종료 데이터 확인 실시]");
                Log.d("","\n"+"[데이터 : " + String.valueOf(progress) + "]");
                Log.w("//===========//","================================================");
                Log.d("---","---");

                Send(data);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO [SeekBar 컨트롤 진행 중]
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO [SeekBar 터치 이벤트 발생]

            }
        });


        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO [SeekBar 컨트롤 진행 중]
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO [SeekBar 터치 이벤트 발생]

            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //TODO [SeekBar 터치 이벤트 종료]

                rSpd = seekBar.getProgress();
                rSpd0 = rSpd;
                int rSpd1 = rSpd*10-100;
                textView2.setText(String.valueOf(rSpd1));

                setup(rSpd,lSpd0,0);

                Log.d("---","---");
                Log.w("//===========//","================================================");
                Log.d("","\n"+"[A_DisplayLight > onProgressChanged() 메소드 : 오른쪽 SeekBar 터치 이벤트 종료 데이터 확인 실시]");
                Log.d("","\n"+"[데이터 : " + String.valueOf(progress) + "]");
                Log.w("//===========//","================================================");
                Log.d("---","---");

                Send(data);


            }
        });

    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT); //오류 아님
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                //setup(); 사용 안함
            }
        }


    }

    public String setup(int datl, int datr, int dats) {
        //Button btnSend = findViewById(R.id.button5); //데이터 전송   //여기가 중요
        //btnSend.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        bt.send(btSt, true);
        //    }   //send on to Serial
        //});

        data = String.valueOf(datl)+"!"+String.valueOf(datr)+"!"+String.valueOf(dats);
        Log.d("","\n"+"[생성된 ID : " + data + "]");
        return data;
    }

    public void Send(String data) {
        bt.send(data, true);
        Log.d("","\n"+"[전송된 ID : " + data + "]");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                //setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "블루투스 활성화되지 않음."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



}