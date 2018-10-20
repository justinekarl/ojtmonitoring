package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditPassword extends AppCompatActivity {

    private Button updateCode;
    private EditText oldCode;
    private EditText newCode;
    private EditText confirmNewCode;
    public static String newUpdateValue;
    public static String oldUpdateValue;
    public static String successMessage;
    JSONParser jsonParser =  new JSONParser();
    public static int agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        //pop confirmation
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to update your password?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectToDataBaseViaJson update = new ConnectToDataBaseViaJson();
                        update.execute();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });
        // end of confirmation

        updateCode = (Button) findViewById(R.id.updateCode);
        oldCode = (EditText)findViewById(R.id.oldCode);
        newCode = (EditText)findViewById(R.id.newCode);
        confirmNewCode = (EditText)findViewById(R.id.confirmNewCode);

        //setting of agent values
        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);


        updateCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                String oldCodeValue = oldCode.getText().toString();
                String newCodeValue = newCode.getText().toString();
                String confirmNewCodeValue = confirmNewCode.getText().toString();

                if((null != oldCodeValue && oldCodeValue.trim().length() == 0) ||
                        (null != newCodeValue && newCodeValue.trim().length() == 0) ||
                        (null != confirmNewCodeValue && confirmNewCodeValue.trim().length() == 0)){
                    toastMessage("All Fields Are Required");
                }else{
                    if(newCode.length() >= 5){
                        if(newCodeValue.equals(confirmNewCodeValue)){
                            newUpdateValue = newCodeValue;
                            oldUpdateValue = oldCodeValue;
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            toastMessage("New password and Confirm Password Not the same");
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

    //connecting to the database
    private ProgressDialog pDialog;
    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPassword.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("new_password", newUpdateValue));
            params.add(new BasicNameValuePair("old_password", oldUpdateValue));
            params.add(new BasicNameValuePair("id_agent", agentId+""));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"update_password_user.php",
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                successMessage = json.getString("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.hide();
            if(successMessage.equalsIgnoreCase("updated")){
                Intent activity;
                if(Login.adminRights){
                    activity = new Intent(EditPassword.this, admin.class);
                }else{
                    activity = new Intent(EditPassword.this, content.class);
                }

                startActivity(activity);
            }
            toastMessage(successMessage);
            toastMessage(successMessage);
        }
    }

    //end of connecting

}