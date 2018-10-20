package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    ListView studentListListView;
    private List<Pair<Integer,String>> studentLists = new ArrayList<>();

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private int agentId;
    private boolean weeklyStudent;

    //ListAdapter menuAdapter;
    ArrayAdapter<String> menuAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        PaceSettingManager.lockActivityOrientation(this);

        weeklyStudent = (null != getIntent()) ? getIntent().getBooleanExtra("studentWeekly",false) : false;

        studentListListView = (ListView)findViewById(R.id.studentListListView);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);



        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();


        studentListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent redirect ;
                if(weeklyStudent){
                    redirect = new Intent(getApplicationContext(), StudentWeeklyReportActivity.class);
                    redirect.putExtra("fromCompany",true);
                }else {
                    redirect = new Intent(getApplicationContext(), RateStudentActivity.class);
                }


                if(null != studentListListView.getItemAtPosition(position) && studentListListView.getItemAtPosition(position).toString().contains(",")) {
                    String studentIdTxt =studentListListView.getItemAtPosition(position).toString().split(",")[0];
                    String studentNameTxt =studentListListView.getItemAtPosition(position).toString().split(",")[1];
                    if(studentIdTxt.contains(":")) {

                        int studentId = Integer.valueOf(studentIdTxt.split(":")[1]);
                        redirect.putExtra("studentId", studentId);
                    }
                    if(weeklyStudent && null != studentNameTxt && studentNameTxt.trim().length() > 0 && studentNameTxt.contains(":")){
                        String name = studentNameTxt.split(":")[1];
                        redirect.putExtra("studentName", name.replace("Course Name",""));
                    }
                }
                startActivity(redirect);

            }
        });

    }

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StudentListActivity.this);
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


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getStudentNamesByCompanyId.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("student_lists") && null != json.getJSONArray("student_lists")){

                            studentLists.clear();

                            for (int i=0;i<json.getJSONArray("student_lists").length() ; i++){
                                final StringBuffer sbName= new StringBuffer("");
                                int id = 0;



                                for (int k = 0; k < json.getJSONArray("student_lists").getJSONArray(i).length() ; k++) {
                                            if(k==0) {
                                                id = Integer.valueOf(json.getJSONArray("student_lists").getJSONArray(i).getInt(k) + "");
                                            }
                                            if(k==1) {
                                                sbName.append("Student Name : "+json.getJSONArray("student_lists").getJSONArray(i).get(k));
                                            }

                                            if(k==2) {
                                                sbName.append("\n  ");
                                                sbName.append("Course Name : "+json.getJSONArray("student_lists").getJSONArray(i).get(k));
                                            }
                                            /*if (key.equals("student_name")) {
                                                sbName.append("Student Name : "+value);
                                            }
                                            if (key.equals("course")) {
                                                sbName.append("\n  ");
                                                sbName.append("Course Name : "+value);
                                            }*/


                                }

                                Pair<Integer,String> studentVals= new Pair<>(id,sbName.toString());

                                studentLists.add(studentVals);
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
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            List<String> values = new ArrayList<>();

            for(Pair<Integer,String> studentList : studentLists){
                values.add("Student Id :" +studentList.first+","+studentList.second);
            }

            menuAdapter = new ArrayAdapter<String>(StudentListActivity.this,android.R.layout.simple_list_item_1,values.toArray(new String[values.size()])){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view =  super.getView(position, convertView, parent);

                    TextView tv = (TextView)view.findViewById(android.R.id.text1);

                    tv.setTextColor(Color.WHITE);
                    tv.setTypeface(Typeface.DEFAULT_BOLD);

                    return view;
                }
            };
            studentListListView.setAdapter(menuAdapter);


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToPrev = new Intent(StudentListActivity.this,CompanyLoginActivity.class);
        startActivity(backToPrev);
    }
}
