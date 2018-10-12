package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StudentInformationActivity extends AppCompatActivity {
    WebView studentInfoView;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);
        studentInfoView = (WebView)findViewById(R.id.studentInfoView);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        studentId = sharedPreferences.getInt("agent_id",0);

        //web
        pDialog = new ProgressDialog(StudentInformationActivity.this);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();


        studentInfoView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        /*studentInfoView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });*/

        studentInfoView.getSettings().setLoadsImagesAutomatically(true);
        studentInfoView.getSettings().setJavaScriptEnabled(true);
        studentInfoView.getSettings().setDomStorageEnabled(true);
        studentInfoView.getSettings().setLoadWithOverviewMode(true);
        studentInfoView.getSettings().setUseWideViewPort(true);
        studentInfoView.getSettings().setBuiltInZoomControls(true);
        studentInfoView.getSettings().setDisplayZoomControls(false);
        studentInfoView.getSettings().setSupportZoom(true);
        studentInfoView.getSettings().setDefaultTextEncodingName("utf-8");

        /*studentInfoView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);*/
        studentInfoView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        studentInfoView.loadUrl(PaceSettingManager.IP_ADDRESS+"student/"+studentId);





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
