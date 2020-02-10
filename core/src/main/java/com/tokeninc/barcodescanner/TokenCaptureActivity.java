package com.tokeninc.barcodescanner;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TokenCaptureActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private String myResult;
    private Bundle arguments;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            barcodeView.pause();

            myResult = result.getText();
            barcodeView.setStatusText(myResult);
            //Hide or show confirm dialog to return
            if(arguments != null && arguments.getBoolean("confirmDialog")){
                Intent intent = new Intent("com.tokeninc.barcodescanner.REQUEST_BARCODE");
                intent.putExtra("barcode",myResult);
                sendBroadcast(intent);
                finish();
            }
            else{
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TokenCaptureActivity.this);
                dialogBuilder.setMessage("Barcode Info: \n" + myResult);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", (dialog,which) -> {
                    Intent intent = new Intent("com.tokeninc.barcodescanner.REQUEST_BARCODE");
                    intent.putExtra("barcode",myResult);
                    sendBroadcast(intent);
                    dialog.dismiss();
                    finish();
                });
                dialogBuilder.show();
            }
        }


        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_capture);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
        if(getIntent() != null && getIntent().getExtras() != null){
            arguments = getIntent().getExtras();
        }
        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.UPC_A,BarcodeFormat.AZTEC,BarcodeFormat.CODABAR,
                BarcodeFormat.CODE_39,BarcodeFormat.CODE_93,BarcodeFormat.CODE_128,BarcodeFormat.DATA_MATRIX,BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,BarcodeFormat.ITF,BarcodeFormat.MAXICODE,BarcodeFormat.PDF_417,BarcodeFormat.QR_CODE,
                BarcodeFormat.RSS_14,BarcodeFormat.RSS_EXPANDED,BarcodeFormat.UPC_E,BarcodeFormat.UPC_EAN_EXTENSION);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        CameraSettings mCameraSettings = barcodeView.getCameraSettings();
        //mCameraSettings.setMeteringEnabled(false);
        //mCameraSettings.setBarcodeSceneModeEnabled(true);
        mCameraSettings.setAutoFocusEnabled(true);
        //mCameraSettings.setContinuousFocusEnabled(true);
        //mCameraSettings.setExposureEnabled(false);
        barcodeView.setCameraSettings(mCameraSettings);
        barcodeView.initializeFromIntent(new Intent());
        barcodeView.decodeSingle(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        barcodeView = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Permission required to scan barcode with camera");
                    builder.setNegativeButton("OK", ((dialog, which) -> {
                        dialog.dismiss();
                        throw new RuntimeException("Permission required to scan barcode with camera");
                    }));
                }
            }
        }
    }
}



