package com.example.ojtmonitoring;

import android.app.ProgressDialog;
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
import android.widget.ListView;
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

public class NewStudentAccountsActivity extends AppCompatActivity {

    HashMap<Integer, Boolean> studentAcctMap = new HashMap<Integer, Boolean>();
    List<String> studentsToUpdateList = new ArrayList<String>();
    ListView studentAcctsLstView;
    CustomNewStudentAccountListView newStudentAccountListViewAdapter;
    private ProgressDialog pDialog;
    private List<UserAccountInfo> userAccountInfos;
    private int teacherId;
    JSONParser jsonParser = new JSONParser();
    private String college;
    private boolean triggerUpdate;
    private String message;

    Button apprvSelBtn;
    Button viewAllApprBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student_accounts);
        PaceSettingManager.lockActivityOrientation(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);
        college = sharedPreferences.getString("college","");

        studentAcctsLstView = (ListView)findViewById(R.id.studentAcctsLstView);
        apprvSelBtn = (Button)findViewById(R.id.apprvSelBtn);
        viewAllApprBtn = (Button)findViewById(R.id.viewAllApprBtn);

        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();


        //allowing vertical scroll even in scroll view
        studentAcctsLstView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                v.onTouchEvent(event);
                return true;
            }
        });

        apprvSelBtn.setOnTouchListener(new View.OnTouchListener() {
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


        /*apprvSelBtn.setOnClickListener(
                new View.OnClickListener() {
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
                }
        );*/

        viewAllApprBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent viewApprovedAccts = new Intent(NewStudentAccountsActivity.this,ViewApprovedAccountsActivity.class);
                        startActivity(viewApprovedAccts);
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

        /*viewAllApprBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent viewApprovedAccts = new Intent(NewStudentAccountsActivity.this,ViewApprovedAccountsActivity.class);
                        startActivity(viewApprovedAccts);
                    }
                }
        );*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent viewApprovedAccts = new Intent(NewStudentAccountsActivity.this,TeacherLoginActivity.class);
        startActivity(viewApprovedAccts);
        finish();
    }

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewStudentAccountsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",teacherId+""));
            params.add(new BasicNameValuePair("college",college));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrievePendingStudentAccounts.php",
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

                                                if(key.equals("username")){
                                                    userAccountInfo.setUserName(value);
                                                }

                                                if(key.equals("accounttype")){
                                                    userAccountInfo.setAccountType(Integer.valueOf(value));
                                                }

                                                if(key.equals("accepted")){
                                                    userAccountInfo.setApproved(Boolean.valueOf(value));
                                                }

                                                if(key.equals("gender")){
                                                    userAccountInfo.setGender(value);
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
            newStudentAccountListViewAdapter = new CustomNewStudentAccountListView(userAccountInfos,NewStudentAccountsActivity.this);
            studentAcctsLstView.setAdapter(newStudentAccountListViewAdapter);

        }
    }



    class ProcessAcceptStudents extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewStudentAccountsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessAcceptStudents() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));

            final StringBuffer sb = new StringBuffer("");
            if(null != studentAcctMap && studentAcctMap.size() > 0){

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, Boolean>>(){}.getType();
                String json = gson.toJson(studentAcctMap, integerObjectMapType);
                sb.append(json);



            }

            params.add(new BasicNameValuePair("studentAcctMap",sb.toString()));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"saveApprovedStudentAccounts.php",
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
                Toast.makeText(NewStudentAccountsActivity.this,message,Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
