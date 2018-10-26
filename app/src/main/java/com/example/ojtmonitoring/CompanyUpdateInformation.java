package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompanyUpdateInformation extends AppCompatActivity {

    private EditText companyTxt;
    private EditText addressTxt;
    private EditText phoneNumberTxt;
    private EditText companyTypeTxt;
    private EditText descriptionTxt;
    private RadioButton moaYesRad;
    private RadioButton moaNoRad;
    private Button updateBtn;
    private Button cancelBtn;
    private static int companyId;
    private static String name;
    private static int accountType;
    private static String address;
    private static String phoneNumber;
    private static String department;
    private static String companyType;

    private static boolean moaCertified;
    private static String description;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;


    private static String newCompanyName ;
    private static String newCompanyType;
    private static String newAddress ;
    private static String newPhoneNumber ;
    private static String newDescription ;
    private static boolean isMoaCertified ;
    private static String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_update_information);
        PaceSettingManager.lockActivityOrientation(this);

        companyTxt = (EditText)findViewById(R.id.companyTxt);
        addressTxt = (EditText)findViewById(R.id.custAddressTxt);
        phoneNumberTxt = (EditText)findViewById(R.id.custPhoneNumberTxt);
        companyTypeTxt = (EditText)findViewById(R.id.companyTypeTxt);
        descriptionTxt = (EditText)findViewById(R.id.custDescriptionTxt);
        moaYesRad = (RadioButton)findViewById(R.id.moaYesRad);
        moaNoRad = (RadioButton)findViewById(R.id.moaNoRad);

        updateBtn = (Button) findViewById(R.id.updateBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        companyId = sharedPreferences.getInt("agent_id",0);
        /*name=sharedPreferences.getString("full_name","");
        accountType = sharedPreferences.getInt("accounttype",0);

        address = sharedPreferences.getString("address","");
        phoneNumber = sharedPreferences.getString("phoneNumber","");
        department = sharedPreferences.getString("department","");

        companyTypeTxt.setText(name);
        addressTxt.setText(address);
        phoneNumberTxt.setText(phoneNumber);
        companyTypeTxt.setText(department);*/

        CompanyUpdateInformation.ConnectToDataBaseViaJson retriveCompany = new CompanyUpdateInformation.ConnectToDataBaseViaJson();
        retriveCompany.execute();

        description = null != descriptionTxt.getText() ? descriptionTxt.getText().toString(): "";
        companyType = null != companyTypeTxt.getText() ? companyTypeTxt.getText().toString() : "";


        moaYesRad.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(moaYesRad.isChecked()){
                            moaCertified = true;
                            moaNoRad.setChecked(false);
                        }
                    }
                }
        );


        moaNoRad.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(moaNoRad.isChecked()){
                            moaCertified = false;
                            moaYesRad.setChecked(false);
                        }
                    }
                }
        );


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
                                Intent backToCompanyPage = new Intent(CompanyUpdateInformation.this,CompanyNavigationActivity.class);
                                backToCompanyPage.putExtra("currentModuleSelected","Home");
                                startActivity(backToCompanyPage);
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

        updateBtn.setOnTouchListener(
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
                                newCompanyName = companyTxt.getText().toString();
                                newCompanyType = companyTypeTxt.getText().toString();
                                newAddress = addressTxt.getText().toString();
                                newPhoneNumber = phoneNumberTxt.getText().toString();
                                newDescription = descriptionTxt.getText().toString();
                                isMoaCertified = moaYesRad.isChecked();
                                  CompanyUpdateInformation.UpdateCompanyViaJson update = new CompanyUpdateInformation.UpdateCompanyViaJson();
                                  update.execute();

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


    //connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompanyUpdateInformation.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String agentId = companyId+"";
            String accounType = accountType+"";



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("accountType", accounType));
            params.add(new BasicNameValuePair("agentid",agentId));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveUserAccounts.php",
                    "POST", params);


            try {
                if(null != json){
                    int success = json.getInt("success");
                    if(success == 1) {

                        companyId = json.getInt("id");

                        name = json.getString("name");
                        accountType = json.getInt("accounttype");
                        address = json.getString("address");
                        phoneNumber = json.getString("phonenumber");
                        department = json.getString("department");
                        description = json.getString("description");
                        String moaCer = json.getString("moa_certified");
                        moaCertified = (moaCer.equals("1") ? true : false);



                    }else {
                        if(null != json.getString("message")){
                         //   loginMessage=json.getString("message");
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
            if(companyId > 0) {
                companyTxt.setText(null != name && !name.equals("null") ? name : "");
                companyTypeTxt.setText(null != department && !department.equals("null") ?  department :"");
                addressTxt.setText(null != address && !address.equals("null") ? address : "");
                phoneNumberTxt.setText(null != phoneNumber && !phoneNumber.equals("null") ? phoneNumber : "");
                companyTypeTxt.setText(null != department && !department.equals("null") ? department : "");
                descriptionTxt.setText(null != description && !description.equals("null") ? description : "");
                if(moaCertified) {
                    moaYesRad.setChecked(true);
                    moaNoRad.setChecked(false);
                }else {
                    moaNoRad.setChecked(true);
                    moaYesRad.setChecked(false);
                }


            }
        }
    }
    //end of connecting


    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }




    //connecting to the database

    class UpdateCompanyViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompanyUpdateInformation.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        protected String doInBackground(String... args) {
            String agentId = companyId+"";
            String accounType = accountType+"";

            String newCompanyNameToUse = newCompanyName;
            String newCompanyTypeToUse = newCompanyType;
            String newAddressToUse = newAddress;
            String newPhoneNumberToUse = newPhoneNumber;
            String newDescriptionToUse = newDescription;
            boolean isMoaCertifiedToUse = isMoaCertified;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("newCompanyName", newCompanyNameToUse));
            params.add(new BasicNameValuePair("newCompanyType", newCompanyTypeToUse));
            params.add(new BasicNameValuePair("newAddress", newAddressToUse));
            params.add(new BasicNameValuePair("newPhoneNumber", newPhoneNumberToUse));
            params.add(new BasicNameValuePair("newDescription", newDescriptionToUse));
            params.add(new BasicNameValuePair("isMoaCertified",isMoaCertifiedToUse+""));

            params.add(new BasicNameValuePair("accounttype",accounType));

            params.add(new BasicNameValuePair("agentid",agentId));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"updateCompany.php",
                    "POST", params);


            try {
                if(null != json){
                    int success = json.getInt("success");
                    if(success == 1) {

                        SharedPreferences sharedpreferences = getSharedPreferences(
                                PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.remove("full_name");
                        editor.putString("full_name",newCompanyNameToUse);

                        editor.putString("company_description",newDescriptionToUse);

                        editor.commit();

                        message = "Successfully Updated.";
                    }else {
                        if(null != json.getString("message")){
                            //   loginMessage=json.getString("message");
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
            if(companyId > 0) {
                companyTxt.setText(newCompanyName);
                companyTypeTxt.setText(newCompanyType);
                addressTxt.setText(newAddress);
                phoneNumberTxt.setText(newPhoneNumber);
                descriptionTxt.setText(newDescription);
                if(moaCertified) {
                    moaYesRad.setChecked(true);
                    moaNoRad.setChecked(false);
                }else {
                    moaNoRad.setChecked(true);
                    moaYesRad.setChecked(false);
                }

                toastMessage(message);
            }
        }
    }
    //end of connecting


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,CompanyNavigationActivity.class);
        startActivity(home);
        finish();
    }
}
