package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SectionSelectionActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private int agentId;
    Spinner sectionSpnr;
    ArrayAdapter<String> sectionNameAdapter = null;
    private String[] sectionNames;
    Button refreshListBtn;
    Button enrollSectionBtn;
    Button cancelEnrollSectionBtn;
    private String selectedSectionName;

    private String savedSectionName;
    private String college;

    Button viewSectionSelectedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_selection);
        PaceSettingManager.lockActivityOrientation(this);

        sectionSpnr = (Spinner)findViewById(R.id.sectionSpnr);

        refreshListBtn = (Button)findViewById(R.id.refreshListBtn);
        enrollSectionBtn = (Button)findViewById(R.id.enrollSectionBtn);
        cancelEnrollSectionBtn = (Button)findViewById(R.id.cancelEnrollSectionBtn);
        viewSectionSelectedBtn = (Button)findViewById(R.id.viewSectionSelectedBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        college = sharedPreferences.getString("college","");

        refreshListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectToDBViaJson connectToDataBaseViaJson = new ConnectToDBViaJson();
                connectToDataBaseViaJson.execute();



            }
        });

        /*cancelEnrollSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(SectionSelectionActivity.this,StudentLoginActivity.class);
                startActivity(backToHome);
            }
        });*/

        cancelEnrollSectionBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent backToHome = new Intent(SectionSelectionActivity.this,StudentLoginActivity.class);
                        startActivity(backToHome);
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

        /*enrollSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != sectionSpnr && null != sectionSpnr.getSelectedItem()){
                    selectedSectionName = sectionSpnr.getSelectedItem().toString();
                }
                EnrollWithSectionSelected enrollWithSectionSelected = new EnrollWithSectionSelected();
                enrollWithSectionSelected.execute();
            }
        });*/

        enrollSectionBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        if(null != sectionSpnr && null != sectionSpnr.getSelectedItem()){
                            selectedSectionName = sectionSpnr.getSelectedItem().toString();
                        }
                        EnrollWithSectionSelected enrollWithSectionSelected = new EnrollWithSectionSelected();
                        enrollWithSectionSelected.execute();
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

        sectionSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) sectionSpnr.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                selectedSectionName = sectionSpnr.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*viewSectionSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showSectionInfo = new Intent(getApplicationContext(),ViewSectionsActivity.class);
                showSectionInfo.putExtra("selectedSectionName",selectedSectionName);
                startActivity(showSectionInfo);
            }
        });*/

        viewSectionSelectedBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent showSectionInfo = new Intent(getApplicationContext(),ViewSectionsActivity.class);
                        showSectionInfo.putExtra("selectedSectionName",selectedSectionName);
                        startActivity(showSectionInfo);
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

        refreshListBtn.callOnClick();
    }


    class ConnectToDBViaJson extends AsyncTask<String, String, String> {

        private String[] sectionNames1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SectionSelectionActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDBViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",agentId+""));
            params.add(new BasicNameValuePair("college",college));



            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveSections.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

                        if(json.has("section") && null != json.getJSONObject("section")){
                            savedSectionName = json.getJSONObject("section").getString("section");
                        }

                        if(json.has("section_list")){
                            JSONArray jsonArray = json.getJSONArray("section_list");
                            if(null != jsonArray){
                                sectionNames1= new String[jsonArray.length()+1];
                                for(int ctr = 0;  ctr <= jsonArray.length() ; ctr++) {
                                    if(ctr==0){
                                        sectionNames1[ctr] = "---Select Section---";
                                        continue;
                                    }
                                    for (int i = 0; i < jsonArray.getJSONArray(ctr-1).length(); i++) {
                                        if(null != jsonArray.getJSONArray(ctr-1) && i==1) {
                                            sectionNames1[ctr] = jsonArray.getJSONArray(ctr-1).get(i).toString();
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
            sectionNames = sectionNames1;

            if(null != sectionNames && sectionNames.length > 0) {

                sectionNameAdapter = new ArrayAdapter<String>(SectionSelectionActivity.this, android.R.layout.simple_list_item_1, sectionNames)/*{
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
                }*/;

                int selectedId = 0;
                for(int i =0;i<sectionNames.length;i++){
                    if(null != sectionNames[i] && sectionNames[i].equals(savedSectionName)){
                        selectedId = i;
                    }
                }
                sectionSpnr.setAdapter(sectionNameAdapter);
                sectionSpnr.setSelection(selectedId);
            }

        }
    }


    class EnrollWithSectionSelected extends AsyncTask<String, String, String> {

        boolean isSuccessfullySaved = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SectionSelectionActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            pDialog.show();
        }

        public EnrollWithSectionSelected() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",agentId+""));
            params.add(new BasicNameValuePair("sectionName",selectedSectionName));




            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"save_selected_student_section.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        isSuccessfullySaved = true;
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
            if(isSuccessfullySaved){
                toastMessage("Successfully updated!");
            }else{
                toastMessage("Error Occured!");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,StudentLoginActivity.class);
        startActivity(home);
        finish();
    }
}
