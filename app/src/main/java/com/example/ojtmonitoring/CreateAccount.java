package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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



public class signup extends AppCompatActivity {

    private EditText signUpUserName;
    private EditText signUpPassword;
    private EditText signUpFullName;
    private EditText signUpStudentNumber;
    private EditText signUpPasswordAdmin;
    private EditText signUpPasswordConfirm;
    private Button register;
    private Button backToLogin;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public static String userName;
    public static String password;
    public static String passwordConfirm;
    public static String studentNumber;
    public static String fullName;
    public static boolean registrationSuccessful;
    public static String registrationMessage;
    public static  String passwordAdmin;

    private Button inquire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        register=(Button)findViewById(R.id.register);
        backToLogin=(Button)findViewById(R.id.backToLogin);
        signUpUserName=(EditText)findViewById(R.id.signUpUserName);
        signUpPassword=(EditText)findViewById(R.id.signUpPassword);
        signUpPasswordConfirm = (EditText)findViewById(R.id.signUpPasswordConfirm);

        signUpFullName=(EditText)findViewById(R.id.fullName);
        signUpStudentNumber=(EditText)findViewById(R.id.studentNumber);
        signUpPasswordAdmin = (EditText)findViewById(R.id.signUpPasswordAdmin);

        inquire = (Button) findViewById(R.id.inquire);

        inquire.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                toastMessage("Kindly ask your Administrator for the Authentication Code");
            }
        });


        backToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                Intent logInPage = new Intent(signup.this, login.class);
                startActivity(logInPage);
           }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                userName = signUpUserName.getText().toString();
                password = signUpPassword.getText().toString();
                passwordConfirm = signUpPasswordConfirm.getText().toString();
                studentNumber = signUpStudentNumber.getText().toString();
                fullName = signUpFullName.getText().toString();
                passwordAdmin = signUpPasswordAdmin.getText().toString();

                if(null != userName && userName.trim().length() == 0
                        || null != password && password.trim().length() == 0
                            || null != studentNumber && studentNumber.trim().length() == 0
                                || null != fullName && fullName.trim().length() == 0
                                    || null != passwordAdmin && passwordAdmin.trim().length() == 0
                                        || null != passwordConfirm && passwordConfirm.trim().length() == 0){
                    toastMessage("All Fields are Required!");
                }else{
                    if(password.length() >= 5){
                        if(password.equals(passwordConfirm)){
                            CreateNewProduct register = new CreateNewProduct();
                            register.execute();
                        }else{
                            toastMessage("Password and Confirm Password not the same!");
                        }
                    }else{
                        toastMessage("Password should be 5 characters and above!");
                    }
                }
            }
        });


    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {

    }


    //connecting to the database

    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(signup.this);
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
            String sign_up_student_number = studentNumber;
            String sign_up_full_name = fullName;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", sign_up_user_name));
            params.add(new BasicNameValuePair("password", sign_up_password));
            params.add(new BasicNameValuePair("student_number", sign_up_student_number));
            params.add(new BasicNameValuePair("full_name", sign_up_full_name));
            params.add(new BasicNameValuePair("admin_password", passwordAdmin));





            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"register.php",
                    "POST", params);

            // check log cat fro response
           // Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    if(null != json.getString("message")){
                        registrationSuccessful = true;
                        registrationMessage=json.getString("message");
                    }
                } else {
                    if(null != json.getString("message")){
                        registrationSuccessful = false;
                        registrationMessage = json.getString("message");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            /*AlertDialog alert = new AlertDialog();
            alert.setMessage(popMessage);
            alert.setCancelable(true);
            alert.show();*/
            if(registrationSuccessful){
                Intent int1 = new Intent(signup.this, login.class);
                startActivity(int1);
            }else{
                toastMessage(registrationMessage);
            }


        }
    }

    //end of connecting

}
