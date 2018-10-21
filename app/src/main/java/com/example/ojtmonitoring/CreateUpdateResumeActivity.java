package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ojtmonitoring.fragments.StudentAccomplishmentsFragment;
import com.example.ojtmonitoring.fragments.StudentPersonalInformationFragment;
import com.example.ojtmonitoring.fragments.StudentReferenceFragment;
import com.example.ojtmonitoring.info.AccomplishmentsInterestInfo;
import com.example.ojtmonitoring.info.EducationalBackgroundInfo;
import com.example.ojtmonitoring.info.ReferencesInfo;
import com.example.ojtmonitoring.info.ResumeInfo;
import com.example.ojtmonitoring.info.StudentPersonalInformationInfo;
import com.example.ojtmonitoring.info.WorkExperienceInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateUpdateResumeActivity extends AppCompatActivity implements StudentPersonalInformationFragment.PersonalInformationListener, StudentAccomplishmentsFragment.StudentAccomplishmentsInterface, StudentReferenceFragment.ReferenceInterface{

    StudentPersonalInformationFragment personalInformationFragment;
    StudentAccomplishmentsFragment accomplishmentsFragment;
    StudentReferenceFragment referenceFragment;
    public static Button createResume;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    ResumeInfo resumeInfo;
   // StudentPersonalInformationInforesumeInfo.getStudentPersonalInformationInfo().
    private static int agentId;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_resume);
        PaceSettingManager.lockActivityOrientation(this);

        createResume = (Button)findViewById(R.id.createResume);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);

        resumeInfo = new ResumeInfo();


        CreateUpdateResumeActivity.RetrieveResume retrieveResume = new CreateUpdateResumeActivity.RetrieveResume();
        retrieveResume.execute();


        createResume.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        personalInformationFragment =  (StudentPersonalInformationFragment)getSupportFragmentManager().findFragmentById(R.id.personalInfofragment);

                        //ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_student_basic_info, null, false);

                        if(null != personalInformationFragment && personalInformationFragment.isInLayout() && null != personalInformationFragment.getActivity()){
                            if(null != resumeInfo && null == resumeInfo.getStudentPersonalInformationInfo()) {
                                resumeInfo.setStudentPersonalInformationInfo(new StudentPersonalInformationInfo());
                            }
                            if(null != personalInformationFragment.getActivity().findViewById(R.id.nameTxt)) {
                                resumeInfo.getStudentPersonalInformationInfo().setName(((EditText) personalInformationFragment.getActivity().findViewById(R.id.nameTxt)).getText().toString());
                            }
                        }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.elemAddTxt)) {
                                resumeInfo.getStudentPersonalInformationInfo().setEmail(((EditText) personalInformationFragment.getActivity().findViewById(R.id.elemAddTxt)).getText().toString());
                            }
                            if(null != personalInformationFragment.getActivity().findViewById(R.id.addressTxt)) {
                                resumeInfo.getStudentPersonalInformationInfo().setAddress(((EditText) personalInformationFragment.getActivity().findViewById(R.id.addressTxt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.phoneNumberTxt)) {
                                resumeInfo.getStudentPersonalInformationInfo().setPhoneNumber(((EditText) personalInformationFragment.getActivity().findViewById(R.id.phoneNumberTxt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.genderSpnr)){
                                resumeInfo.getStudentPersonalInformationInfo().setGender(((Spinner)personalInformationFragment.getActivity().findViewById(R.id.genderSpnr)).getSelectedItem().toString());
                            }

                            if(null != resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos() && resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().size() == 0 ){
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().add(new WorkExperienceInfo());
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().add(new WorkExperienceInfo());
                            }

                            //work experience 1
                            if(null != personalInformationFragment.getActivity().findViewById(R.id.companyName1Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(0)
                                        .setCompanyName(((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyName1Txt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.companyAddress1Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(0)
                                        .setAddress(((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyAddress1Txt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.jobDescription1Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(0)
                                        .setJobDescription(((EditText) personalInformationFragment.getActivity().findViewById(R.id.jobDescription1Txt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.dutiesCompany1Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(0)
                                        .setDutiesResponsibilities(((EditText) personalInformationFragment.getActivity().findViewById(R.id.dutiesCompany1Txt)).getText().toString());
                            }


                            //work experience 2
                            if(null != personalInformationFragment.getActivity().findViewById(R.id.companyName2Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(1)
                                        .setCompanyName(((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyName2Txt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.companyAddress2Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(1)
                                        .setAddress(((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyAddress2Txt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.jobDescription2Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(1)
                                        .setJobDescription(((EditText) personalInformationFragment.getActivity().findViewById(R.id.jobDescription2Txt)).getText().toString());
                            }

                            if(null != personalInformationFragment.getActivity().findViewById(R.id.dutiesCompany2Txt)) {
                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().get(1)
                                        .setDutiesResponsibilities(((EditText) personalInformationFragment.getActivity().findViewById(R.id.dutiesCompany2Txt)).getText().toString());
                            }





                        accomplishmentsFragment =  (StudentAccomplishmentsFragment)getSupportFragmentManager().findFragmentById(R.id.accomplishmentsfragment);

                        if(null == resumeInfo.getAccomplishmentsInterestInfo()){
                            resumeInfo.setAccomplishmentsInterestInfo(new AccomplishmentsInterestInfo());
                        }

                        if(null != accomplishmentsFragment && accomplishmentsFragment.isInLayout() && null != accomplishmentsFragment.getActivity()){

                            resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().clear();

                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.accomplishmentsTxt)) {
                                resumeInfo.getAccomplishmentsInterestInfo().setAccomplishments(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.accomplishmentsTxt)).getText().toString());
                            }

                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.interestTxt)) {
                                resumeInfo.getAccomplishmentsInterestInfo().setInterests(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.interestTxt)).getText().toString());
                            }




                            final EducationalBackgroundInfo educationalBackground1 = new EducationalBackgroundInfo();
                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.elementaryTxt)) {
                                educationalBackground1.setSchoolName(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.elementaryTxt)).getText().toString());
                            }
                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.elemAddTxt)) {
                                educationalBackground1.setAddress(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.elemAddressTxt)).getText().toString());
                            }
                            educationalBackground1.setType(0);

                            resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().add(educationalBackground1);


                            final EducationalBackgroundInfo educationalBackground2 = new EducationalBackgroundInfo();

                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.highSchoolTxt)){
                                educationalBackground2.setSchoolName(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.jobDescription1Txt)).getText().toString());
                            }

                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.highSchoolAddress1Txt)) {
                                educationalBackground2.setAddress(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.highSchoolAddress1Txt)).getText().toString());
                            }
                            educationalBackground2.setType(1);

                            resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().add(educationalBackground2);

                            final EducationalBackgroundInfo educationalBackground3 = new EducationalBackgroundInfo();

                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.collegeTxt)) {
                                educationalBackground3.setSchoolName(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.collegeTxt)).getText().toString());
                            }
                            if(null != accomplishmentsFragment.getActivity().findViewById(R.id.collegeAddressTxt)) {
                                educationalBackground3.setAddress(((EditText) accomplishmentsFragment.getActivity().findViewById(R.id.collegeAddressTxt)).getText().toString());
                            }
                            educationalBackground3.setType(2);

                            resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().add(educationalBackground3);

                        }

                        referenceFragment = (StudentReferenceFragment)getSupportFragmentManager().findFragmentById(R.id.referencesfragment);

                        if(null != referenceFragment && referenceFragment.isInLayout() && null != referenceFragment.getActivity()){

                            //resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().clear();
                            ReferencesInfo referencesInfo1 = new ReferencesInfo();
                            if(null != resumeInfo.getAccomplishmentsInterestInfo() && null != resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos() && resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().size() > 0){
                                referencesInfo1 = resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().get(0);
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refName1Txt)){
                                referencesInfo1.setName(((EditText)referenceFragment.getActivity().findViewById(R.id.refName1Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refAddress1Txt)){
                                referencesInfo1.setAddress(((EditText)referenceFragment.getActivity().findViewById(R.id.refAddress1Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refPhone1Txt)){
                                referencesInfo1.setPhoneNumber(((EditText)referenceFragment.getActivity().findViewById(R.id.refPhone1Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refOccupation1Txt)){
                                referencesInfo1.setOccupation(((EditText)referenceFragment.getActivity().findViewById(R.id.refOccupation1Txt)).getText().toString());
                            }

                            resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().add(referencesInfo1);

                            ReferencesInfo referencesInfo2 = new ReferencesInfo();
                                if(null != resumeInfo.getAccomplishmentsInterestInfo() && null != resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos() && resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().size() > 1) {
                                referencesInfo2 = resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().get(1);
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refName2Txt)){
                                referencesInfo2.setName(((EditText)referenceFragment.getActivity().findViewById(R.id.refName2Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refAddress2Txt)){
                                referencesInfo2.setAddress(((EditText)referenceFragment.getActivity().findViewById(R.id.refAddress2Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refPhone2Txt)){
                                referencesInfo2.setPhoneNumber(((EditText)referenceFragment.getActivity().findViewById(R.id.refPhone2Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refOccupation2Txt)){
                                referencesInfo2.setOccupation(((EditText)referenceFragment.getActivity().findViewById(R.id.refOccupation2Txt)).getText().toString());
                            }

                            resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().add(referencesInfo2);


                            ReferencesInfo referencesInfo3 = new ReferencesInfo();
                            if(null != resumeInfo.getAccomplishmentsInterestInfo() && null != resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos() && resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().size() > 2) {
                                referencesInfo3 = resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().get(2);
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refName3Txt)){
                                referencesInfo3.setName(((EditText)referenceFragment.getActivity().findViewById(R.id.refName3Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refAddress3Txt)){
                                referencesInfo3.setAddress(((EditText)referenceFragment.getActivity().findViewById(R.id.refAddress3Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refPhone3Txt)){
                                referencesInfo3.setPhoneNumber(((EditText)referenceFragment.getActivity().findViewById(R.id.refPhone3Txt)).getText().toString());
                            }

                            if(null != referenceFragment.getActivity().findViewById(R.id.refOccupation3Txt)){
                                referencesInfo3.setOccupation(((EditText)referenceFragment.getActivity().findViewById(R.id.refOccupation3Txt)).getText().toString());
                            }

                            resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().add(referencesInfo3);



                        }


                        CreateUpdateResumeActivity.CreateResume createResume = new CreateUpdateResumeActivity.CreateResume();
                        createResume.execute();


                    }
                }
        );


    }

    @Override
    public StudentPersonalInformationInfo getStudentPersonalInfo(String name, String address, String phoneNumber, String email, String gender) {
        StudentPersonalInformationInfo personalInformationInfo = new StudentPersonalInformationInfo();

        personalInformationFragment =  (StudentPersonalInformationFragment)getSupportFragmentManager().findFragmentById(R.id.personalInfofragment);

        return personalInformationInfo;
    }

    @Override
    public AccomplishmentsInterestInfo getAccomplishmentsInterestInfo() {
        AccomplishmentsInterestInfo accomplishmentsInterestInfo = new AccomplishmentsInterestInfo();

        accomplishmentsFragment = (StudentAccomplishmentsFragment)getSupportFragmentManager().findFragmentById(R.id.accomplishmentsfragment);

        return accomplishmentsInterestInfo;
    }


    @Override
    public ReferencesInfo getReferencesInfo() {
        ReferencesInfo referencesInfo = new ReferencesInfo();

        referenceFragment = (StudentReferenceFragment)getSupportFragmentManager().findFragmentById(R.id.referencesfragment);

        return referencesInfo;
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    class RetrieveResume extends AsyncTask<String, String, String> {
        /**
         * Before starting background_light thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateUpdateResumeActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();

            // Building Parameters
            params.add(new BasicNameValuePair("agentId", agentId+""));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveResumeInfo.php",
                    "POST", params);

            // check for success tag
            if(null != json) {
                // check log cat fro response
                Log.d("Create Response", json.toString());
                try {
                    int success = json.getInt("success");

                    if(success == 1){
                        if(null == resumeInfo){
                            resumeInfo = new ResumeInfo();
                        }

                        if(json.has("resume_details_id") && null != json.get("resume_details_id")){
                            resumeInfo.setId(Integer.parseInt(json.get("resume_details_id").toString()));
                        }

                        if(null == resumeInfo.getStudentPersonalInformationInfo()) {
                            resumeInfo.setStudentPersonalInformationInfo(new StudentPersonalInformationInfo());
                        }

                        resumeInfo.getStudentPersonalInformationInfo().setName(null != json.get("name") ? json.get("name").toString():"");
                        resumeInfo.getStudentPersonalInformationInfo().setAddress(null != json.get("address") ? json.get("address").toString():"");
                        resumeInfo.getStudentPersonalInformationInfo().setPhoneNumber(null != json.get("phonenumber") ? json.get("phonenumber").toString():"");
                        resumeInfo.getStudentPersonalInformationInfo().setEmail(null != json.get("email") ? json.get("email").toString():"");
                        resumeInfo.getStudentPersonalInformationInfo().setGender(null != json.get("gender") ? json.get("gender").toString():"");


                        JSONArray workExperienceJsonArr = json.has("work_experience") ? json.getJSONArray("work_experience") : null;

                        if(null != workExperienceJsonArr){
                            for(int ctr = 0;  ctr < workExperienceJsonArr.length() ; ctr++){

                                final WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();

                                for(int i = 0 ; i <= workExperienceJsonArr.getJSONArray(ctr).length()-1 ; i++) {

                                    String[] row = null;
                                    if(null != workExperienceJsonArr.getJSONArray(ctr) && (workExperienceJsonArr.getJSONArray(ctr).get(i) + "").contains("~")) {
                                        row = (workExperienceJsonArr.getJSONArray(ctr).get(i) + "").split("~");
                                        String key = "";
                                        String value = "";
                                        key = row[0];
                                        if(row.length > 1) {
                                            value = row[1];
                                        }

                                        if(key.equals("work_experience_id")){
                                            workExperienceInfo.setId(Integer.parseInt(value));
                                        }

                                        if(key.equals("company_name")){
                                            workExperienceInfo.setCompanyName(value);
                                        }

                                        if(key.equals("address")){
                                            workExperienceInfo.setAddress(value);
                                        }

                                        if(key.equals("job_description")){
                                            workExperienceInfo.setJobDescription(value);
                                        }

                                        if(key.equals("duties_responsibilities")){
                                            workExperienceInfo.setDutiesResponsibilities(value);
                                        }

                                    }

                                }

                                resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().add(workExperienceInfo);
                            }
                        }

                        if(null == resumeInfo.getAccomplishmentsInterestInfo()) {
                            resumeInfo.setAccomplishmentsInterestInfo(new AccomplishmentsInterestInfo());
                        }

                        JSONArray educationalBackgroundJsonArr = json.has("educational_background") ? json.getJSONArray("educational_background"):null;

                        if(null != educationalBackgroundJsonArr){

                            for(int ctr = 0;  ctr < educationalBackgroundJsonArr.length() ; ctr++){
                                final EducationalBackgroundInfo educationalBackgroundInfo = new EducationalBackgroundInfo();

                                for(int i = 0 ; i <= educationalBackgroundJsonArr.getJSONArray(ctr).length()-1 ; i++) {
                                    String[] row = null;
                                    if(null != educationalBackgroundJsonArr.getJSONArray(ctr) && (educationalBackgroundJsonArr.getJSONArray(ctr).get(i) + "").contains("~")) {
                                        row = (educationalBackgroundJsonArr.getJSONArray(ctr).get(i) + "").split("~");
                                        String key = "";
                                        String value = "";
                                        key = row[0];
                                        if(row.length > 1) {
                                            value = row[1];
                                        }


                                        if(key.equals("name")){
                                            educationalBackgroundInfo.setSchoolName(value);
                                        }

                                        if(key.equals("address")){
                                            educationalBackgroundInfo.setAddress(value);
                                        }


                                        if(key.equals("type")){
                                            educationalBackgroundInfo.setType(Integer.parseInt(value));
                                        }

                                    }
                                }

                                resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().add(educationalBackgroundInfo);

                            }
                        }


                        JSONArray referencesJsonArr = json.has("references") ? json.getJSONArray("references"):null;

                        if(null != referencesJsonArr){
                            for(int ctr = 0;  ctr < referencesJsonArr.length() ; ctr++){
                                final ReferencesInfo referencesInfo = new ReferencesInfo();
                                for(int i = 0 ; i <= referencesJsonArr.getJSONArray(ctr).length()-1 ; i++) {
                                    String[] row = null;
                                    if(null != referencesJsonArr.getJSONArray(ctr) && (referencesJsonArr.getJSONArray(ctr).get(i) + "").contains("~")) {
                                        row = (referencesJsonArr.getJSONArray(ctr).get(i) + "").split("~");
                                        String key = "";
                                        String value = "";
                                        key = row[0];
                                        if(row.length > 1) {
                                            value = row[1];
                                        }

                                        if(key.equals("references_id")){
                                            referencesInfo.setId(Integer.parseInt(value));
                                        }

                                        if(key.equals("name")){
                                            referencesInfo.setName(value);
                                        }

                                        if(key.equals("address")){
                                            referencesInfo.setAddress(value);
                                        }


                                        if(key.equals("phone_number")){
                                            referencesInfo.setPhoneNumber(value);
                                        }

                                        if(key.equals("occupation")){
                                            referencesInfo.setOccupation(value);
                                        }

                                    }

                                }
                                resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().add(referencesInfo);


                            }
                        }


                        resumeInfo.getAccomplishmentsInterestInfo().setAccomplishments(json.has("accomplishments") &&  null != json.get("accomplishments") ? json.get("accomplishments").toString() : "");
                        resumeInfo.getAccomplishmentsInterestInfo().setInterests(json.has("interests") && null != json.get("interests") ? json.get("interests").toString() : "");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        /**
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
           /* // dismiss the dialog once done*/
            if(null != resumeInfo) {
                if (null != resumeInfo.getStudentPersonalInformationInfo() && null != personalInformationFragment && personalInformationFragment.isInLayout() && null != personalInformationFragment.getActivity()) {
                    ((EditText) personalInformationFragment.getActivity().findViewById(R.id.nameTxt)).setText(null != resumeInfo.getStudentPersonalInformationInfo().getName() && !resumeInfo.getStudentPersonalInformationInfo().getName().equals("null") ? resumeInfo.getStudentPersonalInformationInfo().getName() : "");
                    ((EditText) personalInformationFragment.getActivity().findViewById(R.id.addressTxt)).setText(null != resumeInfo.getStudentPersonalInformationInfo().getAddress() && !resumeInfo.getStudentPersonalInformationInfo().getAddress().equals("null") ? resumeInfo.getStudentPersonalInformationInfo().getAddress() : "");
                    ((EditText) personalInformationFragment.getActivity().findViewById(R.id.phoneNumberTxt)).setText(null != resumeInfo.getStudentPersonalInformationInfo().getPhoneNumber() && !resumeInfo.getStudentPersonalInformationInfo().getPhoneNumber().equals("null") ? resumeInfo.getStudentPersonalInformationInfo().getPhoneNumber() : "");
                    ((EditText) personalInformationFragment.getActivity().findViewById(R.id.elemAddTxt)).setText(null != resumeInfo.getStudentPersonalInformationInfo().getEmail() && !resumeInfo.getStudentPersonalInformationInfo().getEmail().equals("null") ? resumeInfo.getStudentPersonalInformationInfo().getEmail() : "");


                    if (null != resumeInfo.getStudentPersonalInformationInfo() && null != resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos() && resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().size() > 0) {
                        int position = 0;
                        for (WorkExperienceInfo workExperienceInfo : resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos()) {
                            if (position == 0) {
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyName1Txt)).setText(workExperienceInfo.getCompanyName());
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyAddress1Txt)).setText(workExperienceInfo.getAddress());
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.jobDescription1Txt)).setText(workExperienceInfo.getJobDescription());
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.dutiesCompany1Txt)).setText(workExperienceInfo.getDutiesResponsibilities());

                            } else {
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyName2Txt)).setText(workExperienceInfo.getCompanyName());
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.companyAddress2Txt)).setText(workExperienceInfo.getAddress());
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.jobDescription2Txt)).setText(workExperienceInfo.getJobDescription());
                                ((EditText) personalInformationFragment.getActivity().findViewById(R.id.dutiesCompany2Txt)).setText(workExperienceInfo.getDutiesResponsibilities());
                            }

                            position++;
                        }
                    }
                 }


                if(null != resumeInfo.getAccomplishmentsInterestInfo() && null != accomplishmentsFragment && accomplishmentsFragment.isInLayout() && null != accomplishmentsFragment.getActivity()){
                    if(null != resumeInfo.getAccomplishmentsInterestInfo() && null != resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds() && resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().size() > 0){
                        for(EducationalBackgroundInfo educationalBackgroundInfo : resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds()){
                            if(educationalBackgroundInfo.getType() == 0) {
                                ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.elementaryTxt)).setText(educationalBackgroundInfo.getSchoolName());
                                ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.elemAddressTxt)).setText(educationalBackgroundInfo.getAddress());
                            }else if(educationalBackgroundInfo.getType() == 1){
                                ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.highSchoolTxt)).setText(educationalBackgroundInfo.getSchoolName());
                                ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.highSchoolAddress1Txt)).setText(educationalBackgroundInfo.getAddress());
                            }else{
                                ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.collegeTxt)).setText(educationalBackgroundInfo.getSchoolName());
                                ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.collegeAddressTxt)).setText(educationalBackgroundInfo.getAddress());
                            }
                        }
                    }


                    if(null != resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos() && resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().size() > 0){
                        int position = 0;
                        for (ReferencesInfo referencesInfo : resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos()){
                            if(position == 0){
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refName1Txt)).setText(referencesInfo.getName());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refAddress1Txt)).setText(referencesInfo.getAddress());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refPhone1Txt)).setText(referencesInfo.getPhoneNumber());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refOccupation1Txt)).setText(referencesInfo.getOccupation());
                            }else if(position == 1){
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refName2Txt)).setText(referencesInfo.getName());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refAddress2Txt)).setText(referencesInfo.getAddress());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refPhone2Txt)).setText(referencesInfo.getPhoneNumber());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refOccupation2Txt)).setText(referencesInfo.getOccupation());
                            }else{
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refName3Txt)).setText(referencesInfo.getName());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refAddress3Txt)).setText(referencesInfo.getAddress());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refPhone3Txt)).setText(referencesInfo.getPhoneNumber());
                                ((EditText)referenceFragment.getActivity().findViewById(R.id.refOccupation3Txt)).setText(referencesInfo.getOccupation());
                            }
                            position++;
                        }
                    }
                }

                 if(null != resumeInfo.getAccomplishmentsInterestInfo() && null != accomplishmentsFragment && accomplishmentsFragment.isInLayout() && null != accomplishmentsFragment.getActivity()){
                     ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.accomplishmentsTxt)).setText(resumeInfo.getAccomplishmentsInterestInfo().getAccomplishments());
                     ((EditText)accomplishmentsFragment.getActivity().findViewById(R.id.interestTxt)).setText(resumeInfo.getAccomplishmentsInterestInfo().getInterests());
                 }

            }

        }
    }

    class CreateResume extends AsyncTask<String, String, String> {
        /**
         * Before starting background_light thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateUpdateResumeActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();

            // Building Parameters
            params.add(new BasicNameValuePair("agent_id", agentId+""));
            if(null != resumeInfo){
                params.add(new BasicNameValuePair("resume_details_id",resumeInfo.getId()+""));
            }
            if(null !=resumeInfo.getStudentPersonalInformationInfo()){
                params.add(new BasicNameValuePair("name",resumeInfo.getStudentPersonalInformationInfo().getName()));
                params.add(new BasicNameValuePair("address",resumeInfo.getStudentPersonalInformationInfo().getAddress()));
                params.add(new BasicNameValuePair("phoneNumber",resumeInfo.getStudentPersonalInformationInfo().getPhoneNumber()));
                params.add(new BasicNameValuePair("email",resumeInfo.getStudentPersonalInformationInfo().getEmail()));
                params.add(new BasicNameValuePair("gender",resumeInfo.getStudentPersonalInformationInfo().getGender()));

                if(null !=resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos() &&resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos().size() > 0){
                    int position = 0;
                    for(final WorkExperienceInfo workExperienceInfo: resumeInfo.getStudentPersonalInformationInfo().getWorkExperienceInfos()){

                        position++;
                        params.add(new BasicNameValuePair("workExperienceId"+position, workExperienceInfo.getId()+""));
                        params.add(new BasicNameValuePair("companyName"+position, workExperienceInfo.getCompanyName()));
                        params.add(new BasicNameValuePair("companyAddress"+position, workExperienceInfo.getAddress()));
                        params.add(new BasicNameValuePair("jobDescription"+position, workExperienceInfo.getJobDescription()));
                        params.add(new BasicNameValuePair("dutiesResponsibilities"+position, workExperienceInfo.getDutiesResponsibilities()));

                    }
                }
            }


            if(null != resumeInfo.getAccomplishmentsInterestInfo()){
                params.add(new BasicNameValuePair("accomplishments",resumeInfo.getAccomplishmentsInterestInfo().getAccomplishments()));
                params.add(new BasicNameValuePair("interests",resumeInfo.getAccomplishmentsInterestInfo().getInterests()));

                if(null != resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds() && resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds().size() > 0){
                    for(EducationalBackgroundInfo educationalBackgroundInfo:resumeInfo.getAccomplishmentsInterestInfo().getEducationalBackgrounds()){
                        params.add(new BasicNameValuePair("type", educationalBackgroundInfo.getType() + ""));
                        if(educationalBackgroundInfo.getType() == 0) {
                            params.add(new BasicNameValuePair("elementary", educationalBackgroundInfo.getSchoolName()));
                            params.add(new BasicNameValuePair("elementaryAddress", educationalBackgroundInfo.getAddress()));
                        }else if(educationalBackgroundInfo.getType() == 1){
                            params.add(new BasicNameValuePair("highSchool", educationalBackgroundInfo.getSchoolName()));
                            params.add(new BasicNameValuePair("highSchoolAddress", educationalBackgroundInfo.getAddress()));
                        }else{
                            params.add(new BasicNameValuePair("college", educationalBackgroundInfo.getSchoolName()));
                            params.add(new BasicNameValuePair("collegeAddress", educationalBackgroundInfo.getAddress()));
                        }

                    }
                }

                if(null != resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos() && resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos().size() > 0){
                    int position = 0;
                    for(final ReferencesInfo referencesInfo : resumeInfo.getAccomplishmentsInterestInfo().getReferencesInfos()){
                        position++;
                        params.add(new BasicNameValuePair("referenceId"+position,referencesInfo.getId()+""));
                        params.add(new BasicNameValuePair("refName"+position,referencesInfo.getName()));
                        params.add(new BasicNameValuePair("refAddress"+position,referencesInfo.getAddress()));
                        params.add(new BasicNameValuePair("refPhone"+position,referencesInfo.getPhoneNumber()));
                        params.add(new BasicNameValuePair("refOccupation"+position,referencesInfo.getOccupation()));
                    }
                }
            }


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"createResume.php",
                    "POST", params);

            // check for success tag
            if(null != json) {
                // check log cat fro response
                Log.d("Create Response", json.toString());


                try {
                    int success = json.getInt("success");
                    if (success == 1) {

                        message = "Saved Successfully!";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        /**
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();


            CreateUpdateResumeActivity.RetrieveResume retrieveResume = new CreateUpdateResumeActivity.RetrieveResume();
            retrieveResume.execute();

            toastMessage(message);

           /* // dismiss the dialog once done*/
/*
            if(registrationSuccessful){

                toastMessage(registrationMessage);
                Intent int1 = new Intent(CreateStudentActivity.this, Login.class);
                startActivity(int1);
            }else{
                toastMessage(registrationMessage);
            }*/


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this,StudentLoginActivity.class);
        startActivity(home);
        finish();
    }
}
