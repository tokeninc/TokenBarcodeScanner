package com.tokeninc.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

/**
 * Sample App Project
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_scanning).setOnClickListener(v ->
                TokenBarcodeScanner.getInstance().requestBarcodeScanner(getSupportFragmentManager(), result ->
                        Log.d("barcode",result)));
    }
}
