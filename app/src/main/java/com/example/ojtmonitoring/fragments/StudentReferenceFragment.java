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
import com.example.ojtmonitoring.info.ReferencesInfo;

public class StudentReferenceFragment extends Fragment{

    private EditText refName1Txt;
    private EditText refAddress1Txt;
    private EditText refPhone1Txt;
    private EditText refOccupation1Txt;

    private EditText refName2Txt;
    private EditText refAddress2Txt;
    private EditText refPhone2Txt;
    private EditText refOccupation2Txt;

    private EditText refName3Txt;
    private EditText refAddress3Txt;
    private EditText refPhone3Txt;
    private EditText refOccupation3Txt;

    private String refName1;
    private String refAddress1;
    private String refPhone1;
    private String refOccupation1;

    private String refName2;
    private String refAddress2;
    private String refPhone2;
    private String refOccupation2;

    private String refName3;
    private String refAddress3;
    private String refPhone3;
    private String refOccupation3;

    ReferenceInterface referenceInterface;

    public interface ReferenceInterface{
        public ReferencesInfo getReferencesInfo();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            referenceInterface = (ReferenceInterface)context;

        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString());
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_student_references,container,false);

        refName1Txt = (EditText)view.findViewById(R.id.refName1Txt);
        refAddress1Txt = (EditText)view.findViewById(R.id.refAddress1Txt);
        refPhone1Txt = (EditText)view.findViewById(R.id.refPhone1Txt);
        refOccupation1Txt = (EditText)view.findViewById(R.id.refOccupation1Txt);

        refName2Txt = (EditText)view.findViewById(R.id.refName2Txt);
        refAddress2Txt = (EditText)view.findViewById(R.id.refAddress2Txt);
        refPhone2Txt = (EditText)view.findViewById(R.id.refPhone2Txt);
        refOccupation2Txt = (EditText)view.findViewById(R.id.refOccupation2Txt);

        refName3Txt = (EditText)view.findViewById(R.id.refName3Txt);
        refAddress3Txt = (EditText)view.findViewById(R.id.refAddress3Txt);
        refPhone3Txt = (EditText)view.findViewById(R.id.refPhone3Txt);
        refOccupation3Txt = (EditText)view.findViewById(R.id.refOccupation3Txt);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refName1 = null != refName1Txt.getText() ? refName1Txt.getText().toString() : "";
        refAddress1 = null != refAddress1Txt.getText() ? refAddress1Txt.getText().toString() : "";
        refPhone1 =null != refPhone1Txt.getText() ? refPhone1Txt.getText().toString() : "";
        refOccupation1 = null != refOccupation1Txt.getText() ? refOccupation1Txt.getText().toString() : "";

        refName2 = null != refName2Txt.getText() ? refName2Txt.getText().toString() : "";
        refAddress2 = null != refAddress2Txt.getText() ? refAddress2Txt.getText().toString() : "";
        refPhone2 =null != refPhone2Txt.getText() ? refPhone2Txt.getText().toString() : "";
        refOccupation2 = null != refOccupation2Txt.getText() ? refOccupation2Txt.getText().toString() : "";

        refName3 = null != refName3Txt.getText() ? refName3Txt.getText().toString() : "";
        refAddress3 = null != refAddress3Txt.getText() ? refAddress3Txt.getText().toString() : "";
        refPhone3 =null != refPhone3Txt.getText() ? refPhone3Txt.getText().toString() : "";
        refOccupation3 = null != refOccupation3Txt.getText() ? refOccupation3Txt.getText().toString() : "";

        referenceInterface.getReferencesInfo();
    }
}
