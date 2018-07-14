package com.example.ojtmonitoring;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ojtmonitoring.info.StudentCompanyOJTInfo;

import java.util.ArrayList;
import java.util.List;

public class OjtApplicationsListView extends BaseAdapter {
    public List<StudentCompanyOJTInfo> studentCompanyOJTInfos;
    Context context;
    TextView companyNameOjtTxt;
    CheckBox ojtApprovedChk;
    TextView studentNameTxt;
    TextView studentPhoneTxt;
    TextView studentEmailTxt;
    TextView studentCollegeTxt;
    StudentCompanyOJTInfo studentCompanyOJTInfo;


    public OjtApplicationsListView(List<StudentCompanyOJTInfo> studentCompanyOJTInfoList, Context context){
        this.studentCompanyOJTInfos = studentCompanyOJTInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != studentCompanyOJTInfos) {
            return studentCompanyOJTInfos.size();
        }
        return  0;
    }


    @Override
    public Object getItem(int position) {
        return studentCompanyOJTInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(null != studentCompanyOJTInfos) {
                studentCompanyOJTInfos.get((Integer) buttonView.getTag()).setSelected(isChecked ? 1 : 0);
            }
        }
    };


    public ArrayList<StudentCompanyOJTInfo> getStudentCompanyOJTInfoList(){
        ArrayList<StudentCompanyOJTInfo> ojtInfos = new ArrayList<StudentCompanyOJTInfo>();
        if(null != studentCompanyOJTInfos) {
            for (StudentCompanyOJTInfo ojtInfo : studentCompanyOJTInfos) {
                //if (ojtInfo.getSelected() == 1) {
                    ojtInfos.add(ojtInfo);
                //}
            }
        }
        return ojtInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.custom_ojt_applicant_row, null);

        companyNameOjtTxt = (TextView)view.findViewById(R.id.companyNameOjtTxt);
        ojtApprovedChk = (CheckBox)view.findViewById(R.id.ojtApprovedChk);
        studentNameTxt = (TextView)view.findViewById(R.id.studentNameTxt);
        studentPhoneTxt = (TextView)view.findViewById(R.id.studentPhoneTxt);
        studentEmailTxt= (TextView)view.findViewById(R.id.studentEmailTxt);
        studentCollegeTxt= (TextView)view.findViewById(R.id.studentCollegeTxt);

        studentCompanyOJTInfo = studentCompanyOJTInfos.get(position);
        if(null != studentCompanyOJTInfo){
            companyNameOjtTxt.setText(null != studentCompanyOJTInfo.getCompanyInfo() ? studentCompanyOJTInfo.getCompanyInfo().getName() : "");
            if((null !=studentCompanyOJTInfo.getResumeInfo() && null != studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo())){
                studentNameTxt.setText((null != studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().getName() ? studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().getName() : ""));
                studentPhoneTxt.setText((null != studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().getPhoneNumber() ?  studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().getPhoneNumber() : ""));
                studentEmailTxt.setText((null != studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().getEmail() ? studentCompanyOJTInfo.getResumeInfo().getStudentPersonalInformationInfo().getEmail() : "" ));
                studentCollegeTxt.setText((null != studentCompanyOJTInfo.getResumeInfo().getCollege()) ? studentCompanyOJTInfo.getResumeInfo().getCollege() : "");

                ojtApprovedChk.setChecked(studentCompanyOJTInfo.getSelected() == 1 ? true : false);
                ojtApprovedChk.setTag(position);
                ojtApprovedChk.setOnCheckedChangeListener(onCheckedChangeListener);

            }

            /*if(position%2==0){
                view.setBackgroundResource(R.color.divider);
            }else{
                view.setBackgroundResource(R.color.white);
            }*/

            view.setTag(studentCompanyOJTInfo.getResumeInfo().getId());
        }


      /*  ojtApprovedChk.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        if(null != studentCompanyOJTInfo){
                            studentCompanyOJTInfo.setSelected(isChecked ? 1 : 0);

                        }
                    }
                }
        );
*/





      //  ojtApprovedChk.setChecked(studentCompanyOJTInfo.getSelected() == 0 ? false : true);

        return view;
    }
}
