package com.example.ojtmonitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jomer.filetracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CoordinatorLoginActivity extends AppCompatActivity {
    private TextView welcomeLbl;
    private String name;
    private static int agentId;
    private Button logoutBtn;
    private ListView contentLstVw;
    final String[] menuItems = {"Scan Student QR Codes","Show student login/logout"};
    ListAdapter menuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_login);

        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        contentLstVw = (ListView)findViewById(R.id.contentLstVw);


        menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuItems);
        contentLstVw.setAdapter(menuAdapter);


        SharedPreferences sharedpreferences = getSharedPreferences(
                PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
        name=sharedpreferences.getString("full_name","");
        agentId = sharedpreferences.getInt("agent_id",0);

        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");

        welcomeLbl.setText("Logged In User : " +name +" - User id: "+agentId +" \n " +sd.format(new Date().getTime()));

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
                        Intent login = new Intent(CoordinatorLoginActivity.this, Login.class);
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

        contentLstVw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMenu = String.valueOf(parent.getItemAtPosition(position));
                        switch (position){
                            case 0:
                                Intent scanQr = new Intent(CoordinatorLoginActivity.this,AttendanceCheckerMainActivity.class);
                                startActivity(scanQr);
                                return;
                            case 1:
                                Intent showStudentLoginLogoutPage= new Intent(CoordinatorLoginActivity.this,ShowStudentLoginLogoutActivity.class);
                                startActivity(showStudentLoginLogoutPage);
                                return;
                            default:
                                Intent backToHome = new Intent(CoordinatorLoginActivity.this,CoordinatorLoginActivity.class);
                                startActivity(backToHome);

                        }

                    }
                }
        );

    }
}
