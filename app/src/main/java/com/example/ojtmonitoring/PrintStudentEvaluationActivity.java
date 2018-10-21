package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrintStudentEvaluationActivity extends AppCompatActivity {

    WebView printEvalView;
    int agentId;
    int accounttype;
    String college;
    private int studentId;
    private ProgressDialog pDialog;
    private String htmlResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_student_evaluation);
        printEvalView = (WebView)findViewById(R.id.printEvalView);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        accounttype = sharedPreferences.getInt("accounttype",0);
        college = sharedPreferences.getString("college","");


        if(null != getIntent()) {
            studentId = getIntent().getIntExtra("studentId",0);
        }

        new AsyncTask<Void,String,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(PrintStudentEvaluationActivity.this);
                pDialog.setMessage("Processing..");
                pDialog.setIndeterminate(false);

                pDialog.setCancelable(true);
            }

            @Override
            protected String doInBackground(Void... params) {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("student_id",studentId+""));
                JSONParser jsonParser1 = new JSONParser();

                JSONObject json = jsonParser1.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "printStudentEvaluation",
                        "POST", params1);


                try {
                    if (null != json) {

                        // check log cat fro response
                        Log.d("Create Response", json.toString());

                        if (json.has("data")) {
                            htmlResult = json.get("data").toString();
                        }

                                        /*int success = json.getInt("success");
                                        if(success == 1) {
                                            htmlResult
                                        }*/

                    } else {
                        //loginMessage="Invalid User";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //loginMessage="Invalid User";
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();
                printEvalView.loadData(htmlResult, "text/html", "utf-8");
                createWebPrintJob(printEvalView);
            }
        }.execute();

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
}
