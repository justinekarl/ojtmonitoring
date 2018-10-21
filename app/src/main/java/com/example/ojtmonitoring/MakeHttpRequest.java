package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakeHttpRequest {


    public static void RequstGet(final Context context, String url, String method, JSONObject params, final Context from,final Class<?> to){
        final ProgressDialog pDialog;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String test = response.getString("result");
                            Intent move = new Intent(from, to);
                            context.startActivity(move);
                            pDialog.dismiss();
                        }catch (JSONException e){

                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        pDialog.dismiss();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    public static void RequestPost(final Context context, String url, JSONObject params, final Context from, final Class<?> to,final int sender, final int receiver){
        final ProgressDialog pDialog;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            ChatActivity.mMessages.clear();
                            if(response.has("message") && null != response.getJSONArray("message")){
                                for (int i=0;i<response.getJSONArray("message").length() ; i++){

                                    String msg = response.getJSONArray("message").getJSONObject(i).getString("message");
                                    int sender = response.getJSONArray("message").getJSONObject(i).getInt("sender_id");
                                    int receiver = response.getJSONArray("message").getJSONObject(i).getInt("receiver_id");
                                    String userName = response.getJSONArray("message").getJSONObject(i).getString("sender");
                                    ChatActivity.mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                                            .message(msg).receiverId(receiver).senderId(sender).username(userName).build());


                                }
                            }

                            Intent move = new Intent(from, to);
                            move.putExtra("receiverId",receiver);
                            move.putExtra("messageJson",response.getJSONArray("message").toString());
                            context.startActivity(move);
                        }catch (Exception e){
                            pDialog.dismiss();

                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        pDialog.dismiss();

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public static void RequestPostMessage(final Context context, String url, JSONObject params, final Context from, final Class<?> to, final int sender, final int receiver){
        final ProgressDialog pDialog;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            boolean result = response.getBoolean("response");

                            pDialog.dismiss();
                            //PaceSettingManager.toastMessage(context, "sa");
                            if(result){
                                ChatActivity.mMessages.clear();
                                if(response.has("message") && null != response.getJSONArray("message")){
                                    for (int i=0;i<response.getJSONArray("message").length() ; i++){

                                        String msg = response.getJSONArray("message").getJSONObject(i).getString("message");
                                        int sender = response.getJSONArray("message").getJSONObject(i).getInt("sender_id");
                                        int receiver = response.getJSONArray("message").getJSONObject(i).getInt("receiver_id");
                                        String userName = response.getJSONArray("message").getJSONObject(i).getString("sender");
                                        ChatActivity.mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                                                .message(msg).receiverId(receiver).senderId(sender).username(userName).build());


                                    }
                                }

                                Intent move = new Intent(from, to);
                              //  move.putExtra("receiverId", sender);
                                move.putExtra("receiverId",receiver);
                                move.putExtra("messageJson",response.getJSONArray("message").toString());
                                context.startActivity(move);
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            pDialog.dismiss();

                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        pDialog.dismiss();

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    public static synchronized void RequestPostMessageTest(final Context context, String url, JSONObject params, final int sender, final int receiver){
        final ProgressDialog pDialog;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public synchronized void onResponse(JSONObject response) {
                        try{
                            MessageAdapter msgAdapter ;
                            boolean result = response.getBoolean("response");

                            pDialog.dismiss();
                            //PaceSettingManager.toastMessage(context, "sa");
                            if(result){
                                ChatActivity.mMessages.clear();
                                if(response.has("message") && null != response.getJSONArray("message")){
                                    for (int i=0;i<response.getJSONArray("message").length() ; i++){

                                        String msg = response.getJSONArray("message").getJSONObject(i).getString("message");
                                        int sender = response.getJSONArray("message").getJSONObject(i).getInt("sender_id");
                                        int receiver = response.getJSONArray("message").getJSONObject(i).getInt("receiver_id");
                                        String userName = response.getJSONArray("message").getJSONObject(i).getString("sender");
                                        ChatActivity.mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                                                .message(msg).receiverId(receiver).senderId(sender).username(userName).build());


                                    }
                                }

                                /*Intent move = new Intent(from, to);
                                //  move.putExtra("receiverId", sender);
                                move.putExtra("receiverId",receiver);
                                move.putExtra("messageJson",response.getJSONArray("message").toString());
                                context.startActivity(move);*/
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            pDialog.dismiss();

                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        pDialog.dismiss();

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    public static void getBackGround(final Context context,final String url, RequestQueue requestQueue) {
        try {
            //RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean hasMessage = response.getBoolean("response");
                                if (hasMessage) {
                                    String message = response.getString("message");
                                    String sender = response.getString("sender");
                                    int receiverId = response.getInt("receiverId");
                                    int senderId = response.getInt("senderId");
                                    PaceSettingManager.sendNotification(context, message, sender, senderId, senderId);

                                    //Log.i(TAG,message);
                                }

                            } catch (JSONException e) {
                                //PaceSettingManager.toastMessage(context, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PaceSettingManager.toastMessage(context, error.getMessage());
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            /*Log.i("AAAAAAAAAAA",e.getMessage());
            PaceSettingManager.toastMessage(context,"AAAAAAAAAAA");*/
        }
    }

    public static void getTransactionLog(final Context context, final String url, RequestQueue requestQueue, final String entityType){
        try {
            //RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean hasMessage = response.getBoolean("response");
                                boolean loggedIn = false;
                                StringBuilder sb = new StringBuilder("");
                                if (hasMessage) {

                                    if(response.has("log")){
                                        JSONArray jsonArray = response.getJSONArray("log");


                                        if (null != jsonArray && jsonArray.length() > 0) {
                                            for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                                                //for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {jsonArray.getJSONObject(0).get("login_date")
                                                if (null != jsonArray.getJSONObject(ctr)) {
                                                    if (null != jsonArray.getJSONObject(ctr).get("action")) {
                                                        sb.append(jsonArray.getJSONObject(ctr).get("action"));
                                                        sb.append("\n");
                                                    }

                                                    if(entityType.equals("Student") || entityType.equals("Teacher")){
                                                        if (null != jsonArray.getJSONObject(ctr).get("saved_by")) {
                                                            sb.append("Updated by :" +jsonArray.getJSONObject(ctr).get("saved_by"));
                                                            sb.append("\n");
                                                        }
                                                    }


                                                }

                                            }
                                            //}
                                            PaceSettingManager.createTransactionNotification(context, sb.toString(), entityType);
                                        }

                                    }




                                    //Log.i(TAG,message);
                                }

                            } catch (JSONException e) {
                                PaceSettingManager.toastMessage(context, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PaceSettingManager.toastMessage(context, error.getMessage());
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            /*Log.i("AAAAAAAAAAA",e.getMessage());
            PaceSettingManager.toastMessage(context,"AAAAAAAAAAA");*/
        }
    }



    public static void getTeacherTransactionLog(final Context context, final String url, RequestQueue requestQueue, final String entityType){
        try {
            //RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean hasMessage = response.getBoolean("response");
                                boolean loggedIn = false;
                                StringBuilder sb = new StringBuilder("");
                                if (hasMessage) {

                                    if(response.has("log")){
                                        JSONArray jsonArray = response.getJSONArray("log");


                                        if (null != jsonArray && jsonArray.length() > 0) {
                                            for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                                                //for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {jsonArray.getJSONObject(0).get("login_date")
                                                if (null != jsonArray.getJSONObject(ctr)) {
                                                    if (null != jsonArray.getJSONObject(ctr).get("action")) {
                                                        sb.append(jsonArray.getJSONObject(ctr).get("action"));
                                                        sb.append("\n");
                                                    }

                                                    if(entityType.equals("Teacher")){
                                                        if (null != jsonArray.getJSONObject(ctr).get("saved_by")) {
                                                            sb.append("Updated by :" +jsonArray.getJSONObject(ctr).get("saved_by"));
                                                            sb.append("\n");
                                                        }
                                                    }


                                                }

                                            }
                                            //}
                                            PaceSettingManager.createTransactionNotification(context, sb.toString(), entityType);
                                        }

                                    }




                                    //Log.i(TAG,message);
                                }

                            } catch (JSONException e) {
                                PaceSettingManager.toastMessage(context, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PaceSettingManager.toastMessage(context, error.getMessage());
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            /*Log.i("AAAAAAAAAAA",e.getMessage());
            PaceSettingManager.toastMessage(context,"AAAAAAAAAAA");*/
        }
    }


    public static void getCompanyTransactionLog(final Context context, final String url, RequestQueue requestQueue, final String entityType){
        try {
            //RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean hasMessage = response.getBoolean("response");
                                boolean loggedIn = false;
                                StringBuilder sb = new StringBuilder("");
                                if (hasMessage) {

                                    if(response.has("log")){
                                        JSONArray jsonArray = response.getJSONArray("log");


                                        if (null != jsonArray && jsonArray.length() > 0) {
                                            for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                                                //for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {jsonArray.getJSONObject(0).get("login_date")
                                                if (null != jsonArray.getJSONObject(ctr)) {
                                                    if (null != jsonArray.getJSONObject(ctr).get("action")) {
                                                        sb.append(jsonArray.getJSONObject(ctr).get("action"));
                                                        sb.append("\n");
                                                    }

                                                    if(entityType.equals("Company")){
                                                        if (null != jsonArray.getJSONObject(ctr).get("saved_by")) {
                                                            sb.append("Updated by :" +jsonArray.getJSONObject(ctr).get("saved_by"));
                                                            sb.append("\n");
                                                        }
                                                    }


                                                }

                                            }
                                            //}
                                            PaceSettingManager.createTransactionNotification(context, sb.toString(), entityType);
                                        }

                                    }




                                    //Log.i(TAG,message);
                                }

                            } catch (JSONException e) {
                                PaceSettingManager.toastMessage(context, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PaceSettingManager.toastMessage(context, error.getMessage());
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            /*Log.i("AAAAAAAAAAA",e.getMessage());
            PaceSettingManager.toastMessage(context,"AAAAAAAAAAA");*/
        }
    }

    public static void getStudentLog(final Context context,final String url, RequestQueue requestQueue) {
        try {
            //RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean hasMessage = response.getBoolean("response");
                                boolean loggedIn = false;
                                StringBuilder sb = new StringBuilder("");
                                if (hasMessage) {

                                    if(response.has("log")){
                                        JSONArray jsonArray = response.getJSONArray("log");


                                        if (null != jsonArray && jsonArray.length() > 0) {
                                            for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                                                //for (int i = 0; i < jsonArray.getJSONArray(ctr).length(); i++) {jsonArray.getJSONObject(0).get("login_date")
                                                    if (null != jsonArray.getJSONObject(ctr)) {

                                                        if (null != jsonArray.getJSONObject(ctr).get("login_date")
                                                                && !jsonArray.getJSONObject(ctr).get("login_date").toString().equals("null")) {
                                                            loggedIn = true;
                                                            sb.append("Logged IN : " + jsonArray.getJSONObject(ctr).get("login_date"));
                                                            sb.append("\n");
                                                        }



                                                        if (null != jsonArray.getJSONObject(ctr).get("logout_date")
                                                                && !jsonArray.getJSONObject(ctr).get("logout_date").toString().equals("null")) {
                                                            loggedIn = false;
                                                            sb.append("Logged OUT : " + jsonArray.getJSONObject(ctr).get("logout_date"));
                                                            sb.append("\n");
                                                        }



                                                        if (null != jsonArray.getJSONObject(ctr).get("company_name")) {
                                                            sb.append("Company  : " + jsonArray.getJSONObject(ctr).get("company_name"));
                                                            sb.append("\n");
                                                        }

                                                    }

                                                }
                                            //}
                                            PaceSettingManager.createStudentLogNotification(context, sb.toString(), loggedIn);
                                        }

                                    }




                                    //Log.i(TAG,message);
                                }

                            } catch (JSONException e) {
                                PaceSettingManager.toastMessage(context, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PaceSettingManager.toastMessage(context, error.getMessage());
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            /*Log.i("AAAAAAAAAAA",e.getMessage());
            PaceSettingManager.toastMessage(context,"AAAAAAAAAAA");*/
        }
    }

}
