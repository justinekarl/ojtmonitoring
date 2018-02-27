package com.example.ojtmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jomer.filetracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherLoginActivity extends AppCompatActivity {

    private Button logoutBtn;
    private TextView welcomeTeacherLbl;

    private static String name;
    private static int agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        logoutBtn = (Button)findViewById(R.id.logoutBtn);

        welcomeTeacherLbl = (TextView)findViewById(R.id.welcomeTeacherLbl);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        name=sharedPreferences.getString("full_name","");


        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");


        welcomeTeacherLbl.setText("Logged In User : " +name +" - " +sd.format(new Date().getTime()));


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
                        Intent login = new Intent(TeacherLoginActivity.this, Login.class);
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



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0,0,Menu.NONE,"Show Ojt Requests");

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Intent showOjtRequest = new Intent(TeacherLoginActivity.this,ShowOJTApplicationsActivity.class);
                startActivity(showOjtRequest);
                return true;
            case 1:
                Intent addResume = new Intent(TeacherLoginActivity.this,CreateUpdateResumeActivity.class);
                startActivity(addResume);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
