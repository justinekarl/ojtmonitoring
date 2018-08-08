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
            sb.append(" Student Name : " +userAccountInfo.getName());
            sb.append("\n");
            sb.append(" College : " +userAccountInfo.getCollege());
            sb.append("\n");
            sb.append(" Gender : " +userAccountInfo.getGender());

            detailsTxt.setText(sb.toString());

            approvedChk.setChecked(userAccountInfo.isApproved());
            approvedChk.setTag(position);
            approvedChk.setOnCheckedChangeListener(onCheckedChangeListener);
            view.setTag(userAccountInfo.getId());


        }


        return view;
    }
}
