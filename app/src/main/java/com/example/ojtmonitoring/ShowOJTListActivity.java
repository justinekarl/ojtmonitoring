package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.CompanyInfo;
import com.example.ojtmonitoring.info.ResumeInfo;
import com.example.ojtmonitoring.info.StudentPersonalInformationInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowOJTListActivity extends AppCompatActivity {


    private TextView companyNameThisLbl;
    private ListView studentListLstView;


    private ProgressDialog pDialog;
    private static int companyId;
    private ArrayList<String> studentNames;
    JSONParser jsonParser = new JSONParser();
    CustomStudentOJTListView customStudentOJTListAdapter;
    public ArrayList<ResumeInfo> studentInfos ;
    private Button acceptBtn;
    private List<Integer> selectedStudentsToAcceptList = new ArrayList<Integer>();
    private boolean triggerUpdate;
    private String message;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ojtlist);

        companyNameThisLbl = (TextView)findViewById(R.id.companyNameThisLbl);
        studentListLstView = (ListView)findViewById(R.id.studentListLstView);

        acceptBtn = (Button)findViewById(R.id.acceptBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        companyId = sharedPreferences.getInt("agent_id",0);
        name =  sharedPreferences.getString("full_name","");

        if(null != name && name.trim().length() > 0){
            companyNameThisLbl.setText(name);
        }

        ShowOJTListActivity.ConnectToDataBaseViaJson connectToDataBaseViaJson = new ShowOJTListActivity.ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        acceptBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedStudentsToAcceptList.clear();

                        if(null != customStudentOJTListAdapter.getResumeInfoArrayList()){
                            for(ResumeInfo resumeInfo : customStudentOJTListAdapter.getResumeInfoArrayList()){
                                if(resumeInfo.getSelected() == 1){
                                    selectedStudentsToAcceptList.add(resumeInfo.getId());
                                }
                            }
                        }

                        if(null != selectedStudentsToAcceptList && selectedStudentsToAcceptList.size() > 0){
                            ProcessAcceptStudents processAcceptStudent = new ProcessAcceptStudents();
                            processAcceptStudent.execute();
                        }else{
                            Toast.makeText(ShowOJTListActivity.this, "No Student/s Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


    }



    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowOJTListActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
         //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",companyId+""));
            params.add(new BasicNameValuePair("companyName",name));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getStudentByCompanyId.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("student_lists")){
                            studentNames = new ArrayList<String>();
                            studentInfos= new ArrayList<ResumeInfo>();

                            JSONArray studentListArr =json.getJSONArray("student_lists");
                            if(null != studentListArr){
                                for(int i =0 ; i<=studentListArr.length() ; i++){
                                        ResumeInfo studentInfo =  new ResumeInfo();
                                        if(null == studentInfo.getStudentPersonalInformationInfo()){
                                            studentInfo.setStudentPersonalInformationInfo(new StudentPersonalInformationInfo());
                                        }
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
                                                        studentInfo.setId(Integer.parseInt(value));
                                                    }
                                                    if(key.equals("student_name")){
                                                        studentInfo.getStudentPersonalInformationInfo().setName(value);
                                                    }

                                                    if(key.equals("college")){
                                                        studentInfo.setCollege(value);
                                                    }

                                                    if(key.equals("accepted")){
                                                        studentInfo.setSelected(Integer.parseInt(value) > 0 ? 1 : 0);
                                                    }

                                                }
                                         }
                                    }

                                    studentInfos.add(studentInfo);
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
            customStudentOJTListAdapter = new CustomStudentOJTListView(studentInfos,ShowOJTListActivity.this);
            studentListLstView.setAdapter(customStudentOJTListAdapter);
        }
    }


    class ProcessAcceptStudents extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowOJTListActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessAcceptStudents() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",companyId+""));
            if(null != selectedStudentsToAcceptList && selectedStudentsToAcceptList.size() > 0){
                params.add(new BasicNameValuePair("selectedStudentsToAcceptIds",PaceSettingManager.integerTooCommaSeparated(selectedStudentsToAcceptList)+""));
            }

            params.add(new BasicNameValuePair("companyName",name));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"acceptSelectedStudents.php",
                    "POST", params);


            try {
                if(null != json){
                    studentInfos.clear();
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
            ConnectToDataBaseViaJson showStudentsAll = new ConnectToDataBaseViaJson();
            showStudentsAll.execute();

            if(triggerUpdate){
                Toast.makeText(ShowOJTListActivity.this,message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
