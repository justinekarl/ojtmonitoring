package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentLoginActivity extends AppCompatActivity {

    private static String name;
    private int agentId;
    private static int accountType;

    private TextView welcomeLbl;
    private Button logoutBtn;
    private TextView messageNotifTxt;
    private ListView menuOptionsLstView;
    private String status;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private StringBuffer sb = new StringBuffer("");
    String[] menuItems = {"Show Companies","Add/Update My Resume","Select Section","Show My OJT Progress","Rate Company"};
    ListAdapter  menuAdapter;
    boolean hasSectionSelected = false;
    boolean hasMessageNotif=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        name=sharedPreferences.getString("full_name","");
        accountType = sharedPreferences.getInt("accounttype",0);
        status = sharedPreferences.getString("ojt_status","");

        messageNotifTxt = (TextView)findViewById(R.id.messageNotifTxt);


        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        menuOptionsLstView = (ListView)findViewById(R.id.menuOptionsLstView);




        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");


        welcomeLbl.setText("Logged In User : " +name +" - Student id: "+agentId +" \n " +sd.format(new Date().getTime()));

        logoutBtn = (Button)findViewById(R.id.logoutBtn);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to continue with your action?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if(actionTaken == "logout"){
                            SharedPreferences preferences =getSharedPreferences(PaceSettingManager.USER_PREFERENCES,MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent login = new Intent(StudentLoginActivity.this, Login.class);
                            startActivity(login);

                        //}else{
                         //   finishAffinity();
                        //}
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });


        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        if(!hasSectionSelected){

        }

        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,menuItems){
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
        menuOptionsLstView.setAdapter(menuAdapter);


        logoutBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
        );

        menuOptionsLstView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMenu = String.valueOf(parent.getItemAtPosition(position));

                        if(selectedMenu.equals("Rate Company") && status.equals("OJT In progress")){
                            toastMessage("OJT not yet finished!");
                            return;
                        }
                        if(!hasSectionSelected && (selectedMenu.equals("Show My OJT Progress") || selectedMenu.equals("Show Companies"))){
                            toastMessage("No enrolled section yet!");
                            return;
                        }

                        switch (position){
                            case 0:
                                Intent showCompanies = new Intent(StudentLoginActivity.this,ShowCompaniesActivity.class);
                                startActivity(showCompanies);
                                return;
                            case 1:
                                Intent addResume = new Intent(StudentLoginActivity.this,CreateUpdateResumeActivity.class);
                                startActivity(addResume);
                                return;
                            case 2:
                                Intent selectSection = new Intent(StudentLoginActivity.this,SectionSelectionActivity.class);
                                startActivity(selectSection);
                                return;
                            case 3:
                                Intent showAccummulated = new Intent(StudentLoginActivity.this,ShowTimeAccumulatedActivity.class);
                                startActivity(showAccummulated);
                                return;
                            case 4:
                                Intent rateCompany = new Intent(StudentLoginActivity.this,RateCompanyActivity.class);
                                startActivity(rateCompany);
                                return;
                            default:
                                Intent backToHome = new Intent(StudentLoginActivity.this,StudentLoginActivity.class);
                                startActivity(backToHome);

                        }

                    }
                }
        );

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                Intent showCompanies = new Intent(StudentLoginActivity.this,ShowCompaniesActivity.class);
                startActivity(showCompanies);
                return true;
            case 0:
                Intent addResume = new Intent(StudentLoginActivity.this,CreateUpdateResumeActivity.class);
                startActivity(addResume);
                return true;
            default:
                Intent backToHome = new Intent(StudentLoginActivity.this,StudentLoginActivity.class);
                startActivity(backToHome);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(0,0,Menu.NONE,"Add/Update My Resume");
        menu.add(1,1,Menu.NONE,"Show Companies");
        menu.add(2,2,Menu.NONE,"Show Time Accumulated");


        return super.onPrepareOptionsMenu(menu);
    }



    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StudentLoginActivity.this);
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

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveStudentNotif.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        sb = new StringBuffer("");
                        if(json.has("message_notif")){
                            hasMessageNotif = true;
                            JSONArray notifListArr =json.getJSONArray("message_notif");

                            if(null != notifListArr){
                                for(int i =0 ; i<notifListArr.length() ; i++){

                                    for (int k = 0; k < notifListArr.getJSONArray(i).length(); k++) {
                                        String[] row = null;
                                        if(null != notifListArr.getJSONArray(i) && (notifListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                            row = (notifListArr.getJSONArray(i).get(k) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("date_created")){
                                                    sb.append(value +" : ");
                                                }

                                               if(key.equals("message")){
                                                   sb.append(value);
                                                   sb.append("\n\n\n");
                                               }

                                            }
                                        }
                                    }


                                }

                            }
                        }

                        if(json.has("section_id") && null != json.get("section_id") ){

                            hasSectionSelected = true;

                            if(json.get("section_id").toString().equals("null")) {
                                sb = new StringBuffer("");
                                sb.append("You are not enrolled in any sections yet, kindly select a section first before continuing.");
                                sb.append("\n");
                                sb.append("Navigate to \"Select Section\" Page");
                                hasSectionSelected = false;
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
            if(null != sb && sb.toString().trim().length() > 0){
                messageNotifTxt.setText(sb.toString());
            }

            if(!hasMessageNotif){
                messageNotifTxt.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
