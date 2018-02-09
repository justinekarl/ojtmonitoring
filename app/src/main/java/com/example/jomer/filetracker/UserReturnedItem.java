package com.example.jomer.filetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserReturnedItem extends AppCompatActivity {

    private ListView userReturnedItems;
    public JSONParser jsonParser = new JSONParser();
   // private Button backToContent;
    private Button showAll;
    private Button search;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_returned_item);
        //backToContent = (Button) findViewById(R.id.backToContent);
        showAll = (Button) findViewById(R.id.showAllReturned);
        search = (Button) findViewById(R.id.searchReturned);

        userReturnedItems = (ListView) findViewById(R.id.user_list_returned);
        
        //eliminate double
        /*Set<String> data1 = new HashSet<>();
        for (int i = 0; i < content.dataNeeded.size(); i++) {
            data1.add(content.dataNeeded.get(i));
        }

        ArrayList<String> finalData = new ArrayList<>();
        finalData.clear();
        finalData.addAll(data1);*/

        //eliminate double
        

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, content.dataNeeded){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position%2 == 0)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(getResources().getColor(R.color.divider));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }

                return view;
            }
        };
        userReturnedItems.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaceSettingManager.searchAction = "searchReturned";
                Intent search = new Intent(UserReturnedItem.this, Search.class);
                startActivity(search);
            }
        });

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectToDataBaseViaJson showAll = new ConnectToDataBaseViaJson();
                showAll.execute();
            }
        });

       /* backToContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent content = new Intent(UserReturnedItem.this, content.class);
                if(login.adminRights){
                    content = new Intent(UserReturnedItem.this, admin.class);
                }
                startActivity(content);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        Intent content = new Intent(UserReturnedItem.this, content.class);
        if(login.adminRights){
            content = new Intent(UserReturnedItem.this, admin.class);
        }
        startActivity(content);
    }


    //connect database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content.dataNeeded.clear();
            pDialog = new ProgressDialog(UserReturnedItem.this);
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
            if(login.adminRights){
                params.add(new BasicNameValuePair("agent_id", "all"));
            }else{
                params.add(new BasicNameValuePair("agent_id", login.agentId+""));
            }



            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json;
            json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_user_returned_item.php",
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    JSONArray items = json.getJSONArray("data_needed");
                    for(int ctr = 0;  ctr < items.length() ; ctr++){
                        content.dataNeeded.add(items.get(ctr)+"");
                    }
                } else {
                    content.dataNeeded.add(json.getString("data_needed"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Intent intent = new Intent(UserReturnedItem.this, UserReturnedItem.class);
            startActivity(intent);
            pDialog.hide();
        }
    }

    //connect database


}
