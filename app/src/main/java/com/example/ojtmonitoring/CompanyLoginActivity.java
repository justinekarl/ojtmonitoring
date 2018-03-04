package com.example.ojtmonitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jomer.filetracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompanyLoginActivity extends AppCompatActivity {

    private Button logoutBtn;
    private static  String companyName ;
    private TextView companyNameTxt;
    private TextView descriptionTxt;
    private TextView welcomeLbl;
    private String name;
    private static int agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        companyNameTxt = (TextView)findViewById(R.id.custCompanyNameTxt);
        descriptionTxt = (TextView)findViewById(R.id.custDescriptionTxt);
        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);


        SharedPreferences sharedpreferences = getSharedPreferences(
                PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
        name=sharedpreferences.getString("full_name","");
        agentId = sharedpreferences.getInt("agent_id",0);

        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");

        welcomeLbl.setText("Logged In User : " +name +" - Company id: "+agentId +" \n " +sd.format(new Date().getTime()));
        companyNameTxt.setText(sharedpreferences.getString("full_name",""));
        descriptionTxt.setText(sharedpreferences.getString("company_description",""));

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
                        Intent login = new Intent(CompanyLoginActivity.this, Login.class);
                        startActivity(login);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        logoutBtn.setOnTouchListener(
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.updateinformation:
                Intent updateInfoIntent = new Intent(CompanyLoginActivity.this,CompanyUpdateInformation.class);
                startActivity(updateInfoIntent);
                return true;
            case  R.id.addupdatereq:
                Intent addUpdateReq = new Intent(CompanyLoginActivity.this,AddUpdateCompanyRequirements.class);
                startActivity(addUpdateReq);
                return true;
            case R.id.showojtlist:
                Intent ojtList = new Intent(CompanyLoginActivity.this,ShowOJTListActivity.class);
                startActivity(ojtList);
                return true;
            case R.id.scanstudentqrcodest:
                Intent scanQr = new Intent(CompanyLoginActivity.this,AttendanceCheckerMainActivity.class);
                startActivity(scanQr);
                return true;
            case R.id.showloginlogout:
                Intent showStudentLoginLogoutPage= new Intent(CompanyLoginActivity.this,ShowStudentLoginLogoutActivity.class);
                startActivity(showStudentLoginLogoutPage);
                return true;
            default:
                Intent backToHome = new Intent(CompanyLoginActivity.this,CompanyLoginActivity.class);
                startActivity(backToHome);
        }

        return super.onOptionsItemSelected(item);
    }
}
