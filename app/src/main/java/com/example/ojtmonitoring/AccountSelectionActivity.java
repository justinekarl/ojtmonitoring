package com.example.ojtmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class AccountSelectionActivity extends AppCompatActivity {
    private RadioButton companyRadBtn;
    private RadioButton studentRadBtn;
    private RadioButton teacherRadBtn;

    private Button selAcctTypeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_selection);

        companyRadBtn = (RadioButton)findViewById(R.id.companyRadBtn);
        studentRadBtn = (RadioButton)findViewById(R.id.studentRadBtn);
        teacherRadBtn = (RadioButton)findViewById(R.id.teacherRadBtn);

        selAcctTypeBtn = (Button)findViewById(R.id.selAcctTypeBtn);

        companyRadBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(companyRadBtn.isChecked()){
                            studentRadBtn.setChecked(false);
                            teacherRadBtn.setChecked(false);
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
                        }
                    }
                }

        );


        selAcctTypeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent redirectToCreationOfAccnt = null;

                        if(companyRadBtn.isChecked()) {
                            redirectToCreationOfAccnt = new Intent(AccountSelectionActivity.this, CreateCompanyAccountActivity.class);
                        }

                        if(teacherRadBtn.isChecked()){
                            redirectToCreationOfAccnt  = new Intent(AccountSelectionActivity.this,CreateTeacherAccountActivity.class);
                        }

                        if(studentRadBtn.isChecked()){
                            redirectToCreationOfAccnt = new Intent(AccountSelectionActivity.this,CreateStudentActivity.class);
                        }

                        if(null != redirectToCreationOfAccnt) startActivity(redirectToCreationOfAccnt);
                    }
                }
        );
    }

}
