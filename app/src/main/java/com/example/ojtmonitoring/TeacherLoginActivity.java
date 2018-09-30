package com.example.ojtmonitoring;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ojtmonitoring.info.CourseInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TeacherLoginActivity extends AppCompatActivity {

    private Button logoutBtn,teacherHomeBtn;
    private TextView welcomeTeacherLbl;

    private String name;
    private int agentId;
    private boolean adminTeacher;
    String currentModuleSelected = "Student";

    String[] menuItemsDisplay = {};
    final String[] studentMenuItems = {"New Student Accounts",
                                        "Create Section",
                                        "Show Ojt Requests",
                                        "Show Student Login/Logout",
                                        "Show Section Enrollees",
                                        "View Section Information",
                                        "Student Practicum Weekly Report",
                                        "View Student Evaluation",
                                        "Create Weekly Report"};
    final int[] studentMenuImages = {R.mipmap.ic_pending,
                                     R.mipmap.ic_add_generic,
                                     R.mipmap.ic_view,
                                     R.mipmap.ic_list,
                                     R.mipmap.ic_list,
                                     R.mipmap.ic_list,
                                     R.mipmap.ic_list,
                                     R.mipmap.ic_list,
                                     R.mipmap.ic_list};

    final String[] companyMenuItems = {"View Company List"};

    final int[] companyMenuImages = {R.mipmap.ic_list};

    final String[] homeMenuItems = {"New Teacher Accounts"};
    final int[] homeMenuImages = {R.mipmap.ic_pending};


    final String[] menuItems = {"New Student Accounts","Create Section","Show Ojt Requests","Show Student Login/Logout","Show Section Enrollees","View Section Information","View Company List","Student Practicum Weekly Report","View Student Evaluation","Create Weekly Report","New Teacher Accounts"};
    final String[] menuItems2 = {"New Student Accounts","Create Section","Show Ojt Requests","Show Student Login/Logout","Show Section Enrollees","View Section Information","View Company List","Student Practicum Weekly Report","View Student Evaluation","Create Weekly Report"};
    final int[] menuImage = {R.mipmap.ic_pending,R.mipmap.ic_add_generic,R.mipmap.ic_view,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_pending};
    ListAdapter menuAdapter;
    private ListView menuOptionsLstView;
    private CustomMenuAdapter customMenuAdapter;

    private Socket mSocket;
    private String userName;
    private Button logoutTopBtn,studentModule,companyModule;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    Intent backGround;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,TeacherLoginActivity.class);
        startActivity(home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        PaceSettingManager.lockActivityOrientation(this);


        if(!isMessageServiceRunning()) {
            backGround = new Intent(this, BackgroundProcessService.class);
            startService(backGround);
        }

        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutTopBtn = (Button)findViewById(R.id.logoutTopBtn);
        teacherHomeBtn = (Button)findViewById(R.id.teacherHomeBtn);

        welcomeTeacherLbl = (TextView)findViewById(R.id.welcomeTeacherLbl);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        name=sharedPreferences.getString("full_name","");
        userName = sharedPreferences.getString("user_name","");
        adminTeacher = sharedPreferences.getBoolean("admin_teacher",Boolean.FALSE);
        menuOptionsLstView = (ListView)findViewById(R.id.menuOptionsLstView);
        studentModule = (Button)findViewById(R.id.studentModule);
        companyModule = (Button)findViewById(R.id.companyModule);



        studentModule.setBackgroundColor(Color.GRAY);

        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        // mSocket.on("adduser", onConnect);
        mSocket.connect();
        mSocket.emit("adduser", userName);
        //mSocket.emit("check_user", userName,userName);

        /*menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuItems){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);

                TextView tv = (TextView)view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.parseColor("#3088AA"));
                tv.setTypeface(Typeface.DEFAULT_BOLD);

                return view;
            }
        };*/
        customMenuAdapter = new CustomMenuAdapter(this,  adminTeacher ? menuItems : menuItems2, menuImage);
        menuOptionsLstView.setAdapter(customMenuAdapter);

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

        if(!adminTeacher){
            teacherHomeBtn.setVisibility(View.INVISIBLE);
        }


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

        teacherHomeBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        currentModuleSelected = "Home";

                        customMenuAdapter = new CustomMenuAdapter(TeacherLoginActivity.this,  homeMenuItems, homeMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        teacherHomeBtn.setBackgroundColor(Color.GRAY);
                        studentModule.setBackgroundColor(Color.parseColor("#3088AA"));
                        companyModule.setBackgroundColor(Color.parseColor("#3088AA"));

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


        studentModule.setOnTouchListener(new View.OnTouchListener() {
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
                        currentModuleSelected = "Student";

                        customMenuAdapter = new CustomMenuAdapter(TeacherLoginActivity.this,  studentMenuItems, studentMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        studentModule.setBackgroundColor(Color.GRAY);
                        companyModule.setBackgroundColor(Color.parseColor("#3088AA"));
                        teacherHomeBtn.setBackgroundColor(Color.parseColor("#3088AA"));

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

        companyModule.setOnTouchListener(new View.OnTouchListener() {
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
                        currentModuleSelected = "Company";

                        customMenuAdapter = new CustomMenuAdapter(TeacherLoginActivity.this,  companyMenuItems, companyMenuImages);
                        menuOptionsLstView.setAdapter(customMenuAdapter);
                        companyModule.setBackgroundColor(Color.GRAY);
                        studentModule.setBackgroundColor(Color.parseColor("#3088AA"));
                        teacherHomeBtn.setBackgroundColor(Color.parseColor("#3088AA"));

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

        menuOptionsLstView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMenu = String.valueOf(parent.getItemAtPosition(position));
                        getMenuSelected(currentModuleSelected,position);

                    }
                }
        );

    }

    public void getMenuSelected(final String entityType,final int position){
        if(null != entityType && entityType.trim().length() > 0){
            if(entityType.equals("Student")){
                switch (position){
                    case 0:
                        Intent newStudAccount = new Intent(TeacherLoginActivity.this,NewStudentAccountsActivity.class);
                        startActivity(newStudAccount);
                        return ;
                    case 1:
                        Intent createSection = new Intent(TeacherLoginActivity.this,CreateStudentSectionActivity.class);
                        startActivity(createSection);
                        return;
                    case 2:
                        Intent showOjtRequest = new Intent(TeacherLoginActivity.this,ShowOJTApplicationsActivity.class);
                        startActivity(showOjtRequest);
                        return;
                    case 3:
                        Intent showStudentLoginLogoutPage= new Intent(TeacherLoginActivity.this,ShowStudentLoginLogoutActivity.class);
                        startActivity(showStudentLoginLogoutPage);
                        return;
                    case 4:
                        Intent showSectionEnrollees= new Intent(TeacherLoginActivity.this,ShowSectionEnrolleesActivity.class);
                        startActivity(showSectionEnrollees);
                        return;
                    case 5:
                        Intent showSectionInfo = new Intent(TeacherLoginActivity.this,ViewSectionsActivity.class);
                        startActivity(showSectionInfo);
                        return;
                    case 6:
                        Intent viewStudentWeeklyReport = new Intent(TeacherLoginActivity.this,ShowStudentListsActivity.class);
                        viewStudentWeeklyReport.putExtra("studentWeekly",true);
                        startActivity(viewStudentWeeklyReport);
                        return;

                    case 7:
                        Intent viewStudentPracticumEval = new Intent(TeacherLoginActivity.this,ShowStudentListsActivity.class);
                        viewStudentPracticumEval.putExtra("studentEvaluation",true);
                        startActivity(viewStudentPracticumEval);
                        return;

                    case 8:
                        Intent printReport = new Intent(TeacherLoginActivity.this,PrintReportActivity.class);
                        startActivity(printReport);
                        return;
                    case 9:
                        Intent newTeacherAccount = new Intent(TeacherLoginActivity.this,NewTeachersAccountActivity.class);
                        startActivity(newTeacherAccount);
                        return;
                    default:
                        Intent backToHome = new Intent(TeacherLoginActivity.this,TeacherLoginActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Company")){
                switch (position) {
                    case 0:
                        Intent viewCompanies = new Intent(TeacherLoginActivity.this, ViewCompaniesActivity.class);
                        startActivity(viewCompanies);
                        return;
                    default:
                        Intent backToHome = new Intent(TeacherLoginActivity.this,TeacherLoginActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Home")){
                switch (position) {
                    case 0:
                        Intent newTeacherAccount = new Intent(TeacherLoginActivity.this,NewTeachersAccountActivity.class);
                        startActivity(newTeacherAccount);
                        return;
                    default:
                        Intent backToHome = new Intent(TeacherLoginActivity.this,TeacherLoginActivity.class);
                        startActivity(backToHome);
                }
            }
        }



    }

/*

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0,0,Menu.NONE,"Show Ojt Requests");
        menu.add(1,1,Menu.NONE,"Show Student Login/Logout");

        return super.onPrepareOptionsMenu(menu);
    }
*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Intent showOjtRequest = new Intent(TeacherLoginActivity.this,ShowOJTApplicationsActivity.class);
                startActivity(showOjtRequest);
                return true;
            case 1:
                Intent showStudentLoginLogoutPage= new Intent(TeacherLoginActivity.this,ShowStudentLoginLogoutActivity.class);
                startActivity(showStudentLoginLogoutPage);
                return true;

        }

        return super.onOptionsItemSelected(item);
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
