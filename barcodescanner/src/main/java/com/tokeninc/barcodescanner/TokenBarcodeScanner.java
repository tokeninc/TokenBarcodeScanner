package com.tokeninc.barcodescanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class TokenBarcodeScanner {

    private static TokenBarcodeScanner instance;

    private TokenBarcodeScanner(){

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
    private void requestBarcodeScannerWithParams(@NonNull Bundle params,@NonNull FragmentManager fragmentManager,
                                                          @NonNull SingleBarcodeCallback callback){
        CaptureDialog dialog = CaptureDialog.newInstance(params);
        dialog.setBarcodeCallback(callback);
        dialog.show(fragmentManager, CaptureDialog.class.getName());
    }

    /**
     * @param fragmentManager supportFragmentManager
     * @param callback interface to get results in barcode scanner
     */
    public void requestBarcodeScanner(@NonNull FragmentManager fragmentManager, @NonNull SingleBarcodeCallback callback){
        CaptureDialog dialog = new CaptureDialog();
        dialog.setBarcodeCallback(callback);
        dialog.show(fragmentManager, CaptureDialog.class.getName());
    }

    public void requestBarcodeScannerWithoutConfirmDialog(@NonNull FragmentManager fragmentManager,
                                                          @NonNull SingleBarcodeCallback callback){
        Bundle bundle = new Bundle();
        bundle.putBoolean("noConfirmDialog",true);
        CaptureDialog dialog = CaptureDialog.newInstance(bundle);
        dialog.setBarcodeCallback(callback);
        dialog.show(fragmentManager, CaptureDialog.class.getName());
    }
}
