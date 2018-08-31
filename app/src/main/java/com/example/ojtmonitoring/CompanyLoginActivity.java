package com.example.ojtmonitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.Socket;

public class CompanyLoginActivity extends AppCompatActivity {

    private Button logoutBtn;
    private Button signouttopBtn;
    private static  String companyName ;
    private TextView companyNameTxt;
    private TextView descriptionTxt;
    private TextView welcomeLbl;
    private String name;
    private static int agentId;

    final String[] menuItems = {"Update Information","Add/Update Requirements","Show OJT list","Scan Student QR Codes","Show student login/logout","Show Coordinator Request","Create Weekly Report","Rate Student","View Teachers"};
    int[] menuImage = {R.mipmap.ic_update,R.mipmap.ic_add_generic,R.mipmap.ic_view,R.mipmap.ic_scan_qr,R.mipmap.ic_list,R.mipmap.ic_pending,R.mipmap.ic_list,R.mipmap.ic_rate,R.mipmap.ic_view};
    ListAdapter menuAdapter;
    private ListView menuOptionsLstView;
    private CustomMenuAdapter customMenuAdapter;

    private Socket mSocket;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        PaceSettingManager.lockActivityOrientation(this);

        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        companyNameTxt = (TextView)findViewById(R.id.custCompanyNameTxt);
        signouttopBtn = (Button)findViewById(R.id.signouttopBtn);
        //descriptionTxt = (TextView)findViewById(R.id.custDescriptionTxt);
        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        menuOptionsLstView = (ListView)findViewById(R.id.menuOptionsLstView);

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
        menuOptionsLstView.setAdapter(customMenuAdapter);


        SharedPreferences sharedpreferences = getSharedPreferences(
                PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
        name=sharedpreferences.getString("full_name","");
        agentId = sharedpreferences.getInt("agent_id",0);
        userName = sharedpreferences.getString("user_name","");

        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");

        welcomeLbl.setText("Logged In User : " +name +" - Company id: "+agentId +" \n " +sd.format(new Date().getTime()));
        companyNameTxt.setText(sharedpreferences.getString("full_name",""));
        //descriptionTxt.setText(sharedpreferences.getString("company_description",""));


        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        // mSocket.on("adduser", onConnect);
        mSocket.connect();
        mSocket.emit("adduser", userName);

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

        signouttopBtn.setOnTouchListener(
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


        menuOptionsLstView.setOnTouchListener(new ListView.OnTouchListener() {
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


        menuOptionsLstView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMenu = String.valueOf(parent.getItemAtPosition(position));
                        switch (position){
                            case 0:
                                Intent updateInfoIntent = new Intent(CompanyLoginActivity.this,CompanyUpdateInformation.class);
                                startActivity(updateInfoIntent);
                                return;
                            case 1:
                                Intent addUpdateReq = new Intent(CompanyLoginActivity.this,AddUpdateCompanyRequirements.class);
                                startActivity(addUpdateReq);
                                return;
                            case 2:
                                Intent ojtList = new Intent(CompanyLoginActivity.this,ShowOJTListActivity.class);
                                startActivity(ojtList);
                                return;
                            case 3:
                                Intent scanQr = new Intent(CompanyLoginActivity.this,AttendanceCheckerMainActivity.class);
                                startActivity(scanQr);
                                return;
                            case 4:
                                Intent showStudentLoginLogoutPage= new Intent(CompanyLoginActivity.this,ShowStudentLoginLogoutActivity.class);
                                startActivity(showStudentLoginLogoutPage);
                                return;
                            case 5:
                                Intent coorReq = new Intent(CompanyLoginActivity.this,ShowCoordinatorRequestActivity.class);
                                startActivity(coorReq);
                                return;
                            case 6:
                                Intent printReport = new Intent(CompanyLoginActivity.this,PrintReportActivity.class);
                                startActivity(printReport);
                                return;
                            case 7:
                                Intent rateStudent = new Intent(CompanyLoginActivity.this,StudentListActivity.class);
                                startActivity(rateStudent);
                                return;
                            case 8:
                                Intent viewTeachers = new Intent(CompanyLoginActivity.this,ViewTeachersActivity.class);
                                startActivity(viewTeachers);
                                return;
                            default:
                                Intent backToHome = new Intent(CompanyLoginActivity.this,CompanyLoginActivity.class);
                                startActivity(backToHome);

                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,CompanyLoginActivity.class);
        startActivity(home);
    }
}
