package com.tokeninc.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ContinuousCaptureActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private CameraSettings mCameraSettings;
    private int counter = 0 ;
    private String myResult;
    private ArrayList<String> all_results;
    private String addedBarcode;


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            /*if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }*/
            barcodeView.pause();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myResult = result.getText();

            all_results.add(myResult);

            barcodeView.setStatusText(myResult);

            Log.d("BARCODE_SCANNER_RESULT",result.getText());
            barcodeView.resume();
        }


        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_capture);

        if (ContextCompat.checkSelfPermission(ContinuousCaptureActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContinuousCaptureActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
        all_results=new ArrayList<String>();
        //
        setupToolbar();
        //

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.UPC_A,BarcodeFormat.AZTEC,BarcodeFormat.CODABAR,
                BarcodeFormat.CODE_39,BarcodeFormat.CODE_93,BarcodeFormat.CODE_128,BarcodeFormat.DATA_MATRIX,BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,BarcodeFormat.ITF,BarcodeFormat.MAXICODE,BarcodeFormat.PDF_417,BarcodeFormat.QR_CODE,
                BarcodeFormat.RSS_14,BarcodeFormat.RSS_EXPANDED,BarcodeFormat.UPC_E,BarcodeFormat.UPC_EAN_EXTENSION);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        mCameraSettings = barcodeView.getCameraSettings();
        mCameraSettings.setFocusMode(CameraSettings.FocusMode.CONTINUOUS);
        //mCameraSettings.setMeteringEnabled(false);
        //mCameraSettings.setBarcodeSceneModeEnabled(true);
        //mCameraSettings.setAutoFocusEnabled(true);
        //mCameraSettings.setContinuousFocusEnabled(true);
        //mCameraSettings.setExposureEnabled(false);
        barcodeView.setCameraSettings(mCameraSettings);
        barcodeView.initializeFromIntent(getIntent());


        barcodeView.decodeContinuous(callback);
    }


    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barcodeView=null;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);


        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_done){
            Intent returnIntent = new Intent();
            returnIntent.putStringArrayListExtra("result",all_results);
            setResult(RESULT_OK,returnIntent);
            finish();
        }
        else if(item.getItemId() == R.id.action_add){
            addLastLine();
        }
        else if(item.getItemId() == R.id.action_delete){
            deleteLastLine();
        }
        return super.onOptionsItemSelected(item);
    }



    private void deleteLastLine(){
        barcodeView.setStatusText("");
        all_results.remove(all_results.size()-1);
    }

    private void addLastLine(){

        AlertDialog.Builder alert = new AlertDialog.Builder(ContinuousCaptureActivity.this);
        alert.setTitle("Set barcode");
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 addedBarcode= String.valueOf(input.getText());
                 all_results.add(addedBarcode);
                 barcodeView.setStatusText(addedBarcode);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();



    }



}



