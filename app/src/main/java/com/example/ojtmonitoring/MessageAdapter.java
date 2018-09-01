package com.example.ojtmonitoring;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;
    String userName;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);

        SharedPreferences sharedPreferences = context.getSharedPreferences(PaceSettingManager.USER_PREFERENCES, context.MODE_PRIVATE);

        userName = sharedPreferences.getString("user_name","");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.item_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername(),position);


    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;
        private LinearLayout msgBckground;



        public ViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            msgBckground = (LinearLayout) itemView.findViewById(R.id.msgBckground);

        }

        public void setUsername(String username,int position) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
            mUsernameView.setTextColor(getUsernameColor(username));

            /*if(username.equals(userName)){
                msgBckground.removeView(mUsernameView);
                msgBckground.removeView(mMessageView);
                msgBckground.addView(mMessageView);
                msgBckground.addView(mUsernameView);

                mMessageView.setPadding(200,0,0,0);
                mUsernameView.setGravity(Gravity.RIGHT);
                mUsernameView.setPadding(100,0,10,0);
                //mUsernameView.setLa
            }*/

            if(position%2 == 0){
                msgBckground.setBackgroundColor(mUsernameView.getContext().getResources().getColor(R.color.divider));
            }else{
                msgBckground.setBackgroundColor(mUsernameView.getContext().getResources().getColor(R.color.white));
            }
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

        private int getUsernameColor(String username) {
            if(null !=username){
                int hash = 7;
                for (int i = 0, len = username.length(); i < len; i++) {
                    hash = username.codePointAt(i) + (hash << 5) - hash;
                }
                int index = Math.abs(hash % mUsernameColors.length);
                return mUsernameColors[index];
            }return 0;
        }
    }

}

