package com.example.arlan.qrscanner_camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


/**
 * Created by arlan on 7/18/2017.
 */

public class ScanBarcode extends AppCompatActivity {

    SurfaceView cameraPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_view);

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        Log.v("arvinsTag","Oncreate");
        createCameraSource();

    }

    private void createCameraSource() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1024, 768)
                .build();

        Log.v("arvinsTag","Build BarcodeDetector");

        //this is like OnClickListener, you must implements all the methods
        //of the SurfaceHolder.Callback
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {


            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                //check permission
                if (ActivityCompat.checkSelfPermission(ScanBarcode.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //requires to be surrounded by try catch block
                try {
                    cameraSource.start(cameraPreview.getHolder());
                    Log.v("arvinsTag","Camera Started");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("arvinsTag","IOException: " + e);
                }

            }


            /*=====================================================================
            *  This method is always called at least once,
            *  after surfaceCreated(SurfaceHolder).
            *=====================================================================*/
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.v("arvinsTag","surfaceChanged");
            }


            //stop camera when QR code detected
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
                Log.v("arvinsTag","surfaceDestroyed");
            }


        });



        //this is like OnClickListener, you must implements all the methods
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {


            /*=====================================================================
            *  this release() must be implement since Detector.Processor is an interface
            *  even though it has no use for this sample app
            *======================================================================*/
            @Override
            public void release() {
                Log.v("arvinsTag","release");
            }


            /*=====================================================================
            * will keep on looping while camera started until it detected a QR code
            *=====================================================================*/
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() > 0){
                    Intent intent =  new Intent();
                    intent.putExtra("barcodes", barcodes.valueAt(0)); //  get latest barcode from the array
                    setResult(CommonStatusCodes.SUCCESS, intent);
                    Log.v("arvinsTag","receiveDetections");
                    finish();
                }

            }
        });

    }

}
