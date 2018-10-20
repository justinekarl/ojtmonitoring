package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ojtmonitoring.fragments.ChatFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;

public class MessageTeacherActivity extends AppCompatActivity {

    ListView messagesList;
    EditText messageTxt;
    Button sendBtn;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private int agentId;
    private int teacherId;
    private int accountType;
    private ProgressDialog progressDialog;
    ChatFragment chatFragment;
    //MainFragment mainFragment;

    private Socket mSocket;
    private String userNameToSend;
    private String userNameToReceive;
    private Pair usernamePair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_teacher);
        userNameToReceive = getIntent().getStringExtra("userName");

        progressDialog = new ProgressDialog(this);

        teacherId = getIntent().getIntExtra("teacherId",0);

        /*messagesList = (ListView)findViewById(R.id.messagesList);
        messageTxt = (EditText)findViewById(R.id.messageTxt);
        sendBtn = (Button)findViewById(R.id.sendBtn);*/

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        accountType = sharedPreferences.getInt("accounttype",0);
        userNameToSend = sharedPreferences.getString("user_name","");

        /*ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
       // mSocket.on("adduser", onConnect);
        //mSocket.emit("adduser", userName);
        mSocket.emit("check_user", userNameToReceive,userNameToSend);
        mSocket.emit("msg_user_found_new", userNameToReceive);

        usernamePair = new Pair(userNameToSend,userNameToReceive);

        onFragmentInteraction(usernamePair);*/






        /*sendBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        progressDialog.setMessage("Sending..");
                        progressDialog.show();

                        String message = null != messageTxt.getText() ? messageTxt.getText().toString() : "";
                        //lets sent the message here
                        sendMessage(agentId,teacherId,message,accountType);

                        messageTxt.setText("");

                        progressDialog.hide();

                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });*/
    }

    public void sendMessage(int senderId,int recipientId,String message,int senderType){
        if(!TextUtils.isEmpty(message)){
            SendMessage sendMessage = new SendMessage(senderId,senderType,recipientId,message);
            sendMessage.execute();
        }
    }


    class SendMessage extends AsyncTask<String, String, String> {
        private int senderId;
        private int senderType;
        private int recipientId;
        private String message;
        private boolean sent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MessageTeacherActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public SendMessage(int senderId,int senderType,int recipientId,String message) {
            this.senderId = senderId;
            this.senderType = senderType;
            this.recipientId = recipientId;
            this.message = message;
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("senderId",senderId+""));
            params.add(new BasicNameValuePair("recipientId",recipientId+""));
            params.add(new BasicNameValuePair("message",message+""));
            params.add(new BasicNameValuePair("senderType",senderType+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"sendMessage.php",
                    "POST", params);



            try {
                if(null != json) {
                    // check log cat fro response
                    Log.d("Create Response", json.toString());


                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("message") && null != json.getString("message")){
                            sent = true;
                        }
                    }
                }else{
                    //loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(sent){
                toastMessage("Message Sent!");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    /*@Override
    public Pair onFragmentInteraction(Pair pair) {
        return pair;
    }

    @Override
    public Pair onFragmentInteractionPair() {
        return onFragmentInteraction(usernamePair);
    }*/



}
