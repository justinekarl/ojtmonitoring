<?php

require_once 'db_config.php';

$response = array();

// check for required fields
error_log("retrieving user accounts");
if (isset($_POST['agentid'])) {
   
    $accounttype = intval($_POST['accountType']);
   
    $agentId = $_POST['agentid'];
    error_log("log in -".$agentId);

 
   
    error_log($accounttype);

    $query1 = "SELECT user.*,description,moa_certified FROM user  LEFT JOIN company_profile ON user.id = company_profile.user_id where user.id = ".$_POST['agentid'];

        error_log("QUERY TO!!");
        error_log($query1);
   


    $conn = new mysqli($host, $username, $password, $db_name,$port);
   

    $checker= 0;
    if ($result = $conn->query($query1)) {
        while ($row = $result->fetch_assoc()) {
            $checker = $checker+1;
            
                foreach($row  as $key => $value){
                    $response[$key] = $value;
                }
         }
        $result->free();
    }
    

    if($checker > 0){
        $response["success"] = 1;
        error_log(json_encode($response));
        echo json_encode($response);
    }else{
        $response["success"] = 0;
        $response["message"] = "User does not exists";
        error_log(json_encode($response));
        echo json_encode($response);
    }
    

    // check if row inserted or not

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "xx";

    // echoing JSON response
    error_log(json_encode($response));
    echo json_encode($response);
}
?>
