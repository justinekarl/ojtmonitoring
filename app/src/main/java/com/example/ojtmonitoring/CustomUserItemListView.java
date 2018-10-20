package com.example.ojtmonitoring;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ojtmonitoring.info.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jj on 8/21/18.
 */

public class CustomUserItemListView extends BaseAdapter {

    Context context;
    private TextView userinfonameTxt;
    private TextView phoneNumberTxt;
    private TextView addressTxt;
    private List<UserInfo> userInfoList;
    private UserInfo userInfo;
    private ImageView onlinestatus;

    public CustomUserItemListView(List<UserInfo> userInfoList,Context context){
        this.userInfoList = userInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != userInfoList && userInfoList.size() > 0){
            return userInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != userInfoList) {
            return userInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.custom_user_item_layout,null);
        userinfonameTxt = (TextView)view.findViewById(R.id.userinfonameTxt);
        phoneNumberTxt = (TextView)view.findViewById(R.id.phoneNumberTxt);
        addressTxt = (TextView) view.findViewById(R.id.addressTxt);
        onlinestatus = (ImageView)view.findViewById(R.id.onlinestatus);

        userInfo = userInfoList.get(position);

        if (null != userInfo){
            String name = userInfo.getName();
            if(null != userInfo.getCollege() && "" != userInfo.getCollege()){
                name = userInfo.getName() + " \n " + userInfo.getCollege();
            }
            userinfonameTxt.setText(name);
            phoneNumberTxt.setText(userInfo.getPhone());
            addressTxt.setText(userInfo.getAddress());

            onlinestatus.setImageResource(userInfo.isOnline() ? R.mipmap.online : R.mipmap.offline);

        }

        return view;
    }

    public ArrayList<UserInfo> getUserInfoLists(){
        ArrayList<UserInfo> userInfoLists = new ArrayList<>();
        if(null != userInfoList && userInfoList.size() > 0) {
            for (UserInfo userInfo : userInfoList) {
                //if(coordinatorRequestInfo.isApproved()){
                userInfoLists.add(userInfo);
                //}
            }
        }
        return  userInfoLists;
    }
}
