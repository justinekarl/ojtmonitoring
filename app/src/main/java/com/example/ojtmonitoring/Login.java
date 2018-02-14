package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jomer.filetracker.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private EditText editText1 , editText2;
    private Button log_in;
    private Button sign_up;
    private Button exit;
    private ProgressDialog pDialog;
    private TextView forgotPassword;

    JSONParser jsonParser = new JSONParser();

    public static String userName;
    public static String password;
    public static boolean adminRights = false;
    public static String studentNumber;
    public static String fullName;

    public static boolean transferNotification = false;
    public static boolean loginSucessful;
    public static String loginMessage;
    public static int agentId;
    public static ArrayList <String> arrayTransferData = new ArrayList<>();
    public static int accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //pop confirmation
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to continue with your action?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });
        // end of confirmation

        log_in=(Button)findViewById(R.id.log_in);
        sign_up = (Button)findViewById(R.id.sign_up);
        editText1=(EditText)findViewById(R.id.userNameEdTxt);
        editText2=(EditText)findViewById(R.id.editText2);
        exit = (Button) findViewById(R.id.exit);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                toastMessage("Contact Your Admin for Password Retrieval.");
            }
        });

        /*log_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                userName = editText1.getText().toString();
                password = editText2.getText().toString();
                if(null != userName && userName.trim().length() == 0
                        || null != password && password.trim().length() == 0){
                    toastMessage("All Fields are Required!");
                }else{
                    ConnectToDataBaseViaJson login = new ConnectToDataBaseViaJson();
                    login.execute();
                }
            }
        });*/

        //sign up button
      /*  sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
              Intent accntSelectionSignUpPage = new Intent(Login.this, AccountCreationSelectionActivity.class);
                startActivity(accntSelectionSignUpPage);

            }
        });*/

        /*exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });*/
        log_in.setOnTouchListener(
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
                                userName = editText1.getText().toString();
                                password = editText2.getText().toString();
                                if(null != userName && userName.trim().length() == 0
                                        || null != password && password.trim().length() == 0){
                                    toastMessage("All Fields are Required!");
                                }else{
                                    ConnectToDataBaseViaJson login = new ConnectToDataBaseViaJson();
                                    login.execute();
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


        sign_up.setOnTouchListener(
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
                                Intent accntSelectionSignUpPage = new Intent(Login.this, AccountCreationSelectionActivity.class);
                                startActivity(accntSelectionSignUpPage);
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


        exit.setOnTouchListener(
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
                                AlertDialog dialog = builder.create();
                                dialog.show();
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
    @Override
    public void onBackPressed() {

    }


    //connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String sign_up_user_name = userName;
            String sign_up_password = password;



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", sign_up_user_name));
            params.add(new BasicNameValuePair("password", sign_up_password));
            params.add(new BasicNameValuePair("accounttype","1"));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"login.php",
                    "POST", params);


            try {
                if(null != json){
                    int success = json.getInt("success");
                    if(success == 1) {
                        loginSucessful = true;
                        agentId = json.getInt("id");

                        fullName = json.getString("name");
                        accountType = json.getInt("accounttype");

                        /*if(Integer.parseInt(accountType) == 1){
                            studentNumber = json.getString("studentnumber");
                        }

                        if(Integer.parseInt(accountType) == 2){
                            studentNumber = json.getString("studentnumber");
                        }

                        if(Integer.parseInt(accountType) == 3){
                            studentNumber = json.getString("studentnumber");
                        }*/
                    }else {
                        loginSucessful =  false;
                        if(null != json.getString("message")){
                            loginMessage=json.getString("message");
                        }
                    }
                    /*if (success == 1) {
                        transferNotification = false;
                        loginSucessful = true;
                        agentId = json.getInt("id_agent");
                        adminRights = json.getInt("admin") == 1;
                        studentNumber  = json.getString("student_number");
                        fullName = json.getString("full_name");

                    } else if (success == 2) {
                        arrayTransferData.clear();
                        transferNotification = true;
                        loginSucessful = true;
                        agentId = json.getInt("id_agent");
                        adminRights = json.getInt("admin") == 1;
                        studentNumber  = json.getString("student_number");
                        fullName = json.getString("full_name");
                        if(null != json.getJSONArray("transfer_data")){
                            JSONArray transferData = json.getJSONArray("transfer_data");
                            for (int ctr = 0; ctr < transferData.length();ctr++ ) {
                                arrayTransferData.add(transferData.get(ctr)+"");
                            }
                        }
                    } else {
                        loginSucessful =  false;
                        if(null != json.getString("message")){
                            loginMessage=json.getString("message");
                        }
                    }*/
                }else{
                    loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(loginSucessful){
                SharedPreferences sharedpreferences = getSharedPreferences(
                        PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("user_name",userName);
                editor.putInt("agent_id",agentId);
                editor.putString("full_name",fullName);
                editor.putString("student_number",studentNumber);
                editor.putInt("accounttype",accountType);
                editor.commit();


                if(accountType == 1){
                    Intent int1 = new Intent(Login.this, StudentLoginActivity.class);
                    startActivity(int1);
                }

                if(accountType == 2){
                    Intent int1 = new Intent(Login.this, StudentLoginActivity.class);
                    startActivity(int1);
                }

                if(accountType == 3){
                    Intent int1 = new Intent(Login.this, StudentLoginActivity.class);
                    startActivity(int1);
                }
               /* if(transferNotification){
                    Intent int1 = new Intent(Login.this, Transfer.class);
                    startActivity(int1);
                }else{
                    Intent int1 = new Intent(Login.this, content.class);
                    if(adminRights){
                        int1 = new Intent(Login.this, admin.class);
                    }startActivity(int1);
                }*/


            }else{
                toastMessage(loginMessage);
            }
        }
    }
    //end of connecting

}
