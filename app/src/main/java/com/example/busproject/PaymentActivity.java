package com.example.busproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.busproject.Map.MapsActivity;
import com.example.busproject.Model.CurrentTaxi;
import com.example.busproject.Model.PaymentTrackModel;
import com.example.busproject.Utils.FontUtils;
import com.example.busproject.Utils.Helpers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentActivity extends AppCompatActivity {
    Button pardakht;

    TextView txtName, txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);
        Intent intent = getIntent();
        String responseString = intent.getStringExtra("userData");
        final CurrentTaxi currentTaxis = Helpers.gson.fromJson(responseString, CurrentTaxi.class);


        txtName = findViewById(R.id.name);
        txtPrice = findViewById(R.id.price);
        pardakht = findViewById(R.id.pardakht);

        txtName.setText(currentTaxis.getInfo().getFirstName() + " " + currentTaxis.getInfo().getLastName());

        txtPrice.setText(currentTaxis.getInfo().getStationPrice().toString());

        pardakht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Dialog dialog = new Dialog(PaymentActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//                dialog.setContentView(R.layout.pardakhtdialog);
//                dialog.setCancelable(false);
//                dialog.show();
//                Button btnGo = dialog.findViewById(R.id.btnGo);
//
//                btnGo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        Intent i1 = new Intent(getApplicationContext(), MapsActivity.class);
//                        i1.setAction(Intent.ACTION_MAIN);
//                        i1.addCategory(Intent.CATEGORY_HOME);
//                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                        startActivity(i1);
//                        overridePendingTransition(0, 0);
//
//                        finish();
//                    }
//                });


                Intent intent = new Intent(PaymentActivity.this, PaymentActivityDialog.class);
                intent.putExtra("tag", "taxi");
                //id ranande
                intent.putExtra("id", currentTaxis.getInfo().getId().toString());
                startActivity(intent);

            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void PaymentDialog(PaymentTrackModel model) {
        if (model.getMessage().equals("1"))
            showDialog();
        else
            alertShow("پرداخت شما با مشکل مواجه شد. در صورتی که مبلغی از حساب شما کسر شده تا 72 ساعت آینده به حساب شما بازگشت داده خواهد شد.");
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(PaymentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.pardakhtdialog);
        dialog.setCancelable(false);
        dialog.show();
        Button btnGo = dialog.findViewById(R.id.btnGo);
        TextView content = dialog.findViewById(R.id.content);
        content.setText("پرداخت با موفقیت انجام شد.");
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i1 = new Intent(getApplicationContext(), MapsActivity.class);
                i1.setAction(Intent.ACTION_MAIN);
                i1.addCategory(Intent.CATEGORY_HOME);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i1);
                overridePendingTransition(0, 0);

                finish();
            }
        });
    }

    public void alertShow(String text) {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
        AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create();
        alertDialog.setTitle("پیام");
        // alertDialog.setIcon(R.drawable.ic_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        alertDialog.setMessage(FontUtils.typeface(typeface, text));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, FontUtils.typeface(typeface, "بستن"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
}