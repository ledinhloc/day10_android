package com.example.bt2_socket;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {
    ImageButton btnTb1, btnTb2, btnDis;
    TextView txt1, txtMac;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConected = false;
    Set<BluetoothDevice> pairedDevices1;
    String address = null;
    private ProgressDialog progress;
    int flaglamp1;
    int flaglamp2;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blue_control);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);
        setContentView((R.layout.activity_blue_control));

        //anh xa
        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        btnDis = findViewById(R.id.btnDisc);
        txt1 = findViewById(R.id.textV1);
        txtMac = findViewById(R.id.textViewMAC);
        new ConnectBT().execute();
        btnTb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thietTbi1();
            }
        });
        btnTb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thiettbi7();
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });

    }

    private void thietTbi1(){
        if(btSocket != null){
            try {
                if (this.flaglamp1 == 0){
                    this.flaglamp1 = 1;
                    this.btnTb1.setBackgroundResource(R.drawable.switch_on);
                    btSocket.getOutputStream().write("A".toString().getBytes());
                    txt1.setText("thiet bi so 1 dang bat");
                    return;
                }
                else {
                    if (this.flaglamp1 != 1 ) return;
                    {
                        this.flaglamp1 = 0;
                        this.btnTb1.setBackgroundResource(R.drawable.switch_off);
                        btSocket.getOutputStream().write("A".toString().getBytes());
                        txt1.setText("Thiet bi 1 dang tat");

                        return;
                    }

                }

            } catch (IOException e) {
                Toast.makeText(this, "Loi", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void Disconnect(){
        if(btSocket != null){
            try {
                btSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        finish();
    }

    private void thiettbi7(){
        if(btSocket != null){
            try{
                if(this.flaglamp2 ==2){
                    this.flaglamp2 =1;
                    this.btnTb2.setBackgroundResource(R.drawable.switch_on);
                    btSocket.getOutputStream().write("7".toString().getBytes());
                    txt1.setText("Thiet bi so 7 dang bat");
                    return;
                } else {
                    if (this.flaglamp2 != 1) return;
                    {
                        this.flaglamp2 = 0;
                        this.btnTb2.setBackgroundResource(R.drawable.switch_off);
                        btSocket.getOutputStream().write("G".toString().getBytes());
                        txt1.setText("Thiet bi so 7 dang tat");
                        return;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class ConnectBT extends AsyncTask<Void, Void, Void>{
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlueControl.this, "Dang ket noi ...", "Xin voi long doi!!");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (btSocket == null || !isBtConected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();

                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    if(ActivityCompat.checkSelfPermission(BlueControl.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
                        //
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();
                    }
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess){
                msg("ket noi that bai! kiem tra thiet bi");
            }
            else {
                msg("Ket noi thanh cong");
                isBtConected = true;
                pairedDevicesList1();
            }
            progress.dismiss();
        }

    }

    private void msg(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void pairedDevicesList1(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            pairedDevices1 = myBluetooth.getBondedDevices();

            if (pairedDevices1.size()>1){
                for (BluetoothDevice bt : pairedDevices1){
                    txtMac.setText(bt.getName() + " - " + bt.getAddress());

                }
            }else {
                msg("khong tim thay thiet bi ket noi");
            }
        }
    }
}