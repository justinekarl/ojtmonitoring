package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateStudentSectionActivity extends AppCompatActivity {

    private EditText txtSectionName;
    private Button btnSaveSection;
    private Button btnCancelSection;
    private int teacherId;
    private String sectionName;
    private Button viewSectionsBtn;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student_section);
        PaceSettingManager.lockActivityOrientation(this);

        txtSectionName = (EditText) findViewById(R.id.txtSectionName);
        btnSaveSection = (Button) findViewById(R.id.btnSaveSection);
        btnCancelSection = (Button) findViewById(R.id.btnCancelSection);
        viewSectionsBtn = (Button) findViewById(R.id.viewSectionsBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);


        btnSaveSection.setOnTouchListener(new View.OnTouchListener() {
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
                        if(null != txtSectionName && txtSectionName.getText().toString().trim().length() > 0){
                            sectionName = txtSectionName.getText().toString();

                            SaveStudentSection saveStudentSection = new SaveStudentSection();
                            saveStudentSection.execute();
                        }else{
                            toastMessage("Section name must be filled out.");
                        }
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        /*btnSaveSection.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != txtSectionName){
                            sectionName = txtSectionName.getText().toString();

                            SaveStudentSection saveStudentSection = new SaveStudentSection();
                            saveStudentSection.execute();
                        }
                    }
                }
        );*/


        btnCancelSection.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent back = new Intent(CreateStudentSectionActivity.this,TeacherLoginActivity.class);
                        startActivity(back);
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        /*btnCancelSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(CreateStudentSectionActivity.this,TeacherLoginActivity.class);
                startActivity(back);
            }
        });*/

       /* viewSectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showSections = new Intent(CreateStudentSectionActivity.this,ViewSectionsActivity.class);
                startActivity(showSections);
            }
        });*/

        viewSectionsBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent showSections = new Intent(CreateStudentSectionActivity.this,ViewSectionsActivity.class);
                        startActivity(showSections);
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    class SaveStudentSection extends AsyncTask<String, String, String> {
        boolean savedSuccessfully = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateStudentSectionActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public SaveStudentSection() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));
            params.add(new BasicNameValuePair("sectionName",sectionName));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"save_student_section.php",
                    "POST", params);

            try {
                int success = json.getInt("success");

                if(success == 1){
                    savedSuccessfully = true;
                }

            }catch (Exception e){
                e.printStackTrace();
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(savedSuccessfully){
                toastMessage("Successfully saved!");
            }

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(CreateStudentSectionActivity.this,TeacherLoginActivity.class);
        startActivity(home);
    }
}
