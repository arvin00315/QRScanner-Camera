/*=======================================================
*  reference: https://www.youtube.com/watch?v=czmEC5akcos
*========================================================*/


package com.example.arlan.qrscanner_camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    Button barcodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeResult = (Button) findViewById(R.id.scan_barcode);

        barcodeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanBarcode.class);

                //Launch an activity for which you would like a result when it finished.
                startActivityForResult(intent, 0);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcodes");
                    barcodeResult.setText("Barcode Value: "+barcode.displayValue);
                } else {
                    barcodeResult.setText("No barcode found");
                }
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }

        Log.v("arvinsTag","onActivityResult");
    }
}
