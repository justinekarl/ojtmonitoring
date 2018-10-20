package com.example.ojtmonitoring;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceCheckerMainActivity extends AppCompatActivity {


    private Button scanQrCodeBtn;
    private Button exitBtn;
    Activity activity = this;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private static String studentId;
    private static int companyId;
    private static boolean scanned;
    private static String scannedMessage;
    private int accounttype;
    private int updatedById;

    private boolean validToSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_checker_main);
        PaceSettingManager.lockActivityOrientation(this);

        scanQrCodeBtn = (Button)findViewById(R.id.scanQrCodeBtn);
        exitBtn = (Button)findViewById(R.id.exitBtn);


        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        accounttype = sharedPreferences.getInt("accounttype",0);
        if(accounttype == 3) {
            companyId = sharedPreferences.getInt("agent_id", 0);
        }

        if(accounttype == 4) {
            companyId = sharedPreferences.getInt("companyId", 0);
        }

        updatedById = sharedPreferences.getInt("agent_id", 0);


       /* scanQrCodeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //to call the scanner
                        IntentIntegrator integrator = new IntentIntegrator(activity);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Scan");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(false);
                        integrator.initiateScan();

                    }
                }
        );*/


        scanQrCodeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        //to call the scanner
                        IntentIntegrator integrator = new IntentIntegrator(activity);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Scan");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(false);
                        integrator.initiateScan();
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        /*exitBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(accounttype == 3) {
                            Intent companyLogin = new Intent(AttendanceCheckerMainActivity.this, CompanyLoginActivity.class);
                            startActivity(companyLogin);
                        }
                        if(accounttype == 4){
                            Intent coordinatorLogin = new Intent(AttendanceCheckerMainActivity.this, CoordinatorLoginActivity.class);
                            startActivity(coordinatorLogin);
                        }
                    }
                }
        );*/

        exitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        if(accounttype == 3) {
                            Intent companyLogin = new Intent(AttendanceCheckerMainActivity.this, CompanyLoginActivity.class);
                            startActivity(companyLogin);
                        }
                        if(accounttype == 4){
                            Intent coordinatorLogin = new Intent(AttendanceCheckerMainActivity.this, CoordinatorLoginActivity.class);
                            startActivity(coordinatorLogin);
                        }
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    //scan activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled The Scanning", Toast.LENGTH_LONG).show();
            } else {
                if (result.toString().length() > 0) {
                    StringBuffer value = new StringBuffer("");
                    boolean checker  = false;
                    String[] arr = result.toString().split(" ");
                    String first = "QR_CODE\n"+"Contents:";
                    String last  = "bytes:";
                    for ( String ss : arr) {
                        if (first.equalsIgnoreCase(ss)){
                            checker = true;
                            continue;
                        }
                        if (last.equalsIgnoreCase(ss)){
                            checker = false;
                        }
                        if(checker){
                            value.append(" " + ss);
                        }
                    }
                    String finalValue = value+"";
                    finalValue = finalValue.replace("\nRaw","");
                            //finalValue = finalValue.substring(3);
                    //finalValue = finalValue.substring(0, finalValue.length() - 5);

                    studentId = finalValue.trim();
                    try {
                        validToSave = false;
                        processLog(studentId);
                    }catch (Exception e){
                        Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }


                    ///AddData(finalValue+"");
                } else {
                    Toast.makeText(this,"Error Occured!",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    //connect database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background_light thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AttendanceCheckerMainActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agent_id", studentId));
            params.add(new BasicNameValuePair("company_id", companyId+""));
            params.add(new BasicNameValuePair("updated_by_id", updatedById+""));

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date loginDate = new Date();
            Date logoutDate = new Date();

            params.add(new BasicNameValuePair("login_date", dateFormat.format(loginDate)));
            params.add(new BasicNameValuePair("logout_date", dateFormat.format(logoutDate)));

            // getting JSON Object
            JSONObject json;
            json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"processLoginLogout.php","POST",params);

            try {
                if (null != json) {
                    if (json.has("login_logout_action")) {
                        scanned = true;
                        scannedMessage = json.getString("login_logout_action");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(scanned){
                Toast.makeText(AttendanceCheckerMainActivity.this,scannedMessage,Toast.LENGTH_SHORT).show();
            }

        }
    }

    //connect database


    private void processLog(final String studentId) throws Exception{
        if(null != studentId && Integer.valueOf(studentId.trim()) > 0){
            ValidateStudent validateStudent = new ValidateStudent();
            validateStudent.execute();
        }
    }





    class ValidateStudent extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AttendanceCheckerMainActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ValidateStudent() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("studentId",studentId+""));
            params.add(new BasicNameValuePair("companyId",companyId+""));



            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"isStudentAnOJT.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("is_an_ojt")){
                            validToSave = (Integer.parseInt(json.get("is_an_ojt")+"") == 1);
                        }
                    }

                }else{
                    //loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(validToSave) {
                ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
                connectToDataBaseViaJson.execute();
            }else{
                Toast.makeText(AttendanceCheckerMainActivity.this,"Invalid Student!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidToSave(int studentId){
        if(studentId > 0){
            ValidateStudent validateStudent = new ValidateStudent();
            validateStudent.execute();
        }
        return validToSave;
    }


}
