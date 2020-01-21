package com.tokeninc.barcodescanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class TokenBarcodeScanner {

    private static TokenBarcodeScanner instance;

    protected TokenBarcodeScanner(){

    }

    public static TokenBarcodeScanner getInstance() {
        if(instance == null){
            instance = new TokenBarcodeScanner();
        }
        return instance;
    }

    /**
     *
     * @param params parameters to pass instance for usage in barcode scanner
     * @param callback interface to get results in barcode scanner
     */
    protected void requestBarcodeScannerWithParams(@NonNull Bundle params,@NonNull FragmentManager fragmentManager,
                                                          @NonNull TokenBarcodeCallback callback){
        new CaptureDialog(callback).show(fragmentManager, CaptureDialog.class.getName());
    }

    /**
     * @param fragmentManager supportFragmentmanager
     * @param callback interface to get results in barcode scanner
     */
    public void requestBarcodeScanner(@NonNull FragmentManager fragmentManager, @NonNull TokenBarcodeCallback callback){
        new CaptureDialog(callback).show(fragmentManager, CaptureDialog.class.getName());
    }
}
