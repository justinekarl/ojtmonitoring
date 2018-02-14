package com.example.ojtmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jomer.filetracker.R;

public class StudentLoginActivity extends AppCompatActivity {

    private static String name;
    private static int agentId;
    private static int accountType;

    private TextView welcomeLbl;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        name=sharedPreferences.getString("full_name","");
        accountType = sharedPreferences.getInt("accounttype",0);

        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        String accountTypeStr = "";
        if(accountType == 1){
            accountTypeStr = "Student";
        }

        if(accountType == 2){
            accountTypeStr = "Teacher";
        }

        if(accountType == 3){
            accountTypeStr = "Company";
        }
        welcomeLbl.setText("Welcome " +name +" - " +accountTypeStr);

        logoutBtn = (Button)findViewById(R.id.logoutBtn);

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
                            Intent login = new Intent(StudentLoginActivity.this, Login.class);
                            startActivity(login);
                        //}else{
                         //   finishAffinity();
                        //}
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });

        logoutBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
        );

    }


}
