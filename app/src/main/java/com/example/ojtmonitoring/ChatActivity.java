package com.example.ojtmonitoring;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    EditText messageTxt;
    ImageButton sendBtn;
    RecyclerView messagesList;
    PullRefreshLayout refreshLayout;
    Context context;
    int senderId;
    int receiverId;
    public static RecyclerView.Adapter mAdapter;
    public static List<Message> mMessages = new ArrayList<Message>();
    String userName;
    int accountType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = this;
        messageTxt = (EditText) findViewById(R.id.messageTxt);
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);
        messagesList = (RecyclerView)findViewById(R.id.messagesList);
        refreshLayout = (PullRefreshLayout)findViewById(R.id.refreshLayout);

        messagesList.setLayoutManager(new LinearLayoutManager(this));



        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        senderId = sharedPreferences.getInt("agent_id",0);
        accountType = sharedPreferences.getInt("accounttype",0);

        if(getIntent().getIntExtra("receiverId",0) > 0){
            receiverId = getIntent().getIntExtra("receiverId",0);
        }

        if(null != getIntent().getStringExtra("userName")){
            userName = getIntent().getStringExtra("userName");
        }
        try{
            JSONObject params = new JSONObject();
            params.put("sender_id", senderId);
            params.put("receiver_id", receiverId);
            String url = PaceSettingManager.IP_ADDRESS + "getMessage";
            MakeHttpRequest.RequestPostMessageTest(context, url, params,senderId,receiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        /*mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(userName).message("Test Message").receiverId(receiverId).senderId(senderId).build());*/

        setEvents();

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

        processRecyclerViewData();
        //scrollToBottom();

    }

    private void processRecyclerViewData(){
        mAdapter = new MessageAdapter(context, mMessages);

        messagesList.setAdapter(mAdapter);
        //mAdapter.notifyItemInserted(mMessages.size() - 1);
         scrollToBottom();

    }

    private void scrollToBottom() {
        messagesList.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    synchronized void setEvents(){
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new AsyncTask<Void,String,String>(){
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            Thread.sleep(2000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        try{
                            JSONObject params = new JSONObject();
                            params.put("sender_id", senderId);
                            params.put("receiver_id", receiverId);
                            String url = PaceSettingManager.IP_ADDRESS + "getMessage";
                            MakeHttpRequest.RequestPostMessageTest(context, url, params,senderId,receiverId);

                            /*mAdapter = new MessageAdapter(context, mMessages){
                                @Override
                                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    return super.onCreateViewHolder(parent, viewType);
                                }
                            };
                            mAdapter.notifyItemInserted(mMessages.size() - 1);
                            messagesList.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

                            messagesList.setAdapter(mAdapter);
*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /*mAdapter.notifyItemInserted(mMessages.size());
                        mAdapter.notifyDataSetChanged();*/

                        refreshLayout.setRefreshing(false);
                    }
                }.execute();
            }
        });

        //processRecyclerViewData();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent back = new Intent(this,ViewCompaniesActivity.class);
        if(accountType == 2){
            back = new Intent(this,ViewCompaniesActivity.class);
        }

        if(accountType == 3){
            back = new Intent(this,ViewTeachersActivity.class);
        }

        if(null != back) {
            startActivity(back);
        }
        finish();
    }
}
