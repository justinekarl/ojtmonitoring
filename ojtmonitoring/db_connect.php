<?php
 

class DB_CONNECT {
 
	function connect(){
		$host="localhost";
		$username="root";
		$password="1234";
		$db_name="ojtmonitoring";
		$link=mysqli_connect('localhost', 'root', '', 'ojtmonitoring') or die("unable to connect");
	}
	
    
    /* function __construct() {
        $this->connect();
    }
 
    function __destruct() {
        $this->close();
    }
 
    
    function connect() {
      
        require_once __DIR__ . '/db_config.php';
 
     
        $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
 
    
        $db = mysqli_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());
 
      
        return $con;
    }
 
   
    function close() {
        // closing db connection
        mysql_close();
    } */
 
}
 
?>
