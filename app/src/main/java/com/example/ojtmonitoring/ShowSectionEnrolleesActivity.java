package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ojtmonitoring.info.UserAccountInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowSectionEnrolleesActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    Spinner sectionsSpinner;
    ListView studentListView;
    Button selectAllBtn;
    Button clearAllBtn;
    Button saveButton;
    Button cancelButton;
    private String college;
    ArrayAdapter<String> sectionNameAdapter = null;
    private String[] sectionNames;
    private String sectionNameSelected;
    private List<UserAccountInfo> userAccountInfos;
    HashMap<Integer, Boolean> studentAcctMap = new HashMap<Integer, Boolean>();
    CustomNewStudentAccountListView newStudentEnrolleeListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_section_enrollees);

        sectionsSpinner = (Spinner)findViewById(R.id.sectionsSpinner);
        studentListView = (ListView)findViewById(R.id.studentListView);
        selectAllBtn = (Button)findViewById(R.id.selectAllBtn);
        clearAllBtn = (Button)findViewById(R.id.clearAllBtn);
        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        college = sharedPreferences.getString("college","");

        ConnectToDBViaJson populateSectionList = new ConnectToDBViaJson();
        populateSectionList.execute();


        sectionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) sectionsSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                sectionNameSelected = sectionsSpinner.getSelectedItem().toString();
                ConnectToDataBaseViaJson retrieveStudentsBySection = new ConnectToDataBaseViaJson();
                retrieveStudentsBySection.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selectAllBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        processUserInfos(true);
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

        /*selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUserInfos(true);
            }
        });

        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUserInfos(false);
            }
        });*/


        clearAllBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        processUserInfos(false);
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

        /*cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(),TeacherLoginActivity.class);
                startActivity(home);
            }
        });*/


        cancelButton.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent home = new Intent(getApplicationContext(),TeacherLoginActivity.class);
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

        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != userAccountInfos && userAccountInfos.size() > 0){
                    for(final UserAccountInfo userAccountInfo : userAccountInfos){
                        if(null != studentAcctMap){
                            if(!studentAcctMap.containsKey(userAccountInfo.getId()) && userAccountInfo.isApproved()){
                                studentAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                            }else{
                                if(studentAcctMap.size() > 0) {
                                    //let's get the previous value of the account (approved or not)
                                    boolean prevValue = studentAcctMap.get(userAccountInfo.getId());

                                    //if there is a change in the status, remove if from the map and add the new value
                                    if (prevValue != userAccountInfo.isApproved()) {
                                        studentAcctMap.remove(userAccountInfo.getId());
                                        studentAcctMap.put(userAccountInfo.getId(), userAccountInfo.isApproved());
                                    } else {
                                        //remove if they are the same we don't need to re-save them in the backend
                                        studentAcctMap.remove(userAccountInfo.getId());
                                    }
                                }
                            }
                        }
                    }

                    if(null != studentAcctMap && studentAcctMap.size() > 0){
                        ProcessAcceptStudents processAcceptStudent =new ProcessAcceptStudents();
                        processAcceptStudent.execute();
                    }
                }

                if(null != studentAcctMap && studentAcctMap.size() == 0) {
                    toastMessage("No Student/s to approve selected.");
                }
            }
        });*/


        saveButton.setOnTouchListener(new View.OnTouchListener() {
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
                        if(null != userAccountInfos && userAccountInfos.size() > 0){
                            for(final UserAccountInfo userAccountInfo : userAccountInfos){
                                if(null != studentAcctMap){
                                    if(!studentAcctMap.containsKey(userAccountInfo.getId()) && userAccountInfo.isApproved()){
                                        studentAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                    }else{
                                        if(studentAcctMap.size() > 0) {
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = studentAcctMap.get(userAccountInfo.getId());

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if (prevValue != userAccountInfo.isApproved()) {
                                                studentAcctMap.remove(userAccountInfo.getId());
                                                studentAcctMap.put(userAccountInfo.getId(), userAccountInfo.isApproved());
                                            } else {
                                                //remove if they are the same we don't need to re-save them in the backend
                                                studentAcctMap.remove(userAccountInfo.getId());
                                            }
                                        }
                                    }
                                }
                            }

                            if(null != studentAcctMap && studentAcctMap.size() > 0){
                                ProcessAcceptStudents processAcceptStudent =new ProcessAcceptStudents();
                                processAcceptStudent.execute();
                            }
                        }

                        if(null != studentAcctMap && studentAcctMap.size() == 0) {
                            toastMessage("No Student/s to approve selected.");
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

    private void processUserInfos(final boolean selectAll){
        if(null != newStudentEnrolleeListViewAdapter.getUserAccountInfos() && newStudentEnrolleeListViewAdapter.getUserAccountInfos().size() > 0){
            for(final UserAccountInfo userAccountInfo : newStudentEnrolleeListViewAdapter.getUserAccountInfos()){
                userAccountInfo.setApproved(selectAll);
            }

            studentListView.setAdapter(newStudentEnrolleeListViewAdapter);
        }
    }

    class ConnectToDBViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowSectionEnrolleesActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDBViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("college",college));
            params.add(new BasicNameValuePair("agentid","0"));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveSections.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {


                        if(json.has("section_list")){
                            JSONArray jsonArray = json.getJSONArray("section_list");
                            if(null != jsonArray){
                                sectionNames= new String[jsonArray.length()+1];
                                for(int ctr = 0;  ctr <= jsonArray.length() ; ctr++) {
                                    if(ctr==0){
                                        sectionNames[ctr] = "---Select Section---";
                                        continue;
                                    }
                                    for (int i = 0; i < jsonArray.getJSONArray(ctr-1).length(); i++) {
                                        if(null != jsonArray.getJSONArray(ctr-1) && i==1) {
                                            sectionNames[ctr] = jsonArray.getJSONArray(ctr-1).get(i).toString();
                                            break;
                                        }

                                    }
                                }
                            }
                        }

                        if(json.has("section_id")){

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

            if(null != sectionNames && sectionNames.length > 0) {

                sectionNameAdapter = new ArrayAdapter<String>(ShowSectionEnrolleesActivity.this, android.R.layout.simple_list_item_1, sectionNames);
                sectionsSpinner.setAdapter(sectionNameAdapter);
            }


        }
    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowSectionEnrolleesActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("section",sectionNameSelected));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getPendingStudentSectionEnrolles.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("pending_accounts")){
                            userAccountInfos = new ArrayList<UserAccountInfo>();
                            JSONArray studentListArr =json.getJSONArray("pending_accounts");
                            if(null != studentListArr){
                                for(int i =0 ; i<=studentListArr.length() ; i++){
                                    UserAccountInfo userAccountInfo = new UserAccountInfo();
                                    for (int k = 0; k <= studentListArr.getJSONArray(i).length() - 1; k++) {
                                        String[] row = null;
                                        if(null != studentListArr.getJSONArray(i) && (studentListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                            row = (studentListArr.getJSONArray(i).get(k) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("student_id")){
                                                    userAccountInfo.setId(Integer.parseInt(value));
                                                }
                                                if(key.equals("name")){
                                                    userAccountInfo.setName(value);
                                                }

                                                if(key.equals("college")){
                                                    userAccountInfo.setCollege(value);
                                                }

                                                if(key.equals("department")){
                                                    userAccountInfo.setDepartment(value);
                                                }

                                                if(key.equals("gender")){
                                                    userAccountInfo.setGender(value);
                                                }

                                                if(key.equals("accounttype")){
                                                    userAccountInfo.setAccountType(Integer.valueOf(value));
                                                }

                                                if(key.equals("accepted")){
                                                    userAccountInfo.setApproved(Boolean.valueOf(value));
                                                }

                                            }

                                        }
                                    }
                                    userAccountInfos.add(userAccountInfo);
                                    if(null != studentAcctMap){
                                        if(!studentAcctMap.containsKey(userAccountInfo.getId())){
                                            studentAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                        }else{
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = studentAcctMap.get(userAccountInfo.getId());

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if(prevValue != userAccountInfo.isApproved()){
                                                studentAcctMap.remove(userAccountInfo.getId());
                                                studentAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                            }else{
                                                //remove if they are the same we don't need to re-save them in the backend
                                                studentAcctMap.remove(userAccountInfo.getId());
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }else{
                        userAccountInfos = new ArrayList<UserAccountInfo>();
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
            newStudentEnrolleeListViewAdapter = new CustomNewStudentAccountListView(userAccountInfos,ShowSectionEnrolleesActivity.this);
            studentListView.setAdapter(newStudentEnrolleeListViewAdapter);

        }
    }


    class ProcessAcceptStudents extends AsyncTask<String, String, String> {

        boolean triggerUpdate;
        String message;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowSectionEnrolleesActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessAcceptStudents() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("section",sectionNameSelected+""));

            final StringBuffer sb = new StringBuffer("");
            if(null != studentAcctMap && studentAcctMap.size() > 0){

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, Boolean>>(){}.getType();
                String json = gson.toJson(studentAcctMap, integerObjectMapType);
                sb.append(json);



            }

            params.add(new BasicNameValuePair("studentAcctMap",sb.toString()));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"saveApprovedStudentSectionsAccounts.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        triggerUpdate= true;
                        message = "Successfully accepted selected students!";
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
            ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
            connectToDataBaseViaJson.execute();
            if(triggerUpdate){
                Toast.makeText(ShowSectionEnrolleesActivity.this,message,Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
