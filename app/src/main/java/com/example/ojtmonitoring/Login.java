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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

    private boolean administrator;
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

    public static String address;
    public static String phoneNumber;
    public static String teacherNumber;
    public static String department;
    public static String college;
    public static int companyId;
    //boolean ojtDone;
    String ojtDone;
    String adminTeacher;

    public static TextView createAccountNowTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(null != jsonParser){
            jsonParser.json = "";
            jsonParser.jObj = null;
            jsonParser.is = null;
        }

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

        editText1.requestFocus();

        createAccountNowTxt = (TextView)findViewById(R.id.createAccountNowTxt);

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

        createAccountNowTxt.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent accntSelectionSignUpPage = new Intent(Login.this, AccountCreationSelectionActivity.class);
                        startActivity(accntSelectionSignUpPage);
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
                        if(json.has("name")) fullName = json.getString("name");
                        if(json.has("accounttype")) accountType = json.getInt("accounttype");
                        if(json.has("address")) address = json.getString("address");
                        if(json.has("phoneNumber")) phoneNumber = json.getString("phoneNumber");
                        if(json.has("teachernumber")) teacherNumber = json.getString("teachernumber");
                        if(json.has("department")) department = json.getString("department");
                        if(json.has("college")) college = json.getString("college");
                        if(json.has("ojt_done")) ojtDone = json.getString("ojt_done");
                        if(json.has("company_id")) companyId = null != json.get("company_id") && !json.get("company_id").toString().equals("null") ? json.getInt("company_id") : 0;
                        if(json.has("admin_teacher")) adminTeacher = json.getString("admin_teacher");
                        if(json.has("admin_account")) administrator = json.getBoolean("admin_account");

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
                e.printStackTrace();
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
                editor.putString("address",address);
                editor.putString("phoneNumber",phoneNumber);
                editor.putString("college",college);
                editor.putString("department",department);
                editor.putInt("companyId",companyId);
                editor.putString("ojtDone",ojtDone);
                editor.putString("ojt_status",ojtDone.equals("1") ? "OJT Finished" : "OJT In progress");
                editor.putBoolean("admin_teacher",null != adminTeacher ? (adminTeacher.equals("1") ? true : false) : false);
                editor.putBoolean("admin_account",administrator);
                editor.commit();

                if(accountType == 0){
                    Intent admin = new Intent(Login.this, AdministratorActivity.class);
                    startActivity(admin);
                }
                if(accountType == 1){
                    Intent int1 = new Intent(Login.this, StudentLoginActivity.class);
                    startActivity(int1);
                }

                if(accountType == 2){
                    Intent int1 = new Intent(Login.this, TeacherLoginActivity.class);
                    startActivity(int1);
                }

                if(accountType == 3){
                    Intent int1 = new Intent(Login.this, CompanyLoginActivity.class);
                    startActivity(int1);
                }
                if(accountType == 4){
                    Intent int1 = new Intent(Login.this, CoordinatorLoginActivity.class);
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
