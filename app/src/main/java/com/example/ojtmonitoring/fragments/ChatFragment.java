package com.example.ojtmonitoring.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ojtmonitoring.MessagesAdapter;
import com.example.ojtmonitoring.PaceSettingManager;
import com.example.ojtmonitoring.R;
import com.example.ojtmonitoring.info.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by jj on 8/12/18.
 */

public class ChatFragment extends Fragment {

    private EditText messageTxt;
    //private ImageButton sendBtn;
    private RecyclerView messages;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView.Adapter mAdapter;
    private List<Message> mMessages = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    private Socket socket;
    {
        try{
            socket = IO.socket(PaceSettingManager.CHAT_SERVER_ADDRESS);
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }


    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        socket.connect();
        socket.on("message", handleIncomingMessages);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mAdapter = new MessagesAdapter( mMessages);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chatfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messages = (RecyclerView) view.findViewById(R.id.messages);
        messages.setLayoutManager(new LinearLayoutManager(getActivity()));
        messages.setAdapter(mAdapter);

        ImageButton sendBtn = (ImageButton) view.findViewById(R.id.sendBtn);
        messageTxt = (EditText) view.findViewById(R.id.messageTxt);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Bitmap decodeImage(String data)
    {
        byte[] b = Base64.decode(data,Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }
    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message="";
                    String imageText;
                    try {
                        message = data.getString("text").toString();


                    } catch (JSONException e) {
                        // return;
                    }

                    addMessage(message);
                    /*try {
                        imageText = data.getString("image");
                        addImage(decodeImage(imageText));
                    } catch (JSONException e) {
                        //retur
                    }*/

                }
            });
        }
    };


    private void sendMessage(){
        String message = messageTxt.getText().toString().trim();
        messageTxt.setText("");

        JSONObject sendText = new JSONObject();
        try{
            sendText.put("text",message);
            socket.emit("message", sendText);
        }catch(JSONException e){

        }
        addMessage(message);
    }

    private void addMessage(String message) {

        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .message(message).build());
        // mAdapter = new MessageAdapter(mMessages);
        mAdapter = new MessagesAdapter(mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();
    }

    private void addImage(Bitmap bmp){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .image(bmp).build());
        mAdapter = new MessagesAdapter( mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();
    }
    private void scrollToBottom() {
        messages.scrollToPosition(mAdapter.getItemCount() - 1);
    }

}
