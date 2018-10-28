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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentLoginActivity extends AppCompatActivity {

    private static String name;
    private int agentId;
    private static int accountType;

    private TextView welcomeLbl;
    private Button logoutBtn;
    private TextView messageNotifTxt;
    private ListView menuOptionsLstView;
    private String status;
    private String ojtDone;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    Intent backGround;
    Intent backGround2;


    private StringBuffer sb = new StringBuffer("");
    String[] menuItems = {"My Information","Companies","Add/Update My Resume","Select Section","My OJT Progress","Rate Company","Student Weekly Practicum Report","Logs"};
    int[] menuImage = {R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_add_res,R.mipmap.ic_sel,R.mipmap.ic_list,R.mipmap.ic_rate,R.mipmap.ic_list,R.mipmap.ic_list};
    ListAdapter  menuAdapter;
    boolean hasSectionSelected = false;
    boolean hasMessageNotif=false;
    private int percentFinished;
    private  CustomMenuAdapter customMenuAdapter;

    private Button signoutbtntop;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,StudentLoginActivity.class);
        startActivity(home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        PaceSettingManager.lockActivityOrientation(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        name=sharedPreferences.getString("full_name","");
        accountType = sharedPreferences.getInt("accounttype",0);
        status = sharedPreferences.getString("ojt_done","");
        ojtDone = sharedPreferences.getString("ojtDone","");
        messageNotifTxt = (TextView)findViewById(R.id.messageNotifTxt);
        signoutbtntop = (Button)findViewById(R.id.signoutbtntop);


        welcomeLbl = (TextView)findViewById(R.id.welcomeLbl);
        menuOptionsLstView = (ListView)findViewById(R.id.menuOptionsLstView);

        if(!isMessageServiceRunning()) {
            backGround = new Intent(this, BackgroundProcessService.class);
            backGround.putExtra("studentId",agentId);
            startService(backGround);
        }

        if(!isTransactionNotificationServiceRunning()){
            backGround2 = new Intent(this,TransactionLogBackgroundProcessService.class);
            backGround2.removeExtra("studentId");
            backGround2.removeExtra("entityType");
            backGround2.putExtra("studentId",agentId);
            backGround2.putExtra("entityType","Student");
            startService(backGround2);
        }



        SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy");


        welcomeLbl.setText("Logged In User : " +name +" - Student id: "+agentId +" \n " +sd.format(new Date().getTime()));

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
                            DoLogout doLogout = new DoLogout();
                            doLogout.execute();
                            if(isMessageServiceRunning()){
                                stopService(new Intent(StudentLoginActivity.this,BackgroundProcessService.class));
                            }
                            if(isTransactionNotificationServiceRunning()){
                                stopService(new Intent(StudentLoginActivity.this,TransactionLogBackgroundProcessService.class));
                            }
                           /* if(null != backGround){
                                stopService(backGround);
                            }
                            if(null != backGround2){
                                stopService(backGround2);
                            }*/

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


        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        if(!hasSectionSelected){

        }

        customMenuAdapter = new CustomMenuAdapter(this,  menuItems, menuImage){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.linearlayoutCustom);
                        // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(R.id.txtTitle);
                tv.setGravity(Gravity.CENTER);

                // Set the text color of TextView (ListView Item)
                switch (position) {
                    case 0:
                        //relativeLayout.setBackgroundColor(Color.RED);
                        relativeLayout.setBackgroundResource(R.mipmap.indigo);
                        break;
                    case 1:
                        relativeLayout.setBackgroundResource(R.mipmap.red);
                        break;
                    case 2:
                        relativeLayout.setBackgroundResource(R.mipmap.orange);
                        break;
                    case 3:
                        relativeLayout.setBackgroundResource(R.mipmap.skyblue);
                        break;
                    case 4:
                        relativeLayout.setBackgroundResource(R.mipmap.peach);
                        break;
                    case 5:
                        relativeLayout.setBackgroundResource(R.mipmap.gold);
                        break;
                    case 6:
                        relativeLayout.setBackgroundResource(R.mipmap.bluegreen);
                        break;
                    case 7:
                        relativeLayout.setBackgroundResource(R.mipmap.lightpink);
                        break;
                }




                // Generate ListView Item using TextView
                return view;
            }
        };

        /*menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,menuItems){
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
        menuOptionsLstView.setAdapter(customMenuAdapter);


        logoutBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != backGround2){
                            stopService(backGround2);
                        }
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
        );


        signoutbtntop.setOnTouchListener(
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
                                /*if(null != backGround){
                                    stopService(backGround);
                                }
                                if(null != backGround2){
                                    stopService(backGround2);
                                }*/
                                DoLogout doLogout = new DoLogout();
                                doLogout.execute();


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

                        if(selectedMenu.equals("Rate Company") && !ojtDone.equals("1")){
                            toastMessage("OJT not yet finished!");
                            return;
                        }
                        if(!hasSectionSelected && (selectedMenu.equals("Show My OJT Progress") || selectedMenu.equals("Show Companies"))){
                            toastMessage("No enrolled section yet!");
                            return;
                        }

                        switch (position){
                            case 0:
                                Intent showStudentInfo = new Intent(StudentLoginActivity.this,StudentInformationActivity.class);
                                startActivity(showStudentInfo);
                                finish();
                                return;
                            case 1:
                                Intent showCompanies = new Intent(StudentLoginActivity.this,ShowCompaniesActivity.class);
                                startActivity(showCompanies);
                                finish();
                                return;
                            case 2:
                                Intent addResume = new Intent(StudentLoginActivity.this,ResumeActivity.class);
                                /*Intent addResume = new Intent(StudentLoginActivity.this,CreateUpdateResumeActivity.class);*/
                                startActivity(addResume);
                                finish();
                                return;
                            case 3:
                                Intent selectSection = new Intent(StudentLoginActivity.this,SectionSelectionActivity.class);
                                startActivity(selectSection);
                                finish();
                                return;
                            case 4:
                                Intent showAccummulated = new Intent(StudentLoginActivity.this,ShowTimeAccumulatedActivity.class);
                                startActivity(showAccummulated);
                                finish();
                                return;
                            case 5:
                                Intent rateCompany = new Intent(StudentLoginActivity.this,RateCompanyActivity.class);
                                startActivity(rateCompany);
                                finish();
                                return;
                            case 6:
                                Intent studentWeeklyReport = new Intent(StudentLoginActivity.this,StudentWeeklyReportActivity.class);
                                startActivity(studentWeeklyReport);
                                finish();
                                return;
                            case 7:
                                Intent logs = new Intent(StudentLoginActivity.this,StudentOjtLogsActivity.class);
                                startActivity(logs);
                                finish();
                                return;
                            default:
                                Intent backToHome = new Intent(StudentLoginActivity.this,StudentLoginActivity.class);
                                startActivity(backToHome);

                        }

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

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                Intent showCompanies = new Intent(StudentLoginActivity.this,ShowCompaniesActivity.class);
                startActivity(showCompanies);
                return true;
            case 0:
                Intent addResume = new Intent(StudentLoginActivity.this,CreateUpdateResumeActivity.class);
                startActivity(addResume);
                return true;
            default:
                Intent backToHome = new Intent(StudentLoginActivity.this,StudentLoginActivity.class);
                startActivity(backToHome);
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(0,0,Menu.NONE,"Add/Update My Resume");
        menu.add(1,1,Menu.NONE,"Show Companies");
        menu.add(2,2,Menu.NONE,"Show Time Accumulated");


        return super.onPrepareOptionsMenu(menu);
    }*/



    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StudentLoginActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",agentId+""));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveStudentNotif.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    //if(success == 1) {
                        sb = new StringBuffer("");
                        if(json.has("message_notif") && (null != json.get("message_notif") && !json.get("message_notif").toString().equals(""))){
                            hasMessageNotif = true;
                            JSONArray notifListArr =json.getJSONArray("message_notif");

                            if(null != notifListArr){
                                for(int i =0 ; i<notifListArr.length() ; i++){

                                    for (int k = 0; k < notifListArr.getJSONArray(i).length(); k++) {
                                        String[] row = null;
                                        if(null != notifListArr.getJSONArray(i) && (notifListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                            row = (notifListArr.getJSONArray(i).get(k) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("date_created")){
                                                    sb.append(value +" : ");
                                                }

                                               if(key.equals("message")){
                                                   sb.append(value);
                                                   sb.append("\n\n\n");
                                               }

                                            }
                                        }
                                    }


                                }

                            }
                        }

                        if(json.has("section_id") && null != json.get("section_id") ){

                            hasSectionSelected = true;

                            if(json.get("section_id").toString().equals("null") || Long.valueOf(json.get("section_id")+"") == 0 ) {
                                sb = new StringBuffer("");
                                sb.append("You are not enrolled in any sections yet, kindly select a section first before continuing.");
                                sb.append("\n");
                                sb.append("Navigate to \"Select Section\" Page");
                                hasSectionSelected = false;
                            }
                        }
                    }

                /*}else{
                    //loginMessage="Invalid User";
                }*/

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
            if(null != sb && sb.toString().trim().length() > 0){
                messageNotifTxt.setText(sb.toString());
            }

            if(!hasMessageNotif){
                messageNotifTxt.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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
         * After completing background_light task Dismiss the progress dialog
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

    private boolean isTransactionNotificationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        if(null != activityManager){
            for(ActivityManager.RunningServiceInfo runningServiceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(null != runningServiceInfo && null != runningServiceInfo.service) {
                    Log.d("SERVICES......",runningServiceInfo.service.getClassName());
                    if (null != runningServiceInfo
                            && "com.example.ojtmonitoring.TransactionLogBackgroundProcessService".equals(runningServiceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
