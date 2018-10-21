package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateCoordinatorAccountActivity extends AppCompatActivity {

    private EditText coorNameTxt;
    private EditText coorPhoneTxt;
    private EditText coorAddressTxt;
    private Spinner companySpnr;
    private EditText coorUsernameTxt;
    private EditText coorConfirmPassTxt;
    private EditText coorPasswordTxt;
    private Button btnSaveCoor;
    private Button btnCancelCoor;
    private Button refreshCompanyNameBtn;

    private String coorName;
    private String coorPhone;
    private String coorAddress;
    private String companyName;
    private String username;
    private String confirmPassword;
    private String password;


    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private String[] companyNames;

    ArrayAdapter<String> companyNameAdapter = null;
    private ArrayList<String> companyNameList = new ArrayList<>();

    public static boolean registrationSuccessful;
    public static String registrationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coordinator_account);

        coorNameTxt = (EditText)findViewById(R.id.coorNameTxt);
        coorPhoneTxt = (EditText)findViewById(R.id.coorPhoneTxt);
        coorAddressTxt = (EditText)findViewById(R.id.coorAddressTxt);
        companySpnr = (Spinner)findViewById(R.id.companySpnr);
        coorUsernameTxt = (EditText)findViewById(R.id.coorUsernameTxt);
        coorConfirmPassTxt = (EditText)findViewById(R.id.coorConfirmPassTxt);
        coorPasswordTxt = (EditText)findViewById(R.id.coorPasswordTxt);
        btnSaveCoor = (Button)findViewById(R.id.btnSaveCoor);
        btnCancelCoor = (Button)findViewById(R.id.btnCancelCoor);
        refreshCompanyNameBtn = (Button)findViewById(R.id.refreshCompanyNameBtn);

        refreshCompanyNameBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RetrieveCompanyName retrieveCompanyName = new RetrieveCompanyName();
                        retrieveCompanyName.execute();
                    }
                }
        );

        refreshCompanyNameBtn.callOnClick();

        btnCancelCoor.setOnTouchListener(
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
                                Intent backToSelectUserTypeIntent = new Intent(CreateCoordinatorAccountActivity.this,AccountCreationSelectionActivity.class) ;
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

        btnSaveCoor.setOnTouchListener(
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
                                coorName = null != coorNameTxt.getText() ? coorNameTxt.getText().toString() : "";
                                coorPhone =  null != coorPhoneTxt.getText() ? coorPhoneTxt.getText().toString() : "";
                                coorAddress = null != coorAddressTxt.getText() ? coorAddressTxt.getText().toString() : "";
                                companyName = null != companySpnr.getSelectedItem() ?  companySpnr.getSelectedItem().toString() : "";
                                username = null != coorUsernameTxt.getText() ? coorUsernameTxt.getText().toString() : "";
                                password = null != coorPasswordTxt.getText() ? coorPasswordTxt.getText().toString() : "";
                                confirmPassword = null != coorConfirmPassTxt.getText() ? coorConfirmPassTxt.getText().toString() : "";

                                if(coorName.trim().length() == 0
                                        || coorPhone.trim().length() == 0
                                        || coorAddress.trim().length() == 0
                                        || username.trim().length() == 0
                                        || password.trim().length()== 0
                                        ||  confirmPassword.trim().length() == 0
                                        || companyName.trim().length() == 0){
                                    toastMessage("All fields are required!");
                                }else{
                                    if(password.length() >= 5){
                                        if(password.equals(confirmPassword)){
                                            ProcessAddCoordinator processAddCoordinator = new ProcessAddCoordinator();
                                            processAddCoordinator.execute();
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

    class RetrieveCompanyName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(CreateCoordinatorAccountActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public RetrieveCompanyName() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",0+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveCompanyNames.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

                        if(json.has("company_names")){
                            /*JSONArray jsonArray = json.getJSONArray("company_names");
                            if(null != jsonArray){
                                companyNames= new String[json.length()];
                                for(int ctr = 0;  ctr < jsonArray.length() ; ctr++){
                                    for(int i = 0 ; i <= jsonArray.getJSONArray(ctr).length()-1 ; i++) {
                                        String[] row = null;
                                        if(null != jsonArray.getJSONArray(ctr) && (jsonArray.getJSONArray(ctr).get(i) + "").contains("~")) {
                                              = (jsonArray.getJSONArray(ctr).get(i) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("name")){
                                                    companyNames[i] = value;
                                                    companyNameList.add(value);
                                                }

                                            }
                                        }
                                    }
                                }
                            }*/

                            JSONArray jsonArray = json.getJSONArray("company_names");
                            if(null != jsonArray){
                                companyNames= new String[jsonArray.length()];
                                for(int ctr = 0;  ctr < jsonArray.length() ; ctr++) {
                                    if(ctr==0){
                                        companyNames[ctr] = "---Select Company---";
                                        continue;
                                    }
                                    for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {
                                        if(null != jsonArray.getJSONArray(ctr) && i==1) {
                                            companyNames[ctr] = jsonArray.getJSONArray(ctr).get(i).toString();
                                            break;
                                        }

                                    }
                                }
                            }
                        }

                    }else {
                        if(null != json.getString("company_names")){

                        }
                    }

                }else{
                    //loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(null != companyNames && companyNames.length > 0) {

                companyNameAdapter = new ArrayAdapter<String>(CreateCoordinatorAccountActivity.this, android.R.layout.simple_list_item_1, companyNames){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position,convertView,parent);
                        if(position%2 == 0)
                        {
                            view.setBackgroundColor(getResources().getColor(R.color.divider));
                        }else{
                            view.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        return view;
                    }
                };

                /*int selectedId = 0;
                for(int i =0;i<sectionNames.length;i++){
                    if(sectionNames[i].equals(savedSectionName)){
                        selectedId = i;
                    }
                }*/
                companySpnr.setAdapter(companyNameAdapter);
                //sectionSpnr.setSelection(selectedId);
            }

        }
    }

    class ProcessAddCoordinator extends AsyncTask<String, String, String>{
        /**
         * Before starting background_light thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateCoordinatorAccountActivity.this);
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
            String sign_up_full_name = coorName;
            String sign_up_address = coorAddress;
            String sign_up_phone = coorPhone;
            String sign_up_company_name = companyName;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", sign_up_user_name));
            params.add(new BasicNameValuePair("password", sign_up_password));
            params.add(new BasicNameValuePair("company_name", sign_up_company_name));
            params.add(new BasicNameValuePair("full_name", sign_up_full_name));
            params.add(new BasicNameValuePair("admin_password", "123456"));
            params.add(new BasicNameValuePair("address",sign_up_address));
            params.add(new BasicNameValuePair("phonenumber",sign_up_phone));
            params.add(new BasicNameValuePair("accounttype","4"));


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
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
           /* // dismiss the dialog once done*/

            if(registrationSuccessful){

                toastMessage(registrationMessage);
                Intent int1 = new Intent(CreateCoordinatorAccountActivity.this, Login.class);
                startActivity(int1);
            }else{
                toastMessage(registrationMessage);
            }


        }
    }

}
