package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowTimeAccumulatedActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private int agentId;
    private Button homeBtn;
    private TextView timeTxt;
    private TextView startDateTxt;
    private TextView endDateTxt;
    private TextView remainingTimeTxt;
    private TextView statusTxt;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time_accumulated);
        PaceSettingManager.lockActivityOrientation(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id", 0);
        status = sharedPreferences.getString("ojt_status","");


        homeBtn = (Button)findViewById(R.id.homeBtn);
        timeTxt = (TextView) findViewById(R.id.timeTxt);
        startDateTxt = (TextView)findViewById(R.id.startDateTxt);
        endDateTxt = (TextView)findViewById(R.id.endDateTxt);
        remainingTimeTxt = (TextView)findViewById(R.id.remainingTimeTxt);
        statusTxt = (TextView)findViewById(R.id.statusTxt);


        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        /*homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(ShowTimeAccumulatedActivity.this,StudentLoginActivity.class);
                startActivity(home);
            }
        });*/

        homeBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent home = new Intent(ShowTimeAccumulatedActivity.this,StudentLoginActivity.class);
                        startActivity(home);
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

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        String time="";
        String startDate="";
        String endDate="";
        String remainingTime="";
        String percentageStr="0";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowTimeAccumulatedActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid", agentId + ""));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "getStudentAccumulatedTime.php",
                    "POST", params);


            try {
                if (null != json) {
                    if(json.has("time_accumulated") && null != json.get("time_accumulated") && !json.get("time_accumulated").toString().equals("None")){
                        JSONArray notifListArr =json.getJSONArray("time_accumulated");

                        if(null != notifListArr){
                            for(int i =0 ; i<notifListArr.length() ; i++){

                                for (int k = 0; k < notifListArr.getJSONArray(i).length(); k++) {
                                    String[] row = null;
                                    if(null != notifListArr.getJSONArray(i) && (notifListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                        row = (notifListArr.getJSONArray(i).get(k) + "").split("~");
                                        if (null != row && row.length > 0) {
                                            String key = "";
                                            String value = "";
                                            key = row[0];
                                            if(row.length > 1) {
                                                value = row[1];
                                            }

                                            if(key.equals("time_accumulated")){
                                                time = value;
                                            }

                                            if(key.equals("start_date")){
                                                startDate =value;
                                            }



                                            if(key.equals("remaining_time")){
                                                remainingTime =value;
                                            }

                                        }
                                    }
                                }


                            }

                        }
                    }

                    if(json.has("percentage") && null != json.get("percentage") && !json.get("percentage").toString().equals("null")){
                        double val = Double.valueOf(json.get("percentage").toString());
                        if(val > 100){
                            val = 100;
                        }
                        percentageStr = val+"";
                    }

                    if(json.has("end_date")){
                        endDate =json.getString("end_date");
                    }
                } else {
                    //loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //loginMessage="Invalid User";
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            timeTxt.setText(time);
            startDateTxt.setText(startDate);
            remainingTimeTxt.setText(remainingTime);
            endDateTxt.setText(endDate);
            statusTxt.setText(status + " - "+percentageStr+"%");

            SharedPreferences sharedpreferences = getSharedPreferences(
                    PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("ojtDone",percentageStr.equals("100.0") ? "1" : "0");
            editor.commit();


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,StudentLoginActivity.class);
        startActivity(home);
        finish();
    }
}
