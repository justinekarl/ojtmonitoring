package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student_section);

        txtSectionName = (EditText) findViewById(R.id.txtSectionName);
        btnSaveSection = (Button) findViewById(R.id.btnSaveSection);
        btnCancelSection = (Button) findViewById(R.id.btnCancelSection);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);

        btnSaveSection.setOnClickListener(
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
        );

        btnCancelSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(CreateStudentSectionActivity.this,TeacherLoginActivity.class);
                startActivity(back);
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

}
