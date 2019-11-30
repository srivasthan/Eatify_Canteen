package com.eatify.sale.eatifysale.activitiyfiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatify.sale.eatifysale.HomeScreen;
import com.eatify.sale.eatifysale.R;
import com.eatify.sale.eatifysale.Scan;
import com.eatify.sale.eatifysale.globalfiles.GlobalUrl;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrPage extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_page);
        mScannerView =  findViewById(R.id.scanview);  // Programmatically initialize the scanner view
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.startCamera();           // Stop camera on pause

    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        String scanresult=rawResult.getText();

        Intent intent=new Intent(ScanQrPage.this,EntryScreen.class);
        intent.putExtra("id",scanresult);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent new_intent = new Intent(this, Scan.class);

        this.startActivity(new_intent);
        finish();

    }
}