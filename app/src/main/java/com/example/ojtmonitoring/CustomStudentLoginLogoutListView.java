package com.example.ojtmonitoring;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ojtmonitoring.info.StudentLoginLogoutLogsInfo;

import java.util.List;

public class CustomStudentLoginLogoutListView extends BaseAdapter{

    private List<StudentLoginLogoutLogsInfo> studentLoginLogoutLogsInfos;
    private TextView studentNameTxt;
    private TextView companyNameTxt;
    private TextView loginTxt;
    private TextView logoutTxt;
    private TextView fromFingerprintTxt;
    Context context;
    private StudentLoginLogoutLogsInfo studentLoginLogoutLogsInfo;

    public CustomStudentLoginLogoutListView(List<StudentLoginLogoutLogsInfo> studentLoginLogoutLogsInfos, Context context){
        this.studentLoginLogoutLogsInfos = studentLoginLogoutLogsInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != studentLoginLogoutLogsInfos){
            return studentLoginLogoutLogsInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != studentLoginLogoutLogsInfos){
            return studentLoginLogoutLogsInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.custom_student_login_logout_row,null);

        studentNameTxt = (TextView)view.findViewById(R.id.studentNameTxt);
        companyNameTxt = (TextView)view.findViewById(R.id.companyNameTxt);
        loginTxt = (TextView)view.findViewById(R.id.loginTxt);
        logoutTxt = (TextView)view.findViewById(R.id.logoutTxt);
        fromFingerprintTxt = (TextView)view.findViewById(R.id.fromFingerprintTxt);

        studentLoginLogoutLogsInfo = studentLoginLogoutLogsInfos.get(position);
        if(null != studentLoginLogoutLogsInfo){
            studentNameTxt.setText(studentLoginLogoutLogsInfo.getStudentName());
            companyNameTxt.setText(studentLoginLogoutLogsInfo.getCompanyName());
            loginTxt.setText(studentLoginLogoutLogsInfo.getLoginDate());
            logoutTxt.setText(studentLoginLogoutLogsInfo.getLogoutDate());
            fromFingerprintTxt.setText(studentLoginLogoutLogsInfo.getFromFingerPrint());
        }

        if(position%2==0){
            view.setBackgroundResource(R.color.divider);
        }else{
            view.setBackgroundResource(R.color.white);
        }

        return view;
    }
}
