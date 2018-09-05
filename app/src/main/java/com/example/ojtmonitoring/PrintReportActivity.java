package com.example.ojtmonitoring;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Environment;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PrintReportActivity extends AppCompatActivity {
    private TextView contentHtmlText;
    private WebView webViewPage;
    private LinearLayout lnearlyout;
    private Button updateBtn;
    Bitmap bitmap;
    int width = 0;
    int height = 0;
    String htmlResult;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    int agentId;
    int accounttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_report);
        PaceSettingManager.lockActivityOrientation(this);

        lnearlyout = (LinearLayout)findViewById(R.id.lnearlyout);
        webViewPage = (WebView)findViewById(R.id.webViewPage);
        updateBtn = (Button)findViewById(R.id.updateBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        accounttype = sharedPreferences.getInt("accounttype",0);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
                connectToDataBaseViaJson.execute();
            }
        });

        updateBtn.callOnClick();
    }



    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());


        // Save the job object for later status checking
       // mPrintJob = printJob;
    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PrintReportActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(accounttype == 3) {
                params.add(new BasicNameValuePair("agentId", agentId + ""));
            }

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"reportweekly",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    if(json.has("data")) {
                        htmlResult = json.get("data").toString();
                    }

                    /*int success = json.getInt("success");
                    if(success == 1) {
                        htmlResult
                    }*/

                }else{
                    //loginMessage="Invalid User";
                }

            } catch (Exception e) {
                e.printStackTrace();
                //loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            webViewPage.loadData(htmlResult,"text/html","utf-8");
            createWebPrintJob(webViewPage);

        }
    }
}
