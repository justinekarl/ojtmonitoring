package com.example.ojtmonitoring;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ojtmonitoring.info.ResumeInfo;
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
    private Map<Integer , Boolean> selectedStudentsToAcceptMap = new HashMap<>();
    private boolean triggerUpdate;
    private String message;
    String name;
    int ojtNeeded;

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



        ShowOJTListActivity.ConnectToDataBaseViaJson connectToDataBaseViaJson = new ShowOJTListActivity.ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        acceptBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedStudentsToAcceptList.clear();
                        int countSelected = 0;

                        if(null != customStudentOJTListAdapter.getResumeInfoArrayList()){
                            for(ResumeInfo resumeInfo : customStudentOJTListAdapter.getResumeInfoArrayList()){

                                if(resumeInfo.getSelected() == 1){
                                    countSelected++;
                                }

                                if(countSelected > ojtNeeded){
                                    ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
                                    connectToDataBaseViaJson.execute();
                                    Toast.makeText(ShowOJTListActivity.this, "OJT number exceeded!", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                if(null != selectedStudentsToAcceptMap){
                                    if(!selectedStudentsToAcceptMap.containsKey(resumeInfo.getId()) && resumeInfo.getSelected() == 1){
                                        selectedStudentsToAcceptMap.put(resumeInfo.getId(),(resumeInfo.getSelected() == 1));
                                    }else{
                                        if(selectedStudentsToAcceptMap.size() > 0) {
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = selectedStudentsToAcceptMap.get(resumeInfo.getId());

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if (prevValue != (resumeInfo.getSelected() == 1)) {
                                                selectedStudentsToAcceptMap.remove(resumeInfo.getId());
                                                selectedStudentsToAcceptMap.put(resumeInfo.getId(), resumeInfo.getSelected() == 1);
                                            } else {
                                                //remove if they are the same we don't need to re-save them in the backend
                                                selectedStudentsToAcceptMap.remove(resumeInfo.getId());
                                            }
                                        }
                                    }
                                }



                               /* if(resumeInfo.getSelected() == 1){
                                    selectedStudentsToAcceptList.add(resumeInfo.getId());
                                }*/
                            }
                        }



                        if(null != selectedStudentsToAcceptMap && selectedStudentsToAcceptMap.size() > 0){
                            ProcessAcceptStudents processAcceptStudent = new ProcessAcceptStudents();
                            processAcceptStudent.execute();
                        }else{
                            ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
                            connectToDataBaseViaJson.execute();
                            Toast.makeText(ShowOJTListActivity.this, "No Student/s Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        acceptBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        acceptBtn.callOnClick();
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
        int acceptedCount = 0;

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
                                for(int i =0 ; i<studentListArr.length() ; i++){
                                        ResumeInfo studentInfo =  new ResumeInfo();
                                        if(null == studentInfo.getStudentPersonalInformationInfo()){
                                            studentInfo.setStudentPersonalInformationInfo(new StudentPersonalInformationInfo());
                                        }
                                        for (int k = 0; k < studentListArr.getJSONArray(i).length(); k++) {
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

                                                    if(key.equals("course")){
                                                        studentInfo.setCourse(value);
                                                    }

                                                }
                                         }
                                    }

                                    studentInfos.add(studentInfo);

                                    if(null != selectedStudentsToAcceptMap){
                                        if(!selectedStudentsToAcceptMap.containsKey(studentInfo.getId())){
                                            selectedStudentsToAcceptMap.put(studentInfo.getId(),studentInfo.getSelected() == 1);
                                        }else{
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = selectedStudentsToAcceptMap.get(studentInfo.getId());

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if(prevValue != (studentInfo.getSelected() == 1)){
                                                selectedStudentsToAcceptMap.remove(studentInfo.getId());
                                                selectedStudentsToAcceptMap.put(studentInfo.getId(),studentInfo.getSelected() == 1);
                                            }else{
                                                //remove if they are the same we don't need to re-save them in the backend
                                                selectedStudentsToAcceptMap.remove(studentInfo.getId());
                                            }
                                        }
                                    }
                                }

                            }
                        }

                        if(json.has("accepted_count")){
                            acceptedCount = Integer.parseInt(json.getInt("accepted_count")+"");
                        }

                        if(json.has("ojt_number")){
                            ojtNeeded = json.getInt("ojt_number");
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

            if(null != name && name.trim().length() > 0){
                StringBuffer sb = new StringBuffer();
                sb.append(name);
                sb.append("\n");
                sb.append("Number of Accepted Students - "+acceptedCount);
                companyNameThisLbl.setText(sb.toString());
            }
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

            final StringBuffer sb = new StringBuffer("");

            if(null != selectedStudentsToAcceptMap && selectedStudentsToAcceptMap.size() > 0){
                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, Boolean>>(){}.getType();
                String json = gson.toJson(selectedStudentsToAcceptMap, integerObjectMapType);
                sb.append(json);

                //params.add(new BasicNameValuePair("selectedStudentsToAcceptIds",PaceSettingManager.integerTooCommaSeparated(selectedStudentsToAcceptList)+""));
            }

            params.add(new BasicNameValuePair("selectedStudentsToAcceptIds",sb.toString()));

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
