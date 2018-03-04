package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.StudentLoginLogoutLogsInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowStudentLoginLogoutActivity extends AppCompatActivity {

    private EditText studentNameFilterTxt;
    private Button searchBtn;
    private Button resetBtn;
    private EditText fromTxt;
    private EditText thruTxt;
    private String from;
    private String thru;

    private String studentNameFilter;
    private String companyNameFilter;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private int teacherId;

    private ListView resultLsView;
    private List<StudentLoginLogoutLogsInfo> studentLoginLogoutLogsInfos;
    private Spinner companySpnr;
    private String[] companyNames;

    ArrayAdapter<String> companyNameAdapter = null;
    private ArrayList<String> companyNameList = new ArrayList<>();

    private EditText companyNameFilterTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_login_logout);

        studentNameFilterTxt = (EditText)findViewById(R.id.studentNameFilterTxt);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        resultLsView = (ListView)findViewById(R.id.resultLsView);
        resetBtn = (Button)findViewById(R.id.resetBtn);
        companyNameFilterTxt = (EditText)findViewById(R.id.companyNameFilterTxt);
        fromTxt = (EditText)findViewById(R.id.fromTxt);
        thruTxt = (EditText)findViewById(R.id.thruTxt);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);

        RetrieveCompanyName retrieveCompanyName = new RetrieveCompanyName();
        retrieveCompanyName.execute();

        if(null != companyNames && companyNames.length > 0) {

            companyNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, companyNames){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position,convertView,parent);
                    if(position%2 == 0)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.divider));
                    }else{
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    return view;
                }
            };


            companySpnr.setAdapter(companyNameAdapter);
        }


        searchBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentNameFilter = null != studentNameFilterTxt.getText() ? studentNameFilterTxt.getText().toString() :"";
                        companyNameFilter = null != companyNameFilterTxt.getText() ? companyNameFilterTxt.getText().toString() : "";
                        from = null != fromTxt.getText() ? fromTxt.getText().toString() : "";
                        thru = null != thruTxt.getText() ? thruTxt.getText().toString() : "";

                        if(((null == thru || thru.trim().length() == 0) && (null != from && from.trim().length() > 0)) && ((null == from || from.trim().length() == 0) && (null != thru && thru.trim().length() > 0))){
                            Toast.makeText(ShowStudentLoginLogoutActivity.this,"From and Thru must be filled out.",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
                        connectToDataBaseViaJson.execute();

                    }
                }
        );

        resetBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentNameFilterTxt.setText("");
                        companyNameFilterTxt.setText("");
                        fromTxt.setText("");
                        thruTxt.setText("");
                        if(null != studentLoginLogoutLogsInfos) {
                            studentLoginLogoutLogsInfos.clear();
                        }

                        if(null != resultLsView){
                            resultLsView.setAdapter(null);
                        }
                    }
                }
        );


    }


    //connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowStudentLoginLogoutActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));
            params.add(new BasicNameValuePair("studentName",studentNameFilter));
            params.add(new BasicNameValuePair("companyName",companyNameFilter));
            try {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                if (null != from) {
                    Date fromDate = dateFormat.parse(from);

                    SimpleDateFormat dateFormat1= new SimpleDateFormat("yyyy/MM/dd");
                    params.add(new BasicNameValuePair("from", dateFormat1.format(fromDate)));
                }

                if (null != thru) {

                    Date thruDate = dateFormat.parse(thru);
                    SimpleDateFormat dateFormat1= new SimpleDateFormat("yyyy/MM/dd");

                    params.add(new BasicNameValuePair("thru", dateFormat1.format(thruDate)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }



            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveStudentLoginLogoutLogs.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

                        if(null == studentLoginLogoutLogsInfos){
                            studentLoginLogoutLogsInfos = new ArrayList<StudentLoginLogoutLogsInfo>();
                        }

                        JSONArray items = json.getJSONArray("student_list");

                        studentLoginLogoutLogsInfos.clear();

                        if(null != items){
                            for(int ctr = 0;  ctr < items.length() ; ctr++){
                                StudentLoginLogoutLogsInfo studentLoginLogoutLogsInfo = new StudentLoginLogoutLogsInfo();
                                for(int i = 0 ; i <= items.getJSONArray(ctr).length()-1 ; i++) {
                                    String[] row = null;
                                    if(null != items.getJSONArray(ctr) && (items.getJSONArray(ctr).get(i) + "").contains("~")) {
                                        row = (items.getJSONArray(ctr).get(i) + "").split("~");
                                        if (null != row && row.length > 0) {
                                            String key = "";
                                            String value = "";
                                            key = row[0];
                                            if(row.length > 1) {
                                                value = row[1];
                                            }

                                            if(key.equals("student_name")){
                                                studentLoginLogoutLogsInfo.setStudentName(value);
                                            }

                                            if(key.equals("company_name")){
                                                studentLoginLogoutLogsInfo.setCompanyName(value);
                                            }

                                            if(key.equals("login_date")){
                                                studentLoginLogoutLogsInfo.setLoginDate(value);
                                            }
                                            if(key.equals("logout_date")){
                                                studentLoginLogoutLogsInfo.setLogoutDate(value);
                                            }

                                        }
                                    }
                                }

                                studentLoginLogoutLogsInfos.add(studentLoginLogoutLogsInfo);
                            }
                        }

                    }else {
                        if(null != json.getString("student_list")){
                            if(null != studentLoginLogoutLogsInfos && studentLoginLogoutLogsInfos.size() > 0) {
                                studentLoginLogoutLogsInfos.clear();
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

            if(null != studentLoginLogoutLogsInfos && studentLoginLogoutLogsInfos.size() == 0) {
                Toast.makeText(ShowStudentLoginLogoutActivity.this,"No Results Found",Toast.LENGTH_SHORT).show();
            }

            CustomStudentLoginLogoutListView customStudentLoginLogoutListView = new CustomStudentLoginLogoutListView(studentLoginLogoutLogsInfos, ShowStudentLoginLogoutActivity.this);
            resultLsView.setAdapter(customStudentLoginLogoutListView);



        }
    }
    //end of connecting



    //connecting to the database

    class RetrieveCompanyName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(ShowStudentLoginLogoutActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public RetrieveCompanyName() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveCompanyNames.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

                        if(json.has("company_names")){
                            JSONArray jsonArray = json.getJSONArray("company_names");
                            if(null != jsonArray){
                                companyNames= new String[json.length()];
                                for(int ctr = 0;  ctr < jsonArray.length() ; ctr++){

                                    for(int i = 0 ; i <= jsonArray.getJSONArray(ctr).length()-1 ; i++) {
                                        String[] row = null;
                                        if(null != jsonArray.getJSONArray(ctr) && (jsonArray.getJSONArray(ctr).get(i) + "").contains("~")) {
                                            row = (jsonArray.getJSONArray(ctr).get(i) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("name")){
                                                    companyNames[i] = value;
                                                    companyNameList.add(value);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }else {
                        if(null != json.getString("company_names")){

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


        }
    }
    //end of connecting

}
