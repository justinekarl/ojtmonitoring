<?php

require_once 'db_config.php';
$response = array();



if (isset($_POST['user_name']) && isset($_POST['password'])) {
    error_log("sign up -".$_POST['user_name']."-".$_POST['password']);
    $user_name = $_POST['user_name'];
    $password = $_POST['password'];
    $accounttype = intval($_POST['accounttype']);
    $full_name= $_POST['full_name'];
    $phonenumber = $_POST['phonenumber'];
    


    if($accounttype == 1){
        $student_number= $_POST['student_number'];
        $college = $_POST['college'];
        $address = $_POST['address'];
        $email = $_POST['email'];
    }

    if($accounttype == 2){
        $department = $_POST['department'];
        $teacherNumber = $_POST['phonenumber'];
        $college = $_POST['college'];
    }

    if($accounttype==3){
        $companyType = $_POST['companyType'];
        $address = $_POST['address'];
        //$teacherNumber = $_POST['phonenumber'];
    }


    
    $queryTo = "SELECT count(*) cnt FROM user where username='{$user_name}'";
    $result_checker = mysqli_query($link,$queryTo);
    $checker = (int) mysqli_fetch_assoc($result_checker)["cnt"];

    error_log("register.php checker_restult".print_r($checker,true));

    error_log("register.php query".print_r($queryTo,true));


    $insertedId = 0;

    error_log("accounttype".$accounttype);


    if($checker == 0){

        if($accounttype == 1) {
            $studentQry = "INSERT INTO user(username,password,studentnumber,name,college,address,phonenumber,accounttype,email) VALUES('$user_name', '$password','$student_number','$full_name','$college','$address','$phonenumber','$accounttype','$email')";
            $result=mysqli_query($link,
                $studentQry);

            error_log("studentQry".print_r($studentQry,true));



            $insertedId = mysqli_insert_id($link);
            error_log($insertedId);

             $response['returned_id'] = $insertedId;
             $response['message'] = "Successfully created Student Account";
             $response['success'] = 1;

        }


        if($accounttype == 2) {

            $teacherQry = "INSERT INTO user(username,password,teachernumber,name,department,phonenumber,accounttype,college) VALUES('$user_name', '$password','$teacherNumber','$full_name','$department','$phonenumber','$accounttype','$college')";
            $result=mysqli_query($link,$teacherQry);

            error_log("studentQry".print_r($teacherQry,true));
            $response['message'] = "Successfully created Teacher Account";
            $response['success'] = 1;

        }

        if($accounttype == 3){
        

            $companyQry = "INSERT INTO user(username,password,name,address,department,phonenumber,accounttype) VALUES('$user_name', '$password','$full_name','$address','$companyType','$phonenumber','$accounttype')";
            $result=mysqli_query($link,$companyQry
                );
            $id = mysqli_insert_id($link);
            error_log($id);


            $companyProfileQry = "INSERT INTO company_profile(user_id) 
                                  VALUES ('$id')
                                 ";

            $result=mysqli_query($link,$companyProfileQry);

            $response['message'] = "Successfully created Company Account";
            $response['success'] = 1;


        }


    }

    
    error_log("response".print_r($response, true));
    echo json_encode($response);



} 
?>