package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Transfer extends AppCompatActivity {


    private ListView transferDataItems;
    private ProgressDialog pDialog;
    private Button transferButton;

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        transferDataItems = (ListView) findViewById(R.id.user_transfer_list);
        transferButton = (Button) findViewById(R.id.transferButton);

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Login.arrayTransferData){
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

        transferDataItems.setAdapter(adapter);

        transferButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                ConnectToDataBaseViaJson transfer = new ConnectToDataBaseViaJson();
                transfer.execute();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    //connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Transfer.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agent_id", Login.agentId+""));
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"delete_transfer.php",
                    "POST", params);

            try {
                if(null != json){
                    int success = json.getInt("success");
                    if (success == 1) {
                    }
                }else{
                //    loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                //loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Intent int1 = new Intent(Transfer.this, content.class);
            startActivity(int1);
        }
    }
    //end of connecting
}
