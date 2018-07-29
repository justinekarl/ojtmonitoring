package com.example.ojtmonitoring;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ojtmonitoring.info.ResumeInfo;

import java.util.ArrayList;
import java.util.List;

public class CustomStudentOJTListView extends BaseAdapter {
    List<ResumeInfo> studentInfos;
    ResumeInfo studentInfo;
    CheckBox acceptChkbox;
    TextView displayTxt;
    Context context;

    public CustomStudentOJTListView(List<ResumeInfo> studentInfos,Context context){
        this.studentInfos = studentInfos;
        this.context = context;
    }


    @Override
    public int getCount() {
        if(null != studentInfos){
            return studentInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return studentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<ResumeInfo> getResumeInfoArrayList(){
        ArrayList<ResumeInfo> studentInfosList = new ArrayList<ResumeInfo>();
        if(null != studentInfos && studentInfos.size() > 0) {
            for (ResumeInfo studentInfo : studentInfos) {
                //if(studentInfo.getSelected() == 1){
                studentInfosList.add(studentInfo);
                // }
            }
        }
        return  studentInfosList;
    }



    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(null != studentInfos){
                studentInfos.get((Integer)buttonView.getTag()).setSelected(isChecked ? 1 : 0);
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.custom_student_company_ojt_row,null);

        acceptChkbox = (CheckBox)view.findViewById(R.id.acceptChkbox);
        displayTxt = (TextView)view.findViewById(R.id.displayTxt);

        studentInfo = studentInfos.get(position);

        if(null != studentInfo){

            StringBuffer sb = new StringBuffer("");
            if(null != studentInfo.getStudentPersonalInformationInfo()){
                sb.append(studentInfo.getStudentPersonalInformationInfo().getName());
                sb.append("\n");
                sb.append(studentInfo.getCollege());
                sb.append("\n");
                sb.append("Course: "+studentInfo.getCourse());
            }
            displayTxt.setText(sb.toString());
            acceptChkbox.setChecked(studentInfo.getSelected() == 1 ? true : false);
            acceptChkbox.setTag(position);
            acceptChkbox.setOnCheckedChangeListener(onCheckedChangeListener);


            /*if(position%2==0){
                view.setBackgroundResource(R.color.divider);
            }else{
                view.setBackgroundResource(R.color.white);
            }*/
        }
        return view;
    }
}
