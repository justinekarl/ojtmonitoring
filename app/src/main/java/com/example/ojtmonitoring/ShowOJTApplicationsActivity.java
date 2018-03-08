package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.CompanyInfo;
import com.example.ojtmonitoring.info.ResumeInfo;
import com.example.ojtmonitoring.info.StudentCompanyOJTInfo;
import com.example.ojtmonitoring.info.StudentPersonalInformationInfo;
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


public class ShowOJTApplicationsActivity extends AppCompatActivity {
    private ListView ojtApplicantsLst;
    private static int teacherId;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String college;

    public List<StudentCompanyOJTInfo> studentCompanyOJTInfos;

    HashMap<Integer,List<Integer>> selectedIdsToProcess = new HashMap<>();

    private Button processOjtReqBtn;

    OjtApplicationsListView ojtApplicationsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ojt_requests);

        ojtApplicantsLst = (ListView)findViewById(R.id.ojtApplicantsLst);
        processOjtReqBtn = (Button)findViewById(R.id.processOjtReqBtn);


        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);
        college = sharedPreferences.getString("college","");


        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        processOjtReqBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedIdsToProcess.clear();
                        if(null != ojtApplicationsListAdapter.getStudentCompanyOJTInfoList()){
                            for(StudentCompanyOJTInfo studentCompanyOJTInfo : ojtApplicationsListAdapter.getStudentCompanyOJTInfoList()){
                                if(studentCompanyOJTInfo.getSelected() == 1){

                                    List<Integer> resumeIds = new ArrayList<Integer>();
                                    resumeIds.add(studentCompanyOJTInfo.getResumeInfo().getId());

                                    if(!selectedIdsToProcess.containsKey(studentCompanyOJTInfo.getCompanyInfo().getId())) {
                                        selectedIdsToProcess.put(studentCompanyOJTInfo.getCompanyInfo().getId(),resumeIds);
                                    }else{
                                        selectedIdsToProcess.get(studentCompanyOJTInfo.getCompanyInfo().getId()).add(studentCompanyOJTInfo.getResumeInfo().getId());
                                    }

                                }
                            }
                        }


                        if(null != selectedIdsToProcess && selectedIdsToProcess.size() > 0){
                            ProcessCOjtApplications processCOjtApplications = new ProcessCOjtApplications();
                            processCOjtApplications.execute();
                        }else{
                            Toast.makeText(ShowOJTApplicationsActivity.this, "No item/s Selected", Toast.LENGTH_SHORT).show();
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
            pDialog = new ProgressDialog(ShowOJTApplicationsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",teacherId+""));
            params.add(new BasicNameValuePair("college",college));

            final StringBuffer sb = new StringBuffer("");
            if(null != selectedIdsToProcess && selectedIdsToProcess.size() > 0){

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, List<Integer>>>(){}.getType();
                String json = gson.toJson(selectedIdsToProcess, integerObjectMapType);
                sb.append(json);


            }

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveStudentCompanyOjt.php",
                    "POST", params);





            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("ojt_details")){
                            studentCompanyOJTInfos = new ArrayList<>();

                            JSONArray ojtDetailsJsonArr = json.getJSONArray("ojt_details");

                            if(null != ojtDetailsJsonArr){
                                for(int ctr = 0;  ctr < ojtDetailsJsonArr.length() ; ctr++){
                                    StudentCompanyOJTInfo studentCompanyOJTInfo = new StudentCompanyOJTInfo();

                                    if(null == studentCompanyOJTInfo.getResumeInfo()){
                                        studentCompanyOJTInfo.setResumeInfo(new ResumeInfo());
                                    }

                                    if(null == studentCompanyOJTInfo.getCompanyInfo()){
                                        studentCompanyOJTInfo.setCompanyInfo(new CompanyInfo());
                                    }

                                    if(null != studentCompanyOJTInfo.getResumeInfo() && null == studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo()){
                                        studentCompanyOJTInfo.getResumeInfo().setStudentPersonalInformationInfo(new StudentPersonalInformationInfo());
                                    }

                                    boolean approved = false;
                                    for(int i = 0 ; i <= ojtDetailsJsonArr.getJSONArray(ctr).length()-1 ; i++) {

                                        String[] row = null;
                                        if(null != ojtDetailsJsonArr.getJSONArray(ctr) && (ojtDetailsJsonArr.getJSONArray(ctr).get(i) + "").contains("~")) {
                                            row = (ojtDetailsJsonArr.getJSONArray(ctr).get(i) + "").split("~");
                                            String key = "";
                                            String value = "";
                                            key = row[0];
                                            if(row.length > 1) {
                                                value = row[1];
                                            }

                                            if(key.equals("college")){
                                                studentCompanyOJTInfo.getResumeInfo().setCollege(value);
                                            }

                                            if(key.equals("company_name")){
                                                studentCompanyOJTInfo.getCompanyInfo().setName(value);
                                            }

                                            if(key.equals("student_name")){
                                                studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().setName(value);
                                            }

                                            if(key.equals("phone")){
                                                studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().setPhoneNumber(value);
                                            }

                                            if(key.equals("email")){
                                                studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().setEmail(value);
                                            }

                                            if(key.equals("company_id")){
                                                studentCompanyOJTInfo.getCompanyInfo().setId(Integer.parseInt(value));
                                            }

                                            if(key.equals("resume_id")){
                                                studentCompanyOJTInfo.getResumeInfo().setId(Integer.parseInt(value));
                                            }

                                            if(key.equals("approved")){
                                                approved = true;
                                            }

                                            if(approved){
                                                if(key.equals("selected_company_id") && (null != value && value.trim().length() > 0 && Integer.parseInt(value) > 0)) {
                                                    studentCompanyOJTInfo.getResumeInfo().setApproved(Boolean.valueOf(value));
                                                    studentCompanyOJTInfo.setSelected(Integer.parseInt(value) > 0 ? 1 : 0);
                                                }
                                            }

                                        }
                                    }

                                    studentCompanyOJTInfos.add(studentCompanyOJTInfo);
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
            ojtApplicationsListAdapter = new OjtApplicationsListView(studentCompanyOJTInfos,ShowOJTApplicationsActivity.this);
            ojtApplicantsLst.setAdapter(ojtApplicationsListAdapter);


        }
    }


    class ProcessCOjtApplications extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowOJTApplicationsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessCOjtApplications() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));


            Gson gson = new Gson();
            Type integerObjectMapType = new TypeToken<Map<Integer, List<Integer>>>(){}.getType();
            String jsonSelectedIds = gson.toJson(selectedIdsToProcess, integerObjectMapType);

            if(null != jsonSelectedIds && jsonSelectedIds.trim().length() > 0){
                params.add(new BasicNameValuePair("jsonSelectedIds",jsonSelectedIds));
            }

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"saveApprovedOjtList.php",
                    "POST", params);



            try {
                if(null != json){
                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

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
            /*ShowOJTApplicationsActivity.ConnectToDataBaseViaJson  showCompaniesAll = new ShowOJTApplicationsActivity.ConnectToDataBaseViaJson();
            showCompaniesAll.execute();*/

        }
    }


}
