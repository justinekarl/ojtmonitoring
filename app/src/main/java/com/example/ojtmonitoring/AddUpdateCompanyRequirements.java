package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ojtmonitoring.info.CourseInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateCompanyRequirements extends AppCompatActivity {

    private Spinner collegeSpnr;
    private RadioButton allowanceYesRadBtn;
    private RadioButton allowanceNoRadBtn;
    private EditText allowanceTxt;
    private EditText ojtNumberTxt;
    private Button saveBtn;
    private Button cancelBtn;


    private static String college;
    private static boolean willProvideAllowance;
    private static Integer allowance;
    private static Integer ojtNumber;

    private int companyId;

    private static boolean successfullyUpdated = false;

    private ListView coursesLstVw;
    private List<Integer> selectedCoursesIds = new ArrayList<>();




    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    ArrayAdapter<String> collegeListAdapter = null;

    private static int companyProfileId;
    CustomCourseListView courseListViewAdapter;
    private ArrayList<CourseInfo> courseInfos;
    TextView textView19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_company_requirements);
        PaceSettingManager.lockActivityOrientation(this);

        collegeSpnr = (Spinner)findViewById(R.id.collegeSpnr);
        allowanceYesRadBtn = (RadioButton)findViewById(R.id.allowanceYesRadBtn);
        allowanceNoRadBtn = (RadioButton)findViewById(R.id.allowanceNoRadBtn);
        allowanceTxt = (EditText)findViewById(R.id.allowanceTxt);
        ojtNumberTxt = (EditText)findViewById(R.id.ojtNumberTxt);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        textView19 = (TextView)findViewById(R.id.textView19);

        coursesLstVw = (ListView)findViewById(R.id.coursesLstVw);

        collegeListAdapter = new ArrayAdapter<String> (AddUpdateCompanyRequirements.this,
                                                                            android.R.layout.simple_list_item_1,
                                                                            getResources().getStringArray(R.array.collegelist));

        collegeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpnr.setAdapter(collegeListAdapter);

        college = null != collegeSpnr.getSelectedItem() ? collegeSpnr.getSelectedItem().toString() : "";
        willProvideAllowance = null != allowanceYesRadBtn ? (allowanceYesRadBtn.isSelected() ? true : false) : false;
        allowance = null != allowanceTxt.getText() && allowanceTxt.getText().toString().trim().length() > 0 ? Integer.parseInt(allowanceTxt.getText().toString()): null;
        ojtNumber = null != ojtNumberTxt.getText() && ojtNumberTxt.getText().toString().trim().length() > 0 ?  Integer.parseInt(ojtNumberTxt.getText().toString()):null;

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        companyId = sharedPreferences.getInt("agent_id",0);


        //hiding college dropdown in frontend
        collegeSpnr.setVisibility(View.INVISIBLE);
        textView19.setVisibility(View.INVISIBLE);

        //allowing vertical scroll even in scroll view
        coursesLstVw.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                v.onTouchEvent(event);
                return true;
            }
        });

        AddUpdateCompanyRequirements.ConnectToDataBaseViaJson retriveOjtReq = new AddUpdateCompanyRequirements.ConnectToDataBaseViaJson();
        retriveOjtReq.execute();


        allowanceYesRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(allowanceYesRadBtn.isChecked()){
                            willProvideAllowance = true;
                            allowanceNoRadBtn.setChecked(false);
                        }
                    }
                }
        );


        allowanceNoRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(allowanceNoRadBtn.isChecked()){
                            willProvideAllowance = false;
                            allowanceYesRadBtn.setChecked(false);
                        }
                    }
                }
        );

        cancelBtn.setOnTouchListener(
                new View.OnTouchListener() {

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
                                Intent backToCompanyPage = new Intent(AddUpdateCompanyRequirements.this,CompanyNavigationActivity.class);
                                backToCompanyPage.putExtra("currentModuleSelected","Home");
                                startActivity(backToCompanyPage);
                            case MotionEvent.ACTION_CANCEL: {
                                Button view = (Button) v;
                                view.getBackground().clearColorFilter();
                                view.invalidate();
                                break;
                            }
                        }
                        return true;
                    }
                }
        );


        saveBtn.setOnTouchListener(
                new View.OnTouchListener() {

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
                                college = null != collegeSpnr.getSelectedItem() ? collegeSpnr.getSelectedItem().toString() : "";
                                willProvideAllowance = allowanceYesRadBtn.isChecked();
                                allowance = null != allowanceTxt.getText() && allowanceTxt.getText().toString().trim().length() > 0 ? Integer.parseInt(allowanceTxt.getText().toString()): 0;
                                ojtNumber = null != ojtNumberTxt.getText() && ojtNumberTxt.getText().toString().trim().length() > 0 ? Integer.parseInt(ojtNumberTxt.getText().toString()): 0;

                                selectedCoursesIds.clear();

                                if(null != courseInfos && courseInfos.size() > 0){
                                    for(CourseInfo courseInfo:courseInfos){
                                        if(courseInfo.isSelected()){
                                            selectedCoursesIds.add(courseInfo.getId());
                                        }
                                    }
                                }

                                AddUpdateCompanyRequirements.UpdateOjtRequirements update = new AddUpdateCompanyRequirements.UpdateOjtRequirements();
                                update.execute();

                            case MotionEvent.ACTION_CANCEL: {
                                Button view = (Button) v;
                                view.getBackground().clearColorFilter();
                                view.invalidate();
                                break;
                            }
                        }
                        return true;
                    }
                }
        );

    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddUpdateCompanyRequirements.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String agentId = companyId+"";


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",agentId));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveOjtRequirements.php",
                    "POST", params);


            try {
                if(null != json){
                    int success = json.getInt("success");
                    if(success == 1) {

                        companyProfileId = json.getInt("id");
                        college = json.getString("college");
                        String willProvideAllwnce =  json.getString("does_provide_allowance");
                        willProvideAllowance = willProvideAllwnce.equals("1") ? true : false;
                        allowance = null != json.getString("allowance") && !"null".equals(json.getString("allowance")) ? Integer.valueOf(json.getString("allowance")): null;
                        ojtNumber = null != json.getString("ojt_number") && !"null".equals(json.getString("ojt_number"))? Integer.valueOf(json.getString("ojt_number")):null;


                        JSONArray items = json.getJSONArray("courses");

                        courseInfos = new ArrayList<CourseInfo>();


                        for(int ctr = 0;  ctr < items.length() ; ctr++){

                            CourseInfo courseInfo = new CourseInfo();
                            for(int i = 1 ; i <= items.getJSONArray(ctr).length()-1 ; i++) {
                                courseInfo.setId(Integer.parseInt(items.getJSONArray(ctr).get(0)+""));
                                courseInfo.setName(items.getJSONArray(ctr).get(1)+"");
                            }

                            courseInfos.add(courseInfo);
                        }

                        List<Integer> selectedCourseIds = new ArrayList<>();
                        if(json.has("courses_selected")) {
                            JSONArray items1 = json.getJSONArray("courses_selected");

                            for (int ctr = 0; ctr < items1.length(); ctr++) {
                                for (int i = 0; i < items1.getJSONArray(ctr).length(); i++) {
                                    selectedCourseIds.add(Integer.parseInt(items1.getJSONArray(ctr).get(0) + ""));
                                }
                            }
                        }

                        for(CourseInfo courseInfo : courseInfos){
                            for(Integer courseId : selectedCourseIds){
                                if(courseInfo.getId() == courseId){
                                    courseInfo.setSelected(true);
                                    break;
                                }
                            }
                        }



                    }else {
                        if(null != json.getString("message")){
                            //   loginMessage=json.getString("message");
                        }
                    }

                }else{
                    //loginMessage="Invalid User";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(companyId > 0) {
                if(willProvideAllowance) {
                    allowanceYesRadBtn.setChecked(true);
                    allowanceNoRadBtn.setChecked(false);
                }else {
                    allowanceNoRadBtn.setChecked(true);
                    allowanceYesRadBtn.setChecked(false);
                }
                allowanceTxt.setText(null != allowance ? allowance+"" : "");
                ojtNumberTxt.setText(null != ojtNumber ? ojtNumber+"" :"");
                if(null != collegeListAdapter) {
                    collegeSpnr.setSelection(collegeListAdapter.getPosition(college));
                }
            }

            courseListViewAdapter = new CustomCourseListView(courseInfos,AddUpdateCompanyRequirements.this);
            coursesLstVw.setAdapter(courseListViewAdapter);
        }
    }
    //end of connecting




    //connecting to the database

    class UpdateOjtRequirements extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddUpdateCompanyRequirements.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        protected String doInBackground(String... args) {
            String agentId = companyId+"";

            String newCollegeToUse = college;
            boolean willProvideAllowanceToUse = willProvideAllowance;
            int allowanceToUse = allowance;
            int ojtNumberToUse = ojtNumber;


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("college", newCollegeToUse));
            params.add(new BasicNameValuePair("willProvideAllowance", willProvideAllowanceToUse+""));
            params.add(new BasicNameValuePair("allowance", allowanceToUse+""));
            params.add(new BasicNameValuePair("ojtNumber", ojtNumberToUse+""));
            params.add(new BasicNameValuePair("agentid",agentId));
            params.add(new BasicNameValuePair("selectedCoursesIds",PaceSettingManager.integerTooCommaSeparated(selectedCoursesIds)));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"updateOjtRequirements.php",
                                                        "POST", params);


            try {
                successfullyUpdated  =false;
                if(null != json){
                    int success = json.getInt("success");
                    if(success == 1) {
                        successfullyUpdated  =true;

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
            if(companyId > 0 && successfullyUpdated ) {
                toastMessage("Successfully Updated");
            }else{
                toastMessage("Error Occured!");
            }
        }
    }
    //end of connecting


    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,CompanyNavigationActivity.class);
        startActivity(home);
        finish();
    }
}
