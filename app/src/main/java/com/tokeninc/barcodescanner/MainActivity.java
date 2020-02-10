package com.tokeninc.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tokeninc.barcodescannerimpl.TokenBarcodeScanner;

import java.lang.ref.WeakReference;

/**
 * Sample App Project
 */

public class MainActivity extends AppCompatActivity {
    private TokenBarcodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_scanning).setOnClickListener(v ->
            scanner = new TokenBarcodeScanner(new WeakReference<>(MainActivity.this),data -> {

            }));
    }
}
