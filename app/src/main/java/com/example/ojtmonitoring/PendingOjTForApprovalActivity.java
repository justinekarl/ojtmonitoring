package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PendingOjTForApprovalActivity extends AppCompatActivity {
    WebView pendingojtwv;
    int companyId;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_oj_tfor_approval);
        pendingojtwv = (WebView)findViewById(R.id.pendingojtwv);


        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        companyId = sharedPreferences.getInt("agent_id",0);

        //web
        pDialog = new ProgressDialog(PendingOjTForApprovalActivity.this);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();


        pendingojtwv.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        /*pendingojtwv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });*/

        pendingojtwv.getSettings().setLoadsImagesAutomatically(true);
        pendingojtwv.getSettings().setJavaScriptEnabled(true);
        pendingojtwv.getSettings().setDomStorageEnabled(true);
        pendingojtwv.getSettings().setLoadWithOverviewMode(true);
        pendingojtwv.getSettings().setUseWideViewPort(true);
        pendingojtwv.getSettings().setBuiltInZoomControls(true);
        pendingojtwv.getSettings().setDisplayZoomControls(false);
        pendingojtwv.getSettings().setSupportZoom(true);
        pendingojtwv.getSettings().setDefaultTextEncodingName("utf-8");

        /*pendingojtwv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);*/
        pendingojtwv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        pendingojtwv.loadUrl(PaceSettingManager.IP_ADDRESS+"resume/company/"+companyId);





        pDialog.dismiss();
    }
}
