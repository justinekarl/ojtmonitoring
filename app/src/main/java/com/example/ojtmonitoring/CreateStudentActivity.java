package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.EditText;
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

public class CreateStudentActivity extends AppCompatActivity {

    private Button cancelBtn;

    private Button saveBtn;

    private EditText fullnameTxt;
    private EditText studentNoTxt;
    private EditText phoneNumTxt;
    private EditText addressTxt;
    private EditText collegeTxt;
    private EditText usernameTxt;
    private EditText passwordTxt;
    private Spinner collegeSpnr;
    private EditText emailTxt;
    private Spinner genderSpnr;
    private EditText ojtHoursTxt;
    private Spinner coursesSpnr;

    private EditText confirmPasswordTxt;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    public static String fullname;
    public static String studentNo;
    public static String phoneNum;
    public static String address;
    public static String college;
    public static String username;
    public static String password;
    public static String confirmPassword;
    public static String email;
    public static String gender;
    public static String ojtHours;
    public static String course;

    public static boolean registrationSuccessful;
    public static String registrationMessage;

    ArrayAdapter<String> collegeListAdapter = null;
    ArrayAdapter<String> genderListAdapter = null;
    ArrayAdapter<String> coursesAdapter = null;

    private String[] courses;
    private Button refreshCoursesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student_account);

        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        fullnameTxt = (EditText)findViewById(R.id.fullnameTxt);
        studentNoTxt = (EditText)findViewById(R.id.studentNoTxt);
        phoneNumTxt = (EditText)findViewById(R.id.phoneNumbTxt);
        addressTxt = (EditText)findViewById(R.id.custAddressTxt);
        //collegeTxt = (EditText)findViewById(R.id.collegeTxt);
        collegeSpnr = (Spinner)findViewById(R.id.collegeSpnr);
        usernameTxt = (EditText)findViewById(R.id.userNameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        confirmPasswordTxt = (EditText)findViewById(R.id.confirmPasswordTxt) ;
        emailTxt = (EditText)findViewById(R.id.elemAddTxt);
        genderSpnr = (Spinner)findViewById(R.id.genderSpnr);
        ojtHoursTxt = (EditText)findViewById(R.id.ojtHoursTxt);

        coursesSpnr = (Spinner)findViewById(R.id.coursesSpnr);
        refreshCoursesBtn = (Button)findViewById(R.id.refreshCoursesBtn);

        collegeListAdapter = new ArrayAdapter<String> (CreateStudentActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    getResources().getStringArray(R.array.collegelist)){
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
        };

        collegeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpnr.setAdapter(collegeListAdapter);

        genderListAdapter = new ArrayAdapter<String>(CreateStudentActivity.this,android.R.layout.simple_list_item_1,
                                                    getResources().getStringArray(R.array.genderlist)){
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
        };

        genderListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpnr.setAdapter(genderListAdapter);

        refreshCoursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectToDBViaJson connectToDBViaJson = new ConnectToDBViaJson();
                connectToDBViaJson.execute();

            }
        });


        collegeSpnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int index = parent.getSelectedItemPosition();
                        ((TextView) collegeSpnr.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

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
                                Intent backToSelectUserTypeIntent = new Intent(CreateStudentActivity.this,AccountCreationSelectionActivity.class) ;
                                startActivity(backToSelectUserTypeIntent);
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
                                fullname = null != fullnameTxt.getText() ? fullnameTxt.getText().toString() : "";
                                studentNo = null != studentNoTxt.getText() ? studentNoTxt.getText().toString() : "";
                                phoneNum =  null != phoneNumTxt.getText() ? phoneNumTxt.getText().toString() : "";
                                address = null != addressTxt.getText() ? addressTxt.getText().toString() : "";
                                email = null != emailTxt.getText() ? emailTxt.getText().toString() : "";

                                college = null != collegeSpnr.getSelectedItem() ? collegeSpnr.getSelectedItem().toString() : "";
                                username = null != usernameTxt.getText() ? usernameTxt.getText().toString() : "";
                                password = null != passwordTxt.getText() ? passwordTxt.getText().toString() : "";
                                confirmPassword = null != confirmPasswordTxt.getText() ? confirmPasswordTxt.getText().toString() : "";
                                gender = null != genderSpnr.getSelectedItem() ? genderSpnr.getSelectedItem().toString() : "";
                                ojtHours = null != ojtHoursTxt.getText() ? ojtHoursTxt.getText().toString() : "";
                                course = null != coursesSpnr.getSelectedItem() ? coursesSpnr.getSelectedItem().toString() : "";

                                if(fullname.trim().length() == 0
                                        || studentNo.trim().length() == 0
                                        || phoneNum.trim().length() == 0
                                        || address.trim().length() == 0
                                        || college.trim().equals("--Select Below--")
                                        ||  username.trim().length() == 0
                                        || email.trim().length() == 0
                                        || gender.trim().equals("--Select Below--")
                                        || ojtHours.trim().length() == 0
                                        || course.trim().equals("--Select Below--")){
                                    toastMessage("All fields are required!");
                                }else{
                                    if(password.length() >= 5){
                                        if(password.equals(confirmPassword)){
                                            CreateStudentActivity.ProcessAddStudent register = new CreateStudentActivity.ProcessAddStudent();
                                            register.execute();
                                        }else{
                                            toastMessage("Password and Confirm Password not the same!");
                                        }
                                    }else{
                                        toastMessage("Password should be 5 characters and above!");
                                    }
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
                }
        );

        refreshCoursesBtn.callOnClick();
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    class ProcessAddStudent extends AsyncTask<String, String, String>{
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateStudentActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            String sign_up_user_name = username;
            String sign_up_password = password;
            String sign_up_student_number = studentNo;
            String sign_up_full_name = fullname;
            String sign_up_college = college;
            String sign_up_address = address;
            String sign_up_phone = phoneNum;
            String sign_up_gender = gender;
            String sign_up_ojtHours = ojtHours;
            String sign_up_course = course;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", sign_up_user_name));
            params.add(new BasicNameValuePair("password", sign_up_password));
            params.add(new BasicNameValuePair("student_number", sign_up_student_number));
            params.add(new BasicNameValuePair("full_name", sign_up_full_name));
            params.add(new BasicNameValuePair("admin_password", "123456"));
            params.add(new BasicNameValuePair("college",sign_up_college));
            params.add(new BasicNameValuePair("address",sign_up_address));
            params.add(new BasicNameValuePair("phonenumber",sign_up_phone));
            params.add(new BasicNameValuePair("accounttype","1"));
            params.add(new BasicNameValuePair("email",email));
            params.add(new BasicNameValuePair("gender",sign_up_gender));
            params.add(new BasicNameValuePair("ojtHours",sign_up_ojtHours));
            params.add(new BasicNameValuePair("course",sign_up_course));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"processRegister.php",
                    "POST", params);

            // check for success tag
            if(null != json) {
                // check log cat fro response
                Log.d("Create Response", json.toString());
                try {
                    int success = json.getInt("success");

                    if (success == 1) {
                        if (null != json.getString("message")) {
                            registrationSuccessful = true;
                            registrationMessage = json.getString("message");
                            Log.d("Account Type", (null != json.getString("accounttype") ? json.getString("accounttype") : ""));
                        }
                    } else {
                        if (null != json.getString("message")) {
                            registrationSuccessful = false;
                            registrationMessage = json.getString("message");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
           /* // dismiss the dialog once done*/

            if(registrationSuccessful){

                toastMessage(registrationMessage);
                Intent int1 = new Intent(CreateStudentActivity.this, Login.class);
                startActivity(int1);
            }else{
                toastMessage(registrationMessage);
            }


        }
    }


    class ConnectToDBViaJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateStudentActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDBViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getCoursesList.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {


                        JSONArray items = json.getJSONArray("courses");
                        courses = new String[items.length()];


                        for(int ctr = 0;  ctr < items.length() ; ctr++){
                            if(ctr==0){
                                courses[0] = "---Select Section---";
                                continue;
                            }
                            for(int i = 1 ; i <= items.getJSONArray(ctr).length()-1 ; i++) {

                                courses[ctr] =items.getJSONArray(ctr).get(1)+"";

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


            if(null != courses && courses.length > 0) {

                coursesAdapter = new ArrayAdapter<String>(CreateStudentActivity.this, android.R.layout.simple_list_item_1, courses){
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
                };


                coursesSpnr.setAdapter(coursesAdapter);

            }

        }
    }

}
