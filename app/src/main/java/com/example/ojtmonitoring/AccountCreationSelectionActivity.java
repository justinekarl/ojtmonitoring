package com.example.ojtmonitoring;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.jomer.filetracker.R;

public class AccountCreationSelectionActivity extends AppCompatActivity {

    private RadioButton companyRadBtn;
    private RadioButton studentRadBtn;
    private RadioButton teacherRadBtn;
    private RadioButton coordinatorRadBtn;


    private Button selAcctTypeBtn;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_selection);

        companyRadBtn = (RadioButton)findViewById(R.id.companyRadBtn);
        studentRadBtn = (RadioButton)findViewById(R.id.studentRadBtn);
        teacherRadBtn = (RadioButton)findViewById(R.id.teacherRadBtn);
        coordinatorRadBtn = (RadioButton)findViewById(R.id.coordinatorRadBtn);

        selAcctTypeBtn = (Button)findViewById(R.id.selAcctTypeBtn);
        backBtn = (Button)findViewById(R.id.backBtn);

        companyRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(companyRadBtn.isChecked()){
                            studentRadBtn.setChecked(false);
                            teacherRadBtn.setChecked(false);
                            coordinatorRadBtn.setChecked(false);
                        }
                    }
                }

        );

        studentRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(studentRadBtn.isChecked()){
                            companyRadBtn.setChecked(false);
                            teacherRadBtn.setChecked(false);
                            coordinatorRadBtn.setChecked(false);
                        }
                    }
                }

        );

        teacherRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(teacherRadBtn.isChecked()){
                            studentRadBtn.setChecked(false);
                            companyRadBtn.setChecked(false);
                            coordinatorRadBtn.setChecked(false);
                        }
                    }
                }

        );

        coordinatorRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(coordinatorRadBtn.isChecked()){
                            studentRadBtn.setChecked(false);
                            companyRadBtn.setChecked(false);
                            teacherRadBtn.setChecked(false);
                        }
                    }
                }
        );


       /* selAcctTypeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent redirectToCreationOfAccnt = null;

                        if(companyRadBtn.isChecked()) {
                            redirectToCreationOfAccnt = new Intent(AccountCreationSelectionActivity.this, CreateCompanyAccountActivity.class);
                        }

                        if(teacherRadBtn.isChecked()){
                            redirectToCreationOfAccnt  = new Intent(AccountCreationSelectionActivity.this,CreateTeacherAccountActivity.class);
                        }

                        if(studentRadBtn.isChecked()){
                            redirectToCreationOfAccnt = new Intent(AccountCreationSelectionActivity.this,CreateStudentActivity.class);
                        }

                        if(null != redirectToCreationOfAccnt) startActivity(redirectToCreationOfAccnt);
                    }
                }
        );*/


        selAcctTypeBtn.setOnTouchListener(
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
                                Intent redirectToCreationOfAccnt = null;

                                boolean hasSelected = false;
                                if(companyRadBtn.isChecked()) {
                                    redirectToCreationOfAccnt = new Intent(AccountCreationSelectionActivity.this, CreateCompanyAccountActivity.class);
                                    hasSelected = true;
                                }

                                if(teacherRadBtn.isChecked()){
                                    redirectToCreationOfAccnt  = new Intent(AccountCreationSelectionActivity.this,CreateTeacherAccountActivity.class);
                                    hasSelected = true;
                                }

                                if(studentRadBtn.isChecked()){
                                    redirectToCreationOfAccnt = new Intent(AccountCreationSelectionActivity.this,CreateStudentActivity.class);
                                    hasSelected = true;
                                }

                                if(coordinatorRadBtn.isChecked()){
                                    redirectToCreationOfAccnt = new Intent(AccountCreationSelectionActivity.this,CreateCoordinatorAccountActivity.class);
                                    hasSelected = true;
                                }
                                if(!hasSelected){
                                    toastMessage("Kindly select account type");
                                    Button view = (Button) v;
                                    view.getBackground().clearColorFilter();
                                    view.invalidate();
                                    break;
                                }

                                if(null != redirectToCreationOfAccnt) startActivity(redirectToCreationOfAccnt);
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


        /*backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent login = new Intent(AccountCreationSelectionActivity.this,Login.class);
                        startActivity(login);
                    }
                }
        );*/

        backBtn.setOnTouchListener(
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
                                Intent login = new Intent(AccountCreationSelectionActivity.this,Login.class);
                                startActivity(login);
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
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
