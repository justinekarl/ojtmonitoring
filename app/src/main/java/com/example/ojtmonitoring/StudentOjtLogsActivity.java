package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StudentOjtLogsActivity extends AppCompatActivity {

    int studentId;
    WebView studentLogsView;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_ojt_logs);
        studentLogsView = (WebView)findViewById(R.id.studentLogsView);

        PaceSettingManager.lockActivityOrientation(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        studentId = sharedPreferences.getInt("agent_id",0);

        //web
        pDialog = new ProgressDialog(StudentOjtLogsActivity.this);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();


        studentLogsView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        /*studentLogsView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });*/

        studentLogsView.getSettings().setLoadsImagesAutomatically(true);
        studentLogsView.getSettings().setJavaScriptEnabled(true);
        studentLogsView.getSettings().setDomStorageEnabled(true);
        studentLogsView.getSettings().setLoadWithOverviewMode(true);
        studentLogsView.getSettings().setUseWideViewPort(true);
        studentLogsView.getSettings().setBuiltInZoomControls(true);
        studentLogsView.getSettings().setDisplayZoomControls(false);
        studentLogsView.getSettings().setSupportZoom(true);
        studentLogsView.getSettings().setDefaultTextEncodingName("utf-8");

        /*studentLogsView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);*/
        studentLogsView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        studentLogsView.loadUrl(PaceSettingManager.IP_ADDRESS+"student/log/"+studentId);





        pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,StudentLoginActivity.class);
        startActivity(home);
        finish();
    }
}
