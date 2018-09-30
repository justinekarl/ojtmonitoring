package com.example.ojtmonitoring;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ojtmonitoring.info.UserAccountInfo;

import java.util.List;

public class CustomNewStudentAccountListView extends BaseAdapter {

    private List<UserAccountInfo> userAccountInfos;
    UserAccountInfo userAccountInfo;
    CheckBox approvedChk;
    TextView detailsTxt;
    Context context;


    public List<UserAccountInfo> getUserAccountInfos() {
        return userAccountInfos;
    }

    public void setUserAccountInfos(List<UserAccountInfo> userAccountInfos) {
        this.userAccountInfos = userAccountInfos;
    }

    public CustomNewStudentAccountListView(List<UserAccountInfo> userAccountInfos, Context context){
        this.userAccountInfos = userAccountInfos;
        this.context = context;
    }


    CompoundButton.OnCheckedChangeListener  onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(null != userAccountInfos){
                userAccountInfos.get((Integer)buttonView.getTag()).setApproved(isChecked);
            }
        }
    };

    @Override
    public int getCount() {
        if(null != userAccountInfos){
            return userAccountInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != userAccountInfos) {
            return userAccountInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.custom_new_student_account_row,null);

        approvedChk = (CheckBox)view.findViewById(R.id.approvedChk);
        detailsTxt = (TextView)view.findViewById(R.id.detailsTxt);

        userAccountInfo = userAccountInfos.get(position);

        if(null != userAccountInfo){
            final StringBuffer sb = new StringBuffer("");

            if(userAccountInfo.getAccountType() == 1) {
                sb.append("Student Name : ");
            }else{
                if(userAccountInfo.isAdminViewing()){
                    if(userAccountInfo.getAccountType() == 2){
                        sb.append("Teacher Name : ");
                    }

                    if(userAccountInfo.getAccountType() == 3){
                        sb.append("Company Name : ");
                    }

                    if(userAccountInfo.getAccountType() == 4){
                        sb.append("Company Supervisor Name : ");
                    }
                }else {
                    sb.append("Teacher Name : ");
                }
            }
            sb.append(userAccountInfo.getName());
            sb.append("\n");


            if(!userAccountInfo.isAdminViewing()) {
                sb.append(" College : " + null != userAccountInfo.getCollege() ? userAccountInfo.getCollege() : "");
            }
            if(userAccountInfo.getAccountType() == 1) {
                sb.append("\n");
                sb.append(" Gender : " +(null != userAccountInfo.getGender() ? userAccountInfo.getGender() : ""));
                if(userAccountInfo.isAdminViewing()) {
                    sb.append("\n");
                    sb.append(" Course : " + (null != userAccountInfo.getCourse() ? userAccountInfo.getCourse() : ""));
                }
            }

            if(userAccountInfo.isAdminViewing()){
                if(userAccountInfo.getAccountType() == 2){
                    sb.append("\n");
                    sb.append(" Department : " +(null != userAccountInfo.getDepartment() ? userAccountInfo.getDepartment() : ""));
                }

                if(userAccountInfo.getAccountType() == 3){
                    sb.append("\n");
                    sb.append(" Address : " +(null != userAccountInfo.getAddress() ? userAccountInfo.getAddress() : ""));
                    sb.append("\n");
                    sb.append(" Phone : " +(null != userAccountInfo.getPhoneNumber() ? userAccountInfo.getPhoneNumber() : ""));
                    sb.append("\n");
                    sb.append(" Email : " +(null != userAccountInfo.getEmail() ? userAccountInfo.getEmail() : ""));
                }
            }

            detailsTxt.setText(sb.toString());

            approvedChk.setChecked(userAccountInfo.isApproved());
            approvedChk.setTag(position);
            approvedChk.setOnCheckedChangeListener(onCheckedChangeListener);
            view.setTag(userAccountInfo.getId());


        }


        return view;
    }
}
