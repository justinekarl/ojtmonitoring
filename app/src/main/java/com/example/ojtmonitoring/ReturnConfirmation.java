package com.example.ojtmonitoring;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReturnConfirmation extends AppCompatActivity {

    private EditText returnPassword;
    private Button enterReturnPassword;
    private Button inquire;

    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    public static String actionTaken;
    public static String userName;
    public static int agentId;

    public static String item;
    public static String message;
    final Activity activity = this;
    public static String passwordEntered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_confirmation);

        //setting of agent values
        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        userName = sharedPreferences.getString("user_name", "");
        agentId = sharedPreferences.getInt("agent_id",0);

        enterReturnPassword = (Button)findViewById(R.id.confirm_return_button);
        inquire = (Button) findViewById(R.id.inquire);
        returnPassword=(EditText)findViewById(R.id.return_password_confirmation);


        enterReturnPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {

                actionTaken = "checkPassword";
                passwordEntered = returnPassword.getText().toString();

                if(passwordEntered.trim().length() > 0){
                    ConnectToDataBaseViaJson check = new ConnectToDataBaseViaJson();
                    check.execute();
                }else{
                    toastMessage("Authentication Field is required");
                }


            }
        });

        inquire.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                toastMessage("Kindly ask your Administrator for the Authentication Code");
            }
        });
    }


    //scan activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled The Scanning", Toast.LENGTH_LONG).show();
            } else {
                if (result.toString().length() > 0) {
                    StringBuffer value = new StringBuffer("");
                    boolean checker  = false;
                    String[] arr = result.toString().split(" ");
                    String first = "QR_CODE\n"+"Contents:";
                    String last  = "bytes:";
                    for ( String ss : arr) {
                        if (first.equalsIgnoreCase(ss)){
                            checker = true;
                            continue;
                        }
                        if (last.equalsIgnoreCase(ss)){
                            checker = false;
                        }
                        if(checker){
                            value.append(" " + ss);
                        }
                    }
                    String finalValue = value+"";
                    finalValue = finalValue.substring(3);
                    finalValue = finalValue.substring(0, finalValue.length() - 5);
                    AddData(finalValue+"");
                } else {
                    toastMessage("You must put something in the text field!");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //end scan activity

    public void AddData(String newEntry) {
        //boolean insertData = databaseHelper.addData(newEntry);
        item=newEntry;
        ConnectToDataBaseViaJson connectToDatabase = new ConnectToDataBaseViaJson();
        connectToDatabase.execute();
    }


    //connect database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReturnConfirmation.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            String borrowerName = userName;
            String itemBorrowed = item;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if(actionTaken == "checkPassword"){
                params.add(new BasicNameValuePair("admin_password", passwordEntered));
            }else{
                params.add(new BasicNameValuePair("borrower", borrowerName));
                params.add(new BasicNameValuePair("item", itemBorrowed));
                params.add(new BasicNameValuePair("agent_id", agentId+""));
            }



            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json;
            if(actionTaken == "checkPassword"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"check_password.php",
                        "POST", params);
            }else if(actionTaken == "borrow"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"create_product.php",
                        "POST", params);
            }else{
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"return_product.php",
                        "POST", params);
            }


            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {

                message = json.getString("message");

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
            if(actionTaken == "checkPassword"){
                if(message.equalsIgnoreCase("ok.")){
                    actionTaken="return";
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }else{
                    toastMessage("Incorrect Return Password");
                }
            }else{
                Intent int1 = new Intent(ReturnConfirmation.this, content.class);
                startActivity(int1);
                toastMessage(message);
            }

        }
    }

    //connect database


    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
