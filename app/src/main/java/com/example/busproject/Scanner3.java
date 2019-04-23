package com.example.busproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.example.busproject.Utils.Helpers;
import com.google.android.gms.vision.barcode.Barcode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import info.androidhive.barcode.BarcodeReader;

public class Scanner3 extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner3);
        //getUserData("taxi","8");
        // getting barcode instance


        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);


        /***
         * Providing beep sound. The sound file has to be placed in
         * `assets` folder
         */
        // barcodeReader.setBeepSoundFile("shutter.mp3");

        /**
         * Pausing / resuming barcode reader. This will be useful when you want to
         * do some foreground user interaction while leaving the barcode
         * reader in background
         * */
        // barcodeReader.pauseScanning();
        // barcodeReader.resumeScanning();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        final String id = barcode.displayValue.substring(barcode.displayValue.lastIndexOf("/") + 1);
        final String type = barcode.displayValue.substring(barcode.displayValue.lastIndexOf("/", 29) + 1, 34);
//        Pattern pattern = Pattern.compile("type=[a-z]+");
//        Matcher matcher = pattern.matcher(barcode.displayValue);//todo result bejaye test
//
//        String type = "";
//        if (matcher.find(0)) {
//            type = matcher.group(0);
//        }
//
//        type = type.replace("type=", "");
//
//
//        Pattern pattern2 = Pattern.compile("id=\\d+");
//        Matcher matcher2 = pattern2.matcher(barcode.displayValue);
//        String id = "";
//        if (matcher2.find(0)) {
//            id = matcher2.group(0);
//        }
//
//        id = id.replace("id=", "");




        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getUserData(type, id);
                //Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e(TAG, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
        finish();
    }


    public void getUserData(String type, String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("type", type);
        params.put("api_token", Helpers.getSharePrf("api_token"));
        String url = Helpers.baseUrl + "qrcode";
        AsyncHttpClient client=new AsyncHttpClient();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Helpers.noInternetDialog();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Intent intent = new Intent(Scanner3.this, PaymentActivity.class);
                intent.putExtra("userData", responseString);
                startActivity(intent);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}