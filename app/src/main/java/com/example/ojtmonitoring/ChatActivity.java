package com.example.ojtmonitoring;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    EditText messageTxt;
    ImageButton sendBtn;
    RecyclerView messagesList;
    Context context;
    int senderId;
    int receiverId;
    private RecyclerView.Adapter mAdapter;
    public static List<Message> mMessages = new ArrayList<Message>();
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;
        messageTxt = (EditText) findViewById(R.id.messageTxt);
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);
        messagesList = (RecyclerView)findViewById(R.id.messagesList);
        mAdapter = new MessageAdapter(context, mMessages);

        messagesList.setLayoutManager(new LinearLayoutManager(this));

        messagesList.setAdapter(mAdapter);


        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        senderId = sharedPreferences.getInt("agent_id",0);

        if(getIntent().getIntExtra("receiverId",0) > 0){
            receiverId = getIntent().getIntExtra("receiverId",0);
        }

        if(null != getIntent().getStringExtra("userName")){
            userName = getIntent().getStringExtra("userName");
        }

        /*mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(userName).message("Test Message").receiverId(receiverId).senderId(senderId).build());*/

        mAdapter.notifyItemInserted(mMessages.size() - 1);

        scrollToBottom();

        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {

                try{
                    JSONObject params = new JSONObject();
                    if(null != messageTxt.getText() &&  messageTxt.getText().toString().trim().length() > 0 ) {
                        params.put("message", messageTxt.getText().toString());
                        params.put("sender_id", senderId);
                        params.put("receiver_id", receiverId);
                        String url = PaceSettingManager.IP_ADDRESS + "sendMessage";
                        MakeHttpRequest.RequestPost(context, url, params, ChatActivity.this, ChatActivity.class,senderId,receiverId);



                       /* JSONObject params1 = new JSONObject();
                        params.put("sender_id", senderId);
                        params.put("receiver_id", receiverId);
                        String getMessageUrl = PaceSettingManager.IP_ADDRESS + "getMessage";
                        MakeHttpRequest.RequestPostMessage(context, getMessageUrl, params1, ChatActivity.this, ChatActivity.class);*/

                        //mAdapter.notifyItemInserted(mMessages.size() - 1);

                        scrollToBottom();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }

    private void scrollToBottom() {
        messagesList.scrollToPosition(mAdapter.getItemCount() - 1);
    }
}
