package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ojtmonitoring.info.ResumeInfo;
import com.example.ojtmonitoring.info.StudentPersonalInformationInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSectionsActivity extends AppCompatActivity {

    Spinner sectionsSpnr;
    ListView studentsBySectionListV;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private String college;
    ArrayAdapter<String> sectionNameAdapter = null;
    private String[] sectionNames;
    List<String> studentNames = new ArrayList<>();

    ArrayAdapter<String> menuAdapter = null;
    private String sectionNameSelected;
    TextView totalTxt;

    String sectionFromStudentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sections);
        PaceSettingManager.lockActivityOrientation(this);

        if(null != getIntent().getStringExtra("selectedSectionName") && getIntent().getStringExtra("selectedSectionName").length() > 0){
            sectionFromStudentAccount = getIntent().getStringExtra("selectedSectionName");
        }

        sectionsSpnr = (Spinner)findViewById(R.id.sectionsSpnr);
        studentsBySectionListV = (ListView)findViewById(R.id.studentsBySectionListV);

        totalTxt = (TextView)findViewById(R.id.totalTxt);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        college = sharedPreferences.getString("college","");

        if(null != sectionFromStudentAccount && sectionFromStudentAccount.length() > 0) {
            sectionsSpnr.setVisibility(View.INVISIBLE);
        }

        ConnectToDBViaJson connectToDBViaJson = new ConnectToDBViaJson();
        connectToDBViaJson.execute();

        sectionsSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) sectionsSpnr.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                sectionNameSelected = sectionsSpnr.getSelectedItem().toString();
                RetrieveStudentsBySection retrieveStudentsBySection = new RetrieveStudentsBySection();
                retrieveStudentsBySection.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class ConnectToDBViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewSectionsActivity.this);
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

                sectionNameAdapter = new ArrayAdapter<String>(ViewSectionsActivity.this, android.R.layout.simple_list_item_1, sectionNames);
                sectionsSpnr.setAdapter(sectionNameAdapter);
            }


        }
    }



    class RetrieveStudentsBySection extends AsyncTask<String, String, String> {
        int acceptedCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewSectionsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public RetrieveStudentsBySection() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(null != sectionFromStudentAccount && sectionFromStudentAccount.length() > 0) {
                params.add(new BasicNameValuePair("section", sectionFromStudentAccount + ""));
            }else {
                params.add(new BasicNameValuePair("section", sectionNameSelected + ""));
            }
           /* params.add(new BasicNameValuePair("companyName",name));*/

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getStudentsBySection.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("student_lists")){
                            studentNames = new ArrayList<String>();


                            JSONArray studentListArr =json.getJSONArray("student_lists");
                            if(null != studentListArr){
                                for(int i =0 ; i<studentListArr.length() ; i++){
                                    final StringBuffer sb = new StringBuffer("");

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
                                                    sb.append("ID : "+value);
                                                }

                                                if(key.equals("student_name")){
                                                    sb.append("\n");
                                                    sb.append("Name : "+value);
                                                }

                                                if(key.equals("course")){
                                                    sb.append("\n");
                                                    sb.append("Course : "+value);
                                                }

                                                if(key.equals("gender")){
                                                    sb.append("\n");
                                                    sb.append("Gender : "+value);
                                                }



                                            }
                                        }
                                    }

                                    studentNames.add(sb.toString());


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
            menuAdapter = new ArrayAdapter<String>(ViewSectionsActivity.this,android.R.layout.simple_list_item_1,studentNames.toArray(new String[studentNames.size()])){
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
            studentsBySectionListV.setAdapter(menuAdapter);

            StringBuffer sb = new StringBuffer("Total enrolled : ");
            if(null != studentsBySectionListV){
                sb.append(studentsBySectionListV.getCount());
            }
            totalTxt.setText(sb.toString());
        }
    }


}
