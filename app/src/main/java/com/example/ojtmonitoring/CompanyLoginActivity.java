package com.example.ojtmonitoring;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;

public class CompanyLoginActivity extends AppCompatActivity {

    private Button logoutBtn;
    private Button signouttopBtn,studentModulesBtn,teacherModulesBtn,homeModulesBtn,supervisorModulesBtn;
    private static  String companyName ;
    private TextView companyNameTxt;
    private TextView descriptionTxt;
    private TextView welcomeLbl;
    private String name;
    private static int agentId;

    String currentModuleSelected = "Home";

    final String[] studentMenuItems = {"OJT list","Scan Student QR Codes","Student login/logout","Create Weekly Report","Rate Student","Student Weekly Practicum Report"};
    final int[] studentMenuImages = {R.mipmap.ic_view,R.mipmap.ic_scan_qr,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_rate,R.mipmap.ic_view};

    final String[] teacherMenuItems = {"Teachers"};
    final int[] teacherMenuImages = {R.mipmap.ic_list};

    final String[] supervisorMenuItems = {"Company Supervisor Request"};
    final int[] supervisorMenuImages = {R.mipmap.ic_view};

    final String[] homeMenuItems = {"Update Information","Add/Update Requirements"};
    final int[] homeMenuImages = {R.mipmap.ic_update,R.mipmap.ic_add_generic};

    final String[] menuItems = {"Update Information","Add/Update Requirements","OJT list","Scan Student QR Codes","Student login/logout","Company Supervisor Request","Create Weekly Report","Rate Student","Teachers","Student Weekly Practicum Report"};
    int[] menuImage = {R.mipmap.ic_update,R.mipmap.ic_add_generic,R.mipmap.ic_view,R.mipmap.ic_scan_qr,R.mipmap.ic_list,R.mipmap.ic_pending,R.mipmap.ic_list,R.mipmap.ic_rate,R.mipmap.ic_view,R.mipmap.ic_list};
    ListAdapter menuAdapter;
    //private ListView menuOptionsLstView;
    private CustomMenuAdapter customMenuAdapter;

    private Socket mSocket;
    private String userName;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    Intent backGround;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        PaceSettingManager.lockActivityOrientation(this);

        if(!isMessageServiceRunning()) {
            backGround = new Intent(this, BackgroundProcessService.class);
            startService(backGround);
        }

        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        companyNameTxt = (TextView)findViewById(R.id.custCompanyNameTxt);
        signouttopBtn = (Button)findViewById(R.id.signouttopBtn);
        //descriptionTxt = (TextView)findViewById(R.id.custDescriptionTxt);
        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
      //  menuOptionsLstView = (ListView)findViewById(R.id.menuOptionsLstView);

        studentModulesBtn = (Button)findViewById(R.id.studentModulesBtn);
        teacherModulesBtn = (Button)findViewById(R.id.teacherModulesBtn);
        supervisorModulesBtn = (Button)findViewById(R.id.supervisorModulesBtn);
        homeModulesBtn = (Button)findViewById(R.id.homeModulesBtn);

        /*menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuItems){
            @NonNull
            @OverrideStudentListActivity
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);

                TextView tv = (TextView)view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.parseColor("#3088AA"));
                tv.setTypeface(Typeface.DEFAULT_BOLD);

                return view;
            }
        };*/

        /*customMenuAdapter = new CustomMenuAdapter(this,  menuItems, menuImage);
        menuOptionsLstView.setAdapter(customMenuAdapter);*/
        currentModuleSelected= "Home";

        //customMenuAdapter = new CustomMenuAdapter(CompanyLoginActivity.this,  homeMenuItems , homeMenuImages);
        //menuOptionsLstView.setAdapter(customMenuAdapter);
        //homeModulesBtn.setBackgroundColor(Color.GRAY);
    


        SharedPreferences sharedpreferences = getSharedPreferences(
                PaceSettingManager.USER_PREFERENCES, Context.MODE_PRIVATE);
        name=sharedpreferences.getString("full_name","");
        agentId = sharedpreferences.getInt("agent_id",0);
        userName = sharedpreferences.getString("user_name","");

        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");

        welcomeLbl.setText("Logged In User : " +name +" - Company id: "+agentId +" \n " +sd.format(new Date().getTime()));
        companyNameTxt.setText(sharedpreferences.getString("full_name",""));
        //descriptionTxt.setText(sharedpreferences.getString("company_description",""));


