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

public class NewTeachersAccountActivity extends AppCompatActivity {

    HashMap<Integer, Boolean> teacherAcctMap = new HashMap<Integer, Boolean>();
    ListView teacherAcctsLstView;
    CustomNewStudentAccountListView newTeacherAccountListViewAdapter;
    private ProgressDialog pDialog;
    private List<UserAccountInfo> userAccountInfos;
    private int teacherId;
    JSONParser jsonParser = new JSONParser();
    private String college;
    private boolean triggerUpdate;
    private String message;

    Button apprvSelcBtn;
    Button viewAllApprBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teachers_account);

        PaceSettingManager.lockActivityOrientation(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);
        college = sharedPreferences.getString("college","");

        teacherAcctsLstView = (ListView)findViewById(R.id.teacherAcctsLstView);
        apprvSelcBtn = (Button)findViewById(R.id.apprvSelcBtn);
        //viewAllApprBtn = (Button)findViewById(R.id.viewAllApprBtn);

        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        teacherAcctsLstView.setOnTouchListener(new ListView.OnTouchListener() {
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

        apprvSelcBtn.setOnTouchListener(new View.OnTouchListener() {
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
                                if(null != teacherAcctMap){
                                    if(!teacherAcctMap.containsKey(userAccountInfo.getId()) && userAccountInfo.isApproved()){
                                        teacherAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                    }else{
                                        if(teacherAcctMap.size() > 0) {
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = teacherAcctMap.get(userAccountInfo.getId());

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if (prevValue != userAccountInfo.isApproved()) {
                                                teacherAcctMap.remove(userAccountInfo.getId());
                                                teacherAcctMap.put(userAccountInfo.getId(), userAccountInfo.isApproved());
                                            } else {
                                                //remove if they are the same we don't need to re-save them in the backend
                                                teacherAcctMap.remove(userAccountInfo.getId());
                                            }
                                        }
                                    }
                                }
                            }

                            if(null != teacherAcctMap && teacherAcctMap.size() > 0){
                                ProcessAcceptTeachers processAcceptStudent =new ProcessAcceptTeachers();
                                processAcceptStudent.execute();
                            }
                        }

                        if(null != teacherAcctMap && teacherAcctMap.size() == 0) {
                            PaceSettingManager.toastMessage(NewTeachersAccountActivity.this,"No Teacher/s to approve selected.");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(this,TeacherLoginActivity.class);
        startActivity(back);
    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewTeachersAccountActivity.this);
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

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrievePendingTeacherAccounts.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("pending_accounts")){
                            userAccountInfos = new ArrayList<UserAccountInfo>();
                            JSONArray teacherListArr =json.getJSONArray("pending_accounts");
                            if(null != teacherListArr){
                                for(int i =0 ; i<=teacherListArr.length() ; i++){
                                    UserAccountInfo userAccountInfo = new UserAccountInfo();
                                    for (int k = 0; k <= teacherListArr.getJSONArray(i).length() - 1; k++) {
                                        String[] row = null;
                                        if(null != teacherListArr.getJSONArray(i) && (teacherListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                            row = (teacherListArr.getJSONArray(i).get(k) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("teacher_id")){
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

                                                if(key.equals("approved")){
                                                    userAccountInfo.setApproved(Integer.parseInt(value) > 0);
                                                }


                                            }

                                        }
                                    }
                                    userAccountInfos.add(userAccountInfo);
                                    if(null != teacherAcctMap){
                                        if(!teacherAcctMap.containsKey(userAccountInfo.getId())){
                                            teacherAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                        }else{
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = teacherAcctMap.get(userAccountInfo.getId());

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if(prevValue != userAccountInfo.isApproved()){
                                                teacherAcctMap.remove(userAccountInfo.getId());
                                                teacherAcctMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                            }else{
                                                //remove if they are the same we don't need to re-save them in the backend
                                                teacherAcctMap.remove(userAccountInfo.getId());
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
            newTeacherAccountListViewAdapter = new CustomNewStudentAccountListView(userAccountInfos,NewTeachersAccountActivity.this);
            teacherAcctsLstView.setAdapter(newTeacherAccountListViewAdapter);

        }
    }


    class ProcessAcceptTeachers extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewTeachersAccountActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessAcceptTeachers() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));

            final StringBuffer sb = new StringBuffer("");
            if(null != teacherAcctMap && teacherAcctMap.size() > 0){

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, Boolean>>(){}.getType();
                String json = gson.toJson(teacherAcctMap, integerObjectMapType);
                sb.append(json);



            }

            params.add(new BasicNameValuePair("teacherAcctMap",sb.toString()));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"saveApprovedTeacherAccounts.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        triggerUpdate= true;
                        message = "Successfully Accepted Selected Teachers!";
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
                Toast.makeText(NewTeachersAccountActivity.this,message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
