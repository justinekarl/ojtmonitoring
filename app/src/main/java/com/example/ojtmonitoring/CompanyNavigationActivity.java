package com.example.ojtmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CompanyNavigationActivity extends AppCompatActivity {

    private ListView menuOptionsLstView;
    private CustomMenuAdapter customMenuAdapter;
    String currentModule= "";

    final String[] studentMenuItems = {"OJT list","Scan Student QR Codes","Student login/logout","Create Weekly Report","Rate Student","Student Weekly Practicum Report"};
    final int[] studentMenuImages = {R.mipmap.ic_view,R.mipmap.ic_scan_qr,R.mipmap.ic_list,R.mipmap.ic_list,R.mipmap.ic_rate,R.mipmap.ic_view};

    final String[] teacherMenuItems = {"Teachers"};
    final int[] teacherMenuImages = {R.mipmap.ic_list};

    final String[] supervisorMenuItems = {"Company Supervisor Request"};
    final int[] supervisorMenuImages = {R.mipmap.ic_view};

    final String[] homeMenuItems = {"Update Information","Add/Update Requirements"};
    final int[] homeMenuImages = {R.mipmap.ic_update,R.mipmap.ic_add_generic};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_navigation);
        menuOptionsLstView = (ListView)findViewById(R.id.menuOptionsLstView);

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
                        getMenuSelected(currentModule,position);

                    }
                }
        );


        if(null != getIntent().getStringExtra("currentModuleSelected")){
            currentModule = getIntent().getStringExtra("currentModuleSelected");
        }

        if(currentModule.equals("Home")){
            customMenuAdapter = new CustomMenuAdapter(CompanyNavigationActivity.this,  homeMenuItems , homeMenuImages);
        }else if(currentModule.equals("Student")){
            customMenuAdapter = new CustomMenuAdapter(CompanyNavigationActivity.this,  studentMenuItems, studentMenuImages);
        }else if(currentModule.equals("Supervisor")){
            customMenuAdapter = new CustomMenuAdapter(CompanyNavigationActivity.this,  supervisorMenuItems , supervisorMenuImages);
        }else if(currentModule.equals("Teacher")){
            customMenuAdapter = new CustomMenuAdapter(CompanyNavigationActivity.this,  teacherMenuItems , teacherMenuImages);

        }

        menuOptionsLstView.setAdapter(customMenuAdapter);


    }

    public void getMenuSelected(final String entityType,final int position) {
        if (null != entityType && entityType.trim().length() > 0) {
            if(entityType.equals("Home")){
                switch (position){
                    case 0:
                        Intent updateInfoIntent = new Intent(CompanyNavigationActivity.this,CompanyUpdateInformation.class);
                        startActivity(updateInfoIntent);
                        return;
                    case 1:
                        Intent addUpdateReq = new Intent(CompanyNavigationActivity.this,AddUpdateCompanyRequirements.class);
                        startActivity(addUpdateReq);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyNavigationActivity.this,CompanyNavigationActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Student")){
                switch (position){
                    case 0:
                        Intent ojtList = new Intent(CompanyNavigationActivity.this,PendingOjTForApprovalActivity.class);
                        /*Intent ojtList = new Intent(CompanyNavigationActivity.this,ShowOJTListActivity.class);*/
                        startActivity(ojtList);
                        return;
                    case 1:
                        Intent scanQr = new Intent(CompanyNavigationActivity.this,AttendanceCheckerMainActivity.class);
                        startActivity(scanQr);
                        return;
                    case 2:
                        Intent showStudentLoginLogoutPage= new Intent(CompanyNavigationActivity.this,ShowStudentLoginLogoutActivity.class);
                        startActivity(showStudentLoginLogoutPage);
                        return;
                    case 3:
                        Intent printReport = new Intent(CompanyNavigationActivity.this,PrintReportActivity.class);
                        startActivity(printReport);
                        return;
                    case 4:
                        Intent rateStudent = new Intent(CompanyNavigationActivity.this,StudentListActivity.class);
                        startActivity(rateStudent);
                        return;
                    case 5:
                        Intent viewStudentWeeklyReport = new Intent(CompanyNavigationActivity.this,StudentListActivity.class);
                        viewStudentWeeklyReport.putExtra("studentWeekly",true);
                        startActivity(viewStudentWeeklyReport);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyNavigationActivity.this,CompanyNavigationActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Teacher")){
                switch (position){
                    case 0:
                        Intent viewTeachers = new Intent(CompanyNavigationActivity.this,ViewTeachersActivity.class);
                        startActivity(viewTeachers);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyNavigationActivity.this,CompanyNavigationActivity.class);
                        startActivity(backToHome);
                }
            }

            if(entityType.equals("Supervisor")){
                switch (position){
                    case 0:
                        Intent coorReq = new Intent(CompanyNavigationActivity.this,ShowCoordinatorRequestActivity.class);
                        startActivity(coorReq);
                        return;
                    default:
                        Intent backToHome = new Intent(CompanyNavigationActivity.this,CompanyNavigationActivity.class);
                        startActivity(backToHome);
                }
            }
        }
    }

}
