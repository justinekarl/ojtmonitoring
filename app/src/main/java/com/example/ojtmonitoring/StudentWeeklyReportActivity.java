package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ojtmonitoring.info.StudentWeeklyPracticumInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentWeeklyReportActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private int studentId,accounttype;

    StudentWeeklyPracticumInfo studentWeeklyPracticumInfo;

    JSONParser jsonParser = new JSONParser();
    EditText studentName,staffName,weekNo,startDate,endDate,task1,task2,task3,task4,task5,task6,task7,remarks1,remarks2,remarks3,remarks4,remarks5,remarks6,remarks7,comments,skillsGained;
    Button saveBtn,addNewBtn,cancelBtn,printBtn,printBtn1;

    String staffNameText,weekNoText,startDateText,endDateText,task1Text,task2Text,task3Text,task4Text,task5Text,task6Text,task7Text,remarks1Text,remarks2Text,remarks3Text,remarks4Text,remarks5Text,remarks6Text,remarks7Text,commentsText,skillsGainedText;

    boolean fromCompany;
    boolean fromTeacher;
    String studentNameTxt;
    int updatedBy = 0;

    Calendar c = Calendar.getInstance();
    Date start,end;

    private String htmlResult;
    private WebView printResultWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_weekly_report);

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        start = c.getTime();

        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        end = c.getTime();

        if(null != getIntent()){
            fromCompany = getIntent().getBooleanExtra("fromCompany",false);
            fromTeacher = getIntent().getBooleanExtra("fromTeacher",false);
        }

        studentName = (EditText)findViewById(R.id.studentName);
        staffName = (EditText)findViewById(R.id.staffName);
        weekNo = (EditText)findViewById(R.id.weekNo);
        startDate = (EditText)findViewById(R.id.startDate);
        endDate = (EditText)findViewById(R.id.endDate);
        task1 = (EditText)findViewById(R.id.task1);
        task2 = (EditText)findViewById(R.id.task2);
        task3 = (EditText)findViewById(R.id.task3);
        task4 = (EditText)findViewById(R.id.task4);
        task5 = (EditText)findViewById(R.id.task5);
        task6 = (EditText)findViewById(R.id.task6);
        task7 = (EditText)findViewById(R.id.task7);

        remarks1 = (EditText)findViewById(R.id.remarks1);
        remarks2 = (EditText)findViewById(R.id.remarks2);
        remarks3 = (EditText)findViewById(R.id.remarks3);
        remarks4 = (EditText)findViewById(R.id.remarks4);
        remarks5 = (EditText)findViewById(R.id.remarks5);
        remarks6 = (EditText)findViewById(R.id.remarks6);
        remarks7 = (EditText)findViewById(R.id.remarks7);

        comments = (EditText)findViewById(R.id.comments);
        skillsGained = (EditText)findViewById(R.id.skillsGained);

        saveBtn = (Button)findViewById(R.id.saveBtn);
        addNewBtn = (Button)findViewById(R.id.addNewBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        printBtn = (Button)findViewById(R.id.printBtn);
        printBtn1 = (Button)findViewById(R.id.printBtn1);
        printResultWebView = (WebView)findViewById(R.id.printResultWebView);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        startDate.setText(dateFormat1.format(start));
        endDate.setText(dateFormat1.format(end));

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);

        if(fromCompany||fromTeacher){
            if(null != getIntent()) {
                studentId = getIntent().getIntExtra("studentId",0);
                studentNameTxt = getIntent().getStringExtra("studentName");
            }
            if(fromCompany) {
                updatedBy = sharedPreferences.getInt("agent_id", 0);
                task1.setEnabled(false);
                task2.setEnabled(false);
                task3.setEnabled(false);
                task4.setEnabled(false);
                task5.setEnabled(false);
                task6.setEnabled(false);
                task7.setEnabled(false);
            }
        }else  {
           studentId = sharedPreferences.getInt("agent_id", 0);
        }
        accounttype = sharedPreferences.getInt("accounttype",0);
        RetrieveData retrieveData = new RetrieveData();
        retrieveData.execute();


        if(accounttype == 1){
            studentNameTxt = sharedPreferences.getString("full_name","");

            comments.setVisibility(View.INVISIBLE);
            skillsGained.setVisibility(View.INVISIBLE);
            printBtn.setVisibility(View.INVISIBLE);

            remarks1.setEnabled(false);
            remarks2.setEnabled(false);
            remarks3.setEnabled(false);
            remarks4.setEnabled(false);
            remarks5.setEnabled(false);
            remarks6.setEnabled(false);
            remarks7.setEnabled(false);

        }

        if(!(accounttype == 1)){
            addNewBtn.setVisibility(View.INVISIBLE);
            printBtn1.setVisibility(View.INVISIBLE);
        }

        studentName.setText(studentNameTxt);
        studentName.setEnabled(false);

        if(accounttype == 3 || accounttype == 2){
            studentName.setEnabled(false);
            staffName.setEnabled(false);
            weekNo.setEnabled(false);
            startDate.setEnabled(false);
            endDate.setEnabled(false);
            task1.setEnabled(false);
            task2.setEnabled(false);
            task3.setEnabled(false);
            task4.setEnabled(false);
            task5.setEnabled(false);
            task6.setEnabled(false);
            task7.setEnabled(false);

            if(accounttype == 2) {
                remarks1.setEnabled(false);
                remarks2.setEnabled(false);
                remarks3.setEnabled(false);
                remarks4.setEnabled(false);
                remarks5.setEnabled(false);
                remarks6.setEnabled(false);
                remarks7.setEnabled(false);
            }
            if(accounttype == 2){
                comments.setEnabled(false);
                skillsGained.setEnabled(false);
            }

        }

        cancelBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent backToHome = new Intent(StudentWeeklyReportActivity.this,StudentLoginActivity.class);
                        if(fromCompany){
                            backToHome = new Intent(StudentWeeklyReportActivity.this,CompanyLoginActivity.class);
                        }
                        if(fromTeacher){
                            backToHome = new Intent(StudentWeeklyReportActivity.this,TeacherLoginActivity.class);
                        }
                        startActivity(backToHome);
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

        saveBtn.setOnTouchListener(new View.OnTouchListener() {
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

                        if(null != staffName && null != staffName.getText()){
                            staffNameText = staffName.getText().toString();
                        }
                        if(null != weekNo && null != weekNo.getText()){
                            weekNoText = weekNo.getText().toString();
                        }
                        if(null != startDate && null != startDate.getText()){
                            startDateText = startDate.getText().toString();
                        }
                        if(null != endDate && null != endDate.getText()){
                            endDateText = endDate.getText().toString();
                        }
                        if(null != task1 && null != task1.getText()){
                            task1Text = task1.getText().toString();
                        }
                        if(null != task2 && null != task2.getText()){
                            task2Text = task2.getText().toString();
                        }
                        if(null != task3 && null != task3.getText()){
                            task3Text = task3.getText().toString();
                        }
                        if(null != task4 && null != task4.getText()){
                            task4Text = task4.getText().toString();
                        }
                        if(null != task5 && null != task5.getText()){
                            task5Text = task5.getText().toString();
                        }
                        if(null != task7 && null != task7.getText()){
                            task7Text = task7.getText().toString();
                        }
                        if(null != task6 && null != task6.getText()){
                            task6Text = task6.getText().toString();
                        }

                        if(null != remarks1 && null != remarks1.getText()){
                            remarks1Text = remarks1.getText().toString();
                        }
                        if(null != remarks2 && null != remarks2.getText()){
                            remarks2Text = remarks2.getText().toString();
                        }
                        if(null != remarks3 && null != remarks3.getText()){
                            remarks3Text = remarks3.getText().toString();
                        }
                        if(null != remarks4 && null != remarks4.getText()){
                            remarks4Text = remarks4.getText().toString();
                        }
                        if(null != remarks5 && null != remarks5.getText()){
                            remarks5Text = remarks5.getText().toString();
                        }
                        if(null != remarks6 && null != remarks6.getText()){
                            remarks6Text = remarks6.getText().toString();
                        }
                        if(null != remarks7 && null != remarks7.getText()){
                            remarks7Text = remarks7.getText().toString();
                        }
                        if(null != comments && null != comments.getText()){
                            commentsText = comments.getText().toString();
                        }
                        if(null != skillsGained && null != skillsGained.getText()){
                            skillsGainedText = skillsGained.getText().toString();
                        }

                        ProcessSave processSave = new ProcessSave();
                        processSave.execute();


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


        addNewBtn.setOnTouchListener(new View.OnTouchListener() {
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

                        //studentName.setText("");
                        staffName.setText("");
                        weekNo.setText("");
                        startDate.setText("");
                        endDate.setText("");
                        task1.setText("");
                        task2.setText("");
                        task3.setText("");
                        task4.setText("");
                        task5.setText("");
                        task6.setText("");
                        task7.setText("");

                        remarks1.setText("");
                        remarks2.setText("");
                        remarks3.setText("");
                        remarks4.setText("");
                        remarks5.setText("");
                        remarks6.setText("");
                        remarks7.setText("");

                        comments.setText("");
                        skillsGained.setText("");

                        //staffNameText = "";
                        weekNoText = "";
                        startDateText = "";
                        endDateText = "";
                        task1Text = "";
                        task2Text = "";
                        task3Text = "";
                        task4Text = "";
                        task5Text = "";
                        task6Text = "";
                        task7Text = "";
                        remarks1Text = "";
                        remarks2Text = "";
                        remarks3Text = "";
                        remarks4Text = "";
                        remarks5Text = "";
                        remarks6Text = "";
                        remarks7Text = "";
                        commentsText = "";
                        skillsGainedText = "";

                        studentWeeklyPracticumInfo = new StudentWeeklyPracticumInfo();
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
                        startDate.setText(dateFormat1.format(start));
                        endDate.setText(dateFormat1.format(end));

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



        printBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        new AsyncTask<Void,String,String>(){

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pDialog = new ProgressDialog(StudentWeeklyReportActivity.this);
                                pDialog.setMessage("Processing..");
                                pDialog.setIndeterminate(false);

                                pDialog.setCancelable(true);
                                //   pDialog.show();
                            }

                            @Override
                            protected String doInBackground(Void... args) {

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("student_id",studentId+""));
                                JSONParser jsonParser1 = new JSONParser();

                                JSONObject json = jsonParser1.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "printStudentWeeklyReport",
                                        "POST", params);


                                try {
                                    if (null != json) {

                                        // check log cat fro response
                                        Log.d("Create Response", json.toString());

                                        if (json.has("data")) {
                                            htmlResult = json.get("data").toString();
                                        }

                                        /*int success = json.getInt("success");
                                        if(success == 1) {
                                            htmlResult
                                        }*/

                                        } else {
                                            //loginMessage="Invalid User";
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //loginMessage="Invalid User";
                                    }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                pDialog.dismiss();
                                super.onPostExecute(s);
                                printResultWebView.loadData(htmlResult, "text/html", "utf-8");
                                createWebPrintJob(printResultWebView);
                            }
                        }.execute();


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



        printBtn1.setOnTouchListener(new View.OnTouchListener() {
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
                        new AsyncTask<Void,String,String>(){

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pDialog = new ProgressDialog(StudentWeeklyReportActivity.this);
                                pDialog.setMessage("Processing..");
                                pDialog.setIndeterminate(false);

                                pDialog.setCancelable(true);
                                //   pDialog.show();
                            }

                            @Override
                            protected String doInBackground(Void... args) {

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("student_id",studentId+""));
                                JSONParser jsonParser1 = new JSONParser();

                                JSONObject json = jsonParser1.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "printStudentWeeklyReport",
                                        "POST", params);


                                try {
                                    if (null != json) {

                                        // check log cat fro response
                                        Log.d("Create Response", json.toString());

                                        if (json.has("data")) {
                                            htmlResult = json.get("data").toString();
                                        }

                                        /*int success = json.getInt("success");
                                        if(success == 1) {
                                            htmlResult
                                        }*/

                                    } else {
                                        //loginMessage="Invalid User";
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //loginMessage="Invalid User";
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                pDialog.dismiss();
                                super.onPostExecute(s);
                                printResultWebView.loadData(htmlResult, "text/html", "utf-8");
                                createWebPrintJob(printResultWebView);
                            }
                        }.execute();


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
    }



    class ProcessSave extends AsyncTask<String, String, String> {
        boolean validDate = true;
        boolean isSuccessfullySaved = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StudentWeeklyReportActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ProcessSave() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",studentId+""));

            params.add(new BasicNameValuePair("staff_name",staffNameText+""));
            Date fromDate = null;
            Date endDate = null;
            try {
                if(startDateText.trim().length() > 0) {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    fromDate = dateFormat.parse(startDateText);

                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                    params.add(new BasicNameValuePair("start_date", dateFormat1.format(fromDate)));
                }else{
                    params.add(new BasicNameValuePair("start_date", null));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                if(endDateText.trim().length() > 0) {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    endDate = dateFormat.parse(endDateText);

                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");

                    params.add(new BasicNameValuePair("end_date", dateFormat1.format(endDate)));
                }else{
                    params.add(new BasicNameValuePair("end_date", null));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

                if(null != fromDate && null != endDate){
                    if(fromDate.after(endDate)){
                        validDate = false;

                    }
                }


            params.add(new BasicNameValuePair("id_to_update",(null != studentWeeklyPracticumInfo ? studentWeeklyPracticumInfo.getId() + "" : 0+"")));

            params.add(new BasicNameValuePair("week",weekNoText+""));
            params.add(new BasicNameValuePair("comments",commentsText+""));
            params.add(new BasicNameValuePair("skills_gained",skillsGainedText+""));

            params.add(new BasicNameValuePair("task1",task1Text+""));
            params.add(new BasicNameValuePair("task2",task2Text+""));
            params.add(new BasicNameValuePair("task3",task3Text+""));
            params.add(new BasicNameValuePair("task4",task4Text+""));
            params.add(new BasicNameValuePair("task5",task5Text+""));
            params.add(new BasicNameValuePair("task6",task6Text+""));
            params.add(new BasicNameValuePair("task7",task7Text+""));

            params.add(new BasicNameValuePair("remarks1",remarks1Text+""));
            params.add(new BasicNameValuePair("remarks2",remarks2Text+""));
            params.add(new BasicNameValuePair("remarks3",remarks3Text+""));
            params.add(new BasicNameValuePair("remarks4",remarks4Text+""));
            params.add(new BasicNameValuePair("remarks5",remarks5Text+""));
            params.add(new BasicNameValuePair("remarks6",remarks6Text+""));
            params.add(new BasicNameValuePair("remarks7",remarks7Text+""));

            params.add(new BasicNameValuePair("updatedBy",updatedBy+""));
            params.add(new BasicNameValuePair("accountType",accounttype+""));


            if(validDate) {
                JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS + "saveStudentWeeklyPracticum.php",
                        "POST", params);


                try {
                    if (null != json) {

                        // check log cat fro response
                        Log.d("Create Response", json.toString());

                        int success = json.getInt("success");
                        if (success == 1) {
                            isSuccessfullySaved = true;
                        }

                    } else {
                        //loginMessage="Invalid User";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //loginMessage="Invalid User";
                }
            }

            return null;
        }

        /**
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if(!validDate){
                PaceSettingManager.toastMessage(StudentWeeklyReportActivity.this,"Start Date Must Not Be After End Date.");
                return;
            }

            if(isSuccessfullySaved){
                PaceSettingManager.toastMessage(StudentWeeklyReportActivity.this,"Successfully updated!");
            }else{
                PaceSettingManager.toastMessage(StudentWeeklyReportActivity.this,"Error Occured!");
            }
        }
    }





    class RetrieveData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(StudentWeeklyReportActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public RetrieveData() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",studentId+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveStudentWeeklyPracticum.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("student_weekly_practicum")){
                           JSONArray data = json.getJSONArray("student_weekly_practicum");
                            if(null != data){
                                for(int ctr = 0;  ctr < data.length() ; ctr++) {
                                    studentWeeklyPracticumInfo = new StudentWeeklyPracticumInfo();
                                    for (int i = 0; i <= data.getJSONArray(ctr).length() - 1; i++) {
                                        Log.d("AAAA",data.getJSONArray(ctr).get(i)+"");
                                        if(i==0){
                                            //id
                                            studentWeeklyPracticumInfo.setId(Integer.parseInt(data.getJSONArray(ctr).get(i)+""));
                                        }
                                        if(i==1){
                                            //week
                                            studentWeeklyPracticumInfo.setWeekNo(Integer.parseInt(data.getJSONArray(ctr).get(i)+""));
                                        }
                                        if(i==2){
                                            //staff_name
                                            studentWeeklyPracticumInfo.setStaffName(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==3){

                                            //start_date
                                            try {
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                Date fromDate = dateFormat.parse(data.getJSONArray(ctr).get(i)+"");

                                                SimpleDateFormat dateFormat1= new SimpleDateFormat("MM/dd/yyyy");
                                                studentWeeklyPracticumInfo.setStartDate(dateFormat1.format(fromDate));

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }



                                            //studentWeeklyPracticumInfo.setStartDate(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==4){
                                            //end_date

                                            //start_date
                                            try {
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                Date fromDate = dateFormat.parse(data.getJSONArray(ctr).get(i)+"");

                                                SimpleDateFormat dateFormat1= new SimpleDateFormat("MM/dd/yyyy");
                                                studentWeeklyPracticumInfo.setEndDate(dateFormat1.format(fromDate));

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            //studentWeeklyPracticumInfo.setEndDate(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==5){
                                            //student_id
                                            studentWeeklyPracticumInfo.setStudentId(Integer.parseInt(data.getJSONArray(ctr).get(i)+""));
                                        }
                                        if(i==6){
                                            //comments
                                            studentWeeklyPracticumInfo.setComments(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==7){
                                            //skills_gained
                                            studentWeeklyPracticumInfo.setSkillsGained(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==11){
                                            //task1
                                            studentWeeklyPracticumInfo.setTask1(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==12){
                                            //task2
                                            studentWeeklyPracticumInfo.setTask2(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==13){
                                            //task3
                                            studentWeeklyPracticumInfo.setTask3(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==14){
                                            //task4
                                            studentWeeklyPracticumInfo.setTask4(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==15){
                                            //task5
                                            studentWeeklyPracticumInfo.setTask5(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==16){
                                            //task6
                                            studentWeeklyPracticumInfo.setTask6(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==17){
                                            //task7
                                            studentWeeklyPracticumInfo.setTask7(data.getJSONArray(ctr).get(i)+"");
                                        }

                                        if(i==18){
                                            //remarks1
                                            studentWeeklyPracticumInfo.setRemarks1(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==19){
                                            //remarks2
                                            studentWeeklyPracticumInfo.setRemarks2(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==20){
                                            //remarks3
                                            studentWeeklyPracticumInfo.setRemarks3(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==21){
                                            //remarks4
                                            studentWeeklyPracticumInfo.setRemarks4(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==22){
                                            //remarks5
                                            studentWeeklyPracticumInfo.setRemarks5(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==23){
                                            //remarks6
                                            studentWeeklyPracticumInfo.setRemarks6(data.getJSONArray(ctr).get(i)+"");
                                        }
                                        if(i==24){
                                            //remarks7
                                            studentWeeklyPracticumInfo.setRemarks7(data.getJSONArray(ctr).get(i)+"");
                                        }
                                    }
                                }
                            }
                        }
                    }

                }else{
                    //loginMessage="Invalid User";
                }

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
            if(null != studentWeeklyPracticumInfo){
                weekNo.setText(studentWeeklyPracticumInfo.getWeekNo()+"");
                staffName.setText(null == studentWeeklyPracticumInfo.getStaffName() || "null".equals(studentWeeklyPracticumInfo.getStaffName()) ? "": studentWeeklyPracticumInfo.getStaffName()+"");
                startDate.setText(null == studentWeeklyPracticumInfo.getStartDate() || "null".equals(studentWeeklyPracticumInfo.getStartDate())? "":studentWeeklyPracticumInfo.getStartDate()+"");
                endDate.setText(null == studentWeeklyPracticumInfo.getEndDate() || "null".equals(studentWeeklyPracticumInfo.getEndDate())? "":studentWeeklyPracticumInfo.getEndDate()+"");
                comments.setText(null == studentWeeklyPracticumInfo.getComments() || "null".equals(studentWeeklyPracticumInfo.getComments())? "":studentWeeklyPracticumInfo.getComments()+"");
                skillsGained.setText(null == studentWeeklyPracticumInfo.getSkillsGained() || "null".equals(studentWeeklyPracticumInfo.getSkillsGained())? "":studentWeeklyPracticumInfo.getSkillsGained()+"");
                task1.setText(null == studentWeeklyPracticumInfo.getTask1() || "null".equals(studentWeeklyPracticumInfo.getTask1()) ? "":studentWeeklyPracticumInfo.getTask1()+"");
                task2.setText(null == studentWeeklyPracticumInfo.getTask2() || "null".equals(studentWeeklyPracticumInfo.getTask2())? "":studentWeeklyPracticumInfo.getTask2()+"");
                task3.setText(null == studentWeeklyPracticumInfo.getTask3() || "null".equals(studentWeeklyPracticumInfo.getTask3())? "":studentWeeklyPracticumInfo.getTask3()+"");
                task4.setText(null == studentWeeklyPracticumInfo.getTask4() || "null".equals(studentWeeklyPracticumInfo.getTask4())? "":studentWeeklyPracticumInfo.getTask4()+"");
                task5.setText(null == studentWeeklyPracticumInfo.getTask5() || "null".equals(studentWeeklyPracticumInfo.getTask5())? "":studentWeeklyPracticumInfo.getTask5()+"");
                task6.setText(null == studentWeeklyPracticumInfo.getTask6() || "null".equals(studentWeeklyPracticumInfo.getTask6())? "":studentWeeklyPracticumInfo.getTask6()+"");
                task7.setText(null == studentWeeklyPracticumInfo.getTask7() || "null".equals(studentWeeklyPracticumInfo.getTask7())? "":studentWeeklyPracticumInfo.getTask7()+"");

                remarks1.setText(null == studentWeeklyPracticumInfo.getRemarks1() || "null".equals(studentWeeklyPracticumInfo.getTask1())? "":studentWeeklyPracticumInfo.getRemarks1()+"");
                remarks2.setText(null == studentWeeklyPracticumInfo.getRemarks2() || "null".equals(studentWeeklyPracticumInfo.getTask2())? "":studentWeeklyPracticumInfo.getRemarks2()+"");
                remarks3.setText(null == studentWeeklyPracticumInfo.getRemarks3() || "null".equals(studentWeeklyPracticumInfo.getTask3()) ? "":studentWeeklyPracticumInfo.getRemarks3()+"");
                remarks4.setText(null == studentWeeklyPracticumInfo.getRemarks4() || "null".equals(studentWeeklyPracticumInfo.getTask4())? "":studentWeeklyPracticumInfo.getRemarks4()+"");
                remarks5.setText(null == studentWeeklyPracticumInfo.getRemarks5() || "null".equals(studentWeeklyPracticumInfo.getTask5())? "":studentWeeklyPracticumInfo.getRemarks5()+"");
                remarks6.setText(null == studentWeeklyPracticumInfo.getRemarks6() || "null".equals(studentWeeklyPracticumInfo.getTask6())? "":studentWeeklyPracticumInfo.getRemarks6()+"");
                remarks7.setText(null == studentWeeklyPracticumInfo.getRemarks7() || "null".equals(studentWeeklyPracticumInfo.getTask7())? "":studentWeeklyPracticumInfo.getRemarks7()+"");
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,StudentLoginActivity.class);
        if(fromCompany){
            home = new Intent(this,CompanyLoginActivity.class);
        }
        if(fromTeacher){
            home = new Intent(this,TeacherLoginActivity.class);
        }
        startActivity(home);
    }

    public static int dpToPx(final float dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());


        // Save the job object for later status checking
        // mPrintJob = printJob;
    }
}