        /*ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        // mSocket.on("adduser", onConnect);
        mSocket.connect();
        mSocket.emit("adduser", userName);*/

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to continue with your action?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if(actionTaken == "logout"){
                        DoLogout doLogout = new DoLogout();
                        doLogout.execute();

                        if(null != backGround){
                            stopService(backGround);
                        }
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


        /*menuOptionsLstView.setOnTouchListener(new ListView.OnTouchListener() {
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
        });*/

        studentModulesBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent companyNav = new Intent(CompanyLoginActivity.this,CompanyNavigationActivity.class);
                        companyNav.putExtra("currentModuleSelected","Student");
                        startActivity(companyNav);
                        /*currentModuleSelected= "Student";

                        customMenuAdapter = new CustomMenuAdapter(CompanyLoginActivity.this,  studentMenuItems, studentMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        teacherModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        homeModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        supervisorModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        studentModulesBtn.setBackgroundColor(Color.GRAY);*/

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

        teacherModulesBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent companyNav = new Intent(CompanyLoginActivity.this,CompanyNavigationActivity.class);
                        companyNav.putExtra("currentModuleSelected","Teacher");
                        startActivity(companyNav);
                        /*currentModuleSelected= "Teacher";

                        customMenuAdapter = new CustomMenuAdapter(CompanyLoginActivity.this,  teacherMenuItems , teacherMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        teacherModulesBtn.setBackgroundColor(Color.GRAY);
                        homeModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        supervisorModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        studentModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/
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

        supervisorModulesBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent companyNav = new Intent(CompanyLoginActivity.this,CompanyNavigationActivity.class);
                        companyNav.putExtra("currentModuleSelected","Supervisor");
                        startActivity(companyNav);
                        /*currentModuleSelected= "Supervisor";

                        customMenuAdapter = new CustomMenuAdapter(CompanyLoginActivity.this,  supervisorMenuItems , supervisorMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        teacherModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        homeModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        supervisorModulesBtn.setBackgroundColor(Color.GRAY);
                        studentModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/
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

        homeModulesBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent companyNav = new Intent(CompanyLoginActivity.this,CompanyNavigationActivity.class);
                        companyNav.putExtra("currentModuleSelected","Home");
                        startActivity(companyNav);
                        /*currentModuleSelected= "Home";

                        customMenuAdapter = new CustomMenuAdapter(CompanyLoginActivity.this,  homeMenuItems , homeMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        teacherModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        homeModulesBtn.setBackgroundColor(Color.GRAY);
                        supervisorModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));
                        studentModulesBtn.setBackgroundColor(Color.parseColor("#3088AA"));*/
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

        /*menuOptionsLstView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMenu = String.valueOf(parent.getItemAtPosition(position));
                        getMenuSelected(currentModuleSelected,position);

                    }
                }
        );*/
    }

    public void getMenuSelected(final String entityType,final int position) {
        if (null != entityType && entityType.trim().length() > 0) {
            if(entityType.equals("Home")){
                switch (position){
                    case 0:
                        Intent updateInfoIntent = new Intent(CompanyLoginActivity.this,CompanyUpdateInformation.class);
                        startActivity(updateInfoIntent);
                        return;
                    case 1:
                        Intent addUpdateReq = new Intent(CompanyLoginActivity.this,AddUpdateCompanyRequirements.class);
                        startActivity(addUpdateReq);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyLoginActivity.this,CompanyLoginActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Student")){
                switch (position){
                    case 0:
                        Intent ojtList = new Intent(CompanyLoginActivity.this,PendingOjTForApprovalActivity.class);
                        /*Intent ojtList = new Intent(CompanyLoginActivity.this,ShowOJTListActivity.class);*/
                        startActivity(ojtList);
                        return;
                    case 1:
                        Intent scanQr = new Intent(CompanyLoginActivity.this,AttendanceCheckerMainActivity.class);
                        startActivity(scanQr);
                        return;
                    case 2:
                        Intent showStudentLoginLogoutPage= new Intent(CompanyLoginActivity.this,ShowStudentLoginLogoutActivity.class);
                        startActivity(showStudentLoginLogoutPage);
                        return;
                    case 3:
                        Intent printReport = new Intent(CompanyLoginActivity.this,PrintReportActivity.class);
                        startActivity(printReport);
                        return;
                    case 4:
                        Intent rateStudent = new Intent(CompanyLoginActivity.this,StudentListActivity.class);
                        startActivity(rateStudent);
                        return;
                    case 5:
                        Intent viewStudentWeeklyReport = new Intent(CompanyLoginActivity.this,StudentListActivity.class);
                        viewStudentWeeklyReport.putExtra("studentWeekly",true);
                        startActivity(viewStudentWeeklyReport);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyLoginActivity.this,CompanyLoginActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Teacher")){
                switch (position){
                    case 0:
                        Intent viewTeachers = new Intent(CompanyLoginActivity.this,ViewTeachersActivity.class);
                        startActivity(viewTeachers);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyLoginActivity.this,CompanyLoginActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Supervisor")){
                switch (position){
                    case 0:
                        Intent coorReq = new Intent(CompanyLoginActivity.this,ShowCoordinatorRequestActivity.class);
                        startActivity(coorReq);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyLoginActivity.this,CompanyLoginActivity.class);
                        startActivity(backToHome);
                }
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }*/

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

    class DoLogout extends AsyncTask<String, String, String> {
        boolean savedSuccessfully = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(TeacherLoginActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        public DoLogout() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",agentId +""));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"logout.php",
                    "POST", params);

            try {
                int success = json.getInt("success");

                if(success == 1){
                    savedSuccessfully = true;
                }

            }catch (Exception e){
                e.printStackTrace();
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            //pDialog.dismiss();


        }
    }

    private boolean isMessageServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        if(null != activityManager){
            for(ActivityManager.RunningServiceInfo runningServiceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(null != runningServiceInfo && null != runningServiceInfo.service) {
                    Log.d("SERVICES......",runningServiceInfo.service.getClassName());
                    if (null != runningServiceInfo
                            && "com.example.ojtmonitoring.BackgroundProcessService".equals(runningServiceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
