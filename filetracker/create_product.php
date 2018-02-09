<?php
 

require_once 'db_config.php';
$response = array();
 
error_log("borrow");
if (isset($_POST['borrower']) && isset($_POST['item']) && isset($_POST['agent_id'])) {
 
    $borrower = $_POST['borrower'];
    $item = $_POST['item'];
    $agent_id = $_POST['agent_id'];
 

    		$query = "select count(*) as checker from borrowed where (item LIKE '%".$item."%')";
    
    		$borrower_checker= mysqli_query($link,$query);
    		$checker = (int) mysqli_fetch_assoc($borrower_checker)["checker"];
    		
    		
    		/* if($checker > 0){
    			$conn->query("delete from borrowed where (item LIKE '%".$item."%')");
    			error_log($sql);
    			if($conn->query($sql) === TRUE) {
    				echo "delete successfully";
    			}
			} */
			

			$result_check_borrowed = mysqli_query($link,"select count(*) as borrowed_count from borrowed where (item LIKE '%".$item."%') and agent_id = ".$agent_id."");
			$borrowedCount = (int) mysqli_fetch_assoc($result_check_borrowed)["borrowed_count"];;
			
			
			//error_log("size of = ".print_r(mysqli_fetch_assoc($result_check_borrowed),true));
				
			if($borrowedCount== 0) {	
				$conn = new mysqli($host, $username, $password, $db_name);
				
				$sql = "delete from borrowed where (item LIKE '%".$item."%')";
				error_log($sql);
				if($conn->query($sql) === TRUE) {
					echo "delete successfully";
				}
				$conn->query($sql);
				
				
				$sql = "INSERT INTO borrowed(borrower,item,agent_id) VALUES('$borrower', '$item',$agent_id)";
				error_log($sql);
				if($conn->query($sql) === TRUE) {
					echo "New record borrowed created successfully";
				}
				
				$sql = "INSERT INTO transaction_logs(item,agent_id,borrowed) VALUES('$item','$agent_id',true)";
				error_log($sql);
				if($conn->query($sql) === TRUE) {
					echo "New record transaction_logs created successfully";
				}
				
				$conn->close();
				$response["success"] = 1;
				$response["message"] = "Product successfully created.";
				error_log(json_encode($response));
				echo json_encode($response);
			
			}else {
			$response["success"] = 0;
			$response["message"] = "Required field(s) is missing 1";
			error_log(json_encode($response));
			echo json_encode($response);
			}
	
	
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing 2";
    error_log(json_encode($response));
    echo json_encode($response);
}
?>