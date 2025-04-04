package com.example.bt2_socket;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button btnPaired;
    ListView listDanhSach;
    public static int REQUEST_BLUETOOTH = 1;
    private BluetoothAdapter myBluetooth = null;

    private Set<BluetoothDevice>  pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1001);

                return; // Dừng lại để chờ người dùng cấp quyền
            }
        }


        // anh xa
        btnPaired = findViewById(R.id.btnTimThietbi);
        listDanhSach = findViewById(R.id.listTB);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth == null){
            Toast.makeText(this, "Thiet bi Bluetooth chua bat", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (!myBluetooth.isEnabled()){
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Thiet bi bluetooth da bat", Toast.LENGTH_SHORT).show();
                startActivityForResult(turnBTon, REQUEST_BLUETOOTH);
            }
            
            btnPaired.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pairedDevicesList();
                }
            });
        }
    }
    
    private void pairedDevicesList(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){

            pairedDevices = myBluetooth.getBondedDevices();
            ArrayList list = new ArrayList();
            if(pairedDevices.size() > 0){
                for(BluetoothDevice bt : pairedDevices){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Danh sach thiet bi Bluetooth da bat", Toast.LENGTH_SHORT).show();
                        list.add(bt.getName() + "\n" + bt.getAddress());

                    }
                }
            }else {
                Toast.makeText(this, "Khong tim thay thiet bi ket noi", Toast.LENGTH_SHORT).show();
            }
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listDanhSach.setAdapter(adapter);
          //  listDanhSach.setOnItemClickListener(myListClickListener);
        }
    }
}