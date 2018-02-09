package com.example.jomer.filetracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class content extends AppCompatActivity {

    private TextView textView;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private Button userViewLogBorrowed;
    private Button userViewLogReturned;
    private Button userBorrow;
    private Button userReturn;
    private Button logout;
    private Button exit;
    private Button changePassword;
    private Button history;
    public static String item;
    public static String message;

    public static String actionTaken;
    public static String userName;
    public static int agentId;
    public static String fullName;
    public static String studentNumber;

    //fetch borrowed items
    public static ArrayList<String> dataNeeded = new ArrayList<>();

    //end of fetch borrowed items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);


        //pop confirmation
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to continue with your action?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(actionTaken == "logout"){
                            SharedPreferences preferences =getSharedPreferences(PaceSettingManager.USER_PREFERENCES,MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent signUpPage = new Intent(content.this, login.class);
                            startActivity(signUpPage);
                        }else{
                            finishAffinity();
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });
        // end of confirmation

        //setting of agent values
        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        userName = sharedPreferences.getString("user_name", "");
        agentId = sharedPreferences.getInt("agent_id",0);
        fullName =sharedPreferences.getString("full_name","");
        studentNumber = sharedPreferences.getString("student_number","");

        TextView textView = (TextView) findViewById(R.id.user_name);
        textView.setText("Welcome " + fullName  );

        //end of setting of agent values

        userViewLogBorrowed = (Button) findViewById(R.id.user_view_log_borrowed);
        userViewLogReturned= (Button) findViewById(R.id.user_view_log_ruturned);
        userBorrow= (Button) findViewById(R.id.user_borrow);
        userReturn= (Button) findViewById(R.id.user_return);
        logout = (Button) findViewById(R.id.user_logout);
        exit = (Button) findViewById(R.id.exit);
        changePassword = (Button) findViewById(R.id.changePassword);
        history = (Button) findViewById(R.id.history);

        final Activity activity = this;

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editPassword = new Intent(content.this, EditPassword.class);
                startActivity(editPassword);
            }
        });

        userBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTaken="borrow";
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        userReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTaken="return";
                Intent returnConfirmation = new Intent(content.this, ReturnConfirmation.class);
                startActivity(returnConfirmation);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken = "logout";
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        userViewLogBorrowed.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken="viewBorrowed";
                ConnectToDataBaseViaJson fetchBorrowed = new ConnectToDataBaseViaJson();
                fetchBorrowed.execute();
            }
        });


        userViewLogReturned.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken="viewReturned";
                ConnectToDataBaseViaJson fetchBReturned = new ConnectToDataBaseViaJson();
                fetchBReturned.execute();

            }
        });

        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken = "exit";
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        history.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken="history";
                ConnectToDataBaseViaJson fetchBReturned = new ConnectToDataBaseViaJson();
                fetchBReturned.execute();

            }
        });

        /*userViewLogReturned.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                Intent returnConfirmation = new Intent(content.this, ReturnConfirmation.class);
                startActivity(returnConfirmation);
            }
        });
*/
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
            content.dataNeeded.clear();
            pDialog = new ProgressDialog(content.this);
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
            params.add(new BasicNameValuePair("borrower", borrowerName));
            params.add(new BasicNameValuePair("item", itemBorrowed));
            params.add(new BasicNameValuePair("agent_id", agentId+""));


            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = new JSONObject();
            if(actionTaken == "borrow"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"create_product.php",
                        "POST", params);
            }else if(actionTaken == "return"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"return_product.php",
                        "POST", params);
            }else if(actionTaken == "viewBorrowed"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_user_borrowed_item.php",
                        "POST", params);
            }else if(actionTaken == "viewReturned"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_user_returned_item.php",
                        "POST", params);
            }else if (actionTaken == "history"){
                params.add(new BasicNameValuePair("all_log", "true"));
                params.add(new BasicNameValuePair("agent_id", agentId+""));
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_logs.php",
                        "POST", params);
            }

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (actionTaken == "viewBorrowed" || actionTaken == "viewReturned") {
                    if (success == 1) {
                        JSONArray items = json.getJSONArray("data_needed");
                        for(int ctr = 0;  ctr < items.length() ; ctr++){
                            content.dataNeeded.add(items.get(ctr)+"");
                        }
                    } else {
                        content.dataNeeded.clear();
                    }
                }else if(actionTaken == "borrow" || actionTaken == "return"){
                    message = json.getString("message");
                }else if(actionTaken == "history"){
                    admin.transactionLogsData.clear();
                    if (success == 1) {
                        JSONArray logs = json.getJSONArray("data_needed");
                        List<Map<String, String>> logValuePair = new ArrayList<>();
                        for (int ctr = 0; ctr < logs.length();ctr++ ) {
                            Map<String, String> logMap = new HashMap<>();
                            logMap.put("id",logs.getJSONArray(ctr).getJSONObject(0).get("id").toString());
                            logMap.put("item",logs.getJSONArray(ctr).getJSONObject(1).get("item").toString());
                            logMap.put("date_created",logs.getJSONArray(ctr).getJSONObject(3).get("date_created").toString());
                            logMap.put("borrowed",logs.getJSONArray(ctr).getJSONObject(4).get("borrowed").toString());
                            logMap.put("returned",logs.getJSONArray(ctr).getJSONObject(5).get("returned").toString());
                            logMap.put("full_name",logs.getJSONArray(ctr).getJSONObject(12).get("full_name").toString());
                            logValuePair.add(logMap);
                        }


                        ArrayList <Map<String, String>> transactionLogsDataHolder = new ArrayList<>();
                        for(Map<String, String> log : logValuePair){
                            StringBuffer returnString = new StringBuffer();
                            returnString.append("Transaction No: "+log.get("id")+" \n");
                            if(Long.valueOf(log.get("borrowed")) == 1){
                                returnString.append("Transaction Type: Borrowed \n");
                            }else {
                                returnString.append("Transaction Type: Returned \n");
                            }
                            returnString.append("Name: "+log.get("full_name")+" \n");
                            returnString.append("Date: "+log.get("date_created")+" \n \n");
                            returnString.append("Item: \n"+log.get("item")+" \n");
                            //content.dataNeeded.add(returnString+"");
                            Map<String, String> value = new HashMap<>();
                            value.put("id",log.get("id"));
                            value.put("value",returnString.toString());
                            transactionLogsDataHolder.add(value);
                        }admin.transactionLogsData.addAll(transactionLogsDataHolder);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if(actionTaken == "viewBorrowed"){
                Intent intent = new Intent(content.this, UserBorrowedItems.class);
                startActivity(intent);
            }else if(actionTaken == "viewReturned"){
                Intent userReturnedItem = new Intent(content.this, UserReturnedItem.class);
                startActivity(userReturnedItem);
            }else if(actionTaken == "borrow" || actionTaken == "return"){
                toastMessage(message);
            }else if(actionTaken == "history" || actionTaken == "history"){
                Intent int1= new Intent(content.this,AllLogsUser.class);
                startActivity(int1);
            }else {
                pDialog.dismiss();
                Intent int1 = new Intent(content.this, content.class);
                startActivity(int1);
            }
            pDialog.hide();
        }
    }

    //connect database

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

    }


}
