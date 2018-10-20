package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllUsers extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private ListView allUsersList;
    private ProgressDialog pDialog;
    private Button clearUser;
    private Button showAll;
    private Button search;
    private CheckBox checkAllUser;
    JSONParser jsonParser = new JSONParser();
    public static String userAction;
    ArrayList<Integer> allCheckUser = new ArrayList<>();
    ArrayList<Integer> checkedUser = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        //pop confirmation
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to clear selected users?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectToDataBaseViaJson clearAll = new ConnectToDataBaseViaJson();
                        clearAll.execute();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });
        // end of confirmation

        clearUser = (Button) findViewById(R.id.clearUser);
        showAll = (Button) findViewById(R.id.showallUser);
        search = (Button) findViewById(R.id.searchUser);
        checkAllUser = (CheckBox)findViewById(R.id.checkAllUser);

        LinearLayout checkboxLayout = (LinearLayout) findViewById(R.id.checkboxusers);

        int ctr=1;
        for (Map<String, String> user : admin.allUsers){
            CheckBox cb = new CheckBox(this);
            cb.setText(user.get("value"));
            cb.setTextSize(12);
            cb.setId(Integer.valueOf(user.get("id")));
            if("null".equalsIgnoreCase(user.get("not_clear"))){
                allCheckUser.add(Integer.valueOf(user.get("id")));
                cb.setTextColor(Color.rgb(0, 0, 0));
            }else{
                cb.setTextColor(Color.rgb(193,34,65));
                cb.setEnabled(false);
            }

            if((ctr%2) == 0){
                cb.setBackgroundColor(getResources().getColor(R.color.divider));
            }
            cb.setTypeface(Typeface.MONOSPACE);
            checkboxLayout.addView(cb);
            cb.setOnCheckedChangeListener(this);
            ctr++;
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaceSettingManager.searchAction = "searchUsers";
                Intent search = new Intent(AllUsers.this, Search.class);
                startActivity(search);
            }
        });

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAction = "showAllUser";
                admin.allUsers.clear();
                ConnectToDataBaseViaJson showAll = new ConnectToDataBaseViaJson();
                showAll.execute();

            }
        });

        clearUser.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedUser.size() > 0){
                    userAction = "clearUser";
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    toastMessage("Nothing Selected");
                }

            }
        });

        checkAllUser.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                 for (Integer id :  allCheckUser){
                     ((CheckBox) findViewById(id)).setChecked(isChecked);
                 }
             }
            }
        );

    }

    public void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent content = new Intent(AllUsers.this, admin.class);
        startActivity(content);
    }

    public void onCheckedChanged(CompoundButton cb, boolean isChecked){
        if(isChecked){
            checkedUser.add(cb.getId());
        } else {
            ArrayList <Integer>checkedUserRemove = new ArrayList<>();
            checkedUserRemove.add(cb.getId());
            checkedUser.removeAll(checkedUserRemove);
        }
    }

    public void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
        /*flag=1;
        savedState.putIntegerArrayList("checkedLogs", checkedLogs);
        savedState.putInt("savedflag", flag);*/
    }



    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            admin.allUsers.clear();

            pDialog = new ProgressDialog(AllUsers.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agent_id", "all"));


            // getting JSON Object
            // Note that create product url accepts POST method
            String url = PaceSettingManager.IP_ADDRESS+"get_all_users.php";
            if( userAction == "clearUser"){
                params.add(new BasicNameValuePair("agent_ids", PaceSettingManager.integerTooCommaSeparated(checkedUser)));
                url=PaceSettingManager.IP_ADDRESS+"clear_user.php";
            }
            JSONObject json;
            json = jsonParser.makeHttpRequest(url,
                    "POST", params);



            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {

                JSONArray users = json.getJSONArray("data_needed");
                List<Map<String, String>> usersValuePair = new ArrayList<>();
                for (int ctr = 0; ctr < users.length();ctr++ ) {
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("agent_id",users.getJSONArray(ctr).getJSONObject(0).get("id_agent").toString());
                    userMap.put("student_number",users.getJSONArray(ctr).getJSONObject(3).get("student_number").toString());
                    userMap.put("full_name",users.getJSONArray(ctr).getJSONObject(4).get("full_name").toString());
                    userMap.put("not_clear",users.getJSONArray(ctr).getJSONObject(6).get("not_clear").toString());
                    usersValuePair.add(userMap);
                }

                for(Map<String, String> user : usersValuePair){
                    StringBuffer returnString = new StringBuffer();
                    returnString.append("ID No: "+user.get("student_number")+" \n");
                    returnString.append("Name: "+user.get("full_name")+" \n");
                    Map<String, String> value = new HashMap<>();
                    value.put("id",user.get("agent_id"));
                    value.put("value",returnString.toString());
                    value.put("not_clear",user.get("not_clear"));
                    admin.allUsers.add(value);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            Intent int1= new Intent(AllUsers.this,AllUsers.class);
            startActivity(int1);
            pDialog.hide();
        }
    }

}
