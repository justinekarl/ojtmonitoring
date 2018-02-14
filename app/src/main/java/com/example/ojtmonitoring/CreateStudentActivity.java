package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jomer.filetracker.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    private EditText confirmPasswordTxt;private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    public static String fullname;
    public static String studentNo;
    public static String phoneNum;
    public static String address;
    public static String college;
    public static String username;
    public static String password;
    public static String confirmPassword;

    public static boolean registrationSuccessful;
    public static String registrationMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student_account);

        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        fullnameTxt = (EditText)findViewById(R.id.fullnameTxt);
        studentNoTxt = (EditText)findViewById(R.id.studentNoTxt);
        phoneNumTxt = (EditText)findViewById(R.id.phoneNumbTxt);
        addressTxt = (EditText)findViewById(R.id.addressTxt);
        collegeTxt = (EditText)findViewById(R.id.collegeTxt);
        usernameTxt = (EditText)findViewById(R.id.userNameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        confirmPasswordTxt = (EditText)findViewById(R.id.confirmPasswordTxt) ;

        /*cancelBtn.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent backToSelectUserTypeIntent = new Intent(CreateStudentActivity.this,AccountCreationSelectionActivity.class) ;
                        startActivity(backToSelectUserTypeIntent);
                    }
                }
        );*/


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
        /*saveBtn.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fullname = null != fullnameTxt.getText() ? fullnameTxt.getText().toString() : "";
                        studentNo = null != studentNoTxt.getText() ? studentNoTxt.getText().toString() : "";
                        phoneNum =  null != phoneNumTxt.getText() ? phoneNumTxt.getText().toString() : "";
                        address = null != addressTxt.getText() ? addressTxt.getText().toString() : "";
                        college = null != collegeTxt.getText() ? collegeTxt.getText().toString() : "";
                        username = null != usernameTxt.getText() ? usernameTxt.getText().toString() : "";
                        password = null != passwordTxt.getText() ? passwordTxt.getText().toString() : "";
                        confirmPassword = null != confirmPasswordTxt.getText() ? confirmPasswordTxt.getText().toString() : "";

                        if(fullname.trim().length() == 0
                                || studentNo.trim().length() == 0
                                    || phoneNum.trim().length() == 0
                                        || address.trim().length() == 0
                                            || college.trim().length()== 0
                                                ||  username.trim().length() == 0){
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

                    }
                }
        );*/

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
                                college = null != collegeTxt.getText() ? collegeTxt.getText().toString() : "";
                                username = null != usernameTxt.getText() ? usernameTxt.getText().toString() : "";
                                password = null != passwordTxt.getText() ? passwordTxt.getText().toString() : "";
                                confirmPassword = null != confirmPasswordTxt.getText() ? confirmPasswordTxt.getText().toString() : "";

                                if(fullname.trim().length() == 0
                                        || studentNo.trim().length() == 0
                                        || phoneNum.trim().length() == 0
                                        || address.trim().length() == 0
                                        || college.trim().length()== 0
                                        ||  username.trim().length() == 0){
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

}
