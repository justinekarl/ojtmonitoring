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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateTeacherAccountActivity extends AppCompatActivity {

    private Button cancelBtn;
    private Button saveBtn;
    private EditText fullNameTxt;
    private EditText teacherNoTxt;
    private EditText phoneNumTxt;
    private EditText departmentTxt;
    private EditText userNameTxt;
    private EditText passwordTxt;
    private EditText confirmPasswordTxt;
    private Spinner collegeSpnr;
    ArrayAdapter<String> collegeListAdapter = null;

    private String fullName;
    private String teacherNo;
    private String phoneNum;
    private String department;
    private String userName;
    private String password;
    private String confirmPassword;
    public static String college;

    public static boolean registrationSuccessful;
    public static String registrationMessage;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher_account);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        saveBtn = (Button)findViewById(R.id.saveTeacherBtn);
        fullNameTxt = (EditText)findViewById(R.id.fullNameTxt);
        teacherNoTxt = (EditText)findViewById(R.id.teacherNoTxt);
        phoneNumTxt = (EditText)findViewById(R.id.custPhoneNumberTxt);
        departmentTxt = (EditText)findViewById(R.id.departmentTxt);
        userNameTxt = (EditText)findViewById(R.id.userNameTxt);
        passwordTxt = (EditText)findViewById(R.id.PasswordTxt);
        confirmPasswordTxt = (EditText)findViewById(R.id.confirmPasswordTxt);
        collegeSpnr = (Spinner)findViewById(R.id.collegeSpnr);

        collegeListAdapter = new ArrayAdapter<String>(CreateTeacherAccountActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.collegelist));

        collegeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpnr.setAdapter(collegeListAdapter);

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
                                Intent backToSelectUserTypeIntent = new Intent(CreateTeacherAccountActivity.this,AccountCreationSelectionActivity.class) ;
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
                                fullName = (null != fullNameTxt.getText() ? fullNameTxt.getText().toString() : "");
                                teacherNo = (null != teacherNoTxt.getText() ? teacherNoTxt.getText().toString() : "");
                                phoneNum = (null != phoneNumTxt.getText() ? phoneNumTxt.getText().toString() : "");
                                department = (null != departmentTxt.getText() ? departmentTxt.getText().toString() : "");
                                userName = (null != userNameTxt.getText() ? userNameTxt.getText().toString() : "");
                                password = (null != passwordTxt.getText() ? passwordTxt.getText().toString() : "");
                                confirmPassword = (null != confirmPasswordTxt.getText() ? confirmPasswordTxt.getText().toString() : "");
                                college = null != collegeSpnr.getSelectedItem() ? collegeSpnr.getSelectedItem().toString() : "";

                                if(fullName.trim().length() == 0
                                        || teacherNo.trim().length() == 0
                                        || phoneNum.trim().length() == 0
                                        || department.trim().length() == 0
                                        || userName.trim().length() == 0
                                        || college.trim().length()== 0
                                        || password.trim().length() == 0
                                        || confirmPassword.trim().length() == 0){
                                    toastMessage("All fields are required!");
                                }else{
                                    if(password.length() >= 5){
                                        if(password.equals(confirmPassword)){
                                            CreateTeacherAccountActivity.ProcessAddTeacher register = new CreateTeacherAccountActivity.ProcessAddTeacher();
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

    }


    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    class ProcessAddTeacher extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateTeacherAccountActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            String sign_up_user_name = userName;
            String sign_up_password = password;
            String sign_up_teacher_number = teacherNo;
            String sign_up_full_name = fullName;
            String sign_up_department = department;
            String sign_up_phone = phoneNum;
            String sign_up_college = college;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", sign_up_user_name));
            params.add(new BasicNameValuePair("password", sign_up_password));
            params.add(new BasicNameValuePair("teacher_number", sign_up_teacher_number));
            params.add(new BasicNameValuePair("full_name", sign_up_full_name));
            params.add(new BasicNameValuePair("admin_password", "123456"));
            params.add(new BasicNameValuePair("department",sign_up_department));
            params.add(new BasicNameValuePair("phonenumber",sign_up_phone));
            params.add(new BasicNameValuePair("accounttype","2"));
            params.add(new BasicNameValuePair("college",sign_up_college));



            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"processRegister.php",
                    "POST", params);



            if(null != json) {

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag
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
                Intent int1 = new Intent(CreateTeacherAccountActivity.this, Login.class);
                startActivity(int1);
            }else{
                toastMessage(registrationMessage);
            }


        }
    }
}
