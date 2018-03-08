package com.example.ojtmonitoring;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.CompanyInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class CustomCompanyListView extends BaseAdapter{

    public ArrayList<HashMap<String,String>> companylist;
    public List<CompanyInfo> companyInfos;
    TextView custCompanyNameTxt;
    TextView custAddressTxt;
    TextView custPhoneNumberTxt;
    TextView custEmailTxt;
    TextView custDescriptionTxt;
    CheckBox selCompanyChk;
    Context context;
    CompanyInfo companyInfo;


    public CustomCompanyListView(List<CompanyInfo> companyInfos, Context context) {
        this.companyInfos = companyInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != companyInfos) {
            return companyInfos.size();
        }
        return  0;
    }

    @Override
    public Object getItem(int position) {
        return companyInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public ArrayList<CompanyInfo> getCompanyInfosList(){
        ArrayList<CompanyInfo> infos = new ArrayList<CompanyInfo>();
        if(null != companyInfos) {
            for (CompanyInfo companyInfo : companyInfos) {
                if(companyInfo.getSelected() == 1) {
                    infos.add(companyInfo);
                }
            }
        }
        return infos;
    }

    CompoundButton.OnCheckedChangeListener  onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(null != companyInfos){
                companyInfos.get((Integer)buttonView.getTag()).setSelected(isChecked ? 1 : 0);
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context,R.layout.custom_company_row,null);


        custCompanyNameTxt = (TextView)view.findViewById(R.id.custCompanyNameTxt);
        custAddressTxt = (TextView)view.findViewById(R.id.custAddressTxt);
        custPhoneNumberTxt = (TextView)view.findViewById(R.id.custPhoneNumberTxt);
        custEmailTxt = (TextView)view.findViewById(R.id.custEmailTxt);
        custDescriptionTxt = (TextView)view.findViewById(R.id.custDescriptionTxt);
        selCompanyChk = (CheckBox)view.findViewById(R.id.selCompanyChk);


        companyInfo = companyInfos.get(position);
        if(null != companyInfo) {
            custCompanyNameTxt.setText(companyInfo.getName());
            custAddressTxt.setText(companyInfo.getAddress());
            custPhoneNumberTxt.setText(companyInfo.getPhoneNumber());
            custEmailTxt.setText(companyInfo.getEmailAddress());
            custDescriptionTxt.setText(companyInfo.getDescription());
            selCompanyChk.setChecked(companyInfo.getSelected() == 0 ? false : true);
            selCompanyChk.setTag(position);
            selCompanyChk.setOnCheckedChangeListener(onCheckedChangeListener);
            if(position%2==0){
                view.setBackgroundResource(R.color.divider);
            }else{
                view.setBackgroundResource(R.color.white);
            }

            view.setTag(companyInfo.getId());
        }


       /* selCompanyChk.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(null != companyInfo){
                            companyInfo.setSelected(isChecked ? 1 : 0);
                        }
                    }
                }
        );*/


        return view;
    }
}
