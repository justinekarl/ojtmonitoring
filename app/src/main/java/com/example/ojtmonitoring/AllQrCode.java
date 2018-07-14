package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllQrCode extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private ProgressDialog pDialog;
    private Button clearLogs;
    private Button search;
    private Button showAll;
    private CheckBox checkAll;
    public static String logAction;
    JSONParser jsonParser = new JSONParser();

    RelativeLayout relativeMain;
    int flag=0;
    ArrayList <Integer>checkedLogs = new ArrayList<>();
    ArrayList <Integer>allCheckedLogs = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_qr_code);

        //pop confirmation
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete selected Qr Codes?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(null != checkedLogs && checkedLogs.size() > 0){
                            ConnectToDataBaseViaJson clearLogs = new ConnectToDataBaseViaJson();
                            clearLogs.execute();
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });
        // end of confirmation

        checkAll = (CheckBox) findViewById(R.id.checkAllQr);
        clearLogs = (Button) findViewById(R.id.clearLogsQr);
        search = (Button) findViewById(R.id.searchQr);
        showAll = (Button) findViewById(R.id.showAllQrCodes);




        LinearLayout checkboxLayout = (LinearLayout) findViewById(R.id.chkboxlytQr);
        int ctr=1;
        for (Map<String, String> transaction : admin.allQrCodes) {
            allCheckedLogs.add(Integer.valueOf(transaction.get("id_qr_codes")));
            CheckBox cb = new CheckBox(this);
            cb.setText(transaction.get("item"));
            cb.setId(Integer.valueOf(transaction.get("id_qr_codes")));
            cb.setTextSize(12);
            cb.setTextColor(Color.rgb(0, 0, 0));
            cb.setTypeface(Typeface.MONOSPACE);

            if((ctr%2) == 0){
                cb.setBackgroundColor(getResources().getColor(R.color.divider));
            }
            checkboxLayout.addView(cb);
            cb.setOnCheckedChangeListener(this);
            ctr++;
        }



        checkAll.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                     for (Integer id :  allCheckedLogs){
                         ((CheckBox) findViewById(id)).setChecked(isChecked);
                     }
                 }
             }
        );

        search.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaceSettingManager.searchAction = "searchQr";
                Intent search = new Intent(AllQrCode.this, Search.class);
                startActivity(search);
            }
        });

        showAll.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAll.setClickable(false);
                logAction = "showAllQr";
                admin.allQrCodes.clear();
                ConnectToDataBaseViaJson showAllQr = new ConnectToDataBaseViaJson();
                showAllQr.execute();
            }
        });

        clearLogs.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedLogs.size() > 0){
                    logAction = "clearLogsQr";
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    toastMessage("Nothing Selected");
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent content = new Intent(AllQrCode.this, admin.class);
        startActivity(content);
    }


    public void onCheckedChanged(CompoundButton cb, boolean isChecked){
        if(isChecked){
            checkedLogs.add(cb.getId());
        } else {
            ArrayList <Integer>checkedLogsRemove = new ArrayList<>();
            checkedLogsRemove.add(cb.getId());
            checkedLogs.removeAll(checkedLogsRemove);
        }
    }

    public void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
        flag=1;
        savedState.putIntegerArrayList("checkedLogs", checkedLogs);
        savedState.putInt("savedflag", flag);
    }






    //connect database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            admin.allQrCodes.clear();
            super.onPreExecute();
            pDialog = new ProgressDialog(AllQrCode.this);
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

            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = new JSONObject();
            if(logAction == "showAllQr"){
                params.add(new BasicNameValuePair("action", "search"));
                params.add(new BasicNameValuePair("parameter", "all"));
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_qr_code.php",
                        "POST", params);
            }else if(logAction == "clearLogsQr"){
                params.add(new BasicNameValuePair("action", "delete"));
                params.add(new BasicNameValuePair("parameter", PaceSettingManager.integerTooCommaSeparated(checkedLogs)));
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_qr_code.php",
                        "POST", params);
            }

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                JSONArray fetchedQrCodes = json.getJSONArray("data_needed");
                for (int ctr = 0; ctr < fetchedQrCodes.length();ctr++ ) {
                    Map<String, String> qrMap = new HashMap<>();
                    qrMap.put("id_qr_codes",fetchedQrCodes.getJSONArray(ctr).getJSONObject(0).get("id_qr_codes").toString());
                    qrMap.put("item",fetchedQrCodes.getJSONArray(ctr).getJSONObject(1).get("item").toString());
                    admin.allQrCodes.add(qrMap);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            Intent qrClass = new Intent(AllQrCode.this,AllQrCode.class);
            startActivity(qrClass);
            showAll.setClickable(true);
            pDialog.hide();
        }
    }

    //connect database

    public void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }




}
