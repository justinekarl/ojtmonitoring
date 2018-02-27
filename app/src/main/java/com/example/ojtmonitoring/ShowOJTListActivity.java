package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jomer.filetracker.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ojtlist);

        companyNameThisLbl = (TextView)findViewById(R.id.companyNameThisLbl);
        studentListLstView = (ListView)findViewById(R.id.studentListLstView);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        companyId = sharedPreferences.getInt("agent_id",0);
        String name =  sharedPreferences.getString("full_name","");

        if(null != name && name.trim().length() > 0){
            companyNameThisLbl.setText(name);
        }

        ShowOJTListActivity.ConnectToDataBaseViaJson connectToDataBaseViaJson = new ShowOJTListActivity.ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();


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
                            JSONArray studentListArr =json.getJSONArray("student_lists");
                            if(null != studentListArr){
                                for(int i =0 ; i<=studentListArr.length() ; i++){
                                    for(int k = 0 ; k <= studentListArr.getJSONArray(i).length()-1 ; k++) {
                                        if(!studentNames.contains(studentListArr.getJSONArray(i).get(0) + "")) {
                                            studentNames.add(studentListArr.getJSONArray(i).get(0) + "");
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
            if(null != studentNames && studentNames.size()>0){
                ListAdapter arrayAdapter = new ArrayAdapter<String>(ShowOJTListActivity.this,android.R.layout.simple_list_item_1,studentNames);
                studentListLstView.setAdapter(arrayAdapter);
            }
        }
    }

}
