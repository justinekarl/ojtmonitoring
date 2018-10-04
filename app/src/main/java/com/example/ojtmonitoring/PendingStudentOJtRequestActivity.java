package com.example.ojtmonitoring;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PendingStudentOJtRequestActivity extends AppCompatActivity {
    WebView pendingStudentsWebView;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    int teacherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_student_ojt_request);

        pendingStudentsWebView = (WebView)findViewById(R.id.pendingStudentsWebView);
        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);

        //web
        pDialog = new ProgressDialog(PendingStudentOJtRequestActivity.this);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();


        pendingStudentsWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        pendingStudentsWebView.setWebChromeClient(new WebChromeClient() {
                                  @Override
                                  public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                                      //Required functionality here
                                      return super.onJsAlert(view, url, message, result);
                                  }
                              });

        pendingStudentsWebView.getSettings().setLoadsImagesAutomatically(true);
        pendingStudentsWebView.getSettings().setJavaScriptEnabled(true);
        pendingStudentsWebView.getSettings().setDomStorageEnabled(true);
        pendingStudentsWebView.getSettings().setLoadWithOverviewMode(true);
        pendingStudentsWebView.getSettings().setUseWideViewPort(true);
        pendingStudentsWebView.getSettings().setBuiltInZoomControls(true);
        pendingStudentsWebView.getSettings().setDisplayZoomControls(false);
        pendingStudentsWebView.getSettings().setSupportZoom(true);
        pendingStudentsWebView.getSettings().setDefaultTextEncodingName("utf-8");

        pendingStudentsWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        pendingStudentsWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        pendingStudentsWebView.loadUrl(PaceSettingManager.IP_ADDRESS+"resume/teacher/"+teacherId);


        /*pendingStudentsWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });*/


        /*pendingStudentsWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Downloading...");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });*/


        pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent viewApprovedAccts = new Intent(this,TeacherLoginActivity.class);
        startActivity(viewApprovedAccts);
        finish();
    }
}
