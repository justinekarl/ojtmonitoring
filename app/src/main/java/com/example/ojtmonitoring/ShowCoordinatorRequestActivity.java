package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ojtmonitoring.info.CoordinatorRequestInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowCoordinatorRequestActivity extends AppCompatActivity {
    private ListView coorListReqLstView;
    private static int agentId;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private Button processCoorListBtn;

    List<CoordinatorRequestInfo> coordinatorRequestInfos;

    CustomCoorRequestListView customCoorRequestListViewAdapter;
    private List<Integer> idsToProcess = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coordinator_request);

        coorListReqLstView = (ListView)findViewById(R.id.coorListReq);
        processCoorListBtn = (Button)findViewById(R.id.processCoorListBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);


        ShowCoordinatorRequestActivity.ConnectToDataBaseViaJson connectToDataBaseViaJson = new ShowCoordinatorRequestActivity.ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        processCoorListBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idsToProcess.clear();
                        if(null != customCoorRequestListViewAdapter.getCoordinatorRequestList()){
                            for(final CoordinatorRequestInfo coordinatorRequestInfo : customCoorRequestListViewAdapter.getCoordinatorRequestList()){
                                if(coordinatorRequestInfo.isApproved()){
                                    idsToProcess.add(coordinatorRequestInfo.getId());
                                }
                            }
                        }


                        if(null != idsToProcess && idsToProcess.size() > 0){
                            ShowCoordinatorRequestActivity.ProcessCoorRequest processCOjtApplications = new ShowCoordinatorRequestActivity.ProcessCoorRequest();
                            processCOjtApplications.execute();

                        }else{
                            Toast.makeText(ShowCoordinatorRequestActivity.this, "No item/s Selected", Toast.LENGTH_SHORT).show();
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
            pDialog = new ProgressDialog(ShowCoordinatorRequestActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("companyId",agentId+""));


            final StringBuffer sb = new StringBuffer("");
            /*if(null != selectedIdsToProcess && selectedIdsToProcess.size() > 0){

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, List<Integer>>>(){}.getType();
                String json = gson.toJson(selectedIdsToProcess, integerObjectMapType);
                sb.append(json);


            }*/

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveCoordinatorReq.php",
                    "POST", params);

            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("coordinator_details")){

                            coordinatorRequestInfos = new ArrayList<>();
                            JSONArray coorDetailsJsonArr = json.getJSONArray("coordinator_details");

                            if(null != coorDetailsJsonArr){
                                for(int ctr = 0;  ctr < coorDetailsJsonArr.length() ; ctr++){
                                    CoordinatorRequestInfo coordinatorRequestInfo = new CoordinatorRequestInfo();
                                    boolean approved = false;
                                    for(int i = 0 ; i <= coorDetailsJsonArr.getJSONArray(ctr).length()-1 ; i++) {

                                        String[] row = null;
                                        if(null != coorDetailsJsonArr.getJSONArray(ctr) && (coorDetailsJsonArr.getJSONArray(ctr).get(i) + "").contains("~")) {
                                            row = (coorDetailsJsonArr.getJSONArray(ctr).get(i) + "").split("~");
                                            String key = "";
                                            String value = "";
                                            key = row[0];
                                            if(row.length > 1) {
                                                value = row[1];
                                            }

                                            if(key.equals("id")){
                                                coordinatorRequestInfo.setId(Integer.valueOf(value));
                                            }

                                            if(key.equals("name")){
                                                coordinatorRequestInfo.setName(value);
                                            }

                                            if(key.equals("phonenumber")){
                                                coordinatorRequestInfo.setPhoneNumber(value);
                                            }

                                            if(key.equals("address")){
                                                coordinatorRequestInfo.setAddress(value);
                                            }


                                            if(approved){
                                                if(key.equals("selected_company_id") && (null != value && value.trim().length() > 0 && Integer.parseInt(value) > 0)) {

                                                }
                                            }

                                        }
                                    }

                                    coordinatorRequestInfos.add(coordinatorRequestInfo);
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
            customCoorRequestListViewAdapter = new CustomCoorRequestListView(coordinatorRequestInfos,ShowCoordinatorRequestActivity.this);
            coorListReqLstView.setAdapter(customCoorRequestListViewAdapter);
           /* ojtApplicationsListAdapter = new OjtApplicationsListView(studentCompanyOJTInfos,ShowOJTApplicationsActivity.this);
            ojtApplicantsLst.setAdapter(ojtApplicationsListAdapter);*/


        }
    }


    class ProcessCoorRequest extends AsyncTask<String, String, String> {

        String notification;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowCoordinatorRequestActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessCoorRequest() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",agentId+""));


            if(null != idsToProcess && idsToProcess.size() > 0){
                params.add(new BasicNameValuePair("jsonSelectedIds",PaceSettingManager.integerTooCommaSeparated(idsToProcess)+""));
            }


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"saveApprovedCoordinators.php",
                    "POST", params);



            try {
                if(null != json){
                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        notification = "Successfully approved selected coordinators";
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
            ConnectToDataBaseViaJson connectToDataBaseViaJson1 = new ConnectToDataBaseViaJson();
            connectToDataBaseViaJson1.execute();
            Toast.makeText(ShowCoordinatorRequestActivity.this,notification,Toast.LENGTH_SHORT);
            /*ShowOJTApplicationsActivity.ConnectToDataBaseViaJson  showCompaniesAll = new ShowOJTApplicationsActivity.ConnectToDataBaseViaJson();
            showCompaniesAll.execute();*/

        }
    }


}
