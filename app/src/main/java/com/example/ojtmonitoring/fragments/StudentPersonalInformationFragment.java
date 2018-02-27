package com.example.ojtmonitoring.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.StudentPersonalInformationInfo;


public class StudentPersonalInformationFragment extends Fragment {

    //personal info
    private EditText nameTxt;
    private EditText addressTxt;
    private EditText phoneNumberTxt;
    private EditText emailTxt;

    //work experience
    private EditText companyName1Txt;
    private EditText companyAddress1Txt;
    private EditText jobDescription1Txt;
    private EditText dutiesCompany1Txt;

    private EditText companyName2Txt;
    private EditText companyAddress2Txt;
    private EditText jobDescription2Txt;
    private EditText dutiesCompany2Txt;





    private static String name;
    private static String address;
    private static String phoneNumber;
    private static String email;

    private static String companyName1;
    private static String companyAddress1;
    private static String jobDescription1;
    private static String dutiesCompany1;

    private static String companyName2;
    private static String companyAddress2;
    private static String jobDescription2;
    private static String dutiesCompany2;


    PersonalInformationListener personalInformationListener;

    public interface PersonalInformationListener{
        public StudentPersonalInformationInfo getStudentPersonalInfo(String name,String address,String phoneNumber,String email);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            personalInformationListener = (PersonalInformationListener)context;

        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name = null != nameTxt.getText() ? nameTxt.getText().toString() : "";
        address = null != addressTxt.getText() ? addressTxt.getText().toString() : "";
        phoneNumber = null != phoneNumberTxt.getText() ? phoneNumberTxt.getText().toString() : "";
        email = null != emailTxt.getText() ? emailTxt.getText().toString() : "";

        companyName1 = null != companyName1Txt.getText() ? companyName1Txt.getText().toString() : "";
        companyAddress1 = null != companyAddress1Txt.getText() ? companyAddress1Txt.getText().toString() : "";
        jobDescription1 = null != jobDescription1Txt.getText() ? jobDescription1Txt.getText().toString() : "";
        dutiesCompany1 = null != dutiesCompany1Txt.getText() ? dutiesCompany1Txt.getText().toString() : "";

        companyName2 = null != companyName2Txt.getText() ? companyName2Txt.getText().toString() : "";
        companyAddress2 = null != companyAddress2Txt.getText() ? companyAddress2Txt.getText().toString() : "";
        jobDescription2 = null != jobDescription2Txt.getText() ? jobDescription2Txt.getText().toString() : "";
        dutiesCompany2 = null != dutiesCompany2Txt.getText() ? dutiesCompany2Txt.getText().toString() : "";


        personalInformationListener.getStudentPersonalInfo(name,address,phoneNumber,email);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_basic_info,container,false);
        nameTxt = (EditText)view.findViewById(R.id.nameTxt);
        addressTxt = (EditText)view.findViewById(R.id.addressTxt);
        phoneNumberTxt = (EditText)view.findViewById(R.id.phoneNumberTxt);
        emailTxt = (EditText)view.findViewById(R.id.elemAddTxt);

        companyName1Txt = (EditText)view.findViewById(R.id.companyName1Txt);
        companyAddress1Txt = (EditText)view.findViewById(R.id.companyAddress1Txt);
        jobDescription1Txt = (EditText)view.findViewById(R.id.jobDescription1Txt);
        dutiesCompany1Txt = (EditText)view.findViewById(R.id.dutiesCompany1Txt);

        companyName2Txt = (EditText)view.findViewById(R.id.companyName2Txt);
        companyAddress2Txt = (EditText)view.findViewById(R.id.companyAddress2Txt);
        jobDescription2Txt = (EditText)view.findViewById(R.id.jobDescription2Txt);
        dutiesCompany2Txt = (EditText)view.findViewById(R.id.dutiesCompany2Txt);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameTxt = (EditText)view.findViewById(R.id.nameTxt);
        addressTxt = (EditText)view.findViewById(R.id.addressTxt);
        phoneNumberTxt = (EditText)view.findViewById(R.id.phoneNumberTxt);
        emailTxt = (EditText)view.findViewById(R.id.elemAddTxt);

    }
}
