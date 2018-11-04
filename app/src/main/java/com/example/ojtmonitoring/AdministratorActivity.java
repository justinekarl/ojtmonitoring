package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdministratorActivity extends AppCompatActivity {
    Button topLogoutBtn,bottomLogoutBtn,teacherBtn,companyBtn,studentBtn,supervisorBtn,resetAllBtn;
    TextView welcomeLbl,adminNameTxt;
    ListView menuLstView;
    String name;
    int agentId;

    String currentSelectedModule = "";

    private ProgressDialog pDialog;
    private CustomMenuAdapter customMenuAdapter;

    final String[] studentMenuOptions = {"Student Accounts"};
    final int[] studentMenuImages = {R.mipmap.ic_list};

    final String[] teacherMenuOptions = {"Teacher Accounts"};
    final int[] teacherMenuImages = {R.mipmap.ic_list};

    final String[] companyMenuOptions = {"Company Accounts"};
    final int[] companyMenuImages = {R.mipmap.ic_list};

    final String[] supervisorMenuOptions = {"Company Supervisor Accounts"};
    final int[] supervisorMenuImages = {R.mipmap.ic_list};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        topLogoutBtn = (Button)findViewById(R.id.topLogoutBtn);
        bottomLogoutBtn = (Button)findViewById(R.id.bottomLogoutBtn);
        teacherBtn = (Button)findViewById(R.id.teacherBtn);
        companyBtn = (Button)findViewById(R.id.companyBtn);
        studentBtn = (Button)findViewById(R.id.studentBtn);
        supervisorBtn = (Button)findViewById(R.id.supervisorBtn);
        resetAllBtn = (Button)findViewById(R.id.resetAllBtn);


        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        adminNameTxt = (TextView)findViewById(R.id.adminNameTxt);

        menuLstView = (ListView)findViewById(R.id.menuLstView);

        SharedPreferences sharedpreferences = getSharedPreferences(
                PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
        name=sharedpreferences.getString("full_name","");
        agentId = sharedpreferences.getInt("agent_id",0);


        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");

        welcomeLbl.setText("Logged In User : " +name +" \n " +sd.format(new Date().getTime()));
        adminNameTxt.setText(sharedpreferences.getString("full_name",""));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to continue with your action?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if(actionTaken == "logout"){
                        SharedPreferences preferences =getSharedPreferences(PaceSettingManager.USER_PREFERENCES,MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        Intent login = new Intent(AdministratorActivity.this, Login.class);
                        startActivity(login);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog.Builder resetBuilder = new AlertDialog.Builder(this);
        resetBuilder.setCancelable(true);
        resetBuilder.setTitle("Reset Action Confirmation");
        resetBuilder.setMessage("This action will remove all existing accounts, Are you sure you want to reset All accounts?");
        resetBuilder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if(actionTaken == "logout"){
                      new AsyncTask<Void,String,String>(){
                          @Override
                          protected void onPreExecute() {
                              super.onPreExecute();
                              pDialog = new ProgressDialog(AdministratorActivity.this);
                              pDialog.setMessage("Processing..");
                              pDialog.setIndeterminate(false);

                              pDialog.setCancelable(true);
                          }
                          @Override
                          protected String doInBackground(Void... params) {
                              JSONParser jsonParser1 = new JSONParser();
                              List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                              JSONObject json = jsonParser1.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "resetAllAccounts.php",
                                      "POST", params1);
                              try {
                                  if (null != json) {

                                      // check log cat fro response
                                      Log.d("Create Response", json.toString());

                                  } else {
                                  }

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                              return null;
                          }

                          @Override
                          protected void onPostExecute(String s) {
                              super.onPostExecute(s);
                              pDialog.dismiss();
                              PaceSettingManager.toastMessage(AdministratorActivity.this,"Reset Completed!");
                          }
                      }.execute();
                    }
                });
        resetBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });



        teacherBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        currentSelectedModule = "Teacher";

                        /*customMenuAdapter = new CustomMenuAdapter(AdministratorActivity.this,  teacherMenuOptions, teacherMenuImages);
                        menuLstView.setAdapter(customMenuAdapter);*/

                        teacherBtn.setBackgroundColor(Color.GRAY);
                        /*studentBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        companyBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        supervisorBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/

                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Teacher");
                        startActivity(home1);

                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        studentBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        currentSelectedModule = "Student";

                        /*customMenuAdapter = new CustomMenuAdapter(AdministratorActivity.this,  studentMenuOptions, studentMenuImages);
                        menuLstView.setAdapter(customMenuAdapter);*/

                        //teacherBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        studentBtn.setBackgroundColor(Color.GRAY);
                        /*companyBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        supervisorBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/
                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Student");
                        startActivity(home1);

                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        companyBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        currentSelectedModule = "Company";

//                        customMenuAdapter = new CustomMenuAdapter(AdministratorActivity.this,  companyMenuOptions, companyMenuImages);
//                        menuLstView.setAdapter(customMenuAdapter);

                        /*teacherBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        studentBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/
                        companyBtn.setBackgroundColor(Color.GRAY);
                        //supervisorBtn.setBackgroundColor(Color.parseColor("#3088AA"));

                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Company");
                        startActivity(home1);

                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        supervisorBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        currentSelectedModule = "Supervisor";

                        /*customMenuAdapter = new CustomMenuAdapter(AdministratorActivity.this,  supervisorMenuOptions, supervisorMenuImages);
                        menuLstView.setAdapter(customMenuAdapter);*/

                      /*  teacherBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        studentBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        companyBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/
                        supervisorBtn.setBackgroundColor(Color.GRAY);
                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Supervisor");
                        startActivity(home1);


                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        resetAllBtn.setOnTouchListener(
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
                                AlertDialog dialog = resetBuilder.create();
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

        topLogoutBtn.setOnTouchListener(
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

        bottomLogoutBtn.setOnTouchListener(
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


        menuLstView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMenu = String.valueOf(parent.getItemAtPosition(position));
                        getMenuSelected(currentSelectedModule,position);

                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,AdministratorActivity.class);
        startActivity(home);
    }

    private void getMenuSelected(final String entityType, final int position) {
        if (null != entityType && entityType.trim().length() > 0) {
            if(entityType.equals("Student")){
                switch (position){
                    case 0:
                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Student");
                        startActivity(home1);
                        return;
                    default:
                        Intent home = new Intent(AdministratorActivity.this,AdministratorActivity.class);
                        startActivity(home);
                }
            }

            if(entityType.equals("Company")){
                switch (position){
                    case 0:
                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Company");
                        startActivity(home1);
                        return;
                    default:
                        Intent home = new Intent(AdministratorActivity.this,AdministratorActivity.class);
                        startActivity(home);
                }
            }

            if(entityType.equals("Teacher")){
                switch (position){
                    case 0:
                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Teacher");
                        startActivity(home1);
                        return;
                    default:
                        Intent home = new Intent(AdministratorActivity.this,AdministratorActivity.class);
                        startActivity(home);
                }
            }

            if(entityType.equals("Supervisor")){
                switch (position){
                    case 0:
                        Intent home1 = new Intent(AdministratorActivity.this,UserAccountsActivity.class);
                        home1.putExtra("accountType","Supervisor");
                        startActivity(home1);
                        return;
                    default:
                        Intent home = new Intent(AdministratorActivity.this,AdministratorActivity.class);
                        startActivity(home);
                }
            }
        }
    }
}
