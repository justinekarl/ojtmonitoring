package com.example.ojtmonitoring;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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


public class admin extends AppCompatActivity implements View.OnClickListener{
    Button logout,  allAccounts,editAuthCode, exit;
    /*Button allQrCode,registerCode;*/
    /* Button logHistory,borrowedLogs, returnedLogs, allLogs; */
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private ProgressDialog pDialog;
    private TextView adminUserName;
    public RelativeLayout blurredLayout;
    public RelativeLayout adminLayout;

    public static String actionTaken;
    public static String item;
    public static String message;

    public static ArrayList <Map<String, String>> transactionLogsData = new ArrayList<>();
    public static Set<Map<String, String>> allUsers = new HashSet<>();
    public static Set<Map<String, String>> allQrCodes = new HashSet<>();

    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminLayout = (RelativeLayout) findViewById(R.id.activity_admin);
        blurredLayout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        init();
        popupInit();

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
                            Intent signUpPage = new Intent(admin.this, Login.class);
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

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        String fullName =sharedPreferences.getString("full_name","");

        adminUserName = (TextView) findViewById(R.id.adminUserName);
        adminUserName.setText(fullName);

        logout = (Button) findViewById(R.id.logOut);
        changePassword = (Button) findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editPassword = new Intent(admin.this, EditPassword.class);
                startActivity(editPassword);
            }
        });
        /*borrowedLogs = (Button) findViewById(R.id.viewUserBorrowedLogs);
        returnedLogs = (Button) findViewById(R.id.viewUserReturnedLogs);
        allLogs = (Button) findViewById(R.id.allLogs);
        logHistory = (Button) findViewById(R.id.logHistory);*/

        allAccounts = (Button) findViewById(R.id.allAccounts);

        editAuthCode = (Button) findViewById(R.id.editAuthCode);
       /* registerCode = (Button) findViewById(R.id.registerCode);
        allQrCode = (Button) findViewById(R.id.allQrCode);*/

        exit = (Button) findViewById(R.id.exit);

        final Activity activity = this;

        /*allLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTaken = "allLogs";
                ConnectToDataBaseViaJson fetchAllLogs = new ConnectToDataBaseViaJson();
                fetchAllLogs.execute();

            }
        });

        borrowedLogs.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken="viewBorrowed";
                ConnectToDataBaseViaJson fetchBorrowed = new ConnectToDataBaseViaJson();
                fetchBorrowed.execute();
            }
        });

        returnedLogs.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken="viewReturned";
                ConnectToDataBaseViaJson fetchReturned =  new ConnectToDataBaseViaJson();
                fetchReturned.execute();
            }
        });

        logHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken = "allLogsHistory";
                ConnectToDataBaseViaJson fetchAllLogs = new ConnectToDataBaseViaJson();
                fetchAllLogs.execute();
            }
        });*/

        allAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTaken = "allUsers";
                adminLayout.setEnabled(false);
                ConnectToDataBaseViaJson fetchAllUsers= new ConnectToDataBaseViaJson();
                fetchAllUsers.execute();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTaken = "logout";
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        editAuthCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                Intent view = new Intent(admin.this, EditAuthCode.class);
                startActivity(view);
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


        /*allQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTaken = "allQrCode";
                adminLayout.setEnabled(false);
                ConnectToDataBaseViaJson fetchAllQrCode= new ConnectToDataBaseViaJson();
                fetchAllQrCode.execute();

            }
        });

        registerCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                actionTaken = "registerCode";
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });*/

    }
    @Override
    public void onBackPressed() {

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
            transactionLogsData.clear();
            allUsers.clear();
            allQrCodes.clear();
            pDialog = new ProgressDialog(admin.this);
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
            params.add(new BasicNameValuePair("agent_id", "all"));


            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = new JSONObject();
            if(actionTaken == "viewBorrowed"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_user_borrowed_item.php",
                        "POST", params);
            }else if(actionTaken == "viewReturned"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_user_returned_item.php",
                        "POST", params);
            }else if(actionTaken == "allLogs"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_logs.php",
                        "POST", params);
            }else if(actionTaken == "allUsers"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_users.php",
                        "POST", params);
            }else if (actionTaken == "allLogsHistory"){
                params.add(new BasicNameValuePair("all_log", "true"));
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_logs.php",
                        "POST", params);
            }else if (actionTaken == "registerCode"){
                params.add(new BasicNameValuePair("item", item));
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"register_item.php",
                        "POST", params);
            }else if(actionTaken == "allQrCode"){
                params.add(new BasicNameValuePair("action", "search"));
                params.add(new BasicNameValuePair("parameter", "all"));
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_qr_code.php",
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
                        content.dataNeeded.add(json.getString("data_needed"));

                    }
                }else if(actionTaken == "allLogs" || actionTaken == "allLogsHistory"){
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
                        }transactionLogsData.addAll(transactionLogsDataHolder);
                    }
                }else if(actionTaken == "allUsers"){
                    JSONArray users = json.getJSONArray("data_needed");
                    List<Map<String, String>> usersValuePair = new ArrayList<>();
                    for (int ctr = 0; ctr < users.length();ctr++ ) {
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("agent_id",users.getJSONArray(ctr).getJSONObject(0).get("id_agent").toString());
                        userMap.put("student_number",users.getJSONArray(ctr).getJSONObject(3).get("student_number").toString());
                        userMap.put("full_name",users.getJSONArray(ctr).getJSONObject(4).get("full_name").toString());
                        userMap.put("not_clear",users.getJSONArray(ctr).getJSONObject(6).get("not_clear").toString());
                        usersValuePair.add(userMap);
                    }

                    for(Map<String, String> user : usersValuePair){
                        StringBuffer returnString = new StringBuffer();
                        returnString.append("ID No: "+user.get("student_number")+" \n");
                        returnString.append("Name: "+user.get("full_name")+" \n");
                        Map<String, String> value = new HashMap<>();
                        value.put("id",user.get("agent_id"));
                        value.put("value",returnString.toString());
                        value.put("not_clear",user.get("not_clear"));
                        allUsers.add(value);
                    }
                }

                else if (actionTaken == "registerCode"){
                    message = json.getString("message");
                }

                else if(actionTaken == "allQrCode"){
                    JSONArray fetchedQrCodes = json.getJSONArray("data_needed");
                    for (int ctr = 0; ctr < fetchedQrCodes.length();ctr++ ) {
                        Map<String, String> qrMap = new HashMap<>();
                        qrMap.put("id_qr_codes",fetchedQrCodes.getJSONArray(ctr).getJSONObject(0).get("id_qr_codes").toString());
                        qrMap.put("item",fetchedQrCodes.getJSONArray(ctr).getJSONObject(1).get("item").toString());
                        allQrCodes.add(qrMap);
                    }

                    //
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if(actionTaken == "viewBorrowed"){
                Intent intent = new Intent(admin.this, UserBorrowedItems.class);
                startActivity(intent);
            }else if(actionTaken == "viewReturned"){
                Intent userReturnedItem = new Intent(admin.this, UserReturnedItem.class);
                startActivity(userReturnedItem);
            }else if(actionTaken == "allLogs" || actionTaken == "allLogsHistory"){
                Intent int1= new Intent(admin.this,AllLogs.class);
                startActivity(int1);
            }else if(actionTaken == "allUsers"){
                Intent int1= new Intent(admin.this,AllUsers.class);
                startActivity(int1);
            }else if (actionTaken == "registerCode"){
                toastMessage(message);
            }else if(actionTaken == "allQrCode"){
                Intent qrClass = new Intent(admin.this,AllQrCode.class);
                startActivity(qrClass);
            }
            pDialog.hide();
            adminLayout.setEnabled(true);
        }
    }

    //connect database


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

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    //for pop up

    LinearLayout layoutOfPopup;
    PopupWindow popupWindow;


    Button manageLogs;

    Button returnedButton;
    Button borrowButton;
    Button allLogsButton;
    Button historyButton;

    Button manageQrCodes;
    Button allQrCode;
    Button registerCode;


    Button cancelButton;

    TextView popupText;
    String cancel = "0";
    String returned = "1";
    String borrow = "2";
    String allLogs = "3";
    String history = "4";

    String allQrCodeId = "5";
    String registerCodeId = "6";


    public void init() {
        manageLogs = (Button) findViewById(R.id.manageLogs);
        manageQrCodes = (Button) findViewById(R.id.manageQrCodes);
        /*popupText = new TextView(this);*/

        // process buttons

        //manage logs buttons
        returnedButton = new Button(this);
        returnedButton.setText("Returned");
        returnedButton.setId(Integer.valueOf(returned));
        returnedButton.setTextColor(getResources().getColor(R.color.white));
        returnedButton.setBackgroundColor(getResources().getColor(R.color.light_orange));
        //returnedButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.logs,0);
        LinearLayout.LayoutParams returnedButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        returnedButtonLayout.setMargins(20, 20, 20, 20);
        returnedButton.setLayoutParams(returnedButtonLayout);

        borrowButton = new Button(this);
        borrowButton.setText("Borrowed");
        borrowButton.setId(Integer.valueOf(borrow));
        borrowButton.setTextColor(getResources().getColor(R.color.white));
        borrowButton.setBackgroundColor(getResources().getColor(R.color.light_orange));
        //borrowButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.logs,0);
        LinearLayout.LayoutParams borrowButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        borrowButtonLayout.setMargins(20, 20, 20, 20);
        borrowButton.setLayoutParams(borrowButtonLayout);

        allLogsButton = new Button(this);
        allLogsButton.setText("All logs");
        allLogsButton.setId(Integer.valueOf(allLogs));
        allLogsButton.setTextColor(getResources().getColor(R.color.white));
        allLogsButton.setBackgroundColor(getResources().getColor(R.color.light_orange));
        //allLogsButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.logs,0);
        LinearLayout.LayoutParams allLogsButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        allLogsButtonLayout.setMargins(20, 20, 20, 20);
        allLogsButton.setLayoutParams(allLogsButtonLayout);

        historyButton = new Button(this);
        historyButton.setText("Log History");
        historyButton.setId(Integer.valueOf(history));
        historyButton.setTextColor(getResources().getColor(R.color.white));
        historyButton.setBackgroundColor(getResources().getColor(R.color.light_orange));
        //historyButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.logs,0);
        LinearLayout.LayoutParams historyButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        historyButtonLayout.setMargins(20, 20, 20, 20);
        historyButton.setLayoutParams(historyButtonLayout);

        //manage logs buttons

        //manage Qr codes

        allQrCode = new Button(this);
        allQrCode.setText("All QrCodes");
        allQrCode.setId(Integer.valueOf(allQrCodeId));
        allQrCode.setTextColor(getResources().getColor(R.color.white));
        allQrCode.setBackgroundColor(getResources().getColor(R.color.light_orange));
        //allQrCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.multiple_qr,0);
        LinearLayout.LayoutParams allQrCodeButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        allQrCodeButtonLayout.setMargins(20, 20, 20, 20);
        allQrCode.setLayoutParams(allQrCodeButtonLayout);

        registerCode = new Button(this);
        registerCode.setText("Register QrCode");
        registerCode.setId(Integer.valueOf(registerCodeId));
        registerCode.setTextColor(getResources().getColor(R.color.white));
        registerCode.setBackgroundColor(getResources().getColor(R.color.light_orange));
        //registerCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.qr_code,0);
        LinearLayout.LayoutParams registerCodeButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        registerCodeButtonLayout.setMargins(20, 20, 20, 20);
        registerCode.setLayoutParams(registerCodeButtonLayout);
        //manage qr codes


        cancelButton = new Button(this);
        cancelButton.setId(Integer.valueOf(cancel));
        cancelButton.setText("Cancel");
        cancelButton.setTextColor(getResources().getColor(R.color.white));
        cancelButton.setBackgroundColor(getResources().getColor(R.color.light_orange));

        LinearLayout.LayoutParams cancelButtonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        cancelButtonLayout.setMargins(20, 20, 20, 20);
        cancelButton.setLayoutParams(cancelButtonLayout);
        // end of process buttons

        layoutOfPopup = new LinearLayout(this);



       /* popupText.setPadding(0, 0, 0, 20);*/

        layoutOfPopup.setBackgroundColor(getResources().getColor(R.color.light_teal));
        layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
       /* layoutOfPopup.addView(popupText);*/

    }

    public void popupInit() {
        manageLogs.setOnClickListener(this);
        manageQrCodes.setOnClickListener(this);

        //manage logs buttons
        returnedButton.setOnClickListener(this);
        borrowButton.setOnClickListener(this);
        allLogsButton.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        //manage logs buttons

        //manage QR codes
        allQrCode.setOnClickListener(this);
        registerCode.setOnClickListener(this);
        //manage QR codes

        cancelButton.setOnClickListener(this);

        popupWindow = new PopupWindow(layoutOfPopup, ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(layoutOfPopup);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.manageLogs) {
            // blurredLayout.setVisibility(View.VISIBLE);
            adminLayout.setAlpha(Float.valueOf("0.10"));
            adminLayout.setEnabled(false);

            //mange logs
            layoutOfPopup.removeAllViews();
            layoutOfPopup.addView(returnedButton);
            layoutOfPopup.addView(borrowButton);
            layoutOfPopup.addView(allLogsButton);
            layoutOfPopup.addView(historyButton);
            layoutOfPopup.addView(cancelButton);

            popupWindow.showAsDropDown(manageLogs, 0, 0);

        }else if (v.getId() == R.id.manageQrCodes) {
            adminLayout.setAlpha(Float.valueOf("0.10"));
            adminLayout.setEnabled(false);
            layoutOfPopup.removeAllViews();
            layoutOfPopup.addView(allQrCode);
            layoutOfPopup.addView(registerCode);
            layoutOfPopup.addView(cancelButton);
            popupWindow.showAsDropDown(manageQrCodes, 0, 0);
        }

        else if (v.getId() == Integer.valueOf(returned)) {
            actionTaken="viewReturned";
            adminLayout.setEnabled(false);
            ConnectToDataBaseViaJson fetchReturned =  new ConnectToDataBaseViaJson();
            fetchReturned.execute();
        }
        else if(v.getId() == Integer.valueOf(borrow)){
            actionTaken="viewBorrowed";
            adminLayout.setEnabled(false);
            ConnectToDataBaseViaJson fetchBorrowed = new ConnectToDataBaseViaJson();
            fetchBorrowed.execute();
        }
        else if(v.getId() == Integer.valueOf(allLogs)){
            actionTaken = "allLogs";
            adminLayout.setEnabled(false);
            ConnectToDataBaseViaJson fetchAllLogs = new ConnectToDataBaseViaJson();
            fetchAllLogs.execute();
        }
        else if(v.getId() == Integer.valueOf(history)){
            actionTaken = "allLogsHistory";
            adminLayout.setEnabled(false);
            ConnectToDataBaseViaJson fetchAllLogs = new ConnectToDataBaseViaJson();
            fetchAllLogs.execute();
        }
        else if (v.getId() == Integer.valueOf(cancel)) {
            popupWindow.dismiss();
            //  blurredLayout.setVisibility(View.GONE);
            adminLayout.setEnabled(true);
            adminLayout.setAlpha(Float.valueOf("1"));
        }
        else if (v.getId() == Integer.valueOf(allQrCodeId)) {
            actionTaken = "allQrCode";
            adminLayout.setEnabled(false);
            ConnectToDataBaseViaJson fetchAllQrCode= new ConnectToDataBaseViaJson();
            fetchAllQrCode.execute();
        }
        else if (v.getId() == Integer.valueOf(registerCodeId)) {
            actionTaken = "registerCode";
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }

    }

    // end for pop up

}