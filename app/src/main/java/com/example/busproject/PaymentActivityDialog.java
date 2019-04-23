package com.example.busproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.busproject.Model.PaymentTrackModel;
import com.example.busproject.Utils.Helpers;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PaymentActivityDialog extends AppCompatActivity {

    private WebView webView;

    ProgressDialog dialog;

    ImageView imgMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        bindViews();

    }

    private void bindViews() {

        // toolbarText = findViewById(R.id.tooTitle);
        imgMenu=findViewById(R.id.imgMenu);
        webView = findViewById(R.id.webView);
        //    imgbMenu = findViewById(R.id.imgbBack);

        //    imgbMenu.setVisibility(View.GONE);
        //     toolbarText.setText("درگاه پرداخت");

        imgMenu.setVisibility(View.GONE);
        dialog = new ProgressDialog(PaymentActivityDialog.this);
        dialog.setMessage("درحال بارگذاری ...");
        dialog.show();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        final String tag = intent.getStringExtra("tag");
        String paymentApi = Helpers.baseUrl + "parsian_call";
//        String postData = "id=56&mobile=09356495962&get=0&tag=shop&state=050003&api_token=" + tools.getSharePrf("api_token");
        String postData = "id=" + id + "&tag=" + tag + "&api_token=" + Helpers.getSharePrf("api_token") + "&mobile=" + Helpers.getMobile();

        webView.postUrl(paymentApi, postData.getBytes());

        webView.getSettings().setJavaScriptEnabled(true);

        WebViewClient mWebViewClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    dialog.show();
                } catch (Exception e) {
                }

                if (url.contains("backapp"))
                    if (url.toLowerCase().contains("status=ok")) {

                        Pattern pattern = Pattern.compile("track=\\d+");
                        Matcher matcher = pattern.matcher(url);

                        String ref_id = "";
                        if (matcher.find(0)) {
                            ref_id = matcher.group(0);
                        }

                        ref_id = ref_id.replace("track=", "");

                        if (tag.equals("taxi"))
                            EventBus.getDefault().post(new PaymentTrackModel("1"));


                        finish();
                    } else if (url.toLowerCase().contains("status=notok")) {
                        if (tag.equals("taxi"))
                            EventBus.getDefault().post(new PaymentTrackModel("0"));

                        finish();
                    }
            }
        };

        webView.setWebViewClient(mWebViewClient);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new PaymentTrackModel("0"));

    }
}
