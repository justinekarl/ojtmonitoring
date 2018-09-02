package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ojtmonitoring.info.UserInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ViewTeachersActivity extends AppCompatActivity {

    private ListView teacherLstView;
    private int agentId;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private List<Pair<Integer,String>> teachersList = new ArrayList<>();
    ArrayAdapter<String> menuAdapter = null;

    private Socket mSocket;
    private String userName;
    CustomUserItemListView userItemAdapter;
    private List<UserInfo> teacherInfoList = new ArrayList<>();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teachers);
        PaceSettingManager.lockActivityOrientation(this);
        teacherLstView = (ListView)findViewById(R.id.teacherLstView);
        context = this;

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        userName = sharedPreferences.getString("user_name","");


        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        teacherLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //commented out socketio implementation
                //Intent messageTeacher = new Intent(getApplicationContext(),MessageTeacherActivity.class);
                Intent messageTeacher = new Intent(getApplicationContext(),ChatActivity.class);
                if(null != userItemAdapter && null != userItemAdapter.getUserInfoLists() && userItemAdapter.getUserInfoLists().size() > 0) {

                    UserInfo userInfo =  userItemAdapter.getUserInfoLists().get(position);
                    messageTeacher.putExtra("receiverId", userInfo.getId());
                    messageTeacher.putExtra("userName",userInfo.getUserName());

                    try{
                        JSONObject params = new JSONObject();
                        params.put("sender_id", agentId);
                        params.put("receiver_id", userInfo.getId());
                        String url = PaceSettingManager.IP_ADDRESS + "getMessage";
                        MakeHttpRequest.RequestPostMessage(context, url, params, ViewTeachersActivity.this, ChatActivity.class,agentId,userInfo.getId());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                //startActivity(messageTeacher);
                //finish();
            }
        });
    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewTeachersActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",agentId+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getTeachersByCourses.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("teacher_lists") && null != json.getJSONArray("teacher_lists")){

                            teacherInfoList.clear();

                            for (int i=0;i<json.getJSONArray("teacher_lists").length() ; i++){
                                final StringBuffer sbName= new StringBuffer("");
                                int id = 0;

                                UserInfo teacherInfo = new UserInfo(2);

                                for (int k = 0; k < json.getJSONArray("teacher_lists").getJSONArray(i).length() ; k++) {



                                    if(k==0) {
                                        teacherInfo.setId(Integer.valueOf(json.getJSONArray("teacher_lists").getJSONArray(i).getInt(k) + ""));
                                    }
                                    if(k==1) {
                                        if(null != json.getJSONArray("teacher_lists").getJSONArray(i) && null != json.getJSONArray("teacher_lists").getJSONArray(i).get(k)) {
                                            teacherInfo.setName(json.getJSONArray("teacher_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }

                                    if(k==2) {
                                        if(null != json.getJSONArray("teacher_lists").getJSONArray(i) && null != json.getJSONArray("teacher_lists").getJSONArray(i).get(k)) {
                                            teacherInfo.setUserName(json.getJSONArray("teacher_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }

                                    if(k==3) {
                                        if(null != json.getJSONArray("teacher_lists").getJSONArray(i) && null != json.getJSONArray("teacher_lists").getJSONArray(i).get(k)) {
                                            teacherInfo.setOnline( (json.getJSONArray("teacher_lists").getJSONArray(i).get(k) + "").equals("1") ? true : false);
                                        }
                                    }

                                  /*  if(k==4) {
                                        if(null != json.getJSONArray("teacher_lists").getJSONArray(i) && null != json.getJSONArray("teacher_lists").getJSONArray(i).get(k)) {
                                            teacherInfo.setAddress(json.getJSONArray("teacher_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }*/


                                }


                                teacherInfoList.add(teacherInfo);
                            }
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

            userItemAdapter = new CustomUserItemListView(teacherInfoList,ViewTeachersActivity.this);

            teacherLstView.setAdapter(userItemAdapter);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToPrev = new Intent(ViewTeachersActivity.this,CompanyLoginActivity.class);
        startActivity(backToPrev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //mSocket.off("login", onLogin);
    }
}
