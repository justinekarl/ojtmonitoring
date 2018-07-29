package com.example.ojtmonitoring;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ojtmonitoring.info.CoordinatorRequestInfo;

import java.util.ArrayList;
import java.util.List;

public class CustomCoorRequestListView extends BaseAdapter {

    private CheckBox approveChkBx;
    Context context;
    private TextView coordinatorNameTxt;
    private TextView addressTxt;
    private TextView phoneTxt;
    List<CoordinatorRequestInfo> coordinatorRequestInfoList;
    private CoordinatorRequestInfo coordinatorRequestInfo;

    public CustomCoorRequestListView(List<CoordinatorRequestInfo> coordinatorRequestInfoList,Context context){
        this.coordinatorRequestInfoList = coordinatorRequestInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != coordinatorRequestInfoList){
            return coordinatorRequestInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return coordinatorRequestInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.custom_coor_request_row,null);

        approveChkBx = (CheckBox)view.findViewById(R.id.approveChkBx);
        coordinatorNameTxt = (TextView)view.findViewById(R.id.coordinatorNameTxt);
        addressTxt = (TextView)view.findViewById(R.id.addressTxt);
        phoneTxt = (TextView)view.findViewById(R.id.phoneTxt);

        coordinatorRequestInfo = coordinatorRequestInfoList.get(position);

        if(null != coordinatorRequestInfo){
            coordinatorNameTxt.setText(coordinatorRequestInfo.getName());
            addressTxt.setText("Address: "+coordinatorRequestInfo.getAddress());
            phoneTxt.setText("Phone: "+coordinatorRequestInfo.getPhoneNumber());
            approveChkBx.setChecked(coordinatorRequestInfo.isApproved());
            approveChkBx.setOnCheckedChangeListener(onCheckedChangeListener);
            approveChkBx.setTag(position);
        }

        /*if(position%2==0){
            view.setBackgroundResource(R.color.divider);
        }else{
            view.setBackgroundResource(R.color.white);
        }*/

        return view;
    }

    public ArrayList<CoordinatorRequestInfo> getCoordinatorRequestList(){
        ArrayList<CoordinatorRequestInfo> coordinatorRequestInfos = new ArrayList<>();
        if(null != coordinatorRequestInfoList && coordinatorRequestInfoList.size() > 0) {
            for (CoordinatorRequestInfo coordinatorRequestInfo : coordinatorRequestInfoList) {
                //if(coordinatorRequestInfo.isApproved()){
                coordinatorRequestInfos.add(coordinatorRequestInfo);
                //}
            }
        }
        return  coordinatorRequestInfos;
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(null != coordinatorRequestInfoList) {
                coordinatorRequestInfoList.get((Integer) buttonView.getTag()).setApproved(isChecked);
            }
        }
    };
}
