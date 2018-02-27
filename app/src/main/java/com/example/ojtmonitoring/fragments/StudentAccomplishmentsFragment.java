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
import com.example.ojtmonitoring.info.AccomplishmentsInterestInfo;


public class StudentAccomplishmentsFragment extends Fragment {

    private EditText accomplishmentsTxt;
    private EditText interestTxt;
    private EditText elementaryTxt;
    private EditText elemAddressTxt;
    private EditText highSchoolTxt;
    private EditText highSchoolAddressTxt;
    private EditText collegeTxt;
    private EditText collegeAddressTxt;


    private String accomplishments;
    private String interest;
    private String elementary;
    private String elemAddress;
    private String highSchool;
    private String highSchoolAddress;
    private String college;
    private String collegeAddress;

    StudentAccomplishmentsInterface studentAccomplishmentsInterface;

    public interface StudentAccomplishmentsInterface{
        public AccomplishmentsInterestInfo getAccomplishmentsInterestInfo();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            studentAccomplishmentsInterface = (StudentAccomplishmentsInterface)context;

        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString());
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accomplishments = null != accomplishmentsTxt.getText() ? accomplishmentsTxt.getText().toString() : "";
        interest = null != interestTxt.getText() ? interestTxt.getText().toString() : "";
        elementary = null != elementaryTxt.getText() ? elementaryTxt.getText().toString() : "";
        elemAddress = null != elemAddressTxt.getText() ? elemAddressTxt.getText().toString() : "";

        highSchool = null != highSchoolTxt.getText() ? highSchoolTxt.getText().toString() : "";
        highSchoolAddress = null != highSchoolAddressTxt.getText() ? highSchoolAddressTxt.getText().toString() : "";
        college = null != collegeTxt.getText() ? collegeTxt.getText().toString() : "";
        collegeAddress = null != collegeAddressTxt.getText() ? collegeAddressTxt.getText().toString() : "";

        studentAccomplishmentsInterface.getAccomplishmentsInterestInfo();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_accomplishments,container,false);

        accomplishmentsTxt = (EditText)view.findViewById(R.id.accomplishmentsTxt);
        interestTxt = (EditText)view.findViewById(R.id.interestTxt);
        elementaryTxt = (EditText)view.findViewById(R.id.elementaryTxt);
        elemAddressTxt = (EditText)view.findViewById(R.id.elemAddressTxt);
        highSchoolTxt = (EditText)view.findViewById(R.id.highSchoolTxt);
        highSchoolAddressTxt = (EditText)view.findViewById(R.id.highSchoolAddress1Txt);
        collegeTxt = (EditText)view.findViewById(R.id.collegeTxt);
        collegeAddressTxt = (EditText)view.findViewById(R.id.collegeAddressTxt);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
