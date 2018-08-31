package com.example.ojtmonitoring;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CoordinatorLoginActivity extends AppCompatActivity {
    private TextView welcomeLbl;
    private String name;
    private static int agentId;
    private Button logoutBtn;
    private ListView contentLstVw;
    final String[] menuItems = {"Scan Student QR Codes","Show student login/logout","Print Weekly Report"};
    final int[] menuImage = {R.mipmap.ic_scan_qr,R.mipmap.ic_view,R.mipmap.ic_list};
    ListAdapter menuAdapter;
    private CustomMenuAdapter customMenuAdapter;

    ScrollView scrollView;
    private Button logoutTopBtn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,CoordinatorLoginActivity.class);
        startActivity(home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_login);
        PaceSettingManager.lockActivityOrientation(this);

        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        contentLstVw = (ListView)findViewById(R.id.contentLstVw);

        scrollView = (ScrollView)findViewById(R.id.scrollViewCoor);

        logoutTopBtn=(Button)findViewById(R.id.logoutTopBtn);


        /*menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuItems){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);

                TextView tv = (TextView)view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);
                tv.setTypeface(Typeface.DEFAULT_BOLD);

                return view;
            }
        };*/

        customMenuAdapter = new CustomMenuAdapter(this,  menuItems, menuImage);
        contentLstVw.setAdapter(customMenuAdapter);

        //allowing vertical scroll even in scroll view
        contentLstVw.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                v.onTouchEvent(event);
                return true;
            }
        });



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

        logoutTopBtn.setOnTouchListener(
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
                            case 2:
                                Intent printReport = new Intent(CoordinatorLoginActivity.this,PrintReportActivity.class);
                                startActivity(printReport);
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
