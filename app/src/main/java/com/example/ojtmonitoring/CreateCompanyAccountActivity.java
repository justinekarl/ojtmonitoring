package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateCompanyAccountActivity extends AppCompatActivity {

    private Button cancelSaveCompany;
    private EditText companyNameTxt;
    private EditText addressText;
    private EditText phoneNumberTxt;
    private EditText companyTypeTxt;
    private EditText userNameTxt;
    private EditText passwordTxt;
    private EditText confirmPasswordTxt;
    private Button saveCompanyAccount;

    private static String companyName;
    private static String address;
    private static String phoneNumber;
    private static String companyType;
    private static String userName;
    private static String password;
    private static String confirmPassword;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static boolean registrationSuccessful;
    public static String registrationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company_account);
        cancelSaveCompany = (Button)findViewById(R.id.cancelSaveCompany);

        companyNameTxt = (EditText)findViewById(R.id.companyNameTxt);
        addressText = (EditText)findViewById(R.id.addressText);
        phoneNumberTxt = (EditText)findViewById(R.id.phoneNumberTxt);
        companyTypeTxt = (EditText)findViewById(R.id.companyTypeTxt);
        userNameTxt = (EditText)findViewById(R.id.userNameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        confirmPasswordTxt = (EditText)findViewById(R.id.confirmPasswordTxt);
        saveCompanyAccount = (Button)findViewById(R.id.saveCompanyAccount);

        /*cancelSaveCompany.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent backToSelectUserTypeIntent = new Intent(CreateCompanyAccountActivity.this,AccountCreationSelectionActivity.class) ;
                        startActivity(backToSelectUserTypeIntent);
                    }
                }
        );*/

        cancelSaveCompany.setOnTouchListener(
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
                                Intent backToSelectUserTypeIntent = new Intent(CreateCompanyAccountActivity.this,AccountCreationSelectionActivity.class) ;
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

       /* saveCompanyAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        companyName = (null != companyNameTxt.getText() ? companyNameTxt.getText().toString() : "");
                        address = (null != addressText.getText() ? addressText.getText().toString() : "");
                        phoneNumber = (null != phoneNumberTxt.getText() ? phoneNumberTxt.getText().toString() : "");
                        companyType = (null != companyTypeTxt.getText() ? companyTypeTxt.getText().toString() : "");
                        userName = (null != userNameTxt.getText() ? userNameTxt.getText().toString() : "");
                        password = (null != passwordTxt.getText() ? passwordTxt.getText().toString() : "");
                        confirmPassword = (null != confirmPasswordTxt.getText() ? confirmPasswordTxt.getText().toString() : "");

                        if (companyName.trim().length() == 0
                                || address.trim().length() == 0
                                || phoneNumber.trim().length() == 0
                                || companyType.trim().length() == 0
                                || userName.trim().length() == 0
                                || password.trim().length() == 0
                                || confirmPassword.trim().length() == 0) {
                            toastMessage("All fields are required!");
                        } else {
                            if (password.length() >= 5) {
                                if (password.equals(confirmPassword)) {
                                    CreateCompanyAccountActivity.ProcessAddCompany register = new CreateCompanyAccountActivity.ProcessAddCompany();
                                    register.execute();
                                } else {
                                    toastMessage("Password and Confirm Password not the same!");
                                }
                            } else {
                                toastMessage("Password should be 5 characters and above!");
                            }
                        }

                    }
                }
        );*/

        saveCompanyAccount.setOnTouchListener(
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
                                companyName = (null != companyNameTxt.getText() ? companyNameTxt.getText().toString() : "");
                                address = (null != addressText.getText() ? addressText.getText().toString() : "");
                                phoneNumber = (null != phoneNumberTxt.getText() ? phoneNumberTxt.getText().toString() : "");
                                companyType = (null != companyTypeTxt.getText() ? companyTypeTxt.getText().toString() : "");
                                userName = (null != userNameTxt.getText() ? userNameTxt.getText().toString() : "");
                                password = (null != passwordTxt.getText() ? passwordTxt.getText().toString() : "");
                                confirmPassword = (null != confirmPasswordTxt.getText() ? confirmPasswordTxt.getText().toString() : "");

                                if (companyName.trim().length() == 0
                                        || address.trim().length() == 0
                                        || phoneNumber.trim().length() == 0
                                        || companyType.trim().length() == 0
                                        || userName.trim().length() == 0
                                        || password.trim().length() == 0
                                        || confirmPassword.trim().length() == 0) {
                                    toastMessage("All fields are required!");
                                } else {
                                    if (password.length() >= 5) {
                                        if (password.equals(confirmPassword)) {
                                            CreateCompanyAccountActivity.ProcessAddCompany register = new CreateCompanyAccountActivity.ProcessAddCompany();
                                            register.execute();
                                        } else {
                                            toastMessage("Password and Confirm Password not the same!");
                                        }
                                    } else {
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

    class ProcessAddCompany extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateCompanyAccountActivity.this);
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
            //String sign_up_student_number = studentNo;
            String sign_up_full_name = companyName;
            String sign_up_company_type = companyType;
            String sign_up_address = address;
            String sign_up_phone = phoneNumber;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", sign_up_user_name));
            params.add(new BasicNameValuePair("password", sign_up_password));
            //params.add(new BasicNameValuePair("student_number", sign_up_student_number));
            params.add(new BasicNameValuePair("full_name", sign_up_full_name));
            params.add(new BasicNameValuePair("admin_password", "123456"));
            params.add(new BasicNameValuePair("companyType",sign_up_company_type));
            params.add(new BasicNameValuePair("address",sign_up_address));
            params.add(new BasicNameValuePair("phonenumber",sign_up_phone));
            params.add(new BasicNameValuePair("accounttype","3"));





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
                Intent int1 = new Intent(CreateCompanyAccountActivity.this, Login.class);
                startActivity(int1);
            }else{
                toastMessage(registrationMessage);
            }


        }
    }

}
