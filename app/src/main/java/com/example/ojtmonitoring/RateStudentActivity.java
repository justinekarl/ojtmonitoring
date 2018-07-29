package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RateStudentActivity extends AppCompatActivity {

    TextView studentNameTxt;
    RatingBar studentRatingBar;
    EditText remarksTxt;
    Button submitBtn;
    Button cancelBtn;
    int agentId;
    int studentId;
    private int rating;
    private String remarks;


    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_student);


        studentId = getIntent().getIntExtra("studentId",0);

        studentNameTxt = (TextView) findViewById(R.id.studentNameTxt);
        studentRatingBar = (RatingBar) findViewById(R.id.studentRatingBar);
        remarksTxt = (EditText)findViewById(R.id.remarksTxt);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);


        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);

        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentRatingBar.getRating() > 0) {
                    rating = (int) studentRatingBar.getRating();

                    remarks = null != remarksTxt.getText() ? remarksTxt.getText().toString() : "";
                    UpdateStudentRating updateStudentRating = new UpdateStudentRating();
                    updateStudentRating.execute();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(),StudentListActivity.class);
                startActivity(back);
            }
        });


    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        String displayText;
        String remarks;
        int rating;
        StringBuffer sb = new StringBuffer("");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RateStudentActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",agentId+""));
            params.add(new BasicNameValuePair("studentId",studentId+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getStudentRateValue.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("student_lists")){
                            JSONArray companyListArr =json.getJSONArray("student_lists");
                            if(null != companyListArr){
                                for(int i =0 ; i<companyListArr.length() ; i++) {

                                    for (int k = 0; k < companyListArr.getJSONArray(i).length(); k++) {
                                        String[] row = null;
                                        if (null != companyListArr.getJSONArray(i) && (companyListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                            row = (companyListArr.getJSONArray(i).get(k) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if (row.length > 1) {
                                                    value = row[1];
                                                }

                                                if (key.equals("student_name")) {
                                                    sb.append("Student Name: "+value);
                                                }
                                                if (key.equals("course")) {
                                                    sb.append("\n");
                                                    sb.append("Course: " +value);

                                                }

                                                if (key.equals("remarks")) {
                                                    remarks= value;
                                                }

                                                if (key.equals("student_rating")) {
                                                    rating = Integer.parseInt(value);;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

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
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            studentNameTxt.setText(sb.toString());
            studentRatingBar.setRating(rating);
            remarksTxt.setText(remarks);

        }
    }


    class UpdateStudentRating extends AsyncTask<String, String, String> {

        boolean isSuccessfullySaved = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RateStudentActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public UpdateStudentRating() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("studentId",studentId+""));
            params.add(new BasicNameValuePair("rating",rating+""));
            params.add(new BasicNameValuePair("companyId",agentId+""));
            params.add(new BasicNameValuePair("remarks",remarks));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"saveStudentRate.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        isSuccessfullySaved = true;
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
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(isSuccessfullySaved){
                toastMessage("Successfully updated!");
            }else{
                toastMessage("Error Occured!");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
