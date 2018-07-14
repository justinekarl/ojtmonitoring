package com.example.ojtmonitoring;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ojtmonitoring.info.CourseInfo;

import java.util.ArrayList;
import java.util.List;

public class CustomCourseListView extends BaseAdapter {
    private List<CourseInfo> courseInfos;
    Context context;
    CheckBox selectedChBx;
    TextView courseTxt;
    private CourseInfo courseInfo;

    public CustomCourseListView(List<CourseInfo> courseInfos ,Context context){
        this.courseInfos = courseInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != courseInfos){
            return  courseInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return courseInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<CourseInfo> getCourseInfoList(){
        ArrayList<CourseInfo> infos = new ArrayList<CourseInfo>();
        if(null != courseInfos) {
            for (CourseInfo courseInfo : courseInfos) {
                if(courseInfo.isSelected()) {
                    infos.add(courseInfo);
                }
            }
        }
        return infos;
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (null != courseInfos) {
                courseInfos.get((Integer) buttonView.getTag()).setSelected(isChecked);
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context,R.layout.custom_course_row,null);

        selectedChBx = (CheckBox)view.findViewById(R.id.selectedChBx);
        courseTxt = (TextView)view.findViewById(R.id.courseTxt);

        courseInfo = courseInfos.get(position);
        if(null != courseInfo){
            courseTxt.setText(courseInfo.getName());
            selectedChBx.setChecked(courseInfo.isSelected());
            selectedChBx.setTag(position);
            selectedChBx.setOnCheckedChangeListener(onCheckedChangeListener);
            view.setTag(courseInfo.getId());
        }

        return view;
    }
}
