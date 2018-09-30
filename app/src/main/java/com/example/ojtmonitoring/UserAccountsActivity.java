package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class UserAccountsActivity extends AppCompatActivity {

    TextView labelAccountsTxt;
    Spinner collegeSpinner;
    ListView collegesLstView;
    ArrayAdapter<String> collegeListAdapter = null;
    String selectedCollege = "";
    String selectedEntity = "";
    Button processBtn;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    List<UserAccountInfo> userAccountInfos = new ArrayList<UserAccountInfo>();
    CustomNewStudentAccountListView customNewAccountsListView;

    HashMap<Integer, Boolean> userAccountsMap = new HashMap<Integer, Boolean>();

    private boolean triggerUpdate;
    private String message;

    int accounttype = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accounts);

        labelAccountsTxt = (TextView)findViewById(R.id.labelAccountsTxt);
        collegeSpinner = (Spinner)findViewById(R.id.collegeSpinner);
        collegesLstView = (ListView)findViewById(R.id.collegesLstView);
        processBtn = (Button)findViewById(R.id.processBtn);

        selectedEntity = getIntent().getStringExtra("accountType");

        if(null != selectedEntity){
            labelAccountsTxt.setText(selectedEntity + " "+labelAccountsTxt.getText().toString());
        }


        collegeListAdapter = new ArrayAdapter<String>(UserAccountsActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.collegelist));

        collegeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpinner.setAdapter(collegeListAdapter);

        collegeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int index = parent.getSelectedItemPosition();
                        ((TextView) collegeSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                        selectedCollege = collegeSpinner.getSelectedItem().toString();
                        if(!selectedEntity.equals("--Select Below--")) {
                            populateAccountsByCollege(selectedCollege);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        processBtn.setOnTouchListener(new View.OnTouchListener() {
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
                                if(null != userAccountsMap){
                                    if(!userAccountsMap.containsKey(userAccountInfo.getId()) && userAccountInfo.isApproved()){
                                        userAccountsMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                    }else{
                                        if(userAccountsMap.size() > 0) {
                                            //let's get the previous value of the account (approved or not)
                                            boolean prevValue = null != userAccountsMap.get(userAccountInfo.getId()) ? userAccountsMap.get(userAccountInfo.getId()) : false;

                                            //if there is a change in the status, remove if from the map and add the new value
                                            if (prevValue != userAccountInfo.isApproved()) {
                                                userAccountsMap.remove(userAccountInfo.getId());
                                                userAccountsMap.put(userAccountInfo.getId(), userAccountInfo.isApproved());
                                            } else {
                                                //remove if they are the same we don't need to re-save them in the backend
                                                userAccountsMap.remove(userAccountInfo.getId());
                                            }
                                        }
                                    }
                                }
                            }

                            if(null != userAccountsMap && userAccountsMap.size() > 0){
                                ProcessAccounts processAccounts = new ProcessAccounts();
                                processAccounts.execute();
                            }
                        }

                        if(null != userAccountsMap && userAccountsMap.size() == 0) {
                            PaceSettingManager.toastMessage(UserAccountsActivity.this,"No Accounts to process selected.");
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
        Intent home = new Intent(this,AdministratorActivity.class);
        startActivity(home);
        finish();
    }

    private synchronized void populateAccountsByCollege(final String selectedCollege){
        try{

            new AsyncTask<String,String,String>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(UserAccountsActivity.this);
                    pDialog.setMessage("Processing..");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                }

                @Override
                protected String doInBackground(String... args) {

                    Log.e("Error doInBackground", "EEERRORRR");

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("college",selectedCollege));
                    //int accounttype = 0;
                    if(selectedEntity.equals("Student")){
                        accounttype = 1;
                    }

                    if(selectedEntity.equals("Teacher")){
                        accounttype = 2;
                    }

                    if(selectedEntity.equals("Company")){
                        accounttype = 3;
                    }

                    if(selectedEntity.equals("Supervisor")){
                        accounttype = 4;
                    }

                    params.add(new BasicNameValuePair("accounttype",accounttype+""));

                    if(accounttype > 0) {
                        JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "retrieveUserAccounts.php",
                                "POST", params);


                        try {
                            if (null != json) {
                                userAccountInfos.clear();
                                // check log cat fro response
                                Log.d("Create Response", json.toString());

                                int success = json.getInt("success");
                                if (success == 1) {

                                    if (json.has("user_list")) {

                                        JSONArray jsonArray = json.getJSONArray("user_list");
                                        if (null != jsonArray) {
                                            for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                                                final UserAccountInfo userAccountInfo = new UserAccountInfo();
                                                for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {
                                                    if (null != jsonArray.getJSONArray(ctr) && null != jsonArray.getJSONArray(ctr).get(i)) {

                                                        if (i == 0) {
                                                            userAccountInfo.setId(Integer.valueOf(jsonArray.getJSONArray(ctr).get(i).toString()));
                                                        }

                                                        if (i == 1) {
                                                            userAccountInfo.setName(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 2) {
                                                            userAccountInfo.setAddress(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 3) {
                                                            userAccountInfo.setPhoneNumber(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 4) {
                                                            userAccountInfo.setGender(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 5) {
                                                            userAccountInfo.setEmail(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 6) {
                                                            userAccountInfo.setCourse(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 7){
                                                            userAccountInfo.setDepartment(jsonArray.getJSONArray(ctr).get(i).toString());
                                                        }

                                                        if (i == 8){
                                                            userAccountInfo.setApproved(jsonArray.getJSONArray(ctr).get(i).toString().equals("1") ? Boolean.TRUE : Boolean.FALSE);
                                                        }
                                                    }


                                                    System.out.println(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }

                                                userAccountInfo.setAccountType(accounttype);
                                                userAccountInfo.setAdminViewing(true);
                                                userAccountInfos.add(userAccountInfo);

                                                if(null != userAccountsMap){
                                                    if(!userAccountsMap.containsKey(userAccountInfo.getId())){
                                                        userAccountsMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                                    }else{
                                                        //let's get the previous value of the account (approved or not)
                                                        boolean prevValue = userAccountsMap.get(userAccountInfo.getId());

                                                        //if there is a change in the status, remove if from the map and add the new value
                                                        if(prevValue != userAccountInfo.isApproved()){
                                                            userAccountsMap.remove(userAccountInfo.getId());
                                                            userAccountsMap.put(userAccountInfo.getId(),userAccountInfo.isApproved());
                                                        }else{
                                                            //remove if they are the same we don't need to re-save them in the backend
                                                            userAccountsMap.remove(userAccountInfo.getId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                //loginMessage="Invalid User";
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //loginMessage="Invalid User";
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pDialog.dismiss();
                    customNewAccountsListView = new CustomNewStudentAccountListView(userAccountInfos,UserAccountsActivity.this);

                    collegesLstView.setAdapter(customNewAccountsListView);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    class PopulateAccountsByCollege extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserAccountsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            pDialog.show();
        }

        public PopulateAccountsByCollege() {
        }

        protected String doInBackground(String... args) {

            Log.e("Error doInBackground", "EEERRORRR");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("college",selectedCollege));

            if(selectedEntity.equals("Student")){
                accounttype = 1;
            }

            if(selectedEntity.equals("Teacher") || selectedEntity.equals("College")){
                accounttype = 2;
            }

           /* if(selectedEntity.equals("College")){
                accounttype = 3;
            }*/

            if(selectedEntity.equals("Supervisor")){
                accounttype = 4;
            }

            params.add(new BasicNameValuePair("accounttype",accounttype+""));

            if(accounttype > 0) {
                JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "retrieveUserAccounts.php",
                        "POST", params);


                try {
                    if (null != json) {
                        userAccountInfos.clear();
                        // check log cat fro response
                        Log.d("Create Response", json.toString());

                        int success = json.getInt("success");
                        if (success == 1) {

                            if (json.has("user_list")) {

                                JSONArray jsonArray = json.getJSONArray("user_list");
                                if (null != jsonArray) {
                                    for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                                        final UserAccountInfo userAccountInfo = new UserAccountInfo();
                                        for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {
                                            if (null != jsonArray.getJSONArray(ctr) && null != jsonArray.getJSONArray(ctr).get(i)) {

                                                if (i == 0) {
                                                    userAccountInfo.setId(Integer.valueOf(jsonArray.getJSONArray(ctr).get(i).toString()));
                                                }

                                                if (i == 1) {
                                                    userAccountInfo.setName(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }

                                                if (i == 2) {
                                                    userAccountInfo.setAddress(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }

                                                if (i == 3) {
                                                    userAccountInfo.setPhoneNumber(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }

                                                if (i == 4) {
                                                    userAccountInfo.setGender(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }

                                                if (i == 5) {
                                                    userAccountInfo.setEmail(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }

                                                if (i == 6) {
                                                    userAccountInfo.setCourse(jsonArray.getJSONArray(ctr).get(i).toString());
                                                }
                                            }


                                            System.out.println(jsonArray.getJSONArray(ctr).get(i).toString());
                                        }
                                        userAccountInfos.add(userAccountInfo);
                                    }
                                }
                            }
                        }

                    } else {
                        //loginMessage="Invalid User";
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //loginMessage="Invalid User";
                }
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            pDialog.dismiss();
            customNewAccountsListView = new CustomNewStudentAccountListView(userAccountInfos,UserAccountsActivity.this);

            collegesLstView.setAdapter(customNewAccountsListView);


        }
    }

    class ProcessAccounts extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserAccountsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessAccounts() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            final StringBuffer sb = new StringBuffer("");
            if(null != userAccountsMap && userAccountsMap.size() > 0){

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<Integer, Boolean>>(){}.getType();
                String json = gson.toJson(userAccountsMap, integerObjectMapType);
                sb.append(json);



            }

            params.add(new BasicNameValuePair("userAccountsMap",sb.toString()));
            params.add(new BasicNameValuePair("accounttype",accounttype+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"processUserAccounts.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        triggerUpdate= true;
                        message = "Successfully Processed User Accounts!";
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

            if(triggerUpdate){
                Toast.makeText(UserAccountsActivity.this,message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
