package dharmawan.fp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinearLayout layout;
    private ImageButton send,clear;
    private Spinner radius;
    public int r = 30;
    private CustomView map;
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = new CustomView(this,true);
        findViewById();
    }

    /*public String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }*/

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.ib_send:
                    offloading();
                    break;
                case R.id.ib_clear:
                    map.point = new ArrayList<>();
                    map.invalidate();
                    break;
            }
        }
    };

    //wifi
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

    private void offloading() {
        //battery
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        //wifi
        int wifi = 0;
        if (checkInternetConnection()) {
            wifi = 1;
        }

        if(map.point.size()>30 && batteryPct >= 0.3 && wifi == 1) {
            setserver();
        }
        else {
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("koordinat", map.point);
            intent.putExtra("url", "");
            startActivity(intent);
        }
    }

    private void setserver() {
        final View v = LayoutInflater.from(this).inflate(R.layout.input_server,null);
        final EditText server = (EditText) v.findViewById(R.id.et_url);

        AlertDialog.Builder tampil = new AlertDialog.Builder(this);
        tampil.setTitle("Masukkan Link Server");
        tampil.setView(v);
        tampil.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                URL = server.getText().toString();
                //if(!URL.startsWith("http://")) URL = "http://"+ URL;

                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("koordinat", map.point);
                intent.putExtra("url", URL);
                startActivity(intent);

                dialog.dismiss();
            }
        });
        tampil.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        tampil.show();
    }

    private void findViewById() {
        send = (ImageButton) findViewById(R.id.ib_send);
        clear = (ImageButton) findViewById(R.id.ib_clear);
        send.setOnClickListener(op);
        clear.setOnClickListener(op);

        radius = (Spinner) findViewById(R.id.radius);
        radius.setOnItemSelectedListener(this);
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("30");
        categories.add("40");
        categories.add("50");
        categories.add("60");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(dataAdapter);

        layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(map);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        r = Integer.valueOf(item);
        map.r = r;
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}