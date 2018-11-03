package com.example.ojtmonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jj on 8/19/18.
 */

public class CustomMenuAdapter extends BaseAdapter {
    private Context mContext;
    private String[]  Title;
    private int[] imge;

    public CustomMenuAdapter(Context context, String[] text1,int[] imageIds) {
        mContext = context;
        Title = text1;
        imge = imageIds;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(mContext, R.layout.custom_menu_layout,null);
        TextView title;
        ImageView i1;
        i1 = (ImageView) view.findViewById(R.id.imgIcon);
        i1.setBackgroundResource(R.color.zxing_transparent);
        title = (TextView) view.findViewById(R.id.txtTitle);
        i1.setImageResource(imge[position]);
        title.setText(Title[position]);

        return (view);
    }

}
