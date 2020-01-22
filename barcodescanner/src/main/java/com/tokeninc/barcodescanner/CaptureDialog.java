package com.tokeninc.barcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

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

public class CaptureDialog extends DialogFragment {
    private DecoratedBarcodeView barcodeView;
    private String myResult;
    private SingleBarcodeCallback barcodeCallback;
    private Context context;

    static CaptureDialog newInstance(Bundle args) {

        CaptureDialog fragment = new CaptureDialog();
        fragment.setArguments(args);
        return fragment;
    }

    void setBarcodeCallback(SingleBarcodeCallback barcodeCallback) {
        this.barcodeCallback = barcodeCallback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            barcodeView.pause();

            myResult = result.getText();
            barcodeView.setStatusText(myResult);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("Barcode Info: \n" + myResult);
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    barcodeCallback.getBarcodeData(myResult);
                    dialog.dismiss();
                    CaptureDialog.this.dismiss();
                }
            });
            dialogBuilder.show();
        }


        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_continuous_capture,container,false);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
        barcodeView = view.findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.UPC_A,BarcodeFormat.AZTEC,BarcodeFormat.CODABAR,
                BarcodeFormat.CODE_39,BarcodeFormat.CODE_93,BarcodeFormat.CODE_128,BarcodeFormat.DATA_MATRIX,BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,BarcodeFormat.ITF,BarcodeFormat.MAXICODE,BarcodeFormat.PDF_417,BarcodeFormat.QR_CODE,
                BarcodeFormat.RSS_14,BarcodeFormat.RSS_EXPANDED,BarcodeFormat.UPC_E,BarcodeFormat.UPC_EAN_EXTENSION);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        CameraSettings mCameraSettings = barcodeView.getCameraSettings();
        mCameraSettings.setFocusMode(CameraSettings.FocusMode.CONTINUOUS);
        //mCameraSettings.setMeteringEnabled(false);
        //mCameraSettings.setBarcodeSceneModeEnabled(true);
        //mCameraSettings.setAutoFocusEnabled(true);
        //mCameraSettings.setContinuousFocusEnabled(true);
        //mCameraSettings.setExposureEnabled(false);
        barcodeView.setCameraSettings(mCameraSettings);
        barcodeView.initializeFromIntent(new Intent());


        barcodeView.decodeContinuous(callback);
        return view;
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
        barcodeView=null;
    }
}



